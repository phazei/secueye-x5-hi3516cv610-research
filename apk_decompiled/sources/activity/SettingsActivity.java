package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import bean.CameraNameModify;
import bean.CameraRemove;
import bean.DeviceInfoBean;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tools.ut.AUserTrack;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.seculink.app.R;
import config.AppConfig;
import config.Constants;
import dialog.BaseDialog;
import dialog.SelectDialogView;
import dialog.UpgradeDialogIpc;
import event.EventType;
import event.MyEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import kt.APNActivity;
import kt.DeviceTimingRebootActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sdk.ChannelManager;
import sdk.IPCManager;
import tools.DateUtil;
import tools.DensityUtil;
import tools.LogEx;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.ItemView;
import view.LongItemView;
import view.SelectorDialogFragment;
import view.SettingTitleView;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class SettingsActivity extends CommonActivity implements View.OnClickListener {
    private String IccId;
    int WifiConfigIsExist;
    private ItemView apnItem;
    LongItemView auto_door;
    private CountDownTimer countDownTimer;
    private int currentInfrarred;
    private int currentMotionDetectSensitivity;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean device2;
    private DeviceInfoBean device3;
    private String[] faceDetectSensitityArr;
    private int faceDetectSensitivity;
    private TitleView fl_titlebar;
    LongItemView gunball_setting;
    LongItemView image_setting;
    private String[] infrarredMode;
    private int isSupport4G;
    private boolean isYunServiceEventPlanChecked;
    LongItemView itemAlarmMode;
    ItemView itemCameraCover;
    LongItemView itemCameraInfo;
    LongItemView itemCameraSetting;
    ItemView itemDayLight;
    ItemView itemDynamicVideo;
    LongItemView itemFirmwareVersion;
    LongItemView itemHumanDetection;
    LongItemView itemLightMode;
    ItemView itemMotionDetection;
    LongItemView itemMotionDetectionSetting;
    LongItemView itemMyCardStorage;
    LongItemView itemMyCloudSettings;
    LongItemView itemMyCloudStorage;
    LongItemView itemNetworkInfo;
    LongItemView itemRecordSetting;
    LongItemView itemResetSetting;
    LongItemView itemRestartDev;
    ItemView itemShareManage;
    ItemView itemSoundLightDetection;
    ItemView itemTimeSetting;
    LongItemView itemTrafficPay;
    LongItemView itemYunPay;
    LongItemView item_4g_switch;
    ItemView item_low_power;
    LongItemView item_nat_share;
    ItemView item_power_model;
    private LinearLayout layout_main;
    private LinearLayout ll_layout;
    private String[] motionDetectSensitivity;
    private String[] motionDetectSensitivityValue;
    private String[] net4GModeArr;
    private SelectorDialogFragment net4gModeFragment;
    SettingTitleView network;
    private SelectorDialogFragment nightModeFragment;
    private DeviceInfoBean nvrDevice;
    private float remainStorage;
    SettingTitleView safety;
    private SelectDialogView sensitivityDialog;
    ItemView smart_cloud;
    SettingTitleView smart_device;
    SettingTitleView storage;
    private String[] storageStatus;
    private int storageStatusValues;
    SettingTitleView stvYunPay;
    private int supportMotionDetect;
    private Switch swCameraCover;
    private Switch swDynamicVideo;
    private Switch swHumanDetection;
    private SwitchCompat swYunServiceEventPlan;
    private SelectorDialogFragment switch4gFragment;
    private SwitchCompat switchFor4G;
    LongItemView switch_4g;
    private String[] switch_4gArr;
    private float totalStorage;
    private UpgradeDialogIpc upgradeDialogIpc;
    private String[] yunServiceRecordMode;
    private String iotId = "";
    private Handler uiHandler = new Handler();
    private int wifiFourPosition = 0;
    private String nvrIotId = "";
    private String shotIotId = "";
    private int nvrOwner = 0;
    private int net4gSwitch = 1;
    private Handler wakeUpHandler = new Handler() { // from class: activity.SettingsActivity.1
        @Override // android.os.Handler
        public void handleMessage(@NonNull Message message) {
            if (message.what == 2) {
                SettingsActivity.this.sendLowPowerAppStatus();
                SettingsActivity.this.sendLowPowerAppStatusHandel();
            }
        }
    };
    private ChannelManager.IMobileMsgListener iMobileMsgListener = new ChannelManager.IMobileMsgListener() { // from class: activity.SettingsActivity.2
        @Override // sdk.ChannelManager.IMobileMsgListener
        public void onCommand(String str, String str2) {
            Log.e(SettingsActivity.this.TAG, "ChannelManager.IMobileMsgListener    topic:" + str + "     msg:" + str2);
            str.equals("/thing/properties");
        }
    };
    private SharePreferenceManager.OnCallSetListener mSPModifyListener = new SharePreferenceManager.OnCallSetListener() { // from class: activity.SettingsActivity.37
        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(final String str, final String str2) {
            SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.37.1
                @Override // java.lang.Runnable
                public void run() {
                    String str3 = str2;
                    if (str3 == null || str3.trim().equals("") || str2.equals(SettingsActivity.this.getString(R.string.mic_switch_key)) || str2.equals(SettingsActivity.this.getString(R.string.speaker_switch_key)) || str2.equals(SettingsActivity.this.getString(R.string.status_light_switch_key))) {
                        return;
                    }
                    if (str2.equals(SettingsActivity.this.getString(R.string.day_night_mode_key))) {
                        SettingsActivity.this.currentInfrarred = SharePreferenceManager.getInstance().getDayNightMode(SettingsActivity.this.device.getIotId());
                        SettingsActivity.this.itemLightMode.setVisibility(0);
                        SettingsActivity.this.safety.setVisibility(0);
                        SettingsActivity.this.itemAlarmMode.setLineShow(true);
                        return;
                    }
                    if (str2.equals(SettingsActivity.this.getString(R.string.stream_video_quality_key)) || str2.equals(SettingsActivity.this.getString(R.string.subStream_video_quality_key)) || str2.equals(SettingsActivity.this.getString(R.string.image_flip_status_key)) || str2.equals(SettingsActivity.this.getString(R.string.encrypt_switch_key)) || str2.equals(SettingsActivity.this.getString(R.string.alarm_switch_key))) {
                        return;
                    }
                    if (str2.equals(SettingsActivity.this.getString(R.string.motion_detect_sensitivity_key))) {
                        SettingsActivity.this.currentMotionDetectSensitivity = SharePreferenceManager.getInstance().getMotionDetectSensitivity(str);
                        if (SettingsActivity.this.motionDetectSensitivity.length - 1 < SettingsActivity.this.currentMotionDetectSensitivity) {
                            SettingsActivity.this.currentMotionDetectSensitivity = SettingsActivity.this.motionDetectSensitivity.length - 1;
                        }
                        SettingsActivity.this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(str);
                        if (SettingsActivity.this.supportMotionDetect != 0) {
                            SettingsActivity.this.itemMotionDetectionSetting.setRightText(SettingsActivity.this.motionDetectSensitivity[SettingsActivity.this.currentMotionDetectSensitivity]);
                            return;
                        }
                        return;
                    }
                    if (str2.equals(SettingsActivity.this.getString(R.string.face_detect_key))) {
                        SettingsActivity.this.faceDetectSensitivity = SharePreferenceManager.getInstance().getFaceDetectMode(str);
                        SettingsActivity.this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(str);
                        if (SettingsActivity.this.supportMotionDetect != 0 || SettingsActivity.this.faceDetectSensitivity == -1) {
                            return;
                        }
                        SettingsActivity.this.itemMotionDetectionSetting.setRightText(SettingsActivity.this.faceDetectSensitityArr[SettingsActivity.this.faceDetectSensitivity]);
                        return;
                    }
                    if (str2.equals(SettingsActivity.this.getString(R.string.voice_detect_sensitivity_key)) || str2.equals(SettingsActivity.this.getString(R.string.alarm_frequency_level_key))) {
                        return;
                    }
                    if (str2.equals(SettingsActivity.this.getString(R.string.storage_status_key))) {
                        SettingsActivity.this.storageStatusValues = SharePreferenceManager.getInstance().getStorageStatus(str);
                        if (SettingsActivity.this.storageStatusValues == 0) {
                            SettingsActivity.this.itemMyCardStorage.setRightText(SettingsActivity.this.storageStatus[0]);
                            SettingsActivity.this.itemMyCardStorage.setEnabled(false);
                            return;
                        }
                        SettingsActivity.this.totalStorage = SharePreferenceManager.getInstance().getStorageTotalCapacity(str) / 1024.0f;
                        SettingsActivity.this.remainStorage = SharePreferenceManager.getInstance().getStorageRemainingCapacity(str) / 1024.0f;
                        Log.e(SettingsActivity.this.TAG, "totalStorage: " + SettingsActivity.this.totalStorage + "  remainStorage: " + SettingsActivity.this.remainStorage);
                        new DecimalFormat("0.00").format((double) SettingsActivity.this.totalStorage);
                        if (SettingsActivity.this.storageStatusValues == 4) {
                            SettingsActivity.this.itemMyCardStorage.setRightText(SettingsActivity.this.getString(R.string.card_inserted));
                            SettingsActivity.this.itemMyCardStorage.setEnabled(true);
                        } else {
                            SettingsActivity.this.itemMyCardStorage.setRightText(SettingsActivity.this.getString(R.string.card_inserted));
                            SettingsActivity.this.itemMyCardStorage.setEnabled(true);
                        }
                        SettingsActivity.this.itemMyCardStorage.setRightText(SettingsActivity.this.getString(R.string.card_inserted));
                        SettingsActivity.this.itemMyCardStorage.setEnabled(true);
                        return;
                    }
                    if (str2.equals(SettingsActivity.this.getString(R.string.storage_total_capacity_key)) || str2.equals(SettingsActivity.this.getString(R.string.storage_remain_capacity_key)) || str2.equals(SettingsActivity.this.getString(R.string.storage_record_mode_key))) {
                        return;
                    }
                    if (str2.equals(SettingsActivity.this.getString(R.string.device_owner_key))) {
                        if ("".equals(SettingsActivity.this.nvrIotId) || SettingsActivity.this.nvrIotId.equals("")) {
                            if (SettingsActivity.this.device == null || SettingsActivity.this.device.getOwned() != 1) {
                                return;
                            }
                            SettingsActivity.this.ll_layout.setVisibility(0);
                            return;
                        }
                        if (SettingsActivity.this.device == null || SettingsActivity.this.nvrOwner != 1 || SettingsActivity.this.device == null || SettingsActivity.this.nvrOwner != 1) {
                            return;
                        }
                        SettingsActivity.this.ll_layout.setVisibility(0);
                        return;
                    }
                    if (str2.equals(SettingsActivity.this.getString(R.string.firmware_version_key))) {
                        SettingsActivity.this.itemFirmwareVersion.setRightText(SharePreferenceManager.getInstance().getFirmwareVersion(!"".equals(SettingsActivity.this.nvrIotId) ? SettingsActivity.this.nvrIotId : SettingsActivity.this.device.getIotId()));
                        return;
                    }
                    if (str2.equals(SettingsActivity.this.getString(R.string.device_time_key)) || str2.equals(SettingsActivity.this.getString(R.string.device_tz_key))) {
                        SettingsActivity.this.getDevTime();
                        return;
                    }
                    if (str2.equals(SettingsActivity.this.getString(R.string.support_4g_key))) {
                        SettingsActivity.this.isSupport4G = SharePreferenceManager.getInstance().getSupport4G(SettingsActivity.this.device.getIotId());
                        if (SettingsActivity.this.isSupport4G == 1) {
                            if ("".equals(SettingsActivity.this.nvrIotId) || SettingsActivity.this.nvrIotId.equals("")) {
                                if (SettingsActivity.this.device == null || SettingsActivity.this.device.getOwned() != 1) {
                                    return;
                                }
                                SettingsActivity.this.itemShareManage.setLineShow(true);
                                SettingsActivity.this.itemTrafficPay.setVisibility(0);
                                SettingsActivity.this.stvYunPay.setVisibility(8);
                                return;
                            }
                            if (SettingsActivity.this.device == null || SettingsActivity.this.nvrOwner != 1) {
                                return;
                            }
                            SettingsActivity.this.itemShareManage.setLineShow(true);
                            SettingsActivity.this.itemTrafficPay.setVisibility(0);
                            SettingsActivity.this.stvYunPay.setVisibility(8);
                            return;
                        }
                        SettingsActivity.this.itemShareManage.setLineShow(false);
                        SettingsActivity.this.itemTrafficPay.setVisibility(8);
                        SettingsActivity.this.stvYunPay.setVisibility(8);
                        return;
                    }
                    if (str2.equals(SettingsActivity.this.getString(R.string.iccid_key))) {
                        SettingsActivity.this.IccId = SharePreferenceManager.getInstance().getIccId(SettingsActivity.this.device.getIotId());
                        return;
                    }
                    if (str2.equals(SettingsActivity.this.getString(R.string.Net4GMode)) && SettingsActivity.this.device != null && SettingsActivity.this.device.getOwned() == 1) {
                        SettingsActivity.this.net4gSwitch = SharePreferenceManager.getInstance().getNet4GMode(str);
                        SettingsActivity.this.item_4g_switch.setVisibility(0);
                        SettingsActivity.this.item_4g_switch.setRightText(SettingsActivity.this.net4GModeArr[SettingsActivity.this.net4gSwitch != 1 ? (char) 1 : (char) 0]);
                    }
                }
            });
        }
    };

    interface CallBack {
        void finish();
    }

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.layout_settings;
    }

    public String getIotId() {
        return this.iotId;
    }

    private void dismissUpgradeDialog() {
        UpgradeDialogIpc upgradeDialogIpc = this.upgradeDialogIpc;
        if (upgradeDialogIpc == null || !upgradeDialogIpc.isShowing()) {
            return;
        }
        this.upgradeDialogIpc.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendLowPowerAppStatusHandel() {
        Message messageObtain = Message.obtain();
        messageObtain.what = 2;
        this.wakeUpHandler.sendMessageDelayed(messageObtain, 30000L);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyEvent myEvent) {
        if (myEvent != null && myEvent.getTYPE() == EventType.FINISH_ACTIVITY) {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cameraNameModify(CameraNameModify cameraNameModify) {
        this.device.setNickName(cameraNameModify.getName());
        this.itemCameraInfo.setRightText(this.device.getName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getDevTime() {
        int deviceTime = SharePreferenceManager.getInstance().getDeviceTime(this.device.getIotId());
        String deviceTZ = SharePreferenceManager.getInstance().getDeviceTZ(this.device.getIotId());
        Log.e(this.TAG, "getDeviceTime(): " + deviceTime);
        Date date = new Date(((long) deviceTime) * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(deviceTZ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.itemTimeSetting.setRightText(simpleDateFormat.format(date));
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 1) {
            int deviceTime = SharePreferenceManager.getInstance().getDeviceTime(this.device.getIotId());
            String deviceTZ = SharePreferenceManager.getInstance().getDeviceTZ(this.device.getIotId());
            Date date = new Date(((long) deviceTime) * 1000);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone(deviceTZ));
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.itemTimeSetting.setRightText(simpleDateFormat.format(date));
        }
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        DeviceInfoBean deviceInfoBean;
        super.initWidget(bundle);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.device2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.device3 = (DeviceInfoBean) extras.getSerializable("device3");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
            DeviceInfoBean deviceInfoBean2 = this.device;
            if (deviceInfoBean2 != null) {
                this.iotId = deviceInfoBean2.getIotId();
            }
            DeviceInfoBean deviceInfoBean3 = this.device1;
            if (deviceInfoBean3 != null) {
                this.shotIotId = deviceInfoBean3.getIotId();
            }
            this.nvrOwner = getIntent().getIntExtra("nvrOwner", 0);
            DeviceInfoBean deviceInfoBean4 = this.nvrDevice;
            if (deviceInfoBean4 != null) {
                this.nvrIotId = deviceInfoBean4.getIotId();
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
            if (this.device3 != null) {
                Log.d("设置IotId", "device2: " + this.device3.getIotId());
                Log.d("设置dn", "device2: " + this.device3.getDeviceName());
                Log.d("设置pk", "device2: " + this.device3.getProductKey());
            }
            if (this.nvrDevice != null) {
                Log.d("设置IotId", "nvrDevice: " + this.nvrDevice.getIotId());
                Log.d("设置dn", "nvrDevice: " + this.nvrDevice.getDeviceName());
                Log.d("设置pk", "nvrDevice: " + this.nvrDevice.getProductKey());
            }
            sendLowPowerAppStatus();
            sendLowPowerAppStatusHandel();
        }
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.SettingsActivity.3
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                SettingsActivity.this.finish();
            }
        });
        this.itemTimeSetting = (ItemView) findViewById(R.id.item_time_setting);
        this.image_setting = (LongItemView) findViewById(R.id.image_setting);
        this.gunball_setting = (LongItemView) findViewById(R.id.gunball_setting);
        findViewById(R.id.item_time_setting).setOnClickListener(this);
        findViewById(R.id.bt_remove_camera).setOnClickListener(this);
        this.itemCameraCover = (ItemView) findViewById(R.id.item_camera_cover);
        this.itemCameraInfo = (LongItemView) findViewById(R.id.item_camera_info);
        this.itemCameraSetting = (LongItemView) findViewById(R.id.item_camera_setting);
        this.itemMotionDetection = (ItemView) findViewById(R.id.item_motion_detection);
        this.itemMotionDetectionSetting = (LongItemView) findViewById(R.id.item_motion_detection_setting);
        this.itemHumanDetection = (LongItemView) findViewById(R.id.item_human_detection);
        this.itemSoundLightDetection = (ItemView) findViewById(R.id.item_sound_light_detection);
        this.itemNetworkInfo = (LongItemView) findViewById(R.id.item_network_info);
        this.item_nat_share = (LongItemView) findViewById(R.id.item_nat_share);
        this.itemMyCloudStorage = (LongItemView) findViewById(R.id.item_my_cloud_storage);
        this.itemMyCloudSettings = (LongItemView) findViewById(R.id.item_my_cloud_setting);
        this.itemMyCardStorage = (LongItemView) findViewById(R.id.item_my_card_storage);
        this.itemDynamicVideo = (ItemView) findViewById(R.id.item_dynamic_video);
        this.itemShareManage = (ItemView) findViewById(R.id.item_share_manage);
        this.itemRecordSetting = (LongItemView) findViewById(R.id.item_record_setting);
        this.itemFirmwareVersion = (LongItemView) findViewById(R.id.item_firmware_version);
        this.itemDayLight = (ItemView) findViewById(R.id.item_day_light);
        this.itemLightMode = (LongItemView) findViewById(R.id.item_night_mode);
        this.itemAlarmMode = (LongItemView) findViewById(R.id.item_alarm_mode);
        this.itemYunPay = (LongItemView) findViewById(R.id.item_yun_pay);
        this.itemTrafficPay = (LongItemView) findViewById(R.id.item_traffic_pay);
        this.ll_layout = (LinearLayout) findViewById(R.id.ll_layout);
        this.stvYunPay = (SettingTitleView) findViewById(R.id.stv_yun_pay);
        this.safety = (SettingTitleView) findViewById(R.id.Safety);
        this.network = (SettingTitleView) findViewById(R.id.network);
        this.storage = (SettingTitleView) findViewById(R.id.storage);
        this.switch_4g = (LongItemView) findViewById(R.id.switch_4g);
        this.auto_door = (LongItemView) findViewById(R.id.auto_door);
        this.smart_device = (SettingTitleView) findViewById(R.id.smart_device);
        this.smart_cloud = (ItemView) findViewById(R.id.smart_cloud);
        this.item_4g_switch = (LongItemView) findViewById(R.id.item_4g_switch);
        this.apnItem = (ItemView) findViewById(R.id.apn_switch);
        this.item_low_power = (ItemView) findViewById(R.id.item_low_power);
        this.item_power_model = (ItemView) findViewById(R.id.item_power_model);
        this.smart_cloud.setOnClickListener(new View.OnClickListener() { // from class: activity.SettingsActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(SettingsActivity.this.getApplicationContext(), (Class<?>) SmartCloudAISettingActivity.class);
                intent.putExtra("iotId", SettingsActivity.this.iotId);
                SettingsActivity.this.startActivity(intent);
            }
        });
        this.auto_door.setOnClickListener(new View.OnClickListener() { // from class: activity.SettingsActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(SettingsActivity.this.getApplicationContext(), (Class<?>) AutoDoorActivity.class);
                intent.putExtra("iotId", SettingsActivity.this.iotId);
                SettingsActivity.this.startActivity(intent);
            }
        });
        Log.e("时区", DateUtil.getCurrntDate());
        this.item_4g_switch.setOnClickListener(new View.OnClickListener() { // from class: activity.SettingsActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(SettingsActivity.this, (Class<?>) Net4GSwitchActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, SettingsActivity.this.device);
                if (SettingsActivity.this.device1 != null) {
                    bundle2.putSerializable("device1", SettingsActivity.this.device1);
                    bundle2.putSerializable("nvrDevice", SettingsActivity.this.nvrDevice);
                }
                intent.putExtras(bundle2);
                SettingsActivity.this.startActivity(intent);
            }
        });
        this.switch_4g.setOnClickListener(new View.OnClickListener() { // from class: activity.SettingsActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                SettingsActivity.this.switch4gFragment.showAllowingStateLoss(SettingsActivity.this.getSupportFragmentManager(), "", SettingsActivity.this.wifiFourPosition);
            }
        });
        this.apnItem.setOnClickListener(new View.OnClickListener() { // from class: activity.SettingsActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(SettingsActivity.this.getApplicationContext(), (Class<?>) APNActivity.class);
                intent.putExtra("iotId", SettingsActivity.this.iotId);
                SettingsActivity.this.startActivity(intent);
            }
        });
        if (SharePreferenceManager.getInstance().getDisplayController(this.iotId) == 0 || this.device.getOwned() != 1) {
            this.auto_door.setVisibility(8);
            this.smart_device.setVisibility(8);
        }
        this.net4GModeArr = getResources().getStringArray(R.array.net_4g_Switch);
        if (((SharePreferenceManager.getInstance().getPageControlEx(this.iotId) & 524288) >> 19) == 1 && (deviceInfoBean = this.device) != null && deviceInfoBean.getOwned() == 1) {
            this.net4gSwitch = SharePreferenceManager.getInstance().getNet4GMode(this.iotId);
            this.item_4g_switch.setVisibility(0);
            this.item_4g_switch.setRightText(this.net4GModeArr[this.net4gSwitch == 1 ? (char) 0 : (char) 1]);
        }
        findViewById(R.id.item_record_setting).setOnClickListener(this);
        this.itemCameraInfo.setRightText(this.device.getName());
        this.itemCameraCover.addRightView(new Switch(getActivity()));
        this.swCameraCover = (Switch) this.itemCameraCover.getRightView();
        this.swCameraCover.setTextOff("");
        this.swCameraCover.setTextOn("");
        this.swCameraCover.setText("");
        this.swCameraCover.setBackgroundResource(R.drawable.sel_switch);
        this.itemHumanDetection.addRightView(new Switch(getActivity()));
        this.swHumanDetection = (Switch) this.itemHumanDetection.getRightView();
        this.swHumanDetection.setTextOff("");
        this.swHumanDetection.setTextOn("");
        this.swHumanDetection.setText("");
        this.swHumanDetection.setBackgroundResource(R.drawable.sel_switch);
        this.itemMyCloudStorage.addRightView(new SwitchCompat(this));
        this.swYunServiceEventPlan = (SwitchCompat) this.itemMyCloudStorage.getRightView();
        this.swYunServiceEventPlan.setTextOff("");
        this.swYunServiceEventPlan.setTextOn("");
        this.swYunServiceEventPlan.setText("");
        this.swYunServiceEventPlan.setThumbDrawable(null);
        this.swYunServiceEventPlan.setBackgroundResource(R.drawable.sel_switch);
        this.itemDynamicVideo.addRightView(new Switch(getActivity()));
        this.swDynamicVideo = (Switch) this.itemDynamicVideo.getRightView();
        this.swDynamicVideo.setTextOff("");
        this.swDynamicVideo.setTextOn("");
        this.swDynamicVideo.setText("");
        this.swDynamicVideo.setBackgroundResource(R.drawable.sel_switch);
        this.itemCameraInfo.setOnClickListener(this);
        this.itemCameraSetting.setOnClickListener(this);
        this.itemMotionDetection.setOnClickListener(this);
        this.itemMyCardStorage.setOnClickListener(this);
        this.itemMotionDetectionSetting.setOnClickListener(this);
        this.itemSoundLightDetection.setOnClickListener(this);
        this.itemNetworkInfo.setOnClickListener(this);
        this.item_nat_share.setOnClickListener(this);
        this.itemShareManage.setOnClickListener(this);
        this.itemDayLight.setOnClickListener(this);
        this.itemLightMode.setOnClickListener(this);
        this.itemAlarmMode.setOnClickListener(this);
        this.itemMyCloudStorage.setOnClickListener(this);
        this.itemMyCloudSettings.setOnClickListener(this);
        this.itemYunPay.setOnClickListener(this);
        this.itemTrafficPay.setOnClickListener(this);
        this.image_setting.setOnClickListener(this);
        this.gunball_setting.setOnClickListener(this);
        this.item_low_power.setOnClickListener(this);
        this.item_power_model.setOnClickListener(this);
        if (this.device1 == null) {
            this.gunball_setting.setVisibility(8);
            this.image_setting.setLineShow(false);
            this.itemCameraInfo.setLineShow(false);
        } else if (SharePreferenceManager.getInstance().getEventRecord(this.device.getIotId()) != 1 || SharePreferenceManager.getInstance().getSupport4G(this.device.getIotId()) == 1) {
            this.itemMyCloudSettings.setVisibility(8);
        }
        if (!AppConfig.isChina) {
            this.itemMyCloudSettings.setVisibility(8);
            if (!SharePreferenceManager.getInstance().getAPNConfig(this.device.getIotId()).isEmpty()) {
                this.apnItem.setVisibility(0);
            }
        }
        this.motionDetectSensitivity = getResources().getStringArray(R.array.MotionDetectSensitivity);
        this.motionDetectSensitivityValue = getResources().getStringArray(R.array.MotionDetectSensitivityValue);
        this.sensitivityDialog = new SelectDialogView.Builder().title(getString(R.string.motion_detect_sensitivity_title)).selectItems(this.motionDetectSensitivity).isAllowCancel(false).build();
        this.sensitivityDialog.addOnClickListener(new SelectDialogView.OnClickListener() { // from class: activity.SettingsActivity.9
            @Override // dialog.SelectDialogView.OnClickListener
            public void onNegativeClick() {
            }

            @Override // dialog.SelectDialogView.OnClickListener
            public void onPositiveClick(int i, String str, Object obj) {
                SettingsActivity settingsActivity = SettingsActivity.this;
                settingsActivity.updateDevParam(settingsActivity.getString(R.string.motion_detect_sensitivity_key), SettingsActivity.this.motionDetectSensitivityValue[i]);
            }
        });
        this.itemFirmwareVersion.setRightText(SharePreferenceManager.getInstance().getFirmwareVersion(this.device.getIotId()));
        this.itemFirmwareVersion.setOnClickListener(new View.OnClickListener() { // from class: activity.SettingsActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                SettingsActivity.this.getUpgradeInfo();
            }
        });
        this.itemRestartDev = (LongItemView) findViewById(R.id.item_restart_dev);
        this.itemResetSetting = (LongItemView) findViewById(R.id.item_reset_setting);
        this.itemRestartDev.setOnClickListener(this);
        this.itemResetSetting.setOnClickListener(this);
        this.infrarredMode = getResources().getStringArray(R.array.InfrarredMode);
        this.nightModeFragment = new SelectorDialogFragment(getString(R.string.night_mode), this.infrarredMode);
        this.nightModeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.SettingsActivity.11
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                SettingsActivity settingsActivity = SettingsActivity.this;
                settingsActivity.updateNightMode(settingsActivity.getString(R.string.day_night_mode_key), Integer.valueOf(i));
            }
        });
        this.net4gModeFragment = new SelectorDialogFragment(getString(R.string.net_4G_text), this.net4GModeArr);
        this.net4gModeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.SettingsActivity.12
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                SettingsActivity settingsActivity = SettingsActivity.this;
                settingsActivity.net4gMode(settingsActivity.getString(R.string.Net4GMode), i == 0 ? 1 : 2);
            }
        });
        this.switch_4gArr = getResources().getStringArray(R.array.switch_4g);
        this.switch4gFragment = new SelectorDialogFragment(getString(R.string.network_change), true, this.switch_4gArr);
        this.switch4gFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.SettingsActivity.13
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                if (i != 0 || SettingsActivity.this.wifiFourPosition == i) {
                    if (i != 1 || SettingsActivity.this.wifiFourPosition == i) {
                        if (i != 2 || SettingsActivity.this.wifiFourPosition == i) {
                            return;
                        }
                        SettingsActivity settingsActivity = SettingsActivity.this;
                        settingsActivity.switch4gMode(settingsActivity.getString(R.string.Net4GEnableSwitch), i);
                        SettingsActivity.this.FourGChangeDialog(i);
                        return;
                    }
                    if (SettingsActivity.this.WifiConfigIsExist == 0) {
                        WiFiListActivity.start(SettingsActivity.this.getActivity(), SettingsActivity.this.iotId, "1");
                        return;
                    } else {
                        if (SettingsActivity.this.WifiConfigIsExist == 1) {
                            SettingsActivity settingsActivity2 = SettingsActivity.this;
                            settingsActivity2.switch4gMode(settingsActivity2.getString(R.string.Net4GEnableSwitch), i);
                            SettingsActivity.this.FourGChangeDialog(i);
                            return;
                        }
                        return;
                    }
                }
                SettingsActivity settingsActivity3 = SettingsActivity.this;
                settingsActivity3.switch4gMode(settingsActivity3.getString(R.string.Net4GEnableSwitch), i);
                SettingsActivity.this.FourGChangeDialog(i);
            }
        });
        this.yunServiceRecordMode = getResources().getStringArray(R.array.YunServiceRecordMode);
        this.swYunServiceEventPlan.setOnClickListener(new View.OnClickListener() { // from class: activity.SettingsActivity.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (!(!SettingsActivity.this.isYunServiceEventPlanChecked)) {
                    SettingsActivity.this.showProgressDialog();
                    SettingsActivity.this.unbindPlanToDevice();
                } else {
                    SettingsActivity.this.showProgressDialog();
                    SettingsActivity.this.triggerRecordPlan();
                }
            }
        });
        if (AppConfig.isChina) {
            showProgressDialog();
            getBindYunServicePlan(this.iotId, "", false);
        } else {
            this.stvYunPay.setVisibility(8);
            this.itemYunPay.setVisibility(8);
            this.itemMyCardStorage.setLineShow(this.itemYunPay.getVisibility() == 0);
        }
        if (SharePreferenceManager.getInstance().getNatWLANSwitchShow(this.iotId) == 1 || SharePreferenceManager.getInstance().getNatETHSwitchShow(this.iotId) == 1) {
            this.item_nat_share.setVisibility(0);
        } else {
            this.item_nat_share.setVisibility(8);
        }
        if (SharePreferenceManager.getInstance().getLowPower(this.iotId) == 1) {
            this.item_low_power.setVisibility(0);
        } else {
            this.item_low_power.setVisibility(8);
        }
        if (SharePreferenceManager.getInstance().getPowerModeShow(this.iotId) == 1) {
            this.item_power_model.setVisibility(0);
        } else {
            this.item_power_model.setVisibility(8);
        }
        Log.d(this.TAG, "initWidget: puppet:nvrIotId===" + this.nvrIotId);
        Log.d(this.TAG, "initWidget: puppet:nvrOwner=====" + this.nvrOwner);
        String str = this.nvrIotId;
        if (str != null && !str.equals("")) {
            if (this.device != null && this.nvrOwner == 1) {
                Log.d(this.TAG, "initWidget: puppet555555555");
                this.ll_layout.setVisibility(0);
                return;
            }
            Log.d(this.TAG, "initWidget: puppet6666666666");
            this.itemCameraCover.setVisibility(8);
            this.itemCameraInfo.setVisibility(0);
            this.itemCameraInfo.setLineShow(false);
            this.itemCameraSetting.setVisibility(8);
            this.itemMotionDetectionSetting.setVisibility(8);
            this.itemHumanDetection.setVisibility(8);
            this.itemSoundLightDetection.setVisibility(8);
            this.switch_4g.setVisibility(8);
            this.itemNetworkInfo.setVisibility(8);
            this.item_nat_share.setVisibility(8);
            this.itemMyCloudStorage.setVisibility(8);
            this.itemMyCloudSettings.setVisibility(8);
            this.itemMyCardStorage.setVisibility(8);
            this.itemDynamicVideo.setVisibility(8);
            this.itemMotionDetection.setVisibility(8);
            this.itemShareManage.setVisibility(8);
            this.itemTimeSetting.setVisibility(8);
            this.itemRecordSetting.setVisibility(8);
            this.image_setting.setVisibility(8);
            this.itemFirmwareVersion.setVisibility(0);
            this.itemFirmwareVersion.setLineShow(false);
            this.itemRestartDev.setVisibility(8);
            this.itemResetSetting.setVisibility(8);
            this.itemDayLight.setVisibility(8);
            this.itemLightMode.setVisibility(8);
            this.itemAlarmMode.setVisibility(8);
            this.itemYunPay.setVisibility(8);
            this.itemMyCardStorage.setLineShow(this.itemYunPay.getVisibility() == 0);
            this.itemTrafficPay.setVisibility(8);
            this.storage.setVisibility(8);
            this.network.setVisibility(8);
            this.apnItem.setVisibility(8);
            this.safety.setVisibility(8);
            this.stvYunPay.setVisibility(8);
            this.item_4g_switch.setVisibility(8);
            this.item_low_power.setVisibility(8);
            this.item_power_model.setVisibility(8);
            if (this.auto_door.getVisibility() == 8) {
                this.smart_device.setVisibility(8);
                return;
            }
            return;
        }
        DeviceInfoBean deviceInfoBean5 = this.device;
        if (deviceInfoBean5 != null && deviceInfoBean5.getOwned() == 1) {
            Log.d(this.TAG, "initWidget: puppet:8888888888");
            this.ll_layout.setVisibility(0);
            return;
        }
        Log.d(this.TAG, "initWidget: puppet:9999999999");
        this.itemCameraCover.setVisibility(8);
        this.itemCameraInfo.setVisibility(0);
        this.itemCameraInfo.setLineShow(false);
        this.itemCameraSetting.setVisibility(8);
        this.itemMotionDetectionSetting.setVisibility(8);
        this.itemHumanDetection.setVisibility(8);
        this.itemSoundLightDetection.setVisibility(8);
        this.switch_4g.setVisibility(8);
        this.itemNetworkInfo.setVisibility(8);
        this.item_nat_share.setVisibility(8);
        this.itemMyCloudStorage.setVisibility(8);
        this.itemMyCloudSettings.setVisibility(8);
        this.itemMyCardStorage.setVisibility(8);
        this.itemDynamicVideo.setVisibility(8);
        this.itemMotionDetection.setVisibility(8);
        this.itemShareManage.setVisibility(8);
        this.itemTimeSetting.setVisibility(8);
        this.itemRecordSetting.setVisibility(8);
        this.image_setting.setVisibility(8);
        this.itemFirmwareVersion.setVisibility(0);
        this.itemFirmwareVersion.setLineShow(false);
        this.itemRestartDev.setVisibility(8);
        this.itemResetSetting.setVisibility(8);
        this.itemDayLight.setVisibility(8);
        this.itemLightMode.setVisibility(8);
        this.itemAlarmMode.setVisibility(8);
        this.itemYunPay.setVisibility(8);
        this.itemMyCardStorage.setLineShow(this.itemYunPay.getVisibility() == 0);
        this.itemTrafficPay.setVisibility(8);
        this.storage.setVisibility(8);
        this.network.setVisibility(8);
        this.apnItem.setVisibility(8);
        this.safety.setVisibility(8);
        this.stvYunPay.setVisibility(8);
        this.item_4g_switch.setVisibility(8);
        this.item_low_power.setVisibility(8);
        this.item_power_model.setVisibility(8);
        if (this.auto_door.getVisibility() == 8) {
            this.smart_device.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendLowPowerAppStatus() {
        HashMap map = new HashMap();
        map.put(Constants.LowPowerAppStatus, 1);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.SettingsActivity.15
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
            }
        });
    }

    public void getUpgradeInfo() {
        this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.16
            @Override // java.lang.Runnable
            public void run() {
                SettingsActivity.this.showProgressDialog();
            }
        });
        HashMap map = new HashMap();
        Log.e("版本", !"".equals(this.nvrIotId) ? this.nvrIotId : this.device.getIotId());
        map.put("iotId", !"".equals(this.nvrIotId) ? this.nvrIotId : this.device.getIotId());
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/ota/info/queryByUser").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass17());
    }

    /* JADX INFO: renamed from: activity.SettingsActivity$17, reason: invalid class name */
    class AnonymousClass17 implements IoTCallback {
        AnonymousClass17() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, SettingsActivity.this.TAG, "onFailure");
            SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.17.1
                @Override // java.lang.Runnable
                public void run() {
                    SettingsActivity.this.showToast(SettingsActivity.this.getString(R.string.get_version_upgrade_fail));
                    SettingsActivity.this.dismissProgressDialog();
                }
            });
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            int code = ioTResponse.getCode();
            if (code != 200) {
                if (code == 28601) {
                    SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.17.2
                        @Override // java.lang.Runnable
                        public void run() {
                            SettingsActivity.this.showToast(SettingsActivity.this.getString(R.string.no_permission));
                            SettingsActivity.this.dismissProgressDialog();
                        }
                    });
                    return;
                } else {
                    SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.17.3
                        @Override // java.lang.Runnable
                        public void run() {
                            SettingsActivity.this.showToast(SettingsActivity.this.getString(R.string.new_version));
                            SettingsActivity.this.dismissProgressDialog();
                        }
                    });
                    return;
                }
            }
            Object data = ioTResponse.getData();
            if (data == null || !(data instanceof JSONObject)) {
                SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.17.7
                    @Override // java.lang.Runnable
                    public void run() {
                        SettingsActivity.this.showToast(SettingsActivity.this.getString(R.string.new_version));
                        SettingsActivity.this.dismissProgressDialog();
                    }
                });
                return;
            }
            try {
                JSONObject jSONObject = (JSONObject) data;
                String str = (String) jSONObject.get("currentVersion");
                final String str2 = (String) jSONObject.get("version");
                if (!str.equals(str2)) {
                    SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.17.4
                        @Override // java.lang.Runnable
                        public void run() {
                            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(SettingsActivity.this.getString(R.string.found_device_upgrade_or_not, new Object[]{str2})).leftBtnText(SettingsActivity.this.getString(R.string.confirm_upgrade)).rightBtnText(SettingsActivity.this.getString(R.string.cancel)).clickLeft(new View.OnClickListener() { // from class: activity.SettingsActivity.17.4.1
                                @Override // android.view.View.OnClickListener
                                public void onClick(View view2) {
                                    SettingsActivity.this.updateDevice(str2);
                                }
                            }).canCancel(false).create().show(SettingsActivity.this.getSupportFragmentManager(), "");
                            SettingsActivity.this.dismissProgressDialog();
                        }
                    });
                } else {
                    SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.17.5
                        @Override // java.lang.Runnable
                        public void run() {
                            new BaseDialog.Builder().view(R.layout.dialog_common).content(SettingsActivity.this.getString(R.string.new_version)).rightBtnText(SettingsActivity.this.getString(R.string.known)).canCancel(false).create().show(SettingsActivity.this.getSupportFragmentManager(), "");
                            SettingsActivity.this.showToast(SettingsActivity.this.getString(R.string.new_version));
                            SettingsActivity.this.dismissProgressDialog();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
                SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.17.6
                    @Override // java.lang.Runnable
                    public void run() {
                        SettingsActivity.this.showToast(SettingsActivity.this.getString(R.string.new_version));
                        SettingsActivity.this.dismissProgressDialog();
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
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/ota/batchUpgradeByUser").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass18(str));
    }

    /* JADX INFO: renamed from: activity.SettingsActivity$18, reason: invalid class name */
    class AnonymousClass18 implements IoTCallback {
        final /* synthetic */ String val$version;

        AnonymousClass18(String str) {
            this.val$version = str;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, SettingsActivity.this.TAG, "onFailure");
            SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.18.1
                @Override // java.lang.Runnable
                public void run() {
                    SettingsActivity.this.showToast(SettingsActivity.this.getString(R.string.can_not_upgrade));
                    SettingsActivity.this.dismissProgressDialog();
                }
            });
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            if (ioTResponse.getCode() != 200) {
                SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.18.2
                    @Override // java.lang.Runnable
                    public void run() {
                        SettingsActivity.this.showToast(SettingsActivity.this.getString(R.string.can_not_upgrade));
                        SettingsActivity.this.dismissProgressDialog();
                    }
                });
            } else {
                SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.18.3
                    @Override // java.lang.Runnable
                    public void run() {
                        SettingsActivity.this.dismissProgressDialog();
                        SettingsActivity.this.upgradeDialogIpc = new UpgradeDialogIpc(SettingsActivity.this.getActivity(), R.style.progress_dialog);
                        SettingsActivity.this.upgradeDialogIpc.setOnViewClick(new UpgradeDialogIpc.OnViewClick() { // from class: activity.SettingsActivity.18.3.1
                            @Override // dialog.UpgradeDialogIpc.OnViewClick
                            public void onDismiss() {
                                if (SettingsActivity.this.upgradeDialogIpc.getProgress() < 100) {
                                    SettingsActivity.this.cancelUpgrade(!"".equals(SettingsActivity.this.nvrIotId) ? SettingsActivity.this.nvrIotId : SettingsActivity.this.device.getIotId(), AnonymousClass18.this.val$version);
                                }
                            }
                        });
                        SettingsActivity.this.upgradeDialogIpc.setCancelable(false);
                        SettingsActivity.this.upgradeDialogIpc.setCanceledOnTouchOutside(false);
                        SettingsActivity.this.upgradeDialogIpc.show();
                        SettingsActivity.this.queryUpgradeStep(!"".equals(SettingsActivity.this.nvrIotId) ? SettingsActivity.this.nvrIotId : SettingsActivity.this.device.getIotId(), AnonymousClass18.this.val$version);
                    }
                });
            }
        }
    }

    public void queryUpgradeStep(final String str, final String str2) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("version", str2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/ota/progress/getByUser").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.SettingsActivity.19
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.d(SettingsActivity.this.TAG, "onFailure");
                SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.19.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SettingsActivity.this.showToast(SettingsActivity.this.getResources().getString(R.string.can_not_upgrade));
                        SettingsActivity.this.failUpgradeDialog();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() != 200) {
                    SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.19.2
                        @Override // java.lang.Runnable
                        public void run() {
                            SettingsActivity.this.showToast(SettingsActivity.this.getResources().getString(R.string.can_not_upgrade));
                            SettingsActivity.this.failUpgradeDialog();
                        }
                    });
                    return;
                }
                if (ioTResponse.getData() == null) {
                    SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.19.9
                        @Override // java.lang.Runnable
                        public void run() {
                            SettingsActivity.this.showToast(SettingsActivity.this.getResources().getString(R.string.can_not_upgrade));
                            SettingsActivity.this.failUpgradeDialog();
                        }
                    });
                    return;
                }
                try {
                    JSONObject jSONObject = (JSONObject) ioTResponse.getData();
                    int i = jSONObject.getInt(WifiProvisionUtConst.KEY_STEP);
                    int i2 = jSONObject.getInt("upgradeStatus");
                    if (i2 == 4) {
                        SettingsActivity.this.uiHandler.postDelayed(new Runnable() { // from class: activity.SettingsActivity.19.3
                            @Override // java.lang.Runnable
                            public void run() {
                                if (SettingsActivity.this.isFinishing()) {
                                    return;
                                }
                                SettingsActivity.this.successUpgradeDialog();
                                SettingsActivity.this.showToast(SettingsActivity.this.getResources().getString(R.string.upgrade_version_success));
                                SharePreferenceManager.getInstance().setFirstUpdateVersion(str, str2);
                            }
                        }, 1000L);
                        SettingsCtrl.getInstance().getProperties(str, new MyCallback() { // from class: activity.SettingsActivity.19.4
                            @Override // tools.MyCallback
                            public void onComplete(boolean z) {
                            }
                        });
                        return;
                    }
                    if (i2 != 2 && i2 != 3) {
                        if (i < 0) {
                            SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.19.6
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (SettingsActivity.this.isFinishing()) {
                                        return;
                                    }
                                    SettingsActivity.this.showToast(SettingsActivity.this.getResources().getString(R.string.upgrade_version_fail));
                                    SettingsActivity.this.failUpgradeDialog();
                                }
                            });
                            return;
                        } else {
                            SettingsActivity.this.uiHandler.postDelayed(new Runnable() { // from class: activity.SettingsActivity.19.7
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (SettingsActivity.this.isFinishing() || !SettingsActivity.this.upgradeDialogIpc.isShowing() || SettingsActivity.this.upgradeDialogIpc.isDownloadSucceed()) {
                                        return;
                                    }
                                    SettingsActivity.this.queryUpgradeStep(str, str2);
                                }
                            }, 1000L);
                            return;
                        }
                    }
                    SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.19.5
                        @Override // java.lang.Runnable
                        public void run() {
                            if (SettingsActivity.this.isFinishing()) {
                                return;
                            }
                            SettingsActivity.this.showToast(SettingsActivity.this.getResources().getString(R.string.upgrade_version_fail));
                            SettingsActivity.this.failUpgradeDialog();
                        }
                    });
                } catch (Exception e) {
                    SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.19.8
                        @Override // java.lang.Runnable
                        public void run() {
                            SettingsActivity.this.showToast(SettingsActivity.this.getResources().getString(R.string.can_not_upgrade));
                            SettingsActivity.this.failUpgradeDialog();
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

    public void cancelUpgrade(String str, String str2) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("version", str2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/ota/unupgradeByUser").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.SettingsActivity.20
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.d(SettingsActivity.this.TAG, "onFailure");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ioTResponse.getCode();
            }
        });
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        DeviceInfoBean deviceInfoBean;
        super.onResume();
        SharePreferenceManager.getInstance().registerOnCallSetListener(this.mSPModifyListener);
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.SettingsActivity.21
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
        ChannelManager.getInstance().registerListener(this.iMobileMsgListener);
        this.currentMotionDetectSensitivity = SharePreferenceManager.getInstance().getMotionDetectSensitivity(this.iotId);
        String[] strArr = this.motionDetectSensitivity;
        if (strArr.length - 1 < this.currentMotionDetectSensitivity) {
            this.currentMotionDetectSensitivity = strArr.length - 1;
        }
        this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(this.iotId);
        this.faceDetectSensitivity = SharePreferenceManager.getInstance().getFaceDetectMode(this.iotId);
        this.faceDetectSensitityArr = getResources().getStringArray(R.array.FaceDetectSensitivity);
        if (this.supportMotionDetect == 0) {
            int i = this.faceDetectSensitivity;
            if (i != -1) {
                this.itemMotionDetectionSetting.setRightText(this.faceDetectSensitityArr[i]);
            }
        } else {
            this.itemMotionDetectionSetting.setRightText(this.motionDetectSensitivity[this.currentMotionDetectSensitivity]);
        }
        this.storageStatus = getResources().getStringArray(R.array.StorageStatus);
        this.storageStatusValues = SharePreferenceManager.getInstance().getStorageStatus(this.iotId);
        if (this.storageStatusValues == 0) {
            this.itemMyCardStorage.setRightText(this.storageStatus[0]);
            this.itemMyCardStorage.setEnabled(false);
        } else {
            this.totalStorage = SharePreferenceManager.getInstance().getStorageTotalCapacity(this.iotId) / 1024.0f;
            this.remainStorage = SharePreferenceManager.getInstance().getStorageRemainingCapacity(this.iotId) / 1024.0f;
            Log.e(this.TAG, "totalStorage: " + this.totalStorage + "  remainStorage: " + this.remainStorage);
            new DecimalFormat("0.00").format((double) this.totalStorage);
            if (this.storageStatusValues == 4) {
                this.itemMyCardStorage.setRightText(getString(R.string.card_inserted));
                this.itemMyCardStorage.setEnabled(true);
            } else {
                this.itemMyCardStorage.setRightText(getString(R.string.card_inserted));
                this.itemMyCardStorage.setEnabled(true);
            }
            this.itemMyCardStorage.setRightText(getString(R.string.card_inserted));
            this.itemMyCardStorage.setEnabled(true);
        }
        getDevTime();
        this.IccId = SharePreferenceManager.getInstance().getIccId(this.device.getIotId());
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.SettingsActivity.22
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                if (z) {
                    SettingsActivity.this.switch_4g.setRightText(SettingsActivity.this.switch_4gArr[SharePreferenceManager.getInstance().getNet4GEnableSwitch(SettingsActivity.this.iotId)]);
                    SettingsActivity.this.wifiFourPosition = SharePreferenceManager.getInstance().getNet4GEnableSwitch(SettingsActivity.this.iotId);
                    if (SharePreferenceManager.getInstance().getNet4GMode(SettingsActivity.this.iotId) != -1) {
                        SettingsActivity.this.item_4g_switch.setRightText(SettingsActivity.this.net4GModeArr[SettingsActivity.this.net4gSwitch == 1 ? (char) 0 : (char) 1]);
                    }
                    SettingsActivity.this.WifiConfigIsExist = SharePreferenceManager.getInstance().getWifiConfigIsExist(SettingsActivity.this.iotId);
                }
            }
        });
        String str = this.nvrIotId;
        if (str != null && !str.equals("")) {
            if (this.device != null && this.nvrOwner == 1) {
                this.switch_4g.setVisibility(SharePreferenceManager.getInstance().getDoubleNetWork(this.iotId) == 1 ? 0 : 8);
            }
        } else {
            DeviceInfoBean deviceInfoBean2 = this.device;
            if (deviceInfoBean2 != null && deviceInfoBean2.getOwned() == 1) {
                this.switch_4g.setVisibility(SharePreferenceManager.getInstance().getDoubleNetWork(this.iotId) == 1 ? 0 : 8);
            }
        }
        if (((SharePreferenceManager.getInstance().getPageControlEx(this.iotId) & 524288) >> 19) == 1 && (deviceInfoBean = this.device) != null && deviceInfoBean.getOwned() == 1) {
            this.net4gSwitch = SharePreferenceManager.getInstance().getNet4GMode(this.iotId);
            this.item_4g_switch.setVisibility(0);
            this.item_4g_switch.setRightText(this.net4GModeArr[this.net4gSwitch != 1 ? (char) 1 : (char) 0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDevParam(String str, final Object obj) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.motion_detect_sensitivity_key))) {
            map.put(Constants.MOTION_DETECT_SENSITIVITY_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
        }
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.SettingsActivity.23
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj2) {
                if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                    return;
                }
                com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(obj2));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.23.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    } else {
                        SharePreferenceManager.getInstance().setMotionDetectSensitivity(SettingsActivity.this.iotId, Integer.parseInt(obj.toString()));
                        SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.23.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNightMode(String str, final Object obj) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.day_night_mode_key))) {
            map.put(Constants.DAY_NIGHT_MODE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
        }
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.SettingsActivity.24
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj2) {
                if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                    return;
                }
                com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(obj2));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.24.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    } else {
                        SharePreferenceManager.getInstance().setDayNightMode(SettingsActivity.this.iotId, Integer.parseInt(obj.toString()));
                        SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.24.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void net4gMode(String str, final int i) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.Net4GMode))) {
            map.put(Constants.Net4GMode, Integer.valueOf(i));
        }
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.SettingsActivity.25
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (z) {
                    if (obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.25.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                            return;
                        }
                        SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.25.2
                            @Override // java.lang.Runnable
                            public void run() {
                                SharePreferenceManager.getInstance().setNet4GMode(SettingsActivity.this.iotId, i);
                                SettingsActivity.this.net4gSwitch = i;
                                SettingsActivity.this.item_4g_switch.setRightText(SettingsActivity.this.net4GModeArr[SettingsActivity.this.net4gSwitch == 1 ? (char) 0 : (char) 1]);
                                Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                        Log.e("4G卡切换存储", i + "" + SharePreferenceManager.getInstance().getNet4GMode(SettingsActivity.this.iotId));
                        return;
                    }
                    return;
                }
                SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.25.3
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switch4gMode(String str, int i) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.Net4GEnableSwitch))) {
            map.put(Constants.Net4GEnableSwitch, Integer.valueOf(i));
        }
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.SettingsActivity.26
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code")) {
                    object.getInteger("code").intValue();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void FourGChangeDialog(int i) {
        View viewInflate = View.inflate(this, R.layout.switch_layout, null);
        ProgressBar progressBar = (ProgressBar) viewInflate.findViewById(R.id.load_progressbar);
        ImageButton imageButton = (ImageButton) viewInflate.findViewById(R.id.close_btn);
        Drawable drawable = getResources().getDrawable(R.drawable.et_cancel);
        drawable.setBounds(0, 0, (int) (((double) drawable.getIntrinsicWidth()) * 0.5d), (int) (((double) drawable.getIntrinsicHeight()) * 0.5d));
        imageButton.setBackground(drawable);
        Button button = (Button) viewInflate.findViewById(R.id.cancel);
        TextView textView = (TextView) viewInflate.findViewById(R.id.load_progressbar_text);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.image_result_text);
        final AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setView(viewInflate).create();
        alertDialogCreate.setCanceledOnTouchOutside(true);
        alertDialogCreate.show();
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.SettingsActivity.27
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                SettingsActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.SettingsActivity.28
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.SettingsActivity.29
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.countDownTimer = new AnonymousClass30(60000L, 4000L, i, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.SettingsActivity$30, reason: invalid class name */
    class AnonymousClass30 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ int val$position;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass30(long j, long j2, int i, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
            super(j, j2);
            this.val$position = i;
            this.val$progressBarText = textView;
            this.val$progressBar = progressBar;
            this.val$imageViewText = textView2;
            this.val$imageView = imageView;
            this.val$imageButton = imageButton;
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            SettingsCtrl.getInstance().getProperties(SettingsActivity.this.iotId, new MyCallback() { // from class: activity.SettingsActivity.30.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getNet4GEnableSwitch(SettingsActivity.this.iotId) == AnonymousClass30.this.val$position) {
                        SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.30.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AnonymousClass30.this.val$progressBarText.setVisibility(8);
                                AnonymousClass30.this.val$progressBar.setVisibility(8);
                                AnonymousClass30.this.val$imageViewText.setVisibility(0);
                                AnonymousClass30.this.val$imageViewText.setText(R.string.switched_success);
                                AnonymousClass30.this.val$imageView.setVisibility(0);
                                AnonymousClass30.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass30.this.val$imageButton.setVisibility(0);
                                SettingsActivity.this.switch_4g.setRightText(SettingsActivity.this.switch_4gArr[AnonymousClass30.this.val$position]);
                                SettingsActivity.this.wifiFourPosition = AnonymousClass30.this.val$position;
                                Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                        AnonymousClass30.this.cancel();
                        SettingsActivity.this.countDownTimer = null;
                    }
                }
            });
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.30.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass30.this.val$progressBarText.setVisibility(8);
                    AnonymousClass30.this.val$progressBar.setVisibility(8);
                    AnonymousClass30.this.val$imageViewText.setVisibility(0);
                    AnonymousClass30.this.val$imageViewText.setText(R.string.mofify_failed);
                    AnonymousClass30.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass30.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            SettingsActivity.this.countDownTimer = null;
        }
    }

    public void cancelCount() {
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.countDownTimer = null;
        }
    }

    private void getQueryNetModeStatus(final CallBack callBack) {
        IPCManager.getInstance().getDevice(this.iotId).getQueryNetModeStatus(new IPanelCallback() { // from class: activity.SettingsActivity.31
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (z) {
                    callBack.finish();
                }
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.bt_remove_camera) {
            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(getString(R.string.confirm_delete_camera)).leftBtnText(getString(R.string.sure_delete)).rightBtnText(getString(R.string.cancel)).clickLeft(new View.OnClickListener() { // from class: activity.SettingsActivity.32
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    if (SettingsActivity.this.device != null) {
                        SettingsActivity settingsActivity = SettingsActivity.this;
                        settingsActivity.unbindDevice(settingsActivity.device.getIotId());
                    }
                    if (SettingsActivity.this.device1 != null) {
                        SettingsActivity settingsActivity2 = SettingsActivity.this;
                        settingsActivity2.unbindDevice(settingsActivity2.device1.getIotId());
                    }
                    if (SettingsActivity.this.device2 != null) {
                        SettingsActivity settingsActivity3 = SettingsActivity.this;
                        settingsActivity3.unbindDevice(settingsActivity3.device2.getIotId());
                    }
                    if (SettingsActivity.this.device3 != null) {
                        SettingsActivity settingsActivity4 = SettingsActivity.this;
                        settingsActivity4.unbindDevice(settingsActivity4.device3.getIotId());
                    }
                    if (SettingsActivity.this.nvrDevice != null) {
                        SettingsActivity settingsActivity5 = SettingsActivity.this;
                        settingsActivity5.unbindDevice(settingsActivity5.nvrDevice.getIotId());
                    }
                }
            }).canCancel(false).create().show(getSupportFragmentManager(), "");
            return;
        }
        if (view2.getId() == R.id.item_camera_info) {
            Intent intent = new Intent(this, (Class<?>) CameraInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
            DeviceInfoBean deviceInfoBean = this.device1;
            if (deviceInfoBean != null) {
                bundle.putSerializable("device1", deviceInfoBean);
            }
            DeviceInfoBean deviceInfoBean2 = this.device2;
            if (deviceInfoBean2 != null) {
                bundle.putSerializable("device2", deviceInfoBean2);
            }
            DeviceInfoBean deviceInfoBean3 = this.nvrDevice;
            if (deviceInfoBean3 != null) {
                bundle.putSerializable("nvrDevice", deviceInfoBean3);
            }
            intent.putExtras(bundle);
            startActivity(intent);
            return;
        }
        if (view2.getId() == R.id.item_camera_setting) {
            CameraSettingActivity.start(getActivity(), this.device);
            return;
        }
        if (view2.getId() == R.id.item_motion_detection) {
            this.sensitivityDialog.setSelectStr(this.motionDetectSensitivity[this.currentMotionDetectSensitivity]);
            this.sensitivityDialog.show(getSupportFragmentManager(), this.TAG);
            return;
        }
        if (view2.getId() == R.id.item_my_card_storage) {
            Intent intent2 = new Intent(this, (Class<?>) StorageStatusActivity.class);
            intent2.putExtra("totalStorage", this.totalStorage);
            intent2.putExtra("remainStorage", this.remainStorage);
            intent2.putExtra("storageStatusValues", this.storageStatusValues);
            Bundle bundle2 = new Bundle();
            bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
            DeviceInfoBean deviceInfoBean4 = this.device1;
            if (deviceInfoBean4 != null) {
                bundle2.putSerializable("device1", deviceInfoBean4);
            }
            DeviceInfoBean deviceInfoBean5 = this.device2;
            if (deviceInfoBean5 != null) {
                bundle2.putSerializable("device2", deviceInfoBean5);
            }
            DeviceInfoBean deviceInfoBean6 = this.device3;
            if (deviceInfoBean6 != null) {
                bundle2.putSerializable("device3", deviceInfoBean6);
            }
            DeviceInfoBean deviceInfoBean7 = this.nvrDevice;
            if (deviceInfoBean7 != null) {
                bundle2.putSerializable("nvrDevice", deviceInfoBean7);
            }
            intent2.putExtras(bundle2);
            startActivity(intent2);
            return;
        }
        if (view2.getId() == R.id.item_motion_detection_setting) {
            Intent intent3 = new Intent(this, (Class<?>) MotionDetectActivity.class);
            Bundle bundle3 = new Bundle();
            bundle3.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
            DeviceInfoBean deviceInfoBean8 = this.device1;
            if (deviceInfoBean8 != null) {
                bundle3.putSerializable("device1", deviceInfoBean8);
            }
            DeviceInfoBean deviceInfoBean9 = this.device2;
            if (deviceInfoBean9 != null) {
                bundle3.putSerializable("device2", deviceInfoBean9);
            }
            DeviceInfoBean deviceInfoBean10 = this.device3;
            if (deviceInfoBean10 != null) {
                bundle3.putSerializable("device3", deviceInfoBean10);
            }
            DeviceInfoBean deviceInfoBean11 = this.nvrDevice;
            if (deviceInfoBean11 != null) {
                bundle3.putSerializable("nvrDevice", deviceInfoBean11);
            }
            intent3.putExtras(bundle3);
            startActivity(intent3);
            return;
        }
        if (view2.getId() == R.id.item_sound_light_detection) {
            SoundLightDetectionActivity.start(this);
            return;
        }
        if (view2.getId() == R.id.item_network_info) {
            WiFiListActivity.start(this, this.iotId, "");
            return;
        }
        if (view2.getId() == R.id.item_nat_share) {
            Intent intent4 = new Intent(this, (Class<?>) NetShareActivity.class);
            Bundle bundle4 = new Bundle();
            bundle4.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
            DeviceInfoBean deviceInfoBean12 = this.device1;
            if (deviceInfoBean12 != null) {
                bundle4.putSerializable("device1", deviceInfoBean12);
            }
            DeviceInfoBean deviceInfoBean13 = this.device2;
            if (deviceInfoBean13 != null) {
                bundle4.putSerializable("device2", deviceInfoBean13);
            }
            DeviceInfoBean deviceInfoBean14 = this.nvrDevice;
            if (deviceInfoBean14 != null) {
                bundle4.putSerializable("nvrDevice", deviceInfoBean14);
            }
            intent4.putExtras(bundle4);
            startActivity(intent4);
            return;
        }
        if (view2.getId() == R.id.item_share_manage) {
            Intent intent5 = new Intent(this, (Class<?>) ShareManageActivity.class);
            Bundle bundle5 = new Bundle();
            bundle5.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
            DeviceInfoBean deviceInfoBean15 = this.device1;
            if (deviceInfoBean15 != null) {
                bundle5.putSerializable("device1", deviceInfoBean15);
            }
            DeviceInfoBean deviceInfoBean16 = this.device2;
            if (deviceInfoBean16 != null) {
                bundle5.putSerializable("device2", deviceInfoBean16);
            }
            DeviceInfoBean deviceInfoBean17 = this.device3;
            if (deviceInfoBean17 != null) {
                bundle5.putSerializable("device3", deviceInfoBean17);
            }
            DeviceInfoBean deviceInfoBean18 = this.nvrDevice;
            if (deviceInfoBean18 != null) {
                bundle5.putSerializable("nvrDevice", deviceInfoBean18);
            }
            intent5.putExtras(bundle5);
            startActivity(intent5);
            return;
        }
        if (view2.getId() == R.id.item_time_setting) {
            Intent intent6 = new Intent(this, (Class<?>) TimeSettingActivity.class);
            intent6.putExtra("iotId", this.device.getIotId());
            startActivityForResult(intent6, 1);
            return;
        }
        if (view2.getId() == R.id.item_record_setting) {
            Intent intent7 = new Intent(this, (Class<?>) RecordSettingActivity.class);
            intent7.putExtra("iotId", this.device.getIotId());
            startActivity(intent7);
            return;
        }
        if (view2.getId() == R.id.item_restart_dev) {
            if (((SharePreferenceManager.getInstance().getPageControlEx(this.device.getIotId()) & 128) >> 7) == 1) {
                Intent intent8 = new Intent(this, (Class<?>) DeviceTimingRebootActivity.class);
                Bundle bundle6 = new Bundle();
                bundle6.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
                intent8.putExtras(bundle6);
                startActivity(intent8);
                return;
            }
            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(getString(R.string.sure_restart_camera)).leftBtnText(getString(R.string.sure_restart)).rightBtnText(getString(R.string.cancel)).clickLeft(new AnonymousClass33()).canCancel(false).create().show(getSupportFragmentManager(), "");
            return;
        }
        if (view2.getId() == R.id.item_reset_setting) {
            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(getString(R.string.sure_reset_camera)).leftBtnText(getString(R.string.sure_reset)).rightBtnText(getString(R.string.cancel)).clickLeft(new AnonymousClass34()).canCancel(false).create().show(getSupportFragmentManager(), "");
            return;
        }
        if (view2.getId() == R.id.item_day_light) {
            Intent intent9 = new Intent(this, (Class<?>) DayLightActivity.class);
            intent9.putExtra("iotId", this.device.getIotId());
            startActivity(intent9);
            return;
        }
        if (view2.getId() == R.id.item_night_mode) {
            Intent intent10 = new Intent(this, (Class<?>) NightLightingSettingsActivity.class);
            Bundle bundle7 = new Bundle();
            bundle7.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
            DeviceInfoBean deviceInfoBean19 = this.device1;
            if (deviceInfoBean19 != null) {
                bundle7.putSerializable("device1", deviceInfoBean19);
            }
            DeviceInfoBean deviceInfoBean20 = this.device2;
            if (deviceInfoBean20 != null) {
                bundle7.putSerializable("device2", deviceInfoBean20);
            }
            DeviceInfoBean deviceInfoBean21 = this.nvrDevice;
            if (deviceInfoBean21 != null) {
                bundle7.putSerializable("nvrDevice", deviceInfoBean21);
            }
            intent10.putExtras(bundle7);
            startActivity(intent10);
            return;
        }
        if (view2.getId() == R.id.item_alarm_mode) {
            Intent intent11 = new Intent(this, (Class<?>) AlarmModeActivity.class);
            Bundle bundle8 = new Bundle();
            bundle8.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
            DeviceInfoBean deviceInfoBean22 = this.device1;
            if (deviceInfoBean22 != null) {
                bundle8.putSerializable("device1", deviceInfoBean22);
            }
            DeviceInfoBean deviceInfoBean23 = this.device2;
            if (deviceInfoBean23 != null) {
                bundle8.putSerializable("device2", deviceInfoBean23);
            }
            DeviceInfoBean deviceInfoBean24 = this.nvrDevice;
            if (deviceInfoBean24 != null) {
                bundle8.putSerializable("nvrDevice", deviceInfoBean24);
            }
            intent11.putExtras(bundle8);
            startActivity(intent11);
            return;
        }
        if (view2.getId() == R.id.item_my_cloud_setting) {
            Intent intent12 = new Intent(this, (Class<?>) CloudSettingsActivity.class);
            Bundle bundle9 = new Bundle();
            bundle9.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
            DeviceInfoBean deviceInfoBean25 = this.device1;
            if (deviceInfoBean25 != null) {
                bundle9.putSerializable("device1", deviceInfoBean25);
            }
            DeviceInfoBean deviceInfoBean26 = this.device2;
            if (deviceInfoBean26 != null) {
                bundle9.putSerializable("device2", deviceInfoBean26);
            }
            DeviceInfoBean deviceInfoBean27 = this.nvrDevice;
            if (deviceInfoBean27 != null) {
                bundle9.putSerializable("nvrDevice", deviceInfoBean27);
            }
            intent12.putExtras(bundle9);
            intent12.putExtra("iotId", this.device.getIotId());
            startActivity(intent12);
            return;
        }
        if (view2.getId() == R.id.item_my_cloud_storage) {
            return;
        }
        if (view2.getId() == R.id.item_yun_pay) {
            Intent intent13 = new Intent(getActivity(), (Class<?>) PayYunServiceActivity2.class);
            intent13.putExtra("iotId", this.iotId);
            intent13.putExtra(AlinkConstants.KEY_DN, this.device.getDeviceName());
            intent13.putExtra(AlinkConstants.KEY_PK, this.device.getProductKey());
            startActivity(intent13);
            return;
        }
        if (view2.getId() == R.id.item_traffic_pay) {
            if (TextUtils.isEmpty(this.IccId)) {
                showToast(getResources().getString(R.string.query_traffic_fail));
                return;
            }
            if (((SharePreferenceManager.getInstance().getPageControlEx(this.iotId) & 524288) >> 19) == 1) {
                if (SharePreferenceManager.getInstance().getIccId1(this.device.getIotId()).equals("") && SharePreferenceManager.getInstance().getIccId2(this.device.getIotId()).equals("")) {
                    Intent intent14 = new Intent(getActivity(), (Class<?>) Traffic4GActivity.class);
                    intent14.putExtra("iccid", this.IccId);
                    intent14.putExtra("iotId", this.iotId);
                    DeviceInfoBean deviceInfoBean28 = this.nvrDevice;
                    if (deviceInfoBean28 != null) {
                        intent14.putExtra(AlinkConstants.KEY_DN, deviceInfoBean28.getDeviceName());
                        intent14.putExtra(AlinkConstants.KEY_PK, this.nvrDevice.getProductKey());
                    } else {
                        intent14.putExtra(AlinkConstants.KEY_DN, this.device.getDeviceName());
                        intent14.putExtra(AlinkConstants.KEY_PK, this.device.getProductKey());
                    }
                    startActivity(intent14);
                    return;
                }
                Intent intent15 = new Intent(this, (Class<?>) Net4GSwitchActivity.class);
                Bundle bundle10 = new Bundle();
                bundle10.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
                DeviceInfoBean deviceInfoBean29 = this.device1;
                if (deviceInfoBean29 != null) {
                    bundle10.putSerializable("device1", deviceInfoBean29);
                }
                DeviceInfoBean deviceInfoBean30 = this.nvrDevice;
                if (deviceInfoBean30 != null) {
                    bundle10.putSerializable("nvrDevice", deviceInfoBean30);
                }
                intent15.putExtras(bundle10);
                startActivity(intent15);
                return;
            }
            Intent intent16 = new Intent(getActivity(), (Class<?>) Traffic4GActivity.class);
            intent16.putExtra("iccid", this.IccId);
            intent16.putExtra("iotId", this.iotId);
            DeviceInfoBean deviceInfoBean31 = this.nvrDevice;
            if (deviceInfoBean31 != null) {
                intent16.putExtra(AlinkConstants.KEY_DN, deviceInfoBean31.getDeviceName());
                intent16.putExtra(AlinkConstants.KEY_PK, this.nvrDevice.getProductKey());
            } else {
                intent16.putExtra(AlinkConstants.KEY_DN, this.device.getDeviceName());
                intent16.putExtra(AlinkConstants.KEY_PK, this.device.getProductKey());
            }
            startActivity(intent16);
            return;
        }
        if (view2.getId() == R.id.image_setting) {
            Intent intent17 = new Intent(this, (Class<?>) ImageSettingActivity.class);
            Bundle bundle11 = new Bundle();
            bundle11.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
            DeviceInfoBean deviceInfoBean32 = this.device1;
            if (deviceInfoBean32 != null) {
                bundle11.putSerializable("device1", deviceInfoBean32);
            }
            DeviceInfoBean deviceInfoBean33 = this.device2;
            if (deviceInfoBean33 != null) {
                bundle11.putSerializable("device2", deviceInfoBean33);
            }
            DeviceInfoBean deviceInfoBean34 = this.nvrDevice;
            if (deviceInfoBean34 != null) {
                bundle11.putSerializable("nvrDevice", deviceInfoBean34);
            }
            intent17.putExtras(bundle11);
            intent17.putExtra("iotId", this.device.getIotId());
            startActivity(intent17);
            return;
        }
        if (view2.getId() == R.id.gunball_setting) {
            Intent intent18 = new Intent(this, (Class<?>) GunballSettingActivity.class);
            intent18.putExtra("iotId", this.shotIotId);
            intent18.putExtra("nvrOwner", this.nvrOwner);
            startActivity(intent18);
            return;
        }
        if (view2.getId() == R.id.item_low_power) {
            Intent intent19 = new Intent(this, (Class<?>) LowPowerSettingsActivity.class);
            intent19.putExtra("iotId", this.iotId);
            startActivity(intent19);
        } else if (view2.getId() == R.id.item_power_model) {
            Intent intent20 = new Intent(this, (Class<?>) PowerModeSettingsActivity.class);
            intent20.putExtra("iotId", this.iotId);
            startActivity(intent20);
        }
    }

    /* JADX INFO: renamed from: activity.SettingsActivity$33, reason: invalid class name */
    class AnonymousClass33 implements View.OnClickListener {
        AnonymousClass33() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            IPCManager.getInstance().getDevice(SettingsActivity.this.device.getIotId()).reboot(new IPanelCallback() { // from class: activity.SettingsActivity.33.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj) {
                    Log.e(SettingsActivity.this.TAG, "reboot onComplete: " + z);
                    if (!z || obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.33.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(SettingsActivity.this.getActivity(), R.string.restart_dev_failed, 0).show();
                                }
                            });
                        } else {
                            SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.33.1.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(SettingsActivity.this.getActivity(), R.string.restart_dev_succeed, 0).show();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    /* JADX INFO: renamed from: activity.SettingsActivity$34, reason: invalid class name */
    class AnonymousClass34 implements View.OnClickListener {
        AnonymousClass34() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            IPCManager.getInstance().getDevice(SettingsActivity.this.device.getIotId()).reset(new IPanelCallback() { // from class: activity.SettingsActivity.34.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj) {
                    Log.e(SettingsActivity.this.TAG, "reset onComplete: " + z);
                    if (!z || obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.34.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(SettingsActivity.this.getActivity(), R.string.reset_setting_failed, 0).show();
                                }
                            });
                        } else {
                            SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.34.1.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(SettingsActivity.this.getActivity(), R.string.reset_setting_succeed, 0).show();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    private void getYunServiceBuild(final String str) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/cloudstorage/presented/get").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.SettingsActivity.35
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, SettingsActivity.this.TAG, exc.getLocalizedMessage());
                SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.35.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(SettingsActivity.this.getActivity(), R.string.query_yunservice_fail, 0).show();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.e(true, SettingsActivity.this.TAG, "getYunServiceBuild:" + ioTResponse.getData() + "");
                int code = ioTResponse.getCode();
                ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.35.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(SettingsActivity.this.getActivity(), R.string.query_yunservice_fail, 0).show();
                        }
                    });
                    return;
                }
                try {
                    com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(ioTResponse.getData().toString());
                    int intValue = object.getIntValue("consumed");
                    int intValue2 = object.getIntValue("expired");
                    String string = object.getString(AUserTrack.UTKEY_START_TIME);
                    String string2 = object.getString(AUserTrack.UTKEY_END_TIME);
                    if (intValue2 == 0) {
                        Intent intent = new Intent(SettingsActivity.this.getActivity(), (Class<?>) NonEventYunServiceGiftActivity.class);
                        intent.putExtra("iotId", str);
                        intent.putExtra("consumed", intValue);
                        intent.putExtra("expired", intValue2);
                        intent.putExtra(AUserTrack.UTKEY_START_TIME, string);
                        intent.putExtra(AUserTrack.UTKEY_END_TIME, string2);
                        SettingsActivity.this.startActivity(intent);
                    } else {
                        Intent intent2 = new Intent(SettingsActivity.this.getActivity(), (Class<?>) PayYunServiceActivity2.class);
                        intent2.putExtra("iotId", str);
                        intent2.putExtra(AlinkConstants.KEY_DN, SettingsActivity.this.device.getDeviceName());
                        intent2.putExtra(AlinkConstants.KEY_PK, SettingsActivity.this.device.getProductKey());
                        SettingsActivity.this.startActivity(intent2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.35.3
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(SettingsActivity.this.getActivity(), R.string.query_yunservice_fail, 0).show();
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindDevice(final String str) {
        showProgressDialog(R.string.remove_ing);
        HashMap map = new HashMap();
        map.put("iotId", str);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/uc/unbindAccountAndDev").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.SettingsActivity.36
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, SettingsActivity.this.TAG, "onFailure");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                LogEx.d(true, SettingsActivity.this.TAG, "unbindDevice onResponse: code: " + code);
                final String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.36.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SettingsActivity.this.dismissProgressDialog();
                            Toast.makeText(SettingsActivity.this.getActivity(), localizedMsg, 0).show();
                        }
                    });
                } else {
                    SettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.SettingsActivity.36.2
                        @Override // java.lang.Runnable
                        public void run() {
                            SettingsActivity.this.unbindDeviceSucceed(str);
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

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.wakeUpHandler.removeCallbacksAndMessages(null);
        this.wakeUpHandler = null;
        SharePreferenceManager.getInstance().unRegisterOnCallSetListener(this.mSPModifyListener);
        ChannelManager.getInstance().unRegisterListener(this.iMobileMsgListener);
        EventBus.getDefault().unregister(this);
    }

    public void triggerRecordPlan() {
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/record/plan/query").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(new HashMap()).build(), new AnonymousClass38());
    }

    /* JADX INFO: renamed from: activity.SettingsActivity$38, reason: invalid class name */
    class AnonymousClass38 implements IoTCallback {
        AnonymousClass38() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, SettingsActivity.this.TAG, exc.getLocalizedMessage());
            SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.38.1
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                    SettingsActivity.this.swYunServiceEventPlan.setChecked(false);
                    SettingsActivity.this.dismissProgressDialog();
                }
            });
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            LogEx.e(true, SettingsActivity.this.TAG, "triggerRecordPlan:" + ioTResponse.getData() + "");
            int code = ioTResponse.getCode();
            ioTResponse.getLocalizedMsg();
            if (code != 200) {
                SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.38.2
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        SettingsActivity.this.swYunServiceEventPlan.setChecked(!SettingsActivity.this.swYunServiceEventPlan.isChecked());
                        SettingsActivity.this.dismissProgressDialog();
                    }
                });
                return;
            }
            Object data = ioTResponse.getData();
            if (data == null) {
                SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.38.3
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        SettingsActivity.this.swYunServiceEventPlan.setChecked(!SettingsActivity.this.swYunServiceEventPlan.isChecked());
                        SettingsActivity.this.dismissProgressDialog();
                    }
                });
                return;
            }
            JSONObject jSONObject = (JSONObject) data;
            int i = -1;
            try {
                i = jSONObject.getInt(AlinkConstants.KEY_TOTAL);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i == 0) {
                HashMap map = new HashMap();
                map.put("name", String.valueOf(System.currentTimeMillis()));
                map.put("allDay", 1);
                map.put("timeSectionList", new ArrayList());
                new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/record/plan/set").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.SettingsActivity.38.4
                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onFailure(IoTRequest ioTRequest2, Exception exc) {
                        LogEx.d(true, SettingsActivity.this.TAG, exc.getLocalizedMessage());
                        SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.38.4.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                SettingsActivity.this.swYunServiceEventPlan.setChecked(!SettingsActivity.this.swYunServiceEventPlan.isChecked());
                                SettingsActivity.this.dismissProgressDialog();
                            }
                        });
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onResponse(IoTRequest ioTRequest2, IoTResponse ioTResponse2) {
                        LogEx.e(true, SettingsActivity.this.TAG, "triggerRecordPlan:" + ioTResponse2.getData() + "");
                        int code2 = ioTResponse2.getCode();
                        ioTResponse2.getLocalizedMsg();
                        if (code2 != 200) {
                            SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.38.4.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                    SettingsActivity.this.swYunServiceEventPlan.setChecked(!SettingsActivity.this.swYunServiceEventPlan.isChecked());
                                    SettingsActivity.this.dismissProgressDialog();
                                }
                            });
                            return;
                        }
                        Object data2 = ioTResponse2.getData();
                        if (data2 == null) {
                            SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.38.4.3
                                @Override // java.lang.Runnable
                                public void run() {
                                    SettingsActivity.this.dismissProgressDialog();
                                }
                            });
                            return;
                        }
                        try {
                            SettingsActivity.this.bindPlanToDevice(((JSONObject) data2).getString("planId"));
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.38.4.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                    SettingsActivity.this.swYunServiceEventPlan.setChecked(!SettingsActivity.this.swYunServiceEventPlan.isChecked());
                                    SettingsActivity.this.dismissProgressDialog();
                                }
                            });
                        }
                    }
                });
                return;
            }
            try {
                JSONArray jSONArray = jSONObject.getJSONArray("recordPlanList");
                if (jSONArray == null || jSONArray.length() <= 0) {
                    throw new Exception();
                }
                if (jSONArray.length() > 0) {
                    SettingsActivity.this.getBindYunServicePlan(SettingsActivity.this.iotId, jSONArray.getJSONObject(0).getString("planId"), true);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.38.5
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        SettingsActivity.this.swYunServiceEventPlan.setChecked(!SettingsActivity.this.swYunServiceEventPlan.isChecked());
                        SettingsActivity.this.dismissProgressDialog();
                    }
                });
            }
        }
    }

    public void getBindYunServicePlan(String str, final String str2, final boolean z) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/record/plan/getbyiotid").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.SettingsActivity.39
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, SettingsActivity.this.TAG, exc.getLocalizedMessage());
                SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.39.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SettingsActivity.this.dismissProgressDialog();
                        if (z) {
                            Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            SettingsActivity.this.swYunServiceEventPlan.setChecked(!SettingsActivity.this.swYunServiceEventPlan.isChecked());
                        }
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.e(true, SettingsActivity.this.TAG, "getBindYunServiceEventPlan:" + ioTResponse.getData() + "");
                int code = ioTResponse.getCode();
                ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    if (code == 9110 && z) {
                        SettingsActivity.this.bindPlanToDevice(str2);
                    } else {
                        SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.39.2
                            @Override // java.lang.Runnable
                            public void run() {
                                SettingsActivity.this.dismissProgressDialog();
                            }
                        });
                    }
                } else {
                    Object data = ioTResponse.getData();
                    if (data == null) {
                        SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.39.3
                            @Override // java.lang.Runnable
                            public void run() {
                                SettingsActivity.this.dismissProgressDialog();
                            }
                        });
                        return;
                    }
                    try {
                        if (((JSONObject) data).getInt("allDay") == 0) {
                            throw new Exception();
                        }
                        SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.39.4
                            @Override // java.lang.Runnable
                            public void run() {
                                SettingsActivity.this.isYunServiceEventPlanChecked = true;
                                SettingsActivity.this.swYunServiceEventPlan.setChecked(true);
                                SettingsActivity.this.dismissProgressDialog();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (z) {
                            SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.39.5
                                @Override // java.lang.Runnable
                                public void run() {
                                    SettingsActivity.this.isYunServiceEventPlanChecked = false;
                                    SettingsActivity.this.swYunServiceEventPlan.setChecked(false);
                                    SettingsActivity.this.dismissProgressDialog();
                                }
                            });
                        } else {
                            SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.39.6
                                @Override // java.lang.Runnable
                                public void run() {
                                    SettingsActivity.this.isYunServiceEventPlanChecked = false;
                                    SettingsActivity.this.swYunServiceEventPlan.setChecked(false);
                                }
                            });
                        }
                    }
                }
                if (z) {
                    return;
                }
                SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.39.7
                    @Override // java.lang.Runnable
                    public void run() {
                        SettingsActivity.this.dismissProgressDialog();
                    }
                });
            }
        });
    }

    public void bindPlanToDevice(String str) {
        HashMap map = new HashMap();
        map.put("planId", str);
        map.put("iotId", this.iotId);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/record/plan/bind").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.SettingsActivity.40
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, SettingsActivity.this.TAG, exc.getLocalizedMessage());
                SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.40.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                        SettingsActivity.this.swYunServiceEventPlan.setChecked(!SettingsActivity.this.swYunServiceEventPlan.isChecked());
                        SettingsActivity.this.dismissProgressDialog();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.e(true, SettingsActivity.this.TAG, "bindPlanToDevice:" + ioTResponse.getData() + "");
                final int code = ioTResponse.getCode();
                ioTResponse.getLocalizedMsg();
                if (code == 200) {
                    SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.40.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            SettingsActivity.this.isYunServiceEventPlanChecked = true;
                            SettingsActivity.this.swYunServiceEventPlan.setChecked(true);
                        }
                    });
                }
                SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.40.3
                    @Override // java.lang.Runnable
                    public void run() {
                        if (code != 200) {
                            Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            SettingsActivity.this.swYunServiceEventPlan.setChecked(!SettingsActivity.this.swYunServiceEventPlan.isChecked());
                        }
                        SettingsActivity.this.dismissProgressDialog();
                    }
                });
            }
        });
    }

    private void setNet4GEnableSwitch(int i) {
        HashMap map = new HashMap();
        map.put(Constants.Net4GEnableSwitch, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.SettingsActivity.41
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                SettingsActivity.this.dismissProgressDialog();
            }
        });
    }

    public void unbindPlanToDevice() {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/record/plan/unbind").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.SettingsActivity.42
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, SettingsActivity.this.TAG, exc.getLocalizedMessage());
                SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.42.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        SettingsActivity.this.swYunServiceEventPlan.setChecked(!SettingsActivity.this.swYunServiceEventPlan.isChecked());
                        SettingsActivity.this.dismissProgressDialog();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.e(true, SettingsActivity.this.TAG, "unbindPlanToDevice:" + ioTResponse.getData() + "");
                final int code = ioTResponse.getCode();
                if (code == 200) {
                    SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.42.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            SettingsActivity.this.isYunServiceEventPlanChecked = false;
                            SettingsActivity.this.swYunServiceEventPlan.setChecked(false);
                        }
                    });
                }
                ioTResponse.getLocalizedMsg();
                SettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.SettingsActivity.42.3
                    @Override // java.lang.Runnable
                    public void run() {
                        if (code != 200) {
                            Toast.makeText(SettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            SettingsActivity.this.swYunServiceEventPlan.setChecked(!SettingsActivity.this.swYunServiceEventPlan.isChecked());
                        }
                        SettingsActivity.this.dismissProgressDialog();
                    }
                });
            }
        });
    }
}
