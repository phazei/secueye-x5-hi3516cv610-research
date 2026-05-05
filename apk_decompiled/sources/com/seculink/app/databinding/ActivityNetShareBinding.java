package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public abstract class ActivityNetShareBinding extends ViewDataBinding {

    @NonNull
    public final Button btSave;

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final LongItemView itemApHotspot;

    @NonNull
    public final LongItemView itemNatEthSwitch;

    @NonNull
    public final LongItemView itemNatWlanSwitch;

    @NonNull
    public final LinearLayout layoutAp;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final LinearLayout layoutNet;

    @NonNull
    public final TextView yiDongZhenCeText;

    protected ActivityNetShareBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, TitleView titleView, LongItemView longItemView, LongItemView longItemView2, LongItemView longItemView3, LinearLayout linearLayout, LinearLayout linearLayout2, LinearLayout linearLayout3, TextView textView) {
        super(dataBindingComponent, view2, i);
        this.btSave = button;
        this.flTitlebar = titleView;
        this.itemApHotspot = longItemView;
        this.itemNatEthSwitch = longItemView2;
        this.itemNatWlanSwitch = longItemView3;
        this.layoutAp = linearLayout;
        this.layoutMain = linearLayout2;
        this.layoutNet = linearLayout3;
        this.yiDongZhenCeText = textView;
    }

    @NonNull
    public static ActivityNetShareBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityNetShareBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNetShareBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_net_share, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityNetShareBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityNetShareBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNetShareBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_net_share, null, false, dataBindingComponent);
    }

    public static ActivityNetShareBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityNetShareBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNetShareBinding) bind(dataBindingComponent, view2, R.layout.activity_net_share);
    }
}
