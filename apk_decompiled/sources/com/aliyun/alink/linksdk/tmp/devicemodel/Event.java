package com.aliyun.alink.linksdk.tmp.devicemodel;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class Event {
    private String desc;
    private String identifier;
    private String method;
    private String name;
    private List<Arg> outputData;
    private String type;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String str) {
        this.desc = str;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String str) {
        this.method = str;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String str) {
        this.identifier = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public List<Arg> getOutputData() {
        return this.outputData;
    }

    public void setOutputData(List<Arg> list) {
        this.outputData = list;
    }
}
