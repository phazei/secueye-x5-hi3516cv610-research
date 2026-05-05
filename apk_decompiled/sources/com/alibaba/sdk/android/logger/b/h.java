package com.alibaba.sdk.android.logger.b;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.sdk.android.logger.ILogger;
import com.alibaba.sdk.android.logger.LogLevel;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class h implements com.alibaba.sdk.android.logger.interceptor.d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String[] f2916a = {TmpConstant.GROUP_ROLE_UNKNOWN};

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String[] f2917b = {"[]"};

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private com.alibaba.sdk.android.logger.a.a f2918c;

    public h(com.alibaba.sdk.android.logger.a.a aVar) {
        this.f2918c = aVar;
    }

    private String[] a(Object[] objArr) {
        if (objArr == null) {
            return f2916a;
        }
        if (objArr.length == 0) {
            return f2917b;
        }
        String[] strArr = new String[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            strArr[i] = this.f2918c.a(objArr[i]);
        }
        return strArr;
    }

    private String[] a(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (String str : strArr) {
            if (str == null) {
                str = "";
            }
            if (z) {
                z = false;
            } else {
                sb.append(" ");
            }
            if (str.contains(SdkConstant.CLOUDAPI_LF)) {
                String[] strArrSplit = str.split(SdkConstant.CLOUDAPI_LF);
                boolean z2 = z;
                StringBuilder sb2 = sb;
                for (int i = 0; i < strArrSplit.length; i++) {
                    sb2.append(strArrSplit[i]);
                    if (i < strArrSplit.length - 1) {
                        arrayList.add(sb2.toString());
                        sb2 = new StringBuilder();
                    } else if (i == strArrSplit.length - 1 && str.endsWith(SdkConstant.CLOUDAPI_LF)) {
                        arrayList.add(sb2.toString());
                        sb2 = new StringBuilder();
                        z2 = true;
                    }
                }
                sb = sb2;
                z = z2;
            } else {
                sb.append(str);
            }
        }
        arrayList.add(sb.toString());
        return (String[]) arrayList.toArray(new String[0]);
    }

    @Override // com.alibaba.sdk.android.logger.interceptor.d
    public void a(LogLevel logLevel, String str, Object[] objArr, ILogger iLogger) {
        for (String str2 : a(a(objArr))) {
            iLogger.print(logLevel, str, str2);
        }
    }
}
