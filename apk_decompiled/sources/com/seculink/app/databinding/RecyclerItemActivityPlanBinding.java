package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class RecyclerItemActivityPlanBinding extends ViewDataBinding {

    @NonNull
    public final SwitchCompat alarmSwitch;

    @NonNull
    public final TextView day;

    @NonNull
    public final ConstraintLayout recyclerItemDelete;

    @NonNull
    public final ConstraintLayout recyclerItemEdit;

    @NonNull
    public final TextView time;

    @NonNull
    public final TextView title;

    @NonNull
    public final View view7;

    @NonNull
    public final View view8;

    protected RecyclerItemActivityPlanBinding(DataBindingComponent dataBindingComponent, View view2, int i, SwitchCompat switchCompat, TextView textView, ConstraintLayout constraintLayout, ConstraintLayout constraintLayout2, TextView textView2, TextView textView3, View view3, View view4) {
        super(dataBindingComponent, view2, i);
        this.alarmSwitch = switchCompat;
        this.day = textView;
        this.recyclerItemDelete = constraintLayout;
        this.recyclerItemEdit = constraintLayout2;
        this.time = textView2;
        this.title = textView3;
        this.view7 = view3;
        this.view8 = view4;
    }

    @NonNull
    public static RecyclerItemActivityPlanBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static RecyclerItemActivityPlanBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (RecyclerItemActivityPlanBinding) DataBindingUtil.inflate(layoutInflater, R.layout.recycler_item_activity_plan, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static RecyclerItemActivityPlanBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static RecyclerItemActivityPlanBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (RecyclerItemActivityPlanBinding) DataBindingUtil.inflate(layoutInflater, R.layout.recycler_item_activity_plan, null, false, dataBindingComponent);
    }

    public static RecyclerItemActivityPlanBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static RecyclerItemActivityPlanBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (RecyclerItemActivityPlanBinding) bind(dataBindingComponent, view2, R.layout.recycler_item_activity_plan);
    }
}
