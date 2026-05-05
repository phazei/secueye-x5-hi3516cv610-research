package a.a.a.a.b.l;

import aisble.BleManager;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.alibaba.ailabs.iot.mesh.callback.MeshMsgListener;
import com.alibaba.ailabs.iot.mesh.ut.IUserTracker;
import com.alibaba.ailabs.iot.mesh.ut.UtTraceInfo;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.spongycastle.util.encoders.Hex;

/* JADX INFO: compiled from: ControlMsgUtHelper.java */
/* JADX INFO: loaded from: classes.dex */
public class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final AtomicInteger f1481a = new AtomicInteger(0);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static long f1482b = 0;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static MeshMsgListener f1483c = new a();

    public static void b() {
        TgMeshManager.getInstance().registerMeshMessageListener(f1483c);
        if (a.a.a.a.b.d.a.f1315a) {
            d.b.a().a(IUserTracker.class, new d());
        }
    }

    public static void c() {
        IUserTracker iUserTracker = (IUserTracker) d.b.a().a(IUserTracker.class);
        if (iUserTracker == null) {
            a.a.a.a.b.m.a.d("ControlMsgUtUtil", "Null IUserTracker implement");
            return;
        }
        HashMap map = new HashMap();
        map.put("args", JSON.toJSONString(new HashMap(2)));
        iUserTracker.customHit("MeshSDK", "BeginGetAllAttr", map, "a21156.b21328984");
    }

    public static void d() {
        if (f1481a.get() > 5) {
            a.a.a.a.b.m.a.c("ControlMsgUtUtil", "broadcast too many times " + f1481a.get());
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (Math.abs(jCurrentTimeMillis - f1482b) < BleManager.CONNECTION_TIMEOUT_THRESHOLD) {
            a.a.a.a.b.m.a.c("ControlMsgUtUtil", "");
            return;
        }
        f1482b = jCurrentTimeMillis;
        f1481a.incrementAndGet();
        byte[] bArr = {a.a.a.a.b.m.f.a(), 31, -16};
        a.a.a.a.b.m.a.c("ControlMsgUtUtil", "sendGetAllAttrCmd parameters = " + Hex.toHexString(bArr) + ", opCode " + Integer.toHexString(13674497));
        TgMeshManager.getInstance().sendMessge(65535, 0, 0, 13674497, bArr, new b());
    }

    public static void a(String str, UtTraceInfo utTraceInfo) {
        a(str, a(utTraceInfo));
    }

    public static void a(UtTraceInfo utTraceInfo, int i, String str) {
        f.a().b(utTraceInfo.getUnicastAddress());
        HashMap<String, String> mapA = a(utTraceInfo);
        mapA.put("errorCode", String.valueOf(i));
        mapA.put(WifiProvisionUtConst.KEY_ERROR_MSG, str);
        a("SDKSendCmdError", mapA);
    }

    public static void a(int i, int i2, String str) {
        HashMap<String, String> mapA;
        UtTraceInfo utTraceInfoB = f.a().b(i);
        if (utTraceInfoB != null) {
            mapA = a(utTraceInfoB.getUtTraceId(), i, utTraceInfoB.getDeviceId(), utTraceInfoB.getProductKey());
        } else {
            mapA = a("", i, "", "");
        }
        mapA.put("errorCode", String.valueOf(i2));
        mapA.put(WifiProvisionUtConst.KEY_ERROR_MSG, str);
        a("SDKSendCmdError", mapA);
    }

    public static void a(int i, String str) {
        HashMap<String, String> mapA;
        UtTraceInfo utTraceInfoA = f.a().a(i);
        if (utTraceInfoA != null) {
            mapA = a(utTraceInfoA.getUtTraceId(), i, utTraceInfoA.getDeviceId(), utTraceInfoA.getProductKey());
        } else {
            mapA = a("", i, "", "");
        }
        mapA.put("deviceType", str);
        a("SDKPrepareSendToMesDevice", mapA);
    }

    public static void a(int i, int i2, boolean z) {
        UtTraceInfo utTraceInfoA;
        HashMap<String, String> mapA;
        if (z) {
            utTraceInfoA = f.a().b(i);
        } else {
            utTraceInfoA = f.a().a(i);
        }
        if (utTraceInfoA != null) {
            mapA = a(utTraceInfoA.getUtTraceId(), i, utTraceInfoA.getDeviceId(), utTraceInfoA.getProductKey());
        } else {
            mapA = a("", i, "", "");
        }
        mapA.put("reportResult", String.valueOf(i2));
        mapA.put("time", String.valueOf(new Date().getTime()));
        a("SDKReportDeviceStatus", mapA);
    }

    public static HashMap<String, String> a(UtTraceInfo utTraceInfo) {
        return a(utTraceInfo.getUtTraceId(), utTraceInfo.getUnicastAddress(), utTraceInfo.getDeviceId(), utTraceInfo.getProductKey());
    }

    public static HashMap<String, String> a(String str, int i, String str2, String str3) {
        HashMap<String, String> map = new HashMap<>(8);
        map.put("app_traceId", str);
        map.put("devAddress", String.valueOf(i));
        map.put("devId", str2);
        map.put(AlinkConstants.KEY_PK, str3);
        return map;
    }

    public static void a(String str, Map<String, String> map) {
        a("MeshSDK", str, map, "a21156.b21328984");
    }

    public static void a(String str, String str2, Map<String, String> map, String str3) {
        if (map == null) {
            map = new HashMap<>(8);
        }
        IUserTracker iUserTracker = (IUserTracker) d.b.a().a(IUserTracker.class);
        if (iUserTracker == null) {
            a.a.a.a.b.m.a.d("ControlMsgUtUtil", "Null IUserTracker implement");
            return;
        }
        HashMap map2 = new HashMap();
        map2.put("args", JSON.toJSONString(map));
        iUserTracker.customHit(str, str2, map2, str3);
    }

    public static void a(int i, String str, String str2, String str3) {
        IUserTracker iUserTracker = (IUserTracker) d.b.a().a(IUserTracker.class);
        if (iUserTracker == null) {
            a.a.a.a.b.m.a.d("ControlMsgUtUtil", "Null IUserTracker implement");
            return;
        }
        HashMap map = new HashMap();
        map.put("devAddress", String.valueOf(i));
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        map.put("devId", str);
        if (TextUtils.isEmpty(str2)) {
            str2 = "";
        }
        map.put(AlinkConstants.KEY_PK, str2);
        map.put("content", str3);
        HashMap map2 = new HashMap();
        map2.put("args", JSON.toJSONString(map));
        iUserTracker.customHit("MeshSDK", "ReceiveAllAttrResp", map2, "a21156.b21328984");
    }
}
