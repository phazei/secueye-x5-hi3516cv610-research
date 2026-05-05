package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import bean.CloudVideo;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ItemCloudImgBinding extends ViewDataBinding {

    @NonNull
    public final View bottom;

    @NonNull
    public final ImageView ivCenter;

    @NonNull
    public final ImageView ivImg;

    @NonNull
    public final ImageView ivPlaying;

    @NonNull
    public final RelativeLayout layoutItem;

    @Bindable
    protected CloudVideo.RecordFileListBean mModel;

    @NonNull
    public final View top;

    @NonNull
    public final TextView tvTime;

    @NonNull
    public final TextView tvTimeSize;

    @NonNull
    public final TextView tvType;

    public abstract void setModel(@Nullable CloudVideo.RecordFileListBean recordFileListBean);

    protected ItemCloudImgBinding(DataBindingComponent dataBindingComponent, View view2, int i, View view3, ImageView imageView, ImageView imageView2, ImageView imageView3, RelativeLayout relativeLayout, View view4, TextView textView, TextView textView2, TextView textView3) {
        super(dataBindingComponent, view2, i);
        this.bottom = view3;
        this.ivCenter = imageView;
        this.ivImg = imageView2;
        this.ivPlaying = imageView3;
        this.layoutItem = relativeLayout;
        this.top = view4;
        this.tvTime = textView;
        this.tvTimeSize = textView2;
        this.tvType = textView3;
    }

    @Nullable
    public CloudVideo.RecordFileListBean getModel() {
        return this.mModel;
    }

    @NonNull
    public static ItemCloudImgBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemCloudImgBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemCloudImgBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_cloud_img, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ItemCloudImgBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ItemCloudImgBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemCloudImgBinding) DataBindingUtil.inflate(layoutInflater, R.layout.item_cloud_img, null, false, dataBindingComponent);
    }

    public static ItemCloudImgBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ItemCloudImgBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ItemCloudImgBinding) bind(dataBindingComponent, view2, R.layout.item_cloud_img);
    }
}
