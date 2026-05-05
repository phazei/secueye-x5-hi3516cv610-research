package com.linkkit.tools.log;

import android.util.Log;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;

/* JADX INFO: loaded from: classes3.dex */
public class LoggerImpl implements ILogger {
    private int logLevel = 3;
    private final String preTag;

    public LoggerImpl(String str) {
        this.preTag = str;
    }

    @Override // com.linkkit.tools.log.ILogger
    public void setLogLevel(int i) {
        if (i < 4) {
            this.logLevel = i;
        } else if (i > 6) {
            this.logLevel = 6;
        } else {
            this.logLevel = i;
        }
    }

    @Override // com.linkkit.tools.log.ILogger
    public void d(String str, String str2) {
        log(3, str, str2, null);
    }

    @Override // com.linkkit.tools.log.ILogger
    public void i(String str, String str2) {
        log(4, str, str2, null);
    }

    @Override // com.linkkit.tools.log.ILogger
    public void w(String str, String str2) {
        log(5, str, str2, null);
    }

    @Override // com.linkkit.tools.log.ILogger
    public void w(String str, String str2, Throwable th) {
        log(5, str, str2, th);
    }

    @Override // com.linkkit.tools.log.ILogger
    public void e(String str, String str2) {
        log(6, str, str2, null);
    }

    @Override // com.linkkit.tools.log.ILogger
    public void e(String str, String str2, Throwable th) {
        log(6, str, str2, th);
    }

    @Override // com.linkkit.tools.log.ILogger
    public void llog(int i, String str, String str2, Throwable th) {
        if (str2 != null) {
            int length = str2.length();
            int i2 = 0;
            while (i2 < length - 900) {
                int i3 = i2 + 900;
                log(i, str, str2.substring(i2, i3), null);
                i2 = i3;
            }
            log(i, str, str2.substring(i2), null);
        }
    }

    private void log(int i, String str, String str2, Throwable th) {
        if (i >= this.logLevel) {
            Log.println(i, this.preTag + str, str2 + SdkConstant.CLOUDAPI_LF + Log.getStackTraceString(th));
        }
    }
}
