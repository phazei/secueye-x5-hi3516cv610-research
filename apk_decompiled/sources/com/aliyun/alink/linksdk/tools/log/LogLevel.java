package com.aliyun.alink.linksdk.tools.log;

import com.alibaba.ailabs.iot.aisbase.plugin.ota.ReportProgressUtil;

/* JADX INFO: loaded from: classes2.dex */
public enum LogLevel {
    DEBUG("DBG"),
    INFO("INF"),
    WRAN("WAR"),
    ERROR(ReportProgressUtil.CODE_ERR);

    private String tag;

    LogLevel(String str) {
        this.tag = null;
        this.tag = str;
    }

    public String getTag() {
        return this.tag;
    }
}
