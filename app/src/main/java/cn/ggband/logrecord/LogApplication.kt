package cn.ggband.logrecord

import android.app.Application
import cn.ggband.loglib.AppdashboardKit

class LogApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppdashboardKit.with(this)
            .logTagCmd("logcat ggband:D ims:I *:S")
            .appId("ENMFXLcN").start()
    }
}