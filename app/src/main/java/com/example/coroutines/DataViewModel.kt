package com.example.coroutines

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataViewModel : ViewModel() {

    fun getUsers() = liveData(Dispatchers.IO) {

        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = callSingleApi()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    suspend fun callSingleApi(): DataModel {
        println("COROUTINES called");
        lateinit var response: DataModel
        val request = Apifactory.retrofit().create(Api::class.java)
        response = request.apiCallOne();
        println("COROUTINES ended " + response.status);
        Log.e("rajesh",response.status)
        return response;
    }

}