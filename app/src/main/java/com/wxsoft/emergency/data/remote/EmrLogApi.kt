package com.wxsoft.emergency.data.remote

import com.wxsoft.emergency.data.entity.Dictionary
import com.wxsoft.emergency.data.entity.EmrOperation
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EmrLogApi{
    /**
     * 保存log
     */
    @POST("OperationLog/")
    fun log(@Body operation: EmrOperation): Flowable<String>

    /**
     * 获取病人的操作日志
     * @param id 病人ID
     */
    @GET("OperationLog/Get/{id}")
    fun getEmrLog(@Path("id")id:String): Flowable<List<Dictionary>>

}