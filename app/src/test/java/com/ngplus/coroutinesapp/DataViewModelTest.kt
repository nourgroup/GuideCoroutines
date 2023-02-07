package com.ngplus.coroutinesapp

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class DataViewModelTest {

    lateinit var mDataViewModel : DataViewModel

    @Before
    fun setUp() {
        mDataViewModel = DataViewModel()
    }

    @Test
    fun `getDataWithFlow() none flows`() = runTest{
        flowOf("one", "two").test{
            assertEquals("one", awaitItem())
            assertEquals("two", awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `computeDistance() with valid param return 900`() = runTest{

        val realTimeStart = System.currentTimeMillis()
        val virtualStartTime= currentTime
        // given - arrangement
        val a = 12.0
        val b = 7783.0
        val expected = 77833.0
        // when - act
        mDataViewModel.computeDistance(a,b)
        //advanceTimeBy(1000)
        // then - assert
        //assertEquals(true,true)

        val realTimeDuration = System.currentTimeMillis() -realTimeStart
        val virtualStartDuration= currentTime - virtualStartTime

        println("realTimeDuration $realTimeDuration real ms")
        println("realTimeDuration $virtualStartDuration virtual ms")

    }


}