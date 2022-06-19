package com.qxf.kotlin.http

import java.io.IOException

interface Callback {
    fun onError(e:IOException)
    fun onSuccess(resultStr: String)
}