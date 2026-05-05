package com.aliyun.iot.aep.sdk.page;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.alibaba.sdk.android.openaccount.ui.widget.SiderBar;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: loaded from: classes2.dex */
public class CountrySiderBar extends SiderBar {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private float f4833a;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f4834c;

    private static float a(float f) {
        if (f <= 1.0f) {
            return 1.0f;
        }
        if (f <= 1.5d) {
            return 1.5f;
        }
        if (f <= 2.0f) {
            return 2.0f;
        }
        if (f <= 3.0f) {
            return 3.0f;
        }
        return f;
    }

    public CountrySiderBar(Context context) {
        this(context, null);
    }

    public CountrySiderBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CountrySiderBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f4834c = R.color.black;
        f2928b = new String[]{MqttTopic.MULTI_LEVEL_WILDCARD, "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    }

    public void setTextColor(int i) {
        this.f4834c = i;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        this.paint.setAntiAlias(true);
        setTextSize(12.0f);
        int size = View.MeasureSpec.getSize(i2);
        if (View.MeasureSpec.getMode(i2) != 1073741824) {
            size = (int) (((this.paint.descent() - this.paint.ascent()) + this.f4833a) * f2928b.length);
        }
        setMeasuredDimension(getDefaultSize(0, i), size);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.SiderBar
    protected void setTextSize(float f) {
        super.setTextSize(TypedValue.applyDimension(2, f, getResources().getDisplayMetrics()));
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.SiderBar, android.view.View
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        float viewHeight = (getViewHeight() * 1.0f) / f2928b.length;
        for (int i = 0; i < f2928b.length; i++) {
            float f = (i * viewHeight) + viewHeight;
            if (f > height) {
                return;
            }
            float f2 = width / 2;
            this.paint.setColor(getResources().getColor(this.f4834c));
            this.paint.setTypeface(Typeface.DEFAULT_BOLD);
            this.paint.setTextSize(a(getContext(), 11.0f));
            if (i == this.choose) {
                setColor(R.color.white);
                setFakeBoldText(true);
            }
            this.paint.setTypeface(Typeface.DEFAULT_BOLD);
            this.paint.setAntiAlias(true);
            this.paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(f2928b[i], f2, f, this.paint);
            this.paint.reset();
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.SiderBar
    protected int getViewHeight() {
        if (this.viewHeight == 0) {
            this.viewHeight = getHeight();
        }
        return this.viewHeight;
    }

    public void setInterval(float f) {
        this.f4833a = TypedValue.applyDimension(1, f, getResources().getDisplayMetrics());
        requestLayout();
    }

    private static int a(Context context, float f) {
        return (int) ((f * a(context)) + 0.5f);
    }

    private static float a(Context context) {
        return a(context.getResources().getDisplayMetrics().scaledDensity);
    }
}
