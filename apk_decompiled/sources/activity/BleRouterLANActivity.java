package activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import bean.DeviceInfoBean;
import bean.EthernetDhcpConfModel;
import bean.NatWLANConfigModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityRouterLanBinding;
import config.Constants;
import java.util.HashMap;
import sdk.IPCManager;
import tools.OnMultiClickListener;
import tools.SharePreferenceManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class BleRouterLANActivity extends CommonActivity {
    private SwitchCompat NatETHSwitch;
    private SwitchCompat NatWLANSwitch;
    private ActivityRouterLanBinding binding;
    private DeviceInfoBean device;
    EthernetDhcpConfModel ethernetDhcpConfModel;
    NatWLANConfigModel natWLANConfigModel;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_router_lan;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
        }
        this.binding = (ActivityRouterLanBinding) DataBindingUtil.setContentView(this, R.layout.activity_router_lan);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.BleRouterLANActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                BleRouterLANActivity.this.finish();
            }
        });
        if (!SharePreferenceManager.getInstance().getEthernetDhcpConf(this.device.getIotId()).equals("")) {
            this.ethernetDhcpConfModel = (EthernetDhcpConfModel) JSONObject.parseObject(SharePreferenceManager.getInstance().getEthernetDhcpConf(this.device.getIotId()), EthernetDhcpConfModel.class);
            this.binding.dhcpLease.setText(this.ethernetDhcpConfModel.DhcpLease + "");
            this.binding.dhcpStartAddress.setText(this.ethernetDhcpConfModel.DhcpStartIPAddr + "");
            this.binding.dhcpEndAddress.setText(this.ethernetDhcpConfModel.DhcpEndIPAddr + "");
            this.binding.dhcpDefaultRouter.setText(this.ethernetDhcpConfModel.DhcpDefaultRouter + "");
            this.binding.dhcpSubNetMask.setText(this.ethernetDhcpConfModel.DhcpSubNetMask + "");
            this.binding.dhcpFirstDnsAddress.setText(this.ethernetDhcpConfModel.DhcpFirstDnsAddr + "");
            this.binding.dhcpSecondDnsAddress.setText(this.ethernetDhcpConfModel.DhcpSecondDnsAddr + "");
        }
        this.binding.itemNatWlanSwitch.addRightView(new SwitchCompat(this));
        this.NatWLANSwitch = (SwitchCompat) this.binding.itemNatWlanSwitch.getRightView();
        this.NatWLANSwitch.setTextOff("");
        this.NatWLANSwitch.setTextOn("");
        this.NatWLANSwitch.setText("");
        this.NatWLANSwitch.setThumbDrawable(null);
        this.NatWLANSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.NatWLANSwitch.setChecked(SharePreferenceManager.getInstance().getNatETHSwitch(this.device.getIotId()) == 1);
        this.NatWLANSwitch.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterLANActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                HashMap map = new HashMap();
                map.put(Constants.NatETHSwitch, Integer.valueOf(BleRouterLANActivity.this.NatWLANSwitch.isChecked() ? 1 : 0));
                IPCManager.getInstance().getDevice(BleRouterLANActivity.this.device.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.BleRouterLANActivity.2.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            SharePreferenceManager.getInstance().setNatETHSwitch(BleRouterLANActivity.this.device.getIotId(), BleRouterLANActivity.this.NatWLANSwitch.isChecked() ? 1 : 0);
                        }
                    }
                });
            }
        });
        this.binding.btSave.setOnClickListener(new AnonymousClass3());
    }

    /* JADX INFO: renamed from: activity.BleRouterLANActivity$3, reason: invalid class name */
    class AnonymousClass3 extends OnMultiClickListener {
        AnonymousClass3() {
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            if (!BleRouterLANActivity.this.binding.dhcpLease.getText().toString().isEmpty()) {
                if (!BleRouterLANActivity.this.binding.dhcpStartAddress.getText().toString().isEmpty()) {
                    if (!BleRouterLANActivity.this.binding.dhcpEndAddress.getText().toString().isEmpty()) {
                        if (!BleRouterLANActivity.this.binding.dhcpDefaultRouter.getText().toString().isEmpty()) {
                            if (!BleRouterLANActivity.this.binding.dhcpSubNetMask.getText().toString().isEmpty()) {
                                if (!BleRouterLANActivity.this.binding.dhcpFirstDnsAddress.getText().toString().isEmpty()) {
                                    if (!BleRouterLANActivity.this.binding.dhcpSecondDnsAddress.getText().toString().isEmpty()) {
                                        if (BleRouterLANActivity.this.binding.dhcpLease.getText().toString().equals(BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpLease + "") && BleRouterLANActivity.this.binding.dhcpStartAddress.getText().toString().equals(BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpStartIPAddr) && BleRouterLANActivity.this.binding.dhcpEndAddress.getText().toString().equals(BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpEndIPAddr) && BleRouterLANActivity.this.binding.dhcpDefaultRouter.getText().toString().equals(BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpDefaultRouter) && BleRouterLANActivity.this.binding.dhcpSubNetMask.getText().toString().equals(BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpSubNetMask) && BleRouterLANActivity.this.binding.dhcpFirstDnsAddress.getText().toString().equals(BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpFirstDnsAddr) && BleRouterLANActivity.this.binding.dhcpSecondDnsAddress.getText().toString().equals(BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpSecondDnsAddr)) {
                                            Toast.makeText(BleRouterLANActivity.this.getActivity(), R.string.save_settings, 0).show();
                                            return;
                                        }
                                        BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpLease = Integer.parseInt(BleRouterLANActivity.this.binding.dhcpLease.getText().toString());
                                        BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpStartIPAddr = BleRouterLANActivity.this.binding.dhcpStartAddress.getText().toString();
                                        BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpEndIPAddr = BleRouterLANActivity.this.binding.dhcpEndAddress.getText().toString();
                                        BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpDefaultRouter = BleRouterLANActivity.this.binding.dhcpDefaultRouter.getText().toString();
                                        BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpSubNetMask = BleRouterLANActivity.this.binding.dhcpSubNetMask.getText().toString();
                                        BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpFirstDnsAddr = BleRouterLANActivity.this.binding.dhcpFirstDnsAddress.getText().toString();
                                        BleRouterLANActivity.this.ethernetDhcpConfModel.DhcpSecondDnsAddr = BleRouterLANActivity.this.binding.dhcpSecondDnsAddress.getText().toString();
                                        HashMap map = new HashMap();
                                        map.put(Constants.EthernetDhcpConf, BleRouterLANActivity.this.ethernetDhcpConfModel);
                                        IPCManager.getInstance().getDevice(BleRouterLANActivity.this.device.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.BleRouterLANActivity.3.1
                                            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                                            public void onComplete(final boolean z, final Object obj) {
                                                new Handler().post(new Runnable() { // from class: activity.BleRouterLANActivity.3.1.1
                                                    @Override // java.lang.Runnable
                                                    public void run() {
                                                        Object obj2;
                                                        if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                                            return;
                                                        }
                                                        JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                                        if (object.containsKey("code")) {
                                                            if (object.getInteger("code").intValue() == 200) {
                                                                SharePreferenceManager.getInstance().setEthernetDhcpConf(BleRouterLANActivity.this.device.getIotId(), JSON.toJSONString(BleRouterLANActivity.this.ethernetDhcpConfModel));
                                                                Toast.makeText(BleRouterLANActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                                            } else {
                                                                Toast.makeText(BleRouterLANActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                        return;
                                    }
                                    BleRouterLANActivity.this.showToast(BleRouterLANActivity.this.getResources().getString(R.string.input) + "" + BleRouterLANActivity.this.getResources().getString(R.string.dhcp_second_dns_address));
                                    return;
                                }
                                BleRouterLANActivity.this.showToast(BleRouterLANActivity.this.getResources().getString(R.string.input) + "" + BleRouterLANActivity.this.getResources().getString(R.string.dhcp_first_dns_address));
                                return;
                            }
                            BleRouterLANActivity.this.showToast(BleRouterLANActivity.this.getResources().getString(R.string.input) + "" + BleRouterLANActivity.this.getResources().getString(R.string.dhcp_sub_net_mask));
                            return;
                        }
                        BleRouterLANActivity.this.showToast(BleRouterLANActivity.this.getResources().getString(R.string.input) + "" + BleRouterLANActivity.this.getResources().getString(R.string.dhcp_default_router));
                        return;
                    }
                    BleRouterLANActivity.this.showToast(BleRouterLANActivity.this.getResources().getString(R.string.input) + "" + BleRouterLANActivity.this.getResources().getString(R.string.dhcp_end_address));
                    return;
                }
                BleRouterLANActivity.this.showToast(BleRouterLANActivity.this.getResources().getString(R.string.input) + "" + BleRouterLANActivity.this.getResources().getString(R.string.dhcp_start_address));
                return;
            }
            BleRouterLANActivity.this.showToast(BleRouterLANActivity.this.getResources().getString(R.string.input) + "" + BleRouterLANActivity.this.getResources().getString(R.string.dhcp_lease));
        }
    }
}
