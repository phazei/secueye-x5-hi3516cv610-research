package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.broadcast.AlinkBroadcastConfigStrategy;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.google.android.exoplayer2.DefaultLoadControl;
import java.net.DatagramSocket;
import java.net.InetAddress;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.r, reason: case insensitive filesystem */
/* JADX INFO: compiled from: AlinkBroadcastConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class RunnableC0484r implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AlinkBroadcastConfigStrategy f3715a;

    public RunnableC0484r(AlinkBroadcastConfigStrategy alinkBroadcastConfigStrategy) {
        this.f3715a = alinkBroadcastConfigStrategy;
    }

    @Override // java.lang.Runnable
    public void run() {
        ALog.d(AlinkBroadcastConfigStrategy.TAG, "startProvisonThread run!");
        try {
            this.f3715a.address = InetAddress.getByName("255.255.255.255");
            this.f3715a.udpSocket = new DatagramSocket();
            this.f3715a.udpSocket.setBroadcast(true);
            this.f3715a.udpSocket.setReuseAddress(true);
            if (!AlinkHelper.isBatchBroadcast(this.f3715a.mConfigParams) && this.f3715a.delayBroadcastTimeAI.get() > 0) {
                Thread.sleep(this.f3715a.delayBroadcastTimeAI.get() + 200);
            }
            String[] strArr = new String[2];
            strArr[0] = AlinkConstants.KEY_BROADCAST;
            strArr[1] = String.valueOf(System.currentTimeMillis());
            DCUserTrack.addTrackData(strArr);
            ALog.d(AlinkBroadcastConfigStrategy.TAG, "start send broadcast packet.");
            while (!this.f3715a.provisionHasStopped.get() && this.f3715a.isProvisioningAB.get()) {
                this.f3715a.port = DefaultLoadControl.DEFAULT_MAX_BUFFER_MS;
                this.f3715a.portIndex = 0;
                this.f3715a.UDP_SEND(1248);
                this.f3715a.UDP_SEND(1248);
                this.f3715a.UDP_SEND(1248);
                this.f3715a.UDP_SEND(1248);
                this.f3715a.UDP_SEND(1248);
                this.f3715a.UDP_SEND(1248);
                byte b2 = this.f3715a.send_data[0];
                int i = 2;
                int i2 = 0;
                for (int i3 = 0; i3 < b2; i3++) {
                    this.f3715a.UDP_SEND((i * 128) + (this.f3715a.send_data[i3] & 255));
                    if (i3 % 8 == 7) {
                        i2++;
                        int i4 = i2 + 992;
                        this.f3715a.UDP_SEND(i4);
                        this.f3715a.UDP_SEND(i4);
                        if (this.f3715a.provisionHasStopped.get()) {
                            break;
                        }
                    }
                    i++;
                    if (i == 10) {
                        i = 2;
                    }
                }
                if (this.f3715a.provisionHasStopped.get()) {
                    break;
                }
                Thread.sleep(this.f3715a.INTERVAL_UDP_Loop);
                String str = AlinkBroadcastConfigStrategy.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("broadcast finish ");
                sb.append(this.f3715a.INTERVAL_UDP_Loop);
                sb.append("ms one loop send.");
                ALog.d(str, sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AlinkBroadcastConfigStrategy alinkBroadcastConfigStrategy = this.f3715a;
        alinkBroadcastConfigStrategy.closeSocket(alinkBroadcastConfigStrategy.udpSocket);
    }
}
