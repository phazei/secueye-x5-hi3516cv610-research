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
import androidx.databinding.adapters.TextViewBindingAdapter;
import bean.AddDeviceModel;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ItemAddDeviceBindingImpl extends ItemAddDeviceBinding {

    @Nullable
    private static final ViewDataBinding.IncludedLayouts sIncludes = null;

    @Nullable
    private static final SparseIntArray sViewsWithIds = new SparseIntArray();
    private long mDirtyFlags;

    @NonNull
    private final TextView mboundView1;

    @NonNull
    private final TextView mboundView2;

    @Override // androidx.databinding.ViewDataBinding
    protected boolean onFieldChange(int i, Object obj, int i2) {
        return false;
    }

    static {
        sViewsWithIds.put(R.id.iv_type, 3);
    }

    public ItemAddDeviceBindingImpl(@Nullable DataBindingComponent dataBindingComponent, @NonNull View view2) {
        this(dataBindingComponent, view2, mapBindings(dataBindingComponent, view2, 4, sIncludes, sViewsWithIds));
    }

    private ItemAddDeviceBindingImpl(DataBindingComponent dataBindingComponent, View view2, Object[] objArr) {
        super(dataBindingComponent, view2, 0, (ImageView) objArr[3], (LinearLayout) objArr[0]);
        this.mDirtyFlags = -1L;
        this.layoutItem.setTag(null);
        this.mboundView1 = (TextView) objArr[1];
        this.mboundView1.setTag(null);
        this.mboundView2 = (TextView) objArr[2];
        this.mboundView2.setTag(null);
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
        setModel((AddDeviceModel) obj);
        return true;
    }

    @Override // com.seculink.app.databinding.ItemAddDeviceBinding
    public void setModel(@Nullable AddDeviceModel addDeviceModel) {
        this.mModel = addDeviceModel;
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
        synchronized (this) {
            j = this.mDirtyFlags;
            this.mDirtyFlags = 0L;
        }
        AddDeviceModel addDeviceModel = this.mModel;
        long j2 = j & 3;
        String str2 = null;
        if (j2 == 0 || addDeviceModel == null) {
            str = null;
        } else {
            str2 = addDeviceModel.name;
            str = addDeviceModel.tips;
        }
        if (j2 != 0) {
            TextViewBindingAdapter.setText(this.mboundView1, str2);
            TextViewBindingAdapter.setText(this.mboundView2, str);
        }
    }
}
