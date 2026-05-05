package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.LongItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityNet4gSwitchBinding extends ViewDataBinding {

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final LongItemView item4gSwitch;

    @NonNull
    public final ImageView ivExternalOperator;

    @NonNull
    public final ImageView ivOfficialOperator;

    @NonNull
    public final LinearLayout layoutBottom;

    @NonNull
    public final LinearLayout layoutCustomer;

    @NonNull
    public final LinearLayout layoutExternal;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final LinearLayout layoutOfficial;

    @NonNull
    public final LinearLayout layoutWechat;

    @NonNull
    public final LinearLayout simExternal;

    @NonNull
    public final LinearLayout simOfficial;

    @NonNull
    public final TextView tvExternalIccid;

    @NonNull
    public final TextView tvExternalOperator;

    @NonNull
    public final TextView tvExternalSelect;

    @NonNull
    public final TextView tvExternalState;

    @NonNull
    public final TextView tvOfficialIccid;

    @NonNull
    public final TextView tvOfficialOperator;

    @NonNull
    public final TextView tvOfficialSelect;

    @NonNull
    public final TextView tvOfficialState;

    protected ActivityNet4gSwitchBinding(DataBindingComponent dataBindingComponent, View view2, int i, TitleView titleView, LongItemView longItemView, ImageView imageView, ImageView imageView2, LinearLayout linearLayout, LinearLayout linearLayout2, LinearLayout linearLayout3, LinearLayout linearLayout4, LinearLayout linearLayout5, LinearLayout linearLayout6, LinearLayout linearLayout7, LinearLayout linearLayout8, TextView textView, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7, TextView textView8) {
        super(dataBindingComponent, view2, i);
        this.flTitlebar = titleView;
        this.item4gSwitch = longItemView;
        this.ivExternalOperator = imageView;
        this.ivOfficialOperator = imageView2;
        this.layoutBottom = linearLayout;
        this.layoutCustomer = linearLayout2;
        this.layoutExternal = linearLayout3;
        this.layoutMain = linearLayout4;
        this.layoutOfficial = linearLayout5;
        this.layoutWechat = linearLayout6;
        this.simExternal = linearLayout7;
        this.simOfficial = linearLayout8;
        this.tvExternalIccid = textView;
        this.tvExternalOperator = textView2;
        this.tvExternalSelect = textView3;
        this.tvExternalState = textView4;
        this.tvOfficialIccid = textView5;
        this.tvOfficialOperator = textView6;
        this.tvOfficialSelect = textView7;
        this.tvOfficialState = textView8;
    }

    @NonNull
    public static ActivityNet4gSwitchBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityNet4gSwitchBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNet4gSwitchBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_net_4g_switch, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityNet4gSwitchBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityNet4gSwitchBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNet4gSwitchBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_net_4g_switch, null, false, dataBindingComponent);
    }

    public static ActivityNet4gSwitchBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityNet4gSwitchBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNet4gSwitchBinding) bind(dataBindingComponent, view2, R.layout.activity_net_4g_switch);
    }
}
