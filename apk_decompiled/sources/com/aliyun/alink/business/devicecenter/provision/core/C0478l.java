package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface;
import com.aliyun.alink.business.devicecenter.channel.ble.TLV;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigState;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigStrategy;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import java.nio.MappedByteBuffer;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.l, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BreezeConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0478l implements IBleInterface.IBleReceiverCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BreezeConfigStrategy f3703a;

    public C0478l(BreezeConfigStrategy breezeConfigStrategy) {
        this.f3703a = breezeConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface.IBleReceiverCallback
    public void onDataReceived(byte[] bArr) {
        ALog.d(BreezeConfigStrategy.TAG, "onMessage() called with: data = " + StringUtils.byteArray2String(bArr) + ", hash=" + hashCode() + ", breeHashCode=" + this.f3703a.hashCode());
        if (bArr != null) {
            try {
                if (bArr.length < 1) {
                    return;
                }
                synchronized (this.f3703a.lockHandleDeviceNotifyLock) {
                    for (TLV.Element element : TLV.parse(bArr)) {
                        if (element != null) {
                            if (element.type == 1) {
                                if (element.length >= 1 && element.value[0] == 2) {
                                    ALog.w(BreezeConfigStrategy.TAG, "onMessage device connect ap or connect mqtt failed.");
                                    this.f3703a.comboDeviceProvisionState = -1;
                                } else if (element.value[0] == 1) {
                                    ALog.i(BreezeConfigStrategy.TAG, "onMessage connect ap success.");
                                    this.f3703a.comboDeviceProvisionState = 1;
                                    this.f3703a.breezeConfigState = BreezeConfigState.BLE_SUCCESS;
                                    this.f3703a.provisionStatusCallback(ProvisionStatus.BLE_DEVICE_CONNECTED_AP);
                                    if (this.f3703a.deviceInfoNotifyListener != null && this.f3703a.mConfigParams != null && !TextUtils.isEmpty(this.f3703a.mConfigParams.productKey) && !TextUtils.isEmpty(this.f3703a.mConfigParams.deviceName)) {
                                        if (!AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_3.equals(this.f3703a.mConfigParams.devType)) {
                                            ALog.i(BreezeConfigStrategy.TAG, "onMessage device connect ap success from breeze(subType!=3) channel-connect ap, wait for wifi connect ap or token or token check.");
                                        } else {
                                            if (this.f3703a.mConfigParams.isInSide) {
                                                return;
                                            }
                                            ALog.i(BreezeConfigStrategy.TAG, "onMessage device connect ap success from breeze(subType=3) channel-connect ap.");
                                            DeviceInfo deviceInfo = new DeviceInfo();
                                            deviceInfo.productKey = this.f3703a.mConfigParams.productKey;
                                            deviceInfo.deviceName = this.f3703a.mConfigParams.deviceName;
                                            this.f3703a.deviceInfoNotifyListener.onDeviceFound(deviceInfo);
                                        }
                                        return;
                                    }
                                } else if (element.value[0] == 3) {
                                    String str = BreezeConfigStrategy.TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("onMessage token report success. params=");
                                    sb.append(this.f3703a.mConfigParams);
                                    sb.append(", isIlop=");
                                    sb.append(this.f3703a.isIlop());
                                    ALog.i(str, sb.toString());
                                    this.f3703a.comboDeviceProvisionState = 2;
                                    this.f3703a.provisionStatusCallback(ProvisionStatus.BLE_DEVICE_CONNECTED_CLOUD);
                                    if (this.f3703a.deviceInfoNotifyListener != null && this.f3703a.mConfigParams != null && !TextUtils.isEmpty(this.f3703a.mConfigParams.productKey) && !TextUtils.isEmpty(this.f3703a.mConfigParams.deviceName) && !TextUtils.isEmpty(this.f3703a.mConfigParams.bindToken) && this.f3703a.isIlop()) {
                                        if (this.f3703a.mConfigParams.isInSide) {
                                            return;
                                        }
                                        DeviceInfo deviceInfo2 = new DeviceInfo();
                                        deviceInfo2.productKey = this.f3703a.mConfigParams.productKey;
                                        deviceInfo2.deviceName = this.f3703a.mConfigParams.deviceName;
                                        deviceInfo2.token = this.f3703a.mConfigParams.bindToken;
                                        ALog.i(BreezeConfigStrategy.TAG, "onMessage provision success from breeze channel-report token.");
                                        this.f3703a.deviceInfoNotifyListener.onDeviceFound(deviceInfo2);
                                        return;
                                    }
                                } else {
                                    continue;
                                }
                            } else if (element.type == 3) {
                                this.f3703a.comboDeviceProvisionState = -1;
                                if (element.length == 2) {
                                    this.f3703a.subErrorCode = (element.value[0] & 255) | ((element.value[1] & 255) << 8);
                                    String str2 = BreezeConfigStrategy.TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("onMessage device error code: ");
                                    sb2.append(this.f3703a.subErrorCode);
                                    ALog.i(str2, sb2.toString());
                                }
                            } else if (element.type == 4) {
                                if (element.length == 2) {
                                    this.f3703a.devSubErrorCodeFromBleReceived = (element.value[0] & 255) | ((element.value[1] & 255) << 8);
                                    String str3 = BreezeConfigStrategy.TAG;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("onMessage device sub error code: ");
                                    sb3.append(this.f3703a.devSubErrorCodeFromBleReceived);
                                    ALog.i(str3, sb3.toString());
                                }
                            } else if (element.type == 7) {
                                this.f3703a.devInfoFromBleReceived = new String(element.value, "UTF-8");
                                String str4 = BreezeConfigStrategy.TAG;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("receive dev info from ble channel, info=");
                                sb4.append(this.f3703a.devInfoFromBleReceived);
                                ALog.i(str4, sb4.toString());
                            } else if (element.type == 6) {
                                String str5 = BreezeConfigStrategy.TAG;
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("receive dev wifi frame from ble channel len=");
                                sb5.append((int) element.length);
                                ALog.i(str5, sb5.toString());
                                try {
                                    if (this.f3703a.hasAllocateWiFiByteBuffer.compareAndSet(false, true) && this.f3703a.devWiFiMFromFromBleReceivedByteBuffer == null) {
                                        this.f3703a.devWiFiMFromFromBleReceivedByteBuffer = MappedByteBuffer.allocate(5120);
                                    }
                                    if (this.f3703a.devWiFiMFromFromBleReceivedByteBuffer != null) {
                                        this.f3703a.devWiFiMFromFromBleReceivedByteBuffer.put(element.value);
                                    }
                                } catch (Exception e) {
                                    String str6 = BreezeConfigStrategy.TAG;
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append("receive dev wifi frame from ble channel, but handle throw exception=");
                                    sb6.append(e);
                                    ALog.w(str6, sb6.toString());
                                }
                            } else if (element.type == 8) {
                                String str7 = BreezeConfigStrategy.TAG;
                                StringBuilder sb7 = new StringBuilder();
                                sb7.append("receive dev runtime log frame from ble channel len=");
                                sb7.append((int) element.length);
                                ALog.i(str7, sb7.toString());
                                try {
                                    String str8 = BreezeConfigStrategy.TAG;
                                    StringBuilder sb8 = new StringBuilder();
                                    sb8.append("log=");
                                    sb8.append(new String(element.value, "UTF-8"));
                                    ALog.d(str8, sb8.toString());
                                    ALog.llogForExternal((byte) 5, AlinkConstants.EXTERNAL_LOG_TAG, element.value);
                                } catch (Exception e2) {
                                    String str9 = BreezeConfigStrategy.TAG;
                                    StringBuilder sb9 = new StringBuilder();
                                    sb9.append("receive dev runtime log from ble channel, but handle throw exception=");
                                    sb9.append(e2);
                                    ALog.w(str9, sb9.toString());
                                }
                            }
                        }
                    }
                    String str10 = BreezeConfigStrategy.TAG;
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append("onMessage subErrorCode=");
                    sb10.append(this.f3703a.subErrorCode);
                    ALog.i(str10, sb10.toString());
                    if (this.f3703a.subErrorCode >= 50404) {
                        if (this.f3703a.comboDeviceProvisionState == -1) {
                            ALog.i(BreezeConfigStrategy.TAG, "onMessage device connect provision fail, wait for device to retry until timeout.");
                            return;
                        }
                        if (this.f3703a.comboDeviceProvisionState == 1) {
                            ALog.i(BreezeConfigStrategy.TAG, "onMessage device connect ap success, device connect cloud failed, wait until timeout.");
                        } else if (this.f3703a.comboDeviceProvisionState == 2) {
                            ALog.i(BreezeConfigStrategy.TAG, "onMessage device connect ap success, reportToken success, wait until loop cloud check.");
                        } else {
                            ALog.i(BreezeConfigStrategy.TAG, "onMessage device unexpected state returned, device connect cloud failed, wait until timeout.");
                        }
                    } else if (this.f3703a.subErrorCode != 0) {
                        ALog.i(BreezeConfigStrategy.TAG, "onMessage device provision fail, device connect cloud failed, provisionFail.");
                        this.f3703a.provisionFailFromBleNotify(this.f3703a.subErrorCode, "device provision fail.");
                    }
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                ALog.w(BreezeConfigStrategy.TAG, "onMessage exception=" + e3);
            }
        }
    }
}
