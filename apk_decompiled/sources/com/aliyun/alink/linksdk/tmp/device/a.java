package com.aliyun.alink.linksdk.tmp.device;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.config.DefaultServerConfig;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.connect.entity.cmp.l;
import com.aliyun.alink.linksdk.tmp.connect.entity.cmp.m;
import com.aliyun.alink.linksdk.tmp.data.ut.ExtraData;
import com.aliyun.alink.linksdk.tmp.device.a.c;
import com.aliyun.alink.linksdk.tmp.device.a.d.d;
import com.aliyun.alink.linksdk.tmp.device.a.d.e;
import com.aliyun.alink.linksdk.tmp.device.a.d.f;
import com.aliyun.alink.linksdk.tmp.device.a.d.g;
import com.aliyun.alink.linksdk.tmp.device.a.d.h;
import com.aliyun.alink.linksdk.tmp.device.a.d.i;
import com.aliyun.alink.linksdk.tmp.device.a.d.j;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.TDeviceShadow;
import com.aliyun.alink.linksdk.tmp.device.payload.KeyValuePair;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.device.payload.cloud.EncryptGroupAuthInfo;
import com.aliyun.alink.linksdk.tmp.devicemodel.DeviceModel;
import com.aliyun.alink.linksdk.tmp.devicemodel.Event;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.devicemodel.Service;
import com.aliyun.alink.linksdk.tmp.event.EventManager;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.listener.IDevRawDataListener;
import com.aliyun.alink.linksdk.tmp.listener.IDevStateChangeListener;
import com.aliyun.alink.linksdk.tmp.listener.IEventListener;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.listener.ITRawDataRequestHandler;
import com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: compiled from: DeviceImpl.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected static final String f4299a = "[Tmp]DeviceImpl";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected com.aliyun.alink.linksdk.tmp.connect.b f4300b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected DeviceBasicData f4301c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected DeviceModel f4302d;
    protected TDeviceShadow f;
    protected DeviceConfig g;
    protected l h;
    protected m i;
    protected boolean j = false;
    protected boolean e = true;

    public a(DeviceConfig deviceConfig, DeviceBasicData deviceBasicData) {
        this.g = deviceConfig;
        this.f4301c = deviceBasicData;
        this.f = new TDeviceShadow(this, this.f4301c, this.g);
    }

    public void a(com.aliyun.alink.linksdk.tmp.connect.b bVar) {
        this.f4300b = bVar;
        TDeviceShadow tDeviceShadow = this.f;
        if (tDeviceShadow != null) {
            tDeviceShadow.setConnect(bVar);
        }
    }

    public com.aliyun.alink.linksdk.tmp.connect.b a() {
        return this.f4300b;
    }

    public TmpEnum.ConnectType b() {
        com.aliyun.alink.linksdk.tmp.connect.b bVar = this.f4300b;
        if (bVar != null) {
            return bVar.a();
        }
        return TmpEnum.ConnectType.CONNECT_TYPE_UNKNOWN;
    }

    public boolean c() {
        return this.j;
    }

    public void a(boolean z) {
        this.j = z;
    }

    public void a(DeviceModel deviceModel) {
        this.f4302d = deviceModel;
        this.f.setDeviceModel(this.f4302d);
    }

    public DeviceModel d() {
        return this.f4302d;
    }

    public boolean e() {
        return this.e;
    }

    public void b(boolean z) {
        this.e = z;
    }

    public void a(String str, String str2) {
        if (this.f4301c != null) {
            if (!TextUtils.isEmpty(str)) {
                this.f4301c.setProductKey(str);
            }
            if (!TextUtils.isEmpty(str2)) {
                this.f4301c.setDeviceName(str2);
            }
        }
        this.f.updateProDev(str, str2);
    }

    public boolean a(Object obj, IDevListener iDevListener) {
        ALog.d(f4299a, "init tag:" + obj + " handler:" + iDevListener);
        if (iDevListener == null || this.f4301c == null) {
            LogCat.d(f4299a, "init error handler:" + iDevListener);
            return false;
        }
        this.f.setDeviceModel(this.f4302d);
        c cVar = new c();
        if (DeviceConfig.DeviceType.CLIENT == this.g.getDeviceType()) {
            return cVar.b(new g(this, this.f4301c, this.g, iDevListener).a(obj)).b(new com.aliyun.alink.linksdk.tmp.device.a.d.a(this, this.f4301c, this.g, iDevListener).a(obj)).b(new i(this.f, iDevListener).a(obj)).a();
        }
        if (DeviceConfig.DeviceType.SERVER == this.g.getDeviceType()) {
            this.h = new l(this);
            this.i = new m(this);
            return cVar.b(new com.aliyun.alink.linksdk.tmp.device.a.d.c(this.g, iDevListener).a(obj)).b(new f(this, this.f4301c, this.g, iDevListener).a(obj)).b(new j(this.f4301c, this.g, iDevListener, this.h, this.i).a(obj)).b(new g(this, this.f4301c, this.g, iDevListener).a(obj)).b(new h(this, this.f4301c, this.g, iDevListener)).b(new i(this.f, iDevListener).a(obj)).a();
        }
        if (DeviceConfig.DeviceType.PROVISION == this.g.getDeviceType()) {
            return cVar.b(new d(this, this.f4301c, this.g, iDevListener).a(obj)).b(new com.aliyun.alink.linksdk.tmp.device.a.i.a(this, obj, this.f4301c, this.g, iDevListener)).a();
        }
        if (DeviceConfig.DeviceType.PROVISION_RECEIVER == this.g.getDeviceType()) {
            return cVar.b(new com.aliyun.alink.linksdk.tmp.device.a.d.c(this.g, iDevListener).a(obj)).b(new e(this, this.f4301c, this.g, iDevListener).a(obj)).b(new h(this, this.f4301c, this.g, iDevListener)).a();
        }
        return false;
    }

    public void f() {
        this.f.unInit();
        Set<String> devSubedEventList = EventManager.getInstance().getDevSubedEventList(hashCode(), j());
        if (devSubedEventList == null || devSubedEventList.isEmpty()) {
            ALog.d(f4299a, "subedEventlist empty");
        } else {
            Iterator<String> it = devSubedEventList.iterator();
            while (it.hasNext()) {
                c(it.next());
            }
        }
        m mVar = this.i;
        if (mVar != null) {
            mVar.a();
        }
        l lVar = this.h;
        if (lVar != null) {
            lVar.a();
        }
        ALog.d(f4299a, "unInit mDeviceShadow:" + this.f + " subedEventlist:" + devSubedEventList + " mUpdatePrefixHandler:" + this.i + " mUpdateBlackListHandler:" + this.h + " mConnect:" + this.f4300b);
    }

    public void g() {
        ALog.d(f4299a, "stopConnect mConnect:" + this.f4300b);
        com.aliyun.alink.linksdk.tmp.connect.b bVar = this.f4300b;
        if (bVar != null) {
            bVar.e();
            this.f4300b = null;
        }
    }

    public String h() {
        if (this.f4301c == null) {
            return null;
        }
        return this.f4301c.getProductKey() + this.f4301c.getDeviceName();
    }

    public String i() {
        DeviceBasicData deviceBasicData = this.f4301c;
        if (deviceBasicData != null) {
            return deviceBasicData.getModelType();
        }
        return null;
    }

    public String j() {
        DeviceBasicData deviceBasicData = this.f4301c;
        if (deviceBasicData != null) {
            return deviceBasicData.getDevId();
        }
        return null;
    }

    public String k() {
        DeviceBasicData deviceBasicData = this.f4301c;
        if (deviceBasicData != null) {
            return deviceBasicData.getIotId();
        }
        return null;
    }

    public TmpEnum.DeviceState l() {
        com.aliyun.alink.linksdk.tmp.connect.b bVar = this.f4300b;
        if (bVar == null) {
            ALog.d(f4299a, "getDeviceState mConnect null return DISCONNECTED");
            return TmpEnum.DeviceState.DISCONNECTED;
        }
        return bVar.d();
    }

    public boolean a(IDevStateChangeListener iDevStateChangeListener) {
        com.aliyun.alink.linksdk.tmp.connect.b bVar = this.f4300b;
        if (bVar != null) {
            return bVar.a(iDevStateChangeListener);
        }
        return false;
    }

    public boolean b(IDevStateChangeListener iDevStateChangeListener) {
        com.aliyun.alink.linksdk.tmp.connect.b bVar = this.f4300b;
        if (bVar != null) {
            return bVar.b(iDevStateChangeListener);
        }
        return false;
    }

    public List<Property> m() {
        DeviceModel deviceModel = this.f4302d;
        if (deviceModel != null) {
            return deviceModel.getProperties();
        }
        return null;
    }

    public List<Service> n() {
        DeviceModel deviceModel = this.f4302d;
        if (deviceModel != null) {
            return deviceModel.getServices();
        }
        return null;
    }

    public List<Event> o() {
        DeviceModel deviceModel = this.f4302d;
        if (deviceModel != null) {
            return deviceModel.getEvents();
        }
        return null;
    }

    public boolean a(List<String> list, Object obj, IDevListener iDevListener) {
        ALog.d(f4299a, "getPropertyValue idList:" + list + " tag:" + obj + " handler:" + iDevListener);
        return this.f.getPropertyValue(list, obj, iDevListener);
    }

    public ValueWrapper a(String str) {
        ALog.d(f4299a, "getPropertyValue propId:" + str);
        return this.f.getPropertyValue(str);
    }

    public Map<String, ValueWrapper> p() {
        ALog.d(f4299a, "getAllPropertyValues");
        return this.f.getAllPropertyValues();
    }

    public boolean a(ExtraData extraData, List<KeyValuePair> list, Object obj, IDevListener iDevListener) {
        ALog.d(f4299a, "setPropertyValue extraData:" + extraData + " propertyPair:" + list + " tag:" + obj + " handler:" + iDevListener);
        return this.f.setPropertyValue(extraData, list, obj, iDevListener);
    }

    public boolean b(List<KeyValuePair> list, Object obj, IDevListener iDevListener) {
        ALog.d(f4299a, "setPropertyValue propertyPair:" + list + " tag:" + obj + " handler:" + iDevListener);
        return this.f.setPropertyValue(list, obj, iDevListener);
    }

    public boolean a(String str, ValueWrapper valueWrapper, boolean z, IPublishResourceListener iPublishResourceListener) {
        return this.f.setPropertyValue(str, valueWrapper, z, iPublishResourceListener);
    }

    public boolean a(Map<String, ValueWrapper> map, boolean z, IPublishResourceListener iPublishResourceListener) {
        return this.f.setPropertyValue(map, z, iPublishResourceListener);
    }

    public boolean a(String str, List<KeyValuePair> list, ExtraData extraData, Object obj, IDevListener iDevListener) {
        ALog.d(f4299a, "invokeService serviceId:" + str + " args:" + list + " tag:" + obj + " handler:" + iDevListener);
        return new c().b(new com.aliyun.alink.linksdk.tmp.device.a.h.a(this, this.f4301c, iDevListener).a(this.f4302d).a(str).a(obj).a(extraData).a(e()).a(list)).a();
    }

    public boolean a(final String str, Object obj, final IEventListener iEventListener) {
        ALog.d(f4299a, "subscribeEvent eventId:" + str + " tag:" + obj + " handler:" + iEventListener);
        if (EventManager.getInstance().isOtherDeviceSubscribed(j(), str)) {
            a(str, j(), iEventListener);
            if (iEventListener != null) {
                iEventListener.onSuccess(obj, null);
            }
            return true;
        }
        if ("post".equalsIgnoreCase(str)) {
            this.f.subPropertyPostEvent(new IDevListener() { // from class: com.aliyun.alink.linksdk.tmp.device.a.1
                @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
                public void onSuccess(Object obj2, OutputParams outputParams) {
                    a aVar = a.this;
                    aVar.a(str, aVar.j(), iEventListener);
                    IEventListener iEventListener2 = iEventListener;
                    if (iEventListener2 != null) {
                        iEventListener2.onSuccess(obj2, null);
                    }
                }

                @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
                public void onFail(Object obj2, ErrorInfo errorInfo) {
                    IEventListener iEventListener2 = iEventListener;
                    if (iEventListener2 != null) {
                        iEventListener2.onFail(obj2, null);
                    }
                }
            });
            return true;
        }
        return new c().b(new com.aliyun.alink.linksdk.tmp.device.a.b.c(this, this.f4301c, this.f4302d, iEventListener, obj).a(EventManager.getInstance()).a(str)).a();
    }

    public boolean a(Object obj, IEventListener iEventListener) {
        ALog.d(f4299a, "subAllEvent  tag:" + obj + " handler:" + iEventListener + " mDeviceModel:" + this.f4302d);
        DeviceModel deviceModel = this.f4302d;
        if (deviceModel != null && deviceModel.getEvents() != null && this.f4302d.getEvents().size() <= 1) {
            return a("post", obj, iEventListener);
        }
        c cVar = new c();
        DeviceModel deviceModel2 = this.f4302d;
        if (deviceModel2 != null) {
            for (Event event2 : deviceModel2.getEvents()) {
                if ("post".equalsIgnoreCase(event2.getIdentifier())) {
                    a("post", obj, iEventListener);
                } else if (EventManager.getInstance().isOtherDeviceSubscribed(j(), event2.getIdentifier())) {
                    a(event2.getIdentifier(), j(), iEventListener);
                } else {
                    cVar.b(new com.aliyun.alink.linksdk.tmp.device.a.b.c(this, this.f4301c, this.f4302d, iEventListener, obj).a(EventManager.getInstance()).a(event2.getIdentifier()));
                }
            }
        }
        boolean zA = cVar.a();
        if (!zA && iEventListener != null) {
            iEventListener.onFail(obj, null);
        }
        return zA;
    }

    public boolean a(String str, Object obj, IDevListener iDevListener) {
        ALog.d(f4299a, "unsubscribeEvent eventId:" + str + " tag:" + obj + " handler:" + iDevListener);
        c(str);
        if (!"post".equalsIgnoreCase(str)) {
            return new c().b(new com.aliyun.alink.linksdk.tmp.device.a.b.a(this, this.f4301c, iDevListener).a(obj).a(e()).a(this.f4302d).a(str)).a();
        }
        if (iDevListener == null) {
            return true;
        }
        iDevListener.onSuccess(obj, null);
        return true;
    }

    public boolean a(String str, TmpEnum.GroupRoleType groupRoleType, Object obj, Object obj2, IDevListener iDevListener) {
        ALog.d(f4299a, "addGroupAuthInfo groupId:" + str + " roleType:" + groupRoleType + " authInfo:" + obj + " tag:" + obj2 + " handler:" + iDevListener);
        if (groupRoleType == null || obj == null) {
            return false;
        }
        return new c().b(new com.aliyun.alink.linksdk.tmp.device.a.e.b(this, this.f4301c, iDevListener).a(obj2).b(str).a(groupRoleType.getValue(), (EncryptGroupAuthInfo) GsonUtils.fromJson(obj.toString(), new TypeToken<EncryptGroupAuthInfo>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.2
        }.getType())).a(TmpConstant.GROUP_OP_ADD)).a();
    }

    public boolean a(String str, TmpEnum.GroupRoleType groupRoleType, Object obj, IDevListener iDevListener) {
        ALog.d(f4299a, "delGroupAUthInfo groupId:" + str + " roleType:" + groupRoleType + " tag:" + obj + " handler:" + iDevListener);
        if (groupRoleType == null) {
            groupRoleType = TmpEnum.GroupRoleType.UNKNOWN;
        }
        return new c().b(new com.aliyun.alink.linksdk.tmp.device.a.e.b(this, this.f4301c, iDevListener).a(obj).b(str).a(groupRoleType.getValue(), (EncryptGroupAuthInfo) null).a(TmpConstant.GROUP_OP_DEL)).a();
    }

    public boolean a(String str, String str2, Object obj, IDevListener iDevListener) {
        return new c().b(new com.aliyun.alink.linksdk.tmp.device.a.e.c(this, iDevListener, this.f4301c).a(str).b(str2)).a();
    }

    public boolean c(List<String> list, Object obj, IDevListener iDevListener) {
        return new c().b(new com.aliyun.alink.linksdk.tmp.device.a.e.a(this, iDevListener, this.f4301c).a(list)).a();
    }

    public boolean a(Object obj, Object obj2, IDevListener iDevListener) {
        com.aliyun.alink.linksdk.tmp.device.a.i.b bVar = new com.aliyun.alink.linksdk.tmp.device.a.i.b(obj2, this.f4301c, this, iDevListener);
        bVar.b(obj);
        return new c().b(bVar).a();
    }

    public boolean b(String str) {
        return EventManager.getInstance().isEventSubscribed(hashCode(), j(), str);
    }

    public void a(String str, String str2, IEventListener iEventListener) {
        EventManager.getInstance().addSubscribedEvent(hashCode(), j(), str);
        EventManager.getInstance().addEventListener(hashCode(), str2, str, iEventListener);
    }

    public void c(String str) {
        EventManager.getInstance().removeSubscribedEvent(hashCode(), j(), str);
        EventManager.getInstance().removeEventListener(hashCode(), j(), str);
    }

    public String a(String str, boolean z, ITResRequestHandler iTResRequestHandler) {
        return a((String) null, str, z, iTResRequestHandler);
    }

    public String a(String str, String str2, boolean z, ITResRequestHandler iTResRequestHandler) {
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        if (TmpConstant.PROPERTY_IDENTIFIER_SET.compareToIgnoreCase(str2) == 0) {
            this.f.setPropSetServiceHandler(iTResRequestHandler);
            return TextHelper.getTopicStr(this.f4302d, str2);
        }
        if (TmpConstant.PROPERTY_IDENTIFIER_GET.compareToIgnoreCase(str2) == 0) {
            this.f.setPropGetServiceHandler(iTResRequestHandler);
            return TextHelper.getTopicStr(this.f4302d, str2);
        }
        return com.aliyun.alink.linksdk.tmp.resource.e.a().a(this.f4300b, str, this.f4302d, str2, z, new com.aliyun.alink.linksdk.tmp.resource.h(iTResRequestHandler));
    }

    public boolean a(String str, ITResRequestHandler iTResRequestHandler) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (TmpConstant.PROPERTY_IDENTIFIER_SET.compareToIgnoreCase(str) == 0) {
            this.f.setPropSetServiceHandler(null);
            return true;
        }
        if (TmpConstant.PROPERTY_IDENTIFIER_GET.compareToIgnoreCase(str) == 0) {
            this.f.setPropGetServiceHandler(null);
            return true;
        }
        return com.aliyun.alink.linksdk.tmp.resource.e.a().a(this.f4300b, this.f4302d, str);
    }

    public boolean a(String str, OutputParams outputParams, IPublishResourceListener iPublishResourceListener) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if ("post".compareToIgnoreCase(str) == 0) {
            Map.Entry<String, ValueWrapper> next = outputParams.entrySet().iterator().next();
            this.f.triggerPostEvent(next.getKey(), next.getValue(), iPublishResourceListener);
            return true;
        }
        return com.aliyun.alink.linksdk.tmp.resource.e.a().a(this.f4300b, this.f4302d, str, outputParams, iPublishResourceListener);
    }

    public boolean a(int i, Object obj) {
        ALog.d(f4299a, "setConnectOption mConnect:" + this.f4300b + " optionType:" + i + " value:" + obj);
        com.aliyun.alink.linksdk.tmp.connect.b bVar = this.f4300b;
        if (bVar != null) {
            return bVar.a(bVar.a(TmpEnum.ConnectType.CONNECT_TYPE_COAP), i, obj);
        }
        return false;
    }

    public void d(String str) {
        ALog.d(f4299a, "updatePrefix prefix:" + str);
        a(1, str);
        DeviceConfig deviceConfig = this.g;
        if (deviceConfig == null || !(deviceConfig instanceof DefaultServerConfig)) {
            return;
        }
        DefaultServerConfig defaultServerConfig = (DefaultServerConfig) deviceConfig;
        defaultServerConfig.setPrefix(str);
        TmpStorage.getInstance().saveServerEnptInfo(j(), str, defaultServerConfig.getSecret());
    }

    public void e(String str) {
        ALog.d(f4299a, "updateBlackList blackList:" + str);
        a(2, str);
        TmpStorage.getInstance().saveBlackList(j(), str);
    }

    public boolean a(byte[] bArr, IDevRawDataListener iDevRawDataListener) {
        return new c().b(new com.aliyun.alink.linksdk.tmp.device.a.g.a(this, this.f4301c, iDevRawDataListener).a(bArr)).a();
    }

    public boolean a(boolean z, ITRawDataRequestHandler iTRawDataRequestHandler) {
        return com.aliyun.alink.linksdk.tmp.resource.e.a().a(this.f4300b, TextHelper.formatDownRawId(this.f4301c.getProductKey(), this.f4301c.getDeviceName()), TextHelper.formatDownRawTopic(this.f4301c.getProductKey(), this.f4301c.getDeviceName()), z, new com.aliyun.alink.linksdk.tmp.resource.c(iTRawDataRequestHandler));
    }

    public boolean b(byte[] bArr, final IDevRawDataListener iDevRawDataListener) {
        ALog.d(f4299a, "publishRawData handler:" + iDevRawDataListener + " data:" + iDevRawDataListener);
        return com.aliyun.alink.linksdk.tmp.resource.e.a().a(this.f4300b, "post", "/sys/" + this.f4301c.getProductKey() + "/" + this.f4301c.getDeviceName() + TmpConstant.URI_THING + TmpConstant.URI_MODEL + "/up_raw", bArr, new IPublishResourceListener() { // from class: com.aliyun.alink.linksdk.tmp.device.a.3
            @Override // com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener
            public void onSuccess(String str, Object obj) {
                IDevRawDataListener iDevRawDataListener2 = iDevRawDataListener;
                if (iDevRawDataListener2 != null) {
                    iDevRawDataListener2.onSuccess(null, obj);
                }
            }

            @Override // com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener
            public void onError(String str, AError aError) {
                IDevRawDataListener iDevRawDataListener2 = iDevRawDataListener;
                if (iDevRawDataListener2 != null) {
                    iDevRawDataListener2.onFail(null, aError == null ? null : new ErrorInfo(aError.getCode(), aError.getMsg()));
                }
            }
        });
    }
}
