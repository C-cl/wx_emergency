package com.wxsoft.emergency.data.remote

import com.wxsoft.emergency.data.entity.Dictionary
import io.reactivex.Flowable
import retrofit2.http.GET

interface DictEnumApi {

    @GET("EnumDic/enumItems/1")
    fun loadDictEvaluations(): Flowable<List<Dictionary>>
}