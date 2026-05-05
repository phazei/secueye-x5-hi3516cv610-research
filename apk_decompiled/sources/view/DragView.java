package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/* JADX INFO: loaded from: classes5.dex */
public class DragView extends View {
    private int lastX;
    private int lastY;

    public DragView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public DragView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DragView(Context context) {
        super(context);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0) {
            this.lastX = x;
            this.lastY = y;
            return true;
        }
        if (action != 2) {
            return true;
        }
        int i = x - this.lastX;
        int i2 = y - this.lastY;
        offsetLeftAndRight(i);
        offsetTopAndBottom(i2);
        return true;
    }
}
