package com.aliyun.alink.linksdk.tmp.device.a.f;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder;
import com.aliyun.alink.linksdk.tmp.connect.a.m;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.data.ut.ExtraData;
import com.aliyun.alink.linksdk.tmp.device.a.d;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.TDeviceShadow;
import com.aliyun.alink.linksdk.tmp.device.payload.KeyValuePair;
import com.aliyun.alink.linksdk.tmp.device.payload.property.SetPropertyRequestPayload;
import com.aliyun.alink.linksdk.tmp.device.payload.property.SetPropertyResponsePayload;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: compiled from: SetPropertyTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class c extends d<c> implements com.aliyun.alink.linksdk.tmp.connect.c {
    protected static final String n = "[Tmp]SetPropertyTask";
    protected List<KeyValuePair> o;
    protected WeakReference<TDeviceShadow> p;
    protected ExtraData q;

    public c(TDeviceShadow tDeviceShadow, com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, IDevListener iDevListener) {
        super(aVar, iDevListener);
        a(deviceBasicData);
        this.p = new WeakReference<>(tDeviceShadow);
    }

    public c a(List<KeyValuePair> list) {
        this.o = list;
        return this;
    }

    public c a(ExtraData extraData) {
        this.q = extraData;
        return this;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        SetPropertyResponsePayload setPropertyResponsePayload;
        if (eVar != null && eVar.b() && (setPropertyResponsePayload = (SetPropertyResponsePayload) GsonUtils.fromJson(eVar.e(), new TypeToken<SetPropertyResponsePayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.f.c.1
        }.getType())) != null && setPropertyResponsePayload.getCode() == 200) {
            TDeviceShadow tDeviceShadow = this.p.get();
            for (KeyValuePair keyValuePair : this.o) {
                if (tDeviceShadow != null) {
                    tDeviceShadow.setPropertyValue(keyValuePair.getKey(), keyValuePair.getValueWrapper(), false, (IPublishResourceListener) null);
                }
            }
            LogCat.d(n, "onLoad success");
            a(dVar, eVar);
            return;
        }
        LogCat.d(n, "onLoad error response:" + eVar.toString());
        b(dVar, new ErrorInfo(300, "response error"));
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        b(dVar, errorInfo);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        super.a();
        if (this.j == null || this.k == null || this.i == null) {
            ALog.e(n, "mDeviceBasicData or mDeviceModel or mConnect null ：" + this.j + " mDeviceModel：" + this.k + " mConnect：" + this.i);
            b((Object) null, new ErrorInfo(300, "param is invalid"));
            return false;
        }
        SetPropertyRequestPayload setPropertyRequestPayload = new SetPropertyRequestPayload(this.j.getProductKey(), this.j.getDeviceName());
        HashMap map = new HashMap();
        List<KeyValuePair> list = this.o;
        if (list == null || list.isEmpty()) {
            b((Object) null, new ErrorInfo(300, "param is invalid"));
            return false;
        }
        for (KeyValuePair keyValuePair : this.o) {
            map.put(keyValuePair.getKey(), keyValuePair.getValueWrapper());
        }
        setPropertyRequestPayload.setParams(map);
        setPropertyRequestPayload.setMethod(this.k.getServiceMethod(TmpConstant.PROPERTY_IDENTIFIER_SET));
        m mVarD = m.d();
        ExtraData extraData = this.q;
        m mVarB = mVarD.e(extraData == null ? "" : extraData.performanceId).a(this.j.getAddr()).a(this.j.getPort()).k(this.j.getProductKey()).l(this.j.getDeviceName()).m(setPropertyRequestPayload.getMethod()).c(CommonRequestBuilder.a(this.k.getProfile(), setPropertyRequestPayload.getMethod())).a(this.e).a(true).b(setPropertyRequestPayload);
        ExtraData extraData2 = this.q;
        this.i.a(mVarB.a(extraData2 != null ? extraData2.option : null).n(this.j.isBleMeshDevice() ? this.j.iotId : "").o(this.j.isBleMeshDevice() ? "thing.attribute.set" : "").c(), this);
        LogCat.d(n, "properties :" + this.o + " mIsSecure:" + this.l);
        return false;
    }
}
