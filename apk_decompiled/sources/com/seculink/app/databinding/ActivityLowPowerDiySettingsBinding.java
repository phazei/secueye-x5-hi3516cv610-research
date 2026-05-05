package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.LongItemView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityLowPowerDiySettingsBinding extends ViewDataBinding {

    @NonNull
    public final LongItemView itIvp;

    @NonNull
    public final LongItemView itLowPowerDiy;

    @NonNull
    public final LongItemView itLowPowerFixedTime;

    @NonNull
    public final LongItemView itLowPowerPir;

    @NonNull
    public final LongItemView itLowPowerRecord;

    @NonNull
    public final LongItemView itLowPowerWake;

    @NonNull
    public final LinearLayout layoutApp;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    protected ActivityLowPowerDiySettingsBinding(DataBindingComponent dataBindingComponent, View view2, int i, LongItemView longItemView, LongItemView longItemView2, LongItemView longItemView3, LongItemView longItemView4, LongItemView longItemView5, LongItemView longItemView6, LinearLayout linearLayout, LinearLayout linearLayout2, ImageView imageView, RelativeLayout relativeLayout) {
        super(dataBindingComponent, view2, i);
        this.itIvp = longItemView;
        this.itLowPowerDiy = longItemView2;
        this.itLowPowerFixedTime = longItemView3;
        this.itLowPowerPir = longItemView4;
        this.itLowPowerRecord = longItemView5;
        this.itLowPowerWake = longItemView6;
        this.layoutApp = linearLayout;
        this.layoutMain = linearLayout2;
        this.leftImg = imageView;
        this.leftRl = relativeLayout;
    }

    @NonNull
    public static ActivityLowPowerDiySettingsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityLowPowerDiySettingsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityLowPowerDiySettingsBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_low_power_diy_settings, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityLowPowerDiySettingsBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityLowPowerDiySettingsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityLowPowerDiySettingsBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_low_power_diy_settings, null, false, dataBindingComponent);
    }

    public static ActivityLowPowerDiySettingsBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityLowPowerDiySettingsBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityLowPowerDiySettingsBinding) bind(dataBindingComponent, view2, R.layout.activity_low_power_diy_settings);
    }
}
