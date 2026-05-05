package anet.channel.util;

import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.statist.NetTypeStat;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.thread.ThreadPoolExecutorFactory;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.android.netutil.UdpConnectType;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class c {
    public static final int IPV4_ONLY = 1;
    public static final int IPV6_ONLY = 2;
    public static final int IP_DUAL_STACK = 3;
    public static final int IP_STACK_UNKNOWN = 0;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    static volatile String f1940b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    static f f1941c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static final byte[][] f1939a = {new byte[]{-64, 0, 0, -86}, new byte[]{-64, 0, 0, -85}};

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    static ConcurrentHashMap<String, f> f1942d = new ConcurrentHashMap<>();
    static ConcurrentHashMap<String, Integer> e = new ConcurrentHashMap<>();

    public static boolean a() {
        return false;
    }

    static {
        f1940b = null;
        f1941c = null;
        try {
            f1941c = new f((Inet6Address) InetAddress.getAllByName("64:ff9b::")[0], 96);
            f1940b = b(NetworkStatusHelper.getStatus());
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String b(NetworkStatusHelper.NetworkStatus networkStatus) {
        if (networkStatus.isWifi()) {
            String wifiBSSID = NetworkStatusHelper.getWifiBSSID();
            if (TextUtils.isEmpty(wifiBSSID)) {
                wifiBSSID = "";
            }
            return "WIFI$" + wifiBSSID;
        }
        if (!networkStatus.isMobile()) {
            return "UnknownNetwork";
        }
        return networkStatus.getType() + "$" + NetworkStatusHelper.getApn();
    }

    public static boolean b() {
        Integer num = e.get(f1940b);
        return num != null && num.intValue() == 1;
    }

    public static int c() {
        Integer num = e.get(f1940b);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    public static f d() {
        f fVar = f1942d.get(f1940b);
        return fVar == null ? f1941c : fVar;
    }

    public static String a(Inet4Address inet4Address) throws Exception {
        if (inet4Address == null) {
            throw new InvalidParameterException("address in null");
        }
        f fVarD = d();
        if (fVarD == null) {
            throw new Exception("cannot get nat64 prefix");
        }
        byte[] address = inet4Address.getAddress();
        byte[] address2 = fVarD.f1947b.getAddress();
        int i = fVarD.f1946a / 8;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = i2 + i;
            if (i4 > 15 || i3 >= 4) {
                break;
            }
            if (i4 != 8) {
                address2[i4] = (byte) (address[i3] | address2[i4]);
                i3++;
            }
            i2++;
        }
        return InetAddress.getByAddress(address2).getHostAddress();
    }

    public static String a(String str) throws Exception {
        return a((Inet4Address) Inet4Address.getByName(str));
    }

    private static int h() throws SocketException {
        String str;
        int iIntValue;
        TreeMap treeMap = new TreeMap();
        Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
        while (true) {
            str = null;
            iIntValue = 0;
            if (!it.hasNext()) {
                break;
            }
            NetworkInterface networkInterface = (NetworkInterface) it.next();
            if (!networkInterface.getInterfaceAddresses().isEmpty()) {
                String displayName = networkInterface.getDisplayName();
                ALog.i("awcn.Inet64Util", "find NetworkInterface:" + displayName, null, new Object[0]);
                Iterator<InterfaceAddress> it2 = networkInterface.getInterfaceAddresses().iterator();
                int i = 0;
                while (it2.hasNext()) {
                    InetAddress address = it2.next().getAddress();
                    if (address instanceof Inet6Address) {
                        Inet6Address inet6Address = (Inet6Address) address;
                        if (!a(inet6Address)) {
                            ALog.e("awcn.Inet64Util", "Found IPv6 address:" + inet6Address.toString(), null, new Object[0]);
                            i |= 2;
                        }
                    } else if (address instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address;
                        if (!a((InetAddress) inet4Address) && !inet4Address.getHostAddress().startsWith("192.168.43.")) {
                            ALog.e("awcn.Inet64Util", "Found IPv4 address:" + inet4Address.toString(), null, new Object[0]);
                            i |= 1;
                        }
                    }
                }
                if (i != 0) {
                    treeMap.put(displayName.toLowerCase(), Integer.valueOf(i));
                }
            }
        }
        if (treeMap.isEmpty()) {
            return 0;
        }
        if (treeMap.size() == 1) {
            return ((Integer) treeMap.firstEntry().getValue()).intValue();
        }
        if (NetworkStatusHelper.getStatus().isWifi()) {
            str = "wlan";
        } else if (NetworkStatusHelper.getStatus().isMobile()) {
            str = "rmnet";
        }
        if (str != null) {
            Iterator it3 = treeMap.entrySet().iterator();
            while (true) {
                if (!it3.hasNext()) {
                    break;
                }
                Map.Entry entry = (Map.Entry) it3.next();
                if (((String) entry.getKey()).startsWith(str)) {
                    iIntValue = ((Integer) entry.getValue()).intValue();
                    break;
                }
            }
        }
        return (iIntValue == 2 && treeMap.containsKey("v4-wlan0")) ? iIntValue | ((Integer) treeMap.remove("v4-wlan0")).intValue() : iIntValue;
    }

    private static int i() {
        SpdyAgent.getInstance(GlobalAppRuntimeInfo.getContext(), SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
        int i = UdpConnectType.testUdpConnectIpv4() ? 1 : 0;
        return UdpConnectType.testUdpConnectIpv6() ? i | 2 : i;
    }

    public static void e() {
        if (!AwcnConfig.isIpv6Enable()) {
            ALog.e("awcn.Inet64Util", "[startIpStackDetect]ipv6Enable=false", null, new Object[0]);
            return;
        }
        f1940b = b(NetworkStatusHelper.getStatus());
        if (e.putIfAbsent(f1940b, 0) != null) {
            return;
        }
        int iJ = j();
        e.put(f1940b, Integer.valueOf(iJ));
        NetTypeStat netTypeStat = new NetTypeStat();
        netTypeStat.ipStackType = iJ;
        String str = f1940b;
        if (iJ == 2 || iJ == 3) {
            ThreadPoolExecutorFactory.submitScheduledTask(new d(str, netTypeStat), 1500L, TimeUnit.MILLISECONDS);
        } else if (GlobalAppRuntimeInfo.isTargetProcess()) {
            AppMonitor.getInstance().commitStat(netTypeStat);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    /* JADX WARN: Type inference failed for: r6v2, types: [java.lang.Object[]] */
    public static int j() {
        ?? IsIpStackDetectByUdpConnect;
        int iH;
        try {
            IsIpStackDetectByUdpConnect = AwcnConfig.isIpStackDetectByUdpConnect();
            try {
                if (IsIpStackDetectByUdpConnect != 0) {
                    String str = "udp_connect";
                    iH = i();
                    IsIpStackDetectByUdpConnect = str;
                } else {
                    String str2 = "interfaces";
                    iH = h();
                    IsIpStackDetectByUdpConnect = str2;
                }
            } catch (Throwable th) {
                th = th;
                ALog.e("awcn.Inet64Util", "[detectIpStack]error.", null, th, new Object[0]);
                iH = 0;
            }
        } catch (Throwable th2) {
            th = th2;
            IsIpStackDetectByUdpConnect = 0;
        }
        ALog.e("awcn.Inet64Util", "startIpStackDetect", null, new Object[]{"ip stack", Integer.valueOf(iH), "detectType", IsIpStackDetectByUdpConnect});
        return iH;
    }

    private static boolean a(InetAddress inetAddress) {
        return inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress() || inetAddress.isAnyLocalAddress();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static f k() throws UnknownHostException {
        InetAddress byName;
        boolean z;
        try {
            byName = InetAddress.getByName("ipv4only.arpa");
        } catch (Exception unused) {
            byName = null;
        }
        if (byName instanceof Inet6Address) {
            ALog.i("awcn.Inet64Util", "Resolved AAAA: " + byName.toString(), null, new Object[0]);
            byte[] address = byName.getAddress();
            if (address.length != 16) {
                return null;
            }
            int i = 12;
            while (true) {
                z = true;
                if (i < 0) {
                    z = false;
                    break;
                }
                byte b2 = address[i];
                byte[][] bArr = f1939a;
                if ((b2 & bArr[0][0]) != 0 && address[i + 1] == 0 && address[i + 2] == 0) {
                    int i2 = i + 3;
                    if (address[i2] == bArr[0][3] || address[i2] == bArr[1][3]) {
                        break;
                    }
                }
                i--;
            }
            if (z) {
                address[i + 3] = 0;
                address[i + 2] = 0;
                address[i + 1] = 0;
                address[i] = 0;
                return new f(Inet6Address.getByAddress("ipv4only.arpa", address, 0), i * 8);
            }
        } else if (byName instanceof Inet4Address) {
            ALog.i("awcn.Inet64Util", "Resolved A: " + byName.toString(), null, new Object[0]);
        }
        return null;
    }
}
