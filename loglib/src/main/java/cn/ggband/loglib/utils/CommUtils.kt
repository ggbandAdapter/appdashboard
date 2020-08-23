package cn.ggband.loglib.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log


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


    fun isJson(content: String?): Boolean {
        if (content == null) return false
        return (content.startsWith("{") && content.endsWith("}"))
                || (content.startsWith("[") && content.endsWith("]"))
    }

    //Object转Map
    @Throws(IllegalAccessException::class)
    fun getObjectToMap(obj: Any): Map<String, Any>? {
        val map: MutableMap<String, Any> =
            LinkedHashMap()
        val clazz: Class<*> = obj.javaClass
        println(clazz)
        for (field in clazz.declaredFields) {
            field.isAccessible = true
            val fieldName = field.name
            var value = field[obj]
            if (value == null) {
                value = ""
            }
            map[fieldName] = value
        }
        return map
    }

    fun getPhoneModel(): String {
        return Build.MANUFACTURER + "-" + Build.MODEL
    }

}