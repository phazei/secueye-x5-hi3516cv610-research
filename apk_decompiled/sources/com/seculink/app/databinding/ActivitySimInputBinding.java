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
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivitySimInputBinding extends ViewDataBinding {

    @NonNull
    public final Button btSave;

    @NonNull
    public final EditText editSearch;

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final ImageView ivScan;

    @NonNull
    public final RelativeLayout layoutMain;

    @NonNull
    public final RelativeLayout layoutSearch;

    @NonNull
    public final TextView tvTips;

    protected ActivitySimInputBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, EditText editText, TitleView titleView, ImageView imageView, RelativeLayout relativeLayout, RelativeLayout relativeLayout2, TextView textView) {
        super(dataBindingComponent, view2, i);
        this.btSave = button;
        this.editSearch = editText;
        this.flTitlebar = titleView;
        this.ivScan = imageView;
        this.layoutMain = relativeLayout;
        this.layoutSearch = relativeLayout2;
        this.tvTips = textView;
    }

    @NonNull
    public static ActivitySimInputBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivitySimInputBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivitySimInputBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_sim_input, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivitySimInputBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivitySimInputBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivitySimInputBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_sim_input, null, false, dataBindingComponent);
    }

    public static ActivitySimInputBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivitySimInputBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivitySimInputBinding) bind(dataBindingComponent, view2, R.layout.activity_sim_input);
    }
}
