package activity;

import adapter.DefaultFragmentTabAdapter;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import bean.DeviceInfoBean;
import bean.DeviceInfoBeans;
import bean.PushBean;
import bean.RefreshPicture;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tools.ThreadTools;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.credential.listener.IoTTokenInvalidListener;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.ILogoutCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.seculink.app.R;
import config.AppConfig;
import dialog.DialogUtil;
import fragment.HomeTabFragment;
import fragment.MessageFragment;
import fragment.MyAccountTabFragment;
import fragment.MyFragmentTabLayout;
import fragment.PictureFragment;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import sdk.DemoApplication;
import tools.ActivityManager;
import tools.FloatWindowHelper;
import tools.GooglePlayCore;
import tools.IChangeTab;
import tools.MediaUtil;
import tools.SharePreferenceManager;
import tools.StatusBarUtil;
import tools.WindowUtils;

/* JADX INFO: loaded from: classes.dex */
public class MainActivity extends CommonActivity implements IChangeTab {
    public static CountDownTimer countDownTimer;
    private MyFragmentTabLayout fragmentTabHost;
    private DefaultFragmentTabAdapter mAdapter;
    private String[] textViewArray;
    private String TAG = HomeTabFragment.class.getSimpleName();
    private Class[] fragmentClass = {HomeTabFragment.class, MessageFragment.class, PictureFragment.class, MyAccountTabFragment.class};
    private Integer[] drawables = {Integer.valueOf(R.drawable.tab_device_btn), Integer.valueOf(R.drawable.tab_message_btn), Integer.valueOf(R.drawable.tab_picture_btn), Integer.valueOf(R.drawable.tab_mine_btn)};
    private Handler mHandler = new Handler();

