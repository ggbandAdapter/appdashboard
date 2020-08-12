package cn.ggband.loglib

import android.content.Context
import android.os.Build
import cn.ggband.loglib.bean.AppNewVersionBean
import cn.ggband.loglib.req.HttpClient
import cn.ggband.loglib.utils.CommUtils.getAppName
import cn.ggband.loglib.utils.CommUtils.getAppVersionCode
import cn.ggband.loglib.utils.CommUtils.getAppVersionName
import org.json.JSONObject
import java.io.File
import java.util.*

class UpLoadClient(
    private val mContent: Context,
    private val mAppId: String,
    private val mServerUrl: String
) {
    /**
     * 上传日志
     */
    fun upLogFile(logFile: File?, logTag: String, softVersion: Byte) {
        val reqPair: MutableMap<String, Any> = HashMap()
        reqPair["appVersionCode"] = mContent.getAppVersionCode()
        reqPair["appVersionName"] = mContent.getAppVersionName()
        reqPair["appName"] = mContent.getAppName()
        reqPair["softVersion"] = softVersion
        reqPair["logTag"] = logTag
        reqPair["phoneModel"] = Build.MANUFACTURER + "-" + Build.MODEL
    }

    fun checkNewVersion(versionCode: Int, softVersion: Int): AppNewVersionBean {
        val reqPair = mapOf("versionCode" to versionCode, "softVersion" to softVersion)
        return try {
            val newVersionBean = AppNewVersionBean()
            val resultStr = HttpClient.reqPost(
                mServerUrl + "app/version/check",
                reqPair,
                mapOf("appId" to mAppId)
            )
            //解析
            val versionInfo = AppNewVersionBean.VersionBean()
            val jsonData = JSONObject(resultStr).optJSONObject("data")
            val jsonVersion = jsonData?.optJSONObject("version")
            newVersionBean.isHashNewVersion = jsonData?.optBoolean("hashNewVersion") ?: false
            versionInfo.appName = jsonVersion?.optString("appName")
            versionInfo.versionCode = jsonVersion?.optInt("versionCode") ?: 0
            versionInfo.versionName = jsonVersion?.optString("versionName")
            versionInfo.packageName = jsonVersion?.optString("packageName")
            versionInfo.downUrl = jsonVersion?.optString("downUrl")
            versionInfo.isForce = jsonVersion?.optInt("isForce") ?: 0
            versionInfo.softVersion = jsonVersion?.optInt("softVersion") ?: 0
            versionInfo.versionTips = jsonVersion?.optString("versionTips")
            versionInfo.enable = jsonVersion?.optInt("enable") ?: 0
            versionInfo.remarks = jsonVersion?.optString("remarks")
            versionInfo.fileSize = jsonVersion?.optInt("fileSize") ?: 0
            versionInfo.createTime = jsonVersion?.optLong("createTime") ?: 0L
            versionInfo.updateTime = jsonVersion?.optLong("updateTime") ?: 0L
            newVersionBean.version = versionInfo
            newVersionBean
        } catch (e: Throwable) {
            AppNewVersionBean()
        }
    }


}