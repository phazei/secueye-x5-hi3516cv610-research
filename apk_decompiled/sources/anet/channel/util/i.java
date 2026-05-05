package anet.channel.util;

import android.text.TextUtils;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AtomicInteger f1953a = new AtomicInteger();

    public static String a(String str) {
        if (f1953a.get() == Integer.MAX_VALUE) {
            f1953a.set(0);
        }
        if (!TextUtils.isEmpty(str)) {
            return StringUtils.concatString(str, ".AWCN", String.valueOf(f1953a.incrementAndGet()));
        }
        return StringUtils.concatString("AWCN", String.valueOf(f1953a.incrementAndGet()));
    }
}
