package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
public abstract class ActivityBleRouterSettingBinding extends ViewDataBinding {

    @NonNull
    public final Button btRemoveCamera;

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final ItemView itemApn;

    @NonNull
    public final LongItemView itemCameraInfo;

    @NonNull
    public final ItemView itemConnectedDevice;

    @NonNull
    public final LongItemView itemFirmwareVersion;

    @NonNull
    public final ItemView itemHotspot;

    @NonNull
    public final ItemView itemLan;

    @NonNull
    public final ItemView itemResetSetting;

    @NonNull
    public final ItemView itemRestartDev;

    @NonNull
    public final ItemView itemShareManage;

    @NonNull
    public final ItemView itemTimeSetting;

    @NonNull
    public final LinearLayout layoutMain;

    protected ActivityBleRouterSettingBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, TitleView titleView, ItemView itemView, LongItemView longItemView, ItemView itemView2, LongItemView longItemView2, ItemView itemView3, ItemView itemView4, ItemView itemView5, ItemView itemView6, ItemView itemView7, ItemView itemView8, LinearLayout linearLayout) {
        super(dataBindingComponent, view2, i);
        this.btRemoveCamera = button;
        this.flTitlebar = titleView;
        this.itemApn = itemView;
        this.itemCameraInfo = longItemView;
        this.itemConnectedDevice = itemView2;
        this.itemFirmwareVersion = longItemView2;
        this.itemHotspot = itemView3;
        this.itemLan = itemView4;
        this.itemResetSetting = itemView5;
        this.itemRestartDev = itemView6;
        this.itemShareManage = itemView7;
        this.itemTimeSetting = itemView8;
        this.layoutMain = linearLayout;
    }

    @NonNull
    public static ActivityBleRouterSettingBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityBleRouterSettingBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleRouterSettingBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ble_router_setting, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityBleRouterSettingBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityBleRouterSettingBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleRouterSettingBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ble_router_setting, null, false, dataBindingComponent);
    }

    public static ActivityBleRouterSettingBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityBleRouterSettingBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleRouterSettingBinding) bind(dataBindingComponent, view2, R.layout.activity_ble_router_setting);
    }
}
