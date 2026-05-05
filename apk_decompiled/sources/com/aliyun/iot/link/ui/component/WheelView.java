package com.aliyun.iot.link.ui.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes2.dex */
public class WheelView extends View {
    private static final int CLICK_DISTANCE = 2;
    private static final int GO_ON_MOVE_END = 10011;
    private static final int GO_ON_MOVE_INTERRUPTED = 10012;
    private static final int GO_ON_MOVE_REFRESH = 10010;
    private static final int GO_ON_REFRESH_INTERVAL_MILLIS = 10;
    private static final int MOVE_NUMBER = 1;
    private static final int REFRESH_VIEW = 1;
    private static final int SHOWTIME = 200;
    private static final int SLOW_MOVE_SPEED = 1;
    private static final String TAG = "WheelView";
    private boolean _isCyclic;
    private Handler callbackHandler;
    private int clickDistance;
    private int clickTimeout;
    private float controlHeight;
    private float controlWidth;
    private ArrayList<String> dataList;
    private int defaultIndex;
    private float density;
    private long downTime;
    private int downY;
    private int goOnDistance;
    int goOnLimit;
    private int goOnMove;
    Interpolator goonInterpolator;
    private boolean isClearing;
    private boolean isCyclic;
    private boolean isEnable;
    private boolean isGoOnMove;
    private boolean isScrolling;
    private ArrayList<ItemObject> itemList;
    private int itemNumber;
    private float lastMeasuredHeight;
    private int lastY;
    private int lineColor;
    private float lineHeight;
    private Paint linePaint;
    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;
    private String mSuffix;
    private float mSuffixPadding;
    private TextPaint mSuffixPaint;
    private VelocityTracker mVelocityTracker;
    private int maxTextWidth;
    private String maxWidthText;
    private int moveDistance;
    private Handler moveHandler;
    private HandlerThread moveHandlerThread;
    private boolean noEmpty;
    private int normalColor;
    private float normalFont;
    private OnSelectListener onSelectListener;
    private boolean scale;
    private int selectedColor;
    private float selectedFont;
    private int showTime;
    private int slowMoveSpeed;
    private ItemObject[] toShowItems;
    private float unitHeight;

    public interface OnSelectListener {
        void endSelect(int i, String str);

        void selecting(int i, String str);
    }

    static /* synthetic */ int access$108(WheelView wheelView) {
        int i = wheelView.showTime;
        wheelView.showTime = i + 1;
        return i;
    }

