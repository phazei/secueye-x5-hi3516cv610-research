package com.aliyun.alink.business.devicecenter.discover.scanap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.discovery.DiscoveryType;
import com.aliyun.alink.business.devicecenter.api.discovery.IDeviceDiscoveryListener;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.cache.WiFiScanResultsCache;
import com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz;
import com.aliyun.alink.business.devicecenter.discover.annotation.DeviceDiscovery;
import com.aliyun.alink.business.devicecenter.discover.base.DiscoverChainBase;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.PermissionCheckerUtils;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import com.aliyun.alink.business.devicecenter.utils.WiFiUtils;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
@DeviceDiscovery(discoveryType = {DiscoveryType.SOFT_AP_DEVICE, DiscoveryType.BEACON_DEVICE})
public class SoftAPDiscoverChain extends DiscoverChainBase {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final int[] f3637c = {10, 10};

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final int[] f3638d = {10, 30, 80};
    public Context e;
    public IDeviceDiscoveryListener f;
    public ScheduledFuture g;
    public Future h;
    public AtomicBoolean i;
    public AtomicBoolean j;
    public AtomicInteger k;
    public int[] l;
    public AtomicBoolean m;
    public AtomicBoolean n;
    public Runnable o;
    public BroadcastReceiver p;

    public SoftAPDiscoverChain(Context context) {
        super(context);
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = new AtomicBoolean(false);
        this.j = new AtomicBoolean(false);
        this.k = new AtomicInteger(0);
        this.l = f3637c;
        this.m = new AtomicBoolean(false);
        this.n = new AtomicBoolean(false);
        this.o = new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.scanap.SoftAPDiscoverChain.3
            @Override // java.lang.Runnable
            public void run() {
                int i;
                try {
                    int i2 = SoftAPDiscoverChain.this.l[0];
                    SoftAPDiscoverChain.this.a(SoftAPDiscoverChain.this.e);
                    while (SoftAPDiscoverChain.this.i.get()) {
                        if (PermissionCheckerUtils.hasLocationAccess(SoftAPDiscoverChain.this.e)) {
                            boolean zStartScan = WiFiUtils.startScan(SoftAPDiscoverChain.this.e);
                            StringBuilder sb = new StringBuilder();
                            sb.append("scan runnable, scan started =");
                            sb.append(zStartScan);
                            ALog.d("SoftAPDiscoverChain", sb.toString());
                            SoftAPDiscoverChain.this.j.set(true);
                            i = SoftAPDiscoverChain.this.l[SoftAPDiscoverChain.this.k.getAndIncrement() % SoftAPDiscoverChain.this.l.length];
                        } else {
                            ALog.d("SoftAPDiscoverChain", "scan runnable, wait for location permission.");
                            i = 5;
                        }
                        try {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("sleep ");
                            sb2.append(i);
                            sb2.append(" before next scan.");
                            ALog.d("SoftAPDiscoverChain", sb2.toString());
                            Thread.sleep(i * 1000);
                        } catch (Exception unused) {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.p = new BroadcastReceiver() { // from class: com.aliyun.alink.business.devicecenter.discover.scanap.SoftAPDiscoverChain.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent == null) {
                    return;
                }
                if (!"android.net.wifi.SCAN_RESULTS".equals(intent.getAction())) {
                    ALog.d("SoftAPDiscoverChain", "action not equal to android.net.wifi.SCAN_RESULTS, ignore.");
                    return;
                }
                if (!SoftAPDiscoverChain.this.i.get()) {
                    ALog.d("SoftAPDiscoverChain", "scan has stopped, ignore to get scan results.");
                    return;
                }
                boolean booleanExtra = intent.getBooleanExtra("resultsUpdated", false);
                ALog.d("SoftAPDiscoverChain", "onReceive() scan started called with: c = [" + context2 + "], intent = [" + intent + "], success = [" + booleanExtra + "]");
                if (!booleanExtra) {
                    SoftAPDiscoverChain softAPDiscoverChain = SoftAPDiscoverChain.this;
                    softAPDiscoverChain.a(softAPDiscoverChain.f, 2, "BRStartFailed");
                } else if (Build.VERSION.SDK_INT < 27) {
                    SoftAPDiscoverChain softAPDiscoverChain2 = SoftAPDiscoverChain.this;
                    softAPDiscoverChain2.a(softAPDiscoverChain2.f, 2, "BRStartSuccessDelay");
                } else {
                    SoftAPDiscoverChain softAPDiscoverChain3 = SoftAPDiscoverChain.this;
                    softAPDiscoverChain3.a(softAPDiscoverChain3.f, 0, "BRStartSuccess");
                }
            }
        };
        this.e = context;
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.base.AbilityReceiver
    public void onNotify(Intent intent) {
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public void startDiscover(final IDeviceDiscoveryListener iDeviceDiscoveryListener) {
        ALog.d("SoftAPDiscoverChain", "startDiscover() called with: listener = [" + iDeviceDiscoveryListener + "]");
        this.f = iDeviceDiscoveryListener;
        if (this.i.get()) {
            ALog.i("SoftAPDiscoverChain", "scan started, return.");
            return;
        }
        this.k.set(0);
        this.i.set(true);
        if (Build.VERSION.SDK_INT > 27) {
            this.l = f3638d;
        } else {
            this.l = f3637c;
        }
        ThreadPool.submit(this.o);
        this.h = ThreadPool.schedule(new Callable<Object>() { // from class: com.aliyun.alink.business.devicecenter.discover.scanap.SoftAPDiscoverChain.1
            @Override // java.util.concurrent.Callable
            public Object call() throws Exception {
                if (SoftAPDiscoverChain.this.i.get()) {
                    SoftAPDiscoverChain.this.a(iDeviceDiscoveryListener, 0, "defaultDelayToScan");
                    return null;
                }
                ALog.d("SoftAPDiscoverChain", "isScanning = false, defaultDelayToScan return.");
                return null;
            }
        }, 5L, TimeUnit.SECONDS);
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public void stopDiscover() {
        ALog.d("SoftAPDiscoverChain", "stopDiscover() called");
        try {
            this.f = null;
            this.i.set(false);
            this.j.set(false);
            this.k.set(0);
            this.n.set(false);
            if (this.g != null) {
                this.g.cancel(true);
                this.g = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.h != null) {
                this.h.cancel(true);
                this.h = null;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            if (this.e == null || !this.m.get()) {
                return;
            }
            this.m.set(false);
            this.e.unregisterReceiver(this.p);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public final void a(final IDeviceDiscoveryListener iDeviceDiscoveryListener, int i, String str) {
        ALog.d("SoftAPDiscoverChain", "to getScanResult listener=" + iDeviceDiscoveryListener + ", initDelay=" + i + ", from=" + str);
        if (this.n.get()) {
            ALog.d("SoftAPDiscoverChain", "to getScanResult has started, return.");
        } else if (!this.i.get()) {
            ALog.d("SoftAPDiscoverChain", "to getScanResult has stopped, return.");
        } else {
            this.n.set(true);
            this.g = ThreadPool.scheduleAtFixedRate(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.scanap.SoftAPDiscoverChain.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (SoftAPDiscoverChain.this.j.get() && SoftAPDiscoverChain.this.i.get()) {
                            if (iDeviceDiscoveryListener == null) {
                                ALog.w("SoftAPDiscoverChain", "startScan not started. or listener=null.");
                                return;
                            }
                            List<ScanResult> scanResult = WiFiUtils.getScanResult(SoftAPDiscoverChain.this.e);
                            if (scanResult == null || scanResult.size() <= 1) {
                                return;
                            }
                            final ArrayList arrayList = new ArrayList();
                            final ArrayList arrayList2 = new ArrayList();
                            ArrayList arrayList3 = new ArrayList();
                            for (int i2 = 0; i2 < scanResult.size(); i2++) {
                                ScanResult scanResult2 = scanResult.get(i2);
                                if (scanResult2 != null && !TextUtils.isEmpty(scanResult2.SSID)) {
                                    String pkFromAp = AlinkHelper.getPkFromAp(scanResult2.SSID);
                                    if (!TextUtils.isEmpty(pkFromAp) || !scanResult2.SSID.startsWith(AlinkConstants.SOFT_AP_SSID_PREFIX)) {
                                        DeviceInfo deviceInfo = new DeviceInfo();
                                        deviceInfo.productKey = pkFromAp;
                                        deviceInfo.id = AlinkHelper.getMacFromAp(scanResult2.SSID);
                                        deviceInfo.setExtraDeviceInfo(AlinkConstants.KEY_APP_SSID, scanResult2.SSID);
                                        deviceInfo.setExtraDeviceInfo(AlinkConstants.KEY_APP_BSSID, scanResult2.BSSID);
                                        if (scanResult2.SSID.startsWith(AlinkConstants.MOCK_AP_SSID_PREFIX) && SoftAPDiscoverChain.this.discoveryTypes != null && SoftAPDiscoverChain.this.discoveryTypes.contains(DiscoveryType.BEACON_DEVICE)) {
                                            arrayList2.add(deviceInfo);
                                        } else if (AlinkHelper.isValidSoftAp(scanResult2.SSID, false) && SoftAPDiscoverChain.this.discoveryTypes != null && SoftAPDiscoverChain.this.discoveryTypes.contains(DiscoveryType.SOFT_AP_DEVICE)) {
                                            arrayList.add(deviceInfo);
                                        }
                                        arrayList3.add(scanResult2);
                                    }
                                }
                            }
                            if (SoftAPDiscoverChain.this.j.get()) {
                                if (arrayList.size() > 0 || arrayList2.size() > 0) {
                                    WiFiScanResultsCache.getInstance().updateCache(arrayList3);
                                    DeviceCenterBiz.getInstance().runOnUIThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.scanap.SoftAPDiscoverChain.2.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            if (iDeviceDiscoveryListener != null) {
                                                ArrayList arrayList4 = arrayList;
                                                if (arrayList4 != null && arrayList4.size() > 0) {
                                                    iDeviceDiscoveryListener.onDeviceFound(DiscoveryType.SOFT_AP_DEVICE, arrayList);
                                                }
                                                ArrayList arrayList5 = arrayList2;
                                                if (arrayList5 == null || arrayList5.size() <= 0) {
                                                    return;
                                                }
                                                iDeviceDiscoveryListener.onDeviceFound(DiscoveryType.BEACON_DEVICE, arrayList2);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, i, 2L, TimeUnit.SECONDS);
        }
    }

    public final void a(Context context) {
        ALog.d("SoftAPDiscoverChain", "registerScan() called with: context = [" + context + "], hasRegisteredReceiver=" + this.m);
        if (context != null) {
            try {
                if (this.m.compareAndSet(false, true)) {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
                    context.registerReceiver(this.p, intentFilter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public SoftAPDiscoverChain(Context context, EnumSet<DiscoveryType> enumSet) {
        super(context);
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = new AtomicBoolean(false);
        this.j = new AtomicBoolean(false);
        this.k = new AtomicInteger(0);
        this.l = f3637c;
        this.m = new AtomicBoolean(false);
        this.n = new AtomicBoolean(false);
        this.o = new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.scanap.SoftAPDiscoverChain.3
            @Override // java.lang.Runnable
            public void run() {
                int i;
                try {
                    int i2 = SoftAPDiscoverChain.this.l[0];
                    SoftAPDiscoverChain.this.a(SoftAPDiscoverChain.this.e);
                    while (SoftAPDiscoverChain.this.i.get()) {
                        if (PermissionCheckerUtils.hasLocationAccess(SoftAPDiscoverChain.this.e)) {
                            boolean zStartScan = WiFiUtils.startScan(SoftAPDiscoverChain.this.e);
                            StringBuilder sb = new StringBuilder();
                            sb.append("scan runnable, scan started =");
                            sb.append(zStartScan);
                            ALog.d("SoftAPDiscoverChain", sb.toString());
                            SoftAPDiscoverChain.this.j.set(true);
                            i = SoftAPDiscoverChain.this.l[SoftAPDiscoverChain.this.k.getAndIncrement() % SoftAPDiscoverChain.this.l.length];
                        } else {
                            ALog.d("SoftAPDiscoverChain", "scan runnable, wait for location permission.");
                            i = 5;
                        }
                        try {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("sleep ");
                            sb2.append(i);
                            sb2.append(" before next scan.");
                            ALog.d("SoftAPDiscoverChain", sb2.toString());
                            Thread.sleep(i * 1000);
                        } catch (Exception unused) {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.p = new BroadcastReceiver() { // from class: com.aliyun.alink.business.devicecenter.discover.scanap.SoftAPDiscoverChain.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent == null) {
                    return;
                }
                if (!"android.net.wifi.SCAN_RESULTS".equals(intent.getAction())) {
                    ALog.d("SoftAPDiscoverChain", "action not equal to android.net.wifi.SCAN_RESULTS, ignore.");
                    return;
                }
                if (!SoftAPDiscoverChain.this.i.get()) {
                    ALog.d("SoftAPDiscoverChain", "scan has stopped, ignore to get scan results.");
                    return;
                }
                boolean booleanExtra = intent.getBooleanExtra("resultsUpdated", false);
                ALog.d("SoftAPDiscoverChain", "onReceive() scan started called with: c = [" + context2 + "], intent = [" + intent + "], success = [" + booleanExtra + "]");
                if (!booleanExtra) {
                    SoftAPDiscoverChain softAPDiscoverChain = SoftAPDiscoverChain.this;
                    softAPDiscoverChain.a(softAPDiscoverChain.f, 2, "BRStartFailed");
                } else if (Build.VERSION.SDK_INT < 27) {
                    SoftAPDiscoverChain softAPDiscoverChain2 = SoftAPDiscoverChain.this;
                    softAPDiscoverChain2.a(softAPDiscoverChain2.f, 2, "BRStartSuccessDelay");
                } else {
                    SoftAPDiscoverChain softAPDiscoverChain3 = SoftAPDiscoverChain.this;
                    softAPDiscoverChain3.a(softAPDiscoverChain3.f, 0, "BRStartSuccess");
                }
            }
        };
        this.e = context;
        if (enumSet == null || enumSet.size() < 1) {
            return;
        }
        if (enumSet.contains(DiscoveryType.BEACON_DEVICE)) {
            addDiscoveryType(DiscoveryType.BEACON_DEVICE);
        }
        if (enumSet.contains(DiscoveryType.SOFT_AP_DEVICE)) {
            addDiscoveryType(DiscoveryType.SOFT_AP_DEVICE);
        }
    }
}
