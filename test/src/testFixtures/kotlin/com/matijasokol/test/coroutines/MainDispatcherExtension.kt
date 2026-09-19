package com.matijasokol.test.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Replaces [Dispatchers.Main] with a [TestDispatcher] for each test, so code running in
 * `viewModelScope` (bound to Dispatchers.Main) can be tested on the JVM.
 */
class MainDispatcherExtension(private val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    BeforeEachCallback,
    AfterEachCallback {

    override fun beforeEach(context: ExtensionContext) {
        Dispatchers.setMain(dispatcher)
    }

    override fun afterEach(context: ExtensionContext) {
        Dispatchers.resetMain()
        dispatcher.cancelChildren()
    }
}
