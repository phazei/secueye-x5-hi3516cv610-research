package com.alibaba.ailabs.iot.aisbase.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.alibaba.ailabs.iot.aisbase.callback.OnDownLoadStateListener;
import com.alibaba.ailabs.tg.utils.FileUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.google.android.gms.stats.CodePackage;
import java.io.File;
import java.net.URI;

/* JADX INFO: loaded from: classes.dex */
public class DownloadManagerUtils {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2681a = "DownloadManagerUtils";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static DownloadManagerUtils f2682b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public DownloadManager f2683c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public ArrayMap<Long, OnDownLoadStateListener> f2684d = new ArrayMap<>();
    public Context e;

    public static class DownloadTaskDetails {
        public int downloadedSize;
        public int totalSize;
    }

    class a extends BroadcastReceiver {
        public a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            long longExtra = intent.getLongExtra("extra_download_id", -1L);
            OnDownLoadStateListener onDownLoadStateListener = (OnDownLoadStateListener) DownloadManagerUtils.this.f2684d.get(Long.valueOf(longExtra));
            if (onDownLoadStateListener != null) {
                Cursor cursorQuery = DownloadManagerUtils.this.f2683c.query(new DownloadManager.Query().setFilterById(longExtra));
                if (cursorQuery == null || !cursorQuery.moveToFirst()) {
                    return;
                }
                String string = cursorQuery.getString(cursorQuery.getColumnIndex("local_uri"));
                cursorQuery.close();
                LogUtils.d(DownloadManagerUtils.f2681a, "filePath = " + string);
                TextUtils.isEmpty(string);
                try {
                    File file = new File(new URI(string));
                    if (file.exists()) {
                        onDownLoadStateListener.downLoadStateCallback(file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    LogUtils.e(DownloadManagerUtils.f2681a, e.toString());
                }
            }
        }
    }

    public DownloadManagerUtils(Context context) {
        this.e = context;
        this.f2683c = (DownloadManager) context.getSystemService("download");
        this.e.registerReceiver(new a(), new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
    }

    public static DownloadManagerUtils getInstance(Context context) {
        if (f2682b == null) {
            synchronized (DownloadManagerUtils.class) {
                if (f2682b == null) {
                    f2682b = new DownloadManagerUtils(context);
                }
            }
        }
        return f2682b;
    }

    public int cancelDownload(long j) {
        DownloadManager downloadManager = this.f2683c;
        if (downloadManager != null) {
            return downloadManager.remove(j);
        }
        return 0;
    }

    public long downloadFile(String str, String str2, String str3, OnDownLoadStateListener onDownLoadStateListener) {
        if (TextUtils.isEmpty(str3)) {
            str3 = FileUtils.getExternalPath(this.e, CodePackage.OTA);
        }
        String strCheckFileInDirWithMd5 = FileUtils.checkFileInDirWithMd5(str3, str2);
        LogUtils.i(f2681a, "ota url->" + str + "\nota dm5->" + str2 + "\nota file path->" + strCheckFileInDirWithMd5);
        if (!TextUtils.isEmpty(strCheckFileInDirWithMd5)) {
            if (onDownLoadStateListener == null) {
                return 0L;
            }
            onDownLoadStateListener.downLoadStateCallback(strCheckFileInDirWithMd5);
            return 0L;
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
        File file = new File(str3, System.currentTimeMillis() + ".bin");
        if (!FileUtils.createOrExistsFile(file)) {
            LogUtils.e(f2681a, "failed to create file");
            return -1L;
        }
        request.setDestinationUri(Uri.fromFile(file));
        long jEnqueue = this.f2683c.enqueue(request);
        if (onDownLoadStateListener != null) {
            this.f2684d.put(Long.valueOf(jEnqueue), onDownLoadStateListener);
        }
        LogUtils.d(f2681a, "download ID: " + jEnqueue);
        return jEnqueue;
    }

    public DownloadTaskDetails getDownloadDetails(long j) {
        Cursor cursorQuery = this.f2683c.query(new DownloadManager.Query().setFilterById(j));
        if (cursorQuery == null || !cursorQuery.moveToFirst()) {
            return null;
        }
        DownloadTaskDetails downloadTaskDetails = new DownloadTaskDetails();
        int i = cursorQuery.getInt(cursorQuery.getColumnIndex("bytes_so_far"));
        int i2 = cursorQuery.getInt(cursorQuery.getColumnIndex("total_size"));
        cursorQuery.close();
        LogUtils.d(f2681a, "download task details, downloadedSize: " + i + ", totalSize: " + i2);
        downloadTaskDetails.downloadedSize = i;
        downloadTaskDetails.totalSize = i2;
        return downloadTaskDetails;
    }

    public int getTotalSize(long j) {
        Cursor cursorQuery = this.f2683c.query(new DownloadManager.Query().setFilterById(j));
        if (cursorQuery == null || !cursorQuery.moveToFirst()) {
            return 0;
        }
        return cursorQuery.getInt(cursorQuery.getColumnIndex("total_size"));
    }

    public int queryDownloadedBytes(long j) {
        Cursor cursorQuery = this.f2683c.query(new DownloadManager.Query().setFilterById(j));
        if (cursorQuery == null || !cursorQuery.moveToFirst()) {
            return 0;
        }
        int i = cursorQuery.getInt(cursorQuery.getColumnIndex("bytes_so_far"));
        cursorQuery.getInt(cursorQuery.getColumnIndex("total_size"));
        return i;
    }

    public int validDownload(long j) {
        Cursor cursorQuery = this.f2683c.query(new DownloadManager.Query().setFilterById(j));
        try {
            if (!cursorQuery.moveToFirst()) {
                cursorQuery.close();
                return -1;
            }
            if (cursorQuery.getInt(cursorQuery.getColumnIndex("status")) == 8) {
                return 0;
            }
            return cursorQuery.getInt(cursorQuery.getColumnIndex("reason"));
        } finally {
            cursorQuery.close();
        }
    }
}
