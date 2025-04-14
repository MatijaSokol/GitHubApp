package com.matijasokol.repo.detail

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.jupiter.api.extension.TestInstancePostProcessor

/**
 * This class enables concurrency behavior and switching from one dispatcher to another
 * For example, if your test is doing something with viewModelScope which is bound to
 * Dispatchers.Main, you will probably need to define custom coroutine TestRule that will be
 * capable to handle switching between dispatchers, since you don't have access to Dispatchers.Main
 * in unit test.
 */
internal class AndroidCoroutinesExtension(
    mockKExtension: MockKExtension = MockKExtension(),
    private val dispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestInstancePostProcessor by mockKExtension,
    ParameterResolver by mockKExtension,
    BeforeEachCallback,
    AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(
            object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

                override fun postToMainThread(runnable: Runnable) = runnable.run()

                override fun isMainThread(): Boolean = true
            },
        )
        Dispatchers.setMain(dispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
        dispatcher.cancelChildren()
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}
