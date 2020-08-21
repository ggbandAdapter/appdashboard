package cn.ggband.loglib

import android.app.Application

object AppdashboardKit {
    const val LOGTAG = "appdashboard"
    var mAppId: String = ""
    var mCmd = "logcat appdashboard:D *:S"
    lateinit var mApp: Application
    // 软件版本；0:Alpha(内测);1:Beta(公测);2:Release（发布)
    var mSoftVersion = 0
    //用户标识
    var mCustomUserAlias = ""
    //api
    lateinit var apiHelper: AppdashBoardApiHelper

    //初始化
    fun init(application: Application, cmd: String, appId: String, softVersion: Int = 2) {
        mApp = application
        mAppId = appId
        mSoftVersion = softVersion
        mCmd = cmd
        start()
    }

    private fun start() {
        apiHelper = AppdashBoardApiHelper()
    }

    /**
     * 释放资源
     */
    fun release() {
        apiHelper.release()
    }


}