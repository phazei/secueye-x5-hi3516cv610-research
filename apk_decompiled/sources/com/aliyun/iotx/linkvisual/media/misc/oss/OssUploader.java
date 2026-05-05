package com.aliyun.iotx.linkvisual.media.misc.oss;

import android.os.Handler;
import android.os.Looper;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.LinkVisual;
import com.aliyun.iotx.linkvisual.media.audio.ILVOssUploadCallback;
import java.io.File;

/* JADX INFO: loaded from: classes2.dex */
public class OssUploader {
    public static final String TAG = "OssUploader";

    /* JADX INFO: renamed from: lvtry, reason: collision with root package name */
    private static OssUploader f5055lvtry;

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private OssUploadListener f5056lvdo;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private long f5057lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private Handler f5058lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    private boolean f5059lvint;

    /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
    protected final ILVOssUploadCallback f5060lvnew = new lvdo();

    public interface OssUploadListener {
        void onProgress(int i);

        void onUploadError(String str, String str2, int i, String str3);

        void onUploadSuccess(String str, String str2);
    }

    class lvdo implements ILVOssUploadCallback {

        /* JADX INFO: renamed from: com.aliyun.iotx.linkvisual.media.misc.oss.OssUploader$lvdo$lvdo, reason: collision with other inner class name */
        class RunnableC0251lvdo implements Runnable {
            RunnableC0251lvdo() {
            }

            @Override // java.lang.Runnable
            public void run() {
                if (OssUploader.this.f5056lvdo != null) {
                    OssUploader.this.f5056lvdo.onProgress(0);
                }
            }
        }

        class lvfor implements Runnable {

            /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
            final /* synthetic */ String f5063lvdo;

            /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
            final /* synthetic */ int f5064lvfor;

            /* JADX INFO: renamed from: lvif, reason: collision with root package name */
            final /* synthetic */ String f5065lvif;

            /* JADX INFO: renamed from: lvint, reason: collision with root package name */
            final /* synthetic */ String f5066lvint;

            lvfor(String str, String str2, int i, String str3) {
                this.f5063lvdo = str;
                this.f5065lvif = str2;
                this.f5064lvfor = i;
                this.f5066lvint = str3;
            }

            @Override // java.lang.Runnable
            public void run() {
                LinkVisual.native_close_oss_upload(OssUploader.this.f5057lvfor);
                if (OssUploader.this.f5056lvdo != null) {
                    OssUploader.this.f5056lvdo.onUploadError(this.f5063lvdo, this.f5065lvif, this.f5064lvfor, this.f5066lvint);
                }
            }
        }

        class lvif implements Runnable {

            /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
            final /* synthetic */ String f5068lvdo;

            /* JADX INFO: renamed from: lvif, reason: collision with root package name */
            final /* synthetic */ String f5070lvif;

            lvif(String str, String str2) {
                this.f5068lvdo = str;
                this.f5070lvif = str2;
            }

            @Override // java.lang.Runnable
            public void run() {
                LinkVisual.native_close_oss_upload(OssUploader.this.f5057lvfor);
                if (OssUploader.this.f5056lvdo != null) {
                    OssUploader.this.f5056lvdo.onUploadSuccess(this.f5068lvdo, this.f5070lvif);
                }
            }
        }

        lvdo() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.ILVOssUploadCallback
        public void onCompletion(String str, String str2) {
            if (!OssUploader.this.f5059lvint) {
                ALog.e(OssUploader.TAG, "ignore onUploadSuccess callback due to not uploading.");
            } else {
                OssUploader.this.f5059lvint = false;
                OssUploader.this.f5058lvif.post(new lvif(str2, str));
            }
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.ILVOssUploadCallback
        public void onError(String str, String str2, int i, String str3) {
            if (!OssUploader.this.f5059lvint) {
                ALog.e(OssUploader.TAG, "ignore onError callback due to not uploading.");
            } else {
                OssUploader.this.f5059lvint = false;
                OssUploader.this.f5058lvif.post(new lvfor(str2, str, i, str3));
            }
        }

        @Override // com.aliyun.iotx.linkvisual.media.audio.ILVOssUploadCallback
        public void onProgress(long j, long j2) {
            OssUploader.this.f5058lvif.post(new RunnableC0251lvdo());
        }
    }

    private OssUploader() {
        this.f5058lvif = new Handler(Looper.myLooper() != null ? Looper.myLooper() : Looper.getMainLooper());
    }

    public static synchronized OssUploader getInstance() {
        if (f5055lvtry == null) {
            LinkVisual.set_log_level(ALog.getLevel());
            f5055lvtry = new OssUploader();
        }
        return f5055lvtry;
    }

    public void upload(File file, String str, String str2, String str3, OssUploadListener ossUploadListener) {
        if (file == null) {
            throw new IllegalArgumentException("amrFile is null.");
        }
        if (!file.canRead()) {
            ALog.e(TAG, file.getAbsolutePath() + " is not readable.");
            return;
        }
        this.f5056lvdo = ossUploadListener;
        if (this.f5059lvint) {
            ALog.e(TAG, "ignore upload due to uploading.");
        } else {
            this.f5059lvint = true;
            this.f5057lvfor = LinkVisual.native_oss_upload_amr(file.getAbsolutePath(), str, str3, str2, this.f5060lvnew);
        }
    }
}
