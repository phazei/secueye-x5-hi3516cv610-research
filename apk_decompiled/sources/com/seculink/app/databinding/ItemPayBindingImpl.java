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
import bean.CloudPayModel;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ItemPayBindingImpl extends ItemPayBinding {

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
        sViewsWithIds.put(R.id.tv_name, 1);
        sViewsWithIds.put(R.id.tv_order, 2);
        sViewsWithIds.put(R.id.tv_type, 3);
        sViewsWithIds.put(R.id.tv_time, 4);
        sViewsWithIds.put(R.id.tv_type_text, 5);
        sViewsWithIds.put(R.id.iv_type, 6);
    }

    public ItemPayBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 7, sIncludes, sViewsWithIds));
    }

    private ItemPayBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (ImageView) objArr[6], (RelativeLayout) objArr[0], (TextView) objArr[1], (TextView) objArr[2], (TextView) objArr[4], (TextView) objArr[3], (TextView) objArr[5]);
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
        setModel((CloudPayModel) obj);
        return true;
    }

    @Override // com.seculink.app.databinding.ItemPayBinding
    public void setModel(@Nullable CloudPayModel cloudPayModel) {
        this.mModel = cloudPayModel;
    }

    @Override // androidx.databinding.ViewDataBinding
    protected void executeBindings() {
        synchronized (this) {
            long j = this.mDirtyFlags;
            this.mDirtyFlags = 0L;
        }
    }
}
