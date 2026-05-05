package com.aliyun.alink.linksdk.tmp.device.e;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.DeviceManager;
import com.aliyun.alink.linksdk.tmp.api.IDevice;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.data.device.Option;
import com.aliyun.alink.linksdk.tmp.data.ut.ExtraData;
import com.aliyun.alink.linksdk.tmp.device.payload.KeyValuePair;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Event;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.devicemodel.Service;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.listener.IDevRawDataListener;
import com.aliyun.alink.linksdk.tmp.listener.IDevStateChangeListener;
import com.aliyun.alink.linksdk.tmp.listener.IEventListener;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.listener.ITRawDataRequestHandler;
import com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: CommonDevWrapper.java */
/* JADX INFO: loaded from: classes2.dex */
public abstract class b implements IDevice {
    public static final String TAG = "[Tmp]CommonDevWrapper";
    protected DeviceConfig mConfig;
    protected com.aliyun.alink.linksdk.tmp.device.a mDeviceImpl;

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public TmpEnum.ConnectType getConnectType() {
        return null;
    }

    public b(DeviceConfig deviceConfig) {
        this.mConfig = deviceConfig;
    }

    protected b() {
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public void init(Object obj, IDevListener iDevListener) {
        DeviceManager deviceManager = DeviceManager.getInstance();
        DeviceConfig deviceConfig = this.mConfig;
        DeviceBasicData deviceBasicData = deviceManager.getDeviceBasicData(deviceConfig == null ? "" : deviceConfig.getBasicData().getDevId());
        if (deviceBasicData == null) {
            DeviceConfig deviceConfig2 = this.mConfig;
            deviceBasicData = new DeviceBasicData(deviceConfig2 == null ? null : deviceConfig2.getBasicData());
        }
        this.mDeviceImpl = new com.aliyun.alink.linksdk.tmp.device.a(this.mConfig, deviceBasicData);
        this.mDeviceImpl.a(obj, iDevListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public void unInit() {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.mDeviceImpl;
        if (aVar != null) {
            aVar.f();
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public String getDevName() {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.mDeviceImpl;
        return aVar != null ? aVar.j() : "";
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public String getDevId() {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.mDeviceImpl;
        return aVar != null ? aVar.j() : "";
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public TmpEnum.DeviceState getDeviceState() {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.mDeviceImpl;
        if (aVar != null) {
            return aVar.l();
        }
        return TmpEnum.DeviceState.DISCONNECTED;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean addDeviceStateChangeListener(IDevStateChangeListener iDevStateChangeListener) {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.mDeviceImpl;
        if (aVar != null) {
            return aVar.a(iDevStateChangeListener);
        }
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean removeDeviceStateChangeListener(IDevStateChangeListener iDevStateChangeListener) {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.mDeviceImpl;
        if (aVar != null) {
            return aVar.b(iDevStateChangeListener);
        }
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public List<Property> getProperties() {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.mDeviceImpl;
        if (aVar != null) {
            return aVar.m();
        }
        return null;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public List<Service> getServices() {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.mDeviceImpl;
        if (aVar != null) {
            return aVar.n();
        }
        return null;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public List<Event> getEvents() {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.mDeviceImpl;
        if (aVar != null) {
            return aVar.o();
        }
        return null;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public ValueWrapper getPropertyValue(String str) {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.mDeviceImpl;
        if (aVar != null) {
            return aVar.a(str);
        }
        return null;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public Map<String, ValueWrapper> getAllPropertyValue() {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.mDeviceImpl;
        if (aVar != null) {
            return aVar.p();
        }
        return null;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean getPropertyValue(List<String> list, Object obj, IDevListener iDevListener) {
        ALog.e(TAG, "getPropertyValue could not exce");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean setPropertyValue(List<KeyValuePair> list, Object obj, IDevListener iDevListener) {
        ALog.e(TAG, "setPropertyValue could not exce");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean setPropertyValue(String str, ValueWrapper valueWrapper, Object obj, IDevListener iDevListener) {
        ALog.e(TAG, "setPropertyValue could not exce");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean setPropertyValue(ExtraData extraData, List<KeyValuePair> list, Object obj, IDevListener iDevListener) {
        ALog.e(TAG, "setPropertyValue could not exce");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean setPropertyValue(Map<String, ValueWrapper> map, boolean z) {
        return setPropertyValue(map, z, (IPublishResourceListener) null);
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean setPropertyValue(Map<String, ValueWrapper> map, boolean z, IPublishResourceListener iPublishResourceListener) {
        ALog.e(TAG, "setPropertyValue could not exce");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean invokeService(String str, List<KeyValuePair> list, Object obj, IDevListener iDevListener) {
        invokeService(str, list, new Option(TmpEnum.QoSLevel.QoS_CON), obj, iDevListener);
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean invokeService(String str, List<KeyValuePair> list, Option option, Object obj, IDevListener iDevListener) {
        invokeService(str, list, new ExtraData(new Option(TmpEnum.QoSLevel.QoS_CON)), obj, iDevListener);
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean invokeService(String str, List<KeyValuePair> list, ExtraData extraData, Object obj, IDevListener iDevListener) {
        ALog.e(TAG, "invokeService could not exce");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean subscribeEvent(String str, Object obj, IEventListener iEventListener) {
        ALog.e(TAG, "subscribeEvent could not exce");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean subAllEvents(Object obj, IEventListener iEventListener) {
        ALog.e(TAG, "subAllEvents could not exce");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean unsubscribeEvent(String str, Object obj, IDevListener iDevListener) {
        ALog.e(TAG, "unsubscribeEvent could not exce");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean setup(Object obj, Object obj2, IDevListener iDevListener) {
        ALog.e(TAG, "setup could not exce");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public String regRes(String str, boolean z, ITResRequestHandler iTResRequestHandler) {
        ALog.e(TAG, "regRes could not exce");
        return null;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean unRegRes(String str, ITResRequestHandler iTResRequestHandler) {
        ALog.e(TAG, "unRegRes could not exce");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean triggerRes(String str, OutputParams outputParams, IPublishResourceListener iPublishResourceListener) {
        ALog.e(TAG, "triggerRes could not exce");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean triggerRes(String str, OutputParams outputParams) {
        return triggerRes(str, outputParams, null);
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean sendRawData(byte[] bArr, IDevRawDataListener iDevRawDataListener) {
        ALog.e(TAG, "sendRawData could not exce");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean regRawRes(boolean z, ITRawDataRequestHandler iTRawDataRequestHandler) {
        ALog.e(TAG, "regRawRes could not exce");
        return false;
    }
}
