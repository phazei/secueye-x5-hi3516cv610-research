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
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityBleRouterBindingImpl extends ActivityBleRouterBinding {

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
        sViewsWithIds.put(R.id.layout_top, 1);
        sViewsWithIds.put(R.id.iv_back, 2);
        sViewsWithIds.put(R.id.tv_title, 3);
        sViewsWithIds.put(R.id.layout_battery, 4);
        sViewsWithIds.put(R.id.iv_battery, 5);
        sViewsWithIds.put(R.id.tv_battery, 6);
        sViewsWithIds.put(R.id.iv_battery_charger, 7);
        sViewsWithIds.put(R.id.tv_battery_voltage, 8);
        sViewsWithIds.put(R.id.tv_sim, 9);
        sViewsWithIds.put(R.id.iv_line, 10);
        sViewsWithIds.put(R.id.tv_line, 11);
        sViewsWithIds.put(R.id.tv_card, 12);
        sViewsWithIds.put(R.id.layout_imei, 13);
        sViewsWithIds.put(R.id.iv_vsim_card, 14);
        sViewsWithIds.put(R.id.tv_imei, 15);
        sViewsWithIds.put(R.id.layout_iccid, 16);
        sViewsWithIds.put(R.id.iv_iccid, 17);
        sViewsWithIds.put(R.id.tv_iccid, 18);
        sViewsWithIds.put(R.id.layout_lan, 19);
        sViewsWithIds.put(R.id.dhcp_start_address, 20);
        sViewsWithIds.put(R.id.dhcp_end_address, 21);
        sViewsWithIds.put(R.id.layout_wifi, 22);
        sViewsWithIds.put(R.id.tv_wifi_name, 23);
        sViewsWithIds.put(R.id.tv_wifi_pass, 24);
        sViewsWithIds.put(R.id.layout_connect, 25);
        sViewsWithIds.put(R.id.tv_lan_size, 26);
        sViewsWithIds.put(R.id.tv_hotspot_size, 27);
        sViewsWithIds.put(R.id.layout_setting, 28);
    }

    public ActivityBleRouterBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 29, sIncludes, sViewsWithIds));
    }

    private ActivityBleRouterBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (TextView) objArr[21], (TextView) objArr[20], (ImageView) objArr[2], (ImageView) objArr[5], (ImageView) objArr[7], (ImageView) objArr[17], (ImageView) objArr[10], (ImageView) objArr[14], (RelativeLayout) objArr[4], (RelativeLayout) objArr[25], (RelativeLayout) objArr[16], (RelativeLayout) objArr[13], (RelativeLayout) objArr[19], (RelativeLayout) objArr[0], (RelativeLayout) objArr[28], (RelativeLayout) objArr[1], (RelativeLayout) objArr[22], (TextView) objArr[6], (TextView) objArr[8], (TextView) objArr[12], (TextView) objArr[27], (TextView) objArr[18], (TextView) objArr[15], (TextView) objArr[26], (TextView) objArr[11], (TextView) objArr[9], (TextView) objArr[3], (TextView) objArr[23], (TextView) objArr[24]);
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
