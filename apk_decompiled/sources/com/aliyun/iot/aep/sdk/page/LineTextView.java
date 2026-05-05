package com.aliyun.iot.aep.sdk.page;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import com.aliyun.iot.aep.sdk.framework.R;

/* JADX INFO: loaded from: classes2.dex */
public class LineTextView extends AppCompatTextView {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private TextPaint f4837a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Paint f4838b;

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

    public static int sp2px(Context context, float f) {
        return (int) ((f * a(context)) + 0.5f);
    }

    private static float a(Context context) {
        return a(context.getResources().getDisplayMetrics().scaledDensity);
    }

    public LineTextView(Context context) {
        super(context);
        this.f4837a = new TextPaint();
        this.f4838b = new Paint();
    }

    public LineTextView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f4837a = new TextPaint();
        this.f4838b = new Paint();
    }

    public LineTextView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f4837a = new TextPaint();
        this.f4838b = new Paint();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.f4837a.setColor(getTextColors().getDefaultColor());
        this.f4837a.setStyle(Paint.Style.FILL);
        this.f4837a.setTextSize(sp2px(getContext(), 16.0f));
        String string = getResources().getString(R.string.location_failed);
        String str = getResources().getString(R.string.location_failed) + getResources().getString(R.string.location_failed_again);
        StaticLayout staticLayout = new StaticLayout(string, this.f4837a, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        StaticLayout staticLayout2 = new StaticLayout(str, this.f4837a, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        int lineCount = staticLayout.getLineCount();
        int lineCount2 = getLineCount();
        this.f4838b.setColor(getTextColors().getDefaultColor());
        this.f4838b.setStrokeWidth(3.0f);
        int i = lineCount - 1;
        for (int i2 = i; i2 < lineCount2; i2++) {
            float lineWidth = 0.0f;
            if (i2 == i) {
                lineWidth = staticLayout.getLineWidth(i);
            }
            canvas.drawLine(lineWidth, getLineHeight() * r12, staticLayout2.getLineWidth(i2), getLineHeight() * r12, this.f4838b);
        }
    }
}
