package dialog;

import android.annotation.SuppressLint;
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
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
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
public class UpgradeDialogIpc extends Dialog implements View.OnClickListener {
    private static final String TAG = "UpgradeDialogIpc";
    ArcProgress arcProgress;
    Button cancelBtn;
    Activity context;
    Handler handler;
    private ImageView imageView;
    private boolean isDownloadSucceed;
    private boolean isMax;
    OnViewClick l;
    LinearLayout ll_nums;
    OnErrorListener onErrorListener;
    int progress;
    Paint progressPaint;
    Paint textPaint;
    TextView text_result;
    private TextView text_tips;
    private TextView text_tips2;
    Thread thread;
    int total;
    TextView tv_tip;

    public interface OnErrorListener {
        void onError();
    }

    public interface OnViewClick {
        void onDismiss();
    }

    public UpgradeDialogIpc(@NonNull Activity activity2) {
        super(activity2);
        this.total = 100;
        this.handler = new Handler();
        this.progressPaint = new Paint(1);
        this.textPaint = new Paint(1);
        this.isMax = false;
        this.context = activity2;
    }

    public UpgradeDialogIpc(@NonNull Activity activity2, int i) {
        super(activity2, i);
        this.total = 100;
        this.handler = new Handler();
        this.progressPaint = new Paint(1);
        this.textPaint = new Paint(1);
        this.isMax = false;
        this.context = activity2;
    }

    protected UpgradeDialogIpc(@NonNull Activity activity2, boolean z, @Nullable DialogInterface.OnCancelListener onCancelListener) {
        super(activity2, z, onCancelListener);
        this.total = 100;
        this.handler = new Handler();
        this.progressPaint = new Paint(1);
        this.textPaint = new Paint(1);
        this.isMax = false;
        this.context = activity2;
    }

