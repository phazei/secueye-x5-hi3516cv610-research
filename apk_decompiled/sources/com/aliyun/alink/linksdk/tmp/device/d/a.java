package com.aliyun.alink.linksdk.tmp.device.d;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.listener.IDiscoveryDeviceStateChangeListener;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: DiscoveryDeviceStateMgr.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4401a = "[Tmp]DiscoveryDeviceStateMgr";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Map<Integer, IDiscoveryDeviceStateChangeListener> f4402b;

    /* JADX INFO: renamed from: com.aliyun.alink.linksdk.tmp.device.d.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: DiscoveryDeviceStateMgr.java */
    private static class C0228a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        protected static a f4403a = new a();

        private C0228a() {
        }
    }

    public static a a() {
        return C0228a.f4403a;
    }

    private a() {
        this.f4402b = new ConcurrentHashMap();
    }

    public void a(IDiscoveryDeviceStateChangeListener iDiscoveryDeviceStateChangeListener) {
        ALog.d(f4401a, "addDiscoveryDeviceStateChangeListener listener:" + iDiscoveryDeviceStateChangeListener);
        if (iDiscoveryDeviceStateChangeListener == null) {
            return;
        }
        this.f4402b.put(Integer.valueOf(iDiscoveryDeviceStateChangeListener.hashCode()), iDiscoveryDeviceStateChangeListener);
    }

    public void b(IDiscoveryDeviceStateChangeListener iDiscoveryDeviceStateChangeListener) {
        ALog.d(f4401a, "removeDiscoveryDeviceStateChangeListener listener:" + iDiscoveryDeviceStateChangeListener);
        if (iDiscoveryDeviceStateChangeListener == null) {
            return;
        }
        this.f4402b.remove(Integer.valueOf(iDiscoveryDeviceStateChangeListener.hashCode()));
    }

    public void a(DeviceBasicData deviceBasicData, TmpEnum.DiscoveryDeviceState discoveryDeviceState) {
        StringBuilder sb = new StringBuilder();
        sb.append("onDiscoveryDeviceStateChange basicData:");
        sb.append(deviceBasicData == null ? "" : deviceBasicData.toString());
        sb.append(" state:");
        sb.append(discoveryDeviceState);
        sb.append(" mDiscoveryDevStateChangeListenerList:");
        sb.append(this.f4402b);
        ALog.d(f4401a, sb.toString());
        ALog.d(f4401a, JSONObject.toJSONString(deviceBasicData));
        Map<Integer, IDiscoveryDeviceStateChangeListener> map = this.f4402b;
        if (map == null || map.isEmpty()) {
            ALog.w(f4401a, "onDiscoveryDeviceStateChange mDiscoveryDevStateChangeListenerList empty");
            return;
        }
        Iterator<Map.Entry<Integer, IDiscoveryDeviceStateChangeListener>> it = this.f4402b.entrySet().iterator();
        while (it.hasNext()) {
            IDiscoveryDeviceStateChangeListener value = it.next().getValue();
            if (value != null) {
                value.onDiscoveryDeviceStateChange(deviceBasicData, discoveryDeviceState);
            }
        }
    }
}
