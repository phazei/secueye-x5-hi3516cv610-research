package activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import bean.AlertBean;
import bean.DeviceInfoBean;
import com.alibaba.fastjson.JSON;
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
import com.heytap.mcssdk.constant.IntentConstant;
import com.seculink.app.R;
import config.AppConfig;
import config.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.android.agoo.common.AgooConstants;
import sdk.ChannelManager;
import sdk.IPCManager;
import tools.LocationUtil;
import tools.LogEx;
import tools.MyCallback;
import tools.OnMultiClickListener;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.ItemView;
import view.LongItemView;
import view.SelectorDialogFragment;
import view.SettingTitleView;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class AlarmModeActivity extends CommonActivity {
    private boolean alertLightSwitch;
    private boolean alertSwitch;
    private SelectorDialogFragment batteryDialog;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean device2;
    private TitleView fl_titlebar;
    LongItemView gexinhua;
    private String iotId;
    ItemView itemAlarmLight;
    ItemView itemAlert;
    LongItemView itemMobilePush;
    ItemView itemMobileStrongPush;
    LongItemView item_online_push;
    ConstraintLayout layout_main;
    LinearLayout layout_push_help;
    private LinearLayout lightL;
    SettingTitleView lightTitle;
    private DeviceInfoBean nvrDevice;
    private boolean pushSwitch;
    ItemView redBlueLight;
    private boolean redBlueLightSwitch;
    private SharedPreferences sharedPreferences;
    private boolean strongPushSwitch;
    SwitchCompat swAlarmLight;
    SwitchCompat swAlert;
    SwitchCompat swMobilePush;
    SwitchCompat swMobileStrongPush;
    SwitchCompat swOnlinePush;
    SwitchCompat swRedBlueLight;
    private Handler uiHandler = new Handler();
    private boolean isNeedBattery = false;
    List<AlertBean> setOnlinePushList = new ArrayList();
    private SharePreferenceManager.OnCallSetListener mSPModifyListener = new SharePreferenceManager.OnCallSetListener() { // from class: activity.AlarmModeActivity.22
        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(final String str, final String str2) {
            AlarmModeActivity.this.uiHandler.post(new Runnable() { // from class: activity.AlarmModeActivity.22.1
                @Override // java.lang.Runnable
                public void run() {
                    String str3 = str2;
                    if (str3 == null || str3.trim().equals("")) {
                        return;
                    }
                    if (str2.equals(AlarmModeActivity.this.getString(R.string.alert_switch_key))) {
                        AlarmModeActivity.this.alertSwitch = SharePreferenceManager.getInstance().getAlertSwitch(str);
                        AlarmModeActivity.this.swAlert.setChecked(AlarmModeActivity.this.alertSwitch);
                        return;
                    }
                    if (str2.equals(AlarmModeActivity.this.getString(R.string.push_switch_key))) {
                        if (SharePreferenceManager.getInstance().getEventRecord(str) != 1) {
                            AlarmModeActivity.this.pushSwitch = SharePreferenceManager.getInstance().getPushSwitch(str);
                            AlarmModeActivity.this.swMobilePush.setChecked(AlarmModeActivity.this.pushSwitch);
                            return;
                        }
                        return;
                    }
                    if (str2.equals(AlarmModeActivity.this.getString(R.string.status_light_switch_key))) {
                        AlarmModeActivity.this.alertLightSwitch = SharePreferenceManager.getInstance().getStatusLightSwitch(str);
                        AlarmModeActivity.this.itemAlarmLight.setVisibility(0);
                        AlarmModeActivity.this.swAlarmLight.setChecked(AlarmModeActivity.this.alertLightSwitch);
                    } else {
                        if (!str2.equals(AlarmModeActivity.this.getString(R.string.strong_reminder_switch)) || SharePreferenceManager.getInstance().getEventRecord(str) == 1) {
                            return;
                        }
                        AlarmModeActivity.this.strongPushSwitch = SharePreferenceManager.getInstance().getStrongReminderSwitch(str) == 1;
                        AlarmModeActivity.this.swMobileStrongPush.setChecked(AlarmModeActivity.this.strongPushSwitch);
                    }
                }
            });
        }
    };
    private ChannelManager.IMobileMsgListener iMobileMsgListener = new ChannelManager.IMobileMsgListener() { // from class: activity.AlarmModeActivity.23
        @Override // sdk.ChannelManager.IMobileMsgListener
        public void onCommand(String str, String str2) {
            Log.e(AlarmModeActivity.this.TAG, "ChannelManager.IMobileMsgListener    topic:" + str + "     msg:" + str2);
            if (str.equals("/thing/properties")) {
                SettingsCtrl.getInstance().getProperties(AlarmModeActivity.this.iotId, new MyCallback() { // from class: activity.AlarmModeActivity.23.1
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
            }
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_alarm_mode;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (ConstraintLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.AlarmModeActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                AlarmModeActivity.this.finish();
            }
        });
        this.itemAlert = (ItemView) findViewById(R.id.item_alert);
        this.itemAlert.addRightView(new SwitchCompat(this));
        this.swAlert = (SwitchCompat) this.itemAlert.getRightView();
        this.swAlert.setTextOff("");
        this.swAlert.setTextOn("");
        this.swAlert.setText("");
        this.swAlert.setThumbDrawable(null);
        this.swAlert.setBackgroundResource(R.drawable.sel_switch);
        this.itemAlarmLight = (ItemView) findViewById(R.id.item_alert_light);
        this.itemAlarmLight.addRightView(new SwitchCompat(this));
        this.swAlarmLight = (SwitchCompat) this.itemAlarmLight.getRightView();
        this.swAlarmLight.setTextOff("");
        this.swAlarmLight.setTextOn("");
        this.swAlarmLight.setText("");
        this.swAlarmLight.setThumbDrawable(null);
        this.swAlarmLight.setBackgroundResource(R.drawable.sel_switch);
        this.itemMobilePush = (LongItemView) findViewById(R.id.item_mobile_push);
        this.itemMobilePush.addRightView(new SwitchCompat(this));
        this.swMobilePush = (SwitchCompat) this.itemMobilePush.getRightView();
        this.swMobilePush.setTextOff("");
        this.swMobilePush.setTextOn("");
        this.swMobilePush.setText("");
        this.swMobilePush.setThumbDrawable(null);
        this.swMobilePush.setBackgroundResource(R.drawable.sel_switch);
        this.itemMobileStrongPush = (ItemView) findViewById(R.id.item_mobile_strong_push);
        this.itemMobileStrongPush.addRightView(new SwitchCompat(this));
        this.swMobileStrongPush = (SwitchCompat) this.itemMobileStrongPush.getRightView();
        this.swMobileStrongPush.setTextOff("");
        this.swMobileStrongPush.setTextOn("");
        this.swMobileStrongPush.setText("");
        this.swMobileStrongPush.setThumbDrawable(null);
        this.swMobileStrongPush.setBackgroundResource(R.drawable.sel_switch);
        this.gexinhua = (LongItemView) findViewById(R.id.gexinhua);
        this.gexinhua.setOnClickListener(new View.OnClickListener() { // from class: activity.AlarmModeActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(AlarmModeActivity.this.getApplication(), (Class<?>) VigilanceActivity.class);
                intent.putExtra("iotId", AlarmModeActivity.this.iotId);
                AlarmModeActivity.this.startActivity(intent);
            }
        });
        this.lightTitle = (SettingTitleView) findViewById(R.id.light_title);
        this.redBlueLight = (ItemView) findViewById(R.id.red_blue_light);
        this.redBlueLight.addRightView(new SwitchCompat(this));
        this.swRedBlueLight = (SwitchCompat) this.redBlueLight.getRightView();
        this.swRedBlueLight.setTextOff("");
        this.swRedBlueLight.setTextOn("");
        this.swRedBlueLight.setText("");
        this.swRedBlueLight.setThumbDrawable(null);
        this.swRedBlueLight.setBackgroundResource(R.drawable.sel_switch);
        this.lightL = (LinearLayout) findViewById(R.id.light_ll);
        this.layout_push_help = (LinearLayout) findViewById(R.id.layout_push_help);
        this.layout_push_help.setOnClickListener(new OnMultiClickListener() { // from class: activity.AlarmModeActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                AlarmModeActivity alarmModeActivity = AlarmModeActivity.this;
                alarmModeActivity.startActivity(new Intent(alarmModeActivity, (Class<?>) PushHelpActivity.class));
            }
        });
        this.item_online_push = (LongItemView) findViewById(R.id.item_online_push);
        this.item_online_push.addRightView(new SwitchCompat(this));
        this.swOnlinePush = (SwitchCompat) this.item_online_push.getRightView();
        this.swOnlinePush.setTextOff("");
        this.swOnlinePush.setTextOn("");
        this.swOnlinePush.setText("");
        this.swOnlinePush.setThumbDrawable(null);
        this.swOnlinePush.setBackgroundResource(R.drawable.sel_switch);
        getOnlinePush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getOnlinePush() {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/message/center/device/notice/list").setApiVersion("1.0.6").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.AlarmModeActivity.4
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.e(true, AlarmModeActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() != 200 || ioTResponse.getData() == null || "".equals(ioTResponse.getData().toString().trim())) {
                    return;
                }
                AlarmModeActivity.this.setOnlinePushList.clear();
                List array = JSON.parseArray(ioTResponse.getData().toString(), AlertBean.class);
                boolean z = false;
                for (int i = 0; i < array.size(); i++) {
                    if (((AlertBean) array.get(i)).getEventId().equals("116170") || ((AlertBean) array.get(i)).getEventId().equals("116169")) {
                        if (((AlertBean) array.get(i)).getNoticeEnabled()) {
                            z = true;
                        }
                        AlarmModeActivity.this.setOnlinePushList.add((AlertBean) array.get(i));
                    }
                }
                AlarmModeActivity.this.swOnlinePush.setChecked(z);
            }
        });
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.iotId = this.device.getIotId();
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.device2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
        }
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        SharePreferenceManager.getInstance().registerOnCallSetListener(this.mSPModifyListener);
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.AlarmModeActivity.5
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                AlarmModeActivity.this.runOnUiThread(new Runnable() { // from class: activity.AlarmModeActivity.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (SharePreferenceManager.getInstance().getDisplayAlert(AlarmModeActivity.this.iotId) == 0) {
                            AlarmModeActivity.this.gexinhua.setVisibility(8);
                            AlarmModeActivity.this.itemAlert.setLineShow(false);
                        } else {
                            AlarmModeActivity.this.gexinhua.setVisibility(0);
                            AlarmModeActivity.this.itemAlert.setLineShow(true);
                        }
                    }
                });
            }
        });
        ChannelManager.getInstance().registerListener(this.iMobileMsgListener);
        if (SharePreferenceManager.getInstance().getEventRecord(this.iotId) == 1) {
            getYunPush();
        } else {
            this.pushSwitch = SharePreferenceManager.getInstance().getPushSwitch(this.iotId);
            this.swMobilePush.setChecked(this.pushSwitch);
        }
        this.swMobilePush.setOnClickListener(new View.OnClickListener() { // from class: activity.AlarmModeActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                AlarmModeActivity.this.pushSwitch = !r4.pushSwitch;
                AlarmModeActivity alarmModeActivity = AlarmModeActivity.this;
                alarmModeActivity.updateDevPush(alarmModeActivity.pushSwitch, AlarmModeActivity.this.device.getIotId());
                if (AlarmModeActivity.this.device1 != null) {
                    new Handler().post(new Runnable() { // from class: activity.AlarmModeActivity.6.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AlarmModeActivity.this.updateDevPush(AlarmModeActivity.this.pushSwitch, AlarmModeActivity.this.device1.getIotId());
                        }
                    });
                }
                if (AlarmModeActivity.this.device2 != null) {
                    new Handler().post(new Runnable() { // from class: activity.AlarmModeActivity.6.2
                        @Override // java.lang.Runnable
                        public void run() {
                            AlarmModeActivity.this.updateDevPush(AlarmModeActivity.this.pushSwitch, AlarmModeActivity.this.device2.getIotId());
                        }
                    });
                }
                new Handler().post(new Runnable() { // from class: activity.AlarmModeActivity.6.3
                    @Override // java.lang.Runnable
                    public void run() {
                        AlarmModeActivity.this.updateDevParam(AlarmModeActivity.this.getString(R.string.push_switch_key), Integer.valueOf(AlarmModeActivity.this.pushSwitch ? 1 : 0));
                    }
                });
                new Handler().postDelayed(new Runnable() { // from class: activity.AlarmModeActivity.6.4
                    @Override // java.lang.Runnable
                    public void run() {
                        AlarmModeActivity.this.getYunPush();
                    }
                }, 50L);
            }
        });
        if (SharePreferenceManager.getInstance().getEventRecord(this.iotId) == 1) {
            getYunCallPush();
        } else {
            this.strongPushSwitch = SharePreferenceManager.getInstance().getStrongReminderSwitch(this.iotId) == 1;
            this.swMobileStrongPush.setChecked(this.strongPushSwitch);
        }
        this.swMobileStrongPush.setOnClickListener(new View.OnClickListener() { // from class: activity.AlarmModeActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                AlarmModeActivity.this.strongPushSwitch = !r4.strongPushSwitch;
                AlarmModeActivity alarmModeActivity = AlarmModeActivity.this;
                alarmModeActivity.updateDevCallPush(alarmModeActivity.strongPushSwitch, AlarmModeActivity.this.device.getIotId());
                if (AlarmModeActivity.this.device1 != null) {
                    new Handler().post(new Runnable() { // from class: activity.AlarmModeActivity.7.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AlarmModeActivity.this.updateDevCallPush(AlarmModeActivity.this.strongPushSwitch, AlarmModeActivity.this.device1.getIotId());
                        }
                    });
                }
                if (AlarmModeActivity.this.device2 != null) {
                    new Handler().post(new Runnable() { // from class: activity.AlarmModeActivity.7.2
                        @Override // java.lang.Runnable
                        public void run() {
                            AlarmModeActivity.this.updateDevCallPush(AlarmModeActivity.this.strongPushSwitch, AlarmModeActivity.this.device2.getIotId());
                        }
                    });
                }
                new Handler().post(new Runnable() { // from class: activity.AlarmModeActivity.7.3
                    @Override // java.lang.Runnable
                    public void run() {
                        AlarmModeActivity.this.updateDevParam(AlarmModeActivity.this.getString(R.string.strong_reminder_switch), Integer.valueOf(AlarmModeActivity.this.strongPushSwitch ? 1 : 0));
                    }
                });
                new Handler().postDelayed(new Runnable() { // from class: activity.AlarmModeActivity.7.4
                    @Override // java.lang.Runnable
                    public void run() {
                        AlarmModeActivity.this.getYunCallPush();
                    }
                }, 50L);
            }
        });
        this.alertSwitch = SharePreferenceManager.getInstance().getAlertSwitch(this.iotId);
        this.swAlert.setChecked(this.alertSwitch);
        this.swAlert.setOnClickListener(new View.OnClickListener() { // from class: activity.AlarmModeActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                AlarmModeActivity.this.alertSwitch = !r3.alertSwitch;
                AlarmModeActivity alarmModeActivity = AlarmModeActivity.this;
                alarmModeActivity.updateDevParam(alarmModeActivity.getString(R.string.alert_switch_key), Integer.valueOf(AlarmModeActivity.this.alertSwitch ? 1 : 0));
            }
        });
        this.alertLightSwitch = SharePreferenceManager.getInstance().getStatusLightSwitch(this.iotId);
        this.swRedBlueLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: activity.AlarmModeActivity.9
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z != AlarmModeActivity.this.redBlueLightSwitch) {
                    AlarmModeActivity.this.swRedBlueLight.setClickable(false);
                    AlarmModeActivity.this.setRedBlueLight(z ? 1 : 0);
                    AlarmModeActivity.this.swRedBlueLight.setClickable(true);
                }
            }
        });
        this.swOnlinePush.setOnClickListener(new View.OnClickListener() { // from class: activity.AlarmModeActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                for (int i = 0; i < AlarmModeActivity.this.setOnlinePushList.size(); i++) {
                    AlarmModeActivity alarmModeActivity = AlarmModeActivity.this;
                    alarmModeActivity.setOnlinePush(alarmModeActivity.setOnlinePushList.get(i));
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDevPush(boolean z, final String str) {
        Log.e("新版推送开关修改", "" + z);
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("eventType", 1);
        map.put("alarmType", 1);
        map.put("eventInterval", Integer.valueOf(AppConfig.PushInterval));
        map.put("switchOn", Boolean.valueOf(z));
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/set").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.AlarmModeActivity.11
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.e(true, AlarmModeActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                if (str.equals(AlarmModeActivity.this.device.getIotId())) {
                    if (code == 200) {
                        AlarmModeActivity.this.runOnUiThread(new Runnable() { // from class: activity.AlarmModeActivity.11.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    } else {
                        AlarmModeActivity.this.runOnUiThread(new Runnable() { // from class: activity.AlarmModeActivity.11.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDevCallPush(boolean z, final String str) {
        Log.e("强推送开关修改", "" + z);
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("eventType", 1);
        map.put("alarmType", 2);
        map.put("eventInterval", Integer.valueOf(AppConfig.PushInterval));
        map.put("switchOn", Boolean.valueOf(z));
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/set").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.AlarmModeActivity.12
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.e(true, AlarmModeActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                if (str.equals(AlarmModeActivity.this.device.getIotId())) {
                    if (code == 200) {
                        AlarmModeActivity.this.runOnUiThread(new Runnable() { // from class: activity.AlarmModeActivity.12.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    } else {
                        AlarmModeActivity.this.runOnUiThread(new Runnable() { // from class: activity.AlarmModeActivity.12.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getYunPush() {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        map.put("eventType", 1);
        map.put("alarmType", 1);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/get").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass13());
    }

    /* JADX INFO: renamed from: activity.AlarmModeActivity$13, reason: invalid class name */
    class AnonymousClass13 implements IoTCallback {
        AnonymousClass13() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.e(true, AlarmModeActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            if (ioTResponse.getCode() == 200) {
                JSONObject object = JSONObject.parseObject(ioTResponse.getData().toString());
                final int iIntValue = object.getInteger("eventInterval").intValue();
                final boolean zBooleanValue = object.getBoolean("switchOn").booleanValue();
                Log.e("新版推送间隔", "" + iIntValue);
                Log.e("新版推送开关", "" + zBooleanValue);
                AlarmModeActivity.this.runOnUiThread(new Runnable() { // from class: activity.AlarmModeActivity.13.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AlarmModeActivity.this.pushSwitch = zBooleanValue;
                        AlarmModeActivity.this.swMobilePush.setChecked(AlarmModeActivity.this.pushSwitch);
                        if (iIntValue == AppConfig.PushInterval || !AlarmModeActivity.this.pushSwitch) {
                            return;
                        }
                        Log.e("新版推送间隔不一致", "值为" + iIntValue);
                        HashMap map = new HashMap();
                        map.put("iotId", AlarmModeActivity.this.iotId);
                        map.put("eventType", 1);
                        map.put("alarmType", 1);
                        map.put("eventInterval", Integer.valueOf(AppConfig.PushInterval));
                        map.put("switchOn", Boolean.valueOf(AlarmModeActivity.this.pushSwitch));
                        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/set").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.AlarmModeActivity.13.1.1
                            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                            public void onFailure(IoTRequest ioTRequest2, Exception exc) {
                                LogEx.e(true, AlarmModeActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
                            }

                            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                            public void onResponse(IoTRequest ioTRequest2, IoTResponse ioTResponse2) {
                                if (ioTResponse2.getCode() == 200) {
                                    Log.e("新版推送间隔不一致", "修改成功");
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getYunCallPush() {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        map.put("eventType", 1);
        map.put("alarmType", 2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/get").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass14());
    }

    /* JADX INFO: renamed from: activity.AlarmModeActivity$14, reason: invalid class name */
    class AnonymousClass14 implements IoTCallback {
        AnonymousClass14() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.e(true, AlarmModeActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            if (ioTResponse.getCode() == 200) {
                JSONObject object = JSONObject.parseObject(ioTResponse.getData().toString());
                final int iIntValue = object.getInteger("eventInterval").intValue();
                final boolean zBooleanValue = object.getBoolean("switchOn").booleanValue();
                Log.e("强推送间隔", "" + iIntValue);
                Log.e("强推送开关", "" + zBooleanValue);
                AlarmModeActivity.this.runOnUiThread(new Runnable() { // from class: activity.AlarmModeActivity.14.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AlarmModeActivity.this.strongPushSwitch = zBooleanValue;
                        AlarmModeActivity.this.swMobileStrongPush.setChecked(AlarmModeActivity.this.strongPushSwitch);
                        if (iIntValue == AppConfig.PushInterval || !AlarmModeActivity.this.strongPushSwitch) {
                            return;
                        }
                        Log.e("强推送间隔不一致", "值为" + iIntValue);
                        HashMap map = new HashMap();
                        map.put("iotId", AlarmModeActivity.this.iotId);
                        map.put("eventType", 1);
                        map.put("alarmType", 2);
                        map.put("eventInterval", Integer.valueOf(AppConfig.PushInterval));
                        map.put("switchOn", Boolean.valueOf(AlarmModeActivity.this.strongPushSwitch));
                        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/set").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.AlarmModeActivity.14.1.1
                            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                            public void onFailure(IoTRequest ioTRequest2, Exception exc) {
                                LogEx.e(true, AlarmModeActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
                            }

                            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                            public void onResponse(IoTRequest ioTRequest2, IoTResponse ioTResponse2) {
                                if (ioTResponse2.getCode() == 200) {
                                    Log.e("强推送间隔不一致", "修改成功");
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOnlinePush(AlertBean alertBean) {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        map.put(IntentConstant.EVENT_ID, alertBean.getEventId());
        map.put("noticeEnabled", Boolean.valueOf(!alertBean.getNoticeEnabled()));
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/message/center/device/notice/set").setApiVersion("1.0.6").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.AlarmModeActivity.15
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.e(true, AlarmModeActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() == 200) {
                    AlarmModeActivity.this.getOnlinePush();
                }
            }
        });
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (SharePreferenceManager.getInstance().getAlarmLightSwitchDisplay(this.iotId) == 0) {
            this.lightTitle.setVisibility(8);
            this.redBlueLight.setVisibility(8);
            this.lightL.setVisibility(8);
        } else {
            this.redBlueLightSwitch = Integer.parseInt(SharePreferenceManager.getInstance().getAlarmLightSwitch(this.iotId)) == 1;
            this.swRedBlueLight.setChecked(this.redBlueLightSwitch);
        }
        if (SharePreferenceManager.getInstance().getStrongReminder(this.iotId) == 1) {
            this.itemMobileStrongPush.setVisibility(0);
            this.itemMobilePush.setLineShow(true);
            this.sharedPreferences = getSharedPreferences("FirstBattery", 0);
            if (this.sharedPreferences.getBoolean("battery_first", true)) {
                if (Build.VERSION.SDK_INT >= 23 && !isIgnoringBatteryOptimizations()) {
                    this.isNeedBattery = true;
                    this.batteryDialog = new SelectorDialogFragment(getString(R.string.strong_call_permission), getString(R.string.refuse), getString(R.string.refuse_never_remind), getString(R.string.agreee_jump_relevant));
                    this.batteryDialog.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.AlarmModeActivity.16
                        @Override // view.SelectorDialogFragment.OnItemClickListener
                        public void onItemClick(int i) {
                            switch (i) {
                                case 0:
                                    AlarmModeActivity.this.batteryDialog.dismiss();
                                    break;
                                case 1:
                                    AlarmModeActivity.this.sharedPreferences.edit().putBoolean("battery_first", false).apply();
                                    AlarmModeActivity.this.batteryDialog.dismiss();
                                    break;
                                case 2:
                                    AlarmModeActivity.this.sharedPreferences.edit().putBoolean("battery_first", false).apply();
                                    AlarmModeActivity.this.batteryDialog.dismiss();
                                    AlarmModeActivity.this.requestIgnoreBatteryOptimizations();
                                    break;
                            }
                        }
                    });
                }
            } else {
                this.isNeedBattery = false;
            }
        } else {
            this.itemMobileStrongPush.setVisibility(8);
            this.itemMobilePush.setLineShow(false);
        }
        if (this.isNeedBattery) {
            this.batteryDialog.showAllowingStateLoss(getSupportFragmentManager(), "", true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRedBlueLight(final int i) {
        HashMap map = new HashMap();
        map.put(Constants.AlarmLightSwitch, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.AlarmModeActivity.17
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (!z) {
                    AlarmModeActivity.this.uiHandler.post(new Runnable() { // from class: activity.AlarmModeActivity.17.2
                        @Override // java.lang.Runnable
                        public void run() {
                            AlarmModeActivity.this.swRedBlueLight.setChecked(!AlarmModeActivity.this.swRedBlueLight.isChecked());
                            Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        }
                    });
                    return;
                }
                if (obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        AlarmModeActivity.this.uiHandler.post(new Runnable() { // from class: activity.AlarmModeActivity.17.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AlarmModeActivity.this.swRedBlueLight.setChecked(!AlarmModeActivity.this.swRedBlueLight.isChecked());
                                Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    } else {
                        AlarmModeActivity.this.redBlueLightSwitch = i == 1;
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDevParam(String str, final Object obj) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.push_switch_key))) {
            map.put(Constants.PUSH_SWITCH, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.AlarmModeActivity.18
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (z) {
                        if (obj2 == null || "".equals(String.valueOf(obj2))) {
                            return;
                        }
                        JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                        if (object.containsKey("code") && object.getInteger("code").intValue() == 200) {
                            Log.e("推送老通道", "" + "1".equals(obj.toString()));
                            SharePreferenceManager.getInstance().setPushSwitch(AlarmModeActivity.this.iotId, "1".equals(obj.toString()));
                            return;
                        }
                        return;
                    }
                    Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                }
            });
            return;
        }
        if (str.equals(getString(R.string.alert_switch_key))) {
            map.put(Constants.ALERT_SWITCH, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.AlarmModeActivity.19
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            AlarmModeActivity.this.uiHandler.post(new Runnable() { // from class: activity.AlarmModeActivity.19.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    AlarmModeActivity.this.swAlert.setChecked(!AlarmModeActivity.this.swAlert.isChecked());
                                    Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setAlertSwitch(AlarmModeActivity.this.iotId, "1".equals(obj.toString()));
                            AlarmModeActivity.this.uiHandler.post(new Runnable() { // from class: activity.AlarmModeActivity.19.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                }
            });
        } else if (str.equals(getString(R.string.status_light_switch_key))) {
            map.put(Constants.STATUS_LIGHT_SWITCH_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.AlarmModeActivity.20
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z) {
                        AlarmModeActivity.this.uiHandler.post(new Runnable() { // from class: activity.AlarmModeActivity.20.3
                            @Override // java.lang.Runnable
                            public void run() {
                                AlarmModeActivity.this.swAlarmLight.setChecked(!AlarmModeActivity.this.swAlarmLight.isChecked());
                                Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                        return;
                    }
                    if (obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            AlarmModeActivity.this.uiHandler.post(new Runnable() { // from class: activity.AlarmModeActivity.20.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    AlarmModeActivity.this.swAlarmLight.setChecked(!AlarmModeActivity.this.swAlarmLight.isChecked());
                                    Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setStatusLightSwitch(AlarmModeActivity.this.iotId, "1".equals(obj.toString()));
                            AlarmModeActivity.this.uiHandler.post(new Runnable() { // from class: activity.AlarmModeActivity.20.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                }
            });
        } else if (str.equals(getString(R.string.strong_reminder_switch))) {
            map.put(Constants.StrongReminderSwitch, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.AlarmModeActivity.21
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z) {
                        AlarmModeActivity.this.uiHandler.post(new Runnable() { // from class: activity.AlarmModeActivity.21.2
                            @Override // java.lang.Runnable
                            public void run() {
                                AlarmModeActivity.this.swMobileStrongPush.setChecked(!AlarmModeActivity.this.swMobileStrongPush.isChecked());
                                Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                        return;
                    }
                    if (obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            AlarmModeActivity.this.uiHandler.post(new Runnable() { // from class: activity.AlarmModeActivity.21.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    AlarmModeActivity.this.swMobileStrongPush.setChecked(!AlarmModeActivity.this.swMobileStrongPush.isChecked());
                                    Toast.makeText(AlarmModeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setStrongReminderSwitch(AlarmModeActivity.this.iotId, Integer.parseInt(obj.toString()));
                        }
                    }
                }
            });
        }
    }

    @RequiresApi(api = 23)
    private boolean isIgnoringBatteryOptimizations() {
        PowerManager powerManager = (PowerManager) getSystemService("power");
        if (powerManager != null) {
            return powerManager.isIgnoringBatteryOptimizations(getPackageName());
        }
        return false;
    }

    @RequiresApi(api = 23)
    @SuppressLint({"BatteryLife"})
    public void requestIgnoreBatteryOptimizations() {
        try {
            Intent intent = new Intent("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToSetting() {
        if (isHuawei()) {
            goHuaweiSetting();
            return;
        }
        if (isXiaomi()) {
            goXiaomiSetting();
            return;
        }
        if (isOPPO()) {
            goOPPOSetting();
            return;
        }
        if (isVIVO()) {
            goVIVOSetting();
            return;
        }
        if (isMeizu()) {
            goMeizuSetting();
            return;
        }
        if (isSamsung()) {
            goSamsungSetting();
        } else if (isLeTV()) {
            goLetvSetting();
        } else if (isSmartisan()) {
            goSmartisanSetting();
        }
    }

    public boolean isHuawei() {
        if (Build.MANUFACTURER == null) {
            return false;
        }
        return Build.MANUFACTURER.toLowerCase().equals(AgooConstants.MESSAGE_SYSTEM_SOURCE_HUAWEI) || Build.MANUFACTURER.toLowerCase().equals(AgooConstants.MESSAGE_SYSTEM_SOURCE_HONOR);
    }

    private void goHuaweiSetting() {
        try {
            showActivity("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } catch (Exception unused) {
            showActivity("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
        }
    }

    private void showActivity(@NonNull String str) {
        startActivity(getPackageManager().getLaunchIntentForPackage(str));
    }

    private void showActivity(@NonNull String str, @NonNull String str2) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(str, str2));
        intent.addFlags(268435456);
        startActivity(intent);
    }

    public static boolean isXiaomi() {
        return Build.MANUFACTURER != null && Build.MANUFACTURER.toLowerCase().equals("xiaomi");
    }

    private void goXiaomiSetting() {
        showActivity("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
    }

    public static boolean isOPPO() {
        return Build.MANUFACTURER != null && Build.MANUFACTURER.toLowerCase().equals(AgooConstants.MESSAGE_SYSTEM_SOURCE_OPPO);
    }

    private void goOPPOSetting() {
        try {
            try {
                try {
                    showActivity("com.coloros.phonemanager");
                } catch (Exception unused) {
                    showActivity("com.coloros.oppoguardelf");
                }
            } catch (Exception unused2) {
                showActivity("com.oppo.safe");
            }
        } catch (Exception unused3) {
            showActivity("com.coloros.safecenter");
        }
    }

    public static boolean isVIVO() {
        return Build.MANUFACTURER != null && Build.MANUFACTURER.toLowerCase().equals("vivo");
    }

    private void goVIVOSetting() {
        showActivity("com.iqoo.secure");
    }

    public static boolean isMeizu() {
        return Build.MANUFACTURER != null && Build.MANUFACTURER.toLowerCase().equals(AgooConstants.MESSAGE_SYSTEM_SOURCE_MEIZU);
    }

    private void goMeizuSetting() {
        showActivity("com.meizu.safe");
    }

    public static boolean isSamsung() {
        return Build.MANUFACTURER != null && Build.MANUFACTURER.toLowerCase().equals(LocationUtil.MANUFACTURER_SAMSUNG);
    }

    private void goSamsungSetting() {
        try {
            showActivity("com.samsung.android.sm_cn");
        } catch (Exception unused) {
            showActivity("com.samsung.android.sm");
        }
    }

    public static boolean isLeTV() {
        return Build.MANUFACTURER != null && Build.MANUFACTURER.toLowerCase().equals("letv");
    }

    private void goLetvSetting() {
        showActivity("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity");
    }

    public static boolean isSmartisan() {
        return Build.MANUFACTURER != null && Build.MANUFACTURER.toLowerCase().equals("smartisan");
    }

    private void goSmartisanSetting() {
        showActivity("com.smartisanos.security");
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 111) {
            this.batteryDialog.dismiss();
        }
    }
}
