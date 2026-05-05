package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
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
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
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
import com.huawei.hms.framework.common.ContainerUtils;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.seculink.app.R;
import com.taobao.accs.common.Constants;
import dialog.BaseDialog;
import dialog.DialogUtil;
import dialog.ProgressDialog;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.codec.CharEncoding;
import tools.AudioPlayManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class ScanSIMActivity extends CommonActivity {
    private static final int IMAGE = 1;
    private static final int NOT_NOTICE = 2;
    public static final int REQUEST_CODE = 10002;
    ImageView Flashlight;
    private AlertDialog alertDialog;
    private DecoratedBarcodeView barcodeScannerView;
    private BeepManager beepManager;

    /* JADX INFO: renamed from: dialog, reason: collision with root package name */
    private BaseDialog f1583dialog;
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
    private BarcodeCallback callback = new BarcodeCallback() { // from class: activity.ScanSIMActivity.1
        @Override // com.journeyapps.barcodescanner.BarcodeCallback
        public void possibleResultPoints(List<ResultPoint> list) {
        }

        @Override // com.journeyapps.barcodescanner.BarcodeCallback
        public void barcodeResult(final BarcodeResult barcodeResult) {
            ScanSIMActivity.this.uiHandler.post(new Runnable() { // from class: activity.ScanSIMActivity.1.1
                @Override // java.lang.Runnable
                public void run() {
                    Activity activity2 = ScanSIMActivity.this.getActivity();
                    if (activity2 == null || activity2.isFinishing()) {
                        return;
                    }
                    Log.e("扫描", "" + barcodeResult.getText());
                    ScanSIMActivity.this.doScanQrCode(barcodeResult.getText());
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
        this.tv_title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.ScanSIMActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                ScanSIMActivity.this.onBackPressed();
            }

            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
                ScanSIMActivity scanSIMActivity = ScanSIMActivity.this;
                DialogUtil.showCancelConfirmDiaLog(scanSIMActivity, scanSIMActivity.getString(R.string.no_permission), ScanSIMActivity.this.getString(R.string.album_service_des), ScanSIMActivity.this.getString(R.string.cancel), ScanSIMActivity.this.getString(R.string.confirm), new DialogUtil.OnCancelConfirmClickListener() { // from class: activity.ScanSIMActivity.2.1
                    @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                    public void CancelListener() {
                    }

                    @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                    public void ConfirmListener() {
                        ScanSIMActivity.this.myRequetPermission();
                    }
                });
            }
        });
        this.tv_title.setTitleBackText("");
        this.tv_title.setTitleTextColor(R.color.color_white);
        this.Flashlight = (ImageView) findViewById(R.id.Flashlight);
        this.flashlight_text = (TextView) findViewById(R.id.flashlight_text);
        this.old_connect = (Button) findViewById(R.id.old_connect);
        this.old_connect.setOnClickListener(new View.OnClickListener() { // from class: activity.ScanSIMActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(ScanSIMActivity.this.getActivity(), (Class<?>) InputWifiActivity.class);
                intent.putExtra("type", TmpConstant.GROUP_OP_ADD);
                ScanSIMActivity.this.startActivityForResult(intent, 10002);
            }
        });
        this.old_connect.setVisibility(8);
        this.barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.dbv_custom);
        this.barcodeScannerView.initializeFromIntent(getIntent());
        this.barcodeScannerView.decodeContinuous(this.callback);
        this.flashlight_text.setOnClickListener(new View.OnClickListener() { // from class: activity.ScanSIMActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (!ScanSIMActivity.this.isLightOpen) {
                    ScanSIMActivity scanSIMActivity = ScanSIMActivity.this;
                    scanSIMActivity.isLightOpen = true;
                    scanSIMActivity.flashlight_text.setText(R.string.close_flashlight);
                    ScanSIMActivity.this.Flashlight.setImageResource(R.drawable.scan_flash_on);
                    ScanSIMActivity.this.barcodeScannerView.setTorchOn();
                    return;
                }
                ScanSIMActivity scanSIMActivity2 = ScanSIMActivity.this;
                scanSIMActivity2.isLightOpen = false;
                scanSIMActivity2.flashlight_text.setText(R.string.open_flashlight);
                ScanSIMActivity.this.Flashlight.setImageResource(R.drawable.scan_flash_off);
                ScanSIMActivity.this.barcodeScannerView.setTorchOff();
            }
        });
        this.Flashlight.setOnClickListener(new View.OnClickListener() { // from class: activity.ScanSIMActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (!ScanSIMActivity.this.isLightOpen) {
                    ScanSIMActivity scanSIMActivity = ScanSIMActivity.this;
                    scanSIMActivity.isLightOpen = true;
                    scanSIMActivity.flashlight_text.setText(R.string.close_flashlight);
                    ScanSIMActivity.this.Flashlight.setImageResource(R.drawable.scan_flash_on);
                    ScanSIMActivity.this.barcodeScannerView.setTorchOn();
                    return;
                }
                ScanSIMActivity scanSIMActivity2 = ScanSIMActivity.this;
                scanSIMActivity2.isLightOpen = false;
                scanSIMActivity2.flashlight_text.setText(R.string.open_flashlight);
                ScanSIMActivity.this.Flashlight.setImageResource(R.drawable.scan_flash_off);
                ScanSIMActivity.this.barcodeScannerView.setTorchOff();
            }
        });
        this.beepManager = new BeepManager(this);
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(), Permission.CAMERA) != 0) {
            DialogUtil.showCancelConfirmDiaLog(this, getString(R.string.no_permission), getString(R.string.location_service_des), getString(R.string.cancel), getString(R.string.confirm), new DialogUtil.OnCancelConfirmClickListener() { // from class: activity.ScanSIMActivity.6
                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                public void CancelListener() {
                }

                @Override // dialog.DialogUtil.OnCancelConfirmClickListener
                public void ConfirmListener() {
                    XXPermissions.with(ScanSIMActivity.this).permission(Permission.CAMERA).request(new OnPermissionCallback() { // from class: activity.ScanSIMActivity.6.1
                        @Override // com.hjq.permissions.OnPermissionCallback
                        public void onDenied(@NonNull List<String> list, boolean z) {
                        }

                        @Override // com.hjq.permissions.OnPermissionCallback
                        public void onGranted(@NonNull List<String> list, boolean z) {
                            if (XXPermissions.isGranted(ScanSIMActivity.this, Permission.CAMERA)) {
                                ScanSIMActivity.this.barcodeScannerView.initializeFromIntent(ScanSIMActivity.this.getIntent());
                                ScanSIMActivity.this.barcodeScannerView.decodeContinuous(ScanSIMActivity.this.callback);
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
        if (this.f1583dialog == null) {
            this.f1583dialog = new BaseDialog.Builder().view(R.layout.dialog_scan).content(getString(R.string.no_found_qr_code)).title(getString(R.string.qr_code_error_title)).rightBtnText(getString(R.string.i_know)).canCancel(false).create();
        }
        if (!this.f1583dialog.isShowing()) {
            this.f1583dialog.show(getSupportFragmentManager(), "");
        }
        this.isScanSuccess = false;
    }

    public void qrcodeResult(String str) {
        this.isScanSuccess = true;
        if (!str.contains("im=") && !str.contains("imei=")) {
            tipNoFoundQrCode();
        }
        String strExtractImei = extractImei(str);
        if (!strExtractImei.isEmpty()) {
            Intent intent = new Intent(this, (Class<?>) SIMWebActivity.class);
            intent.putExtra(Constants.KEY_IMEI, strExtractImei);
            startActivity(intent);
            finish();
        }
        String strExtractIccid = extractIccid(str);
        if (strExtractIccid.isEmpty()) {
            return;
        }
        Intent intent2 = new Intent(this, (Class<?>) SIMWebActivity.class);
        intent2.putExtra("iccid", strExtractIccid);
        startActivity(intent2);
        finish();
    }

    public static String extractImei(String str) {
        String[] strArrSplit;
        try {
            strArrSplit = str.split("\\?");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (strArrSplit.length < 2) {
            return null;
        }
        for (String str2 : strArrSplit[1].split("&")) {
            String[] strArrSplit2 = str2.split(ContainerUtils.KEY_VALUE_DELIMITER, 2);
            if (strArrSplit2.length == 2 && "im".equals(strArrSplit2[0])) {
                return URLDecoder.decode(strArrSplit2[1], "UTF-8");
            }
            if (strArrSplit2.length == 2 && Constants.KEY_IMEI.equals(strArrSplit2[0])) {
                return URLDecoder.decode(strArrSplit2[1], "UTF-8");
            }
        }
        return null;
    }

    public static String extractIccid(String str) {
        String[] strArrSplit;
        try {
            strArrSplit = str.split("\\?");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (strArrSplit.length < 2) {
            return null;
        }
        for (String str2 : strArrSplit[1].split("&")) {
            String[] strArrSplit2 = str2.split(ContainerUtils.KEY_VALUE_DELIMITER, 2);
            if (strArrSplit2.length == 2 && "ic".equals(strArrSplit2[0])) {
                return URLDecoder.decode(strArrSplit2[1], "UTF-8");
            }
            if (strArrSplit2.length == 2 && "ic2".equals(strArrSplit2[0])) {
                return URLDecoder.decode(strArrSplit2[1], "UTF-8");
            }
        }
        return null;
    }

    void setupNet(String str) {
        String strSubstring = null;
        String strSubstring2 = null;
        for (String str2 : str.split("&")) {
            if (str2.length() > 3 && str2.startsWith("pk=")) {
                this.pk = str2.substring(3);
            }
            if (str2.length() > 3 && str2.startsWith("dn=")) {
                this.dn = str2.substring(3);
            }
            if (str2.length() > 3 && str2.startsWith("ic=")) {
                strSubstring = str2.substring(3);
            }
            if (str2.length() > 3 && str2.startsWith("ic2=")) {
                strSubstring = str2.substring(3);
            }
            if (str2.length() > 3 && str2.startsWith("im=")) {
                strSubstring2 = str2.substring(3);
            }
            if (str2.length() > 3 && str2.startsWith("imei=")) {
                strSubstring2 = str2.substring(3);
            }
        }
        if (strSubstring.isEmpty() && strSubstring2.isEmpty()) {
            tipNoFoundQrCode();
            return;
        }
        if (!strSubstring2.isEmpty()) {
            Intent intent = new Intent(this, (Class<?>) SIMWebActivity.class);
            intent.putExtra(Constants.KEY_IMEI, strSubstring2);
            startActivity(intent);
            finish();
        }
        if (strSubstring.isEmpty()) {
            return;
        }
        Intent intent2 = new Intent(this, (Class<?>) SIMWebActivity.class);
        intent2.putExtra("iccid", strSubstring);
        startActivity(intent2);
        finish();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1 && i2 == -1 && intent != null) {
            String[] strArr = {"_data"};
            Cursor cursorQuery = getContentResolver().query(intent.getData(), strArr, null, null, null);
            cursorQuery.moveToFirst();
            final String string = cursorQuery.getString(cursorQuery.getColumnIndex(strArr[0]));
            cursorQuery.close();
            new Thread(new Runnable() { // from class: activity.ScanSIMActivity.7
                @Override // java.lang.Runnable
                public void run() {
                    Result resultScanningImage = ScanSIMActivity.this.scanningImage(string);
                    if (resultScanningImage != null) {
                        ScanSIMActivity.this.beepManager.playBeepSoundAndVibrate();
                        final String strRecode = ScanSIMActivity.this.recode(resultScanningImage.toString());
                        Intent intent2 = new Intent();
                        intent2.putExtra("result", strRecode);
                        ScanSIMActivity.this.setResult(-1, intent2);
                        ScanSIMActivity.this.uiHandler.post(new Runnable() { // from class: activity.ScanSIMActivity.7.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ScanSIMActivity.this.qrcodeResult(strRecode);
                            }
                        });
                        return;
                    }
                    Looper.prepare();
                    Toast.makeText(ScanSIMActivity.this.getApplicationContext(), ScanSIMActivity.this.getString(R.string.wrong_format), 0).show();
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
                    builder.setTitle(getString(R.string.share_permission)).setMessage("点击允许才可以使用扫描本地二维码的功能").setPositiveButton(getString(R.string.allow), new DialogInterface.OnClickListener() { // from class: activity.ScanSIMActivity.8
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i3) {
                            if (ScanSIMActivity.this.mDialog != null) {
                                ScanSIMActivity.this.mDialog.dismiss();
                            }
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", ScanSIMActivity.this.getPackageName(), null));
                            ScanSIMActivity.this.startActivityForResult(intent, 2);
                        }
                    });
                    this.mDialog = builder.create();
                    this.mDialog.setCanceledOnTouchOutside(true);
                    this.mDialog.show();
                } else {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                    builder2.setTitle(getString(R.string.share_permission)).setMessage("点击允许才可以使用我们的app哦").setPositiveButton(getString(R.string.allow), new DialogInterface.OnClickListener() { // from class: activity.ScanSIMActivity.9
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i3) {
                            if (ScanSIMActivity.this.alertDialog != null) {
                                ScanSIMActivity.this.alertDialog.dismiss();
                            }
                            ActivityCompat.requestPermissions(ScanSIMActivity.this, new String[]{Permission.WRITE_EXTERNAL_STORAGE}, 1);
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
