package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import bean.FeedBackReplyList;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ItemFeedbackReplyBinding extends ViewDataBinding {

    @NonNull
    public final RelativeLayout layoutCenter;

    @NonNull
    public final RelativeLayout layoutItem;

    @Bindable
    protected FeedBackReplyList mModel;

    @NonNull
    public final TextView tvTime;

    @NonNull
    public final TextView tvType;

    public abstract void setModel(@Nullable FeedBackReplyList feedBackReplyList);

    protected ItemFeedbackReplyBinding(DataBindingComponent dataBindingComponent, View view2, int i, RelativeLayout relativeLayout, RelativeLayout relativeLayout2, TextView textView, TextView textView2) {
        super(dataBindingComponent, view2, i);
        this.layoutCenter = relativeLayout;
        this.layoutItem = relativeLayout2;
        this.tvTime = textView;
        this.tvType = textView2;
    }

    @Nullable
    public FeedBackReplyList getModel() {
        return this.mModel;
    }

    @NonNull
    public static ItemFeedbackReplyBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemFeedbackReplyBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemFeedbackReplyBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_feedback_reply, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ItemFeedbackReplyBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemFeedbackReplyBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemFeedbackReplyBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_feedback_reply, null, false, dataBindingComponent);
    }

    public static ItemFeedbackReplyBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ItemFeedbackReplyBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemFeedbackReplyBinding) bind(dataBindingComponent, view2, R.layout.item_feedback_reply);
    }
}
