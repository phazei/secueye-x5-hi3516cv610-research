package com.alibaba.sdk.android.tbrest.request;

import com.alibaba.sdk.android.tbrest.SendService;

/* JADX INFO: loaded from: classes.dex */
public class UrlWrapper {
    private static final int MAX_CONNECTION_TIME_OUT = 10000;
    private static final int MAX_READ_CONNECTION_STREAM_TIME_OUT = 60000;
    public static int mErrorCode;
    private static a mRestSslSocketFactory;

    static {
        System.setProperty("http.keepAlive", "true");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0185  */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v14 */
    /* JADX WARN: Type inference failed for: r1v15, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v16 */
    /* JADX WARN: Type inference failed for: r1v17, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v18 */
    /* JADX WARN: Type inference failed for: r1v19 */
    /* JADX WARN: Type inference failed for: r1v20 */
    /* JADX WARN: Type inference failed for: r1v31 */
    /* JADX WARN: Type inference failed for: r1v32 */
    /* JADX WARN: Type inference failed for: r1v33 */
    /* JADX WARN: Type inference failed for: r1v34 */
    /* JADX WARN: Type inference failed for: r1v35 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.alibaba.sdk.android.tbrest.request.BizResponse sendRequest(com.alibaba.sdk.android.tbrest.SendService r5, java.lang.String r6, java.lang.String r7, byte[] r8) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 504
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.tbrest.request.UrlWrapper.sendRequest(com.alibaba.sdk.android.tbrest.SendService, java.lang.String, java.lang.String, byte[]):com.alibaba.sdk.android.tbrest.request.BizResponse");
    }

    public static BizResponse sendRequest(SendService sendService, String str, byte[] bArr) {
        String str2;
        String str3 = sendService.appKey;
        if (sendService.openHttp.booleanValue()) {
            str2 = "http://" + str + "/upload";
        } else {
            str2 = "https://" + str + "/upload";
        }
        return sendRequest(sendService, str3, str2, bArr);
    }

    public static BizResponse sendRequestByUrl(SendService sendService, String str, byte[] bArr) {
        return sendRequest(sendService, sendService.appKey, str, bArr);
    }
}
