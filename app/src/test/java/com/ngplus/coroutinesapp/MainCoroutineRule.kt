package com.ngplus.coroutinesapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

// for error : Module with the Main dispatcher had failed to initialize. For tests Dispatchers.setMain from kotlinx-coroutines-test module can be used
/* JUnit rule: https://junit.org/junit4/javadoc/4.12/org/junit/Rule.html */
@ExperimentalCoroutinesApi
class MainCoroutineRule(
    val dispatcher: TestCoroutineDispatcher
    = TestCoroutineDispatcher()
):
    TestWatcher(),
    TestCoroutineScope by TestCoroutineScope(dispatcher)
{
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}