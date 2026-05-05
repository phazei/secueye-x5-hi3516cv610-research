package com.alibaba.ailabs.iot.aisbase.callback;

import com.alibaba.ailabs.iot.aisbase.C0465y;
import com.alibaba.ailabs.iot.aisbase.RequestManage;
import com.alibaba.ailabs.iot.aisbase.UTLogUtils;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.ReportProgressUtil;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.huawei.hms.push.constant.RemoteMessageConst;

/* JADX INFO: loaded from: classes.dex */
public class OtaActionListener implements IOTAPlugin.IOTAActionListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2556a = "OtaActionListener";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public IOTAPlugin.IOTAActionListener f2557b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public BluetoothDeviceWrapper f2558c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f2559d;
    public String e;
    public String f;
    public String g;
    public final String h = ReportProgressUtil.CODE_OK;
    public final String i = ReportProgressUtil.CODE_ERR;
    public final String j = ReportProgressUtil.TAG_START;
    public final String k = "FINISH";

    public OtaActionListener(IOTAPlugin.IOTAActionListener iOTAActionListener, BluetoothDeviceWrapper bluetoothDeviceWrapper, String str, String str2, String str3, String str4) {
        this.f2557b = iOTAActionListener;
        this.f2558c = bluetoothDeviceWrapper;
        this.g = str;
        this.f2559d = str2;
        this.e = str3;
        this.f = str4;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IOTAActionListener
    public void onFailed(int i, String str) {
        IOTAPlugin.IOTAActionListener iOTAActionListener = this.f2557b;
        if (iOTAActionListener != null) {
            iOTAActionListener.onFailed(i, str);
        }
        BluetoothDeviceWrapper bluetoothDeviceWrapper = this.f2558c;
        if (bluetoothDeviceWrapper != null) {
            UTLogUtils.updateBusInfo("ota", UTLogUtils.buildDeviceInfo(bluetoothDeviceWrapper), UTLogUtils.buildOtaBusInfo("error", 0, i, str));
        }
        a("FINISH", ReportProgressUtil.CODE_ERR, str + OpenAccountUIConstants.UNDER_LINE + i);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IOTAActionListener
    public void onProgress(int i, int i2) {
        IOTAPlugin.IOTAActionListener iOTAActionListener = this.f2557b;
        if (iOTAActionListener != null) {
            iOTAActionListener.onProgress(i, i2);
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IOTAActionListener
    public void onStateChanged(IOTAPlugin.OTAState oTAState) {
        IOTAPlugin.IOTAActionListener iOTAActionListener = this.f2557b;
        if (iOTAActionListener != null) {
            iOTAActionListener.onStateChanged(oTAState);
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin.IOTAActionListener
    public void onSuccess(String str) {
        IOTAPlugin.IOTAActionListener iOTAActionListener = this.f2557b;
        if (iOTAActionListener != null) {
            iOTAActionListener.onSuccess(str);
        }
        UTLogUtils.updateBusInfo("ota", UTLogUtils.buildDeviceInfo(this.f2558c), UTLogUtils.buildOtaBusInfo("success", 0, 0, ""));
        this.e = str;
        a("FINISH", ReportProgressUtil.CODE_OK, "");
    }

    public final void a(String str, String str2, String str3) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("currentVersion", (Object) this.e);
        jSONObject.put("targetVersion", (Object) this.f);
        jSONObject.put(RemoteMessageConst.Notification.TAG, (Object) str);
        jSONObject.put("code", (Object) str2);
        jSONObject.put("message", (Object) str3);
        RequestManage.getInstance().gmaOtaProgressReport(this.g, this.f2559d, jSONObject.toJSONString(), new C0465y(this));
    }
}
