package activity;

import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import bean.CameraNameModify;
import bean.DeviceInfoBean;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.hjq.permissions.Permission;
import com.seculink.app.R;
import dialog.AlertDialogView;
import dialog.BaseDialog;
import dialog.InputDialogView;
import dialog.UpgradeDialogIpc;
import java.util.ArrayList;
import java.util.HashMap;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import tools.BitmapUtil;
import tools.DensityUtil;
import tools.FileUtil;
import tools.LogEx;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.ItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class CameraInfoActivity extends CommonActivity implements View.OnClickListener {
    Button bt_save_code;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean device2;
    private TitleView fl_titlebar;
    private String iotId;
    ItemView item4gInformation;
    ItemView item4gInformation1;
    ItemView itemDeviceID;
    ItemView itemFirmwareVersion;
    ItemView itemIccid;
    ItemView itemIccid1;
    ItemView itemIccid2;
    ItemView itemImei;
    ItemView itemImei1;
    ItemView itemIp;
    ItemView itemMac;
    ItemView itemNameSetting;
    ItemView itemOwner;
    ItemView item_nvr2_version;
    ItemView item_nvr_version;
    ItemView item_wifi;
    private ImageView ivQrCode;
    ImageView iv_qcr_code;
    ImageView iv_qcr_code2;
    LinearLayout layout_code;
    LinearLayout layout_main;
    LinearLayout layout_save_code;
    private AlertDialogView mAlertDialogView;
    private InputDialogView nameSettingDialog;
    private DeviceInfoBean nvrDevice;
    private TextView tvQrCode;
    TextView tv_code_info;
    TextView tv_code_info1;
    private UpgradeDialogIpc upgradeDialogIpc;
    private Handler mHandler = new Handler();
    private Handler handler = new Handler();
    private String nvrIotId = "";
    private String shotIotId = "";
    private int nvrOwner = 0;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_camera_info;
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.device2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
            Log.d(this.TAG, "initWidget: puppet3333333:device====" + this.device.getOwned());
            Log.d(this.TAG, "initWidget: puppet444444:device====" + this.device.toString());
            DeviceInfoBean deviceInfoBean = this.device;
            if (deviceInfoBean != null) {
                this.iotId = deviceInfoBean.getIotId();
            }
            DeviceInfoBean deviceInfoBean2 = this.device1;
            if (deviceInfoBean2 != null) {
                this.shotIotId = deviceInfoBean2.getIotId();
            }
            DeviceInfoBean deviceInfoBean3 = this.nvrDevice;
            if (deviceInfoBean3 != null) {
                this.nvrIotId = deviceInfoBean3.getIotId();
                this.nvrOwner = this.nvrDevice.getOwned();
            }
            Log.d(this.TAG, "initWidget: nvrIotId====" + this.nvrIotId);
            Log.d(this.TAG, "initWidget: shotIotId====" + this.shotIotId);
            Log.d(this.TAG, "initWidget: nvrOwner====" + this.nvrOwner);
        }
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.CameraInfoActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                CameraInfoActivity.this.finish();
            }
        });
        this.itemOwner = (ItemView) findViewById(R.id.item_owner);
        this.itemDeviceID = (ItemView) findViewById(R.id.item_device_ID);
        this.itemFirmwareVersion = (ItemView) findViewById(R.id.item_firmware_version);
        this.item_nvr_version = (ItemView) findViewById(R.id.item_nvr_version);
        this.item_nvr2_version = (ItemView) findViewById(R.id.item_nvr2_version);
        this.itemNameSetting = (ItemView) findViewById(R.id.item_name_setting);
        this.itemIp = (ItemView) findViewById(R.id.item_ip);
        this.itemMac = (ItemView) findViewById(R.id.item_mac);
        this.tvQrCode = (TextView) findViewById(R.id.tv_QR_code);
        this.iv_qcr_code = (ImageView) findViewById(R.id.iv_qcr_code);
        this.iv_qcr_code2 = (ImageView) findViewById(R.id.iv_qcr_code2);
        this.ivQrCode = (ImageView) findViewById(R.id.iv_QR_code);
        this.bt_save_code = (Button) findViewById(R.id.bt_save_code);
        this.tv_code_info = (TextView) findViewById(R.id.tv_code_info);
        this.tv_code_info1 = (TextView) findViewById(R.id.tv_code_info1);
        this.layout_code = (LinearLayout) findViewById(R.id.layout_code);
        this.layout_save_code = (LinearLayout) findViewById(R.id.layout_save_code);
        this.itemIccid = (ItemView) findViewById(R.id.item_iccid);
        this.itemIccid2 = (ItemView) findViewById(R.id.item_iccid2);
        this.itemImei = (ItemView) findViewById(R.id.item_imei);
        this.item4gInformation = (ItemView) findViewById(R.id.item_4g_information);
        this.itemIccid1 = (ItemView) findViewById(R.id.item_iccid1);
        this.itemImei1 = (ItemView) findViewById(R.id.item_imei1);
        this.item_wifi = (ItemView) findViewById(R.id.item_wifi);
        this.item4gInformation1 = (ItemView) findViewById(R.id.item_4g_information1);
        findViewById(R.id.item_name_setting).setOnClickListener(this);
        this.itemIp.setRightText(SharePreferenceManager.getInstance().getDeviceIP(this.device.getIotId()));
        this.itemMac.setRightText(SharePreferenceManager.getInstance().getDeviceMAC(this.device.getIotId()));
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        SettingsCtrl.getInstance().getProperties(this.device.getIotId(), new MyCallback() { // from class: activity.CameraInfoActivity.2
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
        if (SharePreferenceManager.getInstance().getIccId(this.device.getIotId()) != null && !"".equals(SharePreferenceManager.getInstance().getIccId(this.device.getIotId()))) {
            if (((SharePreferenceManager.getInstance().getPageControlEx(this.device.getIotId()) & 524288) >> 19) == 1) {
                if (SharePreferenceManager.getInstance().getIccId1(this.device.getIotId()).equals("") && SharePreferenceManager.getInstance().getIccId2(this.device.getIotId()).equals("")) {
                    this.itemIccid.setRightText(SharePreferenceManager.getInstance().getIccId(this.device.getIotId()));
                    this.itemIccid.setIvEnterImage(R.drawable.copy);
                    this.itemIccid.setIvEnterListener(new ItemView.CopyListener() { // from class: activity.CameraInfoActivity.3
                        @Override // view.ItemView.CopyListener
                        public void copy() {
                            ((ClipboardManager) CameraInfoActivity.this.getSystemService("clipboard")).setText(CameraInfoActivity.this.itemIccid.getRightText());
                            CameraInfoActivity cameraInfoActivity = CameraInfoActivity.this;
                            Toast.makeText(cameraInfoActivity, cameraInfoActivity.getResources().getString(R.string.copy_success), 0).show();
                        }
                    });
                    this.itemIccid.setEnterHide(false);
                    this.itemIccid.setRightViewWeight();
                } else {
                    this.itemIccid.setRightText(SharePreferenceManager.getInstance().getIccId1(this.device.getIotId()));
                    this.itemIccid.setIvEnterImage(R.drawable.copy);
                    this.itemIccid.setIvEnterListener(new ItemView.CopyListener() { // from class: activity.CameraInfoActivity.4
                        @Override // view.ItemView.CopyListener
                        public void copy() {
                            ((ClipboardManager) CameraInfoActivity.this.getSystemService("clipboard")).setText(CameraInfoActivity.this.itemIccid.getRightText());
                            CameraInfoActivity cameraInfoActivity = CameraInfoActivity.this;
                            Toast.makeText(cameraInfoActivity, cameraInfoActivity.getResources().getString(R.string.copy_success), 0).show();
                        }
                    });
                    this.itemIccid.setEnterHide(false);
                    this.itemIccid.setRightViewWeight();
                    Log.e("双卡01", "" + this.device.getIotId());
                    this.itemIccid2.setVisibility(0);
                    this.itemIccid2.setRightText(SharePreferenceManager.getInstance().getIccId2(this.device.getIotId()));
                    this.itemIccid2.setIvEnterImage(R.drawable.copy);
                    this.itemIccid2.setIvEnterListener(new ItemView.CopyListener() { // from class: activity.CameraInfoActivity.5
                        @Override // view.ItemView.CopyListener
                        public void copy() {
                            ((ClipboardManager) CameraInfoActivity.this.getSystemService("clipboard")).setText(CameraInfoActivity.this.itemIccid2.getRightText());
                            CameraInfoActivity cameraInfoActivity = CameraInfoActivity.this;
                            Toast.makeText(cameraInfoActivity, cameraInfoActivity.getResources().getString(R.string.copy_success), 0).show();
                        }
                    });
                    this.itemIccid2.setEnterHide(false);
                    this.itemIccid2.setRightViewWeight();
                }
            } else {
                this.itemIccid.setTitleText(getString(R.string.sim));
                this.itemIccid.setRightText(SharePreferenceManager.getInstance().getIccId(this.device.getIotId()));
                this.itemIccid.setIvEnterImage(R.drawable.copy);
                this.itemIccid.setIvEnterListener(new ItemView.CopyListener() { // from class: activity.CameraInfoActivity.6
                    @Override // view.ItemView.CopyListener
                    public void copy() {
                        ((ClipboardManager) CameraInfoActivity.this.getSystemService("clipboard")).setText(CameraInfoActivity.this.itemIccid.getRightText());
                        CameraInfoActivity cameraInfoActivity = CameraInfoActivity.this;
                        Toast.makeText(cameraInfoActivity, cameraInfoActivity.getResources().getString(R.string.copy_success), 0).show();
                    }
                });
                this.itemIccid.setEnterHide(false);
                this.itemIccid.setRightViewWeight();
            }
            this.itemImei.setRightText(SharePreferenceManager.getInstance().getImei(this.device.getIotId()));
            this.itemImei.setIvEnterImage(R.drawable.copy);
            this.itemImei.setIvEnterListener(new ItemView.CopyListener() { // from class: activity.CameraInfoActivity.7
                @Override // view.ItemView.CopyListener
                public void copy() {
                    ((ClipboardManager) CameraInfoActivity.this.getSystemService("clipboard")).setText(CameraInfoActivity.this.itemImei.getRightText());
                    CameraInfoActivity cameraInfoActivity = CameraInfoActivity.this;
                    Toast.makeText(cameraInfoActivity, cameraInfoActivity.getResources().getString(R.string.copy_success), 0).show();
                }
            });
            this.itemImei.setEnterHide(false);
            this.itemImei.setRightViewWeight();
            this.item4gInformation.setRightText(SharePreferenceManager.getInstance().getNet4gVersion(this.device.getIotId()));
        } else {
            this.itemIccid.setVisibility(8);
            this.itemImei.setVisibility(8);
            this.item4gInformation.setVisibility(8);
        }
        if (!SharePreferenceManager.getInstance().getDeviceOwner(this.device.getIotId()).isEmpty()) {
            this.itemOwner.setRightText(SharePreferenceManager.getInstance().getDeviceOwner(this.device.getIotId()));
        }
        this.itemFirmwareVersion.setRightText(SharePreferenceManager.getInstance().getFirmwareVersion(this.device.getIotId()));
        if (this.device1 != null && !"".equals(SharePreferenceManager.getInstance().getFirmwareVersion(this.device1.getIotId()))) {
            this.itemFirmwareVersion.setTitleText(getString(R.string.ptz_version));
            this.item_nvr_version.setTitleText(getString(R.string.bullet_version));
            this.item_nvr_version.setVisibility(0);
            this.item_nvr_version.setRightText(SharePreferenceManager.getInstance().getFirmwareVersion(this.device1.getIotId()));
        }
        if (this.device2 != null && !"".equals(SharePreferenceManager.getInstance().getFirmwareVersion(this.device2.getIotId()))) {
            this.item_nvr_version.setTitleText(getString(R.string.bullet_version) + "1");
            this.item_nvr2_version.setTitleText(getString(R.string.bullet_version) + "2");
            this.item_nvr2_version.setVisibility(0);
            this.item_nvr2_version.setRightText(SharePreferenceManager.getInstance().getFirmwareVersion(this.device2.getIotId()));
        }
        this.itemFirmwareVersion.setOnClickListener(new View.OnClickListener() { // from class: activity.CameraInfoActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                CameraInfoActivity.this.getUpgradeInfo();
            }
        });
        this.itemNameSetting.setRightText(this.device.getName());
        this.nameSettingDialog = new InputDialogView.Builder().title(getString(R.string.name_setting)).build();
        this.nameSettingDialog.addOnClickListener(new InputDialogView.OnClickListener() { // from class: activity.CameraInfoActivity.9
            @Override // dialog.InputDialogView.OnClickListener
            public void onNegativeClick() {
            }

            @Override // dialog.InputDialogView.OnClickListener
            public void onPositiveClick(String str, Object obj) {
                CameraInfoActivity cameraInfoActivity = CameraInfoActivity.this;
                cameraInfoActivity.modifyDevName(cameraInfoActivity.device.getIotId(), str);
                if (CameraInfoActivity.this.device1 != null) {
                    CameraInfoActivity cameraInfoActivity2 = CameraInfoActivity.this;
                    cameraInfoActivity2.modifyDevNameNoToast(cameraInfoActivity2.device1.getIotId(), str);
                }
                if (CameraInfoActivity.this.device2 != null) {
                    CameraInfoActivity cameraInfoActivity3 = CameraInfoActivity.this;
                    cameraInfoActivity3.modifyDevNameNoToast(cameraInfoActivity3.device2.getIotId(), str);
                }
                if (CameraInfoActivity.this.nvrDevice != null) {
                    CameraInfoActivity cameraInfoActivity4 = CameraInfoActivity.this;
                    cameraInfoActivity4.modifyDevNameNoToast(cameraInfoActivity4.nvrDevice.getIotId(), str);
                }
            }
        });
        this.itemDeviceID.setRightText(this.device.getDeviceName());
        this.itemDeviceID.setIvEnterImage(R.drawable.copy);
        this.itemDeviceID.setIvEnterListener(new ItemView.CopyListener() { // from class: activity.CameraInfoActivity.10
            @Override // view.ItemView.CopyListener
            public void copy() {
                ((ClipboardManager) CameraInfoActivity.this.getSystemService("clipboard")).setText(CameraInfoActivity.this.itemDeviceID.getRightText());
                CameraInfoActivity cameraInfoActivity = CameraInfoActivity.this;
                Toast.makeText(cameraInfoActivity, cameraInfoActivity.getResources().getString(R.string.copy_success), 0).show();
            }
        });
        this.itemDeviceID.setEnterHide(false);
        this.itemDeviceID.setRightViewWeight();
        initQrCode();
        this.bt_save_code.setOnClickListener(new View.OnClickListener() { // from class: activity.CameraInfoActivity.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (ContextCompat.checkSelfPermission(CameraInfoActivity.this, Permission.WRITE_EXTERNAL_STORAGE) != 0) {
                    ActivityCompat.requestPermissions(CameraInfoActivity.this, new String[]{Permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                CameraInfoActivity.this.layout_save_code.setVisibility(0);
                new Handler().postDelayed(new Runnable() { // from class: activity.CameraInfoActivity.11.1
                    @Override // java.lang.Runnable
                    public void run() {
                        FileUtil.saveBitmap(CameraInfoActivity.this, BitmapUtil.LoadBitmapFromView(CameraInfoActivity.this.layout_save_code));
                        CameraInfoActivity.this.layout_save_code.setVisibility(8);
                        CameraInfoActivity.this.showToast(CameraInfoActivity.this.getResources().getString(R.string.save_success));
                    }
                }, 200L);
            }
        });
        if (SharePreferenceManager.getInstance().getWifiModelInfo(this.device.getIotId()).isEmpty()) {
            return;
        }
        this.item_wifi.setRightText(SharePreferenceManager.getInstance().getWifiModelInfo(this.device.getIotId()));
        this.item_wifi.setVisibility(0);
    }

    private void initQrCode() {
        String str;
        String str2;
        if (SharePreferenceManager.getInstance().getSupport4G(this.device.getIotId()) == 1) {
            if (this.device1 != null && !"".equals(this.nvrIotId) && this.nvrDevice != null) {
                str2 = "&pk=" + this.nvrDevice.getProductKey() + "&dn=" + this.nvrDevice.getDeviceName() + "&ic=" + SharePreferenceManager.getInstance().getIccId(this.device.getIotId()) + "&bd=1";
            } else {
                str2 = "&pk=" + this.device.getProductKey() + "&dn=" + this.device.getDeviceName() + "&ic=" + SharePreferenceManager.getInstance().getIccId(this.device.getIotId()) + "&bd=1";
            }
            Bitmap bitmapCreateQRCodeBitmap = BitmapUtil.createQRCodeBitmap(str2, DensityUtil.dip2px(this, 160.0f), DensityUtil.dip2px(this, 160.0f), "UTF-8", "L", "2", ViewCompat.MEASURED_STATE_MASK, -1);
            this.iv_qcr_code.setImageBitmap(bitmapCreateQRCodeBitmap);
            this.iv_qcr_code2.setImageBitmap(bitmapCreateQRCodeBitmap);
            this.tv_code_info.setText("" + SharePreferenceManager.getInstance().getIccId(this.device.getIotId()));
            this.tv_code_info1.setText("" + this.device.getDeviceName());
            this.tv_code_info.setVisibility(0);
            this.layout_code.setVisibility(0);
        } else {
            if (this.device1 != null && !"".equals(this.nvrIotId) && this.nvrDevice != null) {
                str = "&pk=" + this.nvrDevice.getProductKey() + "&dn=" + this.nvrDevice.getDeviceName() + "&bd=0";
            } else {
                str = "&pk=" + this.device.getProductKey() + "&dn=" + this.device.getDeviceName() + "&bd=0";
            }
            Bitmap bitmapCreateQRCodeBitmap2 = BitmapUtil.createQRCodeBitmap(str, DensityUtil.dip2px(this, 160.0f), DensityUtil.dip2px(this, 160.0f), "UTF-8", "L", "2", ViewCompat.MEASURED_STATE_MASK, -1);
            this.iv_qcr_code.setImageBitmap(bitmapCreateQRCodeBitmap2);
            this.iv_qcr_code2.setImageBitmap(bitmapCreateQRCodeBitmap2);
            this.tv_code_info1.setText("" + this.device.getDeviceName());
            this.layout_code.setVisibility(0);
        }
        if (SharePreferenceManager.getInstance().getHideSIMPlans(this.device.getIotId()) == 0 && SharePreferenceManager.getInstance().getVendorID(this.device.getIotId()) == 3 && SharePreferenceManager.getInstance().getEnable(this.device.getIotId()) == 1) {
            Bitmap bitmapCreateQRCodeBitmap3 = BitmapUtil.createQRCodeBitmap("&pk=" + this.device.getProductKey() + "&dn=" + this.device.getDeviceName() + "&imei=" + SharePreferenceManager.getInstance().getImei(this.device.getIotId()) + "&bd=1", DensityUtil.dip2px(this, 160.0f), DensityUtil.dip2px(this, 160.0f), "UTF-8", "L", "2", ViewCompat.MEASURED_STATE_MASK, -1);
            this.iv_qcr_code.setImageBitmap(bitmapCreateQRCodeBitmap3);
            this.iv_qcr_code2.setImageBitmap(bitmapCreateQRCodeBitmap3);
            TextView textView = this.tv_code_info;
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(SharePreferenceManager.getInstance().getImei(this.device.getIotId()));
            textView.setText(sb.toString());
            this.tv_code_info1.setText("" + this.device.getDeviceName());
            this.tv_code_info.setVisibility(0);
            this.layout_code.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void modifyDevName(final String str, final String str2) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("nickName", str2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/uc/setDeviceNickName").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CameraInfoActivity.12
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, CameraInfoActivity.this.TAG, "onFailure");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                LogEx.d(true, CameraInfoActivity.this.TAG, "modifyDevName onResponse: code: " + code);
                final String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    CameraInfoActivity.this.mHandler.post(new Runnable() { // from class: activity.CameraInfoActivity.12.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(CameraInfoActivity.this, localizedMsg, 0).show();
                        }
                    });
                } else {
                    CameraInfoActivity.this.mHandler.post(new Runnable() { // from class: activity.CameraInfoActivity.12.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(CameraInfoActivity.this, R.string.mofify_succeed, 0).show();
                            CameraInfoActivity.this.itemNameSetting.setRightText(str2);
                            EventBus.getDefault().post(new CameraNameModify(str2, str));
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void modifyDevNameNoToast(String str, String str2) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("nickName", str2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/uc/setDeviceNickName").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CameraInfoActivity.13
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, CameraInfoActivity.this.TAG, "onFailure");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                LogEx.d(true, CameraInfoActivity.this.TAG, "modifyDevName onResponse: code: " + code);
                ioTResponse.getLocalizedMsg();
            }
        });
    }

    public void updateDevice(String str) {
        showProgressDialog();
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.device.getIotId());
        map.put("iotIds", arrayList);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/ota/batchUpgradeByUser").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass14(str));
    }

    /* JADX INFO: renamed from: activity.CameraInfoActivity$14, reason: invalid class name */
    class AnonymousClass14 implements IoTCallback {
        final /* synthetic */ String val$version;

        AnonymousClass14(String str) {
            this.val$version = str;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, CameraInfoActivity.this.TAG, "onFailure");
            CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.14.1
                @Override // java.lang.Runnable
                public void run() {
                    CameraInfoActivity.this.showToast(CameraInfoActivity.this.getString(R.string.can_not_upgrade));
                    CameraInfoActivity.this.dismissProgressDialog();
                }
            });
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            if (ioTResponse.getCode() != 200) {
                CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.14.2
                    @Override // java.lang.Runnable
                    public void run() {
                        CameraInfoActivity.this.showToast(CameraInfoActivity.this.getString(R.string.can_not_upgrade));
                        CameraInfoActivity.this.dismissProgressDialog();
                    }
                });
            } else {
                CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.14.3
                    @Override // java.lang.Runnable
                    public void run() {
                        CameraInfoActivity.this.dismissProgressDialog();
                        CameraInfoActivity.this.upgradeDialogIpc = new UpgradeDialogIpc(CameraInfoActivity.this.getActivity(), R.style.progress_dialog);
                        CameraInfoActivity.this.upgradeDialogIpc.setOnViewClick(new UpgradeDialogIpc.OnViewClick() { // from class: activity.CameraInfoActivity.14.3.1
                            @Override // dialog.UpgradeDialogIpc.OnViewClick
                            public void onDismiss() {
                                if (CameraInfoActivity.this.upgradeDialogIpc.getProgress() < 100) {
                                    CameraInfoActivity.this.cancelUpgrade(CameraInfoActivity.this.device.getIotId(), AnonymousClass14.this.val$version);
                                }
                            }
                        });
                        CameraInfoActivity.this.upgradeDialogIpc.setCancelable(false);
                        CameraInfoActivity.this.upgradeDialogIpc.setCanceledOnTouchOutside(false);
                        CameraInfoActivity.this.upgradeDialogIpc.show();
                        CameraInfoActivity.this.queryUpgradeStep(CameraInfoActivity.this.device.getIotId(), AnonymousClass14.this.val$version);
                    }
                });
            }
        }
    }

    public void getUpgradeInfo() {
        this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.15
            @Override // java.lang.Runnable
            public void run() {
                CameraInfoActivity.this.showProgressDialog();
            }
        });
        HashMap map = new HashMap();
        map.put("iotId", this.device.getIotId());
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/ota/info/queryByUser").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass16());
    }

    /* JADX INFO: renamed from: activity.CameraInfoActivity$16, reason: invalid class name */
    class AnonymousClass16 implements IoTCallback {
        AnonymousClass16() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, CameraInfoActivity.this.TAG, "onFailure");
            CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.16.1
                @Override // java.lang.Runnable
                public void run() {
                    CameraInfoActivity.this.showToast(CameraInfoActivity.this.getString(R.string.get_version_upgrade_fail));
                    CameraInfoActivity.this.dismissProgressDialog();
                }
            });
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            int code = ioTResponse.getCode();
            if (code != 200) {
                if (code == 28601) {
                    CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.16.2
                        @Override // java.lang.Runnable
                        public void run() {
                            CameraInfoActivity.this.showToast(CameraInfoActivity.this.getString(R.string.no_permission));
                            CameraInfoActivity.this.dismissProgressDialog();
                        }
                    });
                    return;
                } else {
                    CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.16.3
                        @Override // java.lang.Runnable
                        public void run() {
                            CameraInfoActivity.this.showToast(CameraInfoActivity.this.getString(R.string.get_version_upgrade_fail));
                            CameraInfoActivity.this.dismissProgressDialog();
                        }
                    });
                    return;
                }
            }
            Object data = ioTResponse.getData();
            if (data == null || !(data instanceof JSONObject)) {
                CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.16.7
                    @Override // java.lang.Runnable
                    public void run() {
                        CameraInfoActivity.this.showToast(CameraInfoActivity.this.getString(R.string.get_version_upgrade_fail));
                        CameraInfoActivity.this.dismissProgressDialog();
                    }
                });
                return;
            }
            try {
                JSONObject jSONObject = (JSONObject) data;
                String str = (String) jSONObject.get("currentVersion");
                final String str2 = (String) jSONObject.get("version");
                if (!str.equals(str2)) {
                    CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.16.4
                        @Override // java.lang.Runnable
                        public void run() {
                            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(CameraInfoActivity.this.getString(R.string.found_device_upgrade_or_not, new Object[]{str2})).leftBtnText(CameraInfoActivity.this.getString(R.string.confirm_upgrade)).rightBtnText(CameraInfoActivity.this.getString(R.string.cancel)).clickLeft(new View.OnClickListener() { // from class: activity.CameraInfoActivity.16.4.1
                                @Override // android.view.View.OnClickListener
                                public void onClick(View view2) {
                                    CameraInfoActivity.this.updateDevice(str2);
                                }
                            }).canCancel(false).create().show(CameraInfoActivity.this.getSupportFragmentManager(), "");
                            CameraInfoActivity.this.dismissProgressDialog();
                        }
                    });
                } else {
                    CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.16.5
                        @Override // java.lang.Runnable
                        public void run() {
                            new BaseDialog.Builder().view(R.layout.dialog_common).content(CameraInfoActivity.this.getString(R.string.new_version)).rightBtnText(CameraInfoActivity.this.getString(R.string.known)).canCancel(false).create().show(CameraInfoActivity.this.getSupportFragmentManager(), "");
                            CameraInfoActivity.this.showToast(CameraInfoActivity.this.getString(R.string.new_version));
                            CameraInfoActivity.this.dismissProgressDialog();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
                CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.16.6
                    @Override // java.lang.Runnable
                    public void run() {
                        CameraInfoActivity.this.showToast(CameraInfoActivity.this.getString(R.string.get_version_upgrade_fail));
                        CameraInfoActivity.this.dismissProgressDialog();
                    }
                });
            }
        }
    }

    public void cancelUpgrade(String str, String str2) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("version", str2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/ota/unupgradeByUser").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CameraInfoActivity.17
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.d(CameraInfoActivity.this.TAG, "onFailure");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ioTResponse.getCode();
            }
        });
    }

    public void queryUpgradeStep(final String str, final String str2) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("version", str2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/ota/progress/getByUser").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CameraInfoActivity.18
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.d(CameraInfoActivity.this.TAG, "onFailure");
                CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.18.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CameraInfoActivity.this.showToast(CameraInfoActivity.this.getResources().getString(R.string.can_not_upgrade));
                        CameraInfoActivity.this.failUpgradeDialog();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() != 200) {
                    CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.18.2
                        @Override // java.lang.Runnable
                        public void run() {
                            CameraInfoActivity.this.showToast(CameraInfoActivity.this.getResources().getString(R.string.can_not_upgrade));
                            CameraInfoActivity.this.failUpgradeDialog();
                        }
                    });
                    return;
                }
                if (ioTResponse.getData() == null) {
                    CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.18.9
                        @Override // java.lang.Runnable
                        public void run() {
                            CameraInfoActivity.this.showToast(CameraInfoActivity.this.getResources().getString(R.string.can_not_upgrade));
                            CameraInfoActivity.this.failUpgradeDialog();
                        }
                    });
                    return;
                }
                try {
                    JSONObject jSONObject = (JSONObject) ioTResponse.getData();
                    int i = jSONObject.getInt(WifiProvisionUtConst.KEY_STEP);
                    int i2 = jSONObject.getInt("upgradeStatus");
                    if (i2 == 4) {
                        CameraInfoActivity.this.handler.postDelayed(new Runnable() { // from class: activity.CameraInfoActivity.18.3
                            @Override // java.lang.Runnable
                            public void run() {
                                if (CameraInfoActivity.this.isFinishing()) {
                                    return;
                                }
                                CameraInfoActivity.this.successUpgradeDialog();
                                CameraInfoActivity.this.showToast(CameraInfoActivity.this.getResources().getString(R.string.upgrade_version_success));
                                SharePreferenceManager.getInstance().setFirstUpdateVersion(str, str2);
                            }
                        }, 1000L);
                        SettingsCtrl.getInstance().getProperties(str, new MyCallback() { // from class: activity.CameraInfoActivity.18.4
                            @Override // tools.MyCallback
                            public void onComplete(boolean z) {
                            }
                        });
                        return;
                    }
                    if (i2 != 2 && i2 != 3) {
                        if (i < 0) {
                            CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.18.6
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (CameraInfoActivity.this.isFinishing()) {
                                        return;
                                    }
                                    CameraInfoActivity.this.showToast(CameraInfoActivity.this.getResources().getString(R.string.upgrade_version_fail));
                                    CameraInfoActivity.this.failUpgradeDialog();
                                }
                            });
                            return;
                        } else {
                            CameraInfoActivity.this.handler.postDelayed(new Runnable() { // from class: activity.CameraInfoActivity.18.7
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (CameraInfoActivity.this.isFinishing() || !CameraInfoActivity.this.upgradeDialogIpc.isShowing() || CameraInfoActivity.this.upgradeDialogIpc.isDownloadSucceed()) {
                                        return;
                                    }
                                    CameraInfoActivity.this.queryUpgradeStep(str, str2);
                                }
                            }, 1000L);
                            return;
                        }
                    }
                    CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.18.5
                        @Override // java.lang.Runnable
                        public void run() {
                            if (CameraInfoActivity.this.isFinishing()) {
                                return;
                            }
                            CameraInfoActivity.this.showToast(CameraInfoActivity.this.getResources().getString(R.string.upgrade_version_fail));
                            CameraInfoActivity.this.failUpgradeDialog();
                        }
                    });
                } catch (Exception e) {
                    CameraInfoActivity.this.handler.post(new Runnable() { // from class: activity.CameraInfoActivity.18.8
                        @Override // java.lang.Runnable
                        public void run() {
                            CameraInfoActivity.this.showToast(CameraInfoActivity.this.getResources().getString(R.string.can_not_upgrade));
                            CameraInfoActivity.this.failUpgradeDialog();
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void failUpgradeDialog() {
        UpgradeDialogIpc upgradeDialogIpc = this.upgradeDialogIpc;
        if (upgradeDialogIpc == null || !upgradeDialogIpc.isShowing()) {
            return;
        }
        this.upgradeDialogIpc.setTextUpgradingFail();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void successUpgradeDialog() {
        UpgradeDialogIpc upgradeDialogIpc = this.upgradeDialogIpc;
        if (upgradeDialogIpc == null || !upgradeDialogIpc.isShowing()) {
            return;
        }
        this.upgradeDialogIpc.setTextUpgradingSuccess();
    }

    private void createDevQrCode(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        HashMap map = new HashMap();
        map.put("iotIdList", arrayList);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/uc/generateShareQrCode").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CameraInfoActivity.19
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, CameraInfoActivity.this.TAG, "onFailure");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                Log.e(CameraInfoActivity.this.TAG, "getDevice onResponse: code: " + code);
                final String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    CameraInfoActivity.this.mHandler.post(new Runnable() { // from class: activity.CameraInfoActivity.19.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CameraInfoActivity.this.tvQrCode.setText(localizedMsg);
                            CameraInfoActivity.this.ivQrCode.setVisibility(8);
                        }
                    });
                    return;
                }
                Object data = ioTResponse.getData();
                if (data == null || !(data instanceof JSONObject)) {
                    return;
                }
                try {
                    String string = ((JSONObject) data).getString("qrKey");
                    Log.e(CameraInfoActivity.this.TAG, "createDevQrCode qrKey: " + string);
                    CameraInfoActivity.this.mHandler.post(new Runnable() { // from class: activity.CameraInfoActivity.19.2
                        @Override // java.lang.Runnable
                        public void run() {
                            CameraInfoActivity.this.tvQrCode.setText(CameraInfoActivity.this.getString(R.string.dev_QR_code));
                            CameraInfoActivity.this.ivQrCode.setVisibility(0);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.item_name_setting) {
            this.nameSettingDialog.setContent(this.itemNameSetting.getRightText());
            this.nameSettingDialog.show(getSupportFragmentManager(), this.TAG);
        }
    }
}
