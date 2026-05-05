package com.alibaba.ailabs.iot.aisbase.plugin.ota;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import com.alibaba.ailabs.iot.aisbase.V;
import com.alibaba.ailabs.iot.aisbase.W;
import com.alibaba.ailabs.iot.aisbase.X;
import com.alibaba.ailabs.iot.aisbase.Y;
import com.alibaba.ailabs.iot.aisbase.Z;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin;
import com.alibaba.ailabs.iot.aisbase.utils.DownloadManagerUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import datasource.implemention.data.DeviceVersionInfo;

/* JADX INFO: loaded from: classes.dex */
public class OTADownloadHelper {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public IOTAPlugin.IOTAActionListener f2629c;
    public DownloadManagerUtils j;
    public IOTAPlugin.IFirmwareDownloadListener k;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f2627a = OTADownloadHelper.class.getSimpleName();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public IOTAPlugin.OTAState f2628b = IOTAPlugin.OTAState.IDLE;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Handler f2630d = new Handler(Looper.getMainLooper());
    public SparseArray<IActionListener> e = new SparseArray<>();
    public SparseArray<Runnable> f = new SparseArray<>();
    public String g = null;
    public String h = null;
    public long i = -1;
    public a l = null;
    public DeviceVersionInfo m = null;
    public boolean n = false;
    public String o = null;
    public boolean p = false;
    public Runnable q = new V(this);
    public Runnable r = new W(this);

    public void startDownloadFirmware(Context context, DeviceVersionInfo deviceVersionInfo, String str, IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener) {
        this.k = iFirmwareDownloadListener;
        if (this.i != -1) {
            a(-400, "There is currently a download task with id" + this.i);
            return;
        }
        if (this.j == null) {
            this.j = DownloadManagerUtils.getInstance(context);
        }
        IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener2 = this.k;
        if (iFirmwareDownloadListener2 != null) {
            iFirmwareDownloadListener2.onDownloadStart();
        }
        if (this.p) {
            ReportProgressUtil.reportOtaProgress(this.o, deviceVersionInfo.getModel().getVersion(), this.g, this.h, ReportProgressUtil.TAG_START, ReportProgressUtil.CODE_OK, "");
        }
        this.i = this.j.downloadFile(deviceVersionInfo.getModel().getOtaUrl(), deviceVersionInfo.getModel().getMd5(), str, new X(this, deviceVersionInfo));
        long j = this.i;
        if (j < 0) {
            a(-401, "no write permission or insufficient disk");
            return;
        }
        if (j > 0) {
            a();
        }
        if (this.i == 0) {
            this.i = -1L;
        }
    }

    public void startDownloadIlopFirmware(Context context, DeviceVersionInfo.DeviceInfoModel deviceInfoModel, String str, IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener) {
        LogUtils.e(this.f2627a, "Info Model: " + deviceInfoModel.toString());
        this.k = iFirmwareDownloadListener;
        if (this.i != -1) {
            a(-400, "There is currently a download task with id" + this.i);
            return;
        }
        if (this.j == null) {
            this.j = DownloadManagerUtils.getInstance(context);
        }
        IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener2 = this.k;
        if (iFirmwareDownloadListener2 != null) {
            iFirmwareDownloadListener2.onDownloadStart();
        }
        if (this.p) {
            ReportProgressUtil.reportOtaProgress(this.o, deviceInfoModel.getVersion(), this.g, this.h, ReportProgressUtil.TAG_START, ReportProgressUtil.CODE_OK, "");
        }
        this.i = this.j.downloadFile(deviceInfoModel.getOtaUrl(), deviceInfoModel.getMd5(), str, new Y(this, deviceInfoModel));
        long j = this.i;
        if (j < 0) {
            a(-401, "no write permission or insufficient disk");
            return;
        }
        if (j > 0) {
            a();
        }
        if (this.i == 0) {
            this.i = -1L;
        }
    }

    public void stopDownloadFirmware() {
        DownloadManagerUtils downloadManagerUtils;
        long j = this.i;
        if (j != -1 || (downloadManagerUtils = this.j) == null) {
            return;
        }
        downloadManagerUtils.cancelDownload(j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public class a extends Thread {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public boolean f2631a;

        public a() {
            this.f2631a = true;
        }

        public void a() {
            this.f2631a = false;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (OTADownloadHelper.this.j != null) {
                while (this.f2631a) {
                    int iValidDownload = OTADownloadHelper.this.j.validDownload(OTADownloadHelper.this.i);
                    if (iValidDownload != 0) {
                        OTADownloadHelper.this.a(iValidDownload, "Download failed");
                        OTADownloadHelper.this.i = -1L;
                        return;
                    }
                    DownloadManagerUtils.DownloadTaskDetails downloadDetails = OTADownloadHelper.this.j.getDownloadDetails(OTADownloadHelper.this.i);
                    if (downloadDetails == null) {
                        this.f2631a = false;
                        return;
                    }
                    if (OTADownloadHelper.this.k != null) {
                        OTADownloadHelper.this.f2630d.post(new Z(this, downloadDetails));
                    }
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public /* synthetic */ a(OTADownloadHelper oTADownloadHelper, V v) {
            this();
        }
    }

    public final void b(int i, String str) {
        LogUtils.e(this.f2627a, "ota error (" + i + ":" + str + ")");
        this.n = false;
        this.f2630d.removeCallbacks(this.q);
        this.f2630d.removeCallbacks(this.r);
        this.f2628b = IOTAPlugin.OTAState.ERROR;
        IOTAPlugin.IOTAActionListener iOTAActionListener = this.f2629c;
        if (iOTAActionListener != null) {
            iOTAActionListener.onFailed(i, str);
        }
    }

    public final void a() {
        b();
        this.l = new a(this, null);
        this.l.start();
    }

    public final void a(int i, String str) {
        IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener = this.k;
        if (iFirmwareDownloadListener != null) {
            iFirmwareDownloadListener.onFailed(i, str);
        }
        if (this.p) {
            DeviceVersionInfo deviceVersionInfo = this.m;
            String version = deviceVersionInfo == null ? "" : deviceVersionInfo.getModel().getVersion();
            ReportProgressUtil.reportOtaProgress(this.o, version, this.g, this.h, "FINISH", ReportProgressUtil.CODE_ERR, str + OpenAccountUIConstants.UNDER_LINE + i);
        }
    }

    public final void b() {
        a aVar = this.l;
        if (aVar != null) {
            aVar.a();
            try {
                this.l.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
