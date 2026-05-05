package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class CloudDownloadRecyclerviewItemBinding extends ViewDataBinding {

    @NonNull
    public final TextView beginTime;

    @NonNull
    public final Button btSelect;

    @NonNull
    public final TextView endTime;

    @NonNull
    public final TextView fileSize;

    @NonNull
    public final ImageView imageView;

    @NonNull
    public final TextView textView4;

    @NonNull
    public final TextView textView5;

    protected CloudDownloadRecyclerviewItemBinding(DataBindingComponent dataBindingComponent, View view2, int i, TextView textView, Button button, TextView textView2, TextView textView3, ImageView imageView, TextView textView4, TextView textView5) {
        super(dataBindingComponent, view2, i);
        this.beginTime = textView;
        this.btSelect = button;
        this.endTime = textView2;
        this.fileSize = textView3;
        this.imageView = imageView;
        this.textView4 = textView4;
        this.textView5 = textView5;
    }

    @NonNull
    public static CloudDownloadRecyclerviewItemBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static CloudDownloadRecyclerviewItemBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (CloudDownloadRecyclerviewItemBinding) DataBindingUtil.inflate(layoutInflater, R.layout.cloud_download_recyclerview_item, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static CloudDownloadRecyclerviewItemBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static CloudDownloadRecyclerviewItemBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (CloudDownloadRecyclerviewItemBinding) DataBindingUtil.inflate(layoutInflater, R.layout.cloud_download_recyclerview_item, null, false, dataBindingComponent);
    }

    public static CloudDownloadRecyclerviewItemBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static CloudDownloadRecyclerviewItemBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (CloudDownloadRecyclerviewItemBinding) bind(dataBindingComponent, view2, R.layout.cloud_download_recyclerview_item);
    }
}
