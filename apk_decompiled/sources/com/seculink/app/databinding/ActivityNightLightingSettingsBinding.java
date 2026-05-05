package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.LongItemView;
import view.SettingTitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityNightLightingSettingsBinding extends ViewDataBinding {

    @NonNull
    public final SettingTitleView Safety;

    @NonNull
    public final SettingTitleView SafetyGun;

    @NonNull
    public final SettingTitleView floodlightTips;

    @NonNull
    public final AppCompatSeekBar infraredLightProgressSettingGun;

    @NonNull
    public final AppCompatSeekBar infraredLightProgressSettingPtz;

    @NonNull
    public final LinearLayout infraredLightSettingGun;

    @NonNull
    public final LinearLayout infraredLightSettingPtz;

    @NonNull
    public final LongItemView itExpHighLight;

    @NonNull
    public final LongItemView itFloodlightSwitch;

    @NonNull
    public final LongItemView itFloodlightTimeSetting;

    @NonNull
    public final LongItemView itFloodlightTimeSwitch;

    @NonNull
    public final LongItemView itemNightMode;

    @NonNull
    public final LongItemView itemNightModeGun;

    @NonNull
    public final LinearLayout layoutExpHighLight;

    @NonNull
    public final LinearLayout layoutExpHighLightLevel;

    @NonNull
    public final LinearLayout layoutFloodlight;

    @NonNull
    public final LinearLayout layoutGun;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @NonNull
    public final AppCompatSeekBar lightProgressSettingGun;

    @NonNull
    public final AppCompatSeekBar lightProgressSettingPtz;

    @NonNull
    public final LinearLayout lightSettingGun;

    @NonNull
    public final LinearLayout lightSettingPtz;

    @NonNull
    public final AppCompatSeekBar seekBarExpHighLightLevel;

    protected ActivityNightLightingSettingsBinding(DataBindingComponent dataBindingComponent, View view2, int i, SettingTitleView settingTitleView, SettingTitleView settingTitleView2, SettingTitleView settingTitleView3, AppCompatSeekBar appCompatSeekBar, AppCompatSeekBar appCompatSeekBar2, LinearLayout linearLayout, LinearLayout linearLayout2, LongItemView longItemView, LongItemView longItemView2, LongItemView longItemView3, LongItemView longItemView4, LongItemView longItemView5, LongItemView longItemView6, LinearLayout linearLayout3, LinearLayout linearLayout4, LinearLayout linearLayout5, LinearLayout linearLayout6, LinearLayout linearLayout7, ImageView imageView, RelativeLayout relativeLayout, AppCompatSeekBar appCompatSeekBar3, AppCompatSeekBar appCompatSeekBar4, LinearLayout linearLayout8, LinearLayout linearLayout9, AppCompatSeekBar appCompatSeekBar5) {
        super(dataBindingComponent, view2, i);
        this.Safety = settingTitleView;
        this.SafetyGun = settingTitleView2;
        this.floodlightTips = settingTitleView3;
        this.infraredLightProgressSettingGun = appCompatSeekBar;
        this.infraredLightProgressSettingPtz = appCompatSeekBar2;
        this.infraredLightSettingGun = linearLayout;
        this.infraredLightSettingPtz = linearLayout2;
        this.itExpHighLight = longItemView;
        this.itFloodlightSwitch = longItemView2;
        this.itFloodlightTimeSetting = longItemView3;
        this.itFloodlightTimeSwitch = longItemView4;
        this.itemNightMode = longItemView5;
        this.itemNightModeGun = longItemView6;
        this.layoutExpHighLight = linearLayout3;
        this.layoutExpHighLightLevel = linearLayout4;
        this.layoutFloodlight = linearLayout5;
        this.layoutGun = linearLayout6;
        this.layoutMain = linearLayout7;
        this.leftImg = imageView;
        this.leftRl = relativeLayout;
        this.lightProgressSettingGun = appCompatSeekBar3;
        this.lightProgressSettingPtz = appCompatSeekBar4;
        this.lightSettingGun = linearLayout8;
        this.lightSettingPtz = linearLayout9;
        this.seekBarExpHighLightLevel = appCompatSeekBar5;
    }

    @NonNull
    public static ActivityNightLightingSettingsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityNightLightingSettingsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNightLightingSettingsBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_night_lighting_settings, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityNightLightingSettingsBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityNightLightingSettingsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNightLightingSettingsBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_night_lighting_settings, null, false, dataBindingComponent);
    }

    public static ActivityNightLightingSettingsBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityNightLightingSettingsBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNightLightingSettingsBinding) bind(dataBindingComponent, view2, R.layout.activity_night_lighting_settings);
    }
}
