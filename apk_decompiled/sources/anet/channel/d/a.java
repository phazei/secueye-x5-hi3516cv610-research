package anet.channel.d;

import anet.channel.util.ALog;
import com.taobao.tlog.adapter.AdapterForTLog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a implements ALog.ILog {
    private int a(char c2) {
        switch (c2) {
            case 'D':
                return 1;
            case 'E':
                return 4;
            case 'I':
                return 2;
            case 'V':
                return 0;
            case 'W':
                return 3;
            default:
                return 5;
        }
    }

    @Override // anet.channel.util.ALog.ILog
    public void setLogLevel(int i) {
    }

    @Override // anet.channel.util.ALog.ILog
    public void d(String str, String str2) {
        AdapterForTLog.logd(str, str2);
    }

    @Override // anet.channel.util.ALog.ILog
    public void i(String str, String str2) {
        AdapterForTLog.logi(str, str2);
    }

    @Override // anet.channel.util.ALog.ILog
    public void w(String str, String str2) {
        AdapterForTLog.logw(str, str2);
    }

    @Override // anet.channel.util.ALog.ILog
    public void w(String str, String str2, Throwable th) {
        AdapterForTLog.logw(str, str2, th);
    }

    @Override // anet.channel.util.ALog.ILog
    public void e(String str, String str2) {
        AdapterForTLog.loge(str, str2);
    }

    @Override // anet.channel.util.ALog.ILog
    public void e(String str, String str2, Throwable th) {
        AdapterForTLog.loge(str, str2, th);
    }

    @Override // anet.channel.util.ALog.ILog
    public boolean isPrintLog(int i) {
        return i >= a(AdapterForTLog.getLogLevel().charAt(0));
    }

    @Override // anet.channel.util.ALog.ILog
    public boolean isValid() {
        return AdapterForTLog.isValid();
    }
}
