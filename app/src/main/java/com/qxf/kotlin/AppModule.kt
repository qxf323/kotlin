package com.qxf.kotlin

import android.content.Context
import com.qxf.kotlin.dbflows.Dbflows
import com.qxf.kotlin.dbflows.MyDatabase

object AppModule {
    private var mFlows: Dbflows? = null
    //初始化数据库
    fun init(context: Context?) {
        mFlows = Dbflows.Builder()
            .databaseClass(MyDatabase::class.java)
            .helperListener(Dbflows.defaultHelperListener)
            .modelNotifier(Dbflows.defaultModelNotifier)
            .build()
        mFlows!!.init(context)
    }

    /**
     * 提供数据库组件
     */
    fun provideFlows(): Dbflows? {
        return mFlows
    }
}