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
import bean.SearchModel;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ItemSearchBinding extends ViewDataBinding {

    @NonNull
    public final View bottom;

    @NonNull
    public final ImageView ivCenter;

    @NonNull
    public final RelativeLayout layoutItem;

    @Bindable
    protected SearchModel mModel;

    @NonNull
    public final View top;

    @NonNull
    public final TextView tvTime;

    @NonNull
    public final TextView tvType;

    public abstract void setModel(@Nullable SearchModel searchModel);

    protected ItemSearchBinding(DataBindingComponent dataBindingComponent, View view2, int i, View view3, ImageView imageView, RelativeLayout relativeLayout, View view4, TextView textView, TextView textView2) {
        super(dataBindingComponent, view2, i);
        this.bottom = view3;
        this.ivCenter = imageView;
        this.layoutItem = relativeLayout;
        this.top = view4;
        this.tvTime = textView;
        this.tvType = textView2;
    }

    @Nullable
    public SearchModel getModel() {
        return this.mModel;
    }

    @NonNull
    public static ItemSearchBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemSearchBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemSearchBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_search, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ItemSearchBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemSearchBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemSearchBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_search, null, false, dataBindingComponent);
    }

    public static ItemSearchBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ItemSearchBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemSearchBinding) bind(dataBindingComponent, view2, R.layout.item_search);
    }
}
