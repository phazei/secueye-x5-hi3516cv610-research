package com.aliyun.iot.link.ui.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.aliyun.iot.link.ui.component.adapter.LoadMoreWrapperAdapter;
import com.aliyun.iot.link.ui.component.statusview.AbstractStatusView;
import com.aliyun.iot.link.ui.component.statusview.LinkStatusView;

/* JADX INFO: loaded from: classes2.dex */
public class RefreshRecycleViewLayout extends SwipeRefreshLayout implements AbstractStatusView {
    private LinkStatusView linkStatusView;
    private LoadMoreWrapperAdapter loadMoreWrapperAdapter;
    private LoadMoreWrapperAdapter.RequestLoadMoreListener onLoadMoreListener;
    private RecyclerView recyclerView;

    public RefreshRecycleViewLayout(Context context) {
        this(context, null);
    }

    public RefreshRecycleViewLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.linkStatusView = null;
        this.recyclerView = null;
        this.loadMoreWrapperAdapter = null;
        this.onLoadMoreListener = null;
        applyStyle();
        findSubViews();
    }

    private void applyStyle() {
        setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(1, 24.0f, getResources().getDisplayMetrics()));
        setColorSchemeColors(-16745985);
        setSize(1);
        setProgressBackgroundColorSchemeColor(-1);
    }

    private void findSubViews() {
        this.linkStatusView = new LinkStatusView(getContext());
        this.linkStatusView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        addView(this.linkStatusView);
        this.recyclerView = (RecyclerView) LayoutInflater.from(getContext()).inflate(R.layout.recyclerview, (ViewGroup) this.linkStatusView, false);
        this.recyclerView.setId(R.id.link_status_content_view);
        this.recyclerView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        this.linkStatusView.addView(this.recyclerView, 0);
    }

    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    public void setOnRefreshListener(final SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        super.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.aliyun.iot.link.ui.component.RefreshRecycleViewLayout.1
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                if (onRefreshListener != null) {
                    if (RefreshRecycleViewLayout.this.loadMoreWrapperAdapter != null && RefreshRecycleViewLayout.this.loadMoreWrapperAdapter.isLoading()) {
                        RefreshRecycleViewLayout.this.loadMoreWrapperAdapter.loadMoreComplete();
                    }
                    onRefreshListener.onRefresh();
                }
            }
        });
    }

    public LoadMoreWrapperAdapter getAdapter() {
        return this.loadMoreWrapperAdapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter2) {
        if (adapter2 == null) {
            return;
        }
        this.loadMoreWrapperAdapter = new LoadMoreWrapperAdapter(this, adapter2);
        this.recyclerView.setAdapter(this.loadMoreWrapperAdapter);
    }

    public void setOnLoadMoreListener(LoadMoreWrapperAdapter.RequestLoadMoreListener requestLoadMoreListener) {
        LoadMoreWrapperAdapter loadMoreWrapperAdapter = this.loadMoreWrapperAdapter;
        if (loadMoreWrapperAdapter != null) {
            loadMoreWrapperAdapter.setRequestLoadMoreListener(requestLoadMoreListener);
        }
    }

    public void setEnableLoadMore(boolean z) {
        LoadMoreWrapperAdapter loadMoreWrapperAdapter = this.loadMoreWrapperAdapter;
        if (loadMoreWrapperAdapter != null) {
            loadMoreWrapperAdapter.setEnableLoadMore(z);
        }
    }

    public void loadMoreComplete() {
        LoadMoreWrapperAdapter loadMoreWrapperAdapter = this.loadMoreWrapperAdapter;
        if (loadMoreWrapperAdapter != null) {
            loadMoreWrapperAdapter.loadMoreComplete();
        }
    }

    public void loadMoreEnd() {
        LoadMoreWrapperAdapter loadMoreWrapperAdapter = this.loadMoreWrapperAdapter;
        if (loadMoreWrapperAdapter != null) {
            loadMoreWrapperAdapter.loadMoreEnd(true);
        }
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void showContentView() {
        this.linkStatusView.showContentView();
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void customizeEmptyView(@NonNull View view2, FrameLayout.LayoutParams layoutParams) {
        this.linkStatusView.customizeEmptyView(view2, layoutParams);
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void showEmptyView() {
        this.linkStatusView.showEmptyView();
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void setDefaultEmptyView(@Nullable String str) {
        this.linkStatusView.setDefaultEmptyView(str);
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void setDefaultEmptyView(int i) {
        this.linkStatusView.setDefaultEmptyView(i);
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void setDefaultEmptyView(int i, int i2) {
        this.linkStatusView.setDefaultEmptyView(i, i2);
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void setDefaultEmptyView(@Nullable String str, @Nullable Drawable drawable) {
        this.linkStatusView.setDefaultEmptyView(str, drawable);
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void showErrorView() {
        this.linkStatusView.showErrorView();
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void customizeErrorView(@NonNull View view2, FrameLayout.LayoutParams layoutParams) {
        this.linkStatusView.customizeErrorView(view2, layoutParams);
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void setDefaultErrorView(int i, int i2, @Nullable AbstractStatusView.OnRetryListener onRetryListener) {
        this.linkStatusView.setDefaultErrorView(i, i2, onRetryListener);
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void setDefaultErrorView(@NonNull String str, @NonNull String str2, @Nullable AbstractStatusView.OnRetryListener onRetryListener) {
        this.linkStatusView.setDefaultErrorView(str, str2, onRetryListener);
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void setDefaultErrorView(int i, int i2, int i3, @Nullable AbstractStatusView.OnRetryListener onRetryListener) {
        this.linkStatusView.setDefaultErrorView(i, i2, i3, onRetryListener);
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void setDefaultErrorView(@Nullable String str, @Nullable String str2, @Nullable Drawable drawable, @Nullable AbstractStatusView.OnRetryListener onRetryListener) {
        this.linkStatusView.setDefaultErrorView(str, str2, drawable, onRetryListener);
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void showLoading(FragmentActivity fragmentActivity) {
        this.linkStatusView.showLoading(fragmentActivity);
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void showLoading(FragmentActivity fragmentActivity, int i) {
        LoadingCompact.showLoading(fragmentActivity, 0, i, false, false, null);
    }

    @Override // com.aliyun.iot.link.ui.component.statusview.AbstractStatusView
    public void dismissLoading(FragmentActivity fragmentActivity) {
        LoadingCompact.dismissLoading(fragmentActivity);
    }

    public LinkStatusView getLinkStatusView() {
        return this.linkStatusView;
    }
}
