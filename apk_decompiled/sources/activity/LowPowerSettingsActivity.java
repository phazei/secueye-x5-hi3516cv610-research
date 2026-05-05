package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import bean.LowPowerAbility;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.huawei.hms.framework.common.ContainerUtils;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityLowPowerSettingsBinding;
import config.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sdk.IPCManager;
import tools.MyCallback;
import tools.OnMultiClickListener;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.LongItemView;
import view.SelectorDialogFragment;

/* JADX INFO: loaded from: classes.dex */
public class LowPowerSettingsActivity extends CommonActivity {
    private ActivityLowPowerSettingsBinding binding;
    private String iotId;
    private int lowPowerAOVMode4GSwitch;
    private SwitchCompat lowPowerAOVModeSwitch;
    LowPowerAbility lowPowerAbility;
    private String[] lowPowerFixedTime;
    private SelectorDialogFragment lowPowerFixedTimeFragment;
    private SelectorDialogFragment lowPowerModeFragment;
    private String[] lowPowerPIR;
    private SelectorDialogFragment lowPowerPirFragment;
    private SelectorDialogFragment lowPowerRecordFragment;
    private String[] lowPowerRecordTime;
    private SelectorDialogFragment lowPowerWakeFragment;
    private String[] lowPowerWakeTime;
    private String[] lowPowerWorkModeDiy;
    private SelectorDialogFragment lowPowerWorkModeFragment;
    private SwitchCompat renXingZhuiZongSwitch;
    private Handler uiHandler = new Handler();
    List<String> lowPowerModeList = new ArrayList();

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_low_power_settings;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.iotId = getIntent().getStringExtra("iotId");
        this.binding = (ActivityLowPowerSettingsBinding) DataBindingUtil.setContentView(this, R.layout.activity_low_power_settings);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.LowPowerSettingsActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                LowPowerSettingsActivity.this.finish();
            }
        });
        this.lowPowerModeList.clear();
        if (SharePreferenceManager.getInstance().getLowPowerWorkModeMask(this.iotId) == 0) {
            if (SharePreferenceManager.getInstance().getLowPowerPIR(this.iotId) == 1) {
                Log.e("LowPowerPIR", "" + SharePreferenceManager.getInstance().getLowPowerPIR(this.iotId));
                this.lowPowerModeList.add(getResources().getString(R.string.low_power_model_1));
                this.lowPowerModeList.add(getResources().getString(R.string.low_power_model_2));
                this.lowPowerModeList.add(getResources().getString(R.string.low_power_model_3));
                this.lowPowerModeList.add(getResources().getString(R.string.low_power_model_4));
            } else {
                this.lowPowerModeList.add(getResources().getString(R.string.low_power_model_1));
                this.lowPowerModeList.add(getResources().getString(R.string.low_power_model_3));
                this.lowPowerModeList.add(getResources().getString(R.string.low_power_model_4));
            }
        } else {
            String binaryString = Integer.toBinaryString(SharePreferenceManager.getInstance().getLowPowerWorkModeMask(this.iotId));
            Log.e(Constants.LowPowerWorkModeMask, binaryString + "");
            for (int length = binaryString.length() - 1; length >= 0; length--) {
                int iCharAt = binaryString.charAt(length) - '0';
                Log.e(Constants.LowPowerWorkModeMask, length + ContainerUtils.KEY_VALUE_DELIMITER + iCharAt);
                if (iCharAt == 1) {
                    if (length == 3) {
                        this.lowPowerModeList.add(getResources().getString(R.string.low_power_model_1));
                    }
                    if (length == 2) {
                        this.lowPowerModeList.add(getResources().getString(R.string.low_power_model_2));
                    }
                    if (length == 1) {
                        this.lowPowerModeList.add(getResources().getString(R.string.low_power_model_3));
                    }
                    if (length == 0) {
                        this.lowPowerModeList.add(getResources().getString(R.string.low_power_model_4));
                    }
                }
            }
        }
        this.lowPowerWorkModeDiy = new String[]{getResources().getString(R.string.low_power_awaken_1)};
        this.lowPowerFixedTime = new String[]{"10s", "30s", "60s"};
        this.lowPowerRecordTime = new String[]{"10s", "20s", "30s"};
        this.lowPowerWakeTime = new String[]{getString(R.string.close), "15s", "30s"};
        this.lowPowerPIR = new String[]{getString(R.string.low), getString(R.string.centre), getString(R.string.height)};
        this.lowPowerModeFragment = new SelectorDialogFragment(getString(R.string.low_power_switch_on), this.lowPowerModeList);
        this.lowPowerModeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.LowPowerSettingsActivity.2
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v10 */
            /* JADX WARN: Type inference failed for: r0v11, types: [int] */
            /* JADX WARN: Type inference failed for: r0v12 */
            /* JADX WARN: Type inference failed for: r0v13 */
            /* JADX WARN: Type inference failed for: r0v14 */
            /* JADX WARN: Type inference failed for: r0v15 */
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                String str = LowPowerSettingsActivity.this.lowPowerModeList.get(i);
                str.equals(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_model_1));
                ?? Equals = str.equals(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_model_2));
                if (str.equals(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_model_3))) {
                    Equals = 2;
                }
                ?? r0 = Equals;
                if (str.equals(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_model_4))) {
                    r0 = 3;
                }
                LowPowerSettingsActivity.this.setLowPowerMode(r0);
            }
        });
        getData();
        this.binding.seekLowPowerWake.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: activity.LowPowerSettingsActivity.3
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                LowPowerSettingsActivity.this.lowPowerAbility.WakeUpCoolingDuration = seekBar.getProgress();
                LowPowerSettingsActivity lowPowerSettingsActivity = LowPowerSettingsActivity.this;
                lowPowerSettingsActivity.setData(lowPowerSettingsActivity.lowPowerAbility);
            }
        });
        this.binding.seekLowPowerRecord.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: activity.LowPowerSettingsActivity.4
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                LowPowerSettingsActivity.this.lowPowerAbility.VideoRecordDuration = seekBar.getProgress();
                LowPowerSettingsActivity lowPowerSettingsActivity = LowPowerSettingsActivity.this;
                lowPowerSettingsActivity.setData(lowPowerSettingsActivity.lowPowerAbility);
            }
        });
        this.binding.seekLowPowerFixedTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: activity.LowPowerSettingsActivity.5
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                LowPowerSettingsActivity.this.lowPowerAbility.CycleWakeUpDuration = seekBar.getProgress();
                LowPowerSettingsActivity lowPowerSettingsActivity = LowPowerSettingsActivity.this;
                lowPowerSettingsActivity.setData(lowPowerSettingsActivity.lowPowerAbility);
            }
        });
        this.binding.itLowPowerSwitch.setOnClickListener(new View.OnClickListener() { // from class: activity.LowPowerSettingsActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                LowPowerSettingsActivity.this.lowPowerModeFragment.showAllowingStateLoss(LowPowerSettingsActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.itLowPowerDiy2.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerSettingsActivity.7
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(LowPowerSettingsActivity.this, (Class<?>) LowPowerDiySettingsActivity.class);
                intent.putExtra("iotId", LowPowerSettingsActivity.this.iotId);
                LowPowerSettingsActivity.this.startActivity(intent);
            }
        });
        this.lowPowerWorkModeFragment = new SelectorDialogFragment(getString(R.string.low_power_awaken), this.lowPowerWorkModeDiy);
        this.lowPowerWorkModeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.LowPowerSettingsActivity.8
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                LowPowerSettingsActivity.this.lowPowerAbility.PirWakeUpSwitch = 0;
                LowPowerSettingsActivity.this.lowPowerAbility.CycleWakeUpSwitch = 0;
                LowPowerSettingsActivity.this.lowPowerAbility.OnlyAppWakeUP = 0;
                switch (i) {
                    case 0:
                        LowPowerSettingsActivity.this.lowPowerAbility.PirWakeUpSwitch = 1;
                        break;
                    case 1:
                        LowPowerSettingsActivity.this.lowPowerAbility.OnlyAppWakeUP = 1;
                        break;
                }
                LowPowerSettingsActivity lowPowerSettingsActivity = LowPowerSettingsActivity.this;
                lowPowerSettingsActivity.setDiyData(lowPowerSettingsActivity.lowPowerAbility);
            }
        });
        this.lowPowerFixedTimeFragment = new SelectorDialogFragment(getString(R.string.low_power_fixed_time), this.lowPowerFixedTime);
        this.lowPowerFixedTimeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.LowPowerSettingsActivity.9
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                LowPowerSettingsActivity.this.lowPowerAbility.PirWakeUpSwitch = 0;
                LowPowerSettingsActivity.this.lowPowerAbility.CycleWakeUpSwitch = 1;
                LowPowerSettingsActivity.this.lowPowerAbility.OnlyAppWakeUP = 0;
                switch (i) {
                    case 0:
                        LowPowerSettingsActivity.this.lowPowerAbility.CycleWakeUpDuration = 10;
                        break;
                    case 1:
                        LowPowerSettingsActivity.this.lowPowerAbility.CycleWakeUpDuration = 30;
                        break;
                    case 2:
                        LowPowerSettingsActivity.this.lowPowerAbility.CycleWakeUpDuration = 60;
                        break;
                }
                LowPowerSettingsActivity lowPowerSettingsActivity = LowPowerSettingsActivity.this;
                lowPowerSettingsActivity.setDiyData(lowPowerSettingsActivity.lowPowerAbility);
            }
        });
        this.lowPowerRecordFragment = new SelectorDialogFragment(getString(R.string.low_power_record), this.lowPowerRecordTime);
        this.lowPowerRecordFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.LowPowerSettingsActivity.10
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                switch (i) {
                    case 0:
                        LowPowerSettingsActivity.this.lowPowerAbility.VideoRecordDuration = 10;
                        break;
                    case 1:
                        LowPowerSettingsActivity.this.lowPowerAbility.VideoRecordDuration = 20;
                        break;
                    case 2:
                        LowPowerSettingsActivity.this.lowPowerAbility.VideoRecordDuration = 30;
                        break;
                }
                LowPowerSettingsActivity lowPowerSettingsActivity = LowPowerSettingsActivity.this;
                lowPowerSettingsActivity.setDiyData(lowPowerSettingsActivity.lowPowerAbility);
            }
        });
        this.lowPowerWakeFragment = new SelectorDialogFragment(getString(R.string.low_power_wake), this.lowPowerWakeTime);
        this.lowPowerWakeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.LowPowerSettingsActivity.11
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                switch (i) {
                    case 0:
                        LowPowerSettingsActivity.this.lowPowerAbility.WakeUpCoolingDuration = 0;
                        break;
                    case 1:
                        LowPowerSettingsActivity.this.lowPowerAbility.WakeUpCoolingDuration = 15;
                        break;
                    case 2:
                        LowPowerSettingsActivity.this.lowPowerAbility.WakeUpCoolingDuration = 30;
                        break;
                }
                LowPowerSettingsActivity lowPowerSettingsActivity = LowPowerSettingsActivity.this;
                lowPowerSettingsActivity.setDiyData(lowPowerSettingsActivity.lowPowerAbility);
            }
        });
        this.lowPowerPirFragment = new SelectorDialogFragment(getString(R.string.low_power_pir), this.lowPowerPIR);
        this.lowPowerPirFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.LowPowerSettingsActivity.12
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                LowPowerSettingsActivity.this.lowPowerAbility.PirWakeUpSensitivity = i;
                LowPowerSettingsActivity lowPowerSettingsActivity = LowPowerSettingsActivity.this;
                lowPowerSettingsActivity.setDiyData(lowPowerSettingsActivity.lowPowerAbility);
            }
        });
        this.binding.itIvp.addRightView(new SwitchCompat(this));
        this.renXingZhuiZongSwitch = (SwitchCompat) this.binding.itIvp.getRightView();
        this.renXingZhuiZongSwitch.setTextOff("");
        this.renXingZhuiZongSwitch.setTextOn("");
        this.renXingZhuiZongSwitch.setText("");
        this.renXingZhuiZongSwitch.setThumbDrawable(null);
        this.renXingZhuiZongSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.renXingZhuiZongSwitch.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerSettingsActivity.13
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                LowPowerSettingsActivity.this.lowPowerAbility.WakeUpAlarmIvpAssistSwitch = LowPowerSettingsActivity.this.lowPowerAbility.WakeUpAlarmIvpAssistSwitch == 1 ? 0 : 1;
                LowPowerSettingsActivity lowPowerSettingsActivity = LowPowerSettingsActivity.this;
                lowPowerSettingsActivity.setDiyData(lowPowerSettingsActivity.lowPowerAbility);
            }
        });
        this.binding.itLowPowerDiy.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerSettingsActivity.14
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
            }
        });
        this.binding.itLowPowerFixedTime.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerSettingsActivity.15
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                LowPowerSettingsActivity.this.lowPowerFixedTimeFragment.showAllowingStateLoss(LowPowerSettingsActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.itLowPowerRecord.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerSettingsActivity.16
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                LowPowerSettingsActivity.this.lowPowerRecordFragment.showAllowingStateLoss(LowPowerSettingsActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.itLowPowerWake.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerSettingsActivity.17
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                LowPowerSettingsActivity.this.lowPowerWakeFragment.showAllowingStateLoss(LowPowerSettingsActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.itLowPowerPir.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerSettingsActivity.18
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                LowPowerSettingsActivity.this.lowPowerPirFragment.showAllowingStateLoss(LowPowerSettingsActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.itemSleepSwitch.addRightView(new SwitchCompat(this));
        this.lowPowerAOVModeSwitch = (SwitchCompat) this.binding.itemSleepSwitch.getRightView();
        this.lowPowerAOVModeSwitch.setTextOff("");
        this.lowPowerAOVModeSwitch.setTextOn("");
        this.lowPowerAOVModeSwitch.setText("");
        this.lowPowerAOVModeSwitch.setThumbDrawable(null);
        this.lowPowerAOVModeSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.lowPowerAOVModeSwitch.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerSettingsActivity.19
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                LowPowerSettingsActivity lowPowerSettingsActivity = LowPowerSettingsActivity.this;
                lowPowerSettingsActivity.lowPowerAOVMode4GSwitch = lowPowerSettingsActivity.lowPowerAOVMode4GSwitch == 1 ? 0 : 1;
                LowPowerSettingsActivity lowPowerSettingsActivity2 = LowPowerSettingsActivity.this;
                lowPowerSettingsActivity2.setLowPowerAOVMode4GSwitch(lowPowerSettingsActivity2.lowPowerAOVMode4GSwitch);
            }
        });
        getDiyData();
    }

    private void getData() {
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.LowPowerSettingsActivity.20
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                LowPowerSettingsActivity.this.lowPowerAbility = (LowPowerAbility) JSONObject.parseObject(SharePreferenceManager.getInstance().getLowPowerAbility(LowPowerSettingsActivity.this.iotId), LowPowerAbility.class);
                if (LowPowerSettingsActivity.this.lowPowerAbility != null) {
                    LowPowerSettingsActivity.this.binding.seekLowPowerWake.setProgress(LowPowerSettingsActivity.this.lowPowerAbility.WakeUpCoolingDuration);
                    LowPowerSettingsActivity.this.binding.seekLowPowerRecord.setProgress(LowPowerSettingsActivity.this.lowPowerAbility.VideoRecordDuration);
                    LowPowerSettingsActivity.this.binding.seekLowPowerFixedTime.setProgress(LowPowerSettingsActivity.this.lowPowerAbility.CycleWakeUpDuration);
                    if (LowPowerSettingsActivity.this.lowPowerAbility.PirWakeUpSwitch == 1) {
                        LowPowerSettingsActivity.this.binding.itLowPowerDiy.setRightText(LowPowerSettingsActivity.this.lowPowerWorkModeDiy[0]);
                    }
                    if (LowPowerSettingsActivity.this.lowPowerAbility.PirWakeUpSwitch == 0 && LowPowerSettingsActivity.this.lowPowerAbility.CycleWakeUpSwitch == 0 && LowPowerSettingsActivity.this.lowPowerAbility.OnlyAppWakeUP == 0) {
                        LowPowerSettingsActivity.this.binding.itLowPowerDiy.setRightText(LowPowerSettingsActivity.this.lowPowerWorkModeDiy[0]);
                    }
                }
                int lowPowerWorkMode = SharePreferenceManager.getInstance().getLowPowerWorkMode(LowPowerSettingsActivity.this.iotId);
                LowPowerSettingsActivity.this.binding.layoutSleepSwitch.setVisibility(8);
                switch (lowPowerWorkMode) {
                    case 0:
                        LowPowerSettingsActivity.this.binding.itLowPowerSwitch.setRightText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_model_1));
                        LowPowerSettingsActivity.this.binding.tvLowPowerTips.setText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_tips_1));
                        LowPowerSettingsActivity.this.lowPowerAOVMode4GSwitch = SharePreferenceManager.getInstance().getLowPowerAOVMode4GSwitch(LowPowerSettingsActivity.this.iotId);
                        if (LowPowerSettingsActivity.this.lowPowerAOVMode4GSwitch != -1) {
                            LowPowerSettingsActivity.this.binding.layoutSleepSwitch.setVisibility(0);
                            LowPowerSettingsActivity.this.lowPowerAOVModeSwitch.setChecked(LowPowerSettingsActivity.this.lowPowerAOVMode4GSwitch == 1);
                        } else {
                            LowPowerSettingsActivity.this.binding.layoutSleepSwitch.setVisibility(8);
                        }
                        break;
                    case 1:
                        LowPowerSettingsActivity.this.binding.itLowPowerSwitch.setRightText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_model_2));
                        LowPowerSettingsActivity.this.binding.tvLowPowerTips.setText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_tips_2));
                        break;
                    case 2:
                        LowPowerSettingsActivity.this.binding.itLowPowerSwitch.setRightText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_model_3));
                        LowPowerSettingsActivity.this.binding.tvLowPowerTips.setText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_tips_3));
                        break;
                    case 3:
                        LowPowerSettingsActivity.this.binding.itLowPowerSwitch.setRightText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_model_4));
                        LowPowerSettingsActivity.this.binding.tvLowPowerTips.setText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_tips_4));
                        break;
                }
                LowPowerSettingsActivity.this.binding.layoutDiy.setVisibility(SharePreferenceManager.getInstance().getLowPowerWorkMode(LowPowerSettingsActivity.this.iotId) != 1 ? 8 : 0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setData(LowPowerAbility lowPowerAbility) {
        HashMap map = new HashMap();
        map.put(Constants.LowPowerAbility, lowPowerAbility);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.LowPowerSettingsActivity.21
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLowPowerMode(final int i) {
        HashMap map = new HashMap();
        map.put(Constants.LowPowerWorkMode, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.LowPowerSettingsActivity.22
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() == 200) {
                            SharePreferenceManager.getInstance().setLowPowerWorkMode(LowPowerSettingsActivity.this.iotId, i);
                            int lowPowerWorkMode = SharePreferenceManager.getInstance().getLowPowerWorkMode(LowPowerSettingsActivity.this.iotId);
                            LowPowerSettingsActivity.this.binding.layoutSleepSwitch.setVisibility(8);
                            int i2 = 0;
                            switch (lowPowerWorkMode) {
                                case 0:
                                    LowPowerSettingsActivity.this.binding.itLowPowerSwitch.setRightText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_model_1));
                                    LowPowerSettingsActivity.this.binding.tvLowPowerTips.setText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_tips_1));
                                    LowPowerSettingsActivity.this.lowPowerAOVMode4GSwitch = SharePreferenceManager.getInstance().getLowPowerAOVMode4GSwitch(LowPowerSettingsActivity.this.iotId);
                                    if (LowPowerSettingsActivity.this.lowPowerAOVMode4GSwitch != -1) {
                                        LowPowerSettingsActivity.this.binding.layoutSleepSwitch.setVisibility(0);
                                        LowPowerSettingsActivity.this.lowPowerAOVModeSwitch.setChecked(LowPowerSettingsActivity.this.lowPowerAOVMode4GSwitch == 1);
                                    } else {
                                        LowPowerSettingsActivity.this.binding.layoutSleepSwitch.setVisibility(8);
                                    }
                                    break;
                                case 1:
                                    LowPowerSettingsActivity.this.binding.itLowPowerSwitch.setRightText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_model_2));
                                    LowPowerSettingsActivity.this.binding.tvLowPowerTips.setText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_tips_2));
                                    break;
                                case 2:
                                    LowPowerSettingsActivity.this.binding.itLowPowerSwitch.setRightText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_model_3));
                                    LowPowerSettingsActivity.this.binding.tvLowPowerTips.setText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_tips_3));
                                    break;
                                case 3:
                                    LowPowerSettingsActivity.this.binding.itLowPowerSwitch.setRightText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_model_4));
                                    LowPowerSettingsActivity.this.binding.tvLowPowerTips.setText(LowPowerSettingsActivity.this.getResources().getString(R.string.low_power_tips_4));
                                    break;
                            }
                            LinearLayout linearLayout = LowPowerSettingsActivity.this.binding.layoutDiy;
                            if (SharePreferenceManager.getInstance().getLowPowerWorkMode(LowPowerSettingsActivity.this.iotId) != 1) {
                                i2 = 8;
                            }
                            linearLayout.setVisibility(i2);
                            return;
                        }
                        LowPowerSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.LowPowerSettingsActivity.22.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(LowPowerSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getDiyData() {
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.LowPowerSettingsActivity.23
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                StringBuilder sb;
                String string;
                LowPowerSettingsActivity.this.lowPowerAbility = (LowPowerAbility) JSONObject.parseObject(SharePreferenceManager.getInstance().getLowPowerAbility(LowPowerSettingsActivity.this.iotId), LowPowerAbility.class);
                if (LowPowerSettingsActivity.this.lowPowerAbility != null) {
                    LowPowerSettingsActivity.this.binding.itLowPowerFixedTime.setVisibility(8);
                    LowPowerSettingsActivity.this.binding.itLowPowerPir.setVisibility(8);
                    LowPowerSettingsActivity.this.binding.layoutApp.setVisibility(0);
                    if (LowPowerSettingsActivity.this.lowPowerAbility.PirWakeUpSwitch == 1) {
                        LowPowerSettingsActivity.this.binding.itLowPowerDiy.setRightText(LowPowerSettingsActivity.this.lowPowerWorkModeDiy[0]);
                        LowPowerSettingsActivity.this.binding.itLowPowerDiy.setLineShow(true);
                        LowPowerSettingsActivity.this.binding.itLowPowerPir.setVisibility(0);
                        LowPowerSettingsActivity.this.binding.itLowPowerPir.setRightText(LowPowerSettingsActivity.this.lowPowerPIR[LowPowerSettingsActivity.this.lowPowerAbility.PirWakeUpSensitivity]);
                    }
                    if (LowPowerSettingsActivity.this.lowPowerAbility.PirWakeUpSwitch == 0 && LowPowerSettingsActivity.this.lowPowerAbility.CycleWakeUpSwitch == 0 && LowPowerSettingsActivity.this.lowPowerAbility.OnlyAppWakeUP == 0) {
                        LowPowerSettingsActivity.this.binding.itLowPowerDiy.setRightText(LowPowerSettingsActivity.this.lowPowerWorkModeDiy[0]);
                    }
                    LowPowerSettingsActivity.this.binding.itLowPowerRecord.setRightText(LowPowerSettingsActivity.this.lowPowerAbility.VideoRecordDuration + "s");
                    LongItemView longItemView = LowPowerSettingsActivity.this.binding.itLowPowerWake;
                    if (LowPowerSettingsActivity.this.lowPowerAbility.WakeUpCoolingDuration != 0) {
                        sb = new StringBuilder();
                        sb.append(LowPowerSettingsActivity.this.lowPowerAbility.WakeUpCoolingDuration);
                        string = "s";
                    } else {
                        sb = new StringBuilder();
                        sb.append("");
                        string = LowPowerSettingsActivity.this.getString(R.string.close);
                    }
                    sb.append(string);
                    longItemView.setRightText(sb.toString());
                    LowPowerSettingsActivity.this.renXingZhuiZongSwitch.setChecked(LowPowerSettingsActivity.this.lowPowerAbility.WakeUpAlarmIvpAssistSwitch == 1);
                }
                LowPowerSettingsActivity.this.lowPowerAOVMode4GSwitch = SharePreferenceManager.getInstance().getLowPowerAOVMode4GSwitch(LowPowerSettingsActivity.this.iotId);
                if (LowPowerSettingsActivity.this.lowPowerAOVMode4GSwitch != -1) {
                    LowPowerSettingsActivity.this.binding.layoutSleepSwitch.setVisibility(0);
                    LowPowerSettingsActivity.this.lowPowerAOVModeSwitch.setChecked(LowPowerSettingsActivity.this.lowPowerAOVMode4GSwitch == 1);
                } else {
                    LowPowerSettingsActivity.this.binding.layoutSleepSwitch.setVisibility(8);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDiyData(final LowPowerAbility lowPowerAbility) {
        HashMap map = new HashMap();
        map.put(Constants.LowPowerAbility, lowPowerAbility);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.LowPowerSettingsActivity.24
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                StringBuilder sb;
                String string;
                if (z) {
                    SharePreferenceManager.getInstance().setLowPowerAbility(LowPowerSettingsActivity.this.iotId, JSONObject.toJSONString(lowPowerAbility));
                    LowPowerSettingsActivity.this.binding.itLowPowerFixedTime.setVisibility(8);
                    LowPowerSettingsActivity.this.binding.itLowPowerPir.setVisibility(8);
                    LowPowerSettingsActivity.this.binding.layoutApp.setVisibility(0);
                    if (lowPowerAbility.PirWakeUpSwitch == 1) {
                        LowPowerSettingsActivity.this.binding.itLowPowerDiy.setRightText(LowPowerSettingsActivity.this.lowPowerWorkModeDiy[0]);
                        LowPowerSettingsActivity.this.binding.itLowPowerDiy.setLineShow(true);
                        LowPowerSettingsActivity.this.binding.itLowPowerPir.setVisibility(0);
                        LowPowerSettingsActivity.this.binding.itLowPowerPir.setRightText(LowPowerSettingsActivity.this.lowPowerPIR[lowPowerAbility.PirWakeUpSensitivity]);
                    }
                    if (lowPowerAbility.PirWakeUpSwitch == 0 && lowPowerAbility.CycleWakeUpSwitch == 0 && lowPowerAbility.OnlyAppWakeUP == 0) {
                        LowPowerSettingsActivity.this.binding.itLowPowerDiy.setRightText(LowPowerSettingsActivity.this.lowPowerWorkModeDiy[0]);
                    }
                    LowPowerSettingsActivity.this.binding.itLowPowerRecord.setRightText(lowPowerAbility.VideoRecordDuration + "s");
                    LongItemView longItemView = LowPowerSettingsActivity.this.binding.itLowPowerWake;
                    if (lowPowerAbility.WakeUpCoolingDuration != 0) {
                        sb = new StringBuilder();
                        sb.append(lowPowerAbility.WakeUpCoolingDuration);
                        string = "s";
                    } else {
                        sb = new StringBuilder();
                        sb.append("");
                        string = LowPowerSettingsActivity.this.getString(R.string.close);
                    }
                    sb.append(string);
                    longItemView.setRightText(sb.toString());
                    LowPowerSettingsActivity.this.renXingZhuiZongSwitch.setChecked(lowPowerAbility.WakeUpAlarmIvpAssistSwitch == 1);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLowPowerAOVMode4GSwitch(final int i) {
        HashMap map = new HashMap();
        map.put(Constants.LowPowerAOVMode4GSwitch, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.LowPowerSettingsActivity.25
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code") && object.getInteger("code").intValue() == 200) {
                    SharePreferenceManager.getInstance().setLowPowerAOVMode4GSwitch(LowPowerSettingsActivity.this.iotId, i);
                    LowPowerSettingsActivity.this.lowPowerAOVModeSwitch.setChecked(i == 1);
                }
            }
        });
    }
}
