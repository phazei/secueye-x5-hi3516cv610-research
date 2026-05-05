package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.LongItemView;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityLowPowerSettingsBindingImpl extends ActivityLowPowerSettingsBinding {

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
        sViewsWithIds.put(R.id.it_low_power_switch, 3);
        sViewsWithIds.put(R.id.tv_low_power_tips, 4);
        sViewsWithIds.put(R.id.layout_sleep_switch, 5);
        sViewsWithIds.put(R.id.item_sleep_switch, 6);
        sViewsWithIds.put(R.id.it_low_power_diy2, 7);
        sViewsWithIds.put(R.id.tv_low_power_diy_tips, 8);
        sViewsWithIds.put(R.id.layout_low_power_wake, 9);
        sViewsWithIds.put(R.id.seek_low_power_wake, 10);
        sViewsWithIds.put(R.id.layout_low_power_record, 11);
        sViewsWithIds.put(R.id.seek_low_power_record, 12);
        sViewsWithIds.put(R.id.layout_low_power_fixed_time, 13);
        sViewsWithIds.put(R.id.seek_low_power_fixed_time, 14);
        sViewsWithIds.put(R.id.layout_diy, 15);
        sViewsWithIds.put(R.id.it_low_power_diy, 16);
        sViewsWithIds.put(R.id.it_low_power_fixed_time, 17);
        sViewsWithIds.put(R.id.it_low_power_pir, 18);
        sViewsWithIds.put(R.id.layout_app, 19);
        sViewsWithIds.put(R.id.it_ivp, 20);
        sViewsWithIds.put(R.id.it_low_power_record, 21);
        sViewsWithIds.put(R.id.it_low_power_wake, 22);
    }

    public ActivityLowPowerSettingsBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 23, sIncludes, sViewsWithIds));
    }

    private ActivityLowPowerSettingsBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (LongItemView) objArr[20], (LongItemView) objArr[16], (LongItemView) objArr[7], (LongItemView) objArr[17], (LongItemView) objArr[18], (LongItemView) objArr[21], (LongItemView) objArr[3], (LongItemView) objArr[22], (LongItemView) objArr[6], (LinearLayout) objArr[19], (LinearLayout) objArr[15], (LinearLayout) objArr[13], (LinearLayout) objArr[11], (LinearLayout) objArr[9], (LinearLayout) objArr[0], (LinearLayout) objArr[5], (ImageView) objArr[2], (RelativeLayout) objArr[1], (AppCompatSeekBar) objArr[14], (AppCompatSeekBar) objArr[12], (AppCompatSeekBar) objArr[10], (TextView) objArr[8], (TextView) objArr[4]);
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
