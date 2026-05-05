package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityNvrYunHistoryBinding extends ViewDataBinding {

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final RelativeLayout layoutNoData;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @NonNull
    public final RecyclerView rvList;

    protected ActivityNvrYunHistoryBinding(DataBindingComponent dataBindingComponent, View view2, int i, LinearLayout linearLayout, RelativeLayout relativeLayout, ImageView imageView, RelativeLayout relativeLayout2, RecyclerView recyclerView) {
        super(dataBindingComponent, view2, i);
        this.layoutMain = linearLayout;
        this.layoutNoData = relativeLayout;
        this.leftImg = imageView;
        this.leftRl = relativeLayout2;
        this.rvList = recyclerView;
    }

    @NonNull
    public static ActivityNvrYunHistoryBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityNvrYunHistoryBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNvrYunHistoryBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_nvr_yun_history, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityNvrYunHistoryBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityNvrYunHistoryBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNvrYunHistoryBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_nvr_yun_history, null, false, dataBindingComponent);
    }

    public static ActivityNvrYunHistoryBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityNvrYunHistoryBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNvrYunHistoryBinding) bind(dataBindingComponent, view2, R.layout.activity_nvr_yun_history);
    }
}
