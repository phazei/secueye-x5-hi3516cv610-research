package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ItemFourLiveBinding extends ViewDataBinding {

    @NonNull
    public final RelativeLayout layoutItem;

    protected ItemFourLiveBinding(DataBindingComponent dataBindingComponent, View view2, int i, RelativeLayout relativeLayout) {
        super(dataBindingComponent, view2, i);
        this.layoutItem = relativeLayout;
    }

    @NonNull
    public static ItemFourLiveBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemFourLiveBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemFourLiveBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_four_live, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ItemFourLiveBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemFourLiveBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemFourLiveBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_four_live, null, false, dataBindingComponent);
    }

    public static ItemFourLiveBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ItemFourLiveBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemFourLiveBinding) bind(dataBindingComponent, view2, R.layout.item_four_live);
    }
}
