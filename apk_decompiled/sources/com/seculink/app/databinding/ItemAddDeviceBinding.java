package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import bean.AddDeviceModel;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ItemAddDeviceBinding extends ViewDataBinding {

    @NonNull
    public final ImageView ivType;

    @NonNull
    public final LinearLayout layoutItem;

    @Bindable
    protected AddDeviceModel mModel;

    public abstract void setModel(@Nullable AddDeviceModel addDeviceModel);

    protected ItemAddDeviceBinding(DataBindingComponent dataBindingComponent, View view2, int i, ImageView imageView, LinearLayout linearLayout) {
        super(dataBindingComponent, view2, i);
        this.ivType = imageView;
        this.layoutItem = linearLayout;
    }

    @Nullable
    public AddDeviceModel getModel() {
        return this.mModel;
    }

    @NonNull
    public static ItemAddDeviceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemAddDeviceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemAddDeviceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_add_device, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ItemAddDeviceBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemAddDeviceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemAddDeviceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_add_device, null, false, dataBindingComponent);
    }

    public static ItemAddDeviceBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ItemAddDeviceBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemAddDeviceBinding) bind(dataBindingComponent, view2, R.layout.item_add_device);
    }
}
