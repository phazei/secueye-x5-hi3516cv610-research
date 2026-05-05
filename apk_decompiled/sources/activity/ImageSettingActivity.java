package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import bean.DeviceInfoBean;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import config.Constants;
import java.util.HashMap;
import sdk.ChannelManager;
import sdk.IPCManager;
import tools.MyCallback;
import tools.OnMultiClickListener;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.ItemView;
import view.SelectorDialogFragment;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class ImageSettingActivity extends CommonActivity {
    private boolean alertLightSwitch;
    private boolean alertSwitch;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean device2;
    private TitleView fl_titlebar;
    private String iotId;
    ItemView itemAlert;
    ItemView itemLowPowerMode;
    ItemView itemPowerSavingMode;
    ItemView itemTemperatureHumidityBatteryDisplay;
    ConstraintLayout layout_main;
    ItemView liveModel;
    private SelectorDialogFragment liveModelFragment;
    private String[] liveModelList;
    View low_power_view;
    private DeviceInfoBean nvrDevice;
    ItemView osd_name;
    private boolean pushSwitch;
    SeekBar sound_setting;
    SwitchCompat swAlert;
    SwitchCompat swLowPowerMode;
    SwitchCompat swPowerSavingMode;
    SwitchCompat swTemperatureHumidityBatteryDisplay;
    View view1;
    View view2;
    LinearLayout voice_setting;
    private String wakeUpData;
    private Handler uiHandler = new Handler();
    private int rotationOrientate = 0;
    private SharePreferenceManager.OnCallSetListener mSPModifyListener = new SharePreferenceManager.OnCallSetListener() { // from class: activity.ImageSettingActivity.9
        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(final String str, final String str2) {
            ImageSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.ImageSettingActivity.9.1
                @Override // java.lang.Runnable
                public void run() {
                    String str3 = str2;
                    if (str3 == null || str3.trim().equals("")) {
                        return;
                    }
                    if (str2.equals(ImageSettingActivity.this.getString(R.string.alert_switch_key))) {
                        ImageSettingActivity.this.alertSwitch = SharePreferenceManager.getInstance().getAlertSwitch(str);
                        ImageSettingActivity.this.swAlert.setChecked(ImageSettingActivity.this.alertSwitch);
                    } else if (str2.equals(ImageSettingActivity.this.getString(R.string.push_switch_key))) {
                        ImageSettingActivity.this.pushSwitch = SharePreferenceManager.getInstance().getPushSwitch(str);
                    } else if (str2.equals(ImageSettingActivity.this.getString(R.string.status_light_switch_key))) {
                        ImageSettingActivity.this.alertLightSwitch = SharePreferenceManager.getInstance().getStatusLightSwitch(str);
                    }
                }
            });
        }
    };
    private ChannelManager.IMobileMsgListener iMobileMsgListener = new ChannelManager.IMobileMsgListener() { // from class: activity.ImageSettingActivity.10
        @Override // sdk.ChannelManager.IMobileMsgListener
        public void onCommand(String str, String str2) {
            Log.e(ImageSettingActivity.this.TAG, "ChannelManager.IMobileMsgListener    topic:" + str + "     msg:" + str2);
            if (str.equals("/thing/properties")) {
                SettingsCtrl.getInstance().getProperties(ImageSettingActivity.this.iotId, new MyCallback() { // from class: activity.ImageSettingActivity.10.1
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
            }
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_image_setting;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (ConstraintLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.ImageSettingActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                ImageSettingActivity.this.finish();
            }
        });
        this.voice_setting = (LinearLayout) findViewById(R.id.voice_setting);
        this.itemAlert = (ItemView) findViewById(R.id.image_flip_status_title);
        this.itemAlert.addRightView(new SwitchCompat(this));
        this.swAlert = (SwitchCompat) this.itemAlert.getRightView();
        this.swAlert.setTextOff("");
        this.swAlert.setTextOn("");
        this.swAlert.setText("");
        this.swAlert.setThumbDrawable(null);
        this.swAlert.setBackgroundResource(R.drawable.sel_switch);
        this.itemPowerSavingMode = (ItemView) findViewById(R.id.power_saving_mode);
        this.itemPowerSavingMode.addRightView(new SwitchCompat(this));
        this.swPowerSavingMode = (SwitchCompat) this.itemPowerSavingMode.getRightView();
        this.swPowerSavingMode.setTextOff("");
        this.swPowerSavingMode.setTextOn("");
        this.swPowerSavingMode.setText("");
        this.swPowerSavingMode.setThumbDrawable(null);
        this.swPowerSavingMode.setBackgroundResource(R.drawable.sel_switch);
        this.itemTemperatureHumidityBatteryDisplay = (ItemView) findViewById(R.id.temperature_humidity_battery_display);
        this.itemTemperatureHumidityBatteryDisplay.addRightView(new SwitchCompat(this));
        this.swTemperatureHumidityBatteryDisplay = (SwitchCompat) this.itemTemperatureHumidityBatteryDisplay.getRightView();
        this.swTemperatureHumidityBatteryDisplay.setTextOff("");
        this.swTemperatureHumidityBatteryDisplay.setTextOn("");
        this.swTemperatureHumidityBatteryDisplay.setText("");
        this.swTemperatureHumidityBatteryDisplay.setThumbDrawable(null);
        this.swTemperatureHumidityBatteryDisplay.setBackgroundResource(R.drawable.sel_switch);
        this.low_power_view = findViewById(R.id.low_power_view);
        this.view1 = findViewById(R.id.view1);
        this.view2 = findViewById(R.id.view2);
        this.itemLowPowerMode = (ItemView) findViewById(R.id.low_power_mode);
        this.itemLowPowerMode.addRightView(new SwitchCompat(this));
        this.swLowPowerMode = (SwitchCompat) this.itemLowPowerMode.getRightView();
        this.swLowPowerMode.setTextOff("");
        this.swLowPowerMode.setTextOn("");
        this.swLowPowerMode.setText("");
        this.swLowPowerMode.setThumbDrawable(null);
        this.swLowPowerMode.setBackgroundResource(R.drawable.sel_switch);
        this.osd_name = (ItemView) findViewById(R.id.osd_name);
        this.osd_name.setOnClickListener(new View.OnClickListener() { // from class: activity.ImageSettingActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ImageSettingActivity.this.showTwo();
            }
        });
        this.sound_setting = (SeekBar) findViewById(R.id.sound_setting);
        this.sound_setting.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: activity.ImageSettingActivity.3
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(ImageSettingActivity.this.TAG, "onStopTrackingTouch: " + seekBar.getProgress());
                ImageSettingActivity.this.setAOVolumeSize(seekBar.getProgress());
            }
        });
        this.liveModelList = new String[]{getString(R.string.show_mode1), getString(R.string.live_model_2)};
        this.liveModel = (ItemView) findViewById(R.id.live_model);
        if (SharePreferenceManager.getInstance().getFakeDualShow(this.iotId) == 0) {
            this.liveModel.setVisibility(8);
            this.osd_name.setLineShow(false);
        } else {
            this.liveModel.setRightText(this.liveModelList[SharePreferenceManager.getInstance().getFakeDualEnable(this.iotId)]);
        }
        this.liveModelFragment = new SelectorDialogFragment(getString(R.string.live_model), this.liveModelList);
        this.liveModelFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.ImageSettingActivity.4
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                ImageSettingActivity.this.updateFakeDual(Integer.valueOf(i));
            }
        });
        this.liveModel.setOnClickListener(new OnMultiClickListener() { // from class: activity.ImageSettingActivity.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                ImageSettingActivity.this.liveModelFragment.showAllowingStateLoss(ImageSettingActivity.this.getSupportFragmentManager(), "", SharePreferenceManager.getInstance().getFakeDualEnable(ImageSettingActivity.this.device.getIotId()));
            }
        });
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        this.iotId = intent.getStringExtra("iotId");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
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
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.ImageSettingActivity.6
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                ImageSettingActivity.this.runOnUiThread(new Runnable() { // from class: activity.ImageSettingActivity.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (SharePreferenceManager.getInstance().getDisplayVoice(ImageSettingActivity.this.iotId) == 0) {
                            ImageSettingActivity.this.voice_setting.setVisibility(8);
                            ImageSettingActivity.this.view2.setVisibility(8);
                        } else {
                            ImageSettingActivity.this.voice_setting.setVisibility(0);
                            ImageSettingActivity.this.view2.setVisibility(0);
                            ImageSettingActivity.this.sound_setting.setProgress(SharePreferenceManager.getInstance().getAOVolumeSize(ImageSettingActivity.this.iotId));
                        }
                    }
                });
            }
        });
        ChannelManager.getInstance().registerListener(this.iMobileMsgListener);
        this.rotationOrientate = SharePreferenceManager.getInstance().getImageFlip(this.iotId);
        if (this.rotationOrientate == 0) {
            this.alertSwitch = false;
        } else {
            this.alertSwitch = true;
        }
        this.wakeUpData = SharePreferenceManager.getInstance().getWakeUpData(this.iotId);
        this.swLowPowerMode.setChecked(SharePreferenceManager.getInstance().getLowPowerSwitch(this.iotId) == 1);
        this.swLowPowerMode.setOnClickListener(new View.OnClickListener() { // from class: activity.ImageSettingActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (ImageSettingActivity.this.swLowPowerMode.isChecked()) {
                    ImageSettingActivity.this.updateDev(1);
                } else {
                    ImageSettingActivity.this.updateDev(0);
                }
            }
        });
        this.swAlert.setChecked(this.alertSwitch);
        this.swAlert.setOnClickListener(new View.OnClickListener() { // from class: activity.ImageSettingActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ImageSettingActivity.this.alertSwitch = !r3.alertSwitch;
                HashMap map = new HashMap();
                map.put(Constants.IMAGE_FLIP_STATE_MODEL_NAME, Integer.valueOf(ImageSettingActivity.this.rotationOrientate ^ 1));
                IPCManager.getInstance().getDevice(ImageSettingActivity.this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.ImageSettingActivity.8.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, Object obj) {
                        if (!z || obj == null || "".equals(String.valueOf(obj))) {
                            return;
                        }
                        JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                        if (object.containsKey("code") && object.getInteger("code").intValue() == 200) {
                            ImageSettingActivity.this.rotationOrientate ^= 1;
                            SharePreferenceManager.getInstance().setImageFlip(ImageSettingActivity.this.iotId, ImageSettingActivity.this.rotationOrientate);
                        }
                    }
                });
            }
        });
        this.osd_name.setRightText(SharePreferenceManager.getInstance().getCustom_IPCOSD_Name(this.iotId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTwo() {
        final EditText editText = new EditText(this);
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setIcon(R.drawable.app_launcher).setTitle(R.string.app_name).setView(editText).setPositiveButton(R.string.change_name, new DialogInterface.OnClickListener() { // from class: activity.ImageSettingActivity.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ImageSettingActivity.this.setName(editText.getText().toString());
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: activity.ImageSettingActivity.11
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();
        alertDialogCreate.show();
        alertDialogCreate.getButton(-1).setTextColor(Color.parseColor("#2c99fd"));
        alertDialogCreate.getButton(-2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
        Button button = alertDialogCreate.getButton(-1);
        Button button2 = alertDialogCreate.getButton(-2);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.weight = 10.0f;
        button.setLayoutParams(layoutParams);
        button2.setLayoutParams(layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setName(final String str) {
        HashMap map = new HashMap();
        map.put(Constants.Custom_IPCOSD_Name, str);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.ImageSettingActivity.13
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                Log.d(ImageSettingActivity.this.TAG, "onComplete: ------" + z + "    " + obj.toString());
                if (z) {
                    ImageSettingActivity.this.osd_name.setRightText(str);
                    if (obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        object.getInteger("code").intValue();
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAOVolumeSize(int i) {
        HashMap map = new HashMap();
        map.put(Constants.AOVolumeSize, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.ImageSettingActivity.14
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFakeDual(final Object obj) {
        HashMap map = new HashMap();
        map.put(Constants.FakeDualEnable, Integer.valueOf(Integer.parseInt(obj.toString())));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.ImageSettingActivity.15
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj2) {
                if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        ImageSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.ImageSettingActivity.15.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(ImageSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    } else {
                        SharePreferenceManager.getInstance().setFakeDualEnable(ImageSettingActivity.this.iotId, Integer.parseInt(obj.toString()));
                        ImageSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.ImageSettingActivity.15.2
                            @Override // java.lang.Runnable
                            public void run() {
                                ImageSettingActivity.this.liveModel.setRightText(ImageSettingActivity.this.liveModelList[SharePreferenceManager.getInstance().getFakeDualEnable(ImageSettingActivity.this.iotId)]);
                                Toast.makeText(ImageSettingActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDev(final int i) {
        HashMap map = new HashMap();
        map.put(Constants.LowPowerSwitch, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.ImageSettingActivity.16
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        ImageSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.ImageSettingActivity.16.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(ImageSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    } else {
                        SharePreferenceManager.getInstance().setLowPowerSwitch(ImageSettingActivity.this.iotId, i);
                        ImageSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.ImageSettingActivity.16.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(ImageSettingActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }
}
