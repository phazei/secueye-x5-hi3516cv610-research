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
public class ActivityRouterLanBindingImpl extends ActivityRouterLanBinding {

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
        sViewsWithIds.put(R.id.dhcp_lease, 3);
        sViewsWithIds.put(R.id.dhcp_start_address, 4);
        sViewsWithIds.put(R.id.dhcp_end_address, 5);
        sViewsWithIds.put(R.id.dhcp_default_router, 6);
        sViewsWithIds.put(R.id.dhcp_sub_net_mask, 7);
        sViewsWithIds.put(R.id.dhcp_first_dns_address, 8);
        sViewsWithIds.put(R.id.dhcp_second_dns_address, 9);
        sViewsWithIds.put(R.id.bt_save, 10);
    }

    public ActivityRouterLanBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 11, sIncludes, sViewsWithIds));
    }

    private ActivityRouterLanBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (Button) objArr[10], (EditText) objArr[6], (EditText) objArr[5], (EditText) objArr[8], (EditText) objArr[3], (EditText) objArr[9], (EditText) objArr[4], (EditText) objArr[7], (TitleView) objArr[1], (LongItemView) objArr[2], (RelativeLayout) objArr[0]);
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
