package com.wxsoft.emergency.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.wxsoft.emergency.BR

data class EmrItem(val id:String ): BaseObservable() {


    @Bindable
    var userCase:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.userCase)
        }
    @Bindable
    var name:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.name)
        }
    @Bindable
    var checked:Boolean=false
        set(value) {

            field=value
            notifyPropertyChanged(BR.checked)
        }
    @Bindable
    var create_Time:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.create_Time)
        }

    @Bindable
    var creator_Name:String=""
        set(value) {

            field=value
            notifyPropertyChanged(BR.creator_Name)
        }



}