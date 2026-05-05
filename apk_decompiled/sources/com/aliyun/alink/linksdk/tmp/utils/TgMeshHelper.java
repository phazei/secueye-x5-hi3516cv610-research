package com.aliyun.alink.linksdk.tmp.utils;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.alibaba.ailabs.iot.mesh.bean.ConnectionParams;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tools.ALog;
import com.facebook.internal.NativeProtocol;
import com.linkkit.tools.utils.ReflectUtils;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class TgMeshHelper {
    private static final String TAG = "TgMeshHelper";
    private static DeviceHelper deviceHelper;
    private static GroupHelper groupHelper;
    private static AtomicBoolean hasCheckedTgMeshSDk = new AtomicBoolean(false);
    private static boolean hasTgMeshSDk = false;

    public interface DeviceHelper {
        boolean isMeshDevice(String str);

        boolean isOwnedDevice(String str);
    }

    public interface GroupHelper {
        int getGroupAddress(String str);

        int getGroupCount(String str);

        List<String> getGroupList(String str);

        boolean isMeshGroup(String str);
    }

    public static void addMeshDevice(String str) {
    }

    public static void setDeviceHelper(DeviceHelper deviceHelper2) {
        deviceHelper = deviceHelper2;
    }

    public static void setGroupHelper(GroupHelper groupHelper2) {
        groupHelper = groupHelper2;
    }

    public static boolean hasTgMeshSdkDependency() {
        if (hasCheckedTgMeshSDk.compareAndSet(false, true)) {
            hasTgMeshSDk = ReflectUtils.hasClass("com.alibaba.ailabs.iot.mesh.TgMeshManager");
        }
        return hasTgMeshSDk;
    }

    public static void connect(String str) {
        if (TmpPermissionUtils.checkIsNeedRequestBleScanAndConnect()) {
            return;
        }
        ALog.d(TAG, "connect() called with: iotId = [" + str + "]");
        if (!hasTgMeshSdkDependency()) {
            ALog.w(TAG, "connect exception, no mesh sdk dep, return.");
            return;
        }
        ConnectionParams connectionParams = new ConnectionParams();
        ALog.d(TAG, "mIotId:" + str);
        connectionParams.setDeviceId(str);
        TgMeshManager.getInstance().connect(connectionParams);
    }

    public static boolean isMeshNodeReachable(String str) {
        ALog.d(TAG, "isMeshNodeReachable() called with: iotId = [" + str + "]");
        if (!hasTgMeshSdkDependency()) {
            ALog.w(TAG, "isMeshNodeReachable exception, no mesh sdk dep, return.");
            return false;
        }
        DeviceHelper deviceHelper2 = deviceHelper;
        if (deviceHelper2 != null && deviceHelper2.isOwnedDevice(str)) {
            return TgMeshManager.getInstance().isConnectedToMesh();
        }
        return TgMeshManager.getInstance().isMeshNodeReachable(str);
    }

    public static boolean isMeshGroup(String str) {
        ALog.d(TAG, "isMeshGroup called with: groupId = [" + str + "]");
        GroupHelper groupHelper2 = groupHelper;
        if (groupHelper2 != null) {
            return groupHelper2.isMeshGroup(str);
        }
        return false;
    }

    public static int getCount(String str) {
        ALog.d(TAG, "getCount() called with: groupId = [" + str + "]");
        GroupHelper groupHelper2 = groupHelper;
        if (groupHelper2 != null) {
            return groupHelper2.getGroupCount(str);
        }
        return 0;
    }

    public static int getGroupAddress(String str) {
        ALog.d(TAG, "getGroupAddress() called with: groupId = [" + str + "]");
        GroupHelper groupHelper2 = groupHelper;
        if (groupHelper2 != null) {
            return groupHelper2.getGroupAddress(str);
        }
        return 0;
    }

    public static List<String> getMeshGroupItem(String str) {
        ALog.d(TAG, "addMeshGroupItem() called with: groupId = [" + str + "]");
        GroupHelper groupHelper2 = groupHelper;
        if (groupHelper2 != null) {
            return groupHelper2.getGroupList(str);
        }
        return null;
    }

    public static void removeAllMeshDevices() {
        ALog.d(TAG, "removeAllMeshDevices called");
    }

    public static boolean isMeshDevice(String str) {
        Log.d(TAG, "IsMeshDevice() called with: iotId = [" + str + "]");
        if (deviceHelper == null || TextUtils.isEmpty(str) || !deviceHelper.isOwnedDevice(str)) {
            return false;
        }
        return deviceHelper.isMeshDevice(str);
    }

    public static void groupClear(JSONObject jSONObject, final IPanelCallback iPanelCallback) {
        try {
            if (jSONObject == null) {
                ALog.w(TAG, "groupClear params empty, return");
                onFailCallback(iPanelCallback, "PARAMS_ERROR", "groupClear params empty");
                return;
            }
            if (!jSONObject.has("protocol")) {
                ALog.w(TAG, "groupClear params has no protocol key, return");
                onFailCallback(iPanelCallback, "PARAMS_ERROR", "groupClear params invalid, no protocol");
                return;
            }
            if (!DeviceClassificationUtil.PROTOCOL_BLE_MESH.equals(jSONObject.getString("protocol"))) {
                ALog.w(TAG, "groupClear params -> protocol != ble-mesh, return");
                onFailCallback(iPanelCallback, "PARAMS_ERROR", "groupClear params invalid, protocol != ble-mesh");
                return;
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject("preUpdateIotIdMap");
            if (jSONObject2 != null && jSONObject2.length() >= 1) {
                final AtomicInteger atomicInteger = new AtomicInteger(0);
                final AtomicInteger atomicInteger2 = new AtomicInteger(0);
                atomicInteger.set(jSONObject2.length());
                IActionListener<Boolean> iActionListener = new IActionListener<Boolean>() { // from class: com.aliyun.alink.linksdk.tmp.utils.TgMeshHelper.1
                    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
                    public void onSuccess(Boolean bool) {
                        ALog.d(TgMeshHelper.TAG, "groupClear->configModelSubscriptionDelete->onSuccess() called with: aBoolean = [" + bool + "]");
                        if (!bool.booleanValue()) {
                            ALog.w(TgMeshHelper.TAG, "groupClear->configModelSubscriptionDelete some device clear failed.");
                            TgMeshHelper.onFailCallback(iPanelCallback, "CODE_ERROR", "groupClear->configModelSubscriptionDelete->onSuccess->fail, some device failed.");
                        } else if (atomicInteger2.incrementAndGet() == atomicInteger.get()) {
                            ALog.i(TgMeshHelper.TAG, "groupClear all device clear success.");
                            if (iPanelCallback != null) {
                                if (TmpSdk.getContext() != null) {
                                    Intent intent = new Intent();
                                    intent.setAction("com.ilop.open.panel.delete");
                                    LocalBroadcastManager.getInstance(TmpSdk.getContext()).sendBroadcast(intent);
                                }
                                iPanelCallback.onComplete(true, null);
                            }
                        }
                    }

                    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
                    public void onFailure(int i, String str) {
                        ALog.d(TgMeshHelper.TAG, "groupClear->configModelSubscriptionDelete->onFailure() called with: i = [" + i + "], s = [" + str + "]");
                        TgMeshHelper.onFailCallback(iPanelCallback, "CODE_ERROR", "groupClear->configModelSubscriptionDelete->onFailure, some device failed.");
                    }
                };
                Iterator<String> itKeys = jSONObject2.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    if (TextUtils.isEmpty(next)) {
                        atomicInteger.decrementAndGet();
                    } else {
                        try {
                            JSONObject jSONObject3 = jSONObject2.getJSONObject(next);
                            String string = jSONObject3.getString(NativeProtocol.WEB_DIALOG_ACTION);
                            if (!"DELETE".equalsIgnoreCase(string)) {
                                ALog.w(TAG, "groupClear invalid action=" + string + " for this method.");
                                atomicInteger.decrementAndGet();
                            } else {
                                String string2 = jSONObject3.getString("deviceKey");
                                int i = jSONObject3.getInt("primaryAddress");
                                int i2 = jSONObject3.getInt("elementAddress");
                                int i3 = jSONObject3.getInt("subscriptionAddress");
                                int i4 = jSONObject3.getInt("modelIdentifier");
                                if (!TgMeshManager.getInstance().isMeshNodeReachable(next) && !TmpPermissionUtils.checkIsNeedRequestBleScanAndConnect()) {
                                    ConnectionParams connectionParams = new ConnectionParams();
                                    connectionParams.setDeviceId(next);
                                    TgMeshManager.getInstance().connect(connectionParams);
                                }
                                ALog.i(TAG, "groupClear call configModelSubscriptionDelete dk=" + string2 + ", pa=" + i + ", ea=" + i2 + ", sa=" + i3 + ", ");
                                TgMeshManager.getInstance().configModelSubscriptionDelete(string2, i, i2, i3, i4, iActionListener);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            atomicInteger.decrementAndGet();
                            ALog.w(TAG, "groupClear item JSONException = " + e);
                        } catch (Throwable th) {
                            th.printStackTrace();
                            atomicInteger.decrementAndGet();
                            ALog.w(TAG, "groupClear item Throwable = " + th);
                        }
                    }
                }
                return;
            }
            ALog.w(TAG, "groupClear params -> preUpdateIotIdMap empty, return");
            onFailCallback(iPanelCallback, "PARAMS_ERROR", "groupClear params invalid, preUpdateIotIdMap empty");
        } catch (JSONException e2) {
            e2.printStackTrace();
            ALog.w(TAG, "groupClear exception = " + e2);
        } catch (Throwable th2) {
            th2.printStackTrace();
            ALog.w(TAG, "groupClear throwable = " + th2);
        }
    }

    public static void onFailCallback(IPanelCallback iPanelCallback, String str, String str2) {
        ALog.d(TAG, "onFailCallback() called with: callback = [" + iPanelCallback + "], code = [" + str + "], errorMessage = [" + str2 + "]");
        if (iPanelCallback == null) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("code", str);
            jSONObject.put("msg", str2);
            iPanelCallback.onComplete(false, jSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
            iPanelCallback.onComplete(false, new JSONObject());
        } catch (Exception e2) {
            e2.printStackTrace();
            iPanelCallback.onComplete(false, new JSONObject());
        }
    }
}
