package activity;

import adapter.ConnectDeviceAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import bean.ConnectDeviceInfo;
import bean.DeviceInfoBean;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityRouterConnectedDeviceBinding;
import java.util.ArrayList;
import java.util.List;
import sdk.IPCManager;
import tools.SharePreferenceManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class BleRouterConnectedDeviceActivity extends CommonActivity {
    private ActivityRouterConnectedDeviceBinding binding;
    ConnectDeviceAdapter connectDeviceAdapter;
    private DeviceInfoBean device;
    private Handler uiHandler = new Handler();
    List<ConnectDeviceInfo> connectDevices = new ArrayList();

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_router_connected_device;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
        }
        this.binding = (ActivityRouterConnectedDeviceBinding) DataBindingUtil.setContentView(this, R.layout.activity_router_connected_device);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.BleRouterConnectedDeviceActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                BleRouterConnectedDeviceActivity.this.finish();
            }
        });
        if (SharePreferenceManager.getInstance().getNatETHSwitch(this.device.getIotId()) == 1) {
            IPCManager.getInstance().getDevice(this.device.getIotId()).getNatModeConnectDeviceQuery(0, new IPanelCallback() { // from class: activity.BleRouterConnectedDeviceActivity.2
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(final boolean z, @Nullable final Object obj) {
                    BleRouterConnectedDeviceActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterConnectedDeviceActivity.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Object obj2;
                            if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                return;
                            }
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code")) {
                                if (object.getInteger("code").intValue() != 200) {
                                    Toast.makeText(BleRouterConnectedDeviceActivity.this.getActivity(), R.string.restart_dev_failed, 0).show();
                                    return;
                                }
                                BleRouterConnectedDeviceActivity.this.connectDevices.addAll(object.getJSONObject("data").getJSONArray("ConnectDeviceInfo").toJavaList(ConnectDeviceInfo.class));
                                if (BleRouterConnectedDeviceActivity.this.connectDevices.size() != 0) {
                                    BleRouterConnectedDeviceActivity.this.binding.tvNoData.setVisibility(8);
                                }
                                BleRouterConnectedDeviceActivity.this.connectDeviceAdapter = new ConnectDeviceAdapter(BleRouterConnectedDeviceActivity.this, BleRouterConnectedDeviceActivity.this.connectDevices);
                                BleRouterConnectedDeviceActivity.this.binding.rvList.setLayoutManager(new LinearLayoutManager(BleRouterConnectedDeviceActivity.this));
                                BleRouterConnectedDeviceActivity.this.binding.rvList.setAdapter(BleRouterConnectedDeviceActivity.this.connectDeviceAdapter);
                            }
                        }
                    });
                }
            });
        }
        if (SharePreferenceManager.getInstance().getNatWLANSwitch(this.device.getIotId()) == 1) {
            new Handler().postDelayed(new AnonymousClass3(), 500L);
        }
    }

    /* JADX INFO: renamed from: activity.BleRouterConnectedDeviceActivity$3, reason: invalid class name */
    class AnonymousClass3 implements Runnable {
        AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public void run() {
            IPCManager.getInstance().getDevice(BleRouterConnectedDeviceActivity.this.device.getIotId()).getNatModeConnectDeviceQuery(1, new IPanelCallback() { // from class: activity.BleRouterConnectedDeviceActivity.3.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(final boolean z, @Nullable final Object obj) {
                    BleRouterConnectedDeviceActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleRouterConnectedDeviceActivity.3.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Object obj2;
                            if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                return;
                            }
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code")) {
                                if (object.getInteger("code").intValue() != 200) {
                                    Toast.makeText(BleRouterConnectedDeviceActivity.this.getActivity(), R.string.restart_dev_failed, 0).show();
                                    return;
                                }
                                BleRouterConnectedDeviceActivity.this.connectDevices.addAll(object.getJSONObject("data").getJSONArray("ConnectDeviceInfo").toJavaList(ConnectDeviceInfo.class));
                                if (BleRouterConnectedDeviceActivity.this.connectDevices.size() != 0) {
                                    BleRouterConnectedDeviceActivity.this.binding.tvNoData.setVisibility(8);
                                }
                                BleRouterConnectedDeviceActivity.this.connectDeviceAdapter = new ConnectDeviceAdapter(BleRouterConnectedDeviceActivity.this, BleRouterConnectedDeviceActivity.this.connectDevices);
                                BleRouterConnectedDeviceActivity.this.binding.rvList.setLayoutManager(new LinearLayoutManager(BleRouterConnectedDeviceActivity.this));
                                BleRouterConnectedDeviceActivity.this.binding.rvList.setAdapter(BleRouterConnectedDeviceActivity.this.connectDeviceAdapter);
                            }
                        }
                    });
                }
            });
        }
    }
}
