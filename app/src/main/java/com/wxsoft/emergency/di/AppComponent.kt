package com.wxsoft.emergency.di

import com.wxsoft.emergency.App
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [AndroidSupportInjectionModule::class
            , AppModule::class
            , ViewModelModule::class
            , ActivityBindingModule::class
            , NetWorkModule::class
            ,BaiduMapModule::class])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}