package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityCountrySupportedBinding extends ViewDataBinding {

    @NonNull
    public final Button btNano;

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final RelativeLayout layoutMain;

    protected ActivityCountrySupportedBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, TitleView titleView, RelativeLayout relativeLayout) {
        super(dataBindingComponent, view2, i);
        this.btNano = button;
        this.flTitlebar = titleView;
        this.layoutMain = relativeLayout;
    }

    @NonNull
    public static ActivityCountrySupportedBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityCountrySupportedBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityCountrySupportedBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_country_supported, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityCountrySupportedBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityCountrySupportedBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityCountrySupportedBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_country_supported, null, false, dataBindingComponent);
    }

    public static ActivityCountrySupportedBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityCountrySupportedBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityCountrySupportedBinding) bind(dataBindingComponent, view2, R.layout.activity_country_supported);
    }
}
