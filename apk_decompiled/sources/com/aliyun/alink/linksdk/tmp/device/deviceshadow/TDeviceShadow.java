package com.aliyun.alink.linksdk.tmp.device.deviceshadow;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.cmp.api.ConnectSDK;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.config.DefaultServerConfig;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.connect.b;
import com.aliyun.alink.linksdk.tmp.data.ut.ExtraData;
import com.aliyun.alink.linksdk.tmp.device.a;
import com.aliyun.alink.linksdk.tmp.device.a.c;
import com.aliyun.alink.linksdk.tmp.device.payload.KeyValuePair;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.DeviceModel;
import com.aliyun.alink.linksdk.tmp.event.EventManager;
import com.aliyun.alink.linksdk.tmp.event.INotifyHandler;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler;
import com.aliyun.alink.linksdk.tmp.resource.e;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class TDeviceShadow {
    protected static final String TAG = "[Tmp]TShadow";
    protected DeviceConfig mConfig;
    protected b mConnect;
    protected DeviceBasicData mDeviceBasicData;
    protected DeviceModel mDeviceModel;
    protected WeakReference<a> mImplRef;
    protected Map<String, ValueWrapper> mPropertyValueList = new ConcurrentHashMap();
    protected TPropEventHandler mTPropEventHandler = new TPropEventHandler(this);
    protected TPropServiceHandler mTPropServiceHandler = new TPropServiceHandler(this);

    public TDeviceShadow(a aVar, DeviceBasicData deviceBasicData, DeviceConfig deviceConfig) {
        this.mConfig = deviceConfig;
        this.mImplRef = new WeakReference<>(aVar);
        this.mDeviceBasicData = deviceBasicData;
    }

    public void setConnect(b bVar) {
        this.mConnect = bVar;
    }

    public void setConfig(DeviceConfig deviceConfig) {
        this.mConfig = deviceConfig;
    }

    public void updateProDev(String str, String str2) {
        if (this.mDeviceBasicData != null) {
            if (!TextUtils.isEmpty(str)) {
                this.mDeviceBasicData.setProductKey(str);
            }
            if (TextUtils.isEmpty(str2)) {
                return;
            }
            this.mDeviceBasicData.setDeviceName(str2);
        }
    }

    public String getIotId() {
        DeviceBasicData deviceBasicData = this.mDeviceBasicData;
        if (deviceBasicData != null) {
            return deviceBasicData.getIotId();
        }
        return null;
    }

    public boolean init(IDevListener iDevListener) {
        ALog.d(TAG, "init mConfig:" + this.mConfig + " handler:" + iDevListener);
        if (this.mConfig.getDeviceType() == DeviceConfig.DeviceType.CLIENT) {
            DeviceModel deviceModel = this.mDeviceModel;
            com.aliyun.alink.linksdk.tmp.device.a.b.b bVarA = (deviceModel == null || deviceModel.getEvents() == null || this.mDeviceModel.getEvents().size() < 1) ? null : new com.aliyun.alink.linksdk.tmp.device.a.b.b(this.mImplRef.get(), this.mDeviceBasicData, iDevListener).a(this.mDeviceModel).a("post").a((INotifyHandler) this.mTPropEventHandler);
            this.mTPropEventHandler.subEvent(EventManager.getInstance());
            c cVar = new c();
            if (bVarA != null) {
                cVar.b(bVarA);
            }
            cVar.a();
            if (iDevListener != null) {
                iDevListener.onSuccess(null, null);
            }
        } else if (this.mConfig.getDeviceType() == DeviceConfig.DeviceType.SERVER) {
            DefaultServerConfig defaultServerConfig = (DefaultServerConfig) this.mConfig;
            if (defaultServerConfig.getPropertValues() != null && !defaultServerConfig.getPropertValues().isEmpty()) {
                for (Map.Entry<String, ValueWrapper> entry : defaultServerConfig.getPropertValues().entrySet()) {
                    setPropertyValue(entry.getKey(), entry.getValue(), false, (IPublishResourceListener) null);
                }
            }
            e.a().a(this.mConnect, this.mDeviceModel, TmpConstant.PROPERTY_IDENTIFIER_GET, true, (com.aliyun.alink.linksdk.tmp.resource.b) this.mTPropServiceHandler);
            e.a().a(this.mConnect, this.mDeviceModel, TmpConstant.PROPERTY_IDENTIFIER_SET, true, (com.aliyun.alink.linksdk.tmp.resource.b) this.mTPropServiceHandler);
            if (DefaultServerConfig.ConnectType.isConnectContainCoap(defaultServerConfig.getConnectType())) {
                e.a().a(this.mConnect, ConnectSDK.getInstance().getAlcsServerConnectId(), this.mDeviceModel, "post", true, (com.aliyun.alink.linksdk.tmp.resource.b) this.mTPropServiceHandler);
            }
            if (DefaultServerConfig.ConnectType.isConnectContainMqtt(defaultServerConfig.getConnectType())) {
                e.a().a(this.mConnect, ConnectSDK.getInstance().getPersistentConnectId(), TmpConstant.EVENT_PROPERTY_URI_POST_REPLY, TextHelper.formatPostReplyTopic(defaultServerConfig.mIotProductKey, defaultServerConfig.mIotDeviceName), true, (com.aliyun.alink.linksdk.tmp.resource.b) this.mTPropServiceHandler);
            }
            if (iDevListener != null) {
                iDevListener.onSuccess(null, null);
            }
        }
        return true;
    }

    public void unInit() {
        ALog.d(TAG, "unInit ");
        if (this.mConfig.getDeviceType() == DeviceConfig.DeviceType.CLIENT) {
            this.mTPropEventHandler.unsubEvent(EventManager.getInstance());
        } else if (this.mConfig.getDeviceType() == DeviceConfig.DeviceType.SERVER) {
            e.a().a(this.mConnect, this.mDeviceModel, TmpConstant.PROPERTY_IDENTIFIER_GET);
            e.a().a(this.mConnect, this.mDeviceModel, TmpConstant.PROPERTY_IDENTIFIER_SET);
            e.a().a(this.mConnect, this.mDeviceModel, "post");
        }
    }

    public void subPropertyPostEvent(IDevListener iDevListener) {
        DeviceModel deviceModel = this.mDeviceModel;
        com.aliyun.alink.linksdk.tmp.device.a.b.b bVarA = (deviceModel == null || deviceModel.getEvents() == null || this.mDeviceModel.getEvents().size() < 1) ? null : new com.aliyun.alink.linksdk.tmp.device.a.b.b(this.mImplRef.get(), this.mDeviceBasicData, iDevListener).a(this.mDeviceModel).a("post").a((INotifyHandler) this.mTPropEventHandler);
        c cVar = new c();
        if (bVarA != null) {
            cVar.b(bVarA);
        }
        if (cVar.a() || iDevListener == null) {
            return;
        }
        iDevListener.onFail(null, null);
    }

    public void setDeviceModel(DeviceModel deviceModel) {
        this.mDeviceModel = deviceModel;
    }

    public ValueWrapper getPropertyValue(String str) {
        return this.mPropertyValueList.get(str);
    }

    public Map<String, ValueWrapper> getAllPropertyValues() {
        return this.mPropertyValueList;
    }

    public boolean getPropertyValue(List<String> list, Object obj, IDevListener iDevListener) {
        return new c().b(new com.aliyun.alink.linksdk.tmp.device.a.f.b(this, this.mImplRef.get(), this.mDeviceBasicData, iDevListener).a(obj).a(this.mDeviceModel).a(list)).a();
    }

    public boolean setPropertyValue(String str, ValueWrapper valueWrapper, boolean z, IPublishResourceListener iPublishResourceListener) {
        ALog.d(TAG, "setPropertyValue propId :" + str + " value:" + valueWrapper + " needPublish:" + z);
        if (TextUtils.isEmpty(str) || valueWrapper == null) {
            ALog.e(TAG, "setPropertyValue null");
            return false;
        }
        boolean z2 = this.mPropertyValueList.put(str, valueWrapper) != null;
        if (z) {
            triggerPostEvent(str, valueWrapper, iPublishResourceListener);
        }
        return z2;
    }

    public boolean setPropertyValue(Map<String, ValueWrapper> map, boolean z, IPublishResourceListener iPublishResourceListener) {
        for (Map.Entry<String, ValueWrapper> entry : map.entrySet()) {
            setPropertyValue(entry.getKey(), entry.getValue(), false, (IPublishResourceListener) null);
        }
        if (z) {
            triggerPostEvent(new OutputParams(map), iPublishResourceListener);
        }
        return false;
    }

    public boolean setPropertyValue(ExtraData extraData, List<KeyValuePair> list, Object obj, IDevListener iDevListener) {
        return new c().b(new com.aliyun.alink.linksdk.tmp.device.a.f.c(this, this.mImplRef.get(), this.mDeviceBasicData, iDevListener).a(obj).a(this.mDeviceModel).a(extraData).a(list)).a();
    }

    public boolean setPropertyValue(List<KeyValuePair> list, Object obj, IDevListener iDevListener) {
        return setPropertyValue((ExtraData) null, list, obj, iDevListener);
    }

    public boolean triggerPostEvent(String str, ValueWrapper valueWrapper, IPublishResourceListener iPublishResourceListener) {
        e.a().a(this.mConnect, this.mDeviceModel, "post", new OutputParams(str, valueWrapper), iPublishResourceListener);
        return true;
    }

    public boolean triggerPostEvent(OutputParams outputParams, IPublishResourceListener iPublishResourceListener) {
        ALog.d(TAG, "triggerPostEvent outputParams:" + outputParams);
        e.a().a(this.mConnect, this.mDeviceModel, "post", outputParams, iPublishResourceListener);
        return true;
    }

    public boolean setPropGetServiceHandler(ITResRequestHandler iTResRequestHandler) {
        return this.mTPropServiceHandler.setPropGetServiceHandler(iTResRequestHandler);
    }

    public boolean setPropSetServiceHandler(ITResRequestHandler iTResRequestHandler) {
        return this.mTPropServiceHandler.setPropSetServiceHandler(iTResRequestHandler);
    }
}
