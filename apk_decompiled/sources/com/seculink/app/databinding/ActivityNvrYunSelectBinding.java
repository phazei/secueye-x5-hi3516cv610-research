package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityNvrYunSelectBinding extends ViewDataBinding {

    @NonNull
    public final View gun;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @NonNull
    public final View ptz;

    protected ActivityNvrYunSelectBinding(DataBindingComponent dataBindingComponent, View view2, int i, View view3, LinearLayout linearLayout, ImageView imageView, RelativeLayout relativeLayout, View view4) {
        super(dataBindingComponent, view2, i);
        this.gun = view3;
        this.layoutMain = linearLayout;
        this.leftImg = imageView;
        this.leftRl = relativeLayout;
        this.ptz = view4;
    }

    @NonNull
    public static ActivityNvrYunSelectBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityNvrYunSelectBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNvrYunSelectBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_nvr_yun_select, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityNvrYunSelectBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityNvrYunSelectBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNvrYunSelectBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_nvr_yun_select, null, false, dataBindingComponent);
    }

    public static ActivityNvrYunSelectBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityNvrYunSelectBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNvrYunSelectBinding) bind(dataBindingComponent, view2, R.layout.activity_nvr_yun_select);
    }
}
