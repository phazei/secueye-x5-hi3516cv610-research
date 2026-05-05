package com.aliyun.iot.push.utils;

import com.alibaba.sdk.android.ams.common.logger.LoggerListener;
import com.taobao.accs.utl.ILogAdapter;

/* JADX INFO: loaded from: classes2.dex */
public class PushLogAdapter implements LoggerListener, ILogAdapter {
    @Override // com.alibaba.sdk.android.ams.common.logger.LoggerListener
    public void d(String str, String str2, Throwable th, int i) {
        d(str, str2 + th);
    }

    @Override // com.alibaba.sdk.android.ams.common.logger.LoggerListener
    public void i(String str, String str2, Throwable th, int i) {
        i(str, str2 + th);
    }

    @Override // com.alibaba.sdk.android.ams.common.logger.LoggerListener
    public void w(String str, String str2, Throwable th, int i) {
        w(str, str2 + th);
    }

    @Override // com.alibaba.sdk.android.ams.common.logger.LoggerListener
    public void e(String str, String str2, Throwable th, int i) {
        e(str, str2, th);
    }

    @Override // com.taobao.accs.utl.ILogAdapter
    public void v(String str, String str2) {
        ALog.d(str, str2);
    }

    @Override // com.taobao.accs.utl.ILogAdapter
    public void d(String str, String str2) {
        ALog.d(str, str2);
    }

    @Override // com.taobao.accs.utl.ILogAdapter
    public void i(String str, String str2) {
        ALog.i(str, str2);
    }

    @Override // com.taobao.accs.utl.ILogAdapter
    public void w(String str, String str2) {
        ALog.w(str, str2);
    }

    @Override // com.taobao.accs.utl.ILogAdapter
    public void e(String str, String str2) {
        ALog.e(str, str2);
    }

    @Override // com.taobao.accs.utl.ILogAdapter
    public void e(String str, String str2, Throwable th) {
        ALog.e(str, str2, th);
    }
}
