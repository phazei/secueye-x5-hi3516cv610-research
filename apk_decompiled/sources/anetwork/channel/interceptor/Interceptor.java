package anetwork.channel.interceptor;

import anet.channel.request.Request;
import java.util.concurrent.Future;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public interface Interceptor {

    /* JADX INFO: compiled from: Taobao */
    public interface Chain {
        Callback callback();

        Future proceed(Request request, Callback callback);

        Request request();
    }

    Future intercept(Chain chain);
}
