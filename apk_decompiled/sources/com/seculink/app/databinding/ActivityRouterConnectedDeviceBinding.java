package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityRouterConnectedDeviceBinding extends ViewDataBinding {

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final RecyclerView rvList;

    @NonNull
    public final TextView tvNoData;

    protected ActivityRouterConnectedDeviceBinding(DataBindingComponent dataBindingComponent, View view2, int i, TitleView titleView, LinearLayout linearLayout, RecyclerView recyclerView, TextView textView) {
        super(dataBindingComponent, view2, i);
        this.flTitlebar = titleView;
        this.layoutMain = linearLayout;
        this.rvList = recyclerView;
        this.tvNoData = textView;
    }

    @NonNull
    public static ActivityRouterConnectedDeviceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityRouterConnectedDeviceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityRouterConnectedDeviceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_router_connected_device, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityRouterConnectedDeviceBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityRouterConnectedDeviceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityRouterConnectedDeviceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_router_connected_device, null, false, dataBindingComponent);
    }

    public static ActivityRouterConnectedDeviceBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityRouterConnectedDeviceBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityRouterConnectedDeviceBinding) bind(dataBindingComponent, view2, R.layout.activity_router_connected_device);
    }
}
