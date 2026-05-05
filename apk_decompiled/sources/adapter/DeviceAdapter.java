package adapter;

import activity.Net4GSwitchActivity;
import activity.Traffic4GActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import bean.DeviceInfoBean;
import bean.DeviceInfoBeans;
import com.alibaba.cloudapi.sdk.constant.HttpConstant;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seculink.app.R;
import config.Constants;
import dialog.DialogUtil;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sdk.IPCManager;
import tools.DensityUtil;
import tools.ISetCallback;
import tools.OnMultiClickListener;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.TabView;

/* JADX INFO: loaded from: classes.dex */
public class DeviceAdapter extends BaseQuickAdapter<DeviceInfoBeans, com.chad.library.adapter.base.BaseViewHolder> {
    DrawableCrossFadeFactory drawableCrossFadeFactory;
    private int h;
    private final boolean isChina;
    private boolean isOtherCard;
    public GripDeviceAdapter mAdapter;
    private final Handler mHandler;
    HashMap<String, String> mHashMap;
    OfflineBtnClick offlineBtnClick;
    private int viewHeight;
    private int w;

    public interface OfflineBtnClick {
        void OnClick(String str, String str2, String str3, String str4, boolean z, DeviceInfoBean deviceInfoBean);
    }

    public DeviceAdapter(int i) {
        super(i);
        this.mHandler = new Handler();
        this.drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
        this.isChina = true;
        this.mHashMap = new HashMap<>();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:111:0x03a9  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x03d1  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0416  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0442  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x045e  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0469  */
    /* JADX WARN: Removed duplicated region for block: B:139:0x048b  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0517  */
    /* JADX WARN: Removed duplicated region for block: B:406:0x121e  */
    /* JADX WARN: Removed duplicated region for block: B:410:0x126e  */
    /* JADX WARN: Removed duplicated region for block: B:413:0x12a4  */
    /* JADX WARN: Removed duplicated region for block: B:430:0x12f1  */
    /* JADX WARN: Removed duplicated region for block: B:437:0x1351  */
    /* JADX WARN: Removed duplicated region for block: B:440:0x1370  */
    /* JADX WARN: Removed duplicated region for block: B:445:0x1418  */
    /* JADX WARN: Removed duplicated region for block: B:448:0x1464  */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void convert(final com.chad.library.adapter.base.BaseViewHolder r27, final bean.DeviceInfoBeans r28) {
        /*
            Method dump skipped, instruction units count: 5487
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: adapter.DeviceAdapter.convert(com.chad.library.adapter.base.BaseViewHolder, bean.DeviceInfoBeans):void");
    }

    /* JADX INFO: renamed from: adapter.DeviceAdapter$2, reason: invalid class name */
    class AnonymousClass2 extends OnMultiClickListener {
        final /* synthetic */ DeviceInfoBean val$item;
        final /* synthetic */ ImageView val$iv_night_bottom;

        AnonymousClass2(DeviceInfoBean deviceInfoBean, ImageView imageView) {
            this.val$item = deviceInfoBean;
            this.val$iv_night_bottom = imageView;
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            HashMap map = new HashMap();
            map.put(Constants.FloodlightSwitch, Integer.valueOf(SharePreferenceManager.getInstance().getFloodlightSwitch(this.val$item.getIotId()) == 1 ? 0 : 1));
            IPCManager.getInstance().getDevice(this.val$item.getIotId()).setProperties(map, new IPanelCallback() { // from class: adapter.DeviceAdapter.2.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(final boolean z, @Nullable final Object obj) {
                    new Handler().post(new Runnable() { // from class: adapter.DeviceAdapter.2.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Object obj2;
                            if (!z || (obj2 = obj) == null || "".equals(String.valueOf(obj2))) {
                                return;
                            }
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            if (object.containsKey("code")) {
                                if (object.getInteger("code").intValue() != 200) {
                                    Toast.makeText(DeviceAdapter.this.mContext, R.string.mofify_failed, 0).show();
                                } else if (SharePreferenceManager.getInstance().getFloodlightSwitch(AnonymousClass2.this.val$item.getIotId()) == 0) {
                                    AnonymousClass2.this.val$iv_night_bottom.setImageResource(R.drawable.icon_on_full);
                                    SharePreferenceManager.getInstance().setFloodlightSwitch(AnonymousClass2.this.val$item.getIotId(), 1);
                                } else {
                                    AnonymousClass2.this.val$iv_night_bottom.setImageResource(R.drawable.icon_off_full);
                                    SharePreferenceManager.getInstance().setFloodlightSwitch(AnonymousClass2.this.val$item.getIotId(), 0);
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    /* JADX INFO: renamed from: adapter.DeviceAdapter$8, reason: invalid class name */
    class AnonymousClass8 extends OnMultiClickListener {
        final /* synthetic */ DeviceInfoBeans val$deviceInfoBeansItem;
        final /* synthetic */ com.chad.library.adapter.base.BaseViewHolder val$helper;

        AnonymousClass8(DeviceInfoBeans deviceInfoBeans, com.chad.library.adapter.base.BaseViewHolder baseViewHolder) {
            this.val$deviceInfoBeansItem = deviceInfoBeans;
            this.val$helper = baseViewHolder;
        }

        /* JADX INFO: renamed from: adapter.DeviceAdapter$8$1, reason: invalid class name */
        class AnonymousClass1 implements DialogUtil.OnEditClickListener {
            AnonymousClass1() {
            }

            @Override // dialog.DialogUtil.OnEditClickListener
            public void onEditClick(final String str) {
                OkHttpClient okHttpClient = new OkHttpClient();
                org.json.JSONObject jSONObject = new org.json.JSONObject();
                try {
                    jSONObject.put("id", AnonymousClass8.this.val$deviceInfoBeansItem.id);
                    jSONObject.put("name", str);
                    if (!AnonymousClass8.this.val$deviceInfoBeansItem.imei.isEmpty()) {
                        jSONObject.put(com.taobao.accs.common.Constants.KEY_IMEI, AnonymousClass8.this.val$deviceInfoBeansItem.imei);
                    }
                    if (!AnonymousClass8.this.val$deviceInfoBeansItem.iccid.isEmpty()) {
                        jSONObject.put("iccid", AnonymousClass8.this.val$deviceInfoBeansItem.iccid);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                okHttpClient.newCall(new Request.Builder().url("https://traffic.secueye.app/api/app/update/record").post(RequestBody.create(MediaType.parse(HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON), jSONObject.toString())).build()).enqueue(new Callback() { // from class: adapter.DeviceAdapter.8.1.1
                    @Override // okhttp3.Callback
                    public void onFailure(Call call, IOException iOException) {
                        iOException.printStackTrace();
                        Log.e(com.taobao.accs.common.Constants.KEY_IMEI, "" + iOException.toString());
                    }

                    @Override // okhttp3.Callback
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            Toast.makeText(DeviceAdapter.this.mContext, DeviceAdapter.this.mContext.getString(R.string.connect_failed), 0).show();
                            return;
                        }
                        String strString = response.body().string();
                        Log.e(com.taobao.accs.common.Constants.KEY_IMEI, "" + strString.toString());
                        JSONObject object = JSONObject.parseObject(strString);
                        if (object.containsKey("code")) {
                            if (object.getInteger("code").intValue() == 1000) {
                                DeviceAdapter.this.mHandler.post(new Runnable() { // from class: adapter.DeviceAdapter.8.1.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Toast.makeText(DeviceAdapter.this.mContext, DeviceAdapter.this.mContext.getString(R.string.mofify_succeed), 0).show();
                                        AnonymousClass8.this.val$deviceInfoBeansItem.name = str;
                                        ((TextView) AnonymousClass8.this.val$helper.getView(R.id.tv_card_name)).setText("" + str);
                                    }
                                });
                            } else {
                                Toast.makeText(DeviceAdapter.this.mContext, DeviceAdapter.this.mContext.getString(R.string.connect_failed), 0).show();
                            }
                        }
                    }
                });
            }
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            DialogUtil.showEditDiaLog(DeviceAdapter.this.mContext, DeviceAdapter.this.mContext.getString(R.string.name), this.val$deviceInfoBeansItem.name, new AnonymousClass1());
        }
    }

    /* JADX INFO: renamed from: adapter.DeviceAdapter$9, reason: invalid class name */
    class AnonymousClass9 extends OnMultiClickListener {
        final /* synthetic */ DeviceInfoBeans val$deviceInfoBeansItem;
        final /* synthetic */ com.chad.library.adapter.base.BaseViewHolder val$helper;

        AnonymousClass9(DeviceInfoBeans deviceInfoBeans, com.chad.library.adapter.base.BaseViewHolder baseViewHolder) {
            this.val$deviceInfoBeansItem = deviceInfoBeans;
            this.val$helper = baseViewHolder;
        }

        /* JADX INFO: renamed from: adapter.DeviceAdapter$9$1, reason: invalid class name */
        class AnonymousClass1 implements DialogUtil.OnCancelConfirmClickListener {
            @Override // dialog.DialogUtil.OnCancelConfirmClickListener
            public void CancelListener() {
            }

            AnonymousClass1() {
            }

            @Override // dialog.DialogUtil.OnCancelConfirmClickListener
            public void ConfirmListener() {
                OkHttpClient okHttpClient = new OkHttpClient();
                org.json.JSONObject jSONObject = new org.json.JSONObject();
                try {
                    jSONObject.put("id", AnonymousClass9.this.val$deviceInfoBeansItem.id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                okHttpClient.newCall(new Request.Builder().url("https://traffic.secueye.app/api/app/delete/record").post(RequestBody.create(MediaType.parse(HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON), jSONObject.toString())).build()).enqueue(new Callback() { // from class: adapter.DeviceAdapter.9.1.1
                    @Override // okhttp3.Callback
                    public void onFailure(Call call, IOException iOException) {
                        iOException.printStackTrace();
                    }

                    @Override // okhttp3.Callback
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            Toast.makeText(DeviceAdapter.this.mContext, DeviceAdapter.this.mContext.getString(R.string.connect_failed), 0).show();
                            return;
                        }
                        String strString = response.body().string();
                        Log.e(com.taobao.accs.common.Constants.KEY_IMEI, "" + strString);
                        JSONObject object = JSONObject.parseObject(strString);
                        if (object.containsKey("code")) {
                            if (object.getInteger("code").intValue() == 1000) {
                                DeviceAdapter.this.mHandler.post(new Runnable() { // from class: adapter.DeviceAdapter.9.1.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        Toast.makeText(DeviceAdapter.this.mContext, DeviceAdapter.this.mContext.getString(R.string.delete_succeed), 0).show();
                                        if (AnonymousClass9.this.val$helper.getAdapterPosition() <= DeviceAdapter.this.getData().size() - 1) {
                                            DeviceAdapter.this.remove(AnonymousClass9.this.val$helper.getAdapterPosition());
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(DeviceAdapter.this.mContext, DeviceAdapter.this.mContext.getString(R.string.connect_failed), 0).show();
                            }
                        }
                    }
                });
            }
        }

        @Override // tools.OnMultiClickListener
        public void onMultiClick(View view2) {
            DialogUtil.showCancelConfirmDiaLog(DeviceAdapter.this.mContext, DeviceAdapter.this.mContext.getString(R.string.remove_camera), DeviceAdapter.this.mContext.getString(R.string.confirm_delete_camera), DeviceAdapter.this.mContext.getString(R.string.cancel), DeviceAdapter.this.mContext.getString(R.string.sure_delete), new AnonymousClass1());
        }
    }

    public void setOfflineBtnClick(OfflineBtnClick offlineBtnClick) {
        this.offlineBtnClick = offlineBtnClick;
    }

    public synchronized void refreshTabAlarm(int i) {
        if (i >= getData().size()) {
            return;
        }
        RecyclerView.ViewHolder viewHolderFindViewHolderForLayoutPosition = getRecyclerView().findViewHolderForLayoutPosition(i);
        if (viewHolderFindViewHolderForLayoutPosition == null) {
            return;
        }
        DeviceInfoBeans deviceInfoBeans = getData().get(i);
        if (deviceInfoBeans.getData().size() == 0) {
            return;
        }
        TabView tabView = (TabView) viewHolderFindViewHolderForLayoutPosition.itemView.findViewById(R.id.tab_alarm);
        DeviceInfoBean deviceInfoBean = deviceInfoBeans.getData().get(0);
        if (SharePreferenceManager.getInstance().getAlarmSwitch(deviceInfoBean.getIotId()) == 1) {
            tabView.setTabPic(R.drawable.ic_alarm2);
            tabView.setTabText(this.mContext.getString(R.string.alarm));
            tabView.setVisibility(0);
        } else {
            tabView.setTabPic(R.mipmap.ic_alarm);
            tabView.setTabText(this.mContext.getString(R.string.not_alarm));
            tabView.setVisibility(0);
        }
        if (deviceInfoBean.getOwned() != 1) {
            tabView.setVisibility(8);
        }
        if (SharePreferenceManager.getInstance().getIsRouter(deviceInfoBean.getIotId()) == 1) {
            tabView.setVisibility(8);
        }
        LinearLayout linearLayout = (LinearLayout) viewHolderFindViewHolderForLayoutPosition.itemView.findViewById(R.id.linearLayout2);
        int childCount = linearLayout.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            TabView tabView2 = (TabView) linearLayout.getChildAt(i2);
            if (i2 == 0) {
                tabView2.setGravity(3);
            } else if (childCount != 4 || i2 >= childCount - 1) {
                if (i2 == 3) {
                    tabView2.setGravity(5);
                } else {
                    tabView2.setGravity(17);
                }
            } else if (((TabView) linearLayout.getChildAt(0)).getVisibility() == 8) {
                tabView2.setGravity(3);
            } else {
                tabView2.setGravity(17);
            }
        }
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

    public synchronized void showTip(String str, String str2, String str3, String str4) {
        View viewInflate = View.inflate(this.mContext, R.layout.offline_tips_4g, null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.textView8);
        ProgressBar progressBar = (ProgressBar) viewInflate.findViewById(R.id.load_progressbar);
        ImageButton imageButton = (ImageButton) viewInflate.findViewById(R.id.close_btn);
        Drawable drawable = this.mContext.getResources().getDrawable(R.drawable.et_cancel);
        drawable.setBounds(0, 0, (int) (((double) drawable.getIntrinsicWidth()) * 0.5d), (int) (((double) drawable.getIntrinsicHeight()) * 0.5d));
        imageButton.setBackground(drawable);
        if (str.equals("NET_CELLULAR")) {
            Button button = (Button) viewInflate.findViewById(R.id.cancel);
            final AlertDialog alertDialogCreate = new AlertDialog.Builder(this.mContext).setView(viewInflate).create();
            alertDialogCreate.setCanceledOnTouchOutside(false);
            alertDialogCreate.show();
            alertDialogCreate.getWindow().setLayout(DensityUtil.dip2px(this.mContext, 300.0f), -2);
            button.setOnClickListener(new View.OnClickListener() { // from class: adapter.DeviceAdapter.10
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    alertDialogCreate.dismiss();
                }
            });
            imageButton.setOnClickListener(new View.OnClickListener() { // from class: adapter.DeviceAdapter.11
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    alertDialogCreate.dismiss();
                }
            });
            requestOKHttp(str2, textView, button, alertDialogCreate, str3, str4, progressBar);
        } else {
            View viewInflate2 = View.inflate(this.mContext, R.layout.offline_tips, null);
            Button button2 = (Button) viewInflate2.findViewById(R.id.cancel);
            final AlertDialog alertDialogCreate2 = new AlertDialog.Builder(this.mContext).setView(viewInflate2).create();
            alertDialogCreate2.setCanceledOnTouchOutside(false);
            alertDialogCreate2.show();
            alertDialogCreate2.getWindow().setLayout(DensityUtil.dip2px(this.mContext, 300.0f), -2);
            button2.setOnClickListener(new View.OnClickListener() { // from class: adapter.DeviceAdapter.12
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    alertDialogCreate2.dismiss();
                }
            });
        }
    }

    /* JADX INFO: renamed from: adapter.DeviceAdapter$13, reason: invalid class name */
    class AnonymousClass13 implements ISetCallback {
        final /* synthetic */ Button val$button;
        final /* synthetic */ AlertDialog val$dialog;
        final /* synthetic */ String val$iotId;
        final /* synthetic */ String val$key;
        final /* synthetic */ String val$name;
        final /* synthetic */ ProgressBar val$progressBar;
        final /* synthetic */ TextView val$textView;

        @Override // tools.ISetCallback
        public void onFailed() {
        }

        AnonymousClass13(String str, ProgressBar progressBar, Button button, TextView textView, AlertDialog alertDialog, String str2, String str3) {
            this.val$iotId = str;
            this.val$progressBar = progressBar;
            this.val$button = button;
            this.val$textView = textView;
            this.val$dialog = alertDialog;
            this.val$name = str2;
            this.val$key = str3;
        }

        @Override // tools.ISetCallback
        public void onSucceed() {
            String iccId = SharePreferenceManager.getInstance().getIccId(this.val$iotId);
            if ("".equals(iccId)) {
                DeviceAdapter.this.mHandler.post(new Runnable() { // from class: adapter.DeviceAdapter.13.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AnonymousClass13.this.val$progressBar.setVisibility(8);
                        AnonymousClass13.this.val$button.setVisibility(0);
                        AnonymousClass13.this.val$textView.setText(Html.fromHtml(DeviceAdapter.this.mContext.getResources().getString(R.string.offline_text)));
                    }
                });
                return;
            }
            new OkHttpClient.Builder().connectTimeout(5L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).build().newCall(new Request.Builder().url("http://www.secueye.cn:8000/api/smsApi?iccid=" + iccId + "&method=smsStatusSecueye").get().build()).enqueue(new AnonymousClass2());
        }

        /* JADX INFO: renamed from: adapter.DeviceAdapter$13$2, reason: invalid class name */
        class AnonymousClass2 implements Callback {
            static final /* synthetic */ boolean $assertionsDisabled = false;

            AnonymousClass2() {
            }

            @Override // okhttp3.Callback
            public void onFailure(@NonNull Call call, @NonNull IOException iOException) {
                if (iOException instanceof SocketTimeoutException) {
                    Toast.makeText(DeviceAdapter.this.mContext, R.string.query_timeout, 0).show();
                    AnonymousClass13.this.val$button.performClick();
                } else if (iOException instanceof ConnectException) {
                    Toast.makeText(DeviceAdapter.this.mContext, R.string.query_timeout, 0).show();
                    AnonymousClass13.this.val$button.performClick();
                }
            }

            @Override // okhttp3.Callback
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                JSONObject object = JSONObject.parseObject(response.body().string());
                if (object.containsKey("code")) {
                    int iIntValue = object.getInteger("code").intValue();
                    if (iIntValue == 400) {
                        DeviceAdapter.this.mHandler.post(new Runnable() { // from class: adapter.DeviceAdapter.13.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                AnonymousClass13.this.val$textView.setText(Html.fromHtml(DeviceAdapter.this.mContext.getResources().getString(R.string.device_offline_other_sim)));
                                AnonymousClass13.this.val$button.setVisibility(0);
                                AnonymousClass13.this.val$progressBar.setVisibility(8);
                                DeviceAdapter.this.isOtherCard = true;
                            }
                        });
                        return;
                    } else if (iIntValue != 200) {
                        DeviceAdapter.this.mHandler.post(new Runnable() { // from class: adapter.DeviceAdapter.13.2.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(DeviceAdapter.this.mContext, DeviceAdapter.this.mContext.getResources().getString(R.string.query_fail), 0).show();
                                AnonymousClass13.this.val$button.performClick();
                            }
                        });
                        return;
                    }
                }
                if (!object.containsKey("values") || DeviceAdapter.this.isOtherCard) {
                    return;
                }
                try {
                    JSONObject jSONObject = object.getJSONObject("values");
                    if (jSONObject.containsKey("status")) {
                        if (jSONObject.getString("status").equals("停机")) {
                            DeviceAdapter.this.mHandler.post(new Runnable() { // from class: adapter.DeviceAdapter.13.2.3
                                @Override // java.lang.Runnable
                                public void run() {
                                    AnonymousClass13.this.val$progressBar.setVisibility(8);
                                    AnonymousClass13.this.val$textView.setText(Html.fromHtml(DeviceAdapter.this.mContext.getResources().getString(R.string.device_4g_offline)));
                                    AnonymousClass13.this.val$button.setVisibility(0);
                                    AnonymousClass13.this.val$button.setText(DeviceAdapter.this.mContext.getResources().getString(R.string.renew_now));
                                    AnonymousClass13.this.val$button.setOnClickListener(new View.OnClickListener() { // from class: adapter.DeviceAdapter.13.2.3.1
                                        @Override // android.view.View.OnClickListener
                                        public void onClick(View view2) {
                                            AnonymousClass13.this.val$dialog.dismiss();
                                            if (((SharePreferenceManager.getInstance().getPageControlEx(AnonymousClass13.this.val$iotId) & 524288) >> 19) == 1) {
                                                if (SharePreferenceManager.getInstance().getIccId1(AnonymousClass13.this.val$iotId).equals("") && SharePreferenceManager.getInstance().getIccId2(AnonymousClass13.this.val$iotId).equals("")) {
                                                    Intent intent = new Intent(DeviceAdapter.this.mContext, (Class<?>) Traffic4GActivity.class);
                                                    intent.putExtra("iccid", SharePreferenceManager.getInstance().getIccId(AnonymousClass13.this.val$iotId));
                                                    intent.putExtra(AlinkConstants.KEY_DN, AnonymousClass13.this.val$name);
                                                    intent.putExtra("iotId", AnonymousClass13.this.val$iotId);
                                                    intent.putExtra(AlinkConstants.KEY_PK, AnonymousClass13.this.val$key);
                                                    DeviceAdapter.this.mContext.startActivity(intent);
                                                    return;
                                                }
                                                Intent intent2 = new Intent(DeviceAdapter.this.mContext, (Class<?>) Net4GSwitchActivity.class);
                                                Bundle bundle = new Bundle();
                                                DeviceInfoBean deviceInfoBean = new DeviceInfoBean();
                                                deviceInfoBean.setIotId(AnonymousClass13.this.val$iotId);
                                                deviceInfoBean.setProductKey(AnonymousClass13.this.val$key);
                                                deviceInfoBean.setNickName(AnonymousClass13.this.val$name);
                                                bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, deviceInfoBean);
                                                intent2.putExtras(bundle);
                                                DeviceAdapter.this.mContext.startActivity(intent2);
                                                return;
                                            }
                                            Intent intent3 = new Intent(DeviceAdapter.this.mContext, (Class<?>) Traffic4GActivity.class);
                                            intent3.putExtra("iccid", SharePreferenceManager.getInstance().getIccId(AnonymousClass13.this.val$iotId));
                                            intent3.putExtra(AlinkConstants.KEY_DN, AnonymousClass13.this.val$name);
                                            intent3.putExtra(AlinkConstants.KEY_PK, AnonymousClass13.this.val$key);
                                            DeviceAdapter.this.mContext.startActivity(intent3);
                                        }
                                    });
                                }
                            });
                        } else {
                            DeviceAdapter.this.mHandler.post(new Runnable() { // from class: adapter.DeviceAdapter.13.2.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    AnonymousClass13.this.val$progressBar.setVisibility(8);
                                    AnonymousClass13.this.val$button.setVisibility(0);
                                    AnonymousClass13.this.val$textView.setText(Html.fromHtml(DeviceAdapter.this.mContext.getResources().getString(R.string.offline_text)));
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void requestOKHttp(String str, TextView textView, Button button, AlertDialog alertDialog, String str2, String str3, ProgressBar progressBar) {
        this.isOtherCard = false;
        SettingsCtrl.getInstance().getIccIdParam(str, new AnonymousClass13(str, progressBar, button, textView, alertDialog, str2, str3));
    }

    public synchronized void refreshLowPowerStatus(int i, int i2) {
        if (i >= getData().size()) {
            return;
        }
        RecyclerView.ViewHolder viewHolderFindViewHolderForLayoutPosition = getRecyclerView().findViewHolderForLayoutPosition(i);
        if (viewHolderFindViewHolderForLayoutPosition == null) {
            return;
        }
        DeviceInfoBeans deviceInfoBeans = getData().get(i);
        if (deviceInfoBeans.getData().size() == 0) {
            return;
        }
        deviceInfoBeans.getData().get(0);
        ImageView imageView = (ImageView) viewHolderFindViewHolderForLayoutPosition.itemView.findViewById(R.id.iv_line);
        TextView textView = (TextView) viewHolderFindViewHolderForLayoutPosition.itemView.findViewById(R.id.tv_line);
        if (i2 == 0) {
            imageView.setImageResource(R.drawable.oval_yellow);
            textView.setText(R.string.dormancy);
        } else if (i2 == 1) {
            imageView.setImageResource(R.drawable.oval_green);
            textView.setText(R.string.online);
        }
    }
}
