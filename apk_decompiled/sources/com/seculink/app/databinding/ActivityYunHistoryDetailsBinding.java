package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import bean.CloudPayModel;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityYunHistoryDetailsBinding extends ViewDataBinding {

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @Bindable
    protected CloudPayModel mModel;

    public abstract void setModel(@Nullable CloudPayModel cloudPayModel);

    protected ActivityYunHistoryDetailsBinding(DataBindingComponent dataBindingComponent, View view2, int i, LinearLayout linearLayout, ImageView imageView, RelativeLayout relativeLayout) {
        super(dataBindingComponent, view2, i);
        this.layoutMain = linearLayout;
        this.leftImg = imageView;
        this.leftRl = relativeLayout;
    }

    @Nullable
    public CloudPayModel getModel() {
        return this.mModel;
    }

    @NonNull
    public static ActivityYunHistoryDetailsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityYunHistoryDetailsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityYunHistoryDetailsBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_yun_history_details, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityYunHistoryDetailsBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityYunHistoryDetailsBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityYunHistoryDetailsBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_yun_history_details, null, false, dataBindingComponent);
    }

    public static ActivityYunHistoryDetailsBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityYunHistoryDetailsBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityYunHistoryDetailsBinding) bind(dataBindingComponent, view2, R.layout.activity_yun_history_details);
    }
}
