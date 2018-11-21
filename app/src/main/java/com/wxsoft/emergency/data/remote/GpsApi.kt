package com.wxsoft.emergency.data.remote

import com.wxsoft.emergency.data.entity.GpsLocation
import com.wxsoft.emergency.data.entity.Patient
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface GpsApi {

    @POST("Gis/Save")
    fun save(@Body location: GpsLocation): Observable<String>
}