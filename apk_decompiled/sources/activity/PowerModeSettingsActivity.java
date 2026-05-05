package activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityPowerModeSettingsBinding;
import config.Constants;
import java.util.HashMap;
import sdk.IPCManager;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.SelectorDialogFragment;

/* JADX INFO: loaded from: classes.dex */
public class PowerModeSettingsActivity extends CommonActivity {
    private ActivityPowerModeSettingsBinding binding;
    private String iotId;
    private String[] lowPowerMode;
    private SelectorDialogFragment lowPowerModeFragment;
    private String[] lowPowerModeTps;
    private Handler uiHandler = new Handler();

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_power_mode_settings;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.iotId = getIntent().getStringExtra("iotId");
        this.binding = (ActivityPowerModeSettingsBinding) DataBindingUtil.setContentView(this, R.layout.activity_power_mode_settings);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.PowerModeSettingsActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PowerModeSettingsActivity.this.finish();
            }
        });
        this.lowPowerMode = new String[]{getResources().getString(R.string.low_power_model_3), getResources().getString(R.string.low_power_model_1)};
        this.lowPowerModeTps = new String[]{getResources().getString(R.string.low_power_tips_3), getResources().getString(R.string.low_power_tips_5)};
        getData();
        this.lowPowerModeFragment = new SelectorDialogFragment(getString(R.string.low_power_switch_on), this.lowPowerMode);
        this.lowPowerModeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.PowerModeSettingsActivity.2
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                PowerModeSettingsActivity.this.setLowPowerMode(i);
            }
        });
        this.binding.itLowPowerSwitch.setOnClickListener(new View.OnClickListener() { // from class: activity.PowerModeSettingsActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PowerModeSettingsActivity.this.lowPowerModeFragment.showAllowingStateLoss(PowerModeSettingsActivity.this.getSupportFragmentManager(), "");
            }
        });
    }

    private void getData() {
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.PowerModeSettingsActivity.4
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                PowerModeSettingsActivity.this.binding.itLowPowerSwitch.setRightText(PowerModeSettingsActivity.this.lowPowerMode[SharePreferenceManager.getInstance().getPowerMode(PowerModeSettingsActivity.this.iotId)]);
                PowerModeSettingsActivity.this.binding.tvLowPowerTips.setText(PowerModeSettingsActivity.this.lowPowerModeTps[SharePreferenceManager.getInstance().getPowerMode(PowerModeSettingsActivity.this.iotId)]);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLowPowerMode(final int i) {
        HashMap map = new HashMap();
        map.put(Constants.PowerMode, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.PowerModeSettingsActivity.5
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() == 200) {
                            SharePreferenceManager.getInstance().setPowerMode(PowerModeSettingsActivity.this.iotId, i);
                            PowerModeSettingsActivity.this.binding.itLowPowerSwitch.setRightText(PowerModeSettingsActivity.this.lowPowerMode[i]);
                            PowerModeSettingsActivity.this.binding.tvLowPowerTips.setText(PowerModeSettingsActivity.this.lowPowerModeTps[SharePreferenceManager.getInstance().getPowerMode(PowerModeSettingsActivity.this.iotId)]);
                        } else {
                            PowerModeSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.PowerModeSettingsActivity.5.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(PowerModeSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
