package activity;

import adapter.AddDeviceAdapter;
import adapter.ScanBleAdapter;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import bean.AddDeviceModel;
import bean.BluetoothDeviceModel;
import bean.RefreshDevices;
import bean.setFinish;
import bean.updateTimeModify;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.login.widget.ToolTipPopup;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityAddDeviceBinding;
import com.xiaomi.mipush.sdk.Constants;
import config.AppConfig;
import dialog.DialogUtil;
import fragment.HomeTabFragment;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.mozilla.classfile.ByteCode;
import sdk.IPCManager;
import tools.AudioPlayManager;
import tools.BleClient;
import tools.LocationUtil;
import tools.LogEx;
import tools.MyCallback;
import tools.OnMultiClickListener;
import tools.SecuritySharedPreference;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import tools.Utils;
import tools.VerticalSpaceItemDecoration;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class AddDeviceActivity extends CommonActivity {
    private static final int DEVICE_LINK_TIME = 30;
    private static final int GET_DEVICE_ONLINE = 20;
    private static final int GET_DEVICE_STATE = 10;

    /* JADX INFO: renamed from: adapter, reason: collision with root package name */
    private AddDeviceAdapter f1563adapter;
    private ActivityAddDeviceBinding binding;
    private SimpleDateFormat dateFormat;
    private ScanBleAdapter deviceAdapter;
    String pk;
    private final int REQUEST_LOCATION_SERVICE = 0;
    private List<AddDeviceModel> wifiList = new ArrayList();
    private List<AddDeviceModel> netList = new ArrayList();
    private List<AddDeviceModel> networkList = new ArrayList();
    private List<AddDeviceModel> otherList = new ArrayList();
    private List<BluetoothDeviceModel> bleDeviceList = new ArrayList();
    private int DEVICE_LINK_TIME_MAX = 120;
    private boolean isLink = false;
    private boolean isOnline = false;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private int REQUEST_ENABLE_BT = ByteCode.BREAKPOINT;
    private int REQUEST_CODE_BLUETOOTH_CONNECT = 1;
    private boolean isApplyPermission = false;
    private boolean initBle = false;
    private int item = -1;
    private int count = 0;
    private int countMax = 3;
    private int state = -1;
    String iotId = "";
    String dn = "";
    boolean isLoad = false;
    long bingTime = 0;
    private Handler handler = new Handler(new Handler.Callback() { // from class: activity.AddDeviceActivity.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(@NonNull Message message) {
            int i = message.what;
            if (i == 10) {
                BleClient.getInstence().sendOrder2("STATUS?".getBytes(StandardCharsets.UTF_8));
                if (AddDeviceActivity.this.isLink) {
                    AddDeviceActivity.this.updateProgress();
                }
            } else {
                if (i != 20) {
                    if (i == 30) {
                    }
                } else if (!AddDeviceActivity.this.isOnline) {
                    AddDeviceActivity addDeviceActivity = AddDeviceActivity.this;
                    addDeviceActivity.getDeviceONLIne(addDeviceActivity.iotId);
                }
                AddDeviceActivity.access$410(AddDeviceActivity.this);
                if (AddDeviceActivity.this.isLink) {
                    AddDeviceActivity.this.updateLinkProgress();
                }
                if (AddDeviceActivity.this.DEVICE_LINK_TIME_MAX == 0) {
                    AddDeviceActivity addDeviceActivity2 = AddDeviceActivity.this;
                    addDeviceActivity2.startActivity(new Intent(addDeviceActivity2.getActivity(), (Class<?>) BleLinkFailedActivity.class));
                    AddDeviceActivity.this.binding.layoutBleMain.setVisibility(8);
                    AddDeviceActivity.this.binding.layoutWifi.setVisibility(8);
                    AddDeviceActivity.this.binding.layoutBle.setVisibility(0);
                    BleClient.getInstence().disconnectAll();
                    AddDeviceActivity.this.refreshDeviceList();
                    AddDeviceActivity.this.ScanBle();
                }
            }
            return false;
        }
    });
    private AddDeviceAdapter.OnItemClickListener adapterListener = new AddDeviceAdapter.OnItemClickListener() { // from class: activity.AddDeviceActivity.9
        @Override // adapter.AddDeviceAdapter.OnItemClickListener
        public void onItemClick(AddDeviceModel addDeviceModel, int i) {
            switch (addDeviceModel.type) {
                case 1:
                    if (AppConfig.isChina) {
                        Intent intent = new Intent(AddDeviceActivity.this.getActivity(), (Class<?>) ScanActivity.class);
                        intent.putExtra("type", TmpConstant.GROUP_OP_ADD);
                        intent.putExtra("subType", "wifi");
                        AddDeviceActivity.this.startActivity(intent);
                    } else {
                        Intent intent2 = new Intent(AddDeviceActivity.this.getActivity(), (Class<?>) PowerOnActivity.class);
                        intent2.putExtra("type", TmpConstant.GROUP_OP_ADD);
                        intent2.putExtra("subType", "wifi");
                        AddDeviceActivity.this.startActivity(intent2);
                    }
                    AddDeviceActivity.this.finish();
                    return;
                case 2:
                    Intent intent3 = new Intent(AddDeviceActivity.this.getActivity(), (Class<?>) InsertSelectSimActivity.class);
                    intent3.putExtra("subType", "4g");
                    AddDeviceActivity.this.startActivity(intent3);
                    AddDeviceActivity.this.finish();
                    return;
                case 3:
                    HomeTabFragment.setLanConnect();
                    AddDeviceActivity.this.finish();
                    break;
                case 4:
                    break;
                default:
                    return;
            }
            AddDeviceActivity.this.startActivity(new Intent(AddDeviceActivity.this.getActivity(), (Class<?>) SimInputActivity.class));
            AddDeviceActivity.this.finish();
        }
    };
    private OnMultiClickListener tvBleStateListener = new OnMultiClickListener() { // from class: activity.AddDeviceActivity.18
        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            if (AddDeviceActivity.this.binding.tvBleState.isSelected()) {
                AddDeviceActivity.this.ScanBle();
            } else {
                AddDeviceActivity.this.checkPermission();
            }
        }
    };
    private OnMultiClickListener tvLinkListener = new OnMultiClickListener() { // from class: activity.AddDeviceActivity.19
        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            if (!TextUtils.isEmpty(AddDeviceActivity.this.binding.edWifiName.getText().toString())) {
                if (!TextUtils.isEmpty(AddDeviceActivity.this.binding.edWifiPass.getText().toString())) {
                    AddDeviceActivity.this.binding.tvTitle.setText(AddDeviceActivity.this.getString(R.string.insert_card_tips2));
                    AddDeviceActivity addDeviceActivity = AddDeviceActivity.this;
                    addDeviceActivity.saveWifiPassword(addDeviceActivity.binding.edWifiName.getText().toString(), AddDeviceActivity.this.binding.edWifiPass.getText().toString());
                    String str = "SSID:" + AddDeviceActivity.this.binding.edWifiName.getText().toString();
                    String str2 = "PWD:" + AddDeviceActivity.this.binding.edWifiPass.getText().toString();
                    AddDeviceActivity.this.binding.tvLink.setVisibility(8);
                    AddDeviceActivity.this.binding.layoutWifi.setVisibility(8);
                    AddDeviceActivity.this.binding.layoutLinkState.setVisibility(0);
                    AddDeviceActivity.this.binding.tvDeviceLink.setTextColor(AddDeviceActivity.this.getColor(R.color.colors_aeb3c0));
                    AddDeviceActivity.this.binding.tvDeviceBindUser.setTextColor(AddDeviceActivity.this.getColor(R.color.colors_aeb3c0));
                    Glide.with((FragmentActivity) AddDeviceActivity.this).load(Integer.valueOf(R.drawable.gif_scan)).into(AddDeviceActivity.this.binding.ivGif);
                    final byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
                    final byte[] bytes2 = str2.getBytes(StandardCharsets.UTF_8);
                    new Handler().postDelayed(new Runnable() { // from class: activity.AddDeviceActivity.19.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BleClient.getInstence().sendOrder2(bytes);
                        }
                    }, 500L);
                    new Handler().postDelayed(new Runnable() { // from class: activity.AddDeviceActivity.19.2
                        @Override // java.lang.Runnable
                        public void run() {
                            BleClient.getInstence().sendOrder2(bytes2);
                            AddDeviceActivity.this.state = 2;
                            if (AddDeviceActivity.this.isLink) {
                                return;
                            }
                            AddDeviceActivity.this.handler.removeCallbacksAndMessages(null);
                            AddDeviceActivity.this.isLink = true;
                            AddDeviceActivity.this.updateProgress();
                            AddDeviceActivity.this.updateLinkProgress();
                        }
                    }, 700L);
                    AddDeviceActivity.this.DEVICE_LINK_TIME_MAX = 120;
                    return;
                }
                AddDeviceActivity addDeviceActivity2 = AddDeviceActivity.this;
                addDeviceActivity2.showToast(addDeviceActivity2.getString(R.string.input_must_not_be_null));
                return;
            }
            AddDeviceActivity addDeviceActivity3 = AddDeviceActivity.this;
            addDeviceActivity3.showToast(addDeviceActivity3.getString(R.string.input_must_not_be_null));
        }
    };
    private View.OnClickListener ivPassListener = new View.OnClickListener() { // from class: activity.AddDeviceActivity.20
        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            AddDeviceActivity.this.binding.ivPass.setSelected(!AddDeviceActivity.this.binding.ivPass.isSelected());
            if (AddDeviceActivity.this.binding.ivPass.isSelected()) {
                AddDeviceActivity.this.binding.edWifiPass.setInputType(144);
            } else {
                AddDeviceActivity.this.binding.edWifiPass.setInputType(129);
            }
            AddDeviceActivity.this.binding.edWifiPass.setSelection(AddDeviceActivity.this.binding.edWifiPass.getText().toString().trim().length());
        }
    };
    private OnMultiClickListener ivSwitchListener = new OnMultiClickListener() { // from class: activity.AddDeviceActivity.21
        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            XXPermissions.with(AddDeviceActivity.this).permission(Permission.ACCESS_FINE_LOCATION).request(new OnPermissionCallback() { // from class: activity.AddDeviceActivity.21.1
                @Override // com.hjq.permissions.OnPermissionCallback
                public void onGranted(@NonNull List<String> list, boolean z) {
                    if (!z) {
                        AddDeviceActivity.this.showToast(AddDeviceActivity.this.getString(R.string.permissions_not_all));
                    } else if (XXPermissions.isGranted(AddDeviceActivity.this, Permission.ACCESS_FINE_LOCATION)) {
                        AddDeviceActivity.this.startActivityForResult(new Intent("android.settings.WIFI_SETTINGS"), 4641);
                    }
                }

                @Override // com.hjq.permissions.OnPermissionCallback
                public void onDenied(@NonNull List<String> list, boolean z) {
                    if (z) {
                        AddDeviceActivity.this.showToast(AddDeviceActivity.this.getString(R.string.permissions_refuse));
                    } else {
                        AddDeviceActivity.this.showToast(AddDeviceActivity.this.getString(R.string.permissions_fail));
                    }
                }
            });
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_add_device;
    }

    static /* synthetic */ int access$1608(AddDeviceActivity addDeviceActivity) {
        int i = addDeviceActivity.count;
        addDeviceActivity.count = i + 1;
        return i;
    }

    static /* synthetic */ int access$410(AddDeviceActivity addDeviceActivity) {
        int i = addDeviceActivity.DEVICE_LINK_TIME_MAX;
        addDeviceActivity.DEVICE_LINK_TIME_MAX = i - 1;
        return i;
    }

    private String transformTime(int i) {
        String str;
        String str2;
        if (i <= 60) {
            if (i > 9) {
                return "00:" + i;
            }
            return "00:0" + i;
        }
        int i2 = i / 60;
        int i3 = i % 60;
        if (i2 > 9) {
            str = i2 + "";
        } else {
            str = "0" + i2;
        }
        if (i3 > 9) {
            str2 = i3 + "";
        } else {
            str2 = "0" + i3;
        }
        return str + ":" + str2;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityAddDeviceBinding) DataBindingUtil.setContentView(this, R.layout.activity_add_device);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.AddDeviceActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                AddDeviceActivity.this.finish();
            }
        });
        AddDeviceModel addDeviceModel = new AddDeviceModel();
        addDeviceModel.type = 1;
        addDeviceModel.imgSrc = R.drawable.icon_wifi_1;
        addDeviceModel.name = "Indoor Camera";
        addDeviceModel.tips = "(Power Plug)";
        AddDeviceModel addDeviceModel2 = new AddDeviceModel();
        addDeviceModel2.type = 1;
        addDeviceModel2.imgSrc = R.drawable.icon_wiifi_2;
        addDeviceModel2.name = "Outdoor Camera";
        addDeviceModel2.tips = "(Power Plug)";
        AddDeviceModel addDeviceModel3 = new AddDeviceModel();
        addDeviceModel3.type = 1;
        addDeviceModel3.imgSrc = R.drawable.icon_wifi_3;
        addDeviceModel3.name = "Zoom Camera";
        addDeviceModel3.tips = "(Power Plug)";
        AddDeviceModel addDeviceModel4 = new AddDeviceModel();
        addDeviceModel4.type = 1;
        addDeviceModel4.imgSrc = R.drawable.icon_wifi_4;
        addDeviceModel4.name = "Outdoor Camera";
        addDeviceModel4.tips = "(BATTERY)";
        this.wifiList.add(addDeviceModel);
        this.wifiList.add(addDeviceModel2);
        this.wifiList.add(addDeviceModel3);
        AddDeviceModel addDeviceModel5 = new AddDeviceModel();
        addDeviceModel5.type = 2;
        addDeviceModel5.imgSrc = R.drawable.icon_ble_base;
        addDeviceModel5.name = "4G Camera";
        addDeviceModel5.tips = "(BATTERY)";
        AddDeviceModel addDeviceModel6 = new AddDeviceModel();
        addDeviceModel6.type = 2;
        addDeviceModel6.imgSrc = R.drawable.icon_wiifi_2;
        addDeviceModel6.name = "4G Camera";
        addDeviceModel6.tips = "(Power Plug)";
        this.netList.add(addDeviceModel5);
        this.netList.add(addDeviceModel6);
        AddDeviceModel addDeviceModel7 = new AddDeviceModel();
        addDeviceModel7.type = 3;
        addDeviceModel7.imgSrc = R.drawable.icon_wiifi_2;
        addDeviceModel7.name = "Wired Camera";
        addDeviceModel7.tips = "(Power Plug)";
        this.networkList.add(addDeviceModel7);
        AddDeviceModel addDeviceModel8 = new AddDeviceModel();
        addDeviceModel8.type = 4;
        addDeviceModel8.imgSrc = R.drawable.icon_sim_ieme;
        addDeviceModel8.name = "SIM Cards";
        addDeviceModel8.tips = "(ICCID/IMEI)";
        this.otherList.add(addDeviceModel8);
        selectType(0);
        this.binding.rvList.addItemDecoration(new VerticalSpaceItemDecoration(20));
        this.binding.tvWifi.setOnClickListener(new OnMultiClickListener() { // from class: activity.AddDeviceActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                AddDeviceActivity.this.selectType(0);
            }
        });
        this.binding.tv4g.setOnClickListener(new OnMultiClickListener() { // from class: activity.AddDeviceActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                AddDeviceActivity.this.selectType(1);
            }
        });
        this.binding.tvNetworkCable.setOnClickListener(new OnMultiClickListener() { // from class: activity.AddDeviceActivity.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                AddDeviceActivity.this.selectType(2);
            }
        });
        this.binding.tvOtherDevices.setOnClickListener(new OnMultiClickListener() { // from class: activity.AddDeviceActivity.6
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                AddDeviceActivity.this.selectType(3);
            }
        });
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.gif_to_ble)).into(new SimpleTarget<Drawable>() { // from class: activity.AddDeviceActivity.7
            @Override // com.bumptech.glide.request.target.Target
            public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
                onResourceReady((Drawable) obj, (Transition<? super Drawable>) transition);
            }

            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                if (!AddDeviceActivity.this.isFinishing() && (drawable instanceof GifDrawable)) {
                    GifDrawable gifDrawable = (GifDrawable) drawable;
                    gifDrawable.setLoopCount(-1);
                    gifDrawable.start();
                    AddDeviceActivity.this.binding.ivBleState.setImageDrawable(drawable);
                }
            }

            @Override // com.bumptech.glide.request.target.BaseTarget, com.bumptech.glide.request.target.Target
            public void onLoadCleared(@Nullable Drawable drawable) {
                super.onLoadCleared(drawable);
            }
        });
        this.deviceAdapter = new ScanBleAdapter(this, this.bleDeviceList);
        this.binding.listView.setLayoutManager(new LinearLayoutManager(this));
        this.binding.listView.setItemAnimator(new DefaultItemAnimator());
        this.binding.listView.addItemDecoration(new DividerItemDecoration(this, 1));
        this.binding.listView.setAdapter(this.deviceAdapter);
        this.deviceAdapter.setOnItemClickListener(new ScanBleAdapter.OnItemClickListener() { // from class: activity.AddDeviceActivity.8
            @Override // adapter.ScanBleAdapter.OnItemClickListener
            @RequiresApi(api = 26)
            public void onConnectClick(BluetoothDeviceModel bluetoothDeviceModel, int i) {
                if (BleClient.getInstence().getAllDevices().size() > 6) {
                    return;
                }
                bluetoothDeviceModel.getName().split(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                if (!((BluetoothDeviceModel) AddDeviceActivity.this.bleDeviceList.get(i)).isConnect()) {
                    BleClient.getInstence().cancleBleScan();
                    AddDeviceActivity.this.item = i;
                    BleClient instence = BleClient.getInstence();
                    AddDeviceActivity addDeviceActivity = AddDeviceActivity.this;
                    instence.connectBLE(addDeviceActivity, ((BluetoothDeviceModel) addDeviceActivity.bleDeviceList.get(i)).getAddress());
                    return;
                }
                AddDeviceActivity addDeviceActivity2 = AddDeviceActivity.this;
                addDeviceActivity2.showToast(addDeviceActivity2.getString(R.string.linking));
            }

            @Override // adapter.ScanBleAdapter.OnItemClickListener
            public void onDisconnectClick(BluetoothDeviceModel bluetoothDeviceModel, int i) {
                BleClient.getInstence().disconnectAddress(bluetoothDeviceModel.getAddress());
            }
        });
        if (!this.isApplyPermission && ContextCompat.checkSelfPermission(this, Permission.ACCESS_FINE_LOCATION) == 0 && !this.isApplyPermission && ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_ADMIN") == 0 && LocationUtil.isOpenLocationService(getApplicationContext())) {
            if (Build.VERSION.SDK_INT > 30) {
                if (ContextCompat.checkSelfPermission(this, Permission.BLUETOOTH_SCAN) != 0 || ContextCompat.checkSelfPermission(this, Permission.BLUETOOTH_ADVERTISE) != 0 || ContextCompat.checkSelfPermission(this, Permission.BLUETOOTH_CONNECT) != 0) {
                    ActivityCompat.requestPermissions(this, new String[]{Permission.BLUETOOTH_SCAN, Permission.BLUETOOTH_ADVERTISE, Permission.BLUETOOTH_CONNECT}, this.REQUEST_CODE_BLUETOOTH_CONNECT);
                }
            } else {
                this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (this.mBluetoothAdapter.isEnabled()) {
                    initBleListener();
                    if (BleClient.getInstence().isConnected()) {
                        BleClient.getInstence().disconnectAll();
                    }
                }
            }
        }
        this.binding.ivPass.setOnClickListener(this.ivPassListener);
        this.binding.ivSwitch.setOnClickListener(this.ivSwitchListener);
        this.binding.tvBleState.setOnClickListener(this.tvBleStateListener);
        this.binding.tvLink.setOnClickListener(this.tvLinkListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectType(int i) {
        this.binding.tvWifi.setSelected(i == 0);
        this.binding.tv4g.setSelected(i == 1);
        this.binding.tvNetworkCable.setSelected(i == 2);
        this.binding.tvOtherDevices.setSelected(i == 3);
        switch (i) {
            case 0:
                this.f1563adapter = new AddDeviceAdapter(this, this.wifiList);
                break;
            case 1:
                this.f1563adapter = new AddDeviceAdapter(this, this.netList);
                break;
            case 2:
                this.f1563adapter = new AddDeviceAdapter(this, this.networkList);
                break;
            case 3:
                this.f1563adapter = new AddDeviceAdapter(this, this.otherList);
                break;
        }
        RecyclerView.ItemAnimator itemAnimator = this.binding.rvList.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        this.binding.rvList.getItemAnimator().setChangeDuration(0L);
        this.f1563adapter.setHasStableIds(true);
        this.binding.rvList.setLayoutManager(new GridLayoutManager(this, 2));
        this.binding.rvList.setAdapter(this.f1563adapter);
        this.f1563adapter.setOnItemClickListener(this.adapterListener);
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.isLink = false;
        this.handler.removeCallbacksAndMessages(null);
        BleClient.getInstence().disconnectAll();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.isApplyPermission && LocationUtil.isOpenLocationService(getApplicationContext())) {
            initBle();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Permission.ACCESS_COARSE_LOCATION) != 0) {
            DialogUtil.showCancelConfirmDiaLog(this, getString(R.string.no_permission), getString(R.string.open_ble_tips), getString(R.string.cancel), getString(R.string.confirm), new DialogUtil.OnCancelConfirmClickListener() { // from class: activity.AddDeviceActivity.10
                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                public void CancelListener() {
                }

                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                public void ConfirmListener() {
                    XXPermissions.with(AddDeviceActivity.this).permission(Permission.ACCESS_COARSE_LOCATION).permission(Permission.ACCESS_FINE_LOCATION).request(new OnPermissionCallback() { // from class: activity.AddDeviceActivity.10.1
                        @Override // com.hjq.permissions.OnPermissionCallback
                        public void onDenied(@NonNull List<String> list, boolean z) {
                        }

                        @Override // com.hjq.permissions.OnPermissionCallback
                        public void onGranted(@NonNull List<String> list, boolean z) {
                            if (XXPermissions.isGranted(AddDeviceActivity.this, Permission.ACCESS_COARSE_LOCATION)) {
                                AddDeviceActivity.this.checkLocationService();
                            }
                        }
                    });
                }
            });
        } else {
            checkLocationService();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkLocationService() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!LocationUtil.isOpenLocationService(getApplicationContext())) {
                Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                this.isApplyPermission = true;
                startActivityForResult(intent, 0);
                return;
            }
            initBle();
            return;
        }
        initBle();
    }

    private void initBle() {
        if (Build.VERSION.SDK_INT > 30) {
            if (ContextCompat.checkSelfPermission(this, Permission.BLUETOOTH_SCAN) == 0 && ContextCompat.checkSelfPermission(this, Permission.BLUETOOTH_ADVERTISE) == 0 && ContextCompat.checkSelfPermission(this, Permission.BLUETOOTH_CONNECT) == 0) {
                return;
            }
            ActivityCompat.requestPermissions(this, new String[]{Permission.BLUETOOTH_SCAN, Permission.BLUETOOTH_ADVERTISE, Permission.BLUETOOTH_CONNECT}, this.REQUEST_CODE_BLUETOOTH_CONNECT);
            return;
        }
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!this.mBluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), this.REQUEST_ENABLE_BT);
        } else {
            initBleListener();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == this.REQUEST_CODE_BLUETOOTH_CONNECT && iArr.length > 0 && iArr[0] == 0) {
            this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!this.mBluetoothAdapter.isEnabled()) {
                startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), this.REQUEST_ENABLE_BT);
            } else {
                initBleListener();
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == this.REQUEST_ENABLE_BT && i2 == -1) {
            initBleListener();
            return;
        }
        if (i == 0 && i2 == -1) {
            this.isApplyPermission = false;
            initBle();
            return;
        }
        if (i == 10002 && i2 == -1) {
            setResult(-1);
            finish();
        } else if (i == 777 && i2 == -1) {
            setResult(-1);
            finish();
        } else if (i == 4641) {
            getSSid();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProgress() {
        if (this.isLink) {
            Message messageObtain = Message.obtain();
            messageObtain.what = 10;
            this.handler.sendMessageDelayed(messageObtain, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOnlineProgress() {
        if (this.isOnline) {
            return;
        }
        Message messageObtain = Message.obtain();
        messageObtain.what = 20;
        this.handler.sendMessageDelayed(messageObtain, 1000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLinkProgress() {
        if (this.isLink) {
            Message messageObtain = Message.obtain();
            messageObtain.what = 30;
            this.handler.sendMessageDelayed(messageObtain, 1000L);
            this.binding.tvDeviceLinkTime.setText(transformTime(this.DEVICE_LINK_TIME_MAX));
        }
    }

    private void initBleListener() {
        if (this.initBle) {
            return;
        }
        this.initBle = true;
        BleClient.getInstence().setBleScanListener(new BleClient.BleScanListener() { // from class: activity.AddDeviceActivity.11
            @Override // tools.BleClient.BleScanListener
            public void onScanning(ScanResult scanResult) {
                BluetoothDevice device = scanResult.getDevice();
                if (device == null || device.getName() == null || device.getName().equals("") || AddDeviceActivity.this.bleDeviceList.contains(device)) {
                    return;
                }
                Log.d(AddDeviceActivity.this.TAG, "搜索到的蓝牙名称：" + device.getName());
                Iterator it = AddDeviceActivity.this.bleDeviceList.iterator();
                while (it.hasNext()) {
                    if (device.getAddress().equals(((BluetoothDeviceModel) it.next()).getAddress())) {
                        return;
                    }
                }
                if (device.getName().contains("ipc_xmy") || device.getName().contains("RouterA")) {
                    BluetoothDeviceModel bluetoothDeviceModel = new BluetoothDeviceModel();
                    bluetoothDeviceModel.setAddress(device.getAddress());
                    bluetoothDeviceModel.setName(device.getName());
                    bluetoothDeviceModel.setConnect(false);
                    bluetoothDeviceModel.DeviceModel = -1;
                    AddDeviceActivity.this.bleDeviceList.add(bluetoothDeviceModel);
                    AddDeviceActivity.this.deviceAdapter.notifyDataSetChanged();
                    AddDeviceActivity.this.binding.tvBleState.setVisibility(8);
                    AddDeviceActivity.this.binding.ivBleState.setVisibility(8);
                    AddDeviceActivity.this.binding.tvBle.setText("" + AddDeviceActivity.this.getString(R.string.scan_devices_count) + " (" + AddDeviceActivity.this.bleDeviceList.size() + ")");
                }
            }
        });
        BleClient.getInstence().setBleConnectListener(new AnonymousClass12());
        BleClient.getInstence().setBleReceiveListener(new AnonymousClass13());
        ScanBle();
    }

    /* JADX INFO: renamed from: activity.AddDeviceActivity$12, reason: invalid class name */
    class AnonymousClass12 implements BleClient.BleConnectListener {
        AnonymousClass12() {
        }

        @Override // tools.BleClient.BleConnectListener
        public void onStartConnect() {
            AddDeviceActivity addDeviceActivity = AddDeviceActivity.this;
            addDeviceActivity.showLoadingDialog(addDeviceActivity.getString(R.string.device_link));
            new Handler().postDelayed(new Runnable() { // from class: activity.AddDeviceActivity.12.1
                @Override // java.lang.Runnable
                public void run() {
                    if (BleClient.bleClient.isConnected()) {
                        return;
                    }
                    AddDeviceActivity.this.showLoadingSuccessDialog(AddDeviceActivity.this.getString(R.string.ble_link_fail), 500);
                }
            }, 4000L);
        }

        @Override // tools.BleClient.BleConnectListener
        public void onSuccessConnect() {
            AddDeviceActivity.this.runOnUiThread(new Runnable() { // from class: activity.AddDeviceActivity.12.2
                @Override // java.lang.Runnable
                public void run() {
                    AddDeviceActivity.this.showLoadingSuccessDialog(AddDeviceActivity.this.getString(R.string.ble_link_success), 100);
                    ((BluetoothDeviceModel) AddDeviceActivity.this.bleDeviceList.get(AddDeviceActivity.this.item)).setConnect(true);
                    AddDeviceActivity.this.deviceAdapter.notifyItemChanged(AddDeviceActivity.this.item);
                    AddDeviceActivity.this.deviceAdapter.notifyDataSetChanged();
                    AddDeviceActivity.this.pk = "";
                    AddDeviceActivity.this.dn = "";
                    AddDeviceActivity.this.state = -1;
                    AddDeviceActivity.this.binding.layoutBle.setVisibility(8);
                    AddDeviceActivity.this.binding.layoutLinkState.setVisibility(8);
                    AddDeviceActivity.this.binding.tvLink.setVisibility(0);
                    AddDeviceActivity.this.binding.layoutBleMain.setVisibility(0);
                    AddDeviceActivity.this.binding.layoutWifi.setVisibility(0);
                    new Handler().postDelayed(new Runnable() { // from class: activity.AddDeviceActivity.12.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BleClient.getInstence().sendOrder2("PK&DN?".getBytes(StandardCharsets.UTF_8));
                        }
                    }, 1500L);
                    String[] strArrSplit = BleClient.getInstence().getAllDevices().get(0).getDevice().getName().split(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                    if (strArrSplit[strArrSplit.length - 1].length() == 7 || BleClient.getInstence().getAllDevices().get(0).getDevice().getName().contains("RouterA")) {
                        AddDeviceActivity.this.handler.removeCallbacksAndMessages(null);
                        AddDeviceActivity.this.isLink = false;
                        if (AddDeviceActivity.this.state != 3) {
                            AddDeviceActivity.this.bingTime = System.currentTimeMillis();
                            AddDeviceActivity.this.state = 3;
                            AddDeviceActivity.this.count = 0;
                            AddDeviceActivity.this.binding.layoutWifi.setVisibility(8);
                            AddDeviceActivity.this.binding.layoutBle.setVisibility(8);
                            AddDeviceActivity.this.binding.tvLink.setVisibility(8);
                            AddDeviceActivity.this.binding.layoutBleMain.setVisibility(0);
                            AddDeviceActivity.this.binding.layoutLinkState.setVisibility(0);
                            AddDeviceActivity.this.binding.tvDeviceLink.setText(R.string.device_network_tips2);
                            if (!AddDeviceActivity.this.isLink) {
                                AddDeviceActivity.this.handler.removeCallbacksAndMessages(null);
                                AddDeviceActivity.this.isLink = true;
                                AddDeviceActivity.this.updateProgress();
                                AddDeviceActivity.this.updateLinkProgress();
                            }
                            AddDeviceActivity.this.DEVICE_LINK_TIME_MAX = 120;
                            new Handler().postDelayed(new Runnable() { // from class: activity.AddDeviceActivity.12.2.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    AddDeviceActivity.this.bindUser();
                                }
                            }, 2500L);
                            return;
                        }
                        return;
                    }
                    AddDeviceActivity.this.getSSid();
                    AudioPlayManager.newBuilder().load(AddDeviceActivity.this, Integer.valueOf(R.raw.input_wifi), AudioPlayManager.MediaPlayerBuilder.TYPE.RAW).build().play();
                }
            });
        }

        @Override // tools.BleClient.BleConnectListener
        public void onFailConnect() {
            AddDeviceActivity.this.handler.post(new Runnable() { // from class: activity.AddDeviceActivity.12.3
                @Override // java.lang.Runnable
                public void run() {
                    AddDeviceActivity.this.isLink = false;
                    AddDeviceActivity addDeviceActivity = AddDeviceActivity.this;
                    if (addDeviceActivity == null || addDeviceActivity.isFinishing()) {
                        return;
                    }
                    DialogUtil.showConfirmDiaLog(AddDeviceActivity.this, AddDeviceActivity.this.getString(R.string.ble_link_fail_tips), AddDeviceActivity.this.getString(R.string.confirm), new DialogUtil.OnConfirmClickListener() { // from class: activity.AddDeviceActivity.12.3.1
                        @Override // dialog.DialogUtil.OnConfirmClickListener
                        public void ConfirmListener() {
                            AddDeviceActivity.this.binding.layoutBleMain.setVisibility(8);
                            AddDeviceActivity.this.binding.layoutWifi.setVisibility(8);
                            AddDeviceActivity.this.binding.layoutBle.setVisibility(0);
                            AddDeviceActivity.this.refreshDeviceList();
                            AddDeviceActivity.this.ScanBle();
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: renamed from: activity.AddDeviceActivity$13, reason: invalid class name */
    class AnonymousClass13 implements BleClient.BleReceiveListener {
        AnonymousClass13() {
        }

        @Override // tools.BleClient.BleReceiveListener
        public void onDeviceSendData(final String str, final String str2, final String str3) {
            AddDeviceActivity.this.handler.post(new Runnable() { // from class: activity.AddDeviceActivity.13.1
                @Override // java.lang.Runnable
                public void run() {
                    Log.e("BleClient", "消息内容: name=" + str + " mac=" + str2 + "  data= " + str3);
                    if ("STATUS=wifi_success".equals(str3)) {
                        AddDeviceActivity.this.handler.removeCallbacksAndMessages(null);
                        AddDeviceActivity.this.isLink = false;
                        if (AddDeviceActivity.this.state != 3) {
                            AddDeviceActivity.this.bingTime = System.currentTimeMillis();
                            AddDeviceActivity.this.state = 3;
                            AddDeviceActivity.this.count = 0;
                            AddDeviceActivity.this.binding.layoutWifi.setVisibility(8);
                            AddDeviceActivity.this.binding.layoutBle.setVisibility(8);
                            AddDeviceActivity.this.binding.tvLink.setVisibility(8);
                            AddDeviceActivity.this.binding.layoutBleMain.setVisibility(0);
                            AddDeviceActivity.this.binding.layoutLinkState.setVisibility(0);
                            AddDeviceActivity.this.binding.tvDeviceLink.setTextColor(AddDeviceActivity.this.getColor(R.color.color_tf_green));
                            new Handler().postDelayed(new Runnable() { // from class: activity.AddDeviceActivity.13.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    AddDeviceActivity.this.bindUser();
                                }
                            }, 5000L);
                        }
                    } else if ("STATUS=wifi_failed".equals(str3)) {
                        AddDeviceActivity.this.handler.removeCallbacksAndMessages(null);
                        AddDeviceActivity.this.isLink = false;
                        if (AddDeviceActivity.this.state != 4) {
                            DialogUtil.showCancelConfirmDiaLog(AddDeviceActivity.this, AddDeviceActivity.this.getString(R.string.set_wifi_failed), AddDeviceActivity.this.getString(R.string.text_t), AddDeviceActivity.this.getString(R.string.cancel), AddDeviceActivity.this.getString(R.string.try_again), new DialogUtil.OnCancelConfirmClickListener() { // from class: activity.AddDeviceActivity.13.1.2
                                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                                public void CancelListener() {
                                    AddDeviceActivity.this.isLoad = false;
                                    AddDeviceActivity.this.binding.layoutBleMain.setVisibility(8);
                                    AddDeviceActivity.this.binding.layoutWifi.setVisibility(8);
                                    AddDeviceActivity.this.binding.layoutBle.setVisibility(0);
                                    BleClient.getInstence().disconnectAll();
                                    AddDeviceActivity.this.refreshDeviceList();
                                    AddDeviceActivity.this.ScanBle();
                                }

                                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                                public void ConfirmListener() {
                                    AddDeviceActivity.this.binding.layoutBle.setVisibility(8);
                                    AddDeviceActivity.this.binding.layoutBleMain.setVisibility(0);
                                    AddDeviceActivity.this.binding.layoutWifi.setVisibility(0);
                                    AddDeviceActivity.this.binding.layoutLinkState.setVisibility(8);
                                    AddDeviceActivity.this.getSSid();
                                    AddDeviceActivity.this.state = 4;
                                }
                            });
                        }
                    } else if ("STATUS=wifi_wait".equals(str3)) {
                        if (AddDeviceActivity.this.state != 1) {
                            AddDeviceActivity.this.binding.layoutBle.setVisibility(8);
                            AddDeviceActivity.this.binding.layoutBleMain.setVisibility(0);
                            AddDeviceActivity.this.binding.layoutLinkState.setVisibility(8);
                            AddDeviceActivity.this.binding.layoutWifi.setVisibility(0);
                            AddDeviceActivity.this.getSSid();
                            AddDeviceActivity.this.state = 1;
                        }
                    } else if (("STATUS=wifi_connecting".equals(str3) || "STATUS=wifi_find".equals(str3)) && AddDeviceActivity.this.state != 2) {
                        AddDeviceActivity.this.binding.layoutWifi.setVisibility(8);
                        AddDeviceActivity.this.binding.layoutLinkState.setVisibility(0);
                        AddDeviceActivity.this.binding.tvLink.setText(R.string.ble_net_linking);
                        AddDeviceActivity.this.state = 2;
                    }
                    if (str3.contains(AlinkConstants.KEY_PK) && str3.contains(AlinkConstants.KEY_PK)) {
                        String[] strArrSplit = str3.split("&");
                        AddDeviceActivity.this.pk = strArrSplit[0].replace("pk=", "");
                        AddDeviceActivity.this.dn = strArrSplit[1].replace("dn=", "");
                        Log.e("BleClient", "pk=" + AddDeviceActivity.this.pk + "  dn=" + AddDeviceActivity.this.dn);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindUser() {
        if (this.count >= this.countMax) {
            return;
        }
        if (this.pk.isEmpty() || this.dn.isEmpty()) {
            BleClient.getInstence().sendOrder2("PK&DN?".getBytes(StandardCharsets.UTF_8));
            new Handler().postDelayed(new Runnable() { // from class: activity.AddDeviceActivity.14
                @Override // java.lang.Runnable
                public void run() {
                    AddDeviceActivity.this.bindUser();
                }
            }, 4000L);
        } else {
            HashMap map = new HashMap();
            map.put("productKey", this.pk);
            map.put("deviceName", this.dn);
            new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/awss/time/window/user/bind").setScheme(Scheme.HTTPS).setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass15());
        }
    }

    /* JADX INFO: renamed from: activity.AddDeviceActivity$15, reason: invalid class name */
    class AnonymousClass15 implements IoTCallback {
        AnonymousClass15() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, AddDeviceActivity.this.TAG, "onFailure");
            AddDeviceActivity.access$1608(AddDeviceActivity.this);
            AddDeviceActivity.this.bindUser();
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            int code = ioTResponse.getCode();
            Log.e(AddDeviceActivity.this.TAG, "onResponse: code: " + code);
            Log.e("BleClient", "" + code + "  " + ioTResponse.getLocalizedMsg());
            ioTResponse.getMessage();
            if (code != 200) {
                AddDeviceActivity addDeviceActivity = AddDeviceActivity.this;
                if (addDeviceActivity == null || addDeviceActivity.isFinishing()) {
                    return;
                }
                addDeviceActivity.runOnUiThread(new AnonymousClass1(code));
                return;
            }
            Log.e("耗时", "awss/time/window/user/bind  " + (System.currentTimeMillis() - AddDeviceActivity.this.bingTime));
            AddDeviceActivity addDeviceActivity2 = AddDeviceActivity.this;
            if (addDeviceActivity2 == null || addDeviceActivity2.isFinishing()) {
                return;
            }
            AddDeviceActivity.this.iotId = (String) ioTResponse.getData();
            if (AddDeviceActivity.this.iotId.isEmpty()) {
                return;
            }
            AddDeviceActivity.this.isLink = false;
            AddDeviceActivity.this.updateOnlineProgress();
            SettingsCtrl.getInstance().getProperties(AddDeviceActivity.this.iotId, new MyCallback() { // from class: activity.AddDeviceActivity.15.2
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
        }

        /* JADX INFO: renamed from: activity.AddDeviceActivity$15$1, reason: invalid class name */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ int val$code;

            AnonymousClass1(int i) {
                this.val$code = i;
            }

            @Override // java.lang.Runnable
            public void run() {
                AddDeviceActivity.access$1608(AddDeviceActivity.this);
                int i = this.val$code;
                if (i == 6618) {
                    if (AddDeviceActivity.this.isLoad) {
                        return;
                    }
                    if (AddDeviceActivity.this.count < AddDeviceActivity.this.countMax) {
                        AddDeviceActivity.this.bindUser();
                        return;
                    } else {
                        AddDeviceActivity.this.isLoad = true;
                        DialogUtil.showConfirmDiaLog(AddDeviceActivity.this, AddDeviceActivity.this.getString(R.string.err_6618), AddDeviceActivity.this.getString(R.string.confirm), new DialogUtil.OnConfirmClickListener() { // from class: activity.AddDeviceActivity.15.1.1
                            @Override // dialog.DialogUtil.OnConfirmClickListener
                            public void ConfirmListener() {
                                AddDeviceActivity.this.isLoad = false;
                                AddDeviceActivity.this.binding.layoutBleMain.setVisibility(8);
                                AddDeviceActivity.this.binding.layoutWifi.setVisibility(8);
                                AddDeviceActivity.this.binding.layoutBle.setVisibility(0);
                                BleClient.getInstence().disconnectAll();
                                AddDeviceActivity.this.refreshDeviceList();
                                AddDeviceActivity.this.ScanBle();
                            }
                        });
                        return;
                    }
                }
                if (i == 6221) {
                    if (AddDeviceActivity.this.isLoad) {
                        return;
                    }
                    if (AddDeviceActivity.this.count >= AddDeviceActivity.this.countMax) {
                        AddDeviceActivity.this.isLoad = true;
                        DialogUtil.showConfirmDiaLog(AddDeviceActivity.this, AddDeviceActivity.this.getString(R.string.device_offline_tf), AddDeviceActivity.this.getString(R.string.confirm), new DialogUtil.OnConfirmClickListener() { // from class: activity.AddDeviceActivity.15.1.2
                            @Override // dialog.DialogUtil.OnConfirmClickListener
                            public void ConfirmListener() {
                                AddDeviceActivity.this.isLoad = false;
                                AddDeviceActivity.this.binding.layoutBleMain.setVisibility(8);
                                AddDeviceActivity.this.binding.layoutWifi.setVisibility(8);
                                AddDeviceActivity.this.binding.layoutBle.setVisibility(0);
                                BleClient.getInstence().disconnectAll();
                                AddDeviceActivity.this.refreshDeviceList();
                                AddDeviceActivity.this.ScanBle();
                            }
                        });
                        return;
                    } else {
                        new Handler().postDelayed(new Runnable() { // from class: activity.AddDeviceActivity.15.1.3
                            @Override // java.lang.Runnable
                            public void run() {
                                AddDeviceActivity.this.bindUser();
                            }
                        }, ToolTipPopup.DEFAULT_POPUP_DISPLAY_TIME);
                        return;
                    }
                }
                if (AddDeviceActivity.this.isLoad) {
                    return;
                }
                if (AddDeviceActivity.this.count >= AddDeviceActivity.this.countMax) {
                    AddDeviceActivity.this.isLoad = true;
                    DialogUtil.showConfirmDiaLog(AddDeviceActivity.this, AddDeviceActivity.this.getString(R.string.device_binded), AddDeviceActivity.this.getString(R.string.confirm), new DialogUtil.OnConfirmClickListener() { // from class: activity.AddDeviceActivity.15.1.4
                        @Override // dialog.DialogUtil.OnConfirmClickListener
                        public void ConfirmListener() {
                            AddDeviceActivity.this.isLoad = false;
                            AddDeviceActivity.this.binding.layoutBleMain.setVisibility(8);
                            AddDeviceActivity.this.binding.layoutWifi.setVisibility(8);
                            AddDeviceActivity.this.binding.layoutBle.setVisibility(0);
                            BleClient.getInstence().disconnectAll();
                            AddDeviceActivity.this.refreshDeviceList();
                            AddDeviceActivity.this.ScanBle();
                        }
                    });
                } else {
                    DialogUtil.showCancelConfirmDiaLog(AddDeviceActivity.this, AddDeviceActivity.this.getString(R.string.device_unbind), AddDeviceActivity.this.getString(R.string.device_binded), AddDeviceActivity.this.getString(R.string.cancel), AddDeviceActivity.this.getString(R.string.unbind), new DialogUtil.OnCancelConfirmClickListener() { // from class: activity.AddDeviceActivity.15.1.5
                        @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                        public void CancelListener() {
                            AddDeviceActivity.this.isLoad = false;
                            AddDeviceActivity.this.binding.layoutBleMain.setVisibility(8);
                            AddDeviceActivity.this.binding.layoutWifi.setVisibility(8);
                            AddDeviceActivity.this.binding.layoutBle.setVisibility(0);
                            BleClient.getInstence().disconnectAll();
                            AddDeviceActivity.this.refreshDeviceList();
                            AddDeviceActivity.this.ScanBle();
                        }

                        @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                        public void ConfirmListener() {
                            AddDeviceActivity.this.isLoad = false;
                            BleClient.getInstence().sendOrder2("UNBIND?".getBytes(StandardCharsets.UTF_8));
                            new Handler().postDelayed(new Runnable() { // from class: activity.AddDeviceActivity.15.1.5.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    AddDeviceActivity.this.bindUser();
                                }
                            }, ToolTipPopup.DEFAULT_POPUP_DISPLAY_TIME);
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getDeviceONLIne(String str) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/thing/info/get").setApiVersion("1.0.4").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass16(str));
    }

    /* JADX INFO: renamed from: activity.AddDeviceActivity$16, reason: invalid class name */
    class AnonymousClass16 implements IoTCallback {
        final /* synthetic */ String val$iotId;

        AnonymousClass16(String str) {
            this.val$iotId = str;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            AddDeviceActivity.this.updateOnlineProgress();
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            LogEx.e(true, AddDeviceActivity.this.TAG, "triggerRecordPlan:" + ioTResponse.getData() + "");
            if (ioTResponse.getCode() != 200) {
                AddDeviceActivity.this.updateOnlineProgress();
                return;
            }
            int intValue = JSON.parseObject(ioTResponse.getData().toString()).getIntValue("status");
            Log.e("BleClient 设备在线状态", "" + intValue);
            if (intValue == 1) {
                AddDeviceActivity.this.isOnline = true;
                final String userPhone = Utils.getUserPhone();
                if (!TextUtils.isEmpty(userPhone)) {
                    HashMap map = new HashMap();
                    map.put(config.Constants.DEVICE_OWNER, userPhone);
                    IPCManager.getInstance().getDevice(this.val$iotId).setProperties(map, new IPanelCallback() { // from class: activity.AddDeviceActivity.16.1
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, Object obj) {
                            Log.e("BleClient:绑定所有者", z + "      " + String.valueOf(obj));
                            AddDeviceActivity.this.runOnUiThread(new Runnable() { // from class: activity.AddDeviceActivity.16.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    AddDeviceActivity.this.binding.layoutBleMain.setVisibility(8);
                                    AddDeviceActivity.this.binding.layoutWifi.setVisibility(8);
                                    AddDeviceActivity.this.binding.layoutBle.setVisibility(0);
                                    AddDeviceActivity.this.showToast(AddDeviceActivity.this.getString(R.string.bind_success));
                                    EventBus.getDefault().post(new updateTimeModify(AnonymousClass16.this.val$iotId));
                                    EventBus.getDefault().post(new RefreshDevices());
                                    SharePreferenceManager.getInstance().setDeviceOwner(AnonymousClass16.this.val$iotId, userPhone);
                                    EventBus.getDefault().post(new setFinish("activity.scanNavigationActivity"));
                                    AddDeviceActivity.this.finish();
                                }
                            });
                        }
                    });
                    return;
                }
                AddDeviceActivity.this.runOnUiThread(new Runnable() { // from class: activity.AddDeviceActivity.16.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AddDeviceActivity.this.binding.layoutBleMain.setVisibility(8);
                        AddDeviceActivity.this.binding.layoutWifi.setVisibility(8);
                        AddDeviceActivity.this.binding.layoutBle.setVisibility(0);
                        AddDeviceActivity.this.showToast(AddDeviceActivity.this.getString(R.string.bind_success));
                        EventBus.getDefault().post(new updateTimeModify(AnonymousClass16.this.val$iotId));
                        EventBus.getDefault().post(new RefreshDevices());
                        EventBus.getDefault().post(new setFinish("activity.scanNavigationActivity"));
                        AddDeviceActivity.this.finish();
                    }
                });
                return;
            }
            AddDeviceActivity.this.updateOnlineProgress();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ScanBle() {
        this.binding.tvBleState.setVisibility(8);
        this.binding.tvBle.setText(R.string.ble_scaning);
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.gif_scan)).into(this.binding.ivBleState);
        BleClient.getInstence().cancleBleScan();
        refreshDeviceList();
        BleClient.getInstence().scanBLE(this);
        new Handler().postDelayed(new Runnable() { // from class: activity.AddDeviceActivity.17
            @Override // java.lang.Runnable
            public void run() {
                if (AddDeviceActivity.this.bleDeviceList.size() == 0) {
                    AddDeviceActivity.this.binding.tvBle.setText(R.string.ble_tips);
                    AddDeviceActivity.this.binding.tvBleState.setText(R.string.search_again);
                    AddDeviceActivity.this.binding.tvBleState.setVisibility(0);
                    AddDeviceActivity.this.binding.tvBleState.setSelected(true);
                    AddDeviceActivity.this.binding.ivBleState.setVisibility(8);
                    if (AddDeviceActivity.this.isFinishing()) {
                        return;
                    }
                    AddDeviceActivity addDeviceActivity = AddDeviceActivity.this;
                }
            }
        }, 25000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshDeviceList() {
        this.bleDeviceList.clear();
        for (BluetoothGatt bluetoothGatt : BleClient.getInstence().getAllDevices()) {
            BluetoothDeviceModel bluetoothDeviceModel = new BluetoothDeviceModel();
            bluetoothDeviceModel.setConnect(true);
            bluetoothDeviceModel.setName("" + bluetoothGatt.getDevice().getName());
            bluetoothDeviceModel.setAddress("" + bluetoothGatt.getDevice().getAddress());
            this.bleDeviceList.add(bluetoothDeviceModel);
        }
        this.deviceAdapter.notifyDataSetChanged();
    }

    public void getSSid() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            String extraInfo = activeNetworkInfo.getExtraInfo();
            if (((ConnectivityManager) getSystemService("connectivity")).getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
                if (!TextUtils.isEmpty(extraInfo) && !"<unknown ssid>".equals(extraInfo)) {
                    updateSSID(extraInfo);
                    return;
                }
                try {
                    updateSSID(((WifiManager) getApplicationContext().getSystemService("wifi")).getConnectionInfo().getSSID());
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() { // from class: activity.AddDeviceActivity.22
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(AddDeviceActivity.this.getApplicationContext(), e.getMessage(), 0).show();
                        }
                    });
                    updateSSID("");
                    return;
                }
            }
            updateSSID("");
            return;
        }
        updateSSID("");
    }

    public void updateSSID(String str) {
        if (!TextUtils.isEmpty(str) && !"<unknown ssid>".equals(str)) {
            if (str.startsWith("\"") && str.endsWith("\"")) {
                str = str.substring(1, str.length() - 1);
            }
            this.binding.edWifiName.setText(str + "");
            this.binding.edWifiName.setSelection(this.binding.edWifiName.getText().length());
            this.binding.edWifiPass.setText(getWifiPassword(this.binding.edWifiName.getText().toString()));
            return;
        }
        this.binding.edWifiName.setText("");
        this.binding.edWifiName.setSelection(0);
        this.binding.edWifiPass.setText("");
    }

    public String getWifiPassword(String str) {
        return new SecuritySharedPreference(getApplicationContext(), "wifiConfig", 0).getString(str, "");
    }

    public void saveWifiPassword(String str, String str2) {
        SecuritySharedPreference.SecurityEditor securityEditorEdit = new SecuritySharedPreference(getApplicationContext(), "wifiConfig", 0).edit();
        securityEditorEdit.putString(str, str2);
        securityEditorEdit.apply();
    }
}
