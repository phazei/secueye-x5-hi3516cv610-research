package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.DragFloatActionButton;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityConnectionFailedBinding extends ViewDataBinding {

    @NonNull
    public final DragFloatActionButton btFeedback;

    @NonNull
    public final Button btNano;

    @NonNull
    public final LinearLayout btOtherSim;

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final ImageView ivGif;

    @NonNull
    public final RelativeLayout layoutMain;

    protected ActivityConnectionFailedBinding(DataBindingComponent dataBindingComponent, View view2, int i, DragFloatActionButton dragFloatActionButton, Button button, LinearLayout linearLayout, TitleView titleView, ImageView imageView, RelativeLayout relativeLayout) {
        super(dataBindingComponent, view2, i);
        this.btFeedback = dragFloatActionButton;
        this.btNano = button;
        this.btOtherSim = linearLayout;
        this.flTitlebar = titleView;
        this.ivGif = imageView;
        this.layoutMain = relativeLayout;
    }

    @NonNull
    public static ActivityConnectionFailedBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityConnectionFailedBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityConnectionFailedBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_connection_failed, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityConnectionFailedBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityConnectionFailedBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityConnectionFailedBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_connection_failed, null, false, dataBindingComponent);
    }

    public static ActivityConnectionFailedBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityConnectionFailedBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityConnectionFailedBinding) bind(dataBindingComponent, view2, R.layout.activity_connection_failed);
    }
}
