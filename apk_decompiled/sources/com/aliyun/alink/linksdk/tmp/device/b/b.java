package com.aliyun.alink.linksdk.tmp.device.b;

import android.os.AsyncTask;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.listener.IDiscoveryDeviceStateChangeListener;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: AutoConnecterMgr.java */
/* JADX INFO: loaded from: classes2.dex */
public class b implements IDiscoveryDeviceStateChangeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4382a = "[Tmp]AutoConnecterMgr";

    public void a() {
        com.aliyun.alink.linksdk.tmp.device.d.a.a().a(this);
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDiscoveryDeviceStateChangeListener
    public void onDiscoveryDeviceStateChange(final DeviceBasicData deviceBasicData, TmpEnum.DiscoveryDeviceState discoveryDeviceState) {
        ALog.d(f4382a, "onDiscoveryDeviceStateChange state:" + discoveryDeviceState + " basicData:" + deviceBasicData);
        if (TmpEnum.DiscoveryDeviceState.DISCOVERY_STATE_ONLINE == discoveryDeviceState) {
            AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.device.b.b.1
                @Override // java.lang.Runnable
                public void run() {
                    new a(deviceBasicData).a();
                }
            });
        }
    }
}
