package tools;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes4.dex */
public class PresetGridSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    public PresetGridSpaceItemDecoration(int i, int i2) {
        this.mVerticalSpacing = i;
        this.mHorizontalSpacing = i2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(@NonNull Rect rect, @NonNull View view2, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {
        super.getItemOffsets(rect, view2, recyclerView, state);
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view2.getLayoutParams();
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view2);
        int spanCount = gridLayoutManager.getSpanCount();
        if (gridLayoutManager.getOrientation() == 1) {
            gridLayoutManager.getSpanSizeLookup().getSpanGroupIndex(childAdapterPosition, spanCount);
            if (layoutParams.getSpanSize() == spanCount) {
                int i = this.mHorizontalSpacing;
                rect.left = i;
                rect.right = i;
                return;
            } else {
                float f = spanCount;
                float spanIndex = (spanCount - layoutParams.getSpanIndex()) / f;
                int i2 = this.mHorizontalSpacing;
                rect.left = ((int) (spanIndex * i2)) + 20;
                rect.right = ((int) (((i2 * (spanCount + 1)) / f) - rect.left)) + 40;
                return;
            }
        }
        if (gridLayoutManager.getSpanSizeLookup().getSpanGroupIndex(childAdapterPosition, spanCount) == 0) {
            rect.left = this.mHorizontalSpacing;
        }
        rect.right = this.mHorizontalSpacing;
        if (layoutParams.getSpanSize() == spanCount) {
            rect.top = this.mVerticalSpacing;
        } else {
            rect.top = (int) (((spanCount - layoutParams.getSpanIndex()) / spanCount) * this.mVerticalSpacing);
        }
    }
}
