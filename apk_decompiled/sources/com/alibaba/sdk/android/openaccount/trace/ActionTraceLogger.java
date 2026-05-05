package com.alibaba.sdk.android.openaccount.trace;

import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.util.JSONUtils;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ActionTraceLogger {
    private final String action;
    private long beginTime;
    private Map<String, Object> infos;
    private final String module;

    ActionTraceLogger(String str, String str2) {
        this.module = str;
        this.action = str2;
    }

    public String getAction() {
        return this.action;
    }

    public ActionTraceLogger info(String str, Object obj) {
        if (this.infos == null) {
            this.infos = new LinkedHashMap();
        }
        this.infos.put(str, obj);
        return this;
    }

    public ActionTraceLogger infos(Map<String, ?> map) {
        if (map != null) {
            Map<String, Object> map2 = this.infos;
            if (map2 == null) {
                this.infos = new LinkedHashMap(map);
            } else {
                map2.putAll(map);
            }
        }
        return this;
    }

    public ActionTraceLogger begin() {
        this.beginTime = System.currentTimeMillis();
        AliSDKLogger.d(OpenAccountConstants.LOG_TAG, this.module, this.action, infoString());
        this.infos = null;
        return this;
    }

    public void done() {
        done(".Done");
    }

    private String infoString() {
        Map<String, Object> map = this.infos;
        if (map == null || map.size() <= 0) {
            return null;
        }
        return JSONUtils.toJson(this.infos);
    }

    private void done(boolean z) {
        if (z) {
            done(".Success");
        } else {
            done(".Failed");
        }
    }

    private void done(String str) {
        long j = 0;
        if (this.beginTime > 0) {
            long jCurrentTimeMillis = System.currentTimeMillis() - this.beginTime;
            if (jCurrentTimeMillis > 0) {
                if (this.infos == null) {
                    this.infos = new HashMap();
                }
                this.infos.put("caseTime", Long.valueOf(jCurrentTimeMillis));
                j = jCurrentTimeMillis;
            }
        }
        TraceLoggerManager.INSTANCE.log(OpenAccountConstants.LOG_TAG, 3, this.module, this.action + str, infoString());
        TraceLoggerManager.INSTANCE.actionCountTrack(this.module, this.action, true, (int) j);
        this.infos = null;
    }

    public long getCaseTime() {
        if (this.beginTime > 0) {
            return System.currentTimeMillis() - this.beginTime;
        }
        return 0L;
    }

    public void success() {
        done(true);
    }

    public void failed() {
        done(false);
    }

    public void failed(String str, Object obj) {
        info(str, obj);
        failed();
    }

    public void success(String str, Object obj) {
        info(str, obj);
        success();
    }
}
