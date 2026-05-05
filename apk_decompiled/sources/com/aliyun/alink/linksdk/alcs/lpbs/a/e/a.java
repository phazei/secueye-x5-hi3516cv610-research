package com.aliyun.alink.linksdk.alcs.lpbs.a.e;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ICloudChannelFactory;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IDataDownListener;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalConnectListener;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: CloudCreateListener.java */
/* JADX INFO: loaded from: classes2.dex */
public class a implements PalConnectListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f3966a = "[AlcsLPBS]CloudCreateListener";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private PalDeviceInfo f3967b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private PalConnectListener f3968c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private com.aliyun.alink.linksdk.alcs.lpbs.a.a.a f3969d;
    private IDataDownListener e;
    private IPalConnect f;

    public a(PalDeviceInfo palDeviceInfo, PalConnectListener palConnectListener, IDataDownListener iDataDownListener, com.aliyun.alink.linksdk.alcs.lpbs.a.a.a aVar, IPalConnect iPalConnect) {
        this.f3967b = palDeviceInfo;
        this.f3968c = palConnectListener;
        this.f3969d = aVar;
        this.e = iDataDownListener;
        this.f = iPalConnect;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalConnectListener
    public void onLoad(final int i, final Map<String, Object> map, PalDeviceInfo palDeviceInfo) {
        ALog.d(f3966a, "onLoad errorCode:" + i + " params:" + map);
        if (i == 0 && PluginMgr.getInstance().isDataToCloud(this.f3967b)) {
            this.f3969d.a(this.f3967b, map, this.e, new ICloudChannelFactory.FactoryListener() { // from class: com.aliyun.alink.linksdk.alcs.lpbs.a.e.a.1
                @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ICloudChannelFactory.FactoryListener
                public void onCreate(PalDeviceInfo palDeviceInfo2, IThingCloudChannel iThingCloudChannel) {
                    ALog.d(a.f3966a, "onCreate channel:" + iThingCloudChannel + " mConnect: " + a.this.f);
                    if (a.this.f3968c != null) {
                        a.this.f3968c.onLoad(i, map, palDeviceInfo2);
                    }
                    if (iThingCloudChannel != null) {
                        a aVar = a.this;
                        Map map2 = map;
                        aVar.a(map2 != null ? (String) map2.get("version") : null, palDeviceInfo2.productModel, palDeviceInfo2.deviceId, iThingCloudChannel);
                        a aVar2 = a.this;
                        Map map3 = map;
                        aVar2.b(map3 != null ? (String) map3.get("activateInfo") : null, palDeviceInfo2.productModel, palDeviceInfo2.deviceId, iThingCloudChannel);
                        try {
                            if (a.this.f != null) {
                                a.this.f.onCloudChannelCreate(iThingCloudChannel);
                            }
                        } catch (Throwable th) {
                            ALog.e(a.f3966a, "onCloudChannelCreate error:" + th.toString());
                        }
                    }
                }
            });
            return;
        }
        PalConnectListener palConnectListener = this.f3968c;
        if (palConnectListener != null) {
            palConnectListener.onLoad(i, map, palDeviceInfo);
        }
    }

    void a(final String str, String str2, String str3, IThingCloudChannel iThingCloudChannel) {
        if (iThingCloudChannel == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            ALog.e(f3966a, "reportVersion channel:" + iThingCloudChannel + " version:" + str + " productKey:" + str2 + " deviceName:" + str3);
            return;
        }
        final String str4 = "/ota/device/inform/" + str2 + "/" + str3;
        HashMap map = new HashMap();
        map.put("id", 1);
        HashMap map2 = new HashMap();
        map2.put("version", str);
        map.put("params", map2);
        iThingCloudChannel.reportData(str4, new JSONObject(map).toString(), new IThingCloudChannel.IChannelActionListener() { // from class: com.aliyun.alink.linksdk.alcs.lpbs.a.e.a.2
            @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel.IChannelActionListener
            public void onSuccess() {
                ALog.i(a.f3966a, "reportVersion success version:" + str + " topic:" + str4);
            }

            @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel.IChannelActionListener
            public void onFailed(AError aError) {
                ALog.w(a.f3966a, "reportVersion fail. aError:" + aError);
            }
        });
    }

    void b(final String str, String str2, String str3, IThingCloudChannel iThingCloudChannel) {
        if (iThingCloudChannel == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            ALog.e(f3966a, "reportActivateInfo channel:" + iThingCloudChannel + " activateInfo:" + str + " productKey:" + str2 + " deviceName:" + str3);
            return;
        }
        final String str4 = "/sys/" + str2 + "/" + str3 + "/thing/deviceinfo/update";
        HashMap map = new HashMap();
        map.put("id", Long.valueOf(new SecureRandom().nextLong()));
        map.put("version", "1.0");
        map.put("method", "thing.deviceinfo.update");
        ArrayList arrayList = new ArrayList();
        HashMap map2 = new HashMap();
        map2.put("attrKey", "SYS_ALIOS_ACTIVATION");
        map2.put("attrValue", str);
        map2.put("domain", "SYSTEM");
        arrayList.add(map2);
        map.put("params", arrayList);
        String jSONString = new JSONObject(map).toJSONString();
        ALog.d(f3966a, "payload:" + jSONString);
        iThingCloudChannel.reportData(str4, jSONString, new IThingCloudChannel.IChannelActionListener() { // from class: com.aliyun.alink.linksdk.alcs.lpbs.a.e.a.3
            @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel.IChannelActionListener
            public void onSuccess() {
                ALog.i(a.f3966a, "reportActivateInfo success activateInfo:" + str + " topic:" + str4);
            }

            @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel.IChannelActionListener
            public void onFailed(AError aError) {
                ALog.w(a.f3966a, "reportActivateInfo fail. aError:" + aError);
            }
        });
    }
}
