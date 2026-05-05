package anetwork.channel.download;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.SparseArray;
import anet.channel.util.ALog;
import anet.channel.util.HttpHelper;
import anet.channel.util.StringUtils;
import anetwork.channel.Header;
import anetwork.channel.aidl.Connection;
import anetwork.channel.http.NetworkSdkSetting;
import io.netty.handler.codec.http.HttpHeaders;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class DownloadManager {
    public static final int ERROR_DOWNLOAD_CANCELLED = -105;
    public static final int ERROR_EXCEPTION_HAPPEN = -104;
    public static final int ERROR_FILE_FOLDER_INVALID = -101;
    public static final int ERROR_FILE_RENAME_FAILED = -106;
    public static final int ERROR_IO_EXCEPTION = -103;
    public static final int ERROR_REQUEST_FAIL = -102;
    public static final int ERROR_URL_INVALID = -100;
    public static final String TAG = "anet.DownloadManager";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    SparseArray<b> f2014a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    AtomicInteger f2015b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    ThreadPoolExecutor f2016c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    Context f2017d;

    /* JADX INFO: compiled from: Taobao */
    public interface DownloadListener {
        void onFail(int i, int i2, String str);

        void onProgress(int i, long j, long j2);

        void onSuccess(int i, String str);
    }

    public static DownloadManager getInstance() {
        return a.f2018a;
    }

    private DownloadManager() {
        this.f2014a = new SparseArray<>(6);
        this.f2015b = new AtomicInteger(0);
        this.f2016c = new ThreadPoolExecutor(2, 2, 30L, TimeUnit.SECONDS, new LinkedBlockingDeque());
        this.f2017d = null;
        this.f2017d = NetworkSdkSetting.getContext();
        this.f2016c.allowCoreThreadTimeOut(true);
        a();
    }

    /* JADX INFO: compiled from: Taobao */
    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static DownloadManager f2018a = new DownloadManager();

        private a() {
        }
    }

    public int enqueue(String str, String str2, DownloadListener downloadListener) {
        return enqueue(str, null, str2, downloadListener);
    }

    public int enqueue(String str, String str2, String str3, DownloadListener downloadListener) {
        int i = 0;
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "enqueue", null, "folder", str2, "filename", str3, "url", str);
        }
        if (this.f2017d == null) {
            ALog.e(TAG, "network sdk not initialized.", null, new Object[0]);
            return -1;
        }
        try {
            URL url = new URL(str);
            if (!TextUtils.isEmpty(str2) && !a(str2)) {
                ALog.e(TAG, "file folder invalid.", null, new Object[0]);
                if (downloadListener != null) {
                    downloadListener.onFail(-1, -101, "file folder path invalid");
                }
                return -1;
            }
            synchronized (this.f2014a) {
                int size = this.f2014a.size();
                while (true) {
                    if (i >= size) {
                        break;
                    }
                    b bVarValueAt = this.f2014a.valueAt(i);
                    if (!url.equals(bVarValueAt.f2020b)) {
                        i++;
                    } else if (bVarValueAt.a(downloadListener)) {
                        return bVarValueAt.f2019a;
                    }
                }
                b bVar = new b(url, str2, str3, downloadListener);
                this.f2014a.put(bVar.f2019a, bVar);
                this.f2016c.submit(bVar);
                return bVar.f2019a;
            }
        } catch (MalformedURLException e) {
            ALog.e(TAG, "url invalid.", null, e, new Object[0]);
            if (downloadListener != null) {
                downloadListener.onFail(-1, -100, "url invalid");
            }
            return -1;
        }
    }

    public void cancel(int i) {
        synchronized (this.f2014a) {
            b bVar = this.f2014a.get(i);
            if (bVar != null) {
                if (ALog.isPrintLog(2)) {
                    ALog.i(TAG, "try cancel task" + i + " url=" + bVar.f2020b.toString(), null, new Object[0]);
                }
                this.f2014a.remove(i);
                bVar.a();
            }
        }
    }

    /* JADX INFO: compiled from: Taobao */
    class b implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final int f2019a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final URL f2020b;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final String f2022d;
        private final CopyOnWriteArrayList<DownloadListener> e;
        private final AtomicBoolean f = new AtomicBoolean(false);
        private final AtomicBoolean g = new AtomicBoolean(false);
        private volatile Connection h = null;
        private boolean i;

        b(URL url, String str, String str2, DownloadListener downloadListener) {
            this.i = true;
            this.f2019a = DownloadManager.this.f2015b.getAndIncrement();
            this.f2020b = url;
            str2 = TextUtils.isEmpty(str2) ? a(url) : str2;
            if (TextUtils.isEmpty(str)) {
                this.f2022d = DownloadManager.this.b(str2);
            } else {
                if (str.endsWith("/")) {
                    this.f2022d = str + str2;
                } else {
                    this.f2022d = str + '/' + str2;
                }
                if (str.startsWith("/data/user") || str.startsWith("/data/data")) {
                    this.i = false;
                }
            }
            this.e = new CopyOnWriteArrayList<>();
            this.e.add(downloadListener);
        }

        public boolean a(DownloadListener downloadListener) {
            if (this.g.get()) {
                return false;
            }
            this.e.add(downloadListener);
            return true;
        }

        public void a() {
            this.f.set(true);
            a(-105, "download canceled.");
            if (this.h != null) {
                try {
                    this.h.cancel();
                } catch (RemoteException unused) {
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:194:0x0268 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:200:0x0263 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:202:0x0270 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:205:0x025e A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:223:0x023a A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:229:0x024c A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:233:0x0244 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:244:0x023f A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 638
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: anetwork.channel.download.DownloadManager.b.run():void");
        }

        private void a(String str) {
            if (this.g.compareAndSet(false, true)) {
                Iterator<DownloadListener> it = this.e.iterator();
                while (it.hasNext()) {
                    it.next().onSuccess(this.f2019a, str);
                }
            }
        }

        private void a(int i, String str) {
            if (this.g.compareAndSet(false, true)) {
                Iterator<DownloadListener> it = this.e.iterator();
                while (it.hasNext()) {
                    it.next().onFail(this.f2019a, i, str);
                }
            }
        }

        private void a(long j, long j2) {
            if (this.g.get()) {
                return;
            }
            Iterator<DownloadListener> it = this.e.iterator();
            while (it.hasNext()) {
                it.next().onProgress(this.f2019a, j, j2);
            }
        }

        private long a(int i, Map<String, List<String>> map, long j) {
            int iLastIndexOf;
            try {
                if (i == 200) {
                    return Long.parseLong(HttpHelper.getSingleHeaderFieldByKey(map, "Content-Length"));
                }
                if (i != 206) {
                    return 0L;
                }
                String singleHeaderFieldByKey = HttpHelper.getSingleHeaderFieldByKey(map, HttpHeaders.Names.CONTENT_RANGE);
                long j2 = (singleHeaderFieldByKey == null || (iLastIndexOf = singleHeaderFieldByKey.lastIndexOf(47)) == -1) ? 0L : Long.parseLong(singleHeaderFieldByKey.substring(iLastIndexOf + 1));
                if (j2 != 0) {
                    return j2;
                }
                try {
                    return Long.parseLong(HttpHelper.getSingleHeaderFieldByKey(map, "Content-Length")) + j;
                } catch (Exception unused) {
                    return j2;
                }
            } catch (Exception unused2) {
                return 0L;
            }
        }

        private void a(List<Header> list) {
            if (list != null) {
                ListIterator<Header> listIterator = list.listIterator();
                while (listIterator.hasNext()) {
                    if ("Range".equalsIgnoreCase(listIterator.next().getName())) {
                        listIterator.remove();
                        return;
                    }
                }
            }
        }

        private String a(URL url) {
            String path = url.getPath();
            int iLastIndexOf = path.lastIndexOf(47);
            String strSubstring = iLastIndexOf != -1 ? path.substring(iLastIndexOf + 1, path.length()) : null;
            if (!TextUtils.isEmpty(strSubstring)) {
                return strSubstring;
            }
            String strMd5ToHex = StringUtils.md5ToHex(url.toString());
            return strMd5ToHex == null ? url.getFile() : strMd5ToHex;
        }
    }

    private void a() {
        Context context = this.f2017d;
        if (context != null) {
            File file = new File(context.getExternalFilesDir(null), "downloads");
            if (file.exists()) {
                return;
            }
            file.mkdir();
        }
    }

    private boolean a(String str) {
        if (this.f2017d != null) {
            try {
                File file = new File(str);
                if (file.exists()) {
                    return true;
                }
                return file.mkdir();
            } catch (Exception unused) {
                ALog.e(TAG, "create folder failed", null, "folder", str);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String b(String str) {
        StringBuilder sb = new StringBuilder(32);
        sb.append(this.f2017d.getExternalFilesDir(null));
        sb.append("/");
        sb.append("downloads");
        sb.append("/");
        sb.append(str);
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File a(String str, boolean z) {
        String strMd5ToHex = StringUtils.md5ToHex(str);
        if (strMd5ToHex != null) {
            str = strMd5ToHex;
        }
        if (z) {
            return new File(this.f2017d.getExternalCacheDir(), str);
        }
        return new File(this.f2017d.getCacheDir(), str);
    }
}
