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
public abstract class StartActivityBinding extends ViewDataBinding {

    @NonNull
    public final RelativeLayout containerFl;

    @NonNull
    public final ImageView ivImg;

    @NonNull
    public final LinearLayout layout;

    protected StartActivityBinding(DataBindingComponent dataBindingComponent, View view2, int i, RelativeLayout relativeLayout, ImageView imageView, LinearLayout linearLayout) {
        super(dataBindingComponent, view2, i);
        this.containerFl = relativeLayout;
        this.ivImg = imageView;
        this.layout = linearLayout;
    }

    @NonNull
    public static StartActivityBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static StartActivityBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (StartActivityBinding) DataBindingUtil.inflate(layoutInflater, R.layout.start_activity, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static StartActivityBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static StartActivityBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (StartActivityBinding) DataBindingUtil.inflate(layoutInflater, R.layout.start_activity, null, false, dataBindingComponent);
    }

    public static StartActivityBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static StartActivityBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (StartActivityBinding) bind(dataBindingComponent, view2, R.layout.start_activity);
    }
}
