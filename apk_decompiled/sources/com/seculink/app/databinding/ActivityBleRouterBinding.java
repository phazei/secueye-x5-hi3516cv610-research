package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityBleRouterBinding extends ViewDataBinding {

    @NonNull
    public final TextView dhcpEndAddress;

    @NonNull
    public final TextView dhcpStartAddress;

    @NonNull
    public final ImageView ivBack;

    @NonNull
    public final ImageView ivBattery;

    @NonNull
    public final ImageView ivBatteryCharger;

    @NonNull
    public final ImageView ivIccid;

    @NonNull
    public final ImageView ivLine;

    @NonNull
    public final ImageView ivVsimCard;

    @NonNull
    public final RelativeLayout layoutBattery;

    @NonNull
    public final RelativeLayout layoutConnect;

    @NonNull
    public final RelativeLayout layoutIccid;

    @NonNull
    public final RelativeLayout layoutImei;

    @NonNull
    public final RelativeLayout layoutLan;

    @NonNull
    public final RelativeLayout layoutMain;

    @NonNull
    public final RelativeLayout layoutSetting;

    @NonNull
    public final RelativeLayout layoutTop;

    @NonNull
    public final RelativeLayout layoutWifi;

    @NonNull
    public final TextView tvBattery;

    @NonNull
    public final TextView tvBatteryVoltage;

    @NonNull
    public final TextView tvCard;

    @NonNull
    public final TextView tvHotspotSize;

    @NonNull
    public final TextView tvIccid;

    @NonNull
    public final TextView tvImei;

    @NonNull
    public final TextView tvLanSize;

    @NonNull
    public final TextView tvLine;

    @NonNull
    public final TextView tvSim;

    @NonNull
    public final TextView tvTitle;

    @NonNull
    public final TextView tvWifiName;

    @NonNull
    public final TextView tvWifiPass;

    protected ActivityBleRouterBinding(DataBindingComponent dataBindingComponent, View view2, int i, TextView textView, TextView textView2, ImageView imageView, ImageView imageView2, ImageView imageView3, ImageView imageView4, ImageView imageView5, ImageView imageView6, RelativeLayout relativeLayout, RelativeLayout relativeLayout2, RelativeLayout relativeLayout3, RelativeLayout relativeLayout4, RelativeLayout relativeLayout5, RelativeLayout relativeLayout6, RelativeLayout relativeLayout7, RelativeLayout relativeLayout8, RelativeLayout relativeLayout9, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7, TextView textView8, TextView textView9, TextView textView10, TextView textView11, TextView textView12, TextView textView13, TextView textView14) {
        super(dataBindingComponent, view2, i);
        this.dhcpEndAddress = textView;
        this.dhcpStartAddress = textView2;
        this.ivBack = imageView;
        this.ivBattery = imageView2;
        this.ivBatteryCharger = imageView3;
        this.ivIccid = imageView4;
        this.ivLine = imageView5;
        this.ivVsimCard = imageView6;
        this.layoutBattery = relativeLayout;
        this.layoutConnect = relativeLayout2;
        this.layoutIccid = relativeLayout3;
        this.layoutImei = relativeLayout4;
        this.layoutLan = relativeLayout5;
        this.layoutMain = relativeLayout6;
        this.layoutSetting = relativeLayout7;
        this.layoutTop = relativeLayout8;
        this.layoutWifi = relativeLayout9;
        this.tvBattery = textView3;
        this.tvBatteryVoltage = textView4;
        this.tvCard = textView5;
        this.tvHotspotSize = textView6;
        this.tvIccid = textView7;
        this.tvImei = textView8;
        this.tvLanSize = textView9;
        this.tvLine = textView10;
        this.tvSim = textView11;
        this.tvTitle = textView12;
        this.tvWifiName = textView13;
        this.tvWifiPass = textView14;
    }

    @NonNull
    public static ActivityBleRouterBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityBleRouterBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleRouterBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ble_router, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityBleRouterBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityBleRouterBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleRouterBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ble_router, null, false, dataBindingComponent);
    }

    public static ActivityBleRouterBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityBleRouterBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleRouterBinding) bind(dataBindingComponent, view2, R.layout.activity_ble_router);
    }
}
