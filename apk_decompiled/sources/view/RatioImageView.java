package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class RatioImageView extends AppCompatImageView {
    private float mRatio;

    public RatioImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRatio = 0.0f;
    }

    public RatioImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRatio = 0.0f;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RatioImageView);
        this.mRatio = typedArrayObtainStyledAttributes.getFloat(0, 0.0f);
        typedArrayObtainStyledAttributes.recycle();
    }

    public RatioImageView(Context context) {
        super(context);
        this.mRatio = 0.0f;
    }

    public void setRatio(float f) {
        this.mRatio = f;
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        float f = this.mRatio;
        if (f != 0.0f) {
            i2 = View.MeasureSpec.makeMeasureSpec((int) (size / f), 1073741824);
        }
        super.onMeasure(i, i2);
    }
}
