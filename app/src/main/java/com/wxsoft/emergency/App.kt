package com.wxsoft.emergency

import cn.jpush.android.api.JPushInterface
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.wxsoft.emergency.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class App:DaggerApplication() {


    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            JPushInterface.setDebugMode(true);
        }

        SDKInitializer.initialize(this)
        SDKInitializer.setCoordType(CoordType.BD09LL);


//         	推送相关
        JPushInterface.init(this);
    }


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}