package com.wxsoft.emergency.ui.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.google.gson.Gson
import com.wxsoft.emergency.data.entity.Account
import com.wxsoft.emergency.data.entity.LoginInfo
import com.wxsoft.emergency.data.prefs.SharedPreferenceStorage
import com.wxsoft.emergency.data.remote.AccountApi
import com.wxsoft.emergency.exception.ServerException
import com.wxsoft.emergency.result.Resource
import com.wxsoft.emergency.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val sharedPreferenceStorage: SharedPreferenceStorage,
                                         private val accountApi: AccountApi,
                                         private val gson: Gson):ViewModel() {


    var jpushAccountId:String=""
        set(value) {
            field=value


            if(loginedBefore){
                login()
            }
        }

    var name:String = ""
    var password:String = ""

    private var loginedBefore=false

    /**
     *获取病人信息
     */
    private val loadAccountResult = MediatorLiveData<Resource<Account>>()
    /**
     * 是否正在夹在数据
     */
    val isLoading: LiveData<Boolean>

    val account:LiveData<Account?>
    val logined:LiveData<Boolean>

    init {
        attemptLogin()
        account=loadAccountResult.map {
            var a=(it as? Resource.Success)?.data

            if(a==null){

            }else{
                sharedPreferenceStorage.loginedName=name
                sharedPreferenceStorage.loginedPassword=password
                sharedPreferenceStorage.userInfo=gson.toJson(a)
            }
            return@map a
        }
        logined=loadAccountResult.map {

            var a= it is Resource.Success
            if(a) {
                a = (it as Resource.Success)?.data!=null
            }
            return@map a

        }
        isLoading = loadAccountResult.map { it == Resource.Loading }
    }

    fun attemptLogin(){

        name=sharedPreferenceStorage.loginedName?:""
        password=sharedPreferenceStorage.loginedPassword?:""

        if(isNameValid(name) && isPasswordValid(password))
            loginedBefore=true
    }

    fun login(){

        if(isNameValid(name)&& isPasswordValid(password)) {
            val loginInfo = LoginInfo(name, password, jpushAccountId)


            accountApi.login(loginInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map<Resource<Account>> {
                    if (it.success) {
                        if (it.result != null) {
                            Resource.Success(it.result!!)
                        } else {
                            Resource.Error(ServerException(it.msg))
                        }
                    } else
                        Resource.Error(ServerException(it.msg))
                }
                .onErrorReturn { Resource.Error(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(Resource.Loading)
                .subscribe { result ->
                    loadAccountResult.value = result
                }
        }
    }


    private fun isNameValid(email: String): Boolean {
        return email.isNotEmpty()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty()
    }
}