package com.taobao.accs.data;

import android.text.TextUtils;
import anet.channel.appmonitor.AppMonitor;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.ut.monitor.AssembleMonitor;
import com.taobao.accs.utl.ALog;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class a {
    public static final int SPLITTED_DATA_INDEX = 17;
    public static final int SPLITTED_DATA_MD5 = 18;
    public static final int SPLITTED_DATA_NUMS = 16;
    public static final int SPLITTED_TIME_OUT = 15;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final char[] f6306a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f6307b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f6308c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f6309d;
    private long e;
    private ScheduledFuture<?> g;
    private volatile int f = 0;
    private Map<Integer, byte[]> h = new TreeMap(new b(this));

    public a(String str, int i, String str2) {
        this.f6307b = str;
        this.f6308c = i;
        this.f6309d = str2;
    }

    public void a(long j) {
        if (j <= 0) {
            j = 30000;
        }
        this.g = ThreadPoolExecutorFactory.getScheduledExecutor().schedule(new c(this), j, TimeUnit.MILLISECONDS);
    }

    public byte[] a(int i, int i2, byte[] bArr) {
        long jCurrentTimeMillis;
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d("AssembleMessage", "putBurst", Constants.KEY_DATA_ID, this.f6307b, FirebaseAnalytics.Param.INDEX, Integer.valueOf(i));
        }
        if (i2 != this.f6308c) {
            ALog.e("AssembleMessage", "putBurst fail as burstNums not match", new Object[0]);
            return null;
        }
        if (i < 0 || i >= i2) {
            ALog.e("AssembleMessage", "putBurst fail as burstIndex invalid", new Object[0]);
            return null;
        }
        synchronized (this) {
            if (this.f == 0) {
                if (this.h.get(Integer.valueOf(i)) != null) {
                    ALog.e("AssembleMessage", "putBurst fail as exist old", new Object[0]);
                    return null;
                }
                if (this.h.isEmpty()) {
                    this.e = System.currentTimeMillis();
                }
                this.h.put(Integer.valueOf(i), bArr);
                if (this.h.size() == this.f6308c) {
                    byte[] bArr2 = null;
                    for (byte[] bArr3 : this.h.values()) {
                        if (bArr2 == null) {
                            bArr2 = bArr3;
                        } else {
                            byte[] bArr4 = new byte[bArr2.length + bArr3.length];
                            System.arraycopy(bArr2, 0, bArr4, 0, bArr2.length);
                            System.arraycopy(bArr3, 0, bArr4, bArr2.length, bArr3.length);
                            bArr2 = bArr4;
                        }
                    }
                    if (!TextUtils.isEmpty(this.f6309d)) {
                        String str = new String(a(com.taobao.accs.utl.b.a(bArr2)));
                        if (!this.f6309d.equals(str)) {
                            ALog.w("AssembleMessage", "putBurst fail", Constants.KEY_DATA_ID, this.f6307b, "dataMd5", this.f6309d, "finalDataMd5", str);
                            this.f = 3;
                            bArr2 = null;
                        }
                    }
                    long length = 0;
                    if (bArr2 != null) {
                        length = bArr2.length;
                        jCurrentTimeMillis = System.currentTimeMillis() - this.e;
                        this.f = 2;
                        ALog.i("AssembleMessage", "putBurst completed", Constants.KEY_DATA_ID, this.f6307b, "length", Long.valueOf(length), "cost", Long.valueOf(jCurrentTimeMillis));
                    } else {
                        jCurrentTimeMillis = 0;
                    }
                    AssembleMonitor assembleMonitor = new AssembleMonitor(this.f6307b, String.valueOf(this.f));
                    assembleMonitor.assembleLength = length;
                    assembleMonitor.assembleTimes = jCurrentTimeMillis;
                    AppMonitor.getInstance().commitStat(assembleMonitor);
                    this.h.clear();
                    if (this.g != null) {
                        this.g.cancel(false);
                    }
                    return bArr2;
                }
            } else {
                ALog.e("AssembleMessage", "putBurst fail", "status", Integer.valueOf(this.f));
            }
            return null;
        }
    }

    private static char[] a(byte[] bArr) {
        int length = bArr.length;
        char[] cArr = new char[length << 1];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            char[] cArr2 = f6306a;
            cArr[i] = cArr2[(bArr[i2] & 240) >>> 4];
            i = i3 + 1;
            cArr[i3] = cArr2[bArr[i2] & 15];
        }
        return cArr;
    }
}
