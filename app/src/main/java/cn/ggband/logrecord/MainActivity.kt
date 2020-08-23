package cn.ggband.logrecord

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.ggband.loglib.AppdashboardKit
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppdashboardKit.mUserTag = "ggband"
        setOnClickListener()
    }

    private fun setOnClickListener() {
        createLog.setOnClickListener {
            Log.d(
                "ggband",
                "currentTime:" + SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                ).format(Date())
            )
        }
        createIMsLog.setOnClickListener {
            Log.i(
                "ims",
                "currentTime:" + SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                ).format(Date())
            )

        }

        btnUpLogFile.setOnClickListener {
            runThread {
                val isUpLoad = AppdashboardKit.apiHelper.upLogFile()
                Log.d("ggband", "isUpLoad:$isUpLoad")
            }
        }

        btnCheckNewVersion.setOnClickListener {
            runThread {
                val result = AppdashboardKit.apiHelper.checkNewVersion()
                Log.d("ggband", "checkVersion:$result")
            }
        }
        btnMockCash.setOnClickListener {
            throw NullPointerException("")
        }

        btnGetCash.setOnClickListener {
            runThread {
                Log.d("ggband", AppdashboardKit.apiHelper.getCashLogs().toString())
            }
        }
        btnUpLoadCash.setOnClickListener {
            runThread {
                AppdashboardKit.apiHelper.upLoadCashLogs()
            }
        }
    }

    private fun runThread(block: () -> Unit) {
        Thread {
            block()
        }.start()
    }
}
