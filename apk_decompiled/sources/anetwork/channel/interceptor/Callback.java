package anetwork.channel.interceptor;

import anet.channel.bytes.ByteArray;
import anetwork.channel.aidl.DefaultFinishEvent;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public interface Callback {
    void onDataReceiveSize(int i, int i2, ByteArray byteArray);

    void onFinish(DefaultFinishEvent defaultFinishEvent);

    void onResponseCode(int i, Map<String, List<String>> map);
}
