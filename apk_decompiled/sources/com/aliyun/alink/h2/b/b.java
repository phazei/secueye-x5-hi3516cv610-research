package com.aliyun.alink.h2.b;

import android.util.Log;
import com.aliyun.alink.h2.utils.ILogger;

/* JADX INFO: compiled from: HLoger.java */
/* JADX INFO: loaded from: classes2.dex */
public class b implements ILogger {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f3819a;

    public b(String str) {
        this.f3819a = null;
        this.f3819a = str;
    }

    @Override // com.aliyun.alink.h2.utils.ILogger
    public void d(String str, String str2) {
        a(3, str, str2);
    }

    @Override // com.aliyun.alink.h2.utils.ILogger
    public void i(String str, String str2) {
        a(4, str, str2);
    }

    @Override // com.aliyun.alink.h2.utils.ILogger
    public void w(String str, String str2) {
        a(5, str, str2);
    }

    @Override // com.aliyun.alink.h2.utils.ILogger
    public void e(String str, String str2) {
        a(6, str, str2);
    }

    @Override // com.aliyun.alink.h2.utils.ILogger
    public void e(String str, String str2, Exception exc) {
        if (exc != null) {
            StringBuilder sb = new StringBuilder();
            if (str2 == null) {
                str2 = "";
            }
            sb.append(str2);
            sb.append(" EXCEPTION: ");
            sb.append(exc.getMessage());
            a(6, str, sb.toString());
            exc.printStackTrace();
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        if (str2 == null) {
            str2 = "";
        }
        sb2.append(str2);
        sb2.append(" EXCEPTION: unknown");
        a(6, str, sb2.toString());
    }

    private void a(int i, String str, String str2) {
        Log.println(i, this.f3819a + str, str2);
    }
}
