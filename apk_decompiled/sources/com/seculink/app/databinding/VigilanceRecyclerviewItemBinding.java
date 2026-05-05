package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class VigilanceRecyclerviewItemBinding extends ViewDataBinding {

    @NonNull
    public final RelativeLayout VigilanceTitle;

    @NonNull
    public final TextView delete;

    @NonNull
    public final TextView edit;

    @NonNull
    public final ImageView image;

    @NonNull
    public final ImageView imageBg;

    @NonNull
    public final View line;

    @NonNull
    public final TextView textView;

    @NonNull
    public final TextView vigilanceNameText;

    protected VigilanceRecyclerviewItemBinding(DataBindingComponent dataBindingComponent, View view2, int i, RelativeLayout relativeLayout, TextView textView, TextView textView2, ImageView imageView, ImageView imageView2, View view3, TextView textView3, TextView textView4) {
        super(dataBindingComponent, view2, i);
        this.VigilanceTitle = relativeLayout;
        this.delete = textView;
        this.edit = textView2;
        this.image = imageView;
        this.imageBg = imageView2;
        this.line = view3;
        this.textView = textView3;
        this.vigilanceNameText = textView4;
    }

    @NonNull
    public static VigilanceRecyclerviewItemBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static VigilanceRecyclerviewItemBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (VigilanceRecyclerviewItemBinding) DataBindingUtil.inflate(layoutInflater, R.layout.vigilance_recyclerview_item, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static VigilanceRecyclerviewItemBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static VigilanceRecyclerviewItemBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (VigilanceRecyclerviewItemBinding) DataBindingUtil.inflate(layoutInflater, R.layout.vigilance_recyclerview_item, null, false, dataBindingComponent);
    }

    public static VigilanceRecyclerviewItemBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static VigilanceRecyclerviewItemBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (VigilanceRecyclerviewItemBinding) bind(dataBindingComponent, view2, R.layout.vigilance_recyclerview_item);
    }
}
