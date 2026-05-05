package anetwork.channel;

import anetwork.channel.NetworkEvent;
import anetwork.channel.aidl.ParcelableInputStream;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class NetworkCallBack {

    /* JADX INFO: compiled from: Taobao */
    public interface FinishListener extends NetworkListener {
        void onFinished(NetworkEvent.FinishEvent finishEvent, Object obj);
    }

    /* JADX INFO: compiled from: Taobao */
    public interface InputStreamListener extends NetworkListener {
        void onInputStreamGet(ParcelableInputStream parcelableInputStream, Object obj);
    }

    /* JADX INFO: compiled from: Taobao */
    public interface ProgressListener extends NetworkListener {
        void onDataReceived(NetworkEvent.ProgressEvent progressEvent, Object obj);
    }

    /* JADX INFO: compiled from: Taobao */
    public interface ResponseCodeListener extends NetworkListener {
        boolean onResponseCode(int i, Map<String, List<String>> map, Object obj);
    }
}
