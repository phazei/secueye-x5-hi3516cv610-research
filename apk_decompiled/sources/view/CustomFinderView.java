package view;

import aisble.callback.FailCallback;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import com.journeyapps.barcodescanner.ViewfinderView;

/* JADX INFO: loaded from: classes5.dex */
public class CustomFinderView extends ViewfinderView {
    public static final long CUSTOME_ANIMATION_DELAY = 10;
    private boolean firstInit;
    public int mLineColor;
    public float mLineDepth;
    public float mLineRate;
    public LinearGradient mLinearGradient;
    public float[] mPositions;
    public int[] mScanLineColor;
    public float mScanLineDepth;
    public float mScanLineDy;
    public int mScanLinePosition;

    public CustomFinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLineRate = 0.1f;
        this.mLineDepth = TypedValue.applyDimension(1, 4.0f, getResources().getDisplayMetrics());
        this.mLineColor = -15032065;
        this.mScanLinePosition = 0;
        this.mScanLineDepth = TypedValue.applyDimension(1, 4.0f, getResources().getDisplayMetrics());
        this.mScanLineDy = TypedValue.applyDimension(1, 3.0f, getResources().getDisplayMetrics());
        this.mPositions = new float[]{0.0f, 0.5f, 1.0f};
        this.mScanLineColor = new int[]{16777215, -15032065, 16777215};
        this.firstInit = true;
    }

    @Override // com.journeyapps.barcodescanner.ViewfinderView, android.view.View
    public void onDraw(Canvas canvas) {
        refreshSizes();
        if (this.framingRect == null || this.previewFramingRect == null) {
            return;
        }
        Rect rect = this.framingRect;
        Rect rect2 = this.previewFramingRect;
        if (this.firstInit) {
            this.mScanLinePosition = (rect.height() / 2) + FailCallback.REASON_AUTH_FAILED;
            this.firstInit = false;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        this.paint.setColor(this.mLineColor);
        this.paint.setColor(this.resultBitmap != null ? this.resultColor : this.maskColor);
        float f = width;
        canvas.drawRect(0.0f, 0.0f, f, rect.top, this.paint);
        canvas.drawRect(0.0f, rect.top, rect.left, rect.bottom + 1, this.paint);
        canvas.drawRect(rect.right + 1, rect.top, f, rect.bottom + 1, this.paint);
        canvas.drawRect(0.0f, rect.bottom + 1, f, height, this.paint);
        if (this.resultBitmap != null) {
            this.paint.setAlpha(160);
            canvas.drawBitmap(this.resultBitmap, (Rect) null, rect, this.paint);
        } else {
            this.mScanLinePosition = (int) (this.mScanLinePosition + this.mScanLineDy);
            if (this.mScanLinePosition > (rect.height() / 2) + 500) {
                this.mScanLinePosition = (rect.height() / 2) + FailCallback.REASON_AUTH_FAILED;
            }
            if (this.mScanLinePosition < 0) {
                this.mScanLinePosition = 0;
            }
            this.mLinearGradient = new LinearGradient(rect.left, rect.top + this.mScanLinePosition, rect.right, rect.top + this.mScanLinePosition, this.mScanLineColor, this.mPositions, Shader.TileMode.CLAMP);
            this.paint.setShader(this.mLinearGradient);
            canvas.drawRect(rect.left, rect.top + this.mScanLinePosition, rect.right, rect.top + this.mScanLinePosition + this.mScanLineDepth, this.paint);
            this.paint.setShader(null);
        }
        postInvalidateDelayed(10L, rect.left, rect.top, rect.right, rect.bottom);
    }
}
