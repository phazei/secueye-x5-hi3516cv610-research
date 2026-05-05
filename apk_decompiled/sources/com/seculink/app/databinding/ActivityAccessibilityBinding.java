package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityAccessibilityBinding extends ViewDataBinding {

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final TextView tvAccessibility;

    @NonNull
    public final TextView tvBase;

    @NonNull
    public final TextView tvSuspension;

    protected ActivityAccessibilityBinding(DataBindingComponent dataBindingComponent, View view2, int i, TitleView titleView, TextView textView, TextView textView2, TextView textView3) {
        super(dataBindingComponent, view2, i);
        this.flTitlebar = titleView;
        this.tvAccessibility = textView;
        this.tvBase = textView2;
        this.tvSuspension = textView3;
    }

    @NonNull
    public static ActivityAccessibilityBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityAccessibilityBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityAccessibilityBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_accessibility, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityAccessibilityBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityAccessibilityBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityAccessibilityBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_accessibility, null, false, dataBindingComponent);
    }

    public static ActivityAccessibilityBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityAccessibilityBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityAccessibilityBinding) bind(dataBindingComponent, view2, R.layout.activity_accessibility);
    }
}
