package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import bean.WifiBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.seculink.app.R;
import java.util.List;
import sdk.IPCManager;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.ItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class NetworkInfoActivity extends CommonActivity {
    private TitleView fl_titlebar;
    private String iotId;
    private ItemView itemIp;
    private ItemView itemMac;
    private ItemView itemWifi;
    LinearLayout layout_main;
    private Handler mHandler = new Handler();
    private String ssid;
    private List<WifiBean> wifiBeanList;
    private String wifiJsonString;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_network_info;
    }

    public static void start(Context context, String str) {
        Intent intent = new Intent(context, (Class<?>) NetworkInfoActivity.class);
        intent.putExtra("iotId", str);
        context.startActivity(intent);
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        this.iotId = intent.getStringExtra("iotId");
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.itemWifi = (ItemView) findViewById(R.id.item_wifi);
        this.itemIp = (ItemView) findViewById(R.id.item_ip);
        this.itemMac = (ItemView) findViewById(R.id.item_mac);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.NetworkInfoActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                NetworkInfoActivity.this.finish();
            }
        });
        this.itemWifi.setOnClickListener(new View.OnClickListener() { // from class: activity.NetworkInfoActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                WiFiListActivity.start(NetworkInfoActivity.this.getActivity(), NetworkInfoActivity.this.iotId, NetworkInfoActivity.this.wifiJsonString);
            }
        });
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        showProgressDialog();
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.NetworkInfoActivity.3
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
        getWiFi();
        this.itemIp.setRightText(SharePreferenceManager.getInstance().getDeviceIP(this.iotId));
        this.itemMac.setRightText(SharePreferenceManager.getInstance().getDeviceMAC(this.iotId));
    }

    private void getWiFi() {
        IPCManager.getInstance().getDevice(this.iotId).queryAPList(new IPanelCallback() { // from class: activity.NetworkInfoActivity.4
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(final boolean z, final Object obj) {
                NetworkInfoActivity.this.mHandler.post(new Runnable() { // from class: activity.NetworkInfoActivity.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        NetworkInfoActivity.this.dismissProgressDialog();
                        if (z) {
                            if (obj == null) {
                                return;
                            }
                            NetworkInfoActivity.this.wifiJsonString = obj.toString();
                            IoTResponse ioTResponse = (IoTResponse) JSON.parseObject(NetworkInfoActivity.this.wifiJsonString).toJavaObject(IoTResponse.class);
                            if (ioTResponse.getCode() == 200) {
                                Object data = ioTResponse.getData();
                                if (data == null) {
                                    return;
                                }
                                try {
                                    JSONArray jSONArray = ((JSONObject) data).getJSONArray("APList");
                                    NetworkInfoActivity.this.wifiBeanList = JSON.parseArray(jSONArray.toString(), WifiBean.class);
                                    for (WifiBean wifiBean : NetworkInfoActivity.this.wifiBeanList) {
                                        if (wifiBean.isCurrentWifi()) {
                                            NetworkInfoActivity.this.ssid = wifiBean.getSsid();
                                            NetworkInfoActivity.this.itemWifi.setRightText(NetworkInfoActivity.this.ssid);
                                            return;
                                        }
                                    }
                                    NetworkInfoActivity.this.itemWifi.setRightText(NetworkInfoActivity.this.getString(R.string.not_connect_wifi));
                                    return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                            Toast.makeText(NetworkInfoActivity.this.getActivity(), NetworkInfoActivity.this.getString(R.string.get_wifi_failed), 0).show();
                            return;
                        }
                        Toast.makeText(NetworkInfoActivity.this.getActivity(), NetworkInfoActivity.this.getString(R.string.get_wifi_failed), 0).show();
                    }
                });
            }
        });
    }
}
