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
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class AutoDoorLayoutBinding extends ViewDataBinding {

    @NonNull
    public final ConstraintLayout layoutMain;

    @NonNull
    public final RecyclerView recyclerViewDoor;

    @NonNull
    public final TitleView titleViewDoor;

    @NonNull
    public final View view4;

    protected AutoDoorLayoutBinding(DataBindingComponent dataBindingComponent, View view2, int i, ConstraintLayout constraintLayout, RecyclerView recyclerView, TitleView titleView, View view3) {
        super(dataBindingComponent, view2, i);
        this.layoutMain = constraintLayout;
        this.recyclerViewDoor = recyclerView;
        this.titleViewDoor = titleView;
        this.view4 = view3;
    }

    @NonNull
    public static AutoDoorLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static AutoDoorLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (AutoDoorLayoutBinding) DataBindingUtil.inflate(layoutInflater, R.layout.auto_door_layout, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static AutoDoorLayoutBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static AutoDoorLayoutBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (AutoDoorLayoutBinding) DataBindingUtil.inflate(layoutInflater, R.layout.auto_door_layout, null, false, dataBindingComponent);
    }

    public static AutoDoorLayoutBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static AutoDoorLayoutBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (AutoDoorLayoutBinding) bind(dataBindingComponent, view2, R.layout.auto_door_layout);
    }
}
