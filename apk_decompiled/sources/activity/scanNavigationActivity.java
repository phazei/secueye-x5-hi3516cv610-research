package activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import bean.setFinish;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.seculink.app.R;
import config.AppConfig;
import fragment.HomeTabFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class scanNavigationActivity extends CommonActivity implements View.OnClickListener {
    private TextView all_text;
    private TextView ble_cable_text;
    LinearLayout bluetooth_connection_ll;
    TitleView fl_titlebar;
    LinearLayout fourG_device_connection_ll;
    private TextView fourg_text;
    RelativeLayout layout_main;
    LinearLayout network_cable_connection_ll;
    private TextView network_cable_text;
    LinearLayout qr_code_connection_ll;
    LinearLayout smart_connect_ll;
    private View view1;
    private View view2;
    private View view3;
    private View view4;
    private TextView wlan_device_text;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.scan_navigation;
    }

    @Override // activity.CommonActivity
    protected void initData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setFinish(setFinish setfinish) {
        Log.e("事件", "" + setfinish.getData());
        if (getLocalClassName().equals(setfinish.getData())) {
            finish();
        }
        Log.e("事件", "" + getLocalClassName());
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.layout_main = (RelativeLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.smart_connect_ll = (LinearLayout) findViewById(R.id.smart_connect_ll);
        this.qr_code_connection_ll = (LinearLayout) findViewById(R.id.qr_code_connection_ll);
        this.fourG_device_connection_ll = (LinearLayout) findViewById(R.id.fourG_device_connection_ll);
        this.network_cable_connection_ll = (LinearLayout) findViewById(R.id.network_cable_connection_ll);
        this.bluetooth_connection_ll = (LinearLayout) findViewById(R.id.bluetooth_connection_ll);
        this.smart_connect_ll.setOnClickListener(this);
        this.qr_code_connection_ll.setOnClickListener(this);
        this.fourG_device_connection_ll.setOnClickListener(this);
        this.network_cable_connection_ll.setOnClickListener(this);
        this.bluetooth_connection_ll.setOnClickListener(this);
        this.view1 = findViewById(R.id.view1);
        this.view2 = findViewById(R.id.view2);
        this.view3 = findViewById(R.id.view3);
        this.view4 = findViewById(R.id.view4);
        this.all_text = (TextView) findViewById(R.id.all_text);
        this.wlan_device_text = (TextView) findViewById(R.id.wlan_device_text);
        this.fourg_text = (TextView) findViewById(R.id.fourg_text);
        this.network_cable_text = (TextView) findViewById(R.id.network_cable_text);
        this.ble_cable_text = (TextView) findViewById(R.id.ble_cable_text);
        this.all_text.setOnClickListener(this);
        this.wlan_device_text.setOnClickListener(this);
        this.fourg_text.setOnClickListener(this);
        this.network_cable_text.setOnClickListener(this);
        this.ble_cable_text.setOnClickListener(this);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.scanNavigationActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                scanNavigationActivity.this.finish();
            }
        });
    }

    @Override // android.view.View.OnClickListener
    @SuppressLint({"NonConstantResourceId"})
    public void onClick(View view2) {
        switch (view2.getId()) {
            case R.id.all_text /* 2131296387 */:
                this.bluetooth_connection_ll.setVisibility(0);
                this.smart_connect_ll.setVisibility(0);
                this.qr_code_connection_ll.setVisibility(0);
                this.fourG_device_connection_ll.setVisibility(0);
                this.network_cable_connection_ll.setVisibility(0);
                this.view1.setVisibility(0);
                this.view2.setVisibility(0);
                this.view3.setVisibility(0);
                this.view4.setVisibility(0);
                this.all_text.setBackgroundResource(R.color.color_white);
                this.wlan_device_text.setBackgroundResource(R.color.colorPrimary);
                this.fourg_text.setBackgroundResource(R.color.colorPrimary);
                this.network_cable_text.setBackgroundResource(R.color.colorPrimary);
                this.ble_cable_text.setBackgroundResource(R.color.colorPrimary);
                break;
            case R.id.ble_cable_text /* 2131296438 */:
                this.bluetooth_connection_ll.setVisibility(0);
                this.smart_connect_ll.setVisibility(8);
                this.qr_code_connection_ll.setVisibility(8);
                this.fourG_device_connection_ll.setVisibility(8);
                this.network_cable_connection_ll.setVisibility(8);
                this.view1.setVisibility(8);
                this.view2.setVisibility(8);
                this.view3.setVisibility(8);
                this.view4.setVisibility(0);
                this.all_text.setBackgroundResource(R.color.colorPrimary);
                this.wlan_device_text.setBackgroundResource(R.color.colorPrimary);
                this.fourg_text.setBackgroundResource(R.color.colorPrimary);
                this.network_cable_text.setBackgroundResource(R.color.colorPrimary);
                this.ble_cable_text.setBackgroundResource(R.color.color_white);
                break;
            case R.id.bluetooth_connection_ll /* 2131296440 */:
                if (AppConfig.isChina) {
                    startActivity(new Intent(getActivity(), (Class<?>) BleScantActivity.class));
                } else {
                    Intent intent = new Intent(getActivity(), (Class<?>) PowerOnActivity.class);
                    intent.putExtra("type", "scan");
                    intent.putExtra("subType", "ble");
                    startActivity(intent);
                }
                break;
            case R.id.fourG_device_connection_ll /* 2131296791 */:
                Intent intent2 = new Intent(getActivity(), (Class<?>) ScanActivity.class);
                intent2.putExtra("subType", "4g");
                startActivity(intent2);
                break;
            case R.id.fourg_text /* 2131296795 */:
                this.smart_connect_ll.setVisibility(8);
                this.qr_code_connection_ll.setVisibility(8);
                this.fourG_device_connection_ll.setVisibility(0);
                this.network_cable_connection_ll.setVisibility(8);
                this.bluetooth_connection_ll.setVisibility(8);
                this.view1.setVisibility(8);
                this.view2.setVisibility(8);
                this.view3.setVisibility(0);
                this.view4.setVisibility(8);
                this.all_text.setBackgroundResource(R.color.colorPrimary);
                this.wlan_device_text.setBackgroundResource(R.color.colorPrimary);
                this.fourg_text.setBackgroundResource(R.color.color_white);
                this.network_cable_text.setBackgroundResource(R.color.colorPrimary);
                this.ble_cable_text.setBackgroundResource(R.color.colorPrimary);
                break;
            case R.id.network_cable_connection_ll /* 2131297376 */:
                HomeTabFragment.setLanConnect();
                finish();
                break;
            case R.id.network_cable_text /* 2131297377 */:
                this.smart_connect_ll.setVisibility(8);
                this.qr_code_connection_ll.setVisibility(8);
                this.fourG_device_connection_ll.setVisibility(8);
                this.bluetooth_connection_ll.setVisibility(8);
                this.network_cable_connection_ll.setVisibility(0);
                this.view1.setVisibility(8);
                this.view2.setVisibility(8);
                this.view3.setVisibility(8);
                this.view4.setVisibility(8);
                this.all_text.setBackgroundResource(R.color.colorPrimary);
                this.wlan_device_text.setBackgroundResource(R.color.colorPrimary);
                this.fourg_text.setBackgroundResource(R.color.colorPrimary);
                this.network_cable_text.setBackgroundResource(R.color.color_white);
                this.ble_cable_text.setBackgroundResource(R.color.colorPrimary);
                break;
            case R.id.qr_code_connection_ll /* 2131297502 */:
                if (AppConfig.isChina) {
                    Intent intent3 = new Intent(getActivity(), (Class<?>) ScanActivity.class);
                    intent3.putExtra("type", TmpConstant.GROUP_OP_ADD);
                    intent3.putExtra("subType", "wifi");
                    startActivity(intent3);
                } else {
                    Intent intent4 = new Intent(getActivity(), (Class<?>) PowerOnActivity.class);
                    intent4.putExtra("type", TmpConstant.GROUP_OP_ADD);
                    intent4.putExtra("subType", "wifi");
                    startActivity(intent4);
                }
                break;
            case R.id.smart_connect_ll /* 2131297663 */:
                if (AppConfig.isChina) {
                    Intent intent5 = new Intent(getActivity(), (Class<?>) ScanActivity.class);
                    intent5.putExtra("type", "scan");
                    intent5.putExtra("subType", "wifi");
                    startActivity(intent5);
                } else {
                    Intent intent6 = new Intent(getActivity(), (Class<?>) PowerOnActivity.class);
                    intent6.putExtra("type", "scan");
                    intent6.putExtra("subType", "wifi");
                    startActivity(intent6);
                }
                break;
            case R.id.wlan_device_text /* 2131298101 */:
                this.smart_connect_ll.setVisibility(0);
                this.qr_code_connection_ll.setVisibility(0);
                this.fourG_device_connection_ll.setVisibility(8);
                this.network_cable_connection_ll.setVisibility(8);
                this.bluetooth_connection_ll.setVisibility(8);
                this.view1.setVisibility(0);
                this.view2.setVisibility(0);
                this.view3.setVisibility(8);
                this.view4.setVisibility(8);
                this.all_text.setBackgroundResource(R.color.colorPrimary);
                this.wlan_device_text.setBackgroundResource(R.color.color_white);
                this.fourg_text.setBackgroundResource(R.color.colorPrimary);
                this.network_cable_text.setBackgroundResource(R.color.colorPrimary);
                this.ble_cable_text.setBackgroundResource(R.color.colorPrimary);
                break;
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }
}
