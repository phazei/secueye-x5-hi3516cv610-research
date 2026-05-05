package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.broadcast.AlinkBroadcastConfigStrategy;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.utils.AlinkWifiSolutionUtils;
import java.net.InetAddress;
import java.net.MulticastSocket;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.s, reason: case insensitive filesystem */
/* JADX INFO: compiled from: AlinkBroadcastConfigStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class RunnableC0485s implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ AlinkBroadcastConfigStrategy f3716a;

    public RunnableC0485s(AlinkBroadcastConfigStrategy alinkBroadcastConfigStrategy) {
        this.f3716a = alinkBroadcastConfigStrategy;
    }

    @Override // java.lang.Runnable
    public void run() {
        ALog.d(AlinkBroadcastConfigStrategy.TAG, "start send multicast called!");
        try {
            String[] strArr = new String[2];
            strArr[0] = AlinkConstants.KEY_BROADCAST_MULTICAST;
            strArr[1] = String.valueOf(System.currentTimeMillis());
            DCUserTrack.addTrackData(strArr);
            int length = (this.f3716a.packetMulticastByteArray.length + 1) / 2;
            this.f3716a.udpSocketMulticast = new MulticastSocket();
            this.f3716a.udpSocketMulticast.setBroadcast(true);
            this.f3716a.udpSocketMulticast.setReuseAddress(true);
            AlinkWifiSolutionUtils.printByteArray(this.f3716a.packetMulticastByteArray);
            while (!this.f3716a.provisionHasStopped.get() && this.f3716a.isProvisioningAB.get()) {
                int i = 0;
                while (i < length && !this.f3716a.provisionHasStopped.get()) {
                    int i2 = i * 2;
                    int i3 = this.f3716a.packetMulticastByteArray[i2] & 255;
                    int i4 = (i == length + (-1) && this.f3716a.packetMulticastByteArray.length % 2 == 1) ? 0 : this.f3716a.packetMulticastByteArray[i2 + 1] & 255;
                    AlinkBroadcastConfigStrategy alinkBroadcastConfigStrategy = this.f3716a;
                    StringBuilder sb = new StringBuilder();
                    sb.append("239.");
                    sb.append(i);
                    sb.append(".");
                    sb.append(i3);
                    sb.append(".");
                    sb.append(i4);
                    alinkBroadcastConfigStrategy.addressMulticast = InetAddress.getByName(sb.toString());
                    this.f3716a.sendMulticastUdpPacket(this.f3716a.addressMulticast, 1);
                    i++;
                }
                Thread.sleep(this.f3716a.INTERVAL_UDP_Loop);
                String str = AlinkBroadcastConfigStrategy.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("multicast finish ");
                sb2.append(this.f3716a.INTERVAL_UDP_Loop);
                sb2.append("ms one loop send.");
                ALog.d(str, sb2.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AlinkBroadcastConfigStrategy alinkBroadcastConfigStrategy2 = this.f3716a;
        alinkBroadcastConfigStrategy2.closeSocket(alinkBroadcastConfigStrategy2.udpSocketMulticast);
    }
}
