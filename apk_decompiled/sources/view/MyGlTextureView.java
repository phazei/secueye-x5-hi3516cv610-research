package view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import java.math.BigDecimal;
import tools.ScreenUtil;

/* JADX INFO: loaded from: classes5.dex */
public class MyGlTextureView extends ZoomableTextureView {
    public boolean firstAddZoom;
    float keepX;
    float keepY;
    private int lastAction;
    private float lastTouchX;
    private float lastTouchY;
    private Handler mHandler;
    private Matrix matrix;
    private float maxScale;
    private PointF mid;
    private float minScale;
    private int mode;
    private int multipleDownNum;
    private int offset;
    private float oriDis;
    protected int s_height;
    protected int s_width;
    private boolean scaleMode;
    int times;
    private float x;
    private float y;

    public MyGlTextureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mid = new PointF();
        this.maxScale = 5.0f;
        this.mode = 0;
        this.minScale = 1.0f;
        this.matrix = new Matrix();
        this.x = getPivotX();
        this.y = getPivotY();
        this.times = 0;
        this.firstAddZoom = true;
        this.offset = ScreenUtil.dp2Px(context, 100.0f);
    }

    public MyGlTextureView(Context context) {
        super(context);
        this.mid = new PointF();
        this.maxScale = 5.0f;
        this.mode = 0;
        this.minScale = 1.0f;
        this.matrix = new Matrix();
        this.x = getPivotX();
        this.y = getPivotY();
        this.times = 0;
        this.firstAddZoom = true;
        this.offset = ScreenUtil.dp2Px(context, 100.0f);
    }

    @Override // android.view.TextureView, android.view.View
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

    @Override // view.ZoomableTextureView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Log.d("ZoomableTextureView", "getXY-------- touch: " + motionEvent.getX() + "   " + motionEvent.getY());
        return super.onTouchEvent(motionEvent);
    }

    public void reset() {
        zoomOut(true);
    }

    public void reset2() {
        zoomOut(false);
    }

    public void addZoom() {
        int i = this.times;
        if (i < 50) {
            this.times = i + 1;
        }
        double dDoubleValue = new BigDecimal(getScale()).setScale(2, 4).doubleValue();
        if (dDoubleValue < this.maxScale && dDoubleValue < 2.0d) {
            dDoubleValue += 0.05000000074505806d;
        } else if (dDoubleValue >= 2.0d && dDoubleValue < this.maxScale) {
            dDoubleValue += 0.10000000149011612d;
        }
        if (this.firstAddZoom) {
            this.firstAddZoom = false;
            setPointF(getPivotX(), getPivotY());
            this.keepX = getPivotX();
            this.keepY = getPivotY();
            smoothScaleToTarget(1, 0L, (float) dDoubleValue, this.x, this.y);
        }
        setPointF(this.keepX, this.keepY);
        smoothScaleToTarget(1, 0L, (float) dDoubleValue, getPF().x, getPF().y);
    }

    public void reduceZoom() {
        int i = this.times;
        if (i > 0) {
            this.times = i - 1;
        }
        double dDoubleValue = new BigDecimal(getScale()).setScale(2, 4).doubleValue();
        Log.d("ZoomableTextureView", "changeZoom: ----" + dDoubleValue);
        if (dDoubleValue > this.minScale && dDoubleValue <= 2.0d) {
            dDoubleValue -= 0.05000000074505806d;
        } else if (dDoubleValue > this.minScale && dDoubleValue > 2.0d) {
            dDoubleValue -= 0.10000000149011612d;
        }
        smoothScaleToTarget(1, 0L, (float) dDoubleValue, getPF().x, getPF().y);
    }

    public int getTimes() {
        return this.times;
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

    public float getMaxScale() {
        return this.maxScale;
    }

    public void fanZ(float f) {
        smoothScaleToTarget(1, 0L, f, this.x, this.y);
    }

    public void Right(Context context, double d2, double d3) {
        Log.e("缩放", "width=" + d2 + "  height=" + d3);
        stretching((float) d2, (float) d3, (float) this.s_width, (float) this.s_height);
        setMove(false);
    }

    public void Zomm(double d2) {
        float f = (float) d2;
        smoothScaleToTarget(1, 0L, f, r11 / 2, (float) (((double) (this.s_width / 2)) / 1.82d));
        smoothScaleToTarget(0, 0L, f, r11 / 2, (float) (((double) (this.s_width / 2)) / 1.82d));
    }

    public void LeftMove(Context context, double d2, double d3) {
        setLast((float) d2, (float) d3);
        setMove(false);
    }

    public void RightMove(Context context, double d2, double d3) {
        setLast((float) (-d2), (float) d3);
        setMove(false);
    }

    public void Left(Context context, double d2, double d3) {
        setMove(false);
        stretching((float) d2, (float) d3, this.s_width, this.s_height);
    }
}
