package cn.ggband.loglib

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import cn.ggband.loglib.bean.AppNewVersionBean
import cn.ggband.loglib.db.DBHelper
import cn.ggband.loglib.db.dao.CashDao
import cn.ggband.loglib.utils.CommUtils.getAppVersionCode

object AppdashboardKit {


    const val LOGTAG = "appdashboard"
    private lateinit var app: Application
    private var mCmd: String = ""
    private val mLogRecord: LogRecordManager by lazy { LogRecordManager() }
    private val cashDb: SQLiteDatabase by lazy {
        DBHelper(app).writableDatabase
    }
    val cashDao: CashDao by lazy { CashDao(cashDb) }


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
        mClient = UpLoadClient(app, appId)
    }

    fun setUserAlias(alias: String) {
        mLogRecord.mCustomUserAlias = alias
    }

    /**
     * 日志上传
     * @return 上传是否成功
     */
    fun upLogFile(): Boolean {
        val logFile = mLogRecord.getUpLoadLogFile().firstOrNull()
        return if (logFile != null) {
            val filePath = logFile.absolutePath
            mClient.upLogFile(mapOf("file" to filePath), mLogRecord.mCustomUserAlias, 2, 0)
        } else
            false
    }

    /**
     * 检查更新
     * @param softVersion 软件版本；0:Alpha(内测);1:Beta(公测);2:Release（发布)
     * @return AppNewVersionBean
     */
    fun checkNewVersion(softVersion: Int): AppNewVersionBean {
        return mClient.checkNewVersion(app.getAppVersionCode(), softVersion)
    }

    /**
     * 上传cash记录
     */
    fun upCash() {
        val unCashLogs = cashDao.unReportCashList
        Log.d(LOGTAG, "unCashLogs=$unCashLogs")
        //上传成功，将记录置为已上传
        unCashLogs.map {
            it.isReported = 1
            it
        }.forEach {
            cashDao.updateCash(it)
        }
    }

    /**
     * 释放资源
     */
    fun release() {
        cashDb.close()
    }


}