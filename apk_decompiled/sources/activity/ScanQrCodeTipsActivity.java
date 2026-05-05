package activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import bean.CameraNameModify;
import bean.RefreshDevices;
import bean.setFinish;
import bean.updateTimeModify;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.api.add.AddDeviceBiz;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.IAddDeviceListener;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.api.config.ProvisionConfigCenter;
import com.aliyun.alink.business.devicecenter.api.config.ProvisionConfigParams;
import com.aliyun.alink.business.devicecenter.api.discovery.GetTokenParams;
import com.aliyun.alink.business.devicecenter.api.discovery.GetTokenResult;
import com.aliyun.alink.business.devicecenter.api.discovery.IOnTokenGetListerner;
import com.aliyun.alink.business.devicecenter.api.discovery.LocalDeviceMgr;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.czp.library.ArcProgress;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.seculink.app.R;
import config.AppConfig;
import dialog.ScanProgressDialog;
import fragment.HomeTabFragment;
import java.lang.Thread;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import org.greenrobot.eventbus.EventBus;
import tools.AudioPlayManager;
import tools.LogEx;
import tools.MyCallback;
import tools.ScreenUtil;
import tools.SettingsCtrl;
import tools.StatusBarUtil;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class ScanQrCodeTipsActivity extends CommonActivity implements View.OnClickListener {
    ArcProgress arcProgress;
    private EditText camera_name;
    Button cancelBtn;
    RelativeLayout connect_state;
    private SimpleDateFormat dateFormat;
    private TextView fail_tip;
    RelativeLayout fl_progress;
    TitleView fl_titlebar;
    private ImageView image_cloud;
    private ImageView image_result;
    DeviceInfo info;
    ScanProgressDialog.OnViewClick l;
    RelativeLayout layout_main;
    private LinearLayout ll1_result;
    private LinearLayout ll2_result;
    private LinearLayout ll3_result;
    private ScanProgressDialog.onProgressChangeListener onProgressChangeListener;
    int progress;
    private RelativeLayout progress_ll;
    TextView scan_layout_tips;
    TextView status_text;
    LinearLayout step_image;
    private String textTip;
    private TextView text_result;
    Thread thread;
    TextView tv_close;
    TextView tv_found_devices;
    TextView tv_num;
    Activity context = getActivity();
    int total = 100;
    Handler handler = new Handler();
    Paint progressPaint = new Paint(1);
    Paint textPaint = new Paint(1);
    private String productKey = "";
    private String deviceName = "";
    private String iotId = "";
    private boolean isBind = false;
    public int count = 0;
    public int bindCount = 0;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.scan_layout;
    }

    @Override // activity.CommonActivity
    protected void initWindows() {
    }

    public String getTextTip() {
        return this.textTip;
    }

    public void setTextTip(String str) {
        this.textTip = str;
    }

    @Override // activity.CommonActivity
    protected void beforeInitWidget() {
        Log.d(this.TAG, "beforeInitWidget: " + getIntent().getStringExtra("type"));
        if ("scan".equals(getIntent().getStringExtra("type"))) {
            Log.d(this.TAG, "beforeInitWidget: scanToken------------------");
            QrCodeforWIFI(getIntent().getStringExtra("wifiSSid"), getIntent().getStringExtra("wifiPwd"), "ALI_DEFAULT");
        } else {
            Log.d(this.TAG, "beforeInitWidget: addToken--------------------");
            findViewById(R.id.scan_layout_layout).setVisibility(8);
            QrCodeforWIFI(getIntent().getStringExtra("wifiSSid"), getIntent().getStringExtra("wifiPwd"), "ALI_QR");
        }
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        super.onCreate(bundle);
        StatusBarUtil.setTranslucentStatus(getActivity());
        StatusBarUtil.setLightStatusBar(getActivity(), true);
        this.layout_main = (RelativeLayout) findViewById(R.id.scan_layout_layout);
        setEdgeToEdge(this.layout_main);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.ScanQrCodeTipsActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                ScanQrCodeTipsActivity.this.finish();
            }
        });
        getWindow().setDimAmount(0.3f);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_white_rect);
        this.tv_num = (TextView) findViewById(R.id.tv_num);
        this.cancelBtn = (Button) findViewById(R.id.cancelBtn);
        this.cancelBtn.setOnClickListener(this);
        this.arcProgress = (ArcProgress) findViewById(R.id.myProgress);
        this.tv_close = (TextView) findViewById(R.id.tv_close);
        this.tv_found_devices = (TextView) findViewById(R.id.tv_found_devices);
        this.connect_state = (RelativeLayout) findViewById(R.id.connect_state);
        this.text_result = (TextView) findViewById(R.id.text_result);
        this.ll1_result = (LinearLayout) findViewById(R.id.ll1_result);
        this.ll2_result = (LinearLayout) findViewById(R.id.ll2_result);
        this.ll3_result = (LinearLayout) findViewById(R.id.ll3_result);
        this.camera_name = (EditText) findViewById(R.id.camera_name);
        this.camera_name.setHint(getResources().getText(R.string.my_camera));
        this.status_text = (TextView) findViewById(R.id.status_text);
        this.fl_progress = (RelativeLayout) findViewById(R.id.fl_progress);
        this.fail_tip = (TextView) findViewById(R.id.fail_tip);
        this.scan_layout_tips = (TextView) findViewById(R.id.scan_layout_tips);
        this.step_image = (LinearLayout) findViewById(R.id.step_image);
        this.progress_ll = (RelativeLayout) findViewById(R.id.progress_ll);
        this.image_result = (ImageView) findViewById(R.id.image_result);
        this.image_cloud = (ImageView) findViewById(R.id.image_cloud);
        this.image_cloud.setImageResource(AppConfig.isChina ? R.drawable.cloud_buy_image_cn : R.drawable.cloud_buy_image_en);
        this.image_cloud.setOnClickListener(this);
        this.image_cloud.setVisibility(8);
        this.handler.postDelayed(new Runnable() { // from class: activity.ScanQrCodeTipsActivity.2
            @Override // java.lang.Runnable
            public void run() {
            }
        }, 10000L);
        this.tv_close.setOnClickListener(this);
        this.progressPaint.setStrokeWidth(35.0f);
        this.progressPaint.setTextSize(ScreenUtil.dp2Px(this.context, 30.0f));
        this.progressPaint.setColor(this.context.getResources().getColor(R.color.color_black));
        this.textPaint.setStrokeWidth(15.0f);
        this.textPaint.setTextSize(ScreenUtil.dp2Px(this.context, 18.0f));
        this.textPaint.setColor(this.context.getResources().getColor(R.color.color_black));
        this.arcProgress.setOnCenterDraw(new ArcProgress.OnCenterDraw() { // from class: activity.ScanQrCodeTipsActivity.3
            @Override // com.czp.library.ArcProgress.OnCenterDraw
            public void draw(Canvas canvas, RectF rectF, float f, float f2, float f3, int i) {
                String str = TextUtils.isEmpty(ScanQrCodeTipsActivity.this.textTip) ? "0" : ScanQrCodeTipsActivity.this.textTip;
                ScanQrCodeTipsActivity.this.tv_num.setText(i + "");
                ScanQrCodeTipsActivity.this.tv_found_devices.setText(ScanQrCodeTipsActivity.this.context.getResources().getString(R.string.found_devices, str));
            }
        });
        this.camera_name.addTextChangedListener(new TextWatcher() { // from class: activity.ScanQrCodeTipsActivity.4
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });
        startProrgress();
    }

    @Override // android.app.Activity
    protected void onRestart() {
        super.onRestart();
        setAddDevice(getIntent().getStringExtra(AlinkConstants.KEY_PK), getIntent().getStringExtra(AlinkConstants.KEY_DN));
    }

    public void startProrgress() {
        Thread thread = this.thread;
        if (thread == null || thread.getState() == Thread.State.TERMINATED) {
            this.progress = -1;
            this.thread = new Thread(new ProgressThread());
            this.thread.start();
        }
    }

    public void stopProgress() {
        this.thread.interrupt();
        this.handler.removeCallbacksAndMessages(null);
    }

    class ProgressThread implements Runnable {
        private long lastTime;

        ProgressThread() {
        }

        @Override // java.lang.Runnable
        public void run() {
            this.lastTime = System.currentTimeMillis();
            ScanQrCodeTipsActivity.this.handler.post(new Runnable() { // from class: activity.ScanQrCodeTipsActivity.ProgressThread.1
                @Override // java.lang.Runnable
                public void run() {
                    if (ScanQrCodeTipsActivity.this.context == null || ScanQrCodeTipsActivity.this.context.isFinishing() || ScanQrCodeTipsActivity.this.progress >= 100) {
                        return;
                    }
                    ScanQrCodeTipsActivity.this.progress++;
                    ScanQrCodeTipsActivity.this.arcProgress.setProgress(ScanQrCodeTipsActivity.this.progress);
                    if (ScanQrCodeTipsActivity.this.onProgressChangeListener != null) {
                        ScanQrCodeTipsActivity.this.onProgressChangeListener.onProgressChange(ScanQrCodeTipsActivity.this.progress);
                    }
                    ScanQrCodeTipsActivity.this.handler.postDelayed(this, 1300L);
                }
            });
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.cancelBtn) {
            if (this.cancelBtn.getText().equals(getString(R.string.finish))) {
                EventBus.getDefault().post(new setFinish("activity.scanNavigationActivity"));
                if (!this.camera_name.getText().toString().equals("")) {
                    modifyDevName(this.iotId, this.camera_name.getText().toString());
                }
            } else if (this.cancelBtn.getText().equals(getString(R.string.reconnect))) {
                startActivity(new Intent(this, (Class<?>) scanNavigationActivity.class));
            }
            HomeTabFragment.getRefresh();
            finish();
            return;
        }
        if (view2.getId() == R.id.image_cloud) {
            Intent intent = new Intent(getActivity(), (Class<?>) PayYunServiceActivity2.class);
            intent.putExtra("iotId", this.iotId);
            intent.putExtra(AlinkConstants.KEY_DN, this.deviceName);
            intent.putExtra(AlinkConstants.KEY_PK, this.productKey);
            startActivity(intent);
        }
    }

    public void QrCodeforWIFI(final String str, final String str2, String str3) {
        ProvisionConfigParams provisionConfigParams = new ProvisionConfigParams();
        provisionConfigParams.enableGlobalCloudToken = true;
        ProvisionConfigCenter.getInstance().setProvisionConfiguration(provisionConfigParams);
        this.info = new DeviceInfo();
        this.info.productKey = getIntent().getStringExtra(AlinkConstants.KEY_PK);
        if (str3.equals("ALI_QR")) {
            this.info.linkType = LinkType.ALI_QR.getName();
        } else {
            this.info.linkType = LinkType.ALI_DEFAULT.getName();
        }
        AddDeviceBiz.getInstance().setDevice(this.info);
        AddDeviceBiz.getInstance().startAddDevice(this.context, new IAddDeviceListener() { // from class: activity.ScanQrCodeTipsActivity.5
            @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceListener
            public void onProvisioning() {
            }

            @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceListener
            public void onPreCheck(boolean z, DCErrorCode dCErrorCode) {
                Log.d(ScanQrCodeTipsActivity.this.TAG, "onPreCheck: " + z);
            }

            @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceListener
            public void onProvisionPrepare(int i) {
                AddDeviceBiz.getInstance().toggleProvision(str, str2, 120);
            }

            @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceListener
            public void onProvisionStatus(ProvisionStatus provisionStatus) {
                if (provisionStatus == ProvisionStatus.QR_PROVISION_READY) {
                    ScanQrCodeTipsActivity.this.createQrCode(ProvisionStatus.QR_PROVISION_READY.message());
                }
            }

            @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceListener
            public void onProvisionedResult(boolean z, DeviceInfo deviceInfo, DCErrorCode dCErrorCode) {
                Log.d(ScanQrCodeTipsActivity.this.TAG, "onProvisionedResult:--------------- " + z);
                AddDeviceBiz.getInstance().stopAddDevice();
                if (!z) {
                    ScanQrCodeTipsActivity.this.getActivity().runOnUiThread(new Runnable() { // from class: activity.ScanQrCodeTipsActivity.5.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ScanQrCodeTipsActivity.this.findViewById(R.id.scan_layout_layout).setVisibility(0);
                            ScanQrCodeTipsActivity.this.step_image.setVisibility(8);
                            ScanQrCodeTipsActivity.this.status_text.setVisibility(8);
                            ScanQrCodeTipsActivity.this.progress_ll.setVisibility(8);
                            ScanQrCodeTipsActivity.this.fl_titlebar.setTitleText(ScanQrCodeTipsActivity.this.getString(R.string.result));
                            ScanQrCodeTipsActivity.this.connect_state.setVisibility(0);
                            ScanQrCodeTipsActivity.this.ll1_result.setVisibility(0);
                            ScanQrCodeTipsActivity.this.ll2_result.setVisibility(8);
                            ScanQrCodeTipsActivity.this.ll3_result.setVisibility(8);
                            ScanQrCodeTipsActivity.this.fl_progress.setVisibility(8);
                            ScanQrCodeTipsActivity.this.scan_layout_tips.setVisibility(8);
                            ScanQrCodeTipsActivity.this.image_result.setImageDrawable(ScanQrCodeTipsActivity.this.getResources().getDrawable(R.drawable.connect_fail_image));
                            String string = ScanQrCodeTipsActivity.this.getResources().getString(R.string.fail_tip_text2);
                            ScanQrCodeTipsActivity.this.text_result.setText(R.string.connection_failed);
                            ScanQrCodeTipsActivity.this.text_result.setTextColor(ScanQrCodeTipsActivity.this.getResources().getColor(R.color.color_ff0000));
                            ScanQrCodeTipsActivity.this.cancelBtn.setText(R.string.reconnect);
                            ScanQrCodeTipsActivity.this.cancelBtn.setBackgroundResource(R.drawable.bg_button_red_radius_20dp);
                            ScanQrCodeTipsActivity.this.fail_tip.setText(Html.fromHtml(string));
                            ScanQrCodeTipsActivity.this.fail_tip.setGravity(3);
                        }
                    });
                    return;
                }
                Log.d(ScanQrCodeTipsActivity.this.TAG, "onProvisionedResult: ----------------bindToken");
                GetTokenParams getTokenParams = new GetTokenParams();
                getTokenParams.deviceName = deviceInfo.deviceName;
                getTokenParams.productKey = deviceInfo.productKey;
                LocalDeviceMgr.getInstance().getDeviceToken(ScanQrCodeTipsActivity.this.context, getTokenParams, new IOnTokenGetListerner() { // from class: activity.ScanQrCodeTipsActivity.5.2
                    @Override // com.aliyun.alink.business.devicecenter.api.discovery.IOnTokenGetListerner
                    public void onSuccess(GetTokenResult getTokenResult) {
                        Log.d(ScanQrCodeTipsActivity.this.TAG, "onSuccess: ");
                        ScanQrCodeTipsActivity.this.bindToken(getTokenResult.productKey, getTokenResult.deviceName, getTokenResult.token);
                    }

                    @Override // com.aliyun.alink.business.devicecenter.api.discovery.IOnTokenGetListerner
                    public void onFail(DCErrorCode dCErrorCode2) {
                        Log.d(ScanQrCodeTipsActivity.this.TAG, "onFail:配网失败 ");
                        Log.d(ScanQrCodeTipsActivity.this.TAG, "onFail:可能开启AP隔离 ");
                    }
                });
            }
        });
    }

    private void setAddDevice(String str, String str2) {
        this.bindCount++;
        if (this.bindCount < 4 && !this.isBind) {
            this.isBind = true;
            HashMap map = new HashMap();
            map.put("productKey", str);
            map.put("deviceName", str2);
            new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/awss/time/window/user/bind").setScheme(Scheme.HTTPS).setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.ScanQrCodeTipsActivity.6
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    LogEx.d(true, ScanQrCodeTipsActivity.this.TAG, "onFailure");
                    if (ScanQrCodeTipsActivity.this.connect_state.getVisibility() == 8) {
                        ScanQrCodeTipsActivity.this.isBind = false;
                    }
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    int code = ioTResponse.getCode();
                    ScanQrCodeTipsActivity.this.isBind = false;
                    Log.e(ScanQrCodeTipsActivity.this.TAG, "onResponse: code: " + code);
                    ioTResponse.getLocalizedMsg();
                    ioTResponse.getMessage();
                    if (code != 200) {
                        return;
                    }
                    String str3 = (String) ioTResponse.getData();
                    EventBus.getDefault().post(new updateTimeModify(str3));
                    EventBus.getDefault().post(new RefreshDevices());
                    SettingsCtrl.getInstance().getProperties(str3, new MyCallback() { // from class: activity.ScanQrCodeTipsActivity.6.1
                        @Override // tools.MyCallback
                        public void onComplete(boolean z) {
                        }
                    });
                    ScanQrCodeTipsActivity.this.status_text.setVisibility(8);
                    ScanQrCodeTipsActivity.this.progress_ll.setVisibility(8);
                    AudioPlayManager.newBuilder().load(ScanQrCodeTipsActivity.this.context, Integer.valueOf(R.raw.bind), AudioPlayManager.MediaPlayerBuilder.TYPE.RAW).build().play();
                    Log.d(ScanQrCodeTipsActivity.this.TAG, "bindToken: bindToken success--------------");
                    ScanQrCodeTipsActivity.this.fl_progress.setVisibility(8);
                    ScanQrCodeTipsActivity.this.scan_layout_tips.setVisibility(8);
                    ScanQrCodeTipsActivity.this.fl_titlebar.setTitleText(ScanQrCodeTipsActivity.this.getString(R.string.result));
                    ScanQrCodeTipsActivity.this.connect_state.setVisibility(0);
                    ScanQrCodeTipsActivity.this.ll1_result.setVisibility(0);
                    ViewGroup.LayoutParams layoutParams = ScanQrCodeTipsActivity.this.ll1_result.getLayoutParams();
                    layoutParams.height = -2;
                    ScanQrCodeTipsActivity.this.ll1_result.setLayoutParams(layoutParams);
                    ScanQrCodeTipsActivity.this.ll2_result.setVisibility(0);
                    ScanQrCodeTipsActivity.this.ll3_result.setVisibility(0);
                    ScanQrCodeTipsActivity.this.fail_tip.setVisibility(8);
                    ScanQrCodeTipsActivity.this.image_result.setImageDrawable(ScanQrCodeTipsActivity.this.getResources().getDrawable(R.drawable.connect_success_image));
                    ScanQrCodeTipsActivity.this.text_result.setText(R.string.connection_succeeded);
                    ScanQrCodeTipsActivity.this.text_result.setTextColor(ScanQrCodeTipsActivity.this.getResources().getColor(R.color.colorAccent));
                    ScanQrCodeTipsActivity.this.step_image.setVisibility(8);
                    ScanQrCodeTipsActivity.this.cancelBtn.setEnabled(true);
                    ScanQrCodeTipsActivity.this.cancelBtn.setBackgroundResource(R.drawable.bg_button_blue_radius_20dp);
                    ScanQrCodeTipsActivity.this.cancelBtn.setText(R.string.finish);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createQrCode(String str) {
        Intent intent = new Intent(this, (Class<?>) WifiScanActivity.class);
        intent.putExtra("json", str);
        intent.putExtra("wifiSSid", getIntent().getStringExtra("wifiSSid"));
        intent.putExtra("wifiPwd", getIntent().getStringExtra("wifiPwd"));
        intent.putExtra("type", "type");
        intent.putExtra(AlinkConstants.KEY_PK, getIntent().getStringExtra(AlinkConstants.KEY_PK));
        intent.putExtra(AlinkConstants.KEY_DN, getIntent().getStringExtra(AlinkConstants.KEY_DN));
        intent.putExtra(AlinkConstants.KEY_WIFI_TYPE, getIntent().getIntExtra(AlinkConstants.KEY_WIFI_TYPE, 0));
        startActivity(intent);
        new Thread(new Runnable() { // from class: activity.ScanQrCodeTipsActivity.7
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Thread.sleep(500L);
                    ScanQrCodeTipsActivity.this.runOnUiThread(new Runnable() { // from class: activity.ScanQrCodeTipsActivity.7.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ScanQrCodeTipsActivity.this.findViewById(R.id.scan_layout_layout).setVisibility(0);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindToken(String str, String str2, String str3) {
        HashMap map = new HashMap();
        this.productKey = str;
        this.deviceName = str2;
        map.put("productKey", str);
        map.put("deviceName", str2);
        map.put("token", str3);
        Log.d(this.TAG, "bindToken: Begin bindToken-------------------");
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/awss/token/user/bind").setScheme(Scheme.HTTPS).setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.ScanQrCodeTipsActivity.8
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ScanQrCodeTipsActivity.this.getActivity().runOnUiThread(new Runnable() { // from class: activity.ScanQrCodeTipsActivity.8.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ScanQrCodeTipsActivity.this.step_image.setVisibility(8);
                        ScanQrCodeTipsActivity.this.status_text.setVisibility(8);
                        ScanQrCodeTipsActivity.this.progress_ll.setVisibility(8);
                        ScanQrCodeTipsActivity.this.fl_titlebar.setTitleText(ScanQrCodeTipsActivity.this.getString(R.string.result));
                        ScanQrCodeTipsActivity.this.connect_state.setVisibility(0);
                        ScanQrCodeTipsActivity.this.fl_progress.setVisibility(8);
                        ScanQrCodeTipsActivity.this.ll1_result.setVisibility(0);
                        ScanQrCodeTipsActivity.this.ll3_result.setVisibility(8);
                        ScanQrCodeTipsActivity.this.ll2_result.setVisibility(8);
                        ScanQrCodeTipsActivity.this.scan_layout_tips.setVisibility(8);
                        ScanQrCodeTipsActivity.this.fail_tip.setText(Html.fromHtml(ScanQrCodeTipsActivity.this.getResources().getString(R.string.fail_tip_text2)));
                        ScanQrCodeTipsActivity.this.fail_tip.setGravity(3);
                        ScanQrCodeTipsActivity.this.image_result.setImageDrawable(ScanQrCodeTipsActivity.this.getResources().getDrawable(R.drawable.connect_fail_image));
                        ScanQrCodeTipsActivity.this.text_result.setText(R.string.connection_failed);
                        ScanQrCodeTipsActivity.this.text_result.setTextColor(ScanQrCodeTipsActivity.this.getResources().getColor(R.color.color_ff0000));
                        ScanQrCodeTipsActivity.this.cancelBtn.setText(R.string.reconnect);
                        ScanQrCodeTipsActivity.this.cancelBtn.setBackgroundResource(R.drawable.bg_button_red_radius_20dp);
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                final int code = ioTResponse.getCode();
                if (200 == code) {
                    try {
                        ScanQrCodeTipsActivity.this.iotId = ioTResponse.getData().toString();
                        EventBus.getDefault().post(new updateTimeModify(ScanQrCodeTipsActivity.this.iotId));
                        EventBus.getDefault().post(new updateTimeModify(ScanQrCodeTipsActivity.this.iotId));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ScanQrCodeTipsActivity.this.getActivity().runOnUiThread(new Runnable() { // from class: activity.ScanQrCodeTipsActivity.8.3
                        @Override // java.lang.Runnable
                        public void run() {
                            ScanQrCodeTipsActivity.this.status_text.setVisibility(8);
                            ScanQrCodeTipsActivity.this.progress_ll.setVisibility(8);
                            AudioPlayManager.newBuilder().load(ScanQrCodeTipsActivity.this.context, Integer.valueOf(R.raw.bind), AudioPlayManager.MediaPlayerBuilder.TYPE.RAW).build().play();
                            Log.d(ScanQrCodeTipsActivity.this.TAG, "bindToken: bindToken success--------------");
                            ScanQrCodeTipsActivity.this.fl_progress.setVisibility(8);
                            ScanQrCodeTipsActivity.this.scan_layout_tips.setVisibility(8);
                            ScanQrCodeTipsActivity.this.fl_titlebar.setTitleText(ScanQrCodeTipsActivity.this.getString(R.string.result));
                            ScanQrCodeTipsActivity.this.connect_state.setVisibility(0);
                            ScanQrCodeTipsActivity.this.ll1_result.setVisibility(0);
                            ViewGroup.LayoutParams layoutParams = ScanQrCodeTipsActivity.this.ll1_result.getLayoutParams();
                            layoutParams.height = -2;
                            ScanQrCodeTipsActivity.this.ll1_result.setLayoutParams(layoutParams);
                            ScanQrCodeTipsActivity.this.ll2_result.setVisibility(0);
                            ScanQrCodeTipsActivity.this.ll3_result.setVisibility(0);
                            ScanQrCodeTipsActivity.this.fail_tip.setVisibility(8);
                            ScanQrCodeTipsActivity.this.image_result.setImageDrawable(ScanQrCodeTipsActivity.this.getResources().getDrawable(R.drawable.connect_success_image));
                            ScanQrCodeTipsActivity.this.text_result.setText(R.string.connection_succeeded);
                            ScanQrCodeTipsActivity.this.text_result.setTextColor(ScanQrCodeTipsActivity.this.getResources().getColor(R.color.colorAccent));
                            ScanQrCodeTipsActivity.this.step_image.setVisibility(8);
                            ScanQrCodeTipsActivity.this.cancelBtn.setEnabled(true);
                            ScanQrCodeTipsActivity.this.cancelBtn.setBackgroundResource(R.drawable.bg_button_blue_radius_20dp);
                            ScanQrCodeTipsActivity.this.cancelBtn.setText(R.string.finish);
                        }
                    });
                } else {
                    ioTResponse.getMessage();
                    ioTResponse.getLocalizedMsg();
                    ScanQrCodeTipsActivity.this.getActivity().runOnUiThread(new Runnable() { // from class: activity.ScanQrCodeTipsActivity.8.2
                        @Override // java.lang.Runnable
                        public void run() {
                            ScanQrCodeTipsActivity.this.status_text.setVisibility(8);
                            ScanQrCodeTipsActivity.this.progress_ll.setVisibility(8);
                            ScanQrCodeTipsActivity.this.fl_progress.setVisibility(8);
                            ScanQrCodeTipsActivity.this.scan_layout_tips.setVisibility(8);
                            ScanQrCodeTipsActivity.this.connect_state.setVisibility(0);
                            ScanQrCodeTipsActivity.this.image_result.setImageDrawable(ScanQrCodeTipsActivity.this.getResources().getDrawable(R.drawable.connect_fail_image));
                            ScanQrCodeTipsActivity.this.text_result.setTextColor(ScanQrCodeTipsActivity.this.getResources().getColor(R.color.color_ff0000));
                            ScanQrCodeTipsActivity.this.ll1_result.setVisibility(0);
                            ScanQrCodeTipsActivity.this.ll2_result.setVisibility(8);
                            ScanQrCodeTipsActivity.this.ll3_result.setVisibility(8);
                            ScanQrCodeTipsActivity.this.step_image.setVisibility(8);
                            ScanQrCodeTipsActivity.this.fl_titlebar.setTitleText(ScanQrCodeTipsActivity.this.getString(R.string.result));
                            if (code == 2064) {
                                ScanQrCodeTipsActivity.this.fail_tip.setText(ScanQrCodeTipsActivity.this.getResources().getString(R.string.fail_tip_text1));
                                ScanQrCodeTipsActivity.this.fail_tip.setGravity(3);
                                ScanQrCodeTipsActivity.this.text_result.setText(R.string.device_binded);
                                ScanQrCodeTipsActivity.this.cancelBtn.setText(ScanQrCodeTipsActivity.this.getString(R.string.finish));
                                ScanQrCodeTipsActivity.this.cancelBtn.setBackgroundResource(R.drawable.bg_button_red_radius_20dp);
                                return;
                            }
                            ScanQrCodeTipsActivity.this.fail_tip.setText(Html.fromHtml(ScanQrCodeTipsActivity.this.getResources().getString(R.string.fail_tip_text2)));
                            ScanQrCodeTipsActivity.this.fail_tip.setGravity(3);
                            ScanQrCodeTipsActivity.this.text_result.setText(R.string.connection_failed);
                            ScanQrCodeTipsActivity.this.cancelBtn.setText(R.string.reconnect);
                            ScanQrCodeTipsActivity.this.cancelBtn.setBackgroundResource(R.drawable.bg_button_red_radius_20dp);
                        }
                    });
                }
                JSONObject object = JSON.parseObject((String) ioTResponse.getData());
                object.getString("code");
                JSONArray jSONArray = object.getJSONObject("data").getJSONArray("data");
                if (jSONArray != null) {
                    for (int i = 0; i < jSONArray.size(); i++) {
                        jSONArray.getJSONObject(i);
                    }
                }
            }
        });
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        AddDeviceBiz.getInstance().stopAddDevice();
        super.onDestroy();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        AudioPlayManager.with().release();
    }

    private void modifyDevName(final String str, final String str2) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("nickName", str2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/uc/setDeviceNickName").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.ScanQrCodeTipsActivity.9
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, ScanQrCodeTipsActivity.this.TAG, "onFailure");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                LogEx.d(true, ScanQrCodeTipsActivity.this.TAG, "modifyDevName onResponse: code: " + code);
                final String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    ScanQrCodeTipsActivity.this.runOnUiThread(new Runnable() { // from class: activity.ScanQrCodeTipsActivity.9.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(ScanQrCodeTipsActivity.this, localizedMsg, 0).show();
                        }
                    });
                } else {
                    ScanQrCodeTipsActivity.this.runOnUiThread(new Runnable() { // from class: activity.ScanQrCodeTipsActivity.9.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(ScanQrCodeTipsActivity.this, R.string.mofify_succeed, 0).show();
                        }
                    });
                    ScanQrCodeTipsActivity.this.handler.postDelayed(new Runnable() { // from class: activity.ScanQrCodeTipsActivity.9.3
                        @Override // java.lang.Runnable
                        public void run() {
                            EventBus.getDefault().post(new CameraNameModify(str2, str));
                        }
                    }, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
                }
            }
        });
    }
}
