package tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.huawei.hms.framework.common.ContainerUtils;
import com.seculink.app.R;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes4.dex */
public class SharePreferenceManager {
    private Context context;
    private SharedPreferences.Editor editor;
    private List<SharedPreferences.OnSharedPreferenceChangeListener> listenerList;
    List<OnCallSetListener> setListenerList;
    private SharedPreferences settings;

    public interface OnCallSetListener {
        void onCallSet(String str, String str2);
    }

    public void clear() {
    }

    private SharePreferenceManager() {
        this.listenerList = new LinkedList();
        this.setListenerList = new LinkedList();
    }

    private static class SharePreferenceManagerHolder {
        private static final SharePreferenceManager manager = new SharePreferenceManager();

        private SharePreferenceManagerHolder() {
        }
    }

    public static SharePreferenceManager getInstance() {
        return SharePreferenceManagerHolder.manager;
    }

    public void init(Context context) {
        this.settings = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = this.settings.edit();
        this.context = context;
    }

    public void registerListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        if (this.listenerList.contains(onSharedPreferenceChangeListener)) {
            return;
        }
        this.listenerList.add(onSharedPreferenceChangeListener);
        this.settings.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public void unRegisterListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        if (this.listenerList.contains(onSharedPreferenceChangeListener)) {
            this.listenerList.remove(onSharedPreferenceChangeListener);
            this.settings.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        }
    }

    public void registerOnCallSetListener(OnCallSetListener onCallSetListener) {
        if (this.setListenerList.contains(onCallSetListener)) {
            return;
        }
        this.setListenerList.add(onCallSetListener);
    }

    public void unRegisterOnCallSetListener(OnCallSetListener onCallSetListener) {
        if (this.setListenerList.contains(onCallSetListener)) {
            this.setListenerList.remove(onCallSetListener);
        }
    }

    private void notifyCalledSet(String str, String str2) {
        List<OnCallSetListener> list = this.setListenerList;
        if (list == null || list.size() <= 0) {
            return;
        }
        Iterator<OnCallSetListener> it = this.setListenerList.iterator();
        while (it.hasNext()) {
            it.next().onCallSet(str, str2);
        }
    }

    public boolean getMicSwitch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return true;
        }
        return sharedPreferences.getBoolean(this.context.getString(R.string.mic_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, true);
    }

    public void setMicSwitch(String str, boolean z) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putBoolean(this.context.getString(R.string.mic_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, z).commit();
        notifyCalledSet(str, this.context.getString(R.string.mic_switch_key));
    }

    public boolean getSpeakerSwitch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return true;
        }
        return sharedPreferences.getBoolean(this.context.getString(R.string.speaker_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, true);
    }

    public void setSpeakerSwitch(String str, boolean z) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putBoolean(this.context.getString(R.string.speaker_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, z).commit();
        notifyCalledSet(str, this.context.getString(R.string.speaker_switch_key));
    }

    public boolean getStatusLightSwitch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return true;
        }
        return sharedPreferences.getBoolean(this.context.getString(R.string.status_light_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, true);
    }

    public void setStatusLightSwitch(String str, boolean z) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putBoolean(this.context.getString(R.string.status_light_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, z).commit();
        notifyCalledSet(str, this.context.getString(R.string.status_light_switch_key));
    }

    public int getDayNightMode(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.day_night_mode_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setDayNightMode(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.day_night_mode_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.day_night_mode_key));
    }

    public int getStreamVideoQuality(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.stream_video_quality_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setStreamVideoQuality(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.stream_video_quality_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.stream_video_quality_key));
    }

    public int getSubStreamVideoQuality(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.subStream_video_quality_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setSubStreamVideoQuality(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.subStream_video_quality_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.subStream_video_quality_key));
    }

    public int getImageFlip(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.image_flip_status_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setImageFlip(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.image_flip_status_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.image_flip_status_key));
    }

    public boolean getEncryptSwitch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return true;
        }
        return sharedPreferences.getBoolean(this.context.getString(R.string.encrypt_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, true);
    }

    public void setEncryptSwitch(String str, boolean z) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putBoolean(this.context.getString(R.string.encrypt_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, z).commit();
        notifyCalledSet(str, this.context.getString(R.string.encrypt_switch_key));
    }

    public boolean getForceIFrameSwitch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return false;
        }
        return sharedPreferences.getBoolean(this.context.getString(R.string.force_iframe_key) + OpenAccountUIConstants.UNDER_LINE + str, false);
    }

    public void setForceIFrameSwitch(String str, boolean z) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putBoolean(this.context.getString(R.string.force_iframe_key) + OpenAccountUIConstants.UNDER_LINE + str, z).commit();
    }

    public int getAlarmSwitch(String str) {
        return this.settings.getInt(this.context.getString(R.string.alarm_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, -1);
    }

    public void setAlarmSwitch(String str, int i) {
        if (this.context == null || "".equals(str)) {
            return;
        }
        this.editor.putInt(this.context.getString(R.string.alarm_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.alarm_switch_key));
    }

    public String getLowPowerMode(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.low_power_mode) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setLowPowerMode(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.low_power_mode) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
    }

    public int getLowPowerStatus(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return -1;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.low_power_status) + OpenAccountUIConstants.UNDER_LINE + str, -1);
    }

    public void setLowPowerStatus(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.low_power_status) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public String getWakeUpData(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.wake_up_data) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setWakeUpData(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.wake_up_data) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
    }

    public int getMotionDetectSensitivity(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.motion_detect_sensitivity_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setMotionDetectSensitivity(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.motion_detect_sensitivity_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.motion_detect_sensitivity_key));
    }

    public int getAlarmMode(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.alarm_mode_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setAlarmMode(String str, int i) {
        if (this.context == null || str == null || "".equals(str) || this.editor == null) {
            return;
        }
        notifyCalledSet(str, this.context.getString(R.string.alarm_mode_key));
        this.editor.putString(this.context.getString(R.string.alarm_mode_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
    }

    public int getFaceDetectMode(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.face_detect_key) + OpenAccountUIConstants.UNDER_LINE + str, "-1")).intValue();
    }

    public void setFaceDetectMode(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.face_detect_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.face_detect_key));
    }

    public int getIntelligentMode(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.intellgent_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setIntelligentMode(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.intellgent_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.intellgent_key));
    }

    public int getSupportMotionDetect(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return -1;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.support_motion_detect_key) + OpenAccountUIConstants.UNDER_LINE + str, "-1")).intValue();
    }

    public void setSupportMotionDetect(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.support_motion_detect_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.support_motion_detect_key));
    }

    public int getVoiceDetectSensitivity(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.voice_detect_sensitivity_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setVoiceDetectSensitivity(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.voice_detect_sensitivity_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.voice_detect_sensitivity_key));
    }

    public int getAlarmFrequencyLevel(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.alarm_frequency_level_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setAlarmFrequencyLevel(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.alarm_frequency_level_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.alarm_frequency_level_key));
    }

    public int getStorageStatus(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.storage_status_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setStorageStatus(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.storage_status_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.storage_status_key));
    }

    public float getStorageTotalCapacity(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0.0f;
        }
        return sharedPreferences.getFloat(this.context.getString(R.string.storage_total_capacity_key) + OpenAccountUIConstants.UNDER_LINE + str, 0.0f);
    }

    public void setStorageTotalCapacity(String str, float f) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putFloat(this.context.getString(R.string.storage_total_capacity_key) + OpenAccountUIConstants.UNDER_LINE + str, f).commit();
        notifyCalledSet(str, this.context.getString(R.string.storage_total_capacity_key));
    }

    public float getStorageRemainingCapacity(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0.0f;
        }
        return sharedPreferences.getFloat(this.context.getString(R.string.storage_remain_capacity_key) + OpenAccountUIConstants.UNDER_LINE + str, 0.0f);
    }

    public void setStorageRemainingCapacity(String str, float f) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putFloat(this.context.getString(R.string.storage_remain_capacity_key) + OpenAccountUIConstants.UNDER_LINE + str, f).commit();
        notifyCalledSet(str, this.context.getString(R.string.storage_remain_capacity_key));
    }

    public int getStorageRecordMode(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 1;
        }
        if (Integer.parseInt(sharedPreferences.getString(this.context.getString(R.string.storage_record_mode_key) + OpenAccountUIConstants.UNDER_LINE + str, "1")) == 0) {
            return 1;
        }
        return Integer.parseInt(this.settings.getString(this.context.getString(R.string.storage_record_mode_key) + OpenAccountUIConstants.UNDER_LINE + str, "1"));
    }

    public void setStorageRecordMode(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.storage_record_mode_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.storage_record_mode_key));
    }

    public String getDeviceName(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.device_name_key) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setDeviceName(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.device_name_key) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.device_name_key));
    }

    public int getDeviceTime(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.device_time_key) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setDeviceTime(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.device_time_key) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.device_time_key));
    }

    public String getDeviceTZ(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.device_tz_key) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setDeviceTZ(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.device_tz_key) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.device_tz_key));
    }

    public boolean getSyncPhoneTime(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return false;
        }
        return sharedPreferences.getBoolean(this.context.getString(R.string.sync_phone_time_key) + OpenAccountUIConstants.UNDER_LINE + str, false);
    }

    public void setSyncPhoneTime(String str, boolean z) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putBoolean(this.context.getString(R.string.sync_phone_time_key) + OpenAccountUIConstants.UNDER_LINE + str, z).commit();
        notifyCalledSet(str, this.context.getString(R.string.sync_phone_time_key));
    }

    public boolean getAlertSwitch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return false;
        }
        return sharedPreferences.getBoolean(this.context.getString(R.string.alert_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, false);
    }

    public void setAlertSwitch(String str, boolean z) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putBoolean(this.context.getString(R.string.alert_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, z).commit();
        notifyCalledSet(str, this.context.getString(R.string.alert_switch_key));
    }

    public String getWireless(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.wireless_key) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setWireless(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.wireless_key) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.wireless_key));
    }

    public String getDeviceMAC(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.device_mac_key) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setDeviceMAC(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.device_mac_key) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.device_mac_key));
    }

    public String getDeviceIP(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.device_ip_key) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setDeviceIP(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.device_ip_key) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.device_ip_key));
    }

    public String getDeviceID(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.device_id_key) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setDeviceID(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.device_id_key) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.device_id_key));
    }

    public String getFirmwareVersion(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.firmware_version_key) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setFirmwareVersion(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.firmware_version_key) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.firmware_version_key));
    }

    public String getDeviceOwner(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.device_owner_key) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setDeviceOwner(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.device_owner_key) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.device_owner_key));
    }

    public boolean getPushSwitch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return false;
        }
        return sharedPreferences.getBoolean(this.context.getString(R.string.push_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, false);
    }

    public void setPushSwitch(String str, boolean z) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putBoolean(this.context.getString(R.string.push_switch_key) + OpenAccountUIConstants.UNDER_LINE + str, z).commit();
        notifyCalledSet(str, this.context.getString(R.string.push_switch_key));
    }

    public String getAlarmPlan(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.alarm_plan) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setAlarmPlan(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.alarm_plan) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.alarm_plan));
    }

    public int getSupport4G(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.support_4g_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setSupport4G(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.support_4g_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.support_4g_key));
    }

    public String getIccId(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.iccid_key) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setIccId(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.iccid_key) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.iccid_key));
    }

    public String getIccId1(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.iccid1_key) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setIccId1(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.iccid1_key) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.iccid1_key));
    }

    public String getIccId2(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.iccid2_key) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setIccId2(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.iccid2_key) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.iccid2_key));
    }

    public String getCarrier1(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.Carrier1_key) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setCarrier1(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.Carrier1_key) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.Carrier1_key));
    }

    public String getCarrier2(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.Carrier2_key) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setCarrier2(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.Carrier2_key) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.Carrier2_key));
    }

    public int getSupportZoom(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.support_zoom_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setSupportZoom(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.support_zoom_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.support_zoom_key));
    }

    public int getSupportFocus(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.support_focus_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setSupportFocus(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.support_focus_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.support_focus_key));
    }

    public int getSupportPTZEx(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.support_ptz_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setSupportPTZEx(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.support_ptz_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.support_ptz_key));
    }

    public int getMaxLens(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.maxlens) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setMaxLens(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.maxlens) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.maxlens));
    }

    public int getSupportPreset(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.support_preset_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setSupportPreset(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.support_preset_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.support_preset_key));
    }

    public int getNewSupportPreset(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.new_support_preset_key) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setNewSupportPreset(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.new_support_preset_key) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.new_support_preset_key));
    }

    public int getMixZoom(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.support_mix_zoom) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setMixZoom(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.support_mix_zoom) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.support_mix_zoom));
    }

    public int getNewSupportEZOOM(String str) {
        return Integer.valueOf(this.settings.getString(this.context.getString(R.string.EZOOM) + OpenAccountUIConstants.UNDER_LINE + str, "0")).intValue();
    }

    public void setNewSupportEZOOM(String str, int i) {
        this.editor.putString(this.context.getString(R.string.EZOOM) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.EZOOM));
    }

    public void setTwoWayIntercom(int i) {
        SharedPreferences.Editor editor;
        Context context = this.context;
        if (context == null || (editor = this.editor) == null) {
            return;
        }
        editor.putString(context.getString(R.string.TwoWayIntercom), i + "").commit();
    }

    public int getTwoWayIntercom() {
        if (this.settings.getString(this.context.getString(R.string.TwoWayIntercom), "0") == null) {
            setTwoWayIntercom(0);
        }
        return Integer.valueOf(this.settings.getString(this.context.getString(R.string.TwoWayIntercom), "0")).intValue();
    }

    public String getCustom_IPCOSD_Name(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.CustomIPCOSDName) + OpenAccountUIConstants.UNDER_LINE + str, "0");
    }

    public void setCustom_IPCOSD_Name(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.CustomIPCOSDName) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.CustomIPCOSDName));
    }

    public String getVoice_Prompt_Type(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.Voice_Prompt_Type) + OpenAccountUIConstants.UNDER_LINE + str, "0");
    }

    public void setVoice_Prompt_Type(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.Voice_Prompt_Type) + OpenAccountUIConstants.UNDER_LINE + str, str2 + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.Voice_Prompt_Type));
    }

    public boolean getFirstEnterActivity(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return true;
        }
        return sharedPreferences.getBoolean(this.context.getString(R.string.First_Enter_Activity) + OpenAccountUIConstants.UNDER_LINE + str, true);
    }

    public void setFirstEnterActivity(String str, boolean z) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putBoolean(this.context.getString(R.string.First_Enter_Activity) + OpenAccountUIConstants.UNDER_LINE + str, z).commit();
    }

    public boolean getFirstNet(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return true;
        }
        return sharedPreferences.getBoolean(this.context.getString(R.string.First_Net) + OpenAccountUIConstants.UNDER_LINE + str, true);
    }

    public void setFirstNet(String str, boolean z) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putBoolean(this.context.getString(R.string.First_Net) + OpenAccountUIConstants.UNDER_LINE + str, z).commit();
    }

    public String getFirstUpdateVersion(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.First_Update_Version) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setFirstUpdateVersion(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.First_Update_Version) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
    }

    public boolean getFirstFormatInIpc(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return true;
        }
        return sharedPreferences.getBoolean(this.context.getString(R.string.First_Format) + OpenAccountUIConstants.UNDER_LINE + str, true);
    }

    public void setFirstFormatInIpc(String str, boolean z) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putBoolean(this.context.getString(R.string.First_Format) + OpenAccountUIConstants.UNDER_LINE + str, z).commit();
    }

    public int getAOVolumeSize(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return -1;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.AOVolumeSize) + OpenAccountUIConstants.UNDER_LINE + str, -1);
    }

    public void setAOVolumeSize(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.AOVolumeSize) + OpenAccountUIConstants.UNDER_LINE + str, i);
    }

    public int getPageControlEx(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.PageControlEx) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setPageControlEx(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.PageControlEx) + OpenAccountUIConstants.UNDER_LINE + str, i);
        notifyCalledSet(str, this.context.getString(R.string.PageControlEx));
    }

    public int getPageControlEx2(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.PageControlEx2) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setPageControlEx2(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.PageControlEx2) + OpenAccountUIConstants.UNDER_LINE + str, i);
        notifyCalledSet(str, this.context.getString(R.string.PageControlEx2));
    }

    public int getNet4GEnableSwitch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.Net4GEnableSwitch) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setNet4GEnableSwitch(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.Net4GEnableSwitch) + OpenAccountUIConstants.UNDER_LINE + str, i);
    }

    public String getAlarmPlanJson(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.AlarmSchedule) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setAlarmPlanJson(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.AlarmSchedule) + OpenAccountUIConstants.UNDER_LINE + str, str2);
        this.editor.commit();
    }

    public void setFloodlightScheduleJson(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.FloodlightSchedule) + OpenAccountUIConstants.UNDER_LINE + str, str2);
        this.editor.commit();
    }

    public String getFloodlightScheduleJson(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.FloodlightSchedule) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setPresetPosition(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.preset_position) + OpenAccountUIConstants.UNDER_LINE + str, str2 + "").commit();
    }

    public void setDisplayVoice(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.DisplayVoice) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public int getDisplayVoice(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.DisplayVoice) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setDisplayReplay(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.DisplayReplay) + OpenAccountUIConstants.UNDER_LINE + str, i);
    }

    public int getDisplayReplay(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.DisplayReplay) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setDisplayAlert(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.DisplayAlert) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public int getDisplayAlert(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.DisplayAlert) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setDisplayController(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.DisplayController) + OpenAccountUIConstants.UNDER_LINE + str, i).apply();
        notifyCalledSet(str, this.context.getString(R.string.DisplayController));
    }

    public int getDisplayController(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.DisplayController) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setAlarmPlanVersion(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.AlarmPlanVersion) + OpenAccountUIConstants.UNDER_LINE + str, i);
    }

    public int getAlarmPlanVersion(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.AlarmPlanVersion) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setSensorViewDisplay(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.DisplaySensorView) + OpenAccountUIConstants.UNDER_LINE + str, i);
        this.editor.commit();
    }

    public int getSensorViewDisplay(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.DisplaySensorView) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setAlarmLightSwitchDisplay(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.Alarm_Light_Switch_Display) + OpenAccountUIConstants.UNDER_LINE + str, i);
        this.editor.apply();
    }

    public int getAlarmLightSwitchDisplay(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.Alarm_Light_Switch_Display) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void saveAutoName(String str, String str2, int i) {
        if (this.context == null || str == null || "".equals(str) || this.editor == null) {
            return;
        }
        SharedPreferences.Editor editorEdit = this.context.getSharedPreferences("auto_door", 0).edit();
        editorEdit.putString(this.context.getString(R.string.AutoDoorName) + OpenAccountUIConstants.UNDER_LINE + str + OpenAccountUIConstants.UNDER_LINE + i, str2);
        editorEdit.apply();
    }

    public String getAutoName(String str, int i) {
        Context context;
        if (this.settings == null || (context = this.context) == null || str == null) {
            return "";
        }
        return context.getSharedPreferences("auto_door", 0).getString(this.context.getString(R.string.AutoDoorName) + OpenAccountUIConstants.UNDER_LINE + str + OpenAccountUIConstants.UNDER_LINE + i, "");
    }

    public void setNet4GMode(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.Net4GMode) + OpenAccountUIConstants.UNDER_LINE + str, i);
    }

    public int getNet4GMode(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return -1;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.Net4GMode) + OpenAccountUIConstants.UNDER_LINE + str, -1);
    }

    public void setWifiConfigIsExist(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.WifiConfigIsExist) + OpenAccountUIConstants.UNDER_LINE + str, i);
    }

    public int getWifiConfigIsExist(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.WifiConfigIsExist) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public String getPresetPosition(String str) {
        if (this.settings.getString(this.context.getString(R.string.preset_position) + OpenAccountUIConstants.UNDER_LINE + str, null) == null) {
            return TmpConstant.GROUP_ROLE_UNKNOWN;
        }
        return this.settings.getString(this.context.getString(R.string.preset_position) + OpenAccountUIConstants.UNDER_LINE + str, null);
    }

    public void setGuardPositionBitmap(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.guard_position) + OpenAccountUIConstants.UNDER_LINE + str, str2 + "").commit();
    }

    public String getGuardPositionBitmap(String str) {
        if (this.settings.getString(this.context.getString(R.string.guard_position) + OpenAccountUIConstants.UNDER_LINE + str, null) == null) {
            return TmpConstant.GROUP_ROLE_UNKNOWN;
        }
        return this.settings.getString(this.context.getString(R.string.guard_position) + OpenAccountUIConstants.UNDER_LINE + str, null);
    }

    public void setCrossLineDetect(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.cross_line_detect) + OpenAccountUIConstants.UNDER_LINE + str, str2 + "").commit();
    }

    public String getCrossLineDetect(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.cross_line_detect) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setRegionDetectPoint(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.regionDetectPoint) + OpenAccountUIConstants.UNDER_LINE + str, str2 + "").commit();
    }

    public String getRegionDetectPoint(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.regionDetectPoint) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setHumanoidTracking(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.Humanoid_tracking) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.Humanoid_tracking));
    }

    public Integer getHumanoidTracking(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences != null && this.context != null && str != null) {
            return Integer.valueOf(sharedPreferences.getInt(this.context.getString(R.string.Humanoid_tracking) + OpenAccountUIConstants.UNDER_LINE + str, 0));
        }
        return 0;
    }

    public Integer getCrossLine(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences != null && this.context != null && str != null) {
            return Integer.valueOf(sharedPreferences.getInt(this.context.getString(R.string.Cross_line) + OpenAccountUIConstants.UNDER_LINE + str, 0));
        }
        return 0;
    }

    public void setCrossLine(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.Cross_line) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public Integer getAreaDetect(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences != null && this.context != null && str != null) {
            return Integer.valueOf(sharedPreferences.getInt(this.context.getString(R.string.Area_Detect) + OpenAccountUIConstants.UNDER_LINE + str, 0));
        }
        return 0;
    }

    public void setAreaDetect(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.Area_Detect) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public void setTlrClRgn(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.tlr_cl_rgn) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public Integer getTlrClRgn(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences != null && this.context != null && str != null) {
            return Integer.valueOf(sharedPreferences.getInt(this.context.getString(R.string.tlr_cl_rgn) + OpenAccountUIConstants.UNDER_LINE + str, 0));
        }
        return 0;
    }

    public void setTFStorageIVP(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.TFStorageIVP) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public Integer getTFStorageIVP(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences != null && this.context != null && str != null) {
            return Integer.valueOf(sharedPreferences.getInt(this.context.getString(R.string.TFStorageIVP) + OpenAccountUIConstants.UNDER_LINE + str, 0));
        }
        return 0;
    }

    public void setHumanoidTrackingEnable(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.Humanoid_tracking_enable) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public Integer getHumanoidTrackingEnable(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences != null && this.context != null && str != null) {
            return Integer.valueOf(sharedPreferences.getInt(this.context.getString(R.string.Humanoid_tracking_enable) + OpenAccountUIConstants.UNDER_LINE + str, 0));
        }
        return 0;
    }

    public void setCrossLineEnable(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.Cross_line_enable) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public Integer getCrossLineEnable(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences != null && this.context != null && str != null) {
            return Integer.valueOf(sharedPreferences.getInt(this.context.getString(R.string.Cross_line_enable) + OpenAccountUIConstants.UNDER_LINE + str, 0));
        }
        return 0;
    }

    public void setAreaDetectEnable(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.Area_Detect_enable) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public Integer getAreaDetectEnable(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences != null && this.context != null && str != null) {
            return Integer.valueOf(sharedPreferences.getInt(this.context.getString(R.string.Area_Detect_enable) + OpenAccountUIConstants.UNDER_LINE + str, 0));
        }
        return 0;
    }

    public Integer getDisplayLineArea(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences != null && this.context != null && str != null) {
            return Integer.valueOf(sharedPreferences.getInt(this.context.getString(R.string.Display_line_area) + OpenAccountUIConstants.UNDER_LINE + str, 0));
        }
        return 0;
    }

    public void setDisplayLineArea(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.Display_line_area) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public void setAlarmLightSwitch(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.Alarm_Light_Switch) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
    }

    public String getAlarmLightSwitch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.Alarm_Light_Switch) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setRebootSchedule(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.rebootSchedule) + OpenAccountUIConstants.UNDER_LINE + str, str2 + "").commit();
    }

    public String getRebootSchedule(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.rebootSchedule) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setImei(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.imei) + OpenAccountUIConstants.UNDER_LINE + str, str2 + "").commit();
    }

    public String getImei(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.imei) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setNet4gVersion(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.net_4g_version) + OpenAccountUIConstants.UNDER_LINE + str, str2 + "").commit();
    }

    public String getNet4gVersion(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.net_4g_version) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setUserMallUrl(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.user_mall_url) + OpenAccountUIConstants.UNDER_LINE + str, str2 + "").commit();
    }

    public String getUserMallUrl(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.user_mall_url) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setLowPower(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.low_power) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public int getLowPower(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return -1;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.low_power) + OpenAccountUIConstants.UNDER_LINE + str, -1);
    }

    public void setDoubleNetWork(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.double_net) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.double_net));
    }

    public int getDoubleNetWork(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return -1;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.double_net) + OpenAccountUIConstants.UNDER_LINE + str, -1);
    }

    public void setWhiteLightBrightness(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.white_light_brightness) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public int getWhiteLightBrightness(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 1;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.white_light_brightness) + OpenAccountUIConstants.UNDER_LINE + str, 1);
    }

    public void setIRLightBrightness(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.IRLightBrightness) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public int getIRLightBrightness(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 1;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.IRLightBrightness) + OpenAccountUIConstants.UNDER_LINE + str, 1);
    }

    public void setPwmCtrl(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.pwm_ctrl) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public int getPwmCtrl(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.pwm_ctrl) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setStrongReminder(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.strong_reminder) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.strong_reminder));
    }

    public int getStrongReminder(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.strong_reminder) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setTFCardInfo(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.tf_power) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.tf_power));
    }

    public int getTFCardInfo(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.tf_power) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setStrongReminderSwitch(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.strong_reminder_switch) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.strong_reminder_switch));
    }

    public int getStrongReminderSwitch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.strong_reminder_switch) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setVoicePromptMask(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.voice_prompt_mask) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.voice_prompt_mask));
    }

    public int getVoicePromptMask(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.voice_prompt_mask) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setRecordQualityAbility(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.record_quality_ability) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.record_quality_ability));
    }

    public int getRecordQualityAbility(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.record_quality_ability) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setAPNAbility(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.apn_ability) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.apn_ability));
    }

    public int getAPNAbility(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.apn_ability) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setNet4GAbility(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.net_4g_ability) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.net_4g_ability));
    }

    public int getNet4GAbility(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.net_4g_ability) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setRecordQuality(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.record_quality) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.record_quality));
    }

    public int getRecordQuality(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.record_quality) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public String getAPNConfig(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.APNConfig) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setAPNConfig(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.APNConfig) + OpenAccountUIConstants.UNDER_LINE + str, str2);
        this.editor.commit();
    }

    public String getURL(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.URL) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setURL(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.URL) + OpenAccountUIConstants.UNDER_LINE + str, str2);
        this.editor.commit();
    }

    public int getVendorID(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.VendorID) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setVendorID(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.VendorID) + OpenAccountUIConstants.UNDER_LINE + str, i);
        this.editor.commit();
    }

    public int getEnable(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.Enable) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public String getURLSlave(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.URLSlave) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setURLSlave(String str, String str2) {
        if (this.context == null || str == null || "".equals(str) || this.editor == null) {
            return;
        }
        Log.e("setURLSlave", str + "" + str2);
        this.editor.putString(this.context.getString(R.string.URLSlave) + OpenAccountUIConstants.UNDER_LINE + str, str2);
        this.editor.commit();
    }

    public void setEnable(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.Enable) + OpenAccountUIConstants.UNDER_LINE + str, i);
        this.editor.commit();
    }

    public void setHideSIMPlans(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.HideSIMPlans) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.HideSIMPlans));
    }

    public int getHideSIMPlans(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.HideSIMPlans) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setChannelList(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.ChannelList) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.ChannelList));
    }

    public String getChannelList(String str) {
        if (this.context == null || str == null || "".equals(str) || this.editor == null) {
            return "";
        }
        return this.settings.getString(this.context.getString(R.string.ChannelList) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setChannelNumber(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.ChannelNumber) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.ChannelNumber));
    }

    public int getChannelNumber(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.ChannelNumber) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setPTZLinkageSwitch(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.PTZLinkageSwitch) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.PTZLinkageSwitch));
    }

    public int getPTZLinkageSwitch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.PTZLinkageSwitch) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setPTZLinkageTrackSwitch(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.PTZLinkageTrackSwitch) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public int getPTZLinkageTrackSwitch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.PTZLinkageTrackSwitch) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setAllDeviceList(String str) {
        SharedPreferences.Editor editor;
        Context context = this.context;
        if (context == null || (editor = this.editor) == null) {
            return;
        }
        editor.putString(context.getString(R.string.AllDeviceList), str).commit();
    }

    public String getAllDeviceList() {
        Context context;
        SharedPreferences sharedPreferences = this.settings;
        return (sharedPreferences == null || (context = this.context) == null) ? "" : sharedPreferences.getString(context.getString(R.string.AllDeviceList), "");
    }

    public void setAllIMEIList(String str) {
        SharedPreferences.Editor editor;
        Context context = this.context;
        if (context == null || (editor = this.editor) == null) {
            return;
        }
        editor.putString(context.getString(R.string.AllDeviceList), str).commit();
    }

    public String getAllIMEIList() {
        Context context;
        SharedPreferences sharedPreferences = this.settings;
        return (sharedPreferences == null || (context = this.context) == null) ? "" : sharedPreferences.getString(context.getString(R.string.AllDeviceList), "");
    }

    public void setPushDevice(String str) {
        SharedPreferences.Editor editor;
        Context context = this.context;
        if (context == null || (editor = this.editor) == null) {
            return;
        }
        editor.putString(context.getString(R.string.PushDevice), str).commit();
    }

    public String getPushDevice() {
        Context context = this.context;
        return (context == null || this.editor == null) ? "" : this.settings.getString(context.getString(R.string.PushDevice), "");
    }

    public void setCustomerServiceShow(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.CustomerService) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public int getCustomerServiceShow(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.CustomerService) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setEventRecord(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(this.context.getString(R.string.EventRecord) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public int getEventRecord(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.EventRecord) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setDSTSwitch(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.DSTSwitch) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.DSTSwitch));
    }

    public int getDSTSwitch(String str) {
        return this.settings.getInt(this.context.getString(R.string.DSTSwitch) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public boolean getNoticeIsRefuse() {
        Context context;
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || (context = this.context) == null) {
            return true;
        }
        return sharedPreferences.getBoolean(context.getString(R.string.NoticeIsRefuse), false);
    }

    public void setNoticeIsRefuse(boolean z) {
        SharedPreferences.Editor editor;
        Context context = this.context;
        if (context == null || (editor = this.editor) == null) {
            return;
        }
        editor.putBoolean(context.getString(R.string.NoticeIsRefuse), z).commit();
    }

    public void setNetState(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.NetState) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.NetState));
    }

    public int getNetState(String str) {
        return this.settings.getInt(this.context.getString(R.string.NetState) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setFakeDualShow(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.FakeDualShow) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.FakeDualShow));
    }

    public int getFakeDualShow(String str) {
        return this.settings.getInt(this.context.getString(R.string.FakeDualShow) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setFakeDualEnable(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.FakeDualEnable) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.FakeDualEnable));
    }

    public int getFakeDualEnable(String str) {
        return this.settings.getInt(this.context.getString(R.string.FakeDualEnable) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setSmartP(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.SmartP) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.SmartP));
    }

    public int getSmartP(String str) {
        return this.settings.getInt(this.context.getString(R.string.SmartP) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public String getLowPowerAbility(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.LowPowerAbility) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setLowPowerAbility(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.LowPowerAbility) + OpenAccountUIConstants.UNDER_LINE + str, str2);
        this.editor.commit();
    }

    public String getLowPowerDeviceStatus(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.LowPowerDeviceStatus) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setLowPowerDeviceStatus(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.LowPowerDeviceStatus) + OpenAccountUIConstants.UNDER_LINE + str, str2);
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(str2);
        Log.e("LowPowerDeviceStatus = ", sb.toString());
        this.editor.commit();
    }

    public void setLowPowerSwitch(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.LowPowerSwitch) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.LowPowerSwitch));
    }

    public int getLowPowerSwitch(String str) {
        return this.settings.getInt(this.context.getString(R.string.LowPowerSwitch) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setLowPowerWorkMode(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.LowPowerWorkMode) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.LowPowerWorkMode));
    }

    public int getLowPowerWorkMode(String str) {
        return this.settings.getInt(this.context.getString(R.string.LowPowerWorkMode) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setLowPowerPIR(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.LowPowerPIR) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.LowPowerPIR));
    }

    public int getLowPowerPIR(String str) {
        return this.settings.getInt(this.context.getString(R.string.LowPowerPIR) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public int getLowPowerAOVMode4GSwitch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return -1;
        }
        return Integer.valueOf(sharedPreferences.getString(this.context.getString(R.string.LowPowerAOVMode4GSwitch) + OpenAccountUIConstants.UNDER_LINE + str, "-1")).intValue();
    }

    public void setLowPowerAOVMode4GSwitch(String str, int i) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.LowPowerAOVMode4GSwitch) + OpenAccountUIConstants.UNDER_LINE + str, i + "").commit();
        notifyCalledSet(str, this.context.getString(R.string.LowPowerAOVMode4GSwitch));
    }

    public void setPowerMode(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.PowerMode) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.PowerMode));
    }

    public int getPowerMode(String str) {
        return this.settings.getInt(this.context.getString(R.string.PowerMode) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setLowPowerWorkModeMask(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.LowPowerWorkModeMask) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.LowPowerWorkModeMask));
    }

    public int getLowPowerWorkModeMask(String str) {
        return this.settings.getInt(this.context.getString(R.string.LowPowerWorkModeMask) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setNightVisionModeShowCtrl(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.NightVisionModeShowCtrl) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.NightVisionModeShowCtrl));
    }

    public int getNightVisionModeShowCtrl(String str) {
        return this.settings.getInt(this.context.getString(R.string.NightVisionModeShowCtrl) + OpenAccountUIConstants.UNDER_LINE + str, -1);
    }

    public void setPowerModeShow(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.PowerModeShow) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.PowerModeShow));
    }

    public int getPowerModeShow(String str) {
        return this.settings.getInt(this.context.getString(R.string.PowerModeShow) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setNatETHSwitch(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.NatETHSwitch) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.NatETHSwitch));
    }

    public int getNatETHSwitch(String str) {
        return this.settings.getInt(this.context.getString(R.string.NatETHSwitch) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setNatETHSwitchShow(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.NatETHSwitchShow) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.NatETHSwitchShow));
    }

    public int getNatETHSwitchShow(String str) {
        return this.settings.getInt(this.context.getString(R.string.NatETHSwitchShow) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setNatWLANSwitch(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.NatWLANSwitch) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.NatWLANSwitch));
    }

    public int getNatWLANSwitch(String str) {
        return this.settings.getInt(this.context.getString(R.string.NatWLANSwitch) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setNatWLANSwitchShow(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.NatWLANSwitchShow) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.NatWLANSwitchShow));
    }

    public int getNatWLANSwitchShow(String str) {
        return this.settings.getInt(this.context.getString(R.string.NatWLANSwitchShow) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public String getNatWLANSwitchConfig(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.NatAPConfig) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setNatWLANSwitchConfig(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.NatAPConfig) + OpenAccountUIConstants.UNDER_LINE + str, str2);
        this.editor.commit();
    }

    public void setNatAPSecurityConf(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.NatAPSecurityConf) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.NatAPSecurityConf));
    }

    public int getNatAPSecurityConf(String str) {
        return this.settings.getInt(this.context.getString(R.string.NatAPSecurityConf) + OpenAccountUIConstants.UNDER_LINE + str, -1);
    }

    public void setNet4GVSIMMode(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.Net4GVSIMMode) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.Net4GVSIMMode));
    }

    public int getNet4GVSIMMode(String str) {
        return this.settings.getInt(this.context.getString(R.string.Net4GVSIMMode) + OpenAccountUIConstants.UNDER_LINE + str, -1);
    }

    public String getNatAPConfigEx(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.NatAPConfigEx) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setNatAPConfigEx(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.NatAPConfigEx) + OpenAccountUIConstants.UNDER_LINE + str, str2);
        this.editor.commit();
    }

    public String getWlanDhcpConf(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.WlanDhcpConf) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setWlanDhcpConf(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.WlanDhcpConf) + OpenAccountUIConstants.UNDER_LINE + str, str2);
        this.editor.commit();
    }

    public String getEthernetDhcpConf(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.EthernetDhcpConf) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setEthernetDhcpConf(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.EthernetDhcpConf) + OpenAccountUIConstants.UNDER_LINE + str, str2);
        this.editor.commit();
    }

    public void setNet4GPlansUrl(String str, String str2) {
        this.editor.putString(this.context.getString(R.string.Net4GPlansUrl) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.Net4GPlansUrl));
    }

    public String getNet4GPlansUrl(String str) {
        return this.settings.getString(this.context.getString(R.string.Net4GPlansUrl) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setWifiModelInfo(String str, String str2) {
        this.editor.putString(this.context.getString(R.string.WifiModelInfo) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.WifiModelInfo));
    }

    public String getWifiModelInfo(String str) {
        return this.settings.getString(this.context.getString(R.string.WifiModelInfo) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setFloodlightSwitch(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.FloodlightSwitch) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.FloodlightSwitch));
    }

    public int getFloodlightSwitch(String str) {
        return this.settings.getInt(this.context.getString(R.string.FloodlightSwitch) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setFloodlightSwitchShow(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.FloodlightSwitchShow) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.FloodlightSwitchShow));
    }

    public int getFloodlightSwitchShow(String str) {
        return this.settings.getInt(this.context.getString(R.string.FloodlightSwitchShow) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setFloodlightScheduleEnable(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.FloodlightScheduleEnable) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.FloodlightScheduleEnable));
    }

    public int getFloodlightScheduleEnable(String str) {
        return this.settings.getInt(this.context.getString(R.string.FloodlightScheduleEnable) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public int getADSwitch() {
        Context context;
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || (context = this.context) == null) {
            return 0;
        }
        return sharedPreferences.getInt(context.getString(R.string.ADSwitch), 0);
    }

    public void setADSwitch(int i) {
        SharedPreferences.Editor editor;
        Context context = this.context;
        if (context == null || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(context.getString(R.string.ADSwitch), i).commit();
    }

    public int getIsScore() {
        Context context;
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || (context = this.context) == null) {
            return 0;
        }
        return sharedPreferences.getInt(context.getString(R.string.Score), 0);
    }

    public void setIsScore(int i) {
        SharedPreferences.Editor editor;
        Context context = this.context;
        if (context == null || (editor = this.editor) == null) {
            return;
        }
        editor.putInt(context.getString(R.string.Score), i).commit();
    }

    public int getExpHighLightShow(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.ExpHighLightShow) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setExpHighLightShow(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.ExpHighLightShow) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.ExpHighLightShow));
    }

    public String getExpHighLight(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.ExpHighLight) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setExpHighLight(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.ExpHighLight) + OpenAccountUIConstants.UNDER_LINE + str, str2);
        this.editor.commit();
    }

    public void setPTZHide(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.PTZHide) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.PTZHide));
    }

    public int getPTZHide(String str) {
        return this.settings.getInt(this.context.getString(R.string.PTZHide) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setNightVisionHide(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.NightVisionHide) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.NightVisionHide));
    }

    public int getNightVisionHide(String str) {
        return this.settings.getInt(this.context.getString(R.string.NightVisionHide) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setTandemVuNightVisionHide(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.TandemVuNightVisionHide) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.TandemVuNightVisionHide));
    }

    public int getTandemVuNightVisionHide(String str) {
        return this.settings.getInt(this.context.getString(R.string.TandemVuNightVisionHide) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setTimeRecordEnable(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.TimeRecordEnable) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.TimeRecordEnable));
    }

    public int getTimeRecordEnable(String str) {
        return this.settings.getInt(this.context.getString(R.string.TimeRecordEnable) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setTimeRecordPlan(String str, String str2) {
        SharedPreferences.Editor editor;
        if (this.context == null || str == null || "".equals(str) || (editor = this.editor) == null) {
            return;
        }
        editor.putString(this.context.getString(R.string.TimeRecordPlan) + OpenAccountUIConstants.UNDER_LINE + str, str2);
        this.editor.commit();
    }

    public String getTimeRecordPlan(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return "";
        }
        return sharedPreferences.getString(this.context.getString(R.string.TimeRecordPlan) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setMapShow(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.MapShow) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.MapShow));
    }

    public int getMapShow(String str) {
        return this.settings.getInt(this.context.getString(R.string.MapShow) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public void setLocationAbility(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.LocationAbility) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.LocationAbility));
    }

    public int getLocationAbility(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.LocationAbility) + OpenAccountUIConstants.UNDER_LINE + str, -1);
    }

    public void setEventSearch(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.EventSearch) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.EventSearch));
    }

    public int getEventSearch(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.EventSearch) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public String getShareHistory() {
        Context context;
        SharedPreferences sharedPreferences = this.settings;
        return (sharedPreferences == null || (context = this.context) == null) ? "" : sharedPreferences.getString(context.getString(R.string.ShareHistory), "");
    }

    public void setShareHistory(String str) {
        SharedPreferences.Editor editor;
        Context context = this.context;
        if (context == null || (editor = this.editor) == null) {
            return;
        }
        editor.putString(context.getString(R.string.ShareHistory), str);
        this.editor.commit();
    }

    public void setIsRouter(String str, int i) {
        if (this.context == null || str == null || "".equals(str) || this.editor == null) {
            return;
        }
        Log.e("路由器", str + ContainerUtils.KEY_VALUE_DELIMITER + i);
        this.editor.putInt(this.context.getString(R.string.IsRouter) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
    }

    public int getIsRouter(String str) {
        SharedPreferences sharedPreferences = this.settings;
        if (sharedPreferences == null || this.context == null || str == null) {
            return 0;
        }
        return sharedPreferences.getInt(this.context.getString(R.string.IsRouter) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public String getLikeList() {
        Context context;
        SharedPreferences sharedPreferences = this.settings;
        return (sharedPreferences == null || (context = this.context) == null) ? "" : sharedPreferences.getString(context.getString(R.string.LikeList), "");
    }

    public void setLikeList(String str) {
        SharedPreferences.Editor editor;
        Context context = this.context;
        if (context == null || (editor = this.editor) == null) {
            return;
        }
        editor.putString(context.getString(R.string.LikeList), str).commit();
    }

    public void setIvpZoomEnable(String str, int i) {
        this.editor.putInt(this.context.getString(R.string.IvpZoomEnable) + OpenAccountUIConstants.UNDER_LINE + str, i).commit();
        notifyCalledSet(str, this.context.getString(R.string.IvpZoomEnable));
    }

    public int getIvpZoomEnable(String str) {
        return this.settings.getInt(this.context.getString(R.string.IvpZoomEnable) + OpenAccountUIConstants.UNDER_LINE + str, 0);
    }

    public String getStreamChnMaxEzoom(String str) {
        return this.settings.getString(this.context.getString(R.string.StreamChnMaxEzoom) + OpenAccountUIConstants.UNDER_LINE + str, "");
    }

    public void setStreamChnMaxEzoom(String str, String str2) {
        this.editor.putString(this.context.getString(R.string.StreamChnMaxEzoom) + OpenAccountUIConstants.UNDER_LINE + str, str2).commit();
        notifyCalledSet(str, this.context.getString(R.string.StreamChnMaxEzoom));
    }
}
