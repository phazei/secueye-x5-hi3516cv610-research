package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.LongItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityRouterHotspotBindingImpl extends ActivityRouterHotspotBinding {

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
        sViewsWithIds.put(R.id.item_nat_wlan_switch, 2);
        sViewsWithIds.put(R.id.edit_name, 3);
        sViewsWithIds.put(R.id.edit_pass, 4);
        sViewsWithIds.put(R.id.item_nat_wlan_security, 5);
        sViewsWithIds.put(R.id.item_mac, 6);
        sViewsWithIds.put(R.id.item_ip, 7);
        sViewsWithIds.put(R.id.item_mask, 8);
        sViewsWithIds.put(R.id.dhcp_lease, 9);
        sViewsWithIds.put(R.id.dhcp_start_address, 10);
        sViewsWithIds.put(R.id.dhcp_end_address, 11);
        sViewsWithIds.put(R.id.dhcp_default_router, 12);
        sViewsWithIds.put(R.id.dhcp_sub_net_mask, 13);
        sViewsWithIds.put(R.id.dhcp_first_dns_address, 14);
        sViewsWithIds.put(R.id.dhcp_second_dns_address, 15);
        sViewsWithIds.put(R.id.bt_save, 16);
    }

    public ActivityRouterHotspotBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 17, sIncludes, sViewsWithIds));
    }

    private ActivityRouterHotspotBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (Button) objArr[16], (EditText) objArr[12], (EditText) objArr[11], (EditText) objArr[14], (EditText) objArr[9], (EditText) objArr[15], (EditText) objArr[10], (EditText) objArr[13], (EditText) objArr[3], (EditText) objArr[4], (TitleView) objArr[1], (LongItemView) objArr[7], (LongItemView) objArr[6], (LongItemView) objArr[8], (LongItemView) objArr[5], (LongItemView) objArr[2], (RelativeLayout) objArr[0]);
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
