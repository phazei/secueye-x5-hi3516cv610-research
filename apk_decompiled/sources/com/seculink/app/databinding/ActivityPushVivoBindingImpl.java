package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityPushVivoBindingImpl extends ActivityPushVivoBinding {

    @Nullable
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;

    @Nullable
    private static final SparseIntArray sViewsWithIds = new SparseIntArray();
    private long mDirtyFlags;

    @Override // androidx.databinding.ViewDataBinding
    protected boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    @Override // androidx.databinding.ViewDataBinding
    public boolean setVariable(int i, @Nullable Object obj) {
        return true;
    }

    static {
        sViewsWithIds.put(R.id.fl_titlebar, 1);
        sViewsWithIds.put(R.id.tv_text_1, 2);
        sViewsWithIds.put(R.id.tv_text_3, 3);
    }

    public ActivityPushVivoBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 4, sIncludes, sViewsWithIds));
    }

    private ActivityPushVivoBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (TitleView) objArr[1], (LinearLayout) objArr[0], (TextView) objArr[2], (TextView) objArr[3]);
        this.mDirtyFlags = -1L;
        this.layoutMain.setTag(null);
        setRootTag(view2);
        invalidateAll();
    }

    @Override // androidx.databinding.ViewDataBinding
    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 1L;
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
    protected void executeBindings() {
        synchronized (this) {
            long j = this.mDirtyFlags;
            this.mDirtyFlags = 0L;
        }
    }
}
