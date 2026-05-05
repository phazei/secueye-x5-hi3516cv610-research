package tools;

import activity.IPCDoubleEyeActivity;
import activity.IPCThreeEyesActivity;
import activity.IPCameraActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import bean.DeviceInfoBean;
import bean.DeviceInfoBeans;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.alcs.lpbs.api.AlcsPalConst;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iotx.linkvisual.media.LinkVisualMedia;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seculink.app.R;
import java.util.HashMap;
import java.util.List;
import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;
import receiver.PushReceiver;
import sdk.EnvConfigure;

/* JADX INFO: loaded from: classes4.dex */
public class WindowUtils {
    private static WindowUtils windowUtils;
    private final String LOG_TAG = "WindowUtils";
    private View mView = null;
    private WindowManager mWindowManager = null;
    private Context mContext = null;
    public Boolean isShown = false;

    interface back {
        void str(String str);
    }

    public static synchronized WindowUtils getInstance() {
        if (windowUtils == null) {
            synchronized (WindowUtils.class) {
                if (windowUtils == null) {
                    windowUtils = new WindowUtils();
                }
            }
        }
        return windowUtils;
    }

    public void showPopupWindow(Context context, String str, DeviceInfoBean deviceInfoBean) {
        Log.e("离线推送内容body", "9" + deviceInfoBean);
        if (this.isShown.booleanValue() || deviceInfoBean == null) {
            return;
        }
        Log.e("离线推送内容body", AgooConstants.ACK_REMOVE_PACKAGE);
        this.isShown = true;
        this.mContext = context.getApplicationContext();
        this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        this.mView = setUpView(context, str, deviceInfoBean);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = 2038;
        layoutParams.flags = 32;
        layoutParams.format = -3;
        layoutParams.gravity = 49;
        layoutParams.y = 20;
        layoutParams.width = (int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.95d);
        layoutParams.height = DensityUtil.dip2px(context, 103.0f);
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(context)) {
                context.startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION"));
                return;
            } else {
                Log.e("离线推送内容body", AgooConstants.ACK_BODY_NULL);
                this.mWindowManager.addView(this.mView, layoutParams);
                return;
            }
        }
        Log.e("离线推送内容body", AgooConstants.ACK_PACK_NULL);
        this.mWindowManager.addView(this.mView, layoutParams);
    }

    public void hidePopupWindow() {
        View view2;
        if (!this.isShown.booleanValue() || (view2 = this.mView) == null) {
            return;
        }
        this.mWindowManager.removeView(view2);
        this.isShown = false;
    }

    @SuppressLint({"SetTextI18n"})
    private View setUpView(final Context context, String str, final DeviceInfoBean deviceInfoBean) {
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.popupwindow, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.call_name_text);
        if (deviceInfoBean != null && deviceInfoBean.getName() != null) {
            textView.setText(deviceInfoBean.getName() + context.getString(R.string.alarm_event_occurring));
        }
        ((LinearLayout) viewInflate.findViewById(R.id.positiveBtn)).setOnClickListener(new View.OnClickListener() { // from class: tools.WindowUtils.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PushReceiver.stopAlarm();
                if (deviceInfoBean.getStatus() == 1) {
                    LinkVisualMedia.getInstance().preConnectByIotId(deviceInfoBean.getIotId());
                }
                List list = (List) new Gson().fromJson(SharePreferenceManager.getInstance().getAllDeviceList(), new TypeToken<List<DeviceInfoBeans>>() { // from class: tools.WindowUtils.1.1
                }.getType());
                int i = 0;
                boolean z = false;
                while (true) {
                    if (i >= list.size()) {
                        break;
                    }
                    if (((DeviceInfoBeans) list.get(i)).getCellDeviceInfoBean() != null) {
                        if (deviceInfoBean.getIotId().equals(((DeviceInfoBeans) list.get(i)).getCellDeviceInfoBean().getIotId()) && ((DeviceInfoBeans) list.get(i)).getCellDeviceInfoBean().getNodeType().equals("GATEWAY")) {
                            z = true;
                        }
                        boolean z2 = z;
                        for (int i2 = 0; i2 < ((DeviceInfoBeans) list.get(i)).getData().size(); i2++) {
                            if (deviceInfoBean.getIotId().equals(((DeviceInfoBeans) list.get(i)).getData().get(i2).getIotId()) && ((DeviceInfoBeans) list.get(i)).getCellDeviceInfoBean().getNodeType().equals("GATEWAY")) {
                                z2 = true;
                            }
                        }
                        if (z2) {
                            if (((DeviceInfoBeans) list.get(i)).getData().size() == 3) {
                                Intent intent = new Intent(context, (Class<?>) IPCThreeEyesActivity.class);
                                intent.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, ((DeviceInfoBeans) list.get(i)).getData().get(0));
                                intent.putExtra("device1", ((DeviceInfoBeans) list.get(i)).getData().get(1));
                                intent.putExtra("device2", ((DeviceInfoBeans) list.get(i)).getData().get(2));
                                intent.putExtra("nvrDevice", ((DeviceInfoBeans) list.get(i)).getCellDeviceInfoBean());
                                intent.putExtra("strongRemind", 1);
                                context.startActivity(intent);
                            } else if (((DeviceInfoBeans) list.get(i)).getData().size() == 2) {
                                Intent intent2 = new Intent(context, (Class<?>) IPCDoubleEyeActivity.class);
                                intent2.setFlags(335544320);
                                Bundle bundle = new Bundle();
                                DeviceInfoBean deviceInfoBean2 = ((DeviceInfoBeans) list.get(i)).getData().get(0);
                                bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, deviceInfoBean2);
                                intent2.putExtra("nvrOwner", ((DeviceInfoBeans) list.get(i)).getCellDeviceInfoBean().getOwned());
                                intent2.putExtra("nvrIotId", ((DeviceInfoBeans) list.get(i)).getCellDeviceInfoBean().getIotId());
                                intent2.putExtra("ballIotId", deviceInfoBean2.getIotId());
                                intent2.putExtra("strongRemind", 1);
                                if (((DeviceInfoBeans) list.get(i)).getData().size() > 1) {
                                    DeviceInfoBean deviceInfoBean3 = ((DeviceInfoBeans) list.get(i)).getData().get(1);
                                    bundle.putSerializable("device1", deviceInfoBean3);
                                    intent2.putExtra("gunIotId", deviceInfoBean3.getIotId());
                                }
                                intent2.putExtras(bundle);
                                intent2.putExtra("appKey", EnvConfigure.getEnvArg(EnvConfigure.KEY_APPKEY));
                                Log.e("推送设备信息跳转", AlcsPalConst.MODEL_TYPE_TGMESH);
                                context.startActivity(intent2);
                            }
                            z = z2;
                        } else {
                            z = z2;
                        }
                    }
                    i++;
                }
                if (!z) {
                    Intent intent3 = new Intent(context, (Class<?>) IPCameraActivity.class);
                    intent3.setFlags(335544320);
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, deviceInfoBean);
                    intent3.putExtras(bundle2);
                    String[] strArr = new String[4];
                    strArr[0] = deviceInfoBean.getIotId();
                    intent3.putExtra(AlinkConstants.KEY_LIST, strArr);
                    intent3.putExtra("strongRemind", 1);
                    intent3.putExtra("iotId", deviceInfoBean.getIotId());
                    context.startActivity(intent3);
                }
                WindowUtils.getInstance().hidePopupWindow();
            }
        });
        ((LinearLayout) viewInflate.findViewById(R.id.negativeBtn)).setOnClickListener(new View.OnClickListener() { // from class: tools.WindowUtils.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PushReceiver.stopAlarm();
                WindowUtils.getInstance().hidePopupWindow();
            }
        });
        return viewInflate;
    }

    public void getSharePersonsList(String str, final back backVar) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put(AlinkConstants.KEY_PAGE_NO, 1);
        map.put(AlinkConstants.KEY_PAGE_SIZE, 100);
        map.put("owned", 1);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/uc/listBindingByDev").setScheme(Scheme.HTTPS).setApiVersion("1.0.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: tools.WindowUtils.3
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.d("TAG", "onFailure");
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
