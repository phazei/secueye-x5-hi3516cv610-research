package com.aliyun.alink.linksdk.tmp.device.a.f;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.device.a.d;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.TDeviceShadow;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.device.payload.property.GetPropertyRequestPayload;
import com.aliyun.alink.linksdk.tmp.device.payload.property.GetPropertyResponsePayload;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: GetPropertyTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class b extends d<b> implements com.aliyun.alink.linksdk.tmp.connect.c {
    protected static final String n = "AllPropertyTask";
    protected List<String> o;
    protected WeakReference<TDeviceShadow> p;

    public b(TDeviceShadow tDeviceShadow, com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, IDevListener iDevListener) {
        super(aVar, iDevListener);
        this.p = new WeakReference<>(tDeviceShadow);
        a(deviceBasicData);
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        if (eVar != null && eVar.b()) {
            LogCat.i(n, "onLoad response success");
            GetPropertyResponsePayload getPropertyResponsePayload = (GetPropertyResponsePayload) GsonUtils.fromJson(eVar.e(), new TypeToken<GetPropertyResponsePayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.f.b.1
            }.getType());
            if (getPropertyResponsePayload != null && getPropertyResponsePayload.getCode() == 200) {
                LogCat.i(n, "onLoad response payload success");
                Map<String, ValueWrapper> property = getPropertyResponsePayload.getProperty();
                TDeviceShadow tDeviceShadow = this.p.get();
                if (property != null && property != null && !property.isEmpty() && tDeviceShadow != null) {
                    for (Map.Entry<String, ValueWrapper> entry : property.entrySet()) {
                        tDeviceShadow.setPropertyValue(entry.getKey(), entry.getValue(), false, (IPublishResourceListener) null);
                    }
                }
                LogCat.i(n, "onLoad taskSuccess");
                a(dVar, eVar);
                return;
            }
        }
        LogCat.i(n, "onLoad taskError");
        b(dVar, new ErrorInfo(300, "response error"));
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        b(dVar, errorInfo);
    }

    public b a(List<String> list) {
        this.o = list;
        return this;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        String str;
        super.a();
        if (this.j == null || this.k == null || this.i == null) {
            ALog.e(n, "mDeviceBasicData or mDeviceModel or mConnect null");
            b((Object) null, new ErrorInfo(300, "param is invalid"));
            return false;
        }
        GetPropertyRequestPayload getPropertyRequestPayload = new GetPropertyRequestPayload(this.j.getProductKey(), this.j.getDeviceName());
        getPropertyRequestPayload.setProperty(this.o);
        getPropertyRequestPayload.setMethod(this.k.getServiceMethod(TmpConstant.PROPERTY_IDENTIFIER_GET));
        this.i.a(com.aliyun.alink.linksdk.tmp.connect.a.d.d().k(this.j.getProductKey()).l(this.j.getDeviceName()).a(this.j.getAddr()).a(this.j.getPort()).m(getPropertyRequestPayload.getMethod()).c(CommonRequestBuilder.a(this.j.getProductKey(), this.j.getDeviceName(), getPropertyRequestPayload.getMethod(), "sys")).a(this.e).a(true).n(this.j.isBleMeshDevice() ? this.j.iotId : "").o(this.j.isBleMeshDevice() ? "thing.attribute.get" : "").b(getPropertyRequestPayload).c(), this);
        if (("properties :" + this.o) == null) {
            str = "empty";
        } else {
            str = this.o.toString() + " mIsSecure:" + this.l;
        }
        LogCat.d(n, str);
        return true;
    }
}
