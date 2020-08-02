package cn.ggband.loglib

import android.app.Application
import cn.ggband.loglib.utils.FileUtils

object AppdashboardKit {


    const val LOGTAG = "appdashboard"
    private lateinit var app: Application
    private var mCmd: String = ""
    private val mLogRecord: LogRecordManager by lazy { LogRecordManager() }
    //appId
     var appId :String = ""
    var userTag:String = "ggband"
    private lateinit var mWebView: LogWebView

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

    fun appId(appId:String): AppdashboardKit{
        this.appId = appId
        return this
    }


    fun start() {
        mLogRecord.execute(app, mCmd)
        mWebView = LogWebView(app)
        mWebView.load()
    }

    fun setUserAlias(alias: String) {
        mLogRecord.mCustomUserAlias = alias
    }

    fun upLogFile() {
        val logFile = mLogRecord.getUpLoadLogFile().firstOrNull()
        if(logFile!=null){
            val filePath = logFile.absolutePath
            val fileStr = FileUtils.file2Str(filePath)
            mWebView.upLogFile(fileStr,2,logFile.name)
        }
    }

    fun checkNewVersion(versionCode: Int, softVersion: Int) {
        mWebView.checkNewVersion(versionCode, softVersion)
    }

}