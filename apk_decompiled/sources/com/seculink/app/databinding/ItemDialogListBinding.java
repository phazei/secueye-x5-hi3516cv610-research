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
public abstract class ItemDialogListBinding extends ViewDataBinding {

    @NonNull
    public final LinearLayout layoutItem;

    @NonNull
    public final View line;

    @NonNull
    public final TextView tvText;

    protected ItemDialogListBinding(DataBindingComponent dataBindingComponent, View view2, int i, LinearLayout linearLayout, View view3, TextView textView) {
        super(dataBindingComponent, view2, i);
        this.layoutItem = linearLayout;
        this.line = view3;
        this.tvText = textView;
    }

    @NonNull
    public static ItemDialogListBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemDialogListBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemDialogListBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_dialog_list, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ItemDialogListBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemDialogListBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemDialogListBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_dialog_list, null, false, dataBindingComponent);
    }

    public static ItemDialogListBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ItemDialogListBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemDialogListBinding) bind(dataBindingComponent, view2, R.layout.item_dialog_list);
    }
}
