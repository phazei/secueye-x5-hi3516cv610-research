package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.SettingTitleView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityPushHelpBinding extends ViewDataBinding {

    @NonNull
    public final SettingTitleView alert;

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final LinearLayout layoutHuawei;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final LinearLayout layoutMi;

    @NonNull
    public final LinearLayout layoutOppo;

    @NonNull
    public final LinearLayout layoutVivo;

    protected ActivityPushHelpBinding(DataBindingComponent dataBindingComponent, View view2, int i, SettingTitleView settingTitleView, TitleView titleView, LinearLayout linearLayout, LinearLayout linearLayout2, LinearLayout linearLayout3, LinearLayout linearLayout4, LinearLayout linearLayout5) {
        super(dataBindingComponent, view2, i);
        this.alert = settingTitleView;
        this.flTitlebar = titleView;
        this.layoutHuawei = linearLayout;
        this.layoutMain = linearLayout2;
        this.layoutMi = linearLayout3;
        this.layoutOppo = linearLayout4;
        this.layoutVivo = linearLayout5;
    }

    @NonNull
    public static ActivityPushHelpBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityPushHelpBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityPushHelpBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_push_help, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityPushHelpBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityPushHelpBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityPushHelpBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_push_help, null, false, dataBindingComponent);
    }

    public static ActivityPushHelpBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityPushHelpBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityPushHelpBinding) bind(dataBindingComponent, view2, R.layout.activity_push_help);
    }
}
