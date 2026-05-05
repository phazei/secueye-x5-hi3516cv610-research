package a.a.a.a.b.m;

import com.alibaba.ailabs.iot.mesh.ut.IUserTracker;
import com.alibaba.ailabs.iot.mesh.utils.AliMeshUUIDParserUtil;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.DeviceCommonConstants;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;
import com.taobao.accs.utl.BaseMonitor;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: BlinkUtUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f1498a = "b";

    public static void a(String str, String str2, String str3, String str4, long j, int i, String str5) {
        try {
            HashMap map = new HashMap(4);
            map.put(WifiProvisionUtConst.KEY_STEP, "error");
            map.put("channel", str2);
            map.put("productKey", str3);
            map.put("errorCode", Integer.valueOf(i));
            map.put(WifiProvisionUtConst.KEY_ERROR_MSG, str5);
            map.put("costTime", Long.valueOf(j));
            HashMap map2 = new HashMap(2);
            map2.put(WifiProvisionUtConst.KEY_BUSIZ_INFO, JSON.toJSONString(map));
            a(str, BaseMonitor.ALARM_POINT_CONNECT, map2, str4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void a(String str, String str2, String str3, boolean z, byte[] bArr, String str4) {
        try {
            HashMap map = new HashMap(4);
            map.put(WifiProvisionUtConst.KEY_STEP, "start");
            map.put("channel", str2);
            map.put("productKey", str3);
            if (bArr != null) {
                map.put(DeviceCommonConstants.KEY_DEVICE_ID, Utils.bytes2HexString(bArr));
                map.put("type", AliMeshUUIDParserUtil.getAliMeshSolutionTypeFromUUID(bArr));
            }
            map.put("batch_mode", Boolean.valueOf(z));
            map.put("type", AliMeshUUIDParserUtil.getAliMeshSolutionTypeFromUUID(bArr));
            HashMap map2 = new HashMap(2);
            map2.put(WifiProvisionUtConst.KEY_BUSIZ_INFO, JSON.toJSONString(map));
            a(str, WifiProvisionUtConst.ARG_CONNECTION, map2, str4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void a(String str, String str2, String str3, boolean z, byte[] bArr, String str4, long j) {
        try {
            HashMap map = new HashMap(4);
            map.put(WifiProvisionUtConst.KEY_STEP, "success");
            map.put("channel", str2);
            map.put("productKey", str3);
            if (bArr != null) {
                map.put(DeviceCommonConstants.KEY_DEVICE_ID, Utils.bytes2HexString(bArr));
                map.put("type", AliMeshUUIDParserUtil.getAliMeshSolutionTypeFromUUID(bArr));
            }
            map.put("batch_mode", Boolean.valueOf(z));
            map.put("errorCode", 0);
            map.put(WifiProvisionUtConst.KEY_ERROR_MSG, "");
            map.put("costTime", Long.valueOf(j));
            HashMap map2 = new HashMap(2);
            map2.put(WifiProvisionUtConst.KEY_BUSIZ_INFO, JSON.toJSONString(map));
            a(str, WifiProvisionUtConst.ARG_CONNECTION, map2, str4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void a(String str, String str2, String str3, boolean z, byte[] bArr, String str4, long j, int i, String str5, String str6, String str7) {
        try {
            HashMap map = new HashMap(4);
            map.put(WifiProvisionUtConst.KEY_STEP, "error");
            map.put("channel", str2);
            map.put("productKey", str3);
            if (bArr != null) {
                map.put(DeviceCommonConstants.KEY_DEVICE_ID, Utils.bytes2HexString(bArr));
                map.put("type", AliMeshUUIDParserUtil.getAliMeshSolutionTypeFromUUID(bArr));
            }
            map.put("batch_mode", Boolean.valueOf(z));
            map.put("errorCode", Integer.valueOf(i));
            map.put(WifiProvisionUtConst.KEY_ERROR_MSG, str5);
            map.put("costTime", Long.valueOf(j));
            map.put("subErrorCode", str6);
            map.put("subErrorMsg", str7);
            HashMap map2 = new HashMap(2);
            map2.put(WifiProvisionUtConst.KEY_BUSIZ_INFO, JSON.toJSONString(map));
            a(str, WifiProvisionUtConst.ARG_CONNECTION, map2, str4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void a(String str, String str2, String str3, boolean z, byte[] bArr, String str4, long j, int i, String str5) {
        try {
            HashMap map = new HashMap(4);
            map.put(WifiProvisionUtConst.KEY_STEP, "error");
            map.put("channel", str2);
            map.put("productKey", str3);
            if (bArr != null) {
                map.put(DeviceCommonConstants.KEY_DEVICE_ID, Utils.bytes2HexString(bArr));
                map.put("type", AliMeshUUIDParserUtil.getAliMeshSolutionTypeFromUUID(bArr));
            }
            map.put("batch_mode", Boolean.valueOf(z));
            map.put("errorCode", Integer.valueOf(i));
            map.put(WifiProvisionUtConst.KEY_ERROR_MSG, str5);
            map.put("costTime", Long.valueOf(j));
            HashMap map2 = new HashMap(2);
            map2.put(WifiProvisionUtConst.KEY_BUSIZ_INFO, JSON.toJSONString(map));
            a(str, WifiProvisionUtConst.ARG_CONNECTION, map2, str4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void a(String str, String str2, Map<String, String> map, String str3) {
        if (map == null) {
            map = new HashMap<>(8);
        }
        IUserTracker iUserTracker = (IUserTracker) d.b.a().a(IUserTracker.class);
        if (iUserTracker == null) {
            a.d(f1498a, "Null IUserTracker implement");
        } else {
            iUserTracker.customHit(str, str2, map, str3);
        }
    }
}
