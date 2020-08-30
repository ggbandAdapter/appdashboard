package cn.ggband.loglib

import android.database.sqlite.SQLiteDatabase
import cn.ggband.loglib.bean.AppNewVersionBean
import cn.ggband.loglib.bean.LogType
import cn.ggband.loglib.bean.value
import cn.ggband.loglib.db.DBHelper
import cn.ggband.loglib.db.dao.CashDao
import cn.ggband.loglib.db.tb.TbCash
import cn.ggband.loglib.utils.CommUtils.getAppVersionCode
import java.io.File

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
     * 默认日志上传
     * 上传最近两个日志文件
     * @return 上传是否成功
     */
    fun upLogFile(): Boolean {
        val logFiles = mLogRecord.getLogFiles()
        val upLogFiles = if (logFiles.size > 2) {
            logFiles.subList(0, 2)
        } else logFiles
        return upLogFile(upLogFiles.map { it.absolutePath })
    }

    /**
     * 上传日志文件
     * @param filePaths 日志文件路径组
     */
    fun upLogFile(filePaths: List<String>): Boolean {
        if (filePaths.isNullOrEmpty()) return false
        return filePaths.map {
            mClient.upLogFile(
                mapOf("file" to it),
                AppdashboardKit.mUserTag,
                AppdashboardKit.mSoftVersion.value(),
                LogType.DEF_LOG.value()
            )
        }.last()
    }

    /**
     * 上传异常日志文件
     * @param filePaths 异常日志文件路径组
     */
    fun upCashLogFile(filePaths: List<String>): Boolean {
        if (filePaths.isNullOrEmpty()) return false
        return filePaths.map {
            mClient.upLogFile(
                mapOf("file" to it),
                AppdashboardKit.mUserTag,
                AppdashboardKit.mSoftVersion.value(),
                LogType.CASH_LOG.value()
            )
        }.last()
    }

    /**
     * 检查更新
     * @return AppNewVersionBean
     */
    fun checkNewVersion(): AppNewVersionBean {
        return mClient.checkNewVersion(
            AppdashboardKit.mApp.getAppVersionCode(),
            AppdashboardKit.mSoftVersion.value()
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


    /**
     * 获取日志目录
     */
    fun getLogPath(): String {
        return mLogRecord.getLogPath()
    }

    /**
     * 获取异常目录
     */
    fun getCashPath(): String {
        return mLogRecord.getCashPath()
    }

    /**
     * 获取日志文件组
     */
    fun getLogFiles(): List<File> {
        return mLogRecord.getLogFiles()
    }

    /**
     * 获取异常文件组
     */
    fun getCashFiles(): List<File> {
        return mLogRecord.getCashFiles()
    }

    fun release() {
        cashDb.close()
    }

}