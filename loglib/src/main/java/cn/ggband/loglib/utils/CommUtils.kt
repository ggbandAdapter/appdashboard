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

    /**
     * 获取应用程序名称
     */
    @Synchronized
    fun Context.getAppName(): String {
        try {
            val packageInfo = packageManager.getPackageInfo(
                packageName, 0
            )
            val labelRes = packageInfo.applicationInfo.labelRes
            return resources.getString(labelRes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

}