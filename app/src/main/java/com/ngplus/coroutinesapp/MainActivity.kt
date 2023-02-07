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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import kotlin.system.measureTimeMillis


class MainActivity : ComponentActivity() {

    private val dataViewModel : DataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //val dataViewModel = viewModel<DataViewModel>()
            dataViewModel.startCounter()
            val text = dataViewModel.stateFlow.collectAsState(initial = "")

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ){
                Greeting("${text.value}")
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

        /*lifecycleScope.launch {
            dataViewModel
                .stateFlow
                .flowWithLifecycle(lifecycle,Lifecycle.State.STARTED)
                .collect{
                    Log.i("Flow_tutorial","$it received")
                }
        }*/
        /*dataViewModel.getData()
        lifecycleScope.launch{
            dataViewModel
                .sharedStateFlow
                .flowWithLifecycle(lifecycle,Lifecycle.State.STARTED)
                .collect{
                    Log.i("Flow_tutorial","$it received sharedFlow")
                }
        }*/

        /*lifecycleScope.launchWhenStarted{
            dataViewModel
                .getDataWithFlow()
                .flowWithLifecycle(lifecycle,Lifecycle.State.STARTED)
                .collect{
                Log.i("Flow_tutorial","$it received")
            }
        }
        Log.i("Flow_tutorial","onCreated !!")*/

        /****
         * udemy course
         * ***/

        /*runBlocking {
            val scope = CoroutineScope(Dispatchers.Default)
            val job = scope.launch {
                delay(100)
                Log.d("udemy_tuto","coroutine completed (Activity)")
            }
            job.invokeOnCompletion {
                    th ->
                if(th is CancellationException){
                    Log.d("udemy_tuto","Coroutine was cancelled (Activity)")
                }
            }
            //in this cas coroutine complete job and no exception is raised
            //delay(500)
            // the exception type cancellation wa raised
            delay(50)
            scope.cancel()
        }*/
        /*val scopeJob = Job()
        val scope = CoroutineScope(Dispatchers.Default + scopeJob)
        var childCoroutineJob : Job? = null

        val coroutineJob = scope.launch {
            childCoroutineJob = launch {
                Log.d("udemy_tuto","Starting child coroutine")
                delay(1000)
            }
            Log.d("udemy_tuto","Starting coroutine")
            delay(1000)
        }
        Thread.sleep(100)
        Log.d("udemy_tuto","coroutineJob is parent of childCoroutineJob ${coroutineJob.children.contains(childCoroutineJob)}") // true
        Log.d("udemy_tuto","scopeJob is parent of coroutineJob ${scopeJob.children.contains(coroutineJob)}") // true
        */
        /**
         * 66
         */
        /*
        val scopeJob = Job()
        val scope = CoroutineScope(Dispatchers.Default + scopeJob)
        val passedJob = Job()
        val coroutineJob = scope.launch(passedJob){
            Log.d("udemy_tuto","Starting coroutine")
            delay(1000)
        }
        Thread.sleep(100)
        Log.d("udemy_tuto","pased job and job are references to the same job object: ${ passedJob === coroutineJob }")
        Log.d("udemy_tuto","parent of: ${coroutineJob.children.contains(passedJob)}")
         */
        /**
         * 67
         */
        /*
        runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)
            val parentCoroutineJob = scope.launch {
                launch {
                    delay(1000)
                    Log.d("udemy_tuto","Child coroutine 1 has completed")
                }
                launch {
                    delay(1000)
                    Log.d("udemy_tuto","Child coroutine 2 has completed")
                }
            }
            //parentCoroutineJob.join()
            Log.d("udemy_tuto","Parent coroutine has completed!")
            */
        /*runBlocking<Unit> {
            // coroutine parent is launched then child by launch{}
            val scope = CoroutineScope(Dispatchers.Default)
            scope.coroutineContext[Job]!!.invokeOnCompletion { throwable ->
                if (throwable is CancellationException) {
                    Timber.tag("udemy_tuto").d("parent job was cancelled!")
                }
            }
            val childCoroutineJob = scope.launch {
                delay(1000)
                Timber.tag("udemy_tuto").d("coroutine 1 has completed!")
            }
            childCoroutineJob.invokeOnCompletion { throwable ->
                if (throwable is CancellationException) {
                    Timber.tag("udemy_tuto")
                        .d("coroutine 1 was cancelled, CancellationException is raised!")
                }
            }
            val childCoroutineJob1 = scope.launch {
                delay(1000)
                Timber.tag("udemy_tuto").d("coroutine 2 has completed!")
            }
            childCoroutineJob1.invokeOnCompletion { throwable ->
                if (throwable is CancellationException) {
                    Timber.tag("udemy_tuto")
                        .d("coroutine 2 was cancelled, CancellationException is raised!");
                }
            }
        }*/
            // in video udemy, they tell us that any log will appear, but for me it's the two log are displayed, it's logic because :
            // two job coroutine are blocked one second while the cancel is invoked, in video he add scope.coroutineContext[Job]!!.join() to wait for
            // the two job coroutines
            // that solution 1
            /*scope.cancel()
            scope.coroutineContext[Job]!!.join()*/
            // or solution 2 : parent job is cancelled
            /*
            scope.coroutineContext[Job]!!.cancelAndJoin()
            */
            // solution 3 : parent job isn't cancelled
            /*childCoroutineJob.cancelAndJoin()
            childCoroutineJob1.cancelAndJoin()*/
            /**
             * 68 when one job fails, all the subling fails and even the parent, see below active (scope1.isAlive is false)
             * to resolve that we put SupervisorJob() + exceptionHandler, job 2 still work and scope1.isAlive is false.
             * so the utility belongs to use case, if UC tell that all subling get cancelled if one of these got cancelled.
             *
             */
         /*   val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
                Timber.tag("udemy_tuto").d("caught exception")
            }
            val scope1 = CoroutineScope(Job() + exceptionHandler)
            scope1.launch {
                Timber.tag("udemy_tuto").d("Coroutine 1 starts")
                delay(50)
                Timber.tag("udemy_tuto").d("Coroutine 1 fails")
                throw RuntimeException()
            }

            scope1.launch {
                Timber.tag("udemy_tuto").d("Coroutine 2 starts")
                delay(500)
                Timber.tag("udemy_tuto").d("Coroutine 2 completed")
            }.invokeOnCompletion { throwable ->
                if(throwable is CancellationException){
                    Timber.tag("udemy_tuto").d("coroutine 2 got cancelled")
                }
            }
        Timber.tag("udemy_tuto").d("Scope got cancelled: ${scope1.isActive}")
        */
        /**
         * 69
         * Unstructure concurrency :
         */

         /**
          * 70
          * These is no job object associated with the global scope.
         * coroutine that is started with GlobalScope.launch{} will create a global top coroutines that are completely independent from each other,
         * GlobalScope is to be avoided in our application
         */
        Timber.tag("udemy_tuto").d("Job of Global: ${GlobalScope.coroutineContext[Job]}") // null

        /**
         * 71 viewModelScope
         *
         */
        //dataViewModel.doSomeWork()
        /**
         * 72 lifecycleScope
         * se below
         */
        /**
         * 73.
         * if we want to execute two first coroutines and after execute the other job.
         * solution 1; join() the two Job to wait ,j1.join() j2.join() then j3
         * solution 2; put the two jobs inside a parent job then invoke join() ; put j1 & j2 inside launch {}.join()
         * solution 3: put "coroutineScope{}" as a parent for the two job.
         * note that "coroutineScope{}" is not "supervisorJob{}"
         * a fail of supervisorJob{} won't affect its siblings.
         * //// results ////:
         * solution 1 & 2
            Starting Task 1
            Task 1 completed
            Task 2 completed
            Starting Task 3
            Task 3 completed
         */
        val scope2 = CoroutineScope(Job())
        scope2.launch {
            coroutineScope {
                /*val j1 = launch {
                    Timber.tag("udemy_tuto").d("Starting Task 1")
                    delay(1000)
                    Timber.tag("udemy_tuto").d("Task 1 completed")
                }
                val j2 = launch {
                    Timber.tag("udemy_tuto").d("Starting Task 2")
                    delay(2000)
                    Timber.tag("udemy_tuto").d("Task 2 completed")
                }*/
                doSomeTasks()
            }

            launch {
                Timber.tag("udemy_tuto").d("Starting Task 3")
                delay(300)
                Timber.tag("udemy_tuto").d("Task 3 completed")
            }
        }
        /**
         * 73.
         */

    }
    // solution extension function CoroutineScope
    suspend fun doSomeTasks() = supervisorScope{ 
        launch {
            Timber.tag("udemy_tuto").d("Starting Task 1")
            delay(1000)
            Timber.tag("udemy_tuto").d("Task 1 completed")
        }
        launch {
            Timber.tag("udemy_tuto").d("Starting Task 2")
            delay(2000)
            Timber.tag("udemy_tuto").d("Task 2 completed")
        }
    }

    init {
        /**
         * 72 lifecycleScope : coroutine started one of these options will be suspended until the lifecycle is at least
         * in one of these states,
         * note that when the configuration changes lifecycle will also change
         */
        lifecycleScope.launchWhenStarted {

        }
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