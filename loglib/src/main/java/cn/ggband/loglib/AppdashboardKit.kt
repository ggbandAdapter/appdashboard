package cn.ggband.loglib

import android.app.Application
import cn.ggband.loglib.bean.AppNewVersionBean
import cn.ggband.loglib.utils.FileUtils

object AppdashboardKit {


    const val LOGTAG = "appdashboard"
    private lateinit var app: Application
    private var mCmd: String = ""
    private val mLogRecord: LogRecordManager by lazy { LogRecordManager() }
    //appId
    var appId: String = ""
    private lateinit var mClient: UpLoadClient

    //初始化
    fun with(application: Application): AppdashboardKit {
        app = application
        return this
    }

    /**
     * 配置 log标签
     */
    fun logTagCmd(cmd: String): AppdashboardKit {
        mCmd = cmd
        return this
    }

    fun appId(appId: String): AppdashboardKit {
        this.appId = appId
        return this
    }


    fun start() {
        mLogRecord.execute(app, mCmd)
        mClient = UpLoadClient(app, appId, "http://47.93.250.227/")
    }

    fun setUserAlias(alias: String) {
        mLogRecord.mCustomUserAlias = alias
    }

    fun upLogFile() {
        val logFile = mLogRecord.getUpLoadLogFile().firstOrNull()
        if (logFile != null) {
            val filePath = logFile.absolutePath
            val fileStr = FileUtils.file2Str(filePath)

        }
    }

    fun checkNewVersion(versionCode: Int, softVersion: Int): AppNewVersionBean {
        return mClient.checkNewVersion(versionCode, softVersion)
    }

}