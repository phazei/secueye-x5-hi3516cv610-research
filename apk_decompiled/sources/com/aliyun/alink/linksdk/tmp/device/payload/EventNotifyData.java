package com.aliyun.alink.linksdk.tmp.device.payload;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class EventNotifyData {
    protected List<KeyValuePair> args;
    protected String name;

    public String getName() {
        return this.name;
    }

    public EventNotifyData setName(String str) {
        this.name = str;
        return this;
    }

    public List<KeyValuePair> getArgs() {
        return this.args;
    }

    public EventNotifyData setArgs(List<KeyValuePair> list) {
        this.args = list;
        return this;
    }
}
