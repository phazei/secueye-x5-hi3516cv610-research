package view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import bean.PadLocationType;
import bean.PadStyle;
import bean.TouchViewModel;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class TouchView extends View {
    private static final String TAG = "rustAppTouchView";
    private Bitmap bgBmp;
    private boolean isMoving;
    private JoystickTouchViewListener jListener;
    private float mContentCenterX;
    private float mContentCenterY;
    private Bitmap mDirectionBmp;
    private PadLocationType mPadLocationType;
    private PadStyle mPadStyle;
    private float mRoundBgPadding;
    private int mRoundBgRadius;
    private float mWholePadHeight;
    private float mWholePadWid;
    protected float mWholeViewHeight;
    protected float mWholeViewWid;
    private TouchViewModel model;
    private float newHeight;
    private float newWid;
    private boolean shouldShowDirectionBmp;
    private Bitmap touchBmp;
    private float touchBmpDefaultX;
    private float touchBmpDefaultY;
    protected float touchImageX;
    protected float touchImageY;
    private ValueAnimator valueAnimatorResetX;
    private ValueAnimator valueAnimatorResetY;

    public TouchView(Context context) {
        super(context);
        this.mPadStyle = PadStyle.FIXED;
        this.mPadLocationType = PadLocationType.LEFT_BOT;
        this.mRoundBgPadding = 20.0f;
    }

    public TouchView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPadStyle = PadStyle.FIXED;
        this.mPadLocationType = PadLocationType.LEFT_BOT;
        this.mRoundBgPadding = 20.0f;
    }

    public TouchView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mPadStyle = PadStyle.FIXED;
        this.mPadLocationType = PadLocationType.LEFT_BOT;
        this.mRoundBgPadding = 20.0f;
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public float getRoundBgPadding() {
        return this.mRoundBgPadding;
    }

    public float getWholeViewWid() {
        return this.mWholeViewWid;
    }

    public float getWholeViewHeight() {
        return this.mWholeViewHeight;
    }

    public float getWholePadWid() {
        return this.mWholePadWid;
    }

    public float getWholePadHeight() {
        return this.mWholePadHeight;
    }

    public int getRoundBgRadius() {
        return this.mRoundBgRadius;
    }

    public void init(TouchViewModel touchViewModel) {
        this.mPadStyle = touchViewModel.getPadStyle();
        this.mPadLocationType = touchViewModel.getPadLocationType();
        Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(getResources(), touchViewModel.getBgResId());
        Bitmap bitmapDecodeResource2 = BitmapFactory.decodeResource(getResources(), touchViewModel.getTouchBmpResId());
        this.mRoundBgPadding = touchViewModel.getRoundBgPadding();
        this.mWholeViewHeight = touchViewModel.getWholeViewHeight();
        this.mWholeViewWid = touchViewModel.getWholeViewWid();
        this.mWholePadWid = touchViewModel.getWholePadWid();
        this.mWholePadHeight = touchViewModel.getWholePadHeight();
        this.mRoundBgRadius = touchViewModel.getRoundBgRadius();
        int i = this.mRoundBgRadius;
        float f = this.mRoundBgPadding;
        this.newWid = ((i - ((int) f)) * 2) - 20;
        this.newHeight = ((i - ((int) f)) * 2) - 20;
        this.isMoving = false;
        this.shouldShowDirectionBmp = touchViewModel.isShowDirectionPic();
        if (this.shouldShowDirectionBmp) {
            Bitmap bitmapDecodeResource3 = BitmapFactory.decodeResource(getResources(), touchViewModel.getDirectionPicResId());
            if (bitmapDecodeResource3.getWidth() == this.mWholePadWid && bitmapDecodeResource3.getHeight() == this.mWholePadHeight) {
                this.mDirectionBmp = bitmapDecodeResource3;
            } else {
                this.mDirectionBmp = Bitmap.createScaledBitmap(bitmapDecodeResource3, (int) this.newWid, (int) this.newHeight, true);
                bitmapDecodeResource3.recycle();
            }
        }
        if (bitmapDecodeResource.getWidth() == (this.mRoundBgRadius - ((int) this.mRoundBgPadding)) * 2 && bitmapDecodeResource.getHeight() == (this.mRoundBgRadius - ((int) this.mRoundBgPadding)) * 2) {
            this.bgBmp = bitmapDecodeResource;
        } else {
            int i2 = this.mRoundBgRadius;
            float f2 = this.mRoundBgPadding;
            this.bgBmp = Bitmap.createScaledBitmap(bitmapDecodeResource, (i2 - ((int) f2)) * 2, (i2 - ((int) f2)) * 2, true);
            bitmapDecodeResource.recycle();
        }
        if (bitmapDecodeResource2.getWidth() == touchViewModel.getTouchBallRadius() * 2 && bitmapDecodeResource2.getHeight() == touchViewModel.getTouchBallRadius() * 2) {
            this.touchBmp = bitmapDecodeResource2;
        } else if (touchViewModel.getTouchBallRadius() * 2 > 0 && touchViewModel.getTouchBallRadius() * 2 > 0) {
            this.touchBmp = Bitmap.createScaledBitmap(bitmapDecodeResource2, touchViewModel.getTouchBallRadius() * 2, touchViewModel.getTouchBallRadius() * 2, true);
            bitmapDecodeResource2.recycle();
        }
        setupContentCenter();
        Bitmap bitmap = this.touchBmp;
        if (bitmap == null || bitmap.getWidth() == 0) {
            return;
        }
        this.touchBmpDefaultX = this.mContentCenterX - (this.touchBmp.getWidth() / 2);
        this.touchBmpDefaultY = this.mContentCenterY - (this.touchBmp.getWidth() / 2);
        this.touchImageX = this.touchBmpDefaultX;
        this.touchImageY = this.touchBmpDefaultY;
    }

    private void setupContentCenter() {
        switch (this.mPadLocationType) {
            case LEFT_BOT:
                this.mContentCenterX = this.mWholePadWid / 2.0f;
                this.mContentCenterY = this.mWholeViewHeight - (this.mWholePadHeight / 2.0f);
                break;
            case RIGHT_BOT:
                this.mContentCenterX = this.mWholeViewWid - (this.mWholePadWid / 2.0f);
                this.mContentCenterY = this.mWholeViewHeight - (this.mWholePadHeight / 2.0f);
                break;
            case CENTER:
                this.mContentCenterX = this.mWholeViewWid / 2.0f;
                this.mContentCenterY = this.mWholeViewHeight / 2.0f;
                break;
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(measureWidth(i), measureHeight(i2));
    }

    private int measureHeight(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == 1073741824) {
            return size;
        }
        int wholeViewWid = (int) getWholeViewWid();
        return mode == Integer.MIN_VALUE ? Math.min(wholeViewWid, size) : wholeViewWid;
    }

    private int measureWidth(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == 1073741824) {
            return size;
        }
        int wholeViewHeight = (int) getWholeViewHeight();
        return mode == Integer.MIN_VALUE ? Math.min(wholeViewHeight, size) : wholeViewHeight;
    }

    public TouchViewModel getModel() {
        if (this.model == null) {
            this.model = new TouchViewModel(R.drawable.ui_pic_joystick_right_pad2, R.drawable.ui_pic_joystick_control_ball2);
        }
        return this.model;
    }

    public void setDefaultSize(int i, int i2) {
        float f = i;
        this.model.setWholeViewSize(f, f);
        this.model.setPadSize(f, f);
        int i3 = i / 2;
        this.model.setContentSize(i3, i3 / 4);
        this.model.setStyle(PadStyle.FIXED, PadLocationType.CENTER);
        this.model.setDirectionPicResId(R.drawable.ui_pic_joystick_arrow);
        this.model.setRoundBgPadding(i2);
        init(this.model);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x004a  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onDraw(android.graphics.Canvas r13) {
        /*
            r12 = this;
            android.graphics.Bitmap r0 = r12.bgBmp
            if (r0 == 0) goto L96
            int r0 = r12.getWidth()
            if (r0 <= 0) goto L96
            android.graphics.Bitmap r0 = r12.bgBmp
            float r1 = r12.mContentCenterX
            int r2 = r0.getWidth()
            int r2 = r2 / 2
            float r2 = (float) r2
            float r1 = r1 - r2
            float r2 = r12.mContentCenterY
            android.graphics.Bitmap r3 = r12.bgBmp
            int r3 = r3.getHeight()
            int r3 = r3 / 2
            float r3 = (float) r3
            float r2 = r2 - r3
            r3 = 0
            r13.drawBitmap(r0, r1, r2, r3)
            boolean r0 = r12.shouldShowDirectionBmp
            if (r0 == 0) goto L85
            float r0 = r12.touchBmpDefaultX
            float r1 = r12.touchImageX
            r2 = 1120403456(0x42c80000, float:100.0)
            float r4 = r1 - r2
            int r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r4 < 0) goto L4a
            float r4 = r12.touchBmpDefaultY
            float r5 = r12.touchImageY
            float r6 = r5 - r2
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 < 0) goto L4a
            float r1 = r1 + r2
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto L4a
            float r5 = r5 + r2
            int r0 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r0 <= 0) goto L85
        L4a:
            float r0 = r12.mContentCenterX
            double r4 = (double) r0
            float r0 = r12.mContentCenterY
            double r6 = (double) r0
            float r0 = r12.touchImageX
            android.graphics.Bitmap r1 = r12.touchBmp
            int r1 = r1.getWidth()
            int r1 = r1 / 2
            float r1 = (float) r1
            float r0 = r0 + r1
            double r8 = (double) r0
            float r0 = r12.touchImageY
            android.graphics.Bitmap r1 = r12.touchBmp
            int r1 = r1.getWidth()
            int r1 = r1 / 2
            float r1 = (float) r1
            float r0 = r0 + r1
            double r10 = (double) r0
            double r0 = view.RoundCalculator.calTwoPointAngleDegree(r4, r6, r8, r10)
            float r0 = (float) r0
            android.graphics.Bitmap r1 = r12.mDirectionBmp
            r2 = 1127481344(0x43340000, float:180.0)
            float r2 = r2 - r0
            float r0 = r12.mContentCenterX
            float r4 = r12.newWid
            r5 = 1073741824(0x40000000, float:2.0)
            float r4 = r4 / r5
            float r0 = r0 - r4
            float r4 = r12.mContentCenterY
            float r6 = r12.newHeight
            float r6 = r6 / r5
            float r4 = r4 - r6
            drawRotateBitmap(r13, r1, r2, r0, r4)
        L85:
            android.graphics.Bitmap r0 = r12.touchBmp
            boolean r0 = r0.isRecycled()
            if (r0 != 0) goto L96
            android.graphics.Bitmap r0 = r12.touchBmp
            float r1 = r12.touchImageX
            float r2 = r12.touchImageY
            r13.drawBitmap(r0, r1, r2, r3)
        L96:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: view.TouchView.onDraw(android.graphics.Canvas):void");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        if (motionEvent.getY() > 800.0f) {
            this.isMoving = false;
            setupContentCenter();
            reset();
            return true;
        }
        if (motionEvent.getAction() == 1) {
            this.isMoving = false;
            setupContentCenter();
            reset();
            JoystickTouchViewListener joystickTouchViewListener = this.jListener;
            if (joystickTouchViewListener != null) {
                joystickTouchViewListener.onActionUp();
            }
        } else if (motionEvent.getAction() == 0) {
            switch (this.mPadStyle) {
                case FLOATING:
                    if (motionEvent.getX() > (this.mWholeViewWid / 2.0f) + (this.mWholePadWid / 2.0f) || motionEvent.getY() < this.mWholePadHeight / 2.0f) {
                        return false;
                    }
                    this.mContentCenterX = motionEvent.getX();
                    this.mContentCenterY = motionEvent.getY();
                    this.isMoving = true;
                    userMoving(motionEvent);
                    break;
                    break;
                case FIXED:
                    if (motionEvent.getX() < this.mContentCenterX - (this.mWholePadWid / 2.0f) || motionEvent.getX() > this.mContentCenterX + (this.mWholePadWid / 2.0f) || motionEvent.getY() < this.mContentCenterY - (this.mWholePadHeight / 2.0f) || motionEvent.getY() > this.mContentCenterY + (this.mWholePadHeight / 2.0f)) {
                        return false;
                    }
                    this.isMoving = true;
                    userMoving(motionEvent);
                    JoystickTouchViewListener joystickTouchViewListener2 = this.jListener;
                    if (joystickTouchViewListener2 != null) {
                        joystickTouchViewListener2.onActionDown();
                    }
                    break;
            }
        } else if (this.isMoving) {
            userMoving(motionEvent);
        }
        return true;
    }

    private void userMoving(MotionEvent motionEvent) {
        ValueAnimator valueAnimator = this.valueAnimatorResetX;
        if (valueAnimator != null && this.valueAnimatorResetY != null) {
            valueAnimator.removeAllUpdateListeners();
            this.valueAnimatorResetY.removeAllUpdateListeners();
        }
        float fCalTwoPointDistant = (float) RoundCalculator.calTwoPointDistant(this.mContentCenterX, this.mContentCenterY, motionEvent.getX(), motionEvent.getY());
        double width = ((this.bgBmp.getWidth() - this.touchBmp.getWidth()) / 2) + 20;
        if (fCalTwoPointDistant <= width) {
            onBallMove(motionEvent.getX(), motionEvent.getY(), motionEvent);
        } else {
            double[] dArrCalPointLocationByAngle = RoundCalculator.calPointLocationByAngle(this.mContentCenterX, this.mContentCenterY, motionEvent.getX(), motionEvent.getY(), width);
            onBallMove((float) dArrCalPointLocationByAngle[0], (float) dArrCalPointLocationByAngle[1], motionEvent);
        }
    }

    protected void onBallMove(float f, float f2, MotionEvent motionEvent) {
        float width = f - (this.touchBmp.getWidth() / 2);
        float height = f2 - (this.touchBmp.getHeight() / 2);
        if (width != this.touchImageX || height != this.touchImageY) {
            invalidate();
        }
        this.touchImageX = width;
        this.touchImageY = height;
        if (this.jListener != null) {
            float f3 = this.mContentCenterX;
            this.bgBmp.getWidth();
            this.touchBmp.getWidth();
            float f4 = this.mContentCenterY;
            this.bgBmp.getHeight();
            this.touchBmp.getHeight();
            this.jListener.onTouch(f - this.mContentCenterX, this.mContentCenterY - f2);
        }
    }

    public void setPadStyle(PadStyle padStyle) {
        this.mPadStyle = padStyle;
    }

    protected void reset() {
        this.valueAnimatorResetX = new ValueAnimator();
        this.valueAnimatorResetX.setFloatValues(this.touchImageX, this.touchBmpDefaultX);
        this.valueAnimatorResetX.setDuration(200L);
        this.valueAnimatorResetX.start();
        this.valueAnimatorResetX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: view.TouchView.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                TouchView.this.touchImageX = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                TouchView.this.invalidate();
            }
        });
        this.valueAnimatorResetY = new ValueAnimator();
        this.valueAnimatorResetY.setFloatValues(this.touchImageY, this.touchBmpDefaultY);
        this.valueAnimatorResetY.setDuration(200L);
        this.valueAnimatorResetY.start();
        this.valueAnimatorResetY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: view.TouchView.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                TouchView.this.touchImageY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                TouchView.this.invalidate();
            }
        });
        JoystickTouchViewListener joystickTouchViewListener = this.jListener;
        if (joystickTouchViewListener != null) {
            joystickTouchViewListener.onReset();
        }
    }

    public void resetView() {
        this.valueAnimatorResetX = new ValueAnimator();
        this.valueAnimatorResetX.setFloatValues(this.touchImageX, this.touchBmpDefaultX);
        this.valueAnimatorResetX.setDuration(200L);
        this.valueAnimatorResetX.start();
        this.valueAnimatorResetX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: view.TouchView.3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                TouchView.this.touchImageX = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                TouchView.this.invalidate();
            }
        });
        this.valueAnimatorResetY = new ValueAnimator();
        this.valueAnimatorResetY.setFloatValues(this.touchImageY, this.touchBmpDefaultY);
        this.valueAnimatorResetY.setDuration(200L);
        this.valueAnimatorResetY.start();
        this.valueAnimatorResetY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: view.TouchView.4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                TouchView.this.touchImageY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                TouchView.this.invalidate();
            }
        });
        JoystickTouchViewListener joystickTouchViewListener = this.jListener;
        if (joystickTouchViewListener != null) {
            joystickTouchViewListener.onReset();
        }
    }

    public void setListener(JoystickTouchViewListener joystickTouchViewListener) {
        this.jListener = joystickTouchViewListener;
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        if (z || !this.isMoving) {
            return;
        }
        this.isMoving = false;
        reset();
    }

    private static void drawRotateBitmap(Canvas canvas, Bitmap bitmap, float f, float f2, float f3) {
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth() / 2;
        int height = bitmap.getHeight() / 2;
        matrix.postTranslate(-width, -height);
        matrix.postRotate(f);
        matrix.postTranslate(f2 + width, f3 + height);
        canvas.drawBitmap(bitmap, matrix, null);
    }
}
