package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.LongItemView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityLowPowerSettingsBinding extends ViewDataBinding {

    @NonNull
    public final LongItemView itIvp;

    @NonNull
    public final LongItemView itLowPowerDiy;

    @NonNull
    public final LongItemView itLowPowerDiy2;

    @NonNull
    public final LongItemView itLowPowerFixedTime;

    @NonNull
    public final LongItemView itLowPowerPir;

    @NonNull
    public final LongItemView itLowPowerRecord;

    @NonNull
    public final LongItemView itLowPowerSwitch;

    @NonNull
    public final LongItemView itLowPowerWake;

    @NonNull
    public final LongItemView itemSleepSwitch;

    @NonNull
    public final LinearLayout layoutApp;

    @NonNull
    public final LinearLayout layoutDiy;

    @NonNull
    public final LinearLayout layoutLowPowerFixedTime;

    @NonNull
    public final LinearLayout layoutLowPowerRecord;

    @NonNull
    public final LinearLayout layoutLowPowerWake;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final LinearLayout layoutSleepSwitch;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @NonNull
    public final AppCompatSeekBar seekLowPowerFixedTime;

    @NonNull
    public final AppCompatSeekBar seekLowPowerRecord;

    @NonNull
    public final AppCompatSeekBar seekLowPowerWake;

    @NonNull
    public final TextView tvLowPowerDiyTips;

    @NonNull
    public final TextView tvLowPowerTips;

    protected ActivityLowPowerSettingsBinding(DataBindingComponent dataBindingComponent, View view2, int i, LongItemView longItemView, LongItemView longItemView2, LongItemView longItemView3, LongItemView longItemView4, LongItemView longItemView5, LongItemView longItemView6, LongItemView longItemView7, LongItemView longItemView8, LongItemView longItemView9, LinearLayout linearLayout, LinearLayout linearLayout2, LinearLayout linearLayout3, LinearLayout linearLayout4, LinearLayout linearLayout5, LinearLayout linearLayout6, LinearLayout linearLayout7, ImageView imageView, RelativeLayout relativeLayout, AppCompatSeekBar appCompatSeekBar, AppCompatSeekBar appCompatSeekBar2, AppCompatSeekBar appCompatSeekBar3, TextView textView, TextView textView2) {
        super(dataBindingComponent, view2, i);
        this.itIvp = longItemView;
        this.itLowPowerDiy = longItemView2;
        this.itLowPowerDiy2 = longItemView3;
        this.itLowPowerFixedTime = longItemView4;
        this.itLowPowerPir = longItemView5;
        this.itLowPowerRecord = longItemView6;
        this.itLowPowerSwitch = longItemView7;
        this.itLowPowerWake = longItemView8;
        this.itemSleepSwitch = longItemView9;
        this.layoutApp = linearLayout;
        this.layoutDiy = linearLayout2;
        this.layoutLowPowerFixedTime = linearLayout3;
        this.layoutLowPowerRecord = linearLayout4;
        this.layoutLowPowerWake = linearLayout5;
        this.layoutMain = linearLayout6;
        this.layoutSleepSwitch = linearLayout7;
        this.leftImg = imageView;
        this.leftRl = relativeLayout;
        this.seekLowPowerFixedTime = appCompatSeekBar;
        this.seekLowPowerRecord = appCompatSeekBar2;
        this.seekLowPowerWake = appCompatSeekBar3;
        this.tvLowPowerDiyTips = textView;
        this.tvLowPowerTips = textView2;
    }

    @NonNull
    public static ActivityLowPowerSettingsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityLowPowerSettingsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityLowPowerSettingsBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_low_power_settings, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityLowPowerSettingsBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityLowPowerSettingsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityLowPowerSettingsBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_low_power_settings, null, false, dataBindingComponent);
    }

    public static ActivityLowPowerSettingsBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityLowPowerSettingsBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityLowPowerSettingsBinding) bind(dataBindingComponent, view2, R.layout.activity_low_power_settings);
    }
}