    public void setOnViewClick(OnViewClick onViewClick) {
        this.l = onViewClick;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        setContentView(R.layout.layout_upgrade_progress_ipc);
        getWindow().setDimAmount(0.3f);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_white_rect);
        this.text_tips2 = (TextView) findViewById(R.id.text_tips2);
        this.text_tips = (TextView) findViewById(R.id.text_tips);
        this.text_result = (TextView) findViewById(R.id.text_result);
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
        this.arcProgress.setOnCenterDraw(new ArcProgress.OnCenterDraw() { // from class: dialog.UpgradeDialogIpc.1
            @Override // com.czp.library.ArcProgress.OnCenterDraw
            @SuppressLint({"SetTextI18n"})
            public void draw(Canvas canvas, RectF rectF, float f, float f2, float f3, int i) {
                if (UpgradeDialogIpc.this.isDownloadSucceed) {
                    return;
                }
                UpgradeDialogIpc.this.text_result.setVisibility(0);
                UpgradeDialogIpc.this.text_tips.setVisibility(0);
                UpgradeDialogIpc.this.text_tips2.setVisibility(0);
                UpgradeDialogIpc.this.text_result.setText(i + "" + UpgradeDialogIpc.this.context.getString(R.string.upgrading));
                UpgradeDialogIpc.this.text_tips.setText(R.string.upgrade_tip1);
                UpgradeDialogIpc.this.text_tips2.setText(R.string.upgrade_tip2);
                UpgradeDialogIpc.this.cancelBtn.setVisibility(8);
            }
        });
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = this.context.getRequestedOrientation() == 0 ? (ScreenUtil.getDisplayMetrics(this.context)[0] * 4) / 7 : (ScreenUtil.getDisplayMetrics(this.context)[0] * 7) / 9;
        attributes.height = this.context.getRequestedOrientation() == 0 ? (ScreenUtil.getDisplayMetrics(this.context)[1] * 7) / 9 : (ScreenUtil.getDisplayMetrics(this.context)[1] * 4) / 7;
        getWindow().setAttributes(attributes);
        setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: dialog.UpgradeDialogIpc.2
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                UpgradeDialogIpc.this.dismiss();
                return false;
            }
        });
        this.imageView = (ImageView) findViewById(R.id.imageView);
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 20.0f, 0.0f, -20.0f);
        translateAnimation.setFillAfter(true);
        translateAnimation.setRepeatCount(-1);
        translateAnimation.setRepeatMode(2);
        translateAnimation.setDuration(1000L);
        this.imageView.startAnimation(translateAnimation);
    }

    public void setTextTip(@StringRes int i) {
        this.tv_tip.setText(i);
    }

    public void showTextTip() {
        this.tv_tip.setVisibility(0);
    }

    public void setTextUpgrading() {
        this.text_tips.setVisibility(0);
        this.text_tips.setText(R.string.upgrade_tip1);
        this.text_tips2.setVisibility(0);
        this.text_tips2.setText(R.string.upgrade_tip2);
        this.cancelBtn.setVisibility(8);
        this.cancelBtn.setText(R.string.finish);
    }

    public void setTextUpgradingSuccess() {
        this.isDownloadSucceed = true;
        stopProgress();
        this.l.onDismiss();
        this.arcProgress.setBackgroundResource(R.drawable.update_success_bg);
        this.arcProgress.setProgress(100);
        this.imageView.clearAnimation();
        this.imageView.setImageResource(R.drawable.update_success_rocket);
        this.text_result.setVisibility(0);
        this.text_result.setText(R.string.upgrade_success);
        this.text_tips.setVisibility(8);
        this.text_tips2.setVisibility(8);
        this.cancelBtn.setVisibility(0);
        this.cancelBtn.setText(R.string.finish);
    }

    public void setTextUpgradingFail() {
        this.isDownloadSucceed = true;
        stopProgress();
        this.l.onDismiss();
        this.arcProgress.setBackgroundResource(R.drawable.update_fail_bg);
        this.imageView.clearAnimation();
        this.imageView.setImageResource(R.drawable.update_fail_rocket);
        this.text_result.setVisibility(0);
        this.text_result.setText(R.string.upgrade_fail);
        this.text_tips.setVisibility(0);
        this.text_tips.setText(R.string.check_net_state);
        this.text_tips2.setVisibility(0);
        this.text_tips2.setText(R.string.check_upgrade_power);
        this.cancelBtn.setVisibility(0);
        this.cancelBtn.setText(R.string.i_know);
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
            UpgradeDialogIpc.this.handler.postDelayed(new Runnable() { // from class: dialog.UpgradeDialogIpc.ProgressThread.1
                @Override // java.lang.Runnable
                public void run() {
                    if (UpgradeDialogIpc.this.context == null || UpgradeDialogIpc.this.context.isFinishing() || !UpgradeDialogIpc.this.isShowing() || UpgradeDialogIpc.this.progress > 100) {
                        return;
                    }
                    if (System.currentTimeMillis() - ProgressThread.this.lastTime < 300000) {
                        if (UpgradeDialogIpc.this.isMax) {
                            UpgradeDialogIpc.this.handler.postDelayed(this, 1000L);
                            return;
                        }
                        if (UpgradeDialogIpc.this.progress >= 100) {
                            UpgradeDialogIpc.this.isMax = true;
                            UpgradeDialogIpc.this.progress = 99;
                            UpgradeDialogIpc.this.arcProgress.setProgress(UpgradeDialogIpc.this.progress);
                        } else {
                            UpgradeDialogIpc.this.progress++;
                            if (UpgradeDialogIpc.this.progress >= 100) {
                                UpgradeDialogIpc.this.arcProgress.setProgress(99);
                            } else {
                                UpgradeDialogIpc.this.arcProgress.setProgress(UpgradeDialogIpc.this.progress);
                            }
                        }
                        if (!UpgradeDialogIpc.this.isDownloadSucceed) {
                            UpgradeDialogIpc.this.handler.postDelayed(this, 1000L);
                            return;
                        } else {
                            UpgradeDialogIpc.this.handler.post(this);
                            return;
                        }
                    }
                    UpgradeDialogIpc.this.setTextUpgradingFail();
                }
            }, 1000L);
        }
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public boolean isDownloadSucceed() {
        return this.isDownloadSucceed;
    }
}
