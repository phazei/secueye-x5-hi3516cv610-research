package com.aliyun.alink.linksdk.tmp.connect.entity.cmp;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.cmp.api.CommonResource;
import com.aliyun.alink.linksdk.cmp.api.ConnectSDK;
import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsServerConnectOption;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResource;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectDiscovery;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectResourceRegister;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.data.discovery.DiscoveryConfig;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;
import com.aliyun.alink.linksdk.tmp.event.INotifyHandler;
import com.aliyun.alink.linksdk.tmp.listener.IDevStateChangeListener;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: CmpConnect.java */
/* JADX INFO: loaded from: classes2.dex */
public class a extends com.aliyun.alink.linksdk.tmp.connect.b {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected static final String f4251d = "[Tmp]CmpConnect";
    protected String e;
    protected Map<String, AResource> f = new HashMap();
    protected String g;
    protected String h;
    protected PalDeviceInfo i;

    public a(String str, String str2, String str3) {
        this.e = str;
        ConnectSDK.getInstance().registerNofityListener(this.e, CmpNotifyManager.getInstance());
        this.i = new PalDeviceInfo(str2, str3);
    }

    public a(String str, String str2) {
        this.g = str;
        this.h = str2;
        if (TextUtils.isEmpty(str2)) {
            this.e = str;
            ConnectSDK.getInstance().registerNofityListener(this.e, CmpNotifyManager.getInstance());
        } else if (TextUtils.isEmpty(str)) {
            this.e = str2;
            ConnectSDK.getInstance().registerNofityListener(this.e, CmpNotifyManager.getInstance());
        } else {
            ConnectSDK.getInstance().registerNofityListener(this.h, CmpNotifyManager.getInstance());
            ConnectSDK.getInstance().registerNofityListener(this.g, CmpNotifyManager.getInstance());
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.b, com.aliyun.alink.linksdk.tmp.connect.IConnect
    public TmpEnum.ConnectType a() {
        int connectType = PluginMgr.getInstance().getConnectType(this.i);
        if (connectType == 1) {
            return TmpEnum.ConnectType.CONNECT_TYPE_COAP;
        }
        if (connectType == 4) {
            return TmpEnum.ConnectType.CONNECT_TYPE_BLE;
        }
        if (connectType == 5) {
            return TmpEnum.ConnectType.CONNECT_TYPE_TGMESH;
        }
        return super.a();
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public String a(TmpEnum.ConnectType connectType) {
        if (TmpEnum.ConnectType.CONNECT_TYPE_COAP == connectType) {
            return this.h;
        }
        if (TmpEnum.ConnectType.CONNECT_TYPE_MQTT == connectType) {
            return this.g;
        }
        return this.e;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean a(com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.c cVar) {
        ALog.d(f4251d, "send() called with: request = [" + dVar + "], handler = [" + cVar + "]");
        ConnectSDK.getInstance().send(this.e, (ARequest) dVar.c(), new d(dVar, cVar));
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean a(com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.c cVar, INotifyHandler iNotifyHandler) {
        ALog.d(f4251d, "subscribe request:" + dVar);
        a(dVar, new g(this.e, dVar, iNotifyHandler));
        ConnectSDK.getInstance().subscribe(this.e, (ARequest) dVar.c(), new j(dVar, new com.aliyun.alink.linksdk.tmp.connect.f(cVar, dVar)));
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean b(com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.c cVar) {
        ALog.d(f4251d, "unsubscribe request:" + dVar);
        ConnectSDK.getInstance().unsubscribe(this.e, (ARequest) dVar.c(), new k(dVar, new com.aliyun.alink.linksdk.tmp.connect.f(cVar, dVar)));
        a(dVar);
        return true;
    }

    public boolean a(com.aliyun.alink.linksdk.tmp.connect.d dVar, g gVar) {
        ALog.d(f4251d, "registerNofityListener request:" + dVar);
        if (gVar == null || dVar == null || TextUtils.isEmpty(dVar.d())) {
            ALog.w(f4251d, "registerNofityListener null");
            return false;
        }
        CmpNotifyManager.getInstance().addHandler(hashCode(), this.e, dVar.d(), gVar);
        return true;
    }

    public boolean a(com.aliyun.alink.linksdk.tmp.connect.d dVar) {
        ALog.d(f4251d, "unregisterNofityListener request:" + dVar);
        if (dVar == null || TextUtils.isEmpty(dVar.d())) {
            ALog.w(f4251d, "unregisterNofityListener null");
            return false;
        }
        CmpNotifyManager.getInstance().removeHandler(hashCode(), this.e, dVar.d());
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean a(String str, String str2, String str3, OutputParams outputParams, IPublishResourceListener iPublishResourceListener) {
        ALog.d(f4251d, "resourcePublish() called with: identifier = [" + str + "], method = [" + str2 + "], topic = [" + str3 + "], params = [" + outputParams + "], listener = [" + iPublishResourceListener + "]");
        CommonResource commonResource = new CommonResource();
        commonResource.topic = str3;
        CommonRequestPayload commonRequestPayload = new CommonRequestPayload(null, null);
        commonRequestPayload.setMethod(str2);
        commonRequestPayload.setVersion("1.0");
        commonRequestPayload.setParams(outputParams);
        String json = GsonUtils.toJson(commonRequestPayload);
        if (!TextUtils.isEmpty(json)) {
            commonResource.payload = json.getBytes();
        }
        ConnectSDK.getInstance().publishResource(commonResource, new com.aliyun.alink.linksdk.tmp.resource.f(str, iPublishResourceListener));
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean a(String str, String str2, byte[] bArr, IPublishResourceListener iPublishResourceListener) {
        ALog.d(f4251d, "rawResourcePublish() called with: identifier = [" + str + "], topic = [" + str2 + "], data = [" + bArr + "], listener = [" + iPublishResourceListener + "]");
        CommonResource commonResource = new CommonResource();
        commonResource.topic = str2;
        commonResource.payload = bArr;
        ConnectSDK.getInstance().publishResource(commonResource, new com.aliyun.alink.linksdk.tmp.resource.f(str, iPublishResourceListener));
        return true;
    }

    public boolean a(String str, String str2, boolean z, com.aliyun.alink.linksdk.tmp.resource.b bVar) {
        ALog.d(f4251d, "regTopic identifier:" + str + " topic:" + str2);
        CommonResource commonResource = new CommonResource();
        commonResource.topic = str2;
        commonResource.isNeedAuth = z;
        this.f.put(str, commonResource);
        ConnectSDK.getInstance().registerResource(commonResource, new com.aliyun.alink.linksdk.tmp.resource.g(str, bVar));
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean a(String str, String str2, String str3, boolean z, com.aliyun.alink.linksdk.tmp.resource.b bVar) {
        if (TextUtils.isEmpty(str)) {
            return a(str2, str3, z, bVar);
        }
        ALog.d(f4251d, "regTopic connectId:" + str + " identifier:" + str2 + " topic:" + str3);
        IConnectResourceRegister connectResourceRegister = ConnectSDK.getInstance().getConnectResourceRegister(str);
        if (connectResourceRegister == null) {
            ALog.d(f4251d, "regTopic faile getConnectResourceRegister null mConnectId:" + this.e);
            return false;
        }
        CommonResource commonResource = new CommonResource();
        commonResource.topic = str3;
        commonResource.isNeedAuth = z;
        this.f.put(str2, commonResource);
        connectResourceRegister.registerResource(commonResource, new com.aliyun.alink.linksdk.tmp.resource.g(str2, bVar));
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean a(String str, String str2) {
        ALog.d(f4251d, "unRegTopic() called with: identifier = [" + str + "], topic = [" + str2 + "]");
        IConnectResourceRegister connectResourceRegister = ConnectSDK.getInstance().getConnectResourceRegister(this.e);
        AResource aResourceRemove = this.f.remove(str);
        if (connectResourceRegister == null) {
            return true;
        }
        connectResourceRegister.unregisterResource(aResourceRemove, null);
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean c(com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.c cVar) {
        ALog.d(f4251d, "reDiscoveryDevice() called with: request = [" + dVar + "], handler = [" + cVar + "]");
        try {
            ConnectSDK.getInstance().getConnectDiscovery(this.e).discoveryCertainDevice((ARequest) dVar.c(), new b(cVar, dVar));
            return true;
        } catch (Throwable th) {
            ALog.w(f4251d, "reDiscoveryDevice t:" + th.toString());
            return true;
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean a(int i, DiscoveryConfig discoveryConfig, INotifyHandler iNotifyHandler) {
        ALog.d(f4251d, "startDiscovery() called with: timeOut = [" + i + "], config = [" + discoveryConfig + "], handler = [" + iNotifyHandler + "]");
        try {
            IConnectDiscovery connectDiscovery = ConnectSDK.getInstance().getConnectDiscovery(this.e);
            if (connectDiscovery == null) {
                return true;
            }
            connectDiscovery.startDiscovery(i, discoveryConfig != null ? discoveryConfig.translateToALCSDiscoveryConfig() : null, new e(iNotifyHandler));
            return true;
        } catch (Throwable th) {
            ALog.w(f4251d, "startDiscovery t:" + th.toString());
            return true;
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean b() {
        ALog.d(f4251d, "stopDiscovery() called");
        IConnectDiscovery connectDiscovery = ConnectSDK.getInstance().getConnectDiscovery(this.e);
        if (connectDiscovery == null) {
            return true;
        }
        connectDiscovery.stopDiscovery();
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean a(INotifyHandler iNotifyHandler) {
        ALog.d(f4251d, "startNotifyMonitor() called with: handler = [" + iNotifyHandler + "]");
        try {
            ConnectSDK.getInstance().getConnectDiscovery(this.e).startNotifyMonitor(new e(iNotifyHandler));
            return true;
        } catch (Throwable th) {
            ALog.w(f4251d, "startNotifyMonitor t:" + th.toString());
            return true;
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean c() {
        ALog.d(f4251d, "stopNotifyMonitor() called");
        try {
            ConnectSDK.getInstance().getConnectDiscovery(this.e).stopNotifyMonitor();
            return true;
        } catch (Throwable th) {
            ALog.w(f4251d, "stopNotifyMonitor error:" + th.toString());
            return true;
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.b, com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean a(String str, int i, Object obj) {
        ALog.d(f4251d, "setOption option:" + i + " value:" + obj);
        if (obj == null) {
            return false;
        }
        if (TextUtils.isEmpty(str)) {
            str = this.h;
        }
        switch (i) {
            case 1:
                if (obj instanceof String) {
                    AlcsServerConnectOption alcsServerConnectOption = new AlcsServerConnectOption();
                    alcsServerConnectOption.setPrefix((String) obj);
                    ConnectSDK.getInstance().updateConnectOption(this.e, alcsServerConnectOption);
                }
                break;
            case 2:
                if (obj instanceof String) {
                    AlcsServerConnectOption alcsServerConnectOption2 = new AlcsServerConnectOption();
                    alcsServerConnectOption2.setBlackClients((String) obj);
                    ConnectSDK.getInstance().updateConnectOption(this.e, alcsServerConnectOption2);
                }
                break;
        }
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public TmpEnum.DeviceState d() {
        ConnectState connectState = ConnectSDK.getInstance().getConnectState(this.e);
        ALog.d(f4251d, "getConnectState mConnectId:" + this.e + " connectState:" + connectState);
        return TmpEnum.DeviceState.createConnectState(connectState);
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean a(IDevStateChangeListener iDevStateChangeListener) {
        ALog.d(f4251d, "addConnectChangeListener() called with: listener = [" + iDevStateChangeListener + "]");
        CmpNotifyManager.getInstance().addConnectStateChangeListener(this.e, iDevStateChangeListener);
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean b(IDevStateChangeListener iDevStateChangeListener) {
        ALog.d(f4251d, "removeConnectChangeListener() called with: listener = [" + iDevStateChangeListener + "]");
        CmpNotifyManager.getInstance().removeConnectStateChangeListener(this.e, iDevStateChangeListener);
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean e() {
        ALog.d(f4251d, "stopConnect() called");
        ConnectSDK.getInstance().destoryConnect(this.e);
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.IConnect
    public boolean f() {
        ALog.d(f4251d, "unInit mConnectId:" + this.e);
        this.f.clear();
        ConnectSDK.getInstance().unregisterConnect(this.e);
        return true;
    }
}
