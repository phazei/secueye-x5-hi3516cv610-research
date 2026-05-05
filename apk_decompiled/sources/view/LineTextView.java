package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class LineTextView extends TextView {
    private Paint paint;
    private TextPaint tp;

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

    public static int sp2px(Context context, float f) {
        return (int) ((f * getScale(context)) + 0.5f);
    }

    private static float getScale(Context context) {
        return findScale(context.getResources().getDisplayMetrics().scaledDensity);
    }

    public LineTextView(Context context) {
        super(context);
        this.tp = new TextPaint();
        this.paint = new Paint();
    }

    public LineTextView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.tp = new TextPaint();
        this.paint = new Paint();
    }

    public LineTextView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.tp = new TextPaint();
        this.paint = new Paint();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.tp.setColor(getTextColors().getDefaultColor());
        this.tp.setStyle(Paint.Style.FILL);
        this.tp.setTextSize(sp2px(getContext(), 16.0f));
        String string = getResources().getString(R.string.location_failed);
        String str = getResources().getString(R.string.location_failed) + getResources().getString(R.string.location_failed_again);
        StaticLayout staticLayout = new StaticLayout(string, this.tp, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        StaticLayout staticLayout2 = new StaticLayout(str, this.tp, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        int lineCount = staticLayout.getLineCount();
        int lineCount2 = getLineCount();
        this.paint.setColor(getTextColors().getDefaultColor());
        this.paint.setStrokeWidth(3.0f);
        int i = lineCount - 1;
        for (int i2 = i; i2 < lineCount2; i2++) {
            float lineWidth = 0.0f;
            if (i2 == i) {
                lineWidth = staticLayout.getLineWidth(i);
            }
            canvas.drawLine(lineWidth, getLineHeight() * r12, staticLayout2.getLineWidth(i2), getLineHeight() * r12, this.paint);
        }
    }
}
