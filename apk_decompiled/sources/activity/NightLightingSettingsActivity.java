package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import bean.DeviceInfoBean;
import bean.ExpHighLight;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityNightLightingSettingsBinding;
import config.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sdk.IPCManager;
import tools.MyCallback;
import tools.OnMultiClickListener;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.SelectorDialogFragment;

/* JADX INFO: loaded from: classes.dex */
public class NightLightingSettingsActivity extends CommonActivity {
    private ActivityNightLightingSettingsBinding binding;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean device2;
    private ExpHighLight expHighLight;
    private String[] infrarredMode;
    private String iotId;
    private boolean isExpHighLight;
    private boolean isFloodlight;
    private boolean isFloodlightTime;
    private SelectorDialogFragment nightModeFragment;
    private DeviceInfoBean nvrDevice;
    private SwitchCompat swExpHighLight;
    private SwitchCompat swFloodlight;
    private SwitchCompat swFloodlightTimeSwitch;
    private List<String> nightModelList = new ArrayList();
    private Handler uiHandler = new Handler();

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_night_lighting_settings;
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        SettingsCtrl.getInstance().getProperties(this.device.getIotId(), new MyCallback() { // from class: activity.NightLightingSettingsActivity.1
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                NightLightingSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.NightLightingSettingsActivity.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        NightLightingSettingsActivity.this.binding.itemNightMode.setRightText(NightLightingSettingsActivity.this.infrarredMode[SharePreferenceManager.getInstance().getDayNightMode(NightLightingSettingsActivity.this.device.getIotId())]);
                        if (SharePreferenceManager.getInstance().getPwmCtrl(NightLightingSettingsActivity.this.device.getIotId()) == 0) {
                            NightLightingSettingsActivity.this.binding.lightSettingPtz.setVisibility(8);
                            NightLightingSettingsActivity.this.binding.infraredLightSettingPtz.setVisibility(8);
                        } else {
                            NightLightingSettingsActivity.this.binding.lightSettingPtz.setVisibility(0);
                            NightLightingSettingsActivity.this.binding.infraredLightSettingPtz.setVisibility(0);
                            NightLightingSettingsActivity.this.binding.lightProgressSettingPtz.setProgress(SharePreferenceManager.getInstance().getWhiteLightBrightness(NightLightingSettingsActivity.this.device.getIotId()));
                            NightLightingSettingsActivity.this.binding.infraredLightProgressSettingPtz.setProgress(SharePreferenceManager.getInstance().getIRLightBrightness(NightLightingSettingsActivity.this.device.getIotId()));
                        }
                        if (SharePreferenceManager.getInstance().getFloodlightSwitchShow(NightLightingSettingsActivity.this.device.getIotId()) == 1) {
                            NightLightingSettingsActivity.this.binding.layoutFloodlight.setVisibility(0);
                            NightLightingSettingsActivity.this.isFloodlight = SharePreferenceManager.getInstance().getFloodlightSwitch(NightLightingSettingsActivity.this.device.getIotId()) == 1;
                            NightLightingSettingsActivity.this.swFloodlight.setChecked(NightLightingSettingsActivity.this.isFloodlight);
                            NightLightingSettingsActivity.this.isFloodlightTime = SharePreferenceManager.getInstance().getFloodlightScheduleEnable(NightLightingSettingsActivity.this.device.getIotId()) == 1;
                            NightLightingSettingsActivity.this.swFloodlightTimeSwitch.setChecked(NightLightingSettingsActivity.this.isFloodlightTime);
                        } else {
                            NightLightingSettingsActivity.this.binding.layoutFloodlight.setVisibility(8);
                        }
                        if (SharePreferenceManager.getInstance().getExpHighLightShow(NightLightingSettingsActivity.this.device.getIotId()) == 1) {
                            NightLightingSettingsActivity.this.binding.layoutExpHighLight.setVisibility(0);
                            NightLightingSettingsActivity.this.expHighLight = (ExpHighLight) JSONObject.parseObject(SharePreferenceManager.getInstance().getExpHighLight(NightLightingSettingsActivity.this.iotId), ExpHighLight.class);
                            if (NightLightingSettingsActivity.this.expHighLight != null) {
                                Log.e("强光抑制" + NightLightingSettingsActivity.this.expHighLight.Enable, "" + NightLightingSettingsActivity.this.expHighLight.Level);
                                NightLightingSettingsActivity.this.isExpHighLight = NightLightingSettingsActivity.this.expHighLight.Enable == 1;
                                NightLightingSettingsActivity.this.swExpHighLight.setChecked(NightLightingSettingsActivity.this.isExpHighLight);
                                NightLightingSettingsActivity.this.binding.layoutExpHighLightLevel.setVisibility(NightLightingSettingsActivity.this.expHighLight.Enable == 1 ? 0 : 8);
                                NightLightingSettingsActivity.this.binding.itExpHighLight.setLineShow(NightLightingSettingsActivity.this.expHighLight.Enable == 1);
                                NightLightingSettingsActivity.this.binding.seekBarExpHighLightLevel.setProgress(NightLightingSettingsActivity.this.expHighLight.Level);
                            }
                        }
                        if (SharePreferenceManager.getInstance().getNightVisionHide(NightLightingSettingsActivity.this.device.getIotId()) == 1) {
                            NightLightingSettingsActivity.this.binding.itemNightMode.setVisibility(8);
                            NightLightingSettingsActivity.this.binding.Safety.setVisibility(8);
                        }
                        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(NightLightingSettingsActivity.this.device.getIotId()) == 0) {
                            NightLightingSettingsActivity.this.binding.itemNightMode.setVisibility(8);
                            NightLightingSettingsActivity.this.binding.Safety.setVisibility(8);
                            NightLightingSettingsActivity.this.binding.itemNightModeGun.setVisibility(8);
                            NightLightingSettingsActivity.this.binding.SafetyGun.setVisibility(8);
                        }
                        if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(NightLightingSettingsActivity.this.device.getIotId()) == 1) {
                            NightLightingSettingsActivity.this.binding.itemNightModeGun.setVisibility(8);
                            NightLightingSettingsActivity.this.binding.SafetyGun.setVisibility(8);
                        }
                    }
                });
            }
        });
        if (this.device1 != null) {
            SettingsCtrl.getInstance().getProperties(this.device1.getIotId(), new MyCallback() { // from class: activity.NightLightingSettingsActivity.2
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    NightLightingSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.NightLightingSettingsActivity.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            NightLightingSettingsActivity.this.binding.itemNightModeGun.setRightText(NightLightingSettingsActivity.this.infrarredMode[SharePreferenceManager.getInstance().getDayNightMode(NightLightingSettingsActivity.this.device1.getIotId())]);
                            NightLightingSettingsActivity.this.binding.layoutGun.setVisibility(0);
                            NightLightingSettingsActivity.this.binding.Safety.setVisibility(0);
                            if (SharePreferenceManager.getInstance().getPwmCtrl(NightLightingSettingsActivity.this.device1.getIotId()) == 0) {
                                NightLightingSettingsActivity.this.binding.lightSettingGun.setVisibility(8);
                                NightLightingSettingsActivity.this.binding.infraredLightSettingGun.setVisibility(8);
                            } else {
                                NightLightingSettingsActivity.this.binding.lightSettingGun.setVisibility(0);
                                NightLightingSettingsActivity.this.binding.infraredLightSettingGun.setVisibility(0);
                                NightLightingSettingsActivity.this.binding.lightProgressSettingGun.setProgress(SharePreferenceManager.getInstance().getWhiteLightBrightness(NightLightingSettingsActivity.this.device1.getIotId()));
                                NightLightingSettingsActivity.this.binding.infraredLightProgressSettingGun.setProgress(SharePreferenceManager.getInstance().getIRLightBrightness(NightLightingSettingsActivity.this.device1.getIotId()));
                            }
                            if (SharePreferenceManager.getInstance().getNightVisionHide(NightLightingSettingsActivity.this.device1.getIotId()) == 1) {
                                NightLightingSettingsActivity.this.binding.itemNightModeGun.setVisibility(8);
                                NightLightingSettingsActivity.this.binding.SafetyGun.setVisibility(8);
                            }
                            if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(NightLightingSettingsActivity.this.device1.getIotId()) == 0) {
                                NightLightingSettingsActivity.this.binding.itemNightModeGun.setVisibility(8);
                                NightLightingSettingsActivity.this.binding.SafetyGun.setVisibility(8);
                            }
                        }
                    });
                }
            });
        }
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.device2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
            this.iotId = this.device.getIotId();
        }
        this.binding = (ActivityNightLightingSettingsBinding) DataBindingUtil.setContentView(this, R.layout.activity_night_lighting_settings);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.NightLightingSettingsActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                NightLightingSettingsActivity.this.finish();
            }
        });
        this.infrarredMode = getResources().getStringArray(R.array.InfrarredMode);
        this.nightModelList.clear();
        int i = 0;
        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.iotId) == -1) {
            while (true) {
                String[] strArr = this.infrarredMode;
                if (i >= strArr.length) {
                    break;
                }
                this.nightModelList.add(strArr[i]);
                i++;
            }
        } else {
            StringBuilder sbReverse = new StringBuilder(Integer.toBinaryString(SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.iotId))).reverse();
            for (int i2 = 0; i2 < sbReverse.length(); i2++) {
                if (sbReverse.charAt(i2) - '0' == 1) {
                    if (i2 == 0) {
                        this.nightModelList.add(this.infrarredMode[2]);
                    }
                    if (i2 == 1) {
                        this.nightModelList.add(this.infrarredMode[0]);
                    }
                    if (i2 == 2) {
                        this.nightModelList.add(this.infrarredMode[1]);
                    }
                }
            }
        }
        this.nightModeFragment = new SelectorDialogFragment(getString(R.string.night_mode), this.nightModelList);
        this.nightModeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.NightLightingSettingsActivity.4
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v10, types: [int] */
            /* JADX WARN: Type inference failed for: r0v12 */
            /* JADX WARN: Type inference failed for: r0v13 */
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i3) {
                ((String) NightLightingSettingsActivity.this.nightModelList.get(i3)).equals(NightLightingSettingsActivity.this.infrarredMode[0]);
                ?? Equals = ((String) NightLightingSettingsActivity.this.nightModelList.get(i3)).equals(NightLightingSettingsActivity.this.infrarredMode[1]);
                if (((String) NightLightingSettingsActivity.this.nightModelList.get(i3)).equals(NightLightingSettingsActivity.this.infrarredMode[2])) {
                    Equals = 2;
                }
                NightLightingSettingsActivity nightLightingSettingsActivity = NightLightingSettingsActivity.this;
                nightLightingSettingsActivity.updateNightMode(nightLightingSettingsActivity.getString(R.string.day_night_mode_key), Integer.valueOf((int) Equals));
            }
        });
        this.binding.itemNightMode.setOnClickListener(new OnMultiClickListener() { // from class: activity.NightLightingSettingsActivity.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                NightLightingSettingsActivity nightLightingSettingsActivity = NightLightingSettingsActivity.this;
                nightLightingSettingsActivity.iotId = nightLightingSettingsActivity.device.getIotId();
                int i3 = 0;
                for (int i4 = 0; i4 < NightLightingSettingsActivity.this.nightModelList.size(); i4++) {
                    if (((String) NightLightingSettingsActivity.this.nightModelList.get(i4)).equals(NightLightingSettingsActivity.this.infrarredMode[SharePreferenceManager.getInstance().getDayNightMode(NightLightingSettingsActivity.this.device.getIotId())])) {
                        i3 = i4;
                    }
                }
                NightLightingSettingsActivity.this.nightModeFragment.showAllowingStateLoss(NightLightingSettingsActivity.this.getSupportFragmentManager(), "", i3);
            }
        });
        this.binding.lightProgressSettingPtz.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: activity.NightLightingSettingsActivity.6
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(NightLightingSettingsActivity.this.TAG, "onStopTrackingTouch: " + seekBar.getProgress());
                NightLightingSettingsActivity.this.setWhiteLightBrightness(seekBar.getProgress(), NightLightingSettingsActivity.this.device.getIotId());
            }
        });
        this.binding.infraredLightProgressSettingPtz.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: activity.NightLightingSettingsActivity.7
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(NightLightingSettingsActivity.this.TAG, "onStopTrackingTouch: " + seekBar.getProgress());
                NightLightingSettingsActivity.this.setIRLightBrightness(seekBar.getProgress(), NightLightingSettingsActivity.this.device.getIotId());
            }
        });
        this.binding.itemNightModeGun.setOnClickListener(new OnMultiClickListener() { // from class: activity.NightLightingSettingsActivity.8
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                NightLightingSettingsActivity nightLightingSettingsActivity = NightLightingSettingsActivity.this;
                nightLightingSettingsActivity.iotId = nightLightingSettingsActivity.device1.getIotId();
                int i3 = 0;
                for (int i4 = 0; i4 < NightLightingSettingsActivity.this.nightModelList.size(); i4++) {
                    if (((String) NightLightingSettingsActivity.this.nightModelList.get(i4)).equals(NightLightingSettingsActivity.this.infrarredMode[SharePreferenceManager.getInstance().getDayNightMode(NightLightingSettingsActivity.this.device1.getIotId())])) {
                        i3 = i4;
                    }
                }
                NightLightingSettingsActivity.this.nightModeFragment.showAllowingStateLoss(NightLightingSettingsActivity.this.getSupportFragmentManager(), "", i3);
            }
        });
        this.binding.lightProgressSettingGun.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: activity.NightLightingSettingsActivity.9
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(NightLightingSettingsActivity.this.TAG, "onStopTrackingTouch: " + seekBar.getProgress());
                NightLightingSettingsActivity.this.setWhiteLightBrightness(seekBar.getProgress(), NightLightingSettingsActivity.this.device1.getIotId());
            }
        });
        this.binding.infraredLightProgressSettingGun.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: activity.NightLightingSettingsActivity.10
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(NightLightingSettingsActivity.this.TAG, "onStopTrackingTouch: " + seekBar.getProgress());
                NightLightingSettingsActivity.this.setIRLightBrightness(seekBar.getProgress(), NightLightingSettingsActivity.this.device1.getIotId());
            }
        });
        this.binding.itFloodlightSwitch.addRightView(new SwitchCompat(this));
        this.swFloodlight = (SwitchCompat) this.binding.itFloodlightSwitch.getRightView();
        this.swFloodlight.setTextOff("");
        this.swFloodlight.setTextOn("");
        this.swFloodlight.setText("");
        this.swFloodlight.setThumbDrawable(null);
        this.swFloodlight.setBackgroundResource(R.drawable.sel_switch);
        this.swFloodlight.setOnClickListener(new AnonymousClass11());
        this.binding.itFloodlightTimeSwitch.addRightView(new SwitchCompat(this));
        this.swFloodlightTimeSwitch = (SwitchCompat) this.binding.itFloodlightTimeSwitch.getRightView();
        this.swFloodlightTimeSwitch.setTextOff("");
        this.swFloodlightTimeSwitch.setTextOn("");
        this.swFloodlightTimeSwitch.setText("");
        this.swFloodlightTimeSwitch.setThumbDrawable(null);
        this.swFloodlightTimeSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.swFloodlightTimeSwitch.setOnClickListener(new AnonymousClass12());
        this.binding.itFloodlightTimeSetting.setOnClickListener(new OnMultiClickListener() { // from class: activity.NightLightingSettingsActivity.13
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(NightLightingSettingsActivity.this.getActivity(), (Class<?>) FloodlightTimeActivity.class);
                intent.putExtra("iotId", NightLightingSettingsActivity.this.device.getIotId());
                NightLightingSettingsActivity.this.startActivity(intent);
            }
        });
        this.binding.itExpHighLight.addRightView(new SwitchCompat(this));
        this.swExpHighLight = (SwitchCompat) this.binding.itExpHighLight.getRightView();
        this.swExpHighLight.setTextOff("");
        this.swExpHighLight.setTextOn("");
        this.swExpHighLight.setText("");
        this.swExpHighLight.setThumbDrawable(null);
        this.swExpHighLight.setBackgroundResource(R.drawable.sel_switch);
        this.swExpHighLight.setOnClickListener(new OnMultiClickListener() { // from class: activity.NightLightingSettingsActivity.14
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                NightLightingSettingsActivity.this.isExpHighLight = !r2.isExpHighLight;
                if (NightLightingSettingsActivity.this.expHighLight != null) {
                    NightLightingSettingsActivity.this.expHighLight.Enable = NightLightingSettingsActivity.this.isExpHighLight ? 1 : 0;
                    NightLightingSettingsActivity nightLightingSettingsActivity = NightLightingSettingsActivity.this;
                    nightLightingSettingsActivity.setExpHighLight(nightLightingSettingsActivity.expHighLight);
                }
            }
        });
        this.binding.seekBarExpHighLightLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: activity.NightLightingSettingsActivity.15
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(NightLightingSettingsActivity.this.TAG, "onStopTrackingTouch: " + seekBar.getProgress());
                if (NightLightingSettingsActivity.this.expHighLight != null) {
                    NightLightingSettingsActivity.this.expHighLight.Level = seekBar.getProgress();
                    NightLightingSettingsActivity nightLightingSettingsActivity = NightLightingSettingsActivity.this;
                    nightLightingSettingsActivity.setExpHighLight(nightLightingSettingsActivity.expHighLight);
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.NightLightingSettingsActivity$11, reason: invalid class name */
    class AnonymousClass11 extends OnMultiClickListener {
        AnonymousClass11() {
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            if (NightLightingSettingsActivity.this.isFloodlightTime) {
                HashMap map = new HashMap();
                map.put(Constants.FloodlightScheduleEnable, Integer.valueOf(!NightLightingSettingsActivity.this.isFloodlightTime ? 1 : 0));
                IPCManager.getInstance().getDevice(NightLightingSettingsActivity.this.device.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.NightLightingSettingsActivity.11.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(final boolean z, @Nullable final Object obj) {
                        new Handler().post(new Runnable() { // from class: activity.NightLightingSettingsActivity.11.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Object obj2;
                                if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                    return;
                                }
                                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                if (object.containsKey("code") && object.getInteger("code").intValue() == 200) {
                                    NightLightingSettingsActivity.this.isFloodlightTime = !NightLightingSettingsActivity.this.isFloodlightTime;
                                    NightLightingSettingsActivity.this.swFloodlightTimeSwitch.setChecked(NightLightingSettingsActivity.this.isFloodlightTime);
                                    SharePreferenceManager.getInstance().setFloodlightScheduleEnable(NightLightingSettingsActivity.this.device.getIotId(), NightLightingSettingsActivity.this.isFloodlightTime ? 1 : 0);
                                }
                            }
                        });
                    }
                });
            }
            HashMap map2 = new HashMap();
            map2.put(Constants.FloodlightSwitch, Integer.valueOf(!NightLightingSettingsActivity.this.isFloodlight ? 1 : 0));
            IPCManager.getInstance().getDevice(NightLightingSettingsActivity.this.device.getIotId()).setProperties(map2, new IPanelCallback() { // from class: activity.NightLightingSettingsActivity.11.2
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(final boolean z, @Nullable final Object obj) {
                    new Handler().post(new Runnable() { // from class: activity.NightLightingSettingsActivity.11.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Object obj2;
                            if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                return;
                            }
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code") && object.getInteger("code").intValue() == 200) {
                                NightLightingSettingsActivity.this.isFloodlight = !NightLightingSettingsActivity.this.isFloodlight;
                                NightLightingSettingsActivity.this.swFloodlight.setChecked(NightLightingSettingsActivity.this.isFloodlight);
                                SharePreferenceManager.getInstance().setFloodlightSwitch(NightLightingSettingsActivity.this.device.getIotId(), NightLightingSettingsActivity.this.isFloodlight ? 1 : 0);
                            }
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: renamed from: activity.NightLightingSettingsActivity$12, reason: invalid class name */
    class AnonymousClass12 extends OnMultiClickListener {
        AnonymousClass12() {
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            HashMap map = new HashMap();
            map.put(Constants.FloodlightScheduleEnable, Integer.valueOf(!NightLightingSettingsActivity.this.isFloodlightTime ? 1 : 0));
            IPCManager.getInstance().getDevice(NightLightingSettingsActivity.this.device.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.NightLightingSettingsActivity.12.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(final boolean z, @Nullable final Object obj) {
                    new Handler().post(new Runnable() { // from class: activity.NightLightingSettingsActivity.12.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Object obj2;
                            if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                return;
                            }
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code") && object.getInteger("code").intValue() == 200) {
                                NightLightingSettingsActivity.this.isFloodlightTime = !NightLightingSettingsActivity.this.isFloodlightTime;
                                NightLightingSettingsActivity.this.swFloodlightTimeSwitch.setChecked(NightLightingSettingsActivity.this.isFloodlightTime);
                                SharePreferenceManager.getInstance().setFloodlightScheduleEnable(NightLightingSettingsActivity.this.device.getIotId(), NightLightingSettingsActivity.this.isFloodlightTime ? 1 : 0);
                            }
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setExpHighLight(final ExpHighLight expHighLight) {
        HashMap map = new HashMap();
        map.put(Constants.ExpHighLight, expHighLight);
        IPCManager.getInstance().getDevice(this.device.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.NightLightingSettingsActivity.16
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(final boolean z, @Nullable final Object obj) {
                new Handler().post(new Runnable() { // from class: activity.NightLightingSettingsActivity.16.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Object obj2;
                        if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                            return;
                        }
                        JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                        if (object.containsKey("code") && object.getInteger("code").intValue() == 200) {
                            NightLightingSettingsActivity.this.isExpHighLight = expHighLight.Enable == 1;
                            NightLightingSettingsActivity.this.swExpHighLight.setChecked(NightLightingSettingsActivity.this.isExpHighLight);
                            NightLightingSettingsActivity.this.binding.layoutExpHighLightLevel.setVisibility(expHighLight.Enable == 1 ? 0 : 8);
                            NightLightingSettingsActivity.this.binding.itExpHighLight.setLineShow(expHighLight.Enable == 1);
                            NightLightingSettingsActivity.this.binding.seekBarExpHighLightLevel.setProgress(expHighLight.Level);
                            SharePreferenceManager.getInstance().setExpHighLight(NightLightingSettingsActivity.this.iotId, JSONObject.toJSONString(expHighLight));
                        }
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setWhiteLightBrightness(int i, String str) {
        HashMap map = new HashMap();
        map.put(Constants.WhiteLightBrightness, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(str).setProperties(map, new IPanelCallback() { // from class: activity.NightLightingSettingsActivity.17
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIRLightBrightness(int i, String str) {
        HashMap map = new HashMap();
        map.put(Constants.IRLightBrightness, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(str).setProperties(map, new IPanelCallback() { // from class: activity.NightLightingSettingsActivity.18
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNightMode(String str, final Object obj) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.day_night_mode_key))) {
            map.put(Constants.DAY_NIGHT_MODE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
        }
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.NightLightingSettingsActivity.19
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj2) {
                if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        NightLightingSettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.NightLightingSettingsActivity.19.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(NightLightingSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    } else {
                        SharePreferenceManager.getInstance().setDayNightMode(NightLightingSettingsActivity.this.iotId, Integer.parseInt(obj.toString()));
                        NightLightingSettingsActivity.this.uiHandler.post(new Runnable() { // from class: activity.NightLightingSettingsActivity.19.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (NightLightingSettingsActivity.this.iotId.equals(NightLightingSettingsActivity.this.device.getIotId())) {
                                    NightLightingSettingsActivity.this.binding.itemNightMode.setRightText(NightLightingSettingsActivity.this.infrarredMode[SharePreferenceManager.getInstance().getDayNightMode(NightLightingSettingsActivity.this.device.getIotId())]);
                                }
                                if (NightLightingSettingsActivity.this.device1 != null && NightLightingSettingsActivity.this.iotId.equals(NightLightingSettingsActivity.this.device1.getIotId())) {
                                    NightLightingSettingsActivity.this.binding.itemNightModeGun.setRightText(NightLightingSettingsActivity.this.infrarredMode[SharePreferenceManager.getInstance().getDayNightMode(NightLightingSettingsActivity.this.device1.getIotId())]);
                                }
                                Toast.makeText(NightLightingSettingsActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }
}
