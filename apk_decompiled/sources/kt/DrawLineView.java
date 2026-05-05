package kt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;
import bean.AreaPointBean;
import com.aliyun.alink.linksdk.alcs.coap.resources.LinkFormat;
import com.seculink.app.R;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: DrawLineView.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0014J(\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u000f\u001a\u00020\u00152\u0006\u0010\u0007\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0015H\u0014J\u001e\u0010\u0018\u001a\u00020\u00112\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00152\u0006\u0010\u0007\u001a\u00020\u0015R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lkt/DrawLineView;", "Landroid/view/View;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", LinkFormat.HOST, "", "hasDate", "", "paint", "Landroid/graphics/Paint;", "pointBean", "Lbean/AreaPointBean;", "w", "onDraw", "", "canvas", "Landroid/graphics/Canvas;", "onSizeChanged", "", "oldw", "oldh", "setPointList", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
public final class DrawLineView extends View {
    private HashMap _$_findViewCache;
    private float h;
    private boolean hasDate;
    private final Paint paint;
    private AreaPointBean pointBean;
    private float w;

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
    public DrawLineView(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.w = 1.6f;
        this.h = 1.6f;
        Paint paint = new Paint(1);
        paint.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        paint.setStrokeWidth(UtilsKt.getDp(2));
        this.paint = paint;
        setWillNotDraw(false);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = getWidth() / 640.0f;
        this.h = getHeight() / 360.0f;
    }

    @Override // android.view.View
    protected void onDraw(@NotNull Canvas canvas) {
        Intrinsics.checkParameterIsNotNull(canvas, "canvas");
        super.onDraw(canvas);
        if (this.hasDate) {
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            float fIntValue = r0.getLeftTop_X().intValue() * this.w;
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            float fIntValue2 = r0.getLeftTop_Y().intValue() * this.h;
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            float fIntValue3 = r0.getRightTop_X().intValue() * this.w;
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            canvas.drawLine(fIntValue, fIntValue2, fIntValue3, r0.getRightTop_Y().intValue() * this.h, this.paint);
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            float fIntValue4 = r0.getRightTop_X().intValue() * this.w;
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            float fIntValue5 = r0.getRightTop_Y().intValue() * this.h;
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            float fIntValue6 = r0.getRightBottom_X().intValue() * this.w;
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            canvas.drawLine(fIntValue4, fIntValue5, fIntValue6, r0.getRightBottom_Y().intValue() * this.h, this.paint);
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            float fIntValue7 = r0.getRightBottom_X().intValue() * this.w;
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            float fIntValue8 = r0.getRightBottom_Y().intValue() * this.h;
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            float fIntValue9 = r0.getLeftBottom_X().intValue() * this.w;
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            canvas.drawLine(fIntValue7, fIntValue8, fIntValue9, r0.getLeftBottom_Y().intValue() * this.h, this.paint);
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            float fIntValue10 = r0.getLeftBottom_X().intValue() * this.w;
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            float fIntValue11 = r0.getLeftBottom_Y().intValue() * this.h;
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            float fIntValue12 = r0.getLeftTop_X().intValue() * this.w;
            if (this.pointBean == null) {
                Intrinsics.throwUninitializedPropertyAccessException("pointBean");
            }
            canvas.drawLine(fIntValue10, fIntValue11, fIntValue12, r0.getLeftTop_Y().intValue() * this.h, this.paint);
        }
    }

    public final void setPointList(@NotNull AreaPointBean pointBean, int w, int h) {
        Intrinsics.checkParameterIsNotNull(pointBean, "pointBean");
        this.pointBean = pointBean;
        this.w = w / 640.0f;
        this.h = h / 360.0f;
        this.hasDate = true;
        invalidate();
    }
}
