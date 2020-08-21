package cn.ggband.loglib

import android.database.sqlite.SQLiteDatabase
import cn.ggband.loglib.bean.AppNewVersionBean
import cn.ggband.loglib.db.DBHelper
import cn.ggband.loglib.db.dao.CashDao
import cn.ggband.loglib.db.tb.TbCash
import cn.ggband.loglib.utils.CommUtils.getAppVersionCode

/**
 * API Helper
 */
class AppdashBoardApiHelper {

    private val mLogRecord: LogRecordManager by lazy { LogRecordManager() }
    private val mClient: UpLoadClient by lazy {
        UpLoadClient(
            AppdashboardKit.mApp,
            AppdashboardKit.mAppId
        )
    }
    private val cashDb: SQLiteDatabase by lazy {
        DBHelper(AppdashboardKit.mApp).writableDatabase
    }
    private val cashDao: CashDao by lazy { CashDao(cashDb) }

    init {
        mLogRecord.execute(AppdashboardKit.mApp, AppdashboardKit.mCmd)
    }

    /**
     * 插入异常日志
     */
    fun insertCashLog(tbCashLog: TbCash): Long {
        return cashDao.insertCash(tbCashLog)
    }

    /**
     * 获取所以异常日志
     */
    fun getCashLogs(): List<TbCash> {
        return cashDao.cashList
    }

    /**
     * 获取未上报的异常日志
     */
    fun getUnReportCashLogs(): List<TbCash> {
        return cashDao.unReportCashList
    }

    /**
     * 更新异常日志
     */
    fun upDateCash(tbCashLog: TbCash): Int {
        return cashDao.updateCash(tbCashLog)
    }

    /**
     * 日志上传
     * @return 上传是否成功
     */
    fun upLogFile(): Boolean {
        val logFile = mLogRecord.getUpLoadLogFile().firstOrNull()
        return if (logFile != null) {
            val filePath = logFile.absolutePath
            mClient.upLogFile(
                mapOf("file" to filePath),
                AppdashboardKit.mCustomUserAlias, 2, 0
            )
        } else
            false
    }

    /**
     * 检查更新
     * @return AppNewVersionBean
     */
    fun checkNewVersion(): AppNewVersionBean {
        return mClient.checkNewVersion(
            AppdashboardKit.mApp.getAppVersionCode(),
            AppdashboardKit.mSoftVersion
        )
    }

    /**
     * 上传cash记录
     */
    fun upLoadCashLogs(): Boolean {
        val unCashLogs = getUnReportCashLogs()
        val isSuccess = mClient.upCashLog(unCashLogs)
        if (isSuccess) {
            //上传成功，将记录置为已上传
            unCashLogs.map {
                it.isReported = 1
                it
            }.forEach {
                upDateCash(it)
            }
        }
        return isSuccess
    }

    fun release() {
        cashDb.close()
    }

}