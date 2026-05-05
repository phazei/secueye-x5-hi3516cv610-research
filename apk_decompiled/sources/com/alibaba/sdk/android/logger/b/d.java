package com.alibaba.sdk.android.logger.b;

import android.util.Log;
import com.alibaba.sdk.android.logger.ILogger;
import com.alibaba.sdk.android.logger.LogLevel;

/* JADX INFO: loaded from: classes.dex */
public class d implements ILogger {
    @Override // com.alibaba.sdk.android.logger.ILogger
    public void print(LogLevel logLevel, String str, String str2) {
        String strSubstring;
        if (str2.length() > 4000) {
            strSubstring = str2.substring(4000);
            str2 = str2.substring(0, 4000);
        } else {
            strSubstring = null;
        }
        switch (logLevel) {
            case DEBUG:
                Log.d(str, str2);
                break;
            case INFO:
                Log.i(str, str2);
                break;
            case WARN:
                Log.w(str, str2);
                break;
            case ERROR:
                Log.e(str, str2);
                break;
        }
        if (strSubstring != null) {
            print(logLevel, str, strSubstring);
        }
    }
}
