package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityThreeYunSelectBinding extends ViewDataBinding {

    @NonNull
    public final TextView gun1;

    @NonNull
    public final TextView gun2;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @NonNull
    public final TextView ptz;

    protected ActivityThreeYunSelectBinding(DataBindingComponent dataBindingComponent, View view2, int i, TextView textView, TextView textView2, LinearLayout linearLayout, ImageView imageView, RelativeLayout relativeLayout, TextView textView3) {
        super(dataBindingComponent, view2, i);
        this.gun1 = textView;
        this.gun2 = textView2;
        this.layoutMain = linearLayout;
        this.leftImg = imageView;
        this.leftRl = relativeLayout;
        this.ptz = textView3;
    }

    @NonNull
    public static ActivityThreeYunSelectBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityThreeYunSelectBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityThreeYunSelectBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_three_yun_select, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityThreeYunSelectBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityThreeYunSelectBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityThreeYunSelectBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_three_yun_select, null, false, dataBindingComponent);
    }

    public static ActivityThreeYunSelectBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityThreeYunSelectBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityThreeYunSelectBinding) bind(dataBindingComponent, view2, R.layout.activity_three_yun_select);
    }
}
