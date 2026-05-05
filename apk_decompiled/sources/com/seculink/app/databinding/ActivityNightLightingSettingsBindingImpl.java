package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.LongItemView;
import view.SettingTitleView;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityNightLightingSettingsBindingImpl extends ActivityNightLightingSettingsBinding {

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
        sViewsWithIds.put(R.id.Safety, 3);
        sViewsWithIds.put(R.id.item_night_mode, 4);
        sViewsWithIds.put(R.id.light_setting_ptz, 5);
        sViewsWithIds.put(R.id.light_progress_setting_ptz, 6);
        sViewsWithIds.put(R.id.infrared_light_setting_ptz, 7);
        sViewsWithIds.put(R.id.infrared_light_progress_setting_ptz, 8);
        sViewsWithIds.put(R.id.layout_gun, 9);
        sViewsWithIds.put(R.id.Safety_gun, 10);
        sViewsWithIds.put(R.id.item_night_mode_gun, 11);
        sViewsWithIds.put(R.id.light_setting_gun, 12);
        sViewsWithIds.put(R.id.light_progress_setting_gun, 13);
        sViewsWithIds.put(R.id.infrared_light_setting_gun, 14);
        sViewsWithIds.put(R.id.infrared_light_progress_setting_gun, 15);
        sViewsWithIds.put(R.id.layout_floodlight, 16);
        sViewsWithIds.put(R.id.floodlight_tips, 17);
        sViewsWithIds.put(R.id.it_floodlight_switch, 18);
        sViewsWithIds.put(R.id.it_floodlight_time_switch, 19);
        sViewsWithIds.put(R.id.it_floodlight_time_setting, 20);
        sViewsWithIds.put(R.id.layout_exp_high_light, 21);
        sViewsWithIds.put(R.id.it_exp_high_light, 22);
        sViewsWithIds.put(R.id.layout_exp_high_light_level, 23);
        sViewsWithIds.put(R.id.seek_bar_exp_high_light_level, 24);
    }

    public ActivityNightLightingSettingsBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 25, sIncludes, sViewsWithIds));
    }

    private ActivityNightLightingSettingsBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (SettingTitleView) objArr[3], (SettingTitleView) objArr[10], (SettingTitleView) objArr[17], (AppCompatSeekBar) objArr[15], (AppCompatSeekBar) objArr[8], (LinearLayout) objArr[14], (LinearLayout) objArr[7], (LongItemView) objArr[22], (LongItemView) objArr[18], (LongItemView) objArr[20], (LongItemView) objArr[19], (LongItemView) objArr[4], (LongItemView) objArr[11], (LinearLayout) objArr[21], (LinearLayout) objArr[23], (LinearLayout) objArr[16], (LinearLayout) objArr[9], (LinearLayout) objArr[0], (ImageView) objArr[2], (RelativeLayout) objArr[1], (AppCompatSeekBar) objArr[13], (AppCompatSeekBar) objArr[6], (LinearLayout) objArr[12], (LinearLayout) objArr[5], (AppCompatSeekBar) objArr[24]);
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
