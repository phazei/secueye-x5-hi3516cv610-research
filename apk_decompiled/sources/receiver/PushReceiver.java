package receiver;

import activity.IPCDoubleEyeActivity;
import activity.IPCThreeEyesActivity;
import activity.IPCameraActivity;
import activity.StartActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import bean.DeviceInfoBean;
import bean.DeviceInfoBeans;
import bean.PushMsgBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.alcs.lpbs.api.AlcsPalConst;
import com.google.gson.Gson;
import com.seculink.app.R;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import sdk.EnvConfigure;
import tools.ActivityManager;
import tools.LogEx;
import tools.MediaUtil;
import tools.SharePreferenceManager;
import tools.WindowUtils;

/* JADX INFO: loaded from: classes4.dex */
public class PushReceiver extends MessageReceiver {
    public static final String REC_TAG = "receiver";
    public static CountDownTimer countDownTimer;
    private Handler uiHandler = new Handler();

    @Override // com.alibaba.sdk.android.push.MessageReceiver
    public void onMessage(Context context, CPushMessage cPushMessage) {
    }

    @Override // com.alibaba.sdk.android.push.MessageReceiver
    protected void onNotificationClickedWithNoAction(Context context, String str, String str2, String str3) {
    }

    @Override // com.alibaba.sdk.android.push.MessageReceiver
    public void onNotification(Context context, String str, String str2, Map<String, String> map) {
        final Activity activity2 = ActivityManager.getInstance().getActivity();
        LogEx.e(true, "MyMessageReceiver", "Receive notification, title: " + str + ", summary: " + str2 + ", extraMap: " + map);
        final String str3 = map.get("iotId");
        if (map.containsKey("strongReminder") && ((String) Objects.requireNonNull(map.get("strongReminder"))).equals("1") && map.containsKey("iotId")) {
            MediaUtil.playRing(context);
            if (activity2 != null && Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(activity2)) {
                    Toast.makeText(activity2, context.getString(R.string.open_windows), 1).show();
                    int i = Build.VERSION.SDK_INT;
                    Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
                    if (i < 26) {
                        intent.setData(Uri.parse("package:" + activity2.getPackageName()));
                    }
                    activity2.startActivityForResult(intent, 1);
                } else {
                    List array = JSON.parseArray(SharePreferenceManager.getInstance().getAllDeviceList(), DeviceInfoBeans.class);
                    final DeviceInfoBean cellDeviceInfoBean = null;
                    for (int i2 = 0; i2 < array.size(); i2++) {
                        if (((DeviceInfoBeans) array.get(i2)).getData().get(0).getIotId().equals(str3)) {
                            cellDeviceInfoBean = ((DeviceInfoBeans) array.get(i2)).getData().get(0);
                        }
                    }
                    if (cellDeviceInfoBean == null) {
                        for (int i3 = 0; i3 < array.size(); i3++) {
                            if (((DeviceInfoBeans) array.get(i3)).getCellDeviceInfoBean().getIotId().equals(str3)) {
                                cellDeviceInfoBean = ((DeviceInfoBeans) array.get(i3)).getCellDeviceInfoBean();
                            }
                        }
                    }
                    this.uiHandler.post(new Runnable() { // from class: receiver.PushReceiver.1
                        @Override // java.lang.Runnable
                        public void run() {
                            WindowUtils.getInstance().showPopupWindow(activity2, str3, cellDeviceInfoBean);
                        }
                    });
                }
            }
            if (countDownTimer == null) {
                countDownTimer = new CountDownTimer(30000L, 5000L) { // from class: receiver.PushReceiver.2
                    @Override // android.os.CountDownTimer
                    public void onTick(long j) {
                    }

                    @Override // android.os.CountDownTimer
                    public void onFinish() {
                        cancel();
                        PushReceiver.countDownTimer = null;
                        MediaUtil.stopRing();
                        WindowUtils.getInstance().hidePopupWindow();
                    }
                };
                countDownTimer.start();
            }
        }
    }

    @Override // com.alibaba.sdk.android.push.MessageReceiver
    public void onNotificationOpened(Context context, String str, String str2, String str3) {
        Intent intent;
        Log.e("MyMessageReceiver", "onNotificationOpened, title: " + str + ", summary: " + str2 + ", extraMap:" + str3);
        PushMsgBean pushMsgBean = (PushMsgBean) new Gson().fromJson(str3, PushMsgBean.class);
        Activity activity2 = ActivityManager.getInstance().getActivity();
        if (pushMsgBean.getIotId() == null) {
            if (activity2 == null) {
                intent = new Intent(context, (Class<?>) StartActivity.class);
                intent.addFlags(268435456);
            } else {
                intent = new Intent(context, activity2.getClass());
                intent.setFlags(805306368);
            }
            context.startActivity(intent);
            return;
        }
        DeviceInfoBean deviceInfoBean = null;
        if (pushMsgBean.getStrongReminder() != 1) {
            if (activity2 == null) {
                Intent intent2 = new Intent(context, (Class<?>) StartActivity.class);
                intent2.addFlags(268435456);
                context.startActivity(intent2);
                return;
            }
            List array = JSON.parseArray(SharePreferenceManager.getInstance().getAllDeviceList(), DeviceInfoBeans.class);
            int i = 0;
            boolean z = false;
            while (true) {
                if (i >= array.size()) {
                    break;
                }
                if (((DeviceInfoBeans) array.get(i)).getCellDeviceInfoBean() != null) {
                    if (deviceInfoBean.getIotId().equals(((DeviceInfoBeans) array.get(i)).getCellDeviceInfoBean().getIotId()) && ((DeviceInfoBeans) array.get(i)).getCellDeviceInfoBean().getNodeType().equals("GATEWAY")) {
                        z = true;
                    }
                    boolean z2 = z;
                    for (int i2 = 0; i2 < ((DeviceInfoBeans) array.get(i)).getData().size(); i2++) {
                        if (deviceInfoBean.getIotId().equals(((DeviceInfoBeans) array.get(i)).getData().get(i2).getIotId()) && ((DeviceInfoBeans) array.get(i)).getCellDeviceInfoBean().getNodeType().equals("GATEWAY")) {
                            z2 = true;
                        }
                    }
                    if (z2) {
                        if (((DeviceInfoBeans) array.get(i)).getData().size() == 3) {
                            Intent intent3 = new Intent(context, (Class<?>) IPCThreeEyesActivity.class);
                            intent3.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, ((DeviceInfoBeans) array.get(i)).getData().get(0));
                            intent3.putExtra("device1", ((DeviceInfoBeans) array.get(i)).getData().get(1));
                            intent3.putExtra("device2", ((DeviceInfoBeans) array.get(i)).getData().get(2));
                            intent3.putExtra("nvrDevice", ((DeviceInfoBeans) array.get(i)).getCellDeviceInfoBean());
                            intent3.putExtra("strongRemind", 1);
                            context.startActivity(intent3);
                        } else if (((DeviceInfoBeans) array.get(i)).getData().size() == 2) {
                            Intent intent4 = new Intent(context, (Class<?>) IPCDoubleEyeActivity.class);
                            intent4.setFlags(335544320);
                            Bundle bundle = new Bundle();
                            DeviceInfoBean deviceInfoBean2 = ((DeviceInfoBeans) array.get(i)).getData().get(0);
                            bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, deviceInfoBean2);
                            intent4.putExtra("nvrOwner", ((DeviceInfoBeans) array.get(i)).getCellDeviceInfoBean().getOwned());
                            intent4.putExtra("nvrIotId", ((DeviceInfoBeans) array.get(i)).getCellDeviceInfoBean().getIotId());
                            intent4.putExtra("ballIotId", deviceInfoBean2.getIotId());
                            intent4.putExtra("strongRemind", 1);
                            if (((DeviceInfoBeans) array.get(i)).getData().size() > 1) {
                                DeviceInfoBean deviceInfoBean3 = ((DeviceInfoBeans) array.get(i)).getData().get(1);
                                bundle.putSerializable("device1", deviceInfoBean3);
                                intent4.putExtra("gunIotId", deviceInfoBean3.getIotId());
                            }
                            intent4.putExtras(bundle);
                            intent4.putExtra("appKey", EnvConfigure.getEnvArg(EnvConfigure.KEY_APPKEY));
                            Log.e("推送设备信息跳转", AlcsPalConst.MODEL_TYPE_TGMESH);
                            context.startActivity(intent4);
                        }
                        z = z2;
                    } else {
                        z = z2;
                    }
                }
                i++;
            }
            if (z) {
                return;
            }
            Intent intent5 = new Intent(context, (Class<?>) IPCameraActivity.class);
            intent5.setFlags(335544320);
            Bundle bundle2 = new Bundle();
            bundle2.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, null);
            intent5.putExtras(bundle2);
            String[] strArr = new String[4];
            strArr[0] = deviceInfoBean.getIotId();
            intent5.putExtra(AlinkConstants.KEY_LIST, strArr);
            intent5.putExtra("strongRemind", 1);
            Log.e("推送设备信息跳转", "2");
            context.startActivity(intent5);
            return;
        }
        if (activity2 == null) {
            MediaUtil.stopRing();
            CountDownTimer countDownTimer2 = countDownTimer;
            if (countDownTimer2 != null) {
                countDownTimer2.cancel();
                countDownTimer = null;
            }
            List array2 = JSON.parseArray(SharePreferenceManager.getInstance().getAllDeviceList(), DeviceInfoBeans.class);
            int i3 = 0;
            boolean z3 = false;
            while (true) {
                if (i3 >= array2.size()) {
                    break;
                }
                if (((DeviceInfoBeans) array2.get(i3)).getCellDeviceInfoBean() != null) {
                    if (deviceInfoBean.getIotId().equals(((DeviceInfoBeans) array2.get(i3)).getCellDeviceInfoBean().getIotId()) && ((DeviceInfoBeans) array2.get(i3)).getCellDeviceInfoBean().getNodeType().equals("GATEWAY")) {
                        z3 = true;
                    }
                    boolean z4 = z3;
                    for (int i4 = 0; i4 < ((DeviceInfoBeans) array2.get(i3)).getData().size(); i4++) {
                        if (deviceInfoBean.getIotId().equals(((DeviceInfoBeans) array2.get(i3)).getData().get(i4).getIotId()) && ((DeviceInfoBeans) array2.get(i3)).getCellDeviceInfoBean().getNodeType().equals("GATEWAY")) {
                            z4 = true;
                        }
                    }
                    if (z4) {
                        if (((DeviceInfoBeans) array2.get(i3)).getData().size() == 3) {
                            Intent intent6 = new Intent(context, (Class<?>) IPCThreeEyesActivity.class);
                            intent6.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, ((DeviceInfoBeans) array2.get(i3)).getData().get(0));
                            intent6.putExtra("device1", ((DeviceInfoBeans) array2.get(i3)).getData().get(1));
                            intent6.putExtra("device2", ((DeviceInfoBeans) array2.get(i3)).getData().get(2));
                            intent6.putExtra("nvrDevice", ((DeviceInfoBeans) array2.get(i3)).getCellDeviceInfoBean());
                            intent6.putExtra("strongRemind", 1);
                            context.startActivity(intent6);
                        } else if (((DeviceInfoBeans) array2.get(i3)).getData().size() == 2) {
                            Intent intent7 = new Intent(context, (Class<?>) IPCDoubleEyeActivity.class);
                            intent7.setFlags(335544320);
                            Bundle bundle3 = new Bundle();
                            DeviceInfoBean deviceInfoBean4 = ((DeviceInfoBeans) array2.get(i3)).getData().get(0);
                            bundle3.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, deviceInfoBean4);
                            intent7.putExtra("nvrOwner", ((DeviceInfoBeans) array2.get(i3)).getCellDeviceInfoBean().getOwned());
                            intent7.putExtra("nvrIotId", ((DeviceInfoBeans) array2.get(i3)).getCellDeviceInfoBean().getIotId());
                            intent7.putExtra("ballIotId", deviceInfoBean4.getIotId());
                            intent7.putExtra("strongRemind", 1);
                            if (((DeviceInfoBeans) array2.get(i3)).getData().size() > 1) {
                                DeviceInfoBean deviceInfoBean5 = ((DeviceInfoBeans) array2.get(i3)).getData().get(1);
                                bundle3.putSerializable("device1", deviceInfoBean5);
                                intent7.putExtra("gunIotId", deviceInfoBean5.getIotId());
                            }
                            intent7.putExtras(bundle3);
                            intent7.putExtra("appKey", EnvConfigure.getEnvArg(EnvConfigure.KEY_APPKEY));
                            Log.e("推送设备信息跳转", AlcsPalConst.MODEL_TYPE_TGMESH);
                            context.startActivity(intent7);
                        }
                        z3 = z4;
                    } else {
                        z3 = z4;
                    }
                }
                i3++;
            }
            if (z3) {
                return;
            }
            Intent intent8 = new Intent(context, (Class<?>) IPCameraActivity.class);
            intent8.setFlags(335544320);
            Bundle bundle4 = new Bundle();
            bundle4.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, null);
            intent8.putExtras(bundle4);
            String[] strArr2 = new String[4];
            strArr2[0] = deviceInfoBean.getIotId();
            intent8.putExtra(AlinkConstants.KEY_LIST, strArr2);
            intent8.putExtra("strongRemind", 1);
            Log.e("推送设备信息跳转", "2");
            context.startActivity(intent8);
            return;
        }
        MediaUtil.stopRing();
        CountDownTimer countDownTimer3 = countDownTimer;
        if (countDownTimer3 != null) {
            countDownTimer3.cancel();
            countDownTimer = null;
        }
        List array3 = JSON.parseArray(SharePreferenceManager.getInstance().getAllDeviceList(), DeviceInfoBeans.class);
        int i5 = 0;
        boolean z5 = false;
        while (true) {
            if (i5 >= array3.size()) {
                break;
            }
            if (((DeviceInfoBeans) array3.get(i5)).getCellDeviceInfoBean() != null) {
                if (deviceInfoBean.getIotId().equals(((DeviceInfoBeans) array3.get(i5)).getCellDeviceInfoBean().getIotId()) && ((DeviceInfoBeans) array3.get(i5)).getCellDeviceInfoBean().getNodeType().equals("GATEWAY")) {
                    z5 = true;
                }
                boolean z6 = z5;
                for (int i6 = 0; i6 < ((DeviceInfoBeans) array3.get(i5)).getData().size(); i6++) {
                    if (deviceInfoBean.getIotId().equals(((DeviceInfoBeans) array3.get(i5)).getData().get(i6).getIotId()) && ((DeviceInfoBeans) array3.get(i5)).getCellDeviceInfoBean().getNodeType().equals("GATEWAY")) {
                        z6 = true;
                    }
                }
                if (z6) {
                    if (((DeviceInfoBeans) array3.get(i5)).getData().size() == 3) {
                        Intent intent9 = new Intent(context, (Class<?>) IPCThreeEyesActivity.class);
                        intent9.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, ((DeviceInfoBeans) array3.get(i5)).getData().get(0));
                        intent9.putExtra("device1", ((DeviceInfoBeans) array3.get(i5)).getData().get(1));
                        intent9.putExtra("device2", ((DeviceInfoBeans) array3.get(i5)).getData().get(2));
                        intent9.putExtra("nvrDevice", ((DeviceInfoBeans) array3.get(i5)).getCellDeviceInfoBean());
                        intent9.putExtra("strongRemind", 1);
                        context.startActivity(intent9);
                    } else if (((DeviceInfoBeans) array3.get(i5)).getData().size() == 2) {
                        Intent intent10 = new Intent(context, (Class<?>) IPCDoubleEyeActivity.class);
                        intent10.setFlags(335544320);
                        Bundle bundle5 = new Bundle();
                        DeviceInfoBean deviceInfoBean6 = ((DeviceInfoBeans) array3.get(i5)).getData().get(0);
                        bundle5.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, deviceInfoBean6);
                        intent10.putExtra("nvrOwner", ((DeviceInfoBeans) array3.get(i5)).getCellDeviceInfoBean().getOwned());
                        intent10.putExtra("nvrIotId", ((DeviceInfoBeans) array3.get(i5)).getCellDeviceInfoBean().getIotId());
                        intent10.putExtra("ballIotId", deviceInfoBean6.getIotId());
                        intent10.putExtra("strongRemind", 1);
                        if (((DeviceInfoBeans) array3.get(i5)).getData().size() > 1) {
                            DeviceInfoBean deviceInfoBean7 = ((DeviceInfoBeans) array3.get(i5)).getData().get(1);
                            bundle5.putSerializable("device1", deviceInfoBean7);
                            intent10.putExtra("gunIotId", deviceInfoBean7.getIotId());
                        }
                        intent10.putExtras(bundle5);
                        intent10.putExtra("appKey", EnvConfigure.getEnvArg(EnvConfigure.KEY_APPKEY));
                        Log.e("推送设备信息跳转", AlcsPalConst.MODEL_TYPE_TGMESH);
                        context.startActivity(intent10);
                    }
                    z5 = z6;
                } else {
                    z5 = z6;
                }
            }
            i5++;
        }
        if (z5) {
            return;
        }
        Intent intent11 = new Intent(context, (Class<?>) IPCameraActivity.class);
        intent11.setFlags(335544320);
        Bundle bundle6 = new Bundle();
        bundle6.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, null);
        intent11.putExtras(bundle6);
        String[] strArr3 = new String[4];
        strArr3[0] = deviceInfoBean.getIotId();
        intent11.putExtra(AlinkConstants.KEY_LIST, strArr3);
        intent11.putExtra("strongRemind", 1);
        Log.e("推送设备信息跳转", "2");
        context.startActivity(intent11);
    }

    @Override // com.alibaba.sdk.android.push.MessageReceiver
    protected void onNotificationReceivedInApp(Context context, String str, String str2, Map<String, String> map, int i, String str3, String str4) {
        LogEx.e(true, "MyMessageReceiver", "onNotificationReceivedInApp, title: " + str + ", summary: " + str2 + ", extraMap: " + map.toString() + ",openType：" + i + ",openActivity:" + str3 + ",openUrl:" + str4);
    }

    @Override // com.alibaba.sdk.android.push.MessageReceiver
    protected void onNotificationRemoved(Context context, String str) {
        Log.e("MyMessageReceiver", "onNotificationRemoved");
        Log.e("MyMessageReceiver", "onNotificationRemoved:messageId====" + str);
        MediaUtil.stopRing();
        CountDownTimer countDownTimer2 = countDownTimer;
        if (countDownTimer2 != null) {
            countDownTimer2.cancel();
            countDownTimer = null;
        }
    }

    public static void stopAlarm() {
        MediaUtil.stopRing();
        CountDownTimer countDownTimer2 = countDownTimer;
        if (countDownTimer2 != null) {
            countDownTimer2.cancel();
            countDownTimer = null;
        }
    }
}
