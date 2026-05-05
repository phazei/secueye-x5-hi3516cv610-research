package adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes.dex */
public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "GridSpaceItemDecoration";
    private Paint dividerPaint = new Paint();
    private int mColumnSpacing;
    private Drawable mDivider;
    private int mHorizontalSpacing;
    private int mRowSpacing;
    private int mSpanCount;
    private int mVerticalSpacing;
    private int type;

    public GridSpaceItemDecoration(int i, int i2, int i3) {
        this.mSpanCount = i;
        this.mVerticalSpacing = i2;
        this.mHorizontalSpacing = i3;
        this.dividerPaint.setColor(Color.parseColor("#C6C8C9"));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(@NonNull Rect rect, @NonNull View view2, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {
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

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {
        recyclerView.getChildCount();
        if (recyclerView.getLayoutManager() != null) {
            if ((recyclerView.getLayoutManager() instanceof LinearLayoutManager) && !(recyclerView.getLayoutManager() instanceof GridLayoutManager)) {
                if (((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation() == 0) {
                    drawHorizontal(canvas, recyclerView);
                    return;
                } else {
                    drawVertical(canvas, recyclerView);
                    return;
                }
            }
            if (this.type == 0) {
                drawGrideview(canvas, recyclerView);
            } else {
                drawGrideview1(canvas, recyclerView);
            }
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView recyclerView) {
        int paddingTop = recyclerView.getPaddingTop();
        int measuredHeight = recyclerView.getMeasuredHeight() - recyclerView.getPaddingBottom();
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            int right = childAt.getRight() + ((RecyclerView.LayoutParams) childAt.getLayoutParams()).rightMargin;
            int i2 = this.mVerticalSpacing + right;
            Drawable drawable = this.mDivider;
            if (drawable != null) {
                drawable.setBounds(right, paddingTop, i2, measuredHeight);
                this.mDivider.draw(canvas);
            }
            Paint paint = this.dividerPaint;
            if (paint != null) {
                canvas.drawRect(right, paddingTop, i2, measuredHeight, paint);
            }
        }
    }

    private void drawHorizontal(Canvas canvas, RecyclerView recyclerView) {
        int paddingLeft = recyclerView.getPaddingLeft();
        int measuredWidth = recyclerView.getMeasuredWidth() - recyclerView.getPaddingRight();
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            int bottom = childAt.getBottom() + ((RecyclerView.LayoutParams) childAt.getLayoutParams()).bottomMargin;
            int i2 = this.mVerticalSpacing + bottom;
            Drawable drawable = this.mDivider;
            if (drawable != null) {
                drawable.setBounds(paddingLeft, bottom, measuredWidth, i2);
                this.mDivider.draw(canvas);
            }
            Paint paint = this.dividerPaint;
            if (paint != null) {
                canvas.drawRect(paddingLeft, bottom, measuredWidth, i2, paint);
            }
        }
    }

    private void drawGrideview(Canvas canvas, RecyclerView recyclerView) {
        RecyclerView.LayoutParams layoutParams;
        boolean z;
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        int childCount = recyclerView.getChildCount();
        int childCount2 = (recyclerView.getChildCount() / gridLayoutManager.getSpanCount()) - 1;
        if (childCount2 < 1) {
            childCount2 = 1;
        }
        int childCount3 = recyclerView.getChildCount() < gridLayoutManager.getSpanCount() ? recyclerView.getChildCount() : childCount2 * gridLayoutManager.getSpanCount();
        int spanCount = gridLayoutManager.getSpanCount() - 1;
        for (int i = 0; i < childCount; i++) {
            View childAt = recyclerView.getChildAt(i);
            RecyclerView.LayoutParams layoutParams2 = (RecyclerView.LayoutParams) childAt.getLayoutParams();
            if (i < childCount3) {
                int bottom = childAt.getBottom() + layoutParams2.bottomMargin;
                int i2 = this.mVerticalSpacing + bottom;
                int i3 = i + 1;
                int i4 = (layoutParams2.leftMargin + this.mVerticalSpacing) * i3;
                int measuredWidth = (childAt.getMeasuredWidth() * i3) + i4 + (this.mVerticalSpacing * i);
                Drawable drawable = this.mDivider;
                if (drawable != null) {
                    drawable.setBounds(i4, bottom, measuredWidth, i2);
                    this.mDivider.draw(canvas);
                }
                Paint paint = this.dividerPaint;
                if (paint != null) {
                    layoutParams = layoutParams2;
                    canvas.drawRect(i4, bottom, measuredWidth, i2, paint);
                } else {
                    layoutParams = layoutParams2;
                }
            } else {
                layoutParams = layoutParams2;
            }
            if (i != spanCount) {
                z = true;
                int spanCount2 = (layoutParams.topMargin + this.mVerticalSpacing) * ((i / gridLayoutManager.getSpanCount()) + 1);
                int bottom2 = childAt.getBottom() + layoutParams.bottomMargin;
                int right = childAt.getRight() + layoutParams.rightMargin;
                int i5 = this.mVerticalSpacing + right;
                Drawable drawable2 = this.mDivider;
                if (drawable2 != null) {
                    drawable2.setBounds(right, spanCount2, i5, bottom2);
                    this.mDivider.draw(canvas);
                }
                Paint paint2 = this.dividerPaint;
                if (paint2 != null) {
                    canvas.drawRect(right, spanCount2, i5, bottom2, paint2);
                }
            } else {
                z = true;
                spanCount += 4;
            }
        }
    }

    private void drawGrideview1(Canvas canvas, RecyclerView recyclerView) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        int childCount = recyclerView.getChildCount();
        int spanCount = gridLayoutManager.getSpanCount();
        int i = 0;
        while (i < childCount) {
            View childAt = recyclerView.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childAt.getLayoutParams();
            int bottom = childAt.getBottom() + layoutParams.bottomMargin;
            int i2 = this.mVerticalSpacing + bottom;
            int paddingLeft = layoutParams.leftMargin + childAt.getPaddingLeft() + this.mVerticalSpacing;
            int i3 = i + 1;
            int measuredWidth = (childAt.getMeasuredWidth() * i3) + paddingLeft + (this.mVerticalSpacing * i);
            Drawable drawable = this.mDivider;
            if (drawable != null) {
                drawable.setBounds(paddingLeft, bottom, measuredWidth, i2);
                this.mDivider.draw(canvas);
            }
            Paint paint = this.dividerPaint;
            if (paint != null) {
                canvas.drawRect(paddingLeft, bottom, measuredWidth, i2, paint);
            }
            int spanCount2 = (layoutParams.topMargin + this.mVerticalSpacing) * ((i / gridLayoutManager.getSpanCount()) + 1);
            int measuredHeight = ((childAt.getMeasuredHeight() + this.mVerticalSpacing) * ((i / gridLayoutManager.getSpanCount()) + 1)) + this.mVerticalSpacing;
            int right = childAt.getRight() + layoutParams.rightMargin;
            int i4 = this.mVerticalSpacing + right;
            Drawable drawable2 = this.mDivider;
            if (drawable2 != null) {
                drawable2.setBounds(right, spanCount2, i4, measuredHeight);
                this.mDivider.draw(canvas);
            }
            Paint paint2 = this.dividerPaint;
            if (paint2 != null) {
                canvas.drawRect(right, spanCount2, i4, measuredHeight, paint2);
            }
            if (i < spanCount) {
                int top = childAt.getTop() + layoutParams.topMargin;
                int i5 = this.mVerticalSpacing + top;
                int i6 = (layoutParams.leftMargin + this.mVerticalSpacing) * i3;
                int measuredWidth2 = (childAt.getMeasuredWidth() * i3) + i6 + (this.mVerticalSpacing * i);
                Drawable drawable3 = this.mDivider;
                if (drawable3 != null) {
                    drawable3.setBounds(i6, top, measuredWidth2, i5);
                    this.mDivider.draw(canvas);
                }
                Paint paint3 = this.dividerPaint;
                if (paint3 != null) {
                    canvas.drawRect(i6, top, measuredWidth2, i5, paint3);
                }
            }
            if (i % spanCount == 0) {
                int spanCount3 = (layoutParams.topMargin + this.mVerticalSpacing) * ((i / gridLayoutManager.getSpanCount()) + 1);
                int measuredHeight2 = ((childAt.getMeasuredHeight() + this.mVerticalSpacing) * ((i / gridLayoutManager.getSpanCount()) + 1)) + this.mVerticalSpacing;
                int left = childAt.getLeft() + layoutParams.leftMargin;
                int i7 = this.mVerticalSpacing + left;
                Drawable drawable4 = this.mDivider;
                if (drawable4 != null) {
                    drawable4.setBounds(left, spanCount3, i7, measuredHeight2);
                    this.mDivider.draw(canvas);
                }
                Paint paint4 = this.dividerPaint;
                if (paint4 != null) {
                    canvas.drawRect(left, spanCount3, i7, measuredHeight2, paint4);
                }
            }
            i = i3;
        }
    }
}
