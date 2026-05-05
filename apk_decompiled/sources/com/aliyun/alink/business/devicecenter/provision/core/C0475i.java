package com.aliyun.alink.business.devicecenter.provision.core;

import com.alibaba.ailabs.iot.aisbase.plugin.auth.AuthPluginBusinessProxy;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigState;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigStrategy;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.ut.LinkUtHelper;
import com.aliyun.alink.business.devicecenter.ut.UtLinkInfo;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.i, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BreezeConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0475i implements IBleInterface.IBleConnectionCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BreezeConfigStrategy f3700a;

    public C0475i(BreezeConfigStrategy breezeConfigStrategy) {
        this.f3700a = breezeConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface.IBleConnectionCallback
    public void onChannelStateChanged(IBleInterface.IBleChannelDevice iBleChannelDevice, IBleInterface.BleChannelState bleChannelState) {
        ALog.d(BreezeConfigStrategy.TAG, "onConnectionStateChange() called with: device = [" + iBleChannelDevice + "], state = [" + bleChannelState + "]");
        if (!this.f3700a.waitForResult.get() || this.f3700a.provisionHasStopped.get()) {
            return;
        }
        if (bleChannelState == IBleInterface.BleChannelState.CONNECTED) {
            this.f3700a.updateProvisionState(BreezeConfigState.BLE_CONNECTED);
            this.f3700a.mBleChannelDevice = iBleChannelDevice;
            this.f3700a.deviceConnection = "2";
            if (iBleChannelDevice != null) {
                LinkUtHelper.connectEvent(LinkUtHelper.CONNECT_SUCCESS, new UtLinkInfo(this.f3700a.mConfigParams.userId, String.valueOf(System.currentTimeMillis() - this.f3700a.utStartTime), this.f3700a.mConfigParams.productKey, this.f3700a.mConfigParams.linkType.getName()));
                return;
            }
            return;
        }
        if (bleChannelState == IBleInterface.BleChannelState.AUTH_SUCCESSFUL) {
            this.f3700a.updateProvisionState(BreezeConfigState.BLE_AUTHT_SUCC);
            this.f3700a.deviceConnection = "2";
            if (this.f3700a.hasBleEverConnectedAB.get()) {
                ALog.d(BreezeConfigStrategy.TAG, "provision has already started, return.");
                return;
            }
            this.f3700a.hasBleEverConnectedAB.set(true);
            DCUserTrack.addTrackData(AlinkConstants.KEY_END_TIME_CONNECT_BLE, String.valueOf(System.currentTimeMillis()));
            try {
                PerformanceLog.trace(BreezeConfigStrategy.TAG, "connectBleResult", PerformanceLog.getJsonObject("result", "success"));
                if (iBleChannelDevice != null) {
                    this.f3700a.mBleChannelDevice = iBleChannelDevice;
                }
                this.f3700a.getDeviceName(this.f3700a.mBleChannelDevice);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                ALog.w(BreezeConfigStrategy.TAG, "onConnectionStateChange exception=" + e);
                return;
            }
        }
        if (bleChannelState != IBleInterface.BleChannelState.DISCONNECTED) {
            if (bleChannelState == IBleInterface.BleChannelState.AUTH_FAILED) {
                ALog.w(BreezeConfigStrategy.TAG, "ble auth failed.");
                this.f3700a.waitForResult.set(false);
                this.f3700a.deviceConnection = "3";
                this.f3700a.provisionErrorInfo = new DCErrorCode("DeviceFail", DCErrorCode.PF_DEVICE_FAIL).setMsg("BLE error, auth failed.").setSubcode(DCErrorCode.SUBCODE_DF_BLE_AUTH_FAIL);
                this.f3700a.provisionResultCallback(null);
                this.f3700a.stopConfig();
                if (this.f3700a.mConfigParams != null) {
                    UtLinkInfo utLinkInfo = new UtLinkInfo(this.f3700a.mConfigParams.userId, this.f3700a.mConfigParams.productKey, this.f3700a.mConfigParams.linkType.getName());
                    utLinkInfo.setErrorCode(String.valueOf(DCErrorCode.SUBCODE_DF_BLE_AUTH_FAIL));
                    LinkUtHelper.connectEvent(LinkUtHelper.CONNECT_FAIL, utLinkInfo);
                    return;
                }
                return;
            }
            return;
        }
        boolean z = this.f3700a.provisionHasStopped.get();
        int i = DCErrorCode.SUBCODE_DF_BLE_DISCONNECT;
        if (!z && this.f3700a.mBleRetryConnectCount.getAndIncrement() < 4) {
            this.f3700a.provisionErrorInfo = new DCErrorCode("DeviceFail", DCErrorCode.PF_DEVICE_FAIL).setMsg("BLE error, disconnected. " + this.f3700a.mBleRetryConnectCount.get()).setSubcode(DCErrorCode.SUBCODE_DF_BLE_DISCONNECT);
            this.f3700a.mHandler.sendEmptyMessageDelayed(BreezeConfigStrategy.MSG_RETRY_CONNECT_BLE_DEVICE, 1000L);
            return;
        }
        if (!this.f3700a.provisionHasStopped.get()) {
            PerformanceLog.trace(BreezeConfigStrategy.TAG, "connectBleResult", PerformanceLog.getJsonObject("result", "fail"));
            DCUserTrack.addTrackData(AlinkConstants.KEY_END_TIME_CONNECT_BLE, String.valueOf(System.currentTimeMillis()));
        }
        this.f3700a.deviceConnection = "3";
        if (!this.f3700a.waitForResult.get() || this.f3700a.hasBleEverConnectedAB.get()) {
            return;
        }
        this.f3700a.waitForResult.set(false);
        if (DCEnvHelper.isILopEnv() && AuthPluginBusinessProxy.isAuthAndBind.get()) {
            i = 2064;
        }
        this.f3700a.provisionErrorInfo = new DCErrorCode("DeviceFail", DCErrorCode.PF_DEVICE_FAIL).setMsg(i == 2064 ? "need authorize to bind." : "ble disconnected.").setSubcode(i);
        this.f3700a.provisionResultCallback(null);
        this.f3700a.stopConfig();
    }
}
