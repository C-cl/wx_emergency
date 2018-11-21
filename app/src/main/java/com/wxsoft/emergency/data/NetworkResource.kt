package com.wxsoft.emergency.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.os.AsyncTask
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.wxsoft.emergency.result.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class NetworkResource<T> @MainThread
constructor() {

    private val result = MediatorLiveData<Resource<T>>()

    /**
     * 获取数据
     * @return
     */
    val asLiveData: LiveData<Resource<T>>
        get() = result

    init {

        fetchFromNetwork()
    }


    /**
     * 联网失败再整这个
     */
    @MainThread
    protected abstract fun onFetchFailed()

    /**
     * 保存数据
     * @param item
     */
    @WorkerThread
    protected abstract fun saveCallResult(item: T)

    /**
     * 创建Call
     * @return
     */
    @MainThread
    protected abstract fun createCall(): Call<T>


    /**
     *
     * @param response
     */
    @MainThread
    private fun saveResultAndReInit(response: T?) {
        object : AsyncTask<Void, Void, Void>() {

            override fun doInBackground(vararg voids: Void): Void? {
                saveCallResult(response!!)
                return null
            }

            override fun onPostExecute(aVoid: Void?) {
                result.setValue(Resource.Success(response!!))
            }
        }.execute()
    }

    /**
     *
     */
    private fun fetchFromNetwork() {
        createCall().enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    when (response.code()) {
                        200 ->

                            saveResultAndReInit(response.body())
                    }
                }

            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onFetchFailed()
                try {
                    result.postValue(Resource.Error(t))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }
}