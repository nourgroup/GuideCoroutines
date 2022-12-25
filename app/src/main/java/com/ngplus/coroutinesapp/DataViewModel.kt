package com.ngplus.coroutinesapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed

import kotlinx.coroutines.launch

class DataViewModel : ViewModel() {

    var myData = MutableLiveData<String>()
    private var _stateFlow = MutableStateFlow<String>("")
    var stateFlow: StateFlow<String> = _stateFlow

    fun startCounter(){
        val myList = listOf("A","B","B","B","B","C","D","E")
        viewModelScope.launch {
            myList.forEach {
                myData.value = it
                delay(1000)
            }
        }
    }

    suspend fun getData(): StateFlow<Int> =
        //val flow1 =
        flow<Int> {
            for(i in 0..100){
                emit(i)
                delay(1000)
            }
        }
            .filter { it.mod(2)==0 }
            .stateIn(
                scope = viewModelScope,
                initialValue = 0,
                started = WhileSubscribed(5000)
            )

        /*val flow2 = flow<String> {
            for(i in 0..10){
                emit(i.toString())
                delay(1000)
            }
        }.filter { it!="" }*/
        /**
         * zip() operator
         */
        //stateFlow = flow1.zip(flow2){ a,b -> "$a $b" }.stateIn(scope = viewModelScope, initialValue = "", started = WhileSubscribed(5000) )
}