    interface back {
        void str(String str);
    }

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
    }

    @Override // android.app.Activity
    public void onSaveInstanceState(Bundle bundle, PersistableBundle persistableBundle) {
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("离线推送内容intent", "" + intent.getData());
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        this.textViewArray = new String[]{getResources().getString(R.string.device), getResources().getString(R.string.message), getResources().getString(R.string.picture), getResources().getString(R.string.mime)};
        FloatWindowHelper floatWindowHelper = FloatWindowHelper.getInstance(getApplication());
        if (floatWindowHelper != null) {
            floatWindowHelper.setNeedShowFloatWindowFlag(false);
        }
        super.onCreate(bundle);
        setSwipeBackEnable(false);
        requestNotificationPermission();
        this.fragmentTabHost = (MyFragmentTabLayout) findViewById(R.id.tab_layout);
        setEdgeToEdge(this.fragmentTabHost);
        this.mAdapter = new DefaultFragmentTabAdapter(Arrays.asList(this.fragmentClass), Arrays.asList(this.textViewArray), Arrays.asList(this.drawables)) { // from class: activity.MainActivity.1
            @Override // adapter.DefaultFragmentTabAdapter, fragment.MyFragmentTabLayout.FragmentTabLayoutAdapter
            public View createView(int i) {
                View viewInflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item, (ViewGroup) null);
                ((ImageView) viewInflate.findViewById(R.id.img)).setImageResource(MainActivity.this.drawables[i].intValue());
                TextView textView = (TextView) viewInflate.findViewById(R.id.tab_text);
                textView.setTextColor(MainActivity.this.getResources().getColor(R.color.color_black));
                textView.setText(MainActivity.this.textViewArray[i]);
                textView.setTextSize(10.0f);
                return viewInflate;
            }

            @Override // adapter.DefaultFragmentTabAdapter, fragment.MyFragmentTabLayout.FragmentTabLayoutAdapter
            public void onClick(int i) {
                switch (i) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        StatusBarUtil.setLightStatusBar(MainActivity.this.getActivity(), true);
                        break;
                }
            }
        };
        this.fragmentTabHost.init(getSupportFragmentManager()).setFragmentTabLayoutAdapter(this.mAdapter).creat();
        DemoApplication demoApplication = (DemoApplication) getApplication();
        IoTCredentialManageImpl.getInstance(demoApplication).setIotTokenInvalidListener(new AnonymousClass2(demoApplication));
    }

    /* JADX INFO: renamed from: activity.MainActivity$2, reason: invalid class name */
    class AnonymousClass2 implements IoTTokenInvalidListener {
        final /* synthetic */ DemoApplication val$app;

        AnonymousClass2(DemoApplication demoApplication) {
            this.val$app = demoApplication;
        }

        /* JADX INFO: renamed from: activity.MainActivity$2$1, reason: invalid class name */
        class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                LoginBusiness.logout(new ILogoutCallback() { // from class: activity.MainActivity.2.1.1
                    @Override // com.aliyun.iot.aep.sdk.login.ILogoutCallback
                    public void onLogoutSuccess() {
                        AnonymousClass2.this.val$app.stopPushAndDisconnect();
                        SharePreferenceManager.getInstance().clear();
                        ActivityManager.getInstance().clear();
                        LoginBusiness.login(new ILoginCallback() { // from class: activity.MainActivity.2.1.1.1
                            @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
                            public void onLoginSuccess() {
                                Intent intent = new Intent(DemoApplication.getInstance(), (Class<?>) MainActivity.class);
                                intent.setFlags(268468224);
                                DemoApplication.getInstance().startActivity(intent);
                            }

                            @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
                            public void onLoginFailed(int i, String str) {
                                if (i != 10003) {
                                    Toast.makeText(AnonymousClass2.this.val$app, "登录失败 :" + str, 0).show();
                                }
                            }
                        });
                    }

                    @Override // com.aliyun.iot.aep.sdk.login.ILogoutCallback
                    public void onLogoutFailed(int i, String str) {
                        ALog.e(MainActivity.this.TAG, "logout failed");
                        SharePreferenceManager.getInstance().clear();
                        ActivityManager.getInstance().clear();
                        LoginBusiness.login(new ILoginCallback() { // from class: activity.MainActivity.2.1.1.2
                            @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
                            public void onLoginSuccess() {
                                Intent intent = new Intent(DemoApplication.getInstance(), (Class<?>) MainActivity.class);
                                intent.setFlags(268468224);
                                DemoApplication.getInstance().startActivity(intent);
                            }

                            @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
                            public void onLoginFailed(int i2, String str2) {
                                if (i2 != 10003) {
                                    Toast.makeText(AnonymousClass2.this.val$app, "登录失败 :" + str2, 0).show();
                                }
                            }
                        });
                    }
                });
            }
        }

        @Override // com.aliyun.iot.aep.sdk.credential.listener.IoTTokenInvalidListener
        public void onIoTTokenInvalid() {
            ThreadTools.submitTask(new AnonymousClass1(), true);
        }
    }

    public void requestNotificationPermission() {
        if (SharePreferenceManager.getInstance().getNoticeIsRefuse()) {
            return;
        }
        XXPermissions.with(this).permission(Permission.POST_NOTIFICATIONS).request(new OnPermissionCallback() { // from class: activity.MainActivity.3
            @Override // com.hjq.permissions.OnPermissionCallback
            public void onGranted(List<String> list, boolean z) {
            }

            @Override // com.hjq.permissions.OnPermissionCallback
            public void onDenied(List<String> list, boolean z) {
                SharePreferenceManager.getInstance().setNoticeIsRefuse(true);
            }
        });
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 4370) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                return;
            }
            startActivity(new Intent(getActivity(), (Class<?>) ScanActivity.class));
            return;
        }
        if (i == 4374 && iArr.length > 0 && iArr[0] == 0) {
            EventBus.getDefault().post(new RefreshPicture());
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        FloatWindowHelper floatWindowHelper = FloatWindowHelper.getInstance(getApplication());
        if (floatWindowHelper != null) {
            floatWindowHelper.setNeedShowFloatWindowFlag(false);
        }
        super.onDestroy();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        String stringExtra = getIntent().getStringExtra("body");
        Log.e("离线推送内容body", "" + stringExtra);
        if (stringExtra == null || !stringExtra.contains("ext")) {
            return;
        }
        final PushBean pushBean = (PushBean) JSON.parseObject(stringExtra, PushBean.class);
        if (pushBean.ext.strongReminder.equals("1")) {
            Log.e("离线推送内容body", "1");
            MediaUtil.playRing(this);
            if (Build.VERSION.SDK_INT >= 23) {
                Log.e("离线推送内容body", "2");
                if (!Settings.canDrawOverlays(this)) {
                    Log.e("离线推送内容body", "3");
                    Toast.makeText(this, getString(R.string.open_windows), 1).show();
                    int i = Build.VERSION.SDK_INT;
                    Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
                    if (i < 26) {
                        intent.setData(Uri.parse("package:" + getPackageName()));
                    }
                    startActivityForResult(intent, 1);
                } else {
                    Log.e("离线推送内容body", "4");
                    String allDeviceList = SharePreferenceManager.getInstance().getAllDeviceList();
                    List array = JSON.parseArray(allDeviceList, DeviceInfoBeans.class);
                    Log.e("离线推送内容body", "设备列表" + allDeviceList);
                    final DeviceInfoBean cellDeviceInfoBean = null;
                    for (int i2 = 0; i2 < array.size(); i2++) {
                        if (array.get(i2) != null && ((DeviceInfoBeans) array.get(i2)).getCellDeviceInfoBean() != null && ((DeviceInfoBeans) array.get(i2)).getCellDeviceInfoBean().getIotId() != null && pushBean.ext.iotId.equals(((DeviceInfoBeans) array.get(i2)).getCellDeviceInfoBean().getIotId())) {
                            cellDeviceInfoBean = ((DeviceInfoBeans) array.get(i2)).getCellDeviceInfoBean();
                            Log.e("离线推送内容body", "找到" + cellDeviceInfoBean.getIotId());
                        }
                        if (array.get(i2) != null && ((DeviceInfoBeans) array.get(i2)).getCellDeviceInfoBean() != null && ((DeviceInfoBeans) array.get(i2)).getData() != null && ((DeviceInfoBeans) array.get(i2)).getData().size() != 0) {
                            DeviceInfoBean cellDeviceInfoBean2 = cellDeviceInfoBean;
                            for (int i3 = 0; i3 < ((DeviceInfoBeans) array.get(i2)).getData().size(); i3++) {
                                if (((DeviceInfoBeans) array.get(i2)).getData().get(i3).getIotId().equals(pushBean.ext.iotId)) {
                                    cellDeviceInfoBean2 = ((DeviceInfoBeans) array.get(i2)).getCellDeviceInfoBean();
                                    Log.e("离线推送内容body", "找到2" + cellDeviceInfoBean2.getIotId());
                                }
                            }
                            cellDeviceInfoBean = cellDeviceInfoBean2;
                        }
                    }
                    Log.e("离线推送内容body", "6");
                    this.mHandler.post(new Runnable() { // from class: activity.MainActivity.4
                        @Override // java.lang.Runnable
                        public void run() {
                            Log.e("离线推送内容body", "7");
                            WindowUtils.getInstance().showPopupWindow(MainActivity.this, pushBean.ext.iotId, cellDeviceInfoBean);
                        }
                    });
                }
            }
            if (countDownTimer == null) {
                Log.e("离线推送内容body", "8");
                countDownTimer = new CountDownTimer(30000L, 5000L) { // from class: activity.MainActivity.5
                    @Override // android.os.CountDownTimer
                    public void onTick(long j) {
                    }

                    @Override // android.os.CountDownTimer
                    public void onFinish() {
                        cancel();
                        MainActivity.countDownTimer = null;
                        MediaUtil.stopRing();
                        WindowUtils.getInstance().hidePopupWindow();
                    }
                };
                countDownTimer.start();
            }
        }
    }

    private void Score() {
        if (SharePreferenceManager.getInstance().getIsScore() == 0) {
            if (AppConfig.isChina) {
                DialogUtil.showScoreDiaLog(this, new DialogUtil.OnConfirmClickListener() { // from class: activity.MainActivity.6
                    @Override // dialog.DialogUtil.OnConfirmClickListener
                    public void ConfirmListener() {
                        MainActivity.toMarket(MainActivity.this);
                        SharePreferenceManager.getInstance().setIsScore(1);
                    }
                });
            } else {
                GooglePlayCore.launchGooglePlay(this, new GooglePlayCore.GooglePlayFlowListener() { // from class: activity.MainActivity.7
                    @Override // tools.GooglePlayCore.GooglePlayFlowListener
                    public void OnCompleteListener() {
                        SharePreferenceManager.getInstance().setIsScore(1);
                        Log.e("Google Play Score", "OnCompleteListener");
                    }

                    @Override // tools.GooglePlayCore.GooglePlayFlowListener
                    public void OnErrorListener() {
                        Log.e("Google Play Score", "OnErrorListener");
                    }
                });
            }
        }
    }

    public static boolean judge(Context context, Intent intent) {
        List<ResolveInfo> listQueryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 64);
        return listQueryIntentActivities == null || listQueryIntentActivities.size() <= 0;
    }

    public static Intent getIntent(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("market://details?id=" + context.getPackageName());
        Log.e("fang", "getIntent: " + sb.toString());
        return new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
    }

    public static void toMarket(Activity activity2) {
        Intent intent = getIntent(activity2);
        boolean zJudge = judge(activity2, intent);
        Log.e("fang", "b: " + zJudge);
        if (!zJudge) {
            try {
                activity2.startActivity(intent);
                return;
            } catch (ActivityNotFoundException unused) {
                Log.e("fang", "ActivityNotFoundException: Constants.ERROR_NO_MARKET");
                return;
            }
        }
        Log.e("fang", "ActivityNotFoundException: Constants.ERROR_NO_MARKET");
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            Log.d(this.TAG, "onActivityResult");
            if (intent == null || intent.getStringExtra("productKey") == null) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("productKey", intent.getStringExtra("productKey"));
            bundle.putString("deviceName", intent.getStringExtra("deviceName"));
            bundle.putString("token", intent.getStringExtra("token"));
            Intent intent2 = new Intent(this, (Class<?>) BindAndUseActivity.class);
            intent2.putExtras(bundle);
            startActivity(intent2);
        }
    }

    @Override // tools.IChangeTab
    public void changeTab(int i) {
        this.fragmentTabHost.getFragmentTabHost().setCurrentTab(i);
    }

    public void getSharePersonsList(String str, final back backVar) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put(AlinkConstants.KEY_PAGE_NO, 1);
        map.put(AlinkConstants.KEY_PAGE_SIZE, 100);
        map.put("owned", 1);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/uc/listBindingByDev").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.MainActivity.8
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.d(MainActivity.this.TAG, "onFailure");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() == 200) {
                    try {
                        backVar.str(((JSONObject) ioTResponse.getData()).get("data").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
