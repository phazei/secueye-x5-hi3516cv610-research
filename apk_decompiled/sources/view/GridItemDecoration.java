package view;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes5.dex */
public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    public GridItemDecoration(int i, int i2) {
        this.mHorizontalSpacing = i;
        this.mVerticalSpacing = i2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view2, RecyclerView recyclerView, RecyclerView.State state) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view2.getLayoutParams();
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view2);
        int spanCount = gridLayoutManager.getSpanCount();
        if (gridLayoutManager.getOrientation() == 1) {
            if (gridLayoutManager.getSpanSizeLookup().getSpanGroupIndex(childAdapterPosition, spanCount) == 0) {
                rect.top = this.mVerticalSpacing;
            }
            rect.bottom = this.mVerticalSpacing;
            if (layoutParams.getSpanSize() == spanCount) {
                int i = this.mHorizontalSpacing;
                rect.left = i;
                rect.right = i;
                return;
            } else {
                float f = spanCount;
                float spanIndex = (spanCount - layoutParams.getSpanIndex()) / f;
                int i2 = this.mHorizontalSpacing;
                rect.left = (int) (spanIndex * i2);
                rect.right = (int) (((i2 * (spanCount + 1)) / f) - rect.left);
                return;
            }
        }
        if (gridLayoutManager.getSpanSizeLookup().getSpanGroupIndex(childAdapterPosition, spanCount) == 0) {
            rect.left = this.mHorizontalSpacing;
        }
        rect.right = this.mHorizontalSpacing;
        if (layoutParams.getSpanSize() == spanCount) {
            int i3 = this.mVerticalSpacing;
            rect.top = i3;
            rect.bottom = i3;
        } else {
            float f2 = spanCount;
            float spanIndex2 = (spanCount - layoutParams.getSpanIndex()) / f2;
            int i4 = this.mVerticalSpacing;
            rect.top = (int) (spanIndex2 * i4);
            rect.bottom = (int) (((i4 * (spanCount + 1)) / f2) - rect.top);
        }
    }
}
