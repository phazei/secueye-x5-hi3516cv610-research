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
import com.seculink.app.R;
import view.ItemView;
import view.LongItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityMotionDetectBinding extends ViewDataBinding {

    @NonNull
    public final TextView alarmText;

    @NonNull
    public final LongItemView deviceAutoLocate;

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final ItemView itemAlarmMode;

    @NonNull
    public final ItemView itemAlarmTime;

    @NonNull
    public final LongItemView itemMobilePush;

    @NonNull
    public final LongItemView itemMobilePush2;

    @NonNull
    public final ItemView itemMobileStrongPush;

    @NonNull
    public final LongItemView kuaXian;

    @NonNull
    public final LongItemView kuaXianSet;

    @NonNull
    public final LinearLayout layoutBullet;

    @NonNull
    public final LinearLayout layoutBulletShow;

    @NonNull
    public final LinearLayout layoutKuaXian;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final LinearLayout layoutPtz;

    @NonNull
    public final LinearLayout layoutPtzShow;

    @NonNull
    public final LinearLayout layoutQuYu;

    @NonNull
    public final LinearLayout layoutSwitch;

    @NonNull
    public final ItemView lianDong;

    @NonNull
    public final LongItemView quYu;

    @NonNull
    public final LongItemView quYuSet;

    @NonNull
    public final LongItemView renXingZhenCe;

    @NonNull
    public final ItemView renXingZhenCeBullet;

    @NonNull
    public final LongItemView renXingZhuiZong;

    @NonNull
    public final TextView tvPlaybackGun;

    @NonNull
    public final LongItemView yiDongZhenCeLingMingDu;

    @NonNull
    public final ItemView yiDongZhenCeLingMingDuBullet;

    @NonNull
    public final LongItemView yiDongZhuiZong;

    protected ActivityMotionDetectBinding(DataBindingComponent dataBindingComponent, View view2, int i, TextView textView, LongItemView longItemView, TitleView titleView, ItemView itemView, ItemView itemView2, LongItemView longItemView2, LongItemView longItemView3, ItemView itemView3, LongItemView longItemView4, LongItemView longItemView5, LinearLayout linearLayout, LinearLayout linearLayout2, LinearLayout linearLayout3, LinearLayout linearLayout4, LinearLayout linearLayout5, LinearLayout linearLayout6, LinearLayout linearLayout7, LinearLayout linearLayout8, ItemView itemView4, LongItemView longItemView6, LongItemView longItemView7, LongItemView longItemView8, ItemView itemView5, LongItemView longItemView9, TextView textView2, LongItemView longItemView10, ItemView itemView6, LongItemView longItemView11) {
        super(dataBindingComponent, view2, i);
        this.alarmText = textView;
        this.deviceAutoLocate = longItemView;
        this.flTitlebar = titleView;
        this.itemAlarmMode = itemView;
        this.itemAlarmTime = itemView2;
        this.itemMobilePush = longItemView2;
        this.itemMobilePush2 = longItemView3;
        this.itemMobileStrongPush = itemView3;
        this.kuaXian = longItemView4;
        this.kuaXianSet = longItemView5;
        this.layoutBullet = linearLayout;
        this.layoutBulletShow = linearLayout2;
        this.layoutKuaXian = linearLayout3;
        this.layoutMain = linearLayout4;
        this.layoutPtz = linearLayout5;
        this.layoutPtzShow = linearLayout6;
        this.layoutQuYu = linearLayout7;
        this.layoutSwitch = linearLayout8;
        this.lianDong = itemView4;
        this.quYu = longItemView6;
        this.quYuSet = longItemView7;
        this.renXingZhenCe = longItemView8;
        this.renXingZhenCeBullet = itemView5;
        this.renXingZhuiZong = longItemView9;
        this.tvPlaybackGun = textView2;
        this.yiDongZhenCeLingMingDu = longItemView10;
        this.yiDongZhenCeLingMingDuBullet = itemView6;
        this.yiDongZhuiZong = longItemView11;
    }

    @NonNull
    public static ActivityMotionDetectBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityMotionDetectBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityMotionDetectBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_motion_detect, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityMotionDetectBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityMotionDetectBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityMotionDetectBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_motion_detect, null, false, dataBindingComponent);
    }

    public static ActivityMotionDetectBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityMotionDetectBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityMotionDetectBinding) bind(dataBindingComponent, view2, R.layout.activity_motion_detect);
    }
}
