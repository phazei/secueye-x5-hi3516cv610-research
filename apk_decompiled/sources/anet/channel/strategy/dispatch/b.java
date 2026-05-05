package anet.channel.strategy.dispatch;

import android.util.Base64InputStream;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.flow.FlowStat;
import anet.channel.flow.NetworkAnalysis;
import anet.channel.statist.AmdcStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.ConnEvent;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import com.taobao.accs.common.Constants;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HostnameVerifier;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static AtomicInteger f1883a = new AtomicInteger(0);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    static HostnameVerifier f1884b = new c();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    static Random f1885c = new Random();

    b() {
    }

    static List<IConnStrategy> a(String str) {
        List<IConnStrategy> connStrategyListByHost = Collections.EMPTY_LIST;
        if (!NetworkStatusHelper.isProxy()) {
            connStrategyListByHost = StrategyCenter.getInstance().getConnStrategyListByHost(DispatchConstants.getAmdcServerDomain());
            ListIterator<IConnStrategy> listIterator = connStrategyListByHost.listIterator();
            while (listIterator.hasNext()) {
                if (!listIterator.next().getProtocol().protocol.equalsIgnoreCase(str)) {
                    listIterator.remove();
                }
            }
        }
        return connStrategyListByHost;
    }

    public static void a(Map map) throws Throwable {
        IConnStrategy iConnStrategyRemove;
        String strA;
        if (map == null) {
            return;
        }
        String schemeByHost = AmdcRuntimeInfo.isForceHttps() ? HttpConstant.HTTPS : StrategyCenter.getInstance().getSchemeByHost(DispatchConstants.getAmdcServerDomain(), HttpConstant.HTTP);
        List<IConnStrategy> listA = a(schemeByHost);
        for (int i = 0; i < 3; i++) {
            HashMap map2 = new HashMap(map);
            if (i != 2) {
                iConnStrategyRemove = !listA.isEmpty() ? listA.remove(0) : null;
                if (iConnStrategyRemove != null) {
                    strA = a(schemeByHost, iConnStrategyRemove.getIp(), iConnStrategyRemove.getPort(), map2, i);
                } else {
                    strA = a(schemeByHost, (String) null, 0, map2, i);
                }
            } else {
                String[] amdcServerFixIp = DispatchConstants.getAmdcServerFixIp();
                if (amdcServerFixIp != null && amdcServerFixIp.length > 0) {
                    String strA2 = a(schemeByHost, amdcServerFixIp[f1885c.nextInt(amdcServerFixIp.length)], 0, map2, i);
                    iConnStrategyRemove = null;
                    strA = strA2;
                } else {
                    iConnStrategyRemove = null;
                    strA = a(schemeByHost, (String) null, 0, map2, i);
                }
            }
            int iA = a(strA, map2, i);
            if (iConnStrategyRemove != null) {
                ConnEvent connEvent = new ConnEvent();
                connEvent.isSuccess = iA == 0;
                StrategyCenter.getInstance().notifyConnEvent(DispatchConstants.getAmdcServerDomain(), iConnStrategyRemove, connEvent);
            }
            if (iA == 0 || iA == 2) {
                return;
            }
        }
    }

    private static String a(String str, String str2, int i, Map<String, String> map, int i2) {
        StringBuilder sb = new StringBuilder(64);
        if (!AmdcRuntimeInfo.isForceHttps() && i2 == 2 && HttpConstant.HTTPS.equalsIgnoreCase(str) && f1885c.nextBoolean()) {
            str = HttpConstant.HTTP;
        }
        sb.append(str);
        sb.append(HttpConstant.SCHEME_SPLIT);
        if (str2 != null) {
            if (anet.channel.util.c.a() && anet.channel.strategy.utils.c.a(str2)) {
                try {
                    str2 = anet.channel.util.c.a(str2);
                } catch (Exception unused) {
                }
            }
            if (anet.channel.strategy.utils.c.b(str2)) {
                sb.append('[');
                sb.append(str2);
                sb.append(']');
            } else {
                sb.append(str2);
            }
            if (i == 0) {
                i = HttpConstant.HTTPS.equalsIgnoreCase(str) ? Constants.PORT : 80;
            }
            sb.append(":");
            sb.append(i);
        } else {
            sb.append(DispatchConstants.getAmdcServerDomain());
        }
        sb.append(DispatchConstants.serverPath);
        TreeMap treeMap = new TreeMap();
        treeMap.put("appkey", map.remove("appkey"));
        treeMap.put("v", map.remove("v"));
        treeMap.put("platform", map.remove("platform"));
        sb.append('?');
        sb.append(anet.channel.strategy.utils.c.a(treeMap, "utf-8"));
        return sb.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:131:0x02ea A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:159:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int a(java.lang.String r19, java.util.Map r20, int r21) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 763
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.strategy.dispatch.b.a(java.lang.String, java.util.Map, int):int");
    }

    static String a(InputStream inputStream, boolean z) throws Throwable {
        Throwable th;
        IOException e;
        InputStream bufferedInputStream = new BufferedInputStream(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        if (z) {
            try {
                try {
                    bufferedInputStream = new GZIPInputStream(bufferedInputStream);
                } catch (IOException e2) {
                    e = e2;
                    ALog.e("awcn.DispatchCore", "", null, e, new Object[0]);
                    try {
                        bufferedInputStream.close();
                    } catch (IOException unused) {
                    }
                    return null;
                }
            } catch (Throwable th2) {
                th = th2;
                try {
                    bufferedInputStream.close();
                } catch (IOException unused2) {
                }
                throw th;
            }
        }
        Base64InputStream base64InputStream = new Base64InputStream(bufferedInputStream, 0);
        try {
            byte[] bArr = new byte[1024];
            while (true) {
                int i = base64InputStream.read(bArr);
                if (i == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, i);
            }
            String str = new String(byteArrayOutputStream.toByteArray(), "utf-8");
            try {
                base64InputStream.close();
            } catch (IOException unused3) {
            }
            return str;
        } catch (IOException e3) {
            e = e3;
            bufferedInputStream = base64InputStream;
            ALog.e("awcn.DispatchCore", "", null, e, new Object[0]);
            bufferedInputStream.close();
            return null;
        } catch (Throwable th3) {
            th = th3;
            bufferedInputStream = base64InputStream;
            bufferedInputStream.close();
            throw th;
        }
    }

    static void a(String str, String str2, URL url, int i, int i2) {
        if ((i2 != 1 || i == 2) && GlobalAppRuntimeInfo.isTargetProcess()) {
            try {
                AmdcStatistic amdcStatistic = new AmdcStatistic();
                amdcStatistic.errorCode = str;
                amdcStatistic.errorMsg = str2;
                if (url != null) {
                    amdcStatistic.host = url.getHost();
                    amdcStatistic.url = url.toString();
                }
                amdcStatistic.retryTimes = i;
                AppMonitor.getInstance().commitStat(amdcStatistic);
            } catch (Exception unused) {
            }
        }
    }

    static void a(String str, long j, long j2) {
        try {
            FlowStat flowStat = new FlowStat();
            flowStat.refer = "amdc";
            flowStat.protocoltype = HttpConstant.HTTP;
            flowStat.req_identifier = str;
            flowStat.upstream = j;
            flowStat.downstream = j2;
            NetworkAnalysis.getInstance().commitFlow(flowStat);
        } catch (Exception e) {
            ALog.e("awcn.DispatchCore", "commit flow info failed!", null, e, new Object[0]);
        }
    }
}
