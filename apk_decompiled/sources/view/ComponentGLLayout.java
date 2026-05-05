package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.aliyun.iot.aep.sdk.framework.utils.SpUtil;
import com.bumptech.glide.Glide;
import com.seculink.app.R;
import tools.Utils;

/* JADX INFO: loaded from: classes5.dex */
public class ComponentGLLayout extends RelativeLayout {
    private MyGlTextureView glTextureView;
    public int is1100ErrorPre;
    private ImageView ivSnap;
    private Context mContext;
    private OnGLLayoutListener onGLLayoutListener;
    private ProgressBar videoBufferingProgressBar;
    private View videoPauseBtn;
    private View videoPlayBtn;

    public interface OnGLLayoutListener {
        void onVideoPlayBtnChange();

        void onVideoStopBtnChange();
    }

    public ComponentGLLayout(Context context) {
        this(context, null);
    }

    public ComponentGLLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MyGlTextureView getGlTextureView() {
        return this.glTextureView;
    }

    public ComponentGLLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.is1100ErrorPre = 10;
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_componentgl, (ViewGroup) this, true);
        this.ivSnap = (ImageView) findViewById(R.id.iv_snap);
        this.videoBufferingProgressBar = (ProgressBar) findViewById(R.id.video_buffering_bar);
        this.glTextureView = (MyGlTextureView) findViewById(R.id.play);
        this.glTextureView.setClickable(true);
        this.videoPlayBtn = findViewById(R.id.video_play_ibtn);
        this.videoPlayBtn.setOnClickListener(new View.OnClickListener() { // from class: view.ComponentGLLayout.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ComponentGLLayout.this.dismissPlayButton();
                if (ComponentGLLayout.this.onGLLayoutListener != null) {
                    ComponentGLLayout.this.onGLLayoutListener.onVideoPlayBtnChange();
                }
            }
        });
        this.videoPauseBtn = findViewById(R.id.video_pause_ibtn);
        this.videoPauseBtn.setOnClickListener(new View.OnClickListener() { // from class: view.ComponentGLLayout.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ComponentGLLayout.this.showPlayButton();
                if (ComponentGLLayout.this.onGLLayoutListener != null) {
                    ComponentGLLayout.this.onGLLayoutListener.onVideoStopBtnChange();
                }
            }
        });
    }

    public void setOnGLLayoutListener(OnGLLayoutListener onGLLayoutListener) {
        this.onGLLayoutListener = onGLLayoutListener;
    }

    public void showSnapPicture(String str, String str2, int i) {
        Glide.with(this.mContext).load(SpUtil.getString(this.mContext, Utils.getDevSnapKey(OpenAccountUIConstants.UNDER_LINE + str + OpenAccountUIConstants.UNDER_LINE + str2))).into(this.ivSnap);
        this.ivSnap.setVisibility(0);
    }

    public void dismissSnapPicture() {
        this.ivSnap.setVisibility(8);
    }

    public void showPlayButton() {
        this.videoBufferingProgressBar.setVisibility(8);
        this.videoPlayBtn.setVisibility(0);
    }

    public void dismissPlayButton() {
        this.videoPlayBtn.setVisibility(8);
    }

    public void showBuffering() {
        this.videoBufferingProgressBar.setVisibility(0);
    }

    public void dismissBuffering() {
        this.videoBufferingProgressBar.setVisibility(8);
    }
}
