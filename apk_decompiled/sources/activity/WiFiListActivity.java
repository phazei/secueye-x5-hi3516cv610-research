package activity;

import activity.SettingsActivity;
import adapter.WiFiAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import bean.WifiBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seculink.app.R;
import config.Constants;
import dialog.InputDialogView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sdk.IPCManager;
import tools.DensityUtil;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class WiFiListActivity extends CommonActivity {
    private CountDownTimer countDownTimer;
    TitleView flTitlebar;
    private InputDialogView inputDialogView;
    private String iotId;
    LinearLayout layout_main;
    private WiFiAdapter mAdapter;
    private Handler mHandler = new Handler();
    RecyclerView recycler;
    private String selectSsid;
    SwipeRefreshLayout srl;
    private List<WifiBean> wifiBeanList;
    private String wifiJsonString;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_wi_fi_list;
    }

    public static void start(Context context, String str, String str2) {
        Intent intent = new Intent(context, (Class<?>) WiFiListActivity.class);
        intent.putExtra("iotId", str);
        intent.putExtra("wifiJsonString", str2);
        context.startActivity(intent);
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        this.iotId = intent.getStringExtra("iotId");
        this.wifiJsonString = intent.getStringExtra("wifiJsonString");
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.inputDialogView = new InputDialogView.Builder().build();
        this.inputDialogView.addOnClickListener(new AnonymousClass1());
        this.srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        this.recycler = (RecyclerView) findViewById(R.id.recycler);
        this.flTitlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.WiFiListActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                WiFiListActivity.this.finish();
            }
        });
        this.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mAdapter = new WiFiAdapter(R.layout.item_wifi);
        this.mAdapter.bindToRecyclerView(this.recycler);
        this.mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() { // from class: activity.WiFiListActivity.3
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view2, int i) {
                WifiBean wifiBean = WiFiListActivity.this.mAdapter.getData().get(i);
                if (wifiBean.isCurrentWifi()) {
                    return;
                }
                WiFiListActivity.this.selectSsid = wifiBean.getSsid();
                WiFiListActivity.this.inputDialogView.setTitle(WiFiListActivity.this.selectSsid);
                WiFiListActivity.this.inputDialogView.setHint(WiFiListActivity.this.getString(R.string.input_pwd));
                WiFiListActivity.this.inputDialogView.show(WiFiListActivity.this.getSupportFragmentManager(), WiFiListActivity.this.TAG);
            }
        });
        this.srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: activity.WiFiListActivity.4
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                WiFiListActivity.this.getWiFiList();
            }
        });
    }

    /* JADX INFO: renamed from: activity.WiFiListActivity$1, reason: invalid class name */
    class AnonymousClass1 implements InputDialogView.OnClickListener {
        @Override // dialog.InputDialogView.OnClickListener
        public void onNegativeClick() {
        }

        AnonymousClass1() {
        }

        @Override // dialog.InputDialogView.OnClickListener
        public void onPositiveClick(String str, Object obj) {
            WiFiListActivity.this.showProgressDialog();
            IPCManager.getInstance().getDevice(WiFiListActivity.this.iotId).setAPList(WiFiListActivity.this.selectSsid, str, new IPanelCallback() { // from class: activity.WiFiListActivity.1.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    boolean z2 = false;
                    try {
                        if (z) {
                            if (obj2 == null) {
                                WiFiListActivity.this.mHandler.post(new Runnable() { // from class: activity.WiFiListActivity.1.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ToastUtils.toast(WiFiListActivity.this.getActivity(), WiFiListActivity.this.getString(R.string.set_wifi_failed));
                                    }
                                });
                                return;
                            } else if (((IoTResponse) JSON.parseObject(obj2.toString()).toJavaObject(IoTResponse.class)).getCode() != 200) {
                                WiFiListActivity.this.mHandler.post(new Runnable() { // from class: activity.WiFiListActivity.1.1.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ToastUtils.toast(WiFiListActivity.this.getActivity(), WiFiListActivity.this.getString(R.string.set_wifi_failed));
                                    }
                                });
                            } else {
                                z2 = true;
                                if (WiFiListActivity.this.wifiJsonString.equals("1")) {
                                    WiFiListActivity.this.runOnUiThread(new Runnable() { // from class: activity.WiFiListActivity.1.1.3
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            WiFiListActivity.this.FourGChangeDialog();
                                        }
                                    });
                                }
                            }
                        }
                        if (!z2) {
                            WiFiListActivity.this.mHandler.post(new Runnable() { // from class: activity.WiFiListActivity.1.1.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(WiFiListActivity.this.getActivity(), WiFiListActivity.this.getString(R.string.set_wifi_failed), 0).show();
                                }
                            });
                        }
                    } finally {
                        WiFiListActivity.this.mHandler.post(new Runnable() { // from class: activity.WiFiListActivity.1.1.5
                            @Override // java.lang.Runnable
                            public void run() {
                                WiFiListActivity.this.dismissProgressDialog();
                            }
                        });
                    }
                }
            });
        }
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        this.srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        this.srl.measure(0, 0);
        this.srl.setRefreshing(true);
        getWiFiList();
        SharePreferenceManager.getInstance().setFirstNet(this.iotId, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getWiFiList() {
        IPCManager.getInstance().getDevice(this.iotId).queryAPList(new IPanelCallback() { // from class: activity.WiFiListActivity.5
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z || obj == null) {
                    return;
                }
                IoTResponse ioTResponse = (IoTResponse) JSON.parseObject(obj.toString()).toJavaObject(IoTResponse.class);
                if (ioTResponse.getCode() != 200) {
                    WiFiListActivity.this.mHandler.post(new Runnable() { // from class: activity.WiFiListActivity.5.1
                        @Override // java.lang.Runnable
                        public void run() {
                            WiFiListActivity.this.getWiFiListFailed();
                        }
                    });
                    return;
                }
                Object data = ioTResponse.getData();
                if (data != null) {
                    try {
                        JSONArray jSONArray = ((JSONObject) data).getJSONArray("APList");
                        WiFiListActivity.this.wifiBeanList = JSON.parseArray(jSONArray.toString(), WifiBean.class);
                        WiFiListActivity.this.mHandler.post(new Runnable() { // from class: activity.WiFiListActivity.5.2
                            @Override // java.lang.Runnable
                            public void run() {
                                WiFiListActivity.this.getWiFiListSucceed(WiFiListActivity.this.wifiBeanList);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        WiFiListActivity.this.mHandler.post(new Runnable() { // from class: activity.WiFiListActivity.5.3
                            @Override // java.lang.Runnable
                            public void run() {
                                WiFiListActivity.this.getWiFiListFailed();
                            }
                        });
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getWiFiListSucceed(List<WifiBean> list) {
        if (this.srl.isRefreshing()) {
            this.srl.setRefreshing(false);
        }
        if (list == null || list.size() == 0) {
            return;
        }
        this.mAdapter.replaceData(list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getWiFiListFailed() {
        if (this.srl.isRefreshing()) {
            this.srl.setRefreshing(false);
        }
        Toast.makeText(this, getString(R.string.get_wifi_failed), 0).show();
    }

    public List<WifiBean> parseData(String str) {
        Object data;
        ArrayList arrayList = new ArrayList();
        if (str == null) {
            return arrayList;
        }
        IoTResponse ioTResponse = (IoTResponse) JSON.parseObject(str).toJavaObject(IoTResponse.class);
        if (ioTResponse.getCode() != 200 || (data = ioTResponse.getData()) == null) {
            return arrayList;
        }
        try {
            return JSON.parseArray(((JSONObject) data).getJSONArray("APList").toString(), WifiBean.class);
        } catch (Exception unused) {
            return arrayList;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void FourGChangeDialog() {
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
        alertDialogCreate.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: activity.WiFiListActivity.6
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                WiFiListActivity.this.cancelCount();
            }
        });
        ImageView imageView = (ImageView) viewInflate.findViewById(R.id.image_result);
        alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this, 300.0f), -2);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: activity.WiFiListActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.WiFiListActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                alertDialogCreate.dismiss();
            }
        });
        if (this.countDownTimer == null) {
            this.countDownTimer = new AnonymousClass9(60000L, 4000L, textView, progressBar, textView2, imageView, imageButton);
            this.countDownTimer.start();
        }
    }

    /* JADX INFO: renamed from: activity.WiFiListActivity$9, reason: invalid class name */
    class AnonymousClass9 extends CountDownTimer {
        final /* synthetic */ ImageButton val$imageButton;
        final /* synthetic */ ImageView val$imageView;
        final /* synthetic */ TextView val$imageViewText;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$progressBarText;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass9(long j, long j2, TextView textView, ProgressBar progressBar, TextView textView2, ImageView imageView, ImageButton imageButton) {
            super(j, j2);
            this.val$progressBarText = textView;
            this.val$progressBar = progressBar;
            this.val$imageViewText = textView2;
            this.val$imageView = imageView;
            this.val$imageButton = imageButton;
        }

        @Override // android.os.CountDownTimer
        public void onTick(long j) {
            SettingsCtrl.getInstance().getProperties(WiFiListActivity.this.iotId, new MyCallback() { // from class: activity.WiFiListActivity.9.1
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z && SharePreferenceManager.getInstance().getWifiConfigIsExist(WiFiListActivity.this.iotId) == 1) {
                        WiFiListActivity.this.mHandler.post(new Runnable() { // from class: activity.WiFiListActivity.9.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AnonymousClass9.this.val$progressBarText.setVisibility(8);
                                AnonymousClass9.this.val$progressBar.setVisibility(8);
                                AnonymousClass9.this.val$imageViewText.setVisibility(0);
                                AnonymousClass9.this.val$imageViewText.setText("切换成功!");
                                AnonymousClass9.this.val$imageView.setVisibility(0);
                                AnonymousClass9.this.val$imageView.setImageResource(R.drawable.success);
                                AnonymousClass9.this.val$imageButton.setVisibility(0);
                            }
                        });
                        AnonymousClass9.this.cancel();
                        WiFiListActivity.this.countDownTimer = null;
                        WiFiListActivity.this.finish();
                    }
                }
            });
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            WiFiListActivity.this.mHandler.post(new Runnable() { // from class: activity.WiFiListActivity.9.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass9.this.val$progressBarText.setVisibility(8);
                    AnonymousClass9.this.val$progressBar.setVisibility(8);
                    AnonymousClass9.this.val$imageViewText.setVisibility(0);
                    AnonymousClass9.this.val$imageViewText.setText("切换失败，请检查路由器是否异常!");
                    AnonymousClass9.this.val$imageView.setVisibility(0);
                    AnonymousClass9.this.val$imageView.setImageResource(R.drawable.fail);
                    AnonymousClass9.this.val$imageButton.setVisibility(0);
                }
            });
            cancel();
            WiFiListActivity.this.countDownTimer = null;
        }
    }

    public void cancelCount() {
        CountDownTimer countDownTimer = this.countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.countDownTimer = null;
        }
    }

    private void getQueryNetModeStatus(final SettingsActivity.CallBack callBack) {
        IPCManager.getInstance().getDevice(this.iotId).getQueryNetModeStatus(new IPanelCallback() { // from class: activity.WiFiListActivity.10
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                if (z) {
                    callBack.finish();
                }
                Log.d(WiFiListActivity.this.TAG, "onComplete: " + obj.toString());
            }
        });
    }

    private void switch4gMode(final SettingsActivity.CallBack callBack) {
        HashMap map = new HashMap();
        map.put(Constants.Net4GEnableSwitch, 1);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.WiFiListActivity.11
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (z) {
                    if (obj == null || "".equals(String.valueOf(obj))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code") && object.getInteger("code").intValue() == 200) {
                        SharePreferenceManager.getInstance().setNet4GEnableSwitch(WiFiListActivity.this.iotId, 1);
                        ToastUtils.toast(WiFiListActivity.this.getActivity(), WiFiListActivity.this.getResources().getString(R.string.mofify_succeed));
                        callBack.finish();
                        return;
                    }
                    return;
                }
                WiFiListActivity.this.runOnUiThread(new Runnable() { // from class: activity.WiFiListActivity.11.1
                    @Override // java.lang.Runnable
                    public void run() {
                    }
                });
            }
        });
    }
}
