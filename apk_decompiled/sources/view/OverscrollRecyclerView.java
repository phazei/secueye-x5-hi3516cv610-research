package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes5.dex */
public class OverscrollRecyclerView extends RecyclerView {
    private boolean isAtBottom;
    private float lastY;
    private OnBottomOverscrollListener listener;

    public interface OnBottomOverscrollListener {
        void onBottomOverscroll(int i);
    }

    public OverscrollRecyclerView(Context context) {
        super(context);
    }

    public OverscrollRecyclerView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public OverscrollRecyclerView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setOnBottomOverscrollListener(OnBottomOverscrollListener onBottomOverscrollListener) {
        this.listener = onBottomOverscrollListener;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        OnBottomOverscrollListener onBottomOverscrollListener;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.lastY = motionEvent.getRawY();
        } else if (action == 2) {
            float rawY = motionEvent.getRawY();
            float f = rawY - this.lastY;
            this.lastY = rawY;
            if (this.isAtBottom && f > 0.0f && (onBottomOverscrollListener = this.listener) != null) {
                onBottomOverscrollListener.onBottomOverscroll((int) f);
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void onScrolled(int i, int i2) {
        super.onScrolled(i, i2);
        this.isAtBottom = true ^ canScrollVertically(1);
    }
}
