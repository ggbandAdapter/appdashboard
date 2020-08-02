package cn.ggband.logrecord

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cn.ggband.loglib.AppdashboardKit
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppdashboardKit.setUserAlias("15390089473")
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
            AppdashboardKit.upLogFile()
        }

        btnCheckNewVersion.setOnClickListener {
            AppdashboardKit.checkNewVersion(0, 2)
        }

    }
}
