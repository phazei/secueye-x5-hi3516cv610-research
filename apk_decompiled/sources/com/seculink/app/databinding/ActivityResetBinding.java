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
public abstract class ActivityResetBinding extends ViewDataBinding {

    @NonNull
    public final Button btQrCode;

    @NonNull
    public final Button btSmart;

    @NonNull
    public final CheckBox check;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @NonNull
    public final LinearLayout permissionLl;

    protected ActivityResetBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, Button button2, CheckBox checkBox, LinearLayout linearLayout, ImageView imageView, RelativeLayout relativeLayout, LinearLayout linearLayout2) {
        super(dataBindingComponent, view2, i);
        this.btQrCode = button;
        this.btSmart = button2;
        this.check = checkBox;
        this.layoutMain = linearLayout;
        this.leftImg = imageView;
        this.leftRl = relativeLayout;
        this.permissionLl = linearLayout2;
    }

    @NonNull
    public static ActivityResetBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityResetBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityResetBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_reset, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityResetBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityResetBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityResetBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_reset, null, false, dataBindingComponent);
    }

    public static ActivityResetBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityResetBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityResetBinding) bind(dataBindingComponent, view2, R.layout.activity_reset);
    }
}
