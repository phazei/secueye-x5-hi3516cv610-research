package activity;

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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
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
import com.google.gson.Gson;
import com.hjq.permissions.Permission;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityIpcameraDoubleEyeLayoutBinding;
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
import fragment.MoreFragmentDoubleEye;
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
import tools.DensityUtils;
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
import view.CircleTooView;
import view.DialogView;
import view.FourPicturesView;
import view.IPCTitleView;
import view.JoystickTouchViewListener;
import view.MyGlTextureView;
import view.SelectorDialogFragment;
import view.ShadowButton;
import view.TouchView;
import view.WhiteProgressDialog;
import view.ZoomableTextureView;

/* JADX INFO: loaded from: classes.dex */
public class IPCDoubleEyeActivity extends CommonActivity implements ControllerFragment.Mylistener, FourPicturesView.ShowFloat, LensControllerFragment.listener, MoreFragmentDoubleEye.MyBackListener, MoreFragmentDoubleEye.FragmentContextChangeListener, PresetFragment.PresetBackListener, PresetFragment.PresetDataChange, OldPresetFragment.OldPresetBackListener, CircleTooView.setOnLister {
    private static String[] PERMISSIONS_STORAGE = {Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String DeviceName;
    private String IccId;
    private String ProductKey;
    private int WifiConfigIsExist;
    boolean ZoomIsMax;
    int ZoomMax;
    String address;
    LinearLayout back;
    Badge badge;
    private Timer batteryTimer;
    private DeviceInfoBean beanInfo;
    private ActivityIpcameraDoubleEyeLayoutBinding binding;
    ControllerFragment controllerFragment;
    private CountDownTimer countDownTimer;
    private int currentInfrarred;
    private int currentInfrarred2;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;

    /* JADX INFO: renamed from: dialog, reason: collision with root package name */
    private AlertDialog f1574dialog;
    TextView door_text;
    private int faceDetectionAbility;
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
    private MoreFragmentDoubleEye moreFragment;
    private boolean netVisible;
    private SelectorDialogFragment nightModeFragment;
    private DeviceInfoBean nvrDevice;
    private OldPresetFragment oldPresetFragment;
    private Timer onTouchTimer;
    private LivePlayer playBall;
    private LivePlayer playGun;
    private PresetFragment presetFragment;
    private List<Integer> presetList;
    private Timer ptzTimer;
    private String selectIotId;
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
    private double viewHeight;
    private String wakeUpData;
    private WhiteProgressDialog whiteProgressDialog;
    private List<WifiBean> wifiBeanList;
    private String title = "";
    private String iotId = "";
    private String iotId2 = "";
    private String appKey = "";
    private int lowPowerMode = -1;
    private boolean speakerSwitch = false;
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
    private int countWakeUp = 0;
    private int strongRemind = 0;
    private boolean needTFInit = true;
    private List<String> nightModelList = new ArrayList();
    private List<String> mapList = new ArrayList();
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
    private String nvrIotId = "";
    private String gunIotId = "";
    private String ballIotId = "";
    private int nvrOwner = 0;
    private int showMode = 0;
    private List<String> definitionList = new ArrayList();
    private boolean isRatio = false;
    private boolean havePermission = false;
    private boolean isLiveIntercoming = false;
    private Handler wakeUpHandler = new AnonymousClass98();
    private SharePreferenceManager.OnCallSetListener definitionChangeListener = new AnonymousClass100();
    ViewTreeObserver.OnGlobalLayoutListener nGlobalLayoutListener = new AnonymousClass105();
    float nowScale = 0.0f;
    private boolean isFloat = false;
    final Runnable updatePlayInfoTimerTask = new Runnable() { // from class: activity.IPCDoubleEyeActivity.107
        @Override // java.lang.Runnable
        public void run() {
            IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.107.1
                @Override // java.lang.Runnable
                public void run() {
                    if (IPCDoubleEyeActivity.this.isFinishing()) {
                        return;
                    }
                    IPCDoubleEyeActivity.this.updatePlayInfo();
                }
            });
        }
    };
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private long lastOnclickTime = 0;
    int i = 0;
    List<Integer> controllerList = new ArrayList<Integer>() { // from class: activity.IPCDoubleEyeActivity.116
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
        return R.layout.activity_ipcamera_double_eye_layout;
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void lightMode() {
    }

    static /* synthetic */ int access$608(IPCDoubleEyeActivity iPCDoubleEyeActivity) {
        int i = iPCDoubleEyeActivity.showMode;
        iPCDoubleEyeActivity.showMode = i + 1;
        return i;
    }

    static /* synthetic */ int access$8408(IPCDoubleEyeActivity iPCDoubleEyeActivity) {
        int i = iPCDoubleEyeActivity.countWakeUp;
        iPCDoubleEyeActivity.countWakeUp = i + 1;
        return i;
    }

    static /* synthetic */ int access$8510(IPCDoubleEyeActivity iPCDoubleEyeActivity) {
        int i = iPCDoubleEyeActivity.is1100ErrorPre;
        iPCDoubleEyeActivity.is1100ErrorPre = i - 1;
        return i;
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
        int intValue;
        if (setproperties.getData().contains(Constants.LowPowerStatus)) {
            Log.e("属性监听", "LowPowerStatus ");
            JSONObject object = JSONObject.parseObject(String.valueOf(setproperties.getData()));
            String string = object.getString("iotId");
            if (this.ballIotId != null && !string.isEmpty() && string.equals(this.ballIotId) && (intValue = object.getJSONObject("items").getJSONObject(Constants.LowPowerMode).getJSONObject("value").getIntValue(Constants.LowPowerStatus)) == 1) {
                this.uiHandler.post(new Runnable() { // from class: activity.-$$Lambda$IPCDoubleEyeActivity$HpQ13ZTnkaTosDoTnY3C8UEk5w4
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
        String[] strArr;
        super.initWidget(bundle);
        this.binding = (ActivityIpcameraDoubleEyeLayoutBinding) DataBindingUtil.setContentView(this, R.layout.activity_ipcamera_double_eye_layout);
        setEdgeToEdge(this.binding.maxLayout);
        NotchScreenManager.getInstance().setDisplayInNotch(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.binding.maxLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: activity.IPCDoubleEyeActivity.1
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                IPCDoubleEyeActivity.this.binding.maxLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                int width = IPCDoubleEyeActivity.this.binding.maxLayout.getWidth();
                int height = IPCDoubleEyeActivity.this.binding.maxLayout.getHeight();
                double dDoubleValue = new BigDecimal(width / height).setScale(2, 4).doubleValue();
                Log.e("屏幕", "宽高比例" + dDoubleValue);
                if (dDoubleValue >= 0.49d) {
                    IPCDoubleEyeActivity.this.isRatio = true;
                    IPCDoubleEyeActivity.this.viewHeight = height / 2;
                    Log.e("屏幕", "宽=" + width + " 高=" + height + " viewHeight=" + IPCDoubleEyeActivity.this.viewHeight);
                    IPCDoubleEyeActivity.setViewLayoutParams(IPCDoubleEyeActivity.this.binding.portraitPlayer, -1, (int) IPCDoubleEyeActivity.this.viewHeight);
                    IPCDoubleEyeActivity.setViewLayoutParams(IPCDoubleEyeActivity.this.binding.player2, (((int) (IPCDoubleEyeActivity.this.viewHeight / 2.0d)) / 9) * 16, (int) (IPCDoubleEyeActivity.this.viewHeight / 2.0d));
                    IPCDoubleEyeActivity.setViewLayoutParams(IPCDoubleEyeActivity.this.binding.player, (((int) (IPCDoubleEyeActivity.this.viewHeight / 2.0d)) / 9) * 16, (int) (IPCDoubleEyeActivity.this.viewHeight / 2.0d));
                    IPCDoubleEyeActivity.setViewLayoutParams(IPCDoubleEyeActivity.this.binding.lineViewItem, (((int) (IPCDoubleEyeActivity.this.viewHeight / 2.0d)) / 9) * 16, (int) (IPCDoubleEyeActivity.this.viewHeight / 2.0d));
                } else {
                    IPCDoubleEyeActivity.this.isRatio = false;
                    IPCDoubleEyeActivity.this.viewHeight = (width / 8) * 9;
                    Log.e("屏幕", "宽=" + width + " 高=" + height + " viewHeight=" + IPCDoubleEyeActivity.this.viewHeight);
                    IPCDoubleEyeActivity.setViewLayoutParams(IPCDoubleEyeActivity.this.binding.portraitPlayer, -1, (int) IPCDoubleEyeActivity.this.viewHeight);
                    IPCDoubleEyeActivity.setViewLayoutParams(IPCDoubleEyeActivity.this.binding.player2, -1, (int) (IPCDoubleEyeActivity.this.viewHeight / 2.0d));
                    IPCDoubleEyeActivity.setViewLayoutParams(IPCDoubleEyeActivity.this.binding.player, -1, (int) (IPCDoubleEyeActivity.this.viewHeight / 2.0d));
                    IPCDoubleEyeActivity.setViewLayoutParams(IPCDoubleEyeActivity.this.binding.lineViewItem, -1, (int) (IPCDoubleEyeActivity.this.viewHeight / 2.0d));
                }
                IPCDoubleEyeActivity.this.addControlTouchView(false);
                return true;
            }
        });
        this.binding.llCapture.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCDoubleEyeActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCDoubleEyeActivity.this.snapshot();
            }
        });
        this.binding.llRecord.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCDoubleEyeActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCDoubleEyeActivity.this.startOrStopRecordingMp4();
            }
        });
        this.binding.fullSwitchWindow.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCDoubleEyeActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCDoubleEyeActivity.access$608(IPCDoubleEyeActivity.this);
                if (IPCDoubleEyeActivity.this.showMode > 2) {
                    IPCDoubleEyeActivity.this.showMode = 0;
                }
                IPCDoubleEyeActivity.this.moreFragment.updateData(IPCDoubleEyeActivity.this.isDetecting, IPCDoubleEyeActivity.this.lightVisible, SharePreferenceManager.getInstance().getSupport4G(IPCDoubleEyeActivity.this.device.getIotId()) == 1, IPCDoubleEyeActivity.this.currentInfrarred2, IPCDoubleEyeActivity.this.switchText, IPCDoubleEyeActivity.this.shopVisible, IPCDoubleEyeActivity.this.smartDoorVisible, IPCDoubleEyeActivity.this.isFourState, IPCDoubleEyeActivity.this.supportMotionDetect, IPCDoubleEyeActivity.this.showMode);
                IPCDoubleEyeActivity.this.setFullScreen();
            }
        });
        this.binding.llFull.setOnClickListener(new OnMultiClickListener() { // from class: activity.IPCDoubleEyeActivity.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IPCDoubleEyeActivity.this.doubleSameWindow(0);
            }
        });
        this.binding.btPresetInvoke.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCDoubleEyeActivity.this.getActivity() == null) {
                    return;
                }
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.hideKeyboard(iPCDoubleEyeActivity.getActivity());
                if ("".equals(((Editable) Objects.requireNonNull(IPCDoubleEyeActivity.this.binding.etPreset.getText())).toString())) {
                    return;
                }
                IPCManager.getInstance().getDevice(IPCDoubleEyeActivity.this.iotId).changePresetLocation(Integer.parseInt(IPCDoubleEyeActivity.this.binding.etPreset.getText().toString()), new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.6.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode();
                        }
                    }
                });
            }
        });
        this.binding.btPresetAdd.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCDoubleEyeActivity.this.getActivity() == null) {
                    return;
                }
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.hideKeyboard(iPCDoubleEyeActivity.getActivity());
                if ("".equals(((Editable) Objects.requireNonNull(IPCDoubleEyeActivity.this.binding.etPreset.getText())).toString())) {
                    return;
                }
                IPCManager.getInstance().getDevice(IPCDoubleEyeActivity.this.iotId).addPresetLocation(Integer.parseInt(IPCDoubleEyeActivity.this.binding.etPreset.getText().toString()), new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.7.1
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        if (z) {
                            ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode();
                        }
                    }
                });
            }
        });
        this.binding.ivCharge4gFlow.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.isSelected()) {
                    IPCDoubleEyeActivity.this.isNet4GSwitch();
                    return;
                }
                Intent intent = new Intent(IPCDoubleEyeActivity.this.getActivity(), (Class<?>) YunDoubleSelectActivity.class);
                intent.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCDoubleEyeActivity.this.device);
                intent.putExtra("device1", IPCDoubleEyeActivity.this.device1);
                intent.putExtra("nvrDevice", IPCDoubleEyeActivity.this.nvrDevice);
                IPCDoubleEyeActivity.this.startActivity(intent);
            }
        });
        this.binding.ptzText.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.binding.rlTouchView.setVisibility(0);
                IPCDoubleEyeActivity.this.binding.ZOOMView.setVisibility(8);
                IPCDoubleEyeActivity.this.binding.autorView.setVisibility(8);
                IPCDoubleEyeActivity.this.binding.ptzText.setBackgroundResource(R.drawable.text_underline);
                IPCDoubleEyeActivity.this.binding.zoomText.setBackgroundResource(0);
            }
        });
        this.binding.zoomText.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.binding.ptzText.setBackgroundResource(0);
                IPCDoubleEyeActivity.this.binding.zoomText.setBackgroundResource(R.drawable.text_underline);
                IPCDoubleEyeActivity.this.binding.rlTouchView.setVisibility(8);
                IPCDoubleEyeActivity.this.binding.ZOOMView.setVisibility(0);
                IPCDoubleEyeActivity.this.binding.autorView.setVisibility(8);
            }
        });
        this.binding.tvZoomBack.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.binding.rlTouchView.setVisibility(0);
                IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.setVisibility(0);
                Log.e("云存=", "" + SharePreferenceManager.getInstance().getEventRecord(IPCDoubleEyeActivity.this.iotId) + "  4G=" + SharePreferenceManager.getInstance().getSupport4G(IPCDoubleEyeActivity.this.device.getIotId()));
                if (SharePreferenceManager.getInstance().getEventRecord(IPCDoubleEyeActivity.this.iotId) != 1 && SharePreferenceManager.getInstance().getSupport4G(IPCDoubleEyeActivity.this.device.getIotId()) != 1) {
                    IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                }
                if (!AppConfig.isChina && !IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.isSelected()) {
                    IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                }
                IPCDoubleEyeActivity.this.binding.ZOOMView.setVisibility(8);
                IPCDoubleEyeActivity.this.binding.autorView.setVisibility(8);
                IPCDoubleEyeActivity.this.binding.ptzText.setBackgroundResource(R.drawable.text_underline);
                IPCDoubleEyeActivity.this.binding.zoomText.setBackgroundResource(0);
            }
        });
        this.binding.ivLightWhile.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.12
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCDoubleEyeActivity.this.showMode == 0) {
                    IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                    iPCDoubleEyeActivity.selectIotId = iPCDoubleEyeActivity.iotId;
                }
                IPCDoubleEyeActivity.this.currentInfrarred2 = SharePreferenceManager.getInstance().getDayNightMode(IPCDoubleEyeActivity.this.iotId2);
                IPCDoubleEyeActivity.this.currentInfrarred = SharePreferenceManager.getInstance().getDayNightMode(IPCDoubleEyeActivity.this.iotId);
                int i = 0;
                int i2 = 0;
                for (int i3 = 0; i3 < IPCDoubleEyeActivity.this.nightModelList.size(); i3++) {
                    if (((String) IPCDoubleEyeActivity.this.nightModelList.get(i3)).equals(IPCDoubleEyeActivity.this.infrarredMode[IPCDoubleEyeActivity.this.currentInfrarred])) {
                        i = i3;
                    }
                    if (((String) IPCDoubleEyeActivity.this.nightModelList.get(i3)).equals(IPCDoubleEyeActivity.this.infrarredMode[IPCDoubleEyeActivity.this.currentInfrarred2])) {
                        i2 = i3;
                    }
                }
                SelectorDialogFragment selectorDialogFragment = IPCDoubleEyeActivity.this.nightModeFragment;
                FragmentManager supportFragmentManager = IPCDoubleEyeActivity.this.getSupportFragmentManager();
                if (IPCDoubleEyeActivity.this.selectIotId.equals(IPCDoubleEyeActivity.this.iotId2)) {
                    i = i2;
                }
                selectorDialogFragment.showAllowingStateLoss(supportFragmentManager, "", i);
                SettingsCtrl.getInstance().getProperties(IPCDoubleEyeActivity.this.iotId2, new MyCallback() { // from class: activity.IPCDoubleEyeActivity.12.1
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
                SettingsCtrl.getInstance().getProperties(IPCDoubleEyeActivity.this.iotId, new MyCallback() { // from class: activity.IPCDoubleEyeActivity.12.2
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
                SettingsCtrl.getInstance().getProperties(IPCDoubleEyeActivity.this.nvrIotId, new MyCallback() { // from class: activity.IPCDoubleEyeActivity.12.3
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
            }
        });
        this.binding.ivLightWhile2.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.13
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.selectIotId = iPCDoubleEyeActivity.iotId2;
                IPCDoubleEyeActivity.this.currentInfrarred2 = SharePreferenceManager.getInstance().getDayNightMode(IPCDoubleEyeActivity.this.iotId2);
                IPCDoubleEyeActivity.this.currentInfrarred = SharePreferenceManager.getInstance().getDayNightMode(IPCDoubleEyeActivity.this.iotId);
                int i = 0;
                int i2 = 0;
                for (int i3 = 0; i3 < IPCDoubleEyeActivity.this.nightModelList.size(); i3++) {
                    if (((String) IPCDoubleEyeActivity.this.nightModelList.get(i3)).equals(IPCDoubleEyeActivity.this.infrarredMode[IPCDoubleEyeActivity.this.currentInfrarred])) {
                        i = i3;
                    }
                    if (((String) IPCDoubleEyeActivity.this.nightModelList.get(i3)).equals(IPCDoubleEyeActivity.this.infrarredMode[IPCDoubleEyeActivity.this.currentInfrarred2])) {
                        i2 = i3;
                    }
                }
                SelectorDialogFragment selectorDialogFragment = IPCDoubleEyeActivity.this.nightModeFragment;
                FragmentManager supportFragmentManager = IPCDoubleEyeActivity.this.getSupportFragmentManager();
                if (IPCDoubleEyeActivity.this.selectIotId.equals(IPCDoubleEyeActivity.this.iotId2)) {
                    i = i2;
                }
                selectorDialogFragment.showAllowingStateLoss(supportFragmentManager, "", i);
                SettingsCtrl.getInstance().getProperties(IPCDoubleEyeActivity.this.iotId2, new MyCallback() { // from class: activity.IPCDoubleEyeActivity.13.1
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
                SettingsCtrl.getInstance().getProperties(IPCDoubleEyeActivity.this.iotId, new MyCallback() { // from class: activity.IPCDoubleEyeActivity.13.2
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
                SettingsCtrl.getInstance().getProperties(IPCDoubleEyeActivity.this.nvrIotId, new MyCallback() { // from class: activity.IPCDoubleEyeActivity.13.3
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
            }
        });
        this.binding.zoomReduceBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.14
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.zoomReduceBtn.setBackgroundResource(R.drawable.reduce_press_ipc);
                    if (IPCDoubleEyeActivity.this.onTouchTimer == null) {
                        IPCDoubleEyeActivity.this.onTouchTimer = new Timer();
                        IPCDoubleEyeActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCDoubleEyeActivity.14.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCDoubleEyeActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCDoubleEyeActivity.this.binding.zoomReduceBtn.setBackgroundResource(R.drawable.reduce_ipc);
                    if (IPCDoubleEyeActivity.this.onTouchTimer != null) {
                        IPCDoubleEyeActivity.this.onTouchTimer.cancel();
                        IPCDoubleEyeActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.btZoomReduceBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.15
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.btZoomReduceBtn.setBackgroundResource(R.drawable.reduce_press_ipc);
                    if (IPCDoubleEyeActivity.this.onTouchTimer == null) {
                        IPCDoubleEyeActivity.this.onTouchTimer = new Timer();
                        IPCDoubleEyeActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCDoubleEyeActivity.15.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCDoubleEyeActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCDoubleEyeActivity.this.binding.btZoomReduceBtn.setBackgroundResource(R.drawable.reduce_ipc);
                    if (IPCDoubleEyeActivity.this.onTouchTimer != null) {
                        IPCDoubleEyeActivity.this.onTouchTimer.cancel();
                        IPCDoubleEyeActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.zoomAddBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.16
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.zoomAddBtn.setBackgroundResource(R.drawable.add_press_ipc);
                    if (IPCDoubleEyeActivity.this.onTouchTimer == null) {
                        IPCDoubleEyeActivity.this.onTouchTimer = new Timer();
                        IPCDoubleEyeActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCDoubleEyeActivity.16.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCDoubleEyeActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCDoubleEyeActivity.this.binding.zoomAddBtn.setBackgroundResource(R.drawable.add_ipc);
                    if (IPCDoubleEyeActivity.this.onTouchTimer != null) {
                        IPCDoubleEyeActivity.this.onTouchTimer.cancel();
                        IPCDoubleEyeActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.btZoomAddBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.17
            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.btZoomAddBtn.setBackgroundResource(R.drawable.add_press_ipc);
                    if (IPCDoubleEyeActivity.this.onTouchTimer == null) {
                        IPCDoubleEyeActivity.this.onTouchTimer = new Timer();
                        IPCDoubleEyeActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCDoubleEyeActivity.17.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCDoubleEyeActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    IPCDoubleEyeActivity.this.binding.btZoomAddBtn.setBackgroundResource(R.drawable.add_ipc);
                    if (IPCDoubleEyeActivity.this.onTouchTimer != null) {
                        IPCDoubleEyeActivity.this.onTouchTimer.cancel();
                        IPCDoubleEyeActivity.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.binding.focusReduceBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.18
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.changeFocus(0);
            }
        });
        this.binding.focusAddBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.19
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.changeFocus(1);
            }
        });
        this.switch_4gArr = getResources().getStringArray(R.array.switch_4g);
        this.switch4gFragment = new SelectorDialogFragment(getString(R.string.network_change), true, this.switch_4gArr);
        this.switch4gFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.IPCDoubleEyeActivity.20
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                if (i != 0 || IPCDoubleEyeActivity.this.wifiFourPosition == i) {
                    if (i != 1 || IPCDoubleEyeActivity.this.wifiFourPosition == i) {
                        if (i != 2 || IPCDoubleEyeActivity.this.wifiFourPosition == i) {
                            return;
                        }
                        IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                        iPCDoubleEyeActivity.switch4gMode(iPCDoubleEyeActivity.getString(R.string.Net4GEnableSwitch), i);
                        IPCDoubleEyeActivity.this.FourGChangeDialog(i);
                        return;
                    }
                    if (IPCDoubleEyeActivity.this.WifiConfigIsExist == 0) {
                        WiFiListActivity.start(IPCDoubleEyeActivity.this.getActivity(), IPCDoubleEyeActivity.this.iotId, "1");
                        return;
                    } else {
                        if (IPCDoubleEyeActivity.this.WifiConfigIsExist == 1) {
                            IPCDoubleEyeActivity iPCDoubleEyeActivity2 = IPCDoubleEyeActivity.this;
                            iPCDoubleEyeActivity2.switch4gMode(iPCDoubleEyeActivity2.getString(R.string.Net4GEnableSwitch), i);
                            IPCDoubleEyeActivity.this.FourGChangeDialog(i);
                            return;
                        }
                        return;
                    }
                }
                IPCDoubleEyeActivity iPCDoubleEyeActivity3 = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity3.switch4gMode(iPCDoubleEyeActivity3.getString(R.string.Net4GEnableSwitch), i);
                IPCDoubleEyeActivity.this.FourGChangeDialog(i);
            }
        });
        this.inputDialogView = new InputDialogViewIpc.Builder().build();
        this.inputDialogView.addOnClickListener(new AnonymousClass21());
        if (AppConfig.isChina) {
            strArr = new String[]{"高德地图", "百度地图"};
        } else {
            strArr = new String[]{"Google Map"};
        }
        this.mapFragment = new SelectorDialogFragment("" + getResources().getString(R.string.select_map), strArr);
        this.mapFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.IPCDoubleEyeActivity.22
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                switch (i) {
                    case 0:
                        if (AppConfig.isChina) {
                            if (MapUtils.isAvilible(IPCDoubleEyeActivity.this, "com.autonavi.minimap")) {
                                try {
                                    StringBuffer stringBuffer = new StringBuffer("androidamap://navi?sourceApplication=");
                                    stringBuffer.append("yitu8_driver");
                                    stringBuffer.append("&lat=");
                                    stringBuffer.append(IPCDoubleEyeActivity.this.lat);
                                    stringBuffer.append("&lon=");
                                    stringBuffer.append(IPCDoubleEyeActivity.this.lon);
                                    stringBuffer.append("&dev=");
                                    stringBuffer.append(1);
                                    stringBuffer.append("&style=");
                                    stringBuffer.append(0);
                                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer.toString()));
                                    intent.addCategory("android.intent.category.DEFAULT");
                                    intent.setPackage("com.autonavi.minimap");
                                    IPCDoubleEyeActivity.this.startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return;
                                }
                            } else {
                                Toast.makeText(IPCDoubleEyeActivity.this, "您尚未安装高德地图", 1).show();
                            }
                        } else if (MapUtils.isAvilible(IPCDoubleEyeActivity.this, "com.google.android.apps.maps")) {
                            Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse("google.navigation:q=" + IPCDoubleEyeActivity.this.lat + "," + IPCDoubleEyeActivity.this.lon + ", + Sydney +Australia"));
                            intent2.setPackage("com.google.android.apps.maps");
                            IPCDoubleEyeActivity.this.startActivity(intent2);
                        } else {
                            Toast.makeText(IPCDoubleEyeActivity.this, IPCDoubleEyeActivity.this.getString(R.string.not_installed) + "Google Map", 1).show();
                            IPCDoubleEyeActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.google.android.apps.maps")));
                        }
                        break;
                    case 1:
                        if (MapUtils.isAvilible(IPCDoubleEyeActivity.this, "com.baidu.BaiduMap")) {
                            try {
                                StringBuffer stringBuffer2 = new StringBuffer("baidumap://map/navi?location=");
                                stringBuffer2.append(IPCDoubleEyeActivity.this.lat);
                                stringBuffer2.append(",");
                                stringBuffer2.append(IPCDoubleEyeActivity.this.lon);
                                stringBuffer2.append("&type=TIME");
                                Intent intent3 = new Intent("android.intent.action.VIEW", Uri.parse(stringBuffer2.toString()));
                                intent3.setPackage("com.baidu.BaiduMap");
                                IPCDoubleEyeActivity.this.startActivity(intent3);
                            } catch (Exception e2) {
                                Log.e("intent", e2.getMessage());
                                return;
                            }
                        } else {
                            Toast.makeText(IPCDoubleEyeActivity.this, "您尚未安装百度地图", 1).show();
                        }
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$21, reason: invalid class name */
    class AnonymousClass21 implements InputDialogViewIpc.OnClickListener {
        AnonymousClass21() {
        }

        @Override // dialog.InputDialogViewIpc.OnClickListener
        public void onPositiveClick(String str, Object obj) {
            IPCDoubleEyeActivity.this.showProgressDialog();
            IPCManager.getInstance().getDevice(IPCDoubleEyeActivity.this.iotId).setAPList(IPCDoubleEyeActivity.this.selectSsid, str, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.21.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (z) {
                        try {
                            if (obj2 == null) {
                                IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.21.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ToastUtils.toast(IPCDoubleEyeActivity.this.getActivity(), IPCDoubleEyeActivity.this.getString(R.string.set_wifi_failed));
                                    }
                                });
                            } else if (((IoTResponse) JSON.parseObject(obj2.toString()).toJavaObject(IoTResponse.class)).getCode() != 200) {
                                IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.21.1.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ToastUtils.toast(IPCDoubleEyeActivity.this.getActivity(), IPCDoubleEyeActivity.this.getString(R.string.set_wifi_failed));
                                    }
                                });
                            } else {
                                IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.21.1.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        IPCDoubleEyeActivity.this.connect();
                                    }
                                });
                            }
                        } finally {
                            IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.21.1.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    IPCDoubleEyeActivity.this.dismissProgressDialog();
                                }
                            });
                        }
                    }
                }
            });
        }

        @Override // dialog.InputDialogViewIpc.OnClickListener
        public void onNegativeClick() {
            IPCDoubleEyeActivity.this.inputDialogView.dismiss();
            IPCDoubleEyeActivity.this.f1574dialog.show();
        }
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        Bundle extras = getIntent().getExtras();
        int i = 0;
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
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
                this.nvrIotId = extras.getString("nvrIotId");
                Log.e(this.TAG, "接收iotId:" + this.nvrIotId);
                this.gunIotId = extras.getString("gunIotId");
                this.ballIotId = extras.getString("ballIotId");
                this.nvrOwner = getIntent().getIntExtra("nvrOwner", 0);
            }
            DeviceInfoBean deviceInfoBean2 = this.device1;
            if (deviceInfoBean2 != null) {
                this.iotId2 = deviceInfoBean2.getIotId();
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
        this.nightModeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.IPCDoubleEyeActivity.23
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v10, types: [int] */
            /* JADX WARN: Type inference failed for: r0v12 */
            /* JADX WARN: Type inference failed for: r0v13 */
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i3) {
                ((String) IPCDoubleEyeActivity.this.nightModelList.get(i3)).equals(IPCDoubleEyeActivity.this.infrarredMode[0]);
                ?? Equals = ((String) IPCDoubleEyeActivity.this.nightModelList.get(i3)).equals(IPCDoubleEyeActivity.this.infrarredMode[1]);
                if (((String) IPCDoubleEyeActivity.this.nightModelList.get(i3)).equals(IPCDoubleEyeActivity.this.infrarredMode[2])) {
                    Equals = 2;
                }
                IPCDoubleEyeActivity.this.updateNightMode(Integer.valueOf((int) Equals), IPCDoubleEyeActivity.this.selectIotId);
            }
        });
    }

    private void getBatteryPercentageAndCuTemperature() {
        if (this.batteryTimer == null) {
            this.batteryTimer = new Timer();
        }
        this.batteryTimer.schedule(new AnonymousClass24(), 0L, 60000L);
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$24, reason: invalid class name */
    class AnonymousClass24 extends TimerTask {
        AnonymousClass24() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            IPCManager.getInstance().getDevice(IPCDoubleEyeActivity.this.iotId).GetBatteryPercentage(new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.24.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (z) {
                        try {
                            final int intValue = ((JSONObject) JSONObject.parseObject(String.valueOf(obj)).get("data")).getIntValue("Value");
                            IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.24.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    IPCDoubleEyeActivity.this.binding.SensorView.setPowerDisplay(intValue != 101);
                                    IPCDoubleEyeActivity.this.binding.SensorView.setPower(intValue);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            IPCManager.getInstance().getDevice(IPCDoubleEyeActivity.this.iotId).GetCuTemperature(new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.24.2
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj) {
                    if (z) {
                        try {
                            final int intValue = ((JSONObject) JSONObject.parseObject(String.valueOf(obj)).get("data")).getIntValue("Value");
                            IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.24.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    IPCDoubleEyeActivity.this.binding.SensorView.setTemperatureDisplay(intValue != 101);
                                    IPCDoubleEyeActivity.this.binding.SensorView.setTemperature(intValue);
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
        Resources resources;
        int i;
        super.onCreate(bundle);
        this.definitionList.add(getString(R.string.quality_l));
        this.definitionList.add(getString(R.string.quality_m));
        this.definitionList.add(getString(R.string.quality_h));
        this.uiHandler = new Handler(getMainLooper());
        this.whiteProgressDialog = new WhiteProgressDialog(this);
        this.binding.tvTitle.bringToFront();
        this.binding.tvTitle.setLineViewId(0);
        this.binding.tvTitle.setTitleText(this.title);
        this.binding.tvTitle.setOnViewClick(new IPCTitleView.OnViewClick() { // from class: activity.IPCDoubleEyeActivity.25
            @Override // view.IPCTitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.IPCTitleView.OnViewClick
            public void OnLeftClick(View view2) {
                IPCDoubleEyeActivity.this.onBackPressed();
            }
        });
        this.binding.tvTitle.setOnRightImageClick(new IPCTitleView.OnImageClick() { // from class: activity.IPCDoubleEyeActivity.26
            @Override // view.IPCTitleView.OnImageClick
            public void OnRightImageClick(View view2) {
                Intent intent = new Intent(IPCDoubleEyeActivity.this, (Class<?>) SettingsActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCDoubleEyeActivity.this.device);
                bundle2.putSerializable("device1", IPCDoubleEyeActivity.this.device1);
                bundle2.putSerializable("nvrDevice", IPCDoubleEyeActivity.this.nvrDevice);
                intent.putExtras(bundle2);
                IPCDoubleEyeActivity.this.startActivity(intent);
            }
        });
        int i2 = (int) ((ScreenUtil.getDisplayMetrics(getActivity())[0] * 9.0f) / 16.0f);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-1, i2);
        layoutParams.height = i2;
        layoutParams.topToTop = this.binding.portraitPlayer.getId();
        layoutParams.startToStart = this.binding.portraitPlayer.getId();
        layoutParams.endToEnd = this.binding.portraitPlayer.getId();
        layoutParams.bottomToTop = this.binding.player.getId();
        this.binding.player2.setLayoutParams(layoutParams);
        ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(-1, i2);
        layoutParams2.height = i2;
        layoutParams2.topToTop = this.binding.portraitPlayer.getId();
        layoutParams2.startToStart = this.binding.portraitPlayer.getId();
        layoutParams2.endToEnd = this.binding.portraitPlayer.getId();
        layoutParams2.setMargins(0, 25, 0, 0);
        this.binding.lineViewItem.setLayoutParams(layoutParams2);
        ConstraintLayout.LayoutParams layoutParams3 = new ConstraintLayout.LayoutParams(-1, i2);
        layoutParams3.height = i2;
        layoutParams3.startToStart = this.binding.portraitPlayer.getId();
        layoutParams3.endToEnd = this.binding.portraitPlayer.getId();
        layoutParams3.bottomToBottom = this.binding.portraitPlayer.getId();
        layoutParams3.topToBottom = this.binding.player2.getId();
        this.binding.player.setLayoutParams(layoutParams3);
        try {
            AreaPointBean areaPointBean = (AreaPointBean) new Gson().fromJson(SharePreferenceManager.getInstance().getRegionDetectPoint(this.iotId), AreaPointBean.class);
            if (areaPointBean != null) {
                ConstraintLayout.LayoutParams layoutParams4 = (ConstraintLayout.LayoutParams) this.binding.drawLineView.getLayoutParams();
                layoutParams4.height = i2;
                this.binding.drawLineView.setLayoutParams(layoutParams4);
                this.binding.drawLineView.setPointList(areaPointBean, ScreenUtil.getDisplayMetrics(getActivity())[0], i2);
            }
        } catch (Exception unused) {
        }
        ConstraintLayout.LayoutParams layoutParams5 = (ConstraintLayout.LayoutParams) this.binding.fourPic.getLayoutParams();
        layoutParams3.height = (int) ((ScreenUtil.getDisplayMetrics(getActivity())[0] * 9.0f) / 16.0f);
        this.binding.fourPic.setLayoutParams(layoutParams5);
        this.binding.qualityBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.27
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.binding.qualityDlg.setVisibility(0);
                IPCDoubleEyeActivity.this.changeQualityDlgView(SharePreferenceManager.getInstance().getStreamVideoQuality(IPCDoubleEyeActivity.this.iotId));
            }
        });
        this.binding.tvHQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.28
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.binding.qualityDlg.setVisibility(8);
                IPCDoubleEyeActivity.this.changeDefinition(2);
            }
        });
        this.binding.tvMQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.29
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.binding.qualityDlg.setVisibility(8);
                IPCDoubleEyeActivity.this.changeDefinition(1);
            }
        });
        this.binding.tvLQuality.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.30
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.binding.qualityDlg.setVisibility(8);
                IPCDoubleEyeActivity.this.changeDefinition(0);
            }
        });
        initPlayer();
        initPlayer2();
        initLiveIntercom(this.iotId);
        this.binding.lineViewItem.setVisibility(8);
        this.binding.llCapture.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.31
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.tvCapture.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colorAccent));
                    IPCDoubleEyeActivity.this.binding.captureBtn.setImageResource(R.drawable.video_camera_ipc_light);
                    return false;
                }
                IPCDoubleEyeActivity.this.binding.tvCapture.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                IPCDoubleEyeActivity.this.binding.captureBtn.setImageResource(R.drawable.video_camera_ipc);
                return false;
            }
        });
        this.binding.fullCamera.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.32
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.snapshot();
            }
        });
        this.binding.llRecord.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.33
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.startOrStopRecordingMp4();
            }
        });
        this.binding.fullVideo.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.34
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.startOrStopRecordingMp4();
            }
        });
        this.binding.maxLayout.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.35
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.isFloat = !r2.isFloat;
                IPCDoubleEyeActivity.this.setFloatBarState();
            }
        });
        this.binding.speakerBtn.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.36
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                Resources resources2;
                int i3;
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.startOrStopLiveIntercom();
                    ShadowButton shadowButton = IPCDoubleEyeActivity.this.binding.fullIntercom;
                    if (IPCDoubleEyeActivity.this.isLiveIntercoming) {
                        resources2 = IPCDoubleEyeActivity.this.getResources();
                        i3 = R.drawable.full_intercom;
                    } else {
                        resources2 = IPCDoubleEyeActivity.this.getResources();
                        i3 = R.drawable.full_intercom_;
                    }
                    shadowButton.setBackground(resources2.getDrawable(i3));
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
            this.binding.llFlips.setVisibility(0);
            this.binding.llShare.setVisibility(0);
        } else {
            this.binding.llFlips.setVisibility(8);
            this.binding.llShare.setVisibility(8);
        }
        this.binding.fullIntercom.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.37
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Resources resources2;
                int i3;
                IPCDoubleEyeActivity.this.startOrStopLiveIntercom();
                ShadowButton shadowButton = IPCDoubleEyeActivity.this.binding.fullIntercom;
                if (IPCDoubleEyeActivity.this.isLiveIntercoming) {
                    resources2 = IPCDoubleEyeActivity.this.getResources();
                    i3 = R.drawable.full_intercom;
                } else {
                    resources2 = IPCDoubleEyeActivity.this.getResources();
                    i3 = R.drawable.full_intercom_;
                }
                shadowButton.setBackground(resources2.getDrawable(i3));
            }
        });
        this.binding.llListener.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.38
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Resources resources2;
                int i3;
                IPCDoubleEyeActivity.this.speakerSwitch = !r3.speakerSwitch;
                IPCDoubleEyeActivity.this.playBall.setVolume(IPCDoubleEyeActivity.this.speakerSwitch ? 1.0f : 0.0f);
                IPCDoubleEyeActivity.this.binding.listenerBtn.setImageResource(IPCDoubleEyeActivity.this.speakerSwitch ? R.drawable.video_sound_light : R.drawable.video_sound);
                TextView textView = IPCDoubleEyeActivity.this.binding.tvVoice;
                if (IPCDoubleEyeActivity.this.speakerSwitch) {
                    resources2 = IPCDoubleEyeActivity.this.getResources();
                    i3 = R.color.colorAccent;
                } else {
                    resources2 = IPCDoubleEyeActivity.this.getResources();
                    i3 = R.color.colors_ipc_image_text;
                }
                textView.setTextColor(resources2.getColor(i3));
            }
        });
        this.binding.llMoreDoubleEye.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.39
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.moreImage.setBackgroundResource(R.drawable.more_ipc_light);
                    IPCDoubleEyeActivity.this.binding.moreTextDoubleEye.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCDoubleEyeActivity.this.binding.moreImage.setBackgroundResource(R.drawable.more_ipc);
                IPCDoubleEyeActivity.this.binding.moreTextDoubleEye.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                return false;
            }
        });
        this.binding.llMoreDoubleEye.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.40
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                FragmentTransaction fragmentTransactionBeginTransaction = IPCDoubleEyeActivity.this.getSupportFragmentManager().beginTransaction();
                if (IPCDoubleEyeActivity.this.isMoreFragmentShow) {
                    IPCDoubleEyeActivity.this.isMoreFragmentShow = false;
                    if (IPCDoubleEyeActivity.this.binding.rlTouchView.getVisibility() == 0) {
                        IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.setVisibility(0);
                        Log.e("云存=", "" + SharePreferenceManager.getInstance().getEventRecord(IPCDoubleEyeActivity.this.iotId) + "  4G=" + SharePreferenceManager.getInstance().getSupport4G(IPCDoubleEyeActivity.this.device.getIotId()));
                        if (SharePreferenceManager.getInstance().getEventRecord(IPCDoubleEyeActivity.this.iotId) != 1 && SharePreferenceManager.getInstance().getSupport4G(IPCDoubleEyeActivity.this.device.getIotId()) != 1) {
                            IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                        }
                        if (!AppConfig.isChina && !IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.isSelected()) {
                            IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                        }
                    }
                    if (IPCDoubleEyeActivity.this.isOldPresetDevice) {
                        fragmentTransactionBeginTransaction.hide(IPCDoubleEyeActivity.this.controllerFragment).hide(IPCDoubleEyeActivity.this.moreFragment).hide(IPCDoubleEyeActivity.this.oldPresetFragment);
                        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
                        IPCDoubleEyeActivity.this.binding.moreImage.setBackgroundResource(R.drawable.more_ipc);
                        IPCDoubleEyeActivity.this.binding.moreTextDoubleEye.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                        return;
                    }
                    fragmentTransactionBeginTransaction.hide(IPCDoubleEyeActivity.this.controllerFragment).hide(IPCDoubleEyeActivity.this.moreFragment).hide(IPCDoubleEyeActivity.this.presetFragment);
                    fragmentTransactionBeginTransaction.commitAllowingStateLoss();
                    IPCDoubleEyeActivity.this.binding.moreImage.setBackgroundResource(R.drawable.more_ipc);
                    IPCDoubleEyeActivity.this.binding.moreTextDoubleEye.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                    return;
                }
                Log.e("云存=", "" + SharePreferenceManager.getInstance().getEventRecord(IPCDoubleEyeActivity.this.iotId) + "  4G=" + SharePreferenceManager.getInstance().getSupport4G(IPCDoubleEyeActivity.this.device.getIotId()));
                IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                IPCDoubleEyeActivity.this.isMoreFragmentShow = true;
                fragmentTransactionBeginTransaction.hide(IPCDoubleEyeActivity.this.controllerFragment).show(IPCDoubleEyeActivity.this.moreFragment);
                fragmentTransactionBeginTransaction.commitAllowingStateLoss();
                IPCDoubleEyeActivity.this.binding.moreImage.setBackgroundResource(R.drawable.more_ipc_light);
                IPCDoubleEyeActivity.this.binding.moreTextDoubleEye.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colorAccent));
            }
        });
        this.binding.fullSound.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.41
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Resources resources2;
                int i3;
                IPCDoubleEyeActivity.this.speakerSwitch = !r3.speakerSwitch;
                IPCDoubleEyeActivity.this.playBall.setVolume(IPCDoubleEyeActivity.this.speakerSwitch ? 1.0f : 0.0f);
                ShadowButton shadowButton = IPCDoubleEyeActivity.this.binding.fullSound;
                if (IPCDoubleEyeActivity.this.speakerSwitch) {
                    resources2 = IPCDoubleEyeActivity.this.getResources();
                    i3 = R.drawable.full_sound;
                } else {
                    resources2 = IPCDoubleEyeActivity.this.getResources();
                    i3 = R.drawable.full_sound_;
                }
                shadowButton.setBackground(resources2.getDrawable(i3));
            }
        });
        this.binding.fullNightVision.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.42
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(IPCDoubleEyeActivity.this.device.getIotId()) == 1) {
                    IPCDoubleEyeActivity.this.currentInfrarred = SharePreferenceManager.getInstance().getDayNightMode(IPCDoubleEyeActivity.this.iotId);
                    IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                    iPCDoubleEyeActivity.changeLightDlgView(iPCDoubleEyeActivity.currentInfrarred);
                    IPCDoubleEyeActivity.this.binding.lightDlg.setVisibility(0);
                } else {
                    IPCDoubleEyeActivity.this.currentInfrarred2 = SharePreferenceManager.getInstance().getDayNightMode(IPCDoubleEyeActivity.this.iotId2);
                    IPCDoubleEyeActivity.this.currentInfrarred = SharePreferenceManager.getInstance().getDayNightMode(IPCDoubleEyeActivity.this.iotId);
                    int i3 = 0;
                    int i4 = 0;
                    for (int i5 = 0; i5 < IPCDoubleEyeActivity.this.nightModelList.size(); i5++) {
                        if (((String) IPCDoubleEyeActivity.this.nightModelList.get(i5)).equals(IPCDoubleEyeActivity.this.infrarredMode[IPCDoubleEyeActivity.this.currentInfrarred])) {
                            i3 = i5;
                        }
                        if (((String) IPCDoubleEyeActivity.this.nightModelList.get(i5)).equals(IPCDoubleEyeActivity.this.infrarredMode[IPCDoubleEyeActivity.this.currentInfrarred2])) {
                            i4 = i5;
                        }
                    }
                    IPCDoubleEyeActivity iPCDoubleEyeActivity2 = IPCDoubleEyeActivity.this;
                    if (iPCDoubleEyeActivity2.selectIotId.equals(IPCDoubleEyeActivity.this.iotId2)) {
                        i3 = i4;
                    }
                    iPCDoubleEyeActivity2.changeLightDlgView(i3);
                    IPCDoubleEyeActivity.this.binding.lightDlg.setVisibility(0);
                }
                if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCDoubleEyeActivity.this.iotId) != -1) {
                    IPCDoubleEyeActivity.this.binding.tvLight1.setVisibility(8);
                    IPCDoubleEyeActivity.this.binding.tvLight2.setVisibility(8);
                    IPCDoubleEyeActivity.this.binding.tvLight3.setVisibility(8);
                    StringBuilder sbReverse = new StringBuilder(Integer.toBinaryString(SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCDoubleEyeActivity.this.iotId))).reverse();
                    for (int i6 = 0; i6 < sbReverse.length(); i6++) {
                        if (sbReverse.charAt(i6) - '0' == 1) {
                            if (i6 == 0) {
                                IPCDoubleEyeActivity.this.binding.tvLight3.setVisibility(0);
                            }
                            if (i6 == 1) {
                                IPCDoubleEyeActivity.this.binding.tvLight1.setVisibility(0);
                            }
                            if (i6 == 2) {
                                IPCDoubleEyeActivity.this.binding.tvLight2.setVisibility(0);
                            }
                        }
                    }
                }
            }
        });
        this.binding.lightDlg.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.43
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.binding.lightDlg.setVisibility(8);
            }
        });
        this.binding.tvLight1.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.44
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.binding.lightDlg.setVisibility(8);
                IPCDoubleEyeActivity.this.updateNightMode(0, IPCDoubleEyeActivity.this.selectIotId);
            }
        });
        this.binding.tvLight2.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.45
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.binding.lightDlg.setVisibility(8);
                IPCDoubleEyeActivity.this.updateNightMode(1, IPCDoubleEyeActivity.this.selectIotId);
            }
        });
        this.binding.tvLight3.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.46
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.binding.lightDlg.setVisibility(8);
                IPCDoubleEyeActivity.this.updateNightMode(2, IPCDoubleEyeActivity.this.selectIotId);
            }
        });
        this.binding.fourPicBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.47
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCDoubleEyeActivity.this.getRequestedOrientation() == 1) {
                    IPCDoubleEyeActivity.this.setRequestedOrientation(0);
                } else {
                    IPCDoubleEyeActivity.this.setRequestedOrientation(8);
                }
            }
        });
        this.binding.llService4g.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.48
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (!TextUtils.isEmpty(IPCDoubleEyeActivity.this.IccId)) {
                    IPCDoubleEyeActivity.this.isNet4GSwitch();
                } else {
                    IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                    iPCDoubleEyeActivity.showToast(iPCDoubleEyeActivity.getResources().getString(R.string.query_traffic_fail));
                }
            }
        });
        this.binding.llService4g.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.49
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.service4gBtn.setBackgroundResource(R.drawable.fourg_ipc_light);
                    IPCDoubleEyeActivity.this.binding.service4gText.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCDoubleEyeActivity.this.binding.service4gBtn.setBackgroundResource(R.drawable.fourg_ipc);
                IPCDoubleEyeActivity.this.binding.service4gText.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                return false;
            }
        });
        this.binding.llShare.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.50
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.shareBtn.setBackgroundResource(R.drawable.share_ipc_light);
                    IPCDoubleEyeActivity.this.binding.shareText.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCDoubleEyeActivity.this.binding.shareBtn.setBackgroundResource(R.drawable.share_ipc);
                IPCDoubleEyeActivity.this.binding.shareText.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                return false;
            }
        });
        this.binding.llShare.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.51
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.shareDialog2 = ((ShareDialog.Builder) new ShareDialog.Builder().view(R.layout.dialog_input2).leftBtnText(IPCDoubleEyeActivity.this.getString(R.string.cancel)).rightBtnText(IPCDoubleEyeActivity.this.getString(R.string.share_immediately)).clickRight(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.51.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view3) {
                        if (IPCDoubleEyeActivity.this.shareDialog2.getContent() != null) {
                            if (IPCDoubleEyeActivity.this.shareDialog2.getMode() != 0 || SystemUtil.isPhone(IPCDoubleEyeActivity.this.shareDialog2.getContent())) {
                                if (IPCDoubleEyeActivity.this.shareDialog2.getMode() == 1 && !SystemUtil.isEmail(IPCDoubleEyeActivity.this.shareDialog2.getContent())) {
                                    ToastUtils.toast(IPCDoubleEyeActivity.this.getActivity(), IPCDoubleEyeActivity.this.getString(R.string.email_invalid));
                                    return;
                                }
                                ArrayList arrayList = new ArrayList();
                                arrayList.add(IPCDoubleEyeActivity.this.nvrIotId);
                                arrayList.add(IPCDoubleEyeActivity.this.ballIotId);
                                arrayList.add(IPCDoubleEyeActivity.this.gunIotId);
                                IPCDoubleEyeActivity.this.shareDevice(IPCDoubleEyeActivity.this.shareDialog2.getContent(), arrayList, IPCDoubleEyeActivity.this.shareDialog2.getMode() == 0 ? IPCDoubleEyeActivity.this.shareDialog2.getDistinct() : null);
                                return;
                            }
                            ToastUtils.toast(IPCDoubleEyeActivity.this.getActivity(), IPCDoubleEyeActivity.this.getString(R.string.phone_invalid));
                            return;
                        }
                        Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.share_user_null_error, 0).show();
                    }
                })).create();
                IPCDoubleEyeActivity.this.shareDialog2.setOnShareClick(new ShareDialog.OnShareClickListener() { // from class: activity.IPCDoubleEyeActivity.51.2
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
                IPCDoubleEyeActivity.this.shareDialog2.setExtra(IPCDoubleEyeActivity.this.device);
                IPCDoubleEyeActivity.this.shareDialog2.show(IPCDoubleEyeActivity.this.getSupportFragmentManager(), "");
            }
        });
        this.binding.llMore.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.52
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc_light);
                    IPCDoubleEyeActivity.this.binding.moreText.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCDoubleEyeActivity.this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc);
                IPCDoubleEyeActivity.this.binding.moreText.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                return false;
            }
        });
        this.binding.llMore.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.53
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                FragmentTransaction fragmentTransactionBeginTransaction = IPCDoubleEyeActivity.this.getSupportFragmentManager().beginTransaction();
                if (IPCDoubleEyeActivity.this.isMoreFragmentShow) {
                    IPCDoubleEyeActivity.this.isMoreFragmentShow = false;
                    if (IPCDoubleEyeActivity.this.isOldPresetDevice) {
                        fragmentTransactionBeginTransaction.hide(IPCDoubleEyeActivity.this.controllerFragment).hide(IPCDoubleEyeActivity.this.moreFragment).hide(IPCDoubleEyeActivity.this.oldPresetFragment);
                        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
                        IPCDoubleEyeActivity.this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc);
                        IPCDoubleEyeActivity.this.binding.moreText.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                        return;
                    }
                    fragmentTransactionBeginTransaction.hide(IPCDoubleEyeActivity.this.controllerFragment).hide(IPCDoubleEyeActivity.this.moreFragment).hide(IPCDoubleEyeActivity.this.presetFragment);
                    fragmentTransactionBeginTransaction.commitAllowingStateLoss();
                    IPCDoubleEyeActivity.this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc);
                    IPCDoubleEyeActivity.this.binding.moreText.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                    return;
                }
                IPCDoubleEyeActivity.this.isMoreFragmentShow = true;
                fragmentTransactionBeginTransaction.hide(IPCDoubleEyeActivity.this.controllerFragment).show(IPCDoubleEyeActivity.this.moreFragment);
                fragmentTransactionBeginTransaction.commitAllowingStateLoss();
                IPCDoubleEyeActivity.this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc_light);
                IPCDoubleEyeActivity.this.binding.moreText.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colorAccent));
            }
        });
        this.binding.llFlips.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.54
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.flipBtn.setBackgroundResource(R.drawable.cloudicon_light);
                    IPCDoubleEyeActivity.this.binding.cloudText.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCDoubleEyeActivity.this.binding.flipBtn.setBackgroundResource(R.drawable.cloudicon);
                IPCDoubleEyeActivity.this.binding.cloudText.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                return false;
            }
        });
        this.binding.llFlips.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.55
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.isNet4GSwitch();
            }
        });
        this.binding.llFlip.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.56
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.videoBtn.setBackgroundResource(R.drawable.video_back_light);
                    IPCDoubleEyeActivity.this.binding.videoBackText.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCDoubleEyeActivity.this.binding.videoBtn.setBackgroundResource(R.drawable.video_back);
                IPCDoubleEyeActivity.this.binding.videoBackText.setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colors_ipc_image_text));
                return false;
            }
        });
        this.binding.llFlip.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.57
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(IPCDoubleEyeActivity.this, (Class<?>) RecordVideoActivity.class);
                intent.putExtra("title", IPCDoubleEyeActivity.this.binding.tvTitle.getTitleText());
                intent.putExtra("iotId", IPCDoubleEyeActivity.this.iotId);
                intent.putExtra("appKey", IPCDoubleEyeActivity.this.appKey);
                IPCDoubleEyeActivity.this.startActivity(intent);
            }
        });
        this.binding.videoPlayIbtn.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.58
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.dismissPlayButton();
                if (SharePreferenceManager.getInstance().getLowPower(IPCDoubleEyeActivity.this.iotId) == 1) {
                    IPCDoubleEyeActivity.this.wakeUpHandler.removeCallbacksAndMessages(null);
                    IPCDoubleEyeActivity.this.wakeUpDevice();
                    IPCDoubleEyeActivity.this.wakeUpDeviceHandel();
                    return;
                }
                IPCDoubleEyeActivity.this.playLive();
            }
        });
        this.binding.bottomZoom.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.59
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCDoubleEyeActivity.this.binding.changeZoom.getVisibility() == 0) {
                    IPCDoubleEyeActivity.this.binding.changeZoom.setVisibility(8);
                } else {
                    IPCDoubleEyeActivity.this.binding.changeZoom.setVisibility(0);
                }
            }
        });
        this.binding.addZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.60
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCDoubleEyeActivity.this.onTouchTimer == null) {
                        IPCDoubleEyeActivity.this.onTouchTimer = new Timer();
                        IPCDoubleEyeActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCDoubleEyeActivity.60.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCDoubleEyeActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCDoubleEyeActivity.this.onTouchTimer != null) {
                    IPCDoubleEyeActivity.this.onTouchTimer.cancel();
                    IPCDoubleEyeActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.binding.fullAddZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.61
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCDoubleEyeActivity.this.onTouchTimer == null) {
                        IPCDoubleEyeActivity.this.onTouchTimer = new Timer();
                        IPCDoubleEyeActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCDoubleEyeActivity.61.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCDoubleEyeActivity.this.changeZoom(1);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCDoubleEyeActivity.this.onTouchTimer != null) {
                    IPCDoubleEyeActivity.this.onTouchTimer.cancel();
                    IPCDoubleEyeActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.binding.reduceZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.62
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCDoubleEyeActivity.this.onTouchTimer == null) {
                        IPCDoubleEyeActivity.this.onTouchTimer = new Timer();
                        IPCDoubleEyeActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCDoubleEyeActivity.62.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCDoubleEyeActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCDoubleEyeActivity.this.onTouchTimer != null) {
                    IPCDoubleEyeActivity.this.onTouchTimer.cancel();
                    IPCDoubleEyeActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.binding.fullReduceZoom.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.63
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (IPCDoubleEyeActivity.this.onTouchTimer == null) {
                        IPCDoubleEyeActivity.this.onTouchTimer = new Timer();
                        IPCDoubleEyeActivity.this.onTouchTimer.schedule(new TimerTask() { // from class: activity.IPCDoubleEyeActivity.63.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                IPCDoubleEyeActivity.this.changeZoom(0);
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1 && IPCDoubleEyeActivity.this.onTouchTimer != null) {
                    IPCDoubleEyeActivity.this.onTouchTimer.cancel();
                    IPCDoubleEyeActivity.this.onTouchTimer = null;
                }
                return true;
            }
        });
        this.zoom.observe(this, new Observer<Float>() { // from class: activity.IPCDoubleEyeActivity.64
            @Override // androidx.lifecycle.Observer
            public void onChanged(@Nullable Float f) {
                Log.d(IPCDoubleEyeActivity.this.TAG, "changeOpticalZoom:- " + f);
                if (f != null) {
                    if (f.floatValue() > 1.0f) {
                        IPCDoubleEyeActivity.this.binding.OSD.setVisibility(0);
                    } else {
                        IPCDoubleEyeActivity.this.binding.OSD.setVisibility(8);
                    }
                }
            }
        });
        this.binding.qualityDlg.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.65
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.binding.qualityDlg.setVisibility(8);
            }
        });
        new DeviceInfoBean().setIotId(this.iotId);
        if (SharePreferenceManager.getInstance().getDisplayController(this.ballIotId) == 1) {
            getControllerList();
        }
        this.binding.player.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.66
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
                throw new UnsupportedOperationException("Method not decompiled: activity.IPCDoubleEyeActivity.AnonymousClass66.onTouch(android.view.View, android.view.MotionEvent):boolean");
            }
        });
        this.binding.immediateRenewal.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.67
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.isNet4GSwitch();
            }
        });
        initAutorView();
        if (this.strongRemind == 1) {
            startLiveIntercom();
            this.speakerSwitch = true;
            this.playBall.setVolume(this.speakerSwitch ? 1.0f : 0.0f);
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
        if (SharePreferenceManager.getInstance().getNightVisionHide(this.device.getIotId()) == 1 || SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.iotId) == 0) {
            this.binding.ivLightWhile.setVisibility(8);
            this.binding.ivLightWhile2.setVisibility(8);
            this.binding.fullNightVision.setVisibility(8);
        }
        if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(this.device.getIotId()) == 1) {
            this.binding.ivLightWhile2.setVisibility(8);
            this.binding.fullNightVision.setVisibility(8);
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        SettingsCtrl.getInstance().getProperties(this.iotId2, new MyCallback() { // from class: activity.IPCDoubleEyeActivity.68
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.IPCDoubleEyeActivity.69
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
        SettingsCtrl.getInstance().getProperties(this.nvrIotId, new MyCallback() { // from class: activity.IPCDoubleEyeActivity.70
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
        this.binding.lineViewItem.setVisibility(SharePreferenceManager.getInstance().getPTZLinkageTrackSwitch(this.gunIotId) == 0 ? 8 : 0);
        this.isMoreFragmentShow = false;
        this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc);
        this.binding.moreText.setTextColor(getResources().getColor(R.color.colors_ipc_image_text));
        this.binding.moreImage.setBackgroundResource(R.drawable.more_ipc);
        this.binding.moreTextDoubleEye.setTextColor(getResources().getColor(R.color.colors_ipc_image_text));
        this.binding.ipcOfflineText.setVisibility(8);
        SharePreferenceManager.getInstance().registerOnCallSetListener(this.definitionChangeListener);
        this.binding.qualityBtn.setText(this.definitionList.get(SharePreferenceManager.getInstance().getStreamVideoQuality(this.iotId)));
        this.rotationOrientate = SharePreferenceManager.getInstance().getImageFlip(this.iotId);
        getProperties(new AnonymousClass71());
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
        bundle.putBoolean("isOwner", this.isOwner);
        if (this.lightVisible) {
            bundle.putInt("currentInfrarred", SharePreferenceManager.getInstance().getDayNightMode(this.iotId));
        }
        this.netVisible = SharePreferenceManager.getInstance().getDoubleNetWork(this.iotId) == 1;
        if (this.netVisible) {
            bundle.putString("switchText", this.switchText);
        }
        this.moreFragment = new MoreFragmentDoubleEye();
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
        if (SharePreferenceManager.getInstance().getDisplayController(this.ballIotId) == 1) {
            getControllerList();
        }
        this.controllerFragment.setList(this.controllerList);
        setList(this.controllerList);
        if (SharePreferenceManager.getInstance().getLowPower(this.iotId) == 1) {
            this.lowPowerMode = SharePreferenceManager.getInstance().getLowPowerStatus(this.iotId);
            this.wakeUpHandler.removeCallbacksAndMessages(null);
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
            this.binding.f6228fragment.setClickable(false);
            this.isMoreFragmentShow = true;
        }
        this.smartDoorVisible = SharePreferenceManager.getInstance().getDisplayController(this.iotId) == 1;
        this.binding.lineViewItem.bringToFront();
        this.binding.lineViewItem.setOnMotionEventListener(this);
        if (this.showMode != 0) {
            this.binding.lineViewItem.setVisibility(8);
        }
        resetInactivityTimer();
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$71, reason: invalid class name */
    class AnonymousClass71 implements MyCallback {
        AnonymousClass71() {
        }

        @Override // tools.MyCallback
        public void onComplete(boolean z) {
            if (SharePreferenceManager.getInstance().getMixZoom(IPCDoubleEyeActivity.this.iotId) == 1) {
                IPCDoubleEyeActivity.this.isMixZoom = true;
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.71.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.OSD.setVisibility(8);
                    }
                });
            } else {
                IPCDoubleEyeActivity.this.isMixZoom = false;
            }
            Log.e("变焦", "" + SharePreferenceManager.getInstance().getSupportZoom(IPCDoubleEyeActivity.this.iotId));
            if (SharePreferenceManager.getInstance().getSupportZoom(IPCDoubleEyeActivity.this.iotId) != 1) {
                IPCDoubleEyeActivity.this.isOpticalZoom = false;
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.71.3
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.layoutAf.setVisibility(8);
                    }
                });
            } else {
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.71.2
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.OSD.setVisibility(8);
                    }
                });
                IPCDoubleEyeActivity.this.isOpticalZoom = true;
            }
            if (SharePreferenceManager.getInstance().getSensorViewDisplay(IPCDoubleEyeActivity.this.iotId) == 0) {
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.71.4
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.SensorView.setVisibility(8);
                    }
                });
            } else {
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.71.5
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.SensorView.setVisibility(0);
                        IPCDoubleEyeActivity.this.binding.SensorView.setTemperatureDisplay(false);
                    }
                });
            }
            IPCDoubleEyeActivity.this.binding.lineViewItem.setVisibility(SharePreferenceManager.getInstance().getPTZLinkageTrackSwitch(IPCDoubleEyeActivity.this.gunIotId) == 0 ? 8 : 0);
            if (SharePreferenceManager.getInstance().getDisplayController(IPCDoubleEyeActivity.this.iotId) == 0) {
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.71.6
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.llController.setVisibility(8);
                    }
                });
            }
            if (!AppConfig.isChina) {
                IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.setImageResource(R.drawable.selector_server_en);
            }
            IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.setSelected(SharePreferenceManager.getInstance().getSupport4G(IPCDoubleEyeActivity.this.device.getIotId()) == 1);
            Log.e("云存=", "" + SharePreferenceManager.getInstance().getEventRecord(IPCDoubleEyeActivity.this.iotId) + "  4G=" + SharePreferenceManager.getInstance().getSupport4G(IPCDoubleEyeActivity.this.device.getIotId()));
            if (SharePreferenceManager.getInstance().getEventRecord(IPCDoubleEyeActivity.this.iotId) == 1 || SharePreferenceManager.getInstance().getSupport4G(IPCDoubleEyeActivity.this.device.getIotId()) == 1) {
                IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.setVisibility(0);
                if (!AppConfig.isChina && !IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.isSelected()) {
                    IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.setVisibility(8);
                }
            } else {
                IPCDoubleEyeActivity.this.binding.ivCharge4gFlow.setVisibility(8);
            }
            if (((SharePreferenceManager.getInstance().getPageControlEx(IPCDoubleEyeActivity.this.iotId) & 256) >> 8) == 1) {
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.71.7
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.bottomShop.setVisibility(0);
                        IPCDoubleEyeActivity.this.binding.bottomShop.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.71.7.1
                            @Override // android.view.View.OnClickListener
                            public void onClick(View view2) {
                                IPCDoubleEyeActivity.this.openH5(SharePreferenceManager.getInstance().getUserMallUrl(IPCDoubleEyeActivity.this.iotId));
                            }
                        });
                    }
                });
            }
            IPCDoubleEyeActivity.this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(IPCDoubleEyeActivity.this.iotId);
            IPCDoubleEyeActivity.this.faceDetectionAbility = SharePreferenceManager.getInstance().getHumanoidTracking(IPCDoubleEyeActivity.this.iotId).intValue();
            if (IPCDoubleEyeActivity.this.faceDetectionAbility == 1) {
                IPCDoubleEyeActivity.this.isDetecting = SharePreferenceManager.getInstance().getHumanoidTrackingEnable(IPCDoubleEyeActivity.this.iotId).intValue() == 1;
            } else {
                IPCDoubleEyeActivity.this.isDetecting = SharePreferenceManager.getInstance().getIntelligentMode(IPCDoubleEyeActivity.this.iotId) == 1;
            }
            if (IPCDoubleEyeActivity.this.moreFragment != null) {
                IPCDoubleEyeActivity.this.moreFragment.setDetecting(IPCDoubleEyeActivity.this.isDetecting);
            }
        }
    }

    private void showFormatDialog(final float f, final float f2, final int i) {
        this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.72
            @Override // java.lang.Runnable
            public void run() {
                new BaseDialog.Builder().view(R.layout.dialog_common).content(IPCDoubleEyeActivity.this.getString(R.string.sd_card_not_initialized)).leftBtnText(IPCDoubleEyeActivity.this.getString(R.string.cancel)).clickLeft(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.72.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        SharePreferenceManager.getInstance().setFirstFormatInIpc(IPCDoubleEyeActivity.this.iotId, false);
                    }
                }).rightBtnText(IPCDoubleEyeActivity.this.getString(R.string.format)).clickRight(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.72.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        Intent intent = new Intent(IPCDoubleEyeActivity.this, (Class<?>) StorageStatusActivity.class);
                        intent.putExtra("totalStorage", f);
                        intent.putExtra("remainStorage", f2);
                        intent.putExtra("storageStatusValues", i);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, IPCDoubleEyeActivity.this.device);
                        if (IPCDoubleEyeActivity.this.device1 != null) {
                            bundle.putSerializable("device1", IPCDoubleEyeActivity.this.device1);
                        }
                        if (IPCDoubleEyeActivity.this.nvrDevice != null) {
                            bundle.putSerializable("nvrDevice", IPCDoubleEyeActivity.this.nvrDevice);
                        }
                        intent.putExtras(bundle);
                        IPCDoubleEyeActivity.this.startActivity(intent);
                        IPCDoubleEyeActivity.this.needTFInit = false;
                    }
                }).canCancel(false).create().show(IPCDoubleEyeActivity.this.getSupportFragmentManager(), "");
            }
        });
    }

    private void getThingsStatus(String str) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/status/get").setScheme(Scheme.HTTPS).setApiVersion("1.0.4").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass73());
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$73, reason: invalid class name */
    class AnonymousClass73 implements IoTCallback {
        AnonymousClass73() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            exc.printStackTrace();
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
            Log.d(IPCDoubleEyeActivity.this.TAG, "run: ---------------" + Thread.currentThread().getName());
            try {
                if (((org.json.JSONObject) ioTResponse.getData()).get("status").toString().equals("3")) {
                    if (IPCDoubleEyeActivity.this.IccId == null || "".equals(IPCDoubleEyeActivity.this.IccId)) {
                        IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.73.2
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCDoubleEyeActivity.this.binding.ipcOfflineText.bringToFront();
                                IPCDoubleEyeActivity.this.binding.ipcOfflineText.setVisibility(0);
                            }
                        });
                    } else {
                        new OkHttpClient().newCall(new Request.Builder().url("http://www.secueye.cn:8000/api/smsApi?iccid=" + IPCDoubleEyeActivity.this.IccId + "&method=smsStatusSecueye").get().build()).enqueue(new Callback() { // from class: activity.IPCDoubleEyeActivity.73.1
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
                                            IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.73.1.1
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCDoubleEyeActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCDoubleEyeActivity.this.binding.ipcOfflineText.setVisibility(0);
                                                    IPCDoubleEyeActivity.this.isOtherCard = true;
                                                }
                                            });
                                            return;
                                        } else if (iIntValue != 200) {
                                            IPCDoubleEyeActivity.this.showToast(IPCDoubleEyeActivity.this.getString(R.string.exception_4g_data));
                                            return;
                                        }
                                    }
                                    if (!object.containsKey("values") || IPCDoubleEyeActivity.this.isOtherCard) {
                                        return;
                                    }
                                    JSONObject jSONObject = object.getJSONObject("values");
                                    if (jSONObject.containsKey("status")) {
                                        if (!jSONObject.getString("status").equals("停机")) {
                                            IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.73.1.4
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCDoubleEyeActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCDoubleEyeActivity.this.binding.ipcOfflineText.setVisibility(0);
                                                }
                                            });
                                        } else if (AppConfig.isChina) {
                                            IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.73.1.2
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    if (!IPCDoubleEyeActivity.this.isHorizontal) {
                                                        IPCDoubleEyeActivity.this.binding.traffic4gExpired.bringToFront();
                                                        IPCDoubleEyeActivity.this.binding.immediateRenewal.bringToFront();
                                                        IPCDoubleEyeActivity.this.binding.outlineTime.bringToFront();
                                                        IPCDoubleEyeActivity.this.binding.videoPlayIbtn.setVisibility(8);
                                                        IPCDoubleEyeActivity.this.binding.ipcOfflineText.setVisibility(8);
                                                        IPCDoubleEyeActivity.this.binding.traffic4gExpired.setVisibility(0);
                                                        IPCDoubleEyeActivity.this.binding.immediateRenewal.setVisibility(0);
                                                        IPCDoubleEyeActivity.this.binding.outlineTime.setVisibility(0);
                                                    }
                                                    try {
                                                        IPCDoubleEyeActivity.this.binding.outlineTime.setText(((Object) IPCDoubleEyeActivity.this.getResources().getText(R.string.time_of_off_line)) + "：" + TimeUtil.TimeStamp2Date(((org.json.JSONObject) ioTResponse.getData()).get("time").toString()));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    IPCDoubleEyeActivity.this.needRecharge = true;
                                                }
                                            });
                                        } else {
                                            IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.73.1.3
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    IPCDoubleEyeActivity.this.binding.ipcOfflineText.bringToFront();
                                                    IPCDoubleEyeActivity.this.binding.ipcOfflineText.setVisibility(0);
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
        this.playBall.release();
        this.playGun.release();
        LiveIntercomV2 liveIntercomV2 = this.liveIntercom;
        if (liveIntercomV2 != null) {
            liveIntercomV2.release();
        }
        Timer timer = this.ptzTimer;
        if (timer != null) {
            timer.cancel();
            this.ptzTimer = null;
        }
        LiveIntercomV2 liveIntercomV22 = this.liveIntercom;
        if (liveIntercomV22 != null) {
            liveIntercomV22.release();
        }
        Handler handler = this.wakeUpHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.wakeUpHandler = null;
        }
        EventBus.getDefault().unregister(this);
        SharePreferenceManager.getInstance().unRegisterOnCallSetListener(this.definitionChangeListener);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        stopScreenLight();
        AutoSnap();
        LivePlayer livePlayer = this.playBall;
        if (livePlayer != null) {
            livePlayer.stop();
        }
        LivePlayer livePlayer2 = this.playGun;
        if (livePlayer2 != null) {
            livePlayer2.stop();
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
        LiveIntercomV2 liveIntercomV2 = this.liveIntercom;
        if (liveIntercomV2 != null) {
            liveIntercomV2.release();
        }
        stopInactivityTimer();
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

    @Override // android.app.Activity
    public void onUserInteraction() {
        resetInactivityTimer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetInactivityTimer() {
        DeviceInfoBean deviceInfoBean = this.device;
        if (deviceInfoBean == null || deviceInfoBean.getIotId() == null) {
            return;
        }
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
            this.inactivityTimer.schedule(new InactivityTimerTask(), AppConfig.INACTIVITY_DELAY);
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

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.InactivityTimerTask.1
                @Override // java.lang.Runnable
                public void run() {
                    if (IPCDoubleEyeActivity.this.playBall.getPlayState() == 3 || IPCDoubleEyeActivity.this.playGun.getPlayState() == 3) {
                        if (IPCDoubleEyeActivity.this.playBall != null) {
                            IPCDoubleEyeActivity.this.playBall.stop();
                        }
                        if (IPCDoubleEyeActivity.this.playGun != null) {
                            IPCDoubleEyeActivity.this.playGun.stop();
                        }
                        IPCDoubleEyeActivity.this.wakeUpHandler.removeCallbacksAndMessages(null);
                        Log.e("防沉迷", "在播放");
                        IPCDoubleEyeActivity.this.showPlayButton();
                        DialogUtil.showTipsConfirmDiaLog(IPCDoubleEyeActivity.this.getActivity(), IPCDoubleEyeActivity.this.getString(R.string.warm_tips), IPCDoubleEyeActivity.this.getString(R.string.warm_tips_1), IPCDoubleEyeActivity.this.getString(R.string.i_know), new DialogUtil.OnConfirmClickListener() { // from class: activity.IPCDoubleEyeActivity.InactivityTimerTask.1.1
                            @Override // dialog.DialogUtil.OnConfirmClickListener
                            public void ConfirmListener() {
                                IPCDoubleEyeActivity.this.resetInactivityTimer();
                            }
                        });
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getProperties(MyCallback myCallback) {
        SettingsCtrl.getInstance().getProperties(this.iotId, myCallback);
    }

    @Override // fragment.MoreFragmentDoubleEye.MyBackListener
    public void backOut() {
        getSupportFragmentManager().beginTransaction().hide(this.moreFragment).commitAllowingStateLoss();
        this.binding.f6228fragment.setClickable(false);
        this.isMoreFragmentShow = false;
        this.binding.moreBtn.setBackgroundResource(R.drawable.more_ipc);
        this.binding.moreText.setTextColor(getResources().getColor(R.color.colors_ipc_image_text));
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void CloudBackPlay() {
        Intent intent = new Intent(getActivity(), (Class<?>) CloudStorageActivity.class);
        intent.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
        intent.putExtra("device1", this.device1);
        intent.putExtra("nvrDevice", this.nvrDevice);
        startActivity(intent);
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void humanoidTracking(TopicBean topicBean, RecyclerView.Adapter adapter2, int i, int i2, boolean z) {
        if (SharePreferenceManager.getInstance().getFaceDetectMode(this.iotId) == 0) {
            HashMap map = new HashMap();
            map.put(Constants.FACE_DETECT_SENSITIVITY, 2);
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.74
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z2, Object obj) {
                    if (!z2 || obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.74.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setFaceDetectMode(IPCDoubleEyeActivity.this.iotId, 2);
                        }
                    }
                }
            });
        }
        if (z) {
            checkSwitch(topicBean, adapter2, i, i2);
        } else {
            this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.75
                @Override // java.lang.Runnable
                public void run() {
                    IPCDoubleEyeActivity.this.showProgressDialog();
                }
            });
            setMobileTracking(topicBean, adapter2, i, i2);
        }
        if (topicBean.isSelect()) {
            IPCManager.getInstance().getDevice(this.iotId).changePresetLocation(103, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.76
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z2, @Nullable Object obj) {
                    if (z2 && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        Log.e("预置位", "103");
                    }
                }
            });
        } else {
            IPCManager.getInstance().getDevice(this.iotId).addPresetLocation(99, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.77
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z2, @Nullable Object obj) {
                    if (z2 && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        Log.e("预置位", "99");
                    }
                }
            });
            IPCManager.getInstance().getDevice(this.iotId).changePresetLocation(100, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.78
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z2, @Nullable Object obj) {
                    if (z2 && ((IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class)).getCode() == 200) {
                        Log.e("预置位", MessageService.MSG_DB_COMPLETE);
                    }
                }
            });
        }
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void fourPicture(TopicBean topicBean, RecyclerView.Adapter adapter2, int i) {
        Log.d(this.TAG, "onClick: -------------" + this.playBall.getPlayState());
        if (this.playBall.getPlayState() == 2 || this.playBall.getPlayState() == 1) {
            Log.d(this.TAG, "onClick: ");
            return;
        }
        if (!this.isFirst) {
            this.isFirst = true;
            this.isFour = true;
            this.playBall.stop();
            this.binding.fourPic.setData(this.iotIdList);
            this.binding.player.setVisibility(8);
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
            this.playBall.stop();
            this.binding.fourPic.startPlayer();
            this.binding.player.setVisibility(8);
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
            this.binding.player.setVisibility(0);
            this.binding.fourPic.setVisibility(8);
            this.beanInfo = this.binding.fourPic.getDevice();
            DeviceInfoBean deviceInfoBean = this.beanInfo;
            this.device = deviceInfoBean;
            this.iotId = deviceInfoBean.getIotId();
            this.title = this.beanInfo.getName();
            this.isOwner = this.beanInfo.getOwned() == 1;
            this.DeviceName = this.beanInfo.getDeviceName();
            this.ProductKey = this.beanInfo.getProductKey();
            this.binding.llBottom.setVisibility(0);
            this.playBall.setIPCLiveDataSource(this.iotId, 0, false, 0, true, 0);
            this.playBall.prepare();
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
            getProperties(new AnonymousClass79());
            this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.80
                @Override // java.lang.Runnable
                public void run() {
                    IPCDoubleEyeActivity.this.binding.tvTitle.setTitleText(IPCDoubleEyeActivity.this.title);
                }
            });
        }
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$79, reason: invalid class name */
    class AnonymousClass79 implements MyCallback {
        AnonymousClass79() {
        }

        @Override // tools.MyCallback
        public void onComplete(boolean z) {
            if (SharePreferenceManager.getInstance().getMixZoom(IPCDoubleEyeActivity.this.iotId) == 1) {
                IPCDoubleEyeActivity.this.isMixZoom = true;
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.79.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.OSD.setVisibility(8);
                    }
                });
            } else {
                IPCDoubleEyeActivity.this.isMixZoom = false;
            }
            if (SharePreferenceManager.getInstance().getSupportZoom(IPCDoubleEyeActivity.this.iotId) != 1) {
                IPCDoubleEyeActivity.this.isOpticalZoom = false;
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.79.3
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.layoutAf.setVisibility(8);
                    }
                });
            } else {
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.79.2
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.OSD.setVisibility(8);
                    }
                });
                IPCDoubleEyeActivity.this.isOpticalZoom = true;
            }
            if (SharePreferenceManager.getInstance().getSensorViewDisplay(IPCDoubleEyeActivity.this.iotId) == 0) {
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.79.4
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.SensorView.setVisibility(8);
                    }
                });
            } else {
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.79.5
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.SensorView.setVisibility(0);
                        IPCDoubleEyeActivity.this.binding.SensorView.setTemperatureDisplay(false);
                    }
                });
            }
            if (SharePreferenceManager.getInstance().getDisplayController(IPCDoubleEyeActivity.this.iotId) == 0) {
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.79.6
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.llController.setVisibility(8);
                    }
                });
            }
            if (((SharePreferenceManager.getInstance().getPageControlEx(IPCDoubleEyeActivity.this.iotId) & 256) >> 8) == 1) {
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.79.7
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.bottomShop.setVisibility(0);
                        IPCDoubleEyeActivity.this.binding.bottomShop.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.79.7.1
                            @Override // android.view.View.OnClickListener
                            public void onClick(View view2) {
                                IPCDoubleEyeActivity.this.openH5(SharePreferenceManager.getInstance().getUserMallUrl(IPCDoubleEyeActivity.this.iotId));
                            }
                        });
                    }
                });
            }
            IPCDoubleEyeActivity.this.faceDetectionAbility = SharePreferenceManager.getInstance().getHumanoidTracking(IPCDoubleEyeActivity.this.iotId).intValue();
            IPCDoubleEyeActivity.this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(IPCDoubleEyeActivity.this.iotId);
            if (IPCDoubleEyeActivity.this.faceDetectionAbility == 1) {
                IPCDoubleEyeActivity.this.isDetecting = SharePreferenceManager.getInstance().getHumanoidTrackingEnable(IPCDoubleEyeActivity.this.iotId).intValue() == 1;
            } else {
                IPCDoubleEyeActivity.this.isDetecting = SharePreferenceManager.getInstance().getIntelligentMode(IPCDoubleEyeActivity.this.iotId) == 1;
            }
        }
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void mall() {
        openH5(SharePreferenceManager.getInstance().getUserMallUrl(this.iotId));
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void zoom() {
        this.isMoreFragmentShow = false;
        FragmentTransaction fragmentTransactionBeginTransaction = getSupportFragmentManager().beginTransaction();
        if (this.isOldPresetDevice) {
            fragmentTransactionBeginTransaction.hide(this.controllerFragment).hide(this.moreFragment).hide(this.oldPresetFragment);
            fragmentTransactionBeginTransaction.commitAllowingStateLoss();
            this.binding.moreImage.setBackgroundResource(R.drawable.more_ipc);
            this.binding.moreTextDoubleEye.setTextColor(getResources().getColor(R.color.colors_ipc_image_text));
        } else {
            fragmentTransactionBeginTransaction.hide(this.controllerFragment).hide(this.moreFragment).hide(this.presetFragment);
            fragmentTransactionBeginTransaction.commitAllowingStateLoss();
            this.binding.moreImage.setBackgroundResource(R.drawable.more_ipc);
            this.binding.moreTextDoubleEye.setTextColor(getResources().getColor(R.color.colors_ipc_image_text));
        }
        this.binding.ptzText.setBackgroundResource(0);
        this.binding.zoomText.setBackgroundResource(R.drawable.text_underline);
        this.binding.rlTouchView.setVisibility(8);
        this.binding.ZOOMView.setVisibility(0);
        this.binding.autorView.setVisibility(8);
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void netWorkSwitch() {
        isNet4GSwitch();
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
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

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void smart() {
        FragmentTransaction fragmentTransactionBeginTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransactionBeginTransaction.show(this.controllerFragment).hide(this.moreFragment);
        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
        getControllerList();
        this.controllerFragment.setList(this.controllerList);
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void share() {
        this.shareDialog2 = ((ShareDialog.Builder) new ShareDialog.Builder().view(R.layout.dialog_input2).leftBtnText(getString(R.string.cancel)).rightBtnText(getString(R.string.share_immediately)).clickRight(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.81
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (IPCDoubleEyeActivity.this.shareDialog2.getContent() != null) {
                    if (IPCDoubleEyeActivity.this.shareDialog2.getMode() != 0 || SystemUtil.isPhone(IPCDoubleEyeActivity.this.shareDialog2.getContent())) {
                        if (IPCDoubleEyeActivity.this.shareDialog2.getMode() == 1 && !SystemUtil.isEmail(IPCDoubleEyeActivity.this.shareDialog2.getContent())) {
                            ToastUtils.toast(IPCDoubleEyeActivity.this.getActivity(), IPCDoubleEyeActivity.this.getString(R.string.email_invalid));
                            return;
                        }
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(IPCDoubleEyeActivity.this.nvrIotId);
                        arrayList.add(IPCDoubleEyeActivity.this.ballIotId);
                        arrayList.add(IPCDoubleEyeActivity.this.gunIotId);
                        IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                        iPCDoubleEyeActivity.shareDevice(iPCDoubleEyeActivity.shareDialog2.getContent(), arrayList, IPCDoubleEyeActivity.this.shareDialog2.getMode() == 0 ? IPCDoubleEyeActivity.this.shareDialog2.getDistinct() : null);
                        return;
                    }
                    ToastUtils.toast(IPCDoubleEyeActivity.this.getActivity(), IPCDoubleEyeActivity.this.getString(R.string.phone_invalid));
                    return;
                }
                Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.share_user_null_error, 0).show();
            }
        })).create();
        this.shareDialog2.setOnShareClick(new ShareDialog.OnShareClickListener() { // from class: activity.IPCDoubleEyeActivity.82
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
        this.shareDialog2.setExtra(this.device);
        this.shareDialog2.show(getSupportFragmentManager(), "");
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void SDplayBack() {
        Intent intent = new Intent(this, (Class<?>) RecordVideoActivity.class);
        intent.putExtra("title", this.binding.tvTitle.getTitleText());
        intent.putExtra("iotId", this.iotId);
        intent.putExtra("iotId2", this.iotId2);
        intent.putExtra("appKey", this.appKey);
        startActivity(intent);
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void CustomerService() {
        Intent intent = new Intent(this, (Class<?>) CustomerServiceActivity.class);
        intent.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
        intent.putExtra("device1", this.device1);
        intent.putExtra("nvrDevice", this.nvrDevice);
        startActivity(intent);
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void doubleSameWindow(int i) {
        float f = (ScreenUtil.getDisplayMetrics(getActivity())[0] * 9.0f) / 16.0f;
        if (this.isRecording) {
            showToast("正在录屏中，切换失败");
        }
        this.showMode++;
        if (this.showMode > 2) {
            this.showMode = 0;
        }
        this.moreFragment.updateData(this.isDetecting, this.lightVisible, SharePreferenceManager.getInstance().getSupport4G(this.device.getIotId()) == 1, this.currentInfrarred2, this.switchText, this.shopVisible, this.smartDoorVisible, this.isFourState, this.supportMotionDetect, this.showMode);
        switch (this.showMode) {
            case 0:
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-1, ((int) this.viewHeight) / 2);
                int i2 = (int) f;
                layoutParams.height = i2;
                layoutParams.topToTop = this.binding.portraitPlayer.getId();
                layoutParams.startToStart = this.binding.portraitPlayer.getId();
                layoutParams.endToEnd = this.binding.portraitPlayer.getId();
                layoutParams.bottomToTop = this.binding.player.getId();
                this.binding.player2.setLayoutParams(layoutParams);
                ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(-1, ((int) this.viewHeight) / 2);
                layoutParams2.height = i2;
                layoutParams2.topToTop = this.binding.portraitPlayer.getId();
                layoutParams2.startToStart = this.binding.portraitPlayer.getId();
                layoutParams2.endToEnd = this.binding.portraitPlayer.getId();
                layoutParams2.setMargins(0, 25, 0, 0);
                this.binding.lineViewItem.setLayoutParams(layoutParams2);
                ConstraintLayout.LayoutParams layoutParams3 = new ConstraintLayout.LayoutParams(-1, ((int) this.viewHeight) / 2);
                layoutParams3.height = i2;
                layoutParams3.startToStart = this.binding.portraitPlayer.getId();
                layoutParams3.endToEnd = this.binding.portraitPlayer.getId();
                layoutParams3.bottomToBottom = this.binding.portraitPlayer.getId();
                layoutParams3.topToBottom = this.binding.player2.getId();
                this.binding.player.setLayoutParams(layoutParams3);
                if (this.isRatio) {
                    setViewLayoutParams(this.binding.portraitPlayer, -1, (int) this.viewHeight);
                    MyGlTextureView myGlTextureView = this.binding.player2;
                    double d2 = this.viewHeight;
                    setViewLayoutParams(myGlTextureView, (((int) (d2 / 2.0d)) / 9) * 16, (int) (d2 / 2.0d));
                    MyGlTextureView myGlTextureView2 = this.binding.player;
                    double d3 = this.viewHeight;
                    setViewLayoutParams(myGlTextureView2, (((int) (d3 / 2.0d)) / 9) * 16, (int) (d3 / 2.0d));
                    CircleTooView circleTooView = this.binding.lineViewItem;
                    double d4 = this.viewHeight;
                    setViewLayoutParams(circleTooView, (((int) (d4 / 2.0d)) / 9) * 16, (int) (d4 / 2.0d));
                }
                this.binding.player.setVisibility(0);
                this.binding.player2.setVisibility(0);
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.device.getIotId()) == 0) {
                    this.binding.ivLightWhile2.setVisibility(0);
                }
                if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(this.device.getIotId()) == 1) {
                    this.binding.ivLightWhile2.setVisibility(8);
                    this.binding.fullNightVision.setVisibility(8);
                }
                if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.iotId) == 0) {
                    this.binding.ivLightWhile2.setVisibility(8);
                }
                this.binding.lineViewItem.setVisibility(SharePreferenceManager.getInstance().getPTZLinkageTrackSwitch(this.gunIotId) == 0 ? 8 : 0);
                break;
            case 1:
                this.selectIotId = this.iotId2;
                int i3 = (int) f;
                ConstraintLayout.LayoutParams layoutParams4 = new ConstraintLayout.LayoutParams(-1, i3);
                layoutParams4.height = i3;
                layoutParams4.topToTop = this.binding.portraitPlayer.getId();
                layoutParams4.bottomToBottom = this.binding.portraitPlayer.getId();
                this.binding.player2.setLayoutParams(layoutParams4);
                this.binding.player2.setVisibility(0);
                this.binding.player.setVisibility(8);
                this.binding.ivLightWhile2.setVisibility(8);
                this.binding.lineViewItem.setVisibility(8);
                if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(this.device.getIotId()) == 1) {
                    this.binding.ivLightWhile.setVisibility(8);
                    this.binding.fullNightVision.setVisibility(8);
                }
                break;
            case 2:
                this.selectIotId = this.iotId;
                int i4 = (int) f;
                ConstraintLayout.LayoutParams layoutParams5 = new ConstraintLayout.LayoutParams(-1, i4);
                layoutParams5.height = i4;
                layoutParams5.topToTop = this.binding.portraitPlayer.getId();
                layoutParams5.bottomToBottom = this.binding.portraitPlayer.getId();
                this.binding.player.setLayoutParams(layoutParams5);
                this.binding.player.setVisibility(0);
                this.binding.player2.setVisibility(8);
                this.binding.lineViewItem.setVisibility(8);
                if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(this.device.getIotId()) == 1) {
                    this.binding.ivLightWhile.setVisibility(0);
                }
                break;
        }
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void Floodlight() {
        HashMap map = new HashMap();
        map.put(Constants.FloodlightSwitch, Integer.valueOf(SharePreferenceManager.getInstance().getFloodlightSwitch(this.iotId) == 1 ? 0 : 1));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.83
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (z) {
                    new Handler().post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.83.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (SharePreferenceManager.getInstance().getFloodlightSwitch(IPCDoubleEyeActivity.this.iotId) == 0) {
                                SharePreferenceManager.getInstance().setFloodlightSwitch(IPCDoubleEyeActivity.this.iotId, 1);
                            } else {
                                SharePreferenceManager.getInstance().setFloodlightSwitch(IPCDoubleEyeActivity.this.iotId, 0);
                            }
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$84, reason: invalid class name */
    class AnonymousClass84 implements IPanelCallback {
        AnonymousClass84() {
        }

        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
        public void onComplete(boolean z, @Nullable final Object obj) {
            if (z) {
                if (obj != null && !"".equals(String.valueOf(obj))) {
                    IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.84.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Log.e("基站定位信息", String.valueOf(obj));
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.getInteger("code").intValue() != 200) {
                                IPCDoubleEyeActivity.this.showToast(IPCDoubleEyeActivity.this.getString(R.string.play_failed_retry));
                                return;
                            }
                            if (!String.valueOf(obj).contains("CellIdentity")) {
                                IPCDoubleEyeActivity.this.showToast(IPCDoubleEyeActivity.this.getString(R.string.play_failed_retry));
                                return;
                            }
                            JSONObject jSONObject = object.getJSONObject("data");
                            String string = jSONObject.getString("CellIdentity");
                            jSONObject.getInteger("MobileNetworkCode").intValue();
                            String string2 = jSONObject.getString("TrackingAreaCode");
                            int i = Integer.parseInt(string, 16);
                            int i2 = Integer.parseInt(string2, 16);
                            new OkHttpClient().newCall(new Request.Builder().url("http://api.cellocation.com:84/cell/?mcc=460&mnc=1&lac=" + i2 + "&ci=" + i + "&output=json").get().build()).enqueue(new Callback() { // from class: activity.IPCDoubleEyeActivity.84.1.1
                                static final /* synthetic */ boolean $assertionsDisabled = false;

                                @Override // okhttp3.Callback
                                public void onFailure(Call call, IOException iOException) {
                                    IPCDoubleEyeActivity.this.showToast(IPCDoubleEyeActivity.this.getString(R.string.play_failed_retry));
                                }

                                @Override // okhttp3.Callback
                                public void onResponse(Call call, Response response) throws IOException {
                                    try {
                                        JSONObject object2 = JSONObject.parseObject(response.body().string());
                                        if (object2.getInteger("errcode").intValue() != 0) {
                                            IPCDoubleEyeActivity.this.showToast(IPCDoubleEyeActivity.this.getString(R.string.play_failed_retry));
                                            return;
                                        }
                                        IPCDoubleEyeActivity.this.lat = object2.getString(DispatchConstants.LATITUDE);
                                        IPCDoubleEyeActivity.this.lon = object2.getString("lon");
                                        object2.getString("radius");
                                        IPCDoubleEyeActivity.this.address = object2.getString("address");
                                        IPCDoubleEyeActivity.this.mapFragment.showAllowingStateLoss(IPCDoubleEyeActivity.this.getSupportFragmentManager(), "");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                    return;
                } else {
                    IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                    iPCDoubleEyeActivity.showToast(iPCDoubleEyeActivity.getString(R.string.play_failed_retry));
                    return;
                }
            }
            IPCDoubleEyeActivity iPCDoubleEyeActivity2 = IPCDoubleEyeActivity.this;
            iPCDoubleEyeActivity2.showToast(iPCDoubleEyeActivity2.getString(R.string.play_failed_retry));
        }
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void Locate() {
        IPCManager.getInstance().getDevice(this.ballIotId).getLocationBasedService(new AnonymousClass84());
    }

    @Override // fragment.MoreFragmentDoubleEye.FragmentContextChangeListener
    public void GPSLocate() {
        IPCManager.getInstance().getDevice(this.ballIotId).getGPSPositioningService(new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.85
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable final Object obj) {
                if (z) {
                    if (obj != null && !"".equals(String.valueOf(obj))) {
                        IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.85.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Log.e("基站定位信息", String.valueOf(obj));
                                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                if (object.getInteger("code").intValue() != 200) {
                                    IPCDoubleEyeActivity.this.showToast(IPCDoubleEyeActivity.this.getString(R.string.play_failed_retry));
                                    return;
                                }
                                if (!String.valueOf(obj).contains("Latitude")) {
                                    IPCDoubleEyeActivity.this.showToast(IPCDoubleEyeActivity.this.getString(R.string.play_failed_retry));
                                    return;
                                }
                                JSONObject jSONObject = object.getJSONObject("data");
                                String string = jSONObject.getString("Latitude");
                                String string2 = jSONObject.getString("Longitude");
                                MapUtils.dddmmToDecimal(Double.parseDouble(string));
                                IPCDoubleEyeActivity.this.lat = MapUtils.dddmmToDecimal(Double.parseDouble(string)) + "";
                                IPCDoubleEyeActivity.this.lon = MapUtils.dddmmToDecimal(Double.parseDouble(string2)) + "";
                                IPCDoubleEyeActivity.this.mapFragment.showAllowingStateLoss(IPCDoubleEyeActivity.this.getSupportFragmentManager(), "");
                            }
                        });
                        return;
                    } else {
                        IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                        iPCDoubleEyeActivity.showToast(iPCDoubleEyeActivity.getString(R.string.play_failed_retry));
                        return;
                    }
                }
                IPCDoubleEyeActivity iPCDoubleEyeActivity2 = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity2.showToast(iPCDoubleEyeActivity2.getString(R.string.play_failed_retry));
            }
        });
    }

    public static void setViewLayoutParams(View view2, int i, int i2) {
        ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
        if (layoutParams.height == i2 && layoutParams.width == i) {
            return;
        }
        layoutParams.width = i;
        layoutParams.height = i2;
        view2.setLayoutParams(layoutParams);
    }

    @Override // fragment.PresetFragment.PresetBackListener
    public void presetBack() {
        this.binding.llBottom.setVisibility(0);
        getSupportFragmentManager().beginTransaction().hide(this.presetFragment).show(this.moreFragment).commitAllowingStateLoss();
        this.binding.f6228fragment.setClickable(false);
        this.isMoreFragmentShow = true;
    }

    @Override // fragment.PresetFragment.PresetDataChange
    public void snapPicture(PresetBean presetBean, RecyclerView.Adapter adapter2, int i) {
        Bitmap bitmapSnapShot;
        if (ActivityCompat.checkSelfPermission(getActivity(), Permission.WRITE_EXTERNAL_STORAGE) == 0 && this.playBall.getPlayState() == 3 && (bitmapSnapShot = this.playBall.snapShot()) != null && bitmapSnapShot != null) {
            saveBitmap(bitmapSnapShot, i, presetBean, adapter2);
        }
    }

    @Override // fragment.PresetFragment.PresetDataChange
    public void deletePicture(PresetBean presetBean, RecyclerView.Adapter adapter2, int i) {
        deleteBitmap(i, presetBean, adapter2);
    }

    @Override // fragment.PresetFragment.PresetDataChange
    public void outDelete() {
        this.binding.llBottom.setVisibility(0);
    }

    @Override // fragment.OldPresetFragment.OldPresetBackListener
    public void oldPresetBack() {
        getSupportFragmentManager().beginTransaction().hide(this.oldPresetFragment).show(this.moreFragment).commitAllowingStateLoss();
        this.binding.f6228fragment.setClickable(false);
        this.isMoreFragmentShow = true;
    }

    private void initPlayer() {
        this.playBall = new LivePlayer(getApplicationContext());
        this.playBall.setTextureView(this.binding.player);
        this.binding.player.setClickable(true);
        this.playBall.setVolume(this.speakerSwitch ? 1.0f : 0.0f);
        this.playBall.setVideoScalingMode(1);
        this.binding.player.setOnZoomableTextureListener(new ZoomableTextureView.OnZoomableTextureListener() { // from class: activity.IPCDoubleEyeActivity.86
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
                Log.d(IPCDoubleEyeActivity.this.TAG, "onScaleChanged: " + dDoubleValue);
                if (IPCDoubleEyeActivity.this.isMixZoom) {
                    str = decimalFormat.format((((dDoubleValue - 1.0d) * 2.25d) + 1.0d) * ((double) IPCDoubleEyeActivity.this.ZoomMax));
                } else {
                    str = decimalFormat.format(((dDoubleValue - 1.0d) * 2.25d) + 1.0d);
                }
                IPCDoubleEyeActivity.this.zoom.postValue(Float.valueOf(f));
                IPCDoubleEyeActivity.this.binding.OSD.setText(str + "X");
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onSingleTapConfirmed(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                IPCDoubleEyeActivity.this.isFloat = !r2.isFloat;
                IPCDoubleEyeActivity.this.setFloatBarState();
                return true;
            }
        });
        this.playBall.setOnErrorListener(new AnonymousClass87());
        this.playBall.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCDoubleEyeActivity.88
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                switch (i) {
                    case 1:
                        LogEx.i(true, IPCDoubleEyeActivity.this.TAG, "STATE_IDLE");
                        break;
                    case 2:
                        IPCDoubleEyeActivity.this.dismissPlayButton();
                        IPCDoubleEyeActivity.this.showBuffering();
                        if (SharePreferenceManager.getInstance().getLowPower(IPCDoubleEyeActivity.this.device.getIotId()) == 1) {
                            IPCDoubleEyeActivity.this.binding.wakeupText.setVisibility(8);
                            IPCDoubleEyeActivity.this.binding.wakeupText.bringToFront();
                        } else {
                            IPCDoubleEyeActivity.this.binding.wakeupText.setVisibility(8);
                        }
                        LogEx.i(true, IPCDoubleEyeActivity.this.TAG, "STATE_BUFFERING");
                        break;
                    case 3:
                        IPCDoubleEyeActivity.this.lowPowerMode = 1;
                        IPCDoubleEyeActivity.this.needWakeUp = false;
                        IPCDoubleEyeActivity.this.is1100ErrorPre = 10;
                        IPCDoubleEyeActivity.this.dismissSnapPicture();
                        IPCDoubleEyeActivity.this.dismissBuffering();
                        IPCDoubleEyeActivity.this.binding.wakeupText.setVisibility(8);
                        LogEx.i(true, IPCDoubleEyeActivity.this.TAG, "STATE_READY");
                        IPCDoubleEyeActivity.this.isFirstShowStreamType = true;
                        IPCDoubleEyeActivity.this.showPlayInfo();
                        break;
                    case 4:
                        LogEx.i(true, IPCDoubleEyeActivity.this.TAG, "STATE_ENDED");
                        IPCDoubleEyeActivity.this.dismissPlayInfo();
                        if (SharePreferenceManager.getInstance().getLowPower(IPCDoubleEyeActivity.this.device.getIotId()) == 1 && IPCDoubleEyeActivity.this.nvrDevice.getStatus() == 1) {
                            IPCDoubleEyeActivity.this.binding.wakeupText.setVisibility(8);
                            IPCDoubleEyeActivity.this.binding.wakeupText.bringToFront();
                        }
                        IPCDoubleEyeActivity.this.playBall.stopRecordingContent();
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$87, reason: invalid class name */
    class AnonymousClass87 implements OnErrorListener {
        AnonymousClass87() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
        public void onError(PlayerException playerException) {
            Log.e(playerException.getLocalizedMessage() + "   在线修改  getCode=" + playerException.getCode(), "    getSubCode=" + playerException.getSubCode());
            if (playerException.getSubCode() != 1009 || playerException.getCode() != 6 || !playerException.getLocalizedMessage().equals("请求认证错误")) {
                if (!IPCDoubleEyeActivity.this.needWakeUp || IPCDoubleEyeActivity.this.countWakeUp >= 5) {
                    switch (playerException.getCode()) {
                        case 6:
                            switch (playerException.getSubCode()) {
                                case 1005:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity.showToast(iPCDoubleEyeActivity.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1006:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity2 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity2.showToast(iPCDoubleEyeActivity2.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1007:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity3 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity3.showToast(iPCDoubleEyeActivity3.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1008:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity4 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity4.showToast(iPCDoubleEyeActivity4.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1009:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity5 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity5.showToast(iPCDoubleEyeActivity5.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                            }
                            break;
                        case 7:
                            if (playerException.getSubCode() == 1000) {
                                IPCDoubleEyeActivity iPCDoubleEyeActivity6 = IPCDoubleEyeActivity.this;
                                iPCDoubleEyeActivity6.showToast(iPCDoubleEyeActivity6.getString(R.string.play_failed_retry, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                            }
                            break;
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCDoubleEyeActivity.this.is1100ErrorPre <= 0) {
                                    if (SharePreferenceManager.getInstance().getDoubleNetWork(IPCDoubleEyeActivity.this.iotId) == 1 && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCDoubleEyeActivity.this.iotId) == 1) {
                                        IPCDoubleEyeActivity.this.showBadNetDialog();
                                    }
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity7 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity7.showToast(iPCDoubleEyeActivity7.getString(R.string.play_failed_retry, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                } else {
                                    IPCDoubleEyeActivity.access$8510(IPCDoubleEyeActivity.this);
                                    SharePreferenceManager.getInstance().getStreamVideoQuality(IPCDoubleEyeActivity.this.iotId);
                                    IPCDoubleEyeActivity.this.playBall.stop();
                                    if (SharePreferenceManager.getInstance().getNetState(IPCDoubleEyeActivity.this.device.getIotId()) != 3) {
                                        Handler handler = IPCDoubleEyeActivity.this.uiHandler;
                                        final IPCDoubleEyeActivity iPCDoubleEyeActivity8 = IPCDoubleEyeActivity.this;
                                        handler.postDelayed(new Runnable() { // from class: activity.-$$Lambda$IPCDoubleEyeActivity$87$3xEsFhaohJFDuYr5jwXxMfzPq60
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                iPCDoubleEyeActivity8.playLive();
                                            }
                                        }, 500L);
                                        return;
                                    }
                                    return;
                                }
                            }
                            break;
                    }
                    if (IPCDoubleEyeActivity.this.needRecharge) {
                        return;
                    }
                    IPCDoubleEyeActivity.this.showPlayButton();
                    return;
                }
                return;
            }
            IPCDoubleEyeActivity iPCDoubleEyeActivity9 = IPCDoubleEyeActivity.this;
            iPCDoubleEyeActivity9.showToast(iPCDoubleEyeActivity9.getString(R.string.account_squeezed));
        }
    }

    private void initPlayer2() {
        this.playGun = new LivePlayer(getApplicationContext());
        this.playGun.setTextureView(this.binding.player2);
        this.binding.player2.setClickable(true);
        this.playGun.setVolume(this.speakerSwitch ? 1.0f : 0.0f);
        this.playGun.setVideoScalingMode(1);
        this.binding.player2.setOnZoomableTextureListener(new ZoomableTextureView.OnZoomableTextureListener() { // from class: activity.IPCDoubleEyeActivity.89
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
            }

            @Override // view.ZoomableTextureView.OnZoomableTextureListener
            public boolean onSingleTapConfirmed(ZoomableTextureView zoomableTextureView, MotionEvent motionEvent) {
                IPCDoubleEyeActivity.this.isFloat = !r2.isFloat;
                IPCDoubleEyeActivity.this.setFloatBarState();
                return true;
            }
        });
        this.playGun.setOnErrorListener(new AnonymousClass90());
        this.playGun.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.IPCDoubleEyeActivity.91
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                switch (i) {
                    case 1:
                        LogEx.i(true, IPCDoubleEyeActivity.this.TAG, "STATE_IDLE");
                        break;
                    case 2:
                        IPCDoubleEyeActivity.this.dismissPlayButton();
                        IPCDoubleEyeActivity.this.showBuffering();
                        if (SharePreferenceManager.getInstance().getLowPower(IPCDoubleEyeActivity.this.device.getIotId()) == 1) {
                            IPCDoubleEyeActivity.this.binding.wakeupText.setVisibility(8);
                        } else {
                            IPCDoubleEyeActivity.this.binding.wakeupText.setVisibility(8);
                        }
                        LogEx.i(true, IPCDoubleEyeActivity.this.TAG, "STATE_BUFFERING");
                        break;
                    case 3:
                        if (IPCDoubleEyeActivity.this.showMode == 0) {
                            if (SharePreferenceManager.getInstance().getNightVisionHide(IPCDoubleEyeActivity.this.device.getIotId()) == 0) {
                                IPCDoubleEyeActivity.this.binding.ivLightWhile2.setVisibility(0);
                            }
                            if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(IPCDoubleEyeActivity.this.iotId) == 0) {
                                IPCDoubleEyeActivity.this.binding.ivLightWhile2.setVisibility(8);
                            }
                            if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(IPCDoubleEyeActivity.this.device.getIotId()) == 1) {
                                IPCDoubleEyeActivity.this.binding.ivLightWhile2.setVisibility(8);
                                IPCDoubleEyeActivity.this.binding.fullNightVision.setVisibility(8);
                            }
                        }
                        IPCDoubleEyeActivity.this.lowPowerMode = 1;
                        IPCDoubleEyeActivity.this.needWakeUp = false;
                        IPCDoubleEyeActivity.this.is1100ErrorPre = 10;
                        IPCDoubleEyeActivity.this.dismissSnapPicture();
                        IPCDoubleEyeActivity.this.dismissBuffering();
                        IPCDoubleEyeActivity.this.binding.wakeupText.setVisibility(8);
                        LogEx.i(true, IPCDoubleEyeActivity.this.TAG, "STATE_READY");
                        IPCDoubleEyeActivity.this.isFirstShowStreamType = true;
                        IPCDoubleEyeActivity.this.showPlayInfo();
                        break;
                    case 4:
                        LogEx.i(true, IPCDoubleEyeActivity.this.TAG, "STATE_ENDED");
                        IPCDoubleEyeActivity.this.dismissPlayInfo();
                        IPCDoubleEyeActivity.this.playGun.stopRecordingContent();
                        break;
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$90, reason: invalid class name */
    class AnonymousClass90 implements OnErrorListener {
        AnonymousClass90() {
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
        public void onError(PlayerException playerException) {
            if (playerException.getSubCode() != 1009 || playerException.getCode() != 6 || !playerException.getLocalizedMessage().equals("请求认证错误")) {
                if (!IPCDoubleEyeActivity.this.needWakeUp || IPCDoubleEyeActivity.this.countWakeUp >= 5) {
                    switch (playerException.getCode()) {
                        case 6:
                            switch (playerException.getSubCode()) {
                                case 1005:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity.showToast(iPCDoubleEyeActivity.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1006:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity2 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity2.showToast(iPCDoubleEyeActivity2.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1007:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity3 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity3.showToast(iPCDoubleEyeActivity3.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1008:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity4 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity4.showToast(iPCDoubleEyeActivity4.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                case 1009:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity5 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity5.showToast(iPCDoubleEyeActivity5.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                            }
                            break;
                        case 7:
                            if (playerException.getSubCode() == 1000) {
                                IPCDoubleEyeActivity iPCDoubleEyeActivity6 = IPCDoubleEyeActivity.this;
                                iPCDoubleEyeActivity6.showToast(iPCDoubleEyeActivity6.getString(R.string.play_failed_retry, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                            }
                            break;
                        case 8:
                            if (playerException.getSubCode() == 1100) {
                                if (IPCDoubleEyeActivity.this.is1100ErrorPre <= 0) {
                                    if (SharePreferenceManager.getInstance().getDoubleNetWork(IPCDoubleEyeActivity.this.iotId) == 1 && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCDoubleEyeActivity.this.iotId) == 1) {
                                        IPCDoubleEyeActivity.this.showBadNetDialog();
                                    }
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity7 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity7.showToast(iPCDoubleEyeActivity7.getString(R.string.play_failed_retry, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                } else {
                                    IPCDoubleEyeActivity.access$8510(IPCDoubleEyeActivity.this);
                                    SharePreferenceManager.getInstance().getStreamVideoQuality(IPCDoubleEyeActivity.this.iotId2);
                                    IPCDoubleEyeActivity.this.playGun.stop();
                                    if (SharePreferenceManager.getInstance().getNetState(IPCDoubleEyeActivity.this.device.getIotId()) != 3) {
                                        Handler handler = IPCDoubleEyeActivity.this.uiHandler;
                                        final IPCDoubleEyeActivity iPCDoubleEyeActivity8 = IPCDoubleEyeActivity.this;
                                        handler.postDelayed(new Runnable() { // from class: activity.-$$Lambda$IPCDoubleEyeActivity$90$K2x_DLfzIZ-oWcYvqbAadv2D_i0
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                iPCDoubleEyeActivity8.playLive();
                                            }
                                        }, 500L);
                                        return;
                                    }
                                    return;
                                }
                            }
                            break;
                    }
                    if (IPCDoubleEyeActivity.this.needRecharge) {
                        return;
                    }
                    IPCDoubleEyeActivity.this.showPlayButton();
                    return;
                }
                return;
            }
            IPCDoubleEyeActivity iPCDoubleEyeActivity9 = IPCDoubleEyeActivity.this;
            iPCDoubleEyeActivity9.showToast(iPCDoubleEyeActivity9.getString(R.string.account_squeezed));
        }
    }

    private void initLiveIntercom(String str) {
        this.liveIntercom = new LiveIntercomV2(this, str, LiveIntercomV2.LiveIntercomMode.SingleTalk, AudioParams.AUDIOPARAM_MONO_8K_G711A);
        this.liveIntercom.setGainLevel(-1);
        this.liveIntercom.setLiveIntercomV2Listener(new LiveIntercomV2Listener() { // from class: activity.IPCDoubleEyeActivity.92
            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordBufferReceived(byte[] bArr, int i, int i2) {
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onTalkReady() {
                LogEx.e(true, "speaker----", "1 " + IPCDoubleEyeActivity.this.isLiveIntercoming);
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.92.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.showToast(IPCDoubleEyeActivity.this.getResources().getString(R.string.can_begin_talk));
                        if (IPCDoubleEyeActivity.this.isFinishing()) {
                            return;
                        }
                        IPCDoubleEyeActivity.this.whiteProgressDialog.dismiss();
                        IPCDoubleEyeActivity.this.setSpeakerBtn(2);
                        IPCDoubleEyeActivity.this.binding.speakerBtn.setEnabled(true);
                    }
                });
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onError(LiveIntercomException liveIntercomException) {
                LogEx.e(true, "speaker----", "2 " + IPCDoubleEyeActivity.this.isLiveIntercoming);
                int code = liveIntercomException.getCode();
                if (code != 16) {
                    switch (code) {
                        case 1:
                            IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                            iPCDoubleEyeActivity.showToast(iPCDoubleEyeActivity.getString(R.string.record_error1));
                            IPCDoubleEyeActivity.this.handleLiveIntercomError();
                            break;
                        case 2:
                            IPCDoubleEyeActivity iPCDoubleEyeActivity2 = IPCDoubleEyeActivity.this;
                            iPCDoubleEyeActivity2.showToast(iPCDoubleEyeActivity2.getString(R.string.record_error2));
                            IPCDoubleEyeActivity.this.handleLiveIntercomError();
                            break;
                        case 3:
                            IPCDoubleEyeActivity iPCDoubleEyeActivity3 = IPCDoubleEyeActivity.this;
                            iPCDoubleEyeActivity3.showToast(iPCDoubleEyeActivity3.getString(R.string.record_error3));
                            IPCDoubleEyeActivity.this.handleLiveIntercomError();
                            break;
                        default:
                            switch (code) {
                                case 5:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity4 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity4.showToast(iPCDoubleEyeActivity4.getString(R.string.record_error4));
                                    IPCDoubleEyeActivity.this.handleLiveIntercomError();
                                    break;
                                case 6:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity5 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity5.showToast(iPCDoubleEyeActivity5.getString(R.string.record_error5));
                                    IPCDoubleEyeActivity.this.handleLiveIntercomError();
                                    break;
                                case 7:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity6 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity6.showToast(iPCDoubleEyeActivity6.getString(R.string.record_error6));
                                    IPCDoubleEyeActivity.this.onRecordError();
                                    break;
                                case 8:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity7 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity7.showToast(iPCDoubleEyeActivity7.getString(R.string.record_error7));
                                    IPCDoubleEyeActivity.this.onRecordError();
                                    break;
                                case 9:
                                    IPCDoubleEyeActivity iPCDoubleEyeActivity8 = IPCDoubleEyeActivity.this;
                                    iPCDoubleEyeActivity8.showToast(iPCDoubleEyeActivity8.getString(R.string.record_error8));
                                    IPCDoubleEyeActivity.this.onRecordError();
                                    break;
                            }
                            break;
                    }
                } else {
                    IPCDoubleEyeActivity iPCDoubleEyeActivity9 = IPCDoubleEyeActivity.this;
                    iPCDoubleEyeActivity9.showToast(iPCDoubleEyeActivity9.getString(R.string.record_error9));
                    IPCDoubleEyeActivity.this.onRecordError();
                }
                liveIntercomException.printStackTrace();
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordStart() {
                LogEx.d(true, IPCDoubleEyeActivity.this.TAG, "onRecordStart");
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.92.2
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.isLiveIntercoming = true;
                    }
                });
            }

            @Override // com.aliyun.iotx.linkvisual.media.audio.listener.LiveIntercomV2Listener
            public void onRecordEnd() {
                LogEx.d(true, IPCDoubleEyeActivity.this.TAG, "onRecordEnd");
                IPCDoubleEyeActivity.this.liveIntercom.stop();
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.92.3
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.isLiveIntercoming = false;
                        if (IPCDoubleEyeActivity.this.isFinishing()) {
                            return;
                        }
                        IPCDoubleEyeActivity.this.whiteProgressDialog.dismiss();
                        IPCDoubleEyeActivity.this.setSpeakerBtn(0);
                        IPCDoubleEyeActivity.this.binding.speakerBtn.setEnabled(true);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playLive() {
        LivePlayer livePlayer = this.playBall;
        if (livePlayer != null) {
            livePlayer.stop();
        }
        LivePlayer livePlayer2 = this.playGun;
        if (livePlayer2 != null) {
            livePlayer2.stop();
        }
        if (isFinishing()) {
            return;
        }
        if (this.playBall.getPlayState() != 3) {
            this.binding.player.reset();
            showSnapPicture();
        }
        if (this.playGun.getPlayState() != 3) {
            this.binding.player2.reset();
            showSnapPicture();
        }
        LogEx.i(true, this.TAG, "playLive");
        if (this.device.getStatus() == 1) {
            this.playBall.setIPCLiveDataSource(this.iotId, 0, false, 0, true, 0);
            this.playBall.setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCDoubleEyeActivity.93
                @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
                public void onPrepared() {
                    IPCDoubleEyeActivity.this.playBall.start();
                }
            });
            this.playBall.prepare();
        }
        if (this.device1.getStatus() == 1) {
            Log.d(this.TAG, "playLive: puppet:iotId1====" + this.iotId2);
            this.playGun.setIPCLiveDataSource(this.iotId2, 0, false, 0, true, 0);
            this.playGun.setOnPreparedListener(new OnPreparedListener() { // from class: activity.IPCDoubleEyeActivity.94
                @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
                public void onPrepared() {
                    IPCDoubleEyeActivity.this.playGun.start();
                }
            });
            this.playGun.prepare();
        }
        keepScreenLight();
        this.binding.lineViewItem.bringToFront();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wakeUpDevice() {
        if (this.playBall.getPlayState() == 3 || this.playGun.getPlayState() == 3 || !isActivityForeground()) {
            return;
        }
        this.countWakeUp = 0;
        HashMap map = new HashMap();
        map.put(Constants.LowPowerAppStatus, 1);
        IPCManager.getInstance().getDevice(this.ballIotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.95
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                IPCDoubleEyeActivity.this.needWakeUp = true;
                if (IPCDoubleEyeActivity.this.playBall != null && IPCDoubleEyeActivity.this.playBall.getPlayState() != 3) {
                    SettingsCtrl.getInstance().getProperties(IPCDoubleEyeActivity.this.ballIotId, new AnonymousClass1());
                }
                IPCDoubleEyeActivity.access$8408(IPCDoubleEyeActivity.this);
                if (IPCDoubleEyeActivity.this.wakeUpHandler != null) {
                    IPCDoubleEyeActivity.this.wakeUpHandler.sendEmptyMessageDelayed(1, 8000L);
                }
            }

            /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$95$1, reason: invalid class name */
            class AnonymousClass1 implements MyCallback {
                AnonymousClass1() {
                }

                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (SharePreferenceManager.getInstance().getLowPowerStatus(IPCDoubleEyeActivity.this.ballIotId) == 1) {
                        Handler handler = IPCDoubleEyeActivity.this.uiHandler;
                        final IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                        handler.post(new Runnable() { // from class: activity.-$$Lambda$IPCDoubleEyeActivity$95$1$PSlzwjisbTmyCq6umvUez1G7dKo
                            @Override // java.lang.Runnable
                            public final void run() {
                                iPCDoubleEyeActivity.playLive();
                            }
                        });
                    }
                }
            }
        });
        LivePlayer livePlayer = this.playBall;
        if (livePlayer != null && livePlayer.getPlayState() != 3) {
            SettingsCtrl.getInstance().getProperties(this.ballIotId, new AnonymousClass96());
        }
        HashMap map2 = new HashMap();
        map2.put(Constants.LowPowerWakeUp, 1);
        IPCManager.getInstance().getDevice(this.nvrIotId).setProperties(map2, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.97
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
            }
        });
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$96, reason: invalid class name */
    class AnonymousClass96 implements MyCallback {
        AnonymousClass96() {
        }

        @Override // tools.MyCallback
        public void onComplete(boolean z) {
            if (SharePreferenceManager.getInstance().getLowPowerStatus(IPCDoubleEyeActivity.this.ballIotId) == 1) {
                Handler handler = IPCDoubleEyeActivity.this.uiHandler;
                final IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                handler.post(new Runnable() { // from class: activity.-$$Lambda$IPCDoubleEyeActivity$96$v4I3VSr3dFD4tPLxIYzhrdVqYSk
                    @Override // java.lang.Runnable
                    public final void run() {
                        iPCDoubleEyeActivity.playLive();
                    }
                });
            }
        }
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$98, reason: invalid class name */
    class AnonymousClass98 extends Handler {
        AnonymousClass98() {
        }

        @Override // android.os.Handler
        public void handleMessage(@NonNull Message message) {
            if (message.what == 1) {
                if (IPCDoubleEyeActivity.this.playBall.getPlayState() != 3) {
                    IPCDoubleEyeActivity.access$8408(IPCDoubleEyeActivity.this);
                    if (SharePreferenceManager.getInstance().getNetState(IPCDoubleEyeActivity.this.device.getIotId()) != 3) {
                        Handler handler = IPCDoubleEyeActivity.this.uiHandler;
                        final IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                        handler.post(new Runnable() { // from class: activity.-$$Lambda$IPCDoubleEyeActivity$98$xZGCOawSkOIj1B40tLtH-HVUs18
                            @Override // java.lang.Runnable
                            public final void run() {
                                iPCDoubleEyeActivity.playLive();
                            }
                        });
                    }
                    if (IPCDoubleEyeActivity.this.countWakeUp < 5) {
                        IPCDoubleEyeActivity.this.wakeUpHandler.sendEmptyMessageDelayed(1, 8000L);
                        return;
                    }
                    return;
                }
                return;
            }
            if (message.what == 2) {
                IPCDoubleEyeActivity.this.wakeUpDevice();
                IPCDoubleEyeActivity.this.wakeUpDeviceHandel();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wakeUpDeviceHandel() {
        Message messageObtain = Message.obtain();
        messageObtain.what = 2;
        this.wakeUpHandler.sendMessageDelayed(messageObtain, AppConfig.LOW_POWER);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeDefinition(final int i) {
        if (i < 0 || i > 3) {
            return;
        }
        HashMap map = new HashMap();
        map.put(Constants.STREAM_VIDEO_QUALITY_MODEL_NAME, Integer.valueOf(i));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.99
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() == 200) {
                            SharePreferenceManager.getInstance().setStreamVideoQuality(IPCDoubleEyeActivity.this.iotId, i);
                            IPCDoubleEyeActivity.this.binding.qualityBtn.setText((CharSequence) IPCDoubleEyeActivity.this.definitionList.get(SharePreferenceManager.getInstance().getStreamVideoQuality(IPCDoubleEyeActivity.this.iotId)));
                        } else {
                            IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.99.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
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

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$100, reason: invalid class name */
    class AnonymousClass100 implements SharePreferenceManager.OnCallSetListener {
        AnonymousClass100() {
        }

        /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$100$1, reason: invalid class name */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ String val$iotId;
            final /* synthetic */ String val$key;

            AnonymousClass1(String str, String str2) {
                this.val$key = str;
                this.val$iotId = str2;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.stream_video_quality_key))) {
                    IPCDoubleEyeActivity.this.binding.qualityBtn.setText((CharSequence) IPCDoubleEyeActivity.this.definitionList.get(SharePreferenceManager.getInstance().getStreamVideoQuality(this.val$iotId)));
                } else if (this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.image_flip_status_key))) {
                    IPCDoubleEyeActivity.this.rotationOrientate = SharePreferenceManager.getInstance().getImageFlip(this.val$iotId);
                } else if (this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.day_night_mode_key))) {
                    IPCDoubleEyeActivity.this.currentInfrarred2 = SharePreferenceManager.getInstance().getDayNightMode(IPCDoubleEyeActivity.this.iotId2);
                    IPCDoubleEyeActivity.this.currentInfrarred = SharePreferenceManager.getInstance().getDayNightMode(this.val$iotId);
                    int i = 0;
                    int i2 = 0;
                    for (int i3 = 0; i3 < IPCDoubleEyeActivity.this.nightModelList.size(); i3++) {
                        if (((String) IPCDoubleEyeActivity.this.nightModelList.get(i3)).equals(IPCDoubleEyeActivity.this.infrarredMode[IPCDoubleEyeActivity.this.currentInfrarred])) {
                            i2 = i3;
                        }
                        if (((String) IPCDoubleEyeActivity.this.nightModelList.get(i3)).equals(IPCDoubleEyeActivity.this.infrarredMode[IPCDoubleEyeActivity.this.currentInfrarred2])) {
                            i = i3;
                        }
                    }
                    IPCDoubleEyeActivity.this.changeLightDlgView(i);
                    IPCDoubleEyeActivity.this.changeLightDlgView(i2);
                    IPCDoubleEyeActivity.this.lightVisible = true;
                } else if (this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.double_net))) {
                    IPCDoubleEyeActivity.this.switchText = IPCDoubleEyeActivity.this.switch_4gArr[SharePreferenceManager.getInstance().getNet4GEnableSwitch(this.val$iotId)];
                    IPCDoubleEyeActivity.this.wifiFourPosition = SharePreferenceManager.getInstance().getNet4GEnableSwitch(this.val$iotId);
                    IPCDoubleEyeActivity.this.WifiConfigIsExist = SharePreferenceManager.getInstance().getWifiConfigIsExist(this.val$iotId);
                    IPCDoubleEyeActivity.this.netVisible = SharePreferenceManager.getInstance().getDoubleNetWork(this.val$iotId) == 1;
                } else if (!this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.support_zoom_key)) && !this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.support_focus_key)) && !this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.support_preset_key))) {
                    if (this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.support_4g_key))) {
                        if (IPCDoubleEyeActivity.this.device == null || IPCDoubleEyeActivity.this.device.getOwned() != 1) {
                            IPCDoubleEyeActivity.this.binding.llService4g.setVisibility(0);
                            if (IPCDoubleEyeActivity.this.badge != null) {
                                IPCDoubleEyeActivity.this.badge.hideView();
                                IPCDoubleEyeActivity.this.badge = null;
                            }
                            IPCDoubleEyeActivity.this.binding.llFlips.setVisibility(8);
                        } else {
                            IPCDoubleEyeActivity.this.isSupport4G = SharePreferenceManager.getInstance().getSupport4G(IPCDoubleEyeActivity.this.device.getIotId());
                            if (IPCDoubleEyeActivity.this.isSupport4G == 1) {
                                IPCDoubleEyeActivity.this.binding.llService4g.setVisibility(0);
                                if (IPCDoubleEyeActivity.this.badge != null) {
                                    IPCDoubleEyeActivity.this.badge.hideView();
                                    IPCDoubleEyeActivity.this.badge = null;
                                }
                                IPCDoubleEyeActivity.this.binding.llFlips.setVisibility(8);
                            } else {
                                IPCDoubleEyeActivity.this.binding.llService4g.setVisibility(8);
                            }
                        }
                    } else if (this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.iccid_key))) {
                        if (IPCDoubleEyeActivity.this.device != null && !"".equals(IPCDoubleEyeActivity.this.device.getIotId())) {
                            IPCDoubleEyeActivity.this.IccId = SharePreferenceManager.getInstance().getIccId(IPCDoubleEyeActivity.this.device.getIotId());
                        }
                    } else if (this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.PageControlEx))) {
                        IPCDoubleEyeActivity.this.shopVisible = ((SharePreferenceManager.getInstance().getPageControlEx(this.val$iotId) & 256) >> 8) == 1;
                    } else if (this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.new_support_preset_key))) {
                        IPCDoubleEyeActivity.this.isOldPresetDevice = SharePreferenceManager.getInstance().getNewSupportPreset(this.val$iotId) != 1;
                        IPCManager.getInstance().getDevice(this.val$iotId).QueryPresetMap(new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.100.1.1
                            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                            public void onComplete(boolean z, Object obj) {
                                if (z) {
                                    try {
                                        JSONArray jSONArray = JSONObject.parseObject(String.valueOf(obj)).getJSONObject("data").getJSONArray("PresetList");
                                        if (IPCDoubleEyeActivity.this.presetList != null) {
                                            IPCDoubleEyeActivity.this.presetList = null;
                                            IPCDoubleEyeActivity.this.presetList = new ArrayList();
                                        } else {
                                            IPCDoubleEyeActivity.this.presetList = new ArrayList();
                                        }
                                        for (int i4 = 0; i4 < jSONArray.size(); i4++) {
                                            JSONObject jSONObject = jSONArray.getJSONObject(i4);
                                            if (jSONObject.containsKey("Number")) {
                                                IPCDoubleEyeActivity.this.presetList.add(Integer.valueOf(jSONObject.getIntValue("Number")));
                                            }
                                        }
                                        IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.100.1.1.1
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                IPCDoubleEyeActivity.this.presetFragment.update(IPCDoubleEyeActivity.this.presetList);
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } else if (this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.DisplayController))) {
                        IPCDoubleEyeActivity.this.smartDoorVisible = SharePreferenceManager.getInstance().getDisplayController(this.val$iotId) == 1;
                        IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.100.1.2
                            @Override // java.lang.Runnable
                            public void run() {
                            }
                        });
                    } else if (this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.PTZLinkageSwitch))) {
                        Log.e("改变", "" + SharePreferenceManager.getInstance().getPTZLinkageTrackSwitch(IPCDoubleEyeActivity.this.gunIotId));
                        IPCDoubleEyeActivity.this.binding.lineViewItem.setVisibility(SharePreferenceManager.getInstance().getPTZLinkageTrackSwitch(IPCDoubleEyeActivity.this.gunIotId) == 0 ? 8 : 0);
                    } else if (this.val$key.equals(IPCDoubleEyeActivity.this.getString(R.string.PTZLinkageTrackSwitch)) && IPCDoubleEyeActivity.this.showMode != 0) {
                        IPCDoubleEyeActivity.this.binding.lineViewItem.setVisibility(SharePreferenceManager.getInstance().getPTZLinkageTrackSwitch(IPCDoubleEyeActivity.this.gunIotId) == 0 ? 8 : 0);
                    }
                }
                if (IPCDoubleEyeActivity.this.device != null && IPCDoubleEyeActivity.this.device.getOwned() == 1) {
                    IPCDoubleEyeActivity.this.binding.llShare.setVisibility(0);
                    IPCDoubleEyeActivity.this.isSupport4G = SharePreferenceManager.getInstance().getSupport4G(IPCDoubleEyeActivity.this.device.getIotId());
                    if (IPCDoubleEyeActivity.this.isSupport4G == 1) {
                        if (IPCDoubleEyeActivity.this.badge != null) {
                            IPCDoubleEyeActivity.this.badge.hideView();
                            IPCDoubleEyeActivity.this.badge = null;
                        }
                        IPCDoubleEyeActivity.this.binding.llService4g.setVisibility(0);
                        IPCDoubleEyeActivity.this.binding.llFlips.setVisibility(8);
                    } else {
                        if (IPCDoubleEyeActivity.this.badge == null) {
                            boolean z = AppConfig.isChina;
                        }
                        IPCDoubleEyeActivity.this.binding.llService4g.setVisibility(8);
                        IPCDoubleEyeActivity.this.binding.llFlips.setVisibility(0);
                    }
                } else {
                    if (IPCDoubleEyeActivity.this.badge != null) {
                        IPCDoubleEyeActivity.this.badge.hideView();
                        IPCDoubleEyeActivity.this.badge = null;
                    }
                    IPCDoubleEyeActivity.this.binding.llShare.setVisibility(8);
                    IPCDoubleEyeActivity.this.binding.llFlips.setVisibility(8);
                }
                IPCDoubleEyeActivity.this.supportMotionDetect = SharePreferenceManager.getInstance().getSupportMotionDetect(this.val$iotId);
                if (IPCDoubleEyeActivity.this.faceDetectionAbility == 1) {
                    IPCDoubleEyeActivity.this.isDetecting = SharePreferenceManager.getInstance().getHumanoidTrackingEnable(this.val$iotId).intValue() == 1;
                } else {
                    IPCDoubleEyeActivity.this.isDetecting = SharePreferenceManager.getInstance().getIntelligentMode(this.val$iotId) == 1;
                }
                if (IPCDoubleEyeActivity.this.isSwitching) {
                    return;
                }
                IPCDoubleEyeActivity.this.moreFragment.updateData(IPCDoubleEyeActivity.this.isDetecting, IPCDoubleEyeActivity.this.lightVisible, SharePreferenceManager.getInstance().getSupport4G(IPCDoubleEyeActivity.this.device.getIotId()) == 1, IPCDoubleEyeActivity.this.currentInfrarred2, IPCDoubleEyeActivity.this.switchText, IPCDoubleEyeActivity.this.shopVisible, IPCDoubleEyeActivity.this.smartDoorVisible, IPCDoubleEyeActivity.this.isFourState, IPCDoubleEyeActivity.this.supportMotionDetect, IPCDoubleEyeActivity.this.showMode);
            }
        }

        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(String str, String str2) {
            IPCDoubleEyeActivity.this.uiHandler.post(new AnonymousClass1(str2, str));
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
        Bitmap bitmapSnapShot;
        LivePlayer livePlayer2;
        Bitmap bitmapSnapShot2;
        verifyStoragePermissions(this);
        if (this.playBall.getPlayState() != 3) {
            Toast.makeText(getActivity(), R.string.only_play_snap, 0).show();
            return;
        }
        int i = this.showMode;
        if ((i == 0 || i == 2) && (livePlayer = this.playBall) != null && (bitmapSnapShot = livePlayer.snapShot()) != null) {
            Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmapSnapShot, 2560, 1440, true);
            if (bitmapCreateScaledBitmap == null) {
                showToast(getResources().getString(R.string.no_snap));
                return;
            }
            if (bitmapCreateScaledBitmap != null) {
                scanFile(bitmapCreateScaledBitmap);
                if (Build.VERSION.SDK_INT >= 29) {
                    MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmapCreateScaledBitmap, "IMG" + Calendar.getInstance().getTime(), (String) null);
                } else {
                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmapCreateScaledBitmap, "", "");
                }
            }
        }
        int i2 = this.showMode;
        if ((i2 == 0 || i2 == 1) && (livePlayer2 = this.playGun) != null && (bitmapSnapShot2 = livePlayer2.snapShot()) != null) {
            Bitmap bitmapCreateScaledBitmap2 = Bitmap.createScaledBitmap(bitmapSnapShot2, 2560, 1440, true);
            if (bitmapCreateScaledBitmap2 == null) {
                showToast(getResources().getString(R.string.no_snap));
                return;
            }
            if (bitmapCreateScaledBitmap2 != null) {
                scanFile(bitmapCreateScaledBitmap2);
                if (Build.VERSION.SDK_INT >= 29) {
                    MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmapCreateScaledBitmap2, "IMG" + Calendar.getInstance().getTime(), (String) null);
                } else {
                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmapCreateScaledBitmap2, "", "");
                }
            }
        }
        showToast(getResources().getString(R.string.camera_check));
    }

    private Bitmap addBitmap(Bitmap bitmap, Bitmap bitmap2) {
        int iMax = Math.max(bitmap.getWidth(), bitmap2.getWidth());
        int height = bitmap.getHeight() + bitmap2.getHeight();
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(iMax, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(bitmap2, 0.0f, bitmap.getHeight(), (Paint) null);
        Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.app_launcher), 300, 300, true);
        canvas.drawBitmap(bitmap2, 0.0f, bitmap.getHeight(), (Paint) null);
        Paint paint = new Paint();
        int width = (iMax - bitmapCreateScaledBitmap.getWidth()) - 100;
        int height2 = (height - bitmapCreateScaledBitmap.getHeight()) - 100;
        paint.setAlpha(128);
        canvas.drawBitmap(bitmapCreateScaledBitmap, width, height2, paint);
        return bitmapCreateBitmap;
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
        String path;
        if ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            path = ((File) Objects.requireNonNull(context.getExternalFilesDir(""))).getPath();
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
            File file = new File(getFilesPath(this) + "/video/");
            if (!file.exists() && !file.mkdirs()) {
                return;
            }
            int i4 = this.showMode;
            if (i4 == 0 || i4 == 1) {
                try {
                    this.playGun.startRecordingContent(new File(file, (System.currentTimeMillis() + 100) + ".mp4"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int i5 = this.showMode;
            if (i5 == 0 || i5 == 2) {
                try {
                    this.playBall.startRecordingContent(new File(file, System.currentTimeMillis() + ".mp4"));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            showTimer();
        } else {
            this.isRecording = false;
            int i6 = this.showMode;
            if (i6 == 0 || i6 == 1) {
                this.playGun.stopRecordingContent();
            }
            int i7 = this.showMode;
            if (i7 == 0 || i7 == 2) {
                this.playBall.stopRecordingContent();
            }
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
        runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.101
            @Override // java.lang.Runnable
            public void run() {
                if (IPCDoubleEyeActivity.this.isFinishing()) {
                    return;
                }
                IPCDoubleEyeActivity.this.isSpeakerOpen = i;
                if (IPCDoubleEyeActivity.this.getResources().getConfiguration().orientation == 2) {
                    if (IPCDoubleEyeActivity.this.isSpeakerOpen == 1) {
                        IPCDoubleEyeActivity.this.binding.speakerBtn.setImageResource(R.drawable.voice);
                        return;
                    } else if (IPCDoubleEyeActivity.this.isSpeakerOpen == 0) {
                        IPCDoubleEyeActivity.this.binding.speakerBtn.setImageResource(R.drawable.voice);
                        return;
                    } else {
                        IPCDoubleEyeActivity.this.binding.speakerBtn.setImageResource(R.drawable.ic_speaking2);
                        return;
                    }
                }
                if (IPCDoubleEyeActivity.this.isSpeakerOpen == 1) {
                    IPCDoubleEyeActivity.this.binding.speakerBtn.setImageResource(R.drawable.camera_mic_push);
                } else if (IPCDoubleEyeActivity.this.isSpeakerOpen == 0) {
                    IPCDoubleEyeActivity.this.binding.speakerBtn.setImageResource(R.drawable.camera_mic_nor);
                } else {
                    IPCDoubleEyeActivity.this.binding.speakerBtn.setImageResource(R.drawable.ic_speaking2);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLiveIntercomError() {
        runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.102
            @Override // java.lang.Runnable
            public void run() {
                if (IPCDoubleEyeActivity.this.isFinishing()) {
                    return;
                }
                IPCDoubleEyeActivity.this.whiteProgressDialog.dismiss();
            }
        });
        this.liveIntercom.stop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRecordError() {
        handleLiveIntercomError();
        runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.103
            @Override // java.lang.Runnable
            public void run() {
                IPCDoubleEyeActivity.this.isLiveIntercoming = false;
                if (IPCDoubleEyeActivity.this.isFinishing()) {
                    return;
                }
                IPCDoubleEyeActivity.this.setSpeakerBtn(0);
                IPCDoubleEyeActivity.this.binding.speakerBtn.setEnabled(true);
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
            this.liveIntercom.start();
            Log.e("speaker----", "play");
            this.binding.speakerBtn.setEnabled(false);
            this.whiteProgressDialog.setText(getResources().getString(R.string.open_speech));
            this.whiteProgressDialog.show();
            return;
        }
        this.binding.speakerBtn.setEnabled(false);
        this.whiteProgressDialog.setText(getResources().getString(R.string.close_speech));
        this.whiteProgressDialog.show();
        runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.104
            @Override // java.lang.Runnable
            public void run() {
                IPCDoubleEyeActivity.this.binding.speakerBtn.clearAnimation();
            }
        });
        this.liveIntercom.stop();
        Log.e("speaker----", "stop");
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$105, reason: invalid class name */
    class AnonymousClass105 implements ViewTreeObserver.OnGlobalLayoutListener {
        AnonymousClass105() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (IPCDoubleEyeActivity.this.touchView.getParent() != null) {
                ((ViewGroup) IPCDoubleEyeActivity.this.touchView.getParent()).removeView(IPCDoubleEyeActivity.this.touchView);
            }
            if (!IPCDoubleEyeActivity.this.isLand) {
                if (IPCDoubleEyeActivity.this.binding.rlTouchView.getHeight() == 0) {
                    return;
                }
                IPCDoubleEyeActivity.this.touchView.getModel().setBgResId(R.drawable.ui_pic_joystick_right_pad2);
                int dimensionPixelSize = IPCDoubleEyeActivity.this.getResources().getDimensionPixelSize(R.dimen.dimen_10);
                Log.e("屏幕", "" + IPCDoubleEyeActivity.this.isRatio);
                if (IPCDoubleEyeActivity.this.isRatio) {
                    IPCDoubleEyeActivity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCDoubleEyeActivity.this.getActivity(), 120.0f) + (dimensionPixelSize * 2), dimensionPixelSize);
                } else {
                    IPCDoubleEyeActivity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCDoubleEyeActivity.this.getActivity(), 150.0f) + (dimensionPixelSize * 2), dimensionPixelSize);
                }
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.addRule(13, -1);
                IPCDoubleEyeActivity.this.touchView.setLayoutParams(layoutParams);
                IPCDoubleEyeActivity.this.binding.rlTouchView.addView(IPCDoubleEyeActivity.this.touchView);
            } else {
                ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(-2, -2);
                IPCDoubleEyeActivity.this.touchView.getModel().setBgResId(R.drawable.ui_pic_joystick_right_pad3);
                int dimensionPixelSize2 = IPCDoubleEyeActivity.this.getResources().getDimensionPixelSize(R.dimen.dimen_10);
                IPCDoubleEyeActivity.this.touchView.setDefaultSize(ScreenUtil.dp2Px(IPCDoubleEyeActivity.this.getActivity(), 120.0f) + (dimensionPixelSize2 * 2), dimensionPixelSize2);
                layoutParams2.bottomToBottom = IPCDoubleEyeActivity.this.binding.fullScreen.getId();
                IPCDoubleEyeActivity.this.binding.fullScreen.addView(IPCDoubleEyeActivity.this.touchView, layoutParams2);
            }
            IPCDoubleEyeActivity.this.touchView.setListener(new JoystickTouchViewListener() { // from class: activity.IPCDoubleEyeActivity.105.1
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
                        Method dump skipped, instruction units count: 261
                        To view this dump add '--comments-level debug' option
                    */
                    throw new UnsupportedOperationException("Method not decompiled: activity.IPCDoubleEyeActivity.AnonymousClass105.AnonymousClass1.onTouch(float, float):void");
                }

                @Override // view.JoystickTouchViewListener
                public void onReset() {
                    if (IPCDoubleEyeActivity.this.ptzTimer != null) {
                        IPCDoubleEyeActivity.this.ptzTimer.cancel();
                        IPCDoubleEyeActivity.this.ptzTimer = null;
                    }
                    IPCDoubleEyeActivity.this.lastActionTypeEnum = null;
                }

                @Override // view.JoystickTouchViewListener
                public void onActionUp() {
                    Log.e("云台", "抬起");
                    IPCDoubleEyeActivity.this.touchView.resetView();
                    if (IPCDoubleEyeActivity.this.ptzTimer != null) {
                        IPCDoubleEyeActivity.this.ptzTimer.cancel();
                        IPCDoubleEyeActivity.this.ptzTimer = null;
                    }
                    IPCDoubleEyeActivity.this.lastActionTypeEnum = null;
                }
            });
            IPCDoubleEyeActivity.this.binding.rlTouchView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addControlTouchView(boolean z) {
        if (this.touchView == null) {
            this.touchView = new TouchView(getActivity());
        }
        this.binding.rlTouchView.getViewTreeObserver().addOnGlobalLayoutListener(this.nGlobalLayoutListener);
    }

    public void startPTZEx(ActionTypeEnum actionTypeEnum, SpeedEnum speedEnum) {
        IPCManager.getInstance().getDevice(this.iotId).startPTZEx(actionTypeEnum.getCode(), speedEnum.getCode(), new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.106
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                String str = IPCDoubleEyeActivity.this.TAG;
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
        this.nowScale = this.binding.player.getScale();
        if (configuration.orientation == 2) {
            setFullScreen();
            this.isHorizontal = true;
            this.binding.player.firstAddZoom = true;
            this.binding.player2.firstAddZoom = true;
        } else {
            this.isHorizontal = false;
            backFullScreen();
        }
        super.onConfigurationChanged(configuration);
        setSwipeBackEnable(!this.isLand);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFullScreen() {
        Resources resources;
        int i;
        this.binding.portraitPlayer.removeAllViews();
        if (this.binding.player.getParent() != null) {
            ((ViewGroup) this.binding.player.getParent()).removeView(this.binding.player);
        }
        this.binding.landscapePlayer.addView(this.binding.player);
        this.binding.player.requestLayout();
        if (this.binding.player2.getParent() != null) {
            ((ViewGroup) this.binding.player2.getParent()).removeView(this.binding.player2);
        }
        this.binding.landscapePlayer.addView(this.binding.player2);
        this.binding.player2.requestLayout();
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
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.binding.timer.getLayoutParams();
        layoutParams.setMargins(0, DensityUtil.dip2px(this, 70.0f), 0, 0);
        this.binding.timer.setLayoutParams(layoutParams);
        if (this.binding.timer.getParent() != null) {
            ((ViewGroup) this.binding.timer.getParent()).removeView(this.binding.timer);
        }
        this.binding.landscapePlayer.addView(this.binding.timer);
        this.binding.timer.requestLayout();
        this.binding.tvTitle.setRightLlGone(true);
        this.binding.tvTitle.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        this.binding.tvTitle.setTBackgroundDrawable(getResources().getDrawable(R.drawable.bg_gray_transparent));
        this.binding.controllerPanel.setVisibility(8);
        Log.e("云存=", "" + SharePreferenceManager.getInstance().getEventRecord(this.iotId) + "  4G=" + SharePreferenceManager.getInstance().getSupport4G(this.device.getIotId()));
        this.binding.ivCharge4gFlow.setVisibility(8);
        float f = ((float) ScreenUtil.getDisplayMetrics(getActivity())[0]) / 2.0f;
        float f2 = (9.0f * f) / 16.0f;
        switch (this.showMode) {
            case 0:
                int i2 = (int) f;
                int i3 = (int) f2;
                ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(i2, i3);
                layoutParams2.topToTop = this.binding.landscapePlayer.getTop();
                layoutParams2.leftToLeft = this.binding.landscapePlayer.getId();
                layoutParams2.bottomToBottom = this.binding.landscapePlayer.getId();
                this.binding.player2.setLayoutParams(layoutParams2);
                setViewLayoutParams(this.binding.player2, DensityUtils.getAppSize(this, true).x / 2, ((DensityUtils.getAppSize(this, true).x / 2) / 16) * 9);
                ConstraintLayout.LayoutParams layoutParams3 = new ConstraintLayout.LayoutParams(i2, i3);
                layoutParams3.topToTop = this.binding.landscapePlayer.getTop();
                layoutParams3.rightToRight = this.binding.landscapePlayer.getId();
                layoutParams3.bottomToBottom = this.binding.landscapePlayer.getId();
                this.binding.player.setLayoutParams(layoutParams3);
                setViewLayoutParams(this.binding.player, DensityUtils.getAppSize(this, true).x / 2, ((DensityUtils.getAppSize(this, true).x / 2) / 16) * 9);
                this.binding.player.setVisibility(0);
                this.binding.player2.setVisibility(0);
                this.binding.fullNightVision.setVisibility(8);
                break;
            case 1:
                this.selectIotId = this.iotId2;
                this.binding.player.setVisibility(8);
                this.binding.player2.setVisibility(0);
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.device.getIotId()) == 0) {
                    this.binding.fullNightVision.setVisibility(0);
                }
                if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.iotId) == 0) {
                    this.binding.fullNightVision.setVisibility(8);
                }
                if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(this.device.getIotId()) == 1) {
                    this.binding.ivLightWhile2.setVisibility(8);
                    this.binding.fullNightVision.setVisibility(8);
                }
                setViewLayoutParams(this.binding.player2, DensityUtils.getAppSize(this, true).x, DensityUtils.getAppSize(this, true).y);
                break;
            case 2:
                this.selectIotId = this.iotId;
                this.binding.player.setVisibility(0);
                this.binding.player2.setVisibility(8);
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.device.getIotId()) == 0) {
                    this.binding.fullNightVision.setVisibility(0);
                }
                if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.iotId) == 0) {
                    this.binding.fullNightVision.setVisibility(8);
                }
                setViewLayoutParams(this.binding.player, DensityUtils.getAppSize(this, true).x, DensityUtils.getAppSize(this, true).y);
                break;
        }
        this.binding.drawLineView.setLayoutParams(new ConstraintLayout.LayoutParams(-1, -1));
        ConstraintLayout.LayoutParams layoutParams4 = new ConstraintLayout.LayoutParams(-1, -1);
        layoutParams4.topToTop = this.binding.landscapePlayer.getTop();
        layoutParams4.leftToLeft = this.binding.landscapePlayer.getId();
        layoutParams4.rightToRight = this.binding.landscapePlayer.getId();
        layoutParams4.bottomToBottom = this.binding.landscapePlayer.getId();
        this.binding.fourPic.setLayoutParams(layoutParams4);
        this.binding.qualityControl.setVisibility(8);
        if (!this.binding.tvTitle.isSelected()) {
            this.binding.tvTitle.setVisibility(8);
            this.binding.tvTitle.setSelected(true);
        }
        ShadowButton shadowButton = this.binding.fullSound;
        if (this.speakerSwitch) {
            resources = getResources();
            i = R.drawable.full_sound;
        } else {
            resources = getResources();
            i = R.drawable.full_sound_;
        }
        shadowButton.setBackground(resources.getDrawable(i));
        addControlTouchView(true);
        this.binding.layoutOsd.bringToFront();
        if (this.binding.llVideoQt.getParent() != null) {
            ((ViewGroup) this.binding.llVideoQt.getParent()).removeView(this.binding.llVideoQt);
        }
        this.binding.clarity.addView(this.binding.llVideoQt);
        this.binding.llVideoQt.bringToFront();
        this.binding.llVideoQt.requestLayout();
        ConstraintLayout.LayoutParams layoutParams5 = (ConstraintLayout.LayoutParams) this.binding.qualityDlg.getLayoutParams();
        layoutParams5.topToTop = this.binding.landscapePlayer.getId();
        layoutParams5.bottomToBottom = this.binding.landscapePlayer.getId();
        this.binding.qualityDlg.setLayoutParams(layoutParams5);
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
        if (this.binding.drawLineView.getParent() != null) {
            ((ViewGroup) this.binding.drawLineView.getParent()).removeView(this.binding.drawLineView);
        }
        this.binding.portraitPlayer.addView(this.binding.drawLineView);
        this.binding.drawLineView.requestLayout();
        if (this.binding.player.getParent() != null) {
            ((ViewGroup) this.binding.player.getParent()).removeView(this.binding.player);
        }
        this.binding.portraitPlayer.addView(this.binding.player);
        this.binding.player.requestLayout();
        if (this.binding.player2.getParent() != null) {
            ((ViewGroup) this.binding.player2.getParent()).removeView(this.binding.player2);
        }
        this.binding.portraitPlayer.addView(this.binding.player2);
        this.binding.player2.requestLayout();
        if (this.binding.lineViewItem.getParent() != null) {
            ((ViewGroup) this.binding.lineViewItem.getParent()).removeView(this.binding.lineViewItem);
        }
        this.binding.portraitPlayer.addView(this.binding.lineViewItem);
        this.binding.lineViewItem.requestLayout();
        if (this.binding.SensorView.getParent() != null) {
            ((ViewGroup) this.binding.SensorView.getParent()).removeView(this.binding.SensorView);
        }
        this.binding.portraitPlayer.addView(this.binding.SensorView);
        this.binding.SensorView.requestLayout();
        if (this.binding.qualityControl.getParent() != null) {
            ((ViewGroup) this.binding.qualityControl.getParent()).removeView(this.binding.qualityControl);
        }
        this.binding.portraitPlayer.addView(this.binding.qualityControl);
        this.binding.qualityControl.requestLayout();
        if (this.binding.ivLightWhile2.getParent() != null) {
            ((ViewGroup) this.binding.ivLightWhile2.getParent()).removeView(this.binding.ivLightWhile2);
        }
        this.binding.portraitPlayer.addView(this.binding.ivLightWhile2);
        this.binding.ivLightWhile2.requestLayout();
        this.binding.tvTitle.setRightLlGone(false);
        this.binding.tvTitle.setBackgroundColor(getResources().getColor(R.color.color_black));
        this.binding.tvTitle.setSelected(false);
        this.binding.controllerPanel.setVisibility(0);
        if (this.binding.rlTouchView.getVisibility() == 0) {
            this.binding.ivCharge4gFlow.setVisibility(0);
            Log.e("云存=", "" + SharePreferenceManager.getInstance().getEventRecord(this.iotId) + "  4G=" + SharePreferenceManager.getInstance().getSupport4G(this.device.getIotId()));
            if (SharePreferenceManager.getInstance().getEventRecord(this.iotId) != 1 || this.isMoreFragmentShow) {
                this.binding.ivCharge4gFlow.setVisibility(8);
            }
            if (!AppConfig.isChina && !this.binding.ivCharge4gFlow.isSelected()) {
                this.binding.ivCharge4gFlow.setVisibility(8);
            }
        }
        float f = (ScreenUtil.getDisplayMetrics(getActivity())[0] * 9.0f) / 16.0f;
        switch (this.showMode) {
            case 0:
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-1, ((int) this.viewHeight) / 2);
                int i = (int) f;
                layoutParams.height = i;
                layoutParams.topToTop = this.binding.portraitPlayer.getId();
                layoutParams.startToStart = this.binding.portraitPlayer.getId();
                layoutParams.endToEnd = this.binding.portraitPlayer.getId();
                layoutParams.bottomToTop = this.binding.player.getId();
                this.binding.player2.setLayoutParams(layoutParams);
                ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(-1, ((int) this.viewHeight) / 2);
                layoutParams2.height = i;
                layoutParams2.topToTop = this.binding.portraitPlayer.getId();
                layoutParams2.startToStart = this.binding.portraitPlayer.getId();
                layoutParams2.endToEnd = this.binding.portraitPlayer.getId();
                layoutParams2.setMargins(0, 25, 0, 0);
                this.binding.lineViewItem.setLayoutParams(layoutParams2);
                ConstraintLayout.LayoutParams layoutParams3 = new ConstraintLayout.LayoutParams(-1, ((int) this.viewHeight) / 2);
                layoutParams3.height = i;
                layoutParams3.startToStart = this.binding.portraitPlayer.getId();
                layoutParams3.endToEnd = this.binding.portraitPlayer.getId();
                layoutParams3.bottomToBottom = this.binding.portraitPlayer.getId();
                layoutParams3.topToBottom = this.binding.player2.getId();
                this.binding.player.setLayoutParams(layoutParams3);
                if (this.isRatio) {
                    setViewLayoutParams(this.binding.portraitPlayer, -1, (int) this.viewHeight);
                    MyGlTextureView myGlTextureView = this.binding.player2;
                    double d2 = this.viewHeight;
                    setViewLayoutParams(myGlTextureView, (((int) (d2 / 2.0d)) / 9) * 16, (int) (d2 / 2.0d));
                    MyGlTextureView myGlTextureView2 = this.binding.player;
                    double d3 = this.viewHeight;
                    setViewLayoutParams(myGlTextureView2, (((int) (d3 / 2.0d)) / 9) * 16, (int) (d3 / 2.0d));
                    CircleTooView circleTooView = this.binding.lineViewItem;
                    double d4 = this.viewHeight;
                    setViewLayoutParams(circleTooView, (((int) (d4 / 2.0d)) / 9) * 16, (int) (d4 / 2.0d));
                }
                this.binding.player.setVisibility(0);
                this.binding.player2.setVisibility(0);
                if (SharePreferenceManager.getInstance().getNightVisionHide(this.device.getIotId()) == 0) {
                    this.binding.ivLightWhile2.setVisibility(0);
                }
                if (SharePreferenceManager.getInstance().getTandemVuNightVisionHide(this.device.getIotId()) == 1) {
                    this.binding.ivLightWhile2.setVisibility(8);
                    this.binding.fullNightVision.setVisibility(8);
                }
                if (SharePreferenceManager.getInstance().getNightVisionModeShowCtrl(this.iotId) == 0) {
                    this.binding.ivLightWhile2.setVisibility(8);
                }
                this.binding.lineViewItem.setVisibility(SharePreferenceManager.getInstance().getPTZLinkageTrackSwitch(this.gunIotId) == 0 ? 8 : 0);
                break;
            case 1:
                int i2 = (int) f;
                ConstraintLayout.LayoutParams layoutParams4 = new ConstraintLayout.LayoutParams(-1, i2);
                layoutParams4.height = i2;
                layoutParams4.topToTop = this.binding.portraitPlayer.getId();
                layoutParams4.bottomToBottom = this.binding.portraitPlayer.getId();
                this.binding.player2.setLayoutParams(layoutParams4);
                this.binding.player2.setVisibility(0);
                this.binding.lineViewItem.setVisibility(8);
                this.binding.ivLightWhile2.setVisibility(8);
                break;
            case 2:
                int i3 = (int) f;
                ConstraintLayout.LayoutParams layoutParams5 = new ConstraintLayout.LayoutParams(-1, i3);
                layoutParams5.height = i3;
                layoutParams5.topToTop = this.binding.portraitPlayer.getId();
                layoutParams5.bottomToBottom = this.binding.portraitPlayer.getId();
                this.binding.player.setLayoutParams(layoutParams5);
                this.binding.player.setVisibility(0);
                this.binding.player2.setVisibility(8);
                this.binding.lineViewItem.setVisibility(8);
                break;
        }
        ConstraintLayout.LayoutParams layoutParams6 = (ConstraintLayout.LayoutParams) this.binding.fourPic.getLayoutParams();
        layoutParams6.height = (int) f;
        layoutParams6.topToTop = this.binding.portraitPlayer.getId();
        layoutParams6.bottomToBottom = this.binding.portraitPlayer.getId();
        this.binding.fourPic.setLayoutParams(layoutParams6);
        addControlTouchView(false);
        this.binding.fullScreen.setVisibility(8);
        this.binding.lightDlg.setVisibility(8);
        this.binding.layoutOsd.bringToFront();
        this.binding.clarity.removeView(this.binding.llVideoQt);
        if (this.binding.llVideoQt.getParent() != null) {
            ((ViewGroup) this.binding.llVideoQt.getParent()).removeView(this.binding.llVideoQt);
        }
        this.binding.qualityControl.addView(this.binding.llVideoQt);
        this.binding.llVideoQt.requestLayout();
        ConstraintLayout.LayoutParams layoutParams7 = (ConstraintLayout.LayoutParams) this.binding.qualityDlg.getLayoutParams();
        layoutParams7.topToTop = this.binding.portraitPlayer.getId();
        layoutParams7.bottomToBottom = this.binding.portraitPlayer.getId();
        this.binding.qualityDlg.setLayoutParams(layoutParams7);
        if (this.needRecharge) {
            dismissPlayButton();
            this.binding.immediateRenewal.setVisibility(0);
            this.binding.traffic4gExpired.setVisibility(0);
            this.binding.outlineTime.setVisibility(0);
        }
        this.binding.qualityControl.setVisibility(0);
        this.binding.tvTitle.setVisibility(0);
        ConstraintLayout.LayoutParams layoutParams8 = (ConstraintLayout.LayoutParams) this.binding.timer.getLayoutParams();
        layoutParams8.setMargins(0, 0, 0, 0);
        this.binding.timer.setLayoutParams(layoutParams8);
        if (this.binding.timer.getParent() != null) {
            ((ViewGroup) this.binding.timer.getParent()).removeView(this.binding.timer);
        }
        this.binding.portraitPlayer.addView(this.binding.timer);
        this.binding.timer.requestLayout();
        getWindow().clearFlags(1024);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFloatBarState() {
        this.binding.playerInfoTv.setVisibility((this.isFloat && this.playBall.getPlayState() == 3) ? 0 : 8);
        if (getResources().getConfiguration().orientation == 2) {
            this.binding.fullScreen.setVisibility(this.isFloat ? 0 : 8);
            this.binding.tvTitle.setVisibility(this.isFloat ? 0 : 8);
            if (this.timer != null) {
                this.binding.timer.setVisibility(this.isFloat ? 0 : 8);
            }
        }
    }

    @SuppressLint({"SetTextI18n"})
    public void updatePlayInfo() {
        if (this.updatePlayInfoHandle == null) {
            this.updatePlayInfoHandle = this.scheduledExecutorService.scheduleAtFixedRate(this.updatePlayInfoTimerTask, 1L, 1L, TimeUnit.SECONDS);
        }
        final String str = ((this.playBall.getCurrentPlayInfo().bitRate / 1024) / 8) + "KB/S";
        if (this.isFirstShowStreamType) {
            this.binding.playerInfoTv.setText(str);
            this.uiHandler.postDelayed(new Runnable() { // from class: activity.IPCDoubleEyeActivity.108
                @Override // java.lang.Runnable
                public void run() {
                    if (IPCDoubleEyeActivity.this.isFinishing()) {
                        return;
                    }
                    IPCDoubleEyeActivity.this.binding.playerInfoTv.setText(str);
                    IPCDoubleEyeActivity.this.isFirstShowStreamType = false;
                }
            }, 5000L);
        } else {
            this.binding.playerInfoTv.setText(str);
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
        this.binding.videoBufferingBar.bringToFront();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissBuffering() {
        this.binding.videoBufferingBar.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissSnapPicture() {
        this.binding.ivSnap.setVisibility(8);
    }

    private void dismissSnapPicture1() {
        this.binding.ivSnap.setVisibility(8);
    }

    private void AutoSnap() {
        Bitmap bitmapSnapShot;
        Bitmap bitmapSnapShot2;
        if (this.playBall.getPlayState() == 3 && (bitmapSnapShot2 = this.playBall.snapShot()) != null) {
            saveBitmap(bitmapSnapShot2, this.iotId);
        }
        if (this.playGun.getPlayState() != 3 || (bitmapSnapShot = this.playGun.snapShot()) == null) {
            return;
        }
        saveBitmap(bitmapSnapShot, this.iotId2);
    }

    public void saveBitmap(final Bitmap bitmap, final String str) {
        Log.d(this.TAG, "saveBitmap: -------------------------------");
        final Application application = getApplication();
        new Thread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.109
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                FileOutputStream fileOutputStream;
                File file;
                String string = SpUtil.getString(IPCDoubleEyeActivity.this.getActivity(), Utils.getDevSnapKey(str), "");
                long jCurrentTimeMillis = System.currentTimeMillis();
                LogEx.d(true, IPCDoubleEyeActivity.this.TAG, "保存图片");
                StringBuilder sb = new StringBuilder();
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                sb.append(iPCDoubleEyeActivity.getFilesPath(iPCDoubleEyeActivity.getApplication()));
                sb.append("/snap/");
                sb.append(str);
                sb.append("/");
                String string2 = sb.toString();
                Log.d(IPCDoubleEyeActivity.this.TAG, "run: puppet:dirPath====" + string2);
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
                        } catch (Exception e) {
                            e = e;
                        }
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                    }
                    try {
                        boolean zCompress = bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                        Log.d(IPCDoubleEyeActivity.this.TAG, "run: puppet:b=====" + zCompress);
                        fileOutputStream.flush();
                        LogEx.e(true, IPCDoubleEyeActivity.this.TAG, "图片保存地址: " + file.getAbsolutePath());
                        Log.d(IPCDoubleEyeActivity.this.TAG, "puppet:图片保存地址: file.getAbsolutePath()======" + file.getAbsolutePath());
                        Log.d(IPCDoubleEyeActivity.this.TAG, "puppet:图片保存地址: Utils.getDevSnapKey(tempIotId)======" + Utils.getDevSnapKey(str));
                        SpUtil.putValue(application, Utils.getDevSnapKey(str), file.getAbsolutePath());
                        EventBus.getDefault().post(new CameraSnapUpdate(str, jCurrentTimeMillis));
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
        ThreadPool.DefaultThreadPool.getInstance().submit(new Runnable() { // from class: activity.IPCDoubleEyeActivity.110
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                Exception e;
                Throwable th;
                FileOutputStream fileOutputStream;
                final File file;
                long jCurrentTimeMillis = System.currentTimeMillis();
                LogEx.d(true, IPCDoubleEyeActivity.this.TAG, "保存图片");
                StringBuilder sb = new StringBuilder();
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                sb.append(iPCDoubleEyeActivity.getFilesPath(iPCDoubleEyeActivity.getApplication()));
                sb.append("/snap/");
                sb.append(IPCDoubleEyeActivity.this.iotId);
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
                        LogEx.e(true, IPCDoubleEyeActivity.this.TAG, "图片保存地址: " + file.getAbsolutePath());
                        SpUtil.putValue(application, Utils.getDevSnapKey(IPCDoubleEyeActivity.this.iotId) + String.valueOf(i + 1), file.getAbsolutePath());
                        IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.110.1
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
        new Thread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.111
            @Override // java.lang.Runnable
            public void run() {
                String string = SpUtil.getString(IPCDoubleEyeActivity.this.getActivity(), Utils.getDevSnapKey(IPCDoubleEyeActivity.this.iotId) + String.valueOf(i + 1), "");
                SpUtil.putValue(application, Utils.getDevSnapKey(IPCDoubleEyeActivity.this.iotId) + String.valueOf(i + 1), "");
                FileUtil.deleteFile(string);
                IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.111.1
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
            this.binding.playerInfoTv.setVisibility(0);
        }
        updatePlayInfo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissPlayInfo() {
        this.binding.playerInfoTv.setVisibility(8);
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
            if (i == 1 && this.ZoomIsMax && this.binding.player.getScale() < this.binding.player.getMaxScale()) {
                this.binding.player.addZoom();
                return;
            } else if (i == 0 && this.binding.player.getScale() > 1.0f) {
                this.binding.player.reduceZoom();
                return;
            } else {
                changeOpticalZoom(i);
                return;
            }
        }
        if (!this.isOpticalZoom) {
            if (i == 1) {
                this.binding.player.addZoom();
                return;
            } else {
                this.binding.player.reduceZoom();
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
            IPCManager.getInstance().getDevice(this.iotId).changeEZoom(i, 0, SharePreferenceManager.getInstance().getStreamVideoQuality(this.iotId) == 2 ? 0 : 1, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.112
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (!z || obj == null || String.valueOf(obj).equals("")) {
                        return;
                    }
                    try {
                        final JSONObject object = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(obj)).getString("data"));
                        IPCDoubleEyeActivity.this.ZoomMax = object.getInteger("ZoomMax").intValue();
                        IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                        boolean z2 = true;
                        if (object.getInteger("ZoomIsMax").intValue() != 1) {
                            z2 = false;
                        }
                        iPCDoubleEyeActivity.ZoomIsMax = z2;
                        IPCDoubleEyeActivity.this.zoom.postValue(Float.valueOf(object.getInteger("Lens").intValue()));
                        IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.112.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCDoubleEyeActivity.this.binding.OSD.setText(object.getInteger("Lens") + "X");
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
            IPCManager.getInstance().getDevice(this.iotId).changeZoom(i, this.binding.player.getTimes(), new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.113
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (!z || obj == null || String.valueOf(obj).equals("")) {
                        return;
                    }
                    try {
                        JSONObject object = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(obj)).getString("data"));
                        IPCDoubleEyeActivity.this.ZoomMax = object.getInteger("ZoomMax").intValue();
                        IPCDoubleEyeActivity.this.ZoomIsMax = object.getBoolean("ZoomIsMax").booleanValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNightMode(final Object obj, final String str) {
        HashMap map = new HashMap();
        map.put(Constants.DAY_NIGHT_MODE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
        IPCManager.getInstance().getDevice(str).setProperties(map, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.114
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj2) {
                if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.114.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setDayNightMode(str, Integer.parseInt(obj.toString()));
                            IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.114.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
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
        this.timer.schedule(new TimerTask() { // from class: activity.IPCDoubleEyeActivity.115
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.115.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IPCDoubleEyeActivity.this.binding.timer.setText(IPCDoubleEyeActivity.this.transformTime(IPCDoubleEyeActivity.this.i));
                    }
                });
                IPCDoubleEyeActivity.this.i++;
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

    /* JADX INFO: Access modifiers changed from: private */
    public void isNet4GSwitch() {
        if (((SharePreferenceManager.getInstance().getPageControlEx(this.iotId) & 524288) >> 19) == 1) {
            if (SharePreferenceManager.getInstance().getIccId1(this.device.getIotId()).equals("") && SharePreferenceManager.getInstance().getIccId2(this.device.getIotId()).equals("")) {
                Intent intent = new Intent(getActivity(), (Class<?>) Traffic4GActivity.class);
                intent.putExtra("iotId", this.device.getIotId());
                intent.putExtra("iccid", this.IccId);
                intent.putExtra(AlinkConstants.KEY_DN, this.nvrDevice.getDeviceName());
                intent.putExtra(AlinkConstants.KEY_PK, this.nvrDevice.getProductKey());
                startActivity(intent);
                return;
            }
            Intent intent2 = new Intent(this, (Class<?>) Net4GSwitchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, this.device);
            bundle.putSerializable("device1", this.device1);
            bundle.putSerializable("nvrDevice", this.nvrDevice);
            intent2.putExtras(bundle);
            startActivity(intent2);
            return;
        }
        Intent intent3 = new Intent(getActivity(), (Class<?>) Traffic4GActivity.class);
        intent3.putExtra("iccid", this.IccId);
        intent3.putExtra("iotId", this.device.getIotId());
        intent3.putExtra(AlinkConstants.KEY_DN, this.nvrDevice.getDeviceName());
        intent3.putExtra(AlinkConstants.KEY_PK, this.nvrDevice.getProductKey());
        startActivity(intent3);
    }

    public void getControllerList() {
        if (this.iotId == null) {
            return;
        }
        IPCManager.getInstance().getDevice(this.iotId).getControllerList(new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.117
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (z) {
                    try {
                        JSONArray jSONArray = JSONObject.parseObject(String.valueOf(obj)).getJSONObject("data").getJSONArray("KeyList");
                        for (int i = 0; i < jSONArray.size(); i++) {
                            IPCDoubleEyeActivity.this.ControllerListBean(i, ((Integer) jSONArray.get(i)).intValue());
                        }
                        IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.117.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCDoubleEyeActivity.this.controllerFragment.refreshButton();
                                IPCDoubleEyeActivity.this.refreshButton();
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
        fragmentTransactionBeginTransaction.commit();
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
        getSupportFragmentManager().beginTransaction().hide(this.lensControllerFragment).show(this.moreFragment).commit();
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
        IPCManager.getInstance().getDevice(this.iotId).changeFocus(i, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.118
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCDoubleEyeActivity.this.TAG, "invoke focus");
            }
        });
    }

    private void changePresetLocation(int i) {
        IPCManager.getInstance().getDevice(this.iotId).changePresetLocation(i, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.119
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCDoubleEyeActivity.this.TAG, "change preset");
            }
        });
    }

    private void addPresetLocation(int i) {
        IPCManager.getInstance().getDevice(this.iotId).addPresetLocation(i, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.120
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (!z || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                Log.d(IPCDoubleEyeActivity.this.TAG, "add preset");
            }
        });
    }

    private void WatchPos() {
        IPCManager.getInstance().getDevice(this.iotId).setWatchPos(new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.121
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (z) {
                    Log.d(IPCDoubleEyeActivity.this.TAG, "onComplete: setWatchPos Finish");
                }
            }
        });
    }

    private void CloudTips() {
        if (this.binding.llFlips.getVisibility() == 8) {
            return;
        }
        this.badge = new BadgeView(getApplicationContext()).bindTarget(this.binding.llFlips).setBadgeTextSize(5.0f, true).setBadgePadding(2.0f, true).setGravityOffset(30.0f, 0.0f, true).setBadgeGravity(8388661);
        this.badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() { // from class: activity.IPCDoubleEyeActivity.122
            @Override // view.Badge.OnDragStateChangedListener
            public void onDragStateChanged(int i, Badge badge, View view2) {
            }
        });
        getCloudQuery();
        getFreeCloudQuery();
        new Thread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.123
            @Override // java.lang.Runnable
            public void run() {
                while (true) {
                    if (IPCDoubleEyeActivity.this.cloudStatusBean.getExpired() != -1 && IPCDoubleEyeActivity.this.cloudStatusBean.getFreeCloud() != -1 && IPCDoubleEyeActivity.this.cloudStatusBean.getFreeCloudExpired() != -1) {
                        break;
                    }
                }
                int expired = IPCDoubleEyeActivity.this.cloudStatusBean.getExpired();
                int freeCloud = IPCDoubleEyeActivity.this.cloudStatusBean.getFreeCloud();
                int freeCloudExpired = IPCDoubleEyeActivity.this.cloudStatusBean.getFreeCloudExpired();
                final String string = "";
                if (freeCloud == 0) {
                    string = IPCDoubleEyeActivity.this.getString(R.string.one_dollar_purchase);
                    IPCDoubleEyeActivity.this.isOneYuan = true;
                } else if (expired == 1 && freeCloudExpired == 1) {
                    string = IPCDoubleEyeActivity.this.getString(R.string.expired);
                } else if (expired == 2 && freeCloudExpired == 1) {
                    string = IPCDoubleEyeActivity.this.getString(R.string.expired);
                }
                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.123.1
                    @Override // java.lang.Runnable
                    public void run() {
                        String str = string;
                        if (str != null && !str.equals("") && IPCDoubleEyeActivity.this.isOwner) {
                            if (IPCDoubleEyeActivity.this.badge == null) {
                                return;
                            } else {
                                IPCDoubleEyeActivity.this.badge.setBadgeText(string);
                            }
                        }
                        if (SharePreferenceManager.getInstance().getFirstEnterActivity(IPCDoubleEyeActivity.this.iotId) && IPCDoubleEyeActivity.this.isOneYuan) {
                            SharePreferenceManager.getInstance().setFirstEnterActivity(IPCDoubleEyeActivity.this.iotId, false);
                        }
                    }
                });
            }
        }).start();
    }

    private void getCloudQuery() {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/vision/customer/cloudstorage/order/query").setScheme(Scheme.HTTPS).setApiVersion("1.0.4").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.IPCDoubleEyeActivity.124
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() == 200) {
                    yunCloudListBean yuncloudlistbean = (yunCloudListBean) new Gson().fromJson(ioTResponse.getData().toString(), yunCloudListBean.class);
                    if (yuncloudlistbean.getOrderList() != null) {
                        IPCDoubleEyeActivity.this.cloudStatusBean.setExpired(1);
                        for (int i = 0; i < yuncloudlistbean.getOrderList().size(); i++) {
                            if (yuncloudlistbean.getOrderList().get(i).getExpired() == 0) {
                                IPCDoubleEyeActivity.this.cloudStatusBean.setExpired(0);
                                return;
                            }
                        }
                        return;
                    }
                    IPCDoubleEyeActivity.this.cloudStatusBean.setExpired(2);
                }
            }
        });
    }

    private void getFreeCloudQuery() {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/vision/customer/freecloudstorage/get").setScheme(Scheme.HTTPS).setApiVersion("1.0.1").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.IPCDoubleEyeActivity.125
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                FreeCloudStorage freeCloudStorage = (FreeCloudStorage) new Gson().fromJson(ioTResponse.getData().toString(), FreeCloudStorage.class);
                if (freeCloudStorage.getConsumed() == 0) {
                    IPCDoubleEyeActivity.this.cloudStatusBean.setFreeCloud(0);
                } else {
                    IPCDoubleEyeActivity.this.cloudStatusBean.setFreeCloud(1);
                }
                if (freeCloudStorage.getExpired() == 0) {
                    IPCDoubleEyeActivity.this.cloudStatusBean.setFreeCloudExpired(0);
                } else {
                    IPCDoubleEyeActivity.this.cloudStatusBean.setFreeCloudExpired(1);
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
        Glide.with(getActivity()).load(SpUtil.getString(getActivity(), Utils.getDevSnapKey(this.iotId), "")).into(this.binding.ivSnap);
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
        this.f1574dialog = new AlertDialog.Builder(this).setView(viewInflate).create();
        this.f1574dialog.setCanceledOnTouchOutside(true);
        this.f1574dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.f1574dialog.show();
        int i = getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams attributes = this.f1574dialog.getWindow().getAttributes();
        attributes.width = (int) (((double) i) * 0.95d);
        this.f1574dialog.getWindow().setAttributes(attributes);
        this.f1574dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCDoubleEyeActivity.126
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCDoubleEyeActivity.this.cancelCount();
            }
        });
        ((Button) viewInflate.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.127
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.f1574dialog.dismiss();
            }
        });
        RecyclerView recyclerView = (RecyclerView) viewInflate.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new IpcWiFiAdapter(R.layout.item_wifi_ipc);
        this.mAdapter.bindToRecyclerView(recyclerView);
        this.mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() { // from class: activity.IPCDoubleEyeActivity.128
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view2, int i2) {
                WifiBean wifiBean = IPCDoubleEyeActivity.this.mAdapter.getData().get(i2);
                if (wifiBean.isCurrentWifi()) {
                    return;
                }
                IPCDoubleEyeActivity.this.f1574dialog.dismiss();
                IPCDoubleEyeActivity.this.selectSsid = wifiBean.getSsid();
                IPCDoubleEyeActivity.this.inputDialogView.setTitle(IPCDoubleEyeActivity.this.selectSsid);
                IPCDoubleEyeActivity.this.inputDialogView.show(IPCDoubleEyeActivity.this.getSupportFragmentManager(), IPCDoubleEyeActivity.this.TAG);
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
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCDoubleEyeActivity.129
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCDoubleEyeActivity.this.isSwitching = false;
                IPCDoubleEyeActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.130
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.131
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.isSwitching = true;
            this.countDownTimer = new AnonymousClass132(60000L, 4000L, i, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$132, reason: invalid class name */
    class AnonymousClass132 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ int val$position;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass132(long j, long j2, int i, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
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
            SettingsCtrl.getInstance().getProperties(IPCDoubleEyeActivity.this.iotId, new MyCallback() { // from class: activity.IPCDoubleEyeActivity.132.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCDoubleEyeActivity.this.iotId) == AnonymousClass132.this.val$position) {
                        IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.132.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AnonymousClass132.this.val$progressBarText.setVisibility(8);
                                AnonymousClass132.this.val$progressBar.setVisibility(8);
                                AnonymousClass132.this.val$imageViewText.setVisibility(0);
                                AnonymousClass132.this.val$imageViewText.setText(R.string.switched_success);
                                AnonymousClass132.this.val$imageView.setVisibility(0);
                                AnonymousClass132.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass132.this.val$imageButton.setVisibility(0);
                                IPCDoubleEyeActivity.this.wifiFourPosition = AnonymousClass132.this.val$position;
                                Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                        AnonymousClass132.this.cancel();
                        IPCDoubleEyeActivity.this.countDownTimer = null;
                        IPCDoubleEyeActivity.this.isSwitching = false;
                        IPCDoubleEyeActivity.this.getProperties(new MyCallback() { // from class: activity.IPCDoubleEyeActivity.132.1.2
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
            IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.132.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass132.this.val$progressBarText.setVisibility(8);
                    AnonymousClass132.this.val$progressBar.setVisibility(8);
                    AnonymousClass132.this.val$imageViewText.setVisibility(0);
                    AnonymousClass132.this.val$imageViewText.setText(R.string.switched_fail);
                    AnonymousClass132.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass132.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            IPCDoubleEyeActivity.this.countDownTimer = null;
            IPCDoubleEyeActivity.this.isSwitching = false;
            IPCDoubleEyeActivity.this.getProperties(new MyCallback() { // from class: activity.IPCDoubleEyeActivity.132.3
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
        IPCManager.getInstance().getDevice(this.iotId).queryAPList(new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.133
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null) {
                    return;
                }
                try {
                    IoTResponse ioTResponse = (IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class);
                    if (ioTResponse.getCode() != 200) {
                        IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.133.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCDoubleEyeActivity.this.getWiFiListFailed();
                            }
                        });
                    } else {
                        Object data = ioTResponse.getData();
                        if (data != null) {
                            try {
                                JSONArray jSONArray = ((JSONObject) data).getJSONArray("APList");
                                IPCDoubleEyeActivity.this.wifiBeanList = JSON.parseArray(jSONArray.toString(), WifiBean.class);
                                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.133.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (IPCDoubleEyeActivity.this.isFinishing()) {
                                            return;
                                        }
                                        IPCDoubleEyeActivity.this.FourGChangeDialog();
                                        IPCDoubleEyeActivity.this.getWiFiListSucceed(IPCDoubleEyeActivity.this.wifiBeanList);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                IPCDoubleEyeActivity.this.runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.133.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (IPCDoubleEyeActivity.this.isFinishing()) {
                                            return;
                                        }
                                        IPCDoubleEyeActivity.this.getWiFiListFailed();
                                        IPCDoubleEyeActivity.this.showToast(IPCDoubleEyeActivity.this.getString(R.string.query_wifi_list_fail));
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
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCDoubleEyeActivity.134
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCDoubleEyeActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.135
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.136
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.countDownTimer = new AnonymousClass137(60000L, 4000L, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$137, reason: invalid class name */
    class AnonymousClass137 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass137(long j, long j2, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
            super(j, j2);
            this.val$progressBarText = textView;
            this.val$progressBar = progressBar;
            this.val$imageViewText = textView2;
            this.val$imageView = imageView;
            this.val$imageButton = imageButton;
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            SettingsCtrl.getInstance().getProperties(IPCDoubleEyeActivity.this.iotId, new MyCallback() { // from class: activity.IPCDoubleEyeActivity.137.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getWifiConfigIsExist(IPCDoubleEyeActivity.this.iotId) == 1) {
                        IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.137.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AnonymousClass137.this.val$progressBarText.setVisibility(8);
                                AnonymousClass137.this.val$progressBar.setVisibility(8);
                                AnonymousClass137.this.val$imageViewText.setVisibility(0);
                                AnonymousClass137.this.val$imageViewText.setText(IPCDoubleEyeActivity.this.getString(R.string.switched_success));
                                AnonymousClass137.this.val$imageView.setVisibility(0);
                                AnonymousClass137.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass137.this.val$imageButton.setVisibility(0);
                            }
                        });
                        AnonymousClass137.this.cancel();
                        IPCDoubleEyeActivity.this.countDownTimer = null;
                    }
                }
            });
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.137.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass137.this.val$progressBarText.setVisibility(8);
                    AnonymousClass137.this.val$progressBar.setVisibility(8);
                    AnonymousClass137.this.val$imageViewText.setVisibility(0);
                    AnonymousClass137.this.val$imageViewText.setText(IPCDoubleEyeActivity.this.getString(R.string.switched_fail));
                    AnonymousClass137.this.val$imageView.setVisibility(0);
                    AnonymousClass137.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass137.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            IPCDoubleEyeActivity.this.countDownTimer = null;
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
        ((Button) viewInflate.findViewById(R.id.btn_left)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.138
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        ((Button) viewInflate.findViewById(R.id.btn_right)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.139
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.switch4gMode(iPCDoubleEyeActivity.getString(R.string.Net4GEnableSwitch), 2);
                alertDialogCreate.dismiss();
                IPCDoubleEyeActivity.this.FourGChangeDialog(2, alertDialogCreate);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switch4gMode(String str, int i) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.Net4GEnableSwitch))) {
            map.put(Constants.Net4GEnableSwitch, Integer.valueOf(i));
        }
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.140
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
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.IPCDoubleEyeActivity.141
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                IPCDoubleEyeActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.142
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.143
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.countDownTimer = new AnonymousClass144(60000L, 4000L, i, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.IPCDoubleEyeActivity$144, reason: invalid class name */
    class AnonymousClass144 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ int val$position;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass144(long j, long j2, int i, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
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
            SettingsCtrl.getInstance().getProperties(IPCDoubleEyeActivity.this.iotId, new MyCallback() { // from class: activity.IPCDoubleEyeActivity.144.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getNet4GEnableSwitch(IPCDoubleEyeActivity.this.iotId) == AnonymousClass144.this.val$position) {
                        IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.144.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IPCDoubleEyeActivity.this.playLive();
                                AnonymousClass144.this.val$progressBarText.setVisibility(8);
                                AnonymousClass144.this.val$progressBar.setVisibility(8);
                                AnonymousClass144.this.val$imageViewText.setVisibility(0);
                                AnonymousClass144.this.val$imageViewText.setText(IPCDoubleEyeActivity.this.getString(R.string.switched_success));
                                AnonymousClass144.this.val$imageView.setVisibility(0);
                                AnonymousClass144.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass144.this.val$imageButton.setVisibility(0);
                                Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                        AnonymousClass144.this.cancel();
                        IPCDoubleEyeActivity.this.countDownTimer = null;
                    }
                }
            });
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.144.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass144.this.val$progressBarText.setVisibility(8);
                    AnonymousClass144.this.val$progressBar.setVisibility(8);
                    AnonymousClass144.this.val$imageViewText.setVisibility(0);
                    AnonymousClass144.this.val$imageViewText.setText(IPCDoubleEyeActivity.this.getString(R.string.switched_fail));
                    AnonymousClass144.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass144.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            IPCDoubleEyeActivity.this.countDownTimer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shareDevice(final String str, List<String> list, String str2) {
        HashMap map = new HashMap();
        map.put(AlinkConstants.KEY_ACCOUNT_ATTR, str);
        if (TextUtils.isEmpty(str2)) {
            map.put(AlinkConstants.KEY_ACCOUNT_ATTR_TYPE, DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_EMAIL);
        } else {
            map.put(AlinkConstants.KEY_ACCOUNT_ATTR_TYPE, DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_MOBILE);
            map.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, str2);
        }
        map.put("iotIdList", list);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath(AlinkConstants.HTTP_PATH_DEVICE_SHARE).setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.IPCDoubleEyeActivity.145
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, IPCDoubleEyeActivity.this.TAG, "onFailure");
                Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.share_failed, 0).show();
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                final int code = ioTResponse.getCode();
                Log.e(IPCDoubleEyeActivity.this.TAG, "shareDevice onResponse: code: " + code);
                final String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.145.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Activity activity2 = IPCDoubleEyeActivity.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            if (code == 2077) {
                                DialogUtil.showTipsConfirmDiaLog(IPCDoubleEyeActivity.this.getActivity(), IPCDoubleEyeActivity.this.getString(R.string.sharing_failed), IPCDoubleEyeActivity.this.getString(R.string.sharing_tips_1) + SdkConstant.CLOUDAPI_LF + IPCDoubleEyeActivity.this.getString(R.string.sharing_tips_2) + SdkConstant.CLOUDAPI_LF + IPCDoubleEyeActivity.this.getString(R.string.sharing_tips_3) + SdkConstant.CLOUDAPI_LF + IPCDoubleEyeActivity.this.getString(R.string.sharing_tips_4), IPCDoubleEyeActivity.this.getString(R.string.i_know));
                                return;
                            }
                            Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), localizedMsg, 0).show();
                        }
                    });
                } else {
                    IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.145.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Activity activity2 = IPCDoubleEyeActivity.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), IPCDoubleEyeActivity.this.getString(R.string.share_succeed, new Object[]{((DeviceInfoBean) IPCDoubleEyeActivity.this.shareDialog2.getExtra()).getName(), str}), 0).show();
                        }
                    });
                }
            }
        });
    }

    private void checkSwitch(final TopicBean topicBean, final RecyclerView.Adapter adapter2, final int i, final int i2) {
        this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.146
            @Override // java.lang.Runnable
            public void run() {
                IPCDoubleEyeActivity.this.showProgressDialog();
            }
        });
        HashMap map = new HashMap();
        map.put(Constants.IvpExSwitch, Integer.valueOf(i2));
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.147
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                IPCDoubleEyeActivity.this.dismissProgressDialog();
                if (!z) {
                    IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.147.3
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
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
                            IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.147.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                            return;
                        }
                        IPCDoubleEyeActivity.this.isDetecting = !IPCDoubleEyeActivity.this.isDetecting;
                        if (IPCDoubleEyeActivity.this.faceDetectionAbility == 1) {
                            if (IPCDoubleEyeActivity.this.isDetecting) {
                                SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCDoubleEyeActivity.this.iotId, 1);
                            } else {
                                SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCDoubleEyeActivity.this.iotId, 0);
                            }
                        } else if (IPCDoubleEyeActivity.this.isDetecting) {
                            SharePreferenceManager.getInstance().setIntelligentMode(IPCDoubleEyeActivity.this.iotId, 1);
                        } else {
                            SharePreferenceManager.getInstance().setIntelligentMode(IPCDoubleEyeActivity.this.iotId, 0);
                        }
                        SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCDoubleEyeActivity.this.selectIotId, i2 & 1);
                        SharePreferenceManager.getInstance().setAreaDetectEnable(IPCDoubleEyeActivity.this.selectIotId, (i2 & 4) >> 2);
                        SharePreferenceManager.getInstance().setCrossLineEnable(IPCDoubleEyeActivity.this.selectIotId, (i2 & 2) >> 1);
                        IPCDoubleEyeActivity.this.moreFragment.setDetecting(IPCDoubleEyeActivity.this.isDetecting);
                        IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.147.2
                            @Override // java.lang.Runnable
                            public void run() {
                                topicBean.setSelect(!topicBean.isSelect());
                                if (topicBean.isSelect()) {
                                    topicBean.setIcon(R.drawable.humanoid_hracking_ipc_light);
                                } else {
                                    topicBean.setIcon(R.drawable.humanoid_hracking_ipc);
                                }
                                adapter2.notifyItemChanged(i, topicBean);
                                Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
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
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.148
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                IPCDoubleEyeActivity.this.dismissProgressDialog();
                if (!z) {
                    IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.148.3
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
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
                            IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.148.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                            return;
                        }
                        IPCDoubleEyeActivity.this.isDetecting = !IPCDoubleEyeActivity.this.isDetecting;
                        if (IPCDoubleEyeActivity.this.faceDetectionAbility == 1) {
                            if (IPCDoubleEyeActivity.this.isDetecting) {
                                SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCDoubleEyeActivity.this.iotId, 1);
                            } else {
                                SharePreferenceManager.getInstance().setHumanoidTrackingEnable(IPCDoubleEyeActivity.this.iotId, 0);
                            }
                        } else if (IPCDoubleEyeActivity.this.isDetecting) {
                            SharePreferenceManager.getInstance().setIntelligentMode(IPCDoubleEyeActivity.this.iotId, 1);
                        } else {
                            SharePreferenceManager.getInstance().setIntelligentMode(IPCDoubleEyeActivity.this.iotId, 0);
                        }
                        IPCDoubleEyeActivity.this.moreFragment.setDetecting(IPCDoubleEyeActivity.this.isDetecting);
                        IPCDoubleEyeActivity.this.uiHandler.post(new Runnable() { // from class: activity.IPCDoubleEyeActivity.148.2
                            @Override // java.lang.Runnable
                            public void run() {
                                topicBean.setSelect(!topicBean.isSelect());
                                if (topicBean.isSelect()) {
                                    topicBean.setIcon(R.drawable.humanoid_hracking_ipc_light);
                                } else {
                                    topicBean.setIcon(R.drawable.humanoid_hracking_ipc);
                                }
                                adapter2.notifyItemChanged(i, topicBean);
                                Toast.makeText(IPCDoubleEyeActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
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
        this.binding.llBottom.setVisibility(8);
    }

    public void hideKeyboard(Activity activity2) {
        ((InputMethodManager) activity2.getSystemService("input_method")).hideSoftInputFromWindow(activity2.getWindow().getDecorView().getWindowToken(), 0);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void initAutorView() {
        this.binding.button1.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.149
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.act((iPCDoubleEyeActivity.Page * IPCDoubleEyeActivity.this.controllerSize) + 0);
            }
        });
        this.binding.button1.setOnLongClickListener(new View.OnLongClickListener() { // from class: activity.IPCDoubleEyeActivity.150
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                if (((Integer) IPCDoubleEyeActivity.this.list.get(IPCDoubleEyeActivity.this.Page * IPCDoubleEyeActivity.this.controllerSize)).intValue() != 0) {
                    return false;
                }
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.showDialogIpc(iPCDoubleEyeActivity.Page * IPCDoubleEyeActivity.this.controllerSize);
                return false;
            }
        });
        this.binding.button1.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.151
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.button1.setBackground(IPCDoubleEyeActivity.this.getResources().getDrawable(R.drawable.controller_up_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCDoubleEyeActivity.this.refreshButton();
                return false;
            }
        });
        this.binding.button2.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.152
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.act((iPCDoubleEyeActivity.Page * IPCDoubleEyeActivity.this.controllerSize) + 1);
            }
        });
        this.binding.button2.setOnLongClickListener(new View.OnLongClickListener() { // from class: activity.IPCDoubleEyeActivity.153
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                if (((Integer) IPCDoubleEyeActivity.this.list.get((IPCDoubleEyeActivity.this.Page * IPCDoubleEyeActivity.this.controllerSize) + 1)).intValue() != 0) {
                    return false;
                }
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.showDialogIpc((iPCDoubleEyeActivity.Page * IPCDoubleEyeActivity.this.controllerSize) + 1);
                return false;
            }
        });
        this.binding.button2.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.154
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.button2.setBackground(IPCDoubleEyeActivity.this.getResources().getDrawable(R.drawable.controller_lock_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCDoubleEyeActivity.this.refreshButton();
                return false;
            }
        });
        this.binding.button3.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.155
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.act((iPCDoubleEyeActivity.Page * IPCDoubleEyeActivity.this.controllerSize) + 2);
            }
        });
        this.binding.button3.setOnLongClickListener(new View.OnLongClickListener() { // from class: activity.IPCDoubleEyeActivity.156
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                if (((Integer) IPCDoubleEyeActivity.this.list.get((IPCDoubleEyeActivity.this.Page * IPCDoubleEyeActivity.this.controllerSize) + 2)).intValue() != 0) {
                    return false;
                }
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.showDialogIpc((iPCDoubleEyeActivity.Page * IPCDoubleEyeActivity.this.controllerSize) + 2);
                return false;
            }
        });
        this.binding.button3.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.157
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.button3.setBackground(IPCDoubleEyeActivity.this.getResources().getDrawable(R.drawable.controller_stop_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCDoubleEyeActivity.this.refreshButton();
                return false;
            }
        });
        this.binding.button4.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.158
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.act((iPCDoubleEyeActivity.Page * IPCDoubleEyeActivity.this.controllerSize) + 3);
            }
        });
        this.binding.button4.setOnLongClickListener(new View.OnLongClickListener() { // from class: activity.IPCDoubleEyeActivity.159
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                if (((Integer) IPCDoubleEyeActivity.this.list.get((IPCDoubleEyeActivity.this.Page * IPCDoubleEyeActivity.this.controllerSize) + 3)).intValue() != 0) {
                    return false;
                }
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.showDialogIpc((iPCDoubleEyeActivity.Page * IPCDoubleEyeActivity.this.controllerSize) + 3);
                return false;
            }
        });
        this.binding.button4.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.160
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.button4.setBackground(IPCDoubleEyeActivity.this.getResources().getDrawable(R.drawable.controller_down_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCDoubleEyeActivity.this.refreshButton();
                return false;
            }
        });
        this.binding.doorbell.setOnLongClickListener(new View.OnLongClickListener() { // from class: activity.IPCDoubleEyeActivity.161
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                if (((Integer) IPCDoubleEyeActivity.this.list.get((IPCDoubleEyeActivity.this.Page * IPCDoubleEyeActivity.this.controllerSize) + 4)).intValue() != 0) {
                    return false;
                }
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.showDialogIpc((iPCDoubleEyeActivity.Page * IPCDoubleEyeActivity.this.controllerSize) + 4);
                return false;
            }
        });
        this.binding.doorbell.setOnTouchListener(new View.OnTouchListener() { // from class: activity.IPCDoubleEyeActivity.162
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    IPCDoubleEyeActivity.this.binding.doorbell.setBackground(IPCDoubleEyeActivity.this.getResources().getDrawable(R.drawable.doorbell_down));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                IPCDoubleEyeActivity.this.refreshButton();
                return false;
            }
        });
        this.buttonList.add(this.binding.button1);
        this.buttonList.add(this.binding.button2);
        this.buttonList.add(this.binding.button3);
        this.buttonList.add(this.binding.button4);
        this.buttonList.add(this.binding.doorbell);
        this.controllerSize = this.buttonList.size();
        ((LinearLayout) findViewById(R.id.ll1)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.163
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.pageSelect.setValue(0);
                IPCDoubleEyeActivity.this.refreshButton();
            }
        });
        ((LinearLayout) findViewById(R.id.ll2)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.164
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.pageSelect.setValue(1);
                IPCDoubleEyeActivity.this.refreshButton();
            }
        });
        ((LinearLayout) findViewById(R.id.ll3)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.165
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.pageSelect.setValue(2);
                IPCDoubleEyeActivity.this.refreshButton();
            }
        });
        ((LinearLayout) findViewById(R.id.ll4)).setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.166
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.pageSelect.setValue(3);
                IPCDoubleEyeActivity.this.refreshButton();
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
        imageView5.setOnClickListener(new View.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.167
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                IPCDoubleEyeActivity.this.showTips();
            }
        });
        this.pageSelect.observe(this, new Observer<Integer>() { // from class: activity.IPCDoubleEyeActivity.168
            @Override // androidx.lifecycle.Observer
            public void onChanged(@Nullable Integer num) {
                if (num != null) {
                    ((ImageView) IPCDoubleEyeActivity.this.doorList.get(IPCDoubleEyeActivity.this.Page)).setImageResource(R.drawable.computer_ipc);
                    ((TextView) IPCDoubleEyeActivity.this.doorTextList.get(IPCDoubleEyeActivity.this.Page)).setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.color_black));
                    IPCDoubleEyeActivity.this.Page = num.intValue();
                    ((ImageView) IPCDoubleEyeActivity.this.doorList.get(num.intValue())).setImageResource(R.drawable.computer_ipc_light);
                    ((TextView) IPCDoubleEyeActivity.this.doorTextList.get(num.intValue())).setTextColor(IPCDoubleEyeActivity.this.getResources().getColor(R.color.colorAccent));
                    final int iIntValue = num.intValue();
                    ((Activity) Objects.requireNonNull(IPCDoubleEyeActivity.this.getActivity())).runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.168.1
                        @Override // java.lang.Runnable
                        public void run() {
                            String autoName = SharePreferenceManager.getInstance().getAutoName(IPCDoubleEyeActivity.this.iotId, IPCDoubleEyeActivity.this.Page);
                            if (autoName == null || "".equals(autoName)) {
                                IPCDoubleEyeActivity.this.door_text.setText(((TextView) IPCDoubleEyeActivity.this.doorTextList.get(iIntValue)).getText());
                                ((TextView) IPCDoubleEyeActivity.this.doorTextList.get(IPCDoubleEyeActivity.this.Page)).setText(((TextView) IPCDoubleEyeActivity.this.doorTextList.get(iIntValue)).getText());
                            } else {
                                IPCDoubleEyeActivity.this.door_text.setText(autoName);
                                ((TextView) IPCDoubleEyeActivity.this.doorTextList.get(IPCDoubleEyeActivity.this.Page)).setText(autoName);
                            }
                        }
                    });
                }
            }
        });
        this.pageSelect.postValue(0);
        refreshButtonViewText();
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
            getActivity().runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.169
                @Override // java.lang.Runnable
                public void run() {
                    IPCDoubleEyeActivity.this.showTest(i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void act(int i) {
        IPCManager.getInstance().getDevice(this.iotId).RFActionControl(i, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.170
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                Log.d(IPCDoubleEyeActivity.this.TAG, "onComplete: -------" + z);
            }
        });
    }

    private void delete() {
        for (int i = 0; i < this.controllerSize; i++) {
            IPCManager.getInstance().getDevice(this.iotId).deleteController((this.Page * this.controllerSize) + i, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.171
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    if (IPCDoubleEyeActivity.this.getActivity() != null) {
                        IPCDoubleEyeActivity.this.getActivity().runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.171.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ((IPCDoubleEyeActivity) IPCDoubleEyeActivity.this.getActivity()).getControllerList();
                            }
                        });
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTest(final int i) {
        new DialogView.Builder(this).setContent(getResources().getString(R.string.learn_mode_tips)).setPositiveClickListener(getResources().getString(R.string.confirm), new DialogView.OnPositiveClickListener() { // from class: activity.IPCDoubleEyeActivity.173
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
                tvConfirm.setText(IPCDoubleEyeActivity.this.getResources().getString(R.string.finish));
                tvDes.setText(IPCDoubleEyeActivity.this.getResources().getString(R.string.remote_control_tips));
                tvDes.setGravity(17);
                dialogView.setStatusNum(1);
                dialogView.hideConfirmButton();
                IPCDoubleEyeActivity.this.addController(i, dialogView);
            }
        }).setNegativeClickListener(getResources().getString(R.string.cancel), new DialogView.OnNegativeClickListener() { // from class: activity.IPCDoubleEyeActivity.172
            @Override // view.DialogView.OnNegativeClickListener
            public void onNegativeClick(DialogView dialogView) {
                dialogView.dismiss();
            }
        }).build().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void QueryRFKeyStatus(final int i, final DialogView dialogView) {
        new Thread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.174
            @Override // java.lang.Runnable
            public void run() {
                IPCDoubleEyeActivity iPCDoubleEyeActivity = IPCDoubleEyeActivity.this;
                iPCDoubleEyeActivity.QueryRFKeyStatusTimes = 0;
                iPCDoubleEyeActivity.flag = false;
                iPCDoubleEyeActivity.statusFlag = 2;
                while (IPCDoubleEyeActivity.this.QueryRFKeyStatusTimes < 140 && IPCDoubleEyeActivity.this.statusFlag == 2) {
                    long jUptimeMillis = SystemClock.uptimeMillis();
                    if (jUptimeMillis - IPCDoubleEyeActivity.this.lastOnclickTime1 >= 500) {
                        IPCDoubleEyeActivity.this.lastOnclickTime1 = jUptimeMillis;
                        IPCManager.getInstance().getDevice(IPCDoubleEyeActivity.this.iotId).QueryRFKeyStatus(i, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.174.1
                            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                            public void onComplete(boolean z, @Nullable Object obj) {
                                if (z) {
                                    Log.d(IPCDoubleEyeActivity.this.TAG, "onComplete: -------" + z);
                                    int iIntValue = JSONObject.parseObject(JSONObject.parseObject(String.valueOf(obj)).getString("data")).getInteger("Status").intValue();
                                    if (iIntValue == 1) {
                                        IPCDoubleEyeActivity.this.statusFlag = iIntValue;
                                        IPCDoubleEyeActivity.this.flag = true;
                                    } else if (iIntValue == 0) {
                                        IPCDoubleEyeActivity.this.statusFlag = iIntValue;
                                        IPCDoubleEyeActivity.this.flag = false;
                                    }
                                }
                            }
                        });
                        IPCDoubleEyeActivity.this.QueryRFKeyStatusTimes++;
                    }
                }
                if (IPCDoubleEyeActivity.this.getActivity() != null) {
                    IPCDoubleEyeActivity.this.getActivity().runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.174.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (IPCDoubleEyeActivity.this.flag) {
                                dialogView.getStatus().setImageResource(R.drawable.success);
                                dialogView.hideConfirmButton();
                                dialogView.getTvDes().setText(R.string.learn_success);
                                dialogView.getTvCancel().setText(R.string.known);
                                ((IPCDoubleEyeActivity) IPCDoubleEyeActivity.this.getActivity()).getControllerList();
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
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setCustomTitle(textView).setView(viewInflate).setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.176
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ((Activity) Objects.requireNonNull(IPCDoubleEyeActivity.this.getActivity())).runOnUiThread(new Runnable() { // from class: activity.IPCDoubleEyeActivity.176.1
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
                            activity.IPCDoubleEyeActivity$176 r1 = activity.IPCDoubleEyeActivity.AnonymousClass176.this
                            activity.IPCDoubleEyeActivity r1 = activity.IPCDoubleEyeActivity.this
                            java.lang.String r1 = activity.IPCDoubleEyeActivity.access$1800(r1)
                            activity.IPCDoubleEyeActivity$176 r2 = activity.IPCDoubleEyeActivity.AnonymousClass176.this
                            android.widget.EditText r2 = r2
                            android.text.Editable r2 = r2.getText()
                            java.lang.String r2 = r2.toString()
                            activity.IPCDoubleEyeActivity$176 r3 = activity.IPCDoubleEyeActivity.AnonymousClass176.this
                            activity.IPCDoubleEyeActivity r3 = activity.IPCDoubleEyeActivity.this
                            int r3 = activity.IPCDoubleEyeActivity.access$12000(r3)
                            r0.saveAutoName(r1, r2, r3)
                            tools.SharePreferenceManager r0 = tools.SharePreferenceManager.getInstance()
                            activity.IPCDoubleEyeActivity$176 r1 = activity.IPCDoubleEyeActivity.AnonymousClass176.this
                            activity.IPCDoubleEyeActivity r1 = activity.IPCDoubleEyeActivity.this
                            java.lang.String r1 = activity.IPCDoubleEyeActivity.access$1800(r1)
                            activity.IPCDoubleEyeActivity$176 r2 = activity.IPCDoubleEyeActivity.AnonymousClass176.this
                            activity.IPCDoubleEyeActivity r2 = activity.IPCDoubleEyeActivity.this
                            int r2 = activity.IPCDoubleEyeActivity.access$12000(r2)
                            java.lang.String r0 = r0.getAutoName(r1, r2)
                            activity.IPCDoubleEyeActivity$176 r1 = activity.IPCDoubleEyeActivity.AnonymousClass176.this
                            activity.IPCDoubleEyeActivity r1 = activity.IPCDoubleEyeActivity.this
                            android.widget.TextView r1 = r1.door_text
                            if (r0 == 0) goto L4c
                            java.lang.String r2 = ""
                            boolean r2 = r0.equals(r2)
                            if (r2 != 0) goto L4c
                            goto L66
                        L4c:
                            activity.IPCDoubleEyeActivity$176 r0 = activity.IPCDoubleEyeActivity.AnonymousClass176.this
                            activity.IPCDoubleEyeActivity r0 = activity.IPCDoubleEyeActivity.this
                            java.util.List r0 = activity.IPCDoubleEyeActivity.access$12800(r0)
                            activity.IPCDoubleEyeActivity$176 r2 = activity.IPCDoubleEyeActivity.AnonymousClass176.this
                            activity.IPCDoubleEyeActivity r2 = activity.IPCDoubleEyeActivity.this
                            int r2 = activity.IPCDoubleEyeActivity.access$12000(r2)
                            java.lang.Object r0 = r0.get(r2)
                            android.widget.TextView r0 = (android.widget.TextView) r0
                            java.lang.CharSequence r0 = r0.getText()
                        L66:
                            r1.setText(r0)
                            activity.IPCDoubleEyeActivity$176 r0 = activity.IPCDoubleEyeActivity.AnonymousClass176.this
                            activity.IPCDoubleEyeActivity r0 = activity.IPCDoubleEyeActivity.this
                            activity.IPCDoubleEyeActivity.access$13100(r0)
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: activity.IPCDoubleEyeActivity.AnonymousClass176.AnonymousClass1.run():void");
                    }
                });
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: activity.IPCDoubleEyeActivity.175
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
        editText.addTextChangedListener(new TextWatcher() { // from class: activity.IPCDoubleEyeActivity.177
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
        IPCManager.getInstance().getDevice(this.iotId).AddController(i, (i + 1) % 5 == 0 ? 1 : 0, new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.178
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (z) {
                    Log.d(IPCDoubleEyeActivity.this.TAG, "onComplete: -------" + z);
                    if (JSONObject.parseObject(String.valueOf(obj)).getIntValue("code") == 200) {
                        IPCDoubleEyeActivity.this.QueryRFKeyStatus(i, dialogView);
                    }
                }
            }
        });
    }

    @Override // view.CircleTooView.setOnLister
    public void setPointControl() {
        IPCManager.getInstance().getDevice(this.iotId2).setPTZPointControl((int) ((this.binding.lineViewItem.getSp().x / this.binding.lineViewItem.getWidth()) * 640.0f), (int) ((this.binding.lineViewItem.getSp().y / this.binding.lineViewItem.getHeight()) * 360.0f), new IPanelCallback() { // from class: activity.IPCDoubleEyeActivity.179
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                Log.d(IPCDoubleEyeActivity.this.TAG, "onComplete: puppet:b====" + z);
            }
        });
    }
}
