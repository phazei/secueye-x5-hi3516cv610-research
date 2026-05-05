package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityNetWlanSettingBinding extends ViewDataBinding {

    @NonNull
    public final Button checkBtn;

    @NonNull
    public final EditText editName;

    @NonNull
    public final EditText editPass;

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final LinearLayout layoutMain;

    @NonNull
    public final View line;

    @NonNull
    public final TextView yiDongZhenCeText;

    protected ActivityNetWlanSettingBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, EditText editText, EditText editText2, TitleView titleView, LinearLayout linearLayout, View view3, TextView textView) {
        super(dataBindingComponent, view2, i);
        this.checkBtn = button;
        this.editName = editText;
        this.editPass = editText2;
        this.flTitlebar = titleView;
        this.layoutMain = linearLayout;
        this.line = view3;
        this.yiDongZhenCeText = textView;
    }

    @NonNull
    public static ActivityNetWlanSettingBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityNetWlanSettingBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNetWlanSettingBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_net_wlan_setting, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityNetWlanSettingBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityNetWlanSettingBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNetWlanSettingBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_net_wlan_setting, null, false, dataBindingComponent);
    }

    public static ActivityNetWlanSettingBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityNetWlanSettingBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityNetWlanSettingBinding) bind(dataBindingComponent, view2, R.layout.activity_net_wlan_setting);
    }
}
