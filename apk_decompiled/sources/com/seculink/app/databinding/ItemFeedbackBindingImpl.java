package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.adapters.TextViewBindingAdapter;
import bean.FeedbackBean;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ItemFeedbackBindingImpl extends ItemFeedbackBinding {

    @Nullable
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;

    @Nullable
    private static final SparseIntArray sViewsWithIds = new SparseIntArray();
    private long mDirtyFlags;

    @NonNull
    private final RelativeLayout mboundView0;

    @NonNull
    private final TextView mboundView1;

    @Override // androidx.databinding.ViewDataBinding
    protected boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    static {
        sViewsWithIds.put(R.id.layout_item, 2);
        sViewsWithIds.put(R.id.tv_type, 3);
        sViewsWithIds.put(R.id.tv_time, 4);
        sViewsWithIds.put(R.id.line, 5);
    }

    public ItemFeedbackBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 6, sIncludes, sViewsWithIds));
    }

    private ItemFeedbackBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (LinearLayout) objArr[2], (View) objArr[5], (TextView) objArr[4], (TextView) objArr[3]);
        this.mDirtyFlags = -1L;
        this.mboundView0 = (RelativeLayout) objArr[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (TextView) objArr[1];
        this.mboundView1.setTag(null);
        setRootTag(view2);
        invalidateAll();
    }

    @Override // androidx.databinding.ViewDataBinding
    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 2L;
        }
        requestRebind();
    }

    @Override // androidx.databinding.ViewDataBinding
    public boolean hasPendingBindings() {
        synchronized (this) {
            return this.mDirtyFlags != 0;
        }
    }

    @Override // androidx.databinding.ViewDataBinding
    public boolean setVariable(int i, @Nullable Object obj) {
        if (2 != i) {
            return false;
        }
        setModel((FeedbackBean) obj);
        return true;
    }

    @Override // com.seculink.app.databinding.ItemFeedbackBinding
    public void setModel(@Nullable FeedbackBean feedbackBean) {
        this.mModel = feedbackBean;
        synchronized (this) {
            this.mDirtyFlags |= 1;
        }
        notifyPropertyChanged(2);
        super.requestRebind();
    }

    @Override // androidx.databinding.ViewDataBinding
    protected void executeBindings() {
        long j;
        synchronized (this) {
            j = this.mDirtyFlags;
            this.mDirtyFlags = 0L;
        }
        String str = null;
        FeedbackBean feedbackBean = this.mModel;
        long j2 = j & 3;
        if (j2 != 0 && feedbackBean != null) {
            str = feedbackBean.content;
        }
        if (j2 != 0) {
            TextViewBindingAdapter.setText(this.mboundView1, str);
        }
    }
}
