package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;
import com.seculink.app.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import tools.LogEx;
import tools.ScreenUtil;

/* JADX INFO: loaded from: classes5.dex */
public class SeekTimeBar extends View {
    final int CHAR_MARGIN;
    final int DAY_TIME_MILLES;
    final int HOUR_SCALES;
    final int INT_MARGIN;
    final int LONG_MARGIN;
    int MARGIN;
    int PANDDING;
    final int SCALE_TOTAL;
    final int SHORT_MARGIN;
    final String SPLITS;
    final String SPLITS1;
    private Handler animationHandler;
    private int beforeTime;
    private List<ColorPaint> colorPaints;
    Context context;
    private int count;
    double currentIndex;
    String date;
    private List<Integer> endDates;
    private long fTime;
    Paint fileFildBluePaint;
    Paint fileFildRedPaint;
    boolean flag;
    private String hms;
    private int intScaleHeight;
    private int intScaleWidth;
    private boolean isSeeked;
    private boolean isSeeking;
    boolean isSmallscaleMode;
    public int lastDownX;
    public int lastDownY;
    double lastX;
    private int longScaleHeight;
    private int longScaleWidth;
    protected int mCurrentX;
    private GestureDetector mGesture;
    protected double mNextX;
    private GestureDetector.OnGestureListener mOnGesture;
    protected Scroller mScroller;
    private SeekPosListener onSeekPosListener;
    Paint paint;
    Paint placeholderPaint;
    private Handler scaleAnimationHandler;
    double scaleLong;
    Paint scalePaint;
    SimpleDateFormat sdf;
    private int shortScaleHeight;
    private int shortScaleWidth;
    int sss;
    private List<Integer> startDates;
    private long tempTime;
    OnTimeListener timeListener;
    Paint timePaint;
    int txtDateHeight;
    int txtDateWidth;
    Paint txtPaint;
    int txtPaintHeight;
    int txtPaintWidth;
    int txtTimeHeight;
    int txtTimeWidth;

    public enum ColorPaint {
        RED,
        BLUE
    }

    public interface OnTimeListener {
        void setTime(int i);

        void setTime(int i, int i2, int i3);
    }

    public interface SeekPosListener {
        void seekError();

        void seekPos(int i, int i2);
    }

