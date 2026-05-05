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
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityAddDeviceBindingImpl extends ActivityAddDeviceBinding {

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
        sViewsWithIds.put(R.id.fl_titlebar, 2);
        sViewsWithIds.put(R.id.tv_wifi, 3);
        sViewsWithIds.put(R.id.tv_4g, 4);
        sViewsWithIds.put(R.id.tv_network_cable, 5);
        sViewsWithIds.put(R.id.tv_other_devices, 6);
        sViewsWithIds.put(R.id.rv_list, 7);
        sViewsWithIds.put(R.id.tv_ble, 8);
        sViewsWithIds.put(R.id.tv_ble_state, 9);
        sViewsWithIds.put(R.id.iv_ble_state, 10);
        sViewsWithIds.put(R.id.list_view, 11);
        sViewsWithIds.put(R.id.layout_ble_main, 12);
        sViewsWithIds.put(R.id.tv_title, 13);
        sViewsWithIds.put(R.id.layout_wifi, 14);
        sViewsWithIds.put(R.id.ed_wifi_name, 15);
        sViewsWithIds.put(R.id.iv_switch, 16);
        sViewsWithIds.put(R.id.ed_wifi_pass, 17);
        sViewsWithIds.put(R.id.iv_pass, 18);
        sViewsWithIds.put(R.id.layout_link_state, 19);
        sViewsWithIds.put(R.id.iv_gif, 20);
        sViewsWithIds.put(R.id.tv_device_link_time, 21);
        sViewsWithIds.put(R.id.tv_device_link, 22);
        sViewsWithIds.put(R.id.tv_device_bind_user, 23);
        sViewsWithIds.put(R.id.tv_link, 24);
    }

    public ActivityAddDeviceBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 25, sIncludes, sViewsWithIds));
    }

    private ActivityAddDeviceBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (EditText) objArr[15], (EditText) objArr[17], (TitleView) objArr[2], (ImageView) objArr[10], (ImageView) objArr[20], (ImageView) objArr[18], (TextView) objArr[16], (RelativeLayout) objArr[1], (RelativeLayout) objArr[12], (LinearLayout) objArr[19], (LinearLayout) objArr[0], (LinearLayout) objArr[14], (RecyclerView) objArr[11], (RecyclerView) objArr[7], (TextView) objArr[4], (TextView) objArr[8], (TextView) objArr[9], (TextView) objArr[23], (TextView) objArr[22], (TextView) objArr[21], (TextView) objArr[24], (TextView) objArr[5], (TextView) objArr[6], (TextView) objArr[13], (TextView) objArr[3]);
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
