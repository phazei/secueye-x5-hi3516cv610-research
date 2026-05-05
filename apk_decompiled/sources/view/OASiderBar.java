package view;

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

/* JADX INFO: loaded from: classes5.dex */
public class OASiderBar extends SiderBar {
    private float mInterval;
    private int mTextColor;

    private static float findScale(float f) {
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

    public OASiderBar(Context context) {
        this(context, null);
    }

    public OASiderBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public OASiderBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTextColor = R.color.black;
        f2928b = new String[]{MqttTopic.MULTI_LEVEL_WILDCARD, "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    }

    public void setTextColor(int i) {
        this.mTextColor = i;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        this.paint.setAntiAlias(true);
        setTextSize(12.0f);
        int size = View.MeasureSpec.getSize(i2);
        if (View.MeasureSpec.getMode(i2) != 1073741824) {
            size = (int) (((this.paint.descent() - this.paint.ascent()) + this.mInterval) * f2928b.length);
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
            this.paint.setColor(getResources().getColor(this.mTextColor));
            this.paint.setTypeface(Typeface.DEFAULT_BOLD);
            this.paint.setTextSize(dip2px(getContext(), 11.0f));
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
        this.mInterval = TypedValue.applyDimension(1, f, getResources().getDisplayMetrics());
        requestLayout();
    }

    private static int dip2px(Context context, float f) {
        return (int) ((f * getScale(context)) + 0.5f);
    }

    private static float getScale(Context context) {
        return findScale(context.getResources().getDisplayMetrics().scaledDensity);
    }
}