    public SeekTimeBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isSmallscaleMode = true;
        this.LONG_MARGIN = 40;
        this.INT_MARGIN = 20;
        this.SHORT_MARGIN = 10;
        this.CHAR_MARGIN = 1;
        this.MARGIN = 1;
        this.SCALE_TOTAL = 1440;
        this.HOUR_SCALES = 60;
        this.DAY_TIME_MILLES = 86400;
        this.SPLITS = "&";
        this.SPLITS1 = ":";
        this.PANDDING = 20;
        this.isSeeking = false;
        this.isSeeked = false;
        this.txtPaintWidth = 0;
        this.txtPaintHeight = 0;
        this.txtDateWidth = 0;
        this.txtDateHeight = 0;
        this.txtTimeWidth = 0;
        this.txtTimeHeight = 0;
        this.scaleLong = 0.0d;
        this.lastX = 0.0d;
        this.currentIndex = 0.0d;
        this.date = "";
        this.longScaleWidth = getContext().getResources().getDimensionPixelSize(R.dimen.dimen_1);
        this.longScaleHeight = getContext().getResources().getDimensionPixelSize(R.dimen.dimen_20);
        this.shortScaleWidth = getContext().getResources().getDimensionPixelSize(R.dimen.dimen_1);
        this.shortScaleHeight = getContext().getResources().getDimensionPixelSize(R.dimen.dimen_10);
        this.intScaleWidth = getContext().getResources().getDimensionPixelSize(R.dimen.dimen_1);
        this.intScaleHeight = getContext().getResources().getDimensionPixelSize(R.dimen.dimen_15);
        this.sdf = new SimpleDateFormat("yyyy-MM-dd&HH:mm:ss");
        this.startDates = new ArrayList();
        this.endDates = new ArrayList();
        this.colorPaints = new ArrayList();
        this.tempTime = 0L;
        this.hms = "";
        this.beforeTime = 0;
        this.mOnGesture = new GestureDetector.SimpleOnGestureListener() { // from class: view.SeekTimeBar.2
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (SeekTimeBar.this.mScroller.computeScrollOffset()) {
                    return true;
                }
                SeekTimeBar.this.mScroller.fling((int) SeekTimeBar.this.mNextX, 0, (int) (((double) (-f)) / 1.5d), 0, -96000, 96000, 0, 0);
                SeekTimeBar.this.setNextMessage(0);
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                synchronized (SeekTimeBar.this) {
                    double d2 = SeekTimeBar.this.mNextX + ((double) f);
                    double width = (d2 - ((double) SeekTimeBar.this.PANDDING)) + ((double) (SeekTimeBar.this.getWidth() / 2));
                    if (width < 0.0d) {
                        width = 0.0d;
                    }
                    if (Integer.parseInt(SeekTimeBar.this.getTime(width).replace(":", "")) > 240000) {
                        return true;
                    }
                    SeekTimeBar.this.mNextX = d2;
                    SeekTimeBar.this.currentIndex = width;
                    if (SeekTimeBar.this.currentIndex < 0.0d) {
                        SeekTimeBar.this.currentIndex = 0.0d;
                    }
                    SeekTimeBar.this.hms = SeekTimeBar.this.getTime();
                    if (Math.abs(f) > 5.0f) {
                        SeekTimeBar.this.isSeeked = true;
                    }
                    SeekTimeBar.this.postInvalidate();
                    return true;
                }
            }
        };
        this.animationHandler = new Handler() { // from class: view.SeekTimeBar.3
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                SeekTimeBar.this.mScroller.computeScrollOffset();
                double currX = ((double) SeekTimeBar.this.mScroller.getCurrX()) - SeekTimeBar.this.mNextX;
                if (currX != 0.0d) {
                    SeekTimeBar.this.scrollView((float) currX);
                }
                if (!SeekTimeBar.this.mScroller.isFinished()) {
                    SeekTimeBar.this.animationHandler.sendEmptyMessage(message.what);
                } else {
                    SeekTimeBar seekTimeBar = SeekTimeBar.this;
                    seekTimeBar.seekToTime(seekTimeBar.currentIndex, SeekTimeBar.this.scaleLong);
                }
            }
        };
        this.flag = false;
        this.scaleAnimationHandler = new Handler() { // from class: view.SeekTimeBar.4
            @Override // android.os.Handler
            public void handleMessage(@NonNull Message message) {
                if (message.what == 0) {
                    SeekTimeBar.this.MARGIN -= 2;
                    if (SeekTimeBar.this.MARGIN <= 1) {
                        SeekTimeBar seekTimeBar = SeekTimeBar.this;
                        seekTimeBar.MARGIN = 1;
                        seekTimeBar.flag = true;
                    }
                    SeekTimeBar.this.setMargin();
                } else if (message.what == 1) {
                    if (SeekTimeBar.this.MARGIN <= 10) {
                        SeekTimeBar.this.MARGIN += 2;
                        if (SeekTimeBar.this.MARGIN >= 10) {
                            SeekTimeBar seekTimeBar2 = SeekTimeBar.this;
                            seekTimeBar2.MARGIN = 10;
                            seekTimeBar2.flag = true;
                        }
                    } else {
                        SeekTimeBar.this.MARGIN -= 2;
                        if (SeekTimeBar.this.MARGIN <= 10) {
                            SeekTimeBar seekTimeBar3 = SeekTimeBar.this;
                            seekTimeBar3.MARGIN = 10;
                            seekTimeBar3.flag = true;
                        }
                    }
                    SeekTimeBar.this.setMargin();
                } else if (message.what == 2) {
                    if (SeekTimeBar.this.MARGIN <= 20) {
                        SeekTimeBar.this.MARGIN += 2;
                        if (SeekTimeBar.this.MARGIN >= 20) {
                            SeekTimeBar seekTimeBar4 = SeekTimeBar.this;
                            seekTimeBar4.MARGIN = 20;
                            seekTimeBar4.flag = true;
                        }
                    } else {
                        SeekTimeBar.this.MARGIN -= 2;
                        if (SeekTimeBar.this.MARGIN <= 20) {
                            SeekTimeBar seekTimeBar5 = SeekTimeBar.this;
                            seekTimeBar5.MARGIN = 20;
                            seekTimeBar5.flag = true;
                        }
                    }
                    SeekTimeBar.this.setMargin();
                } else if (message.what == 3) {
                    SeekTimeBar.this.MARGIN += 2;
                    if (SeekTimeBar.this.MARGIN >= 40) {
                        SeekTimeBar seekTimeBar6 = SeekTimeBar.this;
                        seekTimeBar6.MARGIN = 40;
                        seekTimeBar6.flag = true;
                    }
                    SeekTimeBar.this.setMargin();
                }
                if (!SeekTimeBar.this.flag) {
                    SeekTimeBar.this.scaleAnimationHandler.sendEmptyMessage(message.what);
                } else {
                    SeekTimeBar.this.flag = false;
                }
            }
        };
        this.context = context;
        initView();
    }

    public boolean isSeeking() {
        return this.isSeeking;
    }

    public void setTime(int i) {
        if (this.isSeeking) {
            return;
        }
        if (this.isSeeked) {
            this.beforeTime = i;
            this.isSeeked = false;
            return;
        }
        int i2 = this.beforeTime;
        if (i2 == 0 || Math.abs(i - i2) >= 1) {
            this.beforeTime = 0;
            double d2 = i;
            double d3 = d2 % 3600.0d;
            this.hms = String.format("%02d", Integer.valueOf((int) (d2 / 3600.0d))) + ":" + String.format("%02d", Integer.valueOf((int) (d3 / 60.0d))) + ":" + String.format("%02d", Integer.valueOf((int) (d3 % 60.0d)));
            this.currentIndex = (this.scaleLong * d2) / 86400.0d;
            this.mNextX = (this.currentIndex + ((double) this.PANDDING)) - ((double) (getWidth() / 2));
            postInvalidate();
        }
    }

    public void setTimeToStart() {
        List<Integer> list = this.startDates;
        if (list == null || list.size() <= 0) {
            return;
        }
        setTime(this.startDates.get(0).intValue());
    }

    public void setTimeToEnd() {
        List<Integer> list = this.endDates;
        if (list == null || list.size() <= 0) {
            return;
        }
        setTime(this.endDates.get(r0.size() - 1).intValue());
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(View.MeasureSpec.getSize(i), this.txtDateHeight + 40 + this.longScaleHeight + this.txtPaintHeight);
    }

    public float getRawSize(float f) {
        return ScreenUtil.dp2Px(this.context, f);
    }

    private synchronized void initView() {
        this.mCurrentX = 0;
        this.mNextX = 0.0d;
        this.scalePaint = new Paint();
        this.scalePaint.setColor(-1);
        this.scalePaint.setAntiAlias(true);
        this.placeholderPaint = new Paint();
        this.placeholderPaint.setAlpha(0);
        this.placeholderPaint.setAntiAlias(true);
        this.paint = new Paint();
        this.paint.setColor(SupportMenu.CATEGORY_MASK);
        this.paint.setAntiAlias(true);
        this.txtPaint = new Paint(1);
        this.txtPaint.setTextSize(getRawSize(12.0f));
        this.txtPaint.setColor(-1);
        this.timePaint = new Paint(1);
        this.timePaint.setTextSize(getRawSize(12.0f));
        this.timePaint.setColor(-1);
        this.fileFildBluePaint = new Paint();
        this.fileFildBluePaint.setAntiAlias(true);
        this.fileFildBluePaint.setColor(getContext().getResources().getColor(R.color.color_2c99fd));
        this.fileFildRedPaint = new Paint();
        this.fileFildRedPaint.setAntiAlias(true);
        this.fileFildRedPaint.setColor(getContext().getResources().getColor(R.color.color_845255));
        Rect rectComputeTextScale = computeTextScale(this.txtPaint, "00:00");
        this.txtPaintWidth = rectComputeTextScale.width();
        this.txtPaintHeight = rectComputeTextScale.height();
        this.PANDDING = (this.txtPaintWidth / 2) + 5;
        Rect rectComputeTextScale2 = computeTextScale(this.timePaint, "2014-07-24");
        this.txtDateWidth = rectComputeTextScale2.width();
        this.txtDateHeight = rectComputeTextScale2.height();
        Rect rectComputeTextScale3 = computeTextScale(this.timePaint, "14:55:55");
        this.txtTimeWidth = rectComputeTextScale3.width();
        this.txtTimeHeight = rectComputeTextScale3.height();
        this.mScroller = new Scroller(getContext());
        this.mGesture = new GestureDetector(getContext(), this.mOnGesture);
    }

    public OnTimeListener getTimeListener() {
        return this.timeListener;
    }

    public void setTimeListener(OnTimeListener onTimeListener) {
        this.timeListener = onTimeListener;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public double getScaleLong() {
        return this.scaleLong;
    }

    public void setScaleLong(double d2) {
        this.scaleLong = d2;
    }

    @Override // android.view.View
    protected synchronized void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        int width = getWidth() / 2;
        this.scaleLong = computeScaleLong();
        double d2 = this.scaleLong;
        if (d2 != 0.0d) {
            double d3 = this.mNextX;
            if (d3 > (d2 - ((double) width)) + ((double) this.PANDDING) || d3 < (-(width - r5))) {
                this.mNextX = this.lastX;
            } else {
                this.lastX = d3;
            }
        }
        double d4 = this.mNextX;
        int i2 = this.PANDDING;
        this.currentIndex = (d4 - ((double) i2)) + ((double) width);
        int i3 = (int) ((-d4) + ((double) i2));
        List<Integer> list = this.startDates;
        if (list != null && list.size() > 0) {
            for (int i4 = 0; i4 < this.startDates.size(); i4++) {
                drawFildRect(canvas, this.startDates.get(i4).intValue(), this.endDates.get(i4).intValue(), this.colorPaints.get(i4));
            }
        }
        int i5 = i3;
        for (int i6 = 0; i6 < 1441; i6++) {
            int i7 = i6 % 60;
            if (i7 == 0) {
                int i8 = this.txtDateHeight;
                canvas.drawRect(i5, i8 + 20, this.longScaleWidth + i5, i8 + 20 + this.longScaleHeight, this.scalePaint);
                canvas.drawText(formatTime(i6 / 60), i5 - (this.txtPaintWidth / 2), this.txtDateHeight + 30 + this.longScaleHeight + this.txtPaintHeight, this.txtPaint);
                i = this.longScaleWidth;
            } else if (i6 % 10 == 0) {
                int i9 = (this.longScaleHeight - this.intScaleHeight) / 2;
                int i10 = this.txtDateHeight;
                canvas.drawRect(i5, i10 + 20 + i9, this.intScaleWidth + i5, i10 + 20 + i9 + r2, this.scalePaint);
                if (this.MARGIN >= 10) {
                    canvas.drawText((i6 / 60) + ":" + i7, i5 - (this.txtPaintWidth / 2), this.txtDateHeight + 30 + this.longScaleHeight + this.txtPaintHeight, this.txtPaint);
                }
                i = this.intScaleWidth;
            } else {
                int i11 = (this.longScaleHeight - this.shortScaleHeight) / 2;
                if (this.MARGIN == 1) {
                    int i12 = this.txtDateHeight;
                    canvas.drawRect(i5, i12 + 20 + i11, this.shortScaleWidth + i5, i12 + 20 + i11 + r2, this.placeholderPaint);
                } else {
                    int i13 = this.txtDateHeight;
                    canvas.drawRect(i5, i13 + 20 + i11, this.shortScaleWidth + i5, i13 + 20 + i11 + r2, this.scalePaint);
                }
                i = this.shortScaleWidth;
            }
            i5 = i5 + i + this.MARGIN;
        }
        canvas.drawRect(new Rect(width, 2, width + 4, getHeight() - 2), this.paint);
        canvas.drawText(this.hms, width - (this.txtTimeWidth / 2), this.txtDateHeight + 10, this.timePaint);
        super.onDraw(canvas);
    }

    public String formatTime(int i) {
        return i + ":00";
    }

    public double getProgress() {
        return this.currentIndex;
    }

    public void setTime(int i, int i2, int i3) {
        if (this.isSeeking) {
            return;
        }
        this.currentIndex = (this.scaleLong * ((double) (((i * 3600) + (i2 * 60)) + i3))) / 86400.0d;
        this.mNextX = (this.currentIndex + ((double) this.PANDDING)) - ((double) (getWidth() / 2));
        postInvalidate();
    }

    private float getFloatStringBy2(double d2) {
        String strValueOf = String.valueOf(d2);
        try {
            return Float.parseFloat(strValueOf.substring(0, strValueOf.indexOf(".") + 3));
        } catch (Exception unused) {
            return (float) d2;
        }
    }

    public void drawFildRect(Canvas canvas, int i, int i2, ColorPaint colorPaint) {
        double d2 = this.scaleLong;
        double d3 = (((double) i) * d2) / 86400.0d;
        int i3 = this.PANDDING;
        double d4 = this.mNextX;
        RectF rectF = new RectF(getFloatStringBy2((((double) i3) - d4) + d3), 2.0f, getFloatStringBy2((((double) i3) - d4) + ((d2 * ((double) i2)) / 86400.0d)), getHeight() - 2);
        if (ColorPaint.RED == colorPaint) {
            canvas.drawRect(rectF, this.fileFildRedPaint);
        } else if (ColorPaint.BLUE == colorPaint) {
            canvas.drawRect(rectF, this.fileFildBluePaint);
        }
    }

    public void setTimeDate(List<Integer> list, List<Integer> list2, List<ColorPaint> list3) {
        this.startDates = list;
        this.endDates = list2;
        this.colorPaints = list3;
        postInvalidate();
    }

    public void seekToTime(double d2, double d3) {
        SeekPosListener seekPosListener;
        if (this.startDates == null || this.endDates == null) {
            return;
        }
        if (d2 < 0.0d) {
            d2 = 0.0d;
        }
        double d4 = (d2 * 86400.0d) / d3;
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= this.startDates.size()) {
                break;
            }
            int i2 = i + 1;
            if (i2 < this.startDates.size() && this.endDates.get(i).intValue() < d4 && this.startDates.get(i2).intValue() > d4) {
                SeekPosListener seekPosListener2 = this.onSeekPosListener;
                if (seekPosListener2 != null) {
                    seekPosListener2.seekPos(this.startDates.get(i2).intValue(), i2);
                    return;
                }
                return;
            }
            if (i == 0 && d4 < this.startDates.get(i).intValue()) {
                SeekPosListener seekPosListener3 = this.onSeekPosListener;
                if (seekPosListener3 != null) {
                    seekPosListener3.seekPos(this.startDates.get(i).intValue(), i);
                }
            } else {
                if (i == this.startDates.size() - 1 && d4 > this.endDates.get(i).intValue()) {
                    z = true;
                    break;
                }
                if (d4 >= this.startDates.get(i).intValue() && d4 <= this.endDates.get(i).intValue()) {
                    SeekPosListener seekPosListener4 = this.onSeekPosListener;
                    if (seekPosListener4 != null) {
                        seekPosListener4.seekPos((int) d4, i);
                        return;
                    }
                    return;
                }
                i = i2;
            }
        }
        if (z && (seekPosListener = this.onSeekPosListener) != null) {
            seekPosListener.seekError();
        }
    }

    public void setCorrectTime(int i) {
        int i2 = i % 3600;
        this.hms = String.format("%02d", Integer.valueOf(i / 3600)) + ":" + String.format("%02d", Integer.valueOf(i2 / 60)) + ":" + String.format("%02d", Integer.valueOf(i2 % 60));
        StringBuilder sb = new StringBuilder();
        sb.append("setCorrectTime: ");
        sb.append(this.hms);
        Log.e("seekTimebar", sb.toString());
        this.currentIndex = (this.scaleLong * ((double) i)) / 86400.0d;
        this.mNextX = (this.currentIndex + ((double) this.PANDDING)) - ((double) (getWidth() / 2));
        postInvalidate();
    }

    public int getCorrectTime(int i) {
        if (this.startDates == null || this.endDates == null) {
            return -1;
        }
        int i2 = 0;
        while (i2 < this.startDates.size()) {
            int i3 = i2 + 1;
            if (i3 < this.startDates.size() && this.endDates.get(i2).intValue() < i && this.startDates.get(i3).intValue() > i) {
                return this.startDates.get(i3).intValue();
            }
            if (i2 == 0 && i < this.startDates.get(i2).intValue()) {
                return this.startDates.get(i2).intValue();
            }
            if (i2 == this.startDates.size() - 1 && i > this.endDates.get(i2).intValue()) {
                return i;
            }
            if (i >= this.startDates.get(i2).intValue() && i <= this.endDates.get(i2).intValue()) {
                return i;
            }
            i2 = i3;
        }
        return i;
    }

    public void setOnSeekPosListener(SeekPosListener seekPosListener) {
        this.onSeekPosListener = seekPosListener;
    }

    @Override // android.view.View
    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
    }

    public String getTime() {
        String strValueOf;
        String strValueOf2;
        String strValueOf3;
        double d2 = (this.currentIndex * 86400.0d) / this.scaleLong;
        int i = (int) (d2 / 3600.0d);
        double d3 = d2 % 3600.0d;
        int i2 = (int) (d3 / 60.0d);
        int i3 = (int) (d3 % 60.0d);
        OnTimeListener onTimeListener = this.timeListener;
        if (onTimeListener != null) {
            onTimeListener.setTime(i, i2, i3);
            this.timeListener.setTime((int) d2);
        }
        StringBuilder sb = new StringBuilder();
        if (i > 9) {
            strValueOf = String.valueOf(i);
        } else {
            strValueOf = "0" + i;
        }
        sb.append(strValueOf);
        sb.append(":");
        if (i2 > 9) {
            strValueOf2 = String.valueOf(i2);
        } else {
            strValueOf2 = "0" + i2;
        }
        sb.append(strValueOf2);
        sb.append(":");
        if (i3 > 9) {
            strValueOf3 = String.valueOf(i3);
        } else {
            strValueOf3 = "0" + i3;
        }
        sb.append(strValueOf3);
        return sb.toString();
    }

    public double getCurTime() {
        return (this.currentIndex * 86400.0d) / this.scaleLong;
    }

    public String getTime(double d2) {
        String strValueOf;
        String strValueOf2;
        String strValueOf3;
        double d3 = (d2 * 86400.0d) / this.scaleLong;
        int i = (int) (d3 / 3600.0d);
        double d4 = d3 % 3600.0d;
        int i2 = (int) (d4 / 60.0d);
        int i3 = (int) (d4 % 60.0d);
        OnTimeListener onTimeListener = this.timeListener;
        if (onTimeListener != null) {
            onTimeListener.setTime(i, i2, i3);
            this.timeListener.setTime((int) d3);
        }
        StringBuilder sb = new StringBuilder();
        if (i > 9) {
            strValueOf = String.valueOf(i);
        } else {
            strValueOf = "0" + i;
        }
        sb.append(strValueOf);
        sb.append(":");
        if (i2 > 9) {
            strValueOf2 = String.valueOf(i2);
        } else {
            strValueOf2 = "0" + i2;
        }
        sb.append(strValueOf2);
        sb.append(":");
        if (i3 > 9) {
            strValueOf3 = String.valueOf(i3);
        } else {
            strValueOf3 = "0" + i3;
        }
        sb.append(strValueOf3);
        return sb.toString();
    }

    public double computeScaleLong() {
        int i;
        double d2 = 0.0d;
        for (int i2 = 0; i2 < 1441; i2++) {
            if (i2 % 60 == 0) {
                i = this.longScaleWidth;
            } else {
                i = this.shortScaleWidth;
            }
            d2 = d2 + ((double) i) + ((double) this.MARGIN);
        }
        return (d2 - ((double) this.MARGIN)) - ((double) this.longScaleWidth);
    }

    public Rect computeTextScale(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect;
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean zDispatchTouchEvent = super.dispatchTouchEvent(motionEvent) | this.mGesture.onTouchEvent(motionEvent);
        if (motionEvent.getAction() == 2) {
            this.isSeeking = true;
        } else if (motionEvent.getAction() == 1) {
            this.isSeeking = false;
            if (this.isSeeked) {
                seekToTime(this.currentIndex, this.scaleLong);
            }
        }
        return zDispatchTouchEvent;
    }

    protected boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        synchronized (this) {
            LogEx.d(true, "onFling", "onFling:" + f);
        }
        postInvalidate();
        return false;
    }

    public boolean isDoubleClick(MotionEvent motionEvent) {
        if (Math.abs(motionEvent.getX() - this.lastDownX) > 100.0f || Math.abs(motionEvent.getY() - this.lastDownY) > 100.0f) {
            return false;
        }
        int i = this.count;
        if (i == 0) {
            this.count = i + 1;
            this.fTime = System.currentTimeMillis();
            new Handler().postDelayed(new Runnable() { // from class: view.SeekTimeBar.1
                @Override // java.lang.Runnable
                public void run() {
                    SeekTimeBar.this.count = 0;
                }
            }, 250L);
        } else if (i == 1) {
            if (System.currentTimeMillis() - this.fTime < 250) {
                return true;
            }
            this.count = 0;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setNextMessage(int i) {
        this.animationHandler.removeMessages(0);
        this.animationHandler.sendEmptyMessage(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scrollView(float f) {
        setCurrLocation(this.mNextX + ((double) f));
    }

    private void setCurrLocation(double d2) {
        this.mNextX = d2;
        this.hms = getTime();
        invalidate();
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x003d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void setMargin() {
        /*
            r8 = this;
            double r0 = r8.computeScaleLong()
            r8.scaleLong = r0
            java.lang.String r0 = r8.hms
            java.lang.String r1 = ":"
            boolean r0 = r0.contains(r1)
            r1 = 2
            if (r0 == 0) goto L3d
            java.lang.String r0 = r8.hms
            java.lang.String r2 = ":"
            java.lang.String[] r0 = r0.split(r2)
            int r2 = r0.length
            r3 = 3
            if (r2 != r3) goto L3d
            r2 = 0
            r2 = r0[r2]
            double r2 = java.lang.Double.parseDouble(r2)
            r4 = 4660134898793709568(0x40ac200000000000, double:3600.0)
            double r2 = r2 * r4
            r4 = 1
            r4 = r0[r4]
            double r4 = java.lang.Double.parseDouble(r4)
            r6 = 4633641066610819072(0x404e000000000000, double:60.0)
            double r4 = r4 * r6
            double r2 = r2 + r4
            r0 = r0[r1]
            double r4 = java.lang.Double.parseDouble(r0)
            double r2 = r2 + r4
            goto L3f
        L3d:
            r2 = 0
        L3f:
            double r4 = r8.scaleLong
            double r4 = r4 * r2
            r2 = 4680673776000565248(0x40f5180000000000, double:86400.0)
            double r4 = r4 / r2
            r8.currentIndex = r4
            double r2 = r8.currentIndex
            int r0 = r8.PANDDING
            double r4 = (double) r0
            double r2 = r2 + r4
            int r0 = r8.getWidth()
            int r0 = r0 / r1
            double r0 = (double) r0
            double r2 = r2 - r0
            r8.mNextX = r2
            r8.postInvalidate()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: view.SeekTimeBar.setMargin():void");
    }
}
