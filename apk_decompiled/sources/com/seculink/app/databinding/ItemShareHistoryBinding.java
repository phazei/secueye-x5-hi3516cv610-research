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

/* JADX INFO: loaded from: classes3.dex */
public abstract class ItemShareHistoryBinding extends ViewDataBinding {

    @NonNull
    public final LinearLayout layoutItem;

    @NonNull
    public final TextView tvName;

    protected ItemShareHistoryBinding(DataBindingComponent dataBindingComponent, View view2, int i, LinearLayout linearLayout, TextView textView) {
        super(dataBindingComponent, view2, i);
        this.layoutItem = linearLayout;
        this.tvName = textView;
    }

    @NonNull
    public static ItemShareHistoryBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemShareHistoryBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemShareHistoryBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_share_history, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ItemShareHistoryBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemShareHistoryBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemShareHistoryBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_share_history, null, false, dataBindingComponent);
    }

    public static ItemShareHistoryBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ItemShareHistoryBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemShareHistoryBinding) bind(dataBindingComponent, view2, R.layout.item_share_history);
    }
}
