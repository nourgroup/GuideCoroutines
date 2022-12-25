package com.ngplus.coroutinesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis


class MainActivity : ComponentActivity() {

    private val dataViewModel : DataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataViewModel.startCounter()
        setContent {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ){
                Greeting("Loading ... ")
                CircularProgressIndicator()
            }
            dataViewModel.myData.observe(this){

            }
        }
        /**
         * block UI, two functions in concurrency
         * */
        /*runBlocking {
            launch{ charLoad() }
            launch{ numLoad() }
        }*/
        /**
         * block UI, two functions in not concurrency
         * */
        /*runBlocking {
            launch{
                charLoad()
                numLoad()
            }
        }*/
        /**
         * doesn't block UI, two functions not in concurrency
         * */
        /*GlobalScope.launch {
            launch{
                charLoad()
                numLoad()
            }
        }*/
        /**
         * doesn't block UI, two functions in concurrency
         * */
        /*GlobalScope.launch {
            launch{
                charLoad()
            }
            launch{
                numLoad()
            }
        }*/
        /**
         * doesn't block UI, two functions not in concurrency
         * but why?
         * withContext is suspend function not coroutine manager
         * */
        /*GlobalScope.launch {
            withContext(Dispatchers.IO){
                charLoad()
            }
            withContext(Dispatchers.IO){
                numLoad()
            }
        }*/
        /**
         * Async() & Await() : doesn't block UI and is not in concurrency
         * */
        /*GlobalScope.launch {
            val time = measureTimeMillis {
                val db = async{getDataFromDatabase()}
                val api = async{getDataFromAPI()}
                if(db.await()==api.await())
                    Log.i("coroutine_tutorial","${Thread.currentThread().name} are the same")
                else
                    Log.i("coroutine_tutorial","${Thread.currentThread().name} are not the same")
            }
            Log.i("coroutine_tutorial","${Thread.currentThread().name} $time")
        }*/
        /**
         * Management of jobs
         */
        /*
        val parentJob = Job()
        var resultJob1 = ""
        var resultJob2 = ""
        val job = GlobalScope.launch(parentJob) {
            val subJob1 = launch { resultJob1 = getDataFromDatabase() }
            val subJob2 = launch { resultJob2 = getDataFromAPI() }
            //subJob1.join()
            //subJob2.join()
            joinAll(subJob1,subJob2)
            if(resultJob1==resultJob2){
                Log.i("coroutine_tutorial","are the same : $resultJob1 $resultJob2")
            }else{
                Log.i("coroutine_tutorial","aren't the same : $resultJob1 $resultJob2")
            }
        }
        //parentJob.cancel()
        //job.cancel()
        */
        /**
         * channel : Channel send char every 5 second, receiver wait for that
         */
        /*val myChannel = Channel<String>()
        val myList = listOf("A","B","C","D","E")

        GlobalScope.launch {
            launch{
                myList.forEach {
                    myChannel.send(it)
                    delay(5000)
                }
            }
            launch{
                for(item in myChannel){
                    Log.i("coroutine_tutorial",item)
                }
            }
        }*/
        /**
         * channel : Channel send char, receiver has 5 seconds work,
         * buffer wait for that
         */
        /*val myChannel1 = Channel<String>()
        val myList1 = listOf("A","B","C","D","E")
        GlobalScope.launch {
            launch{
                myList1.forEach {
                    myChannel1.send(it)
                }
            }
            launch{
                for(item in myChannel1){
                    Log.i("coroutine_tutorial",item)
                    delay(5000)
                }
            }
        }*/
        /**
         * channel : Channel offer
         */
        /*val myChannel2 = Channel<String>(5)
        val myList2 = listOf("A","B","C","D","E")
        GlobalScope.launch {
            launch{
                myList2.forEach {
                    myChannel2.send(it)
                }
            }
            launch{
                for(item in myChannel2){
                    Log.i("coroutine_tutorial",item)
                }
            }
        }*/
        /**
         * Flow
         */
        /*val dataFlow = flow<Int> {
            for(i in 0..10){
                emit(i)
                delay(1000)
            }
        }.filter { it.mod(2)==0 }

        GlobalScope.launch(Dispatchers.IO) {
            dataFlow.collect{
                Log.i("flow_tutorial", "$it send")
            }
        }*/
        /**
         *
         */
        lifecycleScope.launch {
            dataViewModel
                .getData()
                .flowWithLifecycle(lifecycle,Lifecycle.State.STARTED)
                .collect{
                Log.i("Flow_tutorial","$it received")
            }
        }
        Log.i("Flow_tutorial","onCreated !!")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = " $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Greeting("Android")
}

suspend fun charLoad(){
    val myList = listOf("A","B","B","B","B","C","C","D","D","D","D","E")

    myList.forEach {
        Log.i("coroutine_tutorial","${Thread.currentThread().name} $it")
        delay(1000)
    }
}
suspend fun numLoad(){
    val myList = listOf(1,2,2,3,4,4,5,5)
    myList.forEach {
        Log.i("coroutine_tutorial","${Thread.currentThread().name} $it")
        delay(1000)
    }
}

suspend fun getDataFromAPI() : String{
    delay(500)
    Log.i("coroutine_tutorial","${Thread.currentThread().name} Data From API")
    return "data"
}

suspend fun getDataFromDatabase() : String{
    delay(3000)
    Log.i("coroutine_tutorial","${Thread.currentThread().name} Data From DB")
    return "data"
}