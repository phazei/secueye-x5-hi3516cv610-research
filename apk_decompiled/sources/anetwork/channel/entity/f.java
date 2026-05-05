package anetwork.channel.entity;

import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.detect.n;
import anet.channel.statist.RequestMonitor;
import anet.channel.statist.RequestStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.util.ALog;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.aidl.ParcelableNetworkListener;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.stat.NetworkStat;
import anetwork.channel.util.RequestConstant;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class f implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ DefaultFinishEvent f2041a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ ParcelableNetworkListener f2042b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ c f2043c;

    f(c cVar, DefaultFinishEvent defaultFinishEvent, ParcelableNetworkListener parcelableNetworkListener) {
        this.f2043c = cVar;
        this.f2041a = defaultFinishEvent;
        this.f2042b = parcelableNetworkListener;
    }

    @Override // java.lang.Runnable
    public void run() {
        DefaultFinishEvent defaultFinishEvent = this.f2041a;
        String strOptString = null;
        if (defaultFinishEvent != null) {
            defaultFinishEvent.setContext(null);
        }
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            RequestStatistic requestStatistic = this.f2041a.rs;
            if (requestStatistic != null) {
                requestStatistic.rspCbStart = jCurrentTimeMillis;
                requestStatistic.lastProcessTime = jCurrentTimeMillis - requestStatistic.rspEnd;
                requestStatistic.oneWayTime = requestStatistic.retryCostTime + (jCurrentTimeMillis - requestStatistic.start);
                this.f2041a.getStatisticData().filledBy(requestStatistic);
            }
            this.f2042b.onFinished(this.f2041a);
            if (requestStatistic != null) {
                long jCurrentTimeMillis2 = System.currentTimeMillis();
                requestStatistic.rspCbEnd = jCurrentTimeMillis2;
                requestStatistic.callbackTime = jCurrentTimeMillis2 - jCurrentTimeMillis;
                anet.channel.fulltrace.a.a().commitRequest(requestStatistic.traceId, requestStatistic);
            }
            if (this.f2043c.f2031c != null) {
                this.f2043c.f2031c.writeEnd();
            }
            if (requestStatistic != null) {
                ALog.e("anet.Repeater", "[traceId:" + requestStatistic.traceId + "]end, " + requestStatistic.toString(), this.f2043c.f2030b, new Object[0]);
                CopyOnWriteArrayList<String> bucketInfo = GlobalAppRuntimeInfo.getBucketInfo();
                int i = 1;
                if (bucketInfo != null) {
                    int size = bucketInfo.size();
                    for (int i2 = 0; i2 < size - 1; i2 += 2) {
                        requestStatistic.putExtra(bucketInfo.get(i2), bucketInfo.get(i2 + 1));
                    }
                }
                if (GlobalAppRuntimeInfo.isAppBackground()) {
                    requestStatistic.putExtra("restrictBg", Integer.valueOf(NetworkStatusHelper.getRestrictBackgroundStatus()));
                }
                anet.channel.fulltrace.b sceneInfo = anet.channel.fulltrace.a.a().getSceneInfo();
                if (sceneInfo != null) {
                    ALog.i("anet.Repeater", sceneInfo.toString(), this.f2043c.f2030b, new Object[0]);
                    requestStatistic.sinceInitTime = requestStatistic.start - sceneInfo.f1752c;
                    requestStatistic.startType = sceneInfo.f1750a;
                    if (sceneInfo.f1750a != 1) {
                        requestStatistic.sinceLastLaunchTime = sceneInfo.f1752c - sceneInfo.f1753d;
                    }
                    requestStatistic.deviceLevel = sceneInfo.e;
                    if (!sceneInfo.f1751b) {
                        i = 0;
                    }
                    requestStatistic.isFromExternal = i;
                    requestStatistic.speedBucket = sceneInfo.f;
                    requestStatistic.abTestBucket = sceneInfo.g;
                }
                requestStatistic.serializeTransferTime = requestStatistic.reqServiceTransmissionEnd - requestStatistic.netReqStart;
                requestStatistic.userInfo = this.f2043c.e.a(RequestConstant.REQUEST_USER_INFO);
                AppMonitor.getInstance().commitStat(requestStatistic);
                if (NetworkConfigCenter.isRequestInMonitorList(requestStatistic)) {
                    AppMonitor.getInstance().commitStat(new RequestMonitor(requestStatistic));
                }
                try {
                    String str = requestStatistic.ip;
                    if (requestStatistic.extra != null) {
                        strOptString = requestStatistic.extra.optString("firstIp");
                    }
                    if (anet.channel.strategy.utils.c.b(str) || anet.channel.strategy.utils.c.b(strOptString)) {
                        AppMonitor.getInstance().commitStat(new RequestMonitor(requestStatistic));
                    }
                } catch (Exception unused) {
                }
                NetworkStat.getNetworkStat().put(this.f2043c.e.g(), this.f2041a.getStatisticData());
                n.a(requestStatistic);
            }
        } catch (Throwable unused2) {
        }
    }
}
