package com.aliyun.alink.linksdk.tmp.device.e;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.data.ut.ExtraData;
import com.aliyun.alink.linksdk.tmp.device.payload.KeyValuePair;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.listener.IDevStateChangeListener;
import com.aliyun.alink.linksdk.tmp.listener.IDiscoveryDeviceStateChangeListener;
import com.aliyun.alink.linksdk.tmp.listener.IEventListener;
import com.aliyun.alink.linksdk.tmp.service.DevService;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: compiled from: ClientWrapper.java */
/* JADX INFO: loaded from: classes2.dex */
public class a extends b implements IDiscoveryDeviceStateChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4404a = "[Tmp]ClientWrapper";

    public a(DeviceConfig deviceConfig) {
        super(deviceConfig);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public TmpEnum.DeviceState getDeviceState() {
        if (this.mDeviceImpl != null) {
            return this.mDeviceImpl.l();
        }
        return TmpEnum.DeviceState.DISCONNECTED;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public TmpEnum.ConnectType getConnectType() {
        if (this.mDeviceImpl != null) {
            return this.mDeviceImpl.b();
        }
        return TmpEnum.ConnectType.CONNECT_TYPE_UNKNOWN;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public void init(Object obj, IDevListener iDevListener) {
        super.init(obj, iDevListener);
        com.aliyun.alink.linksdk.tmp.device.d.a.a().a(this);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public void unInit() {
        com.aliyun.alink.linksdk.tmp.device.d.a.a().b(this);
        super.unInit();
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean getPropertyValue(List<String> list, Object obj, IDevListener iDevListener) {
        return this.mDeviceImpl.a(list, obj, iDevListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean setPropertyValue(List<KeyValuePair> list, Object obj, IDevListener iDevListener) {
        return this.mDeviceImpl.b(list, obj, iDevListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean setPropertyValue(ExtraData extraData, List<KeyValuePair> list, Object obj, IDevListener iDevListener) {
        return this.mDeviceImpl.a(extraData, list, obj, iDevListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean setPropertyValue(String str, ValueWrapper valueWrapper, Object obj, IDevListener iDevListener) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new KeyValuePair(str, valueWrapper));
        return setPropertyValue(arrayList, obj, iDevListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean invokeService(String str, List<KeyValuePair> list, ExtraData extraData, Object obj, IDevListener iDevListener) {
        return this.mDeviceImpl.a(str, list, extraData, obj, iDevListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean subscribeEvent(String str, Object obj, IEventListener iEventListener) {
        return this.mDeviceImpl.a(str, obj, iEventListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean subAllEvents(Object obj, IEventListener iEventListener) {
        return this.mDeviceImpl.a(obj, iEventListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean unsubscribeEvent(String str, Object obj, IDevListener iDevListener) {
        return this.mDeviceImpl.a(str, obj, iDevListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean addDeviceStateChangeListener(IDevStateChangeListener iDevStateChangeListener) {
        return super.addDeviceStateChangeListener(iDevStateChangeListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean removeDeviceStateChangeListener(IDevStateChangeListener iDevStateChangeListener) {
        return super.removeDeviceStateChangeListener(iDevStateChangeListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDiscoveryDeviceStateChangeListener
    public void onDiscoveryDeviceStateChange(DeviceBasicData deviceBasicData, TmpEnum.DiscoveryDeviceState discoveryDeviceState) {
        ALog.d(f4404a, "onDiscoveryDeviceStateChange mDeviceImpl:" + this.mDeviceImpl + " state:" + discoveryDeviceState);
        if (deviceBasicData == null || this.mDeviceImpl == null) {
            return;
        }
        if (TmpEnum.DiscoveryDeviceState.DISCOVERY_STATE_OFFLINE == discoveryDeviceState) {
            if (TextUtils.isEmpty(this.mDeviceImpl.j()) || !this.mDeviceImpl.j().equalsIgnoreCase(deviceBasicData.getDevId())) {
                return;
            }
            ALog.d(f4404a, "stop current connect.");
            this.mDeviceImpl.g();
            return;
        }
        if (TmpEnum.DiscoveryDeviceState.DISCOVERY_STATE_ONLINE == discoveryDeviceState) {
            boolean zIsDeviceWifiAndBleCombo = DevService.isDeviceWifiAndBleCombo(deviceBasicData.getSupportedNetType());
            ALog.d(f4404a, "iscombo=" + zIsDeviceWifiAndBleCombo + ", getDeviceState=" + this.mDeviceImpl.l() + ", mDeviceImpl.getBasicData=" + this.mDeviceImpl.h() + ", mDeviceImpl.getConnectType=" + this.mDeviceImpl.b() + ", changePk=" + deviceBasicData.getProductKey() + ", changeDn=" + deviceBasicData.getDeviceName() + ", changeMac=" + deviceBasicData.mac + ", localdiscoverytype vs wifi =" + (deviceBasicData.localDiscoveryType & TmpEnum.DeviceNetType.NET_WIFI.getValue()) + ", iotId=" + deviceBasicData.iotId + "basicData:" + deviceBasicData);
            if (zIsDeviceWifiAndBleCombo && !TextUtils.isEmpty(this.mDeviceImpl.h()) && this.mDeviceImpl.b() == TmpEnum.ConnectType.CONNECT_TYPE_BLE && this.mDeviceImpl.l() == TmpEnum.DeviceState.CONNECTED && (deviceBasicData.localDiscoveryType & TmpEnum.DeviceNetType.NET_WIFI.getValue()) > 0 && this.mDeviceImpl.h().equalsIgnoreCase(TextHelper.combineStr(deviceBasicData.getProductKey(), deviceBasicData.deviceName))) {
                ALog.d(f4404a, "stop current ble connect.");
                this.mDeviceImpl.g();
                this.mDeviceImpl.a((Object) null, new IDevListener() { // from class: com.aliyun.alink.linksdk.tmp.device.e.a.1
                    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
                    public void onSuccess(Object obj, OutputParams outputParams) {
                        ALog.d(a.f4404a, "stop ble connection and reconnect onSuccess() called with: tag = [" + obj + "], returnValue = [" + outputParams + "]");
                    }

                    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
                    public void onFail(Object obj, ErrorInfo errorInfo) {
                        ALog.e(a.f4404a, "stop ble connection and reconnect onFail() called with: tag = [" + obj + "], errorInfo = [" + errorInfo + "]");
                    }
                });
            }
        }
    }
}
