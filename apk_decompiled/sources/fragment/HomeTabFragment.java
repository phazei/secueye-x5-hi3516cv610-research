package fragment;

import activity.AddDeviceActivity;
import activity.BleRouterActivity;
import activity.BleRouterSettingActivity;
import activity.IPCDoubleEyeActivity;
import activity.IPCFourEyesActivity;
import activity.IPCThreeEyesActivity;
import activity.IPCThreeFalseEyesActivity;
import activity.IPCameraActivity;
import activity.InputWifiActivity;
import activity.Net4GSwitchActivity;
import activity.PayYunServiceActivity2;
import activity.RecordVideoActivity;
import activity.SIMWebActivity;
import activity.ScanActivity;
import activity.SettingsActivity;
import activity.Traffic4GActivity;
import activity.scanNavigationActivity;
import adapter.DeviceAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import bean.AreaCodeModel;
import bean.CameraNameModify;
import bean.CameraRemove;
import bean.CameraSnapUpdate;
import bean.DeviceInfoBean;
import bean.DeviceInfoBeans;
import bean.DeviceTimeModel;
import bean.FoundDeviceListItem;
import bean.PushRefresh;
import bean.RefreshDevices;
import bean.SceneInfoBean;
import bean.ShareDeviceInfoBean;
import bean.TopicModel;
import bean.setProperties;
import bean.updateTimeModify;
import com.alibaba.cloudapi.sdk.constant.HttpConstant;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.discovery.DiscoveryType;
import com.aliyun.alink.business.devicecenter.api.discovery.IDeviceDiscoveryListener;
import com.aliyun.alink.business.devicecenter.api.discovery.LocalDeviceMgr;
import com.aliyun.alink.business.devicecenter.api.share.DeviceShareManager;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.channel.core.base.AError;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileConnectListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileDownstreamListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileRequestListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileSubscrbieListener;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileChannel;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectState;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.permissions.Permission;
import com.seculink.app.R;
import config.AppConfig;
import config.Constants;
import dialog.BaseDialog;
import dialog.DialogUtil;
import dialog.InputDialogView;
import dialog.ScanProgressDialog;
import dialog.ShareDialog;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import sdk.ChannelManager;
import sdk.EnvConfigure;
import sdk.IPCManager;
import tools.DateUtil;
import tools.DensityUtil;
import tools.IChangeTab;
import tools.ISetCallback;
import tools.LogEx;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import tools.SystemUtil;
import tools.TimeUtil;
import tools.Utils;

