package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import bean.SearchModel;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ItemSearchBindingImpl extends ItemSearchBinding {

    @Nullable
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;

    @Nullable
    private static final SparseIntArray sViewsWithIds = new SparseIntArray();
    private long mDirtyFlags;

    @Override // androidx.databinding.ViewDataBinding
    protected boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    static {
        sViewsWithIds.put(R.id.top, 1);
        sViewsWithIds.put(R.id.iv_center, 2);
        sViewsWithIds.put(R.id.bottom, 3);
        sViewsWithIds.put(R.id.tv_time, 4);
        sViewsWithIds.put(R.id.tv_type, 5);
    }

    public ItemSearchBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 6, sIncludes, sViewsWithIds));
    }

    private ItemSearchBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (View) objArr[3], (ImageView) objArr[2], (RelativeLayout) objArr[0], (View) objArr[1], (TextView) objArr[4], (TextView) objArr[5]);
        this.mDirtyFlags = -1L;
        this.layoutItem.setTag(null);
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
        setModel((SearchModel) obj);
        return true;
    }

    @Override // com.seculink.app.databinding.ItemSearchBinding
    public void setModel(@Nullable SearchModel searchModel) {
        this.mModel = searchModel;
    }

    @Override // androidx.databinding.ViewDataBinding
    protected void executeBindings() {
        synchronized (this) {
            long j = this.mDirtyFlags;
            this.mDirtyFlags = 0L;
        }
    }
}
