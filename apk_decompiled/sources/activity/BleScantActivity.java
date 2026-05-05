package activity;

import adapter.ScanBleAdapter;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import bean.BluetoothDeviceModel;
import bean.RefreshDevices;
import bean.setFinish;
import bean.updateTimeModify;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.bumptech.glide.Glide;
import com.facebook.login.widget.ToolTipPopup;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityBleScanBinding;
import com.xiaomi.mipush.sdk.Constants;
import dialog.DialogUtil;
import java.nio.charset.StandardCharsets;
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

/* JADX INFO: loaded from: classes.dex */
public class BleScantActivity extends CommonActivity {
    private static final int GET_DEVICE_ONLINE = 20;
    private static final int GET_DEVICE_STATE = 10;
    private ActivityBleScanBinding binding;
    private ScanBleAdapter deviceAdapter;
    String pk;
    private final int REQUEST_LOCATION_SERVICE = 0;
    private List<BluetoothDeviceModel> bleDeviceList = new ArrayList();
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
    private Handler handler = new Handler(new Handler.Callback() { // from class: activity.BleScantActivity.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(@NonNull Message message) {
            int i = message.what;
            if (i != 10) {
                if (i != 20 || BleScantActivity.this.isOnline) {
                    return false;
                }
                BleScantActivity bleScantActivity = BleScantActivity.this;
                bleScantActivity.getDeviceONLIne(bleScantActivity.iotId);
                return false;
            }
            BleClient.getInstence().sendOrder2("STATUS?".getBytes(StandardCharsets.UTF_8));
            if (!BleScantActivity.this.isLink) {
                return false;
            }
            BleScantActivity.this.updateProgress();
            return false;
        }
    });
    private OnMultiClickListener tvBleStateListener = new OnMultiClickListener() { // from class: activity.BleScantActivity.12
        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            if (BleScantActivity.this.binding.tvBleState.isSelected()) {
                BleScantActivity.this.ScanBle();
            } else {
                BleScantActivity.this.checkPermission();
            }
        }
    };
    private OnMultiClickListener tvLinkListener = new OnMultiClickListener() { // from class: activity.BleScantActivity.13
        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            if (!TextUtils.isEmpty(BleScantActivity.this.binding.edWifiName.getText().toString())) {
                if (TextUtils.isEmpty(BleScantActivity.this.binding.edWifiPass.getText().toString())) {
                    BleScantActivity bleScantActivity = BleScantActivity.this;
                    bleScantActivity.showToast(bleScantActivity.getString(R.string.input_must_not_be_null));
                    return;
                }
                BleScantActivity bleScantActivity2 = BleScantActivity.this;
                bleScantActivity2.saveWifiPassword(bleScantActivity2.binding.edWifiName.getText().toString(), BleScantActivity.this.binding.edWifiPass.getText().toString());
                String str = "SSID:" + BleScantActivity.this.binding.edWifiName.getText().toString();
                String str2 = "PWD:" + BleScantActivity.this.binding.edWifiPass.getText().toString();
                BleScantActivity.this.binding.tvLink.setVisibility(8);
                BleScantActivity.this.binding.layoutWifi.setVisibility(8);
                BleScantActivity.this.binding.layoutLinkState.setVisibility(0);
                BleScantActivity.this.binding.ivDeviceLink.setImageResource(R.drawable.anim_loading);
                BleScantActivity.this.binding.ivDeviceBindUser.setImageResource(R.drawable.wifi_checkdefault);
                final byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
                final byte[] bytes2 = str2.getBytes(StandardCharsets.UTF_8);
                new Handler().postDelayed(new Runnable() { // from class: activity.BleScantActivity.13.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BleClient.getInstence().sendOrder2(bytes);
                    }
                }, 1000L);
                new Handler().postDelayed(new Runnable() { // from class: activity.BleScantActivity.13.2
                    @Override // java.lang.Runnable
                    public void run() {
                        BleClient.getInstence().sendOrder2(bytes2);
                        BleScantActivity.this.state = 2;
                        if (BleScantActivity.this.isLink) {
                            return;
                        }
                        BleScantActivity.this.handler.removeCallbacksAndMessages(null);
                        BleScantActivity.this.isLink = true;
                        BleScantActivity.this.updateProgress();
                    }
                }, 1300L);
                return;
            }
            BleScantActivity bleScantActivity3 = BleScantActivity.this;
            bleScantActivity3.showToast(bleScantActivity3.getString(R.string.input_must_not_be_null));
        }
    };
    private View.OnClickListener ivPassListener = new View.OnClickListener() { // from class: activity.BleScantActivity.14
        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            BleScantActivity.this.binding.ivPass.setSelected(!BleScantActivity.this.binding.ivPass.isSelected());
            if (BleScantActivity.this.binding.ivPass.isSelected()) {
                BleScantActivity.this.binding.edWifiPass.setInputType(144);
            } else {
                BleScantActivity.this.binding.edWifiPass.setInputType(129);
            }
            BleScantActivity.this.binding.edWifiPass.setSelection(BleScantActivity.this.binding.edWifiPass.getText().toString().trim().length());
        }
    };
    private OnMultiClickListener ivSwitchListener = new OnMultiClickListener() { // from class: activity.BleScantActivity.15
        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            XXPermissions.with(BleScantActivity.this).permission(Permission.ACCESS_FINE_LOCATION).request(new OnPermissionCallback() { // from class: activity.BleScantActivity.15.1
                @Override // com.hjq.permissions.OnPermissionCallback
                public void onGranted(@NonNull List<String> list, boolean z) {
                    if (!z) {
                        BleScantActivity.this.showToast(BleScantActivity.this.getString(R.string.permissions_not_all));
                    } else if (XXPermissions.isGranted(BleScantActivity.this, Permission.ACCESS_FINE_LOCATION)) {
                        BleScantActivity.this.startActivityForResult(new Intent("android.settings.WIFI_SETTINGS"), 4641);
                    }
                }

                @Override // com.hjq.permissions.OnPermissionCallback
                public void onDenied(@NonNull List<String> list, boolean z) {
                    if (z) {
                        BleScantActivity.this.showToast(BleScantActivity.this.getString(R.string.permissions_refuse));
                    } else {
                        BleScantActivity.this.showToast(BleScantActivity.this.getString(R.string.permissions_fail));
                    }
                }
            });
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_ble_scan;
    }

    static /* synthetic */ int access$1108(BleScantActivity bleScantActivity) {
        int i = bleScantActivity.count;
        bleScantActivity.count = i + 1;
        return i;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityBleScanBinding) DataBindingUtil.setContentView(this, R.layout.activity_ble_scan);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.BleScantActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                BleScantActivity.this.finish();
            }
        });
        this.deviceAdapter = new ScanBleAdapter(this, this.bleDeviceList);
        this.binding.listView.setLayoutManager(new LinearLayoutManager(this));
        this.binding.listView.setItemAnimator(new DefaultItemAnimator());
        this.binding.listView.addItemDecoration(new DividerItemDecoration(this, 1));
        this.binding.listView.setAdapter(this.deviceAdapter);
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.anim_loading)).into(this.binding.ivScanning);
        this.deviceAdapter.setOnItemClickListener(new ScanBleAdapter.OnItemClickListener() { // from class: activity.BleScantActivity.3
            @Override // adapter.ScanBleAdapter.OnItemClickListener
            @RequiresApi(api = 26)
            public void onConnectClick(BluetoothDeviceModel bluetoothDeviceModel, int i) {
                if (BleClient.getInstence().getAllDevices().size() > 6) {
                    return;
                }
                if (bluetoothDeviceModel.getName().split(Constants.ACCEPT_TIME_SEPARATOR_SERVER)[r4.length - 1].length() != 6) {
                    if (!((BluetoothDeviceModel) BleScantActivity.this.bleDeviceList.get(i)).isConnect()) {
                        BleClient.getInstence().cancleBleScan();
                        BleScantActivity.this.item = i;
                        BleClient instence = BleClient.getInstence();
                        BleScantActivity bleScantActivity = BleScantActivity.this;
                        instence.connectBLE(bleScantActivity, ((BluetoothDeviceModel) bleScantActivity.bleDeviceList.get(i)).getAddress());
                        return;
                    }
                    BleScantActivity bleScantActivity2 = BleScantActivity.this;
                    bleScantActivity2.showToast(bleScantActivity2.getString(R.string.linking));
                    return;
                }
                BleScantActivity bleScantActivity3 = BleScantActivity.this;
                DialogUtil.showTipsConfirmDiaLog(bleScantActivity3, bleScantActivity3.getString(R.string.ble_link_fail), BleScantActivity.this.getString(R.string.ble_bind), BleScantActivity.this.getString(R.string.confirm));
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
            DialogUtil.showCancelConfirmDiaLog(this, getString(R.string.no_permission), getString(R.string.open_ble_tips), getString(R.string.cancel), getString(R.string.confirm), new DialogUtil.OnCancelConfirmClickListener() { // from class: activity.BleScantActivity.4
                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                public void CancelListener() {
                }

                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                public void ConfirmListener() {
                    XXPermissions.with(BleScantActivity.this).permission(Permission.ACCESS_COARSE_LOCATION).permission(Permission.ACCESS_FINE_LOCATION).request(new OnPermissionCallback() { // from class: activity.BleScantActivity.4.1
                        @Override // com.hjq.permissions.OnPermissionCallback
                        public void onDenied(@NonNull List<String> list, boolean z) {
                        }

                        @Override // com.hjq.permissions.OnPermissionCallback
                        public void onGranted(@NonNull List<String> list, boolean z) {
                            if (XXPermissions.isGranted(BleScantActivity.this, Permission.ACCESS_COARSE_LOCATION)) {
                                BleScantActivity.this.checkLocationService();
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

    private void initBleListener() {
        if (this.initBle) {
            return;
        }
        this.initBle = true;
        BleClient.getInstence().setBleScanListener(new BleClient.BleScanListener() { // from class: activity.BleScantActivity.5
            @Override // tools.BleClient.BleScanListener
            public void onScanning(ScanResult scanResult) {
                BluetoothDevice device = scanResult.getDevice();
                if (device == null || device.getName() == null || device.getName().equals("") || BleScantActivity.this.bleDeviceList.contains(device)) {
                    return;
                }
                Log.d(BleScantActivity.this.TAG, "搜索到的蓝牙名称：" + device.getName());
                Iterator it = BleScantActivity.this.bleDeviceList.iterator();
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
                    BleScantActivity.this.bleDeviceList.add(bluetoothDeviceModel);
                    BleScantActivity.this.deviceAdapter.notifyDataSetChanged();
                    BleScantActivity.this.binding.tvBleState.setVisibility(8);
                }
            }
        });
        BleClient.getInstence().setBleConnectListener(new AnonymousClass6());
        BleClient.getInstence().setBleReceiveListener(new AnonymousClass7());
        ScanBle();
    }

    /* JADX INFO: renamed from: activity.BleScantActivity$6, reason: invalid class name */
    class AnonymousClass6 implements BleClient.BleConnectListener {
        AnonymousClass6() {
        }

        @Override // tools.BleClient.BleConnectListener
        public void onStartConnect() {
            BleScantActivity bleScantActivity = BleScantActivity.this;
            bleScantActivity.showLoadingDialog(bleScantActivity.getString(R.string.device_link));
            new Handler().postDelayed(new Runnable() { // from class: activity.BleScantActivity.6.1
                @Override // java.lang.Runnable
                public void run() {
                    if (BleClient.bleClient.isConnected()) {
                        return;
                    }
                    BleScantActivity.this.showLoadingSuccessDialog(BleScantActivity.this.getString(R.string.ble_link_fail), 500);
                }
            }, 4000L);
        }

        @Override // tools.BleClient.BleConnectListener
        public void onSuccessConnect() {
            BleScantActivity.this.runOnUiThread(new Runnable() { // from class: activity.BleScantActivity.6.2
                @Override // java.lang.Runnable
                public void run() {
                    BleScantActivity.this.showLoadingSuccessDialog(BleScantActivity.this.getString(R.string.ble_link_success), 100);
                    ((BluetoothDeviceModel) BleScantActivity.this.bleDeviceList.get(BleScantActivity.this.item)).setConnect(true);
                    BleScantActivity.this.deviceAdapter.notifyItemChanged(BleScantActivity.this.item);
                    BleScantActivity.this.deviceAdapter.notifyDataSetChanged();
                    BleScantActivity.this.pk = "";
                    BleScantActivity.this.dn = "";
                    BleScantActivity.this.state = -1;
                    new Handler().postDelayed(new Runnable() { // from class: activity.BleScantActivity.6.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BleClient.getInstence().sendOrder2("PK&DN?".getBytes(StandardCharsets.UTF_8));
                        }
                    }, 1500L);
                    String[] strArrSplit = BleClient.getInstence().getAllDevices().get(0).getDevice().getName().split(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                    if (strArrSplit[strArrSplit.length - 1].length() == 7 || BleClient.getInstence().getAllDevices().get(0).getDevice().getName().contains("RouterA")) {
                        BleScantActivity.this.handler.removeCallbacksAndMessages(null);
                        BleScantActivity.this.isLink = false;
                        if (BleScantActivity.this.state != 3) {
                            BleScantActivity.this.bingTime = System.currentTimeMillis();
                            BleScantActivity.this.state = 3;
                            BleScantActivity.this.count = 0;
                            BleScantActivity.this.binding.layoutWifi.setVisibility(8);
                            BleScantActivity.this.binding.layoutBle.setVisibility(8);
                            BleScantActivity.this.binding.layoutBleMain.setVisibility(0);
                            BleScantActivity.this.binding.layoutLinkState.setVisibility(0);
                            BleScantActivity.this.binding.ivDeviceLink.setImageResource(R.drawable.success);
                            BleScantActivity.this.binding.tvDeviceLink.setText(R.string.connect_successful);
                            BleScantActivity.this.binding.ivDeviceBindUser.setImageResource(R.drawable.anim_loading);
                            new Handler().postDelayed(new Runnable() { // from class: activity.BleScantActivity.6.2.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    BleScantActivity.this.bindUser();
                                }
                            }, 2500L);
                            return;
                        }
                        return;
                    }
                    BleScantActivity.this.binding.layoutBle.setVisibility(8);
                    BleScantActivity.this.binding.layoutLinkState.setVisibility(8);
                    BleScantActivity.this.binding.tvLink.setVisibility(0);
                    BleScantActivity.this.binding.layoutBleMain.setVisibility(0);
                    BleScantActivity.this.binding.layoutWifi.setVisibility(0);
                    BleScantActivity.this.getSSid();
                    AudioPlayManager.newBuilder().load(BleScantActivity.this, Integer.valueOf(R.raw.input_wifi), AudioPlayManager.MediaPlayerBuilder.TYPE.RAW).build().play();
                }
            });
        }

        @Override // tools.BleClient.BleConnectListener
        public void onFailConnect() {
            BleScantActivity.this.handler.post(new Runnable() { // from class: activity.BleScantActivity.6.3
                @Override // java.lang.Runnable
                public void run() {
                    BleScantActivity.this.isLink = false;
                    BleScantActivity bleScantActivity = BleScantActivity.this;
                    if (bleScantActivity == null || bleScantActivity.isFinishing()) {
                        return;
                    }
                    DialogUtil.showConfirmDiaLog(BleScantActivity.this, BleScantActivity.this.getString(R.string.ble_link_fail_tips), BleScantActivity.this.getString(R.string.confirm), new DialogUtil.OnConfirmClickListener() { // from class: activity.BleScantActivity.6.3.1
                        @Override // dialog.DialogUtil.OnConfirmClickListener
                        public void ConfirmListener() {
                            BleScantActivity.this.binding.layoutBleMain.setVisibility(8);
                            BleScantActivity.this.binding.layoutWifi.setVisibility(8);
                            BleScantActivity.this.binding.layoutBle.setVisibility(0);
                            BleScantActivity.this.refreshDeviceList();
                            BleScantActivity.this.ScanBle();
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: renamed from: activity.BleScantActivity$7, reason: invalid class name */
    class AnonymousClass7 implements BleClient.BleReceiveListener {
        AnonymousClass7() {
        }

        @Override // tools.BleClient.BleReceiveListener
        public void onDeviceSendData(final String str, final String str2, final String str3) {
            BleScantActivity.this.handler.post(new Runnable() { // from class: activity.BleScantActivity.7.1
                @Override // java.lang.Runnable
                public void run() {
                    Log.e("BleClient", "消息内容: name=" + str + " mac=" + str2 + "  data= " + str3);
                    if ("STATUS=wifi_success".equals(str3)) {
                        BleScantActivity.this.handler.removeCallbacksAndMessages(null);
                        BleScantActivity.this.isLink = false;
                        if (BleScantActivity.this.state != 3) {
                            BleScantActivity.this.bingTime = System.currentTimeMillis();
                            BleScantActivity.this.state = 3;
                            BleScantActivity.this.count = 0;
                            BleScantActivity.this.binding.layoutWifi.setVisibility(8);
                            BleScantActivity.this.binding.layoutBle.setVisibility(8);
                            BleScantActivity.this.binding.layoutBleMain.setVisibility(0);
                            BleScantActivity.this.binding.layoutLinkState.setVisibility(0);
                            BleScantActivity.this.binding.ivDeviceLink.setImageResource(R.drawable.success);
                            BleScantActivity.this.binding.tvDeviceLink.setText(R.string.connect_successful);
                            BleScantActivity.this.binding.ivDeviceBindUser.setImageResource(R.drawable.anim_loading);
                            new Handler().postDelayed(new Runnable() { // from class: activity.BleScantActivity.7.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    BleScantActivity.this.bindUser();
                                }
                            }, 5000L);
                        }
                    } else if ("STATUS=wifi_failed".equals(str3)) {
                        BleScantActivity.this.handler.removeCallbacksAndMessages(null);
                        BleScantActivity.this.isLink = false;
                        if (BleScantActivity.this.state != 4) {
                            DialogUtil.showCancelConfirmDiaLog(BleScantActivity.this, BleScantActivity.this.getString(R.string.set_wifi_failed), BleScantActivity.this.getString(R.string.text_t), BleScantActivity.this.getString(R.string.cancel), BleScantActivity.this.getString(R.string.try_again), new DialogUtil.OnCancelConfirmClickListener() { // from class: activity.BleScantActivity.7.1.2
                                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                                public void CancelListener() {
                                    BleScantActivity.this.isLoad = false;
                                    BleScantActivity.this.binding.layoutBleMain.setVisibility(8);
                                    BleScantActivity.this.binding.layoutWifi.setVisibility(8);
                                    BleScantActivity.this.binding.layoutBle.setVisibility(0);
                                    BleClient.getInstence().disconnectAll();
                                    BleScantActivity.this.refreshDeviceList();
                                    BleScantActivity.this.ScanBle();
                                }

                                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                                public void ConfirmListener() {
                                    BleScantActivity.this.binding.layoutBle.setVisibility(8);
                                    BleScantActivity.this.binding.layoutBleMain.setVisibility(0);
                                    BleScantActivity.this.binding.tvLink.setVisibility(0);
                                    BleScantActivity.this.binding.layoutWifi.setVisibility(0);
                                    BleScantActivity.this.binding.layoutLinkState.setVisibility(8);
                                    BleScantActivity.this.getSSid();
                                    BleScantActivity.this.state = 4;
                                }
                            });
                        }
                    } else if ("STATUS=wifi_wait".equals(str3)) {
                        if (BleScantActivity.this.state != 1) {
                            BleScantActivity.this.binding.layoutBle.setVisibility(8);
                            BleScantActivity.this.binding.layoutBleMain.setVisibility(0);
                            BleScantActivity.this.binding.layoutLinkState.setVisibility(8);
                            BleScantActivity.this.binding.layoutWifi.setVisibility(0);
                            BleScantActivity.this.binding.tvLink.setVisibility(0);
                            BleScantActivity.this.getSSid();
                            BleScantActivity.this.state = 1;
                        }
                    } else if (("STATUS=wifi_connecting".equals(str3) || "STATUS=wifi_find".equals(str3)) && BleScantActivity.this.state != 2) {
                        BleScantActivity.this.binding.layoutWifi.setVisibility(8);
                        BleScantActivity.this.binding.layoutLinkState.setVisibility(0);
                        BleScantActivity.this.binding.tvLink.setText(R.string.ble_net_linking);
                        BleScantActivity.this.binding.ivDeviceLink.setImageResource(R.drawable.anim_loading);
                        BleScantActivity.this.state = 2;
                    }
                    if (str3.contains(AlinkConstants.KEY_PK) && str3.contains(AlinkConstants.KEY_PK)) {
                        String[] strArrSplit = str3.split("&");
                        BleScantActivity.this.pk = strArrSplit[0].replace("pk=", "");
                        BleScantActivity.this.dn = strArrSplit[1].replace("dn=", "");
                        Log.e("BleClient", "pk=" + BleScantActivity.this.pk + "  dn=" + BleScantActivity.this.dn);
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
            new Handler().postDelayed(new Runnable() { // from class: activity.BleScantActivity.8
                @Override // java.lang.Runnable
                public void run() {
                    BleScantActivity.this.bindUser();
                }
            }, 4000L);
        } else {
            HashMap map = new HashMap();
            map.put("productKey", this.pk);
            map.put("deviceName", this.dn);
            new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/awss/time/window/user/bind").setScheme(Scheme.HTTPS).setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass9());
        }
    }

    /* JADX INFO: renamed from: activity.BleScantActivity$9, reason: invalid class name */
    class AnonymousClass9 implements IoTCallback {
        AnonymousClass9() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, BleScantActivity.this.TAG, "onFailure");
            BleScantActivity.access$1108(BleScantActivity.this);
            BleScantActivity.this.bindUser();
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            int code = ioTResponse.getCode();
            Log.e(BleScantActivity.this.TAG, "onResponse: code: " + code);
            Log.e("BleClient", "" + code + "  " + ioTResponse.getLocalizedMsg());
            ioTResponse.getMessage();
            if (code != 200) {
                BleScantActivity bleScantActivity = BleScantActivity.this;
                if (bleScantActivity == null || bleScantActivity.isFinishing()) {
                    return;
                }
                bleScantActivity.runOnUiThread(new AnonymousClass1(code));
                return;
            }
            Log.e("耗时", "awss/time/window/user/bind  " + (System.currentTimeMillis() - BleScantActivity.this.bingTime));
            BleScantActivity bleScantActivity2 = BleScantActivity.this;
            if (bleScantActivity2 == null || bleScantActivity2.isFinishing()) {
                return;
            }
            BleScantActivity.this.iotId = (String) ioTResponse.getData();
            if (BleScantActivity.this.iotId.isEmpty()) {
                return;
            }
            BleScantActivity.this.isLink = false;
            BleScantActivity.this.updateOnlineProgress();
            SettingsCtrl.getInstance().getProperties(BleScantActivity.this.iotId, new MyCallback() { // from class: activity.BleScantActivity.9.2
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
        }

        /* JADX INFO: renamed from: activity.BleScantActivity$9$1, reason: invalid class name */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ int val$code;

            AnonymousClass1(int i) {
                this.val$code = i;
            }

            @Override // java.lang.Runnable
            public void run() {
                BleScantActivity.access$1108(BleScantActivity.this);
                int i = this.val$code;
                if (i == 6618) {
                    if (BleScantActivity.this.isLoad) {
                        return;
                    }
                    if (BleScantActivity.this.count < BleScantActivity.this.countMax) {
                        BleScantActivity.this.bindUser();
                        return;
                    } else {
                        BleScantActivity.this.isLoad = true;
                        DialogUtil.showConfirmDiaLog(BleScantActivity.this, BleScantActivity.this.getString(R.string.err_6618), BleScantActivity.this.getString(R.string.confirm), new DialogUtil.OnConfirmClickListener() { // from class: activity.BleScantActivity.9.1.1
                            @Override // dialog.DialogUtil.OnConfirmClickListener
                            public void ConfirmListener() {
                                BleScantActivity.this.isLoad = false;
                                BleScantActivity.this.binding.layoutBleMain.setVisibility(8);
                                BleScantActivity.this.binding.layoutWifi.setVisibility(8);
                                BleScantActivity.this.binding.layoutBle.setVisibility(0);
                                BleClient.getInstence().disconnectAll();
                                BleScantActivity.this.refreshDeviceList();
                                BleScantActivity.this.ScanBle();
                            }
                        });
                        return;
                    }
                }
                if (i == 6221) {
                    if (BleScantActivity.this.isLoad) {
                        return;
                    }
                    if (BleScantActivity.this.count >= BleScantActivity.this.countMax) {
                        BleScantActivity.this.isLoad = true;
                        DialogUtil.showConfirmDiaLog(BleScantActivity.this, BleScantActivity.this.getString(R.string.device_offline_tf), BleScantActivity.this.getString(R.string.confirm), new DialogUtil.OnConfirmClickListener() { // from class: activity.BleScantActivity.9.1.2
                            @Override // dialog.DialogUtil.OnConfirmClickListener
                            public void ConfirmListener() {
                                BleScantActivity.this.isLoad = false;
                                BleScantActivity.this.binding.layoutBleMain.setVisibility(8);
                                BleScantActivity.this.binding.layoutWifi.setVisibility(8);
                                BleScantActivity.this.binding.layoutBle.setVisibility(0);
                                BleClient.getInstence().disconnectAll();
                                BleScantActivity.this.refreshDeviceList();
                                BleScantActivity.this.ScanBle();
                            }
                        });
                        return;
                    } else {
                        new Handler().postDelayed(new Runnable() { // from class: activity.BleScantActivity.9.1.3
                            @Override // java.lang.Runnable
                            public void run() {
                                BleScantActivity.this.bindUser();
                            }
                        }, ToolTipPopup.DEFAULT_POPUP_DISPLAY_TIME);
                        return;
                    }
                }
                if (BleScantActivity.this.isLoad) {
                    return;
                }
                if (BleScantActivity.this.count >= BleScantActivity.this.countMax) {
                    BleScantActivity.this.isLoad = true;
                    DialogUtil.showConfirmDiaLog(BleScantActivity.this, BleScantActivity.this.getString(R.string.device_binded), BleScantActivity.this.getString(R.string.confirm), new DialogUtil.OnConfirmClickListener() { // from class: activity.BleScantActivity.9.1.4
                        @Override // dialog.DialogUtil.OnConfirmClickListener
                        public void ConfirmListener() {
                            BleScantActivity.this.isLoad = false;
                            BleScantActivity.this.binding.layoutBleMain.setVisibility(8);
                            BleScantActivity.this.binding.layoutWifi.setVisibility(8);
                            BleScantActivity.this.binding.layoutBle.setVisibility(0);
                            BleClient.getInstence().disconnectAll();
                            BleScantActivity.this.refreshDeviceList();
                            BleScantActivity.this.ScanBle();
                        }
                    });
                } else {
                    DialogUtil.showCancelConfirmDiaLog(BleScantActivity.this, BleScantActivity.this.getString(R.string.device_unbind), BleScantActivity.this.getString(R.string.device_binded), BleScantActivity.this.getString(R.string.cancel), BleScantActivity.this.getString(R.string.unbind), new DialogUtil.OnCancelConfirmClickListener() { // from class: activity.BleScantActivity.9.1.5
                        @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                        public void CancelListener() {
                            BleScantActivity.this.isLoad = false;
                            BleScantActivity.this.binding.layoutBleMain.setVisibility(8);
                            BleScantActivity.this.binding.layoutWifi.setVisibility(8);
                            BleScantActivity.this.binding.layoutBle.setVisibility(0);
                            BleClient.getInstence().disconnectAll();
                            BleScantActivity.this.refreshDeviceList();
                            BleScantActivity.this.ScanBle();
                        }

                        @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                        public void ConfirmListener() {
                            BleScantActivity.this.isLoad = false;
                            BleClient.getInstence().sendOrder2("UNBIND?".getBytes(StandardCharsets.UTF_8));
                            new Handler().postDelayed(new Runnable() { // from class: activity.BleScantActivity.9.1.5.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    BleScantActivity.this.bindUser();
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
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/thing/info/get").setApiVersion("1.0.4").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass10(str));
    }

    /* JADX INFO: renamed from: activity.BleScantActivity$10, reason: invalid class name */
    class AnonymousClass10 implements IoTCallback {
        final /* synthetic */ String val$iotId;

        AnonymousClass10(String str) {
            this.val$iotId = str;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            BleScantActivity.this.updateOnlineProgress();
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            LogEx.e(true, BleScantActivity.this.TAG, "triggerRecordPlan:" + ioTResponse.getData() + "");
            if (ioTResponse.getCode() != 200) {
                BleScantActivity.this.updateOnlineProgress();
                return;
            }
            int intValue = JSON.parseObject(ioTResponse.getData().toString()).getIntValue("status");
            Log.e("BleClient 设备在线状态", "" + intValue);
            if (intValue == 1) {
                BleScantActivity.this.isOnline = true;
                final String userPhone = Utils.getUserPhone();
                if (!TextUtils.isEmpty(userPhone)) {
                    HashMap map = new HashMap();
                    map.put(config.Constants.DEVICE_OWNER, userPhone);
                    IPCManager.getInstance().getDevice(this.val$iotId).setProperties(map, new IPanelCallback() { // from class: activity.BleScantActivity.10.1
                        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                        public void onComplete(boolean z, Object obj) {
                            Log.e("BleClient:绑定所有者", z + "      " + String.valueOf(obj));
                            BleScantActivity.this.runOnUiThread(new Runnable() { // from class: activity.BleScantActivity.10.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    BleScantActivity.this.binding.layoutBleMain.setVisibility(8);
                                    BleScantActivity.this.binding.layoutWifi.setVisibility(8);
                                    BleScantActivity.this.binding.layoutBle.setVisibility(0);
                                    BleScantActivity.this.showToast(BleScantActivity.this.getString(R.string.bind_success));
                                    EventBus.getDefault().post(new updateTimeModify(AnonymousClass10.this.val$iotId));
                                    EventBus.getDefault().post(new RefreshDevices());
                                    SharePreferenceManager.getInstance().setDeviceOwner(AnonymousClass10.this.val$iotId, userPhone);
                                    EventBus.getDefault().post(new setFinish("activity.scanNavigationActivity"));
                                    BleScantActivity.this.finish();
                                }
                            });
                        }
                    });
                    return;
                }
                BleScantActivity.this.runOnUiThread(new Runnable() { // from class: activity.BleScantActivity.10.2
                    @Override // java.lang.Runnable
                    public void run() {
                        BleScantActivity.this.binding.layoutBleMain.setVisibility(8);
                        BleScantActivity.this.binding.layoutWifi.setVisibility(8);
                        BleScantActivity.this.binding.layoutBle.setVisibility(0);
                        BleScantActivity.this.showToast(BleScantActivity.this.getString(R.string.bind_success));
                        EventBus.getDefault().post(new updateTimeModify(AnonymousClass10.this.val$iotId));
                        EventBus.getDefault().post(new RefreshDevices());
                        EventBus.getDefault().post(new setFinish("activity.scanNavigationActivity"));
                        BleScantActivity.this.finish();
                    }
                });
                return;
            }
            BleScantActivity.this.updateOnlineProgress();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ScanBle() {
        this.binding.tvBleState.setVisibility(8);
        this.binding.tvBle.setText(R.string.ble_scaning);
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.anim_loading)).into(this.binding.ivScanning);
        BleClient.getInstence().cancleBleScan();
        refreshDeviceList();
        BleClient.getInstence().scanBLE(this);
        new Handler().postDelayed(new Runnable() { // from class: activity.BleScantActivity.11
            @Override // java.lang.Runnable
            public void run() {
                if (BleScantActivity.this.bleDeviceList.size() == 0) {
                    BleScantActivity.this.binding.tvBle.setText(R.string.ble_tips);
                    BleScantActivity.this.binding.tvBleState.setText(R.string.search_again);
                    BleScantActivity.this.binding.tvBleState.setVisibility(0);
                    BleScantActivity.this.binding.tvBleState.setSelected(true);
                    if (BleScantActivity.this.isFinishing()) {
                        return;
                    }
                    BleScantActivity bleScantActivity = BleScantActivity.this;
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
                    runOnUiThread(new Runnable() { // from class: activity.BleScantActivity.16
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(BleScantActivity.this.getApplicationContext(), e.getMessage(), 0).show();
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