/* JADX INFO: loaded from: classes4.dex */
public class HomeTabFragment extends CommonFragment implements View.OnClickListener {
    private static final int INTENT_DELETE = 1;
    private static final int INTENT_RECEIVE_SHARE = 2;
    private static final int INTENT_REFUSE_SHARE = 3;
    public static final int REQUEST_CODE = 10002;
    public static final int REQUEST_CODE_SCAN = 10003;
    private static boolean isLanConnect = true;
    private static boolean isRefresh = true;
    Button bt_scan;
    private int currentFirstVisibleItemPosition;
    private SimpleDateFormat dateFormat;
    private List<DeviceInfoBean> deviceInfoBeanListAll;
    private List<ShareDeviceInfoBean> deviceInfoBeanListShareAll;
    private EditText edit_search;
    private int firstVisibleItemPosition;
    Button ilop_main_add_big_btn;
    Button ilop_main_menu_add_device_btn;
    Button ilop_main_menu_scan_btn;
    private List<Integer> intList;
    private boolean isDiscoverying;
    private boolean isOtherCard;
    private boolean isTasking;
    private ImageView iv_search;
    private RelativeLayout layout_top;
    private LinearLayout ll_add_device;
    private LinearLayout ll_scan_device;
    private DeviceAdapter mAdapter;
    private int mGetDeviceIntent;
    private IChangeTab mIChangeTab;
    private SceneInfoBean mSceneInfo;
    FrameLayout mainMenu;
    private List<DeviceInfoBeans> nvrDevicesList;
    private PopupWindow pw;
    RecyclerView recycler;
    RelativeLayout rlEmpty;
    private RelativeLayout rl_search;
    private ScanProgressDialog scanProgressDialog;
    List<ShareDeviceInfoBean> shareDeviceInfoBeanList;
    private InputDialogView shareDialog;
    private ShareDialog shareDialog2;
    SwipeRefreshLayout srl;
    private ThreadPoolExecutor threadPoolExecutor;
    private TextView tv_cancel;
    private TextView tv_search_fail;
    private String TAG = HomeTabFragment.class.getSimpleName();
    private Handler mHandler = new Handler();
    private Bundle mBundle = new Bundle();
    private AtomicInteger foudDevices = new AtomicInteger();
    private AtomicInteger bindDevices = new AtomicInteger();
    private int alarmAdvanceLoadSize = 3;
    private List<DeviceInfoBeans> deviceInfoBeansList = new ArrayList();
    private ChannelManager.IMobileMsgListener iMobileMsgListener = new ChannelManager.IMobileMsgListener() { // from class: fragment.HomeTabFragment.5
        @Override // sdk.ChannelManager.IMobileMsgListener
        public void onCommand(String str, String str2) {
            Log.e(HomeTabFragment.this.TAG, "ChannelManager.IMobileMsgListener    topic:" + str + "     msg:" + str2);
            if (str.equals("/thing/status")) {
                TopicModel topicModel = (TopicModel) JSON.parseObject(str2, TopicModel.class);
                if (SharePreferenceManager.getInstance().getLowPower(topicModel.iotId) == 1) {
                    HashMap map = new HashMap();
                    map.put("iotId", topicModel.iotId);
                    new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/status/get").setScheme(Scheme.HTTPS).setApiVersion("1.0.4").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: fragment.HomeTabFragment.5.1
                        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                        public void onFailure(IoTRequest ioTRequest, Exception exc) {
                        }

                        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                            try {
                                if (((JSONObject) ioTResponse.getData()).get("status").toString().equals("1")) {
                                    EventBus.getDefault().post(new PushRefresh());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    EventBus.getDefault().post(new PushRefresh());
                }
            }
            if (str.equals("/thing/properties") && str2.contains(Constants.LowPowerStatus)) {
                try {
                    JSONObject jSONObject = new JSONObject(str2);
                    String string = jSONObject.getString("iotId");
                    int i = jSONObject.getJSONObject("items").getJSONObject(Constants.LowPowerMode).getJSONObject("value").getInt(Constants.LowPowerStatus);
                    if (string.isEmpty()) {
                        return;
                    }
                    SharePreferenceManager.getInstance().setLowPowerStatus(string, i);
                    List<DeviceInfoBeans> data = HomeTabFragment.this.mAdapter.getData();
                    for (int i2 = 0; i2 < data.size(); i2++) {
                        if (data.get(i2).getData().size() != 0) {
                            for (int i3 = 0; i3 < data.get(i2).getData().size(); i3++) {
                                if (data.get(i2).getData().get(i3).getIotId().equals(string)) {
                                    HomeTabFragment.this.mAdapter.refreshLowPowerStatus(i2, i);
                                    Log.e(HomeTabFragment.this.TAG, string + "     LowPowerStatus: " + i);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void test(String str) {
    }

    @Override // fragment.CommonFragment
    protected int getContentLayoutId() {
        return R.layout.hometab_fragment_layout;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cameraRemove(CameraRemove cameraRemove) {
        startTask(1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cameraNameModify(CameraNameModify cameraNameModify) {
        int i = 0;
        while (true) {
            if (i >= this.mAdapter.getData().size()) {
                break;
            }
            if (this.mAdapter.getData().get(i).getData().get(0).getIotId().equals(cameraNameModify.getIotId())) {
                this.mAdapter.getData().get(i).getData().get(0).setNickName(cameraNameModify.getName());
                this.mAdapter.notifyItemChanged(i);
                break;
            }
            i++;
        }
        notifyDeviceUpdate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTimeModify(final updateTimeModify updatetimemodify) {
        HashMap map = new HashMap();
        map.put("iotId", updatetimemodify.getIotId());
        map.put(AlinkConstants.KEY_PAGE_NO, 1);
        map.put(AlinkConstants.KEY_PAGE_SIZE, 6);
        IoTRequest ioTRequestBuild = new IoTRequestBuilder().setPath("/subdevices/list").setScheme(Scheme.HTTP).setApiVersion("1.0.6").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build();
        IoTAPIClient client = new IoTAPIClientFactory().getClient();
        new ArrayList();
        client.send(ioTRequestBuild, new IoTCallback() { // from class: fragment.HomeTabFragment.1
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                HomeTabFragment.this.setDeviceTimeOwner(updatetimemodify.getIotId());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                ioTResponse.getLocalizedMsg();
                if (code == 200) {
                    List array = null;
                    try {
                        array = JSON.parseArray(((JSONObject) ioTResponse.getData()).getString("data"), DeviceInfoBean.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < array.size(); i++) {
                        HomeTabFragment.this.setAddDevicePush(((DeviceInfoBean) array.get(i)).getIotId());
                        HomeTabFragment.this.setDeviceTimeOwner(((DeviceInfoBean) array.get(i)).getIotId());
                    }
                    return;
                }
                HomeTabFragment.this.setDeviceTimeOwner(updatetimemodify.getIotId());
            }
        });
        setAddDevicePush(updatetimemodify.getIotId());
        setDeviceTimeOwner(updatetimemodify.getIotId());
    }

    public void setDeviceTimeOwner(final String str) {
        Date date = new Date(System.currentTimeMillis());
        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String str2 = this.dateFormat.format(date);
        final String strSubstring = DateUtil.getPhonrCurrentTimeZone().substring(0, 9);
        HashMap map = new HashMap();
        try {
            this.dateFormat.setTimeZone(TimeZone.getTimeZone(strSubstring));
        } catch (Exception e) {
            e.printStackTrace();
        }
        final long dateTime = DateUtil.getDateTime(str2, this.dateFormat);
        final DeviceTimeModel deviceTimeModel = new DeviceTimeModel();
        deviceTimeModel.setTime((int) (dateTime / 1000));
        if (strSubstring.contains("GMT")) {
            deviceTimeModel.setTZ(strSubstring);
        }
        map.put(Constants.DEVICE_TIME, deviceTimeModel);
        IPCManager.getInstance().getDevice(str).setProperties(map, new IPanelCallback() { // from class: fragment.HomeTabFragment.2
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (z) {
                    if (obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            Log.e("设备绑定", str + "时区时间同步失败" + obj.toString());
                            return;
                        }
                        Log.e("设备绑定", str + "时区时间同步" + deviceTimeModel.toString());
                        SharePreferenceManager.getInstance().setDeviceTime(str, (int) (dateTime / 1000));
                        SharePreferenceManager.getInstance().setDeviceTZ(str, strSubstring);
                        return;
                    }
                    return;
                }
                Log.e("设备绑定", "时区时间同步失败" + obj.toString());
            }
        });
        final String userPhone = Utils.getUserPhone();
        if (TextUtils.isEmpty(userPhone)) {
            return;
        }
        HashMap map2 = new HashMap();
        map2.put(Constants.DEVICE_OWNER, userPhone);
        IPCManager.getInstance().getDevice(str).setProperties(map2, new IPanelCallback() { // from class: fragment.HomeTabFragment.3
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (z) {
                    if (obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            Log.e("设备绑定", "绑定所有者失败" + obj.toString());
                            return;
                        }
                        Log.e("设备绑定", str + "绑定所有者" + userPhone);
                        SharePreferenceManager.getInstance().setDeviceOwner(str, userPhone);
                        return;
                    }
                    return;
                }
                Log.e("设备绑定", "绑定所有者失败" + obj.toString());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void CameraSnapUpdate(CameraSnapUpdate cameraSnapUpdate) {
        boolean z = false;
        for (int i = 0; i < this.mAdapter.getData().size(); i++) {
            Iterator<DeviceInfoBean> it = this.mAdapter.getData().get(i).getData().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (cameraSnapUpdate.getIotId().equals(it.next().getIotId())) {
                    this.mAdapter.notifyItemChanged(i);
                    z = true;
                    break;
                }
            }
            if (z) {
                return;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshDevices(RefreshDevices refreshDevices) {
        if (LoginBusiness.isLogin()) {
            startTask(1);
        }
    }

    @Override // fragment.CommonFragment
    protected void initWidget(View view2) {
        super.initWidget(view2);
        this.isTasking = false;
        this.rlEmpty = (RelativeLayout) view2.findViewById(R.id.rl_empty);
        this.ll_add_device = (LinearLayout) view2.findViewById(R.id.ll_add_device);
        this.ll_add_device.setOnClickListener(this);
        this.ll_scan_device = (LinearLayout) view2.findViewById(R.id.ll_scan_device);
        this.ll_scan_device.setOnClickListener(this);
        this.bt_scan = (Button) view2.findViewById(R.id.bt_scan);
        this.bt_scan.setOnClickListener(this);
        this.ilop_main_add_big_btn = (Button) view2.findViewById(R.id.ilop_main_add_big_btn);
        this.ilop_main_add_big_btn.setOnClickListener(this);
        this.ilop_main_menu_add_device_btn = (Button) view2.findViewById(R.id.ilop_main_menu_add_device_btn);
        this.ilop_main_menu_add_device_btn.setOnClickListener(this);
        this.ilop_main_menu_scan_btn = (Button) view2.findViewById(R.id.ilop_main_menu_scan_btn);
        this.ilop_main_menu_scan_btn.setOnClickListener(this);
        this.srl = (SwipeRefreshLayout) view2.findViewById(R.id.srl);
        this.recycler = (RecyclerView) view2.findViewById(R.id.recycler);
        this.mainMenu = (FrameLayout) view2.findViewById(R.id.ilop_main_menu);
        this.edit_search = (EditText) view2.findViewById(R.id.edit_search);
        this.tv_search_fail = (TextView) view2.findViewById(R.id.tv_search_fail);
        this.tv_cancel = (TextView) view2.findViewById(R.id.tv_cancel);
        this.tv_cancel.setOnClickListener(this);
        this.iv_search = (ImageView) view2.findViewById(R.id.iv_search);
        this.iv_search.setOnClickListener(this);
        this.layout_top = (RelativeLayout) view2.findViewById(R.id.layout_top);
        this.rl_search = (RelativeLayout) view2.findViewById(R.id.rl_search);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        SpannableString spannableString = new SpannableString(getString(R.string.search_hint));
        spannableString.setSpan(new AbsoluteSizeSpan(AppConfig.isChina ? 14 : 13, true), 0, spannableString.length(), 33);
        this.edit_search.setHint(new SpannedString(spannableString));
        this.edit_search.addTextChangedListener(new TextWatcher() { // from class: fragment.HomeTabFragment.4
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Log.e("输入", "" + charSequence.toString());
                String string = charSequence.toString();
                if (string.isEmpty()) {
                    HomeTabFragment.this.tv_search_fail.setVisibility(8);
                    if (HomeTabFragment.this.deviceInfoBeansList == null || HomeTabFragment.this.mAdapter == null) {
                        return;
                    }
                    HomeTabFragment.this.mAdapter.replaceData(HomeTabFragment.this.deviceInfoBeansList);
                    HomeTabFragment.this.mAdapter.notifyDataSetChanged();
                    return;
                }
                ArrayList arrayList = new ArrayList();
                if (HomeTabFragment.this.deviceInfoBeansList != null) {
                    for (int i4 = 0; i4 < HomeTabFragment.this.deviceInfoBeansList.size(); i4++) {
                        if (((DeviceInfoBeans) HomeTabFragment.this.deviceInfoBeansList.get(i4)).getData().get(0).getName().contains(string)) {
                            arrayList.add(HomeTabFragment.this.deviceInfoBeansList.get(i4));
                        }
                    }
                }
                if (HomeTabFragment.this.mAdapter != null) {
                    HomeTabFragment.this.mAdapter.replaceData(arrayList);
                    HomeTabFragment.this.mAdapter.notifyDataSetChanged();
                }
                if (arrayList.size() == 0) {
                    HomeTabFragment.this.tv_search_fail.setVisibility(0);
                } else {
                    HomeTabFragment.this.tv_search_fail.setVisibility(8);
                }
            }
        });
    }

    @Override // fragment.CommonFragment
    protected void initData() {
        super.initData();
    }

    @Override // fragment.CommonFragment
    protected void onFirstInitData() {
        super.onFirstInitData();
        this.srl.measure(0, 0);
        this.srl.setRefreshing(true);
        this.srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        if (LoginBusiness.isLogin()) {
            startTask(1);
        }
        this.mIChangeTab = (IChangeTab) getActivity();
        ChannelManager.getInstance().registerListener(this.iMobileMsgListener);
        MobileChannel.getInstance().subscrbie("path/of/topic", new IMobileSubscrbieListener() { // from class: fragment.HomeTabFragment.6
            @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
            public boolean needUISafety() {
                return false;
            }

            @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
            public void onSuccess(String str) {
                Log.d("属性监听", "订阅onSuccess, topic = " + str);
            }

            @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
            public void onFailed(String str, AError aError) {
                Log.d("属性监听", "订阅onFailed, topic = " + str);
            }
        });
        MobileChannel.getInstance().registerDownstreamListener(true, new IMobileDownstreamListener() { // from class: fragment.HomeTabFragment.7
            @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileDownstreamListener
            public boolean shouldHandle(String str) {
                return true;
            }

            @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileDownstreamListener
            public void onCommand(String str, String str2) {
                Log.d("属性监听", "订阅接收到Topic = " + str + ", data=" + str2);
                EventBus.getDefault().post(new setProperties(str2));
                if (str.equals("/thing/properties") && str2.contains(Constants.LowPowerStatus)) {
                    try {
                        JSONObject jSONObject = new JSONObject(str2);
                        String string = jSONObject.getString("iotId");
                        int i = jSONObject.getJSONObject("items").getJSONObject(Constants.LowPowerMode).getJSONObject("value").getInt(Constants.LowPowerStatus);
                        if (string.isEmpty()) {
                            return;
                        }
                        SharePreferenceManager.getInstance().setLowPowerStatus(string, i);
                        List<DeviceInfoBeans> data = HomeTabFragment.this.mAdapter.getData();
                        for (int i2 = 0; i2 < data.size(); i2++) {
                            if (data.get(i2).getData().size() != 0) {
                                for (int i3 = 0; i3 < data.get(i2).getData().size(); i3++) {
                                    if (data.get(i2).getData().get(i3).getIotId().equals(string)) {
                                        HomeTabFragment.this.mAdapter.refreshLowPowerStatus(i2, i);
                                        Log.e("属性监听", string + "  修改设备休眠状态   LowPowerStatus: " + i);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        MobileChannel.getInstance().registerConnectListener(true, new IMobileConnectListener() { // from class: fragment.HomeTabFragment.8
            @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileConnectListener
            public void onConnectStateChange(MobileConnectState mobileConnectState) {
                Log.d("属性监听", "订阅通道状态变化，state=" + mobileConnectState);
            }
        });
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setInitialPrefetchItemCount(5);
        this.recycler.setLayoutManager(linearLayoutManager);
        this.mAdapter = new DeviceAdapter(R.layout.item_device);
        this.mAdapter.setHasStableIds(true);
        this.mAdapter.bindToRecyclerView(this.recycler);
        RecyclerView.ItemAnimator itemAnimator = this.recycler.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        this.recycler.getItemAnimator().setChangeDuration(0L);
        this.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: fragment.HomeTabFragment.9
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
                LinearLayoutManager linearLayoutManager2;
                int iFindFirstVisibleItemPosition;
                super.onScrollStateChanged(recyclerView, i);
                if (i != 0 || (linearLayoutManager2 = (LinearLayoutManager) recyclerView.getLayoutManager()) == null || (iFindFirstVisibleItemPosition = linearLayoutManager2.findFirstVisibleItemPosition()) < 0) {
                    return;
                }
                HomeTabFragment.this.firstVisibleItemPosition = iFindFirstVisibleItemPosition;
                Log.e(HomeTabFragment.this.TAG, "firstVisibleItemPosition: " + HomeTabFragment.this.firstVisibleItemPosition);
                if (HomeTabFragment.this.currentFirstVisibleItemPosition != HomeTabFragment.this.firstVisibleItemPosition) {
                    for (int i2 = HomeTabFragment.this.firstVisibleItemPosition; i2 < HomeTabFragment.this.firstVisibleItemPosition + HomeTabFragment.this.alarmAdvanceLoadSize; i2++) {
                        try {
                            HomeTabFragment.this.getAlarmParam(HomeTabFragment.this.mAdapter.getData().get(i2).getData().get(0), i2);
                        } catch (IndexOutOfBoundsException unused) {
                        }
                    }
                    HomeTabFragment homeTabFragment = HomeTabFragment.this;
                    homeTabFragment.currentFirstVisibleItemPosition = homeTabFragment.firstVisibleItemPosition;
                    Log.e(HomeTabFragment.this.TAG, "currentFirstCompletelyVisibleItemPosition: " + HomeTabFragment.this.currentFirstVisibleItemPosition);
                }
            }
        });
        this.mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() { // from class: fragment.HomeTabFragment.10
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view2, final int i) {
                String[] strArr = new String[4];
                for (int i2 = 0; i2 < 4; i2++) {
                    int i3 = i + i2;
                    if (i3 < HomeTabFragment.this.mAdapter.getItemCount()) {
                        if (HomeTabFragment.this.mAdapter.getData().get(i3).getData().size() > 0) {
                            strArr[i2] = HomeTabFragment.this.mAdapter.getData().get(i3).getData().get(0).getIotId();
                        }
                    } else if (i3 - HomeTabFragment.this.mAdapter.getItemCount() < i && i3 - HomeTabFragment.this.mAdapter.getItemCount() >= 0 && HomeTabFragment.this.mAdapter.getData().get(i).getData().size() > 0) {
                        int itemCount = i3 - HomeTabFragment.this.mAdapter.getItemCount();
                        if (itemCount > HomeTabFragment.this.mAdapter.getItemCount() || HomeTabFragment.this.mAdapter.getData().get(itemCount).getData().size() == 0) {
                            return;
                        } else {
                            strArr[i2] = HomeTabFragment.this.mAdapter.getData().get(itemCount).getData().get(0).getIotId();
                        }
                    }
                }
                if ((!HomeTabFragment.this.mAdapter.getData().get(i).imei.isEmpty() || !HomeTabFragment.this.mAdapter.getData().get(i).iccid.isEmpty()) && view2.getId() == R.id.layout_card) {
                    if (!HomeTabFragment.this.mAdapter.getData().get(i).imei.isEmpty()) {
                        Intent intent = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) SIMWebActivity.class);
                        intent.putExtra(com.taobao.accs.common.Constants.KEY_IMEI, "" + HomeTabFragment.this.mAdapter.getData().get(i).imei);
                        HomeTabFragment.this.startActivity(intent);
                    }
                    if (HomeTabFragment.this.mAdapter.getData().get(i).iccid.isEmpty()) {
                        return;
                    }
                    Intent intent2 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) SIMWebActivity.class);
                    intent2.putExtra("iccid", "" + HomeTabFragment.this.mAdapter.getData().get(i).iccid);
                    HomeTabFragment.this.startActivity(intent2);
                    return;
                }
                if (HomeTabFragment.this.mAdapter.getData().get(i).getData().size() != 0) {
                    final DeviceInfoBean deviceInfoBean = HomeTabFragment.this.mAdapter.getData().get(i).getData().get(0);
                    switch (view2.getId()) {
                        case R.id.fl_img /* 2131296778 */:
                            if (HomeTabFragment.this.mAdapter.getData().get(i).getData().size() != 1) {
                                if (HomeTabFragment.this.mAdapter.getData().get(i).getData().size() != 2) {
                                    if (HomeTabFragment.this.mAdapter.getData().get(i).getData().size() != 3) {
                                        if (HomeTabFragment.this.mAdapter.getData().get(i).getData().size() == 4) {
                                            Intent intent3 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) IPCFourEyesActivity.class);
                                            intent3.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, HomeTabFragment.this.mAdapter.getData().get(i).getData().get(0));
                                            intent3.putExtra("device1", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(1));
                                            intent3.putExtra("device2", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(2));
                                            intent3.putExtra("device3", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(3));
                                            intent3.putExtra("nvrDevice", HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean());
                                            HomeTabFragment.this.startActivity(intent3);
                                        }
                                    } else {
                                        Intent intent4 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) IPCThreeEyesActivity.class);
                                        intent4.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, HomeTabFragment.this.mAdapter.getData().get(i).getData().get(0));
                                        intent4.putExtra("device1", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(1));
                                        intent4.putExtra("device2", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(2));
                                        intent4.putExtra("nvrDevice", HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean());
                                        HomeTabFragment.this.startActivity(intent4);
                                    }
                                } else if (deviceInfoBean != null && deviceInfoBean.getIotId() != null) {
                                    SettingsCtrl.getInstance().getProperties(deviceInfoBean.getIotId(), new MyCallback() { // from class: fragment.HomeTabFragment.10.1
                                        @Override // tools.MyCallback
                                        public void onComplete(boolean z) {
                                            if (SharePreferenceManager.getInstance().getFakeDualEnable(deviceInfoBean.getIotId()) == 1) {
                                                Intent intent5 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) IPCThreeFalseEyesActivity.class);
                                                intent5.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, HomeTabFragment.this.mAdapter.getData().get(i).getData().get(0));
                                                intent5.putExtra("device1", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(1));
                                                intent5.putExtra("device2", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(1));
                                                intent5.putExtra("nvrDevice", HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean());
                                                HomeTabFragment.this.startActivity(intent5);
                                                return;
                                            }
                                            Intent intent6 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) IPCDoubleEyeActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, deviceInfoBean);
                                            intent6.putExtra("nvrOwner", HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean().getOwned());
                                            intent6.putExtra("nvrIotId", HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean().getIotId());
                                            intent6.putExtra("nvrDevice", HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean());
                                            intent6.putExtra("ballIotId", deviceInfoBean.getIotId());
                                            bundle.putSerializable("device1", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(1));
                                            intent6.putExtra("gunIotId", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(1).getIotId());
                                            intent6.putExtras(bundle);
                                            intent6.putExtra("appKey", EnvConfigure.getEnvArg(EnvConfigure.KEY_APPKEY));
                                            HomeTabFragment.this.startActivity(intent6);
                                        }
                                    });
                                } else {
                                    Intent intent5 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) IPCDoubleEyeActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, deviceInfoBean);
                                    intent5.putExtra("nvrOwner", HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean().getOwned());
                                    intent5.putExtra("nvrIotId", HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean().getIotId());
                                    intent5.putExtra("nvrDevice", HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean());
                                    intent5.putExtra("ballIotId", deviceInfoBean.getIotId());
                                    bundle.putSerializable("device1", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(1));
                                    intent5.putExtra("gunIotId", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(1).getIotId());
                                    intent5.putExtras(bundle);
                                    intent5.putExtra("appKey", EnvConfigure.getEnvArg(EnvConfigure.KEY_APPKEY));
                                    HomeTabFragment.this.startActivity(intent5);
                                }
                            } else if (SharePreferenceManager.getInstance().getIsRouter(deviceInfoBean.getIotId()) == 1) {
                                Intent intent6 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) BleRouterActivity.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, deviceInfoBean);
                                intent6.putExtras(bundle2);
                                HomeTabFragment.this.startActivity(intent6);
                            } else {
                                Intent intent7 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) IPCameraActivity.class);
                                Bundle bundle3 = new Bundle();
                                bundle3.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, deviceInfoBean);
                                intent7.putExtras(bundle3);
                                intent7.putExtra("appKey", EnvConfigure.getEnvArg(EnvConfigure.KEY_APPKEY));
                                intent7.putExtra(AlinkConstants.KEY_LIST, strArr);
                                HomeTabFragment.this.startActivity(intent7);
                            }
                            break;
                        case R.id.iv_like /* 2131297058 */:
                            List array = JSON.parseArray(SharePreferenceManager.getInstance().getLikeList(), String.class);
                            if (array == null) {
                                array = new ArrayList();
                            }
                            boolean z = false;
                            for (int i4 = 0; i4 < array.size(); i4++) {
                                if (deviceInfoBean.getIotId().equals(array.get(i4))) {
                                    z = true;
                                }
                            }
                            if (z) {
                                Iterator it = array.iterator();
                                while (it.hasNext()) {
                                    if (deviceInfoBean.getIotId().equals((String) it.next())) {
                                        it.remove();
                                    }
                                }
                            } else {
                                array.add(0, deviceInfoBean.getIotId());
                            }
                            Iterator it2 = HomeTabFragment.this.deviceInfoBeansList.iterator();
                            DeviceInfoBeans deviceInfoBeans = null;
                            while (it2.hasNext()) {
                                DeviceInfoBeans deviceInfoBeans2 = (DeviceInfoBeans) it2.next();
                                if (deviceInfoBeans2.getData() != null && deviceInfoBeans2.getData().size() != 0 && deviceInfoBeans2.getData().get(0) != null && deviceInfoBean.getIotId().equals(deviceInfoBeans2.getData().get(0).getIotId())) {
                                    it2.remove();
                                    deviceInfoBeans = deviceInfoBeans2;
                                }
                            }
                            if (z) {
                                if (deviceInfoBeans.getData().size() == 0) {
                                    deviceInfoBeans.getCellDeviceInfoBean().setLike(false);
                                } else {
                                    deviceInfoBeans.getData().get(0).setLike(false);
                                }
                                HomeTabFragment.this.deviceInfoBeansList.add(deviceInfoBeans);
                            } else {
                                if (deviceInfoBeans.getData().size() == 0) {
                                    deviceInfoBeans.getCellDeviceInfoBean().setLike(true);
                                } else {
                                    deviceInfoBeans.getData().get(0).setLike(true);
                                }
                                HomeTabFragment.this.deviceInfoBeansList.add(0, deviceInfoBeans);
                            }
                            SharePreferenceManager.getInstance().setLikeList(JSON.toJSONString(array));
                            HomeTabFragment.this.mAdapter.replaceData(HomeTabFragment.SortingByOnlineStrategy(HomeTabFragment.this.deviceInfoBeansList));
                            HomeTabFragment.this.mAdapter.notifyDataSetChanged();
                            break;
                        case R.id.tab_alarm /* 2131297727 */:
                            HomeTabFragment.this.updateAlarmParam(deviceInfoBean, i);
                            break;
                        case R.id.tab_back_play /* 2131297728 */:
                            Intent intent8 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) RecordVideoActivity.class);
                            intent8.putExtra("title", deviceInfoBean.getName());
                            intent8.putExtra("iotId", deviceInfoBean.getIotId());
                            intent8.putExtra("appKey", "");
                            HomeTabFragment.this.startActivity(intent8);
                            break;
                        case R.id.tab_setting /* 2131297731 */:
                            if (SharePreferenceManager.getInstance().getIsRouter(deviceInfoBean.getIotId()) == 1) {
                                Intent intent9 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) BleRouterSettingActivity.class);
                                Bundle bundle4 = new Bundle();
                                if (HomeTabFragment.this.mAdapter.getData().get(i).getData().size() >= 1 && HomeTabFragment.this.mAdapter.getData().get(i).getData().get(0) != null) {
                                    bundle4.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, HomeTabFragment.this.mAdapter.getData().get(i).getData().get(0));
                                }
                                if (HomeTabFragment.this.mAdapter.getData().get(i).getData().size() >= 2 && HomeTabFragment.this.mAdapter.getData().get(i).getData().get(1) != null) {
                                    bundle4.putSerializable("device1", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(1));
                                }
                                if (HomeTabFragment.this.mAdapter.getData().get(i).getData().size() >= 3 && HomeTabFragment.this.mAdapter.getData().get(i).getData().get(2) != null) {
                                    bundle4.putSerializable("device2", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(2));
                                }
                                if (HomeTabFragment.this.mAdapter.getData().get(i).getData().size() >= 4 && HomeTabFragment.this.mAdapter.getData().get(i).getData().get(3) != null) {
                                    bundle4.putSerializable("device3", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(3));
                                }
                                if (HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean() != null) {
                                    bundle4.putSerializable("nvrDevice", HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean());
                                }
                                intent9.putExtras(bundle4);
                                HomeTabFragment.this.startActivity(intent9);
                            } else {
                                Intent intent10 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) SettingsActivity.class);
                                Bundle bundle5 = new Bundle();
                                if (HomeTabFragment.this.mAdapter.getData().get(i).getData().size() >= 1 && HomeTabFragment.this.mAdapter.getData().get(i).getData().get(0) != null) {
                                    bundle5.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, HomeTabFragment.this.mAdapter.getData().get(i).getData().get(0));
                                }
                                if (HomeTabFragment.this.mAdapter.getData().get(i).getData().size() >= 2 && HomeTabFragment.this.mAdapter.getData().get(i).getData().get(1) != null) {
                                    bundle5.putSerializable("device1", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(1));
                                }
                                if (HomeTabFragment.this.mAdapter.getData().get(i).getData().size() >= 3 && HomeTabFragment.this.mAdapter.getData().get(i).getData().get(2) != null) {
                                    bundle5.putSerializable("device2", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(2));
                                }
                                if (HomeTabFragment.this.mAdapter.getData().get(i).getData().size() >= 4 && HomeTabFragment.this.mAdapter.getData().get(i).getData().get(3) != null) {
                                    bundle5.putSerializable("device3", HomeTabFragment.this.mAdapter.getData().get(i).getData().get(3));
                                }
                                if (HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean() != null) {
                                    bundle5.putSerializable("nvrDevice", HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean());
                                }
                                intent10.putExtras(bundle5);
                                HomeTabFragment.this.startActivity(intent10);
                            }
                            break;
                        case R.id.tab_share /* 2131297732 */:
                            if (HomeTabFragment.this.mAdapter.getData().get(i).getData().get(0).getOwned() == 1) {
                                HomeTabFragment.this.shareDialog2 = ((ShareDialog.Builder) new ShareDialog.Builder().view(R.layout.dialog_input2).leftBtnText(HomeTabFragment.this.getString(R.string.cancel)).rightBtnText(HomeTabFragment.this.getString(R.string.share_immediately)).clickRight(new View.OnClickListener() { // from class: fragment.HomeTabFragment.10.2
                                    @Override // android.view.View.OnClickListener
                                    public void onClick(View view3) {
                                        if (HomeTabFragment.this.shareDialog2.getContent() != null) {
                                            if (HomeTabFragment.this.shareDialog2.getMode() != 0 || SystemUtil.isPhone(HomeTabFragment.this.shareDialog2.getContent())) {
                                                if (HomeTabFragment.this.shareDialog2.getMode() != 1 || SystemUtil.isEmail(HomeTabFragment.this.shareDialog2.getContent())) {
                                                    DeviceInfoBean deviceInfoBean2 = (DeviceInfoBean) HomeTabFragment.this.shareDialog2.getExtra();
                                                    ArrayList arrayList = new ArrayList();
                                                    if (HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean() != null) {
                                                        arrayList.add(HomeTabFragment.this.mAdapter.getData().get(i).getCellDeviceInfoBean().getIotId());
                                                    }
                                                    for (int i5 = 0; i5 < HomeTabFragment.this.mAdapter.getData().get(i).getData().size(); i5++) {
                                                        arrayList.add(HomeTabFragment.this.mAdapter.getData().get(i).getData().get(i5).getIotId());
                                                    }
                                                    Log.e("分享设备", "" + arrayList.size());
                                                    HomeTabFragment.this.shareDevice(HomeTabFragment.this.shareDialog2.getContent(), deviceInfoBean2, arrayList, HomeTabFragment.this.shareDialog2.getMode() == 0 ? HomeTabFragment.this.shareDialog2.getDistinct() : null);
                                                    return;
                                                }
                                                ToastUtils.toast(HomeTabFragment.this.getActivity(), HomeTabFragment.this.getString(R.string.email_invalid));
                                                return;
                                            }
                                            ToastUtils.toast(HomeTabFragment.this.getActivity(), HomeTabFragment.this.getString(R.string.phone_invalid));
                                            return;
                                        }
                                        Toast.makeText(HomeTabFragment.this.getActivity(), R.string.share_user_null_error, 0).show();
                                    }
                                })).create();
                                HomeTabFragment.this.shareDialog2.setOnShareClick(new ShareDialog.OnShareClickListener() { // from class: fragment.HomeTabFragment.10.3
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
                                HomeTabFragment.this.shareDialog2.setExtra(deviceInfoBean);
                                HomeTabFragment.this.shareDialog2.show(HomeTabFragment.this.getFragmentManager(), "");
                            }
                            break;
                        case R.id.tab_yun_service /* 2131297735 */:
                            if (((SharePreferenceManager.getInstance().getPageControlEx(deviceInfoBean.getIotId()) & 524288) >> 19) == 1) {
                                if (SharePreferenceManager.getInstance().getIccId1(deviceInfoBean.getIotId()).equals("") && SharePreferenceManager.getInstance().getIccId2(deviceInfoBean.getIotId()).equals("")) {
                                    Intent intent11 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) PayYunServiceActivity2.class);
                                    intent11.putExtra("iotId", deviceInfoBean.getIotId());
                                    intent11.putExtra(AlinkConstants.KEY_DN, deviceInfoBean.getDeviceName());
                                    intent11.putExtra(AlinkConstants.KEY_PK, deviceInfoBean.getProductKey());
                                    HomeTabFragment.this.startActivity(intent11);
                                } else {
                                    Intent intent12 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) Net4GSwitchActivity.class);
                                    Bundle bundle6 = new Bundle();
                                    new DeviceInfoBean();
                                    bundle6.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, deviceInfoBean);
                                    intent12.putExtras(bundle6);
                                    HomeTabFragment.this.startActivity(intent12);
                                }
                            } else {
                                Intent intent13 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) PayYunServiceActivity2.class);
                                intent13.putExtra("iotId", deviceInfoBean.getIotId());
                                intent13.putExtra(AlinkConstants.KEY_DN, deviceInfoBean.getDeviceName());
                                intent13.putExtra(AlinkConstants.KEY_PK, deviceInfoBean.getProductKey());
                                HomeTabFragment.this.startActivity(intent13);
                            }
                            break;
                        case R.id.tv_to_receive /* 2131298000 */:
                            new BaseDialog.Builder().view(R.layout.dialog_common).content(HomeTabFragment.this.getString(R.string.receive_device_hint, deviceInfoBean.getInitiatorAlias())).leftBtnText(HomeTabFragment.this.getString(R.string.cancel)).rightBtnText(HomeTabFragment.this.getString(R.string.receive)).clickRight(new View.OnClickListener() { // from class: fragment.HomeTabFragment.10.4
                                @Override // android.view.View.OnClickListener
                                public void onClick(View view3) {
                                    ArrayList arrayList = new ArrayList();
                                    for (int i5 = 0; i5 < HomeTabFragment.this.shareDeviceInfoBeanList.size(); i5++) {
                                        Log.e("接收设备状态", "" + HomeTabFragment.this.shareDeviceInfoBeanList.get(i5).getStatus());
                                        if (HomeTabFragment.this.shareDeviceInfoBeanList.get(i5).getStatus() == -1) {
                                            arrayList.add(HomeTabFragment.this.shareDeviceInfoBeanList.get(i5).getRecordId());
                                        }
                                    }
                                    Log.e("接收设备", "" + arrayList.size());
                                    HomeTabFragment.this.disposeShareDevice(1, arrayList);
                                }
                            }).canCancel(false).create().show(HomeTabFragment.this.getActivity().getSupportFragmentManager(), "");
                            break;
                    }
                    return;
                }
                Toast.makeText(HomeTabFragment.this.getContext(), HomeTabFragment.this.getString(R.string.connect_failed), 0).show();
            }
        });
        this.mAdapter.setOfflineBtnClick(new DeviceAdapter.OfflineBtnClick() { // from class: fragment.HomeTabFragment.11
            @Override // adapter.DeviceAdapter.OfflineBtnClick
            public void OnClick(String str, String str2, String str3, String str4, boolean z, DeviceInfoBean deviceInfoBean) {
                HomeTabFragment.this.showTip(str, str2, str3, str4, z, deviceInfoBean);
            }
        });
        this.srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: fragment.HomeTabFragment.12
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                if (LoginBusiness.isLogin()) {
                    HomeTabFragment.this.startTask(1);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAlarmParam(final DeviceInfoBean deviceInfoBean, final int i) {
        final int i2 = SharePreferenceManager.getInstance().getAlarmSwitch(deviceInfoBean.getIotId()) == 0 ? 1 : 0;
        HashMap map = new HashMap();
        map.put(Constants.ALARM_SWITCH_MODEL_NAME, Integer.valueOf(i2));
        IPCManager.getInstance().getDevice(deviceInfoBean.getIotId()).setProperties(map, new IPanelCallback() { // from class: fragment.HomeTabFragment.13
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.13.1
                            @Override // java.lang.Runnable
                            public void run() {
                                FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                                if (activity2 == null || activity2.isFinishing()) {
                                    return;
                                }
                                Toast.makeText(HomeTabFragment.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    } else {
                        HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.13.2
                            @Override // java.lang.Runnable
                            public void run() {
                                FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                                if (activity2 == null || activity2.isFinishing() || deviceInfoBean.getIotId() == null || deviceInfoBean == null) {
                                    return;
                                }
                                SharePreferenceManager.getInstance().setAlarmSwitch(deviceInfoBean.getIotId(), i2);
                                HomeTabFragment.this.mAdapter.refreshTabAlarm(i);
                                Toast.makeText(HomeTabFragment.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }

    /* JADX INFO: renamed from: fragment.HomeTabFragment$14, reason: invalid class name */
    class AnonymousClass14 implements ISetCallback {
        final /* synthetic */ DeviceInfoBean val$deviceInfoBean;
        final /* synthetic */ int val$pos;

        @Override // tools.ISetCallback
        public void onFailed() {
        }

        AnonymousClass14(DeviceInfoBean deviceInfoBean, int i) {
            this.val$deviceInfoBean = deviceInfoBean;
            this.val$pos = i;
        }

        @Override // tools.ISetCallback
        public void onSucceed() {
            IPCManager.getInstance().getDevice(this.val$deviceInfoBean.getIotId()).getProperties(new IPanelCallback() { // from class: fragment.HomeTabFragment.14.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj) {
                    if (!z || obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code") && object.getInteger("code").intValue() == 200 && object.containsKey("data")) {
                        try {
                            com.alibaba.fastjson.JSONObject jSONObject = object.getJSONObject("data");
                            if (jSONObject.containsKey(Constants.ALARM_SWITCH_MODEL_NAME)) {
                                com.alibaba.fastjson.JSONObject jSONObject2 = jSONObject.getJSONObject(Constants.ALARM_SWITCH_MODEL_NAME);
                                if (jSONObject2.containsKey("value")) {
                                    int iIntValue = jSONObject2.getInteger("value").intValue();
                                    if (iIntValue != SharePreferenceManager.getInstance().getAlarmSwitch(AnonymousClass14.this.val$deviceInfoBean.getIotId())) {
                                        SharePreferenceManager.getInstance().setAlarmSwitch(AnonymousClass14.this.val$deviceInfoBean.getIotId(), iIntValue);
                                    }
                                    HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.14.1.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                                            if (activity2 == null || activity2.isFinishing()) {
                                                return;
                                            }
                                            SharePreferenceManager.getInstance().getAlarmSwitch(AnonymousClass14.this.val$deviceInfoBean.getIotId());
                                            HomeTabFragment.this.mAdapter.refreshTabAlarm(AnonymousClass14.this.val$pos);
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getAlarmParam(DeviceInfoBean deviceInfoBean, int i) {
        SettingsCtrl.getInstance().initCallBack(deviceInfoBean.getIotId(), true, new AnonymousClass14(deviceInfoBean, i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getDeviceFailed(String str) {
        if (this.mAdapter.getData().size() == 0) {
            this.rlEmpty.setVisibility(0);
        }
        if (this.srl.isRefreshing()) {
            this.srl.setRefreshing(false);
        }
        Toast.makeText(getActivity(), str, 0).show();
    }

    private void notifyDeviceUpdate() {
        ArrayList arrayList = new ArrayList();
        Iterator<DeviceInfoBeans> it = this.mAdapter.getData().iterator();
        while (it.hasNext()) {
            arrayList.addAll(it.next().getData());
        }
        EventBus.getDefault().postSticky(new DeviceInfoBeans(arrayList));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getDeviceSucceed(final List<DeviceInfoBean> list) {
        SwipeRefreshLayout swipeRefreshLayout = this.srl;
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            this.srl.setRefreshing(false);
        }
        if (list == null || list.size() == 0) {
            this.rlEmpty.setVisibility(0);
            this.mAdapter.getData().clear();
            DeviceAdapter deviceAdapter = this.mAdapter;
            deviceAdapter.replaceData(deviceAdapter.getData());
            if (!AppConfig.isChina) {
                getList();
            }
            this.mAdapter.notifyDataSetChanged();
            notifyDeviceUpdate();
            return;
        }
        new Handler().post(new Runnable() { // from class: fragment.HomeTabFragment.15
            @Override // java.lang.Runnable
            public void run() {
                if (SharePreferenceManager.getInstance().getADSwitch() != 0 || MyAccountTabFragment.getUserNick().equals("18870116190") || MyAccountTabFragment.getUserNick().equals(HomeTabFragment.this.getContext().getString(R.string.username_not_obtained))) {
                    return;
                }
                SharePreferenceManager.getInstance().setADSwitch(1);
            }
        });
        this.rlEmpty.setVisibility(8);
        this.deviceInfoBeansList = new ArrayList();
        this.deviceInfoBeansList.clear();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList2.clear();
        ArrayList arrayList3 = arrayList;
        for (final int i = 0; i < list.size(); i++) {
            if (SharePreferenceManager.getInstance().getLowPower(list.get(i).getIotId()) == 1 && list.get(i).getStatus() != 1) {
                Log.e("低功耗离线时间" + list.get(i).getIotId(), "" + list.get(i).getStatus());
                HashMap map = new HashMap();
                map.put("iotId", list.get(i).getIotId());
                new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/thing/status/get").setScheme(Scheme.HTTPS).setApiVersion("1.0.4").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: fragment.HomeTabFragment.16
                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                        try {
                            if (((JSONObject) ioTResponse.getData()).get("status").toString().equals("3")) {
                                String string = ((JSONObject) ioTResponse.getData()).get("time").toString();
                                TimeUtil.TimeStamp2Date(string);
                                if (string.isEmpty() || string == null) {
                                    return;
                                }
                                if (System.currentTimeMillis() - Long.parseLong(string) < 15) {
                                    ((DeviceInfoBean) list.get(i)).setStatus(1);
                                    Log.e("修改低功耗离线时间" + ((DeviceInfoBean) list.get(i)).getIotId(), "" + ((DeviceInfoBean) list.get(i)).getStatus());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            if (list.size() <= 50 && list.get(i).getStatus() == 1) {
                HashMap map2 = new HashMap();
                map2.put(Constants.LowPowerWakeUp, 1);
                IPCManager.getInstance().getDevice(list.get(i).getIotId()).setProperties(map2, new IPanelCallback() { // from class: fragment.HomeTabFragment.17
                    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                    public void onComplete(boolean z, @Nullable Object obj) {
                        Log.e("唤醒", obj + "" + z);
                    }
                });
            }
            if ("GATEWAY".equals(list.get(i).getNodeType())) {
                for (int i2 = 0; i2 < this.nvrDevicesList.size(); i2++) {
                    if (this.nvrDevicesList.get(i2) != null && this.nvrDevicesList.get(i2).getNvrIot() != null && this.nvrDevicesList.get(i2).getNvrIot().equals(list.get(i).getIotId())) {
                        if (this.nvrDevicesList.get(i2).getData().size() == 0) {
                            this.nvrDevicesList.get(i2).getData().add(list.get(i));
                        }
                        DeviceInfoBeans deviceInfoBeans = this.nvrDevicesList.get(i2);
                        deviceInfoBeans.setCellDeviceInfoBean(list.get(i));
                        this.deviceInfoBeansList.add(deviceInfoBeans);
                    }
                    List<DeviceInfoBeans> list2 = this.nvrDevicesList;
                    if (list2 != null && list2.get(i2).getData() != null) {
                        for (int i3 = 0; i3 < this.nvrDevicesList.get(i2).getData().size(); i3++) {
                            if (this.nvrDevicesList.get(i2).getData().get(i3).getDeviceName().contains(list.get(i).getDeviceName())) {
                                DeviceInfoBeans deviceInfoBeans2 = this.nvrDevicesList.get(i2);
                                deviceInfoBeans2.setCellDeviceInfoBean(list.get(i));
                                if (!this.deviceInfoBeansList.contains(deviceInfoBeans2)) {
                                    this.deviceInfoBeansList.add(deviceInfoBeans2);
                                    SettingsCtrl.getInstance().getProperties(this.nvrDevicesList.get(i2).getData().get(i3).getIotId(), new MyCallback() { // from class: fragment.HomeTabFragment.18
                                        @Override // tools.MyCallback
                                        public void onComplete(boolean z) {
                                        }
                                    });
                                    if (SharePreferenceManager.getInstance().getLowPower(this.nvrDevicesList.get(i2).getData().get(i3).getIotId()) == 1 && this.nvrDevicesList.get(i2).getData().get(i3).getStatus() == 1) {
                                        arrayList2.add(this.nvrDevicesList.get(i2).getData().get(i3));
                                        Log.e("低功耗添加nvr", "" + this.nvrDevicesList.get(i2).getData().get(i3).getIotId());
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (SharePreferenceManager.getInstance().getLowPower(list.get(i).getIotId()) == 1 && list.get(i).getStatus() == 1) {
                    arrayList2.add(list.get(i));
                    Log.e("低功耗添加", "" + list.get(i).getIotId());
                }
                arrayList3.add(list.get(i));
                this.deviceInfoBeansList.add(new DeviceInfoBeans(arrayList3));
                arrayList3 = new ArrayList();
            }
        }
        AppConfig.LOWPOWER_LIST.addAll(arrayList2);
        Log.e("低功耗添加完成", "" + AppConfig.LOWPOWER_LIST.size());
        for (int i4 = 0; i4 < this.deviceInfoBeansList.size(); i4++) {
            for (int i5 = 0; i5 < this.deviceInfoBeansList.get(i4).getData().size(); i5++) {
                boolean z = this.deviceInfoBeansList.get(i4).getCellDeviceInfoBean() != null && this.deviceInfoBeansList.get(i4).getCellDeviceInfoBean().getStatus() == 1;
                if (this.deviceInfoBeansList.get(i4).getData().get(i5).getStatus() == 1) {
                    z = true;
                }
                if (z) {
                    if (this.deviceInfoBeansList.get(i4).getCellDeviceInfoBean() != null && this.deviceInfoBeansList.get(i4).getCellDeviceInfoBean().getStatus() != 1) {
                        this.deviceInfoBeansList.get(i4).getCellDeviceInfoBean().setStatus(1);
                        Log.e("在线修改  ", "" + this.deviceInfoBeansList.get(i4).getCellDeviceInfoBean().getStatus());
                    }
                    for (int i6 = 0; i6 < this.deviceInfoBeansList.get(i4).getData().size(); i6++) {
                        if (this.deviceInfoBeansList.get(i4).getData().get(i6).getStatus() != 1) {
                            this.deviceInfoBeansList.get(i4).getData().get(i6).setStatus(1);
                            Log.e("在线修改  2", "" + this.deviceInfoBeansList.get(i4).getData().get(i6).getStatus());
                        }
                    }
                }
            }
        }
        AppConfig.isRefresh = true;
        this.deviceInfoBeansList = SortingByOnlineStrategy(this.deviceInfoBeansList);
        this.mAdapter.replaceData(this.deviceInfoBeansList);
        this.mAdapter.notifyDataSetChanged();
        if (!AppConfig.isChina) {
            getList();
        }
        SharePreferenceManager.getInstance().setAllDeviceList(JSON.toJSONString(this.deviceInfoBeansList));
        notifyDeviceUpdate();
        AppConfig.isRefresh = true;
        ArrayList<String> arrayList4 = new ArrayList<>();
        for (DeviceInfoBean deviceInfoBean : list) {
            arrayList4.add(deviceInfoBean.getProductKey() + deviceInfoBean.getDeviceName());
        }
        this.mBundle.putStringArrayList("deviceList", arrayList4);
        int i7 = this.mGetDeviceIntent;
        if (i7 == 2) {
            dismissProgressDialog();
            Toast.makeText(getActivity(), getString(R.string.receive_share_succeed), 0).show();
        } else if (i7 == 3) {
            dismissProgressDialog();
            Toast.makeText(getActivity(), getString(R.string.refuse_share_succeed), 0).show();
        } else if (i7 == 1) {
            dismissProgressDialog();
        }
        this.mGetDeviceIntent = 0;
        for (int i8 = this.firstVisibleItemPosition; i8 < this.firstVisibleItemPosition + this.alarmAdvanceLoadSize; i8++) {
            try {
                getAlarmParam(this.mAdapter.getData().get(i8).getData().get(0), i8);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        AppConfig.isRefresh = true;
    }

    private void getList() {
        String userNick = MyAccountTabFragment.getUserNick();
        if (userNick == null || userNick.equals(getString(R.string.username_not_obtained))) {
            return;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("uid", userNick);
        } catch (Exception e) {
            e.printStackTrace();
        }
        okHttpClient.newCall(new Request.Builder().url("https://traffic.secueye.app/api/app/get/record").post(RequestBody.create(MediaType.parse(HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON), jSONObject.toString())).build()).enqueue(new Callback() { // from class: fragment.HomeTabFragment.19
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                iOException.printStackTrace();
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                int iIntValue;
                if (response.isSuccessful()) {
                    String strString = response.body().string();
                    Log.e(com.taobao.accs.common.Constants.KEY_IMEI, "" + strString);
                    com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(strString);
                    if (object.containsKey("code") && (iIntValue = object.getInteger("code").intValue()) == iIntValue) {
                        List listFilterDevicesByUniqueImei = HomeTabFragment.filterDevicesByUniqueImei(JSON.parseArray(com.alibaba.fastjson.JSONObject.parseObject(object.getString("data")).getJSONArray(AlinkConstants.KEY_LIST).toString(), DeviceInfoBeans.class));
                        SharePreferenceManager.getInstance().setAllIMEIList(JSON.toJSONString(listFilterDevicesByUniqueImei));
                        HomeTabFragment.this.deviceInfoBeansList.addAll(listFilterDevicesByUniqueImei);
                        HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.19.1
                            @Override // java.lang.Runnable
                            public void run() {
                                FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                                if (activity2 == null || activity2.isFinishing()) {
                                    return;
                                }
                                if (HomeTabFragment.this.deviceInfoBeansList.size() != 0) {
                                    HomeTabFragment.this.rlEmpty.setVisibility(8);
                                }
                                HomeTabFragment.this.mAdapter.replaceData(HomeTabFragment.this.deviceInfoBeansList);
                            }
                        });
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<DeviceInfoBeans> filterDevicesByUniqueImei(List<DeviceInfoBeans> list) {
        String str;
        String str2;
        HashSet hashSet = new HashSet();
        ArrayList arrayList = new ArrayList();
        for (DeviceInfoBeans deviceInfoBeans : list) {
            if (deviceInfoBeans.imei != null && (str2 = deviceInfoBeans.imei) != null && !str2.isEmpty() && !hashSet.contains(str2)) {
                hashSet.add(str2);
                arrayList.add(deviceInfoBeans);
            }
            if (deviceInfoBeans.iccid != null && (str = deviceInfoBeans.iccid) != null && !str.isEmpty() && !hashSet.contains(str)) {
                hashSet.add(str);
                arrayList.add(deviceInfoBeans);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getShareDevice(final List<DeviceInfoBean> list, final int i) {
        if (i == 1) {
            List<ShareDeviceInfoBean> list2 = this.deviceInfoBeanListShareAll;
            if (list2 == null) {
                this.deviceInfoBeanListShareAll = new ArrayList();
            } else {
                list2.clear();
            }
        }
        HashMap map = new HashMap();
        map.put("thingType", TmpConstant.GROUP_CLOUD_ROLE_DEVICE);
        map.put(AlinkConstants.KEY_PAGE_NO, Integer.valueOf(i));
        map.put(AlinkConstants.KEY_PAGE_SIZE, 100);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath(AlinkConstants.HTTP_PATH_SHARE_LIST).setScheme(Scheme.HTTPS).setApiVersion(AlinkConstants.HTTP_PATH_GET_CIPHER_VERSION).setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: fragment.HomeTabFragment.20
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, HomeTabFragment.this.TAG, "onFailure");
                HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.20.1
                    @Override // java.lang.Runnable
                    public void run() {
                        FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                        if (activity2 == null || activity2.isFinishing()) {
                            return;
                        }
                        HomeTabFragment.this.getDeviceFailed(HomeTabFragment.this.getString(R.string.query_device_failed));
                        HomeTabFragment.this.isTasking = false;
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.20.2
                        @Override // java.lang.Runnable
                        public void run() {
                            FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            HomeTabFragment.this.getDeviceFailed(HomeTabFragment.this.getString(R.string.query_device_failed));
                            HomeTabFragment.this.isTasking = false;
                        }
                    });
                    return;
                }
                Object data = ioTResponse.getData();
                if (data == null || !(data instanceof JSONObject)) {
                    return;
                }
                try {
                    HomeTabFragment.this.shareDeviceInfoBeanList = JSON.parseArray(((JSONObject) data).getJSONArray("data").toString(), ShareDeviceInfoBean.class);
                    Log.e("接收设备数量", "" + HomeTabFragment.this.shareDeviceInfoBeanList.size());
                    if (HomeTabFragment.this.deviceInfoBeanListShareAll == null) {
                        HomeTabFragment.this.deviceInfoBeanListShareAll = new ArrayList();
                    }
                    if (HomeTabFragment.this.shareDeviceInfoBeanList.size() != 0) {
                        HomeTabFragment.this.deviceInfoBeanListShareAll.add(HomeTabFragment.this.shareDeviceInfoBeanList.get(0));
                    }
                    if (HomeTabFragment.this.shareDeviceInfoBeanList.size() >= 100) {
                        HomeTabFragment.this.getShareDevice(list, i + 1);
                        return;
                    }
                    ArrayList arrayList = new ArrayList();
                    for (ShareDeviceInfoBean shareDeviceInfoBean : HomeTabFragment.this.deviceInfoBeanListShareAll) {
                        if (shareDeviceInfoBean.getIsReceiver() == 1 && shareDeviceInfoBean.getStatus() == -1) {
                            arrayList.add(shareDeviceInfoBean.toDeviceInfoBean());
                        }
                    }
                    if (arrayList.size() == 0) {
                        HomeTabFragment.this.clearShareNoticeList();
                    }
                    list.addAll(arrayList);
                    HomeTabFragment.this.isTasking = false;
                    HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.20.3
                        @Override // java.lang.Runnable
                        public void run() {
                            FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            HomeTabFragment.this.setNvrDeviceList(list);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearShareNoticeList() {
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/uc/clearShareNoticeList").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(new HashMap()).build(), new IoTCallback() { // from class: fragment.HomeTabFragment.21
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, HomeTabFragment.this.TAG, "onFailure");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                Log.e(HomeTabFragment.this.TAG, "clearShareNoticeList onResponse: code: " + code);
                String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    Log.e(HomeTabFragment.this.TAG, localizedMsg);
                } else {
                    Log.e(HomeTabFragment.this.TAG, "清除分享记录成功");
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setNvrDeviceList(List<DeviceInfoBean> list) {
        List<DeviceInfoBeans> list2 = this.nvrDevicesList;
        if (list2 == null) {
            this.nvrDevicesList = new ArrayList();
        } else {
            list2.clear();
        }
        List<Integer> list3 = this.intList;
        if (list3 == null) {
            this.intList = new ArrayList();
        } else {
            list3.clear();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(list);
        boolean z = false;
        for (int i = 0; i < arrayList.size(); i++) {
            if ("GATEWAY".equals(arrayList.get(i).getNodeType())) {
                this.intList.add(Integer.valueOf(i));
                z = true;
            }
        }
        if (z) {
            Iterator<Integer> it = this.intList.iterator();
            while (it.hasNext()) {
                int iIntValue = it.next().intValue();
                HashMap map = new HashMap();
                map.put("iotId", arrayList.get(iIntValue).getIotId());
                map.put(AlinkConstants.KEY_PAGE_NO, 1);
                map.put(AlinkConstants.KEY_PAGE_SIZE, 6);
                new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/subdevices/list").setScheme(Scheme.HTTP).setApiVersion("1.0.6").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass22(arrayList, iIntValue));
            }
            return;
        }
        getDeviceSucceed(arrayList);
    }

    /* JADX INFO: renamed from: fragment.HomeTabFragment$22, reason: invalid class name */
    class AnonymousClass22 implements IoTCallback {
        final /* synthetic */ List val$finalList;
        final /* synthetic */ int val$i;

        AnonymousClass22(List list, int i) {
            this.val$finalList = list;
            this.val$i = i;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, HomeTabFragment.this.TAG, "onFailure");
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            int code = ioTResponse.getCode();
            ioTResponse.getLocalizedMsg();
            if (code == 200) {
                final List array = null;
                try {
                    array = JSON.parseArray(((JSONObject) ioTResponse.getData()).getString("data"), DeviceInfoBean.class);
                    Log.d(HomeTabFragment.this.TAG, "onResponse: puppet:deviceInfoBeans ==== " + array.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < array.size(); i++) {
                    ((DeviceInfoBean) array.get(i)).setOwned(((DeviceInfoBean) this.val$finalList.get(this.val$i)).getOwned());
                }
                if (array.size() == 2) {
                    SettingsCtrl.getInstance().getProperties(((DeviceInfoBean) this.val$finalList.get(this.val$i)).getIotId(), new MyCallback() { // from class: fragment.HomeTabFragment.22.1
                        /* JADX WARN: Removed duplicated region for block: B:29:0x0122  */
                        @Override // tools.MyCallback
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                            To view partially-correct add '--show-bad-code' argument
                        */
                        public void onComplete(boolean r6) {
                            /*
                                Method dump skipped, instruction units count: 535
                                To view this dump add '--comments-level debug' option
                            */
                            throw new UnsupportedOperationException("Method not decompiled: fragment.HomeTabFragment.AnonymousClass22.AnonymousClass1.onComplete(boolean):void");
                        }
                    });
                    return;
                }
                if (array.size() == 3) {
                    Log.e("子通道数量", "" + array.size());
                    SettingsCtrl.getInstance().getProperties(((DeviceInfoBean) this.val$finalList.get(this.val$i)).getIotId(), new MyCallback() { // from class: fragment.HomeTabFragment.22.2
                        /* JADX WARN: Removed duplicated region for block: B:61:0x0254  */
                        @Override // tools.MyCallback
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                            To view partially-correct add '--show-bad-code' argument
                        */
                        public void onComplete(boolean r9) {
                            /*
                                Method dump skipped, instruction units count: 849
                                To view this dump add '--comments-level debug' option
                            */
                            throw new UnsupportedOperationException("Method not decompiled: fragment.HomeTabFragment.AnonymousClass22.AnonymousClass2.onComplete(boolean):void");
                        }
                    });
                    return;
                }
                if (array.size() == 4) {
                    Log.e("子通道数量", "" + array.size());
                    SettingsCtrl.getInstance().getProperties(((DeviceInfoBean) this.val$finalList.get(this.val$i)).getIotId(), new MyCallback() { // from class: fragment.HomeTabFragment.22.3
                        /* JADX WARN: Removed duplicated region for block: B:71:0x02cc  */
                        @Override // tools.MyCallback
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                            To view partially-correct add '--show-bad-code' argument
                        */
                        public void onComplete(boolean r11) {
                            /*
                                Method dump skipped, instruction units count: 977
                                To view this dump add '--comments-level debug' option
                            */
                            throw new UnsupportedOperationException("Method not decompiled: fragment.HomeTabFragment.AnonymousClass22.AnonymousClass3.onComplete(boolean):void");
                        }
                    });
                    return;
                }
                DeviceInfoBeans deviceInfoBeans = new DeviceInfoBeans(array);
                deviceInfoBeans.setNvrIot(((DeviceInfoBean) this.val$finalList.get(this.val$i)).getIotId());
                HomeTabFragment.this.nvrDevicesList.add(deviceInfoBeans);
                if (HomeTabFragment.this.nvrDevicesList.size() == HomeTabFragment.this.intList.size()) {
                    HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.22.4
                        @Override // java.lang.Runnable
                        public void run() {
                            HomeTabFragment.this.getDeviceSucceed(AnonymousClass22.this.val$finalList);
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startTask(final int i) {
        if (this.threadPoolExecutor == null) {
            this.threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
        }
        if (this.isTasking) {
            return;
        }
        this.isTasking = true;
        this.threadPoolExecutor.execute(new Runnable() { // from class: fragment.HomeTabFragment.23
            @Override // java.lang.Runnable
            public void run() {
                HomeTabFragment.this.getDevice(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getDevice(final int i) {
        if (i == 1) {
            List<DeviceInfoBean> list = this.deviceInfoBeanListAll;
            if (list == null) {
                this.deviceInfoBeanListAll = new ArrayList();
            } else {
                list.clear();
            }
            List<DeviceInfoBeans> list2 = this.deviceInfoBeansList;
            if (list2 == null) {
                this.deviceInfoBeansList = new ArrayList();
            } else {
                list2.clear();
            }
        }
        HashMap map = new HashMap();
        map.put("thingType", TmpConstant.GROUP_CLOUD_ROLE_DEVICE);
        map.put(AlinkConstants.KEY_PAGE_NO, Integer.valueOf(i));
        map.put(AlinkConstants.KEY_PAGE_SIZE, 100);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/uc/listBindingByAccount").setScheme(Scheme.HTTPS).setApiVersion(AlinkConstants.HTTP_PATH_GET_CIPHER_VERSION).setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: fragment.HomeTabFragment.24
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, HomeTabFragment.this.TAG, "onFailure");
                HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.24.1
                    @Override // java.lang.Runnable
                    public void run() {
                        FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                        if (activity2 == null || activity2.isFinishing()) {
                            return;
                        }
                        HomeTabFragment.this.getDeviceFailed(HomeTabFragment.this.getString(R.string.query_device_failed));
                        HomeTabFragment.this.isTasking = false;
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                final int code = ioTResponse.getCode();
                Log.e(HomeTabFragment.this.TAG, "getDevice onResponse: code: " + code);
                ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.24.2
                        @Override // java.lang.Runnable
                        public void run() {
                            FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            if (code == 401) {
                                HomeTabFragment.this.getDeviceFailed(HomeTabFragment.this.getString(R.string.account_squeezed));
                            } else {
                                HomeTabFragment.this.getDeviceFailed(HomeTabFragment.this.getString(R.string.query_device_failed));
                            }
                            HomeTabFragment.this.isTasking = false;
                        }
                    });
                    return;
                }
                Object data = ioTResponse.getData();
                if (data instanceof JSONObject) {
                    try {
                        List array = JSON.parseArray(((JSONObject) data).getJSONArray("data").toString(), DeviceInfoBean.class);
                        if (HomeTabFragment.this.deviceInfoBeanListAll == null) {
                            HomeTabFragment.this.deviceInfoBeanListAll = new ArrayList();
                        }
                        HomeTabFragment.this.deviceInfoBeanListAll.addAll(array);
                        if (array.size() >= 100) {
                            HomeTabFragment.this.getDevice(i + 1);
                            return;
                        }
                        Iterator it = HomeTabFragment.this.deviceInfoBeanListAll.iterator();
                        while (it.hasNext()) {
                            if ("NET_OTHER".equals(((DeviceInfoBean) it.next()).getNetType())) {
                                it.remove();
                            }
                        }
                        LogEx.e(true, HomeTabFragment.this.TAG, HomeTabFragment.this.deviceInfoBeanListAll.size() + "");
                        HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.24.3
                            @Override // java.lang.Runnable
                            public void run() {
                                FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                                if (activity2 == null || activity2.isFinishing()) {
                                    return;
                                }
                                HomeTabFragment.this.getShareDevice(HomeTabFragment.this.deviceInfoBeanListAll, 1);
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
    public void shareDevice(String str, DeviceInfoBean deviceInfoBean, List<String> list, String str2) {
        HashMap map = new HashMap();
        map.put(AlinkConstants.KEY_ACCOUNT_ATTR, str);
        if (TextUtils.isEmpty(str2)) {
            map.put(AlinkConstants.KEY_ACCOUNT_ATTR_TYPE, DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_EMAIL);
        } else {
            map.put(AlinkConstants.KEY_ACCOUNT_ATTR_TYPE, DeviceShareManager.SHARE_DEVICE_ACCOUNT_ATTRTYPE_MOBILE);
            map.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, str2);
        }
        map.put("iotIdList", list);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath(AlinkConstants.HTTP_PATH_DEVICE_SHARE).setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass25(str, deviceInfoBean));
    }

    /* JADX INFO: renamed from: fragment.HomeTabFragment$25, reason: invalid class name */
    class AnonymousClass25 implements IoTCallback {
        final /* synthetic */ String val$accountAttr;
        final /* synthetic */ DeviceInfoBean val$deviceInfoBean;

        AnonymousClass25(String str, DeviceInfoBean deviceInfoBean) {
            this.val$accountAttr = str;
            this.val$deviceInfoBean = deviceInfoBean;
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, HomeTabFragment.this.TAG, "onFailure");
            Toast.makeText(HomeTabFragment.this.getActivity(), R.string.share_failed, 0).show();
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            final int code = ioTResponse.getCode();
            Log.e(HomeTabFragment.this.TAG, "shareDevice onResponse: code: " + code);
            final String localizedMsg = ioTResponse.getLocalizedMsg();
            if (code != 200) {
                HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.25.1
                    @Override // java.lang.Runnable
                    public void run() {
                        FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                        if (activity2 == null || activity2.isFinishing()) {
                            return;
                        }
                        if (code == 2077) {
                            HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.25.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    FragmentActivity activity3 = HomeTabFragment.this.getActivity();
                                    if (activity3 == null || activity3.isFinishing()) {
                                        return;
                                    }
                                    DialogUtil.showTipsConfirmDiaLog(HomeTabFragment.this.getActivity(), HomeTabFragment.this.getString(R.string.sharing_failed), HomeTabFragment.this.getString(R.string.sharing_tips_1) + SdkConstant.CLOUDAPI_LF + HomeTabFragment.this.getString(R.string.sharing_tips_2) + SdkConstant.CLOUDAPI_LF + HomeTabFragment.this.getString(R.string.sharing_tips_3) + SdkConstant.CLOUDAPI_LF + HomeTabFragment.this.getString(R.string.sharing_tips_4), HomeTabFragment.this.getString(R.string.i_know));
                                }
                            });
                        } else {
                            Toast.makeText(HomeTabFragment.this.getActivity(), localizedMsg, 0).show();
                        }
                    }
                });
            } else {
                HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.25.2
                    @Override // java.lang.Runnable
                    @SuppressLint({"StringFormatInvalid"})
                    public void run() {
                        FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                        if (activity2 == null || activity2.isFinishing()) {
                            return;
                        }
                        ArrayList arrayList = new ArrayList();
                        if (!SharePreferenceManager.getInstance().getShareHistory().isEmpty()) {
                            arrayList.addAll(JSON.parseArray(SharePreferenceManager.getInstance().getShareHistory(), String.class));
                        }
                        arrayList.add(AnonymousClass25.this.val$accountAttr);
                        SharePreferenceManager.getInstance().setShareHistory(JSON.toJSONString(arrayList));
                        Toast.makeText(HomeTabFragment.this.getActivity(), HomeTabFragment.this.getString(R.string.share_succeed, AnonymousClass25.this.val$deviceInfoBean.getName(), AnonymousClass25.this.val$accountAttr), 0).show();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disposeShareDevice(final int i, ArrayList<String> arrayList) {
        if (i == 0) {
            showProgressDialog(getString(R.string.refuse_ing));
        } else {
            showProgressDialog(getString(R.string.receive_ing));
        }
        HashMap map = new HashMap();
        map.put(AlinkConstants.KEY_AGREE, Integer.valueOf(i));
        map.put(AlinkConstants.KEY_RECORD_ID_LIST, arrayList);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath(AlinkConstants.HTTP_PATH_DEVICE_SHARE_MSG).setScheme(Scheme.HTTPS).setApiVersion("1.0.7").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: fragment.HomeTabFragment.26
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, HomeTabFragment.this.TAG, "onFailure");
                HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.26.1
                    @Override // java.lang.Runnable
                    public void run() {
                        FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                        if (activity2 == null || activity2.isFinishing()) {
                            return;
                        }
                        HomeTabFragment.this.dismissProgressDialog();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                Log.e(HomeTabFragment.this.TAG, "disposeShareDevice onResponse: code: " + code);
                final String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.26.2
                        @Override // java.lang.Runnable
                        public void run() {
                            FragmentActivity activity2 = HomeTabFragment.this.getActivity();
                            if (activity2 == null || activity2.isFinishing()) {
                                return;
                            }
                            HomeTabFragment.this.dismissProgressDialog();
                            Toast.makeText(HomeTabFragment.this.getActivity(), localizedMsg, 0).show();
                        }
                    });
                    return;
                }
                HomeTabFragment.this.mGetDeviceIntent = i == 1 ? 2 : 3;
                HomeTabFragment.this.startTask(1);
            }
        });
    }

    private void dismissScanProgressDialog() {
        ScanProgressDialog scanProgressDialog = this.scanProgressDialog;
        if (scanProgressDialog == null || !scanProgressDialog.isShowing()) {
            return;
        }
        this.scanProgressDialog.dismiss();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            Log.d(this.TAG, "onActivityResult");
            if (intent.getStringExtra("productKey") != null) {
                Bundle bundle = new Bundle();
                bundle.putString("productKey", intent.getStringExtra("productKey"));
                bundle.putString("deviceName", intent.getStringExtra("deviceName"));
                bundle.putString("token", intent.getStringExtra("token"));
                Intent intent2 = new Intent(getActivity(), (Class<?>) HomeTabFragment.class);
                intent2.putExtras(bundle);
                startActivity(intent2);
                return;
            }
            return;
        }
        if (i == 10002 && i2 == -1) {
            this.scanProgressDialog = new ScanProgressDialog(getActivity(), R.style.progress_dialog);
            this.scanProgressDialog.setOnViewClick(new ScanProgressDialog.OnViewClick() { // from class: fragment.HomeTabFragment.27
                @Override // dialog.ScanProgressDialog.OnViewClick
                public void onDismiss() {
                    LocalDeviceMgr.getInstance().stopDiscovery();
                    HomeTabFragment.this.isDiscoverying = false;
                }
            });
            this.scanProgressDialog.show();
            this.foudDevices.set(0);
            this.bindDevices.set(this.mAdapter.getData().size());
            ScanProgressDialog scanProgressDialog = this.scanProgressDialog;
            if (scanProgressDialog == null || !scanProgressDialog.isShowing() || this.isDiscoverying) {
                return;
            }
            startDiscovery();
        }
    }

    public void startDiscovery() {
        this.isDiscoverying = true;
        LocalDeviceMgr.getInstance().startDiscovery(getActivity(), EnumSet.allOf(DiscoveryType.class), null, new IDeviceDiscoveryListener() { // from class: fragment.HomeTabFragment.28
            @Override // com.aliyun.alink.business.devicecenter.api.discovery.IDeviceDiscoveryListener
            public void onDeviceFound(DiscoveryType discoveryType, List<DeviceInfo> list) {
                if (HomeTabFragment.this.scanProgressDialog.isShowing()) {
                    ArrayList arrayList = new ArrayList();
                    for (DeviceInfo deviceInfo : list) {
                        if (discoveryType == DiscoveryType.LOCAL_ONLINE_DEVICE) {
                            HomeTabFragment.this.scanProgressDialog.setTextTip(HomeTabFragment.this.foudDevices.incrementAndGet() + "");
                            FoundDeviceListItem foundDeviceListItem = new FoundDeviceListItem();
                            foundDeviceListItem.deviceStatus = FoundDeviceListItem.NEED_BIND;
                            foundDeviceListItem.discoveryType = discoveryType;
                            foundDeviceListItem.deviceInfo = deviceInfo;
                            foundDeviceListItem.deviceName = deviceInfo.deviceName;
                            foundDeviceListItem.productKey = deviceInfo.productKey;
                            foundDeviceListItem.token = deviceInfo.token;
                            arrayList.add(foundDeviceListItem);
                            HomeTabFragment.this.bindDeviceInternal(arrayList, foundDeviceListItem.productKey, foundDeviceListItem.deviceName, foundDeviceListItem.token);
                        }
                    }
                    arrayList.size();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindDeviceInternal(List<FoundDeviceListItem> list, String str, String str2, String str3) {
        test("5--bind" + str2);
        HashMap map = new HashMap();
        map.put("productKey", str);
        map.put("deviceName", str2);
        if (!TextUtils.isEmpty(str3)) {
            map.put("token", str3);
        }
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/awss/enrollee/user/bind").setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: fragment.HomeTabFragment.29
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, HomeTabFragment.this.TAG, "onFailure");
                HomeTabFragment.this.test("5--fail");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.d(true, HomeTabFragment.this.TAG, "onResponse bindWithWiFi ok");
                HomeTabFragment.this.test("5--" + ioTResponse.getCode());
                if (200 == ioTResponse.getCode() && (ioTResponse.getData() instanceof String)) {
                    EventBus.getDefault().post(new updateTimeModify((String) ioTResponse.getData()));
                    EventBus.getDefault().post(new RefreshDevices());
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<DeviceInfoBeans> SortingByOnlineStrategy(List<DeviceInfoBeans> list) {
        DeviceInfoBean cellDeviceInfoBean;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        List array = JSON.parseArray(SharePreferenceManager.getInstance().getLikeList(), String.class);
        for (DeviceInfoBeans deviceInfoBeans : list) {
            if (deviceInfoBeans.getData().size() == 0) {
                cellDeviceInfoBean = deviceInfoBeans.getCellDeviceInfoBean();
            } else {
                cellDeviceInfoBean = deviceInfoBeans.getData().get(0);
            }
            if (array != null && array.size() != 0) {
                String strJoin = String.join(", ", array);
                if (strJoin != null && cellDeviceInfoBean != null && cellDeviceInfoBean.getIotId() != null && strJoin.contains(cellDeviceInfoBean.getIotId())) {
                    cellDeviceInfoBean.setLike(true);
                    arrayList.add(deviceInfoBeans);
                } else if (cellDeviceInfoBean != null && cellDeviceInfoBean.getIotId() != null && cellDeviceInfoBean.getStatus() == 1) {
                    arrayList2.add(deviceInfoBeans);
                } else if (cellDeviceInfoBean != null && cellDeviceInfoBean.getIotId() != null && cellDeviceInfoBean.getStatus() == 0) {
                    arrayList4.add(deviceInfoBeans);
                } else if (cellDeviceInfoBean != null && cellDeviceInfoBean.getReceiverStatus() == -1) {
                    arrayList4.add(deviceInfoBeans);
                } else {
                    arrayList3.add(deviceInfoBeans);
                }
            } else if (cellDeviceInfoBean != null && cellDeviceInfoBean.getIotId() != null && cellDeviceInfoBean.getStatus() == 1) {
                arrayList2.add(deviceInfoBeans);
            } else if (cellDeviceInfoBean != null && cellDeviceInfoBean.getIotId() != null && cellDeviceInfoBean.getStatus() == 0) {
                arrayList4.add(deviceInfoBeans);
            } else if (cellDeviceInfoBean != null && cellDeviceInfoBean.getReceiverStatus() == -1) {
                arrayList4.add(deviceInfoBeans);
            } else {
                arrayList3.add(deviceInfoBeans);
            }
        }
        arrayList5.addAll(arrayList4);
        arrayList5.addAll(arrayList);
        arrayList5.addAll(arrayList2);
        arrayList5.addAll(arrayList3);
        return arrayList5;
    }

    @Override // fragment.CommonFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        ThreadPoolExecutor threadPoolExecutor = this.threadPoolExecutor;
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
            this.threadPoolExecutor.shutdownNow();
        }
        EventBus.getDefault().unregister(this);
        MobileChannel.getInstance().unSubscrbie("path/of/topic", new IMobileSubscrbieListener() { // from class: fragment.HomeTabFragment.30
            @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
            public boolean needUISafety() {
                return false;
            }

            @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
            public void onSuccess(String str) {
                Log.d(HomeTabFragment.this.TAG, "onSuccess, topic = " + str);
            }

            @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
            public void onFailed(String str, AError aError) {
                Log.d(HomeTabFragment.this.TAG, "onFailed, topic = " + str);
            }
        });
        MobileChannel.getInstance().unBindAccount(new IMobileRequestListener() { // from class: fragment.HomeTabFragment.31
            @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileRequestListener
            public void onFailure(AError aError) {
            }

            @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileRequestListener
            public void onSuccess(String str) {
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pushRefresh(PushRefresh pushRefresh) {
        if (LoginBusiness.isLogin()) {
            startTask(1);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        if (!isRefresh) {
            this.rlEmpty.setVisibility(8);
            this.srl.setRefreshing(true);
            startTask(1);
            isRefresh = true;
        }
        super.onResume();
        if (isLanConnect) {
            return;
        }
        LanConnect();
        isLanConnect = true;
    }

    public static void getRefresh() {
        isRefresh = false;
    }

    public static void setLanConnect() {
        isLanConnect = false;
    }

    protected void LanConnect() {
        this.scanProgressDialog = new ScanProgressDialog(getActivity(), R.style.progress_dialog);
        this.scanProgressDialog.setOnViewClick(new ScanProgressDialog.OnViewClick() { // from class: fragment.HomeTabFragment.32
            @Override // dialog.ScanProgressDialog.OnViewClick
            public void onDismiss() {
                LocalDeviceMgr.getInstance().stopDiscovery();
                HomeTabFragment.this.isDiscoverying = false;
            }
        });
        this.scanProgressDialog.show();
        this.foudDevices.set(0);
        this.bindDevices.set(this.mAdapter.getData().size());
        ScanProgressDialog scanProgressDialog = this.scanProgressDialog;
        if (scanProgressDialog == null || !scanProgressDialog.isShowing() || this.isDiscoverying) {
            return;
        }
        startDiscovery();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAddDevicePush(final String str) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("eventType", 1);
        map.put("alarmType", 1);
        map.put("eventInterval", Integer.valueOf(AppConfig.PushInterval));
        map.put("switchOn", false);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/set").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: fragment.HomeTabFragment.33
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.e(true, HomeTabFragment.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ioTResponse.getCode();
            }
        });
        HashMap map2 = new HashMap();
        map2.put("iotId", str);
        map2.put("eventType", 1);
        map2.put("alarmType", 2);
        map2.put("eventInterval", Integer.valueOf(AppConfig.PushInterval));
        map2.put("switchOn", Boolean.valueOf(true ^ AppConfig.isChina));
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/bizevent/config/set").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map2).build(), new IoTCallback() { // from class: fragment.HomeTabFragment.34
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.e(true, HomeTabFragment.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ioTResponse.getCode();
                Log.e("设备绑定", str + "关闭云存" + ioTResponse.getData());
            }
        });
    }

    private void modifyDevName(String str, String str2) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("nickName", str2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/uc/setDeviceNickName").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: fragment.HomeTabFragment.35
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, HomeTabFragment.this.TAG, "onFailure");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                LogEx.d(true, HomeTabFragment.this.TAG, "modifyDevName onResponse: code: " + code);
            }
        });
    }

    public synchronized void showTip(String str, String str2, String str3, String str4, boolean z, DeviceInfoBean deviceInfoBean) {
        View viewInflate = View.inflate(getContext(), R.layout.offline_tips_4g, null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.textView8);
        ProgressBar progressBar = (ProgressBar) viewInflate.findViewById(R.id.load_progressbar);
        ImageButton imageButton = (ImageButton) viewInflate.findViewById(R.id.close_btn);
        Drawable drawable = getResources().getDrawable(R.drawable.et_cancel);
        drawable.setBounds(0, 0, (int) (((double) drawable.getIntrinsicWidth()) * 0.5d), (int) (((double) drawable.getIntrinsicHeight()) * 0.5d));
        imageButton.setBackground(drawable);
        if (str != null && str.equals("NET_CELLULAR") && !z) {
            Button button = (Button) viewInflate.findViewById(R.id.cancel);
            final AlertDialog alertDialogCreate = new AlertDialog.Builder(getContext()).setView(viewInflate).create();
            alertDialogCreate.setCanceledOnTouchOutside(false);
            alertDialogCreate.show();
            alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(getContext(), 300.0f), -2);
            button.setOnClickListener(new View.OnClickListener() { // from class: fragment.HomeTabFragment.36
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    alertDialogCreate.dismiss();
                }
            });
            imageButton.setOnClickListener(new View.OnClickListener() { // from class: fragment.HomeTabFragment.37
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    alertDialogCreate.dismiss();
                }
            });
            requestOKHttp(str2, textView, button, alertDialogCreate, str3, str4, progressBar, deviceInfoBean);
        } else {
            View viewInflate2 = View.inflate(getContext(), R.layout.offline_tips, null);
            Button button2 = (Button) viewInflate2.findViewById(R.id.cancel);
            final AlertDialog alertDialogCreate2 = new AlertDialog.Builder(getContext()).setView(viewInflate2).create();
            alertDialogCreate2.setCanceledOnTouchOutside(false);
            alertDialogCreate2.show();
            alertDialogCreate2.getWindow().setLayout(DensityUtil.dip2px(getContext(), 300.0f), -2);
            button2.setOnClickListener(new View.OnClickListener() { // from class: fragment.HomeTabFragment.38
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    alertDialogCreate2.dismiss();
                }
            });
        }
    }

    /* JADX INFO: renamed from: fragment.HomeTabFragment$39, reason: invalid class name */
    class AnonymousClass39 implements ISetCallback {
        final /* synthetic */ DeviceInfoBean val$bean;
        final /* synthetic */ Button val$button;
        final /* synthetic */ AlertDialog val$dialog;
        final /* synthetic */ String val$iotId;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$textView;

        AnonymousClass39(String str, ProgressBar progressBar, Button button, TextView textView, AlertDialog alertDialog, DeviceInfoBean deviceInfoBean) {
            this.val$iotId = str;
            this.val$progressBar = progressBar;
            this.val$button = button;
            this.val$textView = textView;
            this.val$dialog = alertDialog;
            this.val$bean = deviceInfoBean;
        }

        @Override // tools.ISetCallback
        public void onSucceed() {
            String iccId = SharePreferenceManager.getInstance().getIccId(this.val$iotId);
            if ("".equals(iccId)) {
                HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.39.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (HomeTabFragment.this.isAdded()) {
                            AnonymousClass39.this.val$progressBar.setVisibility(8);
                            AnonymousClass39.this.val$button.setVisibility(0);
                            AnonymousClass39.this.val$textView.setText(Html.fromHtml(HomeTabFragment.this.getResources().getString(R.string.offline_text)));
                        }
                    }
                });
                return;
            }
            new OkHttpClient.Builder().connectTimeout(5L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).build().newCall(new Request.Builder().url("http://www.secueye.cn:8000/api/smsApi?iccid=" + iccId + "&method=smsStatusSecueye").get().build()).enqueue(new AnonymousClass2());
        }

        /* JADX INFO: renamed from: fragment.HomeTabFragment$39$2, reason: invalid class name */
        class AnonymousClass2 implements Callback {
            static final /* synthetic */ boolean $assertionsDisabled = false;

            AnonymousClass2() {
            }

            @Override // okhttp3.Callback
            public void onFailure(@NonNull Call call, @NonNull IOException iOException) {
                HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.39.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(HomeTabFragment.this.getActivity(), HomeTabFragment.this.getString(R.string.query_timeout), 0).show();
                        AnonymousClass39.this.val$button.performClick();
                    }
                });
            }

            @Override // okhttp3.Callback
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(response.body().string());
                if (object.containsKey("code")) {
                    int iIntValue = object.getInteger("code").intValue();
                    if (iIntValue == 400) {
                        HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.39.2.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (HomeTabFragment.this.isAdded()) {
                                    AnonymousClass39.this.val$textView.setText(Html.fromHtml(HomeTabFragment.this.getResources().getString(R.string.device_offline_other_sim)));
                                    AnonymousClass39.this.val$button.setVisibility(0);
                                    AnonymousClass39.this.val$progressBar.setVisibility(8);
                                    HomeTabFragment.this.isOtherCard = true;
                                }
                            }
                        });
                        return;
                    } else if (iIntValue != 200) {
                        HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.39.2.3
                            @Override // java.lang.Runnable
                            public void run() {
                                if (HomeTabFragment.this.getActivity() == null || HomeTabFragment.this.getActivity().isFinishing()) {
                                    return;
                                }
                                Toast.makeText(HomeTabFragment.this.getActivity(), HomeTabFragment.this.getResources().getString(R.string.query_fail), 0).show();
                                AnonymousClass39.this.val$button.performClick();
                            }
                        });
                        return;
                    }
                }
                if (!object.containsKey("values") || HomeTabFragment.this.isOtherCard) {
                    return;
                }
                try {
                    com.alibaba.fastjson.JSONObject jSONObject = object.getJSONObject("values");
                    if (jSONObject.containsKey("status")) {
                        if (jSONObject.getString("status").equals("停机")) {
                            HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.39.2.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (HomeTabFragment.this.isAdded()) {
                                        AnonymousClass39.this.val$progressBar.setVisibility(8);
                                        AnonymousClass39.this.val$textView.setText(Html.fromHtml(HomeTabFragment.this.getResources().getString(R.string.device_4g_offline)));
                                        AnonymousClass39.this.val$button.setVisibility(0);
                                        AnonymousClass39.this.val$button.setText(HomeTabFragment.this.getResources().getString(R.string.renew_now));
                                        AnonymousClass39.this.val$button.setOnClickListener(new View.OnClickListener() { // from class: fragment.HomeTabFragment.39.2.4.1
                                            @Override // android.view.View.OnClickListener
                                            public void onClick(View view2) {
                                                AnonymousClass39.this.val$dialog.dismiss();
                                                if (((SharePreferenceManager.getInstance().getPageControlEx(AnonymousClass39.this.val$iotId) & 524288) >> 19) == 1) {
                                                    if (SharePreferenceManager.getInstance().getIccId1(AnonymousClass39.this.val$iotId).equals("") && SharePreferenceManager.getInstance().getIccId2(AnonymousClass39.this.val$iotId).equals("")) {
                                                        Intent intent = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) Traffic4GActivity.class);
                                                        intent.putExtra("iccid", SharePreferenceManager.getInstance().getIccId(AnonymousClass39.this.val$iotId));
                                                        intent.putExtra("iotId", AnonymousClass39.this.val$bean.getIotId());
                                                        intent.putExtra(AlinkConstants.KEY_DN, AnonymousClass39.this.val$bean.getDeviceName());
                                                        intent.putExtra(AlinkConstants.KEY_PK, AnonymousClass39.this.val$bean.getProductKey());
                                                        HomeTabFragment.this.startActivity(intent);
                                                        return;
                                                    }
                                                    Intent intent2 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) Net4GSwitchActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, AnonymousClass39.this.val$bean);
                                                    intent2.putExtras(bundle);
                                                    HomeTabFragment.this.startActivity(intent2);
                                                    return;
                                                }
                                                Intent intent3 = new Intent(HomeTabFragment.this.getActivity(), (Class<?>) Traffic4GActivity.class);
                                                intent3.putExtra("iccid", SharePreferenceManager.getInstance().getIccId(AnonymousClass39.this.val$iotId));
                                                intent3.putExtra("iotId", AnonymousClass39.this.val$bean.getIotId());
                                                intent3.putExtra(AlinkConstants.KEY_DN, AnonymousClass39.this.val$bean.getDeviceName());
                                                intent3.putExtra(AlinkConstants.KEY_PK, AnonymousClass39.this.val$bean.getProductKey());
                                                HomeTabFragment.this.startActivity(intent3);
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            HomeTabFragment.this.mHandler.post(new Runnable() { // from class: fragment.HomeTabFragment.39.2.5
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (HomeTabFragment.this.isAdded()) {
                                        AnonymousClass39.this.val$progressBar.setVisibility(8);
                                        AnonymousClass39.this.val$button.setVisibility(0);
                                        AnonymousClass39.this.val$textView.setText(Html.fromHtml(HomeTabFragment.this.getResources().getString(R.string.offline_text)));
                                    }
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override // tools.ISetCallback
        public void onFailed() {
            Log.e("onFailed", "1111");
        }
    }

    public void requestOKHttp(String str, TextView textView, Button button, AlertDialog alertDialog, String str2, String str3, ProgressBar progressBar, DeviceInfoBean deviceInfoBean) {
        this.isOtherCard = false;
        SettingsCtrl.getInstance().getIccIdParam(str, new AnonymousClass39(str, progressBar, button, textView, alertDialog, deviceInfoBean));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        List<DeviceInfoBeans> list;
        DeviceAdapter deviceAdapter;
        switch (view2.getId()) {
            case R.id.bt_scan /* 2131296495 */:
                if (ActivityCompat.checkSelfPermission(getActivity(), Permission.CAMERA) != 0) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Permission.CAMERA}, 4370);
                } else {
                    startActivityForResult(new Intent(getActivity(), (Class<?>) ScanActivity.class), 10003);
                }
                break;
            case R.id.ilop_main_add_big_btn /* 2131296859 */:
            case R.id.ll_add_device /* 2131297261 */:
                startActivity(new Intent(getActivity(), (Class<?>) (AppConfig.isChina ? scanNavigationActivity.class : AddDeviceActivity.class)));
                break;
            case R.id.ilop_main_menu_add_device_btn /* 2131296861 */:
                Router.getInstance().toUrl(getContext(), "page/ilopadddevice", this.mBundle);
                break;
            case R.id.ilop_main_menu_scan_btn /* 2131296862 */:
                startActivityForResult(new Intent(getActivity(), (Class<?>) InputWifiActivity.class), 10002);
                break;
            case R.id.iv_search /* 2131297078 */:
                this.layout_top.setVisibility(8);
                this.rl_search.setVisibility(0);
                this.edit_search.setText("");
                this.srl.setEnabled(false);
                this.edit_search.requestFocus();
                ((InputMethodManager) getActivity().getSystemService("input_method")).showSoftInput(this.edit_search, 1);
                break;
            case R.id.ll_scan_device /* 2131297297 */:
                Intent intent = new Intent(getActivity(), (Class<?>) ScanActivity.class);
                intent.putExtra("type", "scan");
                startActivity(intent);
                break;
            case R.id.tv_cancel /* 2131297858 */:
                if (!this.edit_search.getText().toString().equals("") && (list = this.deviceInfoBeansList) != null && (deviceAdapter = this.mAdapter) != null) {
                    deviceAdapter.replaceData(list);
                    this.mAdapter.notifyDataSetChanged();
                }
                this.layout_top.setVisibility(0);
                this.rl_search.setVisibility(8);
                this.tv_search_fail.setVisibility(8);
                this.srl.setEnabled(true);
                ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.edit_search.getWindowToken(), 0);
                break;
        }
    }
}
