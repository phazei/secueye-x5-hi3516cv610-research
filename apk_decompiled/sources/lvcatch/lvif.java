package lvcatch;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/* JADX INFO: loaded from: classes4.dex */
public class lvif {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private static volatile long f7974lvdo;

    private static DateFormat lvdo() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return simpleDateFormat;
    }

    public static Date lvdo(String str) throws ParseException {
        return lvdo().parse(str);
    }

    public static synchronized void lvdo(long j) {
        f7974lvdo = j - System.currentTimeMillis();
    }
}
