package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityThreeYunSelectBindingImpl extends ActivityThreeYunSelectBinding {

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
        sViewsWithIds.put(R.id.left_rl, 1);
        sViewsWithIds.put(R.id.left_img, 2);
        sViewsWithIds.put(R.id.gun1, 3);
        sViewsWithIds.put(R.id.gun2, 4);
        sViewsWithIds.put(R.id.ptz, 5);
    }

    public ActivityThreeYunSelectBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 6, sIncludes, sViewsWithIds));
    }

    private ActivityThreeYunSelectBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (TextView) objArr[3], (TextView) objArr[4], (LinearLayout) objArr[0], (ImageView) objArr[2], (RelativeLayout) objArr[1], (TextView) objArr[5]);
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
