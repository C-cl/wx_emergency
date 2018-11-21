package com.wxsoft.emergency.data.remote

import com.wxsoft.emergency.data.entity.Account
import com.wxsoft.emergency.data.entity.LoginInfo
import com.wxsoft.emergency.data.entity.Response
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApi {


    @POST("Security")
    fun login(@Body info: LoginInfo): Flowable<Response<Account>>
}