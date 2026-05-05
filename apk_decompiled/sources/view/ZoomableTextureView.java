package view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.TextureView;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes5.dex */
public class ZoomableTextureView extends TextureView {
    private static final int DRAG = 1;
    private static final String MAX_SCALE_KEY = "maxScale";
    private static final String MIN_SCALE_KEY = "minScale";
    private static final int NONE = 0;
    private static final String SUPERSTATE_KEY = "superState";
    public static final String TAG = "ZoomableTextureView";
    private static final int ZOOM = 2;
    private float bottom;
    private Context context;
    boolean flag;
    private boolean isFirstViewEdgeTouch;
    public boolean isMove;
    private PointF last;
    private float[] m;
    private GestureDetector mGestureDetector;
    private Handler mHandler;
    private ScaleGestureDetector mScaleDetector;
    private Matrix matrix;
    private float maxScale;
    private float minScale;
    private int mode;
    private OnViewEdgeListener onViewEdgeListener;
    private OnZoomableTextureListener onZoomableTextureListener;
    private float right;
    private float saveScale;
    private PointF start;

    public interface OnViewEdgeListener {
        void onBottomEdge(ZoomableTextureView zoomableTextureView, float f);

        void onLeftEdge(ZoomableTextureView zoomableTextureView, float f);

        void onRightEdge(ZoomableTextureView zoomableTextureView, float f);

        void onTopEdge(ZoomableTextureView zoomableTextureView, float f);

        void onViewEdgeFirstTouched();
    }

    public interface OnZoomableTextureListener {
        boolean onDoubleTap(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent);

        void onLongPress(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent);

        void onScaleChanged(ZoomableTextureView zoomableTextureView, float f);

        boolean onSingleTapConfirmed(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent);
    }

    public ZoomableTextureView(Context context) {
        super(context);
        this.minScale = 1.0f;
        this.maxScale = 5.0f;
        this.saveScale = 1.0f;
        this.mode = 0;
        this.matrix = new Matrix();
        this.last = new PointF();
        this.start = new PointF();
        this.isFirstViewEdgeTouch = false;
        this.isMove = true;
        this.flag = true;
        this.context = context;
        initView((AttributeSet) null);
    }

    public void setMove(boolean z) {
        this.isMove = z;
    }

