package cn.ggband.loglib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.ggband.loglib.api.LogJsCallInterface;
import cn.ggband.loglib.utils.CommUtils;


public class LogWebView extends WebView {
    public LogWebView(Context context) {
        super(context);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webSettings.setSafeBrowsingEnabled(false);
        }
        clearCache(true);
        addJavascriptInterface(new LogJsCallInterface(), "app");
    }

    public void load() {
        // loadUrl("http://47.93.250.227:8080/app.html");
        loadUrl("http://192.168.0.2:8080/app.html");

    }

    /**
     * 上传日志
     */
    public void upLogFile(String fileStr, byte softVersion,String fileName) {
        Map<String, Object> reqPair = new HashMap<>();
        reqPair.put("appVersionCode", CommUtils.INSTANCE.getAppVersionCode(getContext()));
        reqPair.put("appVersionName", CommUtils.INSTANCE.getAppVersionName(getContext()));
        reqPair.put("softVersion", softVersion);
        reqPair.put("logTag", AppdashboardKit.INSTANCE.getUserTag());
        reqPair.put("phoneModel", Build.MANUFACTURER + "-" + Build.MODEL);
        reqPair.put("fileStr", fileStr);
        reqPair.put("fileName",fileName);
        JSONObject jsonObject = new JSONObject(reqPair);
        callJs("upload", jsonObject.toString());
    }

    public void test() {
        callJs("test");
    }

    public void checkNewVersion(int versionCode, int softVersion) {
        Map<String, Object> reqPair = new HashMap<>();
        reqPair.put("versionCode", versionCode);
        reqPair.put("softVersion", softVersion);
        JSONObject jsonObject = new JSONObject(reqPair);
        callJs("checkNewVersion", jsonObject.toString());
    }


    public void callJs(String name) {
        loadUrl("javascript:" + name + "()");
    }

    public void callJs(String name, String args) {
        String url = "javascript:" + name + "('" + args + "')";
        loadUrl(url);
    }


}
