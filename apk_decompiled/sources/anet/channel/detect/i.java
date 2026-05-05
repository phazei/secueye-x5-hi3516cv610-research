package anet.channel.detect;

import anet.channel.RequestCb;
import anet.channel.bytes.ByteArray;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class i implements RequestCb {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ h f1707a;

    @Override // anet.channel.RequestCb
    public void onDataReceive(ByteArray byteArray, boolean z) {
    }

    i(h hVar) {
        this.f1707a = hVar;
    }

    @Override // anet.channel.RequestCb
    public void onResponseCode(int i, Map<String, List<String>> map) {
        this.f1707a.f1703a.reqErrorCode = i;
    }

    @Override // anet.channel.RequestCb
    public void onFinish(int i, String str, RequestStatistic requestStatistic) {
        ALog.i("anet.HorseRaceDetector", "LongLinkTask request finish", this.f1707a.f1705c, HiAnalyticsConstant.HaKey.BI_KEY_RESULT, Integer.valueOf(i), "msg", str);
        if (this.f1707a.f1703a.reqErrorCode == 0) {
            this.f1707a.f1703a.reqErrorCode = i;
        } else {
            this.f1707a.f1703a.reqRet = this.f1707a.f1703a.reqErrorCode == 200 ? 1 : 0;
        }
        this.f1707a.f1703a.reqTime = (System.currentTimeMillis() - this.f1707a.f1704b) + this.f1707a.f1703a.connTime;
        synchronized (this.f1707a.f1703a) {
            this.f1707a.f1703a.notify();
        }
    }
}
