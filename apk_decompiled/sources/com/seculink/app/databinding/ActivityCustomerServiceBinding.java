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
public abstract class ActivityCustomerServiceBinding extends ViewDataBinding {

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final TextView tvCompanyName;

    protected ActivityCustomerServiceBinding(DataBindingComponent dataBindingComponent, View view2, int i, TitleView titleView, LinearLayout linearLayout, TextView textView) {
        super(dataBindingComponent, view2, i);
        this.flTitlebar = titleView;
        this.layoutMain = linearLayout;
        this.tvCompanyName = textView;
    }

    @NonNull
    public static ActivityCustomerServiceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityCustomerServiceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityCustomerServiceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_customer_service, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityCustomerServiceBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityCustomerServiceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityCustomerServiceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_customer_service, null, false, dataBindingComponent);
    }

    public static ActivityCustomerServiceBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityCustomerServiceBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityCustomerServiceBinding) bind(dataBindingComponent, view2, R.layout.activity_customer_service);
    }
}
