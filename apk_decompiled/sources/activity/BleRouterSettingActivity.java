package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import bean.CameraRemove;
import bean.DeviceInfoBean;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityBleRouterSettingBinding;
import dialog.BaseDialog;
import dialog.UpgradeDialogIpc;
import java.util.ArrayList;
import java.util.HashMap;
import kt.APNActivity;
import kt.DeviceTimingRebootActivity;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import sdk.IPCManager;
import tools.LogEx;
import tools.MyCallback;
import tools.OnMultiClickListener;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class BleRouterSettingActivity extends CommonActivity {
    private ActivityBleRouterSettingBinding binding;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean device2;
    private DeviceInfoBean device3;
    private DeviceInfoBean nvrDevice;
    private UpgradeDialogIpc upgradeDialogIpc;
    private String nvrIotId = "";
    private String shotIotId = "";
    private String iotId = "";
    private int nvrOwner = 0;
    private Handler uiHandler = new Handler();

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_ble_router_setting;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.device2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.device3 = (DeviceInfoBean) extras.getSerializable("device3");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
            DeviceInfoBean deviceInfoBean = this.device;
            if (deviceInfoBean != null) {
                this.iotId = deviceInfoBean.getIotId();
            }
            DeviceInfoBean deviceInfoBean2 = this.device1;
            if (deviceInfoBean2 != null) {
                this.shotIotId = deviceInfoBean2.getIotId();
            }
            this.nvrOwner = getIntent().getIntExtra("nvrOwner", 0);
            DeviceInfoBean deviceInfoBean3 = this.nvrDevice;
            if (deviceInfoBean3 != null) {
                this.nvrIotId = deviceInfoBean3.getIotId();
                this.nvrOwner = this.nvrDevice.getOwned();
            }
            Log.d("设置IotId", "device: " + this.device.getIotId());
            Log.d("设置dn", "device: " + this.device.getDeviceName());
            Log.d("设置pk", "device: " + this.device.getProductKey());
            if (this.device1 != null) {
                Log.d("设置IotId", "device1: " + this.device1.getIotId());
                Log.d("设置dn", "device1: " + this.device1.getDeviceName());
                Log.d("设置pk", "device1: " + this.device1.getProductKey());
            }
            if (this.device2 != null) {
                Log.d("设置IotId", "device2: " + this.device2.getIotId());
                Log.d("设置dn", "device2: " + this.device2.getDeviceName());
                Log.d("设置pk", "device2: " + this.device2.getProductKey());
            }
            if (this.nvrDevice != null) {
                Log.d("设置IotId", "nvrDevice: " + this.nvrDevice.getIotId());
                Log.d("设置dn", "nvrDevice: " + this.nvrDevice.getDeviceName());
                Log.d("设置pk", "nvrDevice: " + this.nvrDevice.getProductKey());
            }
            SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.BleRouterSettingActivity.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
        }
        this.binding = (ActivityBleRouterSettingBinding) DataBindingUtil.setContentView(this, R.layout.activity_ble_router_setting);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.BleRouterSettingActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                BleRouterSettingActivity.this.finish();
            }
        });
        this.binding.itemCameraInfo.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterSettingActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(BleRouterSettingActivity.this, (Class<?>) CameraInfoActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, BleRouterSettingActivity.this.device);
                if (BleRouterSettingActivity.this.device1 != null) {
                    bundle2.putSerializable("device1", BleRouterSettingActivity.this.device1);
                }
                if (BleRouterSettingActivity.this.device2 != null) {
                    bundle2.putSerializable("device2", BleRouterSettingActivity.this.device2);
                }
                if (BleRouterSettingActivity.this.nvrDevice != null) {
                    bundle2.putSerializable("nvrDevice", BleRouterSettingActivity.this.nvrDevice);
                }
                intent.putExtras(bundle2);
                BleRouterSettingActivity.this.startActivity(intent);
            }
        });
        this.binding.itemTimeSetting.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterSettingActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(BleRouterSettingActivity.this, (Class<?>) TimeSettingActivity.class);
                intent.putExtra("iotId", BleRouterSettingActivity.this.device.getIotId());
                BleRouterSettingActivity.this.startActivityForResult(intent, 1);
            }
        });
        this.binding.itemShareManage.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterSettingActivity.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(BleRouterSettingActivity.this, (Class<?>) ShareManageActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, BleRouterSettingActivity.this.device);
                if (BleRouterSettingActivity.this.device1 != null) {
                    bundle2.putSerializable("device1", BleRouterSettingActivity.this.device1);
                }
                if (BleRouterSettingActivity.this.device2 != null) {
                    bundle2.putSerializable("device2", BleRouterSettingActivity.this.device2);
                }
                if (BleRouterSettingActivity.this.nvrDevice != null) {
                    bundle2.putSerializable("nvrDevice", BleRouterSettingActivity.this.nvrDevice);
                }
                intent.putExtras(bundle2);
                BleRouterSettingActivity.this.startActivity(intent);
            }
        });
        this.binding.itemConnectedDevice.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterSettingActivity.6
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(BleRouterSettingActivity.this, (Class<?>) BleRouterConnectedDeviceActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, BleRouterSettingActivity.this.device);
                if (BleRouterSettingActivity.this.device1 != null) {
                    bundle2.putSerializable("device1", BleRouterSettingActivity.this.device1);
                }
                if (BleRouterSettingActivity.this.device2 != null) {
                    bundle2.putSerializable("device2", BleRouterSettingActivity.this.device2);
                }
                if (BleRouterSettingActivity.this.nvrDevice != null) {
                    bundle2.putSerializable("nvrDevice", BleRouterSettingActivity.this.nvrDevice);
                }
                intent.putExtras(bundle2);
                BleRouterSettingActivity.this.startActivity(intent);
            }
        });
        this.binding.itemLan.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterSettingActivity.7
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(BleRouterSettingActivity.this, (Class<?>) BleRouterLANActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, BleRouterSettingActivity.this.device);
                if (BleRouterSettingActivity.this.device1 != null) {
                    bundle2.putSerializable("device1", BleRouterSettingActivity.this.device1);
                }
                if (BleRouterSettingActivity.this.device2 != null) {
                    bundle2.putSerializable("device2", BleRouterSettingActivity.this.device2);
                }
                if (BleRouterSettingActivity.this.nvrDevice != null) {
                    bundle2.putSerializable("nvrDevice", BleRouterSettingActivity.this.nvrDevice);
                }
                intent.putExtras(bundle2);
                BleRouterSettingActivity.this.startActivity(intent);
            }
        });
        this.binding.itemHotspot.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterSettingActivity.8
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(BleRouterSettingActivity.this, (Class<?>) BleRouterHotspotActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, BleRouterSettingActivity.this.device);
                if (BleRouterSettingActivity.this.device1 != null) {
                    bundle2.putSerializable("device1", BleRouterSettingActivity.this.device1);
                }
                if (BleRouterSettingActivity.this.device2 != null) {
                    bundle2.putSerializable("device2", BleRouterSettingActivity.this.device2);
                }
                if (BleRouterSettingActivity.this.nvrDevice != null) {
                    bundle2.putSerializable("nvrDevice", BleRouterSettingActivity.this.nvrDevice);
                }
                intent.putExtras(bundle2);
                BleRouterSettingActivity.this.startActivity(intent);
            }
        });
        this.binding.itemApn.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterSettingActivity.9
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(BleRouterSettingActivity.this.getApplicationContext(), (Class<?>) APNActivity.class);
                intent.putExtra("iotId", BleRouterSettingActivity.this.iotId);
                BleRouterSettingActivity.this.startActivity(intent);
            }
        });
        this.binding.itemFirmwareVersion.setRightText(SharePreferenceManager.getInstance().getFirmwareVersion(this.device.getIotId()));
        this.binding.itemFirmwareVersion.setOnClickListener(new View.OnClickListener() { // from class: activity.BleRouterSettingActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                BleRouterSettingActivity.this.getUpgradeInfo();
            }
        });
        this.binding.itemResetSetting.setOnClickListener(new AnonymousClass11());
        this.binding.itemRestartDev.setOnClickListener(new AnonymousClass12());
        this.binding.btRemoveCamera.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleRouterSettingActivity.13
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(BleRouterSettingActivity.this.getString(R.string.confirm_delete_camera)).leftBtnText(BleRouterSettingActivity.this.getString(R.string.sure_delete)).rightBtnText(BleRouterSettingActivity.this.getString(R.string.cancel)).clickLeft(new View.OnClickListener() { // from class: activity.BleRouterSettingActivity.13.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view3) {
                        if (BleRouterSettingActivity.this.device != null) {
                            BleRouterSettingActivity.this.unbindDevice(BleRouterSettingActivity.this.device.getIotId());
                        }
                        if (BleRouterSettingActivity.this.device1 != null) {
                            BleRouterSettingActivity.this.unbindDevice(BleRouterSettingActivity.this.device1.getIotId());
                        }
                        if (BleRouterSettingActivity.this.device2 != null) {
                            BleRouterSettingActivity.this.unbindDevice(BleRouterSettingActivity.this.device2.getIotId());
                        }
                        if (BleRouterSettingActivity.this.device3 != null) {
                            BleRouterSettingActivity.this.unbindDevice(BleRouterSettingActivity.this.device3.getIotId());
                        }
                        if (BleRouterSettingActivity.this.nvrDevice != null) {
                            BleRouterSettingActivity.this.unbindDevice(BleRouterSettingActivity.this.nvrDevice.getIotId());
                        }
                    }
                }).canCancel(false).create().show(BleRouterSettingActivity.this.getSupportFragmentManager(), "");
            }
        });
    }

    /* JADX INFO: renamed from: activity.BleRouterSettingActivity$11, reason: invalid class name */
    class AnonymousClass11 extends OnMultiClickListener {
        AnonymousClass11() {
        }

        /* JADX INFO: renamed from: activity.BleRouterSettingActivity$11$1, reason: invalid class name */
        class AnonymousClass1 implements View.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCManager.getInstance().getDevice(BleRouterSettingActivity.this.device.getIotId()).reset(new IPanelCallback() { // from class: activity.BleRouterSettingActivity.11.1.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, Object obj) {
                        Log.e(BleRouterSettingActivity.this.TAG, "reset onComplete: " + z);
                        if (!z || obj == null || "".equals(String.valueOf(obj))) {
                            return;
                        }
                        JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                        if (object.containsKey("code")) {
                            if (object.getInteger("code").intValue() != 200) {
                                BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.11.1.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Toast.makeText(BleRouterSettingActivity.this.getActivity(), R.string.reset_setting_failed, 0).show();
                                    }
                                });
                            } else {
                                BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.11.1.1.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Toast.makeText(BleRouterSettingActivity.this.getActivity(), R.string.reset_setting_succeed, 0).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(BleRouterSettingActivity.this.getString(R.string.sure_reset_camera)).leftBtnText(BleRouterSettingActivity.this.getString(R.string.sure_reset)).rightBtnText(BleRouterSettingActivity.this.getString(R.string.cancel)).clickLeft(new AnonymousClass1()).canCancel(false).create().show(BleRouterSettingActivity.this.getSupportFragmentManager(), "");
        }
    }

    /* JADX INFO: renamed from: activity.BleRouterSettingActivity$12, reason: invalid class name */
    class AnonymousClass12 extends OnMultiClickListener {
        AnonymousClass12() {
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            if (((SharePreferenceManager.getInstance().getPageControlEx(BleRouterSettingActivity.this.device.getIotId()) & 128) >> 7) == 1) {
                Intent intent = new Intent(BleRouterSettingActivity.this, (Class<?>) DeviceTimingRebootActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, BleRouterSettingActivity.this.device);
                intent.putExtras(bundle);
                BleRouterSettingActivity.this.startActivity(intent);
                return;
            }
            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(BleRouterSettingActivity.this.getString(R.string.sure_restart_camera)).leftBtnText(BleRouterSettingActivity.this.getString(R.string.sure_restart)).rightBtnText(BleRouterSettingActivity.this.getString(R.string.cancel)).clickLeft(new AnonymousClass1()).canCancel(false).create().show(BleRouterSettingActivity.this.getSupportFragmentManager(), "");
        }

        /* JADX INFO: renamed from: activity.BleRouterSettingActivity$12$1, reason: invalid class name */
        class AnonymousClass1 implements View.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCManager.getInstance().getDevice(BleRouterSettingActivity.this.device.getIotId()).reboot(new IPanelCallback() { // from class: activity.BleRouterSettingActivity.12.1.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, Object obj) {
                        Log.e(BleRouterSettingActivity.this.TAG, "reboot onComplete: " + z);
                        if (!z || obj == null || "".equals(String.valueOf(obj))) {
                            return;
                        }
                        JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                        if (object.containsKey("code")) {
                            if (object.getInteger("code").intValue() != 200) {
                                BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.12.1.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Toast.makeText(BleRouterSettingActivity.this.getActivity(), R.string.restart_dev_failed, 0).show();
                                    }
                                });
                            } else {
                                BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.12.1.1.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Toast.makeText(BleRouterSettingActivity.this.getActivity(), R.string.restart_dev_succeed, 0).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindDevice(final String str) {
        showProgressDialog(R.string.remove_ing);
        HashMap map = new HashMap();
        map.put("iotId", str);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/uc/unbindAccountAndDev").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.BleRouterSettingActivity.14
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, BleRouterSettingActivity.this.TAG, "onFailure");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                LogEx.d(true, BleRouterSettingActivity.this.TAG, "unbindDevice onResponse: code: " + code);
                final String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.14.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BleRouterSettingActivity.this.dismissProgressDialog();
                            Toast.makeText(BleRouterSettingActivity.this.getActivity(), localizedMsg, 0).show();
                        }
                    });
                } else {
                    BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.14.2
                        @Override // java.lang.Runnable
                        public void run() {
                            BleRouterSettingActivity.this.unbindDeviceSucceed(str);
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindDeviceSucceed(String str) {
        dismissProgressDialog();
        Toast.makeText(getActivity(), R.string.delete_succeed, 0).show();
        EventBus.getDefault().post(new CameraRemove(str));
        SharePreferenceManager.getInstance().setFirstNet(str, true);
        finish();
    }

    public void getUpgradeInfo() {
        this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.15
            @Override // java.lang.Runnable
            public void run() {
                BleRouterSettingActivity.this.showProgressDialog();
            }
        });
        HashMap map = new HashMap();
        Log.e("版本", !"".equals(this.nvrIotId) ? this.nvrIotId : this.device.getIotId());
        map.put("iotId", !"".equals(this.nvrIotId) ? this.nvrIotId : this.device.getIotId());
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/ota/info/queryByUser").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass16());
    }

    /* JADX INFO: renamed from: activity.BleRouterSettingActivity$16, reason: invalid class name */
    class AnonymousClass16 implements IoTCallback {
        AnonymousClass16() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, BleRouterSettingActivity.this.TAG, "onFailure");
            BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.16.1
                @Override // java.lang.Runnable
                public void run() {
                    BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getString(R.string.get_version_upgrade_fail));
                    BleRouterSettingActivity.this.dismissProgressDialog();
                }
            });
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            int code = ioTResponse.getCode();
            if (code != 200) {
                if (code == 28601) {
                    BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.16.2
                        @Override // java.lang.Runnable
                        public void run() {
                            BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getString(R.string.no_permission));
                            BleRouterSettingActivity.this.dismissProgressDialog();
                        }
                    });
                    return;
                } else {
                    BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.16.3
                        @Override // java.lang.Runnable
                        public void run() {
                            BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getString(R.string.new_version));
                            BleRouterSettingActivity.this.dismissProgressDialog();
                        }
                    });
                    return;
                }
            }
            Object data = ioTResponse.getData();
            if (data == null || !(data instanceof org.json.JSONObject)) {
                BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.16.7
                    @Override // java.lang.Runnable
                    public void run() {
                        BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getString(R.string.new_version));
                        BleRouterSettingActivity.this.dismissProgressDialog();
                    }
                });
                return;
            }
            try {
                org.json.JSONObject jSONObject = (org.json.JSONObject) data;
                String str = (String) jSONObject.get("currentVersion");
                final String str2 = (String) jSONObject.get("version");
                if (!str.equals(str2)) {
                    BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.16.4
                        @Override // java.lang.Runnable
                        public void run() {
                            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(BleRouterSettingActivity.this.getString(R.string.found_device_upgrade_or_not, new Object[]{str2})).leftBtnText(BleRouterSettingActivity.this.getString(R.string.confirm_upgrade)).rightBtnText(BleRouterSettingActivity.this.getString(R.string.cancel)).clickLeft(new View.OnClickListener() { // from class: activity.BleRouterSettingActivity.16.4.1
                                @Override // android.view.View.OnClickListener
                                public void onClick(View view2) {
                                    BleRouterSettingActivity.this.updateDevice(str2);
                                }
                            }).canCancel(false).create().show(BleRouterSettingActivity.this.getSupportFragmentManager(), "");
                            BleRouterSettingActivity.this.dismissProgressDialog();
                        }
                    });
                } else {
                    BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.16.5
                        @Override // java.lang.Runnable
                        public void run() {
                            new BaseDialog.Builder().view(R.layout.dialog_common).content(BleRouterSettingActivity.this.getString(R.string.new_version)).rightBtnText(BleRouterSettingActivity.this.getString(R.string.known)).canCancel(false).create().show(BleRouterSettingActivity.this.getSupportFragmentManager(), "");
                            BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getString(R.string.new_version));
                            BleRouterSettingActivity.this.dismissProgressDialog();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
                BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.16.6
                    @Override // java.lang.Runnable
                    public void run() {
                        BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getString(R.string.new_version));
                        BleRouterSettingActivity.this.dismissProgressDialog();
                    }
                });
            }
        }
    }

    public void updateDevice(String str) {
        showProgressDialog();
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        arrayList.add(!"".equals(this.nvrIotId) ? this.nvrIotId : this.device.getIotId());
        map.put("iotIds", arrayList);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/ota/batchUpgradeByUser").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass17(str));
    }

    /* JADX INFO: renamed from: activity.BleRouterSettingActivity$17, reason: invalid class name */
    class AnonymousClass17 implements IoTCallback {
        final /* synthetic */ String val$version;

        AnonymousClass17(String str) {
            this.val$version = str;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, BleRouterSettingActivity.this.TAG, "onFailure");
            BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.17.1
                @Override // java.lang.Runnable
                public void run() {
                    BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getString(R.string.can_not_upgrade));
                    BleRouterSettingActivity.this.dismissProgressDialog();
                }
            });
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            if (ioTResponse.getCode() != 200) {
                BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.17.2
                    @Override // java.lang.Runnable
                    public void run() {
                        BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getString(R.string.can_not_upgrade));
                        BleRouterSettingActivity.this.dismissProgressDialog();
                    }
                });
            } else {
                BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.17.3
                    @Override // java.lang.Runnable
                    public void run() {
                        BleRouterSettingActivity.this.dismissProgressDialog();
                        BleRouterSettingActivity.this.upgradeDialogIpc = new UpgradeDialogIpc(BleRouterSettingActivity.this.getActivity(), R.style.progress_dialog);
                        BleRouterSettingActivity.this.upgradeDialogIpc.setOnViewClick(new UpgradeDialogIpc.OnViewClick() { // from class: activity.BleRouterSettingActivity.17.3.1
                            @Override // dialog.UpgradeDialogIpc.OnViewClick
                            public void onDismiss() {
                                if (BleRouterSettingActivity.this.upgradeDialogIpc.getProgress() < 100) {
                                    BleRouterSettingActivity.this.cancelUpgrade(!"".equals(BleRouterSettingActivity.this.nvrIotId) ? BleRouterSettingActivity.this.nvrIotId : BleRouterSettingActivity.this.device.getIotId(), AnonymousClass17.this.val$version);
                                }
                            }
                        });
                        BleRouterSettingActivity.this.upgradeDialogIpc.setCancelable(false);
                        BleRouterSettingActivity.this.upgradeDialogIpc.setCanceledOnTouchOutside(false);
                        BleRouterSettingActivity.this.upgradeDialogIpc.show();
                        BleRouterSettingActivity.this.queryUpgradeStep(!"".equals(BleRouterSettingActivity.this.nvrIotId) ? BleRouterSettingActivity.this.nvrIotId : BleRouterSettingActivity.this.device.getIotId(), AnonymousClass17.this.val$version);
                    }
                });
            }
        }
    }

    public void cancelUpgrade(String str, String str2) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("version", str2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/ota/unupgradeByUser").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.BleRouterSettingActivity.18
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.d(BleRouterSettingActivity.this.TAG, "onFailure");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ioTResponse.getCode();
            }
        });
    }

    public void queryUpgradeStep(final String str, final String str2) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("version", str2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/ota/progress/getByUser").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.BleRouterSettingActivity.19
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.d(BleRouterSettingActivity.this.TAG, "onFailure");
                BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.19.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getResources().getString(R.string.can_not_upgrade));
                        BleRouterSettingActivity.this.failUpgradeDialog();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() != 200) {
                    BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.19.2
                        @Override // java.lang.Runnable
                        public void run() {
                            BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getResources().getString(R.string.can_not_upgrade));
                            BleRouterSettingActivity.this.failUpgradeDialog();
                        }
                    });
                    return;
                }
                if (ioTResponse.getData() == null) {
                    BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.19.9
                        @Override // java.lang.Runnable
                        public void run() {
                            BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getResources().getString(R.string.can_not_upgrade));
                            BleRouterSettingActivity.this.failUpgradeDialog();
                        }
                    });
                    return;
                }
                try {
                    org.json.JSONObject jSONObject = (org.json.JSONObject) ioTResponse.getData();
                    int i = jSONObject.getInt(WifiProvisionUtConst.KEY_STEP);
                    int i2 = jSONObject.getInt("upgradeStatus");
                    if (i2 == 4) {
                        BleRouterSettingActivity.this.uiHandler.postDelayed(new Runnable() { // from class: activity.BleRouterSettingActivity.19.3
                            @Override // java.lang.Runnable
                            public void run() {
                                if (BleRouterSettingActivity.this.isFinishing()) {
                                    return;
                                }
                                BleRouterSettingActivity.this.successUpgradeDialog();
                                BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getResources().getString(R.string.upgrade_version_success));
                                SharePreferenceManager.getInstance().setFirstUpdateVersion(str, str2);
                            }
                        }, 1000L);
                        SettingsCtrl.getInstance().getProperties(str, new MyCallback() { // from class: activity.BleRouterSettingActivity.19.4
                            @Override // tools.MyCallback
                            public void onComplete(boolean z) {
                            }
                        });
                        return;
                    }
                    if (i2 != 2 && i2 != 3) {
                        if (i < 0) {
                            BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.19.6
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (BleRouterSettingActivity.this.isFinishing()) {
                                        return;
                                    }
                                    BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getResources().getString(R.string.upgrade_version_fail));
                                    BleRouterSettingActivity.this.failUpgradeDialog();
                                }
                            });
                            return;
                        } else {
                            BleRouterSettingActivity.this.uiHandler.postDelayed(new Runnable() { // from class: activity.BleRouterSettingActivity.19.7
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (BleRouterSettingActivity.this.isFinishing() || !BleRouterSettingActivity.this.upgradeDialogIpc.isShowing() || BleRouterSettingActivity.this.upgradeDialogIpc.isDownloadSucceed()) {
                                        return;
                                    }
                                    BleRouterSettingActivity.this.queryUpgradeStep(str, str2);
                                }
                            }, 1000L);
                            return;
                        }
                    }
                    BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.19.5
                        @Override // java.lang.Runnable
                        public void run() {
                            if (BleRouterSettingActivity.this.isFinishing()) {
                                return;
                            }
                            BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getResources().getString(R.string.upgrade_version_fail));
                            BleRouterSettingActivity.this.failUpgradeDialog();
                        }
                    });
                } catch (Exception e) {
                    BleRouterSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterSettingActivity.19.8
                        @Override // java.lang.Runnable
                        public void run() {
                            BleRouterSettingActivity.this.showToast(BleRouterSettingActivity.this.getResources().getString(R.string.can_not_upgrade));
                            BleRouterSettingActivity.this.failUpgradeDialog();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void failUpgradeDialog() {
        UpgradeDialogIpc upgradeDialogIpc = this.upgradeDialogIpc;
        if (upgradeDialogIpc == null || !upgradeDialogIpc.isShowing()) {
            return;
        }
        this.upgradeDialogIpc.setTextUpgradingFail();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void successUpgradeDialog() {
        UpgradeDialogIpc upgradeDialogIpc = this.upgradeDialogIpc;
        if (upgradeDialogIpc == null || !upgradeDialogIpc.isShowing()) {
            return;
        }
        this.upgradeDialogIpc.setTextUpgradingSuccess();
    }
}
