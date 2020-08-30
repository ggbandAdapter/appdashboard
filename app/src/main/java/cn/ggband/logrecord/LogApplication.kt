package cn.ggband.logrecord

import android.app.Application
import cn.ggband.loglib.AppdashboardKit

class LogApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppdashboardKit.init(this, "logcat ggband:D ims:I *:S", "kJOxWfRu")

    }
}