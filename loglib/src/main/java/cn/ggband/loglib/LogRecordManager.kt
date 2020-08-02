package cn.ggband.loglib

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Process
import android.util.Log
import cn.ggband.loglib.AppdashboardKit.LOGTAG
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * log 存储
 */
class LogRecordManager : Thread.UncaughtExceptionHandler {

    private lateinit var mContext: Context
    private var mLogThread: Thread? = null
    private var mLogPath: String? = null
    private var mCrashPath: String? = null
    private val LOG_DIR = "logLib"
    private val CRASH_DIR = "crash"
    private val LOG_FILE_HEADER = "log_"
    private val CRASH_FILE_HEADER = "crash_"
    private val LOG_FILE_TYPE = ".txt"
    private val TIME_FORMAT_RULE = "yyyy-MM-dd HH:mm:ss"

    //自定义用户别名
    var mCustomUserAlias = ""

    private var mDefaultCrashHandler: Thread.UncaughtExceptionHandler? = null


    fun execute(context: Context, cmd: String) {
        mContext = context
        initLogPath()
        startCrash()
        startHandleLog(cmd)
    }

    /**
     * 初始化log 目录路径
     */
    private fun initLogPath() {
        val logParentPath =
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
                mContext.getExternalFilesDir(null)!!.absolutePath + File.separator
            else
                mContext.filesDir.absolutePath + File.separator
        Log.d(LOGTAG, "logParentPath:$logParentPath")
        mLogPath = logParentPath + LOG_DIR
        mCrashPath = logParentPath + CRASH_DIR
    }


    /**
     * 初始化异常收集器
     */
    private fun startCrash() {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 开始执行log收集
     */
    private fun startHandleLog(cmd: String) {
        if (null == mLogThread || !mLogThread!!.isAlive) {
            val file = File(mLogPath)
            if (!file.exists()) {
                file.mkdirs()
            } else {
                clearOverdueLog(file)
            }
            mLogThread = Thread(Runnable {
                var fos: FileOutputStream? = null
                try {
                    Runtime.getRuntime().exec("logcat -c")
                    val mPro = Runtime.getRuntime().exec(cmd)
                    val inputStream = mPro.inputStream
                    fos = getOutputSteam()
                    var len = 0
                    var totalSize = 0
                    val bytes = ByteArray(1024)
                    while (-1 != inputStream.read(bytes).also { len = it }) {
                        fos?.write(bytes, 0, len)
                        fos?.flush()
                        totalSize += len
                        if (totalSize >= 1024 * 1024 * 30) {
                            totalSize = 0
                            closeOutputStream(fos)
                            fos = getOutputSteam()
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    closeOutputStream(fos)
                }
            })
            mLogThread!!.start()
        }
    }

    private fun closeOutputStream(fos: FileOutputStream?) {
        try {
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(FileNotFoundException::class)
    private fun getOutputSteam(): FileOutputStream? {
        var os: FileOutputStream? = null
        val time = SimpleDateFormat(TIME_FORMAT_RULE, Locale.getDefault()).format(Date())
        val logFilePath =
            mLogPath + File.separator + LOG_FILE_HEADER + time + LOG_FILE_TYPE
        Log.d(LOGTAG, "logFilePath:$logFilePath")
        os = FileOutputStream(logFilePath)
        try {
            writeAppInfoInFile(os)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return os
    }

    /**
     * log文件中写入app版本等信息
     *
     * @param os
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun writeAppInfoInFile(os: FileOutputStream) {
        val pm: PackageManager = mContext.packageManager
        val pi: PackageInfo = pm.getPackageInfo(
            mContext.packageName,
            PackageManager.GET_ACTIVITIES
        )
        val newLine = System.getProperty("line.separator")
        //当前版本号
        os.write(("App Version:" + pi.versionName + "_" + pi.versionCode).toByteArray())
        os.write(newLine!!.toByteArray())
        //当前系统
        os.write(("OS version:" + Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT).toByteArray())
        os.write(newLine.toByteArray())
        //制造商
        os.write(("Vendor:" + Build.MANUFACTURER).toByteArray())
        os.write(newLine.toByteArray())
        //手机型号
        os.write(("Model:" + Build.MODEL).toByteArray())
        os.write(newLine.toByteArray())
        //CPU架构
        os.write(("CPU ABI:" + Build.CPU_ABI).toByteArray())
        os.write(newLine.toByteArray())
        //CPU架构
        os.write(("User Alias:$mCustomUserAlias").toByteArray())
        os.write(newLine.toByteArray())
        os.flush()
    }

    /**
     * 清理过期的日志文件
     *
     * @param file
     */
    private fun clearOverdueLog(file: File) {
        if (file.isDirectory) {
            val list = file.list() ?: return
            for (fileName in list) {
                if (isCanDel(fileName)) {
                    val mChildFile =
                        File(mLogPath + File.separator + fileName)
                    mChildFile.delete()
                }
            }
        }
    }

    private fun getFileCreateTimeByFileName(fileName: String): Long {
        val fileName = fileName.substring(4, fileName.indexOf("."))
        val dateFormat = SimpleDateFormat(TIME_FORMAT_RULE)
        val createTimeDate = dateFormat.parse(fileName)
        return createTimeDate?.time ?: 0
    }

    private fun isCanDel(name: String): Boolean {
        val fileName = name.substring(4, name.indexOf("."))
        val dateFormat = SimpleDateFormat(TIME_FORMAT_RULE)
        return try {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, -3)
            val mLimitTime = calendar.time
            val mCreateTime = dateFormat.parse(fileName)
            mLimitTime.after(mCreateTime)
        } catch (e: ParseException) {
            e.printStackTrace()
            false
        }
    }

    override fun uncaughtException(t: Thread?, e: Throwable) {
        writeCrashInfo(e)
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler!!.uncaughtException(t, e)
        } else {
            Process.killProcess(Process.myPid())
        }
    }

    private fun writeCrashInfo(ex: Throwable) {
        //如果没有SD卡，直接返回
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            return
        }
        try {
            val fileDir = File(mCrashPath)
            if (!fileDir.exists()) {
                fileDir.mkdirs()
            }
            val currentTime = System.currentTimeMillis()
            val time = SimpleDateFormat("yyyy-MM-dd HH：mm：ss", Locale.getDefault())
                .format(Date(currentTime))
            val exfile =
                File(mCrashPath + File.separator + CRASH_FILE_HEADER + time + LOG_FILE_TYPE)
            val pw =
                PrintWriter(BufferedWriter(FileWriter(exfile)))
            pw.println(time)
            val pm: PackageManager = mContext.packageManager
            var pi: PackageInfo? = null
            pi = pm.getPackageInfo(mContext.packageName, PackageManager.GET_ACTIVITIES)
            //当前版本号
            pw.println("App Version:" + pi!!.versionName + "_" + pi.versionCode)
            //当前系统
            pw.println("OS version:" + Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT)
            //制造商
            pw.println("Vendor:" + Build.MANUFACTURER)
            //手机型号
            pw.println("Model:" + Build.MODEL)
            //CPU架构
            pw.println("CPU ABI:" + Build.CPU_ABI)
            ex.printStackTrace(pw)
            pw.close()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

     fun getUpLoadLogFile(): List<File> {
        val logFile = File(mLogPath)
        if (logFile.isDirectory) {
            val fileNameList = logFile.list() ?: return emptyList()
           val sortFileNameList =  fileNameList.sortedByDescending {
                getFileCreateTimeByFileName(it)
            }
            return sortFileNameList.map {
                File(mLogPath + File.separator + it)
            }
        }
        return emptyList()
    }

}