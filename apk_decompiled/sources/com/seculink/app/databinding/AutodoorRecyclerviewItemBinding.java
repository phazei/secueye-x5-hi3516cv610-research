package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Guideline;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class AutodoorRecyclerviewItemBinding extends ViewDataBinding {

    @NonNull
    public final Guideline guideline6;

    @NonNull
    public final Guideline guideline7;

    @NonNull
    public final TextView name;

    @NonNull
    public final TextView reset;

    protected AutodoorRecyclerviewItemBinding(DataBindingComponent dataBindingComponent, View view2, int i, Guideline guideline, Guideline guideline2, TextView textView, TextView textView2) {
        super(dataBindingComponent, view2, i);
        this.guideline6 = guideline;
        this.guideline7 = guideline2;
        this.name = textView;
        this.reset = textView2;
    }

    @NonNull
    public static AutodoorRecyclerviewItemBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static AutodoorRecyclerviewItemBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (AutodoorRecyclerviewItemBinding) DataBindingUtil.inflate(layoutInflater, R.layout.autodoor_recyclerview_item, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static AutodoorRecyclerviewItemBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static AutodoorRecyclerviewItemBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (AutodoorRecyclerviewItemBinding) DataBindingUtil.inflate(layoutInflater, R.layout.autodoor_recyclerview_item, null, false, dataBindingComponent);
    }

    public static AutodoorRecyclerviewItemBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static AutodoorRecyclerviewItemBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (AutodoorRecyclerviewItemBinding) bind(dataBindingComponent, view2, R.layout.autodoor_recyclerview_item);
    }
}
