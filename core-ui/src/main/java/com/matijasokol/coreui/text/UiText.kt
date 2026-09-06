package com.matijasokol.coreui.text

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
sealed interface UiText {

    data class StringText(val value: String) : UiText

    data class StringResource(
        @StringRes val resId: Int,
        val args: ImmutableList<Any> = persistentListOf(),
    ) : UiText {

        constructor(
            @StringRes resId: Int,
            vararg args: Any,
        ) : this(
            resId = resId,
            args = persistentListOf(*args),
        )
    }
}

@Composable
fun UiText.asString(): String = when (this) {
    is UiText.StringText -> value
    is UiText.StringResource -> when (args.isEmpty()) {
        true -> stringResource(id = resId)
        false -> {
            val resources = LocalResources.current
            stringResource(
                id = resId,
                formatArgs = args.map { it.resolve(resources) }.toTypedArray(),
            )
        }
    }
}

fun UiText.asString(resources: Resources): String = when (this) {
    is UiText.StringText -> value
    is UiText.StringResource -> when (args.isEmpty()) {
        true -> resources.getString(resId)
        false -> resources.getString(
            resId,
            *args.map { it.resolve(resources) }.toTypedArray(),
        )
    }
}

private fun Any.resolve(resources: Resources): Any = when (this) {
    is UiText -> asString(resources)
    else -> this
}
