package com.aliyun.alink.business.devicecenter.provision.other;

import android.net.NetworkInfo;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.other.softap.SoftAPConfigStrategy;
import com.aliyun.alink.business.devicecenter.utils.WiFiConnectiveUtils;

/* JADX INFO: compiled from: SoftAPConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class k implements WiFiConnectiveUtils.IWiFiConnectivityCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SoftAPConfigStrategy f3739a;

    public k(SoftAPConfigStrategy softAPConfigStrategy) {
        this.f3739a = softAPConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.utils.WiFiConnectiveUtils.IWiFiConnectivityCallback
    public void onWiFiStateChange(NetworkInfo networkInfo) {
        ALog.d(SoftAPConfigStrategy.TAG, "onWiFiStateChange() called with: networkInfo = [" + networkInfo + "]");
        try {
            if (this.f3739a.provisionHasStopped.get()) {
                ALog.d(SoftAPConfigStrategy.TAG, "provision stopped, ignore.");
            } else {
                this.f3739a.handleWiFiStateChange(networkInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ALog.w(SoftAPConfigStrategy.TAG, "handleWiFiStateChange exception=" + e);
        }
    }
}
