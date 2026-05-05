package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityFeedbackHistoryBinding extends ViewDataBinding {

    @NonNull
    public final Button btNext;

    @NonNull
    public final EditText etInput;

    @NonNull
    public final RelativeLayout layoutBottom;

    @NonNull
    public final RelativeLayout layoutMain;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @NonNull
    public final RecyclerView rvList;

    @NonNull
    public final TextView tvNoData;

    protected ActivityFeedbackHistoryBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, EditText editText, RelativeLayout relativeLayout, RelativeLayout relativeLayout2, ImageView imageView, RelativeLayout relativeLayout3, RecyclerView recyclerView, TextView textView) {
        super(dataBindingComponent, view2, i);
        this.btNext = button;
        this.etInput = editText;
        this.layoutBottom = relativeLayout;
        this.layoutMain = relativeLayout2;
        this.leftImg = imageView;
        this.leftRl = relativeLayout3;
        this.rvList = recyclerView;
        this.tvNoData = textView;
    }

    @NonNull
    public static ActivityFeedbackHistoryBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityFeedbackHistoryBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityFeedbackHistoryBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_feedback_history, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityFeedbackHistoryBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityFeedbackHistoryBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityFeedbackHistoryBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_feedback_history, null, false, dataBindingComponent);
    }

    public static ActivityFeedbackHistoryBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityFeedbackHistoryBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityFeedbackHistoryBinding) bind(dataBindingComponent, view2, R.layout.activity_feedback_history);
    }
}