    public ZoomableTextureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.minScale = 1.0f;
        this.maxScale = 5.0f;
        this.saveScale = 1.0f;
        this.mode = 0;
        this.matrix = new Matrix();
        this.last = new PointF();
        this.start = new PointF();
        this.isFirstViewEdgeTouch = false;
        this.isMove = true;
        this.flag = true;
        this.context = context;
        initView(attributeSet);
    }

    public ZoomableTextureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.minScale = 1.0f;
        this.maxScale = 5.0f;
        this.saveScale = 1.0f;
        this.mode = 0;
        this.matrix = new Matrix();
        this.last = new PointF();
        this.start = new PointF();
        this.isFirstViewEdgeTouch = false;
        this.isMove = true;
        this.flag = true;
        this.context = context;
        initView(attributeSet);
    }

    public void setMaxScale(float f) {
        if (f >= 1.0f && f >= this.minScale) {
            this.maxScale = f;
            return;
        }
        throw new RuntimeException("maxScale can't be lower than 1 or minScale(" + this.minScale + ")");
    }

    public void setOnZoomableTextureListener(OnZoomableTextureListener onZoomableTextureListener) {
        this.onZoomableTextureListener = onZoomableTextureListener;
    }

    public void setOnViewEdgeListener(OnViewEdgeListener onViewEdgeListener) {
        this.onViewEdgeListener = onViewEdgeListener;
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPERSTATE_KEY, super.onSaveInstanceState());
        bundle.putFloat(MIN_SCALE_KEY, this.minScale);
        bundle.putFloat(MAX_SCALE_KEY, this.maxScale);
        return bundle;
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.minScale = bundle.getFloat(MIN_SCALE_KEY);
            this.maxScale = bundle.getFloat(MAX_SCALE_KEY);
            parcelable = bundle.getParcelable(SUPERSTATE_KEY);
        }
        super.onRestoreInstanceState(parcelable);
    }

    private void initView(AttributeSet attributeSet) {
        this.mHandler = new Handler();
        this.m = new float[9];
        this.mScaleDetector = new ScaleGestureDetector(this.context, new ScaleListener());
        this.mGestureDetector = new GestureDetector(this.context, new GestureListener());
    }

    public void zoomOut(boolean z) {
        if (z) {
            smoothScaleToTarget(20, 0L, this.minScale, getPivotX() / 2.0f, getPivotY() / 2.0f);
            return;
        }
        this.mode = 0;
        this.saveScale = this.minScale;
        this.right = 0.0f;
        this.bottom = 0.0f;
        PointF pointF = this.last;
        pointF.x = 0.0f;
        pointF.y = 0.0f;
        this.matrix.reset();
        setTransform(this.matrix);
        invalidate();
    }

    public float getScale() {
        return this.saveScale;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        if (!this.isMove) {
            return true;
        }
        this.mScaleDetector.onTouchEvent(motionEvent);
        this.mGestureDetector.onTouchEvent(motionEvent);
        this.matrix.getValues(this.m);
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.last.set(motionEvent.getX(), motionEvent.getY());
                this.start.set(this.last);
                this.mode = 1;
                this.isFirstViewEdgeTouch = false;
                break;
            case 1:
                this.mode = 0;
                break;
            case 2:
                ALog.d("ZoomableTextureView", "[" + hashCode() + "] move:\t" + this.mode + "\tsaveScale:\t" + this.saveScale + "\tminScale:\t" + this.minScale);
                int i = this.mode;
                if (i == 2 || (i == 1 && this.saveScale >= this.minScale)) {
                    ALog.d("ZoomableTextureView", " move:" + motionEvent.getX() + "   " + motionEvent.getY());
                    handleMove(motionEvent.getX(), motionEvent.getY());
                }
                break;
            case 5:
                this.last.set(motionEvent.getX(), motionEvent.getY());
                this.start.set(this.last);
                this.mode = 2;
                break;
            case 6:
                this.mode = 0;
                break;
        }
        setTransform(this.matrix);
        invalidate();
        return true;
    }

    public void handleMove(float f, float f2) {
        float[] fArr = this.m;
        float f3 = fArr[2];
        float f4 = fArr[5];
        PointF pointF = new PointF(f, f2);
        float f5 = pointF.x - this.last.x;
        float f6 = pointF.y - this.last.y;
        float f7 = f4 + f6;
        if (f7 > 0.0f) {
            if (this.onViewEdgeListener != null && this.mode == 1) {
                checkFirstViewEdgeTouch();
                this.onViewEdgeListener.onTopEdge(this, Math.abs(f6));
            }
            f6 = -f4;
        } else if (f7 < (-this.bottom)) {
            if (this.onViewEdgeListener != null && this.mode == 1) {
                checkFirstViewEdgeTouch();
                this.onViewEdgeListener.onBottomEdge(this, Math.abs(f6));
            }
            f6 = -(f4 + this.bottom);
        }
        float f8 = f3 + f5;
        if (f8 > 0.0f) {
            if (this.onViewEdgeListener != null && this.mode == 1) {
                checkFirstViewEdgeTouch();
                this.onViewEdgeListener.onLeftEdge(this, Math.abs(f5));
            }
            f5 = -f3;
        } else if (f8 < (-this.right)) {
            if (this.onViewEdgeListener != null && this.mode == 1) {
                checkFirstViewEdgeTouch();
                this.onViewEdgeListener.onRightEdge(this, Math.abs(f5));
            }
            f5 = -(f3 + this.right);
        }
        this.matrix.postTranslate(f5, f6);
        this.last.set(pointF.x, pointF.y);
    }

    public void stretching(float f, float f2, float f3, float f4) {
        float f5 = f / f3;
        float f6 = f2 / f4;
        this.matrix.preScale(f3 / f, f4 / f2);
        if (f5 >= f6) {
            this.matrix.preScale(f6, f6);
        } else {
            this.matrix.preScale(f5, f5);
        }
        setTransform(this.matrix);
        postInvalidate();
    }

    public void stretching2(float f, float f2, float f3, float f4) {
        Matrix matrix = new Matrix();
        matrix.postScale(((int) (f4 * 3.2222223f)) / f3, 1.0f);
        setTransform(matrix);
        postInvalidate();
    }

    public void setLast(float f, float f2) {
        this.matrix.postTranslate(f, f2);
        setTransform(this.matrix);
        invalidate();
    }

    public void setLast2(float f, float f2, float f3, float f4) {
        Log.e("缩放", "width=" + f + "  height=" + f2 + "  getWidth=" + f3 + "   getHeight=" + f4);
        this.matrix.setScale(1.6625f, f2 / f4);
        setTransform(this.matrix);
        invalidate();
    }

    public void setLast3(float f, float f2, float f3, float f4) {
        Log.e("缩放", "width=" + f + "  height=" + f2 + "  getWidth=" + f3 + "   getHeight=" + f4);
        float f5 = f2 / f4;
        this.matrix.postScale(1.005f, f5);
        this.matrix.postScale(0.995f, f5);
        setTransform(this.matrix);
        invalidate();
    }

    public void setLast4(float f, float f2, float f3, float f4) {
        Log.e("缩放", "width=" + f + "  height=" + f2 + "  getWidth=" + f3 + "   getHeight=" + f4);
        float f5 = f2 / f4;
        this.matrix.postScale(1.005f, f5);
        this.matrix.postScale(0.995f, f5);
        setTransform(this.matrix);
        invalidate();
    }

    private void checkFirstViewEdgeTouch() {
        if (this.isFirstViewEdgeTouch) {
            return;
        }
        this.onViewEdgeListener.onViewEdgeFirstTouched();
        this.isFirstViewEdgeTouch = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScale(float f, float f2, float f3) {
        float f4 = this.saveScale;
        this.saveScale = f4 * f;
        float f5 = this.saveScale;
        float f6 = this.maxScale;
        if (f5 > f6) {
            this.saveScale = f6;
            f = f6 / f4;
        } else {
            float f7 = this.minScale;
            if (f5 < f7) {
                this.saveScale = f7;
                f = f7 / f4;
            }
        }
        this.right = (getWidth() * this.saveScale) - getWidth();
        this.bottom = (getHeight() * this.saveScale) - getHeight();
        if (getWidth() < 0 && getHeight() < 0) {
            this.matrix.postScale(f, f, f2, f3);
            this.matrix.getValues(this.m);
            float[] fArr = this.m;
            float f8 = fArr[2];
            float f9 = fArr[5];
            if (f < 1.0f) {
                float f10 = this.right;
                if (f8 < (-f10)) {
                    this.matrix.postTranslate(-(f8 + f10), 0.0f);
                } else if (f8 > 0.0f) {
                    this.matrix.postTranslate(-f8, 0.0f);
                }
                float f11 = this.bottom;
                if (f9 < (-f11)) {
                    this.matrix.postTranslate(0.0f, -(f9 + f11));
                    return;
                } else {
                    if (f9 > 0.0f) {
                        this.matrix.postTranslate(0.0f, -f9);
                        return;
                    }
                    return;
                }
            }
            return;
        }
        this.matrix.postScale(f, f, f2, f3);
        if (f < 1.0f) {
            this.matrix.getValues(this.m);
            float[] fArr2 = this.m;
            float f12 = fArr2[2];
            float f13 = fArr2[5];
            if (f < 1.0f) {
                if (getWidth() > 0) {
                    float f14 = this.bottom;
                    if (f13 < (-f14)) {
                        this.matrix.postTranslate(0.0f, -(f13 + f14));
                        return;
                    } else {
                        if (f13 > 0.0f) {
                            this.matrix.postTranslate(0.0f, -f13);
                            return;
                        }
                        return;
                    }
                }
                float f15 = this.right;
                if (f12 < (-f15)) {
                    this.matrix.postTranslate(-(f12 + f15), 0.0f);
                } else if (f12 > 0.0f) {
                    this.matrix.postTranslate(-f12, 0.0f);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zoomInOrOut(MotionEvent motionEvent) {
        float f = this.saveScale;
        float f2 = this.minScale;
        if (f > f2) {
            smoothScaleToTarget(20, 0L, f2, motionEvent.getX(), motionEvent.getY());
        } else {
            smoothScaleToTarget(20, 0L, this.maxScale, motionEvent.getX(), motionEvent.getY());
        }
    }

    public boolean getFlag() {
        return this.flag;
    }

    public void smoothScaleToTarget(final int i, final long j, final float f, final float f2, final float f3) {
        this.mode = 2;
        this.mHandler.postDelayed(new Runnable() { // from class: view.ZoomableTextureView.1
            @Override // java.lang.Runnable
            public void run() {
                if (i != 0) {
                    ZoomableTextureView.this.matrix.getValues(ZoomableTextureView.this.m);
                    float f4 = (((f - ZoomableTextureView.this.saveScale) / i) + ZoomableTextureView.this.saveScale) / ZoomableTextureView.this.saveScale;
                    ALog.d("ZoomableTextureView", "[" + ZoomableTextureView.this.hashCode() + "]smoothScaleToTarget count=" + i + ", targetScale=" + f + ", scaleFactor=" + f4 + ", x=" + f2 + " y=" + f3);
                    ZoomableTextureView.this.handleScale(f4, f2, f3);
                    ZoomableTextureView.this.handleMove(f2, f3);
                    ZoomableTextureView zoomableTextureView = ZoomableTextureView.this;
                    zoomableTextureView.setTransform(zoomableTextureView.matrix);
                    ZoomableTextureView.this.invalidate();
                    ZoomableTextureView.this.smoothScaleToTarget(i + (-1), j, f, f2, f3);
                    if (ZoomableTextureView.this.onZoomableTextureListener != null) {
                        OnZoomableTextureListener onZoomableTextureListener = ZoomableTextureView.this.onZoomableTextureListener;
                        ZoomableTextureView zoomableTextureView2 = ZoomableTextureView.this;
                        onZoomableTextureListener.onScaleChanged(zoomableTextureView2, zoomableTextureView2.saveScale);
                        return;
                    }
                    return;
                }
                ALog.d("ZoomableTextureView", "[" + ZoomableTextureView.this.hashCode() + "]smoothScaleToTarget end");
                ZoomableTextureView.this.mode = 0;
                if (f == ZoomableTextureView.this.minScale) {
                    ZoomableTextureView.this.matrix.reset();
                    ZoomableTextureView zoomableTextureView3 = ZoomableTextureView.this;
                    zoomableTextureView3.setTransform(zoomableTextureView3.matrix);
                    ZoomableTextureView.this.invalidate();
                }
            }
        }, j);
    }

    public PointF getPF() {
        return this.last;
    }

    public void setPointF(float f, float f2) {
        PointF pointF = this.last;
        pointF.x = f;
        pointF.y = f2;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private GestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            ALog.d("ZoomableTextureView", "[" + ZoomableTextureView.this.hashCode() + "]onDoubleTap " + motionEvent.getAction() + " x=" + motionEvent.getX() + ", y=" + motionEvent.getY());
            if (ZoomableTextureView.this.onZoomableTextureListener != null && ZoomableTextureView.this.onZoomableTextureListener.onDoubleTap(ZoomableTextureView.this, motionEvent)) {
                return true;
            }
            ZoomableTextureView.this.zoomInOrOut(motionEvent);
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            ALog.d("ZoomableTextureView", "[" + ZoomableTextureView.this.hashCode() + "]onSingleTapConfirmed " + motionEvent.getAction() + " x=" + motionEvent.getX() + ", y=" + motionEvent.getY());
            if (ZoomableTextureView.this.onZoomableTextureListener != null) {
                return ZoomableTextureView.this.onZoomableTextureListener.onSingleTapConfirmed(ZoomableTextureView.this, motionEvent);
            }
            return false;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            ALog.d("ZoomableTextureView", "[" + ZoomableTextureView.this.hashCode() + "]onLongPress " + motionEvent.getAction() + " x=" + motionEvent.getX() + ", y=" + motionEvent.getY());
            if (ZoomableTextureView.this.onZoomableTextureListener != null) {
                ZoomableTextureView.this.onZoomableTextureListener.onLongPress(ZoomableTextureView.this, motionEvent);
            }
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            ZoomableTextureView.this.mode = 2;
            return true;
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            ALog.d("ZoomableTextureView", "[" + ZoomableTextureView.this.hashCode() + "]onScale " + scaleGestureDetector.getScaleFactor() + " focusX=" + scaleGestureDetector.getFocusX() + ", focusY=" + scaleGestureDetector.getFocusY());
            ZoomableTextureView.this.handleScale(scaleGestureDetector.getScaleFactor(), scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
            if (ZoomableTextureView.this.onZoomableTextureListener == null) {
                return true;
            }
            OnZoomableTextureListener onZoomableTextureListener = ZoomableTextureView.this.onZoomableTextureListener;
            ZoomableTextureView zoomableTextureView = ZoomableTextureView.this;
            onZoomableTextureListener.onScaleChanged(zoomableTextureView, zoomableTextureView.saveScale);
            return true;
        }
    }
}
