package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ItemBleBinding extends ViewDataBinding {

    @NonNull
    public final ImageView ivSelect;

    @NonNull
    public final LinearLayout layoutItem;

    @NonNull
    public final TextView tvAddress;

    @NonNull
    public final TextView tvName;

    @NonNull
    public final TextView tvState;

    protected ItemBleBinding(DataBindingComponent dataBindingComponent, View view2, int i, ImageView imageView, LinearLayout linearLayout, TextView textView, TextView textView2, TextView textView3) {
        super(dataBindingComponent, view2, i);
        this.ivSelect = imageView;
        this.layoutItem = linearLayout;
        this.tvAddress = textView;
        this.tvName = textView2;
        this.tvState = textView3;
    }

    @NonNull
    public static ItemBleBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemBleBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemBleBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_ble, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ItemBleBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemBleBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemBleBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_ble, null, false, dataBindingComponent);
    }

    public static ItemBleBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ItemBleBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemBleBinding) bind(dataBindingComponent, view2, R.layout.item_ble);
    }
}
