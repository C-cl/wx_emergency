package com.wxsoft.emergency.ui.main.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel;
import com.wxsoft.emergency.data.entity.Patient
import com.wxsoft.emergency.data.remote.PatientApi
import com.wxsoft.emergency.result.Event
import com.wxsoft.emergency.result.Resource
import com.wxsoft.emergency.ui.emr.EventActions
import com.wxsoft.emergency.utils.map
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PatientsViewModel @Inject constructor(val  api: PatientApi): ViewModel(),EventActions {



    private val disposables = CompositeDisposable()
    val isLoading: LiveData<Boolean>
    val patients: LiveData<List<Patient>>
    private val _navigateToOperationAction = MutableLiveData<Event<String>>()
    private val loadPatientsResult= MediatorLiveData<Resource<List<Patient>>>()
    val navigateToOperationAction: LiveData<Event<String>>
        get() = _navigateToOperationAction

    init {

        load()
        isLoading=loadPatientsResult.map { it== Resource.Loading }

        patients = loadPatientsResult.map {
            (it as? Resource.Success)?.data ?: emptyList()
        }

    }

    fun onSwipeRefresh() {
        load()
    }

    fun load(){
        disposables.add(api.patients()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadPatientsResult.setValue(Resource.Loading) }
            .subscribe(
                { result -> loadPatientsResult.setValue(Resource.Success(result)) },
                { throwable -> loadPatientsResult.setValue(Resource.Error(throwable)) }
            ))
    }

    override fun onOpen(id: String) {
        _navigateToOperationAction.value= Event(id)
    }


}