package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class DialogWechatBinding extends ViewDataBinding {

    @NonNull
    public final ImageView ivClose;

    @NonNull
    public final TextView tvSave;

    protected DialogWechatBinding(DataBindingComponent dataBindingComponent, View view2, int i, ImageView imageView, TextView textView) {
        super(dataBindingComponent, view2, i);
        this.ivClose = imageView;
        this.tvSave = textView;
    }

    @NonNull
    public static DialogWechatBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static DialogWechatBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (DialogWechatBinding) DataBindingUtil.inflate(layoutInflater, R.layout.dialog_wechat, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static DialogWechatBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static DialogWechatBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (DialogWechatBinding) DataBindingUtil.inflate(layoutInflater, R.layout.dialog_wechat, null, false, dataBindingComponent);
    }

    public static DialogWechatBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static DialogWechatBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (DialogWechatBinding) bind(dataBindingComponent, view2, R.layout.dialog_wechat);
    }
}
