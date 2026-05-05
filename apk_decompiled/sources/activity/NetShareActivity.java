package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import bean.DeviceInfoBean;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityNetShareBinding;
import config.Constants;
import dialog.BaseDialog;
import java.util.HashMap;
import sdk.IPCManager;
import tools.OnMultiClickListener;
import tools.SharePreferenceManager;

/* JADX INFO: loaded from: classes.dex */
public class NetShareActivity extends CommonActivity {
    private SwitchCompat NatETHSwitch;
    private SwitchCompat NatWLANSwitch;
    private ActivityNetShareBinding binding;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean device2;
    private DeviceInfoBean nvrDevice;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_net_share;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityNetShareBinding) DataBindingUtil.setContentView(this, R.layout.activity_net_share);
        setEdgeToEdge(this.binding.layoutMain);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.device2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
            Log.d("分享", "device: " + this.device.getIotId());
            if (this.device1 != null) {
                Log.d("分享", "device1: " + this.device1.getIotId());
            }
            if (this.device2 != null) {
                Log.d("分享", "device2: " + this.device2.getIotId());
            }
            if (this.nvrDevice != null) {
                Log.d("分享", "nvrDevice: " + this.nvrDevice.getIotId());
            }
        }
        this.binding.layoutAp.setVisibility(SharePreferenceManager.getInstance().getNatWLANSwitchShow(this.device.getIotId()) == 1 ? 0 : 8);
        this.binding.layoutNet.setVisibility(SharePreferenceManager.getInstance().getNatETHSwitchShow(this.device.getIotId()) == 1 ? 0 : 8);
        this.binding.itemNatEthSwitch.addRightView(new SwitchCompat(this));
        this.NatETHSwitch = (SwitchCompat) this.binding.itemNatEthSwitch.getRightView();
        this.NatETHSwitch.setTextOff("");
        this.NatETHSwitch.setTextOn("");
        this.NatETHSwitch.setText("");
        this.NatETHSwitch.setThumbDrawable(null);
        this.NatETHSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.NatETHSwitch.setChecked(SharePreferenceManager.getInstance().getNatETHSwitch(this.device.getIotId()) == 1);
        this.NatETHSwitch.setOnClickListener(new OnMultiClickListener() { // from class: activity.NetShareActivity.1
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                HashMap map = new HashMap();
                map.put(Constants.NatETHSwitch, Integer.valueOf(NetShareActivity.this.NatETHSwitch.isChecked() ? 1 : 0));
                IPCManager.getInstance().getDevice(NetShareActivity.this.device.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.NetShareActivity.1.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            SharePreferenceManager.getInstance().setNatETHSwitch(NetShareActivity.this.device.getIotId(), NetShareActivity.this.NatETHSwitch.isChecked() ? 1 : 0);
                        }
                    }
                });
            }
        });
        this.binding.itemNatWlanSwitch.addRightView(new SwitchCompat(this));
        this.NatWLANSwitch = (SwitchCompat) this.binding.itemNatWlanSwitch.getRightView();
        this.NatWLANSwitch.setTextOff("");
        this.NatWLANSwitch.setTextOn("");
        this.NatWLANSwitch.setText("");
        this.NatWLANSwitch.setThumbDrawable(null);
        this.NatWLANSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.NatWLANSwitch.setChecked(SharePreferenceManager.getInstance().getNatWLANSwitch(this.device.getIotId()) == 1);
        this.NatWLANSwitch.setOnClickListener(new OnMultiClickListener() { // from class: activity.NetShareActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                HashMap map = new HashMap();
                map.put(Constants.NatWLANSwitch, Integer.valueOf(NetShareActivity.this.NatWLANSwitch.isChecked() ? 1 : 0));
                IPCManager.getInstance().getDevice(NetShareActivity.this.device.getIotId()).setProperties(map, new IPanelCallback() { // from class: activity.NetShareActivity.2.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            SharePreferenceManager.getInstance().setNatWLANSwitch(NetShareActivity.this.device.getIotId(), NetShareActivity.this.NatWLANSwitch.isChecked() ? 1 : 0);
                        }
                    }
                });
            }
        });
        this.binding.itemApHotspot.setOnClickListener(new OnMultiClickListener() { // from class: activity.NetShareActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(NetShareActivity.this, (Class<?>) NatWlanSettingActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, NetShareActivity.this.device);
                intent.putExtras(bundle2);
                NetShareActivity.this.startActivity(intent);
            }
        });
        this.binding.btSave.setOnClickListener(new AnonymousClass4());
    }

    /* JADX INFO: renamed from: activity.NetShareActivity$4, reason: invalid class name */
    class AnonymousClass4 extends OnMultiClickListener {
        AnonymousClass4() {
        }

        /* JADX INFO: renamed from: activity.NetShareActivity$4$1, reason: invalid class name */
        class AnonymousClass1 implements View.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCManager.getInstance().getDevice(NetShareActivity.this.device.getIotId()).reboot(new IPanelCallback() { // from class: activity.NetShareActivity.4.1.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(final boolean z, final Object obj) {
                        new Handler().post(new Runnable() { // from class: activity.NetShareActivity.4.1.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Object obj2;
                                Log.e(NetShareActivity.this.TAG, "reboot onComplete: " + z);
                                if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                    return;
                                }
                                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                if (object.containsKey("code")) {
                                    if (object.getInteger("code").intValue() != 200) {
                                        Toast.makeText(NetShareActivity.this.getActivity(), R.string.restart_dev_failed, 0).show();
                                    } else {
                                        Toast.makeText(NetShareActivity.this.getActivity(), R.string.restart_dev_succeed, 0).show();
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(NetShareActivity.this.getString(R.string.ap_name_save)).leftBtnText(NetShareActivity.this.getString(R.string.sure_restart)).rightBtnText(NetShareActivity.this.getString(R.string.cancel)).clickLeft(new AnonymousClass1()).canCancel(false).create().show(NetShareActivity.this.getSupportFragmentManager(), "");
        }
    }
}
