package com.alibaba.sdk.android.openaccount.trace;

import android.util.Log;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.ut.UserTrackerService;
import com.alibaba.sdk.android.openaccount.util.TraceHelper;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.aliyun.linksdk.alcs.AlcsConstant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes.dex */
public class TraceLoggerManager {
    public static final TraceLoggerManager INSTANCE = new TraceLoggerManager();
    private volatile long actionCountTraceTime;

    @Autowired
    private UserTrackerService userTrackerService;
    private int logLevel = 7;
    private UncaughtExceptionTraceHandler uncaughtExceptionTraceHandler = new UncaughtExceptionTraceHandler();
    private final Map<String, Map<String, AtomicInteger[]>> actionCountTrack = new LinkedHashMap();
    private final ReadWriteLock actionCountTrackLock = new ReentrantReadWriteLock();
    private volatile String msgHeaderBuffer = "";

    private TraceLoggerManager() {
    }

    public synchronized void init() {
        try {
            updateMsgHeader(TraceHelper.clientTTID, this.userTrackerService.getDefaultUserTrackerId());
            this.uncaughtExceptionTraceHandler.init(OpenAccountSDK.getAndroidContext());
        } catch (Exception e) {
            Log.w(OpenAccountConstants.LOG_TAG, e);
        }
    }

    public int getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(int i) {
        this.logLevel = i;
    }

    public synchronized void release() {
        sendActionCountTrack(true);
        this.actionCountTrackLock.writeLock().lock();
        try {
            this.actionCountTrack.clear();
        } finally {
            this.actionCountTrackLock.writeLock().unlock();
        }
    }

    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }

    public void log(String str, int i, String str2, String str3, String str4) {
        if (i < this.logLevel) {
            return;
        }
        logCat(str, i, str2, str3, str4);
    }

    public void log(int i, String str, String str2, String str3) {
        logCat(OpenAccountConstants.LOG_TAG, i, str, str2, str3);
    }

    private void logCat(String str, int i, String str2, String str3, String str4) {
        Log.println(i, str, buildMessage(str2, str3, str4).toString());
    }

    private CharSequence buildMessage(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder(this.msgHeaderBuffer);
        buildStrHeader(sb, str);
        if (str2 != null) {
            sb.append(str2);
        }
        if (str3 != null) {
            sb.append(" : ");
            sb.append(str3);
        }
        return sb;
    }

    private void updateMsgHeader(CharSequence... charSequenceArr) {
        this.msgHeaderBuffer = buildStrHeader(new StringBuilder(), charSequenceArr).toString();
    }

    private Appendable buildStrHeader(Appendable appendable, CharSequence... charSequenceArr) {
        try {
            for (CharSequence charSequence : charSequenceArr) {
                appendable.append('[');
                if (charSequence != null) {
                    appendable.append(charSequence);
                }
                appendable.append(']');
            }
        } catch (Exception e) {
            Log.w(OpenAccountConstants.LOG_TAG, e);
        }
        return appendable;
    }

    public ActionTraceLogger action(String str) {
        return new ActionTraceLogger(AlcsConstant.METHOD_DOMAIN_CORE, str);
    }

    public ActionTraceLogger action(String str, String str2) {
        return new ActionTraceLogger(str, str2);
    }

    public void actionCountTrack(String str, String str2, boolean z, int i) {
        this.actionCountTrackLock.readLock().lock();
        try {
            Map<String, AtomicInteger[]> map = this.actionCountTrack.get(str);
            AtomicInteger[] atomicIntegerArr = map != null ? map.get(str2) : null;
            this.actionCountTrackLock.readLock().unlock();
            if (atomicIntegerArr == null) {
                this.actionCountTrackLock.writeLock().lock();
                try {
                    Map<String, AtomicInteger[]> map2 = this.actionCountTrack.get(str);
                    if (map2 == null) {
                        map2 = new HashMap<>();
                        this.actionCountTrack.put(str, map2);
                    } else {
                        atomicIntegerArr = map2.get(str2);
                    }
                    if (atomicIntegerArr == null) {
                        AtomicInteger[] atomicIntegerArr2 = new AtomicInteger[4];
                        for (int i2 = 0; i2 < atomicIntegerArr2.length; i2++) {
                            atomicIntegerArr2[i2] = new AtomicInteger();
                        }
                        map2.put(str2, atomicIntegerArr2);
                        atomicIntegerArr = atomicIntegerArr2;
                    }
                } finally {
                    this.actionCountTrackLock.writeLock().unlock();
                }
            }
            if (z) {
                atomicIntegerArr[0].incrementAndGet();
                atomicIntegerArr[1].addAndGet(i);
            } else {
                atomicIntegerArr[2].incrementAndGet();
                atomicIntegerArr[3].addAndGet(i);
            }
            sendActionCountTrack(false);
        } catch (Throwable th) {
            this.actionCountTrackLock.readLock().unlock();
            throw th;
        }
    }

    private void sendActionCountTrack(boolean z) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if ((z || jCurrentTimeMillis - this.actionCountTraceTime >= 60000) && this.userTrackerService != null) {
            try {
                this.actionCountTrackLock.readLock().lock();
                try {
                    for (Map.Entry<String, Map<String, AtomicInteger[]>> entry : this.actionCountTrack.entrySet()) {
                        String key = entry.getKey();
                        for (Map.Entry<String, AtomicInteger[]> entry2 : entry.getValue().entrySet()) {
                            AtomicInteger[] value = entry2.getValue();
                            if (value[0].get() > 0 || value[2].get() > 0) {
                                String key2 = entry2.getKey();
                                HashMap map = new HashMap();
                                map.put("success", String.valueOf(value[0]));
                                map.put("successTime", String.valueOf(value[1]));
                                map.put("failed", String.valueOf(value[2]));
                                map.put("failedTime", String.valueOf(value[3]));
                                this.userTrackerService.sendCustomHit(key2, 60L, key, map);
                                log(3, AgooConstants.MESSAGE_TRACE, "ActionCount", key2);
                            }
                        }
                    }
                    this.actionCountTrackLock.readLock().unlock();
                    this.actionCountTrackLock.writeLock().lock();
                    try {
                        this.actionCountTrack.clear();
                        this.actionCountTrackLock.writeLock().unlock();
                        this.actionCountTraceTime = jCurrentTimeMillis;
                    } catch (Throwable th) {
                        this.actionCountTrackLock.writeLock().unlock();
                        throw th;
                    }
                } catch (Throwable th2) {
                    this.actionCountTrackLock.readLock().unlock();
                    throw th2;
                }
            } catch (Exception e) {
                Log.w(OpenAccountConstants.LOG_TAG, e);
            }
        }
    }
}
