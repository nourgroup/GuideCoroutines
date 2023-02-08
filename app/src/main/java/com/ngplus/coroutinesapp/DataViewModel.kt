package com.ngplus.coroutinesapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed

import timber.log.Timber

class DataViewModel(
    private val dispatcher: CoroutineDispatcher
    ) : ViewModel() {

    var myData = MutableLiveData<String>()
    private var _resLD = MutableLiveData<Double>()
    var resLD = _resLD

    private var _stateFlow = MutableStateFlow<String>("")
    var stateFlow: StateFlow<String> = _stateFlow
        .stateIn(
        scope = viewModelScope,
        initialValue = "",
        started = WhileSubscribed(10000)
    )

    val sendflow = flow<Int> {
        delay(2000)
        emit(1)
        emit(2)
    }

    private var _sharedStateFlow = MutableStateFlow<String>("")
    var sharedStateFlow: StateFlow<String> = _sharedStateFlow

    fun startCounter(){
        val myList = listOf("A","B","B","B","B","C","D","E")
        viewModelScope.launch {
            myList.forEach {
                myData.value = it
                delay(1000)
            }
        }
    }

     fun getData() {
        viewModelScope.launch {
            for(i in 0..10){
                _stateFlow.emit(i.toString())
            }
        }
    }

    fun getDataWithFlow() : StateFlow<String>{
        val flow1 = flow<Int> {
            for(i in 0..10){
                emit(i)
                delay(1000)
                //Log.i("Flow_tutorial","$i send-first")
            }
        }.filter { it.mod(2) == 0 }

        val flow2 = flow<Int> {
            for(i in 10 downTo 0){
                emit(i)
                delay(1000)
                //Log.i("Flow_tutorial","$i send-second")
            }
        }.filter { it.mod(2) == 1 }

        return flow1.zip(flow2){ a,b -> "$a $b" }
            .stateIn(
                scope = viewModelScope,
                initialValue = "",
                started = WhileSubscribed(5000)
            )
    }

    fun  doSomeWork(){
        val job = viewModelScope.launch {
            Timber.tag("udemy_tuto").d("iam the first statement in the coroutine")
            delay(1000)
        }
        Timber.tag("udemy_tuto").d("iam the first statement after launching the coroutine.")
        job.invokeOnCompletion {
            throwable ->
            if(throwable is CancellationException){
                Timber.tag("udemy_tuto").d("Coroutine was cancelled.")
            }
        }
    }

    /**
     *
     */
    suspend fun computeDistance(lat : Double, long : Double) {
        var res : Double = 0.0
        viewModelScope.launch(dispatcher){
        //viewModelScope.launch(Dispatchers.IO){
        //withContext(Dispatchers.IO){
            delay(1000)
            res = (lat*lat) + (long*long)
            _resLD.postValue(res)
            println("Coroutine completed $res")
        }
    }
}