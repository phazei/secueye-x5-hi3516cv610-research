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
public abstract class ActivityRouterLanBinding extends ViewDataBinding {

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
    public final TitleView flTitlebar;

    @NonNull
    public final LongItemView itemNatWlanSwitch;

    @NonNull
    public final RelativeLayout layoutMain;

    protected ActivityRouterLanBinding(DataBindingComponent dataBindingComponent, View view2, int i, Button button, EditText editText, EditText editText2, EditText editText3, EditText editText4, EditText editText5, EditText editText6, EditText editText7, TitleView titleView, LongItemView longItemView, RelativeLayout relativeLayout) {
        super(dataBindingComponent, view2, i);
        this.btSave = button;
        this.dhcpDefaultRouter = editText;
        this.dhcpEndAddress = editText2;
        this.dhcpFirstDnsAddress = editText3;
        this.dhcpLease = editText4;
        this.dhcpSecondDnsAddress = editText5;
        this.dhcpStartAddress = editText6;
        this.dhcpSubNetMask = editText7;
        this.flTitlebar = titleView;
        this.itemNatWlanSwitch = longItemView;
        this.layoutMain = relativeLayout;
    }

    @NonNull
    public static ActivityRouterLanBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z) {
        return inflate(layoutInflater, viewGroup, z, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityRouterLanBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean z, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityRouterLanBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_router_lan, viewGroup, z, dataBindingComponent);
    }

    @NonNull
    public static ActivityRouterLanBinding inflate(@NonNull LayoutInflater layoutInflater) {
        return inflate(layoutInflater, DataBindingUtil.getDefaultComponent());
    }

    @NonNull
    public static ActivityRouterLanBinding inflate(@NonNull LayoutInflater layoutInflater, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityRouterLanBinding) DataBindingUtil.inflate(layoutInflater, R.layout.activity_router_lan, null, false, dataBindingComponent);
    }

    public static ActivityRouterLanBinding bind(@NonNull View view2) {
        return bind(view2, DataBindingUtil.getDefaultComponent());
    }

    public static ActivityRouterLanBinding bind(@NonNull View view2, @Nullable DataBindingComponent dataBindingComponent) {
        return (ActivityRouterLanBinding) bind(dataBindingComponent, view2, R.layout.activity_router_lan);
    }
}
