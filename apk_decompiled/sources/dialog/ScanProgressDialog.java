package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.czp.library.ArcProgress;
import com.seculink.app.R;
import java.lang.Thread;
import tools.ScreenUtil;

/* JADX INFO: loaded from: classes3.dex */
public class ScanProgressDialog extends Dialog implements View.OnClickListener {
    ArcProgress arcProgress;
    Button cancelBtn;
    Activity context;
    Handler handler;
    OnViewClick l;
    private onProgressChangeListener onProgressChangeListener;
    int progress;
    Paint progressPaint;
    Paint textPaint;
    private String textTip;
    Thread thread;
    int total;
    TextView tv_close;
    TextView tv_found_devices;
    TextView tv_num;

    public interface OnViewClick {
        void onDismiss();
    }

    public interface onProgressChangeListener {
        void onProgressChange(int i);
    }

    public String getTextTip() {
        return this.textTip;
    }

    public void setTextTip(String str) {
        this.textTip = str;
    }

    public ScanProgressDialog(@NonNull Activity activity2) {
        super(activity2);
        this.total = 100;
        this.handler = new Handler();
        this.progressPaint = new Paint(1);
        this.textPaint = new Paint(1);
        this.context = activity2;
    }

    public ScanProgressDialog(@NonNull Activity activity2, int i) {
        super(activity2, i);
        this.total = 100;
        this.handler = new Handler();
        this.progressPaint = new Paint(1);
        this.textPaint = new Paint(1);
        this.context = activity2;
    }

    protected ScanProgressDialog(@NonNull Activity activity2, boolean z, @Nullable DialogInterface.OnCancelListener onCancelListener) {
        super(activity2, z, onCancelListener);
        this.total = 100;
        this.handler = new Handler();
        this.progressPaint = new Paint(1);
        this.textPaint = new Paint(1);
        this.context = activity2;
    }

    public void setOnViewClick(OnViewClick onViewClick) {
        this.l = onViewClick;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        setContentView(R.layout.layout_scan_progress);
        getWindow().setDimAmount(0.3f);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_white_rect);
        this.tv_num = (TextView) findViewById(R.id.tv_num);
        this.cancelBtn = (Button) findViewById(R.id.cancelBtn);
        this.cancelBtn.setOnClickListener(this);
        this.arcProgress = (ArcProgress) findViewById(R.id.myProgress);
        this.tv_close = (TextView) findViewById(R.id.tv_close);
        this.tv_found_devices = (TextView) findViewById(R.id.tv_found_devices);
        this.cancelBtn.setBackgroundResource(R.drawable.bg_button_light_blue);
        this.cancelBtn.setEnabled(false);
        this.handler.postDelayed(new Runnable() { // from class: dialog.ScanProgressDialog.1
            @Override // java.lang.Runnable
            public void run() {
                ScanProgressDialog.this.cancelBtn.setBackgroundResource(R.drawable.bg_button_blue);
                ScanProgressDialog.this.cancelBtn.setEnabled(true);
            }
        }, 30000L);
        this.tv_close.setOnClickListener(this);
        this.progressPaint.setStrokeWidth(35.0f);
        this.progressPaint.setTextSize(ScreenUtil.dp2Px(this.context, 30.0f));
        this.progressPaint.setColor(this.context.getResources().getColor(R.color.color_black));
        this.textPaint.setStrokeWidth(15.0f);
        this.textPaint.setTextSize(ScreenUtil.dp2Px(this.context, 18.0f));
        this.textPaint.setColor(this.context.getResources().getColor(R.color.color_black));
        this.arcProgress.setOnCenterDraw(new ArcProgress.OnCenterDraw() { // from class: dialog.ScanProgressDialog.2
            @Override // com.czp.library.ArcProgress.OnCenterDraw
            public void draw(Canvas canvas, RectF rectF, float f, float f2, float f3, int i) {
                String str = TextUtils.isEmpty(ScanProgressDialog.this.textTip) ? "0" : ScanProgressDialog.this.textTip;
                ScanProgressDialog.this.tv_num.setText(i + "");
                ScanProgressDialog.this.tv_found_devices.setText(ScanProgressDialog.this.getContext().getResources().getString(R.string.found_devices, str));
            }
        });
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = this.context.getRequestedOrientation() == 0 ? (ScreenUtil.getDisplayMetrics(this.context)[0] * 4) / 7 : (ScreenUtil.getDisplayMetrics(this.context)[0] * 7) / 9;
        attributes.height = this.context.getRequestedOrientation() == 0 ? (ScreenUtil.getDisplayMetrics(this.context)[1] * 7) / 9 : (ScreenUtil.getDisplayMetrics(this.context)[1] * 4) / 7;
        getWindow().setAttributes(attributes);
        setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: dialog.ScanProgressDialog.3
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                ScanProgressDialog.this.dismiss();
                return false;
            }
        });
    }

    public void setTotal(int i) {
        this.total = i;
    }

    public void setProgress(final int i) {
        this.handler.post(new Runnable() { // from class: dialog.ScanProgressDialog.4
            @Override // java.lang.Runnable
            public void run() {
                if (ScanProgressDialog.this.isShowing()) {
                    ScanProgressDialog.this.arcProgress.setProgress(i);
                    if (i >= 100) {
                        ScanProgressDialog.this.dismiss();
                    }
                }
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (isShowing()) {
            dismiss();
        }
    }

    @Override // android.app.Dialog
    public void show() {
        super.show();
        startProrgress();
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        stopProgress();
        this.l.onDismiss();
    }

    public void startProrgress() {
        Thread thread = this.thread;
        if (thread == null || thread.getState() == Thread.State.TERMINATED) {
            this.progress = -1;
            this.thread = new Thread(new ProgressThread());
            this.thread.start();
        }
    }

    public void stopProgress() {
        this.thread.interrupt();
        this.handler.removeCallbacksAndMessages(null);
    }

    class ProgressThread implements Runnable {
        private long lastTime;

        ProgressThread() {
        }

        @Override // java.lang.Runnable
        public void run() {
            this.lastTime = System.currentTimeMillis();
            ScanProgressDialog.this.handler.post(new Runnable() { // from class: dialog.ScanProgressDialog.ProgressThread.1
                @Override // java.lang.Runnable
                public void run() {
                    if (ScanProgressDialog.this.context == null || ScanProgressDialog.this.context.isFinishing() || !ScanProgressDialog.this.isShowing() || ScanProgressDialog.this.progress >= 100) {
                        return;
                    }
                    ScanProgressDialog.this.progress++;
                    ScanProgressDialog.this.arcProgress.setProgress(ScanProgressDialog.this.progress);
                    if (ScanProgressDialog.this.onProgressChangeListener != null) {
                        ScanProgressDialog.this.onProgressChangeListener.onProgressChange(ScanProgressDialog.this.progress);
                    }
                    ScanProgressDialog.this.handler.postDelayed(this, 600L);
                }
            });
        }
    }

    public void setOnProgressChangeListener(onProgressChangeListener onprogresschangelistener) {
        this.onProgressChangeListener = onprogresschangelistener;
    }

    public void hideTip() {
        this.tv_found_devices.setVisibility(8);
    }
}
