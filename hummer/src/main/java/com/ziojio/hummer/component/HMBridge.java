package com.ziojio.hummer.component;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.didi.hummer.annotation.Component;
import com.didi.hummer.annotation.JsMethod;
import androidz.util.AppUtil;

import java.util.HashMap;
import java.util.Map;

@Component("HM")
public class HMBridge {

    @JsMethod("getSystemInfo")
    public Map<String, Object> getDeviceInfo() {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @JsMethod("getAppInfo")
    public Map<String, Object> getAppInfo(Context context) {
        Map<String, Object> map = new HashMap<>();
        map.put("appName", AppUtil.getAppName());
        map.put("appVersionName", AppUtil.getVersionName());
        map.put("appVersionCode", AppUtil.getVersionCode());
        map.put("debug", AppUtil.isDebuggable());
        return map;
    }

    @JsMethod("showToast")
    public void showToast(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration >= 2000 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    @JsMethod("log")
    public void log(int priority, String tag, String msg) {
        if (tag == null || msg == null) {
            return;
        }
        if (priority < Log.VERBOSE || priority > Log.ASSERT) {
            priority = Log.WARN;
        }
        Log.println(priority, tag, msg);
    }

}
