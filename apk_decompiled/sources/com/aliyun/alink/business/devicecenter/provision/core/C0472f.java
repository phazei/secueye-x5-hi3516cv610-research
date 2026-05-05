package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigStrategy;
import com.aliyun.alink.business.devicecenter.utils.DeviceInfoUtils;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.f, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BreezeConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0472f implements DeviceInfoUtils.IApiCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ C0473g f3697a;

    public C0472f(C0473g c0473g) {
        this.f3697a = c0473g;
    }

    @Override // com.aliyun.alink.business.devicecenter.utils.DeviceInfoUtils.IApiCallback
    public void onFail(int i, String str) {
        ALog.w(BreezeConfigStrategy.TAG, "pid returnTo Pk is fail");
    }

    @Override // com.aliyun.alink.business.devicecenter.utils.DeviceInfoUtils.IApiCallback
    public void onSuccess(Object obj) {
        if (obj != null) {
            try {
                String strValueOf = String.valueOf(JSON.parseObject(obj.toString()).get("productKey"));
                String str = BreezeConfigStrategy.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("pidReturnToPk productkey:");
                sb.append(strValueOf);
                ALog.d(str, sb.toString());
                if (TextUtils.isEmpty(strValueOf)) {
                    return;
                }
                this.f3697a.f3698a.mConfigParams.productKey = strValueOf;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
