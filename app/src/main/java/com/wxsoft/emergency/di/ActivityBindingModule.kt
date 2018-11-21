package com.wxsoft.emergency.di

import com.wxsoft.emergency.ui.detail.PatientDetailActivity
import com.wxsoft.emergency.ui.detail.PatientDetailModule
import com.wxsoft.emergency.ui.detail.PatientDetailViewModel
import com.wxsoft.emergency.ui.emr.EmrActivity
import com.wxsoft.emergency.ui.emr.EmrModule
import com.wxsoft.warriorsflyshare.di.ActivityScoped
import com.wxsoft.emergency.ui.home.HomeActivity
import com.wxsoft.emergency.ui.home.HomeModule
import com.wxsoft.emergency.ui.login.LoginActivity
import com.wxsoft.emergency.ui.login.LoginModule
import com.wxsoft.emergency.ui.main.MainActivity
import com.wxsoft.emergency.ui.main.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module
 * ActivityBindingModule is on, in our case that will be [AppComponent]. You never
 * need to tell [AppComponent] that it is going to have getAll these subcomponents
 * nor do you need to tell these subcomponents that [AppComponent] exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the
 * specified modules and be aware of a scope annotation [@ActivityScoped].
 * When Dagger.Android annotation processor runs it will create 2 subcomponents for us.
 */
@Module
abstract class ActivityBindingModule {


    @ActivityScoped
    @ContributesAndroidInjector(modules = [HomeModule::class])
    internal abstract fun homeActivity(): HomeActivity



    @ActivityScoped
    @ContributesAndroidInjector(modules = [EmrModule::class])
    internal abstract fun emrActivity(): EmrActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun mainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [PatientDetailModule::class])
    internal abstract fun patientDetailActivity(): PatientDetailActivity


    @ActivityScoped
    @ContributesAndroidInjector(modules = [LoginModule::class])
    internal abstract fun loginActivity(): LoginActivity
}