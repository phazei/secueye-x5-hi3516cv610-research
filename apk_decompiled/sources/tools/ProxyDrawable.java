package tools;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/* JADX INFO: loaded from: classes4.dex */
public class ProxyDrawable extends StateListDrawable {
    private Drawable originDrawable;

    @Override // android.graphics.drawable.StateListDrawable
    public void addState(int[] iArr, Drawable drawable) {
        if (iArr != null && iArr.length == 1 && iArr[0] == 0) {
            this.originDrawable = drawable;
        }
        super.addState(iArr, drawable);
    }

    Drawable getOriginDrawable() {
        return this.originDrawable;
    }
}
