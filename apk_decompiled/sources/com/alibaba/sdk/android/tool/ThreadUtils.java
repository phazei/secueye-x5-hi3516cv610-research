package com.alibaba.sdk.android.tool;

import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class ThreadUtils {
    public static String dumpThread(Thread thread) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(String.format("Thread Name: '%s'\n", thread.getName()));
            sb.append(String.format(Locale.ENGLISH, "\"%s\" prio=%d tid=%d %s\n", thread.getName(), Integer.valueOf(thread.getPriority()), Long.valueOf(thread.getId()), thread.getState()));
            for (StackTraceElement stackTraceElement : thread.getStackTrace()) {
                sb.append(String.format("\tat %s\n", stackTraceElement.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void threadWaitForSec(double d2) {
        try {
            Thread.sleep(((long) d2) * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
