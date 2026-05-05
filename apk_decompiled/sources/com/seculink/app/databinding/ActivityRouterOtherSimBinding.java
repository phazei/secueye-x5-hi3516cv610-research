package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityRouterOtherSimBinding extends ViewDataBinding {

    @NonNull
    public final Button btFeedback;

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final RelativeLayout layoutMain;

    @NonNull
    public final TextView tvIccid;

    @NonNull
    public final TextView tvProvider;

    protected ActivityRouterOtherSimBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, TitleView titleView, RelativeLayout relativeLayout, TextView textView, TextView textView2) {
        super(dataBindingComponent, view2, i);
        this.btFeedback = button;
        this.flTitlebar = titleView;
        this.layoutMain = relativeLayout;
        this.tvIccid = textView;
        this.tvProvider = textView2;
    }

    @NonNull
    public static ActivityRouterOtherSimBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityRouterOtherSimBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityRouterOtherSimBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_router_other_sim, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityRouterOtherSimBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityRouterOtherSimBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityRouterOtherSimBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_router_other_sim, null, false, dataBindingComponent);
    }

    public static ActivityRouterOtherSimBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityRouterOtherSimBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityRouterOtherSimBinding) bind(dataBindingComponent, view2, R.layout.activity_router_other_sim);
    }
}
