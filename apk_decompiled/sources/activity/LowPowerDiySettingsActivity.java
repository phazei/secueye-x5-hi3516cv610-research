package activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import bean.LowPowerAbility;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityLowPowerDiySettingsBinding;
import config.Constants;
import java.util.HashMap;
import sdk.IPCManager;
import tools.MyCallback;
import tools.OnMultiClickListener;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.LongItemView;
import view.SelectorDialogFragment;

/* JADX INFO: loaded from: classes.dex */
public class LowPowerDiySettingsActivity extends CommonActivity {
    private ActivityLowPowerDiySettingsBinding binding;
    private String iotId;
    LowPowerAbility lowPowerAbility;
    private String[] lowPowerFixedTime;
    private SelectorDialogFragment lowPowerFixedTimeFragment;
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

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_low_power_diy_settings;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.iotId = getIntent().getStringExtra("iotId");
        this.binding = (ActivityLowPowerDiySettingsBinding) DataBindingUtil.setContentView(this, R.layout.activity_low_power_diy_settings);
        setEdgeToEdge(this.binding.layoutMain);
        this.lowPowerWorkModeDiy = new String[]{getResources().getString(R.string.low_power_awaken_1), getResources().getString(R.string.low_power_awaken_3)};
        this.lowPowerFixedTime = new String[]{"10s", "30s", "60s"};
        this.lowPowerRecordTime = new String[]{"10s", "20s", "30s"};
        this.lowPowerWakeTime = new String[]{getString(R.string.close), "15s", "30s"};
        this.lowPowerPIR = new String[]{getString(R.string.low), getString(R.string.centre), getString(R.string.height)};
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.LowPowerDiySettingsActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                LowPowerDiySettingsActivity.this.finish();
            }
        });
        this.lowPowerWorkModeFragment = new SelectorDialogFragment(getString(R.string.low_power_awaken), this.lowPowerWorkModeDiy);
        this.lowPowerWorkModeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.LowPowerDiySettingsActivity.2
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                LowPowerDiySettingsActivity.this.lowPowerAbility.PirWakeUpSwitch = 0;
                LowPowerDiySettingsActivity.this.lowPowerAbility.CycleWakeUpSwitch = 0;
                LowPowerDiySettingsActivity.this.lowPowerAbility.OnlyAppWakeUP = 0;
                switch (i) {
                    case 0:
                        LowPowerDiySettingsActivity.this.lowPowerAbility.PirWakeUpSwitch = 1;
                        break;
                    case 1:
                        LowPowerDiySettingsActivity.this.lowPowerAbility.OnlyAppWakeUP = 1;
                        break;
                }
                LowPowerDiySettingsActivity lowPowerDiySettingsActivity = LowPowerDiySettingsActivity.this;
                lowPowerDiySettingsActivity.setDiyData(lowPowerDiySettingsActivity.lowPowerAbility);
            }
        });
        this.lowPowerFixedTimeFragment = new SelectorDialogFragment(getString(R.string.low_power_fixed_time), this.lowPowerFixedTime);
        this.lowPowerFixedTimeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.LowPowerDiySettingsActivity.3
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                LowPowerDiySettingsActivity.this.lowPowerAbility.PirWakeUpSwitch = 0;
                LowPowerDiySettingsActivity.this.lowPowerAbility.CycleWakeUpSwitch = 1;
                LowPowerDiySettingsActivity.this.lowPowerAbility.OnlyAppWakeUP = 0;
                switch (i) {
                    case 0:
                        LowPowerDiySettingsActivity.this.lowPowerAbility.CycleWakeUpDuration = 10;
                        break;
                    case 1:
                        LowPowerDiySettingsActivity.this.lowPowerAbility.CycleWakeUpDuration = 30;
                        break;
                    case 2:
                        LowPowerDiySettingsActivity.this.lowPowerAbility.CycleWakeUpDuration = 60;
                        break;
                }
                LowPowerDiySettingsActivity lowPowerDiySettingsActivity = LowPowerDiySettingsActivity.this;
                lowPowerDiySettingsActivity.setDiyData(lowPowerDiySettingsActivity.lowPowerAbility);
            }
        });
        this.lowPowerRecordFragment = new SelectorDialogFragment(getString(R.string.low_power_record), this.lowPowerRecordTime);
        this.lowPowerRecordFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.LowPowerDiySettingsActivity.4
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                switch (i) {
                    case 0:
                        LowPowerDiySettingsActivity.this.lowPowerAbility.VideoRecordDuration = 10;
                        break;
                    case 1:
                        LowPowerDiySettingsActivity.this.lowPowerAbility.VideoRecordDuration = 20;
                        break;
                    case 2:
                        LowPowerDiySettingsActivity.this.lowPowerAbility.VideoRecordDuration = 30;
                        break;
                }
                LowPowerDiySettingsActivity lowPowerDiySettingsActivity = LowPowerDiySettingsActivity.this;
                lowPowerDiySettingsActivity.setDiyData(lowPowerDiySettingsActivity.lowPowerAbility);
            }
        });
        this.lowPowerWakeFragment = new SelectorDialogFragment(getString(R.string.low_power_wake), this.lowPowerWakeTime);
        this.lowPowerWakeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.LowPowerDiySettingsActivity.5
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                switch (i) {
                    case 0:
                        LowPowerDiySettingsActivity.this.lowPowerAbility.WakeUpCoolingDuration = 0;
                        break;
                    case 1:
                        LowPowerDiySettingsActivity.this.lowPowerAbility.WakeUpCoolingDuration = 15;
                        break;
                    case 2:
                        LowPowerDiySettingsActivity.this.lowPowerAbility.WakeUpCoolingDuration = 30;
                        break;
                }
                LowPowerDiySettingsActivity lowPowerDiySettingsActivity = LowPowerDiySettingsActivity.this;
                lowPowerDiySettingsActivity.setDiyData(lowPowerDiySettingsActivity.lowPowerAbility);
            }
        });
        this.lowPowerPirFragment = new SelectorDialogFragment(getString(R.string.low_power_pir), this.lowPowerPIR);
        this.lowPowerPirFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.LowPowerDiySettingsActivity.6
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                LowPowerDiySettingsActivity.this.lowPowerAbility.PirWakeUpSensitivity = i;
                LowPowerDiySettingsActivity lowPowerDiySettingsActivity = LowPowerDiySettingsActivity.this;
                lowPowerDiySettingsActivity.setDiyData(lowPowerDiySettingsActivity.lowPowerAbility);
            }
        });
        this.binding.itIvp.addRightView(new SwitchCompat(this));
        this.renXingZhuiZongSwitch = (SwitchCompat) this.binding.itIvp.getRightView();
        this.renXingZhuiZongSwitch.setTextOff("");
        this.renXingZhuiZongSwitch.setTextOn("");
        this.renXingZhuiZongSwitch.setText("");
        this.renXingZhuiZongSwitch.setThumbDrawable(null);
        this.renXingZhuiZongSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.renXingZhuiZongSwitch.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerDiySettingsActivity.7
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                LowPowerDiySettingsActivity.this.lowPowerAbility.WakeUpAlarmIvpAssistSwitch = LowPowerDiySettingsActivity.this.lowPowerAbility.WakeUpAlarmIvpAssistSwitch == 1 ? 0 : 1;
                LowPowerDiySettingsActivity lowPowerDiySettingsActivity = LowPowerDiySettingsActivity.this;
                lowPowerDiySettingsActivity.setDiyData(lowPowerDiySettingsActivity.lowPowerAbility);
            }
        });
        this.binding.itLowPowerDiy.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerDiySettingsActivity.8
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                LowPowerDiySettingsActivity.this.lowPowerWorkModeFragment.showAllowingStateLoss(LowPowerDiySettingsActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.itLowPowerFixedTime.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerDiySettingsActivity.9
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                LowPowerDiySettingsActivity.this.lowPowerFixedTimeFragment.showAllowingStateLoss(LowPowerDiySettingsActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.itLowPowerRecord.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerDiySettingsActivity.10
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                LowPowerDiySettingsActivity.this.lowPowerRecordFragment.showAllowingStateLoss(LowPowerDiySettingsActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.itLowPowerWake.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerDiySettingsActivity.11
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                LowPowerDiySettingsActivity.this.lowPowerWakeFragment.showAllowingStateLoss(LowPowerDiySettingsActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.itLowPowerPir.setOnClickListener(new OnMultiClickListener() { // from class: activity.LowPowerDiySettingsActivity.12
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                LowPowerDiySettingsActivity.this.lowPowerPirFragment.showAllowingStateLoss(LowPowerDiySettingsActivity.this.getSupportFragmentManager(), "");
            }
        });
        getDiyData();
    }

    private void getDiyData() {
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.LowPowerDiySettingsActivity.13
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                StringBuilder sb;
                String string;
                LowPowerDiySettingsActivity.this.lowPowerAbility = (LowPowerAbility) JSONObject.parseObject(SharePreferenceManager.getInstance().getLowPowerAbility(LowPowerDiySettingsActivity.this.iotId), LowPowerAbility.class);
                if (LowPowerDiySettingsActivity.this.lowPowerAbility != null) {
                    LowPowerDiySettingsActivity.this.binding.itLowPowerFixedTime.setVisibility(8);
                    LowPowerDiySettingsActivity.this.binding.itLowPowerPir.setVisibility(8);
                    LowPowerDiySettingsActivity.this.binding.layoutApp.setVisibility(0);
                    if (LowPowerDiySettingsActivity.this.lowPowerAbility.PirWakeUpSwitch == 1) {
                        LowPowerDiySettingsActivity.this.binding.itLowPowerDiy.setRightText(LowPowerDiySettingsActivity.this.lowPowerWorkModeDiy[0]);
                        LowPowerDiySettingsActivity.this.binding.itLowPowerDiy.setLineShow(true);
                        LowPowerDiySettingsActivity.this.binding.itLowPowerPir.setVisibility(0);
                        LowPowerDiySettingsActivity.this.binding.itLowPowerPir.setRightText(LowPowerDiySettingsActivity.this.lowPowerPIR[LowPowerDiySettingsActivity.this.lowPowerAbility.PirWakeUpSensitivity]);
                    }
                    if (LowPowerDiySettingsActivity.this.lowPowerAbility.OnlyAppWakeUP == 1) {
                        LowPowerDiySettingsActivity.this.binding.itLowPowerDiy.setRightText(LowPowerDiySettingsActivity.this.lowPowerWorkModeDiy[1]);
                        LowPowerDiySettingsActivity.this.binding.itLowPowerDiy.setLineShow(false);
                        LowPowerDiySettingsActivity.this.binding.layoutApp.setVisibility(8);
                    }
                    if (LowPowerDiySettingsActivity.this.lowPowerAbility.PirWakeUpSwitch == 0 && LowPowerDiySettingsActivity.this.lowPowerAbility.CycleWakeUpSwitch == 0 && LowPowerDiySettingsActivity.this.lowPowerAbility.OnlyAppWakeUP == 0) {
                        LowPowerDiySettingsActivity.this.binding.itLowPowerDiy.setRightText(LowPowerDiySettingsActivity.this.lowPowerWorkModeDiy[0]);
                    }
                    LowPowerDiySettingsActivity.this.binding.itLowPowerRecord.setRightText(LowPowerDiySettingsActivity.this.lowPowerAbility.VideoRecordDuration + "s");
                    LongItemView longItemView = LowPowerDiySettingsActivity.this.binding.itLowPowerWake;
                    if (LowPowerDiySettingsActivity.this.lowPowerAbility.WakeUpCoolingDuration != 0) {
                        sb = new StringBuilder();
                        sb.append(LowPowerDiySettingsActivity.this.lowPowerAbility.WakeUpCoolingDuration);
                        string = "s";
                    } else {
                        sb = new StringBuilder();
                        sb.append("");
                        string = LowPowerDiySettingsActivity.this.getString(R.string.close);
                    }
                    sb.append(string);
                    longItemView.setRightText(sb.toString());
                    LowPowerDiySettingsActivity.this.renXingZhuiZongSwitch.setChecked(LowPowerDiySettingsActivity.this.lowPowerAbility.WakeUpAlarmIvpAssistSwitch == 1);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDiyData(final LowPowerAbility lowPowerAbility) {
        HashMap map = new HashMap();
        map.put(Constants.LowPowerAbility, lowPowerAbility);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.LowPowerDiySettingsActivity.14
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                StringBuilder sb;
                String string;
                if (z) {
                    SharePreferenceManager.getInstance().setLowPowerAbility(LowPowerDiySettingsActivity.this.iotId, JSONObject.toJSONString(lowPowerAbility));
                    LowPowerDiySettingsActivity.this.binding.itLowPowerFixedTime.setVisibility(8);
                    LowPowerDiySettingsActivity.this.binding.itLowPowerPir.setVisibility(8);
                    LowPowerDiySettingsActivity.this.binding.layoutApp.setVisibility(0);
                    if (lowPowerAbility.PirWakeUpSwitch == 1) {
                        LowPowerDiySettingsActivity.this.binding.itLowPowerDiy.setRightText(LowPowerDiySettingsActivity.this.lowPowerWorkModeDiy[0]);
                        LowPowerDiySettingsActivity.this.binding.itLowPowerDiy.setLineShow(true);
                        LowPowerDiySettingsActivity.this.binding.itLowPowerPir.setVisibility(0);
                        LowPowerDiySettingsActivity.this.binding.itLowPowerPir.setRightText(LowPowerDiySettingsActivity.this.lowPowerPIR[lowPowerAbility.PirWakeUpSensitivity]);
                    }
                    if (lowPowerAbility.OnlyAppWakeUP == 1) {
                        LowPowerDiySettingsActivity.this.binding.itLowPowerDiy.setRightText(LowPowerDiySettingsActivity.this.lowPowerWorkModeDiy[1]);
                        LowPowerDiySettingsActivity.this.binding.itLowPowerDiy.setLineShow(false);
                        LowPowerDiySettingsActivity.this.binding.layoutApp.setVisibility(8);
                    }
                    if (lowPowerAbility.PirWakeUpSwitch == 0 && lowPowerAbility.CycleWakeUpSwitch == 0 && lowPowerAbility.OnlyAppWakeUP == 0) {
                        LowPowerDiySettingsActivity.this.binding.itLowPowerDiy.setRightText(LowPowerDiySettingsActivity.this.lowPowerWorkModeDiy[0]);
                    }
                    LowPowerDiySettingsActivity.this.binding.itLowPowerRecord.setRightText(lowPowerAbility.VideoRecordDuration + "s");
                    LongItemView longItemView = LowPowerDiySettingsActivity.this.binding.itLowPowerWake;
                    if (lowPowerAbility.WakeUpCoolingDuration != 0) {
                        sb = new StringBuilder();
                        sb.append(lowPowerAbility.WakeUpCoolingDuration);
                        string = "s";
                    } else {
                        sb = new StringBuilder();
                        sb.append("");
                        string = LowPowerDiySettingsActivity.this.getString(R.string.close);
                    }
                    sb.append(string);
                    longItemView.setRightText(sb.toString());
                    LowPowerDiySettingsActivity.this.renXingZhuiZongSwitch.setChecked(lowPowerAbility.WakeUpAlarmIvpAssistSwitch == 1);
                }
            }
        });
    }
}
