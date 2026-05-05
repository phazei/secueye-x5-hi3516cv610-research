package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public abstract class ActivitySelectSimBinding extends ViewDataBinding {

    @NonNull
    public final LinearLayout btBuilt;

    @NonNull
    public final DragFloatActionButton btFeedback;

    @NonNull
    public final Button btNano;

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final RelativeLayout layoutMain;

    protected ActivitySelectSimBinding(DataBindingComponent dataBindingComponent, View view2, int i, LinearLayout linearLayout, DragFloatActionButton dragFloatActionButton, Button button, TitleView titleView, RelativeLayout relativeLayout) {
        super(dataBindingComponent, view2, i);
        this.btBuilt = linearLayout;
        this.btFeedback = dragFloatActionButton;
        this.btNano = button;
        this.flTitlebar = titleView;
        this.layoutMain = relativeLayout;
    }

    @NonNull
    public static ActivitySelectSimBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivitySelectSimBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivitySelectSimBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_select_sim, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivitySelectSimBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivitySelectSimBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivitySelectSimBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_select_sim, null, false, dataBindingComponent);
    }

    public static ActivitySelectSimBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivitySelectSimBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivitySelectSimBinding) bind(dataBindingComponent, view2, R.layout.activity_select_sim);
    }
}
