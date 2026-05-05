package activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import bean.DeviceInfoBean;
import bean.NatAPConfigExModel;
import bean.NatWLANConfigModel;
import bean.WlanDhcpConfModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityRouterHotspotBinding;
import config.Constants;
import java.util.HashMap;
import sdk.IPCManager;
import tools.OnMultiClickListener;
import tools.SharePreferenceManager;
import view.SelectorDialogFragment;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class BleRouterHotspotActivity extends CommonActivity {
    private SwitchCompat NatETHSwitch;
    private SwitchCompat NatWLANSwitch;
    private ActivityRouterHotspotBinding binding;
    private DeviceInfoBean device;
    private SelectorDialogFragment liveModelFragment;
    NatAPConfigExModel natAPConfigExModel;
    NatWLANConfigModel natWLANConfigModel;
    WlanDhcpConfModel wlanDhcpConfModel;
    String[] wpaList = {"WPA-WPA2", "WPA2-WPA3", "WPA3-SAE"};
    private Handler uiHandler = new Handler();

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_router_hotspot;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
        }
        this.binding = (ActivityRouterHotspotBinding) DataBindingUtil.setContentView(this, R.layout.activity_router_hotspot);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.BleRouterHotspotActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                BleRouterHotspotActivity.this.finish();
            }
        });
        if (!SharePreferenceManager.getInstance().getNatWLANSwitchConfig(this.device.getIotId()).equals("")) {
            this.natWLANConfigModel = (NatWLANConfigModel) JSONObject.parseObject(SharePreferenceManager.getInstance().getNatWLANSwitchConfig(this.device.getIotId()), NatWLANConfigModel.class);
            if (!this.natWLANConfigModel.SSID.equals("")) {
                this.binding.editName.setText(this.natWLANConfigModel.SSID);
            }
            if (!this.natWLANConfigModel.WPA_PASSPHRASE.equals("")) {
                this.binding.editPass.setText(this.natWLANConfigModel.WPA_PASSPHRASE);
            }
        }
        if (!SharePreferenceManager.getInstance().getDeviceIP(this.device.getIotId()).isEmpty()) {
            this.binding.itemIp.setRightText(SharePreferenceManager.getInstance().getDeviceIP(this.device.getIotId()));
            this.binding.itemIp.setRightTextSize(15);
            this.binding.itemIp.setRightTextColor(R.color.color_black);
        }
        if (!SharePreferenceManager.getInstance().getDeviceMAC(this.device.getIotId()).isEmpty()) {
            this.binding.itemMac.setRightText(SharePreferenceManager.getInstance().getDeviceMAC(this.device.getIotId()));
            this.binding.itemMac.setRightTextSize(15);
            this.binding.itemMac.setRightTextColor(R.color.color_black);
        }
        if (!SharePreferenceManager.getInstance().getWlanDhcpConf(this.device.getIotId()).equals("")) {
            this.wlanDhcpConfModel = (WlanDhcpConfModel) JSONObject.parseObject(SharePreferenceManager.getInstance().getWlanDhcpConf(this.device.getIotId()), WlanDhcpConfModel.class);
            if (this.wlanDhcpConfModel != null) {
                this.binding.dhcpLease.setText(this.wlanDhcpConfModel.DhcpLease + "");
                this.binding.dhcpStartAddress.setText(this.wlanDhcpConfModel.DhcpStartIPAddr + "");
                this.binding.dhcpEndAddress.setText(this.wlanDhcpConfModel.DhcpEndIPAddr + "");
                this.binding.dhcpDefaultRouter.setText(this.wlanDhcpConfModel.DhcpDefaultRouter + "");
                this.binding.dhcpSubNetMask.setText(this.wlanDhcpConfModel.DhcpSubNetMask + "");
                this.binding.dhcpFirstDnsAddress.setText(this.wlanDhcpConfModel.DhcpFirstDnsAddr + "");
                this.binding.dhcpSecondDnsAddress.setText(this.wlanDhcpConfModel.DhcpSecondDnsAddr + "");
            }
        }
        this.binding.itemNatWlanSwitch.addRightView(new SwitchCompat(this));
        this.NatWLANSwitch = (SwitchCompat) this.binding.itemNatWlanSwitch.getRightView();
        this.NatWLANSwitch.setTextOff("");
        this.NatWLANSwitch.setTextOn("");
        this.NatWLANSwitch.setText("");
        this.NatWLANSwitch.setThumbDrawable(null);
        this.NatWLANSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.NatWLANSwitch.setChecked(SharePreferenceManager.getInstance().getNatWLANSwitch(this.device.getIotId()) == 1);
        this.NatWLANSwitch.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterHotspotActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                HashMap map = new HashMap();
                map.put(Constants.NatWLANSwitch, Integer.valueOf(BleRouterHotspotActivity.this.NatWLANSwitch.isChecked() ? 1 : 0));
                IPCManager.getInstance().getDevice(BleRouterHotspotActivity.this.device.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.BleRouterHotspotActivity.2.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            SharePreferenceManager.getInstance().setNatWLANSwitch(BleRouterHotspotActivity.this.device.getIotId(), BleRouterHotspotActivity.this.NatWLANSwitch.isChecked() ? 1 : 0);
                        }
                    }
                });
            }
        });
        if (SharePreferenceManager.getInstance().getNatAPConfigEx(this.device.getIotId()) != null) {
            this.natAPConfigExModel = (NatAPConfigExModel) JSONObject.parseObject(SharePreferenceManager.getInstance().getNatAPConfigEx(this.device.getIotId()), NatAPConfigExModel.class);
            NatAPConfigExModel natAPConfigExModel = this.natAPConfigExModel;
            if (natAPConfigExModel != null) {
                if (natAPConfigExModel.Auth == 6) {
                    this.binding.itemNatWlanSecurity.setRightText(this.wpaList[0]);
                }
                if (this.natAPConfigExModel.Auth == 7) {
                    this.binding.itemNatWlanSecurity.setRightText(this.wpaList[1]);
                }
                if (this.natAPConfigExModel.Auth == 8) {
                    this.binding.itemNatWlanSecurity.setRightText(this.wpaList[2]);
                }
            }
        }
        this.liveModelFragment = new SelectorDialogFragment(getString(R.string.security), this.wpaList);
        this.liveModelFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.BleRouterHotspotActivity.3
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                BleRouterHotspotActivity.this.updateFakeDual(i);
            }
        });
        this.binding.itemNatWlanSecurity.setRightTextColor(R.color.color_black);
        this.binding.itemNatWlanSecurity.setRightTextSize(16);
        this.binding.itemNatWlanSecurity.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterHotspotActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                BleRouterHotspotActivity.this.liveModelFragment.showAllowingStateLoss(BleRouterHotspotActivity.this.getSupportFragmentManager(), "", SharePreferenceManager.getInstance().getNatAPSecurityConf(BleRouterHotspotActivity.this.device.getIotId()));
            }
        });
        this.binding.btSave.setOnClickListener(new AnonymousClass5());
    }

    /* JADX INFO: renamed from: activity.BleRouterHotspotActivity$5, reason: invalid class name */
    class AnonymousClass5 extends OnMultiClickListener {
        AnonymousClass5() {
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            if (!BleRouterHotspotActivity.this.binding.editName.getText().toString().isEmpty()) {
                if (!BleRouterHotspotActivity.this.binding.editPass.getText().toString().isEmpty() && BleRouterHotspotActivity.this.binding.editPass.getText().toString().length() >= 8) {
                    if (!BleRouterHotspotActivity.this.binding.editName.getText().toString().equals(BleRouterHotspotActivity.this.natWLANConfigModel.SSID) || !BleRouterHotspotActivity.this.binding.editPass.getText().toString().equals(BleRouterHotspotActivity.this.natWLANConfigModel.WPA_PASSPHRASE)) {
                        BleRouterHotspotActivity.this.natWLANConfigModel.SSID = BleRouterHotspotActivity.this.binding.editName.getText().toString();
                        BleRouterHotspotActivity.this.natWLANConfigModel.WPA_PASSPHRASE = BleRouterHotspotActivity.this.binding.editPass.getText().toString();
                        HashMap map = new HashMap();
                        map.put(Constants.NatAPConfig, BleRouterHotspotActivity.this.natWLANConfigModel);
                        IPCManager.getInstance().getDevice(BleRouterHotspotActivity.this.device.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.BleRouterHotspotActivity.5.1
                            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                            public void onComplete(final boolean z, final Object obj) {
                                new Handler().post(new Runnable() { // from class: activity.BleRouterHotspotActivity.5.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Object obj2;
                                        if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                            return;
                                        }
                                        JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                        if (object.containsKey("code")) {
                                            if (object.getInteger("code").intValue() == 200) {
                                                SharePreferenceManager.getInstance().setNatWLANSwitchConfig(BleRouterHotspotActivity.this.device.getIotId(), JSON.toJSONString(BleRouterHotspotActivity.this.natWLANConfigModel));
                                                Toast.makeText(BleRouterHotspotActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                            } else {
                                                Toast.makeText(BleRouterHotspotActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }
                    if (!BleRouterHotspotActivity.this.binding.dhcpLease.getText().toString().isEmpty()) {
                        if (!BleRouterHotspotActivity.this.binding.dhcpStartAddress.getText().toString().isEmpty()) {
                            if (!BleRouterHotspotActivity.this.binding.dhcpEndAddress.getText().toString().isEmpty()) {
                                if (!BleRouterHotspotActivity.this.binding.dhcpDefaultRouter.getText().toString().isEmpty()) {
                                    if (!BleRouterHotspotActivity.this.binding.dhcpSubNetMask.getText().toString().isEmpty()) {
                                        if (!BleRouterHotspotActivity.this.binding.dhcpFirstDnsAddress.getText().toString().isEmpty()) {
                                            if (!BleRouterHotspotActivity.this.binding.dhcpSecondDnsAddress.getText().toString().isEmpty()) {
                                                if (BleRouterHotspotActivity.this.binding.dhcpLease.getText().toString().equals(BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpLease + "") && BleRouterHotspotActivity.this.binding.dhcpStartAddress.getText().toString().equals(BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpStartIPAddr) && BleRouterHotspotActivity.this.binding.dhcpEndAddress.getText().toString().equals(BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpEndIPAddr) && BleRouterHotspotActivity.this.binding.dhcpDefaultRouter.getText().toString().equals(BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpDefaultRouter) && BleRouterHotspotActivity.this.binding.dhcpSubNetMask.getText().toString().equals(BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpSubNetMask) && BleRouterHotspotActivity.this.binding.dhcpFirstDnsAddress.getText().toString().equals(BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpFirstDnsAddr) && BleRouterHotspotActivity.this.binding.dhcpSecondDnsAddress.getText().toString().equals(BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpSecondDnsAddr)) {
                                                    return;
                                                }
                                                BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpLease = Integer.parseInt(BleRouterHotspotActivity.this.binding.dhcpLease.getText().toString());
                                                BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpStartIPAddr = BleRouterHotspotActivity.this.binding.dhcpStartAddress.getText().toString();
                                                BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpEndIPAddr = BleRouterHotspotActivity.this.binding.dhcpEndAddress.getText().toString();
                                                BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpDefaultRouter = BleRouterHotspotActivity.this.binding.dhcpDefaultRouter.getText().toString();
                                                BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpSubNetMask = BleRouterHotspotActivity.this.binding.dhcpSubNetMask.getText().toString();
                                                BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpFirstDnsAddr = BleRouterHotspotActivity.this.binding.dhcpFirstDnsAddress.getText().toString();
                                                BleRouterHotspotActivity.this.wlanDhcpConfModel.DhcpSecondDnsAddr = BleRouterHotspotActivity.this.binding.dhcpSecondDnsAddress.getText().toString();
                                                HashMap map2 = new HashMap();
                                                map2.put(Constants.WlanDhcpConf, BleRouterHotspotActivity.this.wlanDhcpConfModel);
                                                IPCManager.getInstance().getDevice(BleRouterHotspotActivity.this.device.getIotId()).setProperties(map2, new IPanelCallback() { // from class: activity.BleRouterHotspotActivity.5.2
                                                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                                                    public void onComplete(final boolean z, final Object obj) {
                                                        new Handler().post(new Runnable() { // from class: activity.BleRouterHotspotActivity.5.2.1
                                                            @Override // java.lang.Runnable
                                                            public void run() {
                                                                Object obj2;
                                                                if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                                                    return;
                                                                }
                                                                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                                                if (object.containsKey("code")) {
                                                                    if (object.getInteger("code").intValue() == 200) {
                                                                        SharePreferenceManager.getInstance().setWlanDhcpConf(BleRouterHotspotActivity.this.device.getIotId(), JSON.toJSONString(BleRouterHotspotActivity.this.wlanDhcpConfModel));
                                                                        Toast.makeText(BleRouterHotspotActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                                                    } else {
                                                                        Toast.makeText(BleRouterHotspotActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                                return;
                                            }
                                            BleRouterHotspotActivity.this.showToast(BleRouterHotspotActivity.this.getResources().getString(R.string.input) + "" + BleRouterHotspotActivity.this.getResources().getString(R.string.dhcp_second_dns_address));
                                            return;
                                        }
                                        BleRouterHotspotActivity.this.showToast(BleRouterHotspotActivity.this.getResources().getString(R.string.input) + "" + BleRouterHotspotActivity.this.getResources().getString(R.string.dhcp_first_dns_address));
                                        return;
                                    }
                                    BleRouterHotspotActivity.this.showToast(BleRouterHotspotActivity.this.getResources().getString(R.string.input) + "" + BleRouterHotspotActivity.this.getResources().getString(R.string.dhcp_sub_net_mask));
                                    return;
                                }
                                BleRouterHotspotActivity.this.showToast(BleRouterHotspotActivity.this.getResources().getString(R.string.input) + "" + BleRouterHotspotActivity.this.getResources().getString(R.string.dhcp_default_router));
                                return;
                            }
                            BleRouterHotspotActivity.this.showToast(BleRouterHotspotActivity.this.getResources().getString(R.string.input) + "" + BleRouterHotspotActivity.this.getResources().getString(R.string.dhcp_end_address));
                            return;
                        }
                        BleRouterHotspotActivity.this.showToast(BleRouterHotspotActivity.this.getResources().getString(R.string.input) + "" + BleRouterHotspotActivity.this.getResources().getString(R.string.dhcp_start_address));
                        return;
                    }
                    BleRouterHotspotActivity.this.showToast(BleRouterHotspotActivity.this.getResources().getString(R.string.input) + "" + BleRouterHotspotActivity.this.getResources().getString(R.string.dhcp_lease));
                    return;
                }
                BleRouterHotspotActivity.this.showToast(BleRouterHotspotActivity.this.getResources().getString(R.string.input) + "" + BleRouterHotspotActivity.this.getResources().getString(R.string.wlan_pass));
                return;
            }
            BleRouterHotspotActivity.this.showToast(BleRouterHotspotActivity.this.getResources().getString(R.string.input) + "" + BleRouterHotspotActivity.this.getResources().getString(R.string.wlan_name));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFakeDual(int i) {
        HashMap map = new HashMap();
        if (i == 0) {
            this.natAPConfigExModel.Auth = 6;
        }
        if (i == 1) {
            this.natAPConfigExModel.Auth = 7;
        }
        if (i == 2) {
            this.natAPConfigExModel.Auth = 8;
        }
        map.put(Constants.NatAPConfigEx, this.natAPConfigExModel);
        IPCManager.getInstance().getDevice(this.device.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.BleRouterHotspotActivity.6
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        BleRouterHotspotActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterHotspotActivity.6.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(BleRouterHotspotActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    } else {
                        SharePreferenceManager.getInstance().setNatAPConfigEx(BleRouterHotspotActivity.this.device.getIotId(), JSONObject.toJSONString(BleRouterHotspotActivity.this.natAPConfigExModel));
                        BleRouterHotspotActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterHotspotActivity.6.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (BleRouterHotspotActivity.this.natAPConfigExModel.Auth == 6) {
                                    BleRouterHotspotActivity.this.binding.itemNatWlanSecurity.setRightText(BleRouterHotspotActivity.this.wpaList[0]);
                                }
                                if (BleRouterHotspotActivity.this.natAPConfigExModel.Auth == 7) {
                                    BleRouterHotspotActivity.this.binding.itemNatWlanSecurity.setRightText(BleRouterHotspotActivity.this.wpaList[1]);
                                }
                                if (BleRouterHotspotActivity.this.natAPConfigExModel.Auth == 8) {
                                    BleRouterHotspotActivity.this.binding.itemNatWlanSecurity.setRightText(BleRouterHotspotActivity.this.wpaList[2]);
                                }
                                Toast.makeText(BleRouterHotspotActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }
}
