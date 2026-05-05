package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
public abstract class ItemFeedbackBinding extends ViewDataBinding {

    @NonNull
    public final LinearLayout layoutItem;

    @NonNull
    public final View line;

    @Bindable
    protected FeedbackBean mModel;

    @NonNull
    public final TextView tvTime;

    @NonNull
    public final TextView tvType;

    public abstract void setModel(@Nullable FeedbackBean feedbackBean);

    protected ItemFeedbackBinding(DataBindingComponent dataBindingComponent, View view2, int i, LinearLayout linearLayout, View view3, TextView textView, TextView textView2) {
        super(dataBindingComponent, view2, i);
        this.layoutItem = linearLayout;
        this.line = view3;
        this.tvTime = textView;
        this.tvType = textView2;
    }

    @Nullable
    public FeedbackBean getModel() {
        return this.mModel;
    }

    @NonNull
    public static ItemFeedbackBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemFeedbackBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemFeedbackBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_feedback, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ItemFeedbackBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemFeedbackBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemFeedbackBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_feedback, null, false, dataBindingComponent);
    }

    public static ItemFeedbackBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ItemFeedbackBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemFeedbackBinding) bind(dataBindingComponent, view2, R.layout.item_feedback);
    }
}
