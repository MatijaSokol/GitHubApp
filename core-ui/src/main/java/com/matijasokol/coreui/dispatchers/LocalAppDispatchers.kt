package com.matijasokol.coreui.dispatchers

import androidx.compose.runtime.staticCompositionLocalOf
import com.matijasokol.core.dispatchers.AppDispatchers

val LocalAppDispatchers = staticCompositionLocalOf<AppDispatchers> {
    error("AppDispatchers not provided")
}
