package com.example.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO


class MainActivity : AppCompatActivity() {

    //https://stackoverflow.com/questions/33462720/adb-unable-to-connect-to-192-168-1-105555
    lateinit var tv: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv = findViewById(R.id.tv)

        println("COROUTINES main " + Thread.currentThread().id)

        //call a single api
        //callApiUsingCoroutines()

        //call multiple api's sequentially and finally show the result.
        //callMultipleApisequentially()

        //call multiple api's and wait for the results to update the UI
        //callMultipleApiParallel()

        //call api suing lifecyclescope
        //callApiUsingLifeCycleScope()

        //call api in view model using live data
        val viewmodel: DataViewModel by viewModels()
        viewmodel.getUsers().observe(this,Observer{
            val s=it as Resource<DataViewModel>;
            when(s.status)
            {
                Status.SUCCESS->{
                    tv.text=(s.data as DataModel).status+" view model"
                }

            }
        })


        println("COROUTINES oncreate end")
    }
    private suspend fun getResult1FromApi(): String {
        delay(1000)
        return "Result #1"
    }

    fun callApiUsingLifeCycleScope(){
        lifecycleScope.launch(IO){
            println("COROUTINES CoroutineScope " + Thread.currentThread().id)
            var dataModel: DataModel = callSingleApi()
            withContext(Dispatchers.Main)
            {
                tv.text = dataModel.status
                println("COROUTINES ui updated")
            }
            println("COROUTINES end ")
        }
    }


    fun callMultipleApiParallel()
    {
        println("COROUTINES CoroutineScope " + Thread.currentThread().id)

        val request = Apifactory.retrofit().create(Api::class.java)


        CoroutineScope(IO).launch {
            val result1: Deferred<String> = async {
                /*
                 * call api 1
                 * */
                println("COROUTINES result1 ")
                request.apiCallOne().status;

            }
            var result2: Deferred<String> =async {
                /*
                 * call api 2
                 * */
                println("COROUTINES result2 ")
               request.apiCallTwo().status;

            }
            var result3: Deferred<String> = async {
                /*
                 * call api 3
                 * */
                println("COROUTINES result3 ")
                request.apiCallThree().status;

            }

            withContext(Dispatchers.Main)
            {
                tv.text = ""+result1.await()+"\n"+result2.await()+"\n"+result3.await()
                println("COROUTINES ui updated")
            }
        }
    }

    fun callMultipleApisequentially(){
        CoroutineScope(IO).launch {
            println("COROUTINES CoroutineScope " + Thread.currentThread().id)
            var result=""
            val request = Apifactory.retrofit().create(Api::class.java)

            /*
            * call api 1
            * */
            result=result+"\n"+request.apiCallOne().status;
            println("COROUTINES result "+result)
            /*
            * call api 2
            * */
            result=result+"\n"+request.apiCallTwo().status;
            println("COROUTINES result "+result)
            /*
            * call api 3
            * */
            result=result+"\n"+request.apiCallThree().status;
            println("COROUTINES result "+result)

            withContext(Dispatchers.Main)
            {
                tv.text = result
                println("COROUTINES ui updated")
            }
            println("COROUTINES end ")
        }

    }

    fun callApiUsingCoroutines() {
        CoroutineScope(IO).launch {
            println("COROUTINES CoroutineScope " + Thread.currentThread().id)
            var dataModel: DataModel = callSingleApi()
            withContext(Dispatchers.Main)
            {
                tv.text = dataModel.status
                println("COROUTINES ui updated")
            }
            println("COROUTINES end ")
        }

    }

    suspend fun callSingleApi(): DataModel {
        println("COROUTINES called");
        lateinit var response: DataModel
        val request = Apifactory.retrofit().create(Api::class.java)
        response = request.apiCallOne();
        println("COROUTINES ended " + response.status);
        return response;
    }
}