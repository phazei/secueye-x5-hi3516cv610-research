package lvtry;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.TextureView;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.LinkVisual;
import com.aliyun.iotx.linkvisual.media.video.beans.PlayerStoppedDrawingMode;
import com.aliyun.iotx.linkvisual.media.video.beans.Yuv420pFrame;
import com.aliyun.iotx.linkvisual.media.video.processing.IVideoFrameProcessor;
import java.util.concurrent.atomic.AtomicBoolean;
import lvbyte.lvtry;

/* JADX INFO: loaded from: classes4.dex */
public class lvfor implements TextureView.SurfaceTextureListener, lvtry.lvdo {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private lvnew.lvdo f8072lvdo;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private int f8073lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private int f8074lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    private int f8075lvint;

    /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
    private com.aliyun.iotx.linkvisual.media.video.lvdo f8076lvnew;

    /* JADX INFO: renamed from: lvtry, reason: collision with root package name */
    private lvdo f8077lvtry;

    class lvdo extends Thread {

        /* JADX INFO: renamed from: lvint, reason: collision with root package name */
        private Surface f8081lvint;

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        private final byte[] f8078lvdo = new byte[1];

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        private boolean f8080lvif = false;

        /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
        private AtomicBoolean f8079lvfor = new AtomicBoolean(false);

        public lvdo() {
            setName("PlayerTextureViewGLThread");
        }

        public void lvdo() {
            synchronized (this.f8078lvdo) {
                this.f8080lvif = true;
                this.f8078lvdo.notify();
            }
        }

        public void lvdo(SurfaceTexture surfaceTexture) {
            this.f8081lvint = new Surface(surfaceTexture);
        }

