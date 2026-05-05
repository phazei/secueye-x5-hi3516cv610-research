package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.czp.library.ArcProgress;
import com.seculink.app.R;
import java.lang.Thread;
import tools.ScreenUtil;

/* JADX INFO: loaded from: classes3.dex */
public class UpgradeDialog extends Dialog implements View.OnClickListener {
    ArcProgress arcProgress;
    Button cancelBtn;
    Activity context;
    Handler handler;
    private boolean isDownloadSucceed;
    OnViewClick l;
    LinearLayout ll_nums;
    OnErrorListener onErrorListener;
    int progress;
    Paint progressPaint;
    Paint textPaint;
    Thread thread;
    int total;
    TextView tv_num;
    TextView tv_tip;

    public interface OnErrorListener {
        void onError();
    }

    public interface OnViewClick {
        void onDismiss();
    }

    public UpgradeDialog(@NonNull Activity activity2) {
        super(activity2);
        this.total = 100;
        this.handler = new Handler();
        this.progressPaint = new Paint(1);
        this.textPaint = new Paint(1);
        this.context = activity2;
    }

    public UpgradeDialog(@NonNull Activity activity2, int i) {
        super(activity2, i);
        this.total = 100;
        this.handler = new Handler();
        this.progressPaint = new Paint(1);
        this.textPaint = new Paint(1);
        this.context = activity2;
    }

    protected UpgradeDialog(@NonNull Activity activity2, boolean z, @Nullable DialogInterface.OnCancelListener onCancelListener) {
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
        setContentView(R.layout.layout_upgrade_progress);
        getWindow().setDimAmount(0.3f);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_white_rect);
        this.tv_num = (TextView) findViewById(R.id.tv_num);
        this.cancelBtn = (Button) findViewById(R.id.cancelBtn);
        this.cancelBtn.setOnClickListener(this);
        this.arcProgress = (ArcProgress) findViewById(R.id.myProgress);
        this.tv_tip = (TextView) findViewById(R.id.tv_tip);
        this.ll_nums = (LinearLayout) findViewById(R.id.ll_nums);
        this.progressPaint.setStrokeWidth(35.0f);
        this.progressPaint.setTextSize(ScreenUtil.dp2Px(this.context, 30.0f));
        this.progressPaint.setColor(this.context.getResources().getColor(R.color.color_black));
        this.textPaint.setStrokeWidth(15.0f);
        this.textPaint.setTextSize(ScreenUtil.dp2Px(this.context, 18.0f));
        this.textPaint.setColor(this.context.getResources().getColor(R.color.color_black));
        this.arcProgress.setOnCenterDraw(new ArcProgress.OnCenterDraw() { // from class: dialog.UpgradeDialog.1
            @Override // com.czp.library.ArcProgress.OnCenterDraw
            public void draw(Canvas canvas, RectF rectF, float f, float f2, float f3, int i) {
                if (!UpgradeDialog.this.isDownloadSucceed) {
                    UpgradeDialog.this.ll_nums.setVisibility(0);
                    UpgradeDialog.this.tv_num.setText(String.valueOf(i));
                } else {
                    UpgradeDialog.this.ll_nums.setVisibility(8);
                }
            }
        });
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = this.context.getRequestedOrientation() == 0 ? (ScreenUtil.getDisplayMetrics(this.context)[0] * 4) / 7 : (ScreenUtil.getDisplayMetrics(this.context)[0] * 7) / 9;
        attributes.height = this.context.getRequestedOrientation() == 0 ? (ScreenUtil.getDisplayMetrics(this.context)[1] * 7) / 9 : (ScreenUtil.getDisplayMetrics(this.context)[1] * 4) / 7;
        getWindow().setAttributes(attributes);
        setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: dialog.UpgradeDialog.2
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                UpgradeDialog.this.dismiss();
                return false;
            }
        });
    }

    public void setTextTip(@StringRes int i) {
        this.tv_tip.setText(i);
    }

    public void showTextTip() {
        this.tv_num.setVisibility(8);
        this.tv_tip.setVisibility(0);
    }

    public void setTotal(int i) {
        this.total = i;
    }

    public void setProgress(int i) {
        if (!this.isDownloadSucceed) {
            this.progress = i;
        }
        Thread thread = this.thread;
        if (thread == null || thread.getState() == Thread.State.TERMINATED) {
            this.isDownloadSucceed = false;
            this.thread = new Thread(new ProgressThread());
            this.thread.start();
        }
    }

    public int getProgress() {
        return this.progress;
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
            this.progress = 0;
            this.isDownloadSucceed = false;
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
            UpgradeDialog.this.handler.postDelayed(new Runnable() { // from class: dialog.UpgradeDialog.ProgressThread.1
                @Override // java.lang.Runnable
                public void run() {
                    if (UpgradeDialog.this.context == null || UpgradeDialog.this.context.isFinishing() || !UpgradeDialog.this.isShowing() || UpgradeDialog.this.progress > 100) {
                        return;
                    }
                    if (System.currentTimeMillis() - ProgressThread.this.lastTime < 180000) {
                        if (!UpgradeDialog.this.isDownloadSucceed && UpgradeDialog.this.progress == 100) {
                            UpgradeDialog.this.isDownloadSucceed = true;
                        }
                        if (UpgradeDialog.this.isDownloadSucceed) {
                            if (UpgradeDialog.this.progress < 100) {
                                UpgradeDialog.this.progress++;
                            } else {
                                UpgradeDialog.this.progress = 0;
                            }
                        }
                        UpgradeDialog.this.arcProgress.setProgress(UpgradeDialog.this.progress);
                        if (!UpgradeDialog.this.isDownloadSucceed) {
                            UpgradeDialog.this.handler.postDelayed(this, 1000L);
                            return;
                        } else {
                            UpgradeDialog.this.handler.post(this);
                            return;
                        }
                    }
                    UpgradeDialog.this.dismiss();
                }
            }, 1000L);
        }
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }
}
