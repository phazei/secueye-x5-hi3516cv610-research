package activity;

import adapter.FaceAdapter;
import adapter.IpcWiFiAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import anet.channel.strategy.dispatch.DispatchConstants;
import bean.AreaCodeModel;
import bean.AreaPointBean;
import bean.CameraRemove;
import bean.CameraSnapUpdate;
import bean.CloudStatusBean;
import bean.DeviceInfoBean;
import bean.FreeCloudStorage;
import bean.PresetBean;
import bean.RefreshPicture;
import bean.TopicBean;
import bean.WifiBean;
import bean.setProperties;
import bean.yunCloudListBean;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.api.share.DeviceShareManager;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.aliyun.iotx.linkvisual.media.audio.AudioParams;
import com.aliyun.iotx.linkvisual.media.audio.LiveIntercomException;
import com.aliyun.iotx.linkvisual.media.audio.LiveIntercomV2;
import com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener;
import com.aliyun.iotx.linkvisual.media.video.PlayerException;
import com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener;
import com.aliyun.iotx.linkvisual.media.video.player.LivePlayer;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.hjq.permissions.Permission;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityIpcameraLayoutBinding;
import com.smarx.notchlib.NotchScreenManager;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import config.AppConfig;
import config.Constants;
import dialog.BaseDialog;
import dialog.DialogUtil;
import dialog.InputDialogViewIpc;
import dialog.ShareDialog;
import enums.ActionTypeEnum;
import enums.SpeedEnum;
import fragment.ControllerFragment;
import fragment.LensControllerFragment;
import fragment.MoreFragment;
import fragment.OldPresetFragment;
import fragment.PresetFragment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.android.agoo.common.AgooConstants;
import org.android.agoo.message.MessageService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import sdk.IPCManager;
import tools.DensityUtil;
import tools.FileUtil;
import tools.LogEx;
import tools.MapUtils;
import tools.MyCallback;
import tools.OnMultiClickListener;
import tools.ScreenUtil;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import tools.SpUtil;
import tools.SystemUtil;
import tools.TimeUtil;
import tools.Utils;
import view.Badge;
import view.BadgeView;
import view.DialogView;
import view.FourPicturesView;
import view.IPCTitleView;
import view.JoystickTouchViewListener;
import view.SelectorDialogFragment;
import view.ShadowButton;
import view.TouchView;
import view.WhiteProgressDialog;
import view.ZoomableTextureView;

