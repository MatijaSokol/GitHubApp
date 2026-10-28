package com.matijasokol.test.coroutines

import com.matijasokol.core.dispatchers.AppDispatchers
import kotlinx.coroutines.test.TestDispatcher

fun TestDispatcher.asAppDispatchers(): AppDispatchers = AppDispatchers(
    default = this,
    io = this,
    main = this,
    mainImmediate = this,
)
