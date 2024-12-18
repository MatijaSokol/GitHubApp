package com.matijasokol.coreui.dictionary

interface Dictionary {

    fun getString(resId: Int): String

    fun getString(
        resId: Int,
        vararg args: Any,
    ): String
}
