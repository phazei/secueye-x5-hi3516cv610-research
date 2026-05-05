package com.alibaba.sdk.android.openaccount.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: loaded from: classes.dex */
public class SiderBar extends View {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static String[] f2928b = {WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD, "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", MqttTopic.MULTI_LEVEL_WILDCARD};
    protected int choose;
    protected Context mContext;
    protected TextView mTextDialog;
    protected OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    protected Paint paint;
    protected int viewHeight;

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String str);
    }

    public void setTextView(TextView textView) {
        this.mTextDialog = textView;
    }

    public SiderBar(Context context) {
        super(context);
        this.choose = -1;
        this.paint = new Paint();
        this.mContext = context;
    }

    public SiderBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.choose = -1;
        this.paint = new Paint();
        this.mContext = context;
    }

    public SiderBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.choose = -1;
        this.paint = new Paint();
        this.mContext = context;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        float viewHeight = (getViewHeight() * 1.0f) / f2928b.length;
        int i = 0;
        while (true) {
            String[] strArr = f2928b;
            if (i >= strArr.length) {
                return;
            }
            float f = (i * viewHeight) + viewHeight;
            if (f > height) {
                return;
            }
            float fMeasureText = (width / 2) - (this.paint.measureText(strArr[i]) / 2.0f);
            setColor(Color.rgb(23, 122, 126));
            setTypeface(Typeface.DEFAULT_BOLD);
            setTextSize(20.0f);
            if (i == this.choose) {
                setColor(-16776961);
                setFakeBoldText(true);
            }
            canvas.drawText(f2928b[i], fMeasureText, f, this.paint);
            this.paint.reset();
            i++;
        }
    }

    protected void setTextSize(float f) {
        this.paint.setTextSize(f);
    }

    protected void setFakeBoldText(boolean z) {
        this.paint.setFakeBoldText(z);
    }

    protected void setColor(int i) {
        this.paint.setColor(i);
    }

    protected void setTypeface(Typeface typeface) {
        this.paint.setTypeface(typeface);
    }

    protected int getViewHeight() {
        if (this.viewHeight == 0) {
            this.viewHeight = getHeight();
        }
        return this.viewHeight;
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float y = motionEvent.getY();
        int i = this.choose;
        OnTouchingLetterChangedListener onTouchingLetterChangedListener = this.onTouchingLetterChangedListener;
        String[] strArr = f2928b;
        int viewHeight = (int) ((y / getViewHeight()) * strArr.length);
        if (action == 1) {
            this.choose = -1;
            invalidate();
            TextView textView = this.mTextDialog;
            if (textView != null) {
                textView.setVisibility(4);
            }
        } else if (i != viewHeight && viewHeight >= 0 && viewHeight < strArr.length) {
            if (onTouchingLetterChangedListener != null) {
                onTouchingLetterChangedListener.onTouchingLetterChanged(strArr[viewHeight]);
            }
            TextView textView2 = this.mTextDialog;
            if (textView2 != null) {
                textView2.setText(f2928b[viewHeight]);
                this.mTextDialog.setVisibility(0);
            }
            this.choose = viewHeight;
            invalidate();
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }
}
