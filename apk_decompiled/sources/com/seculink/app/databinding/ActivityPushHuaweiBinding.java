package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityPushHuaweiBinding extends ViewDataBinding {

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final TextView tvText1;

    @NonNull
    public final TextView tvText7;

    @NonNull
    public final TextView tvText8;

    protected ActivityPushHuaweiBinding(DataBindingComponent dataBindingComponent, View view2, int i, TitleView titleView, LinearLayout linearLayout, TextView textView, TextView textView2, TextView textView3) {
        super(dataBindingComponent, view2, i);
        this.flTitlebar = titleView;
        this.layoutMain = linearLayout;
        this.tvText1 = textView;
        this.tvText7 = textView2;
        this.tvText8 = textView3;
    }

    @NonNull
    public static ActivityPushHuaweiBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityPushHuaweiBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityPushHuaweiBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_push_huawei, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityPushHuaweiBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityPushHuaweiBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityPushHuaweiBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_push_huawei, null, false, dataBindingComponent);
    }

    public static ActivityPushHuaweiBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityPushHuaweiBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityPushHuaweiBinding) bind(dataBindingComponent, view2, R.layout.activity_push_huawei);
    }
}
