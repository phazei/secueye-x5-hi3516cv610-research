package com.aliyun.alink.linksdk.tmp.device.panel.listener;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.tmp.a.a;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.device.panel.data.group.DeviceLocalStatusChangePayload;
import com.aliyun.alink.linksdk.tmp.device.request.localgroup.QueryLocalGroupDeviceRequest;
import com.aliyun.alink.linksdk.tmp.listener.IDiscoveryDeviceStateChangeListener;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class LocalDeviceListChangeListenerWrapper implements a.InterfaceC0220a, IDiscoveryDeviceStateChangeListener {
    private static final String TAG = "[Tmp]LocalDeviceListChangeListenerImpl";
    private IPanelGroupEventCallback mCallback;
    private String mGroupId;
    private QueryLocalGroupDeviceRequest.QueryLocalGroupDeviceResData mLocalGroupData;

    public LocalDeviceListChangeListenerWrapper(IPanelGroupEventCallback iPanelGroupEventCallback, String str, QueryLocalGroupDeviceRequest.QueryLocalGroupDeviceResData queryLocalGroupDeviceResData) {
        this.mCallback = iPanelGroupEventCallback;
        this.mLocalGroupData = queryLocalGroupDeviceResData;
        this.mGroupId = str;
    }

    public void setLocalGroupData(QueryLocalGroupDeviceRequest.QueryLocalGroupDeviceResData queryLocalGroupDeviceResData) {
        this.mLocalGroupData = queryLocalGroupDeviceResData;
    }

    @Override // com.aliyun.alink.linksdk.tmp.a.a.InterfaceC0220a
    public void onDeviceListChange(int i, DeviceBasicData deviceBasicData) {
        DeviceLocalStatusChangePayload.Status status;
        QueryLocalGroupDeviceRequest.QueryLocalGroupDeviceResData queryLocalGroupDeviceResData = this.mLocalGroupData;
        if (queryLocalGroupDeviceResData == null || deviceBasicData == null || this.mCallback == null || queryLocalGroupDeviceResData.items == null) {
            ALog.w(TAG, "onDeviceListChange mLocalGroupData empty or changedData emtpy or mCallback empty mLocalGroupData:" + this.mLocalGroupData + " changedData:" + deviceBasicData + " mCallback:" + this.mCallback);
            return;
        }
        for (QueryLocalGroupDeviceRequest.QueryLocalGroupDeviceResDataInner queryLocalGroupDeviceResDataInner : this.mLocalGroupData.items) {
            if (queryLocalGroupDeviceResDataInner.productKey.equalsIgnoreCase(deviceBasicData.getProductKey()) && queryLocalGroupDeviceResDataInner.deviceName.equalsIgnoreCase(deviceBasicData.getDeviceName())) {
                if (i == 1) {
                    status = new DeviceLocalStatusChangePayload.Status(System.currentTimeMillis(), 1);
                } else if (i != 2) {
                    return;
                } else {
                    status = new DeviceLocalStatusChangePayload.Status(System.currentTimeMillis(), 3);
                }
                DeviceLocalStatusChangePayload.DeviceLocalStatus deviceLocalStatus = new DeviceLocalStatusChangePayload.DeviceLocalStatus(queryLocalGroupDeviceResDataInner.iotId, queryLocalGroupDeviceResDataInner.productKey, queryLocalGroupDeviceResDataInner.deviceName, status);
                ArrayList arrayList = new ArrayList();
                arrayList.add(deviceLocalStatus);
                this.mCallback.onNotify(this.mGroupId, TmpConstant.URI_TOPIC_LOCALDEVICE_STATECHANGE, JSON.toJSONString(new DeviceLocalStatusChangePayload(new DeviceLocalStatusChangePayload.DeviceLocalStatusChangeParams(arrayList))));
            }
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDiscoveryDeviceStateChangeListener
    public void onDiscoveryDeviceStateChange(DeviceBasicData deviceBasicData, TmpEnum.DiscoveryDeviceState discoveryDeviceState) {
        if (TmpEnum.DiscoveryDeviceState.DISCOVERY_STATE_ONLINE == discoveryDeviceState) {
            onDeviceListChange(1, deviceBasicData);
            return;
        }
        if (TmpEnum.DiscoveryDeviceState.DISCOVERY_STATE_OFFLINE == discoveryDeviceState) {
            onDeviceListChange(2, deviceBasicData);
            return;
        }
        ALog.e(TAG, "onDiscoveryDeviceStateChange error state:" + discoveryDeviceState);
    }
}
