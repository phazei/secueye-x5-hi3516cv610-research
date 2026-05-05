package com.aliyun.alink.business.devicecenter.provision.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.config.phoneap.AlinkAESHelper;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.utils.AlinkWifiSolutionUtils;
import com.aliyun.alink.business.devicecenter.utils.DictionaryEncryptionUtils;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: compiled from: AlinkP2PProvision.java */
/* JADX INFO: loaded from: classes2.dex */
public class Z {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static String f3686a = "AlinkP2PConfigStrategy";
    public Context k;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f3687b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f3688c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f3689d = null;
    public BroadcastReceiver e = null;
    public WifiP2pManager f = null;
    public WifiP2pManager.Channel g = null;
    public AtomicBoolean h = new AtomicBoolean(false);
    public int i = 0;
    public String j = null;
    public DCAlibabaConfigParams l = null;

    public Z(Context context) {
        this.k = null;
        this.k = context;
    }

    public final void h() {
        ALog.d(f3686a, "unRegisterP2PReceiver(),call");
        try {
            if (this.e != null) {
                ALog.d(f3686a, "unRegisterP2PReceiver(),exe");
                this.k.unregisterReceiver(this.e);
                this.e = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ALog.w(f3686a, "unRegisterP2PReceiver(),error" + e);
        }
    }

    public final void i() {
        ALog.d(f3686a, "p2p unSupportMethod()");
    }

    public void a(DCAlibabaConfigParams dCAlibabaConfigParams) {
        if (dCAlibabaConfigParams == null) {
            ALog.d(f3686a, "provision params null.");
            return;
        }
        this.l = dCAlibabaConfigParams;
        this.j = this.l.productEncryptKey;
        if (this.h.compareAndSet(false, true)) {
            b();
            c();
        }
    }

    public final void b() {
        if (this.f == null) {
            this.f = (WifiP2pManager) this.k.getSystemService("wifip2p");
            WifiP2pManager wifiP2pManager = this.f;
            Context context = this.k;
            this.g = wifiP2pManager.initialize(context, context.getMainLooper(), null);
        }
    }

    public final void c() {
        String strA = a(this.l.ssid, this.l.password, true);
        if (TextUtils.isEmpty(strA)) {
            ALog.w(f3686a, "startProvosion(),data is empty");
            i();
        } else {
            e();
            this.f3689d = strA;
            PerformanceLog.trace(f3686a, AlinkConstants.KEY_BROADCAST, PerformanceLog.getJsonObject("type", "p2p"));
            a(strA);
        }
    }

    public final void d() {
        ALog.d(f3686a, "registerP2PReceiver(),call,originName=" + this.f3687b);
        if (this.e == null) {
            ALog.d(f3686a, "registerP2PReceiver(),exe");
            this.e = new V(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
            Context context = this.k;
            if (context != null) {
                context.registerReceiver(this.e, intentFilter);
            }
        }
    }

    public final void e() {
        d();
    }

    public final void f() {
        ALog.d(f3686a, "stopExposeData()");
        try {
            if (this.f == null || Build.VERSION.SDK_INT < 16) {
                return;
            }
            this.f.stopPeerDiscovery(this.g, new Y(this));
        } catch (Exception e) {
            e.printStackTrace();
            ALog.w(f3686a, "stopExposeData(),error" + e);
        }
    }

    public void g() {
        ALog.d(f3686a, "stopProvosion(),call");
        try {
            this.f3687b = "wifi";
            if (this.h.compareAndSet(true, false)) {
                a(this.f3687b);
                f();
            }
            this.i = 0;
            h();
        } catch (Exception e) {
            e.printStackTrace();
            ALog.w(f3686a, "stop error," + e);
        }
    }

    public final String a(String str, String str2, boolean z) {
        byte[] encode;
        try {
            byte[] bytes = str.getBytes("UTF-8");
            if (!TextUtils.isEmpty(str2) && z) {
                byte[] bArrEncrypt128CFB = AlinkAESHelper.encrypt128CFB(str2, this.j);
                ALog.d(f3686a, "packetDataForP2P(), passwd encrypted data = ");
                AlinkWifiSolutionUtils.printByteArray(bArrEncrypt128CFB);
                encode = AlinkWifiSolutionUtils.eightBitsToSevenBits(bArrEncrypt128CFB);
                ALog.d(f3686a, "packetDataForP2P(), passwd encrypted 8->7 data = ");
                AlinkWifiSolutionUtils.printByteArray(encode);
            } else {
                encode = DictionaryEncryptionUtils.getEncode(str2.getBytes("UTF-8"));
            }
            int length = bytes.length + 3 + encode.length + a(encode);
            if (length > 32) {
                String str3 = f3686a;
                StringBuilder sb = new StringBuilder();
                sb.append("packetDataForP2P(), too long, length = ");
                sb.append(length);
                ALog.d(str3, sb.toString());
                return null;
            }
            byte[] bArr = new byte[bytes.length + 3 + encode.length];
            byte length2 = (byte) bytes.length;
            if (z) {
                length2 = (byte) (length2 | 32);
            }
            bArr[0] = length2;
            byte[] encode2 = DictionaryEncryptionUtils.getEncode(bytes);
            int i = 0;
            int i2 = 1;
            while (i < encode2.length) {
                bArr[i2] = encode2[i];
                i++;
                i2++;
            }
            int i3 = 0;
            while (i3 < encode.length) {
                bArr[i2] = encode[i3];
                i3++;
                i2++;
            }
            short s = 0;
            for (int i4 = 0; i4 < i2; i4++) {
                s = (short) (s + (bArr[i4] & 255));
            }
            int i5 = i2 + 1;
            bArr[i2] = (byte) ((s >> 6) & 63);
            int i6 = i5 + 1;
            bArr[i5] = (byte) (s & 63);
            int i7 = i6 - 2;
            if ((bArr[i7] & 255) == 0) {
                bArr[i7] = 1;
            }
            int i8 = i6 - 1;
            if ((bArr[i8] & 255) == 0) {
                bArr[i8] = 1;
            }
            String str4 = f3686a;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("packetDataForP2P(), deviceNameHexString = ");
            sb2.append(AlinkWifiSolutionUtils.bytesToHexString(bArr));
            ALog.i(str4, sb2.toString());
            AlinkWifiSolutionUtils.printByteArray(bArr);
            String str5 = new String(bArr, "UTF-8");
            String str6 = f3686a;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("packetDataForP2P(), deviceName = ");
            sb3.append(str5);
            ALog.d(str6, sb3.toString());
            this.i = bArr.length;
            byte[] bytes2 = str5.getBytes("UTF-8");
            if (this.i != (bytes2 != null ? bytes2.length : 0)) {
                ALog.d(f3686a, "packetDataForP2P(),UTF8 断层.");
            }
            return str5;
        } catch (Exception e) {
            ALog.w(f3686a, "packetDataForP2P(),error.");
            e.printStackTrace();
            return null;
        }
    }

    public final int a(byte[] bArr) {
        if (bArr.length == 0) {
            return 0;
        }
        int i = 0;
        for (byte b2 : bArr) {
            if ((b2 & 255) == 0) {
                i++;
            }
        }
        ALog.d(f3686a, "count0InByte,count=" + i);
        return i;
    }

    public final void a(String str) {
        try {
            String str2 = f3686a;
            StringBuilder sb = new StringBuilder();
            sb.append("changeDeviceName(),name");
            sb.append(str);
            ALog.d(str2, sb.toString());
            this.f.getClass().getMethod("setDeviceName", WifiP2pManager.Channel.class, String.class, WifiP2pManager.ActionListener.class).invoke(this.f, this.g, str, new W(this, str));
        } catch (NoSuchMethodException unused) {
            ALog.w(f3686a, "p2p unSupportMethod changeDeviceName() NoSuchMethodException.");
            i();
        } catch (Exception e) {
            ALog.w(f3686a, "p2p unSupportMethod changeDeviceName() catch error." + e);
            e.printStackTrace();
        }
    }

    public final void a() {
        ALog.d(f3686a, "exposeData()");
        this.f.discoverPeers(this.g, new X(this));
    }
}
