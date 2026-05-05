package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.LongItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public class ActivityNetShareBindingImpl extends ActivityNetShareBinding {

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
        sViewsWithIds.put(R.id.layout_ap, 2);
        sViewsWithIds.put(R.id.item_nat_wlan_switch, 3);
        sViewsWithIds.put(R.id.item_ap_hotspot, 4);
        sViewsWithIds.put(R.id.yi_dong_zhen_ce_text, 5);
        sViewsWithIds.put(R.id.layout_net, 6);
        sViewsWithIds.put(R.id.item_nat_eth_switch, 7);
        sViewsWithIds.put(R.id.bt_save, 8);
    }

    public ActivityNetShareBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 9, sIncludes, sViewsWithIds));
    }

    private ActivityNetShareBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (Button) objArr[8], (TitleView) objArr[1], (LongItemView) objArr[4], (LongItemView) objArr[7], (LongItemView) objArr[3], (LinearLayout) objArr[2], (LinearLayout) objArr[0], (LinearLayout) objArr[6], (TextView) objArr[5]);
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
