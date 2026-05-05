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
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.LongItemView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityPowerModeSettingsBinding extends ViewDataBinding {

    @NonNull
    public final LongItemView itLowPowerSwitch;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @NonNull
    public final TextView tvLowPowerTips;

    protected ActivityPowerModeSettingsBinding(DataBindingComponent dataBindingComponent, View view2, int i, LongItemView longItemView, LinearLayout linearLayout, ImageView imageView, RelativeLayout relativeLayout, TextView textView) {
        super(dataBindingComponent, view2, i);
        this.itLowPowerSwitch = longItemView;
        this.layoutMain = linearLayout;
        this.leftImg = imageView;
        this.leftRl = relativeLayout;
        this.tvLowPowerTips = textView;
    }

    @NonNull
    public static ActivityPowerModeSettingsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityPowerModeSettingsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityPowerModeSettingsBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_power_mode_settings, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityPowerModeSettingsBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityPowerModeSettingsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityPowerModeSettingsBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_power_mode_settings, null, false, dataBindingComponent);
    }

    public static ActivityPowerModeSettingsBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityPowerModeSettingsBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityPowerModeSettingsBinding) bind(dataBindingComponent, view2, R.layout.activity_power_mode_settings);
    }
}
