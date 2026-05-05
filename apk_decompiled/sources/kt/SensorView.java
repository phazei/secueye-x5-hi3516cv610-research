package kt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.seculink.app.R;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: SensorView.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0015J\u0010\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0017H\u0007J\u000e\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\bJ\u0010\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0017H\u0007J\u000e\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\bR\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, d2 = {"Lkt/SensorView;", "Landroidx/constraintlayout/widget/ConstraintLayout;", "context", "Landroid/content/Context;", "attributeSet", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "displayPower", "", "displayTemperature", "paint", "Landroid/graphics/Paint;", "getPaint", "()Landroid/graphics/Paint;", "percentage", "", "powerPaint", "onDraw", "", "canvas", "Landroid/graphics/Canvas;", "setPower", "string", "", "setPowerDisplay", "b", "setTemperature", "setTemperatureDisplay", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
public final class SensorView extends ConstraintLayout {
    private HashMap _$_findViewCache;
    private boolean displayPower;
    private boolean displayTemperature;

    @NotNull
    private final Paint paint;
    private float percentage;
    private final Paint powerPaint;

    public void _$_clearFindViewByIdCache() {
        HashMap map = this._$_findViewCache;
        if (map != null) {
            map.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view2 = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view2 != null) {
            return view2;
        }
        View viewFindViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), viewFindViewById);
        return viewFindViewById;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SensorView(@NotNull Context context, @NotNull AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(attributeSet, "attributeSet");
        ConstraintLayout.inflate(context, R.layout.sensor_view_layout, this);
        setWillNotDraw(false);
        this.paint = new Paint(1);
        Paint paint = new Paint(1);
        paint.setColor(ContextCompat.getColor(context, R.color.power_green));
        this.powerPaint = paint;
        this.percentage = 1.0f;
        this.displayPower = true;
        this.displayTemperature = true;
    }

    @NotNull
    public final Paint getPaint() {
        return this.paint;
    }

    @Override // android.view.View
    @RequiresApi(21)
    protected void onDraw(@NotNull Canvas canvas) {
        Intrinsics.checkParameterIsNotNull(canvas, "canvas");
        super.onDraw(canvas);
        this.paint.setColor(ContextCompat.getColor(getContext(), R.color.power_bg));
        if (this.displayTemperature || this.displayPower) {
            canvas.drawRoundRect(0.0f, 0.0f, getWidth(), getHeight(), 10.0f, 10.0f, this.paint);
        }
        ImageView battery_icon = (ImageView) _$_findCachedViewById(R.id.battery_icon);
        Intrinsics.checkExpressionValueIsNotNull(battery_icon, "battery_icon");
        float right = battery_icon.getRight();
        ImageView battery_icon2 = (ImageView) _$_findCachedViewById(R.id.battery_icon);
        Intrinsics.checkExpressionValueIsNotNull(battery_icon2, "battery_icon");
        float left = (right - battery_icon2.getLeft()) - 3;
        float f = 1;
        float f2 = left * (f - this.percentage);
        if (this.displayPower) {
            ImageView battery_icon3 = (ImageView) _$_findCachedViewById(R.id.battery_icon);
            Intrinsics.checkExpressionValueIsNotNull(battery_icon3, "battery_icon");
            float f3 = 2;
            float left2 = battery_icon3.getLeft() + f3;
            ImageView battery_icon4 = (ImageView) _$_findCachedViewById(R.id.battery_icon);
            Intrinsics.checkExpressionValueIsNotNull(battery_icon4, "battery_icon");
            float top = battery_icon4.getTop() + f3;
            ImageView battery_icon5 = (ImageView) _$_findCachedViewById(R.id.battery_icon);
            Intrinsics.checkExpressionValueIsNotNull(battery_icon5, "battery_icon");
            float right2 = (battery_icon5.getRight() - 5) - f2;
            ImageView battery_icon6 = (ImageView) _$_findCachedViewById(R.id.battery_icon);
            Intrinsics.checkExpressionValueIsNotNull(battery_icon6, "battery_icon");
            canvas.drawRect(left2, top, right2, battery_icon6.getBottom() - f, this.powerPaint);
        }
    }

    @SuppressLint({"SetTextI18n"})
    public final void setPower(int string) {
        StringBuilder sb = new StringBuilder();
        sb.append("setPower: ");
        ImageView battery_icon = (ImageView) _$_findCachedViewById(R.id.battery_icon);
        Intrinsics.checkExpressionValueIsNotNull(battery_icon, "battery_icon");
        sb.append(battery_icon.getRight());
        sb.append("  ");
        ImageView battery_icon2 = (ImageView) _$_findCachedViewById(R.id.battery_icon);
        Intrinsics.checkExpressionValueIsNotNull(battery_icon2, "battery_icon");
        sb.append(battery_icon2.getLeft());
        Log.d("TAG", sb.toString());
        if (string >= 0 && 25 >= string) {
            this.powerPaint.setColor(ContextCompat.getColor(getContext(), R.color.ali_sdk_openaccount_red));
        } else if (26 <= string && 50 >= string) {
            this.powerPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_f0a32a));
        } else if (51 <= string && 100 >= string) {
            this.powerPaint.setColor(ContextCompat.getColor(getContext(), R.color.power_green));
        }
        this.percentage = string / 100.0f;
        TextView battery_text = (TextView) _$_findCachedViewById(R.id.battery_text);
        Intrinsics.checkExpressionValueIsNotNull(battery_text, "battery_text");
        StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append('%');
        battery_text.setText(sb2.toString());
    }

    @SuppressLint({"SetTextI18n"})
    public final void setTemperature(int string) {
        TextView temperature_text = (TextView) _$_findCachedViewById(R.id.temperature_text);
        Intrinsics.checkExpressionValueIsNotNull(temperature_text, "temperature_text");
        StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append((char) 8451);
        temperature_text.setText(sb.toString());
    }

    public final void setPowerDisplay(boolean b2) {
        this.displayPower = b2;
        if (b2) {
            ImageView battery_icon = (ImageView) _$_findCachedViewById(R.id.battery_icon);
            Intrinsics.checkExpressionValueIsNotNull(battery_icon, "battery_icon");
            battery_icon.setVisibility(0);
            TextView battery_text = (TextView) _$_findCachedViewById(R.id.battery_text);
            Intrinsics.checkExpressionValueIsNotNull(battery_text, "battery_text");
            battery_text.setVisibility(0);
            return;
        }
        ImageView battery_icon2 = (ImageView) _$_findCachedViewById(R.id.battery_icon);
        Intrinsics.checkExpressionValueIsNotNull(battery_icon2, "battery_icon");
        battery_icon2.setVisibility(8);
        TextView battery_text2 = (TextView) _$_findCachedViewById(R.id.battery_text);
        Intrinsics.checkExpressionValueIsNotNull(battery_text2, "battery_text");
        battery_text2.setVisibility(8);
    }

    public final void setTemperatureDisplay(boolean b2) {
        this.displayTemperature = b2;
        if (b2) {
            ImageView temperature_icon = (ImageView) _$_findCachedViewById(R.id.temperature_icon);
            Intrinsics.checkExpressionValueIsNotNull(temperature_icon, "temperature_icon");
            temperature_icon.setVisibility(0);
            TextView temperature_text = (TextView) _$_findCachedViewById(R.id.temperature_text);
            Intrinsics.checkExpressionValueIsNotNull(temperature_text, "temperature_text");
            temperature_text.setVisibility(0);
            return;
        }
        ImageView temperature_icon2 = (ImageView) _$_findCachedViewById(R.id.temperature_icon);
        Intrinsics.checkExpressionValueIsNotNull(temperature_icon2, "temperature_icon");
        temperature_icon2.setVisibility(8);
        TextView temperature_text2 = (TextView) _$_findCachedViewById(R.id.temperature_text);
        Intrinsics.checkExpressionValueIsNotNull(temperature_text2, "temperature_text");
        temperature_text2.setVisibility(8);
    }
}