/* JADX INFO: loaded from: classes.dex */
public class IPCameraActivity extends CommonActivity implements ControllerFragment.Mylistener, FourPicturesView.ShowFloat, LensControllerFragment.listener, MoreFragment.MyBackListener, MoreFragment.FragmentContextChangeListener, PresetFragment.PresetBackListener, PresetFragment.PresetDataChange, OldPresetFragment.OldPresetBackListener {
    private static String[] PERMISSIONS_STORAGE = {Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String DeviceName;
    private String IccId;
    private String ProductKey;
    private int WifiConfigIsExist;
    boolean ZoomIsMax;
    int ZoomMax;
    String address;
    Badge badge;
    private Timer batteryTimer;
    private DeviceInfoBean beanInfo;
    private ActivityIpcameraLayoutBinding binding;
    ControllerFragment controllerFragment;
    private CountDownTimer countDownTimer;
    private int currentInfrarred;
    private DeviceInfoBean device;

    /* JADX INFO: renamed from: dialog, reason: collision with root package name */
    private AlertDialog f1578dialog;
    TextView door_text;
    private int faceDetectionAbility;
    float height;
    private Timer inactivityTimer;
    private String[] infrarredMode;
    private InputDialogViewIpc inputDialogView;
    private String[] iotIdList;
    private boolean isDetecting;
    private boolean isFirstShowStreamType;
    private boolean isMixZoom;
    private boolean isOpticalZoom;
    private boolean isOtherCard;
    private boolean isOwner;
    private int isSpeakerOpen;
    private int isSupport4G;
    private ActionTypeEnum lastActionTypeEnum;
    private long lastCtrlTime;
    String lat;
    LensControllerFragment lensControllerFragment;
    private boolean lightVisible;
    private LiveIntercomV2 liveIntercom;
    String lon;
    private IpcWiFiAdapter mAdapter;
    private SelectorDialogFragment mapFragment;
    private MoreFragment moreFragment;
    private boolean netVisible;
    private SelectorDialogFragment nightModeFragment;
    private OldPresetFragment oldPresetFragment;
    private Timer onTouchTimer;
    private LivePlayer player;
    private PresetFragment presetFragment;
    private List<Integer> presetList;
    private Timer ptzTimer;
    private String selectSsid;
    private ShareDialog shareDialog2;
    private boolean shopVisible;
    private boolean smartDoorVisible;
    private float startX;
    private float startY;
    private int supportMotionDetect;
    private SelectorDialogFragment switch4gFragment;
    private String switchText;
    private String[] switch_4gArr;
    private Timer timer;
    TextView tips;
    private TouchView touchView;
    private Handler uiHandler;
    ScheduledFuture<?> updatePlayInfoHandle;
    private String wakeUpData;
    private WhiteProgressDialog whiteProgressDialog;
    private List<WifiBean> wifiBeanList;
    private String title = "";
    private String iotId = "";
    private String appKey = "";
    private int lowPowerMode = -1;
    private boolean speakerSwitch = false;
    private int defaultDefinition = 1;
    private int rotationOrientate = 0;
    private boolean isRecording = false;
    private int is1100ErrorPre = 10;
    MutableLiveData<Float> zoom = new MutableLiveData<>();
    private boolean isFour = false;
    private boolean isFirst = false;
    private boolean needRecharge = false;
    private boolean isOneYuan = false;
    private boolean isHorizontal = false;
    private boolean needWakeUp = false;
    private boolean needWakeUpSuccess = false;
    private int countWakeUp = 0;
    private int strongRemind = 0;
    private boolean needTFInit = true;
    private List<String> nightModelList = new ArrayList();
    private boolean isMoreFragmentShow = false;
    private int wifiFourPosition = 0;
    private boolean isOldPresetDevice = true;
    private boolean isFourState = false;
    private List<Integer> list = new ArrayList();
    private int Page = 0;
    private int controllerSize = 5;
    private List<ShadowButton> buttonList = new ArrayList();
    private List<ImageView> doorList = new ArrayList();
    private List<TextView> doorTextList = new ArrayList();
    private MutableLiveData<Integer> pageSelect = new MutableLiveData<>();
    private boolean isSwitching = false;
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private boolean isShowPtz = true;
    private boolean havePermission = false;
    private boolean isTo = false;
    private Handler wakeUpHandler = new AnonymousClass1();
    private boolean isLiveIntercoming = false;
    private SharePreferenceManager.OnCallSetListener definitionChangeListener = new AnonymousClass91();
    float nowScale = 0.0f;
    private boolean isFloat = false;
    final Runnable updatePlayInfoTimerTask = new Runnable() { // from class: activity.IPCameraActivity.98
        @Override // java.lang.Runnable
        public void run() {
            IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.98.1
                @Override // java.lang.Runnable
                public void run() {
                    if (IPCameraActivity.this.isFinishing()) {
                        return;
                    }
                    IPCameraActivity.this.updatePlayInfo();
                }
            });
        }
    };
    private long lastOnclickTime = 0;
    int i = 0;
    List<Integer> controllerList = new ArrayList<Integer>() { // from class: activity.IPCameraActivity.107
        {
            for (int i = 0; i < 128; i++) {
                add(0);
            }
        }
    };
    private CloudStatusBean cloudStatusBean = new CloudStatusBean();
    boolean flag = false;
    int QueryRFKeyStatusTimes = 0;
    long lastOnclickTime1 = 0;
    int statusFlag = 2;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_ipcamera_layout;
    }

    static /* synthetic */ int access$208(IPCameraActivity iPCameraActivity) {
        int i = iPCameraActivity.countWakeUp;
        iPCameraActivity.countWakeUp = i + 1;
        return i;
    }

    static /* synthetic */ int access$8710(IPCameraActivity iPCameraActivity) {
        int i = iPCameraActivity.is1100ErrorPre;
        iPCameraActivity.is1100ErrorPre = i - 1;
        return i;
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$1, reason: invalid class name */
    class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(@NonNull Message message) {
            switch (message.what) {
                case 1:
                    if (IPCameraActivity.this.player.getPlayState() != 3 && !IPCameraActivity.this.isTo) {
                        IPCameraActivity.access$208(IPCameraActivity.this);
                        if (SharePreferenceManager.getInstance().getNetState(IPCameraActivity.this.device.getIotId()) != 3) {
                            Handler handler = IPCameraActivity.this.uiHandler;
                            final IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                            handler.post(new Runnable() { // from class: activity.-$$Lambda$IPCameraActivity$1$V3p_lIWb-cocnqRXRIoTajU4X7o
                                @Override // java.lang.Runnable
                                public final void run() {
                                    iPCameraActivity.playLive();
                                }
                            });
                        }
                        Log.e("播放", "8s拉流");
                        if (IPCameraActivity.this.countWakeUp < 5) {
                            IPCameraActivity.this.wakeUpHandler.sendEmptyMessageDelayed(1, 8000L);
                        }
                    }
                    break;
                case 2:
                    break;
                default:
                    return;
            }
            IPCameraActivity.this.wakeUpDevice();
            IPCameraActivity.this.wakeUpDeviceHandel();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wakeUpDeviceHandel() {
        Message messageObtain = Message.obtain();
        messageObtain.what = 2;
        this.wakeUpHandler.sendMessageDelayed(messageObtain, AppConfig.LOW_POWER);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                startActivity(new Intent("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION"));
                return;
            } else {
                this.havePermission = true;
                Log.i("swyLog", "Android 11以上，当前已有权限");
                return;
            }
        }
        if (Build.VERSION.SDK_INT > 23) {
            if (ActivityCompat.checkSelfPermission(this, Permission.WRITE_EXTERNAL_STORAGE) != 0) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
                return;
            } else {
                this.havePermission = true;
                Log.i("swyLog", "Android 6.0以上，11以下，当前已有权限");
                return;
            }
        }
        this.havePermission = true;
        Log.i("swyLog", "Android 6.0以下，已获取权限");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cameraRemove(CameraRemove cameraRemove) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setProperties(setProperties setproperties) {
        Log.e("属性监听 IPCameraActivity", "" + setproperties.getData());
        JSONObject object = JSONObject.parseObject(String.valueOf(setproperties.getData()));
        String string = object.getString("iotId");
        if (this.device != null && !string.isEmpty() && string.equals(this.device.getIotId()) && setproperties.getData().contains(Constants.LowPowerStatus)) {
            Log.e("属性监听", "LowPowerStatus ");
            int intValue = object.getJSONObject("items").getJSONObject(Constants.LowPowerMode).getJSONObject("value").getIntValue(Constants.LowPowerStatus);
            if (intValue == 1) {
                this.uiHandler.post(new Runnable() { // from class: activity.-$$Lambda$IPCameraActivity$MUiXZLNsQEpwMU_mJa_-6WTKgFg
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.playLive();
                    }
                });
                Log.e("属性监听", "播放" + intValue);
            }
        }
    }

    @Override // activity.CommonActivity
    @SuppressLint({"ClickableViewAccessibility"})
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            DeviceInfoBean deviceInfoBean = this.device;
            if (deviceInfoBean != null) {
                this.iotId = deviceInfoBean.getIotId();
                this.title = this.device.getName();
                this.isOwner = this.device.getOwned() == 1;
                this.DeviceName = this.device.getDeviceName();
                this.ProductKey = this.device.getProductKey();
                this.wakeUpData = this.device.getWakeUpData();
                this.strongRemind = getIntent().getIntExtra("strongRemind", 0);
                this.iotIdList = getIntent().getStringArrayExtra(AlinkConstants.KEY_LIST);
            }
        }
        this.binding = (ActivityIpcameraLayoutBinding) DataBindingUtil.setContentView(this, R.layout.activity_ipcamera_layout);
        setEdgeToEdge(this.binding.layoutMain);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        NotchScreenManager.getInstance().setDisplayInNotch(this);
        this.binding.deleteLl.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.presetFragment.deletePresetPosition();
            }
        });
        this.binding.btPresetInvoke.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (IPCameraActivity.this.getActivity() == null) {
                    return;
                }
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.hideKeyboard(iPCameraActivity.getActivity());
                if ("".equals(((Editable) Objects.requireNonNull(IPCameraActivity.this.binding.etPreset.getText())).toString())) {
                    return;
                }
                IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).changePresetLocation(Integer.parseInt(IPCameraActivity.this.binding.etPreset.getText().toString()), new IPanelCallback() { // from class: activity.IPCameraActivity.3.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode();
                        }
                    }
                });
            }
        });
        this.binding.btPresetAdd.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (IPCameraActivity.this.getActivity() == null) {
                    return;
                }
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.hideKeyboard(iPCameraActivity.getActivity());
                if ("".equals(((Editable) Objects.requireNonNull(IPCameraActivity.this.binding.etPreset.getText())).toString())) {
                    return;
                }
                IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).addPresetLocation(Integer.parseInt(IPCameraActivity.this.binding.etPreset.getText().toString()), new IPanelCallback() { // from class: activity.IPCameraActivity.4.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode();
                        }
                    }
                });
            }
        });
        this.binding.ivLightWhile.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.currentInfrarred = SharePreferenceManager.getInstance().getDayNightMode(IPCameraActivity.this.iotId);
                int i = 0;
                for (int i2 = 0; i2 < IPCameraActivity.this.nightModelList.size(); i2++) {
                    if (((String) IPCameraActivity.this.nightModelList.get(i2)).equals(IPCameraActivity.this.infrarredMode[IPCameraActivity.this.currentInfrarred])) {
                        i = i2;
                    }
                }
                IPCameraActivity.this.nightModeFragment.showAllowingStateLoss(IPCameraActivity.this.getSupportFragmentManager(), "", i);
            }
        });
        this.binding.tabLayout.setSelected(false);
        this.binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { // from class: activity.IPCameraActivity.6
            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setCustomView((View) null);
                TextView textView = (TextView) LayoutInflater.from(IPCameraActivity.this).inflate(R.layout.textview_ipc, (ViewGroup) null);
                textView.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colorAccent));
                textView.setText(tab.getText());
                textView.setTypeface(Typeface.defaultFromStyle(1));
                tab.setCustomView(textView);
                if (tab.getText().equals(IPCameraActivity.this.getString(R.string.ptz))) {
                    IPCameraActivity.this.binding.rlTouchView.setVisibility(0);
                    IPCameraActivity.this.binding.ivCharge4gFlow.setVisibility(0);
                    if (SharePreferenceManager.getInstance().getSupport4G(IPCameraActivity.this.device.getIotId()) == 0 && !AppConfig.isChina) {
                        IPCameraActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                    }
                    IPCameraActivity.this.binding.ZOOMView.setVisibility(8);
                    IPCameraActivity.this.binding.autorView.setVisibility(8);
                    return;
                }
                if (tab.getText().equals(IPCameraActivity.this.getString(R.string.zoom))) {
                    IPCameraActivity.this.binding.rlTouchView.setVisibility(8);
                    IPCameraActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                    IPCameraActivity.this.binding.ZOOMView.setVisibility(0);
                    IPCameraActivity.this.binding.autorView.setVisibility(8);
                    return;
                }
                if (tab.getText().equals(IPCameraActivity.this.getString(R.string.auto_door))) {
                    IPCameraActivity.this.binding.rlTouchView.setVisibility(8);
                    IPCameraActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                    IPCameraActivity.this.binding.ZOOMView.setVisibility(8);
                    IPCameraActivity.this.binding.autorView.setVisibility(0);
                }
            }

            @Override // com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView((View) null);
                TextView textView = (TextView) LayoutInflater.from(IPCameraActivity.this).inflate(R.layout.textview_ipc, (ViewGroup) null);
                textView.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.color_gray));
                textView.setText(tab.getText());
                textView.setTypeface(Typeface.defaultFromStyle(0));
                tab.setCustomView(textView);
            }
        });
        this.binding.tabLayout.getTabAt(1).select();
        this.binding.tabLayout.getTabAt(2).select();
        this.binding.tabLayout.getTabAt(0).select();
        this.binding.zoomReduceBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.7
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCameraActivity.this.binding.zoomReduceBtn.setBackgroundResource(R.drawable.reduce_press_ipc);
                    if (IPCameraActivity.this.onTouchTimer == null) {
                        IPCameraActivity.this.onTouchTimer = new Timer();
                        IPCameraActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCameraActivity.7.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCameraActivity.this.binding.zoomReduceBtn.setBackgroundResource(R.drawable.reduce_ipc);
                    if (IPCameraActivity.this.onTouchTimer != null) {
                        IPCameraActivity.this.onTouchTimer.cancel();
                        IPCameraActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.zoomAddBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.8
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCameraActivity.this.binding.zoomAddBtn.setBackgroundResource(R.drawable.add_press_ipc);
                    if (IPCameraActivity.this.onTouchTimer == null) {
                        IPCameraActivity.this.onTouchTimer = new Timer();
                        IPCameraActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCameraActivity.8.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCameraActivity.this.binding.zoomAddBtn.setBackgroundResource(R.drawable.add_ipc);
                    if (IPCameraActivity.this.onTouchTimer != null) {
                        IPCameraActivity.this.onTouchTimer.cancel();
                        IPCameraActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.focusReduceBtn.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.9
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.changeFocus(0);
            }
        });
        this.binding.focusAddBtn.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.10
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.changeFocus(1);
            }
        });
        this.switch_4gArr = getResources().getStringArray(R.array.switch_4g);
        this.switch4gFragment = new SelectorDialogFragment(getString(R.string.network_change), true, this.switch_4gArr);
        this.switch4gFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.IPCameraActivity.11
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                if (i != 0 || IPCameraActivity.this.wifiFourPosition == i) {
                    if (i != 1 || IPCameraActivity.this.wifiFourPosition == i) {
                        if (i != 2 || IPCameraActivity.this.wifiFourPosition == i) {
                            return;
                        }
                        IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                        iPCameraActivity.switch4gMode(iPCameraActivity.getString(R.string.Net4GEnableSwitch), i);
                        IPCameraActivity.this.FourGChangeDialog(i);
                        return;
                    }
                    if (IPCameraActivity.this.WifiConfigIsExist == 0) {
                        WiFiListActivity.start(IPCameraActivity.this.getActivity(), IPCameraActivity.this.iotId, "1");
                        return;
                    } else {
                        if (IPCameraActivity.this.WifiConfigIsExist == 1) {
                            IPCameraActivity iPCameraActivity2 = IPCameraActivity.this;
                            iPCameraActivity2.switch4gMode(iPCameraActivity2.getString(R.string.Net4GEnableSwitch), i);
                            IPCameraActivity.this.FourGChangeDialog(i);
                            return;
                        }
                        return;
                    }
                }
                IPCameraActivity iPCameraActivity3 = IPCameraActivity.this;
                iPCameraActivity3.switch4gMode(iPCameraActivity3.getString(R.string.Net4GEnableSwitch), i);
                IPCameraActivity.this.FourGChangeDialog(i);
            }
        });
        this.inputDialogView = new InputDialogViewIpc.Builder().build();
        this.inputDialogView.addOnClickListener(new AnonymousClass12());
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$12, reason: invalid class name */
    class AnonymousClass12 implements InputDialogViewIpc.OnClickListener {
        AnonymousClass12() {
        }

        @Override // dialog.InputDialogViewIpc.OnClickListener
        public void onPositiveClick(String str, Object obj) {
            IPCameraActivity.this.showProgressDialog();
            IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).setAPList(IPCameraActivity.this.selectSsid, str, new IPanelCallback() { // from class: activity.IPCameraActivity.12.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (z) {
                        try {
                            if (obj2 == null) {
                                IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.12.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ToastUtils.toast(IPCameraActivity.this.getActivity(), IPCameraActivity.this.getString(R.string.set_wifi_failed));
                                    }
                                });
                            } else if (((IoTResponse) JSON.parseObject(obj2.toString()).toJavaObject(IoTResponse.class)).getCode() != 200) {
                                IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.12.1.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ToastUtils.toast(IPCameraActivity.this.getActivity(), IPCameraActivity.this.getString(R.string.set_wifi_failed));
                                    }
                                });
                            } else {
                                IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.12.1.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        IPCameraActivity.this.connect();
                                    }
                                });
                            }
                        } finally {
                            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.12.1.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    IPCameraActivity.this.dismissProgressDialog();
                                }
                            });
                        }
                    }
                }
            });
        }

        @Override // dialog.InputDialogViewIpc.OnClickListener
        public void onNegativeClick() {
            IPCameraActivity.this.inputDialogView.dismiss();
            IPCameraActivity.this.f1578dialog.show();
        }
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        Bundle extras = getIntent().getExtras();
        int i = 0;
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            DeviceInfoBean deviceInfoBean = this.device;
            if (deviceInfoBean != null) {
                this.iotId = deviceInfoBean.getIotId();
                this.title = this.device.getName();
                this.isOwner = this.device.getOwned() == 1;
                this.DeviceName = this.device.getDeviceName();
                this.ProductKey = this.device.getProductKey();
                this.wakeUpData = this.device.getWakeUpData();
                this.strongRemind = getIntent().getIntExtra("strongRemind", 0);
                this.iotIdList = getIntent().getStringArrayExtra(AlinkConstants.KEY_LIST);
                if (!AppConfig.isChina) {
                    this.binding.ivCharge4gFlow.setImageResource(R.drawable.selector_server_en);
                }
                this.binding.ivCharge4gFlow.setSelected(SharePreferenceManager.getInstance().getSupport4G(this.device.getIotId()) == 1);
                if (SharePreferenceManager.getInstance().getSupport4G(this.device.getIotId()) == 0 && !AppConfig.isChina) {
                    this.binding.ivCharge4gFlow.setVisibility(8);
                }
                if (SharePreferenceManager.getInstance().getSupport4G(this.device.getIotId()) == 1) {
                    this.binding.flipBtn.setBackgroundResource(R.drawable.icon_4g_twow);
                    this.binding.cloudText.setText(R.string.charge_4g_flow);
                }
                HashMap map = new HashMap();
                map.put(Constants.LowPowerWakeUp, 1);
                IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCameraActivity.13
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                    }
                });
            }
        }
        if (SharePreferenceManager.getInstance().getLowPower(this.iotId) != 1) {
            getBatteryPercentageAndCuTemperature();
        }
        this.infrarredMode = getResources().getStringArray(R.array.InfrarredMode);
        this.nightModelList.clear();
        if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.iotId) == -1) {
            while (true) {
                String[] strArr = this.infrarredMode;
                if (i >= strArr.length) {
                    break;
                }
                this.nightModelList.add(strArr[i]);
                i++;
            }
        } else {
            StringBuilder sbReverse = new StringBuilder(Integer.toBinaryString(SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.iotId))).reverse();
            for (int i2 = 0; i2 < sbReverse.length(); i2++) {
                if (sbReverse.charAt(i2) - '0' == 1) {
                    if (i2 == 0) {
                        this.nightModelList.add(this.infrarredMode[2]);
                    }
                    if (i2 == 1) {
                        this.nightModelList.add(this.infrarredMode[0]);
                    }
                    if (i2 == 2) {
                        this.nightModelList.add(this.infrarredMode[1]);
                    }
                }
            }
        }
        this.nightModeFragment = new SelectorDialogFragment(getString(R.string.night_mode), this.nightModelList);
        this.nightModeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.IPCameraActivity.14
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v10, types: [int] */
            /* JADX WARN: Type inference failed for: r0v12 */
            /* JADX WARN: Type inference failed for: r0v13 */
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i3) {
                ((String) IPCameraActivity.this.nightModelList.get(i3)).equals(IPCameraActivity.this.infrarredMode[0]);
                ?? Equals = ((String) IPCameraActivity.this.nightModelList.get(i3)).equals(IPCameraActivity.this.infrarredMode[1]);
                if (((String) IPCameraActivity.this.nightModelList.get(i3)).equals(IPCameraActivity.this.infrarredMode[2])) {
                    Equals = 2;
                }
                IPCameraActivity.this.updateNightMode(Integer.valueOf((int) Equals));
            }
        });
    }

    private void getBatteryPercentageAndCuTemperature() {
        if (this.batteryTimer == null) {
            this.batteryTimer = new Timer();
        }
        this.batteryTimer.schedule(new AnonymousClass15(), 0L, 60000L);
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$15, reason: invalid class name */
    class AnonymousClass15 extends TimerTask {
        AnonymousClass15() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).GetBatteryPercentage(new IPanelCallback() { // from class: activity.IPCameraActivity.15.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z) {
                        try {
                            final int intValue = ((JSONObject) JSONObject.parseObject(String.valueOf(obj)).get("data")).getIntValue("Value");
                            IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.15.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    IPCameraActivity.this.binding.SensorView.setPowerDisplay(intValue != 101);
                                    IPCameraActivity.this.binding.SensorView.setPower(intValue);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).GetCuTemperature(new IPanelCallback() { // from class: activity.IPCameraActivity.15.2
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z) {
                        try {
                            final int intValue = ((JSONObject) JSONObject.parseObject(String.valueOf(obj)).get("data")).getIntValue("Value");
                            IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.15.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    IPCameraActivity.this.binding.SensorView.setTemperatureDisplay(intValue != 101);
                                    IPCameraActivity.this.binding.SensorView.setTemperature(intValue);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    @SuppressLint({"ClickableViewAccessibility"})
    protected void onCreate(@Nullable Bundle bundle) {
        String[] strArr;
        Resources resources;
        int i;
        super.onCreate(bundle);
        this.uiHandler = new Handler(getMainLooper());
        this.whiteProgressDialog = new WhiteProgressDialog(this);
        this.binding.tvTitle.bringToFront();
        this.binding.tvTitle.setLineViewId(0);
        this.binding.tvTitle.setTitleText(this.title);
        this.binding.tvTitle.setOnViewClick(new IPCTitleView.OnViewClick() { // from class: activity.IPCameraActivity.16
            @Override // view.IPCTitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.IPCTitleView.OnViewClick
            public void OnLeftClick(View view2) {
                IPCameraActivity.this.onBackPressed();
            }
        });
        this.binding.tvTitle.setOnRightImageClick(new IPCTitleView.OnImageClick() { // from class: activity.IPCameraActivity.17
            @Override // view.IPCTitleView.OnImageClick
            public void OnRightImageClick(View view2) {
                Intent intent = new Intent(IPCameraActivity.this, (Class<?>) SettingsActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCameraActivity.this.device);
                intent.putExtras(bundle2);
                IPCameraActivity.this.startActivity(intent);
            }
        });
        this.height = (ScreenUtil.getDisplayMetrics(getActivity())[0] * 9.0f) / 16.0f;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.binding.play.getLayoutParams();
        layoutParams.height = (int) this.height;
        this.binding.play.setLayoutParams(layoutParams);
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) this.binding.fourPic.getLayoutParams();
        layoutParams.height = (int) ((ScreenUtil.getDisplayMetrics(getActivity())[0] * 9.0f) / 16.0f);
        this.binding.fourPic.setLayoutParams(layoutParams2);
        this.binding.llQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.18
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.binding.qualityDlg.setVisibility(0);
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.changeQualityDlgView(iPCameraActivity.defaultDefinition);
            }
        });
        this.binding.tvHQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.19
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.binding.qualityDlg.setVisibility(8);
                IPCameraActivity.this.changeDefinition(2);
            }
        });
        this.binding.tvMQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.20
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.binding.qualityDlg.setVisibility(8);
                IPCameraActivity.this.changeDefinition(1);
            }
        });
        this.binding.tvLQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.21
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.binding.qualityDlg.setVisibility(8);
                IPCameraActivity.this.changeDefinition(0);
            }
        });
        initPlayer();
        initLiveIntercom(this.iotId);
        this.binding.llCapture.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.22
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.snapshot();
            }
        });
        this.binding.llCapture.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.23
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCameraActivity.this.binding.tvCapture.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colorAccent));
                    IPCameraActivity.this.binding.captureBtn.setImageResource(R.drawable.video_camera_ipc_light);
                    return false;
                }
                IPCameraActivity.this.binding.tvCapture.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                IPCameraActivity.this.binding.captureBtn.setImageResource(R.drawable.video_camera_ipc);
                return false;
            }
        });
        this.binding.ivCharge4gFlow.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.24
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (IPCameraActivity.this.binding.ivCharge4gFlow.isSelected()) {
                    IPCameraActivity.this.isNet4GSwitch();
                    return;
                }
                if (IPCameraActivity.this.isSupport4G == 1) {
                    IPCameraActivity.this.ShowDialogWait();
                    return;
                }
                Intent intent = new Intent(IPCameraActivity.this.getActivity(), (Class<?>) PayYunServiceActivity2.class);
                intent.putExtra("iotId", IPCameraActivity.this.iotId);
                intent.putExtra(AlinkConstants.KEY_DN, IPCameraActivity.this.DeviceName);
                intent.putExtra(AlinkConstants.KEY_PK, IPCameraActivity.this.ProductKey);
                IPCameraActivity.this.startActivity(intent);
            }
        });
        this.binding.fullCamera.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.25
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.snapshot();
            }
        });
        this.binding.llRecord.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.26
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.startOrStopRecordingMp4();
            }
        });
        this.binding.fullVideo.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.27
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.startOrStopRecordingMp4();
            }
        });
        this.binding.speakerBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.28
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                Resources resources2;
                int i2;
                if (motionEvent.getAction() == 0) {
                    IPCameraActivity.this.startOrStopLiveIntercom();
                    ShadowButton shadowButton = IPCameraActivity.this.binding.fullIntercom;
                    if (IPCameraActivity.this.isLiveIntercoming) {
                        resources2 = IPCameraActivity.this.getResources();
                        i2 = R.drawable.full_intercom;
                    } else {
                        resources2 = IPCameraActivity.this.getResources();
                        i2 = R.drawable.full_intercom_;
                    }
                    shadowButton.setBackground(resources2.getDrawable(i2));
                    return false;
                }
                if (motionEvent.getAction() == 1) {
                    return false;
                }
                motionEvent.getAction();
                return false;
            }
        });
        if (this.isOwner) {
            this.binding.llShare.setVisibility(0);
        } else {
            this.binding.llShare.setVisibility(8);
        }
        this.binding.fullIntercom.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.29
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Resources resources2;
                int i2;
                IPCameraActivity.this.startOrStopLiveIntercom();
                ShadowButton shadowButton = IPCameraActivity.this.binding.fullIntercom;
                if (IPCameraActivity.this.isLiveIntercoming) {
                    resources2 = IPCameraActivity.this.getResources();
                    i2 = R.drawable.full_intercom;
                } else {
                    resources2 = IPCameraActivity.this.getResources();
                    i2 = R.drawable.full_intercom_;
                }
                shadowButton.setBackground(resources2.getDrawable(i2));
            }
        });
        this.binding.llListener.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.30
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Resources resources2;
                int i2;
                Resources resources3;
                int i3;
                IPCameraActivity.this.speakerSwitch = !r5.speakerSwitch;
                IPCameraActivity.this.player.setVolume(IPCameraActivity.this.speakerSwitch ? 1.0f : 0.0f);
                ImageButton imageButton = IPCameraActivity.this.binding.listenerBtn;
                boolean z = IPCameraActivity.this.speakerSwitch;
                int i4 = R.drawable.video_sound_light;
                imageButton.setImageResource(z ? R.drawable.video_sound_light : R.drawable.video_sound);
                TextView textView = IPCameraActivity.this.binding.tvVoice;
                if (IPCameraActivity.this.speakerSwitch) {
                    resources2 = IPCameraActivity.this.getResources();
                    i2 = R.color.colorAccent;
                } else {
                    resources2 = IPCameraActivity.this.getResources();
                    i2 = R.color.colors_ipc_image_text;
                }
                textView.setTextColor(resources2.getColor(i2));
                ImageButton imageButton2 = IPCameraActivity.this.binding.listenerBtn;
                if (!IPCameraActivity.this.speakerSwitch) {
                    i4 = R.drawable.video_sound;
                }
                imageButton2.setImageResource(i4);
                ShadowButton shadowButton = IPCameraActivity.this.binding.fullSound;
                if (IPCameraActivity.this.speakerSwitch) {
                    resources3 = IPCameraActivity.this.getResources();
                    i3 = R.drawable.full_sound;
                } else {
                    resources3 = IPCameraActivity.this.getResources();
                    i3 = R.drawable.full_sound_;
                }
                shadowButton.setBackground(resources3.getDrawable(i3));
            }
        });
        this.binding.fullSound.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.31
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Resources resources2;
                int i2;
                Resources resources3;
                int i3;
                IPCameraActivity.this.speakerSwitch = !r5.speakerSwitch;
                IPCameraActivity.this.player.setVolume(IPCameraActivity.this.speakerSwitch ? 1.0f : 0.0f);
                ImageButton imageButton = IPCameraActivity.this.binding.listenerBtn;
                boolean z = IPCameraActivity.this.speakerSwitch;
                int i4 = R.drawable.video_sound_light;
                imageButton.setImageResource(z ? R.drawable.video_sound_light : R.drawable.video_sound);
                TextView textView = IPCameraActivity.this.binding.tvVoice;
                if (IPCameraActivity.this.speakerSwitch) {
                    resources2 = IPCameraActivity.this.getResources();
                    i2 = R.color.colorAccent;
                } else {
                    resources2 = IPCameraActivity.this.getResources();
                    i2 = R.color.colors_ipc_image_text;
                }
                textView.setTextColor(resources2.getColor(i2));
                ImageButton imageButton2 = IPCameraActivity.this.binding.listenerBtn;
                if (!IPCameraActivity.this.speakerSwitch) {
                    i4 = R.drawable.video_sound;
                }
                imageButton2.setImageResource(i4);
                ShadowButton shadowButton = IPCameraActivity.this.binding.fullSound;
                if (IPCameraActivity.this.speakerSwitch) {
                    resources3 = IPCameraActivity.this.getResources();
                    i3 = R.drawable.full_sound;
                } else {
                    resources3 = IPCameraActivity.this.getResources();
                    i3 = R.drawable.full_sound_;
                }
                shadowButton.setBackground(resources3.getDrawable(i3));
            }
        });
        this.binding.fullNightVision.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.32
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.changeLightDlgView(iPCameraActivity.currentInfrarred);
                IPCameraActivity.this.binding.lightDlg.setVisibility(0);
                if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCameraActivity.this.iotId) != -1) {
                    IPCameraActivity.this.binding.tvLight1.setVisibility(8);
                    IPCameraActivity.this.binding.tvLight2.setVisibility(8);
                    IPCameraActivity.this.binding.tvLight3.setVisibility(8);
                    StringBuilder sbReverse = new StringBuilder(Integer.toBinaryString(SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCameraActivity.this.iotId))).reverse();
                    for (int i2 = 0; i2 < sbReverse.length(); i2++) {
                        if (sbReverse.charAt(i2) - '0' == 1) {
                            if (i2 == 0) {
                                IPCameraActivity.this.binding.tvLight3.setVisibility(0);
                            }
                            if (i2 == 1) {
                                IPCameraActivity.this.binding.tvLight1.setVisibility(0);
                            }
                            if (i2 == 2) {
                                IPCameraActivity.this.binding.tvLight2.setVisibility(0);
                            }
                        }
                    }
                }
            }
        });
        this.binding.lightDlg.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.33
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.binding.lightDlg.setVisibility(8);
            }
        });
        this.binding.tvLight1.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.34
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.binding.lightDlg.setVisibility(8);
                IPCameraActivity.this.updateNightMode(0);
            }
        });
        this.binding.tvLight2.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.35
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.binding.lightDlg.setVisibility(8);
                IPCameraActivity.this.updateNightMode(1);
            }
        });
        this.binding.tvLight3.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.36
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.binding.lightDlg.setVisibility(8);
                IPCameraActivity.this.updateNightMode(2);
            }
        });
        this.binding.llZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.37
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCameraActivity.this.binding.fullText.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colorAccent));
                    IPCameraActivity.this.binding.exoZoomTbtn.setBackgroundResource(R.drawable.video_full_screen_light);
                    return false;
                }
                IPCameraActivity.this.binding.fullText.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                IPCameraActivity.this.binding.exoZoomTbtn.setBackgroundResource(R.drawable.video_full_screen);
                return false;
            }
        });
        this.binding.llZoom.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.38
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCameraActivity.this.getRequestedOrientation() == 1) {
                    IPCameraActivity.this.setRequestedOrientation(0);
                } else {
                    IPCameraActivity.this.setRequestedOrientation(8);
                }
            }
        });
        this.binding.llShare.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.39
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCameraActivity.this.binding.shareBtn.setBackgroundResource(R.drawable.share_ipc_light);
                    IPCameraActivity.this.binding.shareText.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCameraActivity.this.binding.shareBtn.setBackgroundResource(R.drawable.share_ipc);
                IPCameraActivity.this.binding.shareText.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                return false;
            }
        });
        this.binding.layoutCloudPlayback.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.40
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                return false;
            }
        });
        this.binding.llShare.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.41
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.shareDialog2 = ((ShareDialog.Builder) new ShareDialog.Builder().view(R.layout.dialog_input2).leftBtnText(IPCameraActivity.this.getString(R.string.cancel)).rightBtnText(IPCameraActivity.this.getString(R.string.share_immediately)).clickRight(new View.OnClickListener() { // from class: activity.IPCameraActivity.41.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view3) {
                        if (IPCameraActivity.this.shareDialog2.getContent() != null) {
                            if (IPCameraActivity.this.shareDialog2.getMode() != 0 || SystemUtil.isPhone(IPCameraActivity.this.shareDialog2.getContent())) {
                                if (IPCameraActivity.this.shareDialog2.getMode() != 1 || SystemUtil.isEmail(IPCameraActivity.this.shareDialog2.getContent())) {
                                    IPCameraActivity.this.shareDevice(IPCameraActivity.this.shareDialog2.getContent(), (DeviceInfoBean) IPCameraActivity.this.shareDialog2.getExtra(), IPCameraActivity.this.shareDialog2.getMode() == 0 ? IPCameraActivity.this.shareDialog2.getDistinct() : null);
                                    return;
                                } else {
                                    ToastUtils.toast(IPCameraActivity.this.getActivity(), IPCameraActivity.this.getString(R.string.email_invalid));
                                    return;
                                }
                            }
                            ToastUtils.toast(IPCameraActivity.this.getActivity(), IPCameraActivity.this.getString(R.string.phone_invalid));
                            return;
                        }
                        Toast.makeText(IPCameraActivity.this.getActivity(), R.string.share_user_null_error, 0).show();
                    }
                })).create();
                IPCameraActivity.this.shareDialog2.setOnShareClick(new ShareDialog.OnShareClickListener() { // from class: activity.IPCameraActivity.41.2
                    @Override // dialog.ShareDialog.OnShareClickListener
                    public void onDistinctChange() {
                    }

                    @Override // dialog.ShareDialog.OnShareClickListener
                    public void onDistinctSelect(AreaCodeModel areaCodeModel) {
                    }

                    @Override // dialog.ShareDialog.OnShareClickListener
                    public void onShareSwitchChange() {
                    }
                });
                IPCameraActivity.this.shareDialog2.setExtra(IPCameraActivity.this.device);
                IPCameraActivity.this.shareDialog2.show(IPCameraActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.llMore.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.42
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCameraActivity.this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc_light);
                    IPCameraActivity.this.binding.moreText.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCameraActivity.this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc);
                IPCameraActivity.this.binding.moreText.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                return false;
            }
        });
        this.binding.llMore.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.43
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                FragmentTransaction fragmentTransactionBeginTransaction = IPCameraActivity.this.getSupportFragmentManager().beginTransaction();
                if (IPCameraActivity.this.isMoreFragmentShow) {
                    IPCameraActivity.this.isMoreFragmentShow = false;
                    if (IPCameraActivity.this.binding.rlTouchView.getVisibility() == 0) {
                        IPCameraActivity.this.binding.ivCharge4gFlow.setVisibility(0);
                        if (SharePreferenceManager.getInstance().getSupport4G(IPCameraActivity.this.device.getIotId()) == 0 && !AppConfig.isChina) {
                            IPCameraActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                        }
                    }
                    if (IPCameraActivity.this.isOldPresetDevice) {
                        fragmentTransactionBeginTransaction.hide(IPCameraActivity.this.controllerFragment).hide(IPCameraActivity.this.moreFragment).hide(IPCameraActivity.this.oldPresetFragment);
                        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
                        IPCameraActivity.this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc);
                        IPCameraActivity.this.binding.moreText.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                    } else {
                        fragmentTransactionBeginTransaction.hide(IPCameraActivity.this.controllerFragment).hide(IPCameraActivity.this.moreFragment).hide(IPCameraActivity.this.presetFragment);
                        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
                        IPCameraActivity.this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc);
                        IPCameraActivity.this.binding.moreText.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                    }
                    IPCameraActivity.this.binding.layoutFaceRecognition.setVisibility(8);
                    IPCameraActivity.this.binding.rlcenter.setVisibility(0);
                    return;
                }
                IPCameraActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                IPCameraActivity.this.isMoreFragmentShow = true;
                fragmentTransactionBeginTransaction.hide(IPCameraActivity.this.controllerFragment).show(IPCameraActivity.this.moreFragment);
                fragmentTransactionBeginTransaction.commitAllowingStateLoss();
                IPCameraActivity.this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc_light);
                IPCameraActivity.this.binding.moreText.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colorAccent));
            }
        });
        this.binding.layoutCloudPlayback.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.44
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCameraActivity.this.binding.ivCharge4gFlow.isSelected()) {
                    IPCameraActivity.this.isNet4GSwitch();
                    return;
                }
                if (IPCameraActivity.this.isSupport4G == 1) {
                    IPCameraActivity.this.ShowDialogWait();
                    return;
                }
                Intent intent = new Intent(IPCameraActivity.this.getActivity(), (Class<?>) PayYunServiceActivity2.class);
                intent.putExtra("iotId", IPCameraActivity.this.iotId);
                intent.putExtra(AlinkConstants.KEY_DN, IPCameraActivity.this.DeviceName);
                intent.putExtra(AlinkConstants.KEY_PK, IPCameraActivity.this.ProductKey);
                IPCameraActivity.this.startActivity(intent);
            }
        });
        this.binding.llFlip.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.45
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCameraActivity.this.binding.videoBtn.setBackgroundResource(R.drawable.video_back_light);
                    IPCameraActivity.this.binding.videoBackText.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCameraActivity.this.binding.videoBtn.setBackgroundResource(R.drawable.video_back);
                IPCameraActivity.this.binding.videoBackText.setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                return false;
            }
        });
        this.binding.llFlip.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.46
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.player.stop();
                IPCameraActivity.this.isTo = true;
                Intent intent = new Intent(IPCameraActivity.this, (Class<?>) RecordVideoActivity.class);
                intent.putExtra("title", IPCameraActivity.this.binding.tvTitle.getTitleText());
                intent.putExtra("iotId", IPCameraActivity.this.iotId);
                intent.putExtra("appKey", IPCameraActivity.this.appKey);
                IPCameraActivity.this.startActivity(intent);
            }
        });
        this.binding.videoPlayIbtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.47
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.dismissPlayButton();
                if (SharePreferenceManager.getInstance().getLowPower(IPCameraActivity.this.iotId) == 1) {
                    if (IPCameraActivity.this.wakeUpHandler != null) {
                        IPCameraActivity.this.wakeUpHandler.removeCallbacksAndMessages(null);
                    }
                    IPCameraActivity.this.wakeUpDevice();
                    IPCameraActivity.this.wakeUpDeviceHandel();
                    return;
                }
                IPCameraActivity.this.playLive();
            }
        });
        this.binding.bottomZoom.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.48
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCameraActivity.this.binding.changeZoom.getVisibility() == 0) {
                    IPCameraActivity.this.binding.changeZoom.setVisibility(8);
                } else {
                    IPCameraActivity.this.binding.changeZoom.setVisibility(0);
                }
            }
        });
        this.binding.addZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.49
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCameraActivity.this.onTouchTimer == null) {
                        IPCameraActivity.this.onTouchTimer = new Timer();
                        IPCameraActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCameraActivity.49.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCameraActivity.this.onTouchTimer != null) {
                    IPCameraActivity.this.onTouchTimer.cancel();
                    IPCameraActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.binding.fullAddZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.50
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCameraActivity.this.onTouchTimer == null) {
                        IPCameraActivity.this.onTouchTimer = new Timer();
                        IPCameraActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCameraActivity.50.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCameraActivity.this.onTouchTimer != null) {
                    IPCameraActivity.this.onTouchTimer.cancel();
                    IPCameraActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.binding.reduceZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.51
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCameraActivity.this.onTouchTimer == null) {
                        IPCameraActivity.this.onTouchTimer = new Timer();
                        IPCameraActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCameraActivity.51.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCameraActivity.this.onTouchTimer != null) {
                    IPCameraActivity.this.onTouchTimer.cancel();
                    IPCameraActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.binding.fullReduceZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.52
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCameraActivity.this.onTouchTimer == null) {
                        IPCameraActivity.this.onTouchTimer = new Timer();
                        IPCameraActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCameraActivity.52.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCameraActivity.this.onTouchTimer != null) {
                    IPCameraActivity.this.onTouchTimer.cancel();
                    IPCameraActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.zoom.observe(this, new Observer<Float>() { // from class: activity.IPCameraActivity.53
            @Override // androidx.lifecycle.Observer
            public void onChanged(@Nullable Float f) {
                Log.d(IPCameraActivity.this.TAG, "changeOpticalZoom:- " + f);
                if (f != null) {
                    if (f.floatValue() > 1.0f) {
                        IPCameraActivity.this.binding.tvOsd.setVisibility(0);
                    } else {
                        IPCameraActivity.this.binding.tvOsd.setVisibility(8);
                    }
                }
            }
        });
        this.binding.qualityDlg.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.54
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.binding.qualityDlg.setVisibility(8);
            }
        });
        DeviceInfoBean deviceInfoBean = new DeviceInfoBean();
        deviceInfoBean.setIotId(this.iotId);
        this.binding.fourPic.setDevice(deviceInfoBean);
        this.binding.fourPic.setTwoTap(this.binding.fourPicBtn);
        this.binding.fourPicBtn.setOnClickListener(new AnonymousClass55());
        if (SharePreferenceManager.getInstance().getDisplayController(this.iotId) == 1) {
            getControllerList();
        }
        this.binding.play.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.56
            /* JADX WARN: Removed duplicated region for block: B:54:0x00fc  */
            @Override // android.view.View.OnTouchListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public boolean onTouch(android.view.View r7, android.view.MotionEvent r8) {
                /*
                    Method dump skipped, instruction units count: 362
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: activity.IPCameraActivity.AnonymousClass56.onTouch(android.view.View, android.view.MotionEvent):boolean");
            }
        });
        this.binding.immediateRenewal.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.57
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.isNet4GSwitch();
            }
        });
        initAutorView();
        if (this.strongRemind == 1) {
            startLiveIntercom();
            this.speakerSwitch = true;
            this.player.setVolume(this.speakerSwitch ? 1.0f : 0.0f);
            this.binding.listenerBtn.setImageResource(this.speakerSwitch ? R.drawable.video_sound_light : R.drawable.video_sound);
            TextView textView = this.binding.tvVoice;
            if (this.speakerSwitch) {
                resources = getResources();
                i = R.color.colorAccent;
            } else {
                resources = getResources();
                i = R.color.colors_ipc_image_text;
            }
            textView.setTextColor(resources.getColor(i));
        }
        resetInactivityTimer();
        if (this.isShowPtz) {
            addControlTouchView(false);
        } else {
            this.binding.rlTouchView.setVisibility(8);
            this.binding.ivCharge4gFlow.setVisibility(8);
            this.binding.ZOOMView.setVisibility(0);
            this.binding.autorView.setVisibility(8);
            this.binding.layoutCloudPlayback.setVisibility(0);
        }
        if (SharePreferenceManager.getInstance().getNightVisionHide(this.device.getIotId()) == 1 || SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.device.getIotId()) == 0) {
            this.binding.ivLightWhile.setVisibility(8);
            this.binding.fullNightVision.setVisibility(8);
        }
        if (AppConfig.isChina) {
            strArr = new String[]{"高德地图", "百度地图"};
        } else {
            strArr = new String[]{"Google Map"};
        }
        this.mapFragment = new SelectorDialogFragment("" + getResources().getString(R.string.select_map), strArr);
        this.mapFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.IPCameraActivity.58
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i2) {
                switch (i2) {
                    case 0:
                        if (AppConfig.isChina) {
                            if (MapUtils.isAvilible(IPCameraActivity.this, "com.autonavi.minimap")) {
                                try {
                                    StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=");
                                    stringBuffer.append("yitu8_driver");
                                    stringBuffer.append("&lat=");
                                    stringBuffer.append(IPCameraActivity.this.lat);
                                    stringBuffer.append("&lon=");
                                    stringBuffer.append(IPCameraActivity.this.lon);
                                    stringBuffer.append("&dev=");
                                    stringBuffer.append(1);
                                    stringBuffer.append("&style=");
                                    stringBuffer.append(0);
                                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
                                    intent.addCategory("android.intent.category.DEFAULT");
                                    intent.setPackage("com.autonavi.minimap");
                                    IPCameraActivity.this.startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return;
                                }
                            } else {
                                Toast.makeText(IPCameraActivity.this, "您尚未安装高德地图", 1).show();
                            }
                        } else if (MapUtils.isAvilible(IPCameraActivity.this, "com.google.android.apps.maps")) {
                            Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse("google.navigation:q=" + IPCameraActivity.this.lat + "," + IPCameraActivity.this.lon + ", + Sydney +Australia"));
                            intent2.setPackage("com.google.android.apps.maps");
                            IPCameraActivity.this.startActivity(intent2);
                        } else {
                            Toast.makeText(IPCameraActivity.this, IPCameraActivity.this.getString(R.string.not_installed) + "Google Map", 1).show();
                            IPCameraActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.google.android.apps.maps")));
                        }
                        break;
                    case 1:
                        if (MapUtils.isAvilible(IPCameraActivity.this, "com.baidu.BaiduMap")) {
                            try {
                                StringBuffer stringBuffer2 = new StringBuffer("baidumap://map/navi?location=");
                                stringBuffer2.append(IPCameraActivity.this.lat);
                                stringBuffer2.append(",");
                                stringBuffer2.append(IPCameraActivity.this.lon);
                                stringBuffer2.append("&type=TIME");
                                Intent intent3 = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer2.toString()));
                                intent3.setPackage("com.baidu.BaiduMap");
                                IPCameraActivity.this.startActivity(intent3);
                            } catch (Exception e2) {
                                Log.e("intent", e2.getMessage());
                                return;
                            }
                        } else {
                            Toast.makeText(IPCameraActivity.this, "您尚未安装百度地图", 1).show();
                        }
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$55, reason: invalid class name */
    class AnonymousClass55 implements View.OnClickListener {
        AnonymousClass55() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            Log.d(IPCameraActivity.this.TAG, "onClick: -------------" + IPCameraActivity.this.player.getPlayState());
            if (IPCameraActivity.this.player.getPlayState() != 2 && IPCameraActivity.this.player.getPlayState() != 1) {
                if (!IPCameraActivity.this.isFirst) {
                    IPCameraActivity.this.isFirst = true;
                    IPCameraActivity.this.isFour = true;
                    IPCameraActivity.this.player.stop();
                    IPCameraActivity.this.binding.fourPic.setData(IPCameraActivity.this.iotIdList);
                    IPCameraActivity.this.binding.play.setVisibility(8);
                    IPCameraActivity.this.binding.fourPic.setVisibility(0);
                    IPCameraActivity.this.binding.llBottom.setVisibility(8);
                    IPCameraActivity.this.binding.fourOnePic.setImageDrawable(IPCameraActivity.this.getResources().getDrawable(R.drawable.four_one));
                    return;
                }
                if (!IPCameraActivity.this.isFour) {
                    IPCameraActivity.this.isFour = true;
                    IPCameraActivity.this.player.stop();
                    IPCameraActivity.this.binding.fourPic.startPlayer();
                    IPCameraActivity.this.binding.play.setVisibility(8);
                    IPCameraActivity.this.binding.fourPic.setVisibility(0);
                    IPCameraActivity.this.binding.llBottom.setVisibility(8);
                    IPCameraActivity.this.binding.fourOnePic.setImageDrawable(IPCameraActivity.this.getResources().getDrawable(R.drawable.four_one));
                    return;
                }
                if (IPCameraActivity.this.binding.fourPic.getStatus() == 3) {
                    IPCameraActivity.this.isFour = false;
                    IPCameraActivity.this.binding.fourPic.stop();
                    IPCameraActivity.this.binding.play.setVisibility(0);
                    IPCameraActivity.this.binding.fourPic.setVisibility(8);
                    IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                    iPCameraActivity.beanInfo = iPCameraActivity.binding.fourPic.getDevice();
                    if (IPCameraActivity.this.beanInfo != null) {
                        IPCameraActivity iPCameraActivity2 = IPCameraActivity.this;
                        iPCameraActivity2.device = iPCameraActivity2.beanInfo;
                        if (IPCameraActivity.this.beanInfo.getIotId() != null && "".equals(IPCameraActivity.this.beanInfo.getIotId())) {
                            IPCameraActivity iPCameraActivity3 = IPCameraActivity.this;
                            iPCameraActivity3.iotId = iPCameraActivity3.beanInfo.getIotId();
                        }
                        IPCameraActivity iPCameraActivity4 = IPCameraActivity.this;
                        iPCameraActivity4.title = iPCameraActivity4.beanInfo.getName();
                        IPCameraActivity iPCameraActivity5 = IPCameraActivity.this;
                        iPCameraActivity5.isOwner = iPCameraActivity5.beanInfo.getOwned() == 1;
                        IPCameraActivity iPCameraActivity6 = IPCameraActivity.this;
                        iPCameraActivity6.DeviceName = iPCameraActivity6.beanInfo.getDeviceName();
                        IPCameraActivity iPCameraActivity7 = IPCameraActivity.this;
                        iPCameraActivity7.ProductKey = iPCameraActivity7.beanInfo.getProductKey();
                    }
                    IPCameraActivity.this.binding.llBottom.setVisibility(0);
                    IPCameraActivity.this.player.setIPCLiveDataSource(IPCameraActivity.this.iotId, 0, false, 0, true, 0);
                    IPCameraActivity.this.player.prepare();
                    IPCameraActivity.this.binding.fourOnePic.setImageDrawable(IPCameraActivity.this.getResources().getDrawable(R.drawable.four_ipc));
                    IPCameraActivity iPCameraActivity8 = IPCameraActivity.this;
                    iPCameraActivity8.initLiveIntercom(iPCameraActivity8.iotId);
                    IPCameraActivity.this.isFourState = false;
                    IPCameraActivity.this.netVisible = false;
                    IPCameraActivity.this.smartDoorVisible = false;
                    IPCameraActivity.this.shopVisible = false;
                    IPCameraActivity.this.lightVisible = false;
                    IPCameraActivity.this.isDetecting = false;
                    IPCameraActivity.this.isOldPresetDevice = true;
                    IPCameraActivity.this.getProperties(new AnonymousClass1());
                    IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.55.2
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCameraActivity.this.binding.tvTitle.setTitleText(IPCameraActivity.this.title);
                        }
                    });
                    return;
                }
                return;
            }
            Log.d(IPCameraActivity.this.TAG, "onClick: ");
        }

        /* JADX INFO: renamed from: activity.IPCameraActivity$55$1, reason: invalid class name */
        class AnonymousClass1 implements MyCallback {
            AnonymousClass1() {
            }

            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                if (SharePreferenceManager.getInstance().getMixZoom(IPCameraActivity.this.iotId) == 1) {
                    IPCameraActivity.this.isMixZoom = true;
                    IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.55.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCameraActivity.this.binding.tvOsd.setVisibility(8);
                        }
                    });
                } else {
                    IPCameraActivity.this.isMixZoom = false;
                }
                if (SharePreferenceManager.getInstance().getSupportZoom(IPCameraActivity.this.iotId) != 1) {
                    IPCameraActivity.this.isOpticalZoom = false;
                } else {
                    IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.55.1.2
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCameraActivity.this.binding.tvOsd.setVisibility(8);
                        }
                    });
                    IPCameraActivity.this.isOpticalZoom = true;
                }
                if (SharePreferenceManager.getInstance().getSensorViewDisplay(IPCameraActivity.this.iotId) == 0) {
                    IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.55.1.3
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCameraActivity.this.binding.SensorView.setVisibility(8);
                        }
                    });
                } else {
                    IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.55.1.4
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCameraActivity.this.binding.SensorView.setVisibility(0);
                        }
                    });
                }
                if (SharePreferenceManager.getInstance().getDisplayController(IPCameraActivity.this.iotId) == 0) {
                    IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.55.1.5
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCameraActivity.this.binding.llController.setVisibility(8);
                        }
                    });
                }
                if (((SharePreferenceManager.getInstance().getPageControlEx(IPCameraActivity.this.iotId) & 256) >> 8) == 1) {
                    IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.55.1.6
                        @Override // java.lang.Runnable
                        public void run() {
                            IPCameraActivity.this.binding.bottomShop.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.55.1.6.1
                                @Override // android.view.View.OnClickListener
                                public void onClick(View view2) {
                                    IPCameraActivity.this.openH5(SharePreferenceManager.getInstance().getUserMallUrl(IPCameraActivity.this.iotId));
                                }
                            });
                        }
                    });
                }
                IPCameraActivity.this.faceDetectionAbility = SharePreferenceManager.getInstance().getHumanoidTracking(IPCameraActivity.this.iotId).intValue();
                IPCameraActivity.this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(IPCameraActivity.this.iotId);
                if (IPCameraActivity.this.faceDetectionAbility == 1) {
                    IPCameraActivity.this.isDetecting = SharePreferenceManager.getInstance().getHumanoidTrackingEnable(IPCameraActivity.this.iotId).intValue() == 1;
                } else {
                    IPCameraActivity.this.isDetecting = SharePreferenceManager.getInstance().getIntelligentMode(IPCameraActivity.this.iotId) == 1;
                }
            }
        }
    }

    @Override // android.app.Activity
    public void onUserInteraction() {
        resetInactivityTimer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetInactivityTimer() {
        if (SharePreferenceManager.getInstance().getNetState(this.device.getIotId()) == 0) {
            if (SharePreferenceManager.getInstance().getSupport4G(this.device.getIotId()) == 1) {
                stopInactivityTimer();
                startInactivityTimer();
                return;
            }
            return;
        }
        if (SharePreferenceManager.getInstance().getNetState(this.device.getIotId()) == 3) {
            stopInactivityTimer();
            startInactivityTimer();
        }
    }

    private void startInactivityTimer() {
        if (this.inactivityTimer == null) {
            this.inactivityTimer = new Timer();
            this.inactivityTimer.schedule(new InactivityTimerTask(this, null), AppConfig.INACTIVITY_DELAY);
            Log.e("防沉迷", "开始");
        }
    }

    private void stopInactivityTimer() {
        Timer timer = this.inactivityTimer;
        if (timer != null) {
            timer.cancel();
            this.inactivityTimer.purge();
            this.inactivityTimer = null;
            Log.e("防沉迷", "停止");
        }
    }

    private class InactivityTimerTask extends TimerTask {
        private InactivityTimerTask() {
        }

        /* synthetic */ InactivityTimerTask(IPCameraActivity iPCameraActivity, AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.InactivityTimerTask.1
                @Override // java.lang.Runnable
                public void run() {
                    if (IPCameraActivity.this.player.getPlayState() == 3) {
                        if (IPCameraActivity.this.player != null) {
                            IPCameraActivity.this.player.stop();
                        }
                        IPCameraActivity.this.showPlayButton();
                        Log.e("防沉迷", "在播放");
                        DialogUtil.showTipsConfirmDiaLog(IPCameraActivity.this.getActivity(), IPCameraActivity.this.getString(R.string.warm_tips), IPCameraActivity.this.getString(R.string.warm_tips_1), IPCameraActivity.this.getString(R.string.i_know), new DialogUtil.OnConfirmClickListener() { // from class: activity.IPCameraActivity.InactivityTimerTask.1.1
                            @Override // dialog.DialogUtil.OnConfirmClickListener
                            public void ConfirmListener() {
                                IPCameraActivity.this.resetInactivityTimer();
                            }
                        });
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void isNet4GSwitch() {
        if (TextUtils.isEmpty(this.IccId)) {
            showToast(getResources().getString(R.string.query_traffic_fail));
            return;
        }
        if (((SharePreferenceManager.getInstance().getPageControlEx(this.iotId) & 524288) >> 19) == 1) {
            if (SharePreferenceManager.getInstance().getIccId1(this.device.getIotId()).equals("") && SharePreferenceManager.getInstance().getIccId2(this.device.getIotId()).equals("")) {
                Intent intent = new Intent(getActivity(), (Class<?>) Traffic4GActivity.class);
                intent.putExtra("iccid", this.IccId);
                intent.putExtra("iotId", this.iotId);
                intent.putExtra(AlinkConstants.KEY_DN, this.device.getDeviceName());
                intent.putExtra(AlinkConstants.KEY_PK, this.device.getProductKey());
                startActivity(intent);
                return;
            }
            Intent intent2 = new Intent(this, (Class<?>) Net4GSwitchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
            intent2.putExtras(bundle);
            startActivity(intent2);
            return;
        }
        Intent intent3 = new Intent(getActivity(), (Class<?>) Traffic4GActivity.class);
        intent3.putExtra("iccid", this.IccId);
        intent3.putExtra("iotId", this.iotId);
        intent3.putExtra(AlinkConstants.KEY_DN, this.device.getDeviceName());
        intent3.putExtra(AlinkConstants.KEY_PK, this.device.getProductKey());
        startActivity(intent3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v6, types: [activity.IPCameraActivity$60] */
    public void ShowDialogWait() {
        final DialogView dialogViewBuild = new DialogView.Builder(this).setTitle(getString(R.string.warning)).setContent(getString(R.string.cloud_buy_tips)).setNegativeClickListener(getString(R.string.cancel), $$Lambda$xLsIGvbwdcdhkPRJ4OVW3aSWueY.INSTANCE).setPositiveClickListener(getString(R.string.confirm), new DialogView.OnPositiveClickListener() { // from class: activity.IPCameraActivity.59
            @Override // view.DialogView.OnPositiveClickListener
            public void onPositiveClick(DialogView dialogView) {
                Intent intent = new Intent(IPCameraActivity.this.getActivity(), (Class<?>) PayYunServiceActivity2.class);
                intent.putExtra("iotId", IPCameraActivity.this.iotId);
                intent.putExtra(AlinkConstants.KEY_DN, IPCameraActivity.this.DeviceName);
                intent.putExtra(AlinkConstants.KEY_PK, IPCameraActivity.this.ProductKey);
                IPCameraActivity.this.startActivity(intent);
                dialogView.dismiss();
            }
        }).build();
        dialogViewBuild.show();
        dialogViewBuild.getTvConfirm().setClickable(false);
        new CountDownTimer(5000L, 1000L) { // from class: activity.IPCameraActivity.60
            @Override // android.os.CountDownTimer
            @SuppressLint({"SetTextI18n"})
            public void onTick(final long j) {
                Log.d(IPCameraActivity.this.TAG, "onTick: -----" + Thread.currentThread().getName());
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.60.1
                    @Override // java.lang.Runnable
                    public void run() {
                        dialogViewBuild.getTvConfirm().setTextColor(IPCameraActivity.this.getResources().getColor(R.color.color_gray));
                        dialogViewBuild.getTvConfirm().setText(IPCameraActivity.this.getString(R.string.confirm) + "(" + (j / 1000) + ")");
                    }
                });
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.60.2
                    @Override // java.lang.Runnable
                    public void run() {
                        dialogViewBuild.getTvConfirm().setTextColor(IPCameraActivity.this.getResources().getColor(R.color.color_black));
                        dialogViewBuild.getTvConfirm().setText(IPCameraActivity.this.getString(R.string.confirm));
                        dialogViewBuild.getTvConfirm().setClickable(true);
                    }
                });
                cancel();
            }
        }.start();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.isMoreFragmentShow = false;
        this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc);
        this.binding.moreText.setTextColor(getResources().getColor(R.color.colors_ipc_image_text));
        this.binding.ipcOfflineText.setVisibility(8);
        SharePreferenceManager.getInstance().registerOnCallSetListener(this.definitionChangeListener);
        this.defaultDefinition = SharePreferenceManager.getInstance().getStreamVideoQuality(this.iotId);
        changeDefinitionView(this.defaultDefinition);
        this.rotationOrientate = SharePreferenceManager.getInstance().getImageFlip(this.iotId);
        getProperties(new AnonymousClass61());
        this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(this.iotId);
        this.faceDetectionAbility = SharePreferenceManager.getInstance().getHumanoidTracking(this.iotId).intValue();
        if (this.faceDetectionAbility == 1) {
            this.isDetecting = SharePreferenceManager.getInstance().getHumanoidTrackingEnable(this.iotId).intValue() == 1;
        } else {
            this.isDetecting = SharePreferenceManager.getInstance().getIntelligentMode(this.iotId) == 1;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("faceDetectionAbility", this.faceDetectionAbility);
        bundle.putInt("supportMotionDetect", this.supportMotionDetect);
        bundle.putString("iotId", this.iotId);
        bundle.putBoolean("isDetecting", this.isDetecting);
        bundle.putBoolean("lightVisible", this.lightVisible);
        bundle.putBoolean("shopVisible", this.shopVisible);
        bundle.putBoolean("netVisible", this.netVisible);
        bundle.putBoolean("smartDoorVisible", this.smartDoorVisible);
        if (this.lightVisible) {
            bundle.putInt("currentInfrarred", SharePreferenceManager.getInstance().getDayNightMode(this.iotId));
        }
        this.netVisible = SharePreferenceManager.getInstance().getDoubleNetWork(this.iotId) == 1;
        if (this.netVisible) {
            bundle.putString("switchText", this.switchText);
        }
        this.moreFragment = new MoreFragment();
        this.moreFragment.setArguments(bundle);
        Bundle bundle2 = new Bundle();
        bundle2.putString("iotId", this.iotId);
        this.controllerFragment = new ControllerFragment();
        this.controllerFragment.setArguments(bundle2);
        if (this.presetList == null) {
            this.presetList = new ArrayList();
        }
        this.presetFragment = new PresetFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("iotId", this.iotId);
        bundle3.putBoolean("isOwner", this.isOwner);
        bundle3.putIntegerArrayList(AlinkConstants.KEY_LIST, (ArrayList) this.presetList);
        this.presetFragment.setArguments(bundle3);
        this.oldPresetFragment = new OldPresetFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putString("iotId", this.iotId);
        this.oldPresetFragment.setArguments(bundle4);
        FragmentTransaction fragmentTransactionBeginTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransactionBeginTransaction.replace(R.id.f6225fragment, this.controllerFragment);
        fragmentTransactionBeginTransaction.add(R.id.f6225fragment, this.moreFragment);
        fragmentTransactionBeginTransaction.add(R.id.f6225fragment, this.presetFragment);
        fragmentTransactionBeginTransaction.add(R.id.f6225fragment, this.oldPresetFragment);
        fragmentTransactionBeginTransaction.hide(this.controllerFragment).hide(this.moreFragment).hide(this.presetFragment).hide(this.oldPresetFragment);
        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
        if (SharePreferenceManager.getInstance().getDisplayController(this.iotId) == 1) {
            getControllerList();
        }
        this.controllerFragment.setList(this.controllerList);
        setList(this.controllerList);
        if (SharePreferenceManager.getInstance().getLowPower(this.iotId) == 1) {
            Handler handler = this.wakeUpHandler;
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
            wakeUpDevice();
            wakeUpDeviceHandel();
        } else {
            playLive();
        }
        ControllerFragment controllerFragment = this.controllerFragment;
        if (controllerFragment != null) {
            controllerFragment.refreshButton();
            refreshButton();
            if (SharePreferenceManager.getInstance().getDisplayController(this.iotId) == 1) {
                getControllerList();
            }
        }
        this.IccId = SharePreferenceManager.getInstance().getIccId(this.iotId);
        String str = this.iotId;
        if (str != null) {
            getThingsStatus(str);
        }
        DeviceInfoBean deviceInfoBean = this.device;
        if (deviceInfoBean != null && deviceInfoBean.getOwned() == 1 && SharePreferenceManager.getInstance().getDoubleNetWork(this.iotId) == 1) {
            this.WifiConfigIsExist = SharePreferenceManager.getInstance().getWifiConfigIsExist(this.iotId);
            if (SharePreferenceManager.getInstance().getFirstNet(this.iotId)) {
                SharePreferenceManager.getInstance().setFirstNet(this.iotId, true);
                if (SharePreferenceManager.getInstance().getWifiConfigIsExist(this.iotId) != 1) {
                    getWiFiList();
                }
            }
        }
        if (SharePreferenceManager.getInstance().getFirstFormatInIpc(this.iotId) && SharePreferenceManager.getInstance().getStorageStatus(this.iotId) == 2 && this.needTFInit) {
            showFormatDialog(SharePreferenceManager.getInstance().getStorageTotalCapacity(this.iotId) / 1024.0f, SharePreferenceManager.getInstance().getStorageRemainingCapacity(this.iotId) / 1024.0f, SharePreferenceManager.getInstance().getStorageStatus(this.iotId));
        }
        if (this.isFourState) {
            getSupportFragmentManager().beginTransaction().hide(this.presetFragment).hide(this.oldPresetFragment).show(this.moreFragment).commitAllowingStateLoss();
            this.binding.f6229fragment.setClickable(false);
            this.isMoreFragmentShow = true;
        }
        resetInactivityTimer();
        DrawLine();
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$61, reason: invalid class name */
    class AnonymousClass61 implements MyCallback {
        AnonymousClass61() {
        }

        @Override // tools.MyCallback
        public void onComplete(boolean z) {
            if (SharePreferenceManager.getInstance().getIsRouter(IPCameraActivity.this.iotId) == 1) {
                Intent intent = new Intent(IPCameraActivity.this.getActivity(), (Class<?>) BleRouterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCameraActivity.this.device);
                intent.putExtras(bundle);
                IPCameraActivity.this.startActivity(intent);
                IPCameraActivity.this.finish();
            }
            if (SharePreferenceManager.getInstance().getMixZoom(IPCameraActivity.this.iotId) == 1) {
                IPCameraActivity.this.isMixZoom = true;
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.61.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.binding.tvOsd.setVisibility(8);
                    }
                });
            } else {
                IPCameraActivity.this.isMixZoom = false;
            }
            if (SharePreferenceManager.getInstance().getSupportZoom(IPCameraActivity.this.iotId) != 1) {
                IPCameraActivity.this.isOpticalZoom = false;
            } else {
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.61.2
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.binding.tvOsd.setVisibility(8);
                    }
                });
                IPCameraActivity.this.isOpticalZoom = true;
            }
            if (SharePreferenceManager.getInstance().getSensorViewDisplay(IPCameraActivity.this.iotId) == 0) {
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.61.3
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.binding.SensorView.setVisibility(8);
                    }
                });
            } else {
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.61.4
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.binding.SensorView.setVisibility(0);
                    }
                });
            }
            if (SharePreferenceManager.getInstance().getDisplayController(IPCameraActivity.this.iotId) == 0) {
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.61.5
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.binding.llController.setVisibility(8);
                    }
                });
            }
            if (((SharePreferenceManager.getInstance().getPageControlEx(IPCameraActivity.this.iotId) & 256) >> 8) == 1) {
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.61.6
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.binding.bottomShop.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.61.6.1
                            @Override // android.view.View.OnClickListener
                            public void onClick(View view2) {
                                IPCameraActivity.this.openH5(SharePreferenceManager.getInstance().getUserMallUrl(IPCameraActivity.this.iotId));
                            }
                        });
                    }
                });
            }
            IPCameraActivity.this.isShowPtz = SharePreferenceManager.getInstance().getPTZHide(IPCameraActivity.this.iotId) != 1;
            if (!IPCameraActivity.this.isShowPtz && IPCameraActivity.this.touchView != null && IPCameraActivity.this.touchView.getVisibility() == 0) {
                IPCameraActivity.this.binding.tabLayout.removeTabAt(0);
                IPCameraActivity.this.touchView.setVisibility(8);
                IPCameraActivity.this.binding.layoutCloudPlayback.setVisibility(0);
            }
            if (SharePreferenceManager.getInstance().getNightVisionHide(IPCameraActivity.this.device.getIotId()) == 1 || SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCameraActivity.this.device.getIotId()) == 0) {
                IPCameraActivity.this.binding.ivLightWhile.setVisibility(8);
                IPCameraActivity.this.binding.fullNightVision.setVisibility(8);
            }
            IPCameraActivity.this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(IPCameraActivity.this.iotId);
            IPCameraActivity.this.faceDetectionAbility = SharePreferenceManager.getInstance().getHumanoidTracking(IPCameraActivity.this.iotId).intValue();
            if (IPCameraActivity.this.faceDetectionAbility == 1) {
                IPCameraActivity.this.isDetecting = SharePreferenceManager.getInstance().getHumanoidTrackingEnable(IPCameraActivity.this.iotId).intValue() == 1;
            } else {
                IPCameraActivity.this.isDetecting = SharePreferenceManager.getInstance().getIntelligentMode(IPCameraActivity.this.iotId) == 1;
            }
            if (IPCameraActivity.this.moreFragment != null) {
                IPCameraActivity.this.moreFragment.setDetecting(IPCameraActivity.this.isDetecting);
            }
            IPCameraActivity.this.smartDoorVisible = SharePreferenceManager.getInstance().getDisplayController(IPCameraActivity.this.iotId) == 1;
            if (IPCameraActivity.this.smartDoorVisible) {
                if (!IPCameraActivity.this.isShowPtz) {
                    if (IPCameraActivity.this.binding.tabLayout.getTabCount() <= 1) {
                        IPCameraActivity.this.binding.tabLayout.addTab(IPCameraActivity.this.binding.tabLayout.newTab().setText(IPCameraActivity.this.getResources().getString(R.string.auto_door)));
                    }
                    if (IPCameraActivity.this.binding.tabLayout.getTabCount() >= 3) {
                        IPCameraActivity.this.binding.tabLayout.removeTabAt(2);
                        return;
                    }
                    return;
                }
                if (IPCameraActivity.this.binding.tabLayout.getTabCount() < 3) {
                    IPCameraActivity.this.binding.tabLayout.addTab(IPCameraActivity.this.binding.tabLayout.newTab().setText(IPCameraActivity.this.getResources().getString(R.string.auto_door)));
                    return;
                }
                return;
            }
            if (IPCameraActivity.this.binding.tabLayout.getTabCount() >= 3) {
                IPCameraActivity.this.binding.tabLayout.removeTabAt(2);
            }
            if (IPCameraActivity.this.isShowPtz) {
                return;
            }
            IPCameraActivity.this.binding.tabLayout.removeTabAt(1);
        }
    }

    private void DrawLine() {
        try {
            AreaPointBean areaPointBean = (AreaPointBean) new Gson().fromJson(SharePreferenceManager.getInstance().getRegionDetectPoint(this.iotId), AreaPointBean.class);
            if (areaPointBean != null) {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.binding.drawLineView.getLayoutParams();
                layoutParams.height = (int) this.height;
                this.binding.drawLineView.setLayoutParams(layoutParams);
                this.binding.drawLineView.setPointList(areaPointBean, ScreenUtil.getDisplayMetrics(getActivity())[0], (int) this.height);
            }
        } catch (Exception unused) {
        }
        this.binding.lineview.setIotId(this.iotId);
        this.binding.lineview.setGone();
    }

    private void showFormatDialog(final float f, final float f2, final int i) {
        this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.62
            @Override // java.lang.Runnable
            public void run() {
                new BaseDialog.Builder().view(R.layout.dialog_common).content(IPCameraActivity.this.getString(R.string.sd_card_not_initialized)).leftBtnText(IPCameraActivity.this.getString(R.string.cancel)).clickLeft(new View.OnClickListener() { // from class: activity.IPCameraActivity.62.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        SharePreferenceManager.getInstance().setFirstFormatInIpc(IPCameraActivity.this.iotId, false);
                    }
                }).rightBtnText(IPCameraActivity.this.getString(R.string.format)).clickRight(new View.OnClickListener() { // from class: activity.IPCameraActivity.62.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        Intent intent = new Intent(IPCameraActivity.this, (Class<?>) StorageStatusActivity.class);
                        intent.putExtra("totalStorage", f);
                        intent.putExtra("remainStorage", f2);
                        intent.putExtra("storageStatusValues", i);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCameraActivity.this.device);
                        intent.putExtras(bundle);
                        IPCameraActivity.this.startActivity(intent);
                        IPCameraActivity.this.needTFInit = false;
                    }
                }).canCancel(false).create().show(IPCameraActivity.this.getSupportFragmentManager(), "");
            }
        });
    }

    private void getThingsStatus(String str) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/status/get").setScheme(Scheme.HTTPS).setApiVersion("1.0.4").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass63());
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$63, reason: invalid class name */
    class AnonymousClass63 implements IoTCallback {
        AnonymousClass63() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            exc.printStackTrace();
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
            Log.d(IPCameraActivity.this.TAG, "run: ---------------" + Thread.currentThread().getName());
            try {
                if (((org.json.JSONObject) ioTResponse.getData()).get("status").toString().equals("3")) {
                    if (!AppConfig.isChina) {
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.63.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.binding.ipcOfflineText.bringToFront();
                                IPCameraActivity.this.binding.ipcOfflineText.setVisibility(0);
                            }
                        });
                    } else if (IPCameraActivity.this.IccId == null || "".equals(IPCameraActivity.this.IccId)) {
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.63.3
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.binding.ipcOfflineText.bringToFront();
                                IPCameraActivity.this.binding.ipcOfflineText.setVisibility(0);
                            }
                        });
                    } else {
                        new OkHttpClient().newCall(new Request.Builder().url("http://www.secueye.cn:8000/api/smsApi?iccid=" + IPCameraActivity.this.IccId + "&method=smsStatusSecueye").get().build()).enqueue(new Callback() { // from class: activity.IPCameraActivity.63.2
                            static final /* synthetic */ boolean $assertionsDisabled = false;

                            @Override // okhttp3.Callback
                            public void onFailure(Call call, IOException iOException) {
                            }

                            @Override // okhttp3.Callback
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    JSONObject object = JSONObject.parseObject(response.body().string());
                                    if (object.containsKey("code")) {
                                        int iIntValue = object.getInteger("code").intValue();
                                        if (iIntValue == 400) {
                                            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.63.2.1
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCameraActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCameraActivity.this.binding.ipcOfflineText.setVisibility(0);
                                                    IPCameraActivity.this.isOtherCard = true;
                                                }
                                            });
                                            return;
                                        } else if (iIntValue != 200) {
                                            IPCameraActivity.this.showToast(IPCameraActivity.this.getString(R.string.exception_4g_data));
                                            return;
                                        }
                                    }
                                    if (!object.containsKey("values") || IPCameraActivity.this.isOtherCard) {
                                        return;
                                    }
                                    JSONObject jSONObject = object.getJSONObject("values");
                                    if (jSONObject.containsKey("status")) {
                                        if (!jSONObject.getString("status").equals("停机")) {
                                            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.63.2.4
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCameraActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCameraActivity.this.binding.ipcOfflineText.setVisibility(0);
                                                }
                                            });
                                        } else if (AppConfig.isChina) {
                                            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.63.2.2
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    if (!IPCameraActivity.this.isHorizontal) {
                                                        IPCameraActivity.this.binding.traffic4gExpired.bringToFront();
                                                        IPCameraActivity.this.binding.immediateRenewal.bringToFront();
                                                        IPCameraActivity.this.binding.outlineTime.bringToFront();
                                                        IPCameraActivity.this.binding.videoPlayIbtn.setVisibility(8);
                                                        IPCameraActivity.this.binding.ipcOfflineText.setVisibility(8);
                                                        IPCameraActivity.this.binding.traffic4gExpired.setVisibility(0);
                                                        IPCameraActivity.this.binding.immediateRenewal.setVisibility(0);
                                                        IPCameraActivity.this.binding.outlineTime.setVisibility(0);
                                                    }
                                                    try {
                                                        IPCameraActivity.this.binding.outlineTime.setText(((Object) IPCameraActivity.this.getResources().getText(R.string.time_of_off_line)) + "：" + TimeUtil.TimeStamp2Date(((org.json.JSONObject) ioTResponse.getData()).get("time").toString()));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    IPCameraActivity.this.needRecharge = true;
                                                }
                                            });
                                        } else {
                                            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.63.2.3
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCameraActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCameraActivity.this.binding.ipcOfflineText.setVisibility(0);
                                                }
                                            });
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        LivePlayer livePlayer = this.player;
        if (livePlayer != null) {
            livePlayer.release();
        }
        LiveIntercomV2 liveIntercomV2 = this.liveIntercom;
        if (liveIntercomV2 != null) {
            liveIntercomV2.release();
        }
        Timer timer = this.ptzTimer;
        if (timer != null) {
            timer.cancel();
            this.ptzTimer = null;
        }
        Handler handler = this.wakeUpHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.wakeUpHandler = null;
        }
        LiveIntercomV2 liveIntercomV22 = this.liveIntercom;
        if (liveIntercomV22 != null) {
            liveIntercomV22.release();
        }
        EventBus.getDefault().unregister(this);
        SharePreferenceManager.getInstance().unRegisterOnCallSetListener(this.definitionChangeListener);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        stopScreenLight();
        AutoSnap();
        LivePlayer livePlayer = this.player;
        if (livePlayer != null) {
            livePlayer.stop();
        }
        Timer timer = this.batteryTimer;
        if (timer != null) {
            timer.cancel();
            this.batteryTimer = null;
        }
        Timer timer2 = this.ptzTimer;
        if (timer2 != null) {
            timer2.cancel();
            this.ptzTimer = null;
        }
        stopInactivityTimer();
        LiveIntercomV2 liveIntercomV2 = this.liveIntercom;
        if (liveIntercomV2 != null) {
            liveIntercomV2.release();
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        Timer timer = this.batteryTimer;
        if (timer != null) {
            timer.cancel();
            this.batteryTimer = null;
        }
        Timer timer2 = this.ptzTimer;
        if (timer2 != null) {
            timer2.cancel();
            this.ptzTimer = null;
        }
        TouchView touchView = this.touchView;
        if (touchView != null) {
            touchView.resetView();
        }
        stopInactivityTimer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getProperties(MyCallback myCallback) {
        SettingsCtrl.getInstance().getProperties(this.iotId, myCallback);
    }

    @Override // fragment.MoreFragment.MyBackListener
    public void backOut() {
        getSupportFragmentManager().beginTransaction().hide(this.moreFragment).commitAllowingStateLoss();
        this.binding.f6229fragment.setClickable(false);
        this.isMoreFragmentShow = false;
        this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc);
        this.binding.moreText.setTextColor(getResources().getColor(R.color.colors_ipc_image_text));
    }

    @Override // fragment.MoreFragment.FragmentContextChangeListener
    public void lightMode() {
        this.nightModeFragment.showAllowingStateLoss(getSupportFragmentManager(), "", this.currentInfrarred);
    }

    @Override // fragment.MoreFragment.FragmentContextChangeListener
    public void humanoidTracking(TopicBean topicBean, RecyclerView.Adapter adapter2, int i, int i2, boolean z) {
        if (SharePreferenceManager.getInstance().getFaceDetectMode(this.iotId) == 0) {
            HashMap map = new HashMap();
            map.put(Constants.FACE_DETECT_SENSITIVITY, 2);
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCameraActivity.64
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z2, Object obj) {
                    if (!z2 || obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.64.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCameraActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setFaceDetectMode(IPCameraActivity.this.iotId, 2);
                        }
                    }
                }
            });
        }
        if (z) {
            checkSwitch(topicBean, adapter2, i, i2);
        } else {
            this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.65
                @Override // java.lang.Runnable
                public void run() {
                    IPCameraActivity.this.showProgressDialog();
                }
            });
            setMobileTracking(topicBean, adapter2, i, i2);
        }
        if (topicBean.isSelect()) {
            IPCManager.getInstance().getDevice(this.iotId).changePresetLocation(103, new IPanelCallback() { // from class: activity.IPCameraActivity.66
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z2, @Nullable Object obj) {
                    if (z2 && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        Log.e("预置位", "103");
                    }
                }
            });
        } else {
            IPCManager.getInstance().getDevice(this.iotId).addPresetLocation(99, new IPanelCallback() { // from class: activity.IPCameraActivity.67
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z2, @Nullable Object obj) {
                    if (z2 && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        Log.e("预置位", "99");
                    }
                }
            });
            IPCManager.getInstance().getDevice(this.iotId).changePresetLocation(100, new IPanelCallback() { // from class: activity.IPCameraActivity.68
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z2, @Nullable Object obj) {
                    if (z2 && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        Log.e("预置位", MessageService.MSG_DB_COMPLETE);
                    }
                }
            });
        }
    }

    @Override // fragment.MoreFragment.FragmentContextChangeListener
    public void fourPicture(TopicBean topicBean, RecyclerView.Adapter adapter2, int i) {
        Log.d(this.TAG, "onClick: -------------" + this.player.getPlayState());
        if (this.player.getPlayState() == 2 || this.player.getPlayState() == 1) {
            Log.d(this.TAG, "onClick: ");
            return;
        }
        if (!this.isFirst) {
            this.isFirst = true;
            this.isFour = true;
            this.player.stop();
            this.binding.fourPic.setData(this.iotIdList);
            this.binding.play.setVisibility(8);
            this.binding.fourPic.setVisibility(0);
            this.binding.llBottom.setVisibility(8);
            this.binding.fourOnePic.setImageDrawable(getResources().getDrawable(R.drawable.four_one));
            topicBean.setSelect(!topicBean.isSelect());
            if (topicBean.isSelect()) {
                topicBean.setIcon(R.drawable.four_pictures_ipc_light);
                this.moreFragment.hideBack();
                this.isFourState = true;
            } else {
                topicBean.setIcon(R.drawable.four_pictures_ipc);
                this.moreFragment.showBack();
                this.isFourState = false;
            }
            adapter2.notifyItemChanged(i, topicBean);
            return;
        }
        if (!this.isFour) {
            this.isFour = true;
            this.player.stop();
            this.binding.fourPic.startPlayer();
            this.binding.play.setVisibility(8);
            this.binding.fourPic.setVisibility(0);
            this.binding.llBottom.setVisibility(8);
            this.binding.fourOnePic.setImageDrawable(getResources().getDrawable(R.drawable.four_one));
            topicBean.setSelect(!topicBean.isSelect());
            if (topicBean.isSelect()) {
                topicBean.setIcon(R.drawable.four_pictures_ipc_light);
                this.moreFragment.hideBack();
                this.isFourState = true;
            } else {
                topicBean.setIcon(R.drawable.four_pictures_ipc);
                this.moreFragment.showBack();
                this.isFourState = false;
            }
            adapter2.notifyItemChanged(i, topicBean);
            return;
        }
        if (this.binding.fourPic.getStatus() == 3) {
            this.isFour = false;
            this.binding.fourPic.stop();
            this.binding.play.setVisibility(0);
            this.binding.fourPic.setVisibility(8);
            if (this.binding.fourPic.getDevice() == null) {
                return;
            }
            this.beanInfo = this.binding.fourPic.getDevice();
            DeviceInfoBean deviceInfoBean = this.beanInfo;
            this.device = deviceInfoBean;
            this.iotId = deviceInfoBean.getIotId();
            this.title = this.beanInfo.getName();
            this.isOwner = this.beanInfo.getOwned() == 1;
            this.DeviceName = this.beanInfo.getDeviceName();
            this.ProductKey = this.beanInfo.getProductKey();
            this.binding.llBottom.setVisibility(0);
            this.player.setIPCLiveDataSource(this.iotId, 0, false, 0, true, 0);
            this.player.prepare();
            this.binding.fourOnePic.setImageDrawable(getResources().getDrawable(R.drawable.four_ipc));
            topicBean.setSelect(!topicBean.isSelect());
            if (topicBean.isSelect()) {
                topicBean.setIcon(R.drawable.four_pictures_ipc_light);
                this.moreFragment.hideBack();
                this.isFourState = true;
            } else {
                topicBean.setIcon(R.drawable.four_pictures_ipc);
                this.moreFragment.showBack();
                this.isFourState = false;
            }
            adapter2.notifyItemChanged(i, topicBean);
            this.netVisible = false;
            this.smartDoorVisible = false;
            this.shopVisible = false;
            this.lightVisible = false;
            this.isDetecting = false;
            this.isOldPresetDevice = true;
            initLiveIntercom(this.iotId);
            getProperties(new AnonymousClass69());
            this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.70
                @Override // java.lang.Runnable
                public void run() {
                    IPCameraActivity.this.binding.tvTitle.setTitleText(IPCameraActivity.this.title);
                }
            });
        }
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$69, reason: invalid class name */
    class AnonymousClass69 implements MyCallback {
        AnonymousClass69() {
        }

        @Override // tools.MyCallback
        public void onComplete(boolean z) {
            if (SharePreferenceManager.getInstance().getMixZoom(IPCameraActivity.this.iotId) == 1) {
                IPCameraActivity.this.isMixZoom = true;
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.69.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.binding.tvOsd.setVisibility(8);
                    }
                });
            } else {
                IPCameraActivity.this.isMixZoom = false;
            }
            if (SharePreferenceManager.getInstance().getSupportZoom(IPCameraActivity.this.iotId) != 1) {
                IPCameraActivity.this.isOpticalZoom = false;
            } else {
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.69.2
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.binding.tvOsd.setVisibility(8);
                    }
                });
                IPCameraActivity.this.isOpticalZoom = true;
            }
            if (SharePreferenceManager.getInstance().getSensorViewDisplay(IPCameraActivity.this.iotId) == 0) {
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.69.3
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.binding.SensorView.setVisibility(8);
                    }
                });
            } else {
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.69.4
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.binding.SensorView.setVisibility(0);
                    }
                });
            }
            if (SharePreferenceManager.getInstance().getDisplayController(IPCameraActivity.this.iotId) == 0) {
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.69.5
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.binding.llController.setVisibility(8);
                    }
                });
            }
            if (((SharePreferenceManager.getInstance().getPageControlEx(IPCameraActivity.this.iotId) & 256) >> 8) == 1) {
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.69.6
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.binding.bottomShop.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.69.6.1
                            @Override // android.view.View.OnClickListener
                            public void onClick(View view2) {
                                IPCameraActivity.this.openH5(SharePreferenceManager.getInstance().getUserMallUrl(IPCameraActivity.this.iotId));
                            }
                        });
                    }
                });
            }
            IPCameraActivity.this.faceDetectionAbility = SharePreferenceManager.getInstance().getHumanoidTracking(IPCameraActivity.this.iotId).intValue();
            IPCameraActivity.this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(IPCameraActivity.this.iotId);
            if (IPCameraActivity.this.faceDetectionAbility == 1) {
                IPCameraActivity.this.isDetecting = SharePreferenceManager.getInstance().getHumanoidTrackingEnable(IPCameraActivity.this.iotId).intValue() == 1;
            } else {
                IPCameraActivity.this.isDetecting = SharePreferenceManager.getInstance().getIntelligentMode(IPCameraActivity.this.iotId) == 1;
            }
        }
    }

    @Override // fragment.MoreFragment.FragmentContextChangeListener
    public void mall() {
        openH5(SharePreferenceManager.getInstance().getUserMallUrl(this.iotId));
    }

    @Override // fragment.MoreFragment.FragmentContextChangeListener
    public void netWorkSwitch() {
        this.switch4gFragment.showAllowingStateLoss(getSupportFragmentManager(), "", this.wifiFourPosition);
    }

    @Override // fragment.MoreFragment.FragmentContextChangeListener
    public void preset() {
        FragmentTransaction fragmentTransactionBeginTransaction = getSupportFragmentManager().beginTransaction();
        if (this.isOldPresetDevice) {
            fragmentTransactionBeginTransaction.hide(this.controllerFragment).hide(this.moreFragment).show(this.oldPresetFragment);
            fragmentTransactionBeginTransaction.commitAllowingStateLoss();
            this.isMoreFragmentShow = true;
            this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc_light);
            this.binding.moreText.setTextColor(getResources().getColor(R.color.colorAccent));
            return;
        }
        fragmentTransactionBeginTransaction.hide(this.controllerFragment).hide(this.moreFragment).show(this.presetFragment);
        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
        this.isMoreFragmentShow = true;
        this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc_light);
        this.binding.moreText.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @Override // fragment.MoreFragment.FragmentContextChangeListener
    public void smart() {
        FragmentTransaction fragmentTransactionBeginTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransactionBeginTransaction.show(this.controllerFragment).hide(this.moreFragment);
        fragmentTransactionBeginTransaction.commit();
        getControllerList();
        this.controllerFragment.setList(this.controllerList);
    }

    @Override // fragment.MoreFragment.FragmentContextChangeListener
    public void Floodlight() {
        HashMap map = new HashMap();
        map.put(Constants.FloodlightSwitch, Integer.valueOf(SharePreferenceManager.getInstance().getFloodlightSwitch(this.iotId) == 1 ? 0 : 1));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCameraActivity.71
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (z) {
                    new Handler().post(new Runnable() { // from class: activity.IPCameraActivity.71.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (SharePreferenceManager.getInstance().getFloodlightSwitch(IPCameraActivity.this.iotId) == 0) {
                                SharePreferenceManager.getInstance().setFloodlightSwitch(IPCameraActivity.this.iotId, 1);
                            } else {
                                SharePreferenceManager.getInstance().setFloodlightSwitch(IPCameraActivity.this.iotId, 0);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override // fragment.MoreFragment.FragmentContextChangeListener
    public void FaceRecognition() {
        getSupportFragmentManager().beginTransaction().hide(this.moreFragment).commitAllowingStateLoss();
        this.binding.f6229fragment.setClickable(false);
        this.binding.rlcenter.setVisibility(8);
        this.binding.layoutFaceRecognition.setVisibility(0);
        this.binding.tvEnter.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.72
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).setFaceRecognitionServer(1, "", new IPanelCallback() { // from class: activity.IPCameraActivity.72.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                    }
                });
            }
        });
        this.binding.tvEnter1.setOnClickListener(new AnonymousClass73());
        this.binding.tvStopEnter.setOnClickListener(new AnonymousClass74());
        this.binding.tvDelete.setOnClickListener(new AnonymousClass75());
        this.binding.tvDeleteAll.setOnClickListener(new AnonymousClass76());
        this.binding.tvEnterModel.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.77
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.binding.layoutFaceQuery.setVisibility(8);
                IPCameraActivity.this.binding.layoutFaceModel.setVisibility(0);
            }
        });
        this.binding.tvQuery.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.78
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.binding.layoutFaceModel.setVisibility(8);
                IPCameraActivity.this.binding.layoutFaceQuery.setVisibility(0);
                IPCameraActivity.this.getFaceList();
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$73, reason: invalid class name */
    class AnonymousClass73 extends OnMultiClickListener {
        AnonymousClass73() {
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            IPCameraActivity.this.uiHandler.postDelayed(new Runnable() { // from class: activity.IPCameraActivity.73.1
                @Override // java.lang.Runnable
                public void run() {
                    if (!IPCameraActivity.this.binding.etEnter.getText().toString().isEmpty()) {
                        IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).setFaceRecognitionServer(2, IPCameraActivity.this.binding.etEnter.getText().toString(), new IPanelCallback() { // from class: activity.IPCameraActivity.73.1.1
                            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                            public void onComplete(boolean z, @Nullable Object obj) {
                                if (z) {
                                    IPCameraActivity.this.showToast("开始录入");
                                }
                            }
                        });
                    } else {
                        IPCameraActivity.this.showToast("请输入人名");
                    }
                }
            }, 500L);
        }
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$74, reason: invalid class name */
    class AnonymousClass74 extends OnMultiClickListener {
        AnonymousClass74() {
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).setFaceRecognitionServer(0, "", new IPanelCallback() { // from class: activity.IPCameraActivity.74.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z) {
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.74.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.showToast("停止录入");
                            }
                        });
                    }
                }
            });
        }
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$75, reason: invalid class name */
    class AnonymousClass75 extends OnMultiClickListener {
        AnonymousClass75() {
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).setFaceRecognitionServer(3, IPCameraActivity.this.binding.etEnter.getText().toString(), new IPanelCallback() { // from class: activity.IPCameraActivity.75.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z) {
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.75.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.showToast("删除成功");
                            }
                        });
                    }
                }
            });
        }
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$76, reason: invalid class name */
    class AnonymousClass76 extends OnMultiClickListener {
        AnonymousClass76() {
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).setFaceRecognitionServer(4, "", new IPanelCallback() { // from class: activity.IPCameraActivity.76.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z) {
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.76.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.showToast("删除全部成功");
                            }
                        });
                    }
                }
            });
        }
    }

    @Override // fragment.MoreFragment.FragmentContextChangeListener
    public void GPSLocate() {
        IPCManager.getInstance().getDevice(this.iotId).getGPSPositioningService(new IPanelCallback() { // from class: activity.IPCameraActivity.79
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable final Object obj) {
                if (z) {
                    if (obj != null && !"".equals(String.valueOf(obj))) {
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.79.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Log.e("基站定位信息", String.valueOf(obj));
                                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                if (object.getInteger("code").intValue() != 200) {
                                    IPCameraActivity.this.showToast(IPCameraActivity.this.getString(R.string.play_failed_retry));
                                    return;
                                }
                                if (!String.valueOf(obj).contains("Latitude")) {
                                    IPCameraActivity.this.showToast(IPCameraActivity.this.getString(R.string.play_failed_retry));
                                    return;
                                }
                                JSONObject jSONObject = object.getJSONObject("data");
                                String string = jSONObject.getString("Latitude");
                                String string2 = jSONObject.getString("Longitude");
                                MapUtils.dddmmToDecimal(Double.parseDouble(string));
                                IPCameraActivity.this.lat = MapUtils.dddmmToDecimal(Double.parseDouble(string)) + "";
                                IPCameraActivity.this.lon = MapUtils.dddmmToDecimal(Double.parseDouble(string2)) + "";
                                IPCameraActivity.this.mapFragment.showAllowingStateLoss(IPCameraActivity.this.getSupportFragmentManager(), "");
                            }
                        });
                        return;
                    } else {
                        IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                        iPCameraActivity.showToast(iPCameraActivity.getString(R.string.play_failed_retry));
                        return;
                    }
                }
                IPCameraActivity iPCameraActivity2 = IPCameraActivity.this;
                iPCameraActivity2.showToast(iPCameraActivity2.getString(R.string.play_failed_retry));
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$80, reason: invalid class name */
    class AnonymousClass80 implements IPanelCallback {
        AnonymousClass80() {
        }

        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
        public void onComplete(boolean z, @Nullable final Object obj) {
            if (z) {
                if (obj != null && !"".equals(String.valueOf(obj))) {
                    IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.80.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Log.e("基站定位信息", String.valueOf(obj));
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.getInteger("code").intValue() != 200) {
                                IPCameraActivity.this.showToast(IPCameraActivity.this.getString(R.string.play_failed_retry));
                                return;
                            }
                            if (!String.valueOf(obj).contains("CellIdentity")) {
                                IPCameraActivity.this.showToast(IPCameraActivity.this.getString(R.string.play_failed_retry));
                                return;
                            }
                            JSONObject jSONObject = object.getJSONObject("data");
                            String string = jSONObject.getString("CellIdentity");
                            jSONObject.getInteger("MobileNetworkCode").intValue();
                            String string2 = jSONObject.getString("TrackingAreaCode");
                            int i = Integer.parseInt(string, 16);
                            int i2 = Integer.parseInt(string2, 16);
                            OkHttpClient okHttpClient = new OkHttpClient();
                            String str = "http://api.cellocation.com:84/cell/?mcc=460&mnc=1&lac=" + i2 + "&ci=" + i + "&output=json";
                            Log.e("基站查询定位", "" + str);
                            okHttpClient.newCall(new Request.Builder().url(str).get().build()).enqueue(new Callback() { // from class: activity.IPCameraActivity.80.1.1
                                static final /* synthetic */ boolean $assertionsDisabled = false;

                                @Override // okhttp3.Callback
                                public void onFailure(Call call, IOException iOException) {
                                    Looper.prepare();
                                    IPCameraActivity.this.showToast(IPCameraActivity.this.getString(R.string.play_failed_retry));
                                    Looper.loop();
                                }

                                @Override // okhttp3.Callback
                                public void onResponse(Call call, Response response) throws IOException {
                                    try {
                                        JSONObject object2 = JSONObject.parseObject(response.body().string());
                                        if (object2.getInteger("errcode").intValue() != 0) {
                                            IPCameraActivity.this.showToast(IPCameraActivity.this.getString(R.string.play_failed_retry));
                                            return;
                                        }
                                        IPCameraActivity.this.lat = object2.getString(DispatchConstants.LATITUDE);
                                        IPCameraActivity.this.lon = object2.getString("lon");
                                        object2.getString("radius");
                                        IPCameraActivity.this.address = object2.getString("address");
                                        IPCameraActivity.this.mapFragment.showAllowingStateLoss(IPCameraActivity.this.getSupportFragmentManager(), "");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                    return;
                } else {
                    IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                    iPCameraActivity.showToast(iPCameraActivity.getString(R.string.play_failed_retry));
                    return;
                }
            }
            IPCameraActivity iPCameraActivity2 = IPCameraActivity.this;
            iPCameraActivity2.showToast(iPCameraActivity2.getString(R.string.play_failed_retry));
        }
    }

    @Override // fragment.MoreFragment.FragmentContextChangeListener
    public void LBSLocate() {
        IPCManager.getInstance().getDevice(this.iotId).getLocationBasedService(new AnonymousClass80());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getFaceList() {
        IPCManager.getInstance().getDevice(this.iotId).getFaceDataBasesStatus(new IPanelCallback() { // from class: activity.IPCameraActivity.81
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code") && object.containsKey("data")) {
                    try {
                        JSONObject jSONObject = object.getJSONObject("data");
                        final int iIntValue = jSONObject.getInteger("FaceDataBasesTotal").intValue();
                        final int iIntValue2 = jSONObject.getInteger("FaceDataBasesUsed").intValue();
                        final int iIntValue3 = jSONObject.getInteger("FaceDataBasesRemain").intValue();
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.81.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.binding.tvFaceData.setText("容量：" + iIntValue + "  已录入：" + iIntValue2 + "  还可录入: " + iIntValue3);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        IPCManager.getInstance().getDevice(this.iotId).getFaceDataBasesQuery(new AnonymousClass82());
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$82, reason: invalid class name */
    class AnonymousClass82 implements IPanelCallback {
        AnonymousClass82() {
        }

        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
        public void onComplete(boolean z, @Nullable Object obj) {
            if (!z || obj == null || "".equals(String.valueOf(obj))) {
                return;
            }
            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
            if (object.containsKey("code") && object.containsKey("data")) {
                try {
                    String[] strArrSplit = object.getJSONObject("data").getString("FaceDateText").split(";");
                    ArrayList arrayList = new ArrayList();
                    for (String str : strArrSplit) {
                        arrayList.add(str);
                    }
                    IPCameraActivity.this.uiHandler.post(new AnonymousClass1(arrayList));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /* JADX INFO: renamed from: activity.IPCameraActivity$82$1, reason: invalid class name */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ List val$list;

            AnonymousClass1(List list) {
                this.val$list = list;
            }

            @Override // java.lang.Runnable
            public void run() {
                IPCameraActivity.this.binding.rvFace.setLayoutManager(new LinearLayoutManager(IPCameraActivity.this));
                FaceAdapter faceAdapter = new FaceAdapter(IPCameraActivity.this, this.val$list);
                IPCameraActivity.this.binding.rvFace.setAdapter(faceAdapter);
                faceAdapter.setOnItemClickListener(new FaceAdapter.OnItemClickListener() { // from class: activity.IPCameraActivity.82.1.1
                    @Override // adapter.FaceAdapter.OnItemClickListener
                    public void onItemClick(int i, String str) {
                        Log.e("删除", str);
                        final String str2 = str.split("\\.")[r3.length - 1];
                        IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).setFaceRecognitionServer(3, str2, new IPanelCallback() { // from class: activity.IPCameraActivity.82.1.1.1
                            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                            public void onComplete(boolean z, @Nullable Object obj) {
                                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                                    return;
                                }
                                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                if (object.containsKey("code") && object.getInteger("code").intValue() == 200) {
                                    IPCameraActivity.this.showToast("删除 " + str2 + "成功");
                                    IPCameraActivity.this.getFaceList();
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    @Override // fragment.PresetFragment.PresetBackListener
    public void presetBack() {
        this.binding.deleteLl.setVisibility(8);
        this.binding.llBottom.setVisibility(0);
        getSupportFragmentManager().beginTransaction().hide(this.presetFragment).show(this.moreFragment).commitAllowingStateLoss();
        this.binding.f6229fragment.setClickable(false);
        this.isMoreFragmentShow = true;
    }

    @Override // fragment.PresetFragment.PresetDataChange
    public void snapPicture(PresetBean presetBean, RecyclerView.Adapter adapter2, int i) {
        Bitmap bitmapSnapShot;
        Bitmap bitmapCreateScaledBitmap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Permission.WRITE_EXTERNAL_STORAGE) != 0 || this.player.getPlayState() != 3 || (bitmapSnapShot = this.player.snapShot()) == null || bitmapSnapShot == null || (bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmapSnapShot, 2880, 1620, true)) == null) {
            return;
        }
        saveBitmap(bitmapCreateScaledBitmap, i, presetBean, adapter2);
    }

    @Override // fragment.PresetFragment.PresetDataChange
    public void deletePicture(PresetBean presetBean, RecyclerView.Adapter adapter2, int i) {
        deleteBitmap(i, presetBean, adapter2);
    }

    @Override // fragment.PresetFragment.PresetDataChange
    public void outDelete() {
        this.binding.llBottom.setVisibility(0);
        this.binding.deleteLl.setVisibility(8);
    }

    @Override // fragment.OldPresetFragment.OldPresetBackListener
    public void oldPresetBack() {
        getSupportFragmentManager().beginTransaction().hide(this.oldPresetFragment).show(this.moreFragment).commitAllowingStateLoss();
        this.binding.f6229fragment.setClickable(false);
        this.isMoreFragmentShow = true;
    }

    private void initPlayer() {
        this.player = new LivePlayer(getApplicationContext());
        this.player.setTextureView(this.binding.play);
        this.binding.play.setClickable(true);
        this.player.setVolume(this.speakerSwitch ? 1.0f : 0.0f);
        this.player.setVideoScalingMode(1);
        this.binding.play.setOnZoomableTextureListener(new ZoomableTextureView.OnZoomableTextureListener() { // from class: activity.IPCameraActivity.83
            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onDoubleTap(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                return true;
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public void onLongPress(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            @SuppressLint({"SetTextI18n"})
            public void onScaleChanged(ZoomableTextureView zoomableTextureView, float f) {
                String str;
                double dDoubleValue = new BigDecimal(f).setScale(2, 4).doubleValue();
                DecimalFormat decimalFormat = new DecimalFormat("##0");
                Log.d(IPCameraActivity.this.TAG, "onScaleChanged: " + dDoubleValue);
                if (IPCameraActivity.this.isMixZoom) {
                    str = decimalFormat.format((((dDoubleValue - 1.0d) * 2.25d) + 1.0d) * ((double) IPCameraActivity.this.ZoomMax));
                } else {
                    str = decimalFormat.format(((dDoubleValue - 1.0d) * 2.25d) + 1.0d);
                }
                IPCameraActivity.this.zoom.postValue(Float.valueOf(f));
                IPCameraActivity.this.binding.tvOsd.setText(str + "X");
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onSingleTapConfirmed(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                IPCameraActivity.this.isFloat = !r2.isFloat;
                IPCameraActivity.this.setFloatBarState();
                return true;
            }
        });
        this.player.setOnErrorListener(new AnonymousClass84());
        this.player.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCameraActivity.85
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                switch (i) {
                    case 1:
                        LogEx.i(true, IPCameraActivity.this.TAG, "STATE_IDLE");
                        break;
                    case 2:
                        IPCameraActivity.this.dismissPlayButton();
                        IPCameraActivity.this.showBuffering();
                        if (IPCameraActivity.this.needWakeUp) {
                            IPCameraActivity.this.binding.wakeupText.setVisibility(8);
                        } else {
                            IPCameraActivity.this.binding.wakeupText.setVisibility(8);
                        }
                        LogEx.i(true, IPCameraActivity.this.TAG, "STATE_BUFFERING");
                        break;
                    case 3:
                        IPCameraActivity.this.lowPowerMode = 1;
                        IPCameraActivity.this.needWakeUp = false;
                        IPCameraActivity.this.needWakeUpSuccess = true;
                        IPCameraActivity.this.is1100ErrorPre = 10;
                        IPCameraActivity.this.dismissSnapPicture();
                        IPCameraActivity.this.dismissBuffering();
                        IPCameraActivity.this.binding.wakeupText.setVisibility(8);
                        LogEx.i(true, IPCameraActivity.this.TAG, "STATE_READY");
                        IPCameraActivity.this.isFirstShowStreamType = true;
                        IPCameraActivity.this.showPlayInfo();
                        break;
                    case 4:
                        LogEx.i(true, IPCameraActivity.this.TAG, "STATE_ENDED");
                        IPCameraActivity.this.dismissPlayInfo();
                        IPCameraActivity.this.needWakeUpSuccess = false;
                        IPCameraActivity.this.player.stopRecordingContent();
                        IPCameraActivity.this.showPlayButton();
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$84, reason: invalid class name */
    class AnonymousClass84 implements OnErrorListener {
        AnonymousClass84() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
        public void onError(PlayerException playerException) {
            if (playerException.getSubCode() != 1009 || playerException.getCode() != 6 || !playerException.getLocalizedMessage().equals("请求认证错误")) {
                if (!IPCameraActivity.this.needWakeUp || IPCameraActivity.this.countWakeUp >= 5) {
                    switch (playerException.getCode()) {
                        case 6:
                            switch (playerException.getSubCode()) {
                                case 1005:
                                    IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                                    iPCameraActivity.showToast(iPCameraActivity.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1006:
                                    IPCameraActivity iPCameraActivity2 = IPCameraActivity.this;
                                    iPCameraActivity2.showToast(iPCameraActivity2.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1007:
                                    IPCameraActivity iPCameraActivity3 = IPCameraActivity.this;
                                    iPCameraActivity3.showToast(iPCameraActivity3.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1008:
                                    IPCameraActivity iPCameraActivity4 = IPCameraActivity.this;
                                    iPCameraActivity4.showToast(iPCameraActivity4.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1009:
                                    IPCameraActivity iPCameraActivity5 = IPCameraActivity.this;
                                    iPCameraActivity5.showToast(iPCameraActivity5.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                            }
                            break;
                        case 7:
                            if (playerException.getSubCode() == 1000) {
                                IPCameraActivity iPCameraActivity6 = IPCameraActivity.this;
                                iPCameraActivity6.showToast(iPCameraActivity6.getString(R.string.play_failed_retry, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                            }
                            break;
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCameraActivity.this.is1100ErrorPre <= 0) {
                                    if (SharePreferenceManager.getInstance().getDoubleNetWork(IPCameraActivity.this.iotId) == 1 && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCameraActivity.this.iotId) == 1) {
                                        IPCameraActivity.this.showBadNetDialog();
                                    }
                                } else {
                                    IPCameraActivity.access$8710(IPCameraActivity.this);
                                    IPCameraActivity.this.defaultDefinition = SharePreferenceManager.getInstance().getStreamVideoQuality(IPCameraActivity.this.iotId);
                                    int unused = IPCameraActivity.this.defaultDefinition;
                                    IPCameraActivity.this.player.stop();
                                    if (SharePreferenceManager.getInstance().getNetState(IPCameraActivity.this.device.getIotId()) != 3) {
                                        Handler handler = IPCameraActivity.this.uiHandler;
                                        final IPCameraActivity iPCameraActivity7 = IPCameraActivity.this;
                                        handler.postDelayed(new Runnable() { // from class: activity.-$$Lambda$IPCameraActivity$84$Ul3qVFE3_bZBku8WUUEPi5NN22k
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                iPCameraActivity7.playLive();
                                            }
                                        }, 500L);
                                        return;
                                    }
                                    return;
                                }
                            }
                            break;
                    }
                    if (IPCameraActivity.this.needRecharge) {
                        return;
                    }
                    IPCameraActivity.this.showPlayButton();
                    return;
                }
                return;
            }
            IPCameraActivity iPCameraActivity8 = IPCameraActivity.this;
            iPCameraActivity8.showToast(iPCameraActivity8.getString(R.string.account_squeezed));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initLiveIntercom(String str) {
        this.liveIntercom = new LiveIntercomV2(this, str, LiveIntercomV2.LiveIntercomMode.SingleTalk, AudioParams.AUDIOPARAM_MONO_8K_G711A);
        this.liveIntercom.setGainLevel(-1);
        this.liveIntercom.setLiveIntercomV2Listener(new LiveIntercomV2Listener() { // from class: activity.IPCameraActivity.86
            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordBufferReceived(byte[] bArr, int i, int i2) {
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onTalkReady() {
                LogEx.e(true, "speaker----", "1 " + IPCameraActivity.this.isLiveIntercoming);
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.86.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.showToast(IPCameraActivity.this.getResources().getString(R.string.can_begin_talk));
                        if (IPCameraActivity.this.isFinishing()) {
                            return;
                        }
                        IPCameraActivity.this.whiteProgressDialog.dismiss();
                        IPCameraActivity.this.setSpeakerBtn(2);
                        IPCameraActivity.this.binding.speakerBtn.setEnabled(true);
                    }
                });
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onError(LiveIntercomException liveIntercomException) {
                LogEx.e(true, "speaker----", "2 " + IPCameraActivity.this.isLiveIntercoming);
                int code = liveIntercomException.getCode();
                if (code != 16) {
                    switch (code) {
                        case 1:
                            IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                            iPCameraActivity.showToast(iPCameraActivity.getString(R.string.record_error1));
                            IPCameraActivity.this.handleLiveIntercomError();
                            break;
                        case 2:
                            IPCameraActivity iPCameraActivity2 = IPCameraActivity.this;
                            iPCameraActivity2.showToast(iPCameraActivity2.getString(R.string.record_error2));
                            IPCameraActivity.this.handleLiveIntercomError();
                            break;
                        case 3:
                            IPCameraActivity iPCameraActivity3 = IPCameraActivity.this;
                            iPCameraActivity3.showToast(iPCameraActivity3.getString(R.string.record_error3));
                            IPCameraActivity.this.handleLiveIntercomError();
                            break;
                        default:
                            switch (code) {
                                case 5:
                                    IPCameraActivity iPCameraActivity4 = IPCameraActivity.this;
                                    iPCameraActivity4.showToast(iPCameraActivity4.getString(R.string.record_error4));
                                    IPCameraActivity.this.handleLiveIntercomError();
                                    break;
                                case 6:
                                    IPCameraActivity iPCameraActivity5 = IPCameraActivity.this;
                                    iPCameraActivity5.showToast(iPCameraActivity5.getString(R.string.record_error5));
                                    IPCameraActivity.this.handleLiveIntercomError();
                                    break;
                                case 7:
                                    IPCameraActivity iPCameraActivity6 = IPCameraActivity.this;
                                    iPCameraActivity6.showToast(iPCameraActivity6.getString(R.string.record_error6));
                                    IPCameraActivity.this.onRecordError();
                                    break;
                                case 8:
                                    IPCameraActivity iPCameraActivity7 = IPCameraActivity.this;
                                    iPCameraActivity7.showToast(iPCameraActivity7.getString(R.string.record_error7));
                                    IPCameraActivity.this.onRecordError();
                                    break;
                                case 9:
                                    IPCameraActivity iPCameraActivity8 = IPCameraActivity.this;
                                    iPCameraActivity8.showToast(iPCameraActivity8.getString(R.string.record_error8));
                                    IPCameraActivity.this.onRecordError();
                                    break;
                            }
                            break;
                    }
                } else {
                    IPCameraActivity iPCameraActivity9 = IPCameraActivity.this;
                    iPCameraActivity9.showToast(iPCameraActivity9.getString(R.string.record_error9));
                    IPCameraActivity.this.onRecordError();
                }
                liveIntercomException.printStackTrace();
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordStart() {
                LogEx.d(true, IPCameraActivity.this.TAG, "onRecordStart");
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.86.2
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.isLiveIntercoming = true;
                    }
                });
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordEnd() {
                LogEx.d(true, IPCameraActivity.this.TAG, "onRecordEnd");
                if (IPCameraActivity.this.liveIntercom != null) {
                    IPCameraActivity.this.liveIntercom.stop();
                }
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.86.3
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.isLiveIntercoming = false;
                        if (IPCameraActivity.this.isFinishing()) {
                            return;
                        }
                        IPCameraActivity.this.whiteProgressDialog.dismiss();
                        IPCameraActivity.this.setSpeakerBtn(0);
                        IPCameraActivity.this.binding.speakerBtn.setEnabled(true);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playLive() {
        if (isFinishing()) {
            return;
        }
        if (this.player.getPlayState() != 3) {
            this.binding.play.reset();
            showSnapPicture();
        }
        LogEx.i(true, this.TAG, "playLive");
        LivePlayer livePlayer = this.player;
        if (livePlayer != null) {
            livePlayer.stop();
        }
        this.player.setIPCLiveDataSource(this.iotId, 0, false, 0, true, 0);
        this.player.setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCameraActivity.87
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                IPCameraActivity.this.player.start();
            }
        });
        this.player.prepare();
        keepScreenLight();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wakeUpDevice() {
        if (this.needWakeUpSuccess || this.player.getPlayState() == 3 || !isActivityForeground()) {
            return;
        }
        this.countWakeUp = 0;
        HashMap map = new HashMap();
        map.put(Constants.LowPowerWakeUp, 1);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCameraActivity.88
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
            }
        });
        HashMap map2 = new HashMap();
        map2.put(Constants.LowPowerAppStatus, 1);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map2, new AnonymousClass89());
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$89, reason: invalid class name */
    class AnonymousClass89 implements IPanelCallback {
        AnonymousClass89() {
        }

        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
        public void onComplete(boolean z, @Nullable Object obj) {
            IPCameraActivity.this.needWakeUp = true;
            if (!IPCameraActivity.this.needWakeUpSuccess) {
                Handler handler = IPCameraActivity.this.uiHandler;
                final IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                handler.post(new Runnable() { // from class: activity.-$$Lambda$IPCameraActivity$89$VXiiX82DjigG-vYv8Vlxcyxo21I
                    @Override // java.lang.Runnable
                    public final void run() {
                        iPCameraActivity.playLive();
                    }
                });
            }
            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.89.1
                @Override // java.lang.Runnable
                public void run() {
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeDefinition(final int i) {
        if (i < 0 || i > 3) {
            return;
        }
        HashMap map = new HashMap();
        map.put(Constants.STREAM_VIDEO_QUALITY_MODEL_NAME, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCameraActivity.90
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() == 200) {
                            SharePreferenceManager.getInstance().setStreamVideoQuality(IPCameraActivity.this.iotId, i);
                        } else {
                            IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.90.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCameraActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeDefinitionView(int i) {
        switch (i) {
            case 0:
                this.binding.qualityBtn1.setVisibility(8);
                this.binding.qualityBtn2.setVisibility(8);
                this.binding.qualityBtn3.setVisibility(0);
                break;
            case 1:
                this.binding.qualityBtn1.setVisibility(8);
                this.binding.qualityBtn2.setVisibility(0);
                this.binding.qualityBtn3.setVisibility(8);
                break;
            case 2:
                this.binding.qualityBtn1.setVisibility(0);
                this.binding.qualityBtn2.setVisibility(8);
                this.binding.qualityBtn3.setVisibility(8);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeLightDlgView(int i) {
        switch (i) {
            case 0:
                this.binding.tvLight1.setTextColor(getResources().getColor(R.color.color_2999ff));
                this.binding.tvLight1.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.binding.tvLight2.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvLight2.setBackground(null);
                this.binding.tvLight3.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvLight3.setBackground(null);
                break;
            case 1:
                this.binding.tvLight1.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvLight1.setBackground(null);
                this.binding.tvLight2.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.binding.tvLight2.setTextColor(getResources().getColor(R.color.color_2999ff));
                this.binding.tvLight3.setBackground(null);
                this.binding.tvLight3.setTextColor(getResources().getColor(R.color.color_white));
                break;
            case 2:
                this.binding.tvLight1.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvLight1.setBackground(null);
                this.binding.tvLight2.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvLight2.setBackground(null);
                this.binding.tvLight3.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.binding.tvLight3.setTextColor(getResources().getColor(R.color.color_2999ff));
                break;
        }
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$91, reason: invalid class name */
    class AnonymousClass91 implements SharePreferenceManager.OnCallSetListener {
        AnonymousClass91() {
        }

        /* JADX INFO: renamed from: activity.IPCameraActivity$91$1, reason: invalid class name */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ String val$iotId;
            final /* synthetic */ String val$key;

            AnonymousClass1(String str, String str2) {
                this.val$key = str;
                this.val$iotId = str2;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (this.val$key.equals(IPCameraActivity.this.getString(R.string.stream_video_quality_key))) {
                    IPCameraActivity.this.defaultDefinition = SharePreferenceManager.getInstance().getStreamVideoQuality(this.val$iotId);
                    IPCameraActivity.this.changeDefinitionView(IPCameraActivity.this.defaultDefinition);
                } else if (this.val$key.equals(IPCameraActivity.this.getString(R.string.image_flip_status_key))) {
                    IPCameraActivity.this.rotationOrientate = SharePreferenceManager.getInstance().getImageFlip(this.val$iotId);
                } else if (this.val$key.equals(IPCameraActivity.this.getString(R.string.day_night_mode_key))) {
                    IPCameraActivity.this.currentInfrarred = SharePreferenceManager.getInstance().getDayNightMode(this.val$iotId);
                    IPCameraActivity.this.changeLightDlgView(IPCameraActivity.this.currentInfrarred);
                    IPCameraActivity.this.lightVisible = true;
                } else if (this.val$key.equals(IPCameraActivity.this.getString(R.string.double_net))) {
                    IPCameraActivity.this.switchText = IPCameraActivity.this.switch_4gArr[SharePreferenceManager.getInstance().getNet4GEnableSwitch(this.val$iotId)];
                    IPCameraActivity.this.wifiFourPosition = SharePreferenceManager.getInstance().getNet4GEnableSwitch(this.val$iotId);
                    IPCameraActivity.this.WifiConfigIsExist = SharePreferenceManager.getInstance().getWifiConfigIsExist(this.val$iotId);
                    IPCameraActivity.this.netVisible = SharePreferenceManager.getInstance().getDoubleNetWork(this.val$iotId) == 1;
                } else if (!this.val$key.equals(IPCameraActivity.this.getString(R.string.support_zoom_key)) && !this.val$key.equals(IPCameraActivity.this.getString(R.string.support_focus_key)) && !this.val$key.equals(IPCameraActivity.this.getString(R.string.support_preset_key))) {
                    if (this.val$key.equals(IPCameraActivity.this.getString(R.string.support_4g_key))) {
                        if (!AppConfig.isChina) {
                            IPCameraActivity.this.binding.ivCharge4gFlow.setImageResource(R.drawable.selector_server_en);
                        }
                        IPCameraActivity.this.binding.ivCharge4gFlow.setSelected(SharePreferenceManager.getInstance().getSupport4G(IPCameraActivity.this.device.getIotId()) == 1);
                        if (SharePreferenceManager.getInstance().getSupport4G(IPCameraActivity.this.device.getIotId()) == 0 && !AppConfig.isChina) {
                            IPCameraActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                        }
                    } else if (this.val$key.equals(IPCameraActivity.this.getString(R.string.iccid_key))) {
                        if (IPCameraActivity.this.device != null && IPCameraActivity.this.device.getIotId() != null) {
                            IPCameraActivity.this.IccId = SharePreferenceManager.getInstance().getIccId(IPCameraActivity.this.device.getIotId());
                        }
                    } else if (this.val$key.equals(IPCameraActivity.this.getString(R.string.PageControlEx))) {
                        IPCameraActivity.this.shopVisible = ((SharePreferenceManager.getInstance().getPageControlEx(this.val$iotId) & 256) >> 8) == 1;
                    } else if (this.val$key.equals(IPCameraActivity.this.getString(R.string.new_support_preset_key))) {
                        IPCameraActivity.this.isOldPresetDevice = SharePreferenceManager.getInstance().getNewSupportPreset(this.val$iotId) != 1;
                        IPCManager.getInstance().getDevice(this.val$iotId).QueryPresetMap(new IPanelCallback() { // from class: activity.IPCameraActivity.91.1.1
                            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                            public void onComplete(boolean z, Object obj) {
                                if (z) {
                                    try {
                                        JSONArray jSONArray = JSONObject.parseObject(String.valueOf(obj)).getJSONObject("data").getJSONArray("PresetList");
                                        if (IPCameraActivity.this.presetList != null) {
                                            IPCameraActivity.this.presetList = null;
                                            IPCameraActivity.this.presetList = new ArrayList();
                                        } else {
                                            IPCameraActivity.this.presetList = new ArrayList();
                                        }
                                        for (int i = 0; i < jSONArray.size(); i++) {
                                            JSONObject jSONObject = jSONArray.getJSONObject(i);
                                            if (jSONObject.containsKey("Number")) {
                                                IPCameraActivity.this.presetList.add(Integer.valueOf(jSONObject.getIntValue("Number")));
                                            }
                                        }
                                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.91.1.1.1
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                IPCameraActivity.this.presetFragment.update(IPCameraActivity.this.presetList);
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } else if (this.val$key.equals(IPCameraActivity.this.getString(R.string.DisplayController))) {
                        IPCameraActivity.this.smartDoorVisible = SharePreferenceManager.getInstance().getDisplayController(this.val$iotId) == 1;
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.91.1.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (IPCameraActivity.this.smartDoorVisible) {
                                    if (!IPCameraActivity.this.isShowPtz) {
                                        if (IPCameraActivity.this.binding.tabLayout.getTabCount() <= 1) {
                                            IPCameraActivity.this.binding.tabLayout.addTab(IPCameraActivity.this.binding.tabLayout.newTab().setText(IPCameraActivity.this.getResources().getString(R.string.auto_door)));
                                        }
                                        if (IPCameraActivity.this.binding.tabLayout.getTabCount() >= 3) {
                                            IPCameraActivity.this.binding.tabLayout.removeTabAt(2);
                                            return;
                                        }
                                        return;
                                    }
                                    if (IPCameraActivity.this.binding.tabLayout.getTabCount() < 3) {
                                        IPCameraActivity.this.binding.tabLayout.addTab(IPCameraActivity.this.binding.tabLayout.newTab().setText(IPCameraActivity.this.getResources().getString(R.string.auto_door)));
                                        return;
                                    }
                                    return;
                                }
                                if (IPCameraActivity.this.binding.tabLayout.getTabCount() >= 3) {
                                    IPCameraActivity.this.binding.tabLayout.removeTabAt(2);
                                }
                                if (IPCameraActivity.this.isShowPtz || IPCameraActivity.this.binding.tabLayout.getTabCount() < 3) {
                                    return;
                                }
                                IPCameraActivity.this.binding.tabLayout.removeTabAt(1);
                            }
                        });
                    }
                }
                if (IPCameraActivity.this.device != null && IPCameraActivity.this.device.getOwned() == 1) {
                    IPCameraActivity.this.binding.llShare.setVisibility(0);
                    IPCameraActivity.this.isSupport4G = SharePreferenceManager.getInstance().getSupport4G(IPCameraActivity.this.device.getIotId());
                    if (IPCameraActivity.this.isSupport4G == 1) {
                        if (IPCameraActivity.this.badge != null) {
                            IPCameraActivity.this.badge.hideView();
                            IPCameraActivity.this.badge = null;
                        }
                    } else if (IPCameraActivity.this.badge == null) {
                        boolean z = AppConfig.isChina;
                    }
                } else {
                    if (IPCameraActivity.this.badge != null) {
                        IPCameraActivity.this.badge.hideView();
                        IPCameraActivity.this.badge = null;
                    }
                    IPCameraActivity.this.binding.llShare.setVisibility(8);
                }
                IPCameraActivity.this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(this.val$iotId);
                if (IPCameraActivity.this.faceDetectionAbility == 1) {
                    IPCameraActivity.this.isDetecting = SharePreferenceManager.getInstance().getHumanoidTrackingEnable(this.val$iotId).intValue() == 1;
                } else {
                    IPCameraActivity.this.isDetecting = SharePreferenceManager.getInstance().getIntelligentMode(this.val$iotId) == 1;
                }
                if (IPCameraActivity.this.isSwitching) {
                    return;
                }
                IPCameraActivity.this.moreFragment.updateData(IPCameraActivity.this.isDetecting, IPCameraActivity.this.lightVisible, IPCameraActivity.this.netVisible, IPCameraActivity.this.currentInfrarred, IPCameraActivity.this.switchText, IPCameraActivity.this.shopVisible, IPCameraActivity.this.smartDoorVisible, IPCameraActivity.this.isFourState, IPCameraActivity.this.supportMotionDetect);
            }
        }

        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(String str, String str2) {
            IPCameraActivity.this.uiHandler.post(new AnonymousClass1(str2, str));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeQualityDlgView(int i) {
        switch (i) {
            case 0:
                this.binding.tvHQuality.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvHQuality.setBackground(null);
                this.binding.tvMQuality.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvMQuality.setBackground(null);
                this.binding.tvLQuality.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.binding.tvLQuality.setTextColor(getResources().getColor(R.color.color_2999ff));
                break;
            case 1:
                this.binding.tvHQuality.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvHQuality.setBackground(null);
                this.binding.tvMQuality.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.binding.tvMQuality.setTextColor(getResources().getColor(R.color.color_2999ff));
                this.binding.tvLQuality.setBackground(null);
                this.binding.tvLQuality.setTextColor(getResources().getColor(R.color.color_white));
                break;
            case 2:
                this.binding.tvHQuality.setTextColor(getResources().getColor(R.color.color_2999ff));
                this.binding.tvHQuality.setBackgroundResource(R.drawable.bg_select_blue_dark);
                this.binding.tvMQuality.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvMQuality.setBackground(null);
                this.binding.tvLQuality.setTextColor(getResources().getColor(R.color.color_white));
                this.binding.tvLQuality.setBackground(null);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void snapshot() {
        LivePlayer livePlayer;
        Bitmap bitmapCreateScaledBitmap;
        verifyStoragePermissions(this);
        if (this.player.getPlayState() != 3 || (livePlayer = this.player) == null) {
            Toast.makeText(getActivity(), R.string.only_play_snap, 0).show();
            return;
        }
        Bitmap bitmapSnapShot = livePlayer.snapShot();
        if (bitmapSnapShot == null) {
            showToast(getResources().getString(R.string.no_snap));
            return;
        }
        if (bitmapSnapShot == null || (bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmapSnapShot, 2880, 1620, true)) == null) {
            return;
        }
        scanFile(bitmapCreateScaledBitmap);
        if (Build.VERSION.SDK_INT >= 29) {
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmapCreateScaledBitmap, "IMG" + Calendar.getInstance().getTime(), (String) null);
        } else {
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmapCreateScaledBitmap, "", "");
        }
        showToast(getResources().getString(R.string.camera_check));
    }

    public void scanFile(Bitmap bitmap) {
        File file = new File(getFilesPath(getApplication()) + "/photo/");
        if (file.exists() || file.mkdirs()) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(file, System.currentTimeMillis() + ".jpg"));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                EventBus.getDefault().post(new RefreshPicture());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFilesPath(Context context) {
        if (context == null) {
            return "";
        }
        String path = "";
        if ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            if (Build.VERSION.SDK_INT >= 8) {
                path = ((File) Objects.requireNonNull(context.getExternalFilesDir(""))).getPath();
            }
        } else {
            path = context.getFilesDir().getPath();
        }
        return path + "//" + Utils.getUserPhone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startOrStopRecordingMp4() {
        Resources resources;
        int i;
        Resources resources2;
        int i2;
        Resources resources3;
        int i3;
        if (!this.isRecording) {
            this.isRecording = true;
            File file = new File(getFilesPath(getApplication()) + "/video/");
            if (!file.exists() && !file.mkdirs()) {
                return;
            }
            try {
                this.player.startRecordingContent(new File(file, System.currentTimeMillis() + ".mp4"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            showTimer();
        } else {
            this.isRecording = false;
            this.player.stopRecordingContent();
            hideTimer();
        }
        ShadowButton shadowButton = this.binding.fullVideo;
        if (this.isRecording) {
            resources = getResources();
            i = R.drawable.full_video_;
        } else {
            resources = getResources();
            i = R.drawable.full_video;
        }
        shadowButton.setBackground(resources.getDrawable(i));
        ImageButton imageButton = this.binding.recordBtn;
        if (this.isRecording) {
            resources2 = getResources();
            i2 = R.drawable.video_video_light;
        } else {
            resources2 = getResources();
            i2 = R.drawable.video_video;
        }
        imageButton.setImageDrawable(resources2.getDrawable(i2));
        TextView textView = this.binding.tvRecord;
        if (this.isRecording) {
            resources3 = getResources();
            i3 = R.color.colorAccent;
        } else {
            resources3 = getResources();
            i3 = R.color.colors_ipc_image_text;
        }
        textView.setTextColor(resources3.getColor(i3));
    }

    public static void verifyStoragePermissions(Activity activity2) {
        try {
            if (ActivityCompat.checkSelfPermission(activity2, Permission.WRITE_EXTERNAL_STORAGE) != 0) {
                ActivityCompat.requestPermissions(activity2, new String[]{Permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSpeakerBtn(final int i) {
        runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.92
            @Override // java.lang.Runnable
            public void run() {
                if (IPCameraActivity.this.isFinishing()) {
                    return;
                }
                IPCameraActivity.this.isSpeakerOpen = i;
                if (IPCameraActivity.this.getResources().getConfiguration().orientation == 2) {
                    if (IPCameraActivity.this.isSpeakerOpen == 1) {
                        IPCameraActivity.this.binding.speakerBtn.setImageResource(R.drawable.camera_mic_push2);
                        return;
                    } else if (IPCameraActivity.this.isSpeakerOpen == 0) {
                        IPCameraActivity.this.binding.speakerBtn.setImageResource(R.drawable.camera_mic_nor2);
                        return;
                    } else {
                        IPCameraActivity.this.binding.speakerBtn.setImageResource(R.drawable.ic_speaking2);
                        return;
                    }
                }
                if (IPCameraActivity.this.isSpeakerOpen == 1) {
                    IPCameraActivity.this.binding.speakerBtn.setImageResource(R.drawable.camera_mic_push);
                } else if (IPCameraActivity.this.isSpeakerOpen == 0) {
                    IPCameraActivity.this.binding.speakerBtn.setImageResource(R.drawable.camera_mic_nor);
                } else {
                    IPCameraActivity.this.binding.speakerBtn.setImageResource(R.drawable.ic_speaking2);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLiveIntercomError() {
        runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.93
            @Override // java.lang.Runnable
            public void run() {
                if (IPCameraActivity.this.isFinishing()) {
                    return;
                }
                IPCameraActivity.this.whiteProgressDialog.dismiss();
            }
        });
        LiveIntercomV2 liveIntercomV2 = this.liveIntercom;
        if (liveIntercomV2 != null) {
            liveIntercomV2.stop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRecordError() {
        handleLiveIntercomError();
        runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.94
            @Override // java.lang.Runnable
            public void run() {
                IPCameraActivity.this.isLiveIntercoming = false;
                if (IPCameraActivity.this.isFinishing()) {
                    return;
                }
                IPCameraActivity.this.setSpeakerBtn(0);
                IPCameraActivity.this.binding.speakerBtn.setEnabled(true);
            }
        });
    }

    private void startLiveIntercom() {
        Resources resources;
        int i;
        if (this.isLiveIntercoming) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Permission.RECORD_AUDIO) != 0) {
            ActivityCompat.requestPermissions(this, new String[]{Permission.RECORD_AUDIO}, 4372);
            return;
        }
        setSpeakerBtn(1);
        this.liveIntercom.start();
        Log.e("speaker----", "play");
        this.binding.speakerBtn.setEnabled(false);
        this.whiteProgressDialog.setText(getResources().getString(R.string.open_speech));
        this.whiteProgressDialog.show();
        ShadowButton shadowButton = this.binding.fullIntercom;
        if (this.isLiveIntercoming) {
            resources = getResources();
            i = R.drawable.full_intercom;
        } else {
            resources = getResources();
            i = R.drawable.full_intercom_;
        }
        shadowButton.setBackground(resources.getDrawable(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startOrStopLiveIntercom() {
        if (!this.isLiveIntercoming) {
            if (ActivityCompat.checkSelfPermission(this, Permission.RECORD_AUDIO) != 0) {
                ActivityCompat.requestPermissions(this, new String[]{Permission.RECORD_AUDIO}, 4372);
                return;
            }
            setSpeakerBtn(1);
            LiveIntercomV2 liveIntercomV2 = this.liveIntercom;
            if (liveIntercomV2 != null) {
                liveIntercomV2.start();
            }
            Log.e("speaker----", "play");
            this.binding.speakerBtn.setEnabled(false);
            this.whiteProgressDialog.setText(getResources().getString(R.string.open_speech));
            this.whiteProgressDialog.show();
            return;
        }
        this.binding.speakerBtn.setEnabled(false);
        this.whiteProgressDialog.setText(getResources().getString(R.string.close_speech));
        this.whiteProgressDialog.show();
        runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.95
            @Override // java.lang.Runnable
            public void run() {
                IPCameraActivity.this.binding.speakerBtn.clearAnimation();
            }
        });
        LiveIntercomV2 liveIntercomV22 = this.liveIntercom;
        if (liveIntercomV22 != null) {
            liveIntercomV22.stop();
        }
        Log.e("speaker----", "stop");
    }

    private void addControlTouchView(boolean z) {
        if (this.touchView == null) {
            this.touchView = new TouchView(getActivity());
        }
        this.binding.rlTouchView.getViewTreeObserver().addOnGlobalLayoutListener(new AnonymousClass96(z));
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$96, reason: invalid class name */
    class AnonymousClass96 implements ViewTreeObserver.OnGlobalLayoutListener {
        final /* synthetic */ boolean val$isLand;

        AnonymousClass96(boolean z) {
            this.val$isLand = z;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (IPCameraActivity.this.touchView.getParent() != null) {
                ((ViewGroup) IPCameraActivity.this.touchView.getParent()).removeView(IPCameraActivity.this.touchView);
            }
            if (!this.val$isLand) {
                if (IPCameraActivity.this.binding.rlTouchView.getHeight() == 0) {
                    return;
                }
                IPCameraActivity.this.touchView.getModel().setBgResId(R.drawable.ui_pic_joystick_right_pad2);
                IPCameraActivity.this.touchView.setDefaultSize((IPCameraActivity.this.binding.rlTouchView.getHeight() * 170) / 192, IPCameraActivity.this.getResources().getDimensionPixelSize(R.dimen.ui_joystick_circle_bg_padding));
                IPCameraActivity.this.touchView.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                IPCameraActivity.this.binding.rlTouchView.addView(IPCameraActivity.this.touchView);
            } else {
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-2, -2);
                IPCameraActivity.this.touchView.getModel().setBgResId(R.drawable.ui_pic_joystick_right_pad3);
                int dimensionPixelSize = IPCameraActivity.this.getResources().getDimensionPixelSize(R.dimen.dimen_10);
                IPCameraActivity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCameraActivity.this.getActivity(), 120.0f) + (dimensionPixelSize * 2), dimensionPixelSize);
                layoutParams.bottomToBottom = IPCameraActivity.this.binding.fullScreen.getId();
                IPCameraActivity.this.binding.fullScreen.addView(IPCameraActivity.this.touchView, layoutParams);
            }
            IPCameraActivity.this.touchView.setListener(new JoystickTouchViewListener() { // from class: activity.IPCameraActivity.96.1
                @Override // view.JoystickTouchViewListener
                public void onActionDown() {
                }

                /* JADX WARN: Removed duplicated region for block: B:38:0x0087  */
                @Override // view.JoystickTouchViewListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public void onTouch(float r8, float r9) {
                    /*
                        Method dump skipped, instruction units count: 270
                        To view this dump add '--comments-level debug' option
                    */
                    throw new UnsupportedOperationException("Method not decompiled: activity.IPCameraActivity.AnonymousClass96.AnonymousClass1.onTouch(float, float):void");
                }

                @Override // view.JoystickTouchViewListener
                public void onReset() {
                    if (IPCameraActivity.this.ptzTimer != null) {
                        IPCameraActivity.this.ptzTimer.cancel();
                        IPCameraActivity.this.ptzTimer = null;
                    }
                    IPCameraActivity.this.lastActionTypeEnum = null;
                }

                @Override // view.JoystickTouchViewListener
                public void onActionUp() {
                    IPCameraActivity.this.touchView.resetView();
                    if (IPCameraActivity.this.ptzTimer != null) {
                        IPCameraActivity.this.ptzTimer.cancel();
                        IPCameraActivity.this.ptzTimer = null;
                    }
                    IPCameraActivity.this.lastActionTypeEnum = null;
                }
            });
            IPCameraActivity.this.binding.rlTouchView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    public void startPTZEx(ActionTypeEnum actionTypeEnum, SpeedEnum speedEnum) {
        IPCManager.getInstance().getDevice(this.iotId).startPTZEx(actionTypeEnum.getCode(), speedEnum.getCode(), new IPanelCallback() { // from class: activity.IPCameraActivity.97
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                String str = IPCameraActivity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("startPTZExControl:");
                sb.append(z);
                sb.append("       o:");
                sb.append(obj != null ? String.valueOf(obj) : TmpConstant.GROUP_ROLE_UNKNOWN);
                LogEx.e(true, str, sb.toString());
            }
        });
    }

    @Override // activity.CommonActivity, androidx.activity.ComponentActivity, android.app.Activity
    @SuppressLint({"SourceLockedOrientationActivity"})
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == 2) {
            if (getRequestedOrientation() == 0) {
                setRequestedOrientation(1);
                return;
            } else {
                setRequestedOrientation(9);
                return;
            }
        }
        super.onBackPressed();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        this.nowScale = this.binding.play.getScale();
        if (configuration.orientation == 2) {
            setFullScreen();
            this.isHorizontal = true;
            this.binding.play.firstAddZoom = true;
        } else {
            this.isHorizontal = false;
            backFullScreen();
        }
        super.onConfigurationChanged(configuration);
        setSwipeBackEnable(!this.isLand);
    }

    private void setFullScreen() {
        Resources resources;
        int i;
        this.binding.portraitPlayer.removeAllViews();
        if (this.binding.play.getParent() != null) {
            ((ViewGroup) this.binding.play.getParent()).removeView(this.binding.play);
        }
        this.binding.landscapePlayer.addView(this.binding.play);
        this.binding.play.requestLayout();
        if (this.binding.layoutOsd.getParent() != null) {
            ((ViewGroup) this.binding.layoutOsd.getParent()).removeView(this.binding.layoutOsd);
        }
        this.binding.landscapePlayer.addView(this.binding.layoutOsd);
        this.binding.layoutOsd.requestLayout();
        if (this.binding.fourPic.getParent() != null) {
            ((ViewGroup) this.binding.fourPic.getParent()).removeView(this.binding.fourPic);
        }
        this.binding.landscapePlayer.addView(this.binding.fourPic);
        this.binding.fourPic.requestLayout();
        if (this.binding.timer.getParent() != null) {
            ((ViewGroup) this.binding.timer.getParent()).removeView(this.binding.timer);
        }
        this.binding.landscapePlayer.addView(this.binding.timer);
        this.binding.timer.requestLayout();
        if (this.binding.SensorView.getParent() != null) {
            ((ViewGroup) this.binding.SensorView.getParent()).removeView(this.binding.SensorView);
        }
        this.binding.landscapePlayer.addView(this.binding.SensorView);
        this.binding.SensorView.requestLayout();
        if (this.binding.drawLineView.getParent() != null) {
            ((ViewGroup) this.binding.drawLineView.getParent()).removeView(this.binding.drawLineView);
        }
        this.binding.landscapePlayer.addView(this.binding.drawLineView);
        this.binding.drawLineView.requestLayout();
        if (this.binding.lineview.getParent() != null) {
            ((ViewGroup) this.binding.lineview.getParent()).removeView(this.binding.lineview);
        }
        this.binding.landscapePlayer.addView(this.binding.lineview);
        this.binding.lineview.requestLayout();
        this.binding.tvTitle.setRightLlGone(true);
        this.binding.tvTitle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        this.binding.tvTitle.setTBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_transparent));
        this.binding.controllerPanel.setVisibility(8);
        this.binding.ivCharge4gFlow.setVisibility(8);
        this.binding.play.setLayoutParams(new ConstraintLayout.LayoutParams(-1, -1));
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-1, -1);
        this.binding.drawLineView.setLayoutParams(layoutParams);
        this.binding.lineview.setLayoutParams(layoutParams);
        ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(-1, -1);
        layoutParams2.topToTop = this.binding.landscapePlayer.getTop();
        layoutParams2.leftToLeft = this.binding.landscapePlayer.getId();
        layoutParams2.rightToRight = this.binding.landscapePlayer.getId();
        layoutParams2.bottomToBottom = this.binding.landscapePlayer.getId();
        this.binding.fourPic.setLayoutParams(layoutParams2);
        this.binding.qualityControl.setVisibility(8);
        this.binding.tvTitle.setVisibility(8);
        ShadowButton shadowButton = this.binding.fullSound;
        if (this.speakerSwitch) {
            resources = getResources();
            i = R.drawable.full_sound;
        } else {
            resources = getResources();
            i = R.drawable.full_sound_;
        }
        shadowButton.setBackground(resources.getDrawable(i));
        if (this.isShowPtz) {
            addControlTouchView(true);
        } else {
            this.binding.ZOOMView.setVisibility(0);
            this.binding.layoutCloudPlayback.setVisibility(0);
        }
        this.binding.layoutOsd.bringToFront();
        if (SharePreferenceManager.getInstance().getNightVisionHide(this.device.getIotId()) == 1 || SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.device.getIotId()) == 0) {
            this.binding.ivLightWhile.setVisibility(8);
            this.binding.fullNightVision.setVisibility(8);
        }
        if (this.binding.llVideoQt.getParent() != null) {
            ((ViewGroup) this.binding.llVideoQt.getParent()).removeView(this.binding.llVideoQt);
        }
        this.binding.clarity.addView(this.binding.llVideoQt);
        this.binding.llVideoQt.bringToFront();
        this.binding.llVideoQt.requestLayout();
        ConstraintLayout.LayoutParams layoutParams3 = (ConstraintLayout.LayoutParams) this.binding.qualityDlg.getLayoutParams();
        layoutParams3.topToTop = this.binding.landscapePlayer.getId();
        layoutParams3.bottomToBottom = this.binding.landscapePlayer.getId();
        this.binding.qualityDlg.setLayoutParams(layoutParams3);
        this.binding.immediateRenewal.setVisibility(8);
        this.binding.traffic4gExpired.setVisibility(8);
        this.binding.outlineTime.setVisibility(8);
        getWindow().setFlags(1024, 1024);
    }

    private void backFullScreen() {
        this.binding.landscapePlayer.removeAllViews();
        if (this.binding.layoutOsd.getParent() != null) {
            ((ViewGroup) this.binding.layoutOsd.getParent()).removeView(this.binding.layoutOsd);
        }
        this.binding.portraitPlayer.addView(this.binding.layoutOsd);
        this.binding.layoutOsd.requestLayout();
        if (this.binding.fourPic.getParent() != null) {
            ((ViewGroup) this.binding.fourPic.getParent()).removeView(this.binding.fourPic);
        }
        this.binding.portraitPlayer.addView(this.binding.fourPic);
        this.binding.fourPic.requestLayout();
        if (this.binding.play.getParent() != null) {
            ((ViewGroup) this.binding.play.getParent()).removeView(this.binding.play);
        }
        this.binding.portraitPlayer.addView(this.binding.play);
        this.binding.play.requestLayout();
        if (this.binding.drawLineView.getParent() != null) {
            ((ViewGroup) this.binding.drawLineView.getParent()).removeView(this.binding.drawLineView);
        }
        this.binding.portraitPlayer.addView(this.binding.drawLineView);
        this.binding.drawLineView.requestLayout();
        if (this.binding.lineview.getParent() != null) {
            ((ViewGroup) this.binding.lineview.getParent()).removeView(this.binding.lineview);
        }
        this.binding.landscapePlayer.addView(this.binding.lineview);
        this.binding.lineview.requestLayout();
        if (this.binding.SensorView.getParent() != null) {
            ((ViewGroup) this.binding.SensorView.getParent()).removeView(this.binding.SensorView);
        }
        this.binding.portraitPlayer.addView(this.binding.SensorView);
        this.binding.SensorView.requestLayout();
        this.binding.tvTitle.setRightLlGone(false);
        this.binding.tvTitle.setBackgroundColor(getResources().getColor(R.color.color_black));
        this.binding.controllerPanel.setVisibility(0);
        if (this.binding.rlTouchView.getVisibility() == 0) {
            this.binding.ivCharge4gFlow.setVisibility(0);
            if (SharePreferenceManager.getInstance().getSupport4G(this.device.getIotId()) == 0 && !AppConfig.isChina) {
                this.binding.ivCharge4gFlow.setVisibility(8);
            }
        }
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.binding.play.getLayoutParams();
        int i = (int) ((ScreenUtil.getDisplayMetrics(getActivity())[0] * 9.0f) / 16.0f);
        layoutParams.height = i;
        layoutParams.bottomToBottom = this.binding.portraitPlayer.getId();
        layoutParams.topToTop = this.binding.portraitPlayer.getId();
        this.binding.play.setLayoutParams(layoutParams);
        this.binding.drawLineView.setLayoutParams(layoutParams);
        this.binding.lineview.setLayoutParams(layoutParams);
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) this.binding.drawLineView.getLayoutParams();
        layoutParams2.height = i;
        layoutParams2.bottomToBottom = this.binding.portraitPlayer.getId();
        layoutParams2.topToTop = this.binding.portraitPlayer.getId();
        this.binding.play.setLayoutParams(layoutParams2);
        ConstraintLayout.LayoutParams layoutParams3 = (ConstraintLayout.LayoutParams) this.binding.fourPic.getLayoutParams();
        layoutParams3.height = i;
        layoutParams3.topToTop = this.binding.portraitPlayer.getId();
        layoutParams3.bottomToBottom = this.binding.portraitPlayer.getId();
        this.binding.fourPic.setLayoutParams(layoutParams3);
        this.binding.qualityControl.setVisibility(0);
        this.binding.tvTitle.setVisibility(0);
        if (this.isShowPtz) {
            addControlTouchView(false);
        } else {
            this.binding.ZOOMView.setVisibility(0);
            this.binding.layoutCloudPlayback.setVisibility(0);
        }
        this.binding.fullScreen.setVisibility(8);
        this.binding.lightDlg.setVisibility(8);
        this.binding.layoutOsd.bringToFront();
        if (SharePreferenceManager.getInstance().getNightVisionHide(this.device.getIotId()) == 1 || SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.device.getIotId()) == 0) {
            this.binding.ivLightWhile.setVisibility(8);
            this.binding.fullNightVision.setVisibility(8);
        }
        this.binding.clarity.removeView(this.binding.llVideoQt);
        if (this.binding.llVideoQt.getParent() != null) {
            ((ViewGroup) this.binding.llVideoQt.getParent()).removeView(this.binding.llVideoQt);
        }
        this.binding.qualityControl.addView(this.binding.llVideoQt);
        this.binding.llVideoQt.requestLayout();
        ConstraintLayout.LayoutParams layoutParams4 = (ConstraintLayout.LayoutParams) this.binding.qualityDlg.getLayoutParams();
        layoutParams4.topToTop = this.binding.portraitPlayer.getId();
        layoutParams4.bottomToBottom = this.binding.portraitPlayer.getId();
        this.binding.qualityDlg.setLayoutParams(layoutParams4);
        if (this.needRecharge) {
            dismissPlayButton();
            this.binding.immediateRenewal.setVisibility(0);
            this.binding.traffic4gExpired.setVisibility(0);
            this.binding.outlineTime.setVisibility(0);
        }
        if (this.binding.timer.getParent() != null) {
            ((ViewGroup) this.binding.timer.getParent()).removeView(this.binding.timer);
        }
        this.binding.portraitPlayer.addView(this.binding.timer);
        this.binding.timer.requestLayout();
        getWindow().clearFlags(1024);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFloatBarState() {
        this.binding.playerInfoTv2.setVisibility((this.isFloat && this.player.getPlayState() == 3) ? 0 : 8);
        if (getResources().getConfiguration().orientation == 2) {
            this.binding.fullScreen.setVisibility(this.isFloat ? 0 : 8);
            this.binding.tvTitle.setVisibility(this.isFloat ? 0 : 8);
        }
    }

    @SuppressLint({"SetTextI18n"})
    public void updatePlayInfo() {
        if (this.updatePlayInfoHandle == null) {
            this.updatePlayInfoHandle = this.scheduledExecutorService.scheduleAtFixedRate(this.updatePlayInfoTimerTask, 1L, 1L, TimeUnit.SECONDS);
        }
        final String str = ((this.player.getCurrentPlayInfo().bitRate / 1024) / 8) + "KB/S";
        if (this.isFirstShowStreamType) {
            this.binding.playerInfoTv2.setText(str);
            this.uiHandler.postDelayed(new Runnable() { // from class: activity.IPCameraActivity.99
                @Override // java.lang.Runnable
                public void run() {
                    if (IPCameraActivity.this.isFinishing()) {
                        return;
                    }
                    IPCameraActivity.this.binding.playerInfoTv2.setText(str);
                    IPCameraActivity.this.isFirstShowStreamType = false;
                }
            }, 5000L);
        } else {
            this.binding.playerInfoTv2.setText(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPlayButton() {
        this.binding.videoBufferingBar.setVisibility(8);
        this.binding.wakeupText.setVisibility(8);
        this.binding.videoPlayIbtn.bringToFront();
        this.binding.videoPlayIbtn.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissPlayButton() {
        this.binding.videoPlayIbtn.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showBuffering() {
        this.binding.videoBufferingBar.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissBuffering() {
        this.binding.videoBufferingBar.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissSnapPicture() {
        this.binding.ivSnap.setVisibility(8);
    }

    private void AutoSnap() {
        Bitmap bitmapSnapShot;
        Bitmap bitmapCreateScaledBitmap;
        if (this.player.getPlayState() != 3 || (bitmapSnapShot = this.player.snapShot()) == null || bitmapSnapShot == null || (bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmapSnapShot, 2880, 1620, true)) == null) {
            return;
        }
        saveBitmap(bitmapCreateScaledBitmap);
    }

    public void saveBitmap(final Bitmap bitmap) {
        Log.d(this.TAG, "saveBitmap: -------------------------------");
        final Application application = getApplication();
        new Thread(new Runnable() { // from class: activity.IPCameraActivity.100
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                FileOutputStream fileOutputStream;
                File file;
                String string = SpUtil.getString(IPCameraActivity.this.getActivity(), Utils.getDevSnapKey(IPCameraActivity.this.iotId), "");
                long jCurrentTimeMillis = System.currentTimeMillis();
                LogEx.d(true, IPCameraActivity.this.TAG, "保存图片");
                StringBuilder sb = new StringBuilder();
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                sb.append(iPCameraActivity.getFilesPath(iPCameraActivity.getApplication()));
                sb.append("/snap/");
                sb.append(IPCameraActivity.this.iotId);
                sb.append("/");
                String string2 = sb.toString();
                FileOutputStream fileOutputStream2 = null;
                try {
                    try {
                        try {
                            File file2 = new File(string2);
                            if (!file2.exists()) {
                                file2.mkdirs();
                            }
                            file = new File(string2, jCurrentTimeMillis + ".png");
                            fileOutputStream = new FileOutputStream(file);
                        } catch (Throwable th) {
                            th = th;
                            fileOutputStream = null;
                        }
                    } catch (Exception e) {
                        e = e;
                    }
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                        fileOutputStream.flush();
                        LogEx.e(true, IPCameraActivity.this.TAG, "图片保存地址: " + file.getAbsolutePath());
                        SpUtil.putValue(application, Utils.getDevSnapKey(IPCameraActivity.this.iotId), file.getAbsolutePath());
                        EventBus.getDefault().post(new CameraSnapUpdate(IPCameraActivity.this.iotId, jCurrentTimeMillis));
                        FileUtil.deleteFile(string);
                        fileOutputStream.close();
                    } catch (Exception e2) {
                        e = e2;
                        fileOutputStream2 = fileOutputStream;
                        e.printStackTrace();
                        FileUtil.delete(string2);
                        if (fileOutputStream2 == null) {
                        } else {
                            fileOutputStream2.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }).start();
    }

    public void saveBitmap(final Bitmap bitmap, final int i, final PresetBean presetBean, final RecyclerView.Adapter adapter2) {
        Log.d(this.TAG, "saveBitmap: -------------------------------");
        final Application application = getApplication();
        ThreadPool.DefaultThreadPool.getInstance().submit(new Runnable() { // from class: activity.IPCameraActivity.101
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                Exception e;
                Throwable th;
                FileOutputStream fileOutputStream;
                final File file;
                long jCurrentTimeMillis = System.currentTimeMillis();
                LogEx.d(true, IPCameraActivity.this.TAG, "保存图片");
                StringBuilder sb = new StringBuilder();
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                sb.append(iPCameraActivity.getFilesPath(iPCameraActivity.getApplication()));
                sb.append("/snap/");
                sb.append(IPCameraActivity.this.iotId);
                sb.append("/");
                String string = sb.toString();
                FileOutputStream fileOutputStream2 = null;
                try {
                    try {
                        try {
                            File file2 = new File(string);
                            if (!file2.exists()) {
                                file2.mkdirs();
                            }
                            file = new File(string, jCurrentTimeMillis + String.valueOf(i + 1) + ".png");
                            fileOutputStream = new FileOutputStream(file);
                        } catch (Throwable th2) {
                            th = th2;
                            fileOutputStream = null;
                        }
                    } catch (Exception e2) {
                        e = e2;
                    }
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                        fileOutputStream.flush();
                        LogEx.e(true, IPCameraActivity.this.TAG, "图片保存地址: " + file.getAbsolutePath());
                        SpUtil.putValue(application, Utils.getDevSnapKey(IPCameraActivity.this.iotId) + String.valueOf(i + 1), file.getAbsolutePath());
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.101.1
                            @Override // java.lang.Runnable
                            public void run() {
                                presetBean.setString(file.getAbsolutePath());
                                presetBean.setCanDelete(true);
                                if (i > 7) {
                                    adapter2.notifyItemChanged(8, presetBean);
                                } else {
                                    adapter2.notifyItemChanged(i, presetBean);
                                }
                            }
                        });
                        fileOutputStream.close();
                    } catch (Exception e3) {
                        e = e3;
                        fileOutputStream2 = fileOutputStream;
                        e.printStackTrace();
                        FileUtil.delete(string);
                        if (fileOutputStream2 == null) {
                        } else {
                            fileOutputStream2.close();
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
        });
    }

    public void deleteBitmap(final int i, final PresetBean presetBean, final RecyclerView.Adapter adapter2) {
        final Application application = getApplication();
        new Thread(new Runnable() { // from class: activity.IPCameraActivity.102
            @Override // java.lang.Runnable
            public void run() {
                String string = SpUtil.getString(IPCameraActivity.this.getActivity(), Utils.getDevSnapKey(IPCameraActivity.this.iotId) + String.valueOf(i + 1), "");
                SpUtil.putValue(application, Utils.getDevSnapKey(IPCameraActivity.this.iotId) + String.valueOf(i + 1), "");
                FileUtil.deleteFile(string);
                IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.102.1
                    @Override // java.lang.Runnable
                    public void run() {
                        presetBean.setString("");
                        presetBean.setCanDelete(false);
                        if (i > 7) {
                            adapter2.notifyItemChanged(8, presetBean);
                        } else {
                            adapter2.notifyItemChanged(i, presetBean);
                        }
                    }
                });
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPlayInfo() {
        if (this.isFloat) {
            this.binding.playerInfoTv2.setVisibility(0);
        }
        updatePlayInfo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissPlayInfo() {
        this.binding.playerInfoTv2.setVisibility(8);
        ScheduledFuture<?> scheduledFuture = this.updatePlayInfoHandle;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            this.updatePlayInfoHandle = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeZoom(int i) {
        if (SharePreferenceManager.getInstance().getNewSupportEZOOM(this.iotId) == 1) {
            changechangeEZoom(i);
            return;
        }
        if (this.isMixZoom) {
            if (i == 1 && this.ZoomIsMax && this.binding.play.getScale() < this.binding.play.getMaxScale()) {
                this.binding.play.addZoom();
                return;
            } else if (i == 0 && this.binding.play.getScale() > 1.0f) {
                this.binding.play.reduceZoom();
                return;
            } else {
                changeOpticalZoom(i);
                return;
            }
        }
        if (!this.isOpticalZoom) {
            if (i == 1) {
                this.binding.play.addZoom();
                return;
            } else {
                this.binding.play.reduceZoom();
                return;
            }
        }
        if (i == 1) {
            changeOpticalZoom(1);
        } else if (i == 0) {
            changeOpticalZoom(0);
        }
    }

    private void changechangeEZoom(int i) {
        long jUptimeMillis = SystemClock.uptimeMillis();
        if (jUptimeMillis - this.lastOnclickTime >= 200) {
            this.lastOnclickTime = jUptimeMillis;
            IPCManager.getInstance().getDevice(this.iotId).changeEZoom(i, 0, SharePreferenceManager.getInstance().getStreamVideoQuality(this.iotId) == 2 ? 0 : 1, new IPanelCallback() { // from class: activity.IPCameraActivity.103
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (!z || obj == null || String.valueOf(obj).equals("")) {
                        return;
                    }
                    try {
                        final JSONObject object = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(obj)).getString("data"));
                        IPCameraActivity.this.ZoomMax = object.getInteger("ZoomMax").intValue();
                        IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                        boolean z2 = true;
                        if (object.getInteger("ZoomIsMax").intValue() != 1) {
                            z2 = false;
                        }
                        iPCameraActivity.ZoomIsMax = z2;
                        IPCameraActivity.this.zoom.postValue(Float.valueOf(object.getInteger("Lens").intValue()));
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.103.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.binding.tvOsd.setText(object.getInteger("Lens") + "X");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void changeOpticalZoom(int i) {
        long jUptimeMillis = SystemClock.uptimeMillis();
        if (jUptimeMillis - this.lastOnclickTime >= 200) {
            this.lastOnclickTime = jUptimeMillis;
            IPCManager.getInstance().getDevice(this.iotId).changeZoom(i, this.binding.play.getTimes(), new IPanelCallback() { // from class: activity.IPCameraActivity.104
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (!z || obj == null || String.valueOf(obj).equals("")) {
                        return;
                    }
                    try {
                        JSONObject object = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(obj)).getString("data"));
                        IPCameraActivity.this.ZoomMax = object.getInteger("ZoomMax").intValue();
                        IPCameraActivity.this.ZoomIsMax = object.getBoolean("ZoomIsMax").booleanValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNightMode(final Object obj) {
        HashMap map = new HashMap();
        map.put(Constants.DAY_NIGHT_MODE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCameraActivity.105
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj2) {
                if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.105.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCameraActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setDayNightMode(IPCameraActivity.this.iotId, Integer.parseInt(obj.toString()));
                            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.105.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCameraActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showTimer() {
        if (this.timer == null) {
            this.timer = new Timer();
        }
        this.binding.timer.setVisibility(0);
        this.timer.schedule(new TimerTask() { // from class: activity.IPCameraActivity.106
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.106.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCameraActivity.this.binding.timer.setText(IPCameraActivity.this.transformTime(IPCameraActivity.this.i));
                    }
                });
                IPCameraActivity.this.i++;
            }
        }, 0L, 1000L);
    }

    private void hideTimer() {
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
            this.i = 0;
            this.binding.timer.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String transformTime(int i) {
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

    public void getControllerList() {
        IPCManager.getInstance().getDevice(this.iotId).getControllerList(new IPanelCallback() { // from class: activity.IPCameraActivity.108
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (z) {
                    try {
                        JSONArray jSONArray = JSONObject.parseObject(String.valueOf(obj)).getJSONObject("data").getJSONArray("KeyList");
                        for (int i = 0; i < jSONArray.size(); i++) {
                            IPCameraActivity.this.ControllerListBean(i, ((Integer) jSONArray.get(i)).intValue());
                        }
                        IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.108.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.controllerFragment.refreshButton();
                                IPCameraActivity.this.refreshButton();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ControllerListBean(int i, int i2) {
        int i3 = i * 32;
        for (int i4 = 0; i4 < 32; i4++) {
            this.controllerList.set(i4 + i3, Integer.valueOf(((1 << i4) & i2) >> i4));
        }
    }

    @Override // fragment.ControllerFragment.Mylistener
    public void thanks() {
        FragmentTransaction fragmentTransactionBeginTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransactionBeginTransaction.hide(this.controllerFragment).show(this.moreFragment);
        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
    }

    @Override // view.FourPicturesView.ShowFloat
    public void tap() {
        this.isFloat = !this.isFloat;
        if (getResources().getConfiguration().orientation == 2) {
            this.binding.tvTitle.setVisibility(this.isFloat ? 0 : 8);
        }
    }

    @Override // fragment.LensControllerFragment.listener
    public void addZoom() {
        changeZoom(1);
    }

    @Override // fragment.LensControllerFragment.listener
    public void reduceZoom() {
        changeZoom(0);
    }

    @Override // fragment.LensControllerFragment.listener
    public void addFocus() {
        changeFocus(1);
    }

    @Override // fragment.LensControllerFragment.listener
    public void reduceFocus() {
        changeFocus(0);
    }

    @Override // fragment.LensControllerFragment.listener
    public void back() {
        getSupportFragmentManager().beginTransaction().hide(this.lensControllerFragment).show(this.moreFragment).commitAllowingStateLoss();
        this.isMoreFragmentShow = true;
    }

    @Override // fragment.LensControllerFragment.listener
    public void OnPresetInvoke(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        changePresetLocation(Integer.parseInt(str));
    }

    @Override // fragment.LensControllerFragment.listener
    public void OnPresetSet(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        addPresetLocation(Integer.parseInt(str));
    }

    @Override // fragment.LensControllerFragment.listener
    public void setWatchPos() {
        WatchPos();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeFocus(int i) {
        IPCManager.getInstance().getDevice(this.iotId).changeFocus(i, new IPanelCallback() { // from class: activity.IPCameraActivity.109
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCameraActivity.this.TAG, "invoke focus");
            }
        });
    }

    private void changePresetLocation(int i) {
        IPCManager.getInstance().getDevice(this.iotId).changePresetLocation(i, new IPanelCallback() { // from class: activity.IPCameraActivity.110
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCameraActivity.this.TAG, "change preset");
            }
        });
    }

    private void addPresetLocation(int i) {
        IPCManager.getInstance().getDevice(this.iotId).addPresetLocation(i, new IPanelCallback() { // from class: activity.IPCameraActivity.111
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCameraActivity.this.TAG, "add preset");
            }
        });
    }

    private void WatchPos() {
        IPCManager.getInstance().getDevice(this.iotId).setWatchPos(new IPanelCallback() { // from class: activity.IPCameraActivity.112
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (z) {
                    Log.d(IPCameraActivity.this.TAG, "onComplete: setWatchPos Finish");
                }
            }
        });
    }

    private void CloudTips() {
        if (this.binding.layoutCloudPlayback.getVisibility() == 8) {
            return;
        }
        this.badge = new BadgeView(getApplicationContext()).bindTarget(this.binding.layoutCloudPlayback).setBadgeTextSize(5.0f, true).setBadgePadding(2.0f, true).setGravityOffset(30.0f, 0.0f, true).setBadgeGravity(8388661);
        this.badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() { // from class: activity.IPCameraActivity.113
            @Override // view.Badge.OnDragStateChangedListener
            public void onDragStateChanged(int i, Badge badge, View view2) {
            }
        });
        getCloudQuery();
        getFreeCloudQuery();
        new Thread(new Runnable() { // from class: activity.IPCameraActivity.114
            @Override // java.lang.Runnable
            public void run() {
                while (true) {
                    if (IPCameraActivity.this.cloudStatusBean.getExpired() != -1 && IPCameraActivity.this.cloudStatusBean.getFreeCloud() != -1 && IPCameraActivity.this.cloudStatusBean.getFreeCloudExpired() != -1) {
                        break;
                    }
                }
                int expired = IPCameraActivity.this.cloudStatusBean.getExpired();
                int freeCloud = IPCameraActivity.this.cloudStatusBean.getFreeCloud();
                int freeCloudExpired = IPCameraActivity.this.cloudStatusBean.getFreeCloudExpired();
                final String string = "";
                if (freeCloud == 0) {
                    string = IPCameraActivity.this.getString(R.string.one_dollar_purchase);
                    IPCameraActivity.this.isOneYuan = true;
                } else if (expired == 1 && freeCloudExpired == 1) {
                    string = IPCameraActivity.this.getString(R.string.expired);
                } else if (expired == 2 && freeCloudExpired == 1) {
                    string = IPCameraActivity.this.getString(R.string.expired);
                }
                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.114.1
                    @Override // java.lang.Runnable
                    public void run() {
                        String str = string;
                        if (str != null && !str.equals("") && IPCameraActivity.this.isOwner) {
                            if (IPCameraActivity.this.badge == null) {
                                return;
                            } else {
                                IPCameraActivity.this.badge.setBadgeText(string);
                            }
                        }
                        if (SharePreferenceManager.getInstance().getFirstEnterActivity(IPCameraActivity.this.iotId) && IPCameraActivity.this.isOneYuan) {
                            SharePreferenceManager.getInstance().setFirstEnterActivity(IPCameraActivity.this.iotId, false);
                        }
                    }
                });
            }
        }).start();
    }

    private void getCloudQuery() {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/vision/customer/cloudstorage/order/query").setScheme(Scheme.HTTPS).setApiVersion("1.0.4").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.IPCameraActivity.115
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() == 200) {
                    yunCloudListBean yuncloudlistbean = (yunCloudListBean) new Gson().fromJson(ioTResponse.getData().toString(), yunCloudListBean.class);
                    if (yuncloudlistbean.getOrderList() != null) {
                        IPCameraActivity.this.cloudStatusBean.setExpired(1);
                        for (int i = 0; i < yuncloudlistbean.getOrderList().size(); i++) {
                            if (yuncloudlistbean.getOrderList().get(i).getExpired() == 0) {
                                IPCameraActivity.this.cloudStatusBean.setExpired(0);
                                return;
                            }
                        }
                        return;
                    }
                    IPCameraActivity.this.cloudStatusBean.setExpired(2);
                }
            }
        });
    }

    private void getFreeCloudQuery() {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/vision/customer/freecloudstorage/get").setScheme(Scheme.HTTPS).setApiVersion("1.0.1").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.IPCameraActivity.116
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                FreeCloudStorage freeCloudStorage = (FreeCloudStorage) new Gson().fromJson(ioTResponse.getData().toString(), FreeCloudStorage.class);
                if (freeCloudStorage.getConsumed() == 0) {
                    IPCameraActivity.this.cloudStatusBean.setFreeCloud(0);
                } else {
                    IPCameraActivity.this.cloudStatusBean.setFreeCloud(1);
                }
                if (freeCloudStorage.getExpired() == 0) {
                    IPCameraActivity.this.cloudStatusBean.setFreeCloudExpired(0);
                } else {
                    IPCameraActivity.this.cloudStatusBean.setFreeCloudExpired(1);
                }
            }
        });
    }

    private void keepScreenLight() {
        getWindow().addFlags(128);
    }

    private void stopScreenLight() {
        getWindow().clearFlags(128);
    }

    private void showSnapPicture() {
        String string = SpUtil.getString(getActivity(), Utils.getDevSnapKey(this.iotId), "");
        if (!isFinishing()) {
            Glide.with((FragmentActivity) this).load(string).into(this.binding.ivSnap);
        }
        this.binding.ivSnap.setVisibility(0);
    }

    private void openShop() {
        if (checkPackage(AgooConstants.TAOBAO_PACKAGE)) {
            Intent intent = new Intent();
            intent.addFlags(536870912);
            intent.setData(Uri.parse("taobao://seculink.tmall.com"));
            startActivity(intent);
        }
    }

    private boolean checkPackage(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        try {
            getPackageManager().getApplicationInfo(str, 8192);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://seculink.tmall.com")));
            return false;
        }
    }

    private void openWXShop() {
        IWXAPI iwxapiCreateWXAPI = WXAPIFactory.createWXAPI(getApplicationContext(), AppConfig.WX_APP_ID);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_1b74801927fd";
        req.miniprogramType = 0;
        iwxapiCreateWXAPI.sendReq(req);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openH5(String str) {
        Intent intent = new Intent(this, (Class<?>) ShopActivity.class);
        intent.putExtra("url", str);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void FourGChangeDialog() {
        View viewInflate = View.inflate(this, R.layout.wifi_list_ipc, null);
        this.f1578dialog = new AlertDialog.Builder(this).setView(viewInflate).create();
        this.f1578dialog.setCanceledOnTouchOutside(true);
        this.f1578dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.f1578dialog.show();
        int i = getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams attributes = this.f1578dialog.getWindow().getAttributes();
        attributes.width = (int) (((double) i) * 0.95d);
        this.f1578dialog.getWindow().setAttributes(attributes);
        this.f1578dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCameraActivity.117
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCameraActivity.this.cancelCount();
            }
        });
        ((Button) viewInflate.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.118
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.f1578dialog.dismiss();
            }
        });
        RecyclerView recyclerView = (RecyclerView) viewInflate.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new IpcWiFiAdapter(R.layout.item_wifi_ipc);
        this.mAdapter.bindToRecyclerView(recyclerView);
        this.mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() { // from class: activity.IPCameraActivity.119
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view2, int i2) {
                WifiBean wifiBean = IPCameraActivity.this.mAdapter.getData().get(i2);
                if (wifiBean.isCurrentWifi()) {
                    return;
                }
                IPCameraActivity.this.f1578dialog.dismiss();
                IPCameraActivity.this.selectSsid = wifiBean.getSsid();
                IPCameraActivity.this.inputDialogView.setTitle(IPCameraActivity.this.selectSsid);
                IPCameraActivity.this.inputDialogView.show(IPCameraActivity.this.getSupportFragmentManager(), IPCameraActivity.this.TAG);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void FourGChangeDialog(int i) {
        View viewInflate = View.inflate(this, R.layout.switch_layout, null);
        ProgressBar progressBar = (ProgressBar) viewInflate.findViewById(R.id.load_progressbar);
        ImageButton imageButton = (ImageButton) viewInflate.findViewById(R.id.close_btn);
        Drawable drawable = getResources().getDrawable(R.drawable.et_cancel);
        drawable.setBounds(0, 0, (int) (((double) drawable.getIntrinsicWidth()) * 0.5d), (int) (((double) drawable.getIntrinsicHeight()) * 0.5d));
        imageButton.setBackground(drawable);
        Button button = (Button) viewInflate.findViewById(R.id.cancel);
        TextView textView = (TextView) viewInflate.findViewById(R.id.load_progressbar_text);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.image_result_text);
        final AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setView(viewInflate).create();
        alertDialogCreate.setCanceledOnTouchOutside(true);
        alertDialogCreate.show();
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCameraActivity.120
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCameraActivity.this.isSwitching = false;
                IPCameraActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.121
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.122
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.isSwitching = true;
            this.countDownTimer = new AnonymousClass123(60000L, 4000L, i, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$123, reason: invalid class name */
    class AnonymousClass123 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ int val$position;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass123(long j, long j2, int i, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
            super(j, j2);
            this.val$position = i;
            this.val$progressBarText = textView;
            this.val$progressBar = progressBar;
            this.val$imageViewText = textView2;
            this.val$imageView = imageView;
            this.val$imageButton = imageButton;
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            SettingsCtrl.getInstance().getProperties(IPCameraActivity.this.iotId, new MyCallback() { // from class: activity.IPCameraActivity.123.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCameraActivity.this.iotId) == AnonymousClass123.this.val$position) {
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.123.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AnonymousClass123.this.val$progressBarText.setVisibility(8);
                                AnonymousClass123.this.val$progressBar.setVisibility(8);
                                AnonymousClass123.this.val$imageViewText.setVisibility(0);
                                AnonymousClass123.this.val$imageViewText.setText(R.string.switched_success);
                                AnonymousClass123.this.val$imageView.setVisibility(0);
                                AnonymousClass123.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass123.this.val$imageButton.setVisibility(0);
                                IPCameraActivity.this.wifiFourPosition = AnonymousClass123.this.val$position;
                                Toast.makeText(IPCameraActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                        AnonymousClass123.this.cancel();
                        IPCameraActivity.this.countDownTimer = null;
                        IPCameraActivity.this.isSwitching = false;
                        IPCameraActivity.this.getProperties(new MyCallback() { // from class: activity.IPCameraActivity.123.1.2
                            @Override // tools.MyCallback
                            public void onComplete(boolean z2) {
                            }
                        });
                    }
                }
            });
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.123.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass123.this.val$progressBarText.setVisibility(8);
                    AnonymousClass123.this.val$progressBar.setVisibility(8);
                    AnonymousClass123.this.val$imageViewText.setVisibility(0);
                    AnonymousClass123.this.val$imageViewText.setText(R.string.switched_fail);
                    AnonymousClass123.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass123.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            IPCameraActivity.this.countDownTimer = null;
            IPCameraActivity.this.isSwitching = false;
            IPCameraActivity.this.getProperties(new MyCallback() { // from class: activity.IPCameraActivity.123.3
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getWiFiListSucceed(List<WifiBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        this.mAdapter.replaceData(list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getWiFiListFailed() {
        Toast.makeText(this, getString(R.string.get_wifi_failed), 0).show();
    }

    private void getWiFiList() {
        SharePreferenceManager.getInstance().setFirstNet(this.iotId, false);
        IPCManager.getInstance().getDevice(this.iotId).queryAPList(new IPanelCallback() { // from class: activity.IPCameraActivity.124
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null) {
                    return;
                }
                try {
                    IoTResponse ioTResponse = (IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class);
                    if (ioTResponse.getCode() != 200) {
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.124.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.getWiFiListFailed();
                            }
                        });
                    } else {
                        Object data = ioTResponse.getData();
                        if (data != null) {
                            try {
                                JSONArray jSONArray = ((JSONObject) data).getJSONArray("APList");
                                IPCameraActivity.this.wifiBeanList = JSON.parseArray(jSONArray.toString(), WifiBean.class);
                                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.124.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (IPCameraActivity.this.isFinishing()) {
                                            return;
                                        }
                                        IPCameraActivity.this.FourGChangeDialog();
                                        IPCameraActivity.this.getWiFiListSucceed(IPCameraActivity.this.wifiBeanList);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.124.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (IPCameraActivity.this.isFinishing()) {
                                            return;
                                        }
                                        IPCameraActivity.this.getWiFiListFailed();
                                        IPCameraActivity.this.showToast(IPCameraActivity.this.getString(R.string.query_wifi_list_fail));
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    public void connect() {
        View viewInflate = View.inflate(this, R.layout.switch_layout, null);
        ProgressBar progressBar = (ProgressBar) viewInflate.findViewById(R.id.load_progressbar);
        ImageButton imageButton = (ImageButton) viewInflate.findViewById(R.id.close_btn);
        Drawable drawable = getResources().getDrawable(R.drawable.et_cancel);
        drawable.setBounds(0, 0, (int) (((double) drawable.getIntrinsicWidth()) * 0.5d), (int) (((double) drawable.getIntrinsicHeight()) * 0.5d));
        imageButton.setBackground(drawable);
        Button button = (Button) viewInflate.findViewById(R.id.cancel);
        TextView textView = (TextView) viewInflate.findViewById(R.id.load_progressbar_text);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.image_result_text);
        final AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setView(viewInflate).create();
        alertDialogCreate.setCanceledOnTouchOutside(true);
        alertDialogCreate.show();
        this.inputDialogView.dismiss();
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCameraActivity.125
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCameraActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.126
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.127
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.countDownTimer = new AnonymousClass128(60000L, 4000L, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$128, reason: invalid class name */
    class AnonymousClass128 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass128(long j, long j2, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
            super(j, j2);
            this.val$progressBarText = textView;
            this.val$progressBar = progressBar;
            this.val$imageViewText = textView2;
            this.val$imageView = imageView;
            this.val$imageButton = imageButton;
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            SettingsCtrl.getInstance().getProperties(IPCameraActivity.this.iotId, new MyCallback() { // from class: activity.IPCameraActivity.128.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getWifiConfigIsExist(IPCameraActivity.this.iotId) == 1) {
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.128.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AnonymousClass128.this.val$progressBarText.setVisibility(8);
                                AnonymousClass128.this.val$progressBar.setVisibility(8);
                                AnonymousClass128.this.val$imageViewText.setVisibility(0);
                                AnonymousClass128.this.val$imageViewText.setText(IPCameraActivity.this.getString(R.string.switched_success));
                                AnonymousClass128.this.val$imageView.setVisibility(0);
                                AnonymousClass128.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass128.this.val$imageButton.setVisibility(0);
                            }
                        });
                        AnonymousClass128.this.cancel();
                        IPCameraActivity.this.countDownTimer = null;
                    }
                }
            });
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.128.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass128.this.val$progressBarText.setVisibility(8);
                    AnonymousClass128.this.val$progressBar.setVisibility(8);
                    AnonymousClass128.this.val$imageViewText.setVisibility(0);
                    AnonymousClass128.this.val$imageViewText.setText(IPCameraActivity.this.getString(R.string.switched_fail));
                    AnonymousClass128.this.val$imageView.setVisibility(0);
                    AnonymousClass128.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass128.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            IPCameraActivity.this.countDownTimer = null;
        }
    }

    public void cancelCount() {
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.countDownTimer = null;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (this.iotId.equals(intent.getStringExtra("iotId"))) {
            this.strongRemind = 1;
        } else {
            finish();
            startActivity(intent);
        }
    }

    public void showBadNetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflate = View.inflate(this, R.layout.switch_network_layout, null);
        builder.setView(viewInflate);
        final AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.show();
        alertDialogCreate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ((Button) viewInflate.findViewById(R.id.btn_left)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.129
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        ((Button) viewInflate.findViewById(R.id.btn_right)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.130
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.switch4gMode(iPCameraActivity.getString(R.string.Net4GEnableSwitch), 2);
                alertDialogCreate.dismiss();
                IPCameraActivity.this.FourGChangeDialog(2, alertDialogCreate);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switch4gMode(String str, int i) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.Net4GEnableSwitch))) {
            map.put(Constants.Net4GEnableSwitch, Integer.valueOf(i));
        }
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCameraActivity.131
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        object.getInteger("code").intValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void FourGChangeDialog(int i, AlertDialog alertDialog) {
        View viewInflate = View.inflate(this, R.layout.switch_layout, null);
        ProgressBar progressBar = (ProgressBar) viewInflate.findViewById(R.id.load_progressbar);
        ImageButton imageButton = (ImageButton) viewInflate.findViewById(R.id.close_btn);
        Drawable drawable = getResources().getDrawable(R.drawable.et_cancel);
        drawable.setBounds(0, 0, (int) (((double) drawable.getIntrinsicWidth()) * 0.5d), (int) (((double) drawable.getIntrinsicHeight()) * 0.5d));
        imageButton.setBackground(drawable);
        Button button = (Button) viewInflate.findViewById(R.id.cancel);
        TextView textView = (TextView) viewInflate.findViewById(R.id.load_progressbar_text);
        TextView textView2 = (TextView) viewInflate.findViewById(R.id.image_result_text);
        final AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setView(viewInflate).create();
        alertDialogCreate.setCanceledOnTouchOutside(true);
        alertDialogCreate.show();
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCameraActivity.132
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCameraActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.133
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.134
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.countDownTimer = new AnonymousClass135(60000L, 4000L, i, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$135, reason: invalid class name */
    class AnonymousClass135 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ int val$position;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass135(long j, long j2, int i, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
            super(j, j2);
            this.val$position = i;
            this.val$progressBarText = textView;
            this.val$progressBar = progressBar;
            this.val$imageViewText = textView2;
            this.val$imageView = imageView;
            this.val$imageButton = imageButton;
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            SettingsCtrl.getInstance().getProperties(IPCameraActivity.this.iotId, new MyCallback() { // from class: activity.IPCameraActivity.135.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCameraActivity.this.iotId) == AnonymousClass135.this.val$position) {
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.135.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCameraActivity.this.playLive();
                                AnonymousClass135.this.val$progressBarText.setVisibility(8);
                                AnonymousClass135.this.val$progressBar.setVisibility(8);
                                AnonymousClass135.this.val$imageViewText.setVisibility(0);
                                AnonymousClass135.this.val$imageViewText.setText(IPCameraActivity.this.getString(R.string.switched_success));
                                AnonymousClass135.this.val$imageView.setVisibility(0);
                                AnonymousClass135.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass135.this.val$imageButton.setVisibility(0);
                                Toast.makeText(IPCameraActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                        AnonymousClass135.this.cancel();
                        IPCameraActivity.this.countDownTimer = null;
                    }
                }
            });
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.135.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass135.this.val$progressBarText.setVisibility(8);
                    AnonymousClass135.this.val$progressBar.setVisibility(8);
                    AnonymousClass135.this.val$imageViewText.setVisibility(0);
                    AnonymousClass135.this.val$imageViewText.setText(IPCameraActivity.this.getString(R.string.switched_fail));
                    AnonymousClass135.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass135.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            IPCameraActivity.this.countDownTimer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shareDevice(final String str, final DeviceInfoBean deviceInfoBean, String str2) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(deviceInfoBean.getIotId());
        HashMap map = new HashMap();
        map.put(AlinkConstants.KEY_ACCOUNT_ATTR, str);
        if (TextUtils.isEmpty(str2)) {
            map.put(AlinkConstants.KEY_ACCOUNT_ATTR_TYPE, DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_EMAIL);
        } else {
            map.put(AlinkConstants.KEY_ACCOUNT_ATTR_TYPE, DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_MOBILE);
            map.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, str2);
        }
        map.put("iotIdList", arrayList);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath(AlinkConstants.HTTP_PATH_DEVICE_SHARE).setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.IPCameraActivity.136
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, IPCameraActivity.this.TAG, "onFailure");
                Toast.makeText(IPCameraActivity.this.getActivity(), R.string.share_failed, 0).show();
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                final int code = ioTResponse.getCode();
                Log.e(IPCameraActivity.this.TAG, "shareDevice onResponse: code: " + code);
                final String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.136.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Activity activity2 = IPCameraActivity.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            if (code == 2077) {
                                DialogUtil.showTipsConfirmDiaLog(IPCameraActivity.this.getActivity(), IPCameraActivity.this.getString(R.string.sharing_failed), IPCameraActivity.this.getString(R.string.sharing_tips_1) + SdkConstant.CLOUDAPI_LF + IPCameraActivity.this.getString(R.string.sharing_tips_2) + SdkConstant.CLOUDAPI_LF + IPCameraActivity.this.getString(R.string.sharing_tips_3) + SdkConstant.CLOUDAPI_LF + IPCameraActivity.this.getString(R.string.sharing_tips_4), IPCameraActivity.this.getString(R.string.i_know));
                                return;
                            }
                            Toast.makeText(IPCameraActivity.this.getActivity(), localizedMsg, 0).show();
                        }
                    });
                } else {
                    IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.136.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Activity activity2 = IPCameraActivity.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            Toast.makeText(IPCameraActivity.this.getActivity(), IPCameraActivity.this.getString(R.string.share_succeed, new Object[]{deviceInfoBean.getName(), str}), 0).show();
                        }
                    });
                }
            }
        });
    }

    private void checkSwitch(final TopicBean topicBean, final RecyclerView.Adapter adapter2, final int i, final int i2) {
        this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.137
            @Override // java.lang.Runnable
            public void run() {
                IPCameraActivity.this.showProgressDialog();
            }
        });
        HashMap map = new HashMap();
        map.put(Constants.IvpExSwitch, Integer.valueOf(i2));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCameraActivity.138
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                IPCameraActivity.this.dismissProgressDialog();
                if (!z) {
                    IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.138.3
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(IPCameraActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        }
                    });
                    return;
                }
                if (obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.138.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCameraActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                            return;
                        }
                        IPCameraActivity.this.isDetecting = !IPCameraActivity.this.isDetecting;
                        if (IPCameraActivity.this.faceDetectionAbility == 1) {
                            if (IPCameraActivity.this.isDetecting) {
                                SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCameraActivity.this.iotId, 1);
                            } else {
                                SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCameraActivity.this.iotId, 0);
                            }
                        } else if (IPCameraActivity.this.isDetecting) {
                            SharePreferenceManager.getInstance().setIntelligentMode(IPCameraActivity.this.iotId, 1);
                        } else {
                            SharePreferenceManager.getInstance().setIntelligentMode(IPCameraActivity.this.iotId, 0);
                        }
                        SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCameraActivity.this.iotId, i2 & 1);
                        SharePreferenceManager.getInstance().setAreaDetectEnable(IPCameraActivity.this.iotId, (i2 & 4) >> 2);
                        SharePreferenceManager.getInstance().setCrossLineEnable(IPCameraActivity.this.iotId, (i2 & 2) >> 1);
                        IPCameraActivity.this.moreFragment.setDetecting(IPCameraActivity.this.isDetecting);
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.138.2
                            @Override // java.lang.Runnable
                            public void run() {
                                topicBean.setSelect(!topicBean.isSelect());
                                if (topicBean.isSelect()) {
                                    topicBean.setIcon(R.drawable.humanoid_hracking_ipc_light);
                                } else {
                                    topicBean.setIcon(R.drawable.humanoid_hracking_ipc);
                                }
                                adapter2.notifyItemChanged(i, topicBean);
                                Toast.makeText(IPCameraActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setMobileTracking(final TopicBean topicBean, final RecyclerView.Adapter adapter2, final int i, int i2) {
        HashMap map = new HashMap();
        map.put(Constants.INTELLIGENT_TRACKING, Integer.valueOf(i2));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCameraActivity.139
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                IPCameraActivity.this.dismissProgressDialog();
                if (!z) {
                    IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.139.3
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(IPCameraActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        }
                    });
                    return;
                }
                if (obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.139.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCameraActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                            return;
                        }
                        IPCameraActivity.this.isDetecting = !IPCameraActivity.this.isDetecting;
                        if (IPCameraActivity.this.faceDetectionAbility == 1) {
                            if (IPCameraActivity.this.isDetecting) {
                                SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCameraActivity.this.iotId, 1);
                            } else {
                                SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCameraActivity.this.iotId, 0);
                            }
                        } else if (IPCameraActivity.this.isDetecting) {
                            SharePreferenceManager.getInstance().setIntelligentMode(IPCameraActivity.this.iotId, 1);
                        } else {
                            SharePreferenceManager.getInstance().setIntelligentMode(IPCameraActivity.this.iotId, 0);
                        }
                        IPCameraActivity.this.moreFragment.setDetecting(IPCameraActivity.this.isDetecting);
                        IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.139.2
                            @Override // java.lang.Runnable
                            public void run() {
                                topicBean.setSelect(!topicBean.isSelect());
                                if (topicBean.isSelect()) {
                                    topicBean.setIcon(R.drawable.humanoid_hracking_ipc_light);
                                } else {
                                    topicBean.setIcon(R.drawable.humanoid_hracking_ipc);
                                }
                                adapter2.notifyItemChanged(i, topicBean);
                                Toast.makeText(IPCameraActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override // fragment.PresetFragment.PresetDataChange
    public void go2Delete() {
        this.binding.deleteLl.setVisibility(0);
        this.binding.llBottom.setVisibility(8);
    }

    public void hideKeyboard(Activity activity2) {
        ((InputMethodManager) activity2.getSystemService("input_method")).hideSoftInputFromWindow(activity2.getWindow().getDecorView().getWindowToken(), 0);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void initAutorView() {
        final ShadowButton shadowButton = (ShadowButton) findViewById(R.id.button1);
        shadowButton.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.140
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.act((iPCameraActivity.Page * IPCameraActivity.this.controllerSize) + 0);
            }
        });
        shadowButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: activity.IPCameraActivity.141
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                if (((Integer) IPCameraActivity.this.list.get(IPCameraActivity.this.Page * IPCameraActivity.this.controllerSize)).intValue() != 0) {
                    return false;
                }
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.showDialogIpc(iPCameraActivity.Page * IPCameraActivity.this.controllerSize);
                return false;
            }
        });
        shadowButton.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.142
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    shadowButton.setBackground(IPCameraActivity.this.getResources().getDrawable(R.drawable.controller_up_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCameraActivity.this.refreshButton();
                return false;
            }
        });
        final ShadowButton shadowButton2 = (ShadowButton) findViewById(R.id.button2);
        shadowButton2.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.143
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.act((iPCameraActivity.Page * IPCameraActivity.this.controllerSize) + 1);
            }
        });
        shadowButton2.setOnLongClickListener(new View.OnLongClickListener() { // from class: activity.IPCameraActivity.144
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                if (((Integer) IPCameraActivity.this.list.get((IPCameraActivity.this.Page * IPCameraActivity.this.controllerSize) + 1)).intValue() != 0) {
                    return false;
                }
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.showDialogIpc((iPCameraActivity.Page * IPCameraActivity.this.controllerSize) + 1);
                return false;
            }
        });
        shadowButton2.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.145
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    shadowButton2.setBackground(IPCameraActivity.this.getResources().getDrawable(R.drawable.controller_lock_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCameraActivity.this.refreshButton();
                return false;
            }
        });
        final ShadowButton shadowButton3 = (ShadowButton) findViewById(R.id.button3);
        shadowButton3.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.146
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.act((iPCameraActivity.Page * IPCameraActivity.this.controllerSize) + 2);
            }
        });
        shadowButton3.setOnLongClickListener(new View.OnLongClickListener() { // from class: activity.IPCameraActivity.147
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                if (((Integer) IPCameraActivity.this.list.get((IPCameraActivity.this.Page * IPCameraActivity.this.controllerSize) + 2)).intValue() != 0) {
                    return false;
                }
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.showDialogIpc((iPCameraActivity.Page * IPCameraActivity.this.controllerSize) + 2);
                return false;
            }
        });
        shadowButton3.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.148
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    shadowButton3.setBackground(IPCameraActivity.this.getResources().getDrawable(R.drawable.controller_stop_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCameraActivity.this.refreshButton();
                return false;
            }
        });
        final ShadowButton shadowButton4 = (ShadowButton) findViewById(R.id.button4);
        shadowButton4.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.149
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.act((iPCameraActivity.Page * IPCameraActivity.this.controllerSize) + 3);
            }
        });
        shadowButton4.setOnLongClickListener(new View.OnLongClickListener() { // from class: activity.IPCameraActivity.150
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                if (((Integer) IPCameraActivity.this.list.get((IPCameraActivity.this.Page * IPCameraActivity.this.controllerSize) + 3)).intValue() != 0) {
                    return false;
                }
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.showDialogIpc((iPCameraActivity.Page * IPCameraActivity.this.controllerSize) + 3);
                return false;
            }
        });
        shadowButton4.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.151
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    shadowButton4.setBackground(IPCameraActivity.this.getResources().getDrawable(R.drawable.controller_down_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCameraActivity.this.refreshButton();
                return false;
            }
        });
        this.binding.button5.setOnClickListener(new AnonymousClass152());
        this.binding.button6.setOnClickListener(new AnonymousClass153());
        final ShadowButton shadowButton5 = (ShadowButton) findViewById(R.id.doorbell);
        shadowButton5.setOnLongClickListener(new View.OnLongClickListener() { // from class: activity.IPCameraActivity.154
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                if (((Integer) IPCameraActivity.this.list.get((IPCameraActivity.this.Page * IPCameraActivity.this.controllerSize) + 4)).intValue() != 0) {
                    return false;
                }
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.showDialogIpc((iPCameraActivity.Page * IPCameraActivity.this.controllerSize) + 4);
                return false;
            }
        });
        shadowButton5.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCameraActivity.155
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    shadowButton5.setBackground(IPCameraActivity.this.getResources().getDrawable(R.drawable.doorbell_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCameraActivity.this.refreshButton();
                return false;
            }
        });
        this.buttonList.add(shadowButton);
        this.buttonList.add(shadowButton2);
        this.buttonList.add(shadowButton3);
        this.buttonList.add(shadowButton4);
        this.buttonList.add(shadowButton5);
        this.controllerSize = this.buttonList.size();
        ((LinearLayout) findViewById(R.id.ll1)).setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.156
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.pageSelect.setValue(0);
                IPCameraActivity.this.refreshButton();
            }
        });
        ((LinearLayout) findViewById(R.id.ll2)).setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.157
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.pageSelect.setValue(1);
                IPCameraActivity.this.refreshButton();
            }
        });
        ((LinearLayout) findViewById(R.id.ll3)).setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.158
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.pageSelect.setValue(2);
                IPCameraActivity.this.refreshButton();
            }
        });
        ((LinearLayout) findViewById(R.id.ll4)).setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCameraActivity.159
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCameraActivity.this.pageSelect.setValue(3);
                IPCameraActivity.this.refreshButton();
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.image1);
        ImageView imageView2 = (ImageView) findViewById(R.id.image2);
        ImageView imageView3 = (ImageView) findViewById(R.id.image3);
        ImageView imageView4 = (ImageView) findViewById(R.id.image4);
        TextView textView = (TextView) findViewById(R.id.text1);
        TextView textView2 = (TextView) findViewById(R.id.text2);
        TextView textView3 = (TextView) findViewById(R.id.text3);
        TextView textView4 = (TextView) findViewById(R.id.text4);
        this.doorList.add(imageView);
        this.doorList.add(imageView2);
        this.doorList.add(imageView3);
        this.doorList.add(imageView4);
        this.doorTextList.add(textView);
        this.doorTextList.add(textView2);
        this.doorTextList.add(textView3);
        this.doorTextList.add(textView4);
        this.door_text = (TextView) findViewById(R.id.door_text);
        ImageView imageView5 = (ImageView) findViewById(R.id.controller_edit);
        this.tips = (TextView) findViewById(R.id.tips);
        imageView5.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCameraActivity.160
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCameraActivity.this.showTips();
            }
        });
        this.pageSelect.observe(this, new Observer<Integer>() { // from class: activity.IPCameraActivity.161
            @Override // androidx.lifecycle.Observer
            public void onChanged(@Nullable Integer num) {
                if (num != null) {
                    ((ImageView) IPCameraActivity.this.doorList.get(IPCameraActivity.this.Page)).setImageResource(R.drawable.computer_ipc);
                    ((TextView) IPCameraActivity.this.doorTextList.get(IPCameraActivity.this.Page)).setTextColor(IPCameraActivity.this.getResources().getColor(R.color.color_black));
                    IPCameraActivity.this.Page = num.intValue();
                    ((ImageView) IPCameraActivity.this.doorList.get(num.intValue())).setImageResource(R.drawable.computer_ipc_light);
                    ((TextView) IPCameraActivity.this.doorTextList.get(num.intValue())).setTextColor(IPCameraActivity.this.getResources().getColor(R.color.colorAccent));
                    final int iIntValue = num.intValue();
                    ((Activity) Objects.requireNonNull(IPCameraActivity.this.getActivity())).runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.161.1
                        @Override // java.lang.Runnable
                        public void run() {
                            String autoName = SharePreferenceManager.getInstance().getAutoName(IPCameraActivity.this.iotId, IPCameraActivity.this.Page);
                            if (autoName == null || "".equals(autoName)) {
                                IPCameraActivity.this.door_text.setText(((TextView) IPCameraActivity.this.doorTextList.get(iIntValue)).getText());
                                ((TextView) IPCameraActivity.this.doorTextList.get(IPCameraActivity.this.Page)).setText(((TextView) IPCameraActivity.this.doorTextList.get(iIntValue)).getText());
                            } else {
                                IPCameraActivity.this.door_text.setText(autoName);
                                ((TextView) IPCameraActivity.this.doorTextList.get(IPCameraActivity.this.Page)).setText(autoName);
                            }
                        }
                    });
                }
            }
        });
        this.pageSelect.postValue(0);
        refreshButtonViewText();
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$152, reason: invalid class name */
    class AnonymousClass152 extends OnMultiClickListener {
        AnonymousClass152() {
        }

        /* JADX INFO: renamed from: activity.IPCameraActivity$152$1, reason: invalid class name */
        class AnonymousClass1 implements IPanelCallback {
            AnonymousClass1() {
            }

            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(final boolean z, final Object obj) {
                Log.d(IPCameraActivity.this.TAG, "onComplete: ------" + z + "    " + obj.toString());
                new Handler().post(new Runnable() { // from class: activity.IPCameraActivity.152.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Object obj2;
                        if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                            return;
                        }
                        try {
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code")) {
                                if (object.getInteger("code").intValue() != 200) {
                                    IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.152.1.1.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            Toast.makeText(IPCameraActivity.this.getActivity(), R.string.set_failed, 0).show();
                                        }
                                    });
                                } else {
                                    IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.152.1.1.2
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            Toast.makeText(IPCameraActivity.this.getActivity(), R.string.set_success, 0).show();
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).setRFAMCControl(new AnonymousClass1());
        }
    }

    /* JADX INFO: renamed from: activity.IPCameraActivity$153, reason: invalid class name */
    class AnonymousClass153 extends OnMultiClickListener {
        AnonymousClass153() {
        }

        /* JADX INFO: renamed from: activity.IPCameraActivity$153$1, reason: invalid class name */
        class AnonymousClass1 implements IPanelCallback {
            AnonymousClass1() {
            }

            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(final boolean z, final Object obj) {
                Log.d(IPCameraActivity.this.TAG, "onComplete: ------" + z + "    " + obj.toString());
                new Handler().post(new Runnable() { // from class: activity.IPCameraActivity.153.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Object obj2;
                        if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                            return;
                        }
                        try {
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code")) {
                                if (object.getInteger("code").intValue() != 200) {
                                    IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.153.1.1.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            Toast.makeText(IPCameraActivity.this.getActivity(), R.string.set_failed, 0).show();
                                        }
                                    });
                                } else {
                                    IPCameraActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.153.1.1.2
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            Toast.makeText(IPCameraActivity.this.getActivity(), R.string.set_success, 0).show();
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).setRFSEEDControl(new AnonymousClass1());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshButtonViewText() {
        for (int i = 0; i < this.doorList.size(); i++) {
            String autoName = SharePreferenceManager.getInstance().getAutoName(this.iotId, i);
            if (autoName != null && !"".equals(autoName)) {
                this.doorTextList.get(i).setText(autoName);
            } else {
                this.doorTextList.get(i).setText(this.doorTextList.get(i).getText());
            }
        }
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public void refreshButton() {
        Log.d(this.TAG, "refreshButton: ------------" + this.Page);
        try {
            this.tips.setVisibility(0);
            boolean z = false;
            for (int i = 0; i < this.buttonList.size(); i++) {
                if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 1 && i == 0) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_up));
                } else if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 0 && i == 0) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_up_gray));
                }
                if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 1 && i == 1) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_lock));
                } else if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 0 && i == 1) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_lock_gray));
                }
                if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 1 && i == 2) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_stop));
                } else if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 0 && i == 2) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_stop_gray));
                }
                if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 1 && i == 3) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_down));
                } else if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 0 && i == 3) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.controller_down_gray));
                }
                if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 1 && i == 4) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.doorbell));
                } else if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 0 && i == 4) {
                    this.buttonList.get(i).setBackground(getResources().getDrawable(R.drawable.doorbell_gray));
                }
                if (this.list.get((this.Page * this.controllerSize) + i).intValue() == 1 && !z) {
                    this.tips.setVisibility(8);
                    z = true;
                }
            }
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDialogIpc(final int i) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.162
                @Override // java.lang.Runnable
                public void run() {
                    IPCameraActivity.this.showTest(i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void act(int i) {
        IPCManager.getInstance().getDevice(this.iotId).RFActionControl(i, new IPanelCallback() { // from class: activity.IPCameraActivity.163
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                Log.d(IPCameraActivity.this.TAG, "onComplete: -------" + z);
                IPCameraActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCameraActivity.163.1
                    @Override // java.lang.Runnable
                    public void run() {
                    }
                });
            }
        });
    }

    private void delete() {
        for (int i = 0; i < this.controllerSize; i++) {
            IPCManager.getInstance().getDevice(this.iotId).deleteController((this.Page * this.controllerSize) + i, new IPanelCallback() { // from class: activity.IPCameraActivity.164
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (!z || IPCameraActivity.this.getActivity() == null) {
                        return;
                    }
                    IPCameraActivity.this.getActivity().runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.164.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ((IPCameraActivity) IPCameraActivity.this.getActivity()).getControllerList();
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTest(final int i) {
        new DialogView.Builder(this).setContent(getResources().getString(R.string.learn_mode_tips)).setPositiveClickListener(getResources().getString(R.string.confirm), new DialogView.OnPositiveClickListener() { // from class: activity.IPCameraActivity.166
            @Override // view.DialogView.OnPositiveClickListener
            public void onPositiveClick(DialogView dialogView) {
                TextView tvConfirm = dialogView.getTvConfirm();
                TextView tvDes = dialogView.getTvDes();
                ProgressBar loading = dialogView.getLoading();
                int statusNum = dialogView.getStatusNum();
                if (statusNum != 0) {
                    if (statusNum == 1) {
                        dialogView.dismiss();
                        return;
                    }
                    return;
                }
                loading.setVisibility(0);
                tvConfirm.setText(IPCameraActivity.this.getResources().getString(R.string.finish));
                tvDes.setText(IPCameraActivity.this.getResources().getString(R.string.remote_control_tips));
                tvDes.setGravity(17);
                dialogView.setStatusNum(1);
                dialogView.hideConfirmButton();
                IPCameraActivity.this.addController(i, dialogView);
            }
        }).setNegativeClickListener(getResources().getString(R.string.cancel), new DialogView.OnNegativeClickListener() { // from class: activity.IPCameraActivity.165
            @Override // view.DialogView.OnNegativeClickListener
            public void onNegativeClick(DialogView dialogView) {
                dialogView.dismiss();
            }
        }).build().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void QueryRFKeyStatus(final int i, final DialogView dialogView) {
        new Thread(new Runnable() { // from class: activity.IPCameraActivity.167
            @Override // java.lang.Runnable
            public void run() {
                IPCameraActivity iPCameraActivity = IPCameraActivity.this;
                iPCameraActivity.QueryRFKeyStatusTimes = 0;
                iPCameraActivity.flag = false;
                iPCameraActivity.statusFlag = 2;
                while (IPCameraActivity.this.QueryRFKeyStatusTimes < 140 && IPCameraActivity.this.statusFlag == 2) {
                    long jUptimeMillis = SystemClock.uptimeMillis();
                    if (jUptimeMillis - IPCameraActivity.this.lastOnclickTime1 >= 500) {
                        IPCameraActivity.this.lastOnclickTime1 = jUptimeMillis;
                        IPCManager.getInstance().getDevice(IPCameraActivity.this.iotId).QueryRFKeyStatus(i, new IPanelCallback() { // from class: activity.IPCameraActivity.167.1
                            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                            public void onComplete(boolean z, @Nullable Object obj) {
                                if (z) {
                                    Log.d(IPCameraActivity.this.TAG, "onComplete: -------" + z);
                                    int iIntValue = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(obj)).getString("data")).getInteger("Status").intValue();
                                    if (iIntValue == 1) {
                                        IPCameraActivity.this.statusFlag = iIntValue;
                                        IPCameraActivity.this.flag = true;
                                    } else if (iIntValue == 0) {
                                        IPCameraActivity.this.statusFlag = iIntValue;
                                        IPCameraActivity.this.flag = false;
                                    }
                                }
                            }
                        });
                        IPCameraActivity.this.QueryRFKeyStatusTimes++;
                    }
                }
                if (IPCameraActivity.this.getActivity() != null) {
                    IPCameraActivity.this.getActivity().runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.167.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (IPCameraActivity.this.flag) {
                                dialogView.getStatus().setImageResource(R.drawable.success);
                                dialogView.hideConfirmButton();
                                dialogView.getTvDes().setText(R.string.learn_success);
                                dialogView.getTvCancel().setText(R.string.known);
                                ((IPCameraActivity) IPCameraActivity.this.getActivity()).getControllerList();
                            } else {
                                dialogView.getTvDes().setText(R.string.learn_fail);
                                dialogView.getStatus().setImageResource(R.drawable.error);
                                dialogView.getTvConfirm().setText(R.string.relearn);
                            }
                            dialogView.getLoading().setVisibility(8);
                            dialogView.getStatus().setVisibility(0);
                        }
                    });
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTips() {
        TextView textView = new TextView(this);
        textView.setText(R.string.autor_door_name);
        textView.setTextSize(20.0f);
        textView.setGravity(17);
        textView.setPadding(0, 50, 0, 0);
        textView.setTextColor(getResources().getColor(R.color.color_black));
        View viewInflate = View.inflate(this, R.layout.alertdialog_edittext_view, null);
        final EditText editText = (EditText) viewInflate.findViewById(R.id.editText);
        editText.setHint(R.string.rename);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        editText.setBackground(getResources().getDrawable(R.drawable.edittext_style));
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setCustomTitle(textView).setView(viewInflate).setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() { // from class: activity.IPCameraActivity.169
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ((Activity) Objects.requireNonNull(IPCameraActivity.this.getActivity())).runOnUiThread(new Runnable() { // from class: activity.IPCameraActivity.169.1
                    /* JADX WARN: Removed duplicated region for block: B:7:0x004c  */
                    @Override // java.lang.Runnable
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    public void run() {
                        /*
                            r4 = this;
                            tools.SharePreferenceManager r0 = tools.SharePreferenceManager.getInstance()
                            activity.IPCameraActivity$169 r1 = activity.IPCameraActivity.AnonymousClass169.this
                            activity.IPCameraActivity r1 = activity.IPCameraActivity.this
                            java.lang.String r1 = activity.IPCameraActivity.access$1100(r1)
                            activity.IPCameraActivity$169 r2 = activity.IPCameraActivity.AnonymousClass169.this
                            android.widget.EditText r2 = r2
                            android.text.Editable r2 = r2.getText()
                            java.lang.String r2 = r2.toString()
                            activity.IPCameraActivity$169 r3 = activity.IPCameraActivity.AnonymousClass169.this
                            activity.IPCameraActivity r3 = activity.IPCameraActivity.this
                            int r3 = activity.IPCameraActivity.access$11900(r3)
                            r0.saveAutoName(r1, r2, r3)
                            tools.SharePreferenceManager r0 = tools.SharePreferenceManager.getInstance()
                            activity.IPCameraActivity$169 r1 = activity.IPCameraActivity.AnonymousClass169.this
                            activity.IPCameraActivity r1 = activity.IPCameraActivity.this
                            java.lang.String r1 = activity.IPCameraActivity.access$1100(r1)
                            activity.IPCameraActivity$169 r2 = activity.IPCameraActivity.AnonymousClass169.this
                            activity.IPCameraActivity r2 = activity.IPCameraActivity.this
                            int r2 = activity.IPCameraActivity.access$11900(r2)
                            java.lang.String r0 = r0.getAutoName(r1, r2)
                            activity.IPCameraActivity$169 r1 = activity.IPCameraActivity.AnonymousClass169.this
                            activity.IPCameraActivity r1 = activity.IPCameraActivity.this
                            android.widget.TextView r1 = r1.door_text
                            if (r0 == 0) goto L4c
                            java.lang.String r2 = ""
                            boolean r2 = r0.equals(r2)
                            if (r2 != 0) goto L4c
                            goto L66
                        L4c:
                            activity.IPCameraActivity$169 r0 = activity.IPCameraActivity.AnonymousClass169.this
                            activity.IPCameraActivity r0 = activity.IPCameraActivity.this
                            java.util.List r0 = activity.IPCameraActivity.access$12700(r0)
                            activity.IPCameraActivity$169 r2 = activity.IPCameraActivity.AnonymousClass169.this
                            activity.IPCameraActivity r2 = activity.IPCameraActivity.this
                            int r2 = activity.IPCameraActivity.access$11900(r2)
                            java.lang.Object r0 = r0.get(r2)
                            android.widget.TextView r0 = (android.widget.TextView) r0
                            java.lang.CharSequence r0 = r0.getText()
                        L66:
                            r1.setText(r0)
                            activity.IPCameraActivity$169 r0 = activity.IPCameraActivity.AnonymousClass169.this
                            activity.IPCameraActivity r0 = activity.IPCameraActivity.this
                            activity.IPCameraActivity.access$13000(r0)
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: activity.IPCameraActivity.AnonymousClass169.AnonymousClass1.run():void");
                    }
                });
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: activity.IPCameraActivity.168
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
        alertDialogCreate.setCanceledOnTouchOutside(false);
        alertDialogCreate.show();
        alertDialogCreate.getButton(-1).setTextColor(Color.parseColor("#2c99fd"));
        alertDialogCreate.getButton(-2).setTextColor(Color.parseColor("#E92E2E"));
        final Button button = alertDialogCreate.getButton(-1);
        Button button2 = alertDialogCreate.getButton(-2);
        button.setEnabled(false);
        button.setTextSize(18.0f);
        button2.setTextSize(18.0f);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.weight = 13.0f;
        button.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) button2.getLayoutParams();
        layoutParams2.weight = 13.0f;
        button.setLayoutParams(layoutParams2);
        editText.addTextChangedListener(new TextWatcher() { // from class: activity.IPCameraActivity.170
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                button.setEnabled((editable == null || editable.toString().equals("")) ? false : true);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addController(final int i, final DialogView dialogView) {
        IPCManager.getInstance().getDevice(this.iotId).AddController(i, (i + 1) % 5 == 0 ? 1 : 0, new IPanelCallback() { // from class: activity.IPCameraActivity.171
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (z) {
                    Log.d(IPCameraActivity.this.TAG, "onComplete: -------" + z);
                    if (JSONObject.parseObject(String.valueOf(obj)).getIntValue("code") == 200) {
                        IPCameraActivity.this.QueryRFKeyStatus(i, dialogView);
                    }
                }
            }
        });
    }
}
