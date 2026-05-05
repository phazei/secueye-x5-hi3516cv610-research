package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityBleScanBindingImpl extends ActivityBleScanBinding {

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
        sViewsWithIds.put(R.id.layout_ble, 1);
        sViewsWithIds.put(R.id.left_rl, 2);
        sViewsWithIds.put(R.id.left_img, 3);
        sViewsWithIds.put(R.id.layout_scan, 4);
        sViewsWithIds.put(R.id.tv_scan, 5);
        sViewsWithIds.put(R.id.iv_scanning, 6);
        sViewsWithIds.put(R.id.tv_ble, 7);
        sViewsWithIds.put(R.id.tv_ble_state, 8);
        sViewsWithIds.put(R.id.list_view, 9);
        sViewsWithIds.put(R.id.layout_ble_main, 10);
        sViewsWithIds.put(R.id.layout_wifi, 11);
        sViewsWithIds.put(R.id.ed_wifi_name, 12);
        sViewsWithIds.put(R.id.iv_switch, 13);
        sViewsWithIds.put(R.id.ed_wifi_pass, 14);
        sViewsWithIds.put(R.id.iv_pass, 15);
        sViewsWithIds.put(R.id.layout_link_state, 16);
        sViewsWithIds.put(R.id.iv_device_link, 17);
        sViewsWithIds.put(R.id.tv_device_link, 18);
        sViewsWithIds.put(R.id.iv_device_bind_user, 19);
        sViewsWithIds.put(R.id.tv_device_bind_user, 20);
        sViewsWithIds.put(R.id.tv_link, 21);
    }

    public ActivityBleScanBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 22, sIncludes, sViewsWithIds));
    }

    private ActivityBleScanBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (EditText) objArr[12], (EditText) objArr[14], (ImageView) objArr[19], (ImageView) objArr[17], (ImageView) objArr[15], (ImageView) objArr[6], (ImageView) objArr[13], (LinearLayout) objArr[1], (RelativeLayout) objArr[10], (LinearLayout) objArr[16], (LinearLayout) objArr[0], (LinearLayout) objArr[4], (LinearLayout) objArr[11], (ImageView) objArr[3], (RelativeLayout) objArr[2], (RecyclerView) objArr[9], (TextView) objArr[7], (TextView) objArr[8], (TextView) objArr[20], (TextView) objArr[18], (TextView) objArr[21], (TextView) objArr[5]);
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
