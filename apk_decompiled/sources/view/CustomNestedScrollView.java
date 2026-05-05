package view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.core.widget.NestedScrollView;

/* JADX INFO: loaded from: classes5.dex */
public class CustomNestedScrollView extends NestedScrollView {
    private int maxScrollY;

    public CustomNestedScrollView(Context context) {
        super(context);
        this.maxScrollY = 200;
    }

    public CustomNestedScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.maxScrollY = 200;
    }

    public CustomNestedScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.maxScrollY = 200;
    }

    @Override // androidx.core.widget.NestedScrollView, android.view.View
    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        int i5 = this.maxScrollY;
        if (i2 > i5) {
            scrollTo(0, i5);
        }
    }

    public void setMaxScrollY(int i) {
        this.maxScrollY = i;
    }
}
