package com.wxsoft.emergency.di

import com.google.gson.Gson
import com.wxsoft.emergency.BuildConfig
import com.wxsoft.emergency.data.remote.*
import com.wxsoft.emergency.data.remote.log.LogInterceptor
import com.wxsoft.emergency.data.remote.log.Logger
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Module
class NetWorkModule {

    @Provides
    @Singleton
    fun provideGson():Gson{
        return Gson()
    }
    /**
     * 未登陆时候使用
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val builder= Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_ENDPOINT)


        //测试模式下打log
        if(BuildConfig.DEBUG){

            val logging = LogInterceptor(Logger())
            logging.setLevel(LogInterceptor.Level.BODY)

            val client= OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(logging)
                    .build()
            builder.client(client)
        }

        return builder.build()
    }

    @Provides
    fun providePatientApi(retrofit: Retrofit):PatientApi{

        return retrofit.create(PatientApi::class.java)
    }

    @Provides
    fun provideEvaluationApi(retrofit: Retrofit):EvaluationApi{

        return retrofit.create(EvaluationApi::class.java)
    }
    @Provides
    fun provideDictEnumApi(retrofit: Retrofit):DictEnumApi{

        return retrofit.create(DictEnumApi::class.java)
    }

    @Provides
    fun provideOperationMenuApi(retrofit: Retrofit):OperationMenuApi{

        return retrofit.create(OperationMenuApi::class.java)
    }

    @Provides
    fun provideEmrLogApi(retrofit: Retrofit):EmrLogApi{

        return retrofit.create(EmrLogApi::class.java)
    }

    @Provides
    fun provideVitalSignApi(retrofit: Retrofit):VitalSignApi{

        return retrofit.create(VitalSignApi::class.java)
    }

    @Provides
    fun provideAccountApi(retrofit: Retrofit):AccountApi{

        return retrofit.create(AccountApi::class.java)
    }

    @Provides
    fun provideGpsApi(retrofit: Retrofit):GpsApi{

        return retrofit.create(GpsApi::class.java)
    }

}