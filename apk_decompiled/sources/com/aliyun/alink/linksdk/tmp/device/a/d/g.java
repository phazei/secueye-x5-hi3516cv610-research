package com.aliyun.alink.linksdk.tmp.device.a.d;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.cmp.api.ConnectSDK;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.device.payload.discovery.GetTslResponsePayload;
import com.aliyun.alink.linksdk.tmp.devicemodel.DeviceModel;
import com.aliyun.alink.linksdk.tmp.devicemodel.loader.ILoaderHandler;
import com.aliyun.alink.linksdk.tmp.devicemodel.loader.RootDeviceModelSerializer;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;

/* JADX INFO: compiled from: ModelSerializeTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class g extends com.aliyun.alink.linksdk.tmp.device.a.d<g> implements com.aliyun.alink.linksdk.tmp.connect.c, ILoaderHandler {
    protected static final String n = "[Tmp]SerializeTask";
    protected DeviceConfig o;

    public g(com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, DeviceConfig deviceConfig, IDevListener iDevListener) {
        super(aVar, iDevListener);
        a(aVar);
        a(deviceBasicData);
        this.o = deviceConfig;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        String tsl;
        DeviceModel modelObject = RootDeviceModelSerializer.getInstance().getModelObject(this.j.getDevId());
        if (modelObject != null) {
            com.aliyun.alink.linksdk.tmp.device.a aVar = this.h;
            ALog.d(n, "action deviceModel found ret deviceImpl:" + aVar);
            if (aVar != null) {
                aVar.a(modelObject);
            } else {
                ALog.d(n, "action deviceModel found setDeviceModel deviceImpl empty");
            }
            a((Object) null, (Object) null);
            return true;
        }
        if (TextUtils.isEmpty(this.j.getDeviceModelJson())) {
            if (TextUtils.isEmpty(this.j.getDevId())) {
                TmpStorage.DeviceInfo deviceInfo = TmpStorage.getInstance().getDeviceInfo(this.o.getBasicData().getIotId());
                tsl = deviceInfo != null ? TmpStorage.getInstance().getTsl(deviceInfo.getId()) : null;
            } else {
                tsl = TmpStorage.getInstance().getTsl(this.j.getDevId());
            }
            if (TextUtils.isEmpty(tsl)) {
                if (!TextUtils.isEmpty(this.o.getBasicData().getIotId())) {
                    this.i = com.aliyun.alink.linksdk.tmp.connect.a.a(ConnectSDK.getInstance().getApiGatewayConnectId(), (String) null, (String) null);
                    return this.i.a(com.aliyun.alink.linksdk.tmp.connect.a.e.d().a(this.e).e(this.o.getBasicData().getIotId()).c(), this);
                }
            } else {
                this.j.setDeviceModelJson(tsl);
                a((com.aliyun.alink.linksdk.tmp.connect.d) null, (com.aliyun.alink.linksdk.tmp.connect.e) null);
            }
        } else {
            a((com.aliyun.alink.linksdk.tmp.connect.d) null, (com.aliyun.alink.linksdk.tmp.connect.e) null);
        }
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    /* JADX INFO: renamed from: b */
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.e eVar, ErrorInfo errorInfo) {
        a(dVar, errorInfo);
    }

    @Override // com.aliyun.alink.linksdk.tmp.devicemodel.loader.ILoaderHandler
    public void onDeserialize(DeviceModel deviceModel) {
        LogCat.d(n, "onDeserialize:" + deviceModel);
        if (deviceModel != null) {
            RootDeviceModelSerializer.getInstance().insertModelObject(this.j.getDevId(), deviceModel);
        }
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.h;
        if (aVar != null) {
            aVar.a(deviceModel);
        }
        a((Object) null, (Object) null);
    }

    @Override // com.aliyun.alink.linksdk.tmp.devicemodel.loader.ILoaderHandler
    public void onDeserializeError(String str) {
        LogCat.d(n, "onDeserializeError:" + str);
        b((Object) null, new ErrorInfo(300, "param is invalid"));
    }

    @Override // com.aliyun.alink.linksdk.tmp.devicemodel.loader.ILoaderHandler
    public void onSerialize(String str) {
        LogCat.e(n, "onSerialize");
    }

    @Override // com.aliyun.alink.linksdk.tmp.devicemodel.loader.ILoaderHandler
    public void onSerializeError(String str) {
        LogCat.e(n, "onSerializeError");
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.e eVar) {
        GetTslResponsePayload getTslResponsePayload;
        ALog.d(n, "onLoad response:" + eVar);
        if (eVar != null && (getTslResponsePayload = (GetTslResponsePayload) GsonUtils.fromJson(eVar.e(), new TypeToken<GetTslResponsePayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.d.g.1
        }.getType())) != null && getTslResponsePayload.data != null) {
            String json = GsonUtils.toJson(getTslResponsePayload.data);
            this.j.setDeviceModelJson(json);
            TmpStorage.getInstance().saveTsl(this.j.getDevId(), json);
            TmpStorage.getInstance().saveDeviceInfo(this.o.getBasicData().getIotId(), this.j.getProdKey(), this.j.getDeviceName());
            TmpStorage.getInstance().saveIotId(this.j.getProdKey(), this.j.getDeviceName(), this.o.getBasicData().getIotId());
        }
        RootDeviceModelSerializer.getInstance().deserialize(RootDeviceModelSerializer.SINGLEEXTEND_DEVICEMODELSERIALIZER_ID, this.j.getDeviceModelJson(), this);
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        b(dVar, errorInfo);
    }
}
