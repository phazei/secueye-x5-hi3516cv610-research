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

/* JADX INFO: loaded from: classes3.dex */
public abstract class DialogSuccessBinding extends ViewDataBinding {

    @NonNull
    public final TextView tvText;

    protected DialogSuccessBinding(DataBindingComponent dataBindingComponent, View view2, int i, TextView textView) {
        super(dataBindingComponent, view2, i);
        this.tvText = textView;
    }

    @NonNull
    public static DialogSuccessBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static DialogSuccessBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (DialogSuccessBinding) DataBindingUtil.inflate(layoutInflater, R.layout.dialog_success, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static DialogSuccessBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static DialogSuccessBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (DialogSuccessBinding) DataBindingUtil.inflate(layoutInflater, R.layout.dialog_success, null, false, dataBindingComponent);
    }

    public static DialogSuccessBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static DialogSuccessBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (DialogSuccessBinding) bind(dataBindingComponent, view2, R.layout.dialog_success);
    }
}
