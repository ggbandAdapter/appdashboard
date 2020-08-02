package cn.ggband.loglib.api;

import android.util.Log;
import android.webkit.JavascriptInterface;

import cn.ggband.loglib.AppdashboardKit;

import static cn.ggband.loglib.AppdashboardKit.LOGTAG;

/**
 * js 调用
 */
public class LogJsCallInterface extends Object{

    @JavascriptInterface
    public void upFileProgress(String progress) {
        Log.d(LOGTAG,"progress:"+progress);
    }

    @JavascriptInterface
    public void upFileFail(String msg) {
        Log.d(LOGTAG,"upFileFail:"+msg);
    }

    @JavascriptInterface
    public void upFileSuccess(String msg) {
        Log.d(LOGTAG,"upFileSuccess:"+msg);
    }

    @JavascriptInterface
    public void log(String msg){
        Log.d(LOGTAG,"log:"+msg);
    }

    @JavascriptInterface
    public String appId(){
        return AppdashboardKit.INSTANCE.getAppId();
    }

}
