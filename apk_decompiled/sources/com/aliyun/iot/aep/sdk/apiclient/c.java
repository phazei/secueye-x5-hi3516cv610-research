package com.aliyun.iot.aep.sdk.apiclient;

import android.util.Log;

/* JADX INFO: compiled from: ALog.java */
/* JADX INFO: loaded from: classes2.dex */
public class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static byte f4564a = 1;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static d f4565b;

    public static void a(String str, String str2) {
        d dVar = f4565b;
        if (dVar != null) {
            dVar.a(str, str2);
        } else {
            a((byte) 1, str, str2);
        }
    }

    public static void b(String str, String str2) {
        d dVar = f4565b;
        if (dVar != null) {
            dVar.b(str, str2);
        } else {
            a((byte) 2, str, str2);
        }
    }

    public static void a(byte b2, String str, String str2) {
        if (f4564a > b2) {
        }
        switch (b2) {
            case 1:
                Log.d(str, str2);
                break;
            case 2:
                Log.i(str, str2);
                break;
            case 3:
                Log.w(str, str2);
                break;
            case 4:
                Log.e(str, str2);
                break;
        }
    }
}
