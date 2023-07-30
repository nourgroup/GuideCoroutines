package com.ngplus.coroutinesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class DataViewModelTest {

    // for the error : Method getMainLooper in android.os.Looper not mocked.
    @get:Rule
    val testInstantTaskExecutorRule : TestRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Test
    fun `getDataWithFlow() none flows`() = runTest{
        flowOf("one", "two").test{
            assertEquals("one", awaitItem())
            assertEquals("two", awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `computeDistance() with valid param return 900`() = coroutineRule.runTest{
        val realTimeStart = System.currentTimeMillis()
        val virtualStartTime= currentTime
        // given - arrangement
        val a = 12.0
        val b = 7783.0
        val expected = 6.0575233E7
        // ajouter
        val mDataViewModel = DataViewModel(coroutineRule.dispatcher)
        //val mDataViewModel = DataViewModel()
        // when - act
        mDataViewModel.computeDistance(a,b)

        advanceUntilIdle()
        // then - assert
        assertEquals(expected,mDataViewModel.resLD.value!!, 0.0)

        val realTimeDuration = System.currentTimeMillis() -realTimeStart
        val virtualStartDuration= currentTime - virtualStartTime
        println("realTimeDuration $realTimeDuration real ms")
        println("realTimeDuration $virtualStartDuration virtual ms")
    }

    @Test
    fun testme(){
        /**
         * plural sight
         */
        val user = User("")
        user.create{
            println("$it created ")
        }
        user.create{
            println(" $it created ")
        }
        /***/
        val createListener = { u: User -> println("$u created") }
        val createListener1 = Created{ print("$it created ") }
        user.create(createListener)
        user.create(createListener1)

    }
}