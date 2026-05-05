package com.aliyun.alink.linksdk.alcs.lpbs.a.a;

import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IDataDownListener;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel;
import com.aliyun.alink.linksdk.alcs.lpbs.utils.TextHelper;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: CloudChannelProxy.java */
/* JADX INFO: loaded from: classes2.dex */
public class b implements IDataDownListener, IThingCloudChannel {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f3959a = "[AlcsLPBS]CloudChannelProxy";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private IThingCloudChannel f3960b;

    public b(IThingCloudChannel iThingCloudChannel, IDataDownListener iDataDownListener) {
        this.f3960b = iThingCloudChannel;
        IThingCloudChannel iThingCloudChannel2 = this.f3960b;
        if (iThingCloudChannel2 != null) {
            iThingCloudChannel2.addDownDataListener(iDataDownListener);
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IDataDownListener
    public void onDataDown(String str, byte[] bArr) {
        ALog.d(f3959a, "onDataDown topic:" + str + " payload hex:" + TextHelper.byte2hex(bArr) + " mChannel:" + this.f3960b);
    }

    IThingCloudChannel a() {
        return this.f3960b;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel
    public void reportData(String str, byte[] bArr) {
        if (this.f3960b != null || bArr != null) {
            ALog.d(f3959a, "reportData topic:" + str + " payload hex:" + TextHelper.byte2hex(bArr) + " payload str:" + new String(bArr) + " mChannel:" + this.f3960b);
            this.f3960b.reportData(str, bArr);
            return;
        }
        ALog.e(f3959a, "reportData topic:" + str + " payload:" + bArr + " mChannel :" + this.f3960b);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel
    public void reportData(String str, Object obj, IThingCloudChannel.IChannelActionListener iChannelActionListener) {
        if (this.f3960b != null || obj != null) {
            ALog.d(f3959a, "reportData topic:" + str + " payload " + obj + " mChannel:" + this.f3960b + " listener:" + iChannelActionListener);
            this.f3960b.reportData(str, obj, iChannelActionListener);
            return;
        }
        ALog.e(f3959a, "reportData topic:" + str + " payload:" + obj + " mChannel :" + this.f3960b + " listener:" + iChannelActionListener);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel
    public void addDownDataListener(IDataDownListener iDataDownListener) {
        ALog.d(f3959a, "addDownDataListener listener:" + iDataDownListener + " mChannel:" + this.f3960b);
        IThingCloudChannel iThingCloudChannel = this.f3960b;
        if (iThingCloudChannel != null) {
            iThingCloudChannel.addDownDataListener(iDataDownListener);
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel
    public void removeDownDataListener(IDataDownListener iDataDownListener) {
        ALog.d(f3959a, "removeDownDataListener listener:" + iDataDownListener + " mChannel:" + this.f3960b);
        IThingCloudChannel iThingCloudChannel = this.f3960b;
        if (iThingCloudChannel != null) {
            iThingCloudChannel.removeDownDataListener(iDataDownListener);
        }
    }
}
