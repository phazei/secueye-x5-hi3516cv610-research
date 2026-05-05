package activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Process;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.framework.AActivity;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.framework.region.CountryManager;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.aliyun.iot.link.ui.component.LinkToast;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.hjq.permissions.Permission;
import com.seculink.app.R;
import com.smarx.notchlib.NotchScreenManager;
import config.AppConfig;
import sdk.DemoApplication;
import sdk.SDKInitHelper;
import tools.FileCacheUtils;
import tools.FileUtil;
import tools.SharePreferenceManager;
import tools.SystemUtil;

/* JADX INFO: loaded from: classes.dex */
public class StartActivity extends AActivity {
    private static final String TAG = "StartActivity";
    private RelativeLayout containerFl;
    private CountDownTimer countDownTimer;
    private ImageView iv_img;
    private LinearLayout layout;
    private SharedPreferences sharedPreferences;
    private Handler mH = new Handler();
    private String mSpaceId = "111065";
    public boolean canJumpImmediately = false;

    private void loadAMPSAd() {
    }

    @Override // com.aliyun.iot.aep.sdk.framework.AActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        NotchScreenManager.getInstance().setDisplayInNotch(this);
        hideStatusBar();
        setContentView(R.layout.start_activity);
        this.containerFl = (RelativeLayout) findViewById(R.id.containerFl);
        this.iv_img = (ImageView) findViewById(R.id.iv_img);
        this.layout = (LinearLayout) findViewById(R.id.layout);
        if (AppConfig.isChina) {
            this.iv_img.setImageResource(R.drawable.view_splash_cn);
        } else {
            this.iv_img.setImageResource(R.drawable.view_splash_en);
        }
        this.countDownTimer = new CountDownTimer(AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS, 1000L) { // from class: activity.StartActivity.1
            @Override // android.os.CountDownTimer
            public void onTick(long j) {
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                if (AppConfig.isChina || StartActivity.this.isFinishing()) {
                    return;
                }
                if (!LoginBusiness.isLogin()) {
                    StartActivity.this.gotoCountryList();
                    return;
                }
                ((DemoApplication) DemoApplication.getInstance()).startPushAndConnect();
                Intent intent = new Intent(DemoApplication.getInstance(), (Class<?>) MainActivity.class);
                intent.setFlags(268468224);
                DemoApplication.getInstance().startActivity(intent);
            }
        };
        this.sharedPreferences = getSharedPreferences("FirstRun", 0);
        if (this.sharedPreferences.getBoolean("First", true)) {
            if (AppConfig.isChina) {
                checkPermission();
                return;
            } else {
                this.sharedPreferences.edit().putBoolean("First", false).apply();
                extLogcat();
                return;
            }
        }
        extLogcat();
        if (AppConfig.isChina) {
            if (SharePreferenceManager.getInstance().getADSwitch() == 1) {
                loadAMPSAd();
            } else {
                jump();
            }
        }
    }

    private void jumpWhenCanClick() {
        if (this.canJumpImmediately) {
            if (LoginBusiness.isLogin()) {
                ((DemoApplication) DemoApplication.getInstance()).startPushAndConnect();
                Intent intent = new Intent(DemoApplication.getInstance(), (Class<?>) MainActivity.class);
                intent.setFlags(268468224);
                DemoApplication.getInstance().startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                return;
            }
            startLogin();
            return;
        }
        this.canJumpImmediately = true;
    }

    @Override // com.aliyun.iot.aep.sdk.framework.AActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
    }

    @Override // com.aliyun.iot.aep.sdk.framework.AActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
    }

    private void jump() {
        if (LoginBusiness.isLogin()) {
            ((DemoApplication) DemoApplication.getInstance()).startPushAndConnect();
            Intent intent = new Intent(DemoApplication.getInstance(), (Class<?>) MainActivity.class);
            intent.setFlags(268468224);
            DemoApplication.getInstance().startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
            return;
        }
        startLogin();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void gotoCountryList() {
        if (AppConfig.isChina) {
            IoTSmart.Country country = new IoTSmart.Country();
            country.code = "86";
            country.areaName = "中国大陆";
            country.domainAbbreviation = CountryManager.COUNTRY_CHINA_ABBR;
            country.isoCode = "CHN";
            country.pinyin = "ZhongGuoDaLu";
            IoTSmart.setCountry(country, new IoTSmart.ICountrySetCallBack() { // from class: activity.-$$Lambda$StartActivity$aheqt0GKROxf3g2iqA8D8Yc8tKE
                @Override // com.aliyun.iot.aep.sdk.IoTSmart.ICountrySetCallBack
                public final void onCountrySet(boolean z) {
                    StartActivity.lambda$gotoCountryList$0(this.f$0, z);
                }
            });
            return;
        }
        if (GlobalConfig.getInstance().getCountry() == null) {
            Intent intent = new Intent(this, (Class<?>) CountryListActivity.class);
            intent.putExtra("needLogin", false);
            intent.putExtra("type", "0");
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
            return;
        }
        startLogin();
    }

    public static /* synthetic */ void lambda$gotoCountryList$0(StartActivity startActivity, boolean z) {
        if (z) {
            startActivity.killProcess();
        } else {
            SDKInitHelper.init(AApplication.getInstance());
            startActivity.startLogin();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLogin() {
        LoginBusiness.login(new ILoginCallback() { // from class: activity.StartActivity.2
            @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
            public void onLoginSuccess() {
                StartActivity.this.mH.postDelayed(new Runnable() { // from class: activity.StartActivity.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ((DemoApplication) DemoApplication.getInstance()).startPushAndConnect();
                        Intent intent = new Intent(DemoApplication.getInstance(), (Class<?>) MainActivity.class);
                        intent.setFlags(268468224);
                        DemoApplication.getInstance().startActivity(intent);
                    }
                }, 0L);
            }

            @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
            public void onLoginFailed(int i, String str) {
                Toast.makeText(DemoApplication.getInstance(), DemoApplication.getInstance().getString(R.string.login_falied_by) + str, 0).show();
            }
        });
        finish();
    }

    private void killProcess() {
        LinkToast.makeText(this, R.string.region_restart_confirm).show();
        ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: activity.-$$Lambda$StartActivity$ma08K0v0XKp596RAmr1T4wg7wFM
            @Override // java.lang.Runnable
            public final void run() {
                Process.killProcess(Process.myPid());
            }
        }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
    }

    @Override // com.aliyun.iot.aep.sdk.framework.AActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        this.countDownTimer = null;
    }

    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(67108864);
            window.getDecorView().setSystemUiVisibility(1284);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1) {
            this.countDownTimer.start();
        }
    }

    public void extLogcat() {
        try {
            SDKInitHelper.init(DemoApplication.getInstance());
            if (ContextCompat.checkSelfPermission(this, Permission.WRITE_EXTERNAL_STORAGE) != 0) {
                this.countDownTimer.start();
            } else {
                this.countDownTimer.start();
            }
            if (FileCacheUtils.getFolderSize(getCacheDir()) > 50000000) {
                Log.e("文件缓存", "清理前" + FileCacheUtils.getFolderSize(getCacheDir()));
                FileCacheUtils.clearAllCache(this);
                Log.e("文件缓存", "清理后" + FileCacheUtils.getFolderSize(getCacheDir()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [activity.StartActivity$3] */
    public void writeLog() {
        if (FileUtil.hasSDCard()) {
            new Thread() { // from class: activity.StartActivity.3
                /* JADX WARN: Can't wrap try/catch for region: R(14:0|2|(9:106|3|(1:5)|6|(1:10)|11|108|12|(4:115|13|112|14))|(5:110|15|(1:17)(1:117)|105|69)|18|97|19|91|23|(1:28)|29|105|69|(1:(0))) */
                /* JADX WARN: Code restructure failed: missing block: B:21:0x00ad, code lost:
                
                    r0 = move-exception;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:22:0x00ae, code lost:
                
                    r0.printStackTrace();
                 */
                /* JADX WARN: Code restructure failed: missing block: B:25:0x00b5, code lost:
                
                    r0 = move-exception;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:26:0x00b6, code lost:
                
                    r0.printStackTrace();
                 */
                /* JADX WARN: Removed duplicated region for block: B:103:0x0143 A[EXC_TOP_SPLITTER, SYNTHETIC] */
                /* JADX WARN: Removed duplicated region for block: B:119:? A[SYNTHETIC] */
                /* JADX WARN: Removed duplicated region for block: B:82:0x013e  */
                /* JADX WARN: Removed duplicated region for block: B:89:0x0134 A[EXC_TOP_SPLITTER, SYNTHETIC] */
                /* JADX WARN: Removed duplicated region for block: B:99:0x012a A[EXC_TOP_SPLITTER, SYNTHETIC] */
                /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:68:0x0123 -> B:105:0x0126). Please report as a decompilation issue!!! */
                @Override // java.lang.Thread, java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public void run() throws java.lang.Throwable {
                    /*
                        Method dump skipped, instruction units count: 332
                        To view this dump add '--comments-level debug' option
                    */
                    throw new UnsupportedOperationException("Method not decompiled: activity.StartActivity.AnonymousClass3.run():void");
                }
            }.start();
        }
    }

    public void LogSystemInfo() {
        Log.e(TAG, "手机厂商：" + SystemUtil.getDeviceBrand());
        Log.e(TAG, "手机型号：" + SystemUtil.getSystemModel());
        Log.e(TAG, "手机当前系统语言：" + SystemUtil.getSystemLanguage());
        Log.e(TAG, "Android系统版本号：" + SystemUtil.getSystemVersion());
        Log.e(TAG, "手机API版本号：" + SystemUtil.getSdkAPILevel());
    }

    public void checkPermission() {
        View viewInflate = View.inflate(this, R.layout.first_permission_view, null);
        final AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setView(viewInflate).create();
        alertDialogCreate.setCanceledOnTouchOutside(false);
        alertDialogCreate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialogCreate.setCancelable(false);
        alertDialogCreate.show();
        int i = getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams attributes = alertDialogCreate.getWindow().getAttributes();
        attributes.width = (int) (((double) i) * 0.85d);
        alertDialogCreate.getWindow().setAttributes(attributes);
        TextView textView = (TextView) viewInflate.findViewById(R.id.permission_text);
        String string = getResources().getString(R.string.permission_start);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(Html.fromHtml(string));
        spannableStringBuilder.setSpan(new ClickableSpan() { // from class: activity.StartActivity.4
            @Override // android.text.style.ClickableSpan
            public void onClick(View view2) {
                StartActivity startActivity = StartActivity.this;
                startActivity.startActivity(new Intent(startActivity, (Class<?>) PrivacyAgreementActivity.class));
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);
                textPaint.setColor(Color.parseColor("#1AA0FF"));
            }
        }, 18, Html.fromHtml(string).length(), 33);
        spannableStringBuilder.setSpan(new StyleSpan(1), 18, Html.fromHtml(string).length(), 33);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableStringBuilder);
        textView.setHighlightColor(Color.parseColor("#00000000"));
        ((Button) viewInflate.findViewById(R.id.btn_left)).setOnClickListener(new View.OnClickListener() { // from class: activity.StartActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                StartActivity.this.finish();
                alertDialogCreate.dismiss();
            }
        });
        ((Button) viewInflate.findViewById(R.id.btn_right)).setOnClickListener(new View.OnClickListener() { // from class: activity.StartActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
                StartActivity.this.sharedPreferences.edit().putBoolean("First", false).apply();
                SDKInitHelper.init(AApplication.getInstance());
                if (!LoginBusiness.isLogin()) {
                    StartActivity.this.startLogin();
                } else {
                    ((DemoApplication) DemoApplication.getInstance()).startPushAndConnect();
                    Intent intent = new Intent(DemoApplication.getInstance(), (Class<?>) MainActivity.class);
                    intent.setFlags(268468224);
                    DemoApplication.getInstance().startActivity(intent);
                    StartActivity.this.finish();
                    StartActivity.this.overridePendingTransition(0, 0);
                }
                StartActivity.this.extLogcat();
            }
        });
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
    }
}
