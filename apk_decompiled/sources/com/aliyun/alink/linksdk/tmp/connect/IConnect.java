package com.aliyun.alink.linksdk.tmp.connect;

import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.data.discovery.DiscoveryConfig;
import com.aliyun.alink.linksdk.tmp.event.INotifyHandler;
import com.aliyun.alink.linksdk.tmp.listener.IDevStateChangeListener;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;

/* JADX INFO: loaded from: classes2.dex */
public interface IConnect {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final int f4235b = 1;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final int f4236c = 2;

    TmpEnum.ConnectType a();

    String a(TmpEnum.ConnectType connectType);

    boolean a(int i, DiscoveryConfig discoveryConfig, INotifyHandler iNotifyHandler);

    boolean a(d dVar, c cVar);

    boolean a(d dVar, c cVar, INotifyHandler iNotifyHandler);

    boolean a(INotifyHandler iNotifyHandler);

    boolean a(IDevStateChangeListener iDevStateChangeListener);

    boolean a(String str, int i, Object obj);

    boolean a(String str, String str2);

    boolean a(String str, String str2, String str3, OutputParams outputParams, IPublishResourceListener iPublishResourceListener);

    boolean a(String str, String str2, String str3, boolean z, com.aliyun.alink.linksdk.tmp.resource.b bVar);

    boolean a(String str, String str2, byte[] bArr, IPublishResourceListener iPublishResourceListener);

    boolean b();

    boolean b(d dVar, c cVar);

    boolean b(IDevStateChangeListener iDevStateChangeListener);

    boolean c();

    boolean c(d dVar, c cVar);

    TmpEnum.DeviceState d();

    boolean e();

    boolean f();

    public enum TConnectState {
        UNKNOW(0),
        CONNECT(1),
        DISCONNECT(2);

        private int value;

        TConnectState(int i) {
            this.value = i;
        }

        public static TConnectState createConnectState(ConnectState connectState) {
            if (connectState == ConnectState.CONNECTED) {
                return CONNECT;
            }
            if (connectState == ConnectState.DISCONNECTED) {
                return DISCONNECT;
            }
            return UNKNOW;
        }
    }
}
