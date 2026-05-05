package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityBleRouterLanDhcpBinding extends ViewDataBinding {

    @NonNull
    public final Button btFeedback;

    @NonNull
    public final TitleView flTitlebar;

    protected ActivityBleRouterLanDhcpBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, TitleView titleView) {
        super(dataBindingComponent, view2, i);
        this.btFeedback = button;
        this.flTitlebar = titleView;
    }

    @NonNull
    public static ActivityBleRouterLanDhcpBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityBleRouterLanDhcpBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleRouterLanDhcpBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ble_router_lan_dhcp, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityBleRouterLanDhcpBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityBleRouterLanDhcpBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleRouterLanDhcpBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ble_router_lan_dhcp, null, false, dataBindingComponent);
    }

    public static ActivityBleRouterLanDhcpBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityBleRouterLanDhcpBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleRouterLanDhcpBinding) bind(dataBindingComponent, view2, R.layout.activity_ble_router_lan_dhcp);
    }
}
