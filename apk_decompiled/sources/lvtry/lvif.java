package lvtry;

import android.opengl.GLSurfaceView;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.LinkVisual;
import com.aliyun.iotx.linkvisual.media.video.beans.Yuv420pFrame;
import com.aliyun.iotx.linkvisual.media.video.processing.IVideoFrameProcessor;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import lvbyte.lvtry;

/* JADX INFO: loaded from: classes4.dex */
public class lvif implements GLSurfaceView.Renderer, lvdo {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private lvnew.lvdo f8083lvdo;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private int f8084lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private int f8085lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    private int f8086lvint;

    /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
    private com.aliyun.iotx.linkvisual.media.video.lvdo f8087lvnew;

    /* JADX INFO: renamed from: lvtry, reason: collision with root package name */
    private GLSurfaceView f8088lvtry;

    public lvif(GLSurfaceView gLSurfaceView, com.aliyun.iotx.linkvisual.media.video.lvdo lvdoVar) {
        this.f8088lvtry = gLSurfaceView;
        this.f8087lvnew = lvdoVar;
    }

    private void lvdo() {
        if (this.f8087lvnew.getVideoWidth() == 0 || this.f8087lvnew.getVideoHeight() == 0) {
            return;
        }
        lvnew.lvdo lvdoVarLvdo = lvtry.lvdo(this.f8085lvif, this.f8084lvfor, this.f8087lvnew.getVideoWidth(), this.f8087lvnew.getVideoHeight(), this.f8086lvint);
        if (lvdoVarLvdo.lvdo(this.f8083lvdo)) {
            return;
        }
        ALog.d("linksdk_lv_PlayerGLRender", "[" + hashCode() + "] video: " + this.f8087lvnew.getVideoWidth() + ", " + this.f8087lvnew.getVideoHeight());
        ALog.d("linksdk_lv_PlayerGLRender", "[" + hashCode() + "] viewPort: " + lvdoVarLvdo.lvfor() + ", " + lvdoVarLvdo.lvint() + ", " + lvdoVarLvdo.lvif() + ", " + lvdoVarLvdo.lvdo());
        LinkVisual.on_surfaceview_viewport_changed(lvdoVarLvdo.lvfor(), lvdoVarLvdo.lvint(), lvdoVarLvdo.lvif(), lvdoVarLvdo.lvdo());
        this.f8083lvdo = lvdoVarLvdo;
    }

    @Override // lvtry.lvdo
    public void lvdo(int i) {
        this.f8086lvint = i;
    }

    @Override // lvtry.lvdo
    public void lvdo(boolean z) {
        this.f8088lvtry.requestRender();
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        ALog.d("linksdk_lv_PlayerGLRender", "[" + hashCode() + "] onDrawFrame");
        lvdo();
        if (!this.f8087lvnew.useVideoFrameProcessing()) {
            LinkVisual.draw_surfaceview_frame_internally(this.f8087lvnew.getHandle());
            return;
        }
        Yuv420pFrame yuvFrame = this.f8087lvnew.getYuvFrame();
        if (yuvFrame == null) {
            return;
        }
        IVideoFrameProcessor videoFrameProcessor = this.f8087lvnew.getVideoFrameProcessor();
        if (videoFrameProcessor != null) {
            videoFrameProcessor.processing(yuvFrame);
        }
        LinkVisual.draw_surfaceview_frame_externally(this.f8087lvnew.getHandle(), yuvFrame.yuv420pFrameDirectBuffer, yuvFrame.width, yuvFrame.height);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        ALog.d("linksdk_lv_PlayerGLRender", "[" + hashCode() + "] onViewportChanged width = " + i + " height = " + i2);
        this.f8085lvif = i;
        this.f8084lvfor = i2;
        lvdo();
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        ALog.d("linksdk_lv_PlayerGLRender", "[" + hashCode() + "] onInitOpengl");
        LinkVisual.init_glsurfaceview_opengl();
        this.f8083lvdo = new lvnew.lvdo();
    }
}
