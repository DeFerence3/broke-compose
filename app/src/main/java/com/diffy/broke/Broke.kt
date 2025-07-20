package com.diffy.broke

import android.app.Application
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class Broke : Application(){

    override fun onCreate() {
        applicationScope = CoroutineScope(SupervisorJob())
        MMKV.initialize(this)
        super.onCreate()
    }

    companion object {
        lateinit var applicationScope: CoroutineScope
    }
}
