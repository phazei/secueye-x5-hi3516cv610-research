package com.aliyun.alink.linksdk.channel.gateway.api.subdevice;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayChannel;

/* JADX INFO: loaded from: classes2.dex */
public class SubDeviceInfo {
    public String deviceName;
    public SubDeviceLoginState loginState = SubDeviceLoginState.OFFLINE;
    public String productKey;
    public String resetFlag;

    public SubDeviceInfo() {
    }

    public SubDeviceInfo(String str, String str2) {
        this.productKey = str;
        this.deviceName = str2;
    }

    public SubDeviceInfo(String str, String str2, String str3) {
        this.productKey = str;
        this.deviceName = str2;
        this.resetFlag = str3;
    }

    public boolean checkValid() {
        return (TextUtils.isEmpty(this.productKey) || TextUtils.isEmpty(this.deviceName)) ? false : true;
    }

    public String getDeviceId() {
        return this.productKey + GatewayChannel.DID_SEPARATOR + this.deviceName;
    }

    public String toString() {
        return "SubDeviceInfo{productKey='" + this.productKey + "', deviceName='" + this.deviceName + "', resetFlag='" + this.resetFlag + "', loginState=" + this.loginState + '}';
    }
}
