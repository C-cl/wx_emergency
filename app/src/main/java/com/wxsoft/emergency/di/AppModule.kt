package com.wxsoft.emergency.di

import android.content.ClipboardManager
import android.content.Context
import android.net.wifi.WifiManager
import android.nfc.NfcManager
import com.baidu.mapapi.SDKInitializer
import com.wxsoft.emergency.App
import com.wxsoft.emergency.data.prefs.PreferenceStorage
import com.wxsoft.emergency.data.prefs.SharedPreferenceStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: App): Context {
        return application.applicationContext
    }

//    @Singleton
//    @Provides
//    fun providesDataBase(context: Context):AppDatabase{
//
//        return Room.databaseBuilder(context,AppDatabase::class.java, "db-emergency").build();
//    }

    @Singleton
    @Provides
    fun providesPreferenceStorage(context: Context): PreferenceStorage =
            SharedPreferenceStorage(context)

    @Provides
    fun providesWifiManager(context: Context): WifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Provides
    fun providesClipboardManager(context: Context): ClipboardManager =
            context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE)
                    as ClipboardManager
//    @Singleton
//    @Provides
//    fun provideNfcManager(context: Context):NfcManager =
//            context.applicationContext.getSystemService(Context.NFC_SERVICE) as NfcManager

}