package com.qxf.kotlin.dbflows

import androidx.annotation.NonNull


/**
 *
 * @param <T>
</T> */
interface Flows<T> {
    /**
     * handle the flows data
     *
     * @param t the value
     * @throws Exception on error
     */
    @Throws(Exception::class)
    fun accept(@NonNull t: T)
}