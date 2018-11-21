package com.wxsoft.emergency.data.entity.chest

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.emergency.BR


class Evaluation(val id:String,val name:String,val code:String): BaseObservable() {


    /**
     * 来源
     */
    @Bindable
    var patientId:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.patientId)
        }

 }