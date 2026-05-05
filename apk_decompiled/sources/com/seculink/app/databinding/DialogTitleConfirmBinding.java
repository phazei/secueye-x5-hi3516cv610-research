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
public abstract class DialogTitleConfirmBinding extends ViewDataBinding {

    @NonNull
    public final TextView tvConfirm;

    @NonNull
    public final TextView tvTips;

    @NonNull
    public final TextView tvTitle;

    protected DialogTitleConfirmBinding(DataBindingComponent dataBindingComponent, View view2, int i, TextView textView, TextView textView2, TextView textView3) {
        super(dataBindingComponent, view2, i);
        this.tvConfirm = textView;
        this.tvTips = textView2;
        this.tvTitle = textView3;
    }

    @NonNull
    public static DialogTitleConfirmBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static DialogTitleConfirmBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (DialogTitleConfirmBinding) DataBindingUtil.inflate(layoutInflater, R.layout.dialog_title_confirm, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static DialogTitleConfirmBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static DialogTitleConfirmBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (DialogTitleConfirmBinding) DataBindingUtil.inflate(layoutInflater, R.layout.dialog_title_confirm, null, false, dataBindingComponent);
    }

    public static DialogTitleConfirmBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static DialogTitleConfirmBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (DialogTitleConfirmBinding) bind(dataBindingComponent, view2, R.layout.dialog_title_confirm);
    }
}
