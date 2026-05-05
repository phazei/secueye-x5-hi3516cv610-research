package a.a.a.a.b.c;

import a.a.a.a.b.na;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.alibaba.ailabs.iot.mesh.callback.DeviceOnlineStatusListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.android.multiendinonebridge.IUpstreamProxy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.DeviceCommonConstants;
import datasource.bean.ConfigurationData;
import datasource.bean.IoTGatewayEvent;
import datasource.bean.Sigmesh;
import datasource.bean.SubscribeGroupAddr;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: compiled from: MultiEndinOneProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class c implements IUpstreamProxy {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f1306a = "" + c.class.getSimpleName();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f1307b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public na f1308c;
    public Map<String, b> e = new HashMap();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public List<DeviceOnlineStatusListener> f1309d = new CopyOnWriteArrayList();

    /* JADX INFO: compiled from: MultiEndinOneProxy.java */
    private final class a implements b {
        public a() {
        }

        @Override // a.a.a.a.b.c.c.b
        public void handle(String str, String str2) {
            JSONObject object = JSON.parseObject(str2);
            if (a.a.a.a.b.d.a.f1315a) {
                object.put("type", (Object) "TmallApp");
                object.put("connectType", (Object) "app-accs");
            } else {
                object.put("type", (Object) "IotxApp");
                object.put("connectType", (Object) "iotx-mqtt");
                object.put(DeviceCommonConstants.KEY_DEVICE_ID, (Object) c.this.f1307b);
            }
            IoTGatewayEvent ioTGatewayEvent = new IoTGatewayEvent();
            IoTGatewayEvent.EventBean eventBean = new IoTGatewayEvent.EventBean();
            eventBean.setName(str);
            eventBean.setNamespace("AliGenie.SmartHome");
            eventBean.setPayload(object);
            ioTGatewayEvent.setEvent(eventBean);
            if (c.this.f1308c != null) {
                c.this.f1308c.a(str, ioTGatewayEvent);
            }
        }

        public /* synthetic */ a(c cVar, a.a.a.a.b.c.a aVar) {
            this();
        }
    }

    /* JADX INFO: compiled from: MultiEndinOneProxy.java */
    private interface b {
        void handle(String str, String str2);
    }

    /* JADX INFO: renamed from: a.a.a.a.b.c.c$c, reason: collision with other inner class name */
    /* JADX INFO: compiled from: MultiEndinOneProxy.java */
    private class C0003c implements b {
        public C0003c() {
        }

        @Override // a.a.a.a.b.c.c.b
        public void handle(String str, String str2) {
            TgMeshManager.DevOnlineStatus devOnlineStatus;
            JSONObject object = JSON.parseObject(str2);
            String string = object.getString(AlinkConstants.KEY_SUB_DEVICE_ID);
            boolean zBooleanValue = object.getBoolean("isOnline").booleanValue();
            for (DeviceOnlineStatusListener deviceOnlineStatusListener : c.this.f1309d) {
                if (zBooleanValue) {
                    try {
                        devOnlineStatus = TgMeshManager.DevOnlineStatus.DEV_ST_ONLINE;
                    } catch (Exception e) {
                        a.a.a.a.b.m.a.b(c.f1306a, e.toString());
                    }
                } else {
                    devOnlineStatus = TgMeshManager.DevOnlineStatus.DEV_ST_OFFLINE;
                }
                deviceOnlineStatusListener.onOnlineStatusChange(string, devOnlineStatus);
            }
        }

        public /* synthetic */ C0003c(c cVar, a.a.a.a.b.c.a aVar) {
            this();
        }
    }

    public c(String str, na naVar) {
        this.f1307b = str;
        this.f1308c = naVar;
        a();
    }

    @Override // com.alibaba.android.multiendinonebridge.IUpstreamProxy
    public void invokeEventMethod(String str, String str2) {
        b bVar = this.e.get(str);
        if (bVar == null) {
            a.a.a.a.b.m.a.d(f1306a, String.format("Ignore unsupported event: %s, payload: %s", str, str2));
        } else {
            a.a.a.a.b.m.a.c(f1306a, String.format(Locale.getDefault(), "Handle event: %s, payload: %s", str, str2));
            bVar.handle(str, str2);
        }
    }

    @Override // com.alibaba.android.multiendinonebridge.IUpstreamProxy
    public void sendIoTCommand(int i, String str) {
        JSONObject object = JSON.parseObject(str);
        if (i == 0) {
            JSONArray jSONArray = object.getJSONArray("sigmesh");
            if (jSONArray == null || jSONArray.size() == 0) {
                a.a.a.a.b.m.a.d(f1306a, "Empty SIGMesh data");
                return;
            }
            Sigmesh sigmesh = (Sigmesh) jSONArray.getJSONObject(0).toJavaObject(Sigmesh.class);
            if (sigmesh == null || sigmesh.getAction() == null || sigmesh.getDevice() == null) {
                a.a.a.a.b.m.a.d(f1306a, "Illegal SIGMesh data");
                return;
            }
            int destAddr = sigmesh.getDevice().getDestAddr();
            int appKeyIndex = sigmesh.getDevice().getAppKeyIndex();
            int netKeyIndex = sigmesh.getDevice().getNetKeyIndex();
            if (sigmesh.getAction().getOpcode() != null) {
                TgMeshManager.getInstance().sendMessge(destAddr, appKeyIndex, netKeyIndex, Utils.byteArray2Int(Utils.getOpCodeBytes(Integer.parseInt(sigmesh.getAction().getOpcode(), 16))), !TextUtils.isEmpty(sigmesh.getAction().getParameters()) ? MeshParserUtils.toByteArray(sigmesh.getAction().getParameters()) : new byte[0], new a.a.a.a.b.c.a(this));
                return;
            }
            return;
        }
        if (i != 12) {
            return;
        }
        JSONObject jSONObject = object.getJSONObject("configuration");
        if (jSONObject == null) {
            a.a.a.a.b.m.a.d(f1306a, "Empty Configuration data");
            return;
        }
        ConfigurationData configurationData = (ConfigurationData) jSONObject.toJavaObject(ConfigurationData.class);
        if (configurationData == null || configurationData.getPrimaryUnicastAddress() == null || configurationData.getConfigResultMap() == null) {
            a.a.a.a.b.m.a.d(f1306a, "Illegal Configuration data");
            return;
        }
        List<SubscribeGroupAddr> configModelSubscription = configurationData.getConfigResultMap().getConfigModelSubscription();
        if (configModelSubscription == null || configModelSubscription.size() <= 0) {
            return;
        }
        for (SubscribeGroupAddr subscribeGroupAddr : configModelSubscription) {
            if (subscribeGroupAddr != null) {
                a.a.a.a.b.m.a.a(f1306a, "Config Subscription Add");
                if (TextUtils.isEmpty(configurationData.getDeviceKey())) {
                    a.a.a.a.b.m.a.d(f1306a, String.format(Locale.getDefault(), "Device(%d) key can not be empty, maybe server error", (Integer) configurationData.getPrimaryUnicastAddress()));
                } else {
                    TgMeshManager.getInstance().configModelSubscriptionAdd(configurationData.getDeviceKey(), ((Integer) configurationData.getPrimaryUnicastAddress()).intValue(), subscribeGroupAddr.getModelElementAddr().intValue(), subscribeGroupAddr.getGroupAddr().intValue(), subscribeGroupAddr.getModelId().intValue(), new a.a.a.a.b.c.b(this));
                }
            }
        }
    }

    public void a(String str) {
        this.f1307b = str;
    }

    public void b(DeviceOnlineStatusListener deviceOnlineStatusListener) {
        if (deviceOnlineStatusListener != null) {
            this.f1309d.remove(deviceOnlineStatusListener);
        }
    }

    public final void a() {
        a.a.a.a.b.c.a aVar = null;
        this.e.put("DeviceRegister", new a(this, aVar));
        this.e.put("ReportOnlineStatus", new C0003c(this, aVar));
    }

    public void a(DeviceOnlineStatusListener deviceOnlineStatusListener) {
        if (this.f1309d.contains(deviceOnlineStatusListener)) {
            return;
        }
        this.f1309d.add(deviceOnlineStatusListener);
    }

    public c(na naVar) {
        this.f1308c = naVar;
        a();
    }
}
