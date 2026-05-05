package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import bean.RefreshDevices;
import bean.setFinish;
import bean.updateTimeModify;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.common.StringUtils;
import com.google.zxing.qrcode.QRCodeReader;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.seculink.app.R;
import com.taobao.accs.common.Constants;
import config.AppConfig;
import dialog.BaseDialog;
import dialog.DialogUtil;
import dialog.ProgressDialog;
import fragment.HomeTabFragment;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.codec.CharEncoding;
import org.greenrobot.eventbus.EventBus;
import tools.AudioPlayManager;
import tools.LogEx;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.ToastUtils;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class ScanActivity extends CommonActivity {
    private static final int IMAGE = 1;
    private static final int NOT_NOTICE = 2;
    public static final int REQUEST_CODE = 10002;
    ImageView Flashlight;
    private AlertDialog alertDialog;
    private DecoratedBarcodeView barcodeScannerView;
    private BeepManager beepManager;

    /* JADX INFO: renamed from: dialog, reason: collision with root package name */
    private BaseDialog f1582dialog;
    private String dn;
    TextView flashlight_text;
    private boolean isScanSuccess;
    LinearLayout layout_main;
    private AlertDialog mDialog;
    private ProgressDialog mProgress;
    Button old_connect;
    private String pk;
    private Bitmap scanBitmap;
    TitleView tv_title;
    boolean isLightOpen = false;
    private Handler uiHandler = new Handler();
    private BarcodeCallback callback = new BarcodeCallback() { // from class: activity.ScanActivity.1
        @Override // com.journeyapps.barcodescanner.BarcodeCallback
        public void possibleResultPoints(List<ResultPoint> list) {
        }

        @Override // com.journeyapps.barcodescanner.BarcodeCallback
        public void barcodeResult(final BarcodeResult barcodeResult) {
            ScanActivity.this.uiHandler.post(new Runnable() { // from class: activity.ScanActivity.1.1
                @Override // java.lang.Runnable
                public void run() {
                    Activity activity2 = ScanActivity.this.getActivity();
                    if (activity2 == null || activity2.isFinishing()) {
                        return;
                    }
                    Log.e("扫描", "" + barcodeResult.getText());
                    ScanActivity.this.doScanQrCode(barcodeResult.getText());
                }
            });
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_scan;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doScanQrCode(String str) {
        if (this.isScanSuccess) {
            return;
        }
        try {
            qrcodeResult(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.tv_title = (TitleView) findViewById(R.id.tv_title);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.tv_title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.ScanActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                ScanActivity.this.onBackPressed();
            }

            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
                ScanActivity scanActivity = ScanActivity.this;
                DialogUtil.showCancelConfirmDiaLog(scanActivity, scanActivity.getString(R.string.no_permission), ScanActivity.this.getString(R.string.album_service_des), ScanActivity.this.getString(R.string.cancel), ScanActivity.this.getString(R.string.confirm), new DialogUtil.OnCancelConfirmClickListener() { // from class: activity.ScanActivity.2.1
                    @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                    public void CancelListener() {
                    }

                    @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                    public void ConfirmListener() {
                        ScanActivity.this.myRequetPermission();
                    }
                });
            }
        });
        this.tv_title.setTitleBackText("");
        this.tv_title.setTitleTextColor(R.color.color_white);
        this.Flashlight = (ImageView) findViewById(R.id.Flashlight);
        this.flashlight_text = (TextView) findViewById(R.id.flashlight_text);
        this.old_connect = (Button) findViewById(R.id.old_connect);
        this.old_connect.setOnClickListener(new View.OnClickListener() { // from class: activity.ScanActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(ScanActivity.this.getActivity(), (Class<?>) InputWifiActivity.class);
                intent.putExtra("type", TmpConstant.GROUP_OP_ADD);
                ScanActivity.this.startActivityForResult(intent, 10002);
            }
        });
        this.barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.dbv_custom);
        this.barcodeScannerView.initializeFromIntent(getIntent());
        this.barcodeScannerView.decodeContinuous(this.callback);
        this.flashlight_text.setOnClickListener(new View.OnClickListener() { // from class: activity.ScanActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (!ScanActivity.this.isLightOpen) {
                    ScanActivity scanActivity = ScanActivity.this;
                    scanActivity.isLightOpen = true;
                    scanActivity.flashlight_text.setText(R.string.close_flashlight);
                    ScanActivity.this.Flashlight.setImageResource(R.drawable.scan_flash_on);
                    ScanActivity.this.barcodeScannerView.setTorchOn();
                    return;
                }
                ScanActivity scanActivity2 = ScanActivity.this;
                scanActivity2.isLightOpen = false;
                scanActivity2.flashlight_text.setText(R.string.open_flashlight);
                ScanActivity.this.Flashlight.setImageResource(R.drawable.scan_flash_off);
                ScanActivity.this.barcodeScannerView.setTorchOff();
            }
        });
        this.Flashlight.setOnClickListener(new View.OnClickListener() { // from class: activity.ScanActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (!ScanActivity.this.isLightOpen) {
                    ScanActivity scanActivity = ScanActivity.this;
                    scanActivity.isLightOpen = true;
                    scanActivity.flashlight_text.setText(R.string.close_flashlight);
                    ScanActivity.this.Flashlight.setImageResource(R.drawable.scan_flash_on);
                    ScanActivity.this.barcodeScannerView.setTorchOn();
                    return;
                }
                ScanActivity scanActivity2 = ScanActivity.this;
                scanActivity2.isLightOpen = false;
                scanActivity2.flashlight_text.setText(R.string.open_flashlight);
                ScanActivity.this.Flashlight.setImageResource(R.drawable.scan_flash_off);
                ScanActivity.this.barcodeScannerView.setTorchOff();
            }
        });
        this.beepManager = new BeepManager(this);
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(), Permission.CAMERA) != 0) {
            DialogUtil.showCancelConfirmDiaLog(this, getString(R.string.no_permission), getString(R.string.location_service_des), getString(R.string.cancel), getString(R.string.confirm), new DialogUtil.OnCancelConfirmClickListener() { // from class: activity.ScanActivity.6
                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                public void CancelListener() {
                }

                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                public void ConfirmListener() {
                    XXPermissions.with(ScanActivity.this).permission(Permission.CAMERA).request(new OnPermissionCallback() { // from class: activity.ScanActivity.6.1
                        @Override // com.hjq.permissions.OnPermissionCallback
                        public void onDenied(@NonNull List<String> list, boolean z) {
                        }

                        @Override // com.hjq.permissions.OnPermissionCallback
                        public void onGranted(@NonNull List<String> list, boolean z) {
                            if (XXPermissions.isGranted(ScanActivity.this, Permission.CAMERA)) {
                                ScanActivity.this.barcodeScannerView.initializeFromIntent(ScanActivity.this.getIntent());
                                ScanActivity.this.barcodeScannerView.decodeContinuous(ScanActivity.this.callback);
                            }
                        }
                    });
                }
            });
        } else {
            this.barcodeScannerView.initializeFromIntent(getIntent());
            this.barcodeScannerView.decodeContinuous(this.callback);
            this.barcodeScannerView.resume();
        }
        AudioPlayManager.newBuilder().load(this, Integer.valueOf(R.raw.scan_qr), AudioPlayManager.MediaPlayerBuilder.TYPE.RAW).build().play();
        this.isScanSuccess = false;
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        AudioPlayManager.with().release();
        this.barcodeScannerView.pause();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return this.barcodeScannerView.onKeyDown(i, keyEvent) || super.onKeyDown(i, keyEvent);
    }

    public void tipNoFoundQrCode() {
        if (this.f1582dialog == null) {
            this.f1582dialog = new BaseDialog.Builder().view(R.layout.dialog_scan).content(getString(R.string.no_found_qr_code)).title(getString(R.string.qr_code_error_title)).rightBtnText(getString(R.string.i_know)).canCancel(false).create();
        }
        if (!this.f1582dialog.isShowing()) {
            this.f1582dialog.show(getSupportFragmentManager(), "");
        }
        this.isScanSuccess = false;
    }

    public void qrcodeResult(String str) {
        boolean zContains = str.contains("a1uZPeFdASC");
        this.isScanSuccess = true;
        int i = 0;
        if (getIntent().getStringExtra("subType") != null && !getIntent().getStringExtra("subType").equals("")) {
            if (getIntent().getStringExtra("subType").equals("wifi") && str.contains("&pk=")) {
                String[] strArrSplit = str.split("&");
                String strSubstring = "";
                String strSubstring2 = "";
                while (i < strArrSplit.length) {
                    if (strArrSplit[i].contains("pk=")) {
                        strSubstring = strArrSplit[i].substring(3, strArrSplit[i].length());
                    } else if (strArrSplit[i].contains("dn=")) {
                        strSubstring2 = strArrSplit[i].substring(3, strArrSplit[i].length());
                    }
                    i++;
                }
                if (getIntent().getStringExtra("type").equals("scan")) {
                    Intent intent = new Intent(getActivity(), (Class<?>) InputWifiActivity.class);
                    intent.putExtra("type", "scan");
                    intent.putExtra("isDualBandWiFi", zContains);
                    startActivityForResult(intent, 100);
                } else if (getIntent().getStringExtra("type").equals(TmpConstant.GROUP_OP_ADD)) {
                    Intent intent2 = new Intent(getActivity(), (Class<?>) InputWifiActivity.class);
                    intent2.putExtra("type", "addToken");
                    intent2.putExtra("isDualBandWiFi", zContains);
                    startActivityForResult(intent2, 111);
                }
                this.pk = strSubstring;
                this.dn = strSubstring2;
                return;
            }
            if (getIntent().getStringExtra("subType").equals("4g")) {
                setupNet(str);
                return;
            }
        }
        if (str.contains("a1zFh1umwUZ") || str.contains("a1Azp3dgHUz") || str.contains("a17s70LF9F5") || str.contains("a17ICpUTq8E") || str.contains("a1d5vLCHdJ6") || str.contains("a1OsQrkCsxV") || str.contains("a19jRhT3oxx") || str.contains("a23Y1oMl7bQ")) {
            if (TextUtils.isEmpty(str) || !str.contains("&") || !str.contains("dn=") || !str.contains("pk=") || str.contains("&WIFI") || str.contains("&NVR")) {
                return;
            }
            setupNet(str);
            return;
        }
        if (str.contains("dn=") && str.contains("pk=") && str.contains("&ic=")) {
            setupNet(str);
            return;
        }
        if (str.contains("dn=") && str.contains("pk=") && str.contains("&ic2=")) {
            setupNet(str);
            return;
        }
        if (str.contains("bd=1")) {
            setupNet(str);
            return;
        }
        if (str.contains("bd=3")) {
            setupNet(str);
            return;
        }
        if (str.contains("&im=")) {
            setupNet(str);
            return;
        }
        if (str.contains("&ic=")) {
            setupNet(str);
            return;
        }
        if (str.contains("&ic2=")) {
            setupNet(str);
            return;
        }
        if (str.contains("dn=") && str.contains("pk=") && str.contains("&NVR")) {
            String[] strArrSplit2 = str.split("&");
            strArrSplit2[1].substring(3);
            strArrSplit2[2].substring(3);
            return;
        }
        if (str.contains("&pk=")) {
            String[] strArrSplit3 = str.split("&");
            String strSubstring3 = "";
            String strSubstring4 = "";
            while (i < strArrSplit3.length) {
                if (strArrSplit3[i].contains("pk=")) {
                    strSubstring3 = strArrSplit3[i].substring(3, strArrSplit3[i].length());
                } else if (strArrSplit3[i].contains("dn=")) {
                    strSubstring4 = strArrSplit3[i].substring(3, strArrSplit3[i].length());
                }
                i++;
            }
            if (getIntent().getStringExtra("type").equals("scan")) {
                Intent intent3 = new Intent(getActivity(), (Class<?>) InputWifiActivity.class);
                intent3.putExtra("type", "scan");
                intent3.putExtra("isDualBandWiFi", zContains);
                startActivityForResult(intent3, 100);
            } else if (getIntent().getStringExtra("type").equals(TmpConstant.GROUP_OP_ADD)) {
                Intent intent4 = new Intent(getActivity(), (Class<?>) InputWifiActivity.class);
                intent4.putExtra("type", "addToken");
                intent4.putExtra("isDualBandWiFi", zContains);
                startActivityForResult(intent4, 111);
            }
            this.pk = strSubstring3;
            this.dn = strSubstring4;
            return;
        }
        tipNoFoundQrCode();
    }

    void setupNet(String str) {
        String strSubstring = null;
        String strSubstring2 = null;
        String strSubstring3 = null;
        String strSubstring4 = null;
        for (String str2 : str.split("&")) {
            if (str2.length() > 3 && str2.startsWith("pk=")) {
                strSubstring2 = str2.substring(3);
            }
            if (str2.length() > 3 && str2.startsWith("dn=")) {
                strSubstring3 = str2.substring(3);
            }
            if (str2.length() > 3 && str2.startsWith("ic=")) {
                strSubstring = str2.substring(3);
            }
            if (str2.length() > 3 && str2.startsWith("ic2=")) {
                strSubstring = str2.substring(3);
            }
            if (str2.length() > 3 && str2.startsWith("im=")) {
                strSubstring4 = str2.substring(3);
            }
        }
        if (!TextUtils.isEmpty(strSubstring3) && !TextUtils.isEmpty(strSubstring2)) {
            this.beepManager.playBeepSoundAndVibrate();
            showProgressDialog();
            HashMap map = new HashMap();
            map.put("productKey", strSubstring2);
            map.put("deviceName", strSubstring3);
            new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/awss/time/window/user/bind").setScheme(Scheme.HTTPS).setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass7(str, strSubstring, strSubstring2, strSubstring3, strSubstring4));
            return;
        }
        tipNoFoundQrCode();
    }

    /* JADX INFO: renamed from: activity.ScanActivity$7, reason: invalid class name */
    class AnonymousClass7 implements IoTCallback {
        final /* synthetic */ String val$fdn;
        final /* synthetic */ String val$ficcid;
        final /* synthetic */ String val$finalImei;
        final /* synthetic */ String val$fpk;
        final /* synthetic */ String val$result;

        AnonymousClass7(String str, String str2, String str3, String str4, String str5) {
            this.val$result = str;
            this.val$ficcid = str2;
            this.val$fpk = str3;
            this.val$fdn = str4;
            this.val$finalImei = str5;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, ScanActivity.this.TAG, "onFailure");
            ScanActivity.this.uiHandler.post(new Runnable() { // from class: activity.ScanActivity.7.1
                @Override // java.lang.Runnable
                public void run() {
                    Activity activity2 = ScanActivity.this.getActivity();
                    if (activity2 == null || activity2.isFinishing()) {
                        return;
                    }
                    ScanActivity.this.dismissProgressDialog();
                    ToastUtils.showToast(ScanActivity.this.getActivity(), ScanActivity.this.getString(R.string.config_network_bind_fail));
                    ScanActivity.this.uiHandler.postDelayed(new Runnable() { // from class: activity.ScanActivity.7.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ScanActivity.this.isScanSuccess = false;
                        }
                    }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
                }
            });
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            final int code = ioTResponse.getCode();
            Log.e(ScanActivity.this.TAG, "onResponse: code: " + code);
            final String localizedMsg = ioTResponse.getLocalizedMsg();
            ioTResponse.getMessage();
            if (code != 200) {
                ScanActivity.this.uiHandler.post(new Runnable() { // from class: activity.ScanActivity.7.2
                    @Override // java.lang.Runnable
                    public void run() {
                        Activity activity2 = ScanActivity.this.getActivity();
                        if (activity2 == null || activity2.isFinishing()) {
                            return;
                        }
                        ScanActivity.this.dismissProgressDialog();
                        int i = code;
                        if (i == 6618) {
                            ToastUtils.showToast(ScanActivity.this.getActivity(), ScanActivity.this.getString(R.string.err_6618));
                            ScanActivity.this.uiHandler.postDelayed(new Runnable() { // from class: activity.ScanActivity.7.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    ScanActivity.this.isScanSuccess = false;
                                }
                            }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
                            return;
                        }
                        if (i == 2064) {
                            ToastUtils.showToast(ScanActivity.this.getActivity(), localizedMsg);
                            ScanActivity.this.uiHandler.postDelayed(new Runnable() { // from class: activity.ScanActivity.7.2.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    ScanActivity.this.isScanSuccess = false;
                                }
                            }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
                            return;
                        }
                        if (!AppConfig.isChina) {
                            ScanActivity.this.showThere(AnonymousClass7.this.val$finalImei);
                            return;
                        }
                        if (AnonymousClass7.this.val$result.contains("&ic=") && AnonymousClass7.this.val$result.contains("&bd=2")) {
                            if (AnonymousClass7.this.val$ficcid != null) {
                                ScanActivity.this.showTwo(AnonymousClass7.this.val$fpk, AnonymousClass7.this.val$fdn, AnonymousClass7.this.val$ficcid);
                            } else {
                                ToastUtils.showToast(ScanActivity.this, ScanActivity.this.getResources().getString(R.string.device_has_offline));
                                ScanActivity.this.uiHandler.postDelayed(new Runnable() { // from class: activity.ScanActivity.7.2.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ScanActivity.this.isScanSuccess = false;
                                    }
                                }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
                            }
                        }
                        if (AnonymousClass7.this.val$result.contains("&ic2=") && AnonymousClass7.this.val$result.contains("&bd=2")) {
                            if (AnonymousClass7.this.val$ficcid != null) {
                                ScanActivity.this.showTwo(AnonymousClass7.this.val$fpk, AnonymousClass7.this.val$fdn, AnonymousClass7.this.val$ficcid);
                                return;
                            } else {
                                ToastUtils.showToast(ScanActivity.this, ScanActivity.this.getResources().getString(R.string.device_has_offline));
                                ScanActivity.this.uiHandler.postDelayed(new Runnable() { // from class: activity.ScanActivity.7.2.4
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ScanActivity.this.isScanSuccess = false;
                                    }
                                }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
                                return;
                            }
                        }
                        if (AnonymousClass7.this.val$result.contains("&ic=") && AnonymousClass7.this.val$result.contains("&bd=3")) {
                            ScanActivity.this.showThree(AnonymousClass7.this.val$fpk, AnonymousClass7.this.val$fdn, AnonymousClass7.this.val$ficcid, AnonymousClass7.this.val$result, 1);
                            return;
                        }
                        if (AnonymousClass7.this.val$result.contains("&ic2=") && AnonymousClass7.this.val$result.contains("&bd=3")) {
                            ScanActivity.this.showThree(AnonymousClass7.this.val$fpk, AnonymousClass7.this.val$fdn, AnonymousClass7.this.val$ficcid, AnonymousClass7.this.val$result, 2);
                        } else if (AnonymousClass7.this.val$ficcid != null) {
                            ScanActivity.this.showTwo(AnonymousClass7.this.val$fpk, AnonymousClass7.this.val$fdn, AnonymousClass7.this.val$ficcid);
                        } else {
                            ToastUtils.showToast(ScanActivity.this, ScanActivity.this.getResources().getString(R.string.device_has_offline));
                            ScanActivity.this.uiHandler.postDelayed(new Runnable() { // from class: activity.ScanActivity.7.2.5
                                @Override // java.lang.Runnable
                                public void run() {
                                    ScanActivity.this.isScanSuccess = false;
                                }
                            }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
                        }
                    }
                });
                return;
            }
            String str = (String) ioTResponse.getData();
            EventBus.getDefault().post(new updateTimeModify(str));
            EventBus.getDefault().post(new RefreshDevices());
            SettingsCtrl.getInstance().getProperties(str, new MyCallback() { // from class: activity.ScanActivity.7.3
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
            ScanActivity.this.uiHandler.post(new Runnable() { // from class: activity.ScanActivity.7.4
                @Override // java.lang.Runnable
                public void run() {
                    Activity activity2 = ScanActivity.this.getActivity();
                    if (activity2 == null || activity2.isFinishing()) {
                        return;
                    }
                    ScanActivity.this.dismissProgressDialog();
                    EventBus.getDefault().post(new setFinish("activity.scanNavigationActivity"));
                    ScanActivity.this.finish();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showThree(final String str, final String str2, final String str3, final String str4, final int i) {
        View viewInflate = View.inflate(this, R.layout.scan_off_line_view, null);
        final AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setView(viewInflate).create();
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.ScanActivity.8
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                ScanActivity.this.isScanSuccess = false;
            }
        });
        alertDialogCreate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialogCreate.show();
        ((TextView) viewInflate.findViewById(R.id.text)).setText(Html.fromHtml(getResources().getString(R.string.qr_code_offline)));
        ((Button) viewInflate.findViewById(R.id.query_flow)).setOnClickListener(new View.OnClickListener() { // from class: activity.ScanActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
                if (i == 2) {
                    Intent intent = new Intent(ScanActivity.this.getActivity(), (Class<?>) Traffic4GIC2Activity.class);
                    intent.putExtra(AlinkConstants.KEY_PK, str);
                    intent.putExtra(AlinkConstants.KEY_DN, str2);
                    intent.putExtra("iccid", str3);
                    ScanActivity.this.startActivity(intent);
                } else {
                    Intent intent2 = new Intent(ScanActivity.this.getActivity(), (Class<?>) Traffic4GActivity.class);
                    intent2.putExtra(AlinkConstants.KEY_PK, str);
                    intent2.putExtra(AlinkConstants.KEY_DN, str2);
                    intent2.putExtra("iccid", str3);
                    ScanActivity.this.startActivity(intent2);
                }
                ScanActivity.this.finish();
            }
        });
        ((Button) viewInflate.findViewById(R.id.qr_scan)).setOnClickListener(new View.OnClickListener() { // from class: activity.ScanActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (str4.contains("&pk=")) {
                    String[] strArrSplit = str4.split("&");
                    String strSubstring = "";
                    String strSubstring2 = "";
                    for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                        if (strArrSplit[i2].contains("pk=")) {
                            strSubstring = strArrSplit[i2].substring(3, strArrSplit[i2].length());
                        } else if (strArrSplit[i2].contains("dn=")) {
                            strSubstring2 = strArrSplit[i2].substring(3, strArrSplit[i2].length());
                        }
                    }
                    boolean zContains = str4.contains("a1uZPeFdASC");
                    Intent intent = new Intent(ScanActivity.this.getActivity(), (Class<?>) InputWifiActivity.class);
                    intent.putExtra("isDualBandWiFi", zContains);
                    intent.putExtra("type", "scan");
                    ScanActivity.this.startActivityForResult(intent, 100);
                    ScanActivity.this.pk = strSubstring;
                    ScanActivity.this.dn = strSubstring2;
                    alertDialogCreate.dismiss();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTwo(final String str, final String str2, final String str3) {
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setIcon(R.drawable.app_launcher).setTitle(getString(R.string.app_name)).setMessage(getString(R.string.scan_traffic)).setPositiveButton(getString(R.string.scan), new DialogInterface.OnClickListener() { // from class: activity.ScanActivity.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ScanActivity.this.getActivity(), (Class<?>) Traffic4GActivity.class);
                intent.putExtra(AlinkConstants.KEY_PK, str);
                intent.putExtra(AlinkConstants.KEY_DN, str2);
                intent.putExtra("iccid", str3);
                ScanActivity.this.startActivity(intent);
                dialogInterface.dismiss();
                ScanActivity.this.finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: activity.ScanActivity.11
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.ScanActivity.13
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                ScanActivity.this.isScanSuccess = false;
            }
        });
        alertDialogCreate.show();
        alertDialogCreate.getButton(-1).setTextColor(Color.parseColor("#2c99fd"));
        alertDialogCreate.getButton(-2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
        Button button = alertDialogCreate.getButton(-1);
        Button button2 = alertDialogCreate.getButton(-2);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.weight = 10.0f;
        button.setLayoutParams(layoutParams);
        button2.setLayoutParams(layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showThere(final String str) {
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setIcon(R.drawable.app_launcher).setTitle(getString(R.string.app_name)).setMessage(getString(R.string.scan_traffic)).setPositiveButton(getString(R.string.scan), new DialogInterface.OnClickListener() { // from class: activity.ScanActivity.15
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ScanActivity.this.getActivity(), (Class<?>) VsimTraffic4GActivity.class);
                intent.putExtra(Constants.KEY_IMEI, str);
                ScanActivity.this.startActivity(intent);
                dialogInterface.dismiss();
                ScanActivity.this.finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class: activity.ScanActivity.14
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.ScanActivity.16
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                ScanActivity.this.isScanSuccess = false;
            }
        });
        alertDialogCreate.show();
        alertDialogCreate.getButton(-1).setTextColor(Color.parseColor("#2c99fd"));
        alertDialogCreate.getButton(-2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
        Button button = alertDialogCreate.getButton(-1);
        Button button2 = alertDialogCreate.getButton(-2);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.weight = 10.0f;
        button.setLayoutParams(layoutParams);
        button2.setLayoutParams(layoutParams);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1) {
            Intent intent2 = new Intent(this, (Class<?>) ScanQrCodeTipsActivity.class);
            intent2.putExtra("wifiSSid", intent.getStringExtra("wifiSSid"));
            intent2.putExtra("wifiPwd", intent.getStringExtra("wifiPwd"));
            intent2.putExtra("type", getIntent().getStringExtra("type"));
            intent2.putExtra(AlinkConstants.KEY_PK, this.pk);
            intent2.putExtra(AlinkConstants.KEY_DN, this.dn);
            intent2.putExtra(AlinkConstants.KEY_WIFI_TYPE, intent.getIntExtra(AlinkConstants.KEY_WIFI_TYPE, 0));
            startActivity(intent2);
            finish();
            return;
        }
        if (i == 111 && i2 == -1) {
            Intent intent3 = new Intent(this, (Class<?>) ScanQrCodeTipsActivity.class);
            intent3.putExtra("type", getIntent().getStringExtra("type"));
            intent3.putExtra("wifiSSid", intent.getStringExtra("wifiSSid"));
            intent3.putExtra("wifiPwd", intent.getStringExtra("wifiPwd"));
            intent3.putExtra(AlinkConstants.KEY_PK, this.pk);
            intent3.putExtra(AlinkConstants.KEY_DN, this.dn);
            intent3.putExtra(AlinkConstants.KEY_WIFI_TYPE, intent.getIntExtra(AlinkConstants.KEY_WIFI_TYPE, 0));
            startActivity(intent3);
            finish();
            return;
        }
        if (i == 10002 && i2 == -1) {
            HomeTabFragment.setLanConnect();
            finish();
            return;
        }
        if (i == 1 && i2 == -1 && intent != null) {
            String[] strArr = {"_data"};
            Cursor cursorQuery = getContentResolver().query(intent.getData(), strArr, null, null, null);
            cursorQuery.moveToFirst();
            final String string = cursorQuery.getString(cursorQuery.getColumnIndex(strArr[0]));
            cursorQuery.close();
            new Thread(new Runnable() { // from class: activity.ScanActivity.17
                @Override // java.lang.Runnable
                public void run() {
                    Result resultScanningImage = ScanActivity.this.scanningImage(string);
                    if (resultScanningImage != null) {
                        ScanActivity.this.beepManager.playBeepSoundAndVibrate();
                        final String strRecode = ScanActivity.this.recode(resultScanningImage.toString());
                        Intent intent4 = new Intent();
                        intent4.putExtra("result", strRecode);
                        ScanActivity.this.setResult(-1, intent4);
                        ScanActivity.this.uiHandler.post(new Runnable() { // from class: activity.ScanActivity.17.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ScanActivity.this.qrcodeResult(strRecode);
                            }
                        });
                        return;
                    }
                    Looper.prepare();
                    Toast.makeText(ScanActivity.this.getApplicationContext(), ScanActivity.this.getString(R.string.wrong_format), 0).show();
                    Looper.loop();
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void myRequetPermission() {
        try {
            if (ContextCompat.checkSelfPermission(this, Permission.WRITE_EXTERNAL_STORAGE) != 0) {
                ActivityCompat.requestPermissions(this, new String[]{Permission.READ_PHONE_STATE, Permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (iArr[i2] == 0) {
                    AlertDialog alertDialog = this.mDialog;
                    if (alertDialog != null && alertDialog.isShowing()) {
                        this.mDialog.dismiss();
                    }
                    AlertDialog alertDialog2 = this.alertDialog;
                    if (alertDialog2 != null) {
                        alertDialog2.dismiss();
                        this.alertDialog = null;
                    }
                    startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1);
                    return;
                }
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, strArr[i2])) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.share_permission)).setMessage("点击允许才可以使用扫描本地二维码的功能").setPositiveButton(getString(R.string.allow), new DialogInterface.OnClickListener() { // from class: activity.ScanActivity.18
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i3) {
                            if (ScanActivity.this.mDialog != null) {
                                ScanActivity.this.mDialog.dismiss();
                            }
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", ScanActivity.this.getPackageName(), null));
                            ScanActivity.this.startActivityForResult(intent, 2);
                        }
                    });
                    this.mDialog = builder.create();
                    this.mDialog.setCanceledOnTouchOutside(true);
                    this.mDialog.show();
                } else {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                    builder2.setTitle(getString(R.string.share_permission)).setMessage("点击允许才可以使用我们的app哦").setPositiveButton(getString(R.string.allow), new DialogInterface.OnClickListener() { // from class: activity.ScanActivity.19
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i3) {
                            if (ScanActivity.this.alertDialog != null) {
                                ScanActivity.this.alertDialog.dismiss();
                            }
                            ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                    });
                    this.alertDialog = builder2.create();
                    this.alertDialog.setCanceledOnTouchOutside(false);
                    this.alertDialog.show();
                }
            }
        }
    }

    protected Result scanningImage(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Hashtable hashtable = new Hashtable();
        hashtable.put(DecodeHintType.CHARACTER_SET, "utf-8");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inJustDecodeBounds = false;
        int i = (int) (options.outHeight / 200.0f);
        options.inSampleSize = i > 0 ? i : 1;
        Bitmap bitmapDecodeFile = BitmapFactory.decodeFile(str, options);
        if (bitmapDecodeFile != null && bitmapDecodeFile.getWidth() != 0 && bitmapDecodeFile.getHeight() != 0) {
            int[] iArr = new int[bitmapDecodeFile.getWidth() * bitmapDecodeFile.getHeight()];
            bitmapDecodeFile.getPixels(iArr, 0, bitmapDecodeFile.getWidth(), 0, 0, bitmapDecodeFile.getWidth(), bitmapDecodeFile.getHeight());
            try {
                return new QRCodeReader().decode(new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(bitmapDecodeFile.getWidth(), bitmapDecodeFile.getHeight(), iArr))), hashtable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String recode(String str) {
        String str2 = "";
        try {
            if (Charset.forName(CharEncoding.ISO_8859_1).newEncoder().canEncode(str)) {
                String str3 = new String(str.getBytes(CharEncoding.ISO_8859_1), StringUtils.GB2312);
                try {
                    Log.i("1234      ISO8859-1", str3);
                    return str3;
                } catch (UnsupportedEncodingException e) {
                    e = e;
                    str2 = str3;
                }
            } else {
                try {
                    Log.i("1234      stringExtra", str);
                    return str;
                } catch (UnsupportedEncodingException e2) {
                    str2 = str;
                    e = e2;
                }
            }
        } catch (UnsupportedEncodingException e3) {
            e = e3;
        }
        e.printStackTrace();
        return str2;
    }
}
