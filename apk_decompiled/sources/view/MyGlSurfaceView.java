package view;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import tools.ScreenUtil;

/* JADX INFO: loaded from: classes5.dex */
public class MyGlSurfaceView extends GLSurfaceView {
    private int lastAction;
    private float lastTouchX;
    private float lastTouchY;
    private float maxScale;
    private PointF mid;
    private int multipleDownNum;
    private int offset;
    private float oriDis;
    protected int s_height;
    protected int s_width;
    private boolean scaleMode;
    private float xScale;

    public MyGlSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mid = new PointF();
        this.maxScale = 3.0f;
        this.xScale = 1.0f;
        this.offset = ScreenUtil.dp2Px(context, 100.0f);
    }

    public MyGlSurfaceView(Context context) {
        super(context);
        this.mid = new PointF();
        this.maxScale = 3.0f;
        this.xScale = 1.0f;
        this.offset = ScreenUtil.dp2Px(context, 100.0f);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.s_width = i;
        this.s_height = i2;
    }

    private void midPoint(PointF pointF, MotionEvent motionEvent) {
        pointF.set((motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f, (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f);
    }

    public void checkRectValid() {
        int[] iArr = new int[2];
        ((ViewGroup) getParent()).getLocationOnScreen(iArr);
        int[] iArr2 = new int[2];
        getLocationOnScreen(iArr2);
        if (iArr2[0] - iArr[0] > 0) {
            setTranslationX((-((iArr2[0] + 1) - iArr[0])) + getTranslationX());
        }
        if (iArr2[0] + (getWidth() * getScaleX()) < iArr[0] + getWidth()) {
            setTranslationX(((iArr[0] + getWidth()) - (iArr2[0] + (getWidth() * getScaleX()))) + getTranslationX());
        }
        if (iArr2[1] - iArr[1] > 0) {
            setTranslationY((-(iArr2[1] - iArr[1])) + getTranslationY());
        }
        if (iArr2[1] + (getHeight() * getScaleY()) < iArr[1] + getHeight()) {
            setTranslationY(((iArr[1] + getHeight()) - (iArr2[1] + (getHeight() * getScaleY()))) + getTranslationY());
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        getHitRect(new Rect());
        ((ViewGroup) getParent()).getLocalVisibleRect(new Rect());
        if (action == 0) {
            this.multipleDownNum = 1;
            this.lastTouchX = motionEvent.getRawX();
            this.lastTouchY = motionEvent.getRawY();
        } else if (action == 5) {
            this.multipleDownNum++;
            this.oriDis = distance(motionEvent);
            this.xScale = getScaleX();
            this.lastTouchX = motionEvent.getRawX();
            this.lastTouchY = motionEvent.getRawY();
            midPoint(this.mid, motionEvent);
        } else if (action == 2) {
            int i = this.multipleDownNum;
            if (i == 1) {
                if (this.lastAction != 6 && this.scaleMode) {
                    float rawX = (motionEvent.getRawX() - this.lastTouchX) + getTranslationX();
                    float rawY = (motionEvent.getRawY() - this.lastTouchY) + getTranslationY();
                    if (motionEvent.getRawX() > this.lastTouchX && motionEvent.getRawX() - this.lastTouchX > (-r1.left)) {
                        rawX = ((-r1.left) + getTranslationX()) - 1.0f;
                    } else if (motionEvent.getRawX() < this.lastTouchX && motionEvent.getRawX() - this.lastTouchX < r2.right - r1.right) {
                        rawX = (r2.right - r1.right) + getTranslationX();
                    }
                    if (motionEvent.getRawY() > this.lastTouchY && motionEvent.getRawY() - this.lastTouchY > (-r1.top)) {
                        rawY = (-r1.top) + getTranslationY();
                    } else if (motionEvent.getRawY() < this.lastTouchY && motionEvent.getRawY() - this.lastTouchY < r2.bottom - r1.bottom) {
                        rawY = (r2.bottom - r1.bottom) + getTranslationY();
                    }
                    setTranslationX(rawX);
                    setTranslationY(rawY);
                }
                this.lastTouchX = motionEvent.getRawX();
                this.lastTouchY = motionEvent.getRawY();
            } else if (i == 2) {
                float scaleX = (float) (((double) getScaleX()) + (((double) (distance(motionEvent) - this.oriDis)) / ((double) getWidth())));
                this.scaleMode = true;
                if (scaleX < 1.0f) {
                    scaleX = 1.0f;
                } else {
                    float f = this.maxScale;
                    if (scaleX > f) {
                        scaleX = f;
                    }
                }
                if (scaleX == 1.0f) {
                    this.scaleMode = false;
                }
                setPivotX(this.mid.x);
                setPivotY(this.mid.y);
                setScaleX(scaleX);
                setScaleY(scaleX);
                Log.e("dsdsdsds", "mid.x=" + this.mid.x + "mid.y=" + this.mid.y + "mScaleX=" + scaleX);
                checkRectValid();
                this.lastTouchX = motionEvent.getRawX();
                this.lastTouchY = motionEvent.getRawY();
            }
        } else if (action == 6) {
            this.multipleDownNum--;
        } else {
            this.multipleDownNum = 0;
            this.lastTouchX = 0.0f;
            this.lastTouchY = 0.0f;
        }
        this.lastAction = action;
        return super.onTouchEvent(motionEvent);
    }

    public void reset() {
        this.scaleMode = false;
        setPivotX(0.0f);
        setPivotY(0.0f);
        setScaleX(1.0f);
        setScaleY(1.0f);
        checkRectValid();
    }

    private float distance(MotionEvent motionEvent) {
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return (float) Math.sqrt((x * x) + (y * y));
    }

    public boolean isScaleMode() {
        return this.scaleMode;
    }

    public void setScaleMode(boolean z) {
        this.scaleMode = z;
    }

    private void setSelfPivot(float f, float f2) {
        float pivotX = getPivotX() + f;
        float pivotY = getPivotY() + f2;
        if (pivotX < 0.0f && pivotY < 0.0f) {
            pivotY = 0.0f;
            pivotX = 0.0f;
        } else if (pivotX <= 0.0f || pivotY >= 0.0f) {
            if (pivotX >= 0.0f || pivotY <= 0.0f) {
                if (pivotX > getWidth()) {
                    pivotX = getWidth();
                }
                if (pivotY > getHeight()) {
                    pivotY = getHeight();
                }
            } else if (pivotY > getHeight()) {
                pivotY = getHeight();
                pivotX = 0.0f;
            } else {
                pivotX = 0.0f;
            }
        } else if (pivotX > getWidth()) {
            pivotX = getWidth();
            pivotY = 0.0f;
        } else {
            pivotY = 0.0f;
        }
        setPivotX(pivotX);
        setPivotY(pivotY);
    }
}
