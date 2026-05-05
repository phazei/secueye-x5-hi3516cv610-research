package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.ItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityGunballSettingBinding extends ViewDataBinding {

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final ItemView itLinkage;

    @NonNull
    public final ItemView itLinkageTrack;

    @NonNull
    public final ItemView itemMotionDetection;

    @NonNull
    public final ConstraintLayout layoutMain;

    @NonNull
    public final View line;

    @Bindable
    protected int mNvrOwner;

    @NonNull
    public final ItemView osdFace;

    @NonNull
    public final ItemView osdNightMode;

    @NonNull
    public final ItemView osdRecordMode;

    @NonNull
    public final ItemView osdRecordVideoQuality;

    @NonNull
    public final View view2;

    @NonNull
    public final View view3;

    public abstract void setNvrOwner(int i);

    protected ActivityGunballSettingBinding(DataBindingComponent dataBindingComponent, View view2, int i, TitleView titleView, ItemView itemView, ItemView itemView2, ItemView itemView3, ConstraintLayout constraintLayout, View view3, ItemView itemView4, ItemView itemView5, ItemView itemView6, ItemView itemView7, View view4, View view5) {
        super(dataBindingComponent, view2, i);
        this.flTitlebar = titleView;
        this.itLinkage = itemView;
        this.itLinkageTrack = itemView2;
        this.itemMotionDetection = itemView3;
        this.layoutMain = constraintLayout;
        this.line = view3;
        this.osdFace = itemView4;
        this.osdNightMode = itemView5;
        this.osdRecordMode = itemView6;
        this.osdRecordVideoQuality = itemView7;
        this.view2 = view4;
        this.view3 = view5;
    }

    public int getNvrOwner() {
        return this.mNvrOwner;
    }

    @NonNull
    public static ActivityGunballSettingBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityGunballSettingBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityGunballSettingBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_gunball_setting, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityGunballSettingBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityGunballSettingBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityGunballSettingBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_gunball_setting, null, false, dataBindingComponent);
    }

    public static ActivityGunballSettingBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityGunballSettingBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityGunballSettingBinding) bind(dataBindingComponent, view2, R.layout.activity_gunball_setting);
    }
}
