package com.aliyun.alink.business.devicecenter.provision.other;

import android.net.NetworkInfo;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.other.softap.SoftAPConfigStrategy;
import com.aliyun.alink.business.devicecenter.provision.other.softap.SoftApState;
import com.aliyun.alink.business.devicecenter.utils.PermissionCheckerUtils;
import com.aliyun.alink.business.devicecenter.utils.WiFiUtils;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: SoftAPConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class m implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AtomicInteger f3741a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ SoftAPConfigStrategy f3742b;

    public m(SoftAPConfigStrategy softAPConfigStrategy, AtomicInteger atomicInteger) {
        this.f3742b = softAPConfigStrategy;
        this.f3741a = atomicInteger;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f3742b.provisionHasStopped.get() || this.f3742b.mConfigParams == null) {
            return;
        }
        this.f3741a.incrementAndGet();
        if (this.f3742b.hasNotifiedUser2ConnectDevAp.get() && PermissionCheckerUtils.hasLocationAccess(this.f3742b.mContext) && this.f3742b.softApState.ordinal() < SoftApState.CONNECTED_DEV_AP.ordinal() && (this.f3742b.mCurrentWiFiState == NetworkInfo.State.CONNECTED || WiFiUtils.isWiFiConnected(this.f3742b.mContext))) {
            String wifiSsid = AlinkHelper.getWifiSsid(this.f3742b.mContext);
            if (!TextUtils.isEmpty(wifiSsid)) {
                if (this.f3742b.isDeviceApConnected("\"" + wifiSsid + "\"")) {
                    this.f3742b.mDeviceApSsid = wifiSsid;
                    ALog.i(SoftAPConfigStrategy.TAG, "call onWiFiConnected in startPatch, maybe get ssid failed when wifi connected.");
                    this.f3742b.onWiFiConnected();
                    return;
                }
            }
        }
        if (this.f3741a.get() == 1 && !this.f3742b.android10plus() && this.f3742b.needReconnectSoftAp() && !TextUtils.isEmpty(this.f3742b.mDeviceApBssid)) {
            this.f3742b.stopDiscover();
            SoftAPConfigStrategy softAPConfigStrategy = this.f3742b;
            softAPConfigStrategy.connectDeviceAp(softAPConfigStrategy.mDeviceApSsid, this.f3742b.mDeviceApBssid, null);
            return;
        }
        if (this.f3741a.get() == 2 && !this.f3742b.android10plus() && this.f3742b.needReconnectSoftAp()) {
            this.f3742b.needReconnectSoftApAB.set(false);
            this.f3742b.stopDiscover();
            SoftAPConfigStrategy softAPConfigStrategy2 = this.f3742b;
            softAPConfigStrategy2.notifyConnectApByUser(softAPConfigStrategy2.mConfigParams.productKey, null);
            return;
        }
        if (!this.f3742b.needRecoverWifi() || this.f3742b.recvSwitchAPAckTime.get() <= 0 || this.f3742b.recvSwitchAPAckTime.get() + 10000 > System.currentTimeMillis()) {
            return;
        }
        if (WiFiUtils.isNetworkAvaiable(this.f3742b.mContext) && this.f3742b.isConnectedWiFiValid()) {
            return;
        }
        this.f3742b.notifyUser2RecoverWiFi(true);
    }
}
