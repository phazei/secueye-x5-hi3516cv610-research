package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.ItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityAdBinding extends ViewDataBinding {

    @NonNull
    public final Button btGoMap;

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final ItemView itemAd;

    @NonNull
    public final LinearLayout layoutMain;

    protected ActivityAdBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, TitleView titleView, ItemView itemView, LinearLayout linearLayout) {
        super(dataBindingComponent, view2, i);
        this.btGoMap = button;
        this.flTitlebar = titleView;
        this.itemAd = itemView;
        this.layoutMain = linearLayout;
    }

    @NonNull
    public static ActivityAdBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityAdBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityAdBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ad, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityAdBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityAdBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityAdBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ad, null, false, dataBindingComponent);
    }

    public static ActivityAdBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityAdBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityAdBinding) bind(dataBindingComponent, view2, R.layout.activity_ad);
    }
}
