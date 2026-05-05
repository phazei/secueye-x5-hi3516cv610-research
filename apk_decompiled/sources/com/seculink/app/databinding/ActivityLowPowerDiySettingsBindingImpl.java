package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.LongItemView;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityLowPowerDiySettingsBindingImpl extends ActivityLowPowerDiySettingsBinding {

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
        sViewsWithIds.put(R.id.it_low_power_diy, 3);
        sViewsWithIds.put(R.id.it_low_power_fixed_time, 4);
        sViewsWithIds.put(R.id.it_low_power_pir, 5);
        sViewsWithIds.put(R.id.layout_app, 6);
        sViewsWithIds.put(R.id.it_ivp, 7);
        sViewsWithIds.put(R.id.it_low_power_record, 8);
        sViewsWithIds.put(R.id.it_low_power_wake, 9);
    }

    public ActivityLowPowerDiySettingsBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 10, sIncludes, sViewsWithIds));
    }

    private ActivityLowPowerDiySettingsBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (LongItemView) objArr[7], (LongItemView) objArr[3], (LongItemView) objArr[4], (LongItemView) objArr[5], (LongItemView) objArr[8], (LongItemView) objArr[9], (LinearLayout) objArr[6], (LinearLayout) objArr[0], (ImageView) objArr[2], (RelativeLayout) objArr[1]);
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
