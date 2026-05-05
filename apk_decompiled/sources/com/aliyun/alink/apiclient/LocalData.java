package com.aliyun.alink.apiclient;

import com.aliyun.alink.apiclient.model.DeviceAuthInfo;

/* JADX INFO: loaded from: classes.dex */
public class LocalData {
    private String authToken;
    private DeviceAuthInfo deviceData;

    private static class SingletonHolder {
        private static final LocalData INSTANCE = new LocalData();

        private SingletonHolder() {
        }
    }

    private LocalData() {
        this.deviceData = null;
        this.authToken = null;
    }

    public static LocalData getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void setDeviceData(DeviceAuthInfo deviceAuthInfo) {
        this.deviceData = deviceAuthInfo;
    }

    public void setAuthToken(String str) {
        this.authToken = str;
    }

    public DeviceAuthInfo getDeviceData() {
        return this.deviceData;
    }

    public String getAuthToken() {
        return this.authToken;
    }
}
