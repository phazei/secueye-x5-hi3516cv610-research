package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.ItemView;
import view.LongItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityBleRouterSettingBindingImpl extends ActivityBleRouterSettingBinding {

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
        sViewsWithIds.put(R.id.item_camera_info, 2);
        sViewsWithIds.put(R.id.item_time_setting, 3);
        sViewsWithIds.put(R.id.item_share_manage, 4);
        sViewsWithIds.put(R.id.item_lan, 5);
        sViewsWithIds.put(R.id.item_hotspot, 6);
        sViewsWithIds.put(R.id.item_connected_device, 7);
        sViewsWithIds.put(R.id.item_apn, 8);
        sViewsWithIds.put(R.id.item_firmware_version, 9);
        sViewsWithIds.put(R.id.item_reset_setting, 10);
        sViewsWithIds.put(R.id.item_restart_dev, 11);
        sViewsWithIds.put(R.id.bt_remove_camera, 12);
    }

    public ActivityBleRouterSettingBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 13, sIncludes, sViewsWithIds));
    }

    private ActivityBleRouterSettingBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (Button) objArr[12], (TitleView) objArr[1], (ItemView) objArr[8], (LongItemView) objArr[2], (ItemView) objArr[7], (LongItemView) objArr[9], (ItemView) objArr[6], (ItemView) objArr[5], (ItemView) objArr[10], (ItemView) objArr[11], (ItemView) objArr[4], (ItemView) objArr[3], (LinearLayout) objArr[0]);
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
