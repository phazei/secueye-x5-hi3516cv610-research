package anet.channel.detect;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.statist.MtuDetectStat;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.util.ALog;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import org.android.netutil.PingEntry;
import org.android.netutil.PingResponse;
import org.android.netutil.PingTask;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class k {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static HashMap<String, Long> f1710a = new HashMap<>();

    k() {
    }

    public void a() {
        NetworkStatusHelper.addStatusChangeListener(new l(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        PingResponse pingResponse;
        if (!AwcnConfig.isNetworkDetectEnable()) {
            ALog.i("anet.MTUDetector", "network detect closed.", null, new Object[0]);
            return;
        }
        ALog.i("anet.MTUDetector", "mtuDetectTask start", null, new Object[0]);
        SpdyAgent.getInstance(GlobalAppRuntimeInfo.getContext(), SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        Long l = f1710a.get(str);
        if (l == null || jCurrentTimeMillis >= l.longValue()) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(GlobalAppRuntimeInfo.getContext());
            long j = defaultSharedPreferences.getLong("sp_mtu_" + str, 0L);
            if (jCurrentTimeMillis < j) {
                f1710a.put(str, Long.valueOf(j));
                ALog.i("anet.MTUDetector", "mtuDetectTask in period of validity", null, new Object[0]);
                return;
            }
            List<IConnStrategy> connStrategyListByHost = StrategyCenter.getInstance().getConnStrategyListByHost("guide-acs.m.taobao.com");
            String ip = (connStrategyListByHost == null || connStrategyListByHost.isEmpty()) ? null : connStrategyListByHost.get(0).getIp();
            if (TextUtils.isEmpty(ip)) {
                return;
            }
            String str2 = ip;
            Future futureLaunch = new PingTask(str2, 1000, 3, 0, 0).launch();
            Future<PingResponse> futureLaunch2 = new PingTask(str2, 1000, 3, 1172, 0).launch();
            Future<PingResponse> futureLaunch3 = new PingTask(str2, 1000, 3, 1272, 0).launch();
            Future<PingResponse> futureLaunch4 = new PingTask(str2, 1000, 3, 1372, 0).launch();
            Future<PingResponse> futureLaunch5 = new PingTask(str2, 1000, 3, 1432, 0).launch();
            try {
                pingResponse = (PingResponse) futureLaunch.get();
            } catch (Exception unused) {
                pingResponse = null;
            }
            if (pingResponse == null) {
                return;
            }
            if (pingResponse.getSuccessCnt() < 2) {
                ALog.e("anet.MTUDetector", "MTU detect preTask error", null, "errCode", Integer.valueOf(pingResponse.getErrcode()), "successCount", Integer.valueOf(pingResponse.getSuccessCnt()));
                return;
            }
            a(1200, futureLaunch2);
            a(1300, futureLaunch3);
            a(1400, futureLaunch4);
            a(1460, futureLaunch5);
            long j2 = jCurrentTimeMillis + 432000000;
            f1710a.put(str, Long.valueOf(j2));
            defaultSharedPreferences.edit().putLong("sp_mtu_" + str, j2).apply();
        }
    }

    private void a(int i, Future<PingResponse> future) {
        PingResponse pingResponse;
        try {
            pingResponse = future.get();
        } catch (Exception unused) {
            pingResponse = null;
        }
        if (pingResponse == null) {
            return;
        }
        int successCnt = pingResponse.getSuccessCnt();
        int i2 = 3 - successCnt;
        StringBuilder sb = new StringBuilder();
        PingEntry[] results = pingResponse.getResults();
        int length = results.length;
        for (int i3 = 0; i3 < length; i3++) {
            sb.append(results[i3].rtt);
            if (i3 != length - 1) {
                sb.append(",");
            }
        }
        ALog.i("anet.MTUDetector", "MTU detect result", null, "mtu", Integer.valueOf(i), "successCount", Integer.valueOf(successCnt), "timeoutCount", Integer.valueOf(i2));
        MtuDetectStat mtuDetectStat = new MtuDetectStat();
        mtuDetectStat.mtu = i;
        mtuDetectStat.pingSuccessCount = successCnt;
        mtuDetectStat.pingTimeoutCount = i2;
        mtuDetectStat.rtt = sb.toString();
        mtuDetectStat.errCode = pingResponse.getErrcode();
        AppMonitor.getInstance().commitStat(mtuDetectStat);
    }
}
