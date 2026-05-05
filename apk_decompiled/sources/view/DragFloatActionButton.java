package view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import androidx.appcompat.widget.AppCompatButton;
import tools.DensityUtil;

/* JADX INFO: loaded from: classes5.dex */
public class DragFloatActionButton extends AppCompatButton {
    private boolean isDrag;
    private int lastX;
    private int lastY;
    private int screenHeight;
    private int screenWidth;
    private int screenWidthHalf;
    private int statusHeight;

    public DragFloatActionButton(Context context) {
        super(context);
        init();
    }

    public DragFloatActionButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public DragFloatActionButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.screenWidth = DensityUtil.getScreenWidth(getContext());
        this.screenWidthHalf = this.screenWidth / 2;
        this.screenHeight = DensityUtil.getScreenHeight(getContext());
        this.statusHeight = DensityUtil.getStatusHeight((Activity) getContext());
        Log.e("拖拽控件", "screenWidth=" + this.screenWidth);
        Log.e("拖拽控件", "screenWidthHalf=" + this.screenWidthHalf);
        Log.e("拖拽控件", "screenHeight=" + this.screenHeight);
        Log.e("拖拽控件", "statusHeight=" + this.statusHeight);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int rawX = (int) motionEvent.getRawX();
        int rawY = (int) motionEvent.getRawY();
        switch (motionEvent.getAction() & 255) {
            case 0:
                this.isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                this.lastX = rawX;
                this.lastY = rawY;
                break;
            case 1:
                if (this.isDrag) {
                    setPressed(false);
                    Log.e("拖拽控件", "rawX=" + rawX);
                    Log.e("拖拽控件", "screenWidthHalf=" + this.screenWidthHalf);
                    if (rawX >= this.screenWidthHalf) {
                        animate().setInterpolator(new DecelerateInterpolator()).setDuration(500L).xBy((this.screenWidth - getWidth()) - getX()).start();
                        Log.e("拖拽控件", "true");
                    } else {
                        animate().setInterpolator(new DecelerateInterpolator()).setDuration(500L).xBy((this.screenWidth - getWidth()) - getX()).start();
                    }
                }
                break;
            case 2:
                this.isDrag = true;
                int i = rawX - this.lastX;
                int i2 = rawY - this.lastY;
                if (((int) Math.sqrt((i * i) + (i2 * i2))) == 0) {
                    this.isDrag = false;
                } else {
                    float x = getX() + i;
                    float y = getY() + i2;
                    if (x < 0.0f) {
                        x = 0.0f;
                    } else if (x > this.screenWidth - getWidth()) {
                        x = this.screenWidth - getWidth();
                    }
                    int i3 = this.statusHeight;
                    if (y < i3) {
                        y = i3;
                    } else {
                        float height = getHeight() + y;
                        int i4 = this.screenHeight;
                        if (height > i4) {
                            y = i4 - getHeight();
                        }
                    }
                    setX(x);
                    setY(y);
                    this.lastX = rawX;
                    this.lastY = rawY;
                    Log.e("拖拽控件", "getX=" + getX() + ";getY=" + getY() + ";screenHeight=" + this.screenHeight);
                }
                break;
        }
        return this.isDrag || super.onTouchEvent(motionEvent);
    }
}
