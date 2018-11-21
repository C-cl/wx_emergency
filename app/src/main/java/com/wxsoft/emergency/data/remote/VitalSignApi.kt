package com.wxsoft.emergency.data.remote

import com.wxsoft.emergency.data.entity.chest.VitalSign
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.*

interface VitalSignApi{

    @POST("VitalSigns")
    fun insert(@Body vitalSign: VitalSign): Observable<String>

    @PUT("VitalSigns")
    fun update(@Body vitalSign: VitalSign): Observable<String>

    @GET("VitalSigns/{id}")
    fun patient(@Path("id")id:String): Flowable<List<VitalSign>>
}