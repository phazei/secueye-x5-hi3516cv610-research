package activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import bean.NetWorkEvent;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.seculink.app.R;
import dialog.DialogUtil;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import tools.AudioPlayManager;
import tools.LocationUtil;
import tools.SecuritySharedPreference;
import view.DialogView;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class InputWifiActivity extends CommonActivity implements View.OnClickListener {
    Button bt_next;
    ImageView ch_pwd_show;
    TextView chooseButton;
    EditText et_wifi_name;
    EditText et_wifi_pwd;
    TitleView fl_titlebar;
    ImageView input_wifi_image;
    LinearLayout layout_main;
    TextView routerText;
    TextView tips;
    TextView wifi_one_text;
    private boolean isHide = true;
    private boolean isDualBandWiFi = false;
    int wifiType = 0;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_input_wifi;
    }

    public void saveWifiPassword(String str, String str2) {
        SecuritySharedPreference.SecurityEditor securityEditorEdit = new SecuritySharedPreference(getApplicationContext(), "wifiConfig", 0).edit();
        securityEditorEdit.putString(str, str2);
        securityEditorEdit.apply();
    }

    public String getWifiPassword(String str) {
        return new SecuritySharedPreference(getApplicationContext(), "wifiConfig", 0).getString(str, "");
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.et_wifi_name = (EditText) findViewById(R.id.et_wifi_name);
        this.et_wifi_pwd = (EditText) findViewById(R.id.et_wifi_pwd);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.ch_pwd_show = (ImageView) findViewById(R.id.ch_pwd_show);
        this.tips = (TextView) findViewById(R.id.tips);
        this.chooseButton = (TextView) findViewById(R.id.tv_choose_net);
        this.routerText = (TextView) findViewById(R.id.router_setting_text);
        this.wifi_one_text = (TextView) findViewById(R.id.wifi_one_text);
        this.input_wifi_image = (ImageView) findViewById(R.id.input_wifi_image);
        this.bt_next = (Button) findViewById(R.id.bt_next);
        this.bt_next.setOnClickListener(this);
        this.chooseButton.setOnClickListener(this);
        this.ch_pwd_show.setOnClickListener(this);
        Drawable drawable = getResources().getDrawable(R.drawable.switch_wifi);
        drawable.setBounds(0, 0, (int) (((double) drawable.getIntrinsicWidth()) * 0.2d), (int) (((double) drawable.getIntrinsicHeight()) * 0.2d));
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.InputWifiActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                InputWifiActivity.this.finish();
            }

            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
                if (view2.getId() == R.id.right_ll) {
                    InputWifiActivity.this.showTips();
                }
            }
        });
        this.routerText.setOnClickListener(new View.OnClickListener() { // from class: activity.InputWifiActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                InputWifiActivity.this.startActivity(new Intent(InputWifiActivity.this, (Class<?>) CommonRouterSettingActivity.class));
            }
        });
        if (ActivityCompat.checkSelfPermission(getActivity(), Permission.ACCESS_COARSE_LOCATION) != 0 || ActivityCompat.checkSelfPermission(this, Permission.ACCESS_FINE_LOCATION) != 0) {
            DialogUtil.showCancelConfirmDiaLog(this, getString(R.string.no_permission), getString(R.string.location_service), getString(R.string.cancel), getString(R.string.confirm), new DialogUtil.OnCancelConfirmClickListener() { // from class: activity.InputWifiActivity.3
                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                public void CancelListener() {
                }

                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                public void ConfirmListener() {
                    XXPermissions.with(InputWifiActivity.this).permission(Permission.ACCESS_FINE_LOCATION).permission(Permission.ACCESS_COARSE_LOCATION).request(new OnPermissionCallback() { // from class: activity.InputWifiActivity.3.1
                        @Override // com.hjq.permissions.OnPermissionCallback
                        public void onDenied(@NonNull List<String> list, boolean z) {
                        }

                        @Override // com.hjq.permissions.OnPermissionCallback
                        public void onGranted(@NonNull List<String> list, boolean z) {
                            if (z) {
                                XXPermissions.isGranted(InputWifiActivity.this, Permission.CAMERA);
                            }
                        }
                    });
                }
            });
        }
        this.tips.setOnClickListener(new View.OnClickListener() { // from class: activity.InputWifiActivity.4
            @Override // android.view.View.OnClickListener
            @RequiresApi(api = 21)
            public void onClick(View view2) {
                InputWifiActivity.this.startActivity(new Intent(InputWifiActivity.this, (Class<?>) WifiHelpActivity.class));
            }
        });
        getSSid();
        EventBus.getDefault().register(this);
        this.isDualBandWiFi = getIntent().getBooleanExtra("isDualBandWiFi", false);
        if (this.isDualBandWiFi) {
            this.wifi_one_text.setVisibility(8);
            this.input_wifi_image.setVisibility(8);
            this.routerText.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"SetTextI18n"})
    public void showTips() {
        TextView textView = new TextView(this);
        textView.setText(R.string.input_activity_tips_title);
        textView.setTextSize(20.0f);
        textView.setGravity(17);
        textView.setPadding(0, 50, 0, 0);
        textView.setTextColor(getResources().getColor(R.color.color_black));
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setCustomTitle(textView).setMessage(R.string.text_t).setPositiveButton(R.string.i_know, new DialogInterface.OnClickListener() { // from class: activity.InputWifiActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                InputWifiActivity.this.dismissProgressDialog();
            }
        }).create();
        alertDialogCreate.show();
        alertDialogCreate.getButton(-1).setTextColor(Color.parseColor("#2c99fd"));
        Button button = alertDialogCreate.getButton(-1);
        button.setTextSize(18.0f);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.weight = 10.0f;
        button.setLayoutParams(layoutParams);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void netEventChange(NetWorkEvent netWorkEvent) {
        if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_WIFI_STATE") != 0 || ActivityCompat.checkSelfPermission(getActivity(), Permission.ACCESS_FINE_LOCATION) != 0) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.ACCESS_WIFI_STATE", Permission.ACCESS_FINE_LOCATION}, 4368);
        } else {
            getSSid();
        }
    }

    public void getSSid() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            String extraInfo = activeNetworkInfo.getExtraInfo();
            if (((ConnectivityManager) getSystemService("connectivity")).getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
                if (!TextUtils.isEmpty(extraInfo) && !"<unknown ssid>".equals(extraInfo)) {
                    Log.d(this.TAG, "getSSid: ssid" + extraInfo);
                    updateSSID(extraInfo);
                    return;
                }
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService("wifi");
                try {
                    Log.d(this.TAG, "getSSid: wifiManager.toString()====" + wifiManager.getConnectionInfo().toString());
                    updateSSID(wifiManager.getConnectionInfo().getSSID());
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() { // from class: activity.InputWifiActivity.6
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(InputWifiActivity.this.getApplicationContext(), e.getMessage(), 0).show();
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

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        AudioPlayManager.newBuilder().load(this, Integer.valueOf(R.raw.input_wifi), AudioPlayManager.MediaPlayerBuilder.TYPE.RAW).build().play();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        AudioPlayManager.with().release();
    }

    @SuppressLint({"SetTextI18n"})
    public void updateSSID(String str) {
        if (!TextUtils.isEmpty(str) && !"<unknown ssid>".equals(str)) {
            if (str.startsWith("\"") && str.endsWith("\"")) {
                str = str.substring(1, str.length() - 1);
            }
            this.et_wifi_name.setText(str + "");
            EditText editText = this.et_wifi_name;
            editText.setSelection(editText.getText().length());
            this.et_wifi_pwd.setText(getWifiPassword(this.et_wifi_name.getText().toString()));
            return;
        }
        this.et_wifi_name.setText("");
        this.et_wifi_name.setSelection(0);
        this.et_wifi_pwd.setText("");
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 4368 || i == 4640) {
            boolean z = true;
            for (int i2 = 0; i2 < iArr.length; i2++) {
                if (iArr[i2] == -1) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, strArr[i2])) {
                        if (strArr[i2].equals(Permission.ACCESS_FINE_LOCATION)) {
                            LocationUtil.remindStartLocateService(this);
                        } else if (strArr[i2].equals("android.permission.ACCESS_WIFI_STATE")) {
                            startActivity(new Intent("android.settings.WIFI_IP_SETTINGS"));
                        }
                    }
                    z = false;
                }
            }
            if (z) {
                if (i == 4368) {
                    getSSid();
                } else {
                    showDialog();
                }
            }
        }
    }

    public void showDialog() {
        new DialogView.Builder(this).setTitle("请手动设置权限").setNegativeClickListener("取消", $$Lambda$xLsIGvbwdcdhkPRJ4OVW3aSWueY.INSTANCE).setPositiveClickListener(getResources().getString(R.string.confirm), new DialogView.OnPositiveClickListener() { // from class: activity.InputWifiActivity.7
            @Override // view.DialogView.OnPositiveClickListener
            public void onPositiveClick(DialogView dialogView) {
                InputWifiActivity.this.startActivityForResult(new Intent("android.settings.WIFI_SETTINGS"), 4641);
                dialogView.dismiss();
            }
        }).build().show();
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
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

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        int id = view2.getId();
        if (id != R.id.bt_next) {
            if (id != R.id.ch_pwd_show) {
                if (id != R.id.tv_choose_net) {
                    return;
                }
                if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_WIFI_STATE") != 0 || ActivityCompat.checkSelfPermission(this, Permission.ACCESS_FINE_LOCATION) != 0) {
                    ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_WIFI_STATE", Permission.ACCESS_FINE_LOCATION}, 4640);
                    return;
                } else {
                    startActivityForResult(new Intent("android.settings.WIFI_SETTINGS"), 4641);
                    return;
                }
            }
            int selectionStart = this.et_wifi_pwd.getSelectionStart();
            if (this.isHide) {
                this.isHide = false;
                this.et_wifi_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                this.ch_pwd_show.setBackgroundResource(R.drawable.wifi_eye_open);
            } else {
                this.isHide = true;
                this.et_wifi_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                this.ch_pwd_show.setBackgroundResource(R.drawable.wifi_eye_close);
            }
            this.et_wifi_pwd.setSelection(selectionStart);
            return;
        }
        if (TextUtils.isEmpty(this.et_wifi_name.getText().toString())) {
            Toast.makeText(this, R.string.input_must_not_be_null, 0).show();
            return;
        }
        String str = "";
        Iterator<ScanResult> it = ((WifiManager) getApplicationContext().getSystemService("wifi")).getScanResults().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ScanResult next = it.next();
            if (next.SSID.equals(this.et_wifi_name.getText().toString())) {
                str = next.capabilities;
                break;
            }
        }
        if (str.contains("WPA")) {
            this.wifiType = 2;
        } else if (str.contains("WEP")) {
            this.wifiType = 1;
        } else {
            this.wifiType = 0;
        }
        Log.d(this.TAG, "onViewClicked: " + str + "  " + this.wifiType);
        Log.d(this.TAG, "onViewClicked: ");
        if (getIntent().getStringExtra("type").equals("newType")) {
            Intent intent = new Intent(this, (Class<?>) ScanQrCodeTipsActivity.class);
            intent.putExtra("wifiSSid", this.et_wifi_name.getText().toString());
            intent.putExtra("wifiPwd", this.et_wifi_pwd.getText().toString());
            intent.putExtra(AlinkConstants.KEY_PK, getIntent().getStringExtra(AlinkConstants.KEY_PK));
            intent.putExtra(AlinkConstants.KEY_DN, getIntent().getStringExtra(AlinkConstants.KEY_DN));
            intent.putExtra("type", "newType");
            intent.putExtra(AlinkConstants.KEY_WIFI_TYPE, this.wifiType);
            startActivity(intent);
            finish();
        }
        saveWifiPassword(this.et_wifi_name.getText().toString(), this.et_wifi_pwd.getText().toString());
        Intent intent2 = new Intent(this, (Class<?>) WifiScanActivity.class);
        intent2.putExtra("wifiSSid", this.et_wifi_name.getText().toString());
        intent2.putExtra("wifiPwd", this.et_wifi_pwd.getText().toString());
        intent2.putExtra(AlinkConstants.KEY_PK, getIntent().getStringExtra(AlinkConstants.KEY_PK));
        intent2.putExtra(AlinkConstants.KEY_DN, getIntent().getStringExtra(AlinkConstants.KEY_DN));
        intent2.putExtra(AlinkConstants.KEY_WIFI_TYPE, this.wifiType);
        if (getIntent().getStringExtra("type").equals(TmpConstant.GROUP_OP_ADD)) {
            startActivityForResult(intent2, 10002);
            return;
        }
        if (getIntent().getStringExtra("type").equals("addToken")) {
            setResult(-1, intent2);
            finish();
        } else {
            intent2.putExtra("type", getIntent().getStringExtra("type"));
            setResult(-1, intent2);
            finish();
        }
    }
}
