package com.matijasokol.coreui.dictionary

import android.app.Application
import javax.inject.Inject

class DictionaryImpl @Inject constructor(
    private val context: Application,
) : Dictionary {

    override fun getString(resId: Int): String = context.getString(resId)

    override fun getString(resId: Int, vararg args: Any): String = context.getString(resId, *args)
}
