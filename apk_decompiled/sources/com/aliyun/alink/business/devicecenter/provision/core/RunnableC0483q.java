package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.model.BackupCheckType;
import com.aliyun.alink.business.devicecenter.config.model.DeviceReportTokenType;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.provision.core.broadcast.AlinkBroadcastConfigStrategy;
import java.util.EnumSet;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.q, reason: case insensitive filesystem */
/* JADX INFO: compiled from: AlinkBroadcastConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class RunnableC0483q implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IConfigCallback f3713a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ AlinkBroadcastConfigStrategy f3714b;

    public RunnableC0483q(AlinkBroadcastConfigStrategy alinkBroadcastConfigStrategy, IConfigCallback iConfigCallback) {
        this.f3714b = alinkBroadcastConfigStrategy;
        this.f3713a = iConfigCallback;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.f3714b.packetData(this.f3714b.mConfigParams.ssid, this.f3714b.mConfigParams.password);
            if (this.f3714b.enableMulticastSend) {
                this.f3714b.packetMulticastData(this.f3714b.mConfigParams.ssid, this.f3714b.mConfigParams.password);
                if (this.f3714b.useAppTokenAB.get()) {
                    String token = this.f3714b.getToken(this.f3714b.appRandom, this.f3714b.mConfigParams.password);
                    if (!TextUtils.isEmpty(token) && token.length() > 31) {
                        this.f3714b.mConfigParams.bindToken = token.substring(0, 32).toUpperCase();
                    }
                }
                String str = AlinkBroadcastConfigStrategy.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("useAppTokenAB=");
                sb.append(this.f3714b.useAppTokenAB);
                sb.append(", token=");
                sb.append(this.f3714b.mConfigParams.bindToken);
                sb.append(", appRandom=");
                sb.append(this.f3714b.appRandom);
                sb.append(", cloudRandom=");
                sb.append(this.f3714b.cloudRandom);
                ALog.d(str, sb.toString());
                if (this.f3713a != null && !AlinkHelper.isBatchBroadcast(this.f3714b.mConfigParams)) {
                    this.f3714b.updateBackupCheckType(DeviceReportTokenType.APP_TOKEN);
                    this.f3714b.startBackupCheck(true, 0L);
                }
                String str2 = AlinkBroadcastConfigStrategy.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("final token = ");
                sb2.append(this.f3714b.mConfigParams.bindToken);
                ALog.d(str2, sb2.toString());
            } else {
                this.f3714b.updateBackupCheckTypeSet(EnumSet.of(BackupCheckType.CHECK_COAP_GET));
                this.f3714b.startBackupCheck(true, 0L);
            }
            PerformanceLog.trace(AlinkBroadcastConfigStrategy.TAG, AlinkConstants.KEY_BROADCAST, PerformanceLog.getJsonObject("type", "smartConfig"));
            this.f3714b.setSendLevel(1);
            this.f3714b.send();
            while (!this.f3714b.provisionHasStopped.get()) {
                this.f3714b.setSendLevel(1);
                Thread.sleep(this.f3714b.INTERVAL_UDP_SENDING);
                this.f3714b.setSendLevel(2);
                Thread.sleep(this.f3714b.INTERVAL_UDP_SENDING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
