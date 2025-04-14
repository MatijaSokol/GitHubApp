package com.matijasokol.test

import com.matijasokol.core.dictionary.Dictionary

@Suppress("FunctionName")
fun FakeDictionary(
    getString: (Int) -> String = { "" },
    getStringArgs: (Int, List<Any>) -> String = { _, _ -> "" },
) = object : Dictionary {

    override fun getString(resId: Int): String = getString(resId)

    override fun getString(resId: Int, vararg args: Any): String = getStringArgs(resId, args.toList())
}
