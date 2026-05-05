package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityAddDeviceBinding extends ViewDataBinding {

    @NonNull
    public final EditText edWifiName;

    @NonNull
    public final EditText edWifiPass;

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final ImageView ivBleState;

    @NonNull
    public final ImageView ivGif;

    @NonNull
    public final ImageView ivPass;

    @NonNull
    public final TextView ivSwitch;

    @NonNull
    public final RelativeLayout layoutBle;

    @NonNull
    public final RelativeLayout layoutBleMain;

    @NonNull
    public final LinearLayout layoutLinkState;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final LinearLayout layoutWifi;

    @NonNull
    public final RecyclerView listView;

    @NonNull
    public final RecyclerView rvList;

    @NonNull
    public final TextView tv4g;

    @NonNull
    public final TextView tvBle;

    @NonNull
    public final TextView tvBleState;

    @NonNull
    public final TextView tvDeviceBindUser;

    @NonNull
    public final TextView tvDeviceLink;

    @NonNull
    public final TextView tvDeviceLinkTime;

    @NonNull
    public final TextView tvLink;

    @NonNull
    public final TextView tvNetworkCable;

    @NonNull
    public final TextView tvOtherDevices;

    @NonNull
    public final TextView tvTitle;

    @NonNull
    public final TextView tvWifi;

    protected ActivityAddDeviceBinding(DataBindingComponent dataBindingComponent, View view2, int i, EditText editText, EditText editText2, TitleView titleView, ImageView imageView, ImageView imageView2, ImageView imageView3, TextView textView, RelativeLayout relativeLayout, RelativeLayout relativeLayout2, LinearLayout linearLayout, LinearLayout linearLayout2, LinearLayout linearLayout3, RecyclerView recyclerView, RecyclerView recyclerView2, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7, TextView textView8, TextView textView9, TextView textView10, TextView textView11, TextView textView12) {
        super(dataBindingComponent, view2, i);
        this.edWifiName = editText;
        this.edWifiPass = editText2;
        this.flTitlebar = titleView;
        this.ivBleState = imageView;
        this.ivGif = imageView2;
        this.ivPass = imageView3;
        this.ivSwitch = textView;
        this.layoutBle = relativeLayout;
        this.layoutBleMain = relativeLayout2;
        this.layoutLinkState = linearLayout;
        this.layoutMain = linearLayout2;
        this.layoutWifi = linearLayout3;
        this.listView = recyclerView;
        this.rvList = recyclerView2;
        this.tv4g = textView2;
        this.tvBle = textView3;
        this.tvBleState = textView4;
        this.tvDeviceBindUser = textView5;
        this.tvDeviceLink = textView6;
        this.tvDeviceLinkTime = textView7;
        this.tvLink = textView8;
        this.tvNetworkCable = textView9;
        this.tvOtherDevices = textView10;
        this.tvTitle = textView11;
        this.tvWifi = textView12;
    }

    @NonNull
    public static ActivityAddDeviceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityAddDeviceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityAddDeviceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_add_device, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityAddDeviceBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityAddDeviceBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityAddDeviceBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_add_device, null, false, dataBindingComponent);
    }

    public static ActivityAddDeviceBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityAddDeviceBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityAddDeviceBinding) bind(dataBindingComponent, view2, R.layout.activity_add_device);
    }
}
