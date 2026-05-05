package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface;
import com.aliyun.alink.business.devicecenter.channel.ble.TLV;
import com.aliyun.alink.business.devicecenter.config.model.DeviceReportTokenType;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigState;
import com.aliyun.alink.business.devicecenter.provision.core.ble.BreezeConfigStrategy;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.k, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BreezeConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0477k implements IBleInterface.IBleActionCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BreezeConfigStrategy f3702a;

    public C0477k(BreezeConfigStrategy breezeConfigStrategy) {
        this.f3702a = breezeConfigStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface.IBleActionCallback
    public void onResponse(int i, byte[] bArr) {
        ALog.d(BreezeConfigStrategy.TAG, "onResponse() called with: code = [" + i + "], data = " + StringUtils.byteArray2String(bArr));
        try {
            if (this.f3702a.waitForResult.get()) {
                this.f3702a.notifyReceiveSwitchApAck();
                String[] strArr = new String[2];
                strArr[0] = AlinkConstants.KEY_END_TIME_SWITCH_AP;
                strArr[1] = String.valueOf(System.currentTimeMillis());
                DCUserTrack.addTrackData(strArr);
                if (i != 0) {
                    PerformanceLog.trace(BreezeConfigStrategy.TAG, "broadcastResult", PerformanceLog.getJsonObject("result", "fail"));
                    String str = BreezeConfigStrategy.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("handleBreBiz response error=");
                    sb.append(i);
                    sb.append(",data=");
                    sb.append(StringUtils.byteArray2String(bArr));
                    ALog.w(str, sb.toString());
                    BreezeConfigStrategy breezeConfigStrategy = this.f3702a;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("ble response error: ");
                    sb2.append(i);
                    sb2.append(",data=");
                    sb2.append(StringUtils.byteArray2String(bArr));
                    breezeConfigStrategy.breezeResponseInfo = sb2.toString();
                    return;
                }
                String str2 = null;
                boolean z = false;
                boolean z2 = false;
                for (TLV.Element element : TLV.parse(bArr)) {
                    if (element.type == 6) {
                        for (TLV.Element element2 : TLV.parse(element.value)) {
                            if (element2.type == 1 && element2.value[0] == 1) {
                                ALog.i(BreezeConfigStrategy.TAG, "onResponse success.");
                                this.f3702a.breezeResponseInfo = "ble response switchap success";
                                z2 = false;
                            } else if (element2.type == 1 && element2.value[0] == 2) {
                                ALog.w(BreezeConfigStrategy.TAG, "onResponse response fail.");
                                BreezeConfigStrategy breezeConfigStrategy2 = this.f3702a;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("ble response switchap failed. creason: ");
                                sb3.append(str2);
                                breezeConfigStrategy2.breezeResponseInfo = sb3.toString();
                                z2 = true;
                            } else if (element2.type == 2) {
                                str2 = new String(element2.value, "UTF-8");
                                String str3 = BreezeConfigStrategy.TAG;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("onResponse message=");
                                sb4.append(str2);
                                ALog.i(str3, sb4.toString());
                                BreezeConfigStrategy breezeConfigStrategy3 = this.f3702a;
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("ble response switchap failed. reason: ");
                                sb5.append(str2);
                                breezeConfigStrategy3.breezeResponseInfo = sb5.toString();
                            } else if (element2.type == 3) {
                                byte b2 = element2.value[0];
                                if (b2 == 1) {
                                    this.f3702a.deviceReportTokenType = DeviceReportTokenType.CLOUD_TOKEN;
                                    this.f3702a.updateBackupCheckType(this.f3702a.deviceReportTokenType, this.f3702a.isIlop());
                                    z = true;
                                } else if (b2 == 0) {
                                    this.f3702a.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
                                    this.f3702a.updateBackupCheckType(this.f3702a.deviceReportTokenType, this.f3702a.isIlop());
                                    z = true;
                                }
                                String str4 = BreezeConfigStrategy.TAG;
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append("onResponse tokenType=");
                                sb6.append((int) b2);
                                ALog.i(str4, sb6.toString());
                            }
                        }
                    }
                }
                if (!z) {
                    if (this.f3702a.sendAppToken2DeviceAB.get()) {
                        this.f3702a.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
                        this.f3702a.updateBackupCheckType(this.f3702a.deviceReportTokenType, this.f3702a.isIlop());
                    } else {
                        this.f3702a.deviceReportTokenType = DeviceReportTokenType.UNKNOWN;
                        this.f3702a.updateBackupCheckType(this.f3702a.deviceReportTokenType, this.f3702a.isIlop());
                    }
                }
                if (z2) {
                    PerformanceLog.trace(BreezeConfigStrategy.TAG, "broadcastResult", PerformanceLog.getJsonObject("result", "fail"));
                } else {
                    PerformanceLog.trace(BreezeConfigStrategy.TAG, "broadcastResult", PerformanceLog.getJsonObject("result", "success"));
                    this.f3702a.updateProvisionState(BreezeConfigState.BLE_SUCCESS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ALog.w(BreezeConfigStrategy.TAG, "onResponse exception=" + e);
            this.f3702a.breezeResponseInfo = "parse ble response exception " + e;
        }
    }
}
