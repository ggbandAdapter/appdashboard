package cn.ggband.loglib

import android.content.Context
import android.os.Build
import android.util.Log
import cn.ggband.loglib.bean.AppNewVersionBean
import cn.ggband.loglib.req.HttpClient
import cn.ggband.loglib.utils.CommUtils.getAppName
import cn.ggband.loglib.utils.CommUtils.getAppVersionCode
import cn.ggband.loglib.utils.CommUtils.getAppVersionName
import org.json.JSONObject
import java.io.File
import java.util.*

class UpLoadClient(
    private val mContext: Context,
    private val mAppId: String,
    private val mServerUrl: String = "http://47.93.250.227/"
) {

    /**
     * 上传日志
     * @param files 文件地址
     * @param logTag 日志标识
     * @param softVersion  软件版本；0:Alpha(内测);1:Beta(公测);2:Release（发布)
     * @param logType 日志类型；0普通日志；1异常日志
     * @return 上传是否成功
     */
    fun upLogFile(
        files: Map<String, String>,
        logTag: String,
        softVersion: Int,
        logType: Int
    ): Boolean {
        val reqPair: MutableMap<String, Any> = HashMap()
        reqPair["appVersionCode"] = mContext.getAppVersionCode()
        reqPair["appVersionName"] = mContext.getAppVersionName()
        reqPair["appName"] = mContext.getAppName()
        reqPair["logType"] = logType
        reqPair["softVersion"] = softVersion
        reqPair["logTag"] = logTag
        reqPair["phoneModel"] = Build.MANUFACTURER + "-" + Build.MODEL
        val isSuccess =
            HttpClient.postFileByForm(
                mServerUrl + "app/log/upload",
                reqPair.toMap(),
                files.toMap(),
                mapOf("appId" to mAppId)
            )
        return isSuccess.isNotEmpty()
    }

    /**
     * 检查更新
     * @param versionCode 当前版本号
     * @param softVersion 软件版本；0:Alpha(内测);1:Beta(公测);2:Release（发布)
     * @return AppNewVersionBean
     */
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