    public WheelView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isScrolling = false;
        this.itemList = new ArrayList<>();
        this.dataList = new ArrayList<>();
        this.downTime = 0L;
        this.density = 1.0f;
        this.slowMoveSpeed = 1;
        this.clickDistance = 2;
        this.clickTimeout = 100;
        this.lineColor = -1644826;
        this.lineHeight = 2.0f;
        this.normalFont = 14.0f;
        this.selectedFont = 22.0f;
        this.unitHeight = 50.0f;
        this.itemNumber = 5;
        this.scale = true;
        this.normalColor = -13421773;
        this.selectedColor = -13421773;
        this.isEnable = true;
        this.noEmpty = true;
        this.isCyclic = true;
        this._isCyclic = true;
        this.isClearing = false;
        this.goonInterpolator = new DecelerateInterpolator(2.0f);
        this.showTime = 0;
        this.isGoOnMove = false;
        this.mSuffix = "";
        init(context, attributeSet);
        initData();
    }

    public WheelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isScrolling = false;
        this.itemList = new ArrayList<>();
        this.dataList = new ArrayList<>();
        this.downTime = 0L;
        this.density = 1.0f;
        this.slowMoveSpeed = 1;
        this.clickDistance = 2;
        this.clickTimeout = 100;
        this.lineColor = -1644826;
        this.lineHeight = 2.0f;
        this.normalFont = 14.0f;
        this.selectedFont = 22.0f;
        this.unitHeight = 50.0f;
        this.itemNumber = 5;
        this.scale = true;
        this.normalColor = -13421773;
        this.selectedColor = -13421773;
        this.isEnable = true;
        this.noEmpty = true;
        this.isCyclic = true;
        this._isCyclic = true;
        this.isClearing = false;
        this.goonInterpolator = new DecelerateInterpolator(2.0f);
        this.showTime = 0;
        this.isGoOnMove = false;
        this.mSuffix = "";
        init(context, attributeSet);
        initData();
    }

    public WheelView(Context context) {
        super(context);
        this.isScrolling = false;
        this.itemList = new ArrayList<>();
        this.dataList = new ArrayList<>();
        this.downTime = 0L;
        this.density = 1.0f;
        this.slowMoveSpeed = 1;
        this.clickDistance = 2;
        this.clickTimeout = 100;
        this.lineColor = -1644826;
        this.lineHeight = 2.0f;
        this.normalFont = 14.0f;
        this.selectedFont = 22.0f;
        this.unitHeight = 50.0f;
        this.itemNumber = 5;
        this.scale = true;
        this.normalColor = -13421773;
        this.selectedColor = -13421773;
        this.isEnable = true;
        this.noEmpty = true;
        this.isCyclic = true;
        this._isCyclic = true;
        this.isClearing = false;
        this.goonInterpolator = new DecelerateInterpolator(2.0f);
        this.showTime = 0;
        this.isGoOnMove = false;
        this.mSuffix = "";
        initData();
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.WheelView);
        this.unitHeight = (int) typedArrayObtainStyledAttributes.getDimension(R.styleable.WheelView_unitHeight, this.unitHeight);
        this.itemNumber = typedArrayObtainStyledAttributes.getInt(R.styleable.WheelView_itemNumber, this.itemNumber);
        this.scale = typedArrayObtainStyledAttributes.getBoolean(R.styleable.WheelView_scale, true);
        this.normalFont = typedArrayObtainStyledAttributes.getDimension(R.styleable.WheelView_normalTextSize, this.normalFont);
        this.selectedFont = typedArrayObtainStyledAttributes.getDimension(R.styleable.WheelView_selectedTextSize, this.selectedFont);
        this.normalColor = typedArrayObtainStyledAttributes.getColor(R.styleable.WheelView_normalTextColor, this.normalColor);
        this.selectedColor = typedArrayObtainStyledAttributes.getColor(R.styleable.WheelView_selectedTextColor, this.selectedColor);
        this.lineColor = typedArrayObtainStyledAttributes.getColor(R.styleable.WheelView_lineColor, this.lineColor);
        this.lineHeight = typedArrayObtainStyledAttributes.getDimension(R.styleable.WheelView_lineHeight, this.lineHeight);
        this.noEmpty = typedArrayObtainStyledAttributes.getBoolean(R.styleable.WheelView_noEmpty, true);
        this.isEnable = typedArrayObtainStyledAttributes.getBoolean(R.styleable.WheelView_isEnable, true);
        this.isCyclic = typedArrayObtainStyledAttributes.getBoolean(R.styleable.WheelView_isCyclic, true);
        this.mSuffix = typedArrayObtainStyledAttributes.getString(R.styleable.WheelView_suffix);
        this.mSuffixPadding = typedArrayObtainStyledAttributes.getDimension(R.styleable.WheelView_suffixPadding, 0.0f);
        typedArrayObtainStyledAttributes.recycle();
        if (TextUtils.isEmpty(this.mSuffix)) {
            this.mSuffix = "";
        }
        this.density = context.getResources().getDisplayMetrics().density;
        float f = this.density;
        this.slowMoveSpeed = (int) (1.0f * f);
        this.clickDistance = (int) (f * 2.0f);
        int i = this.itemNumber;
        this.controlHeight = i * this.unitHeight;
        this.toShowItems = new ItemObject[i + 2];
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.clickTimeout = ViewConfiguration.getTapTimeout();
        this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.callbackHandler = new Handler(Looper.getMainLooper());
        this.mSuffixPaint = new TextPaint(1);
        this.mSuffixPaint.setTextSize(this.selectedFont);
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.moveHandler = new GoOnHandler(Looper.getMainLooper());
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void _setIsCyclic(boolean z) {
        if (this.dataList.size() < this.itemNumber + 2) {
            this._isCyclic = false;
        } else {
            this._isCyclic = z;
        }
    }

    private class GoOnHandler extends Handler {
        GoOnHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = WheelView.this.goOnDistance;
            switch (message.what) {
                case 10010:
                    WheelView.access$108(WheelView.this);
                    WheelView wheelView = WheelView.this;
                    wheelView.goOnDistance = (int) (wheelView.goonInterpolator.getInterpolation(WheelView.this.showTime / 200.0f) * WheelView.this.goOnLimit);
                    WheelView wheelView2 = WheelView.this;
                    wheelView2.actionThreadMove(wheelView2.goOnMove > 0 ? WheelView.this.goOnDistance - i : (WheelView.this.goOnDistance - i) * (-1));
                    if (WheelView.this.showTime >= 200 || !WheelView.this.isGoOnMove || (WheelView.this.showTime >= 40 && Math.abs(i - WheelView.this.goOnDistance) < WheelView.this.slowMoveSpeed)) {
                        WheelView.this.isGoOnMove = false;
                        WheelView.this.moveHandler.sendEmptyMessage(10011);
                    } else {
                        WheelView.this.moveHandler.sendEmptyMessageDelayed(10010, 10L);
                    }
                    break;
                case 10011:
                    WheelView wheelView3 = WheelView.this;
                    wheelView3.slowMove(wheelView3.goOnMove > 0 ? WheelView.this.slowMoveSpeed : WheelView.this.slowMoveSpeed * (-1));
                    WheelView.this.isScrolling = false;
                    WheelView.this.isGoOnMove = false;
                    WheelView.this.goOnDistance = 0;
                    WheelView.this.goOnLimit = 0;
                    break;
                case 10012:
                    WheelView.this.moveDistance += WheelView.this.goOnMove > 0 ? WheelView.this.goOnDistance - i : (WheelView.this.goOnDistance - i) * (-1);
                    WheelView.this.goOnDistance = 0;
                    WheelView.this.isScrolling = false;
                    WheelView.this.isGoOnMove = false;
                    WheelView.this.findItemsToShow();
                    WheelView.this.postInvalidate();
                    break;
            }
        }
    }

    private synchronized void goonMove(int i, long j) {
        this.showTime = 0;
        int iAbs = Math.abs(i / 10);
        if (((long) this.goOnMove) * j > 0) {
            this.goOnLimit += iAbs;
        } else {
            this.goOnLimit = iAbs;
        }
        this.goOnMove = (int) j;
        this.isGoOnMove = true;
        this.moveHandler.sendEmptyMessage(10010);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.isEnable) {
            return true;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int y = (int) motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 0:
                getParent().requestDisallowInterceptTouchEvent(true);
                if (this.isScrolling) {
                    this.isGoOnMove = false;
                    Handler handler = this.moveHandler;
                    if (handler != null) {
                        handler.removeMessages(10010);
                        this.moveHandler.sendEmptyMessage(10012);
                    }
                }
                this.isScrolling = true;
                this.downY = (int) motionEvent.getY();
                this.lastY = (int) motionEvent.getY();
                this.downTime = System.currentTimeMillis();
                return true;
            case 1:
                long jCurrentTimeMillis = System.currentTimeMillis() - this.downTime;
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
                int yVelocity = (int) velocityTracker.getYVelocity();
                if (Math.abs(yVelocity) > this.mMinimumFlingVelocity) {
                    goonMove(yVelocity, y - this.downY);
                } else {
                    if (Math.abs(y - this.downY) <= this.clickDistance && jCurrentTimeMillis <= this.clickTimeout) {
                        int i = this.downY;
                        float f = i;
                        float f2 = this.unitHeight;
                        if (f < ((this.itemNumber / 2) * f2) + ((f2 * 1.0f) / 3.0f) && i > 0) {
                            actionMove((int) (f2 / 3.0f));
                            slowMove(((int) this.unitHeight) / 3);
                        } else {
                            int i2 = this.downY;
                            float f3 = i2;
                            float f4 = this.controlHeight;
                            float f5 = this.unitHeight;
                            if (f3 > (f4 - ((this.itemNumber / 2) * f5)) - ((1.0f * f5) / 3.0f) && i2 < f4) {
                                actionMove(-((int) (f5 / 3.0f)));
                                slowMove((-((int) this.unitHeight)) / 3);
                            } else {
                                noEmpty(y - this.downY);
                            }
                        }
                    } else {
                        slowMove(y - this.downY);
                    }
                    this.isScrolling = false;
                }
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                return true;
            case 2:
                this.isGoOnMove = false;
                this.isScrolling = true;
                actionMove(y - this.lastY);
                this.lastY = y;
                return true;
            default:
                return true;
        }
    }

    private void initData() {
        this.isClearing = true;
        this.itemList.clear();
        this.maxWidthText = "";
        TextPaint textPaint = new TextPaint(1);
        textPaint.setTextSize(this.selectedFont);
        for (int i = 0; i < this.dataList.size(); i++) {
            ItemObject itemObject = new ItemObject();
            itemObject.id = i;
            itemObject.setItemText(this.dataList.get(i));
            itemObject.x = 0;
            itemObject.y = (int) (i * this.unitHeight);
            this.itemList.add(itemObject);
            Rect rect = new Rect();
            textPaint.getTextBounds(this.dataList.get(i), 0, this.dataList.get(i).length(), rect);
            if (rect.width() > this.maxTextWidth) {
                this.maxWidthText = this.dataList.get(i);
                this.maxTextWidth = rect.width();
            }
        }
        this.isClearing = false;
        _setIsCyclic(this.isCyclic);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i2);
        int selected = getSelected();
        if (mode == Integer.MIN_VALUE) {
            int size = View.MeasureSpec.getSize(i2);
            float f = size;
            if (f < this.controlHeight && size != 0) {
                this.controlHeight = f;
                this.unitHeight = (int) (this.controlHeight / this.itemNumber);
                this.unitHeight = Math.max(1.0f, this.unitHeight);
            }
        } else if (mode == 1073741824) {
            this.controlHeight = View.MeasureSpec.getSize(i2);
            this.unitHeight = (int) (this.controlHeight / this.itemNumber);
            this.unitHeight = Math.max(1.0f, this.unitHeight);
        }
        setMeasuredDimension(View.MeasureSpec.getSize(i), (int) this.controlHeight);
        if (Math.abs(this.lastMeasuredHeight - this.controlHeight) > 0.1d) {
            initData();
            if (selected != -1) {
                setDefault(selected);
            } else {
                setDefault(this.defaultIndex);
            }
            this.lastMeasuredHeight = this.controlHeight;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.controlWidth = getWidth();
        drawLine(canvas);
        drawList(canvas);
        drawSuffix(canvas);
        drawMask(canvas);
    }

    private void drawSuffix(Canvas canvas) {
        this.mSuffixPaint.setColor(this.selectedColor);
        this.mSuffixPaint.setTextSize(this.selectedFont);
        Rect rect = new Rect();
        TextPaint textPaint = this.mSuffixPaint;
        String str = this.mSuffix;
        textPaint.getTextBounds(str, 0, str.length(), rect);
        canvas.drawText(this.mSuffix, (this.controlWidth / 2.0f) + this.maxTextWidth + this.mSuffixPadding, (getHeight() / 2.0f) + (rect.height() / 2.0f), this.mSuffixPaint);
    }

    private void drawLine(Canvas canvas) {
        if (this.linePaint == null) {
            this.linePaint = new Paint();
            this.linePaint.setColor(this.lineColor);
            this.linePaint.setAntiAlias(true);
            this.linePaint.setStrokeWidth(this.lineHeight);
        }
        float f = this.controlHeight;
        float f2 = this.unitHeight;
        float f3 = this.lineHeight;
        canvas.drawLine(0.0f, ((f / 2.0f) - (f2 / 2.0f)) + f3, this.controlWidth, ((f / 2.0f) - (f2 / 2.0f)) + f3, this.linePaint);
        float f4 = this.controlHeight;
        float f5 = this.unitHeight;
        float f6 = this.lineHeight;
        canvas.drawLine(0.0f, ((f4 / 2.0f) + (f5 / 2.0f)) - f6, this.controlWidth, ((f4 / 2.0f) + (f5 / 2.0f)) - f6, this.linePaint);
    }

    private synchronized void drawList(Canvas canvas) {
        if (this.isClearing) {
            return;
        }
        synchronized (this.toShowItems) {
            for (ItemObject itemObject : this.toShowItems) {
                if (itemObject != null) {
                    itemObject.drawSelf(canvas, getMeasuredWidth());
                }
            }
        }
    }

    private void drawMask(Canvas canvas) {
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, this.controlHeight / 2.0f, -1, 16777215, Shader.TileMode.MIRROR);
        Paint paint = new Paint();
        paint.setShader(linearGradient);
        canvas.drawRect(0.0f, 0.0f, this.controlWidth, (this.itemNumber / 2.0f) * this.unitHeight, paint);
        float f = this.controlHeight;
        LinearGradient linearGradient2 = new LinearGradient(0.0f, f / 2.0f, 0.0f, f, 16777215, -1, Shader.TileMode.MIRROR);
        Paint paint2 = new Paint();
        paint2.setShader(linearGradient2);
        float f2 = this.controlHeight;
        canvas.drawRect(0.0f, f2 - ((this.itemNumber / 2) * this.unitHeight), this.controlWidth, f2, paint2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void noEmpty(int i) {
        if (this.noEmpty) {
            synchronized (this.toShowItems) {
                findItemsToShow();
                for (ItemObject itemObject : this.toShowItems) {
                    if (itemObject != null && itemObject.selected()) {
                        int iMoveToSelected = (int) itemObject.moveToSelected();
                        onEndSelecting(itemObject);
                        defaultMove(iMoveToSelected);
                        return;
                    }
                }
                if (i > 0) {
                    for (ItemObject itemObject2 : this.toShowItems) {
                        if (itemObject2 != null && itemObject2.couldSelected()) {
                            int iMoveToSelected2 = (int) itemObject2.moveToSelected();
                            onEndSelecting(itemObject2);
                            defaultMove(iMoveToSelected2);
                            return;
                        }
                    }
                } else {
                    for (int length = this.toShowItems.length - 1; length >= 0; length--) {
                        if (this.toShowItems[length] != null && this.toShowItems[length].couldSelected()) {
                            int iMoveToSelected3 = (int) this.toShowItems[length].moveToSelected();
                            onEndSelecting(this.toShowItems[length]);
                            defaultMove(iMoveToSelected3);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void onEndSelecting(final ItemObject itemObject) {
        if (this.onSelectListener != null) {
            this.callbackHandler.post(new Runnable() { // from class: com.aliyun.iot.link.ui.component.WheelView.1
                @Override // java.lang.Runnable
                public void run() {
                    WheelView.this.onSelectListener.endSelect(itemObject.id, itemObject.getItemText());
                }
            });
        }
    }

    private void actionMove(int i) {
        this.moveDistance -= i;
        findItemsToShow();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void actionThreadMove(int i) {
        this.moveDistance -= i;
        findItemsToShow();
        postInvalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void findItemsToShow() {
        int i = 0;
        if (this._isCyclic) {
            if (this.moveDistance > this.unitHeight * this.itemList.size()) {
                this.moveDistance %= ((int) this.unitHeight) * this.itemList.size();
            } else {
                int i2 = this.moveDistance;
                if (i2 < 0) {
                    this.moveDistance = (i2 % (((int) this.unitHeight) * this.itemList.size())) + (((int) this.unitHeight) * this.itemList.size());
                }
            }
            int i3 = this.moveDistance;
            if (this.itemList.size() <= 0) {
                return;
            }
            float f = this.itemList.get(0).y + i3;
            int iAbs = (int) Math.abs(f / this.unitHeight);
            int i4 = (int) (f - (this.unitHeight * iAbs));
            synchronized (this.toShowItems) {
                while (i < this.toShowItems.length) {
                    int size = iAbs + i;
                    if (size < 0) {
                        size += this.itemList.size();
                    } else if (size >= this.itemList.size()) {
                        size -= this.itemList.size();
                    }
                    if (size < this.itemList.size()) {
                        this.toShowItems[i] = this.itemList.get(size);
                        this.toShowItems[i].move(((int) (this.unitHeight * ((i - size) % this.itemList.size()))) - i4);
                    }
                    i++;
                }
            }
        } else {
            float f2 = this.moveDistance;
            float size2 = this.unitHeight * this.itemList.size();
            int i5 = this.itemNumber;
            float f3 = this.unitHeight;
            if (f2 > (size2 - ((i5 / 2) * f3)) - f3) {
                float f4 = this.itemNumber / 2;
                float f5 = this.unitHeight;
                this.moveDistance = (int) (((f3 * this.itemList.size()) - (f4 * f5)) - f5);
                this.moveHandler.removeMessages(10010);
                this.moveHandler.sendEmptyMessage(10012);
            } else if (this.moveDistance < ((-i5) / 2) * f3) {
                this.moveDistance = (int) (((-i5) / 2) * f3);
                this.moveHandler.removeMessages(10010);
                this.moveHandler.sendEmptyMessage(10012);
            }
            int i6 = this.moveDistance;
            if (this.itemList.size() <= 0) {
                return;
            }
            float f6 = this.itemList.get(0).y + i6;
            float f7 = this.unitHeight;
            int i7 = (int) (f6 / f7);
            int i8 = (int) (f6 - (f7 * i7));
            synchronized (this.toShowItems) {
                while (i < this.toShowItems.length) {
                    int i9 = i7 + i;
                    if (i9 < 0 || i9 >= this.itemList.size()) {
                        i9 = -1;
                    }
                    if (i9 == -1) {
                        this.toShowItems[i] = null;
                    } else {
                        this.toShowItems[i] = this.itemList.get(i9);
                        this.toShowItems[i].move(((int) (this.unitHeight * (i - i9))) - i8);
                    }
                    i++;
                }
            }
        }
        if (this.onSelectListener == null || this.toShowItems[this.itemNumber / 2] == null) {
            return;
        }
        this.callbackHandler.post(new Runnable() { // from class: com.aliyun.iot.link.ui.component.WheelView.2
            @Override // java.lang.Runnable
            public void run() {
                WheelView.this.onSelectListener.selecting(WheelView.this.toShowItems[WheelView.this.itemNumber / 2].id, WheelView.this.toShowItems[WheelView.this.itemNumber / 2].getItemText());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void slowMove(final int i) {
        if (this.moveHandler == null) {
            return;
        }
        this.moveHandler.post(new Runnable() { // from class: com.aliyun.iot.link.ui.component.WheelView.3
            @Override // java.lang.Runnable
            public void run() {
                int iMoveToSelected;
                int iMoveToSelected2;
                WheelView.this.findItemsToShow();
                int selected = WheelView.this.getSelected();
                if (selected != -1) {
                    iMoveToSelected2 = (int) ((ItemObject) WheelView.this.itemList.get(selected)).moveToSelected();
                } else {
                    synchronized (WheelView.this.toShowItems) {
                        iMoveToSelected = 0;
                        if (i > 0) {
                            ItemObject[] itemObjectArr = WheelView.this.toShowItems;
                            int length = itemObjectArr.length;
                            int i2 = 0;
                            while (true) {
                                if (i2 < length) {
                                    ItemObject itemObject = itemObjectArr[i2];
                                    if (itemObject != null && itemObject.couldSelected()) {
                                        iMoveToSelected = (int) itemObject.moveToSelected();
                                        break;
                                    }
                                    i2++;
                                } else {
                                    break;
                                }
                            }
                        } else {
                            int length2 = WheelView.this.toShowItems.length - 1;
                            while (true) {
                                if (length2 >= 0) {
                                    if (WheelView.this.toShowItems[length2] != null && WheelView.this.toShowItems[length2].couldSelected()) {
                                        iMoveToSelected = (int) WheelView.this.toShowItems[length2].moveToSelected();
                                        break;
                                    }
                                    length2--;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    iMoveToSelected2 = iMoveToSelected;
                }
                int i3 = iMoveToSelected2 > 0 ? iMoveToSelected2 : iMoveToSelected2 * (-1);
                int i4 = iMoveToSelected2 <= 0 ? -1 : 1;
                int i5 = WheelView.this.slowMoveSpeed;
                while (true) {
                    if (i3 == 0) {
                        break;
                    }
                    i3 -= i5;
                    if (i3 < 0) {
                        WheelView.this.moveDistance -= i3 * i4;
                        WheelView.this.findItemsToShow();
                        WheelView.this.postInvalidate();
                        try {
                            Thread.sleep(10L);
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        WheelView.this.moveDistance -= i5 * i4;
                        WheelView.this.findItemsToShow();
                        WheelView.this.postInvalidate();
                        try {
                            Thread.sleep(10L);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
                WheelView.this.noEmpty(i);
            }
        });
    }

    private void defaultMove(int i) {
        this.moveDistance -= i;
        findItemsToShow();
        postInvalidate();
    }

    public void setData(ArrayList<String> arrayList) {
        if (arrayList != null) {
            this.dataList.clear();
            this.dataList.addAll(arrayList);
        }
        initData();
    }

    public void refreshData(ArrayList<String> arrayList) {
        setData(arrayList);
        findItemsToShow();
        invalidate();
    }

    public int getSelected() {
        synchronized (this.toShowItems) {
            for (ItemObject itemObject : this.toShowItems) {
                if (itemObject != null && itemObject.selected()) {
                    return itemObject.id;
                }
            }
            return -1;
        }
    }

    public String getSelectedText() {
        synchronized (this.toShowItems) {
            for (ItemObject itemObject : this.toShowItems) {
                if (itemObject != null && itemObject.selected()) {
                    return itemObject.getItemText();
                }
            }
            return "";
        }
    }

    public boolean isScrolling() {
        return this.isScrolling;
    }

    public boolean isEnable() {
        return this.isEnable;
    }

    public void setEnable(boolean z) {
        this.isEnable = z;
    }

    public void setDefault(int i) {
        this.defaultIndex = i;
        if (i > this.itemList.size() - 1) {
            return;
        }
        this.moveDistance = 0;
        Iterator<ItemObject> it = this.itemList.iterator();
        while (it.hasNext()) {
            it.next().move = 0;
        }
        findItemsToShow();
        defaultMove((int) this.itemList.get(i).moveToSelected());
    }

    public int getListSize() {
        ArrayList<ItemObject> arrayList = this.itemList;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    public String getItemText(int i) {
        ArrayList<ItemObject> arrayList = this.itemList;
        return arrayList == null ? "" : arrayList.get(i).getItemText();
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public int getItemNumber() {
        return this.itemNumber;
    }

    public void setItemNumber(int i) {
        this.itemNumber = i;
        this.controlHeight = i * this.unitHeight;
        this.toShowItems = new ItemObject[i + 2];
        requestLayout();
    }

    public boolean isCyclic() {
        return this.isCyclic;
    }

    public void setCyclic(boolean z) {
        this.isCyclic = z;
        _setIsCyclic(z);
    }

    public void setSuffix(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mSuffix = str;
        postInvalidate();
    }

    public void stopScroll() {
        Handler handler = this.moveHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            slowMove(0);
        }
    }

    private class ItemObject {
        int id;
        private String itemText;
        int move;
        private boolean shouldRefreshTextPaint;
        private TextPaint textPaint;
        private Rect textRect;
        int x;
        int y;

        private ItemObject() {
            this.id = 0;
            this.itemText = "";
            this.x = 0;
            this.y = 0;
            this.move = 0;
            this.shouldRefreshTextPaint = true;
        }

        public void drawSelf(Canvas canvas, int i) {
            if (isInView()) {
                if (this.textPaint == null) {
                    this.textPaint = new TextPaint();
                    this.textPaint.setAntiAlias(true);
                }
                if (this.textRect == null) {
                    this.textRect = new Rect();
                }
                if (couldSelected()) {
                    this.textPaint.setColor(WheelView.this.selectedColor);
                    float fMoveToSelected = moveToSelected();
                    if (fMoveToSelected <= 0.0f) {
                        fMoveToSelected *= -1.0f;
                    }
                    if (WheelView.this.scale) {
                        this.textPaint.setTextSize(WheelView.this.normalFont + ((WheelView.this.selectedFont - WheelView.this.normalFont) * (1.0f - (fMoveToSelected / WheelView.this.unitHeight))));
                    } else {
                        this.textPaint.setTextSize(WheelView.this.normalFont);
                    }
                } else {
                    this.textPaint.setColor(WheelView.this.normalColor);
                    this.textPaint.setTextSize(WheelView.this.normalFont);
                }
                if (WheelView.this.scale && WheelView.this.unitHeight < Math.max(WheelView.this.selectedFont, WheelView.this.normalFont)) {
                    this.textPaint.setTextSize(WheelView.this.unitHeight - (WheelView.this.lineHeight * 2.0f));
                }
                if (this.shouldRefreshTextPaint) {
                    this.itemText = (String) TextUtils.ellipsize(this.itemText, this.textPaint, i, TextUtils.TruncateAt.END);
                    TextPaint textPaint = this.textPaint;
                    String str = this.itemText;
                    textPaint.getTextBounds(str, 0, str.length(), this.textRect);
                    if (WheelView.this.selectedFont == WheelView.this.normalFont) {
                        this.shouldRefreshTextPaint = false;
                    }
                }
                if (isInView()) {
                    canvas.drawText(this.itemText, (this.x + (WheelView.this.controlWidth / 2.0f)) - (this.textRect.width() / 2.0f), this.y + this.move + (WheelView.this.unitHeight / 2.0f) + (this.textRect.height() / 2.0f), this.textPaint);
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:9:0x0026  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public synchronized boolean isInView() {
            /*
                r2 = this;
                monitor-enter(r2)
                int r0 = r2.y     // Catch: java.lang.Throwable -> L29
                int r1 = r2.move     // Catch: java.lang.Throwable -> L29
                int r0 = r0 + r1
                float r0 = (float) r0     // Catch: java.lang.Throwable -> L29
                com.aliyun.iot.link.ui.component.WheelView r1 = com.aliyun.iot.link.ui.component.WheelView.this     // Catch: java.lang.Throwable -> L29
                float r1 = com.aliyun.iot.link.ui.component.WheelView.access$2500(r1)     // Catch: java.lang.Throwable -> L29
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 > 0) goto L26
                int r0 = r2.y     // Catch: java.lang.Throwable -> L29
                float r0 = (float) r0     // Catch: java.lang.Throwable -> L29
                int r1 = r2.move     // Catch: java.lang.Throwable -> L29
                float r1 = (float) r1     // Catch: java.lang.Throwable -> L29
                float r0 = r0 + r1
                com.aliyun.iot.link.ui.component.WheelView r1 = com.aliyun.iot.link.ui.component.WheelView.this     // Catch: java.lang.Throwable -> L29
                float r1 = com.aliyun.iot.link.ui.component.WheelView.access$2100(r1)     // Catch: java.lang.Throwable -> L29
                float r0 = r0 + r1
                r1 = 0
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 < 0) goto L26
                r0 = 1
                goto L27
            L26:
                r0 = 0
            L27:
                monitor-exit(r2)
                return r0
            L29:
                r0 = move-exception
                monitor-exit(r2)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iot.link.ui.component.WheelView.ItemObject.isInView():boolean");
        }

        public synchronized void move(int i) {
            this.move = i;
        }

        /* JADX WARN: Removed duplicated region for block: B:9:0x0048  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public synchronized boolean couldSelected() {
            /*
                r5 = this;
                monitor-enter(r5)
                r0 = 1
                int r1 = r5.y     // Catch: java.lang.Throwable -> L4b
                int r2 = r5.move     // Catch: java.lang.Throwable -> L4b
                int r1 = r1 + r2
                float r1 = (float) r1     // Catch: java.lang.Throwable -> L4b
                com.aliyun.iot.link.ui.component.WheelView r2 = com.aliyun.iot.link.ui.component.WheelView.this     // Catch: java.lang.Throwable -> L4b
                int r2 = com.aliyun.iot.link.ui.component.WheelView.access$1400(r2)     // Catch: java.lang.Throwable -> L4b
                int r2 = r2 / 2
                float r2 = (float) r2     // Catch: java.lang.Throwable -> L4b
                com.aliyun.iot.link.ui.component.WheelView r3 = com.aliyun.iot.link.ui.component.WheelView.this     // Catch: java.lang.Throwable -> L4b
                float r3 = com.aliyun.iot.link.ui.component.WheelView.access$2100(r3)     // Catch: java.lang.Throwable -> L4b
                float r2 = r2 * r3
                com.aliyun.iot.link.ui.component.WheelView r3 = com.aliyun.iot.link.ui.component.WheelView.this     // Catch: java.lang.Throwable -> L4b
                float r3 = com.aliyun.iot.link.ui.component.WheelView.access$2100(r3)     // Catch: java.lang.Throwable -> L4b
                r4 = 1073741824(0x40000000, float:2.0)
                float r3 = r3 / r4
                float r2 = r2 - r3
                int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
                if (r1 <= 0) goto L48
                int r1 = r5.y     // Catch: java.lang.Throwable -> L4b
                int r2 = r5.move     // Catch: java.lang.Throwable -> L4b
                int r1 = r1 + r2
                float r1 = (float) r1     // Catch: java.lang.Throwable -> L4b
                com.aliyun.iot.link.ui.component.WheelView r2 = com.aliyun.iot.link.ui.component.WheelView.this     // Catch: java.lang.Throwable -> L4b
                int r2 = com.aliyun.iot.link.ui.component.WheelView.access$1400(r2)     // Catch: java.lang.Throwable -> L4b
                int r2 = r2 / 2
                float r2 = (float) r2     // Catch: java.lang.Throwable -> L4b
                com.aliyun.iot.link.ui.component.WheelView r3 = com.aliyun.iot.link.ui.component.WheelView.this     // Catch: java.lang.Throwable -> L4b
                float r3 = com.aliyun.iot.link.ui.component.WheelView.access$2100(r3)     // Catch: java.lang.Throwable -> L4b
                float r2 = r2 * r3
                com.aliyun.iot.link.ui.component.WheelView r3 = com.aliyun.iot.link.ui.component.WheelView.this     // Catch: java.lang.Throwable -> L4b
                float r3 = com.aliyun.iot.link.ui.component.WheelView.access$2100(r3)     // Catch: java.lang.Throwable -> L4b
                float r3 = r3 / r4
                float r2 = r2 + r3
                int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
                if (r1 < 0) goto L49
            L48:
                r0 = 0
            L49:
                monitor-exit(r5)
                return r0
            L4b:
                r0 = move-exception
                monitor-exit(r5)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iot.link.ui.component.WheelView.ItemObject.couldSelected():boolean");
        }

        public synchronized boolean selected() {
            boolean z = false;
            if (this.textRect == null) {
                return false;
            }
            if (this.y + this.move >= (((WheelView.this.itemNumber / 2) * WheelView.this.unitHeight) - (WheelView.this.unitHeight / 2.0f)) + (this.textRect.height() / 2.0f)) {
                if (this.y + this.move <= (((WheelView.this.itemNumber / 2) * WheelView.this.unitHeight) + (WheelView.this.unitHeight / 2.0f)) - (this.textRect.height() / 2.0f)) {
                    z = true;
                }
            }
            return z;
        }

        public String getItemText() {
            return this.itemText;
        }

        public void setItemText(String str) {
            this.shouldRefreshTextPaint = true;
            this.itemText = str;
        }

        public synchronized float moveToSelected() {
            return ((WheelView.this.controlHeight / 2.0f) - (WheelView.this.unitHeight / 2.0f)) - (this.y + this.move);
        }
    }
}
