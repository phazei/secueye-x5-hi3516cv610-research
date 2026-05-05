package com.aliyun.alink.business.devicecenter.provision.other;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.discovery.DiscoveryType;
import com.aliyun.alink.business.devicecenter.api.discovery.IDeviceDiscoveryListener;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.cache.CacheCenter;
import com.aliyun.alink.business.devicecenter.cache.CacheType;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.other.softap.SoftAPConfigStrategy;
import java.util.List;

/* JADX INFO: compiled from: SoftAPConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class d implements IDeviceDiscoveryListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ SoftAPConfigStrategy f3732a;

    public d(SoftAPConfigStrategy softAPConfigStrategy) {
        this.f3732a = softAPConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.api.discovery.IDeviceDiscoveryListener
    public void onDeviceFound(DiscoveryType discoveryType, List<DeviceInfo> list) {
        ALog.d(SoftAPConfigStrategy.TAG, "onDeviceFound() called with: type = [" + discoveryType + "], foundDeviceList = [" + list + "]");
        try {
            if (this.f3732a.foundDeviceAp.get()) {
                ALog.d(SoftAPConfigStrategy.TAG, "have found to provision device.");
                return;
            }
            if (this.f3732a.mConfigParams == null) {
                ALog.d(SoftAPConfigStrategy.TAG, "softap provision has stopped.");
                return;
            }
            if (discoveryType != DiscoveryType.SOFT_AP_DEVICE || list == null || list.size() <= 0) {
                return;
            }
            DeviceInfo deviceInfo = null;
            for (int i = 0; i < list.size(); i++) {
                DeviceInfo deviceInfo2 = list.get(i);
                if (deviceInfo2 != null) {
                    CacheCenter cacheCenter = CacheCenter.getInstance();
                    CacheType cacheType = CacheType.SAP_PROVISIONED_SSID;
                    String[] strArr = new String[2];
                    strArr[0] = String.valueOf(deviceInfo2.getExtraDeviceInfo(AlinkConstants.KEY_APP_SSID));
                    strArr[1] = String.valueOf(deviceInfo2.getExtraDeviceInfo(AlinkConstants.KEY_APP_BSSID));
                    List cachedModel = cacheCenter.getCachedModel(cacheType, strArr);
                    if (cachedModel != null && !cachedModel.isEmpty()) {
                        String str = SoftAPConfigStrategy.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("found a match device ap.[provisioned] ");
                        sb.append(deviceInfo2);
                        ALog.d(str, sb.toString());
                        deviceInfo = deviceInfo2;
                    }
                    String str2 = SoftAPConfigStrategy.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("found to connect device ap. ");
                    sb2.append(deviceInfo2);
                    ALog.d(str2, sb2.toString());
                    deviceInfo = deviceInfo2;
                    break;
                }
            }
            if (deviceInfo == null) {
                ALog.d(SoftAPConfigStrategy.TAG, "no valid soft ap.");
                return;
            }
            if (TextUtils.isEmpty(this.f3732a.mConfigParams.productKey)) {
                String str3 = SoftAPConfigStrategy.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("No productKey, discover device AP, found match ap ");
                sb3.append(deviceInfo);
                ALog.i(str3, sb3.toString());
                this.f3732a.mConfigParams.productKey = deviceInfo.productKey;
                this.f3732a.mConfigParams.id = deviceInfo.id;
                this.f3732a.mConfigParams.deviceApSsid = String.valueOf(deviceInfo.getExtraDeviceInfo(AlinkConstants.KEY_APP_SSID));
                this.f3732a.foundDeviceAp.set(true);
                this.f3732a.stopDiscover();
                this.f3732a.getSSIDAndStartConnectDevAp();
                return;
            }
            if (TextUtils.isEmpty(deviceInfo.productKey)) {
                String str4 = SoftAPConfigStrategy.TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("user define wifi with no productKey, ignore pk check, found match ap ");
                sb4.append(deviceInfo);
                ALog.i(str4, sb4.toString());
                this.f3732a.mConfigParams.deviceApSsid = String.valueOf(deviceInfo.getExtraDeviceInfo(AlinkConstants.KEY_APP_SSID));
                this.f3732a.foundDeviceAp.set(true);
                this.f3732a.stopDiscover();
                this.f3732a.getSSIDAndStartConnectDevAp();
                return;
            }
            if (this.f3732a.mConfigParams.productKey.equals(deviceInfo.productKey)) {
                if (TextUtils.isEmpty(this.f3732a.mConfigParams.deviceName) || this.f3732a.mConfigParams.deviceName.equals(deviceInfo.deviceName)) {
                    String str5 = SoftAPConfigStrategy.TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("discover device AP, found match ap ");
                    sb5.append(deviceInfo);
                    ALog.i(str5, sb5.toString());
                    this.f3732a.mConfigParams.id = deviceInfo.id;
                    this.f3732a.mConfigParams.deviceApSsid = String.valueOf(deviceInfo.getExtraDeviceInfo(AlinkConstants.KEY_APP_SSID));
                    this.f3732a.foundDeviceAp.set(true);
                    this.f3732a.stopDiscover();
                    this.f3732a.getSSIDAndStartConnectDevAp();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
