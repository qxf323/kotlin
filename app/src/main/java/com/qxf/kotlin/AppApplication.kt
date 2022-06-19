package com.qxf.kotlin

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager

class AppApplication  : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            FlowManager.init(FlowConfig.Builder(this).build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}