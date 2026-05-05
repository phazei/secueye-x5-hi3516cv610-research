package fragment;

import activity.EasyPlanSettingsActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.seculink.app.R;
import config.Constants;
import java.util.HashMap;
import sdk.ChannelManager;
import sdk.IPCManager;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;

/* JADX INFO: loaded from: classes4.dex */
public class SettingsPreferenceFragment extends PreferenceFragment {
    private ListPreference alarmFrequencyLevel;
    private Preference alarmNotifyPlan;
    private SwitchPreference alarmSwitch;
    private ListPreference dayNightMode;
    private SwitchPreference encryptSwitch;
    private Preference eventRecordTimeSettings;
    private SwitchPreference iFrameSwitch;
    private ListPreference imageFlip;
    private Activity mActivity;
    private SwitchPreference micSwitch;
    private ListPreference motionDetectSensitivity;
    private SwitchPreference speakerSwitch;
    private SwitchPreference statusLightSwitch;
    private Preference storageFormat;
    private ListPreference storageRecordMode;
    private Preference storageRemainingCapacity;
    private PreferenceScreen storageScreen;
    private Preference storageStatus;
    private Preference storageTotalCapacity;
    private ListPreference streamVideoQuality;
    private ListPreference subStreamVideoQuality;
    private Handler uiHandler;
    private ListPreference voiceDetectSensitivity;
    private String TAG = getClass().getSimpleName();
    private String iotId = "";
    private SharePreferenceManager.OnCallSetListener mOnCallListener = new SharePreferenceManager.OnCallSetListener() { // from class: fragment.SettingsPreferenceFragment.1
        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(String str, final String str2) {
            SettingsPreferenceFragment.this.uiHandler.post(new Runnable() { // from class: fragment.SettingsPreferenceFragment.1.1
                @Override // java.lang.Runnable
                public void run() {
                    SettingsPreferenceFragment.this.refreshUI(str2);
                }
            });
        }
    };
    private ChannelManager.IMobileMsgListener iMobileMsgListener = new ChannelManager.IMobileMsgListener() { // from class: fragment.SettingsPreferenceFragment.3
        @Override // sdk.ChannelManager.IMobileMsgListener
        public void onCommand(String str, String str2) {
            Log.e(SettingsPreferenceFragment.this.TAG, "ChannelManager.IMobileMsgListener    topic:" + str + "     msg:" + str2);
            if (str.equals("/thing/properties")) {
                SettingsCtrl.getInstance().getProperties(SettingsPreferenceFragment.this.iotId, new MyCallback() { // from class: fragment.SettingsPreferenceFragment.3.1
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
            }
        }
    };
    private Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() { // from class: fragment.SettingsPreferenceFragment.7
        @Override // android.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object obj) {
            HashMap map = new HashMap();
            boolean z = true;
            if (preference.getKey().equals(SettingsPreferenceFragment.this.getString(R.string.mic_switch_key))) {
                map.put(Constants.MIC_SWITCH_MODEL_NAME, Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0));
            } else if (preference.getKey().equals(SettingsPreferenceFragment.this.getString(R.string.speaker_switch_key))) {
                map.put(Constants.SPEAKER_SWITCH_MODEL_NAME, Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0));
            } else if (preference.getKey().equals(SettingsPreferenceFragment.this.getString(R.string.status_light_switch_key))) {
                map.put(Constants.STATUS_LIGHT_SWITCH_MODEL_NAME, Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0));
            } else if (preference.getKey().equals(SettingsPreferenceFragment.this.getString(R.string.day_night_mode_key))) {
                map.put(Constants.DAY_NIGHT_MODE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            } else if (preference.getKey().equals(SettingsPreferenceFragment.this.getString(R.string.stream_video_quality_key))) {
                map.put(Constants.STREAM_VIDEO_QUALITY_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            } else if (preference.getKey().equals(SettingsPreferenceFragment.this.getString(R.string.subStream_video_quality_key))) {
                map.put(Constants.SUBSTREAM_VIDEO_QUALITY_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            } else if (preference.getKey().equals(SettingsPreferenceFragment.this.getString(R.string.image_flip_status_key))) {
                map.put(Constants.IMAGE_FLIP_STATE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            } else if (preference.getKey().equals(SettingsPreferenceFragment.this.getString(R.string.encrypt_switch_key))) {
                map.put(Constants.ENCRYPT_SWITCH_MODEL_NAME, Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0));
            } else if (preference.getKey().equals(SettingsPreferenceFragment.this.getString(R.string.alarm_switch_key))) {
                map.put(Constants.ALARM_SWITCH_MODEL_NAME, Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0));
            } else if (preference.getKey().equals(SettingsPreferenceFragment.this.getString(R.string.motion_detect_sensitivity_key))) {
                map.put(Constants.MOTION_DETECT_SENSITIVITY_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            } else if (preference.getKey().equals(SettingsPreferenceFragment.this.getString(R.string.voice_detect_sensitivity_key))) {
                map.put(Constants.VOICE_DETECT_SENSITIVITY_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            } else if (preference.getKey().equals(SettingsPreferenceFragment.this.getString(R.string.alarm_frequency_level_key))) {
                map.put(Constants.ALARM_FREQUENCY_LEVEL_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            } else if (preference.getKey().equals(SettingsPreferenceFragment.this.getString(R.string.storage_record_mode_key))) {
                map.put(Constants.STORAGE_RECORD_MODE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            } else {
                z = false;
            }
            if (z) {
                SettingsCtrl.getInstance().updateSettings(SettingsPreferenceFragment.this.iotId, map, null);
            }
            return false;
        }
    };

    public void setIotId(String str) {
        this.iotId = str;
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mActivity = getActivity();
        addPreferencesFromResource(R.xml.settings_preferences);
        this.uiHandler = new Handler(Looper.getMainLooper());
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onViewCreated(View view2, @Nullable Bundle bundle) {
        super.onViewCreated(view2, bundle);
        initView();
        holdAllChange();
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        SharePreferenceManager.getInstance().registerOnCallSetListener(this.mOnCallListener);
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: fragment.SettingsPreferenceFragment.2
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
        ChannelManager.getInstance().registerListener(this.iMobileMsgListener);
    }

    @Override // android.app.Fragment
    public void onPause() {
        super.onPause();
        SharePreferenceManager.getInstance().unRegisterOnCallSetListener(this.mOnCallListener);
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onStop() {
        super.onStop();
        ChannelManager.getInstance().unRegisterListener(this.iMobileMsgListener);
    }

    private void initView() {
        this.micSwitch = (SwitchPreference) findPreference(getString(R.string.mic_switch_key));
        this.speakerSwitch = (SwitchPreference) findPreference(getString(R.string.speaker_switch_key));
        this.statusLightSwitch = (SwitchPreference) findPreference(getString(R.string.status_light_switch_key));
        this.dayNightMode = (ListPreference) findPreference(getString(R.string.day_night_mode_key));
        this.streamVideoQuality = (ListPreference) findPreference(getString(R.string.stream_video_quality_key));
        this.subStreamVideoQuality = (ListPreference) findPreference(getString(R.string.subStream_video_quality_key));
        this.imageFlip = (ListPreference) findPreference(getString(R.string.image_flip_status_key));
        this.encryptSwitch = (SwitchPreference) findPreference(getString(R.string.encrypt_switch_key));
        this.iFrameSwitch = (SwitchPreference) findPreference(getString(R.string.force_iframe_key));
        this.alarmSwitch = (SwitchPreference) findPreference(getString(R.string.alarm_switch_key));
        this.motionDetectSensitivity = (ListPreference) findPreference(getString(R.string.motion_detect_sensitivity_key));
        this.voiceDetectSensitivity = (ListPreference) findPreference(getString(R.string.voice_detect_sensitivity_key));
        this.alarmFrequencyLevel = (ListPreference) findPreference(getString(R.string.alarm_frequency_level_key));
        this.storageStatus = findPreference(getString(R.string.storage_status_key));
        this.storageTotalCapacity = findPreference(getString(R.string.storage_total_capacity_key));
        this.storageRemainingCapacity = findPreference(getString(R.string.storage_remain_capacity_key));
        this.storageRecordMode = (ListPreference) findPreference(getString(R.string.storage_record_mode_key));
        this.eventRecordTimeSettings = findPreference(getString(R.string.event_record_time_settings_key));
        this.eventRecordTimeSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: fragment.SettingsPreferenceFragment.4
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(SettingsPreferenceFragment.this.mActivity, (Class<?>) EasyPlanSettingsActivity.class);
                intent.putExtra("iotId", SettingsPreferenceFragment.this.iotId);
                SettingsPreferenceFragment.this.startActivity(intent);
                return true;
            }
        });
        this.alarmNotifyPlan = findPreference(getString(R.string.alarm_notify_settings_key));
        this.alarmNotifyPlan.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: fragment.SettingsPreferenceFragment.5
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                SettingsPreferenceFragment.this.uiHandler.post(new Runnable() { // from class: fragment.SettingsPreferenceFragment.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(SettingsPreferenceFragment.this.getActivity(), "暂未开放此功能", 0).show();
                    }
                });
                return true;
            }
        });
        this.storageScreen = (PreferenceScreen) findPreference("card");
        showStorageStatus();
        this.storageTotalCapacity.setSummary(SharePreferenceManager.getInstance().getStorageTotalCapacity(this.iotId) + " MB");
        this.storageRemainingCapacity.setSummary(SharePreferenceManager.getInstance().getStorageRemainingCapacity(this.iotId) + " MB");
        this.storageFormat = findPreference(getString(R.string.storage_format_key));
        this.storageFormat.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: fragment.SettingsPreferenceFragment.6
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference preference) {
                IPCManager.getInstance().getDevice(SettingsPreferenceFragment.this.iotId).formatStorageMedium(new IPanelCallback() { // from class: fragment.SettingsPreferenceFragment.6.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, Object obj) {
                        String str = SettingsPreferenceFragment.this.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("formatStorageMedium:");
                        sb.append(z);
                        sb.append("       o:");
                        sb.append(obj != null ? String.valueOf(obj) : TmpConstant.GROUP_ROLE_UNKNOWN);
                        Log.d(str, sb.toString());
                    }
                });
                return true;
            }
        });
    }

    private void showStorageStatus() {
        String str = "正常";
        switch (SharePreferenceManager.getInstance().getStorageStatus(this.iotId)) {
            case 0:
                str = "未插卡";
                this.storageScreen.setEnabled(false);
                break;
            case 1:
                str = "正常";
                this.storageScreen.setEnabled(true);
                break;
            case 2:
                str = "未格式化";
                this.storageScreen.setEnabled(true);
                break;
            case 3:
                str = "正在格式化";
                this.storageScreen.setEnabled(true);
                break;
        }
        this.storageStatus.setSummary(str);
        this.storageScreen.setSummary(str);
    }

    private void holdAllChange() {
        this.micSwitch.setOnPreferenceChangeListener(this.listener);
        this.speakerSwitch.setOnPreferenceChangeListener(this.listener);
        this.statusLightSwitch.setOnPreferenceChangeListener(this.listener);
        this.dayNightMode.setOnPreferenceChangeListener(this.listener);
        this.streamVideoQuality.setOnPreferenceChangeListener(this.listener);
        this.subStreamVideoQuality.setOnPreferenceChangeListener(this.listener);
        this.imageFlip.setOnPreferenceChangeListener(this.listener);
        this.encryptSwitch.setOnPreferenceChangeListener(this.listener);
        this.alarmSwitch.setOnPreferenceChangeListener(this.listener);
        this.motionDetectSensitivity.setOnPreferenceChangeListener(this.listener);
        this.voiceDetectSensitivity.setOnPreferenceChangeListener(this.listener);
        this.alarmFrequencyLevel.setOnPreferenceChangeListener(this.listener);
        this.storageRecordMode.setOnPreferenceChangeListener(this.listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshUI(String str) {
        if (str == null || str.trim().equals("")) {
            return;
        }
        if (str.equals(getString(R.string.mic_switch_key))) {
            this.micSwitch.setChecked(SharePreferenceManager.getInstance().getMicSwitch(this.iotId));
            return;
        }
        if (str.equals(getString(R.string.speaker_switch_key))) {
            this.speakerSwitch.setChecked(SharePreferenceManager.getInstance().getSpeakerSwitch(this.iotId));
            return;
        }
        if (str.equals(getString(R.string.status_light_switch_key))) {
            this.statusLightSwitch.setChecked(SharePreferenceManager.getInstance().getStatusLightSwitch(this.iotId));
            return;
        }
        if (str.equals(getString(R.string.day_night_mode_key))) {
            this.dayNightMode.setValueIndex(SharePreferenceManager.getInstance().getDayNightMode(this.iotId));
            return;
        }
        if (str.equals(getString(R.string.stream_video_quality_key))) {
            this.streamVideoQuality.setValueIndex(SharePreferenceManager.getInstance().getStreamVideoQuality(this.iotId));
            return;
        }
        if (str.equals(getString(R.string.subStream_video_quality_key))) {
            this.subStreamVideoQuality.setValueIndex(SharePreferenceManager.getInstance().getSubStreamVideoQuality(this.iotId));
            return;
        }
        if (str.equals(getString(R.string.image_flip_status_key))) {
            this.imageFlip.setValueIndex(SharePreferenceManager.getInstance().getImageFlip(this.iotId));
            return;
        }
        if (str.equals(getString(R.string.encrypt_switch_key))) {
            this.encryptSwitch.setChecked(SharePreferenceManager.getInstance().getEncryptSwitch(this.iotId));
            return;
        }
        if (str.equals(getString(R.string.motion_detect_sensitivity_key))) {
            this.motionDetectSensitivity.setValueIndex(SharePreferenceManager.getInstance().getMotionDetectSensitivity(this.iotId));
            return;
        }
        if (str.equals(getString(R.string.voice_detect_sensitivity_key))) {
            this.voiceDetectSensitivity.setValueIndex(SharePreferenceManager.getInstance().getVoiceDetectSensitivity(this.iotId));
            return;
        }
        if (str.equals(getString(R.string.alarm_frequency_level_key))) {
            this.alarmFrequencyLevel.setValueIndex(SharePreferenceManager.getInstance().getAlarmFrequencyLevel(this.iotId));
            return;
        }
        if (str.equals(getString(R.string.storage_status_key))) {
            showStorageStatus();
            return;
        }
        if (str.equals(getString(R.string.storage_total_capacity_key))) {
            this.storageTotalCapacity.setSummary(SharePreferenceManager.getInstance().getStorageTotalCapacity(this.iotId) + " MB");
            return;
        }
        if (str.equals(getString(R.string.storage_remain_capacity_key))) {
            this.storageRemainingCapacity.setSummary(SharePreferenceManager.getInstance().getStorageRemainingCapacity(this.iotId) + " MB");
            return;
        }
        if (str.equals(getString(R.string.storage_record_mode_key))) {
            this.storageRecordMode.setValueIndex(SharePreferenceManager.getInstance().getStorageRecordMode(this.iotId));
        }
    }
}
