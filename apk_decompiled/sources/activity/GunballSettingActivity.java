package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityGunballSettingBinding;
import config.Constants;
import java.util.HashMap;
import sdk.ChannelManager;
import sdk.IPCManager;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.SelectorDialogFragment;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class GunballSettingActivity extends CommonActivity {
    private boolean alertLightSwitch;
    private ActivityGunballSettingBinding binding;
    private SelectorDialogFragment faceDetectDialogFragment;
    private SelectorDialogFragment faceDialogFragment;
    private boolean gunballSwitch;
    private boolean gunballTrackSwitch;
    private SelectorDialogFragment infrarredDialog;
    private String[] infrarredMode;
    private String[] infrarredModeValue;
    private String iotId;
    private String[] motionDetectSensitivity;
    private String[] motionDetectSensitivityValue;
    private int nvrOwner;
    private String[] recordModeArr;
    private String[] recordModeArrDesc;
    private String[] recordModeArrValue;
    private SelectorDialogFragment recordModeDialogFragment;
    private String[] recordQualityArr;
    private String[] recordQualityArrDesc;
    private String[] recordQualityArrValue;
    private SelectorDialogFragment recordQualityFragment;
    SwitchCompat swAlert;
    SwitchCompat swTrack;
    private String wakeUpData;
    private Handler uiHandler = new Handler();
    private SharePreferenceManager.OnCallSetListener mSPModifyListener = new SharePreferenceManager.OnCallSetListener() { // from class: activity.GunballSettingActivity.21
        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(final String str, final String str2) {
            GunballSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.GunballSettingActivity.21.1
                @Override // java.lang.Runnable
                public void run() {
                    String str3 = str2;
                    if (str3 == null || str3.trim().equals("")) {
                        return;
                    }
                    if (str2.equals(GunballSettingActivity.this.getString(R.string.status_light_switch_key))) {
                        GunballSettingActivity.this.alertLightSwitch = SharePreferenceManager.getInstance().getStatusLightSwitch(str);
                        return;
                    }
                    if (str2.equals(GunballSettingActivity.this.getString(R.string.record_quality))) {
                        GunballSettingActivity.this.binding.osdRecordVideoQuality.setRightText(GunballSettingActivity.this.recordQualityArr[SharePreferenceManager.getInstance().getRecordQuality(str)]);
                        return;
                    }
                    if (str2.equals(GunballSettingActivity.this.getString(R.string.storage_record_mode_key))) {
                        GunballSettingActivity.this.binding.osdRecordMode.setRightText(GunballSettingActivity.this.recordModeArr[SharePreferenceManager.getInstance().getStorageRecordMode(str) - 1]);
                    } else if (str2.equals(GunballSettingActivity.this.getString(R.string.PTZLinkageSwitch))) {
                        GunballSettingActivity.this.gunballTrackSwitch = SharePreferenceManager.getInstance().getPTZLinkageSwitch(str) != 0;
                        GunballSettingActivity.this.swTrack.setChecked(GunballSettingActivity.this.gunballTrackSwitch);
                    }
                }
            });
        }
    };
    private ChannelManager.IMobileMsgListener iMobileMsgListener = new ChannelManager.IMobileMsgListener() { // from class: activity.GunballSettingActivity.22
        @Override // sdk.ChannelManager.IMobileMsgListener
        public void onCommand(String str, String str2) {
            Log.e(GunballSettingActivity.this.TAG, "ChannelManager.IMobileMsgListener    topic:" + str + "     msg:" + str2);
            if (str.equals("/thing/properties")) {
                SettingsCtrl.getInstance().getProperties(GunballSettingActivity.this.iotId, new MyCallback() { // from class: activity.GunballSettingActivity.22.1
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
            }
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_gunball_setting;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityGunballSettingBinding) DataBindingUtil.setContentView(this, R.layout.activity_gunball_setting);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.GunballSettingActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                GunballSettingActivity.this.finish();
            }
        });
        this.binding.itLinkage.addRightView(new SwitchCompat(this));
        this.swAlert = (SwitchCompat) this.binding.itLinkage.getRightView();
        this.swAlert.setTextOff("");
        this.swAlert.setTextOn("");
        this.swAlert.setText("");
        this.swAlert.setThumbDrawable(null);
        this.swAlert.setBackgroundResource(R.drawable.sel_switch);
        this.binding.itLinkageTrack.addRightView(new SwitchCompat(this));
        this.swTrack = (SwitchCompat) this.binding.itLinkageTrack.getRightView();
        this.swTrack.setTextOff("");
        this.swTrack.setTextOn("");
        this.swTrack.setText("");
        this.swTrack.setThumbDrawable(null);
        this.swTrack.setBackgroundResource(R.drawable.sel_switch);
        this.motionDetectSensitivity = getResources().getStringArray(R.array.MotionDetectSensitivity);
        this.motionDetectSensitivityValue = getResources().getStringArray(R.array.MotionDetectSensitivityValue);
        this.faceDialogFragment = new SelectorDialogFragment(getString(R.string.face_detect), this.motionDetectSensitivity);
        this.faceDialogFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.GunballSettingActivity.2
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                GunballSettingActivity gunballSettingActivity = GunballSettingActivity.this;
                gunballSettingActivity.updateDevParam(gunballSettingActivity.getString(R.string.face_detect_key), GunballSettingActivity.this.motionDetectSensitivityValue[i]);
            }
        });
        this.faceDetectDialogFragment = new SelectorDialogFragment(getString(R.string.motion_detect_sensitivity_title), this.motionDetectSensitivity);
        this.faceDetectDialogFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.GunballSettingActivity.3
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                GunballSettingActivity gunballSettingActivity = GunballSettingActivity.this;
                gunballSettingActivity.updateDevParam(gunballSettingActivity.getString(R.string.motion_detect_sensitivity_key), GunballSettingActivity.this.motionDetectSensitivityValue[i]);
            }
        });
        this.infrarredMode = getResources().getStringArray(R.array.InfrarredMode);
        this.infrarredModeValue = getResources().getStringArray(R.array.InfrarredModeValue);
        this.infrarredDialog = new SelectorDialogFragment(getString(R.string.night_mode), this.infrarredMode);
        this.infrarredDialog.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.GunballSettingActivity.4
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                GunballSettingActivity gunballSettingActivity = GunballSettingActivity.this;
                gunballSettingActivity.updateDevParam(gunballSettingActivity.getString(R.string.day_night_mode_key), GunballSettingActivity.this.infrarredModeValue[i]);
            }
        });
        this.recordQualityArr = new String[]{getResources().getString(R.string.quality_m), getResources().getString(R.string.quality_h)};
        this.recordQualityArrValue = new String[]{"0", "1"};
        this.recordQualityFragment = new SelectorDialogFragment(getString(R.string.record_video_quality), this.recordQualityArr);
        this.recordQualityFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.GunballSettingActivity.5
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                GunballSettingActivity gunballSettingActivity = GunballSettingActivity.this;
                gunballSettingActivity.updateDevParam(gunballSettingActivity.getString(R.string.record_quality), GunballSettingActivity.this.recordQualityArrValue[i]);
            }
        });
        this.recordModeArr = new String[]{getResources().getString(R.string.alarm_record), getResources().getString(R.string.day_record)};
        this.recordModeArrValue = new String[]{"1", "2"};
        this.recordModeArrDesc = new String[]{getResources().getString(R.string.record_desc1), getResources().getString(R.string.record_desc)};
        this.recordModeDialogFragment = new SelectorDialogFragment(getString(R.string.record_mode), this.recordModeArr);
        this.recordModeDialogFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.GunballSettingActivity.6
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                GunballSettingActivity gunballSettingActivity = GunballSettingActivity.this;
                gunballSettingActivity.updateDevParam(gunballSettingActivity.getString(R.string.storage_record_mode_key), GunballSettingActivity.this.recordModeArrValue[i]);
            }
        });
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        this.iotId = intent.getStringExtra("iotId");
        this.nvrOwner = intent.getIntExtra("nvrOwner", 0);
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        this.binding.setNvrOwner(this.nvrOwner);
        SharePreferenceManager.getInstance().registerOnCallSetListener(this.mSPModifyListener);
        if (this.nvrOwner == 1) {
            this.binding.itLinkageTrack.setVisibility(8);
        } else {
            this.binding.itLinkageTrack.setVisibility(8);
        }
        if (SharePreferenceManager.getInstance().getSupportMotionDetect(this.iotId) == 1) {
            this.binding.itemMotionDetection.setRightText(this.motionDetectSensitivity[SharePreferenceManager.getInstance().getMotionDetectSensitivity(this.iotId)]);
            this.binding.itemMotionDetection.setVisibility(0);
            this.binding.osdFace.setVisibility(8);
            this.binding.itLinkageTrack.setVisibility(8);
        }
        if (SharePreferenceManager.getInstance().getDayNightMode(this.iotId) < this.infrarredMode.length) {
            this.binding.osdNightMode.setRightText(this.infrarredMode[SharePreferenceManager.getInstance().getDayNightMode(this.iotId)]);
        }
        if (-1 != SharePreferenceManager.getInstance().getFaceDetectMode(this.iotId) && SharePreferenceManager.getInstance().getFaceDetectMode(this.iotId) < this.motionDetectSensitivity.length) {
            this.binding.osdFace.setRightText(this.motionDetectSensitivity[SharePreferenceManager.getInstance().getFaceDetectMode(this.iotId)]);
        }
        this.binding.osdRecordVideoQuality.setRightText(this.recordQualityArr[SharePreferenceManager.getInstance().getRecordQuality(this.iotId)]);
        this.binding.osdRecordMode.setRightText(this.recordModeArr[SharePreferenceManager.getInstance().getStorageRecordMode(this.iotId) - 1]);
        this.gunballSwitch = SharePreferenceManager.getInstance().getPTZLinkageTrackSwitch(this.iotId) != 0;
        this.gunballTrackSwitch = SharePreferenceManager.getInstance().getPTZLinkageSwitch(this.iotId) != 0;
        this.swTrack.setChecked(this.gunballTrackSwitch);
        this.binding.itLinkage.setLineShow(this.binding.itLinkage.getVisibility() == 0 && this.binding.itLinkageTrack.getVisibility() == 0);
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.GunballSettingActivity.7
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                GunballSettingActivity.this.runOnUiThread(new Runnable() { // from class: activity.GunballSettingActivity.7.1
                    @Override // java.lang.Runnable
                    public void run() {
                        GunballSettingActivity.this.swAlert.setChecked(GunballSettingActivity.this.gunballSwitch);
                    }
                });
            }
        });
        ChannelManager.getInstance().registerListener(this.iMobileMsgListener);
        this.wakeUpData = SharePreferenceManager.getInstance().getWakeUpData(this.iotId);
        this.swAlert.setOnClickListener(new View.OnClickListener() { // from class: activity.GunballSettingActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                GunballSettingActivity.this.gunballSwitch = !r3.gunballSwitch;
                SharePreferenceManager.getInstance().setPTZLinkageTrackSwitch(GunballSettingActivity.this.iotId, GunballSettingActivity.this.gunballSwitch ? 1 : 0);
            }
        });
        this.swTrack.setOnClickListener(new View.OnClickListener() { // from class: activity.GunballSettingActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                GunballSettingActivity.this.gunballTrackSwitch = !r3.gunballTrackSwitch;
                GunballSettingActivity.this.swTrack.setChecked(GunballSettingActivity.this.gunballTrackSwitch);
                GunballSettingActivity gunballSettingActivity = GunballSettingActivity.this;
                gunballSettingActivity.updateDevParam(gunballSettingActivity.getString(R.string.PTZLinkageSwitch), Integer.valueOf(GunballSettingActivity.this.gunballTrackSwitch ? 1 : 0));
                SharePreferenceManager.getInstance().setPTZLinkageTrackSwitch(GunballSettingActivity.this.iotId, GunballSettingActivity.this.gunballTrackSwitch ? 1 : 0);
            }
        });
        this.binding.osdFace.setOnClickListener(new View.OnClickListener() { // from class: activity.GunballSettingActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                GunballSettingActivity.this.faceDialogFragment.showAllowingStateLoss(GunballSettingActivity.this.getSupportFragmentManager(), "", SharePreferenceManager.getInstance().getFaceDetectMode(GunballSettingActivity.this.iotId));
            }
        });
        this.binding.itemMotionDetection.setOnClickListener(new View.OnClickListener() { // from class: activity.GunballSettingActivity.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                GunballSettingActivity.this.faceDetectDialogFragment.showAllowingStateLoss(GunballSettingActivity.this.getSupportFragmentManager(), "", SharePreferenceManager.getInstance().getMotionDetectSensitivity(GunballSettingActivity.this.iotId));
            }
        });
        this.binding.osdNightMode.setOnClickListener(new View.OnClickListener() { // from class: activity.GunballSettingActivity.12
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                GunballSettingActivity.this.infrarredDialog.showAllowingStateLoss(GunballSettingActivity.this.getSupportFragmentManager(), GunballSettingActivity.this.TAG, SharePreferenceManager.getInstance().getDayNightMode(GunballSettingActivity.this.iotId));
            }
        });
        this.binding.osdRecordVideoQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.GunballSettingActivity.13
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                GunballSettingActivity.this.recordQualityFragment.showAllowingStateLoss(GunballSettingActivity.this.getSupportFragmentManager(), "", SharePreferenceManager.getInstance().getRecordQuality(GunballSettingActivity.this.iotId));
            }
        });
        this.binding.osdRecordMode.setOnClickListener(new View.OnClickListener() { // from class: activity.GunballSettingActivity.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                GunballSettingActivity.this.recordModeDialogFragment.showAllowingStateLoss(GunballSettingActivity.this.getSupportFragmentManager(), "", SharePreferenceManager.getInstance().getStorageRecordMode(GunballSettingActivity.this.iotId) - 1);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDevParam(String str, final Object obj) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.face_detect_key))) {
            map.put(Constants.FACE_DETECT_SENSITIVITY, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.GunballSettingActivity.15
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            GunballSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.GunballSettingActivity.15.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(GunballSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setFaceDetectMode(GunballSettingActivity.this.iotId, Integer.parseInt(obj.toString()));
                            GunballSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.GunballSettingActivity.15.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    GunballSettingActivity.this.binding.osdFace.setRightText(GunballSettingActivity.this.motionDetectSensitivity[SharePreferenceManager.getInstance().getFaceDetectMode(GunballSettingActivity.this.iotId)]);
                                    Toast.makeText(GunballSettingActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                    if (SharePreferenceManager.getInstance().getFaceDetectMode(GunballSettingActivity.this.iotId) != 0) {
                                        GunballSettingActivity.this.binding.itLinkage.setLineShow(true);
                                        GunballSettingActivity.this.binding.itLinkageTrack.setVisibility(8);
                                    } else {
                                        GunballSettingActivity.this.gunballTrackSwitch = false;
                                        GunballSettingActivity.this.updateDevParam(GunballSettingActivity.this.getString(R.string.PTZLinkageSwitch), Integer.valueOf(GunballSettingActivity.this.gunballTrackSwitch ? 1 : 0));
                                        GunballSettingActivity.this.binding.itLinkage.setLineShow(false);
                                        GunballSettingActivity.this.binding.itLinkageTrack.setVisibility(8);
                                    }
                                }
                            });
                        }
                    }
                }
            });
        } else if (str.equals(getString(R.string.PTZLinkageSwitch))) {
            map.put(Constants.PTZLinkageSwitch, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.GunballSettingActivity.16
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            GunballSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.GunballSettingActivity.16.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(GunballSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            GunballSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.GunballSettingActivity.16.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(GunballSettingActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                }
            });
        } else if (str.equals(getString(R.string.day_night_mode_key))) {
            map.put(Constants.DAY_NIGHT_MODE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.GunballSettingActivity.17
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            GunballSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.GunballSettingActivity.17.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(GunballSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setDayNightMode(GunballSettingActivity.this.iotId, Integer.parseInt(obj.toString()));
                            GunballSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.GunballSettingActivity.17.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(GunballSettingActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                }
            });
        } else if (str.equals(getString(R.string.record_quality))) {
            map.put(Constants.StorageRecordQuality, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.GunballSettingActivity.18
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            GunballSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.GunballSettingActivity.18.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(GunballSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setRecordQuality(GunballSettingActivity.this.iotId, Integer.parseInt(obj.toString()));
                            GunballSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.GunballSettingActivity.18.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(GunballSettingActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                }
            });
        } else if (str.equals(getString(R.string.storage_record_mode_key))) {
            map.put(Constants.STORAGE_RECORD_MODE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.GunballSettingActivity.19
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            GunballSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.GunballSettingActivity.19.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(GunballSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setStorageRecordMode(GunballSettingActivity.this.iotId, Integer.parseInt(obj.toString()));
                            GunballSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.GunballSettingActivity.19.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(GunballSettingActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                }
            });
        }
        if (str.equals(getString(R.string.motion_detect_sensitivity_key))) {
            map.put(Constants.MOTION_DETECT_SENSITIVITY_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.GunballSettingActivity.20
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            GunballSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.GunballSettingActivity.20.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(GunballSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            GunballSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.GunballSettingActivity.20.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    SharePreferenceManager.getInstance().setMotionDetectSensitivity(GunballSettingActivity.this.iotId, Integer.parseInt(obj.toString()));
                                    GunballSettingActivity.this.binding.itemMotionDetection.setRightText(GunballSettingActivity.this.motionDetectSensitivity[SharePreferenceManager.getInstance().getMotionDetectSensitivity(GunballSettingActivity.this.iotId)]);
                                }
                            });
                        }
                    }
                }
            });
        }
    }
}
