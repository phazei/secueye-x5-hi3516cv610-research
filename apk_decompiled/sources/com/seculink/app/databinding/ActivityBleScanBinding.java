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

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityBleScanBinding extends ViewDataBinding {

    @NonNull
    public final EditText edWifiName;

    @NonNull
    public final EditText edWifiPass;

    @NonNull
    public final ImageView ivDeviceBindUser;

    @NonNull
    public final ImageView ivDeviceLink;

    @NonNull
    public final ImageView ivPass;

    @NonNull
    public final ImageView ivScanning;

    @NonNull
    public final ImageView ivSwitch;

    @NonNull
    public final LinearLayout layoutBle;

    @NonNull
    public final RelativeLayout layoutBleMain;

    @NonNull
    public final LinearLayout layoutLinkState;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final LinearLayout layoutScan;

    @NonNull
    public final LinearLayout layoutWifi;

    @NonNull
    public final ImageView leftImg;

    @NonNull
    public final RelativeLayout leftRl;

    @NonNull
    public final RecyclerView listView;

    @NonNull
    public final TextView tvBle;

    @NonNull
    public final TextView tvBleState;

    @NonNull
    public final TextView tvDeviceBindUser;

    @NonNull
    public final TextView tvDeviceLink;

    @NonNull
    public final TextView tvLink;

    @NonNull
    public final TextView tvScan;

    protected ActivityBleScanBinding(DataBindingComponent dataBindingComponent, View view2, int i, EditText editText, EditText editText2, ImageView imageView, ImageView imageView2, ImageView imageView3, ImageView imageView4, ImageView imageView5, LinearLayout linearLayout, RelativeLayout relativeLayout, LinearLayout linearLayout2, LinearLayout linearLayout3, LinearLayout linearLayout4, LinearLayout linearLayout5, ImageView imageView6, RelativeLayout relativeLayout2, RecyclerView recyclerView, TextView textView, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6) {
        super(dataBindingComponent, view2, i);
        this.edWifiName = editText;
        this.edWifiPass = editText2;
        this.ivDeviceBindUser = imageView;
        this.ivDeviceLink = imageView2;
        this.ivPass = imageView3;
        this.ivScanning = imageView4;
        this.ivSwitch = imageView5;
        this.layoutBle = linearLayout;
        this.layoutBleMain = relativeLayout;
        this.layoutLinkState = linearLayout2;
        this.layoutMain = linearLayout3;
        this.layoutScan = linearLayout4;
        this.layoutWifi = linearLayout5;
        this.leftImg = imageView6;
        this.leftRl = relativeLayout2;
        this.listView = recyclerView;
        this.tvBle = textView;
        this.tvBleState = textView2;
        this.tvDeviceBindUser = textView3;
        this.tvDeviceLink = textView4;
        this.tvLink = textView5;
        this.tvScan = textView6;
    }

    @NonNull
    public static ActivityBleScanBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityBleScanBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleScanBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ble_scan, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityBleScanBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityBleScanBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleScanBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_ble_scan, null, false, dataBindingComponent);
    }

    public static ActivityBleScanBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityBleScanBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityBleScanBinding) bind(dataBindingComponent, view2, R.layout.activity_ble_scan);
    }
}
