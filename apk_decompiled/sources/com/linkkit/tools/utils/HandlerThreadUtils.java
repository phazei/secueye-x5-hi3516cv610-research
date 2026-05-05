package com.linkkit.tools.utils;

import android.os.HandlerThread;
import android.os.Looper;

/* JADX INFO: loaded from: classes3.dex */
public class HandlerThreadUtils {
    private static final String TAG = "HandlerThread";
    private static String nameThread = "defaultName";
    private HandlerThread mHandlerThread;

    static class SingletonHolder {
        private static final HandlerThreadUtils INSTANCE = new HandlerThreadUtils();

        private SingletonHolder() {
        }
    }

    public static HandlerThreadUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private HandlerThreadUtils() {
        this.mHandlerThread = new HandlerThread(nameThread);
        this.mHandlerThread.start();
    }

    public Looper getLooper() {
        if (this.mHandlerThread == null) {
            this.mHandlerThread = new HandlerThread(nameThread);
        }
        Looper looper = this.mHandlerThread.getLooper();
        if (looper != null) {
            return looper;
        }
        this.mHandlerThread.start();
        return this.mHandlerThread.getLooper();
    }
}
