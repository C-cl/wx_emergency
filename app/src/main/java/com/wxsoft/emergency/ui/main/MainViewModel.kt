package com.wxsoft.emergency.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.wxsoft.emergency.data.entity.Patient
import com.wxsoft.emergency.data.remote.PatientApi
import com.wxsoft.emergency.result.Event
import com.wxsoft.emergency.utils.map
import javax.inject.Inject
import com.wxsoft.emergency.result.Resource
import com.wxsoft.emergency.ui.emr.EventActions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainViewModel @Inject constructor(val  api:PatientApi): ViewModel() ,EventActions{


    private val _navigateToOperationAction = MutableLiveData<Event<String>>()
    val patient: LiveData<Patient>

    private val loadPatientResult= MediatorLiveData<Resource<Patient>>()

    init {

        patient = loadPatientResult.map {
            (it as? Resource.Success)?.data ?: Patient("")
        }

    }

    override fun onOpen(id: String) {
        _navigateToOperationAction.value=Event(id)
    }

    fun loadByRfid(rFid:String){
        api.getPatientByRFID(rFid)

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<Resource<Patient>> {
                Resource.Success(it)
            }
            .onErrorReturn { Resource.Error(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(Resource.Loading)
            .subscribe { result ->
                loadPatientResult.value = result
            }

    }

}