package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TimePicker;

/* JADX INFO: loaded from: classes5.dex */
public class MyTimePicker extends TimePicker {
    public MyTimePicker(Context context) {
        super(context);
    }

    public MyTimePicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MyTimePicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(motionEvent);
    }
}
