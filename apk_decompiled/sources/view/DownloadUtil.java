package view;

import androidx.annotation.NonNull;
import java.io.File;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/* JADX INFO: loaded from: classes5.dex */
public class DownloadUtil {
    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient = new OkHttpClient();

    public interface OnDownloadListener {
        void onDownloadFailed();

        void onDownloadSuccess();

        void onDownloading(int i);
    }

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
    }

    public void download(String str, final String str2, final String str3, final OnDownloadListener onDownloadListener) {
        this.okHttpClient.newCall(new Request.Builder().url(str).build()).enqueue(new Callback() { // from class: view.DownloadUtil.1
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                onDownloadListener.onDownloadFailed();
            }

            /* JADX WARN: Removed duplicated region for block: B:53:0x008d A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:55:0x0088 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:66:? A[SYNTHETIC] */
            @Override // okhttp3.Callback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onResponse(@androidx.annotation.NonNull okhttp3.Call r11, @androidx.annotation.NonNull okhttp3.Response r12) throws java.lang.Throwable {
                /*
                    r10 = this;
                    r11 = 2048(0x800, float:2.87E-42)
                    byte[] r11 = new byte[r11]
                    view.DownloadUtil r0 = view.DownloadUtil.this
                    java.lang.String r1 = r3
                    java.lang.String r0 = view.DownloadUtil.access$000(r0, r1)
                    r1 = 0
                    okhttp3.ResponseBody r2 = r12.body()     // Catch: java.lang.Throwable -> L61 java.lang.Exception -> L65
                    java.io.InputStream r2 = r2.byteStream()     // Catch: java.lang.Throwable -> L61 java.lang.Exception -> L65
                    okhttp3.ResponseBody r12 = r12.body()     // Catch: java.lang.Throwable -> L5b java.lang.Exception -> L5e
                    long r3 = r12.contentLength()     // Catch: java.lang.Throwable -> L5b java.lang.Exception -> L5e
                    java.io.File r12 = new java.io.File     // Catch: java.lang.Throwable -> L5b java.lang.Exception -> L5e
                    java.lang.String r5 = r4     // Catch: java.lang.Throwable -> L5b java.lang.Exception -> L5e
                    r12.<init>(r0, r5)     // Catch: java.lang.Throwable -> L5b java.lang.Exception -> L5e
                    java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L5b java.lang.Exception -> L5e
                    r5.<init>(r12)     // Catch: java.lang.Throwable -> L5b java.lang.Exception -> L5e
                    r6 = 0
                L2b:
                    int r12 = r2.read(r11)     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5f
                    r1 = -1
                    if (r12 == r1) goto L48
                    r1 = 0
                    r5.write(r11, r1, r12)     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5f
                    long r8 = (long) r12     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5f
                    long r6 = r6 + r8
                    float r12 = (float) r6     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5f
                    r1 = 1065353216(0x3f800000, float:1.0)
                    float r12 = r12 * r1
                    float r1 = (float) r3     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5f
                    float r12 = r12 / r1
                    r1 = 1120403456(0x42c80000, float:100.0)
                    float r12 = r12 * r1
                    int r12 = (int) r12     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5f
                    view.DownloadUtil$OnDownloadListener r1 = r2     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5f
                    r1.onDownloading(r12)     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5f
                    goto L2b
                L48:
                    r5.flush()     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5f
                    view.DownloadUtil$OnDownloadListener r11 = r2     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5f
                    r11.onDownloadSuccess()     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5f
                    if (r2 == 0) goto L55
                    r2.close()     // Catch: java.io.IOException -> L55
                L55:
                    r5.close()     // Catch: java.io.IOException -> L83
                    goto L83
                L59:
                    r11 = move-exception
                    goto L86
                L5b:
                    r11 = move-exception
                    r5 = r1
                    goto L86
                L5e:
                    r5 = r1
                L5f:
                    r1 = r2
                    goto L66
                L61:
                    r11 = move-exception
                    r2 = r1
                    r5 = r2
                    goto L86
                L65:
                    r5 = r1
                L66:
                    java.io.File r11 = new java.io.File     // Catch: java.lang.Throwable -> L84
                    java.lang.String r12 = r4     // Catch: java.lang.Throwable -> L84
                    r11.<init>(r0, r12)     // Catch: java.lang.Throwable -> L84
                    boolean r12 = r11.exists()     // Catch: java.lang.Throwable -> L84
                    if (r12 == 0) goto L76
                    r11.delete()     // Catch: java.lang.Throwable -> L84
                L76:
                    view.DownloadUtil$OnDownloadListener r11 = r2     // Catch: java.lang.Throwable -> L84
                    r11.onDownloadFailed()     // Catch: java.lang.Throwable -> L84
                    if (r1 == 0) goto L80
                    r1.close()     // Catch: java.io.IOException -> L80
                L80:
                    if (r5 == 0) goto L83
                    goto L55
                L83:
                    return
                L84:
                    r11 = move-exception
                    r2 = r1
                L86:
                    if (r2 == 0) goto L8b
                    r2.close()     // Catch: java.io.IOException -> L8b
                L8b:
                    if (r5 == 0) goto L90
                    r5.close()     // Catch: java.io.IOException -> L90
                L90:
                    throw r11
                */
                throw new UnsupportedOperationException("Method not decompiled: view.DownloadUtil.AnonymousClass1.onResponse(okhttp3.Call, okhttp3.Response):void");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isExistDir(String str) {
        File file = new File(str, "/video");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getPath();
    }

    @NonNull
    private String getNameFromUrl(String str) {
        return str.substring(str.lastIndexOf("/") + 1);
    }
}
