package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import bean.FeedbackBean;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ItemFaceBinding extends ViewDataBinding {

    @Bindable
    protected FeedbackBean mModel;

    @NonNull
    public final TextView tvDelete;

    @NonNull
    public final TextView tvName;

    public abstract void setModel(@Nullable FeedbackBean feedbackBean);

    protected ItemFaceBinding(DataBindingComponent dataBindingComponent, View view2, int i, TextView textView, TextView textView2) {
        super(dataBindingComponent, view2, i);
        this.tvDelete = textView;
        this.tvName = textView2;
    }

    @Nullable
    public FeedbackBean getModel() {
        return this.mModel;
    }

    @NonNull
    public static ItemFaceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemFaceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemFaceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_face, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ItemFaceBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemFaceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemFaceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_face, null, false, dataBindingComponent);
    }

    public static ItemFaceBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ItemFaceBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemFaceBinding) bind(dataBindingComponent, view2, R.layout.item_face);
    }
}
