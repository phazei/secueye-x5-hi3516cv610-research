package com.aliyun.alink.linksdk.tmp.device.panel.data;

/* JADX INFO: loaded from: classes2.dex */
public class StatusPayload {
    public int code;
    public StatusData data = new StatusData();
    public String id;
    public String message;

    public static class StatusData {
        public int status;
        public long time;
    }
}
