package com.example.coroutines

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object Apifactory {

    //OkhttpClient for building http request url
    private val client = OkHttpClient().newBuilder()
        /*.addInterceptor(object :Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {

                val request: Request = chain.request()
                val response = chain.proceed(request)

                Log.e("rajesh",response.toString())
                Log.e("rajesh",request.toString())
                val newResponse = response.newBuilder()
                var ss="{status:intercept apicallone is successfull}"
                //newResponse.body(ResponseBody.create(MediaType.parse("application/json"), ss))
                //return newResponse.build()

                val cacheControl = CacheControl.Builder()
                    .maxAge(10, TimeUnit.DAYS)
                    .build()
                return response.newBuilder().header("Cache-Control", cacheControl.toString())
                    .build()

            }
        })*/
        /*.addInterceptor(object :Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {

                val request: Request = chain.request()
                val response = chain.proceed(request)
                Log.e("rajesh","ssss "+response.toString())
                Log.e("rajesh","sss "+request.toString())
                return response

            }
        })*/
        .build()



    fun retrofit() : Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://5f632625363f0000162d83a9.mockapi.io/api/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
}