package com.wxsoft.emergency.data.remote

import com.wxsoft.emergency.data.entity.Patient
import com.wxsoft.emergency.data.entity.Response
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.*

interface PatientApi{

    @GET("Patient/GetPatients")
    fun patients():Observable<List<Patient>>

    @GET("Patient/GetById/{id}")
    fun patient(@Path("id")id:String):Flowable<Patient>

    @GET("/api/Patient/GetByWristband/{rFid}")
    fun getPatientByRFID(@Path("rFid")rFid:String):Flowable<Patient>

    @GET("/api/Patient/SetPatientWristband/{patientId}/{wristband_Number}")
    fun bindRfid(@Path("patientId")patientId:String,@Path("wristband_Number")rFid:String):Flowable<Response<String>>

    @POST("patient/SavePatientInfo")
    fun save(@Body patient:Patient):Observable<String>
}