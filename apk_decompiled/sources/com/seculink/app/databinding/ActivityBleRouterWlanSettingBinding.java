package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.ItemView;
import view.LongItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityBleRouterWlanSettingBinding extends ViewDataBinding {

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final LongItemView itemCameraInfo;

    @NonNull
    public final ItemView itemTimeSetting;

    protected ActivityBleRouterWlanSettingBinding(DataBindingComponent dataBindingComponent, View view2, int i, TitleView titleView, LongItemView longItemView, ItemView itemView) {
        super(dataBindingComponent, view2, i);
        this.flTitlebar = titleView;
        this.itemCameraInfo = longItemView;
        this.itemTimeSetting = itemView;
    }

    @NonNull
    public static ActivityBleRouterWlanSettingBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityBleRouterWlanSettingBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleRouterWlanSettingBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ble_router_wlan_setting, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityBleRouterWlanSettingBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityBleRouterWlanSettingBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleRouterWlanSettingBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ble_router_wlan_setting, null, false, dataBindingComponent);
    }

    public static ActivityBleRouterWlanSettingBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityBleRouterWlanSettingBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleRouterWlanSettingBinding) bind(dataBindingComponent, view2, R.layout.activity_ble_router_wlan_setting);
    }
}
