package com.qxf.kotlin.dbflows

import com.raizlabs.android.dbflow.annotation.Database

/**
 * @author qiuxianfu
 * created on 20190712
 */
@Database(
    version = MyDatabase.VERSION,
    name = MyDatabase.NAME
)
object MyDatabase {
    const val VERSION = 1
    const val NAME = "MyDatabase"
}
