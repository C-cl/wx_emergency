package com.wxsoft.emergency.data

import com.wxsoft.emergency.result.Resource
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


abstract class NetworkBoundSingleResource<T>: NetworkBoundResource<T, T>()

abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result: Flowable<Resource<ResultType>>

    init {
        // Lazy disk observable.
        val diskObservable = Flowable.defer {
            loadFromDb()
                // Read from disk on Computation Scheduler
                .subscribeOn(Schedulers.computation())
        }

        // Lazy network observable.
        val networkObservable = Flowable.defer {
            createCall()
                // Request API on IO Scheduler
                .subscribeOn(Schedulers.io())
                // Read/Write to disk on Computation Scheduler
                .observeOn(Schedulers.computation())
                .doOnNext { request:RequestType ->
                    saveCallResult(processResponse(request))
                }
                .onErrorReturn { throwable: Throwable ->

                    throw  throwable
                }
                .flatMap { loadFromDb() }
        }

        result = when {
            shouldFetch() -> networkObservable
                .map<Resource<ResultType>> { Resource.Success(it) }
                .onErrorReturn { Resource.Error(it) }
                // Read results in Android Main Thread (UI)
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(Resource.Loading)
            else -> diskObservable
                .map<Resource<ResultType>> { Resource.Success(it) }
                .onErrorReturn { Resource.Error(it) }
                // Read results in Android Main Thread (UI)
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(Resource.Loading)
        }
    }

    fun asFlowable(): Flowable<Resource<ResultType>> {
        return result
    }

    private fun processResponse(response: RequestType): RequestType {
        return response!!
    }

    protected abstract fun saveCallResult(request: RequestType)

    protected abstract fun loadFromDb(): Flowable<ResultType>

    protected abstract fun createCall(): Flowable<RequestType>

    protected abstract fun shouldFetch(): Boolean
}