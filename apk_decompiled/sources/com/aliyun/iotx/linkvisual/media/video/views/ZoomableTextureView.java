package com.aliyun.iotx.linkvisual.media.video.views;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.TextureView;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
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
    private boolean isFirstViewEdgeTouch;
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

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            ALog.d("ZoomableTextureView", "[" + ZoomableTextureView.this.hashCode() + "]onLongPress " + motionEvent.getAction() + " x=" + motionEvent.getX() + ", y=" + motionEvent.getY());
            if (ZoomableTextureView.this.onZoomableTextureListener != null) {
                ZoomableTextureView.this.onZoomableTextureListener.onLongPress(ZoomableTextureView.this, motionEvent);
            }
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            ALog.d("ZoomableTextureView", "[" + ZoomableTextureView.this.hashCode() + "]onSingleTapConfirmed " + motionEvent.getAction() + " x=" + motionEvent.getX() + ", y=" + motionEvent.getY());
            if (ZoomableTextureView.this.onZoomableTextureListener != null) {
                return ZoomableTextureView.this.onZoomableTextureListener.onSingleTapConfirmed(ZoomableTextureView.this, motionEvent);
            }
            return false;
        }
    }

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

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ScaleListener() {
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

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            ZoomableTextureView.this.mode = 2;
            return true;
        }
    }

    public ZoomableTextureView(Context context) {
        super(context);
        this.minScale = 1.0f;
        this.maxScale = 4.0f;
        this.saveScale = 1.0f;
        this.mode = 0;
        this.matrix = new Matrix();
        this.last = new PointF();
        this.start = new PointF();
        this.isFirstViewEdgeTouch = false;
        this.context = context;
        initView(null);
    }

    public ZoomableTextureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.minScale = 1.0f;
        this.maxScale = 4.0f;
        this.saveScale = 1.0f;
        this.mode = 0;
        this.matrix = new Matrix();
        this.last = new PointF();
        this.start = new PointF();
        this.isFirstViewEdgeTouch = false;
        this.context = context;
        initView(attributeSet);
    }

    public ZoomableTextureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.minScale = 1.0f;
        this.maxScale = 4.0f;
        this.saveScale = 1.0f;
        this.mode = 0;
        this.matrix = new Matrix();
        this.last = new PointF();
        this.start = new PointF();
        this.isFirstViewEdgeTouch = false;
        this.context = context;
        initView(attributeSet);
    }

    private void checkFirstViewEdgeTouch() {
        if (this.isFirstViewEdgeTouch) {
            return;
        }
        this.onViewEdgeListener.onViewEdgeFirstTouched();
        this.isFirstViewEdgeTouch = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0072  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void handleMove(float r8, float r9) {
        /*
            r7 = this;
            float[] r0 = r7.m
            r1 = 2
            r1 = r0[r1]
            r2 = 5
            r0 = r0[r2]
            android.graphics.PointF r2 = new android.graphics.PointF
            r2.<init>(r8, r9)
            float r8 = r2.x
            android.graphics.PointF r9 = r7.last
            float r3 = r9.x
            float r8 = r8 - r3
            float r3 = r2.y
            float r9 = r9.y
            float r3 = r3 - r9
            float r9 = r0 + r3
            r4 = 0
            int r5 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1))
            r6 = 1
            if (r5 <= 0) goto L37
            com.aliyun.iotx.linkvisual.media.video.views.ZoomableTextureView$OnViewEdgeListener r9 = r7.onViewEdgeListener
            if (r9 == 0) goto L35
            int r9 = r7.mode
            if (r9 != r6) goto L35
            r7.checkFirstViewEdgeTouch()
            com.aliyun.iotx.linkvisual.media.video.views.ZoomableTextureView$OnViewEdgeListener r9 = r7.onViewEdgeListener
            float r3 = java.lang.Math.abs(r3)
            r9.onTopEdge(r7, r3)
        L35:
            float r3 = -r0
            goto L56
        L37:
            float r5 = r7.bottom
            float r5 = -r5
            int r9 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
            if (r9 >= 0) goto L56
            com.aliyun.iotx.linkvisual.media.video.views.ZoomableTextureView$OnViewEdgeListener r9 = r7.onViewEdgeListener
            if (r9 == 0) goto L52
            int r9 = r7.mode
            if (r9 != r6) goto L52
            r7.checkFirstViewEdgeTouch()
            com.aliyun.iotx.linkvisual.media.video.views.ZoomableTextureView$OnViewEdgeListener r9 = r7.onViewEdgeListener
            float r3 = java.lang.Math.abs(r3)
            r9.onBottomEdge(r7, r3)
        L52:
            float r9 = r7.bottom
            float r0 = r0 + r9
            goto L35
        L56:
            float r9 = r1 + r8
            int r0 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1))
            if (r0 <= 0) goto L72
            com.aliyun.iotx.linkvisual.media.video.views.ZoomableTextureView$OnViewEdgeListener r9 = r7.onViewEdgeListener
            if (r9 == 0) goto L70
            int r9 = r7.mode
            if (r9 != r6) goto L70
            r7.checkFirstViewEdgeTouch()
            com.aliyun.iotx.linkvisual.media.video.views.ZoomableTextureView$OnViewEdgeListener r9 = r7.onViewEdgeListener
            float r8 = java.lang.Math.abs(r8)
            r9.onLeftEdge(r7, r8)
        L70:
            float r8 = -r1
            goto L91
        L72:
            float r0 = r7.right
            float r0 = -r0
            int r9 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r9 >= 0) goto L91
            com.aliyun.iotx.linkvisual.media.video.views.ZoomableTextureView$OnViewEdgeListener r9 = r7.onViewEdgeListener
            if (r9 == 0) goto L8d
            int r9 = r7.mode
            if (r9 != r6) goto L8d
            r7.checkFirstViewEdgeTouch()
            com.aliyun.iotx.linkvisual.media.video.views.ZoomableTextureView$OnViewEdgeListener r9 = r7.onViewEdgeListener
            float r8 = java.lang.Math.abs(r8)
            r9.onRightEdge(r7, r8)
        L8d:
            float r8 = r7.right
            float r1 = r1 + r8
            goto L70
        L91:
            android.graphics.Matrix r9 = r7.matrix
            r9.postTranslate(r8, r3)
            android.graphics.PointF r8 = r7.last
            float r9 = r2.x
            float r0 = r2.y
            r8.set(r9, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iotx.linkvisual.media.video.views.ZoomableTextureView.handleMove(float, float):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:4:0x000c A[PHI: r2
  0x000c: PHI (r2v3 float) = (r2v0 float), (r2v1 float) binds: [B:3:0x000a, B:6:0x0015] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void handleScale(float r6, float r7, float r8) {
        /*
            Method dump skipped, instruction units count: 216
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iotx.linkvisual.media.video.views.ZoomableTextureView.handleScale(float, float, float):void");
    }

    private void initView(AttributeSet attributeSet) {
        this.mHandler = new Handler();
        this.m = new float[9];
        this.mScaleDetector = new ScaleGestureDetector(this.context, new ScaleListener());
        this.mGestureDetector = new GestureDetector(this.context, new GestureListener());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void smoothScaleToTarget(final int i, final long j, final float f, final float f2, final float f3) {
        this.mode = 2;
        this.mHandler.postDelayed(new Runnable() { // from class: com.aliyun.iotx.linkvisual.media.video.views.ZoomableTextureView.1
            @Override // java.lang.Runnable
            public void run() {
                if (i == 0) {
                    ALog.d("ZoomableTextureView", "[" + ZoomableTextureView.this.hashCode() + "]smoothScaleToTarget end");
                    ZoomableTextureView.this.mode = 0;
                    if (f == ZoomableTextureView.this.minScale) {
                        ZoomableTextureView.this.matrix.reset();
                        ZoomableTextureView zoomableTextureView = ZoomableTextureView.this;
                        zoomableTextureView.setTransform(zoomableTextureView.matrix);
                        ZoomableTextureView.this.invalidate();
                        return;
                    }
                    return;
                }
                ZoomableTextureView.this.matrix.getValues(ZoomableTextureView.this.m);
                float f4 = (((f - ZoomableTextureView.this.saveScale) / i) + ZoomableTextureView.this.saveScale) / ZoomableTextureView.this.saveScale;
                ALog.d("ZoomableTextureView", "[" + ZoomableTextureView.this.hashCode() + "]smoothScaleToTarget count=" + i + ", targetScale=" + f + ", scaleFactor=" + f4 + ", x=" + f2 + " y=" + f3);
                ZoomableTextureView.this.handleScale(f4, f2, f3);
                ZoomableTextureView.this.handleMove(f2, f3);
                ZoomableTextureView zoomableTextureView2 = ZoomableTextureView.this;
                zoomableTextureView2.setTransform(zoomableTextureView2.matrix);
                ZoomableTextureView.this.invalidate();
                ZoomableTextureView.this.smoothScaleToTarget(i + (-1), j, f, f2, f3);
                if (ZoomableTextureView.this.onZoomableTextureListener != null) {
                    OnZoomableTextureListener onZoomableTextureListener = ZoomableTextureView.this.onZoomableTextureListener;
                    ZoomableTextureView zoomableTextureView3 = ZoomableTextureView.this;
                    onZoomableTextureListener.onScaleChanged(zoomableTextureView3, zoomableTextureView3.saveScale);
                }
            }
        }, j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zoomInOrOut(MotionEvent motionEvent) {
        float f = this.saveScale;
        float f2 = this.minScale;
        if (f <= f2) {
            f2 = this.maxScale;
        }
        smoothScaleToTarget(20, 0L, f2, motionEvent.getX(), motionEvent.getY());
    }

    public float getMaxScale() {
        return this.maxScale;
    }

    public float getMinScale() {
        return this.minScale;
    }

    public float getScale() {
        return this.saveScale;
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

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPERSTATE_KEY, super.onSaveInstanceState());
        bundle.putFloat(MIN_SCALE_KEY, this.minScale);
        bundle.putFloat(MAX_SCALE_KEY, this.maxScale);
        return bundle;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0092  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r6) {
        /*
            r5 = this;
            super.onTouchEvent(r6)
            android.view.ScaleGestureDetector r0 = r5.mScaleDetector
            r0.onTouchEvent(r6)
            android.view.GestureDetector r0 = r5.mGestureDetector
            r0.onTouchEvent(r6)
            android.graphics.Matrix r0 = r5.matrix
            float[] r1 = r5.m
            r0.getValues(r1)
            int r0 = r6.getActionMasked()
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L95
            if (r0 == r2) goto L92
            r3 = 2
            if (r0 == r3) goto L40
            r4 = 5
            if (r0 == r4) goto L29
            r6 = 6
            if (r0 == r6) goto L92
            goto Lad
        L29:
            android.graphics.PointF r0 = r5.last
            float r1 = r6.getX()
            float r6 = r6.getY()
            r0.set(r1, r6)
            android.graphics.PointF r6 = r5.start
            android.graphics.PointF r0 = r5.last
            r6.set(r0)
            r5.mode = r3
            goto Lad
        L40:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "["
            r0.append(r1)
            int r1 = r5.hashCode()
            r0.append(r1)
            java.lang.String r1 = "] move:\t"
            r0.append(r1)
            int r1 = r5.mode
            r0.append(r1)
            java.lang.String r1 = "\tsaveScale:\t"
            r0.append(r1)
            float r1 = r5.saveScale
            r0.append(r1)
            java.lang.String r1 = "\tminScale:\t"
            r0.append(r1)
            float r1 = r5.minScale
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "ZoomableTextureView"
            com.aliyun.alink.linksdk.tools.ALog.d(r1, r0)
            int r0 = r5.mode
            if (r0 == r3) goto L86
            if (r0 != r2) goto Lad
            float r0 = r5.saveScale
            float r1 = r5.minScale
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto Lad
        L86:
            float r0 = r6.getX()
            float r6 = r6.getY()
            r5.handleMove(r0, r6)
            goto Lad
        L92:
            r5.mode = r1
            goto Lad
        L95:
            android.graphics.PointF r0 = r5.last
            float r3 = r6.getX()
            float r6 = r6.getY()
            r0.set(r3, r6)
            android.graphics.PointF r6 = r5.start
            android.graphics.PointF r0 = r5.last
            r6.set(r0)
            r5.mode = r2
            r5.isFirstViewEdgeTouch = r1
        Lad:
            android.graphics.Matrix r6 = r5.matrix
            r5.setTransform(r6)
            r5.invalidate()
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iotx.linkvisual.media.video.views.ZoomableTextureView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void setMaxScale(float f) {
        if (f >= 1.0f && f >= this.minScale) {
            this.maxScale = f;
            return;
        }
        throw new RuntimeException("maxScale can't be lower than 1 or minScale(" + this.minScale + ")");
    }

    public void setOnViewEdgeListener(OnViewEdgeListener onViewEdgeListener) {
        this.onViewEdgeListener = onViewEdgeListener;
    }

    public void setOnZoomableTextureListener(OnZoomableTextureListener onZoomableTextureListener) {
        this.onZoomableTextureListener = onZoomableTextureListener;
    }

    public void setZoomScale(boolean z, float f) {
        if (z) {
            smoothScaleToTarget(10, 0L, f, getPivotX(), getPivotY());
            return;
        }
        this.mode = 0;
        handleScale(f / this.saveScale, getPivotX(), getPivotY());
        setTransform(this.matrix);
        invalidate();
    }

    public void zoomOut(boolean z) {
        if (z) {
            smoothScaleToTarget(20, 0L, this.minScale, getPivotX(), getPivotY());
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
}
