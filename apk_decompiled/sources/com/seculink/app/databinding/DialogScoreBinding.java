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
public abstract class DialogScoreBinding extends ViewDataBinding {

    @NonNull
    public final LinearLayout layoutScore;

    @NonNull
    public final TextView tvCancel;

    protected DialogScoreBinding(DataBindingComponent dataBindingComponent, View view2, int i, LinearLayout linearLayout, TextView textView) {
        super(dataBindingComponent, view2, i);
        this.layoutScore = linearLayout;
        this.tvCancel = textView;
    }

    @NonNull
    public static DialogScoreBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static DialogScoreBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (DialogScoreBinding) DataBindingUtil.inflate(layoutInflater, R.layout.dialog_score, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static DialogScoreBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static DialogScoreBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (DialogScoreBinding) DataBindingUtil.inflate(layoutInflater, R.layout.dialog_score, null, false, dataBindingComponent);
    }

    public static DialogScoreBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static DialogScoreBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (DialogScoreBinding) bind(dataBindingComponent, view2, R.layout.dialog_score);
    }
}
