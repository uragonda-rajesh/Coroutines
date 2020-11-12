package com.example.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.lang.RuntimeException

class ManActivity2ErrorHandling : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_man_activity2_error_handling)

        //using superviserscope https://github.com/mitchtabian/Kotlin-Coroutine-Examples/tree/supervisor-job
        useUupervisorJScope()
        //exceptionHandling()
    }

    fun useUupervisorJob(){

        val scope = CoroutineScope( Dispatchers.IO)
        scope.launch {
            repeat(2) { i ->
                try {
                    val result = async(SupervisorJob()) { doWork(i) }.await()
                    println("work result: ${result.toString()}")
                } catch (t: Throwable) {
                    println("caught exception: $t")
                }
            }
        }
    }
    fun useUupervisorJScope(){

        val scope = CoroutineScope( Dispatchers.IO)
        scope.launch {
            repeat(3) { i ->
                try {
                    supervisorScope {
                        val result = async() { doWork(i) }.await()
                        println("work result: ${result.toString()}")
                    }
                } catch (t: Throwable) {
                    println("caught exception: $t")
                }
            }
        }
    }

    private suspend fun doWork(iteration: Int): Int {
        delay(100)
        println("working on $iteration")
        return if (iteration == 0) throw RuntimeException("work failed") else 0
    }
    fun exceptionHandling(){
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Exception thrown in one of the children: $exception")
        }
        val parentJob = CoroutineScope(IO).launch(handler) {

            // --------- JOB A ---------
            val jobA = launch {
                val resultA = getResult(1)
                println("resultA: ${resultA}")
            }
            jobA.invokeOnCompletion { throwable ->
                if(throwable != null){
                    println("Error getting resultA: ${throwable}")
                }
            }

            // --------- JOB B ---------
            val jobB = launch {
                val resultB = getResult(2)
                println("resultB: ${resultB}")
            }
            jobB.invokeOnCompletion { throwable ->
                if(throwable != null){
                    println("Error getting resultB: ${throwable}")
                }
            }

            // --------- JOB C ---------
            val jobC = launch {
                val resultC = getResult(3)
                println("resultC: ${resultC}")
            }
            jobC.invokeOnCompletion { throwable ->
                if(throwable != null){
                    println("Error getting resultC: ${throwable}")
                }
            }
        }
        parentJob.invokeOnCompletion { throwable ->
            if(throwable != null){
                println("Parent job failed: ${throwable}")
            }
            else{
                println("Parent job SUCCESS")
            }
        }
    }

    suspend fun getResult(number: Int): Int{
        return withContext(Main){
            delay(number*500L)
            if(number == 2){
               /* try {
                    var i=1/0
                }
                catch (e:Exception){
                    println("Exception caused arithematic")
                }
                */
//                cancel(CancellationException("Error getting result for number: ${number}"))
                throw CancellationException("Error getting result for number: ${number}") // treated like "cancel()"
                //throw Exception("Error getting result for number: ${number}")
            }
            number*2
        }
    }


    private fun println(message: String){
        Log.d("RRR", message)
    }
}