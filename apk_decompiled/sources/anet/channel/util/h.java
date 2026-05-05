package anet.channel.util;

import anet.channel.request.Request;
import anet.channel.thread.ThreadPoolExecutorFactory;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class h {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Map<String, Integer> f1952a = new HashMap();

    static {
        f1952a.put("tpatch", 3);
        f1952a.put("so", 3);
        f1952a.put("json", 3);
        f1952a.put("html", 4);
        f1952a.put("htm", 4);
        f1952a.put("css", 5);
        f1952a.put("js", 5);
        f1952a.put("webp", 6);
        f1952a.put("png", 6);
        f1952a.put("jpg", 6);
        f1952a.put("do", 6);
        f1952a.put("zip", Integer.valueOf(ThreadPoolExecutorFactory.Priority.LOW));
        f1952a.put("bin", Integer.valueOf(ThreadPoolExecutorFactory.Priority.LOW));
        f1952a.put("apk", Integer.valueOf(ThreadPoolExecutorFactory.Priority.LOW));
    }

    public static int a(Request request) {
        Integer num;
        if (request == null) {
            throw new NullPointerException("url is null!");
        }
        if (request.getHeaders().containsKey(HttpConstant.X_PV)) {
            return 1;
        }
        String strTrySolveFileExtFromUrlPath = HttpHelper.trySolveFileExtFromUrlPath(request.getHttpUrl().path());
        if (strTrySolveFileExtFromUrlPath == null || (num = f1952a.get(strTrySolveFileExtFromUrlPath)) == null) {
            return 6;
        }
        return num.intValue();
    }
}
