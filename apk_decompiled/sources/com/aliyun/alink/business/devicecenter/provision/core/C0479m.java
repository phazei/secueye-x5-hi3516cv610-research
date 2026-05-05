package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.config.IDataCallback;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigStrategy;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.m, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BreezeConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0479m implements IDataCallback<String> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f3704a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ String f3705b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ BreezeConfigStrategy f3706c;

    public C0479m(BreezeConfigStrategy breezeConfigStrategy, int i, String str) {
        this.f3706c = breezeConfigStrategy;
        this.f3704a = i;
        this.f3705b = str;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IDataCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onResult(boolean z, String str) {
        ALog.d(BreezeConfigStrategy.TAG, "upload dev wifi log onResult() called with: success = [" + z + "], data = [" + str + "]");
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IDataCallback
    public void onState(String str, String str2) {
        ALog.d(BreezeConfigStrategy.TAG, "onState() called with: key = [" + str + "], value = [" + str2 + "]");
        if (this.f3706c.provisionHasStopped.get()) {
            ALog.w(BreezeConfigStrategy.TAG, "uploadData2Oss provision has stopped.");
            return;
        }
        if ("devOssKey".equals(str) || "ignored".equals(str)) {
            this.f3706c.provisionErrorInfo = new DCErrorCode("ProvisionFailFromDevice", DCErrorCode.PF_PROVISION_FAIL_FROM_DEVICE).setSubcode(this.f3704a).setMsg(this.f3705b).setExtra(this.f3706c.getExtraErrorInfo());
            if ("devOssKey".equals(str)) {
                this.f3706c.devWiFiMFromOssObjectName = str2;
            } else {
                this.f3706c.devWiFiMFromOssObjectName = null;
            }
            this.f3706c.provisionResultCallback(null);
            this.f3706c.stopConfig();
        }
    }
}
