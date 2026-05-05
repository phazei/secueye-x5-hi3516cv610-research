package com.aliyun.alink.linksdk.tmp.service;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.config.ble.BreezeConstants;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryDeviceInfo;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.DeviceManager;
import com.aliyun.alink.linksdk.tmp.data.SubDevInfo;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener;
import com.aliyun.alink.linksdk.tmp.device.request.auth.GetByAccountAndDevRequest;
import com.aliyun.alink.linksdk.tmp.device.request.auth.GetComboAccessInfoRequest;
import com.aliyun.alink.linksdk.tmp.device.request.auth.NotifyAccessInfoRequest;
import com.aliyun.alink.linksdk.tmp.device.request.other.GetDeviceNetTypesSupportedRequest;
import com.aliyun.alink.linksdk.tmp.listener.IProcessListener;
import com.aliyun.alink.linksdk.tmp.service.DevService;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.breeze.TLV;
import com.aliyun.iot.breeze.mix.ConnectionCallback;
import com.aliyun.iot.breeze.mix.MixBleDelegate;
import com.aliyun.iot.breeze.mix.MixBleDevice;
import com.aliyun.iot.breeze.mix.MixMessage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes2.dex */
public class WifiBTComboDeviceService {
    private static final String TAG = "[Tmp]WifiBTComboDeviceService";
    protected volatile AtomicBoolean isDone = new AtomicBoolean(false);

