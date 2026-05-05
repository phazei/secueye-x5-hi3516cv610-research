package a.a.a.a.b.d;

import a.a.a.a.b.m.j;
import android.os.Build;
import com.alibaba.ailabs.iot.aisbase.env.AppEnv;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: compiled from: AppEnv.java */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final boolean f1315a = a();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final boolean f1316b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static boolean f1317c = false;

    static {
        f1316b = j.a("com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient") && !f1315a;
        if (AgooConstants.ACK_REMOVE_PACKAGE.equals(Build.VERSION.RELEASE) && "STK-AL00".equals(Build.MODEL) && "STK-AL00".equals(Build.BOARD)) {
            f1317c = true;
        }
    }

    public static boolean a() {
        try {
            Class.forName(AppEnv.MTOP_CLASS_NAME);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
