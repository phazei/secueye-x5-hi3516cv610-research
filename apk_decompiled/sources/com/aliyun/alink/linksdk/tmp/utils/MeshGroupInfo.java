package com.aliyun.alink.linksdk.tmp.utils;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class MeshGroupInfo {
    private int count;
    private int groupAddress;
    private List<String> iotIds;

    public MeshGroupInfo(int i, int i2) {
        this.count = i;
        this.groupAddress = i2;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public int getGroupAddress() {
        return this.groupAddress;
    }

    public void setGroupAddress(int i) {
        this.groupAddress = i;
    }

    public List<String> getIotIds() {
        return this.iotIds;
    }

    public void setIotIds(List<String> list) {
        this.iotIds = list;
    }
}
