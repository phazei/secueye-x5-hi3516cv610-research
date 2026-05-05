package com.seculink.app.databinding;

import android.util.SparseIntArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.adapters.TextViewBindingAdapter;
import bean.ConnectDeviceInfo;

/* JADX INFO: loaded from: classes3.dex */
public class ItemConnectDeviceBindingImpl extends ItemConnectDeviceBinding {

    @Nullable
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;

    @Nullable
    private static final SparseIntArray sViewsWithIds = null;
    private long mDirtyFlags;

    @NonNull
    private final LinearLayout mboundView0;

    @NonNull
    private final TextView mboundView1;

    @NonNull
    private final TextView mboundView2;

    @NonNull
    private final TextView mboundView3;

    @NonNull
    private final TextView mboundView4;

    @Override // androidx.databinding.ViewDataBinding
    protected boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    public ItemConnectDeviceBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 5, sIncludes, sViewsWithIds));
    }

    private ItemConnectDeviceBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0);
        this.mDirtyFlags = -1L;
        this.mboundView0 = (LinearLayout) objArr[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (TextView) objArr[1];
        this.mboundView1.setTag(null);
        this.mboundView2 = (TextView) objArr[2];
        this.mboundView2.setTag(null);
        this.mboundView3 = (TextView) objArr[3];
        this.mboundView3.setTag(null);
        this.mboundView4 = (TextView) objArr[4];
        this.mboundView4.setTag(null);
        setRootTag(view2);
        invalidateAll();
    }

    @Override // androidx.databinding.ViewDataBinding
    public void invalidateAll() {
        synchronized (this) {
            this.mDirtyFlags = 2L;
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
    public boolean setVariable(int i, @Nullable Object obj) {
        if (2 != i) {
            return false;
        }
        setModel((ConnectDeviceInfo) obj);
        return true;
    }

    @Override // com.seculink.app.databinding.ItemConnectDeviceBinding
    public void setModel(@Nullable ConnectDeviceInfo connectDeviceInfo) {
        this.mModel = connectDeviceInfo;
        synchronized (this) {
            this.mDirtyFlags |= 1;
        }
        notifyPropertyChanged(2);
        super.requestRebind();
    }

    @Override // androidx.databinding.ViewDataBinding
    protected void executeBindings() {
        long j;
        String str;
        String str2;
        String str3;
        synchronized (this) {
            j = this.mDirtyFlags;
            this.mDirtyFlags = 0L;
        }
        ConnectDeviceInfo connectDeviceInfo = this.mModel;
        long j2 = j & 3;
        String str4 = null;
        if (j2 == 0 || connectDeviceInfo == null) {
            str = null;
            str2 = null;
            str3 = null;
        } else {
            str4 = connectDeviceInfo.IP;
            str = connectDeviceInfo.bOnline;
            str2 = connectDeviceInfo.MAC;
            str3 = connectDeviceInfo.AccessPoint;
        }
        if (j2 != 0) {
            TextViewBindingAdapter.setText(this.mboundView1, str4);
            TextViewBindingAdapter.setText(this.mboundView2, str2);
            TextViewBindingAdapter.setText(this.mboundView3, str3);
            TextViewBindingAdapter.setText(this.mboundView4, str);
        }
    }
}