        public void lvdo(boolean z) {
            synchronized (this.f8078lvdo) {
                this.f8079lvfor.set(z);
                this.f8078lvdo.notify();
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Surface surface = this.f8081lvint;
            LinkVisual.init_textureview_opengl(surface, surface.hashCode());
            while (true) {
                synchronized (this.f8078lvdo) {
                    try {
                        this.f8078lvdo.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (this.f8080lvif) {
                    ALog.d("linksdk_lv_PlayerTextureRender", "[" + lvfor.this.hashCode() + "] doAnimation exiting");
                    LinkVisual.destroy_textureview_opengl(this.f8081lvint.hashCode());
                    this.f8081lvint.release();
                    this.f8081lvint = null;
                    return;
                }
                lvfor.this.lvif(this.f8081lvint.hashCode());
                if (lvfor.this.f8076lvnew.useVideoFrameProcessing()) {
                    Yuv420pFrame yuvFrame = lvfor.this.f8076lvnew.getYuvFrame();
                    if (yuvFrame != null) {
                        IVideoFrameProcessor videoFrameProcessor = lvfor.this.f8076lvnew.getVideoFrameProcessor();
                        if (videoFrameProcessor != null) {
                            videoFrameProcessor.processing(yuvFrame);
                        }
                        if (lvfor.this.f8076lvnew.getPlayerStoppedDrawingMode() != PlayerStoppedDrawingMode.ALWAYS_KEEP_LAST_FRAME || !lvfor.this.f8076lvnew.isInvalidHandle()) {
                            LinkVisual.draw_textureview_frame_externally(lvfor.this.f8076lvnew.getHandle(), this.f8081lvint.hashCode(), yuvFrame.getDirectBuffer(), yuvFrame.width, yuvFrame.height);
                        }
                    }
                } else if (lvfor.this.f8076lvnew.getPlayerStoppedDrawingMode() == PlayerStoppedDrawingMode.ALWAYS_KEEP_LAST_FRAME && lvfor.this.f8076lvnew.isInvalidHandle()) {
                    if (lvfor.this.f8076lvnew.getYuvFrame() != null) {
                        LinkVisual.draw_textureview_frame_externally(lvfor.this.f8076lvnew.getHandle(), this.f8081lvint.hashCode(), lvfor.this.f8076lvnew.getYuvFrame().getDirectBuffer(), lvfor.this.f8076lvnew.getYuvFrame().width, lvfor.this.f8076lvnew.getYuvFrame().height);
                    }
                } else if (this.f8079lvfor.compareAndSet(true, false) || !lvfor.this.f8076lvnew.isInvalidHandle()) {
                    LinkVisual.draw_textureview_frame_internally(lvfor.this.f8076lvnew.getHandle(), this.f8081lvint.hashCode());
                }
            }
        }
    }

    public lvfor(com.aliyun.iotx.linkvisual.media.video.lvdo lvdoVar) {
        this.f8076lvnew = lvdoVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lvif(int i) {
        if (this.f8076lvnew.getVideoHeight() == 0 || this.f8076lvnew.getVideoWidth() == 0) {
            return;
        }
        lvnew.lvdo lvdoVarLvdo = lvtry.lvdo(this.f8074lvif, this.f8073lvfor, this.f8076lvnew.getVideoWidth(), this.f8076lvnew.getVideoHeight(), this.f8075lvint);
        if (lvdoVarLvdo.lvdo(this.f8072lvdo)) {
            return;
        }
        ALog.d("linksdk_lv_PlayerTextureRender", "[" + hashCode() + "] video: " + this.f8076lvnew.getVideoWidth() + ", " + this.f8076lvnew.getVideoHeight());
        ALog.d("linksdk_lv_PlayerTextureRender", "[" + hashCode() + "] viewPort: " + lvdoVarLvdo.lvfor() + ", " + lvdoVarLvdo.lvint() + ", " + lvdoVarLvdo.lvif() + ", " + lvdoVarLvdo.lvdo());
        LinkVisual.on_textureview_viewport_changed(lvdoVarLvdo.lvfor(), lvdoVarLvdo.lvint(), lvdoVarLvdo.lvif(), lvdoVarLvdo.lvdo(), i);
        this.f8072lvdo = lvdoVarLvdo;
    }

    @Override // lvtry.lvdo
    public void lvdo(int i) {
        this.f8075lvint = i;
    }

    @Override // lvtry.lvdo
    public void lvdo(boolean z) {
        ALog.d("linksdk_lv_PlayerTextureRender", "[" + hashCode() + "] requestRender " + z);
        lvdo lvdoVar = this.f8077lvtry;
        if (lvdoVar != null) {
            lvdoVar.lvdo(z);
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        this.f8074lvif = i;
        this.f8073lvfor = i2;
        this.f8072lvdo = new lvnew.lvdo();
        lvdo lvdoVar = new lvdo();
        this.f8077lvtry = lvdoVar;
        lvdoVar.lvdo(surfaceTexture);
        this.f8077lvtry.start();
        ALog.d("linksdk_lv_PlayerTextureRender", "[" + hashCode() + "] onSurfaceTextureAvailable  width:" + i + " height:" + i2);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        ALog.d("linksdk_lv_PlayerTextureRender", "[" + hashCode() + "] onSurfaceTextureDestroyed");
        lvdo lvdoVar = this.f8077lvtry;
        if (lvdoVar == null) {
            return true;
        }
        lvdoVar.lvdo();
        this.f8077lvtry = null;
        return true;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        this.f8074lvif = i;
        this.f8073lvfor = i2;
        ALog.d("linksdk_lv_PlayerTextureRender", "[" + hashCode() + "] onSurfaceTextureSizeChanged width:" + i + " height:" + i2 + " surface=" + surfaceTexture.hashCode());
        lvdo lvdoVar = this.f8077lvtry;
        if (lvdoVar != null) {
            lvdoVar.lvdo(true);
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        ALog.d("linksdk_lv_PlayerTextureRender", "[" + hashCode() + "] onSurfaceTextureUpdated  surface=" + surfaceTexture.hashCode());
    }
}