    public void afterBind(final SubDevInfo subDevInfo, DevService.ServiceListener serviceListener) {
        CloudUtils.setDeviceExtendProperty(subDevInfo.iotId, TmpConstant.DATA_KEY_DEVICENAME, subDevInfo.deviceName, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.service.WifiBTComboDeviceService.1
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                ALog.d(WifiBTComboDeviceService.TAG, "setDeviceExtendProperty onResponse response:" + aResponse);
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.e(WifiBTComboDeviceService.TAG, "setDeviceExtendProperty onFailure error:" + aError + " iotid:" + subDevInfo.iotId + " deviceName:" + subDevInfo.deviceName);
            }
        });
        DevService.setWifiStatus(subDevInfo.iotId, subDevInfo.deviceWifiStatus, new DevService.ServiceListenerEx() { // from class: com.aliyun.alink.linksdk.tmp.service.WifiBTComboDeviceService.2
            @Override // com.aliyun.alink.linksdk.tmp.service.DevService.ServiceListenerEx
            public void onComplete(boolean z, String str) {
                ALog.d(WifiBTComboDeviceService.TAG, "setWifiStatus onComplete isSuccess:" + z + " iotid:" + subDevInfo.iotId + " data:" + str);
            }
        });
        GateWayRequest getByAccountAndDevRequest = new GetByAccountAndDevRequest(subDevInfo.iotId);
        getByAccountAndDevRequest.sendRequest(getByAccountAndDevRequest, new IGateWayRequestListener<GetByAccountAndDevRequest, GetByAccountAndDevRequest.GetByAccountAndDevResponse>() { // from class: com.aliyun.alink.linksdk.tmp.service.WifiBTComboDeviceService.3
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
            public void onSuccess(GetByAccountAndDevRequest getByAccountAndDevRequest2, GetByAccountAndDevRequest.GetByAccountAndDevResponse getByAccountAndDevResponse) {
                if (getByAccountAndDevResponse.data == 0 || TextUtils.isEmpty(((GetByAccountAndDevRequest.GetByAccountAndDevData) getByAccountAndDevResponse.data).deviceName)) {
                    ALog.e(WifiBTComboDeviceService.TAG, "GetByAccountAndDevRequest empty ");
                    return;
                }
                TmpStorage.getInstance().saveDnToMac(((GetByAccountAndDevRequest.GetByAccountAndDevData) getByAccountAndDevResponse.data).deviceName, subDevInfo.deviceName);
                TmpStorage.getInstance().saveMacToDn(subDevInfo.deviceName, ((GetByAccountAndDevRequest.GetByAccountAndDevData) getByAccountAndDevResponse.data).deviceName);
                DeviceManager.getInstance().updateDeviceInfo(subDevInfo.productKey, subDevInfo.deviceName, subDevInfo.productKey, ((GetByAccountAndDevRequest.GetByAccountAndDevData) getByAccountAndDevResponse.data).deviceName);
                DeviceBasicData deviceBasicData = DeviceManager.getInstance().getDeviceBasicData(TextHelper.combineStr(subDevInfo.productKey, ((GetByAccountAndDevRequest.GetByAccountAndDevData) getByAccountAndDevResponse.data).deviceName));
                if (deviceBasicData == null || deviceBasicData.extraData == null) {
                    return;
                }
                deviceBasicData.extraData.put(PalDiscoveryDeviceInfo.EXTRA_KEY_BREEZE_RESET, false);
            }

            @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
            public void onFail(GetByAccountAndDevRequest getByAccountAndDevRequest2, AError aError) {
                ALog.e(WifiBTComboDeviceService.TAG, "GetByAccountAndDevRequest onFail ");
            }
        });
        DeviceShadowMgr.getInstance().getDeviceSupportedNetTypesByIotId(subDevInfo.iotId, new AnonymousClass4(serviceListener, subDevInfo));
    }

    /* JADX INFO: renamed from: com.aliyun.alink.linksdk.tmp.service.WifiBTComboDeviceService$4, reason: invalid class name */
    class AnonymousClass4 implements IProcessListener {
        final /* synthetic */ DevService.ServiceListener val$serviceListener;
        final /* synthetic */ SubDevInfo val$subDevInfo;

        AnonymousClass4(DevService.ServiceListener serviceListener, SubDevInfo subDevInfo) {
            this.val$serviceListener = serviceListener;
            this.val$subDevInfo = subDevInfo;
        }

        @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
        public void onSuccess(Object obj) {
            GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse getDeviceNetTypesSupportedResponse;
            try {
                getDeviceNetTypesSupportedResponse = (GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse) JSON.parseObject(obj.toString(), GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse.class);
            } catch (Exception unused) {
                ALog.e(WifiBTComboDeviceService.TAG, "GetDeviceNetTypesSupportedResponse parse error");
                getDeviceNetTypesSupportedResponse = null;
            }
            if (getDeviceNetTypesSupportedResponse == null || getDeviceNetTypesSupportedResponse.data == 0) {
                ALog.e(WifiBTComboDeviceService.TAG, "GetDeviceNetTypesSupportedResponse response or data empty");
                DevService.ServiceListener serviceListener = this.val$serviceListener;
                if (serviceListener != null) {
                    serviceListener.onComplete(true, null);
                    return;
                }
                return;
            }
            int deviceNetType = TmpEnum.DeviceNetType.formatDeviceNetType((List) getDeviceNetTypesSupportedResponse.data);
            if (!DevService.isDeviceWifiAndBleCombo(deviceNetType)) {
                ALog.e(WifiBTComboDeviceService.TAG, "isDeviceWifiAndBleCombo not  netType:" + deviceNetType);
                DevService.ServiceListener serviceListener2 = this.val$serviceListener;
                if (serviceListener2 != null) {
                    serviceListener2.onComplete(true, null);
                    return;
                }
                return;
            }
            if (this.val$subDevInfo.subDeviceChannel != null) {
                this.val$subDevInfo.subDeviceChannel.offline(new ISubDeviceActionListener() { // from class: com.aliyun.alink.linksdk.tmp.service.WifiBTComboDeviceService.4.1
                    @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                    public void onSuccess() {
                        ALog.d(WifiBTComboDeviceService.TAG, "offline onSuccess");
                    }

                    @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                    public void onFailed(AError aError) {
                        ALog.d(WifiBTComboDeviceService.TAG, "offline onFailed aError:" + aError);
                    }
                });
            }
            GateWayRequest getComboAccessInfoRequest = new GetComboAccessInfoRequest(null, null, this.val$subDevInfo.iotId);
            getComboAccessInfoRequest.sendRequest(getComboAccessInfoRequest, new AnonymousClass2());
        }

        /* JADX INFO: renamed from: com.aliyun.alink.linksdk.tmp.service.WifiBTComboDeviceService$4$2, reason: invalid class name */
        class AnonymousClass2 implements IGateWayRequestListener<GetComboAccessInfoRequest, GetComboAccessInfoRequest.GetComboAccessInfoResponse> {
            AnonymousClass2() {
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
            public void onSuccess(GetComboAccessInfoRequest getComboAccessInfoRequest, final GetComboAccessInfoRequest.GetComboAccessInfoResponse getComboAccessInfoResponse) {
                final boolean z = true;
                if (getComboAccessInfoResponse == null || getComboAccessInfoResponse.data == 0) {
                    ALog.w(WifiBTComboDeviceService.TAG, "GetComboAccessInfoRequest onSuccess data empty");
                    if (AnonymousClass4.this.val$serviceListener != null) {
                        AnonymousClass4.this.val$serviceListener.onComplete(true, null);
                        return;
                    }
                    return;
                }
                if (((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo == null || TextUtils.isEmpty(((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.authCode)) {
                    if (((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).accessInfo == null) {
                        ALog.w(WifiBTComboDeviceService.TAG, "GetComboAccessInfoRequest syncAccessInfo empty and accessInfo empty");
                        if (AnonymousClass4.this.val$serviceListener != null) {
                            AnonymousClass4.this.val$serviceListener.onComplete(true, null);
                            return;
                        }
                        return;
                    }
                    ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo = new GetComboAccessInfoRequest.syncAccessInfo();
                    ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.accessKey = ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).accessInfo.accessKey;
                    ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.accessToken = ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).accessInfo.accessToken;
                    ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.authCode = ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.accessKey.substring(0, 8);
                    z = false;
                }
                MixBleDelegate.getInstance().open(false, AnonymousClass4.this.val$subDevInfo.deviceName, new ConnectionCallback() { // from class: com.aliyun.alink.linksdk.tmp.service.WifiBTComboDeviceService.4.2.1
                    /* JADX WARN: Multi-variable type inference failed */
                    public void onConnectionStateChange(MixBleDevice mixBleDevice, int i, int i2) {
                        ALog.d(WifiBTComboDeviceService.TAG, "BREEZE onConnectionStateChange iBreezeDevice:" + mixBleDevice + " i:" + i + " i1:" + i2 + " isDone:" + WifiBTComboDeviceService.this.isDone);
                        if (i == 2 && mixBleDevice != null && WifiBTComboDeviceService.this.isDone.compareAndSet(false, true)) {
                            ArrayList arrayList = new ArrayList();
                            ArrayList arrayList2 = new ArrayList();
                            arrayList2.add(new TLV.Element(BreezeConstants.BREEZE_PROVISION_VERSION, new byte[]{0}));
                            arrayList2.add(new TLV.Element((byte) 1, ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.authCode.getBytes()));
                            arrayList.add(new TLV.Element((byte) 11, TLV.toPayload(arrayList2)));
                            byte[] payload = TLV.toPayload(arrayList);
                            MixMessage mixMessageNewMessage = mixBleDevice.newMessage(false, 13, payload);
                            ALog.d(WifiBTComboDeviceService.TAG, "authcode data:" + TextHelper.byte2hex(payload, payload.length) + " payload: authCode:" + ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.authCode + " accessKey:" + ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.accessKey + " accessToken:" + ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.accessToken);
                            mixBleDevice.sendMessage(mixMessageNewMessage, new MixBleDevice.ResponseCallback() { // from class: com.aliyun.alink.linksdk.tmp.service.WifiBTComboDeviceService.4.2.1.1
                                /* JADX WARN: Multi-variable type inference failed */
                                public void onResponse(int i3, byte[] bArr) {
                                    boolean z2;
                                    List list;
                                    if (i3 == 1) {
                                        z2 = false;
                                        for (TLV.Element element : TLV.parse(bArr)) {
                                            if (element != null && element.type == 11 && (list = TLV.parse(element.value)) != null) {
                                                Iterator it = list.iterator();
                                                while (true) {
                                                    if (!it.hasNext()) {
                                                        break;
                                                    }
                                                    TLV.Element element2 = (TLV.Element) it.next();
                                                    if (element2 != null && element2.type == 1) {
                                                        if (element2.value.length > 0) {
                                                            z2 = element2.value[0] == 1;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        z2 = false;
                                    }
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("sendMessage onResponse code:");
                                    sb.append(i3);
                                    sb.append(" isSuccess:");
                                    sb.append(z2);
                                    sb.append(" bytes:");
                                    sb.append(TextHelper.byte2hex(bArr, bArr != null ? bArr.length : 0));
                                    sb.append(" fNeedNotify:");
                                    sb.append(z);
                                    ALog.d(WifiBTComboDeviceService.TAG, sb.toString());
                                    if (z2) {
                                        String dnByMac = TmpStorage.getInstance().getDnByMac(AnonymousClass4.this.val$subDevInfo.deviceName);
                                        TmpStorage.getInstance().saveComboAccessInfo(TextHelper.combineStr(AnonymousClass4.this.val$subDevInfo.productKey, dnByMac), ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.accessKey, ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.accessToken);
                                        TmpStorage.getInstance().saveSyncAccessInfo(TextHelper.combineStr(AnonymousClass4.this.val$subDevInfo.productKey, dnByMac), null, null, null);
                                        if (z) {
                                            GateWayRequest notifyAccessInfoRequest = new NotifyAccessInfoRequest(null, null, AnonymousClass4.this.val$subDevInfo.iotId);
                                            notifyAccessInfoRequest.sendRequest(notifyAccessInfoRequest, new IGateWayRequestListener() { // from class: com.aliyun.alink.linksdk.tmp.service.WifiBTComboDeviceService.4.2.1.1.1
                                                @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
                                                public void onSuccess(Object obj, Object obj2) {
                                                    ALog.d(WifiBTComboDeviceService.TAG, "notifyAccessInfoRequest onSuccess o:" + obj + " result:" + obj2);
                                                }

                                                @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
                                                public void onFail(Object obj, AError aError) {
                                                    ALog.e(WifiBTComboDeviceService.TAG, "notifyAccessInfoRequest onFail o:" + obj + " error:" + aError);
                                                }
                                            });
                                        }
                                    }
                                    if (AnonymousClass4.this.val$serviceListener != null) {
                                        AnonymousClass4.this.val$serviceListener.onComplete(true, null);
                                    }
                                }
                            });
                            return;
                        }
                        if (i == 0 && WifiBTComboDeviceService.this.isDone.compareAndSet(false, true)) {
                            ALog.e(WifiBTComboDeviceService.TAG, "BREEZE onConnectionStateChange STATE_DISCONNECTED");
                            if (AnonymousClass4.this.val$serviceListener != null) {
                                AnonymousClass4.this.val$serviceListener.onComplete(true, null);
                            }
                        }
                    }
                });
            }

            @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
            public void onFail(GetComboAccessInfoRequest getComboAccessInfoRequest, AError aError) {
                ALog.e(WifiBTComboDeviceService.TAG, "GetComboAccessInfoRequest onFail:" + aError);
                if (AnonymousClass4.this.val$serviceListener != null) {
                    AnonymousClass4.this.val$serviceListener.onComplete(true, null);
                }
            }
        }

        @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
        public void onFail(ErrorInfo errorInfo) {
            ALog.e(WifiBTComboDeviceService.TAG, "getDeviceSupportedNetTypesByIotId onFail :" + errorInfo);
            DevService.ServiceListener serviceListener = this.val$serviceListener;
            if (serviceListener != null) {
                serviceListener.onComplete(true, null);
            }
        }
    }
}
