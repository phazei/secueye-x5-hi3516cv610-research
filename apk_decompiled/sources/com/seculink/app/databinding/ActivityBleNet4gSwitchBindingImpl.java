package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
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
public class ActivityBleNet4gSwitchBindingImpl extends ActivityBleNet4gSwitchBinding {

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
        sViewsWithIds.put(R.id.item_4g_switch, 2);
        sViewsWithIds.put(R.id.sim_official, 3);
        sViewsWithIds.put(R.id.tv_official_iccid, 4);
        sViewsWithIds.put(R.id.layout_official, 5);
        sViewsWithIds.put(R.id.iv_official_operator, 6);
        sViewsWithIds.put(R.id.tv_official_operator, 7);
        sViewsWithIds.put(R.id.tv_official_state, 8);
        sViewsWithIds.put(R.id.tv_official_select, 9);
        sViewsWithIds.put(R.id.sim_external, 10);
        sViewsWithIds.put(R.id.tv_external_iccid, 11);
        sViewsWithIds.put(R.id.layout_external, 12);
        sViewsWithIds.put(R.id.iv_external_operator, 13);
        sViewsWithIds.put(R.id.tv_external_operator, 14);
        sViewsWithIds.put(R.id.tv_external_state, 15);
        sViewsWithIds.put(R.id.tv_external_select, 16);
        sViewsWithIds.put(R.id.layout_customer, 17);
        sViewsWithIds.put(R.id.layout_wechat, 18);
    }

    public ActivityBleNet4gSwitchBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 19, sIncludes, sViewsWithIds));
    }

    private ActivityBleNet4gSwitchBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (TitleView) objArr[1], (LongItemView) objArr[2], (ImageView) objArr[13], (ImageView) objArr[6], (LinearLayout) objArr[17], (LinearLayout) objArr[12], (LinearLayout) objArr[0], (LinearLayout) objArr[5], (LinearLayout) objArr[18], (LinearLayout) objArr[10], (LinearLayout) objArr[3], (TextView) objArr[11], (TextView) objArr[14], (TextView) objArr[16], (TextView) objArr[15], (TextView) objArr[4], (TextView) objArr[7], (TextView) objArr[9], (TextView) objArr[8]);
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
