package com.alibaba.sdk.android.utils;

import android.content.Context;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.xiaomi.mipush.sdk.Constants;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class AMSDevReporter {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Context f3206a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private static final ExecutorService f29a = Executors.newSingleThreadExecutor(new a());

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private static ConcurrentHashMap<AMSSdkTypeEnum, AMSReportStatusEnum> f28a = new ConcurrentHashMap<>();

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private static boolean f30a = false;
    private static String TAG = "AMSDevReporter";

    public enum AMSReportStatusEnum {
        UNREPORTED,
        REPORTED
    }

    public enum AMSSdkTypeEnum {
        AMS_MAN("MAN"),
        AMS_HTTPDNS("HTTPDNS"),
        AMS_MPUSH("MPUSH"),
        AMS_MAC(TmpConstant.DATA_KEY_DEVICENAME),
        AMS_API("API"),
        AMS_HOTFIX("HOTFIX"),
        AMS_FEEDBACK("FEEDBACK"),
        AMS_IM("IM");

        private String description;

        AMSSdkTypeEnum(String str) {
            this.description = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.description;
        }
    }

    public enum AMSSdkExtInfoKeyEnum {
        AMS_EXTINFO_KEY_VERSION("SdkVersion"),
        AMS_EXTINFO_KEY_PACKAGE("PackageName");

        private String description;

        AMSSdkExtInfoKeyEnum(String str) {
            this.description = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.description;
        }
    }

    static {
        for (AMSSdkTypeEnum aMSSdkTypeEnum : AMSSdkTypeEnum.values()) {
            f28a.put(aMSSdkTypeEnum, AMSReportStatusEnum.UNREPORTED);
        }
    }

    public static void setLogEnabled(boolean z) {
        d.setLogEnabled(z);
    }

    public static AMSReportStatusEnum getReportStatus(AMSSdkTypeEnum aMSSdkTypeEnum) {
        return f28a.get(aMSSdkTypeEnum);
    }

    public static void asyncReport(Context context, AMSSdkTypeEnum aMSSdkTypeEnum) {
        asyncReport(context, aMSSdkTypeEnum, null);
    }

    public static void asyncReport(Context context, final AMSSdkTypeEnum aMSSdkTypeEnum, final Map<String, Object> map) {
        if (context == null) {
            d.c(TAG, "Context is null, return.");
            return;
        }
        f3206a = context;
        d.b(TAG, "Add [" + aMSSdkTypeEnum.toString() + "] to report queue.");
        f30a = false;
        f29a.execute(new Runnable() { // from class: com.alibaba.sdk.android.utils.AMSDevReporter.1
            @Override // java.lang.Runnable
            public void run() {
                if (AMSDevReporter.f30a) {
                    d.c(AMSDevReporter.TAG, "Unable to execute remain task in queue, return.");
                    return;
                }
                d.b(AMSDevReporter.TAG, "Get [" + aMSSdkTypeEnum.toString() + "] from report queue.");
                AMSDevReporter.a(aMSSdkTypeEnum, (Map<String, Object>) map);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void a(AMSSdkTypeEnum aMSSdkTypeEnum, Map<String, Object> map) {
        String string = aMSSdkTypeEnum.toString();
        if (f28a.get(aMSSdkTypeEnum) != AMSReportStatusEnum.UNREPORTED) {
            d.b(TAG, "[" + string + "] already reported, return.");
            return;
        }
        int i = 0;
        int i2 = 5;
        while (true) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Report [");
            sb.append(string);
            sb.append("], times: [");
            i++;
            sb.append(i);
            sb.append("].");
            d.b(str, sb.toString());
            if (!m26a(aMSSdkTypeEnum, map)) {
                if (i <= 10) {
                    d.b(TAG, "Report [" + string + "] failed, wait for [" + i2 + "] seconds.");
                    e.a((double) i2);
                    i2 *= 2;
                    if (i2 >= 60) {
                        i2 = 60;
                    }
                } else {
                    d.c(TAG, "Report [" + string + "] stat failed, exceed max retry times, return.");
                    f28a.put(aMSSdkTypeEnum, AMSReportStatusEnum.UNREPORTED);
                    f30a = true;
                    break;
                }
            } else {
                d.b(TAG, "Report [" + string + "] stat success.");
                f28a.put(aMSSdkTypeEnum, AMSReportStatusEnum.REPORTED);
                break;
            }
        }
        if (f30a) {
            d.c(TAG, "Report [" + string + "] failed, clear remain report in queue.");
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(7:11|(2:96|12)|(3:13|(1:15)(1:98)|68)|16|83|17|(4:(1:20)|(2:90|22)|25|28)) */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0168, code lost:
    
        r1 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0169, code lost:
    
        com.alibaba.sdk.android.utils.d.a(com.alibaba.sdk.android.utils.AMSDevReporter.TAG, r1);
     */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01de  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01eb A[Catch: IOException -> 0x01e7, TRY_LEAVE, TryCatch #8 {IOException -> 0x01e7, blocks: (B:75:0x01e3, B:79:0x01eb), top: B:86:0x01e3 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x01e3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:99:? A[SYNTHETIC] */
    /* JADX INFO: renamed from: a, reason: collision with other method in class */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static boolean m26a(com.alibaba.sdk.android.utils.AMSDevReporter.AMSSdkTypeEnum r8, java.util.Map<java.lang.String, java.lang.Object> r9) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 501
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.utils.AMSDevReporter.m26a(com.alibaba.sdk.android.utils.AMSDevReporter$AMSSdkTypeEnum, java.util.Map):boolean");
    }

    private static String a(AMSSdkTypeEnum aMSSdkTypeEnum, String str, Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(aMSSdkTypeEnum);
        sb.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
        sb.append(str);
        if (map != null) {
            String str2 = (String) map.get(AMSSdkExtInfoKeyEnum.AMS_EXTINFO_KEY_VERSION.toString());
            if (!e.m33a(str2)) {
                sb.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                sb.append(str2);
            }
            String str3 = (String) map.get(AMSSdkExtInfoKeyEnum.AMS_EXTINFO_KEY_PACKAGE.toString());
            if (!e.m33a(str3)) {
                sb.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                sb.append(str3);
            }
        }
        return sb.toString();
    }
}
