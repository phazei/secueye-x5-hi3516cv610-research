package com.seculink.app.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.R;
import view.LongItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityRouterHotspotBinding extends ViewDataBinding {

    @NonNull
    public final Button btSave;

    @NonNull
    public final EditText dhcpDefaultRouter;

    @NonNull
    public final EditText dhcpEndAddress;

    @NonNull
    public final EditText dhcpFirstDnsAddress;

    @NonNull
    public final EditText dhcpLease;

    @NonNull
    public final EditText dhcpSecondDnsAddress;

    @NonNull
    public final EditText dhcpStartAddress;

    @NonNull
    public final EditText dhcpSubNetMask;

    @NonNull
    public final EditText editName;

    @NonNull
    public final EditText editPass;

    @NonNull
    public final TitleView flTitlebar;

    @NonNull
    public final LongItemView itemIp;

    @NonNull
    public final LongItemView itemMac;

    @NonNull
    public final LongItemView itemMask;

    @NonNull
    public final LongItemView itemNatWlanSecurity;

    @NonNull
    public final LongItemView itemNatWlanSwitch;

    @NonNull
    public final RelativeLayout layoutMain;

    protected ActivityRouterHotspotBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, EditText editText, EditText editText2, EditText editText3, EditText editText4, EditText editText5, EditText editText6, EditText editText7, EditText editText8, EditText editText9, TitleView titleView, LongItemView longItemView, LongItemView longItemView2, LongItemView longItemView3, LongItemView longItemView4, LongItemView longItemView5, RelativeLayout relativeLayout) {
        super(dataBindingComponent, view2, i);
        this.btSave = button;
        this.dhcpDefaultRouter = editText;
        this.dhcpEndAddress = editText2;
        this.dhcpFirstDnsAddress = editText3;
        this.dhcpLease = editText4;
        this.dhcpSecondDnsAddress = editText5;
        this.dhcpStartAddress = editText6;
        this.dhcpSubNetMask = editText7;
        this.editName = editText8;
        this.editPass = editText9;
        this.flTitlebar = titleView;
        this.itemIp = longItemView;
        this.itemMac = longItemView2;
        this.itemMask = longItemView3;
        this.itemNatWlanSecurity = longItemView4;
        this.itemNatWlanSwitch = longItemView5;
        this.layoutMain = relativeLayout;
    }

    @NonNull
    public static ActivityRouterHotspotBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityRouterHotspotBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityRouterHotspotBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_router_hotspot, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityRouterHotspotBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityRouterHotspotBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityRouterHotspotBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_router_hotspot, null, false, dataBindingComponent);
    }

    public static ActivityRouterHotspotBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityRouterHotspotBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityRouterHotspotBinding) bind(dataBindingComponent, view2, R.layout.activity_router_hotspot);
    }
}
