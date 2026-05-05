package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import bean.CloudPayModel;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ItemPayBinding extends ViewDataBinding {

    @NonNull
    public final ImageView ivType;

    @NonNull
    public final RelativeLayout layoutItem;

    @Bindable
    protected CloudPayModel mModel;

    @NonNull
    public final TextView tvName;

    @NonNull
    public final TextView tvOrder;

    @NonNull
    public final TextView tvTime;

    @NonNull
    public final TextView tvType;

    @NonNull
    public final TextView tvTypeText;

    public abstract void setModel(@Nullable CloudPayModel cloudPayModel);

    protected ItemPayBinding(DataBindingComponent dataBindingComponent, View view2, int i, ImageView imageView, RelativeLayout relativeLayout, TextView textView, TextView textView2, TextView textView3, TextView textView4, TextView textView5) {
        super(dataBindingComponent, view2, i);
        this.ivType = imageView;
        this.layoutItem = relativeLayout;
        this.tvName = textView;
        this.tvOrder = textView2;
        this.tvTime = textView3;
        this.tvType = textView4;
        this.tvTypeText = textView5;
    }

    @Nullable
    public CloudPayModel getModel() {
        return this.mModel;
    }

    @NonNull
    public static ItemPayBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemPayBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemPayBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_pay, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ItemPayBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemPayBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemPayBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_pay, null, false, dataBindingComponent);
    }

    public static ItemPayBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ItemPayBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemPayBinding) bind(dataBindingComponent, view2, R.layout.item_pay);
    }
}
