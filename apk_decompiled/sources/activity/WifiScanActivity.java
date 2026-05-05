package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import bean.setFinish;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.util.Base64Utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.seculink.app.R;
import config.AppConfig;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import org.greenrobot.eventbus.EventBus;
import tools.AudioPlayManager;
import tools.ScreenUtil;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class WifiScanActivity extends CommonActivity implements View.OnClickListener {
    Button bt_next;
    TextView bt_next_;
    CountDownTimer countDownTimer;
    TitleView fl_titlebar;
    ImageView iv_tip;
    ScrollView layout_main;
    ImageView scanImg;
    boolean tips = false;
    TextView tv_tip;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_wifi_scan;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        this.layout_main = (ScrollView) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        super.initWidget(bundle);
        this.scanImg = (ImageView) findViewById(R.id.img);
        this.tv_tip = (TextView) findViewById(R.id.tv_tip);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.iv_tip = (ImageView) findViewById(R.id.iv_tip);
        this.bt_next = (Button) findViewById(R.id.bt_next);
        this.bt_next_ = (TextView) findViewById(R.id.bt_next_);
        this.bt_next.setOnClickListener(this);
        this.bt_next_.setOnClickListener(this);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.WifiScanActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                WifiScanActivity.this.finish();
                WifiScanActivity.this.sendBroadcast(new Intent("com.aliyun.iot.ilop.demo.page.scan.ScanQrCodeTips"));
                if (WifiScanActivity.this.getIntent().getStringExtra("json") != null) {
                    Intent intent = new Intent(WifiScanActivity.this.getActivity(), (Class<?>) InputWifiActivity.class);
                    intent.putExtra("type", "newType");
                    intent.putExtra(AlinkConstants.KEY_PK, WifiScanActivity.this.getIntent().getStringExtra(AlinkConstants.KEY_PK));
                    intent.putExtra(AlinkConstants.KEY_DN, WifiScanActivity.this.getIntent().getStringExtra(AlinkConstants.KEY_DN));
                    WifiScanActivity.this.startActivity(intent);
                }
            }
        });
        this.tv_tip.setText(Html.fromHtml(getString(R.string.scan_qr_tip)));
        Glide.with((FragmentActivity) this).load(Integer.valueOf(R.drawable.instruction_new)).into(new SimpleTarget<Drawable>() { // from class: activity.WifiScanActivity.2
            @Override // com.bumptech.glide.request.target.Target
            public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
                onResourceReady((Drawable) obj, (Transition<? super Drawable>) transition);
            }

            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                if (!WifiScanActivity.this.isFinishing() && (drawable instanceof GifDrawable)) {
                    GifDrawable gifDrawable = (GifDrawable) drawable;
                    gifDrawable.setLoopCount(-1);
                    gifDrawable.start();
                    WifiScanActivity.this.iv_tip.setImageDrawable(drawable);
                }
            }

            @Override // com.bumptech.glide.request.target.BaseTarget, com.bumptech.glide.request.target.Target
            public void onLoadCleared(@Nullable Drawable drawable) {
                super.onLoadCleared(drawable);
            }
        });
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [activity.WifiScanActivity$4] */
    /* JADX WARN: Type inference failed for: r2v6, types: [activity.WifiScanActivity$3] */
    @Override // activity.CommonActivity
    protected void initData() {
        final String strReplaceAll;
        super.initData();
        if (getIntent().getStringExtra("json") != null) {
            final int iDp2Px = ScreenUtil.getDisplayMetrics(this)[0] - (ScreenUtil.dp2Px(this, 20.0f) * 2);
            StringBuilder sb = new StringBuilder(getIntent().getStringExtra("json"));
            sb.insert(sb.length() - 1, ",\"e\":" + getIntent().getIntExtra(AlinkConstants.KEY_WIFI_TYPE, 0) + "");
            if (!AppConfig.isChina) {
                sb.insert(sb.length() - 1, ",\"r\":" + IoTSmart.getShortRegionId() + "");
            }
            final String string = sb.toString();
            showProgressDialog();
            new Thread() { // from class: activity.WifiScanActivity.3
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    if (WifiScanActivity.this.isFinishing()) {
                        return;
                    }
                    WifiScanActivity wifiScanActivity = WifiScanActivity.this;
                    String str = string;
                    ImageView imageView = wifiScanActivity.scanImg;
                    int i = iDp2Px;
                    wifiScanActivity.createQRcodeImage(str, imageView, i, i);
                }
            }.start();
            return;
        }
        String stringExtra = getIntent().getStringExtra("wifiSSid");
        String stringExtra2 = getIntent().getStringExtra("wifiPwd");
        if (AppConfig.isChina) {
            strReplaceAll = "{\"p\":\"" + stringExtra2 + "\",\"n\":\"" + stringExtra + "\"}";
        } else {
            strReplaceAll = "{\"p\":\"" + stringExtra2 + "\",\"n\":\"" + stringExtra + "\",\"r\":" + IoTSmart.getShortRegionId() + "}";
        }
        Log.e("wifi_json", "" + strReplaceAll);
        try {
            strReplaceAll = Base64Utils.encode(strReplaceAll.getBytes("UTF-8")).replaceAll("[\\s*\t\n\r]", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final int iDp2Px2 = ScreenUtil.getDisplayMetrics(this)[0] - (ScreenUtil.dp2Px(this, 20.0f) * 2);
        showProgressDialog();
        new Thread() { // from class: activity.WifiScanActivity.4
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                if (WifiScanActivity.this.isFinishing()) {
                    return;
                }
                WifiScanActivity wifiScanActivity = WifiScanActivity.this;
                String str = strReplaceAll;
                ImageView imageView = wifiScanActivity.scanImg;
                int i = iDp2Px2;
                wifiScanActivity.createQRcodeImage(str, imageView, i, i);
            }
        }.start();
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [activity.WifiScanActivity$5] */
    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        AudioPlayManager.newBuilder().load(this, Integer.valueOf(R.raw.wifi_scan), AudioPlayManager.MediaPlayerBuilder.TYPE.RAW).build().play();
        try {
            if (getIntent().getStringExtra("type").equals("type")) {
                this.countDownTimer = new CountDownTimer(60000L, 1000L) { // from class: activity.WifiScanActivity.5
                    @Override // android.os.CountDownTimer
                    public void onTick(long j) {
                    }

                    @Override // android.os.CountDownTimer
                    public void onFinish() {
                        if (WifiScanActivity.this.tips) {
                            return;
                        }
                        WifiScanActivity.this.showTips();
                    }
                }.start();
            }
        } catch (NullPointerException unused) {
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        AudioPlayManager.with().release();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTips() {
        this.tips = true;
        TextView textView = new TextView(this);
        textView.setText(R.string.wifi_text);
        textView.setTextSize(20.0f);
        textView.setGravity(17);
        textView.setPadding(0, 50, 0, 0);
        textView.setTextColor(getResources().getColor(R.color.color_black));
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setCustomTitle(textView).setMessage(R.string.text_mei).setPositiveButton(R.string.wifi_text1, new DialogInterface.OnClickListener() { // from class: activity.WifiScanActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                WifiScanActivity.this.tt();
            }
        }).setNegativeButton(R.string.wifi_text2, new DialogInterface.OnClickListener() { // from class: activity.WifiScanActivity.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                WifiScanActivity.this.sendBroadcast(new Intent("com.aliyun.iot.ilop.demo.page.scan.ScanQrCodeTips"));
                Intent intent = new Intent(WifiScanActivity.this.getActivity(), (Class<?>) InputWifiActivity.class);
                intent.putExtra("type", "newType");
                intent.putExtra(AlinkConstants.KEY_PK, WifiScanActivity.this.getIntent().getStringExtra(AlinkConstants.KEY_PK));
                intent.putExtra(AlinkConstants.KEY_DN, WifiScanActivity.this.getIntent().getStringExtra(AlinkConstants.KEY_DN));
                WifiScanActivity.this.startActivity(intent);
                WifiScanActivity.this.finish();
            }
        }).create();
        if (!isFinishing() && !alertDialogCreate.isShowing()) {
            alertDialogCreate.show();
        }
        if (alertDialogCreate == null || alertDialogCreate.getButton(-1) == null || alertDialogCreate == null || alertDialogCreate.getButton(-2) == null) {
            return;
        }
        alertDialogCreate.getButton(-1).setTextColor(getResources().getColor(R.color.color_2c99fd));
        alertDialogCreate.getButton(-2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
        Button button = alertDialogCreate.getButton(-1);
        Button button2 = alertDialogCreate.getButton(-2);
        button2.setTextSize(18.0f);
        button.setTextSize(18.0f);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.weight = 10.0f;
        button.setLayoutParams(layoutParams);
        button2.setLayoutParams(layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tt() {
        sendBroadcast(new Intent("com.aliyun.iot.ilop.demo.page.scan.ScanQrCodeTips"));
        Intent intent = new Intent(this, (Class<?>) ScanQrCodeTipsActivity.class);
        intent.putExtra("type", "scan");
        intent.putExtra("wifiSSid", getIntent().getStringExtra("wifiSSid"));
        intent.putExtra("wifiPwd", getIntent().getStringExtra("wifiPwd"));
        intent.putExtra(AlinkConstants.KEY_PK, getIntent().getStringExtra(AlinkConstants.KEY_PK));
        intent.putExtra(AlinkConstants.KEY_DN, getIntent().getStringExtra(AlinkConstants.KEY_DN));
        Log.d(this.TAG, "onViewClicked: -------------------------------------------------------");
        startActivity(intent);
        finish();
    }

    public void createQRcodeImage(String str, final ImageView imageView, int i, int i2) {
        try {
            Hashtable hashtable = new Hashtable();
            hashtable.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hashtable.put(EncodeHintType.MARGIN, 0);
            hashtable.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            BitMatrix bitMatrixDeleteWhite = deleteWhite(new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, i, i2, hashtable));
            int width = bitMatrixDeleteWhite.getWidth();
            int height = bitMatrixDeleteWhite.getHeight();
            int[] iArr = new int[width * height];
            for (int i3 = 0; i3 < height; i3++) {
                for (int i4 = 0; i4 < width; i4++) {
                    if (bitMatrixDeleteWhite.get(i4, i3)) {
                        iArr[(i3 * width) + i4] = -16777216;
                    } else {
                        iArr[(i3 * width) + i4] = -1;
                    }
                }
            }
            final Bitmap bitmapCreateBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmapCreateBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
            runOnUiThread(new Runnable() { // from class: activity.WifiScanActivity.8
                @Override // java.lang.Runnable
                public void run() {
                    imageView.setImageBitmap(bitmapCreateBitmap);
                    WifiScanActivity.this.dismissProgressDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() { // from class: activity.WifiScanActivity.9
                @Override // java.lang.Runnable
                public void run() {
                    WifiScanActivity.this.dismissProgressDialog();
                }
            });
        }
    }

    private static BitMatrix deleteWhite(BitMatrix bitMatrix) {
        int[] enclosingRectangle = bitMatrix.getEnclosingRectangle();
        int i = enclosingRectangle[2] + 1;
        int i2 = enclosingRectangle[3] + 1;
        BitMatrix bitMatrix2 = new BitMatrix(i, i2);
        bitMatrix2.clear();
        for (int i3 = 0; i3 < i; i3++) {
            for (int i4 = 0; i4 < i2; i4++) {
                if (bitMatrix.get(enclosingRectangle[0] + i3, enclosingRectangle[1] + i4)) {
                    bitMatrix2.set(i3, i4);
                }
            }
        }
        return bitMatrix2;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 10000 && i2 == -1) {
            setResult(-1);
            EventBus.getDefault().post(new setFinish("activity.scanNavigationActivity"));
            finish();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        switch (view2.getId()) {
            case R.id.bt_next /* 2131296478 */:
                setResult(-1);
                EventBus.getDefault().post(new setFinish("activity.scanNavigationActivity"));
                finish();
                break;
            case R.id.bt_next_ /* 2131296479 */:
                startActivityForResult(new Intent(this, (Class<?>) WifiScanFailActivity.class), 10000);
                break;
        }
    }
}
