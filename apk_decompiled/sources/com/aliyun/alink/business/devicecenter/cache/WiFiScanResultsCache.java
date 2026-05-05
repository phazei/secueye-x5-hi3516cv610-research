package com.aliyun.alink.business.devicecenter.cache;

import android.net.wifi.ScanResult;
import android.text.TextUtils;
import android.util.LruCache;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class WiFiScanResultsCache implements ICache<ScanResult> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public LruCache<String, ScanResult> f3420a;

    private static class SingletonHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final WiFiScanResultsCache f3421a = new WiFiScanResultsCache();
    }

    public static WiFiScanResultsCache getInstance() {
        return SingletonHolder.f3421a;
    }

    public final String a(ScanResult scanResult) {
        if (scanResult == null || TextUtils.isEmpty(scanResult.SSID)) {
            return null;
        }
        return scanResult.SSID + scanResult.BSSID;
    }

    @Override // com.aliyun.alink.business.devicecenter.cache.ICache
    public void clearCache() {
        ALog.d("WiFiScanResultsCache", "clearCache() called.");
    }

    @Override // com.aliyun.alink.business.devicecenter.cache.ICache
    public void updateCache(List<ScanResult> list) {
        if (list == null || list.size() < 1 || this.f3420a == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ScanResult scanResult = list.get(i);
            if (scanResult != null && !TextUtils.isEmpty(scanResult.SSID)) {
                this.f3420a.put(a(scanResult), scanResult);
            }
        }
    }

    public WiFiScanResultsCache() {
        this.f3420a = null;
        this.f3420a = new LruCache<>(256);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.aliyun.alink.business.devicecenter.cache.ICache
    public ScanResult getCache(String... strArr) {
        Map<String, ScanResult> mapSnapshot;
        if (strArr == null || strArr.length < 2 || this.f3420a == null) {
            return null;
        }
        String str = strArr[0];
        String str2 = strArr[1];
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || (mapSnapshot = this.f3420a.snapshot()) == null) {
            return null;
        }
        for (Map.Entry<String, ScanResult> entry : mapSnapshot.entrySet()) {
            if (entry != null && entry.getKey() != null && entry.getValue() != null) {
                String pkFromAp = AlinkHelper.getPkFromAp(entry.getValue().SSID);
                String macFromAp = AlinkHelper.getMacFromAp(entry.getValue().SSID);
                if (str != null && str.equals(pkFromAp) && StringUtils.isEqualString(str2, macFromAp)) {
                    ALog.i("WiFiScanResultsCache", "find match cache scan result.");
                    return entry.getValue();
                }
                if (TextUtils.isEmpty(str) && StringUtils.isEqualString(str2, macFromAp)) {
                    ALog.i("WiFiScanResultsCache", "find match cache scan result with pk=null.");
                    return entry.getValue();
                }
            }
        }
        return null;
    }
}
