package activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import bean.DeviceInfoBean;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.bumptech.glide.Glide;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityBleNet4gSwitchBinding;
import com.tencent.mm.opensdk.modelbiz.WXOpenCustomerServiceChat;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import config.AppConfig;
import config.Constants;
import dialog.DialogUtil;
import event.EventType;
import event.MyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.greenrobot.eventbus.EventBus;
import sdk.IPCManager;
import tools.OnMultiClickListener;
import tools.SharePreferenceManager;
import view.SelectorDialogFragment;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class BleNet4GSwitchActivity extends CommonActivity {
    private ActivityBleNet4gSwitchBinding binding;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private String[] net4GModeArr;
    private SelectorDialogFragment net4gModeFragment;
    private DeviceInfoBean nvrDevice;
    private String[] operatorName;
    private String[] switch_4gArr;
    private int net4gSwitch = 1;
    private Handler uiHandler = new Handler(Looper.myLooper());
    private int[] operatorIcon = {R.drawable.ic_mobile, R.drawable.ic_unicom, R.drawable.ic_telecom};
    private int[] operatorColor = {R.color.color_98C143, R.color.color_ff0000, R.color.color_485ff6};
    private String[] operatorStr = {"China Mobile", "China Unicom", "China Telecom"};

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_ble_net_4g_switch;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityBleNet4gSwitchBinding) DataBindingUtil.setContentView(this, R.layout.activity_ble_net_4g_switch);
        setEdgeToEdge(this.binding.layoutMain);
        this.operatorName = new String[]{getString(R.string.china_mobile), getString(R.string.china_unicom), getString(R.string.china_telecom)};
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
        }
        this.binding.flTitlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.BleNet4GSwitchActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                BleNet4GSwitchActivity.this.finish();
            }

            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
                BleNet4GSwitchActivity.this.binding.simOfficial.setVisibility(8);
                BleNet4GSwitchActivity.this.binding.simExternal.setVisibility(8);
                BleNet4GSwitchActivity.this.setData();
            }
        });
        this.binding.tvOfficialIccid.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: activity.BleNet4GSwitchActivity.2
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                BleNet4GSwitchActivity.this.binding.tvOfficialIccid.getViewTreeObserver().removeOnPreDrawListener(this);
                int width = BleNet4GSwitchActivity.this.binding.tvOfficialIccid.getWidth();
                CommonActivity.setViewLayoutParams(BleNet4GSwitchActivity.this.binding.layoutOfficial, width, -2);
                CommonActivity.setViewLayoutParams(BleNet4GSwitchActivity.this.binding.layoutExternal, width, -2);
                return true;
            }
        });
        this.net4GModeArr = getResources().getStringArray(R.array.net_4g_Switch);
        if (((SharePreferenceManager.getInstance().getPageControlEx(this.device.getIotId()) & 524288) >> 19) == 1) {
            this.net4gSwitch = SharePreferenceManager.getInstance().getNet4GMode(this.device.getIotId());
            this.binding.item4gSwitch.setVisibility(0);
            this.binding.item4gSwitch.setRightText(this.net4GModeArr[this.net4gSwitch == 1 ? (char) 0 : (char) 1]);
            this.binding.simOfficial.setSelected(this.net4gSwitch == 1);
            this.binding.simExternal.setSelected(this.net4gSwitch == 2);
        }
        this.net4gModeFragment = new SelectorDialogFragment(getString(R.string.net_4G_text), this.net4GModeArr);
        this.net4gModeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.BleNet4GSwitchActivity.3
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                BleNet4GSwitchActivity bleNet4GSwitchActivity = BleNet4GSwitchActivity.this;
                bleNet4GSwitchActivity.net4gMode(bleNet4GSwitchActivity.getString(R.string.Net4GMode), i == 0 ? 1 : 2);
            }
        });
        setData();
        this.binding.item4gSwitch.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleNet4GSwitchActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                BleNet4GSwitchActivity.this.net4gModeFragment.showAllowingStateLoss(BleNet4GSwitchActivity.this.getSupportFragmentManager(), "", BleNet4GSwitchActivity.this.net4gSwitch == 1 ? 0 : 1);
            }
        });
        this.binding.tvOfficialSelect.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleNet4GSwitchActivity.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (SharePreferenceManager.getInstance().getURL(BleNet4GSwitchActivity.this.device.getIotId()) == null || SharePreferenceManager.getInstance().getURL(BleNet4GSwitchActivity.this.device.getIotId()).equals("")) {
                    if ("".equals(SharePreferenceManager.getInstance().getIccId1(BleNet4GSwitchActivity.this.device.getIotId()))) {
                        return;
                    }
                    if (BleNet4GSwitchActivity.this.nvrDevice != null) {
                        Intent intent = new Intent(BleNet4GSwitchActivity.this.getActivity(), (Class<?>) Traffic4GActivity.class);
                        intent.putExtra("iccid", SharePreferenceManager.getInstance().getIccId1(BleNet4GSwitchActivity.this.device.getIotId()));
                        intent.putExtra("iotId", BleNet4GSwitchActivity.this.nvrDevice.getIotId());
                        intent.putExtra(AlinkConstants.KEY_DN, BleNet4GSwitchActivity.this.nvrDevice.getDeviceName());
                        intent.putExtra(AlinkConstants.KEY_PK, BleNet4GSwitchActivity.this.nvrDevice.getProductKey());
                        BleNet4GSwitchActivity.this.startActivity(intent);
                        return;
                    }
                    Intent intent2 = new Intent(BleNet4GSwitchActivity.this.getActivity(), (Class<?>) Traffic4GActivity.class);
                    intent2.putExtra("iccid", SharePreferenceManager.getInstance().getIccId1(BleNet4GSwitchActivity.this.device.getIotId()));
                    intent2.putExtra("iotId", BleNet4GSwitchActivity.this.device.getIotId());
                    intent2.putExtra(AlinkConstants.KEY_DN, BleNet4GSwitchActivity.this.device.getDeviceName());
                    intent2.putExtra(AlinkConstants.KEY_PK, BleNet4GSwitchActivity.this.device.getProductKey());
                    BleNet4GSwitchActivity.this.startActivity(intent2);
                    return;
                }
                Log.e("外卡连接", "" + SharePreferenceManager.getInstance().getURL(BleNet4GSwitchActivity.this.device.getIotId()));
                Intent intent3 = new Intent(BleNet4GSwitchActivity.this.getActivity(), (Class<?>) Traffic4GNotOfficiallyActivity.class);
                intent3.putExtra(Constants.URLSlave, SharePreferenceManager.getInstance().getURL(BleNet4GSwitchActivity.this.device.getIotId()));
                BleNet4GSwitchActivity.this.startActivity(intent3);
            }
        });
        this.binding.tvExternalSelect.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleNet4GSwitchActivity.6
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (SharePreferenceManager.getInstance().getURLSlave(BleNet4GSwitchActivity.this.device.getIotId()) == null || SharePreferenceManager.getInstance().getURLSlave(BleNet4GSwitchActivity.this.device.getIotId()).equals("") || "".equals(SharePreferenceManager.getInstance().getIccId2(BleNet4GSwitchActivity.this.device.getIotId()))) {
                    if ("".equals(SharePreferenceManager.getInstance().getIccId2(BleNet4GSwitchActivity.this.device.getIotId()))) {
                        return;
                    }
                    if (BleNet4GSwitchActivity.this.nvrDevice != null) {
                        Intent intent = new Intent(BleNet4GSwitchActivity.this.getActivity(), (Class<?>) Traffic4GActivity.class);
                        intent.putExtra("iccid", SharePreferenceManager.getInstance().getIccId2(BleNet4GSwitchActivity.this.device.getIotId()));
                        intent.putExtra("iotId", BleNet4GSwitchActivity.this.nvrDevice.getIotId());
                        intent.putExtra(AlinkConstants.KEY_DN, BleNet4GSwitchActivity.this.nvrDevice.getDeviceName());
                        intent.putExtra(AlinkConstants.KEY_PK, BleNet4GSwitchActivity.this.nvrDevice.getProductKey());
                        BleNet4GSwitchActivity.this.startActivity(intent);
                        return;
                    }
                    Intent intent2 = new Intent(BleNet4GSwitchActivity.this.getActivity(), (Class<?>) Traffic4GActivity.class);
                    intent2.putExtra("iccid", SharePreferenceManager.getInstance().getIccId2(BleNet4GSwitchActivity.this.device.getIotId()));
                    intent2.putExtra("iotId", BleNet4GSwitchActivity.this.device.getIotId());
                    intent2.putExtra(AlinkConstants.KEY_DN, BleNet4GSwitchActivity.this.device.getDeviceName());
                    intent2.putExtra(AlinkConstants.KEY_PK, BleNet4GSwitchActivity.this.device.getProductKey());
                    BleNet4GSwitchActivity.this.startActivity(intent2);
                    return;
                }
                if (SharePreferenceManager.getInstance().getURLSlave(BleNet4GSwitchActivity.this.device.getIotId()) == null || SharePreferenceManager.getInstance().getURLSlave(BleNet4GSwitchActivity.this.device.getIotId()).equals("")) {
                    return;
                }
                Log.e("外卡连接", "" + SharePreferenceManager.getInstance().getURLSlave(BleNet4GSwitchActivity.this.device.getIotId()));
                Intent intent3 = new Intent(BleNet4GSwitchActivity.this.getActivity(), (Class<?>) Traffic4GNotOfficiallyActivity.class);
                intent3.putExtra(Constants.URLSlave, SharePreferenceManager.getInstance().getURLSlave(BleNet4GSwitchActivity.this.device.getIotId()));
                BleNet4GSwitchActivity.this.startActivity(intent3);
            }
        });
        this.binding.layoutCustomer.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleNet4GSwitchActivity.7
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IWXAPI iwxapiCreateWXAPI = WXAPIFactory.createWXAPI(BleNet4GSwitchActivity.this, AppConfig.WX_APP_ID);
                if (iwxapiCreateWXAPI.getWXAppSupportAPI() >= 671090490) {
                    WXOpenCustomerServiceChat.Req req = new WXOpenCustomerServiceChat.Req();
                    req.corpId = "ww6d78c293de291842";
                    req.url = "https://work.weixin.qq.com/kfid/kfc4e35bb68a365822f";
                    iwxapiCreateWXAPI.sendReq(req);
                }
            }
        });
        this.binding.layoutWechat.setOnClickListener(new OnMultiClickListener() { // from class: activity.BleNet4GSwitchActivity.8
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                DialogUtil.showWeChatDiaLog(BleNet4GSwitchActivity.this, new DialogUtil.OnConfirmClickListener() { // from class: activity.BleNet4GSwitchActivity.8.1
                    @Override // dialog.DialogUtil.OnConfirmClickListener
                    public void ConfirmListener() {
                        Bitmap bitmapDecodeResource = BitmapFactory.decodeResource(BleNet4GSwitchActivity.this.getResources(), R.drawable.qrcode);
                        BleNet4GSwitchActivity.this.saveBitmap(bitmapDecodeResource, System.currentTimeMillis() + ".jpg");
                        Toast.makeText(BleNet4GSwitchActivity.this, "保存成功", 0).show();
                    }
                });
            }
        });
    }

    public boolean saveBitmap(Bitmap bitmap, String str) {
        String str2;
        String str3 = Build.BRAND;
        if (str3.equals("xiaomi")) {
            str2 = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + str;
        } else if (str3.equalsIgnoreCase("Huawei")) {
            str2 = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + str;
        } else {
            str2 = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + str;
        }
        if (Build.VERSION.SDK_INT >= 29) {
            saveSignImage(str, bitmap);
            return true;
        }
        Log.v("saveBitmap brand", "" + str3);
        File file = new File(str2);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream)) {
                fileOutputStream.flush();
                fileOutputStream.close();
                if (Build.VERSION.SDK_INT >= 29) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("_data", file.getAbsolutePath());
                    contentValues.put("mime_type", "image/jpeg");
                    getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                } else {
                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), str, (String) null);
                }
            }
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + str2)));
            return true;
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", "FileNotFoundException:" + e.getMessage().toString());
            e.printStackTrace();
            return false;
        } catch (IOException e2) {
            Log.e("IOException", "IOException:" + e2.getMessage().toString());
            e2.printStackTrace();
            return false;
        } catch (Exception e3) {
            Log.e("IOException", "IOException:" + e3.getMessage().toString());
            e3.printStackTrace();
            return false;
        }
    }

    public void saveSignImage(String str, Bitmap bitmap) {
        OutputStream outputStreamOpenOutputStream;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", str);
            if (Build.VERSION.SDK_INT >= 29) {
                contentValues.put("relative_path", "DCIM/");
            } else {
                contentValues.put("_data", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
            }
            contentValues.put("mime_type", "image/JPEG");
            Uri uriInsert = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uriInsert == null || (outputStreamOpenOutputStream = getContentResolver().openOutputStream(uriInsert)) == null) {
                return;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStreamOpenOutputStream);
            outputStreamOpenOutputStream.flush();
            outputStreamOpenOutputStream.close();
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setData() {
        int i = 0;
        if (!SharePreferenceManager.getInstance().getIccId1(this.device.getIotId()).equals("")) {
            this.binding.simOfficial.setVisibility(0);
            if (!SharePreferenceManager.getInstance().getIccId1(this.device.getIotId()).equals("")) {
                this.binding.tvOfficialIccid.setText("ICCID:" + SharePreferenceManager.getInstance().getIccId1(this.device.getIotId()));
                getICCICInfo(SharePreferenceManager.getInstance().getIccId1(this.device.getIotId()), this.binding.tvOfficialState, this.binding.tvOfficialSelect);
            }
            int i2 = 0;
            while (true) {
                String[] strArr = this.operatorStr;
                if (i2 >= strArr.length) {
                    break;
                }
                if (strArr[i2].equals(SharePreferenceManager.getInstance().getCarrier1(this.device.getIotId()))) {
                    setOperator(i2, this.binding.ivOfficialOperator, this.binding.tvOfficialOperator);
                }
                i2++;
            }
        }
        if ("".equals(SharePreferenceManager.getInstance().getIccId2(this.device.getIotId()))) {
            return;
        }
        this.binding.simExternal.setVisibility(0);
        if (!SharePreferenceManager.getInstance().getIccId2(this.device.getIotId()).equals("")) {
            this.binding.tvExternalIccid.setText("ICCID:" + SharePreferenceManager.getInstance().getIccId2(this.device.getIotId()));
            getICCICInfo(SharePreferenceManager.getInstance().getIccId2(this.device.getIotId()), this.binding.tvExternalState, this.binding.tvExternalSelect);
        }
        while (true) {
            String[] strArr2 = this.operatorStr;
            if (i >= strArr2.length) {
                return;
            }
            if (strArr2[i].equals(SharePreferenceManager.getInstance().getCarrier2(this.device.getIotId()))) {
                setOperator(i, this.binding.ivExternalOperator, this.binding.tvExternalOperator);
            }
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void net4gMode(String str, int i) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.Net4GMode))) {
            map.put(Constants.Net4GMode, Integer.valueOf(i));
        }
        IPCManager.getInstance().getDevice(this.device.getIotId()).setProperties(map, new AnonymousClass9(i));
    }

    /* JADX INFO: renamed from: activity.BleNet4GSwitchActivity$9, reason: invalid class name */
    class AnonymousClass9 implements IPanelCallback {
        final /* synthetic */ int val$newValue;

        AnonymousClass9(int i) {
            this.val$newValue = i;
        }

        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
        public void onComplete(boolean z, Object obj) {
            if (z) {
                if (obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        BleNet4GSwitchActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleNet4GSwitchActivity.9.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(BleNet4GSwitchActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                        return;
                    }
                    BleNet4GSwitchActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleNet4GSwitchActivity.9.2
                        @Override // java.lang.Runnable
                        public void run() {
                            DialogUtil.showSuccessDiaLog(BleNet4GSwitchActivity.this, BleNet4GSwitchActivity.this.getString(R.string.config_success), 280, 5000);
                            new Handler().postDelayed(new Runnable() { // from class: activity.BleNet4GSwitchActivity.9.2.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    EventBus.getDefault().post(new MyEvent(EventType.FINISH_ACTIVITY, null));
                                    BleNet4GSwitchActivity.this.finish();
                                }
                            }, 5000L);
                            SharePreferenceManager.getInstance().setNet4GMode(BleNet4GSwitchActivity.this.device.getIotId(), AnonymousClass9.this.val$newValue);
                            BleNet4GSwitchActivity.this.net4gSwitch = AnonymousClass9.this.val$newValue;
                            BleNet4GSwitchActivity.this.binding.item4gSwitch.setRightText(BleNet4GSwitchActivity.this.net4GModeArr[BleNet4GSwitchActivity.this.net4gSwitch == 1 ? (char) 0 : (char) 1]);
                            BleNet4GSwitchActivity.this.binding.simOfficial.setSelected(BleNet4GSwitchActivity.this.net4gSwitch == 1);
                            BleNet4GSwitchActivity.this.binding.simExternal.setSelected(BleNet4GSwitchActivity.this.net4gSwitch == 2);
                        }
                    });
                    Log.e("4G卡切换存储", this.val$newValue + "" + SharePreferenceManager.getInstance().getNet4GMode(BleNet4GSwitchActivity.this.device.getIotId()));
                    return;
                }
                return;
            }
            BleNet4GSwitchActivity.this.runOnUiThread(new Runnable() { // from class: activity.BleNet4GSwitchActivity.9.3
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(BleNet4GSwitchActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                }
            });
        }
    }

    private void getICCICInfo(String str, final TextView textView, final TextView textView2) {
        System.currentTimeMillis();
        new OkHttpClient.Builder().connectTimeout(5L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).build().newCall(new Request.Builder().url("http://www.secueye.cn:8000/api/smsApi?iccid=" + str + "&method=smsStatusSecueye").get().build()).enqueue(new Callback() { // from class: activity.BleNet4GSwitchActivity.10
            static final /* synthetic */ boolean $assertionsDisabled = false;

            @Override // okhttp3.Callback
            public void onFailure(@NonNull Call call, @NonNull IOException iOException) {
            }

            @Override // okhttp3.Callback
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                JSONObject object = JSONObject.parseObject(response.body().string());
                if (object.containsKey("code")) {
                    final int iIntValue = object.getInteger("code").intValue();
                    BleNet4GSwitchActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleNet4GSwitchActivity.10.1
                        @Override // java.lang.Runnable
                        public void run() {
                            textView2.setSelected(iIntValue == 200);
                        }
                    });
                    if (iIntValue != 200) {
                        BleNet4GSwitchActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleNet4GSwitchActivity.10.3
                            @Override // java.lang.Runnable
                            public void run() {
                                ((GradientDrawable) textView2.getBackground()).setColor(BleNet4GSwitchActivity.this.getColor(R.color.color_ff0000));
                                textView2.setText(BleNet4GSwitchActivity.this.getString(R.string.query_timeout));
                            }
                        });
                        return;
                    }
                    if (object.containsKey("values")) {
                        try {
                            JSONObject jSONObject = object.getJSONObject("values");
                            if (jSONObject.containsKey("status")) {
                                final String string = jSONObject.getString("status");
                                BleNet4GSwitchActivity.this.uiHandler.post(new Runnable() { // from class: activity.BleNet4GSwitchActivity.10.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (string.equals("在用")) {
                                            textView.setText(BleNet4GSwitchActivity.this.getString(R.string.state_occupant));
                                            textView.setTextColor(BleNet4GSwitchActivity.this.getColor(R.color.color_305e0f));
                                        } else if (string.equals("在用")) {
                                            textView.setText(BleNet4GSwitchActivity.this.getString(R.string.state_deactivate));
                                            textView.setTextColor(BleNet4GSwitchActivity.this.getColor(R.color.color_ff0000));
                                        } else {
                                            textView.setText(BleNet4GSwitchActivity.this.getString(R.string.state_unknown));
                                            textView.setTextColor(BleNet4GSwitchActivity.this.getColor(R.color.color_ff0000));
                                        }
                                        ((GradientDrawable) textView2.getBackground()).setColor(BleNet4GSwitchActivity.this.getColor(R.color.color_5D9CE9));
                                        textView2.setText(BleNet4GSwitchActivity.this.getString(R.string.query_flow));
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void setOperator(int i, ImageView imageView, TextView textView) {
        Glide.with((FragmentActivity) this).load(Integer.valueOf(this.operatorIcon[i])).into(imageView);
        textView.setText(this.operatorName[i]);
        textView.setTextColor(this.operatorColor[i]);
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
    }
}
