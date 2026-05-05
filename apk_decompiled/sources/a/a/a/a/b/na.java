package a.a.a.a.b;

import androidx.annotation.NonNull;
import anetwork.channel.util.RequestConstant;
import com.alibaba.ailabs.iot.mesh.AuthInfoListener;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import datasource.MeshConfig;
import datasource.MeshConfigCallback;
import datasource.bean.ConfigurationData;
import datasource.bean.DeviceStatus;
import datasource.bean.IoTGatewayEvent;
import datasource.bean.ProvisionInfo;
import datasource.bean.ProvisionInfo4Master;
import datasource.bean.ServerConfirmation;
import datasource.bean.Sigmesh;
import datasource.bean.WakeUpServiceContext;
import java.util.List;

/* JADX INFO: compiled from: RequestManage.java */
/* JADX INFO: loaded from: classes.dex */
public class na {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f1518a = "tg_mesh_sdk_" + na.class.getSimpleName();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public AuthInfoListener f1519b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public MeshConfig f1520c;

    /* JADX INFO: compiled from: RequestManage.java */
    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final na f1521a = new na();
    }

    public static na a() {
        return a.f1521a;
    }

    public void b(@NonNull String str, @NonNull String str2, @NonNull String str3, MeshConfigCallback<String> meshConfigCallback) {
        a.a.a.a.b.m.a.a(f1518a, "getInfoByAuthInfo called...");
        if (this.f1520c == null) {
            a.a.a.a.b.m.a.b(f1518a, "mMeshConfig is null");
            return;
        }
        AuthInfoListener authInfoListener = this.f1519b;
        this.f1520c.getInfoByAuthInfo(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, str2, str3, meshConfigCallback);
    }

    public void c(@NonNull String str, @NonNull String str2, @NonNull String str3, MeshConfigCallback<ProvisionInfo> meshConfigCallback) {
        a.a.a.a.b.m.a.a(f1518a, "getProvisionInfo called...");
        if (this.f1520c == null) {
            a.a.a.a.b.m.a.b(f1518a, "mMeshConfig is null");
            return;
        }
        AuthInfoListener authInfoListener = this.f1519b;
        this.f1520c.getProvisionInfo(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, str2, str3, meshConfigCallback);
    }

    public void d(@NonNull String str, @NonNull String str2, @NonNull String str3, MeshConfigCallback<List<Sigmesh>> meshConfigCallback) {
        a.a.a.a.b.m.a.a(f1518a, "groupControl called...");
        if (this.f1520c == null) {
            a.a.a.a.b.m.a.b(f1518a, "meshConfig is null");
            return;
        }
        AuthInfoListener authInfoListener = this.f1519b;
        if (authInfoListener != null) {
            authInfoListener.getAuthInfo();
        }
        this.f1520c.groupControl(str, str2, str3, meshConfigCallback);
    }

    public void a(AuthInfoListener authInfoListener, MeshConfig meshConfig) {
        a.a.a.a.b.m.a.a(f1518a, "init...");
        this.f1519b = authInfoListener;
        this.f1520c = meshConfig;
    }

    public void a(String str, MeshConfigCallback<ProvisionInfo4Master> meshConfigCallback) {
        a.a.a.a.b.m.a.a(f1518a, "getProvisionInfo4Master called...");
        if (this.f1520c == null) {
            a.a.a.a.b.m.a.b(f1518a, "mMeshConfig is null");
        } else {
            AuthInfoListener authInfoListener = this.f1519b;
            this.f1520c.getProvisionInfo4Master(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, meshConfigCallback);
        }
    }

    public String b() {
        AuthInfoListener authInfoListener = this.f1519b;
        if (authInfoListener == null) {
            a.a.a.a.b.m.a.b(f1518a, "mAuthInfoListener is null");
            return "";
        }
        try {
            JSONObject object = JSONObject.parseObject(authInfoListener.getAuthInfo());
            if (object != null) {
                return object.getString("userId");
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String c() {
        AuthInfoListener authInfoListener = this.f1519b;
        if (authInfoListener == null) {
            a.a.a.a.b.m.a.b(f1518a, "mAuthInfoListener is null");
            return "";
        }
        try {
            JSONObject object = JSONObject.parseObject(authInfoListener.getAuthInfo());
            if (object != null) {
                return object.getString("utdId");
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void a(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, MeshConfigCallback<ServerConfirmation> meshConfigCallback) {
        a.a.a.a.b.m.a.a(f1518a, "provisionConfirm called...");
        if (this.f1520c == null) {
            a.a.a.a.b.m.a.b(f1518a, "mMeshConfig is null");
            return;
        }
        AuthInfoListener authInfoListener = this.f1519b;
        this.f1520c.provisionConfirm(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str2, str, str3, str4, str5, meshConfigCallback);
    }

    public void a(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5, @NonNull String str6, MeshConfigCallback<Boolean> meshConfigCallback) {
        a.a.a.a.b.m.a.a(f1518a, "provisionAuth called...");
        if (this.f1520c == null) {
            a.a.a.a.b.m.a.b(f1518a, "mMeshConfig is null");
            return;
        }
        AuthInfoListener authInfoListener = this.f1519b;
        this.f1520c.provisionAuth(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, str2, str3, str4, str5, str6, meshConfigCallback);
    }

    public void a(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, MeshConfigCallback<ConfigurationData> meshConfigCallback) {
        a.a.a.a.b.m.a.a(f1518a, "provisionComplete called...");
        if (this.f1520c == null) {
            a.a.a.a.b.m.a.b(f1518a, "mMeshConfig is null");
            return;
        }
        AuthInfoListener authInfoListener = this.f1519b;
        this.f1520c.provisionComplete(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, str2, str3, str4, meshConfigCallback);
    }

    public void a(@NonNull String str, @NonNull String str2, @NonNull String str3, MeshConfigCallback<List<Sigmesh>> meshConfigCallback) {
        a.a.a.a.b.m.a.a(f1518a, "deviceControl called...");
        if (this.f1520c == null) {
            a.a.a.a.b.m.a.b(f1518a, "mMeshConfig is null");
            return;
        }
        AuthInfoListener authInfoListener = this.f1519b;
        this.f1520c.deviceControl(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, str2, str3, meshConfigCallback);
    }

    public void a(@NonNull String str, @NonNull List<DeviceStatus> list, MeshConfigCallback<String> meshConfigCallback) {
        a.a.a.a.b.m.a.a(f1518a, "reportDevicesStatus called...");
        if (this.f1520c == null) {
            a.a.a.a.b.m.a.b(f1518a, "mMeshConfig is null");
        } else {
            AuthInfoListener authInfoListener = this.f1519b;
            this.f1520c.reportDevicesStatus(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, list, meshConfigCallback);
        }
    }

    public void a(String str, IoTGatewayEvent ioTGatewayEvent) {
        MeshConfig meshConfig = this.f1520c;
        if (meshConfig == null) {
            a.a.a.a.b.m.a.b(f1518a, "mMeshConfig is null");
        } else {
            meshConfig.triggerGatewayEventAccs(str, ioTGatewayEvent);
        }
    }

    public void a(String str, boolean z, MeshConfigCallback<List<Sigmesh>> meshConfigCallback) {
        a.a.a.a.b.m.a.a(f1518a, "wakeUpDevice called...");
        if (this.f1520c == null) {
            a.a.a.a.b.m.a.b(f1518a, "wakeUpDevice is null");
            return;
        }
        WakeUpServiceContext wakeUpServiceContext = new WakeUpServiceContext();
        if (z) {
            wakeUpServiceContext.setPushGenie("true");
        } else {
            wakeUpServiceContext.setPushGenie(RequestConstant.FALSE);
        }
        this.f1520c.wakeUpDevice(str, wakeUpServiceContext, meshConfigCallback);
    }
}
