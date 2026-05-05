package tools;

import android.os.Looper;
import bean.APNBean;
import bean.AlarmPlanBean;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.google.gson.Gson;
import config.Constants;
import java.util.Map;
import sdk.IPCManager;

/* JADX INFO: loaded from: classes4.dex */
public class SettingsCtrl {
    private static final String TAG = "SettingsCtrl";
    private boolean isInitSucceed;

    private SettingsCtrl() {
    }

    private static class SettingsCtrlHolder {
        public static final SettingsCtrl INSTANCE = new SettingsCtrl();

        private SettingsCtrlHolder() {
        }
    }

    public static SettingsCtrl getInstance() {
        return SettingsCtrlHolder.INSTANCE;
    }

    public void initCallBack(final String str, final boolean z, final ISetCallback iSetCallback) {
        if (!this.isInitSucceed) {
            IPCManager.getInstance().getDevice(str, new IPanelCallback() { // from class: tools.SettingsCtrl.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z2, Object obj) {
                    if (z2) {
                        SettingsCtrl.this.isInitSucceed = true;
                        iSetCallback.onSucceed();
                    } else if (z) {
                        LogEx.d(true, SettingsCtrl.TAG, "设备初始化失败,第一次");
                        SettingsCtrl.this.initCallBack(str, false, iSetCallback);
                    } else {
                        LogEx.e(true, SettingsCtrl.TAG, "设备初始化失败,第二次");
                        iSetCallback.onFailed();
                        SettingsCtrl.this.isInitSucceed = false;
                    }
                }
            });
        } else {
            iSetCallback.onSucceed();
        }
    }

    public void getIccIdParam(final String str, final ISetCallback iSetCallback) {
        IPCManager.getInstance().getDevice(str).getProperties(new IPanelCallback() { // from class: tools.SettingsCtrl.2
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code") && object.getInteger("code").intValue() == 200 && object.containsKey("data")) {
                    try {
                        JSONObject jSONObject = object.getJSONObject("data");
                        if (jSONObject.containsKey(Constants.ICCID)) {
                            JSONObject jSONObject2 = jSONObject.getJSONObject(Constants.ICCID);
                            if (jSONObject2.containsKey("value")) {
                                String string = jSONObject2.getString("value");
                                if (!string.equals(SharePreferenceManager.getInstance().getIccId(str))) {
                                    SharePreferenceManager.getInstance().setIccId(str, string);
                                }
                                iSetCallback.onSucceed();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public synchronized void getProperties(final String str, final MyCallback myCallback) {
        IPCManager.getInstance().getDevice(str).getProperties(new IPanelCallback() { // from class: tools.SettingsCtrl.3
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                int iIntValue;
                int iIntValue2;
                int iIntValue3;
                String string;
                int iIntValue4;
                String string2;
                String string3;
                int iIntValue5;
                int iIntValue6;
                int iIntValue7;
                String string4;
                int iIntValue8;
                int iIntValue9;
                int iIntValue10;
                int iIntValue11;
                int iIntValue12;
                String string5;
                String string6;
                String string7;
                int iIntValue13;
                int iIntValue14;
                String string8;
                String string9;
                int iIntValue15;
                int iIntValue16;
                int iIntValue17;
                int iIntValue18;
                int iIntValue19;
                int iIntValue20;
                int intValue;
                int intValue2;
                int iIntValue21;
                int iIntValue22;
                int iIntValue23;
                int iIntValue24;
                int iIntValue25;
                int iIntValue26;
                int iIntValue27;
                int iIntValue28;
                int iIntValue29;
                int iIntValue30;
                String string10;
                String string11;
                String string12;
                String string13;
                String string14;
                int iIntValue31;
                int iIntValue32;
                int iIntValue33;
                int iIntValue34;
                int iIntValue35;
                int iIntValue36;
                int iIntValue37;
                int iIntValue38;
                int iIntValue39;
                int iIntValue40;
                if (z && obj != null && !"".equals(String.valueOf(obj))) {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code") && object.getInteger("code").intValue() != 200) {
                        return;
                    }
                    if (object.containsKey("data")) {
                        try {
                            JSONObject jSONObject = object.getJSONObject("data");
                            if (jSONObject.containsKey(Constants.MIC_SWITCH_MODEL_NAME)) {
                                JSONObject jSONObject2 = jSONObject.getJSONObject(Constants.MIC_SWITCH_MODEL_NAME);
                                if (jSONObject2.containsKey("value")) {
                                    boolean z2 = jSONObject2.getInteger("value").intValue() == 1;
                                    if (SharePreferenceManager.getInstance().getMicSwitch(str) ^ z2) {
                                        SharePreferenceManager.getInstance().setMicSwitch(str, z2);
                                    }
                                }
                            }
                            if (jSONObject.containsKey(Constants.SPEAKER_SWITCH_MODEL_NAME)) {
                                JSONObject jSONObject3 = jSONObject.getJSONObject(Constants.SPEAKER_SWITCH_MODEL_NAME);
                                if (jSONObject3.containsKey("value")) {
                                    boolean z3 = jSONObject3.getInteger("value").intValue() == 1;
                                    if (SharePreferenceManager.getInstance().getSpeakerSwitch(str) ^ z3) {
                                        SharePreferenceManager.getInstance().setSpeakerSwitch(str, z3);
                                    }
                                }
                            }
                            if (jSONObject.containsKey(Constants.STATUS_LIGHT_SWITCH_MODEL_NAME)) {
                                JSONObject jSONObject4 = jSONObject.getJSONObject(Constants.STATUS_LIGHT_SWITCH_MODEL_NAME);
                                if (jSONObject4.containsKey("value")) {
                                    boolean z4 = jSONObject4.getInteger("value").intValue() == 1;
                                    if (SharePreferenceManager.getInstance().getStatusLightSwitch(str) ^ z4) {
                                        SharePreferenceManager.getInstance().setStatusLightSwitch(str, z4);
                                    }
                                }
                            }
                            if (jSONObject.containsKey(Constants.DAY_NIGHT_MODE_MODEL_NAME)) {
                                JSONObject jSONObject5 = jSONObject.getJSONObject(Constants.DAY_NIGHT_MODE_MODEL_NAME);
                                if (jSONObject5.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setDayNightMode(str, jSONObject5.getInteger("value").intValue());
                                }
                            }
                            if (jSONObject.containsKey(Constants.STREAM_VIDEO_QUALITY_MODEL_NAME)) {
                                JSONObject jSONObject6 = jSONObject.getJSONObject(Constants.STREAM_VIDEO_QUALITY_MODEL_NAME);
                                if (jSONObject6.containsKey("value") && (iIntValue40 = jSONObject6.getInteger("value").intValue()) != SharePreferenceManager.getInstance().getStreamVideoQuality(str)) {
                                    SharePreferenceManager.getInstance().setStreamVideoQuality(str, iIntValue40);
                                }
                            }
                            if (jSONObject.containsKey(Constants.SUBSTREAM_VIDEO_QUALITY_MODEL_NAME)) {
                                JSONObject jSONObject7 = jSONObject.getJSONObject(Constants.SUBSTREAM_VIDEO_QUALITY_MODEL_NAME);
                                if (jSONObject7.containsKey("value") && (iIntValue39 = jSONObject7.getInteger("value").intValue()) != SharePreferenceManager.getInstance().getSubStreamVideoQuality(str)) {
                                    SharePreferenceManager.getInstance().setSubStreamVideoQuality(str, iIntValue39);
                                }
                            }
                            if (jSONObject.containsKey(Constants.IMAGE_FLIP_STATE_MODEL_NAME)) {
                                JSONObject jSONObject8 = jSONObject.getJSONObject(Constants.IMAGE_FLIP_STATE_MODEL_NAME);
                                if (jSONObject8.containsKey("value") && (iIntValue38 = jSONObject8.getInteger("value").intValue()) != SharePreferenceManager.getInstance().getImageFlip(str)) {
                                    SharePreferenceManager.getInstance().setImageFlip(str, iIntValue38);
                                }
                            }
                            if (jSONObject.containsKey(Constants.ENCRYPT_SWITCH_MODEL_NAME)) {
                                JSONObject jSONObject9 = jSONObject.getJSONObject(Constants.ENCRYPT_SWITCH_MODEL_NAME);
                                if (jSONObject9.containsKey("value")) {
                                    boolean z5 = jSONObject9.getInteger("value").intValue() == 1;
                                    if (SharePreferenceManager.getInstance().getEncryptSwitch(str) ^ z5) {
                                        SharePreferenceManager.getInstance().setEncryptSwitch(str, z5);
                                    }
                                }
                            }
                            if (jSONObject.containsKey(Constants.ALARM_SWITCH_MODEL_NAME)) {
                                JSONObject jSONObject10 = jSONObject.getJSONObject(Constants.ALARM_SWITCH_MODEL_NAME);
                                if (jSONObject10.containsKey("value") && (iIntValue37 = jSONObject10.getInteger("value").intValue()) != SharePreferenceManager.getInstance().getAlarmSwitch(str)) {
                                    SharePreferenceManager.getInstance().setAlarmSwitch(str, iIntValue37);
                                }
                            }
                            if (jSONObject.containsKey(Constants.VOICE_DETECT_SENSITIVITY_MODEL_NAME)) {
                                JSONObject jSONObject11 = jSONObject.getJSONObject(Constants.VOICE_DETECT_SENSITIVITY_MODEL_NAME);
                                if (jSONObject11.containsKey("value") && (iIntValue36 = jSONObject11.getInteger("value").intValue()) != SharePreferenceManager.getInstance().getVoiceDetectSensitivity(str)) {
                                    SharePreferenceManager.getInstance().setVoiceDetectSensitivity(str, iIntValue36);
                                }
                            }
                            if (jSONObject.containsKey(Constants.ALARM_FREQUENCY_LEVEL_MODEL_NAME)) {
                                JSONObject jSONObject12 = jSONObject.getJSONObject(Constants.ALARM_FREQUENCY_LEVEL_MODEL_NAME);
                                if (jSONObject12.containsKey("value") && (iIntValue35 = jSONObject12.getInteger("value").intValue()) != SharePreferenceManager.getInstance().getAlarmFrequencyLevel(str)) {
                                    SharePreferenceManager.getInstance().setAlarmFrequencyLevel(str, iIntValue35);
                                }
                            }
                            if (jSONObject.containsKey(Constants.STORAGE_STATUS_MODEL_NAME)) {
                                JSONObject jSONObject13 = jSONObject.getJSONObject(Constants.STORAGE_STATUS_MODEL_NAME);
                                if (jSONObject13.containsKey("value") && (iIntValue34 = jSONObject13.getInteger("value").intValue()) != SharePreferenceManager.getInstance().getStorageStatus(str)) {
                                    SharePreferenceManager.getInstance().setStorageStatus(str, iIntValue34);
                                }
                            }
                            if (jSONObject.containsKey(Constants.STORAGE_TOTAL_CAPACITY_MODEL_NAME)) {
                                JSONObject jSONObject14 = jSONObject.getJSONObject(Constants.STORAGE_TOTAL_CAPACITY_MODEL_NAME);
                                if (jSONObject14.containsKey("value")) {
                                    float floatValue = jSONObject14.getFloatValue("value");
                                    if (floatValue != SharePreferenceManager.getInstance().getStorageTotalCapacity(str)) {
                                        SharePreferenceManager.getInstance().setStorageTotalCapacity(str, floatValue);
                                    }
                                }
                            }
                            if (jSONObject.containsKey(Constants.STORAGE_REMAIN_CAPACITY_MODEL_NAME)) {
                                JSONObject jSONObject15 = jSONObject.getJSONObject(Constants.STORAGE_REMAIN_CAPACITY_MODEL_NAME);
                                if (jSONObject15.containsKey("value")) {
                                    float floatValue2 = jSONObject15.getFloatValue("value");
                                    if (floatValue2 != SharePreferenceManager.getInstance().getStorageRemainingCapacity(str)) {
                                        SharePreferenceManager.getInstance().setStorageRemainingCapacity(str, floatValue2);
                                    }
                                }
                            }
                            if (jSONObject.containsKey(Constants.STORAGE_RECORD_MODE_MODEL_NAME)) {
                                JSONObject jSONObject16 = jSONObject.getJSONObject(Constants.STORAGE_RECORD_MODE_MODEL_NAME);
                                if (jSONObject16.containsKey("value") && (iIntValue33 = jSONObject16.getInteger("value").intValue()) != SharePreferenceManager.getInstance().getStorageRecordMode(str)) {
                                    SharePreferenceManager.getInstance().setStorageRecordMode(str, iIntValue33);
                                }
                            }
                            if (jSONObject.containsKey(Constants.PTZLinkageSwitch) && (iIntValue32 = jSONObject.getJSONObject(Constants.PTZLinkageSwitch).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getPTZLinkageSwitch(str)) {
                                SharePreferenceManager.getInstance().setPTZLinkageSwitch(str, iIntValue32);
                            }
                            if (jSONObject.containsKey("SupportMotionDetect") && (iIntValue31 = jSONObject.getJSONObject("SupportMotionDetect").getInteger("value").intValue()) != SharePreferenceManager.getInstance().getSupportMotionDetect(str)) {
                                SharePreferenceManager.getInstance().setSupportMotionDetect(str, iIntValue31);
                            }
                            if (jSONObject.containsKey(Constants.DEVICE_TIME)) {
                                JSONObject jSONObject17 = jSONObject.getJSONObject(Constants.DEVICE_TIME);
                                if (jSONObject17.containsKey("value")) {
                                    JSONObject jSONObject18 = jSONObject17.getJSONObject("value");
                                    String string15 = jSONObject18.getString("TZ");
                                    SharePreferenceManager.getInstance().setDeviceTime(str, Integer.parseInt(jSONObject18.getString("Time")));
                                    SharePreferenceManager.getInstance().setDeviceTZ(str, string15);
                                }
                            }
                            if (jSONObject.containsKey(Constants.WIRELESS)) {
                                JSONObject jSONObject19 = jSONObject.getJSONObject(Constants.WIRELESS);
                                if (jSONObject19.containsKey("value") && (string14 = jSONObject19.getString("value")) != null && !string14.equals(SharePreferenceManager.getInstance().getWireless(str))) {
                                    SharePreferenceManager.getInstance().setWireless(str, string14);
                                }
                            }
                            if (jSONObject.containsKey(Constants.ALERT_SWITCH)) {
                                JSONObject jSONObject20 = jSONObject.getJSONObject(Constants.ALERT_SWITCH);
                                if (jSONObject20.containsKey("value")) {
                                    boolean z6 = jSONObject20.getInteger("value").intValue() == 1;
                                    if (SharePreferenceManager.getInstance().getAlertSwitch(str) ^ z6) {
                                        SharePreferenceManager.getInstance().setAlertSwitch(str, z6);
                                    }
                                }
                            }
                            if (jSONObject.containsKey(Constants.NETWORK_INFO)) {
                                JSONObject jSONObject21 = jSONObject.getJSONObject(Constants.NETWORK_INFO);
                                if (jSONObject21.containsKey("value")) {
                                    JSONObject jSONObject22 = jSONObject21.getJSONObject("value");
                                    String string16 = jSONObject22.getString("IP");
                                    String string17 = jSONObject22.getString(TmpConstant.DATA_KEY_DEVICENAME);
                                    if (string16 != null && !string16.equals(SharePreferenceManager.getInstance().getDeviceIP(str))) {
                                        SharePreferenceManager.getInstance().setDeviceIP(str, string16);
                                    }
                                    if (string17 != null && !string17.equals(SharePreferenceManager.getInstance().getDeviceMAC(str))) {
                                        SharePreferenceManager.getInstance().setDeviceMAC(str, string17);
                                    }
                                }
                            }
                            if (jSONObject.containsKey(Constants.DEVICE_NAME)) {
                                JSONObject jSONObject23 = jSONObject.getJSONObject(Constants.DEVICE_NAME);
                                if (jSONObject23.containsKey("value") && (string13 = jSONObject23.getString("value")) != null && !string13.equals(SharePreferenceManager.getInstance().getDeviceName(str))) {
                                    SharePreferenceManager.getInstance().setDeviceName(str, string13);
                                }
                            }
                            if (jSONObject.containsKey(Constants.DEVICE_ID)) {
                                JSONObject jSONObject24 = jSONObject.getJSONObject(Constants.DEVICE_ID);
                                if (jSONObject24.containsKey("value") && (string12 = jSONObject24.getString("value")) != null && !string12.equals(SharePreferenceManager.getInstance().getDeviceID(str))) {
                                    SharePreferenceManager.getInstance().setDeviceID(str, string12);
                                }
                            }
                            if (jSONObject.containsKey(Constants.DEVICE_OWNER)) {
                                JSONObject jSONObject25 = jSONObject.getJSONObject(Constants.DEVICE_OWNER);
                                if (jSONObject25.containsKey("value") && (string11 = jSONObject25.getString("value")) != null && !string11.equals(SharePreferenceManager.getInstance().getDeviceOwner(str))) {
                                    SharePreferenceManager.getInstance().setDeviceOwner(str, string11);
                                }
                            }
                            if (jSONObject.containsKey(Constants.FIRMWARE_VERSION)) {
                                JSONObject jSONObject26 = jSONObject.getJSONObject(Constants.FIRMWARE_VERSION);
                                if (jSONObject26.containsKey("value") && (string10 = jSONObject26.getString("value")) != null && !string10.equals(SharePreferenceManager.getInstance().getFirmwareVersion(str))) {
                                    SharePreferenceManager.getInstance().setFirmwareVersion(str, string10);
                                }
                            }
                            if (jSONObject.containsKey(Constants.PUSH_SWITCH)) {
                                JSONObject jSONObject27 = jSONObject.getJSONObject(Constants.PUSH_SWITCH);
                                if (jSONObject27.containsKey("value")) {
                                    boolean z7 = jSONObject27.getInteger("value").intValue() == 1;
                                    if (SharePreferenceManager.getInstance().getPushSwitch(str) ^ z7) {
                                        SharePreferenceManager.getInstance().setPushSwitch(str, z7);
                                    }
                                }
                            }
                            if (jSONObject.containsKey(Constants.ALARM_NOTIFY_PLAN_MODEL_NAME)) {
                                JSONObject jSONObject28 = jSONObject.getJSONObject(Constants.ALARM_NOTIFY_PLAN_MODEL_NAME);
                                if (jSONObject28.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setAlarmPlan(str, jSONObject28.getJSONArray("value").toString());
                                }
                            }
                            if (jSONObject.containsKey(Constants.ALARM_MODE_NAME)) {
                                JSONObject jSONObject29 = jSONObject.getJSONObject(Constants.ALARM_MODE_NAME);
                                if (jSONObject29.containsKey("value") && (iIntValue30 = jSONObject29.getInteger("value").intValue()) != SharePreferenceManager.getInstance().getAlarmMode(str)) {
                                    SharePreferenceManager.getInstance().setAlarmMode(str, iIntValue30);
                                }
                            }
                            if (jSONObject.containsKey("SupportMotionDetect")) {
                                JSONObject jSONObject30 = jSONObject.getJSONObject("SupportMotionDetect");
                                if (jSONObject30.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setSupportMotionDetect(str, jSONObject30.getInteger("value").intValue());
                                }
                            }
                            if (jSONObject.containsKey(Constants.MOTION_DETECT_SENSITIVITY_MODEL_NAME)) {
                                JSONObject jSONObject31 = jSONObject.getJSONObject(Constants.MOTION_DETECT_SENSITIVITY_MODEL_NAME);
                                if (jSONObject31.containsKey("value") && (iIntValue29 = jSONObject31.getInteger("value").intValue()) != SharePreferenceManager.getInstance().getMotionDetectSensitivity(str)) {
                                    SharePreferenceManager.getInstance().setMotionDetectSensitivity(str, iIntValue29);
                                }
                            }
                            if (jSONObject.containsKey(Constants.FACE_DETECT_SENSITIVITY)) {
                                JSONObject jSONObject32 = jSONObject.getJSONObject(Constants.FACE_DETECT_SENSITIVITY);
                                if (jSONObject32.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setFaceDetectMode(str, jSONObject32.getInteger("value").intValue());
                                }
                            }
                            if (jSONObject.containsKey(Constants.INTELLIGENT_TRACKING)) {
                                JSONObject jSONObject33 = jSONObject.getJSONObject(Constants.INTELLIGENT_TRACKING);
                                if (jSONObject33.containsKey("value") && (iIntValue28 = jSONObject33.getInteger("value").intValue()) != SharePreferenceManager.getInstance().getIntelligentMode(str)) {
                                    SharePreferenceManager.getInstance().setIntelligentMode(str, iIntValue28);
                                }
                            }
                            if (jSONObject.containsKey(Constants.SUPPORT_4G)) {
                                JSONObject jSONObject34 = jSONObject.getJSONObject(Constants.SUPPORT_4G);
                                if (jSONObject34.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setSupport4G(str, jSONObject34.getInteger("value").intValue());
                                }
                            }
                            if (jSONObject.containsKey(Constants.ICCID)) {
                                JSONObject jSONObject35 = jSONObject.getJSONObject(Constants.ICCID);
                                if (jSONObject35.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setIccId(str, jSONObject35.getString("value"));
                                }
                            }
                            if (jSONObject.containsKey(Constants.ICCID1)) {
                                JSONObject jSONObject36 = jSONObject.getJSONObject(Constants.ICCID1);
                                if (jSONObject36.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setIccId1(str, jSONObject36.getString("value"));
                                }
                            }
                            if (jSONObject.containsKey(Constants.ICCID2)) {
                                JSONObject jSONObject37 = jSONObject.getJSONObject(Constants.ICCID2);
                                if (jSONObject37.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setIccId2(str, jSONObject37.getString("value"));
                                }
                            }
                            if (jSONObject.containsKey(Constants.Carrier1)) {
                                JSONObject jSONObject38 = jSONObject.getJSONObject(Constants.Carrier1);
                                if (jSONObject38.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setCarrier1(str, jSONObject38.getString("value"));
                                }
                            }
                            if (jSONObject.containsKey(Constants.Carrier2)) {
                                JSONObject jSONObject39 = jSONObject.getJSONObject(Constants.Carrier2);
                                if (jSONObject39.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setCarrier2(str, jSONObject39.getString("value"));
                                }
                            }
                            if (jSONObject.containsKey(Constants.SUPPORT_PTZ_EX)) {
                                JSONObject jSONObject40 = jSONObject.getJSONObject(Constants.SUPPORT_PTZ_EX);
                                if (jSONObject40.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setSupportPTZEx(str, jSONObject40.getInteger("value").intValue());
                                }
                            }
                            if (jSONObject.containsKey(Constants.PTZ_ABILITY_EX)) {
                                JSONObject jSONObject41 = jSONObject.getJSONObject(Constants.PTZ_ABILITY_EX);
                                if (jSONObject41.containsKey("value")) {
                                    StringBuilder sbReverse = new StringBuilder(Integer.toBinaryString(jSONObject41.getInteger("value").intValue())).reverse();
                                    for (int i = 0; i < sbReverse.length(); i++) {
                                        int iCharAt = sbReverse.charAt(i) - '0';
                                        if (i == 0) {
                                            SharePreferenceManager.getInstance().setSupportFocus(str, iCharAt);
                                        } else if (i == 1) {
                                            SharePreferenceManager.getInstance().setSupportZoom(str, iCharAt);
                                        } else if (i == 2) {
                                            SharePreferenceManager.getInstance().setSupportPreset(str, iCharAt);
                                        } else if (i == 3) {
                                            SharePreferenceManager.getInstance().setMixZoom(str, iCharAt);
                                        } else if (i == 4) {
                                            SharePreferenceManager.getInstance().setNewSupportPreset(str, iCharAt);
                                        } else if (i == 6) {
                                            SharePreferenceManager.getInstance().setNewSupportEZOOM(str, iCharAt);
                                        }
                                    }
                                }
                            }
                            if (jSONObject.containsKey(Constants.MaxLens)) {
                                JSONObject jSONObject42 = jSONObject.getJSONObject(Constants.MaxLens);
                                if (jSONObject42.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setMaxLens(str, jSONObject42.getInteger("value").intValue());
                                }
                            }
                            if (jSONObject.containsKey(Constants.Custom_IPCOSD_Name)) {
                                JSONObject jSONObject43 = jSONObject.getJSONObject(Constants.Custom_IPCOSD_Name);
                                if (jSONObject43.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setCustom_IPCOSD_Name(str, jSONObject43.getString("value"));
                                }
                            }
                            if (jSONObject.containsKey(Constants.Voice_Prompt_Type)) {
                                JSONObject jSONObject44 = jSONObject.getJSONObject(Constants.Voice_Prompt_Type);
                                if (jSONObject44.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setVoice_Prompt_Type(str, jSONObject44.getJSONObject("value").getString("FileName"));
                                }
                            }
                            if (jSONObject.containsKey(Constants.AOVolumeSize)) {
                                JSONObject jSONObject45 = jSONObject.getJSONObject(Constants.AOVolumeSize);
                                if (jSONObject45.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setAOVolumeSize(str, jSONObject45.getInteger("value").intValue());
                                }
                            }
                            if (jSONObject.containsKey(Constants.PageControlEx)) {
                                JSONObject jSONObject46 = jSONObject.getJSONObject(Constants.PageControlEx);
                                if (jSONObject46.containsKey("value")) {
                                    int iIntValue41 = jSONObject46.getInteger("value").intValue();
                                    SharePreferenceManager.getInstance().setDisplayReplay(str, 0);
                                    SharePreferenceManager.getInstance().setDisplayVoice(str, 0);
                                    SharePreferenceManager.getInstance().setDisplayAlert(str, 0);
                                    SharePreferenceManager.getInstance().setDisplayController(str, 0);
                                    SharePreferenceManager.getInstance().setAlarmPlanVersion(str, 0);
                                    SharePreferenceManager.getInstance().setSensorViewDisplay(str, 0);
                                    SharePreferenceManager.getInstance().setAlarmLightSwitchDisplay(str, 0);
                                    SharePreferenceManager.getInstance().setLowPower(str, 0);
                                    SharePreferenceManager.getInstance().setDoubleNetWork(str, 0);
                                    SharePreferenceManager.getInstance().setPwmCtrl(str, 0);
                                    SharePreferenceManager.getInstance().setStrongReminder(str, 0);
                                    SharePreferenceManager.getInstance().setTFCardInfo(str, 0);
                                    SharePreferenceManager.getInstance().setVoicePromptMask(str, 0);
                                    SharePreferenceManager.getInstance().setRecordQualityAbility(str, 0);
                                    SharePreferenceManager.getInstance().setAPNAbility(str, 0);
                                    SharePreferenceManager.getInstance().setNet4GAbility(str, 0);
                                    SharePreferenceManager.getInstance().setNatETHSwitchShow(str, 0);
                                    SharePreferenceManager.getInstance().setNatWLANSwitchShow(str, 0);
                                    SharePreferenceManager.getInstance().setFloodlightSwitchShow(str, 0);
                                    SharePreferenceManager.getInstance().setLowPowerPIR(str, 0);
                                    SharePreferenceManager.getInstance().setFakeDualShow(str, 0);
                                    SharePreferenceManager.getInstance().setPowerModeShow(str, 0);
                                    SharePreferenceManager.getInstance().setMapShow(str, 0);
                                    SharePreferenceManager.getInstance().setExpHighLightShow(str, 0);
                                    SharePreferenceManager.getInstance().setEventSearch(str, 0);
                                    String string18 = new StringBuilder(Integer.toBinaryString(iIntValue41)).reverse().toString();
                                    try {
                                        SharePreferenceManager.getInstance().setDisplayReplay(str, Integer.parseInt(String.valueOf(string18.charAt(0))));
                                        SharePreferenceManager.getInstance().setDisplayVoice(str, Integer.parseInt(String.valueOf(string18.charAt(1))));
                                        SharePreferenceManager.getInstance().setDisplayAlert(str, Integer.parseInt(String.valueOf(string18.charAt(2))));
                                        SharePreferenceManager.getInstance().setDisplayController(str, Integer.parseInt(String.valueOf(string18.charAt(3))));
                                        SharePreferenceManager.getInstance().setAlarmPlanVersion(str, Integer.parseInt(String.valueOf(string18.charAt(4))));
                                        SharePreferenceManager.getInstance().setSensorViewDisplay(str, Integer.parseInt(String.valueOf(string18.charAt(5))));
                                        SharePreferenceManager.getInstance().setAlarmLightSwitchDisplay(str, Integer.parseInt(String.valueOf(string18.charAt(6))));
                                        SharePreferenceManager.getInstance().setLowPower(str, Integer.parseInt(String.valueOf(string18.charAt(10))));
                                        SharePreferenceManager.getInstance().setDoubleNetWork(str, Integer.parseInt(String.valueOf(string18.charAt(11))));
                                        SharePreferenceManager.getInstance().setPwmCtrl(str, Integer.parseInt(String.valueOf(string18.charAt(12))));
                                        SharePreferenceManager.getInstance().setStrongReminder(str, Integer.parseInt(String.valueOf(string18.charAt(13))));
                                        SharePreferenceManager.getInstance().setTFCardInfo(str, Integer.parseInt(String.valueOf(string18.charAt(14))));
                                        SharePreferenceManager.getInstance().setVoicePromptMask(str, Integer.parseInt(String.valueOf(string18.charAt(16))));
                                        SharePreferenceManager.getInstance().setRecordQualityAbility(str, Integer.parseInt(String.valueOf(string18.charAt(17))));
                                        SharePreferenceManager.getInstance().setAPNAbility(str, Integer.parseInt(String.valueOf(string18.charAt(18))));
                                        SharePreferenceManager.getInstance().setNet4GAbility(str, Integer.parseInt(String.valueOf(string18.charAt(19))));
                                        SharePreferenceManager.getInstance().setCustomerServiceShow(str, Integer.parseInt(String.valueOf(string18.charAt(21))));
                                        SharePreferenceManager.getInstance().setEventRecord(str, Integer.parseInt(String.valueOf(string18.charAt(22))));
                                        SharePreferenceManager.getInstance().setNatETHSwitchShow(str, Integer.parseInt(String.valueOf(string18.charAt(23))));
                                        SharePreferenceManager.getInstance().setNatWLANSwitchShow(str, Integer.parseInt(String.valueOf(string18.charAt(24))));
                                        SharePreferenceManager.getInstance().setFloodlightSwitchShow(str, Integer.parseInt(String.valueOf(string18.charAt(25))));
                                        SharePreferenceManager.getInstance().setLowPowerPIR(str, Integer.parseInt(String.valueOf(string18.charAt(26))));
                                        SharePreferenceManager.getInstance().setFakeDualShow(str, Integer.parseInt(String.valueOf(string18.charAt(27))));
                                        SharePreferenceManager.getInstance().setPowerModeShow(str, Integer.parseInt(String.valueOf(string18.charAt(28))));
                                        SharePreferenceManager.getInstance().setMapShow(str, Integer.parseInt(String.valueOf(string18.charAt(29))));
                                        SharePreferenceManager.getInstance().setExpHighLightShow(str, Integer.parseInt(String.valueOf(string18.charAt(30))));
                                        SharePreferenceManager.getInstance().setEventSearch(str, Integer.parseInt(String.valueOf(string18.charAt(31))));
                                    } catch (Exception unused) {
                                    }
                                    SharePreferenceManager.getInstance().setPageControlEx(str, iIntValue41);
                                }
                            }
                            if (jSONObject.containsKey(Constants.PageControlEx2)) {
                                JSONObject jSONObject47 = jSONObject.getJSONObject(Constants.PageControlEx2);
                                if (jSONObject47.containsKey("value")) {
                                    int iIntValue42 = jSONObject47.getInteger("value").intValue();
                                    SharePreferenceManager.getInstance().setIsRouter(str, 0);
                                    try {
                                        SharePreferenceManager.getInstance().setIsRouter(str, Integer.parseInt(String.valueOf(new StringBuilder(Integer.toBinaryString(iIntValue42)).reverse().toString().charAt(2))));
                                    } catch (Exception unused2) {
                                    }
                                    SharePreferenceManager.getInstance().setPageControlEx2(str, iIntValue42);
                                }
                            }
                            if (jSONObject.containsKey(Constants.Net4GEnableSwitch)) {
                                JSONObject jSONObject48 = jSONObject.getJSONObject(Constants.Net4GEnableSwitch);
                                if (jSONObject48.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setNet4GEnableSwitch(str, jSONObject48.getInteger("value").intValue());
                                }
                            }
                            if (jSONObject.containsKey(Constants.AlarmSchedule)) {
                                JSONObject jSONObject49 = jSONObject.getJSONObject(Constants.AlarmSchedule);
                                if (jSONObject49.containsKey("value")) {
                                    JSONArray jSONArray = jSONObject49.getJSONArray("value");
                                    Gson gson = new Gson();
                                    AlarmPlanBean alarmPlanBean = new AlarmPlanBean();
                                    for (int i2 = 0; i2 < jSONArray.size(); i2++) {
                                        alarmPlanBean.getList().add((AlarmPlanBean.bean) gson.fromJson(gson.toJson(jSONArray.get(i2)), AlarmPlanBean.bean.class));
                                    }
                                    SharePreferenceManager.getInstance().setAlarmPlanJson(str, gson.toJson(alarmPlanBean));
                                }
                            }
                            if (jSONObject.containsKey(Constants.FloodlightScheduleEnable)) {
                                JSONObject jSONObject50 = jSONObject.getJSONObject(Constants.FloodlightScheduleEnable);
                                if (jSONObject50.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setFloodlightScheduleEnable(str, jSONObject50.getInteger("value").intValue());
                                }
                            }
                            if (jSONObject.containsKey(Constants.FloodlightSchedule)) {
                                JSONObject jSONObject51 = jSONObject.getJSONObject(Constants.FloodlightSchedule);
                                if (jSONObject51.containsKey("value")) {
                                    JSONArray jSONArray2 = jSONObject51.getJSONArray("value");
                                    Gson gson2 = new Gson();
                                    AlarmPlanBean alarmPlanBean2 = new AlarmPlanBean();
                                    for (int i3 = 0; i3 < jSONArray2.size(); i3++) {
                                        alarmPlanBean2.getList().add((AlarmPlanBean.bean) gson2.fromJson(gson2.toJson(jSONArray2.get(i3)), AlarmPlanBean.bean.class));
                                    }
                                    SharePreferenceManager.getInstance().setFloodlightScheduleJson(str, gson2.toJson(alarmPlanBean2));
                                }
                            }
                            if (jSONObject.containsKey(Constants.Net4GMode)) {
                                JSONObject jSONObject52 = jSONObject.getJSONObject(Constants.Net4GMode);
                                if (jSONObject52.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setNet4GMode(str, jSONObject52.getIntValue("value"));
                                }
                            }
                            if (jSONObject.containsKey(Constants.AlarmSchedule)) {
                                jSONObject.getJSONObject(Constants.AlarmSchedule).containsKey("value");
                            }
                            if (jSONObject.containsKey(Constants.WifiConfigIsExist)) {
                                JSONObject jSONObject53 = jSONObject.getJSONObject(Constants.WifiConfigIsExist);
                                if (jSONObject53.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setWifiConfigIsExist(str, jSONObject53.getIntValue("value"));
                                }
                            }
                            try {
                                if (jSONObject.containsKey(Constants.IvpAbility)) {
                                    JSONObject jSONObject54 = jSONObject.getJSONObject(Constants.IvpAbility);
                                    if (jSONObject54.containsKey("value")) {
                                        int iIntValue43 = jSONObject54.getInteger("value").intValue();
                                        int i4 = iIntValue43 & 1;
                                        if (SharePreferenceManager.getInstance().getHumanoidTracking(str).intValue() != i4) {
                                            SharePreferenceManager.getInstance().setHumanoidTracking(str, i4);
                                        }
                                        int i5 = (iIntValue43 & 16) >> 4;
                                        if (SharePreferenceManager.getInstance().getCrossLine(str).intValue() != i5) {
                                            SharePreferenceManager.getInstance().setCrossLine(str, i5);
                                        }
                                        int i6 = (iIntValue43 & 32) >> 5;
                                        if (SharePreferenceManager.getInstance().getAreaDetect(str).intValue() != i6) {
                                            SharePreferenceManager.getInstance().setAreaDetect(str, i6);
                                        }
                                        int i7 = (iIntValue43 & 64) >> 6;
                                        if (SharePreferenceManager.getInstance().getTlrClRgn(str).intValue() != i7) {
                                            SharePreferenceManager.getInstance().setTlrClRgn(str, i7);
                                        }
                                        int i8 = (iIntValue43 & 32768) >> 15;
                                        if (SharePreferenceManager.getInstance().getTFStorageIVP(str).intValue() != i8) {
                                            SharePreferenceManager.getInstance().setTFStorageIVP(str, i8);
                                        }
                                    }
                                }
                                if (jSONObject.containsKey(Constants.IvpExSwitch)) {
                                    JSONObject jSONObject55 = jSONObject.getJSONObject(Constants.IvpExSwitch);
                                    if (jSONObject55.containsKey("value")) {
                                        SharePreferenceManager.getInstance().setHumanoidTrackingEnable(str, 0);
                                        SharePreferenceManager.getInstance().setCrossLineEnable(str, 0);
                                        SharePreferenceManager.getInstance().setAreaDetectEnable(str, 0);
                                        String string19 = new StringBuilder(Integer.toBinaryString(jSONObject55.getInteger("value").intValue())).reverse().toString();
                                        SharePreferenceManager.getInstance().setHumanoidTrackingEnable(str, Integer.parseInt(String.valueOf(string19.charAt(0))));
                                        SharePreferenceManager.getInstance().setCrossLineEnable(str, Integer.parseInt(String.valueOf(string19.charAt(1))));
                                        SharePreferenceManager.getInstance().setAreaDetectEnable(str, Integer.parseInt(String.valueOf(string19.charAt(2))));
                                    }
                                }
                            } catch (Exception unused3) {
                            }
                            if (jSONObject.containsKey(Constants.RegionDetect)) {
                                JSONObject jSONObject56 = jSONObject.getJSONObject(Constants.RegionDetect);
                                if (jSONObject56.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setRegionDetectPoint(str, jSONObject56.get("value").toString());
                                }
                            }
                            if (jSONObject.containsKey(Constants.AlarmLightSwitch)) {
                                JSONObject jSONObject57 = jSONObject.getJSONObject(Constants.AlarmLightSwitch);
                                if (jSONObject57.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setAlarmLightSwitch(str, String.valueOf(jSONObject57.getIntValue("value")));
                                }
                            }
                            if (jSONObject.containsKey(Constants.RebootSchedule)) {
                                JSONObject jSONObject58 = jSONObject.getJSONObject(Constants.RebootSchedule);
                                if (jSONObject58.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setRebootSchedule(str, jSONObject58.get("value").toString());
                                }
                            }
                            if (jSONObject.containsKey(Constants.IMEI)) {
                                JSONObject jSONObject59 = jSONObject.getJSONObject(Constants.IMEI);
                                if (jSONObject59.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setImei(str, jSONObject59.get("value").toString());
                                }
                            }
                            if (jSONObject.containsKey(Constants.Net4gVersion)) {
                                JSONObject jSONObject60 = jSONObject.getJSONObject(Constants.Net4gVersion);
                                if (jSONObject60.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setNet4gVersion(str, jSONObject60.get("value").toString());
                                }
                            }
                            if (jSONObject.containsKey(Constants.UserMallUrl)) {
                                JSONObject jSONObject61 = jSONObject.getJSONObject(Constants.UserMallUrl);
                                if (jSONObject61.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setUserMallUrl(str, jSONObject61.get("value").toString());
                                }
                            }
                            if (jSONObject.containsKey(Constants.Cross_Line_Detect)) {
                                JSONObject jSONObject62 = jSONObject.getJSONObject(Constants.Cross_Line_Detect);
                                if (jSONObject62.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setCrossLineDetect(str, jSONObject62.getString("value"));
                                }
                            }
                            if (jSONObject.containsKey(Constants.LowPowerMode)) {
                                JSONObject jSONObject63 = jSONObject.getJSONObject(Constants.LowPowerMode);
                                if (jSONObject63.containsKey("value")) {
                                    String string20 = jSONObject63.get("value").toString();
                                    if (!string20.equals(SharePreferenceManager.getInstance().getLowPowerMode(str))) {
                                        SharePreferenceManager.getInstance().setLowPowerMode(str, string20);
                                    }
                                    JSONObject jSONObject64 = jSONObject63.getJSONObject("value");
                                    if (jSONObject64.containsKey(Constants.LowPowerStatus) && (iIntValue27 = jSONObject64.getInteger(Constants.LowPowerStatus).intValue()) != SharePreferenceManager.getInstance().getLowPowerStatus(str)) {
                                        SharePreferenceManager.getInstance().setLowPowerStatus(str, iIntValue27);
                                    }
                                    if (jSONObject64.containsKey(Constants.WakeUpData)) {
                                        String string21 = jSONObject64.getString(Constants.WakeUpData);
                                        if (!string21.equals(SharePreferenceManager.getInstance().getWakeUpData(str))) {
                                            SharePreferenceManager.getInstance().setWakeUpData(str, string21);
                                        }
                                    }
                                }
                            }
                            if (jSONObject.containsKey(Constants.LowPowerSwitch) && (iIntValue26 = jSONObject.getJSONObject(Constants.LowPowerSwitch).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getLowPowerSwitch(str)) {
                                SharePreferenceManager.getInstance().setLowPowerSwitch(str, iIntValue26);
                            }
                            if (jSONObject.containsKey(Constants.LowPowerWorkMode) && (iIntValue25 = jSONObject.getJSONObject(Constants.LowPowerWorkMode).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getLowPowerWorkMode(str)) {
                                SharePreferenceManager.getInstance().setLowPowerWorkMode(str, iIntValue25);
                            }
                            if (jSONObject.containsKey(Constants.WhiteLightBrightness) && (iIntValue24 = jSONObject.getJSONObject(Constants.WhiteLightBrightness).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getWhiteLightBrightness(str)) {
                                SharePreferenceManager.getInstance().setWhiteLightBrightness(str, iIntValue24);
                            }
                            if (jSONObject.containsKey(Constants.IRLightBrightness) && (iIntValue23 = jSONObject.getJSONObject(Constants.IRLightBrightness).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getIRLightBrightness(str)) {
                                SharePreferenceManager.getInstance().setIRLightBrightness(str, iIntValue23);
                            }
                            if (jSONObject.containsKey(Constants.StrongReminderSwitch) && (iIntValue22 = jSONObject.getJSONObject(Constants.StrongReminderSwitch).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getStrongReminderSwitch(str)) {
                                SharePreferenceManager.getInstance().setStrongReminderSwitch(str, iIntValue22);
                            }
                            if (jSONObject.containsKey(Constants.StorageRecordQuality) && (iIntValue21 = jSONObject.getJSONObject(Constants.StorageRecordQuality).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getRecordQuality(str)) {
                                SharePreferenceManager.getInstance().setRecordQuality(str, iIntValue21);
                            }
                            if (jSONObject.containsKey(Constants.APNConfig)) {
                                JSONObject jSONObject65 = jSONObject.getJSONObject(Constants.APNConfig);
                                if (jSONObject65.containsKey("value")) {
                                    JSONArray jSONArray3 = jSONObject65.getJSONArray("value");
                                    Gson gson3 = new Gson();
                                    APNBean aPNBean = new APNBean();
                                    for (int i9 = 0; i9 < jSONArray3.size(); i9++) {
                                        aPNBean.getList().add((APNBean.bean) gson3.fromJson(gson3.toJson(jSONArray3.get(i9)), APNBean.bean.class));
                                    }
                                    SharePreferenceManager.getInstance().setAPNConfig(str, gson3.toJson(aPNBean));
                                }
                            }
                            if (jSONObject.containsKey(Constants.Net4GPlans)) {
                                JSONObject jSONObject66 = jSONObject.getJSONObject(Constants.Net4GPlans);
                                if (jSONObject66.containsKey("value")) {
                                    JSONObject jSONObject67 = jSONObject66.getJSONObject("value");
                                    if (jSONObject67.containsKey(Constants.URL)) {
                                        String string22 = jSONObject67.getString(Constants.URL);
                                        if (!string22.equals(SharePreferenceManager.getInstance().getURL(str))) {
                                            SharePreferenceManager.getInstance().setURL(str, string22);
                                        }
                                    }
                                    if (jSONObject67.containsKey(Constants.VendorID) && (intValue2 = jSONObject67.getIntValue(Constants.VendorID)) != SharePreferenceManager.getInstance().getVendorID(str)) {
                                        SharePreferenceManager.getInstance().setVendorID(str, intValue2);
                                    }
                                    if (jSONObject67.containsKey(Constants.Enable) && (intValue = jSONObject67.getIntValue(Constants.Enable)) != SharePreferenceManager.getInstance().getEnable(str)) {
                                        SharePreferenceManager.getInstance().setEnable(str, intValue);
                                    }
                                    if (jSONObject67.containsKey(Constants.URLSlave)) {
                                        String string23 = jSONObject67.getString(Constants.URLSlave);
                                        if (!string23.equals(SharePreferenceManager.getInstance().getURLSlave(str))) {
                                            SharePreferenceManager.getInstance().setURLSlave(str, string23);
                                        }
                                    }
                                }
                            }
                            if (jSONObject.containsKey(Constants.HideSIMPlans) && (iIntValue20 = jSONObject.getJSONObject(Constants.HideSIMPlans).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getHideSIMPlans(str)) {
                                SharePreferenceManager.getInstance().setHideSIMPlans(str, iIntValue20);
                            }
                            if (jSONObject.containsKey(Constants.ChannelList)) {
                                String string24 = jSONObject.getJSONObject(Constants.ChannelList).getString("value");
                                if (!string24.equals(SharePreferenceManager.getInstance().getChannelList(str))) {
                                    SharePreferenceManager.getInstance().setChannelList(str, string24);
                                }
                            }
                            if (jSONObject.containsKey(Constants.ChannelNumber) && (iIntValue19 = jSONObject.getJSONObject(Constants.ChannelNumber).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getChannelNumber(str)) {
                                SharePreferenceManager.getInstance().setChannelNumber(str, iIntValue19);
                            }
                            if (jSONObject.containsKey(Constants.DSTSwitch) && (iIntValue18 = jSONObject.getJSONObject(Constants.DSTSwitch).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getDSTSwitch(str)) {
                                SharePreferenceManager.getInstance().setDSTSwitch(str, iIntValue18);
                            }
                            if (jSONObject.containsKey(Constants.NetState) && (iIntValue17 = jSONObject.getJSONObject(Constants.NetState).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getNetState(str)) {
                                SharePreferenceManager.getInstance().setNetState(str, iIntValue17);
                            }
                            if (jSONObject.containsKey(Constants.FakeDualEnable) && (iIntValue16 = jSONObject.getJSONObject(Constants.FakeDualEnable).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getFakeDualEnable(str)) {
                                SharePreferenceManager.getInstance().setFakeDualEnable(str, iIntValue16);
                            }
                            if (jSONObject.containsKey(Constants.SmartP) && (iIntValue15 = jSONObject.getJSONObject(Constants.SmartP).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getSmartP(str)) {
                                SharePreferenceManager.getInstance().setSmartP(str, iIntValue15);
                            }
                            if (jSONObject.containsKey(Constants.LowPowerAbility) && (string9 = jSONObject.getJSONObject(Constants.LowPowerAbility).getString("value")) != SharePreferenceManager.getInstance().getLowPowerAbility(str)) {
                                SharePreferenceManager.getInstance().setLowPowerAbility(str, string9);
                            }
                            if (jSONObject.containsKey(Constants.LowPowerDeviceStatus) && (string8 = jSONObject.getJSONObject(Constants.LowPowerDeviceStatus).getString("value")) != SharePreferenceManager.getInstance().getLowPowerDeviceStatus(str)) {
                                SharePreferenceManager.getInstance().setLowPowerDeviceStatus(str, string8);
                            }
                            if (jSONObject.containsKey(Constants.NatETHSwitch) && (iIntValue14 = jSONObject.getJSONObject(Constants.NatETHSwitch).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getNatETHSwitch(str)) {
                                SharePreferenceManager.getInstance().setNatETHSwitch(str, iIntValue14);
                            }
                            if (jSONObject.containsKey(Constants.NatWLANSwitch) && (iIntValue13 = jSONObject.getJSONObject(Constants.NatWLANSwitch).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getNatWLANSwitch(str)) {
                                SharePreferenceManager.getInstance().setNatWLANSwitch(str, iIntValue13);
                            }
                            if (jSONObject.containsKey(Constants.NatAPConfig) && (string7 = jSONObject.getJSONObject(Constants.NatAPConfig).getString("value")) != SharePreferenceManager.getInstance().getNatWLANSwitchConfig(str)) {
                                SharePreferenceManager.getInstance().setNatWLANSwitchConfig(str, string7);
                            }
                            if (jSONObject.containsKey(Constants.Net4GPlansUrl) && (string6 = jSONObject.getJSONObject(Constants.Net4GPlansUrl).getString("value")) != SharePreferenceManager.getInstance().getNet4GPlansUrl(str)) {
                                SharePreferenceManager.getInstance().setNet4GPlansUrl(str, string6);
                            }
                            if (jSONObject.containsKey(Constants.WifiModelInfo) && (string5 = jSONObject.getJSONObject(Constants.WifiModelInfo).getString("value")) != SharePreferenceManager.getInstance().getWifiModelInfo(str)) {
                                SharePreferenceManager.getInstance().setWifiModelInfo(str, string5);
                            }
                            if (jSONObject.containsKey(Constants.FloodlightSwitch) && (iIntValue12 = jSONObject.getJSONObject(Constants.FloodlightSwitch).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getFloodlightSwitch(str)) {
                                SharePreferenceManager.getInstance().setFloodlightSwitch(str, iIntValue12);
                            }
                            if (jSONObject.containsKey(Constants.PTZHide) && (iIntValue11 = jSONObject.getJSONObject(Constants.PTZHide).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getPTZHide(str)) {
                                SharePreferenceManager.getInstance().setPTZHide(str, iIntValue11);
                            }
                            if (jSONObject.containsKey(Constants.TimeRecordEnable) && (iIntValue10 = jSONObject.getJSONObject(Constants.TimeRecordEnable).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getTimeRecordEnable(str)) {
                                SharePreferenceManager.getInstance().setTimeRecordEnable(str, iIntValue10);
                            }
                            if (jSONObject.containsKey(Constants.PowerMode) && (iIntValue9 = jSONObject.getJSONObject(Constants.PowerMode).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getPowerMode(str)) {
                                SharePreferenceManager.getInstance().setPowerMode(str, iIntValue9);
                            }
                            if (jSONObject.containsKey(Constants.LocationAbility) && (iIntValue8 = jSONObject.getJSONObject(Constants.LocationAbility).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getLocationAbility(str)) {
                                SharePreferenceManager.getInstance().setLocationAbility(str, iIntValue8);
                            }
                            if (jSONObject.containsKey(Constants.TimeRecordPlan)) {
                                JSONObject jSONObject68 = jSONObject.getJSONObject(Constants.TimeRecordPlan);
                                if (jSONObject68.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setTimeRecordPlan(str, jSONObject68.getJSONArray("value").toString());
                                }
                            }
                            if (jSONObject.containsKey(Constants.ExpHighLight) && (string4 = jSONObject.getJSONObject(Constants.ExpHighLight).getString("value")) != SharePreferenceManager.getInstance().getExpHighLight(str)) {
                                SharePreferenceManager.getInstance().setExpHighLight(str, string4);
                            }
                            if (jSONObject.containsKey(Constants.NightVisionHide) && (iIntValue7 = jSONObject.getJSONObject(Constants.NightVisionHide).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getNightVisionHide(str)) {
                                SharePreferenceManager.getInstance().setNightVisionHide(str, iIntValue7);
                            }
                            if (jSONObject.containsKey(Constants.LowPowerWorkModeMask) && (iIntValue6 = jSONObject.getJSONObject(Constants.LowPowerWorkModeMask).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getLowPowerWorkModeMask(str)) {
                                SharePreferenceManager.getInstance().setLowPowerWorkModeMask(str, iIntValue6);
                            }
                            if (jSONObject.containsKey(Constants.LowPowerAOVMode4GSwitch)) {
                                JSONObject jSONObject69 = jSONObject.getJSONObject(Constants.LowPowerAOVMode4GSwitch);
                                if (jSONObject69.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setLowPowerAOVMode4GSwitch(str, jSONObject69.getInteger("value").intValue());
                                }
                            }
                            if (jSONObject.containsKey(Constants.NightVisionModeShowCtrl) && (iIntValue5 = jSONObject.getJSONObject(Constants.NightVisionModeShowCtrl).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(str)) {
                                SharePreferenceManager.getInstance().setNightVisionModeShowCtrl(str, iIntValue5);
                            }
                            if (jSONObject.containsKey(Constants.WlanDhcpConf) && (string3 = jSONObject.getJSONObject(Constants.WlanDhcpConf).getString("value")) != SharePreferenceManager.getInstance().getWlanDhcpConf(str)) {
                                SharePreferenceManager.getInstance().setWlanDhcpConf(str, string3);
                            }
                            if (jSONObject.containsKey(Constants.EthernetDhcpConf) && (string2 = jSONObject.getJSONObject(Constants.EthernetDhcpConf).getString("value")) != SharePreferenceManager.getInstance().getEthernetDhcpConf(str)) {
                                SharePreferenceManager.getInstance().setEthernetDhcpConf(str, string2);
                            }
                            if (jSONObject.containsKey(Constants.NatAPSecurityConf) && (iIntValue4 = jSONObject.getJSONObject(Constants.NatAPSecurityConf).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getNatAPSecurityConf(str)) {
                                SharePreferenceManager.getInstance().setNatAPSecurityConf(str, iIntValue4);
                            }
                            if (jSONObject.containsKey(Constants.NatAPConfigEx) && (string = jSONObject.getJSONObject(Constants.NatAPConfigEx).getString("value")) != SharePreferenceManager.getInstance().getNatAPConfigEx(str)) {
                                SharePreferenceManager.getInstance().setNatAPConfigEx(str, string);
                            }
                            if (jSONObject.containsKey(Constants.Net4GVSIMMode) && (iIntValue3 = jSONObject.getJSONObject(Constants.Net4GVSIMMode).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getNet4GVSIMMode(str)) {
                                SharePreferenceManager.getInstance().setNet4GVSIMMode(str, iIntValue3);
                            }
                            if (jSONObject.containsKey(Constants.TandemVuNightVisionHide) && (iIntValue2 = jSONObject.getJSONObject(Constants.TandemVuNightVisionHide).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getTandemVuNightVisionHide(str)) {
                                SharePreferenceManager.getInstance().setTandemVuNightVisionHide(str, iIntValue2);
                            }
                            if (jSONObject.containsKey(Constants.IvpZoomEnable) && (iIntValue = jSONObject.getJSONObject(Constants.IvpZoomEnable).getInteger("value").intValue()) != SharePreferenceManager.getInstance().getIvpZoomEnable(str)) {
                                SharePreferenceManager.getInstance().setIvpZoomEnable(str, iIntValue);
                            }
                            if (jSONObject.containsKey(Constants.StreamChnMaxEzoom)) {
                                JSONObject jSONObject70 = jSONObject.getJSONObject(Constants.StreamChnMaxEzoom);
                                if (jSONObject70.containsKey("value")) {
                                    SharePreferenceManager.getInstance().setStreamChnMaxEzoom(str, jSONObject70.getJSONArray("value").toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                myCallback.onComplete(true);
            }
        });
    }

    public void updateSettings(final String str, Map<String, Object> map, final ISetCallback iSetCallback) {
        IPCManager.getInstance().getDevice(str).setProperties(map, new IPanelCallback() { // from class: tools.SettingsCtrl.4
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        ISetCallback iSetCallback2 = iSetCallback;
                        if (iSetCallback2 != null) {
                            iSetCallback2.onFailed();
                            return;
                        }
                        return;
                    }
                    SettingsCtrl.this.getProperties(str, new MyCallback() { // from class: tools.SettingsCtrl.4.1
                        @Override // tools.MyCallback
                        public void onComplete(boolean z2) {
                        }
                    });
                    ISetCallback iSetCallback3 = iSetCallback;
                    if (iSetCallback3 != null) {
                        iSetCallback3.onSucceed();
                    }
                }
            }
        });
    }

    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
