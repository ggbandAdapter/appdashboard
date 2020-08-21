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
        AppdashboardKit.mCustomUserAlias = "ggband"
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
            Thread {
                val isUpLoad = AppdashboardKit.apiHelper.upLogFile()
                Log.d("ggband", "isUpLoad:$isUpLoad")
            }.start()

        }

        btnCheckNewVersion.setOnClickListener {
            Thread {
                val result = AppdashboardKit.apiHelper.checkNewVersion()
                Log.d("ggband", "checkVersion:$result")
            }.start()
        }
        btnMockCash.setOnClickListener {
            throw NullPointerException("")
        }

        btnGetCash.setOnClickListener {
            Log.d("ggband", AppdashboardKit.apiHelper.getCashLogs().toString())

        }
        btnUpLoadCash.setOnClickListener {
            AppdashboardKit.apiHelper.upLoadCashLogs()
        }

    }
}
