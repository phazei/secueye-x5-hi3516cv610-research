package activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.internal.view.SupportMenu;
import androidx.databinding.DataBindingUtil;
import bean.BatteryModel;
import bean.DeviceInfoBean;
import bean.EthernetDhcpConfModel;
import bean.NatWLANConfigModel;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityBleRouterBinding;
import com.taobao.accs.common.Constants;
import sdk.IPCManager;
import tools.MyCallback;
import tools.OnMultiClickListener;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;

/* JADX INFO: loaded from: classes.dex */
public class BleRouterActivity extends CommonActivity {
    private ActivityBleRouterBinding binding;
    private DeviceInfoBean device;
    EthernetDhcpConfModel ethernetDhcpConfModel;
    NatWLANConfigModel natWLANConfigModel;
    private Handler uiHandler = new Handler();

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_ble_router;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
        }
        this.binding = (ActivityBleRouterBinding) DataBindingUtil.setContentView(this, R.layout.activity_ble_router);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.ivBack.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterActivity.1
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                BleRouterActivity.this.finish();
            }
        });
        this.binding.tvTitle.setText(this.device.getName());
        this.binding.layoutLan.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(BleRouterActivity.this, (Class<?>) BleRouterLANActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, BleRouterActivity.this.device);
                intent.putExtras(bundle2);
                BleRouterActivity.this.startActivity(intent);
            }
        });
        this.binding.layoutSetting.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(BleRouterActivity.this, (Class<?>) BleRouterSettingActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, BleRouterActivity.this.device);
                intent.putExtras(bundle2);
                BleRouterActivity.this.startActivity(intent);
            }
        });
        this.binding.layoutWifi.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(BleRouterActivity.this, (Class<?>) BleRouterHotspotActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, BleRouterActivity.this.device);
                intent.putExtras(bundle2);
                BleRouterActivity.this.startActivity(intent);
            }
        });
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    @SuppressLint({"SetTextI18n"})
    protected void onResume() {
        super.onResume();
        SettingsCtrl.getInstance().getProperties(this.device.getIotId(), new MyCallback() { // from class: activity.BleRouterActivity.5
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
        this.binding.ivLine.setImageResource(this.device.getStatus() != 1 ? R.drawable.oval_gray : R.drawable.oval_green);
        this.binding.tvLine.setText(this.device.getStatus() != 1 ? R.string.offline : R.string.online);
        if (SharePreferenceManager.getInstance().getLowPower(this.device.getIotId()) == 1 || SharePreferenceManager.getInstance().getIsRouter(this.device.getIotId()) == 1) {
            BatteryModel batteryModel = (BatteryModel) JSON.parseObject(SharePreferenceManager.getInstance().getLowPowerDeviceStatus(this.device.getIotId()), BatteryModel.class);
            if (batteryModel != null && !batteryModel.Net4GSIMCarrier.isEmpty() && batteryModel.Net4GSIMCarrier != null) {
                this.binding.tvSim.setText("" + batteryModel.Net4GSIMCarrier);
            }
            if (batteryModel != null) {
                if (batteryModel.BatteryCapacity != 0) {
                    this.binding.layoutBattery.setVisibility(0);
                    this.binding.tvBattery.setText("" + batteryModel.BatteryCapacity);
                    this.binding.tvBattery.setVisibility(8);
                    if (batteryModel.SolarPanelVoltage >= 3.8d) {
                        this.binding.ivBatteryCharger.setVisibility(0);
                        this.binding.tvBatteryVoltage.setVisibility(0);
                        this.binding.tvBatteryVoltage.setText(getString(R.string.charging_yes) + "");
                        this.binding.tvBatteryVoltage.setTextColor(-16711936);
                        this.binding.tvBattery.setVisibility(8);
                        if (batteryModel.BatteryCapacity <= 100 && batteryModel.BatteryCapacity >= 80) {
                            this.binding.ivBattery.setImageResource(R.drawable.icon_battery_100_charger);
                        } else if (batteryModel.BatteryCapacity <= 80 && batteryModel.BatteryCapacity >= 60) {
                            this.binding.ivBattery.setImageResource(R.drawable.icon_battery_80_charger);
                        } else if (batteryModel.BatteryCapacity <= 60 && batteryModel.BatteryCapacity >= 40) {
                            this.binding.ivBattery.setImageResource(R.drawable.icon_battery_60_charger);
                        } else if (batteryModel.BatteryCapacity <= 40 && batteryModel.BatteryCapacity >= 1) {
                            this.binding.ivBattery.setImageResource(R.drawable.icon_battery_40_charger);
                        }
                    } else {
                        this.binding.ivBatteryCharger.setVisibility(8);
                        this.binding.tvBatteryVoltage.setVisibility(8);
                        this.binding.tvBatteryVoltage.setText(this.binding.tvBatteryVoltage.getContext().getString(R.string.charging_no) + "");
                        this.binding.tvBatteryVoltage.setTextColor(SupportMenu.CATEGORY_MASK);
                        this.binding.tvBattery.setVisibility(0);
                        if (batteryModel.BatteryCapacity <= 100 && batteryModel.BatteryCapacity >= 80) {
                            this.binding.ivBattery.setImageResource(R.drawable.icon_battery_100_charger);
                        } else if (batteryModel.BatteryCapacity <= 80 && batteryModel.BatteryCapacity >= 60) {
                            this.binding.ivBattery.setImageResource(R.drawable.icon_battery_80_charger);
                        } else if (batteryModel.BatteryCapacity <= 60 && batteryModel.BatteryCapacity >= 40) {
                            this.binding.ivBattery.setImageResource(R.drawable.icon_battery_60_charger);
                        } else if (batteryModel.BatteryCapacity <= 40 && batteryModel.BatteryCapacity >= 20) {
                            this.binding.ivBattery.setImageResource(R.drawable.icon_battery_40_charger);
                        } else if (batteryModel.BatteryCapacity <= 20 && batteryModel.BatteryCapacity > 10) {
                            this.binding.ivBattery.setImageResource(R.drawable.icon_battery_20_10);
                        } else if (batteryModel.BatteryCapacity <= 10 && batteryModel.BatteryCapacity >= 0) {
                            this.binding.ivBattery.setImageResource(R.drawable.icon_battery_10);
                        }
                    }
                } else {
                    this.binding.layoutBattery.setVisibility(8);
                }
            }
            if (SharePreferenceManager.getInstance().getLowPowerStatus(this.device.getIotId()) == 0 && this.device.getStatus() == 1) {
                this.binding.ivLine.setImageResource(R.drawable.oval_yellow);
                this.binding.tvLine.setText(R.string.dormancy);
            }
        }
        if (!SharePreferenceManager.getInstance().getImei(this.device.getIotId()).isEmpty()) {
            this.binding.tvImei.setText("IMEI:" + SharePreferenceManager.getInstance().getImei(this.device.getIotId()));
            if (!SharePreferenceManager.getInstance().getIccId(this.device.getIotId()).isEmpty()) {
                this.binding.tvImei.setText(((Object) this.binding.tvImei.getText()) + "\nICCID:" + SharePreferenceManager.getInstance().getIccId(this.device.getIotId()));
            }
            this.binding.layoutImei.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterActivity.6
                @Override // tools.OnMultiClickListener
                public void onMultiClick(View view2) {
                    Intent intent = new Intent(BleRouterActivity.this, (Class<?>) SIMWebActivity.class);
                    intent.putExtra(Constants.KEY_IMEI, SharePreferenceManager.getInstance().getImei(BleRouterActivity.this.device.getIotId()));
                    BleRouterActivity.this.startActivity(intent);
                }
            });
        }
        this.binding.ivVsimCard.setImageResource(SharePreferenceManager.getInstance().getNet4GVSIMMode(this.device.getIotId()) == 1 ? R.drawable.oval_green : R.drawable.oval_gray);
        this.binding.ivIccid.setImageResource(SharePreferenceManager.getInstance().getNet4GVSIMMode(this.device.getIotId()) == 0 ? R.drawable.oval_green : R.drawable.oval_gray);
        if (SharePreferenceManager.getInstance().getNet4GVSIMMode(this.device.getIotId()) == 0) {
            this.binding.tvCard.setText(R.string.extrapolation_sim);
            if (!SharePreferenceManager.getInstance().getIccId(this.device.getIotId()).isEmpty()) {
                this.binding.tvIccid.setText("ICCID:\n" + SharePreferenceManager.getInstance().getIccId(this.device.getIotId()));
            } else {
                this.binding.tvIccid.setText("ICCID:\n" + getString(R.string.unknown));
            }
            if (!SharePreferenceManager.getInstance().getImei(this.device.getIotId()).isEmpty()) {
                this.binding.tvImei.setText("IMEI:" + SharePreferenceManager.getInstance().getImei(this.device.getIotId()));
            }
        } else if (SharePreferenceManager.getInstance().getNet4GVSIMMode(this.device.getIotId()) == 1) {
            if (!SharePreferenceManager.getInstance().getImei(this.device.getIotId()).isEmpty()) {
                this.binding.tvImei.setText("IMEI:" + SharePreferenceManager.getInstance().getImei(this.device.getIotId()) + "\nICCID:" + SharePreferenceManager.getInstance().getIccId(this.device.getIotId()));
                this.binding.layoutImei.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterActivity.7
                    @Override // tools.OnMultiClickListener
                    public void onMultiClick(View view2) {
                        Intent intent = new Intent(BleRouterActivity.this, (Class<?>) SIMWebActivity.class);
                        intent.putExtra(Constants.KEY_IMEI, SharePreferenceManager.getInstance().getImei(BleRouterActivity.this.device.getIotId()));
                        BleRouterActivity.this.startActivity(intent);
                    }
                });
            } else {
                this.binding.tvImei.setText(getString(R.string.card_name) + SdkConstant.CLOUDAPI_LF + getString(R.string.unknown));
                this.binding.ivVsimCard.setImageResource(R.drawable.oval_gray);
                this.binding.tvCard.setText(R.string.vsim_card);
            }
        }
        if (!SharePreferenceManager.getInstance().getEthernetDhcpConf(this.device.getIotId()).equals("")) {
            this.ethernetDhcpConfModel = (EthernetDhcpConfModel) JSONObject.parseObject(SharePreferenceManager.getInstance().getEthernetDhcpConf(this.device.getIotId()), EthernetDhcpConfModel.class);
            this.binding.dhcpStartAddress.setText(getString(R.string.dhcp_start_address) + "" + this.ethernetDhcpConfModel.DhcpStartIPAddr + "");
            this.binding.dhcpEndAddress.setText(getString(R.string.dhcp_end_address) + "" + this.ethernetDhcpConfModel.DhcpEndIPAddr + "");
        }
        if (!SharePreferenceManager.getInstance().getNatWLANSwitchConfig(this.device.getIotId()).equals("")) {
            this.natWLANConfigModel = (NatWLANConfigModel) JSONObject.parseObject(SharePreferenceManager.getInstance().getNatWLANSwitchConfig(this.device.getIotId()), NatWLANConfigModel.class);
            if (!this.natWLANConfigModel.SSID.equals("")) {
                this.binding.tvWifiName.setText(getString(R.string.ap_name) + "" + this.natWLANConfigModel.SSID);
            }
            if (!this.natWLANConfigModel.WPA_PASSPHRASE.equals("")) {
                this.binding.tvWifiPass.setText(getString(R.string.ap_pass) + "" + this.natWLANConfigModel.WPA_PASSPHRASE);
            }
        }
        this.binding.tvLanSize.setText(getString(R.string.lan) + ":0");
        if (SharePreferenceManager.getInstance().getNatETHSwitch(this.device.getIotId()) == 1) {
            IPCManager.getInstance().getDevice(this.device.getIotId()).getNatModeConnectDeviceQuery(0, new IPanelCallback() { // from class: activity.BleRouterActivity.8
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(final boolean z, @Nullable final Object obj) {
                    BleRouterActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterActivity.8.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Object obj2;
                            if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                return;
                            }
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code")) {
                                if (object.getInteger("code").intValue() != 200) {
                                    Toast.makeText(BleRouterActivity.this.getActivity(), R.string.restart_dev_failed, 0).show();
                                    return;
                                }
                                JSONArray jSONArray = object.getJSONObject("data").getJSONArray("ConnectDeviceInfo");
                                BleRouterActivity.this.binding.tvLanSize.setText(BleRouterActivity.this.getString(R.string.lan) + ":" + jSONArray.size());
                            }
                        }
                    });
                }
            });
        }
        this.binding.tvHotspotSize.setText(getString(R.string.hotspot) + ":0");
        if (SharePreferenceManager.getInstance().getNatWLANSwitch(this.device.getIotId()) == 1) {
            new Handler().postDelayed(new AnonymousClass9(), 500L);
        }
        this.binding.layoutConnect.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterActivity.10
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(BleRouterActivity.this, (Class<?>) BleRouterConnectedDeviceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, BleRouterActivity.this.device);
                intent.putExtras(bundle);
                BleRouterActivity.this.startActivity(intent);
            }
        });
    }

    /* JADX INFO: renamed from: activity.BleRouterActivity$9, reason: invalid class name */
    class AnonymousClass9 implements Runnable {
        AnonymousClass9() {
        }

        @Override // java.lang.Runnable
        public void run() {
            IPCManager.getInstance().getDevice(BleRouterActivity.this.device.getIotId()).getNatModeConnectDeviceQuery(1, new IPanelCallback() { // from class: activity.BleRouterActivity.9.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(final boolean z, @Nullable final Object obj) {
                    BleRouterActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterActivity.9.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Object obj2;
                            if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                return;
                            }
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code")) {
                                if (object.getInteger("code").intValue() != 200) {
                                    Toast.makeText(BleRouterActivity.this.getActivity(), R.string.restart_dev_failed, 0).show();
                                    return;
                                }
                                JSONArray jSONArray = object.getJSONObject("data").getJSONArray("ConnectDeviceInfo");
                                BleRouterActivity.this.binding.tvHotspotSize.setText(BleRouterActivity.this.getString(R.string.hotspot) + ":" + jSONArray.size());
                            }
                        }
                    });
                }
            });
        }
    }
}
