package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
public abstract class ActivityPowerOnBinding extends ViewDataBinding {

    @NonNull
    public final Button btNext;

    @NonNull
    public final CheckBox check;

    @NonNull
    public final ImageView ivGif;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @NonNull
    public final LinearLayout permissionLl;

    protected ActivityPowerOnBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, CheckBox checkBox, ImageView imageView, LinearLayout linearLayout, ImageView imageView2, RelativeLayout relativeLayout, LinearLayout linearLayout2) {
        super(dataBindingComponent, view2, i);
        this.btNext = button;
        this.check = checkBox;
        this.ivGif = imageView;
        this.layoutMain = linearLayout;
        this.leftImg = imageView2;
        this.leftRl = relativeLayout;
        this.permissionLl = linearLayout2;
    }

    @NonNull
    public static ActivityPowerOnBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityPowerOnBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityPowerOnBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_power_on, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityPowerOnBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityPowerOnBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityPowerOnBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_power_on, null, false, dataBindingComponent);
    }

    public static ActivityPowerOnBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityPowerOnBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityPowerOnBinding) bind(dataBindingComponent, view2, R.layout.activity_power_on);
    }
}
