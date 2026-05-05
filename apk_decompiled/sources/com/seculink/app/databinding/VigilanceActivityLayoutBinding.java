package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.seculink.app.R;
import view.ItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class VigilanceActivityLayoutBinding extends ViewDataBinding {

    @NonNull
    public final ItemView Vigilance;

    @NonNull
    public final ConstraintLayout layoutMain;

    @NonNull
    public final View line;

    @NonNull
    public final RecyclerView recyclerView;

    @NonNull
    public final TitleView titleView;

    protected VigilanceActivityLayoutBinding(DataBindingComponent dataBindingComponent, View view2, int i, ItemView itemView, ConstraintLayout constraintLayout, View view3, RecyclerView recyclerView, TitleView titleView) {
        super(dataBindingComponent, view2, i);
        this.Vigilance = itemView;
        this.layoutMain = constraintLayout;
        this.line = view3;
        this.recyclerView = recyclerView;
        this.titleView = titleView;
    }

    @NonNull
    public static VigilanceActivityLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static VigilanceActivityLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (VigilanceActivityLayoutBinding) DataBindingUtil.inflate(layoutInflater, R.layout.vigilance_activity_layout, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static VigilanceActivityLayoutBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static VigilanceActivityLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (VigilanceActivityLayoutBinding) DataBindingUtil.inflate(layoutInflater, R.layout.vigilance_activity_layout, null, false, dataBindingComponent);
    }

    public static VigilanceActivityLayoutBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static VigilanceActivityLayoutBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (VigilanceActivityLayoutBinding) bind(dataBindingComponent, view2, R.layout.vigilance_activity_layout);
    }
}
