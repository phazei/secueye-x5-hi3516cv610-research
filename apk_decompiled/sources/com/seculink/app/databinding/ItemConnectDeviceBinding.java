package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import bean.ConnectDeviceInfo;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ItemConnectDeviceBinding extends ViewDataBinding {

    @Bindable
    protected ConnectDeviceInfo mModel;

    public abstract void setModel(@Nullable ConnectDeviceInfo connectDeviceInfo);

    protected ItemConnectDeviceBinding(DataBindingComponent dataBindingComponent, View view2, int i) {
        super(dataBindingComponent, view2, i);
    }

    @Nullable
    public ConnectDeviceInfo getModel() {
        return this.mModel;
    }

    @NonNull
    public static ItemConnectDeviceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemConnectDeviceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemConnectDeviceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_connect_device, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ItemConnectDeviceBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemConnectDeviceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemConnectDeviceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_connect_device, null, false, dataBindingComponent);
    }

    public static ItemConnectDeviceBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ItemConnectDeviceBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemConnectDeviceBinding) bind(dataBindingComponent, view2, R.layout.item_connect_device);
    }
}
