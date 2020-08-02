package cn.ggband.loglib.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

object CommUtils {
    fun Context.getAppVersionCode(): Int {
        val pm: PackageManager = packageManager
        val pi: PackageInfo = pm.getPackageInfo(
            packageName,
            PackageManager.GET_ACTIVITIES
        )
        return pi.versionCode
    }

    fun Context.getAppVersionName(): String {
        val pm: PackageManager = packageManager
        val pi: PackageInfo = pm.getPackageInfo(
            packageName,
            PackageManager.GET_ACTIVITIES
        )
        return pi.versionName
    }
}