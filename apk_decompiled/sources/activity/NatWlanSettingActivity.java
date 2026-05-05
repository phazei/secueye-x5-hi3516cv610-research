package activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import bean.DeviceInfoBean;
import bean.NatWLANConfigModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityNetWlanSettingBinding;
import config.Constants;
import java.util.HashMap;
import sdk.IPCManager;
import tools.OnMultiClickListener;
import tools.SharePreferenceManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class NatWlanSettingActivity extends CommonActivity {
    private ActivityNetWlanSettingBinding binding;
    private DeviceInfoBean device;
    NatWLANConfigModel natWLANConfigModel;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_net_wlan_setting;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityNetWlanSettingBinding) DataBindingUtil.setContentView(this, R.layout.activity_net_wlan_setting);
        setEdgeToEdge(this.binding.layoutMain);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
        }
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.NatWlanSettingActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                NatWlanSettingActivity.this.finish();
            }
        });
        if (!SharePreferenceManager.getInstance().getNatWLANSwitchConfig(this.device.getIotId()).equals("")) {
            this.natWLANConfigModel = (NatWLANConfigModel) JSONObject.parseObject(SharePreferenceManager.getInstance().getNatWLANSwitchConfig(this.device.getIotId()), NatWLANConfigModel.class);
            if (!this.natWLANConfigModel.SSID.equals("")) {
                this.binding.editName.setText(this.natWLANConfigModel.SSID);
            }
            if (!this.natWLANConfigModel.WPA_PASSPHRASE.equals("")) {
                this.binding.editPass.setText(this.natWLANConfigModel.WPA_PASSPHRASE);
            }
        }
        this.binding.checkBtn.setOnClickListener(new AnonymousClass2());
    }

    /* JADX INFO: renamed from: activity.NatWlanSettingActivity$2, reason: invalid class name */
    class AnonymousClass2 extends OnMultiClickListener {
        AnonymousClass2() {
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            if (NatWlanSettingActivity.this.binding.editName.getText().toString().isEmpty()) {
                NatWlanSettingActivity natWlanSettingActivity = NatWlanSettingActivity.this;
                natWlanSettingActivity.showToast(natWlanSettingActivity.getString(R.string.input_wifi_name));
                return;
            }
            NatWlanSettingActivity.this.natWLANConfigModel.SSID = NatWlanSettingActivity.this.binding.editName.getText().toString();
            if (NatWlanSettingActivity.this.binding.editPass.getText().toString().isEmpty()) {
                NatWlanSettingActivity natWlanSettingActivity2 = NatWlanSettingActivity.this;
                natWlanSettingActivity2.showToast(natWlanSettingActivity2.getString(R.string.input_wifi_pwd));
                return;
            }
            NatWlanSettingActivity.this.natWLANConfigModel.WPA_PASSPHRASE = NatWlanSettingActivity.this.binding.editPass.getText().toString();
            HashMap map = new HashMap();
            map.put(Constants.NatAPConfig, NatWlanSettingActivity.this.natWLANConfigModel);
            IPCManager.getInstance().getDevice(NatWlanSettingActivity.this.device.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.NatWlanSettingActivity.2.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(final boolean z, final Object obj) {
                    new Handler().post(new Runnable() { // from class: activity.NatWlanSettingActivity.2.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Object obj2;
                            if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                return;
                            }
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code")) {
                                if (object.getInteger("code").intValue() == 200) {
                                    SharePreferenceManager.getInstance().setNatWLANSwitchConfig(NatWlanSettingActivity.this.device.getIotId(), JSON.toJSONString(NatWlanSettingActivity.this.natWLANConfigModel));
                                    Toast.makeText(NatWlanSettingActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                } else {
                                    Toast.makeText(NatWlanSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            }
                        }
                    });
                }
            });
        }
    }
}
