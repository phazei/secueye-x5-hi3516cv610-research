package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.internal.view.SupportMenu;
import com.seculink.app.R;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: loaded from: classes5.dex */
public class CircleView extends View {
    private int color;
    private Paint mPaint;
    private float mRadius;

    public CircleView(Context context) {
        super(context, null);
        this.mRadius = 5.0f;
    }

    public CircleView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRadius = 5.0f;
        initView(context, attributeSet);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.circle_view_style);
        this.mRadius = typedArrayObtainStyledAttributes.getDimension(1, 0.0f);
        this.color = typedArrayObtainStyledAttributes.getColor(0, SupportMenu.CATEGORY_MASK);
        this.mPaint = new Paint();
        this.mPaint.setColor(this.color);
        this.mPaint.setAntiAlias(true);
    }

    public void setColor(int i) {
        this.mPaint.setColor(i);
        invalidate();
    }

    public void setColor(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mPaint.setColor(Color.parseColor(MqttTopic.MULTI_LEVEL_WILDCARD + str));
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float f = this.mRadius;
        canvas.drawCircle(f, f, f, this.mPaint);
    }
}
