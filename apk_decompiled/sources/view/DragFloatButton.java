package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatButton;
import tools.DensityUtil;

/* JADX INFO: loaded from: classes5.dex */
public class DragFloatButton extends AppCompatButton {
    private boolean isDrag;
    private int lastX;
    private int lastY;
    private OnItemClickListener onItemClickListener;
    private int screenHeight;
    private int screenWidth;
    private int screenWidthHalf;
    private int statusHeight;

    public interface OnItemClickListener {
        void onItemClick(float f, float f2, int i);
    }

    public DragFloatButton(Context context) {
        super(context);
        init();
    }

    public DragFloatButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public DragFloatButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.screenWidth = DensityUtil.dip2px(getContext(), 61.0f);
        this.screenWidthHalf = this.screenWidth;
        this.screenHeight = DensityUtil.dip2px(getContext(), 136.0f);
        this.statusHeight = 0;
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
                    OnItemClickListener onItemClickListener = this.onItemClickListener;
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getX(), getY(), this.screenHeight);
                    }
                }
                break;
        }
        return this.isDrag || super.onTouchEvent(motionEvent);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
