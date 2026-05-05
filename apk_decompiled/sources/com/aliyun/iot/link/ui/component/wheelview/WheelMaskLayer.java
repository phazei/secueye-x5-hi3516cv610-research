package com.aliyun.iot.link.ui.component.wheelview;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes2.dex */
public class WheelMaskLayer implements WheelLayer {
    private int[] colors;
    private Shader mMask;
    private int mMaskHeight;
    private float[] positions;

    public WheelMaskLayer(@NonNull int[] iArr, @NonNull float[] fArr) {
        this.colors = iArr;
        this.positions = fArr;
    }

    @Override // com.aliyun.iot.link.ui.component.wheelview.WheelLayer
    public void onDraw(ILopWheelView iLopWheelView, Canvas canvas, Rect rect) {
        if (rect.height() != this.mMaskHeight) {
            this.mMask = new LinearGradient(rect.centerX(), rect.top, rect.centerX(), rect.bottom, this.colors, this.positions, Shader.TileMode.CLAMP);
            this.mMaskHeight = rect.height();
        }
        Paint paint = iLopWheelView.getPaint();
        paint.setShader(this.mMask);
        canvas.drawRect(rect, paint);
        paint.setShader(null);
    }
}
