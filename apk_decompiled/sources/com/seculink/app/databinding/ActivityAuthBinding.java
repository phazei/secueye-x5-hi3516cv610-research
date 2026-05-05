package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityAuthBinding extends ViewDataBinding {

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @NonNull
    public final TextView tvCode;

    @NonNull
    public final TextView tvQuality;

    @NonNull
    public final TextView tvQuality2;

    @NonNull
    public final TextView tvQuality3;

    protected ActivityAuthBinding(DataBindingComponent dataBindingComponent, View view2, int i, ImageView imageView, RelativeLayout relativeLayout, TextView textView, TextView textView2, TextView textView3, TextView textView4) {
        super(dataBindingComponent, view2, i);
        this.leftImg = imageView;
        this.leftRl = relativeLayout;
        this.tvCode = textView;
        this.tvQuality = textView2;
        this.tvQuality2 = textView3;
        this.tvQuality3 = textView4;
    }

    @NonNull
    public static ActivityAuthBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityAuthBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityAuthBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_auth, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityAuthBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityAuthBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityAuthBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_auth, null, false, dataBindingComponent);
    }

    public static ActivityAuthBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityAuthBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityAuthBinding) bind(dataBindingComponent, view2, R.layout.activity_auth);
    }
}
