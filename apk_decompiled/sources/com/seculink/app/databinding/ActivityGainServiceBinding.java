package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityGainServiceBinding extends ViewDataBinding {

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    protected ActivityGainServiceBinding(DataBindingComponent dataBindingComponent, View view2, int i, ImageView imageView, RelativeLayout relativeLayout) {
        super(dataBindingComponent, view2, i);
        this.leftImg = imageView;
        this.leftRl = relativeLayout;
    }

    @NonNull
    public static ActivityGainServiceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityGainServiceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityGainServiceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_gain_service, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityGainServiceBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityGainServiceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityGainServiceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_gain_service, null, false, dataBindingComponent);
    }

    public static ActivityGainServiceBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityGainServiceBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityGainServiceBinding) bind(dataBindingComponent, view2, R.layout.activity_gain_service);
    }
}
