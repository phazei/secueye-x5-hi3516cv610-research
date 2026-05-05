package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import bean.CameraNameModify;
import bean.DeviceInfoBean;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.seculink.app.R;
import config.Constants;
import dialog.InputDialogView;
import dialog.SelectDialogView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.greenrobot.eventbus.EventBus;
import sdk.ChannelManager;
import sdk.IPCManager;
import tools.LogEx;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.ItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class CameraSettingActivity extends CommonActivity implements View.OnClickListener {
    private int currentInfrarred;
    private DeviceInfoBean device;
    private TitleView fl_titlebar;
    private SelectDialogView infrarredDialog;
    private String[] infrarredMode;
    private String[] infrarredModeValue;
    private ItemView itemInfrarred;
    private ItemView itemNameSetting;
    private ItemView itemResetSetting;
    private ItemView itemRestartDev;
    private ItemView itemTimeSetting;
    LinearLayout layout_main;
    private InputDialogView nameSettingDialog;
    private Handler mHandler = new Handler();
    private Handler uiHandler = new Handler();
    private SharePreferenceManager.OnCallSetListener mSPModifyListener = new SharePreferenceManager.OnCallSetListener() { // from class: activity.CameraSettingActivity.9
        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(String str, final String str2) {
            CameraSettingActivity.this.mHandler.post(new Runnable() { // from class: activity.CameraSettingActivity.9.1
                @Override // java.lang.Runnable
                public void run() {
                    String str3 = str2;
                    if (str3 == null || str3.trim().equals("") || str2.equals(CameraSettingActivity.this.getString(R.string.device_default_key))) {
                        return;
                    }
                    if (str2.equals(CameraSettingActivity.this.getString(R.string.device_time_key))) {
                        CameraSettingActivity.this.getDevTime();
                    } else if (!str2.equals(CameraSettingActivity.this.getString(R.string.speaker_switch_key)) && str2.equals(CameraSettingActivity.this.getString(R.string.day_night_mode_key))) {
                        CameraSettingActivity.this.currentInfrarred = SharePreferenceManager.getInstance().getDayNightMode(CameraSettingActivity.this.device.getIotId());
                        CameraSettingActivity.this.itemInfrarred.setRightText(CameraSettingActivity.this.infrarredMode[CameraSettingActivity.this.currentInfrarred]);
                    }
                }
            });
        }
    };
    private ChannelManager.IMobileMsgListener iMobileMsgListener = new ChannelManager.IMobileMsgListener() { // from class: activity.CameraSettingActivity.10
        @Override // sdk.ChannelManager.IMobileMsgListener
        public void onCommand(String str, String str2) {
            Log.e(CameraSettingActivity.this.TAG, "ChannelManager.IMobileMsgListener    topic:" + str + "     msg:" + str2);
            if (str.equals("/thing/properties")) {
                SettingsCtrl.getInstance().getProperties(CameraSettingActivity.this.device.getIotId(), new MyCallback() { // from class: activity.CameraSettingActivity.10.1
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
            }
        }
    };

    private void resetSetting(String str, int i) {
    }

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_camera_setting;
    }

    public static void start(Context context, DeviceInfoBean deviceInfoBean) {
        Intent intent = new Intent(context, (Class<?>) CameraSettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, deviceInfoBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
        }
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.CameraSettingActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                CameraSettingActivity.this.finish();
            }
        });
        this.itemNameSetting = (ItemView) findViewById(R.id.item_name_setting);
        this.itemTimeSetting = (ItemView) findViewById(R.id.item_time_setting);
        this.itemRestartDev = (ItemView) findViewById(R.id.item_restart_dev);
        this.itemResetSetting = (ItemView) findViewById(R.id.item_reset_setting);
        this.itemInfrarred = (ItemView) findViewById(R.id.item_infrared);
        findViewById(R.id.item_name_setting).setOnClickListener(this);
        findViewById(R.id.item_time_setting).setOnClickListener(this);
        findViewById(R.id.item_infrared).setOnClickListener(this);
        this.itemRestartDev.setOnClickListener(this);
        this.itemResetSetting.setOnClickListener(this);
        this.nameSettingDialog = new InputDialogView.Builder().title(getString(R.string.name_setting)).build();
        this.nameSettingDialog.addOnClickListener(new InputDialogView.OnClickListener() { // from class: activity.CameraSettingActivity.2
            @Override // dialog.InputDialogView.OnClickListener
            public void onNegativeClick() {
            }

            @Override // dialog.InputDialogView.OnClickListener
            public void onPositiveClick(String str, Object obj) {
                CameraSettingActivity cameraSettingActivity = CameraSettingActivity.this;
                cameraSettingActivity.modifyDevName(cameraSettingActivity.device.getIotId(), str);
            }
        });
        this.infrarredMode = getResources().getStringArray(R.array.InfrarredMode);
        this.infrarredModeValue = getResources().getStringArray(R.array.InfrarredModeValue);
        this.infrarredDialog = new SelectDialogView.Builder().title(getString(R.string.motion_detect_sensitivity_title)).selectItems(this.infrarredMode).isAllowCancel(false).build();
        this.infrarredDialog.addOnClickListener(new SelectDialogView.OnClickListener() { // from class: activity.CameraSettingActivity.3
            @Override // dialog.SelectDialogView.OnClickListener
            public void onNegativeClick() {
            }

            @Override // dialog.SelectDialogView.OnClickListener
            public void onPositiveClick(int i, String str, Object obj) {
                CameraSettingActivity cameraSettingActivity = CameraSettingActivity.this;
                cameraSettingActivity.updateDevParam(cameraSettingActivity.getString(R.string.day_night_mode_key), CameraSettingActivity.this.infrarredModeValue[i]);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDevParam(String str, final Object obj) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.day_night_mode_key))) {
            map.put(Constants.DAY_NIGHT_MODE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.device.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.CameraSettingActivity.4
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            CameraSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.CameraSettingActivity.4.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(CameraSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setDayNightMode(CameraSettingActivity.this.device.getIotId(), Integer.parseInt(obj.toString()));
                            CameraSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.CameraSettingActivity.4.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(CameraSettingActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void modifyDevName(final String str, final String str2) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("nickName", str2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/uc/setDeviceNickName").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CameraSettingActivity.5
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, CameraSettingActivity.this.TAG, "onFailure");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                LogEx.d(true, CameraSettingActivity.this.TAG, "modifyDevName onResponse: code: " + code);
                final String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    CameraSettingActivity.this.mHandler.post(new Runnable() { // from class: activity.CameraSettingActivity.5.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(CameraSettingActivity.this, localizedMsg, 0).show();
                        }
                    });
                } else {
                    CameraSettingActivity.this.mHandler.post(new Runnable() { // from class: activity.CameraSettingActivity.5.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(CameraSettingActivity.this, R.string.mofify_succeed, 0).show();
                            CameraSettingActivity.this.itemNameSetting.setRightText(str2);
                            EventBus.getDefault().post(new CameraNameModify(str2, str));
                        }
                    });
                }
            }
        });
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        this.itemNameSetting.setRightText(this.device.getName());
        SharePreferenceManager.getInstance().registerOnCallSetListener(this.mSPModifyListener);
        this.currentInfrarred = SharePreferenceManager.getInstance().getDayNightMode(this.device.getIotId());
        this.itemInfrarred.setRightText(this.infrarredMode[this.currentInfrarred]);
        getDevTime();
        SettingsCtrl.getInstance().getProperties(this.device.getIotId(), new MyCallback() { // from class: activity.CameraSettingActivity.6
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
        ChannelManager.getInstance().registerListener(this.iMobileMsgListener);
    }

    private void loadComplete(final String str) {
        this.mHandler.post(new Runnable() { // from class: activity.CameraSettingActivity.7
            @Override // java.lang.Runnable
            public void run() {
                CameraSettingActivity.this.dismissProgressDialog();
                CameraSettingActivity.this.currentInfrarred = SharePreferenceManager.getInstance().getDayNightMode(CameraSettingActivity.this.device.getIotId());
                CameraSettingActivity.this.itemInfrarred.setRightText(CameraSettingActivity.this.infrarredMode[CameraSettingActivity.this.currentInfrarred]);
                CameraSettingActivity.this.getDevTime();
                if (str != null) {
                    Toast.makeText(CameraSettingActivity.this.getActivity(), str, 0).show();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getDevTime() {
        int deviceTime = SharePreferenceManager.getInstance().getDeviceTime(this.device.getIotId());
        SharePreferenceManager.getInstance().getDeviceTZ(this.device.getIotId());
        Log.e(this.TAG, "getDeviceTime(): " + deviceTime);
        this.itemTimeSetting.setRightText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(((long) deviceTime) * 1000)));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.item_name_setting) {
            this.nameSettingDialog.setContent(this.itemNameSetting.getRightText());
            this.nameSettingDialog.show(getSupportFragmentManager(), this.TAG);
            return;
        }
        if (view2.getId() == R.id.ib_back) {
            finish();
            return;
        }
        if (view2.getId() == R.id.item_time_setting) {
            Intent intent = new Intent(this, (Class<?>) TimeSettingActivity.class);
            intent.putExtra("iotId", this.device.getIotId());
            startActivityForResult(intent, 1);
        } else if (view2.getId() == R.id.item_restart_dev) {
            this.itemRestartDev.setEnabled(false);
            IPCManager.getInstance().getDevice(this.device.getIotId()).reboot(new IPanelCallback() { // from class: activity.CameraSettingActivity.8
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj) {
                    Log.e(CameraSettingActivity.this.TAG, "reboot onComplete: " + z);
                    if (z) {
                        CameraSettingActivity.this.mHandler.post(new Runnable() { // from class: activity.CameraSettingActivity.8.1
                            @Override // java.lang.Runnable
                            public void run() {
                                CameraSettingActivity.this.itemRestartDev.setEnabled(true);
                                Toast.makeText(CameraSettingActivity.this.getActivity(), R.string.restart_dev_succeed, 0).show();
                            }
                        });
                    }
                }
            });
        } else if (view2.getId() == R.id.item_reset_setting) {
            resetSetting(getString(R.string.device_default_key), 1);
        } else if (view2.getId() == R.id.item_infrared) {
            this.infrarredDialog.setSelectStr(this.infrarredMode[this.currentInfrarred]);
            this.infrarredDialog.show(getSupportFragmentManager(), this.TAG);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i2 == -1 && i == 1) {
            this.itemTimeSetting.setRightText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(((long) SharePreferenceManager.getInstance().getDeviceTime(this.device.getIotId())) * 1000)));
        }
    }
}
