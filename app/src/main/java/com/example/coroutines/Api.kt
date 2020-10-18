package com.example.coroutines

import retrofit2.http.GET

interface Api
{

    @GET("apicallone")
   suspend fun apiCallOne(): DataModel

    @GET("apicalltwo")
    suspend fun apiCallTwo(): DataModel

    @GET("apicallthree")
    suspend fun apiCallThree(): DataModel

}