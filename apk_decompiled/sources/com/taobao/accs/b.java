package com.taobao.accs;

import android.app.Notification;
import com.taobao.accs.ChannelService;
import com.taobao.accs.utl.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ ChannelService.KernelService f6282a;

    b(ChannelService.KernelService kernelService) {
        this.f6282a = kernelService;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            ChannelService channelService = ChannelService.getInstance();
            int i = this.f6282a.f6274b.getPackageManager().getPackageInfo(this.f6282a.getPackageName(), 0).applicationInfo.icon;
            if (i != 0) {
                Notification.Builder builder = new Notification.Builder(this.f6282a.f6274b);
                builder.setSmallIcon(i);
                channelService.startForeground(9371, builder.build());
                Notification.Builder builder2 = new Notification.Builder(this.f6282a.f6274b);
                builder2.setSmallIcon(i);
                ChannelService.KernelService.f6273a.startForeground(9371, builder2.build());
                ChannelService.KernelService.f6273a.stopForeground(true);
            }
            ChannelService.KernelService.f6273a.stopSelf();
        } catch (Throwable th) {
            ALog.e("ChannelService", " onStartCommand run", th, new Object[0]);
        }
    }
}
