package com.wxsoft.emergency.data.remote

import com.wxsoft.emergency.data.entity.chest.Evaluation
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface EvaluationApi {

    @GET("Evaluation/GetEvaluationByPatientId/{id}")
    fun loadPatientEvaluations(@Path("id")id:String): Observable<List<Evaluation>>


    @POST("Evaluation/AddEvalation")
    fun addEvaluation(@Body evaluation: Evaluation): Observable<String>

}