package view;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes5.dex */
public class PagerLayoutManager extends LinearLayoutManager {
    private int mDrift;
    private RecyclerView.OnChildAttachStateChangeListener mOnChildAttachStateChangeListener;
    private OnViewPagerListener mOnViewPagerListener;
    private PagerSnapHelper mPagerSnapHelper;
    private RecyclerView mRecyclerView;

    public PagerLayoutManager(Context context, int i) {
        super(context, i, false);
        this.mOnChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() { // from class: view.PagerLayoutManager.1
            @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
            public void onChildViewAttachedToWindow(@NonNull View view2) {
                if (PagerLayoutManager.this.mOnViewPagerListener == null || PagerLayoutManager.this.getChildCount() != 1) {
                    return;
                }
                PagerLayoutManager.this.mOnViewPagerListener.onInitComplete(view2);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
            public void onChildViewDetachedFromWindow(@NonNull View view2) {
                if (PagerLayoutManager.this.mOnViewPagerListener == null) {
                    return;
                }
                if (PagerLayoutManager.this.mDrift >= 0) {
                    PagerLayoutManager.this.mOnViewPagerListener.onPageRelease(true, PagerLayoutManager.this.getPosition(view2), view2);
                } else {
                    PagerLayoutManager.this.mOnViewPagerListener.onPageRelease(false, PagerLayoutManager.this.getPosition(view2), view2);
                }
            }
        };
        init();
    }

    public PagerLayoutManager(Context context, int i, boolean z) {
        super(context, i, z);
        this.mOnChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() { // from class: view.PagerLayoutManager.1
            @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
            public void onChildViewAttachedToWindow(@NonNull View view2) {
                if (PagerLayoutManager.this.mOnViewPagerListener == null || PagerLayoutManager.this.getChildCount() != 1) {
                    return;
                }
                PagerLayoutManager.this.mOnViewPagerListener.onInitComplete(view2);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
            public void onChildViewDetachedFromWindow(@NonNull View view2) {
                if (PagerLayoutManager.this.mOnViewPagerListener == null) {
                    return;
                }
                if (PagerLayoutManager.this.mDrift >= 0) {
                    PagerLayoutManager.this.mOnViewPagerListener.onPageRelease(true, PagerLayoutManager.this.getPosition(view2), view2);
                } else {
                    PagerLayoutManager.this.mOnViewPagerListener.onPageRelease(false, PagerLayoutManager.this.getPosition(view2), view2);
                }
            }
        };
        init();
    }

    private void init() {
        this.mPagerSnapHelper = new PagerSnapHelper();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onAttachedToWindow(RecyclerView recyclerView) {
        super.onAttachedToWindow(recyclerView);
        this.mRecyclerView = recyclerView;
        this.mRecyclerView.setOnFlingListener(null);
        this.mPagerSnapHelper.attachToRecyclerView(recyclerView);
        this.mRecyclerView.addOnChildAttachStateChangeListener(this.mOnChildAttachStateChangeListener);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onDetachedFromWindow(RecyclerView recyclerView, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(recyclerView, recycler);
        recyclerView.stopScroll();
        recyclerView.clearAnimation();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onScrollStateChanged(int i) {
        super.onScrollStateChanged(i);
        switch (i) {
            case 0:
                View viewFindSnapView = this.mPagerSnapHelper.findSnapView(this);
                if (viewFindSnapView != null) {
                    int position = getPosition(viewFindSnapView);
                    if (this.mOnViewPagerListener != null && getChildCount() == 1) {
                        this.mOnViewPagerListener.onPageSelected(position, position == getItemCount() - 1, viewFindSnapView);
                        break;
                    }
                }
                break;
            case 1:
                if (this.mOnViewPagerListener != null && getChildCount() == 1) {
                    this.mOnViewPagerListener.onPageDragging();
                    break;
                }
                break;
        }
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = i;
        return super.scrollVerticallyBy(i, recycler, state);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = i;
        return super.scrollHorizontallyBy(i, recycler, state);
    }

    public void setOnViewPagerListener(OnViewPagerListener onViewPagerListener) {
        this.mOnViewPagerListener = onViewPagerListener;
    }
}
