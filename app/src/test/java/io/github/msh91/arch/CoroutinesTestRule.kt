package io.github.msh91.arch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class CoroutinesTestRule : TestRule {
  private val testCoroutineDispatcher = TestCoroutineDispatcher()
  private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)


  override fun apply(base: Statement, description: Description?) = object : Statement() {
    override fun evaluate() {
      Dispatchers.setMain(testCoroutineDispatcher)
      Dispatchers.IO
      base.evaluate()

      Dispatchers.resetMain()
      testCoroutineScope.cleanupTestCoroutines()
    }
  }

  fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) {
    testCoroutineScope.runBlockingTest { block() }
  }
}
