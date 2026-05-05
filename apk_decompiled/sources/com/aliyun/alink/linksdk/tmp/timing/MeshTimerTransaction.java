package com.aliyun.alink.linksdk.tmp.timing;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelEventCallback;
import com.aliyun.alink.linksdk.tmp.device.payload.discovery.GetTslResponsePayload;
import com.aliyun.alink.linksdk.tmp.timing.DeviceTimerAttributeModel;
import com.aliyun.alink.linksdk.tmp.timing.MeshTimerModel;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
public class MeshTimerTransaction {
    private static final String TAG = "[Tmp]MeshTimerTransaction";
    private DeviceTimerAttributeModel mDeviceTimerAttribute;
    private String mTargetDeviceIotID;
    private PanelDevice mTargetPanelDevice;
    private static Map<String, MeshTimerTransaction> mTimerTransactionInstances = new LinkedHashMap();
    private static ScheduledExecutorService mScheduledControlService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() { // from class: com.aliyun.alink.linksdk.tmp.timing.MeshTimerTransaction.1
        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, MeshTimerTransaction.TAG);
        }
    });
    private final int TIMER_ADD_OP = 0;
    private final int TIMER_DELETE_OP = 1;
    private final int TIMER_EDIT_OP = 2;
    private LocalTimerExecuteContext mLocalTimerExecuteContext = null;
    private DeviceTimerAttributeModel.ValueItem mUnsetTimerValueObj = null;
    private int mLimitSize = 0;

    public interface ErrorCode {
        public static final int ERROR_EXIT_PENDING_TASK = -7;
        public static final int ERROR_INVALID_TIMER_ID = -4;
        public static final int ERROR_INVOCATION_PROCESS = -2;
        public static final int ERROR_SEND_FAILED = -5;
        public static final int ERROR_SERVER_API_ERROR = -9;
        public static final int ERROR_SET_TIMER_EXCEPTION = -10;
        public static final int ERROR_SET_TIMER_TIMEOUT = -6;
        public static final int ERROR_TIMING_ALREADY_FULL = -3;
        public static final int ERROR_UNSUPPORTED_OPERATIONS = -1;
        public static final int ERROR_WAIT_TIME_CALIBRATION_COMPLETE = -8;
    }

    private static class LocalTimerExecuteContext {
        int action;
        MeshTimerModel changedOperationTimerModel;
        int changedTimersIndex;
        DeviceTimerAttributeModel.ValueItem changedValueItem;
        DeviceTimerAttributeModel.ValueItem rawValueItem;
        ScheduledFuture<?> resultCheckScheduledFuture;
        ScheduledFuture<?> timeoutScheduledFuture;
        ITimerActionCallback<List<MeshTimerModel>> userCallback;

        private LocalTimerExecuteContext() {
        }
    }

    private MeshTimerTransaction(String str) {
        this.mTargetDeviceIotID = str;
        this.mTargetPanelDevice = new PanelDevice(str);
        this.mTargetPanelDevice.subAllEvents(new IPanelEventCallback() { // from class: com.aliyun.alink.linksdk.tmp.timing.MeshTimerTransaction.2
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelEventCallback
            public void onNotify(String str2, String str3, Object obj) {
                ALog.d(MeshTimerTransaction.TAG, "onNotify, topic: " + str3 + ", data: " + obj.toString());
                if (TmpConstant.MQTT_TOPIC_PROPERTIES.equals(str3)) {
                    MeshTimerTransaction.this.mTargetDeviceIotID.equals(str2);
                }
            }
        }, new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.timing.MeshTimerTransaction.3
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
            }
        });
    }

    public static MeshTimerTransaction initWithIotID(String str) {
        MeshTimerTransaction meshTimerTransaction;
        if (mTimerTransactionInstances.get(str) != null) {
            return mTimerTransactionInstances.get(str);
        }
        synchronized (MeshTimerTransaction.class) {
            meshTimerTransaction = mTimerTransactionInstances.get(str);
            if (meshTimerTransaction == null) {
                meshTimerTransaction = new MeshTimerTransaction(str);
                mTimerTransactionInstances.put(str, meshTimerTransaction);
            }
        }
        return meshTimerTransaction;
    }

    public void getDeviceTimerList(final ITimerActionCallback<List<MeshTimerModel>> iTimerActionCallback) {
        if (this.mLimitSize != 0) {
            realGetDeviceTimerList(iTimerActionCallback);
        } else {
            getTimerLimitSize(new ITimerActionCallback<Integer>() { // from class: com.aliyun.alink.linksdk.tmp.timing.MeshTimerTransaction.4
                @Override // com.aliyun.alink.linksdk.tmp.timing.ITimerActionCallback
                public void onSuccess(Integer num) {
                    ALog.i(MeshTimerTransaction.TAG, "On successful to get timer limit size: " + num);
                    MeshTimerTransaction.this.mLimitSize = num.intValue();
                    MeshTimerTransaction.this.realGetDeviceTimerList(iTimerActionCallback);
                }

                @Override // com.aliyun.alink.linksdk.tmp.timing.ITimerActionCallback
                public void onFailure(int i, String str) {
                    ALog.e(MeshTimerTransaction.TAG, "On failed to get timer limit size, erromsg: " + str);
                    MeshTimerTransaction.this.notifyFailed(iTimerActionCallback, i, str);
                }
            });
        }
    }

    private void getTimerLimitSize(final ITimerActionCallback<Integer> iTimerActionCallback) {
        if (iTimerActionCallback == null) {
            ALog.d(TAG, "getDeviceTSL() userCallback == null");
        } else {
            ALog.d(TAG, "getDeviceTSL() called");
            this.mTargetPanelDevice.getDeviceTSL(new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.timing.MeshTimerTransaction.5
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    String str;
                    ALog.i(MeshTimerTransaction.TAG, "getDeviceTSL() complete, isSuccess: " + z + ", data: " + JSON.toJSONString(obj));
                    if (z && (obj instanceof GetTslResponsePayload)) {
                        Object obj2 = ((GetTslResponsePayload) obj).data;
                        if (obj2 instanceof Map) {
                            List list = (List) ((Map) obj2).get(TmpConstant.DEVICE_MODEL_PROPERTIES);
                            if (list == null) {
                                str = "invalid TSL, undefined properties";
                            } else {
                                Map map = null;
                                Iterator it = list.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    Map map2 = (Map) it.next();
                                    if ("DeviceTimer".equals((String) map2.get("identifier"))) {
                                        map = map2;
                                        break;
                                    }
                                }
                                if (map == null) {
                                    str = "invalid TSL, undefined DeviceTimer property";
                                } else {
                                    Map map3 = (Map) map.get("dataType");
                                    if (map3 == null) {
                                        str = "invalid TSL, data type is incorrect for DeviceTimer property";
                                    } else {
                                        Map map4 = (Map) map3.get("specs");
                                        if (map4 == null) {
                                            str = "invalid TSL, specs cannot be empty for DeviceTimer property";
                                        } else {
                                            if (map4.get("size") != null) {
                                                iTimerActionCallback.onSuccess(Integer.valueOf(Integer.parseInt(map4.get("size").toString())));
                                                return;
                                            }
                                            str = "invalid TSL, size in spec cannot be empty for DeviceTimer property";
                                        }
                                    }
                                }
                            }
                            iTimerActionCallback.onFailure(-1, str);
                            return;
                        }
                        return;
                    }
                    iTimerActionCallback.onFailure(-1, "get TSL failed");
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void realGetDeviceTimerList(final ITimerActionCallback<List<MeshTimerModel>> iTimerActionCallback) {
        ALog.d(TAG, "getDeviceTimerList() called");
        this.mTargetPanelDevice.getProperties(new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.timing.MeshTimerTransaction.6
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                DeviceTimerAttributeModel.ValueItem valueItem;
                ALog.i(MeshTimerTransaction.TAG, "getDeviceTimerList complete, isSuccess: " + z + ", data: " + obj);
                if (z && obj != null) {
                    JSONObject object = JSON.parseObject(obj.toString());
                    if (200 == object.getInteger("code").intValue()) {
                        JSONObject jSONObject = object.getJSONObject("data");
                        LinkedList linkedList = new LinkedList();
                        if (jSONObject.getJSONObject("DeviceTimer") != null) {
                            MeshTimerTransaction.this.mDeviceTimerAttribute = (DeviceTimerAttributeModel) jSONObject.getJSONObject("DeviceTimer").toJavaObject(DeviceTimerAttributeModel.class);
                            List<DeviceTimerAttributeModel.ValueItem> value = MeshTimerTransaction.this.mDeviceTimerAttribute.getValue();
                            int size = value.size();
                            for (int i = 0; i < MeshTimerTransaction.this.mLimitSize; i++) {
                                if (i < size) {
                                    valueItem = value.get(i);
                                } else {
                                    valueItem = new DeviceTimerAttributeModel.ValueItem();
                                    valueItem.Y = 0;
                                    valueItem.E = 0;
                                    value.add(valueItem);
                                }
                                MeshTimerModel meshTimerModelFromTimerAttributeValueItem = MeshTimerModel.fromTimerAttributeValueItem(valueItem);
                                meshTimerModelFromTimerAttributeValueItem.setTimerID(String.valueOf(i));
                                linkedList.add(meshTimerModelFromTimerAttributeValueItem);
                            }
                        }
                        MeshTimerTransaction.this.notifySuccess(iTimerActionCallback, linkedList);
                        return;
                    }
                }
                MeshTimerTransaction.this.notifyFailed(iTimerActionCallback, -9, obj == null ? "" : JSON.toJSONString(obj));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queryTimerOperateTask(final ITimerActionCallback<List<MeshTimerModel>> iTimerActionCallback) {
        ALog.d(TAG, "queryTimerOperateTask() called");
        this.mTargetPanelDevice.queryTimerDownlinkTask(new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.timing.MeshTimerTransaction.7
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                MeshTimerModel meshTimerModelBuild;
                ALog.i(MeshTimerTransaction.TAG, "queryTimerDownlinkTask complete, isSuccess: " + z + ", data: " + JSON.toJSONString(obj));
                if (z && (obj instanceof GetTslResponsePayload)) {
                    Object obj2 = ((GetTslResponsePayload) obj).data;
                    if (obj2 instanceof Map) {
                        Map map = (Map) ((Map) obj2).get("timerOperateTask");
                        if (map == null) {
                            iTimerActionCallback.onFailure(-9, "unknown error, timerOperateTask is empty");
                            return;
                        }
                        int iIntValue = ((Integer) map.get("errorCode")).intValue();
                        LinkedList linkedList = new LinkedList();
                        if (iIntValue != 201) {
                            JSONObject object = JSON.parseObject(obj2.toString());
                            if (object.getJSONObject("deviceTimer") != null) {
                                MeshTimerTransaction.this.mDeviceTimerAttribute = (DeviceTimerAttributeModel) object.getJSONObject("deviceTimer").toJavaObject(DeviceTimerAttributeModel.class);
                                List<DeviceTimerAttributeModel.ValueItem> value = MeshTimerTransaction.this.mDeviceTimerAttribute.getValue();
                                for (int i = 0; i < MeshTimerTransaction.this.mLimitSize; i++) {
                                    if (value.size() > i) {
                                        meshTimerModelBuild = MeshTimerModel.fromTimerAttributeValueItem(value.get(i));
                                    } else {
                                        meshTimerModelBuild = new MeshTimerModel.NormalTimerBuilder().build();
                                        value.add(meshTimerModelBuild.toAttributeModel());
                                    }
                                    meshTimerModelBuild.setTimerID(String.valueOf(i));
                                    linkedList.add(meshTimerModelBuild);
                                }
                            }
                        }
                        if (iIntValue == 200) {
                            MeshTimerTransaction.this.notifySuccess(iTimerActionCallback, linkedList);
                        } else {
                            iTimerActionCallback.onFailure(iIntValue, (String) map.get("errorMessage"));
                        }
                    }
                }
            }
        });
    }

    public void addTimerWithTimerModel(MeshTimerModel meshTimerModel, ITimerActionCallback<List<MeshTimerModel>> iTimerActionCallback) {
        ALog.d(TAG, "addTimerWithTimerModel() called, tobeAddTimerModel: " + JSON.toJSONString(meshTimerModel));
        if (preCheckEnvBeforeConfigTimer(iTimerActionCallback)) {
            List<DeviceTimerAttributeModel.ValueItem> value = this.mDeviceTimerAttribute.getValue();
            Iterator<DeviceTimerAttributeModel.ValueItem> it = value.iterator();
            byte b2 = 0;
            while (it.hasNext() && it.next().checkIsSet()) {
                b2 = (byte) (b2 + 1);
            }
            if (b2 >= this.mLimitSize) {
                notifyFailed(iTimerActionCallback, -3, "The timer is full and can no longer be added");
                return;
            }
            DeviceTimerAttributeModel.ValueItem valueItem = value.get(b2);
            DeviceTimerAttributeModel.ValueItem attributeModel = meshTimerModel.toAttributeModel();
            attributeModel.E = TimerEnableType.TIMER_ENABLE.getTypeValue();
            value.set(b2, attributeModel);
            realSetDeviceTimer(value, b2, valueItem, 0, iTimerActionCallback);
        }
    }

    public void deleteTimerWithTimerModel(MeshTimerModel meshTimerModel, ITimerActionCallback<List<MeshTimerModel>> iTimerActionCallback) {
        ALog.d(TAG, "deleteTimerWithTimerModel() called, tobeDeleteTimerModel: " + JSON.toJSONString(meshTimerModel));
        if (preCheckEnvBeforeConfigTimer(iTimerActionCallback)) {
            try {
                int i = Integer.parseInt(meshTimerModel.getTimerID());
                List<DeviceTimerAttributeModel.ValueItem> value = this.mDeviceTimerAttribute.getValue();
                if (i >= value.size()) {
                    notifyFailed(iTimerActionCallback, -4, "invalid timerID");
                    return;
                }
                DeviceTimerAttributeModel.ValueItem valueItem = new DeviceTimerAttributeModel.ValueItem(value.get(i));
                value.get(i).Y = 0;
                realSetDeviceTimer(value, i, valueItem, 1, iTimerActionCallback);
            } catch (NumberFormatException unused) {
                notifyFailed(iTimerActionCallback, -4, "invalid timerID");
            }
        }
    }

    public void editTimerWithTimerModel(MeshTimerModel meshTimerModel, ITimerActionCallback<List<MeshTimerModel>> iTimerActionCallback) {
        ALog.d(TAG, "editTimerWithTimerModel() called, tobeAddTimerModel: " + JSON.toJSONString(meshTimerModel));
        if (preCheckEnvBeforeConfigTimer(iTimerActionCallback)) {
            try {
                int i = Integer.parseInt(meshTimerModel.getTimerID());
                List<DeviceTimerAttributeModel.ValueItem> value = this.mDeviceTimerAttribute.getValue();
                if (i < value.size() && i >= 0) {
                    DeviceTimerAttributeModel.ValueItem valueItem = value.get(i);
                    value.set(i, meshTimerModel.toAttributeModel());
                    realSetDeviceTimer(value, i, valueItem, 2, iTimerActionCallback);
                    return;
                }
                notifyFailed(iTimerActionCallback, -4, "invalid timerID");
            } catch (NumberFormatException unused) {
                notifyFailed(iTimerActionCallback, -4, "invalid timerID");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <T> void notifySuccess(ITimerActionCallback<T> iTimerActionCallback, T t) {
        if (iTimerActionCallback != null) {
            try {
                iTimerActionCallback.onSuccess(t);
            } catch (Exception unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyFailed(ITimerActionCallback iTimerActionCallback, int i, String str) {
        if (iTimerActionCallback != null) {
            try {
                iTimerActionCallback.onFailure(i, str);
            } catch (Exception unused) {
            }
        }
    }

    private boolean preCheckEnvBeforeConfigTimer(ITimerActionCallback<List<MeshTimerModel>> iTimerActionCallback) {
        DeviceTimerAttributeModel deviceTimerAttributeModel = this.mDeviceTimerAttribute;
        if (deviceTimerAttributeModel == null) {
            notifyFailed(iTimerActionCallback, -2, "method getDeviceTimerList must be called first");
            return false;
        }
        if (deviceTimerAttributeModel.getValue() == null || this.mDeviceTimerAttribute.getValue().size() == 0) {
            notifyFailed(iTimerActionCallback, -1, "need check whether the TSL configuration of the product supports local timing");
            return false;
        }
        if (this.mLocalTimerExecuteContext == null) {
            return true;
        }
        notifyFailed(iTimerActionCallback, -7, "need to wait for the previous task call to complete");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unsetLocalTimerExecuteContext() {
        if (this.mLocalTimerExecuteContext.resultCheckScheduledFuture != null) {
            this.mLocalTimerExecuteContext.resultCheckScheduledFuture.cancel(true);
        }
        if (this.mLocalTimerExecuteContext.timeoutScheduledFuture != null) {
            this.mLocalTimerExecuteContext.timeoutScheduledFuture.cancel(true);
        }
        this.mLocalTimerExecuteContext = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setLocalTimerExecuteContext(int i, DeviceTimerAttributeModel.ValueItem valueItem, DeviceTimerAttributeModel.ValueItem valueItem2, int i2, ITimerActionCallback<List<MeshTimerModel>> iTimerActionCallback) {
        if (this.mLocalTimerExecuteContext != null) {
            return;
        }
        this.mLocalTimerExecuteContext = new LocalTimerExecuteContext();
        LocalTimerExecuteContext localTimerExecuteContext = this.mLocalTimerExecuteContext;
        localTimerExecuteContext.changedTimersIndex = i;
        localTimerExecuteContext.changedValueItem = valueItem;
        localTimerExecuteContext.rawValueItem = valueItem2;
        localTimerExecuteContext.action = i2;
        localTimerExecuteContext.userCallback = iTimerActionCallback;
        localTimerExecuteContext.timeoutScheduledFuture = mScheduledControlService.schedule(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.timing.MeshTimerTransaction.8
            @Override // java.lang.Runnable
            public void run() {
                ALog.e(MeshTimerTransaction.TAG, "timeout event trigger");
                ITimerActionCallback<List<MeshTimerModel>> iTimerActionCallback2 = MeshTimerTransaction.this.mLocalTimerExecuteContext.userCallback;
                MeshTimerTransaction.this.unsetLocalTimerExecuteContext();
                MeshTimerTransaction.this.notifyFailed(iTimerActionCallback2, -6, "query timer operate task timeout");
            }
        }, 5000L, TimeUnit.MILLISECONDS);
        this.mLocalTimerExecuteContext.resultCheckScheduledFuture = mScheduledControlService.scheduleAtFixedRate(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.timing.MeshTimerTransaction.9
            @Override // java.lang.Runnable
            public void run() {
                MeshTimerTransaction.this.queryTimerOperateTask(new ITimerActionCallback<List<MeshTimerModel>>() { // from class: com.aliyun.alink.linksdk.tmp.timing.MeshTimerTransaction.9.1
                    @Override // com.aliyun.alink.linksdk.tmp.timing.ITimerActionCallback
                    public void onSuccess(List<MeshTimerModel> list) {
                        ALog.i(MeshTimerTransaction.TAG, "On successful to queryTimerDownlinkTask");
                        MeshTimerTransaction.this.notifySuccess(MeshTimerTransaction.this.mLocalTimerExecuteContext.userCallback, list);
                        MeshTimerTransaction.this.unsetLocalTimerExecuteContext();
                    }

                    @Override // com.aliyun.alink.linksdk.tmp.timing.ITimerActionCallback
                    public void onFailure(int i3, String str) {
                        if (i3 != 201) {
                            ITimerActionCallback<List<MeshTimerModel>> iTimerActionCallback2 = MeshTimerTransaction.this.mLocalTimerExecuteContext.userCallback;
                            MeshTimerTransaction.this.unsetLocalTimerExecuteContext();
                            if (i3 == 33129) {
                                MeshTimerTransaction.this.notifyFailed(iTimerActionCallback2, -8, str);
                            } else {
                                MeshTimerTransaction.this.notifyFailed(iTimerActionCallback2, -10, str);
                            }
                        }
                    }
                });
            }
        }, 1000L, 1500L, TimeUnit.MILLISECONDS);
    }

    private DeviceTimerAttributeModel.ValueItem obtainUnsetTimerValue() {
        DeviceTimerAttributeModel.ValueItem valueItem = this.mUnsetTimerValueObj;
        if (valueItem != null) {
            return valueItem;
        }
        this.mUnsetTimerValueObj = new DeviceTimerAttributeModel.ValueItem();
        DeviceTimerAttributeModel.ValueItem valueItem2 = this.mUnsetTimerValueObj;
        valueItem2.E = 0;
        valueItem2.Y = 0;
        return valueItem2;
    }

    private void realSetDeviceTimer(List<DeviceTimerAttributeModel.ValueItem> list, final int i, final DeviceTimerAttributeModel.ValueItem valueItem, final int i2, final ITimerActionCallback<List<MeshTimerModel>> iTimerActionCallback) {
        final DeviceTimerAttributeModel.ValueItem valueItem2 = list.get(i);
        HashMap map = new HashMap();
        map.put("DeviceTimer", list);
        HashMap map2 = new HashMap();
        map2.put("iotId", this.mTargetDeviceIotID);
        map2.put("items", map);
        this.mTargetPanelDevice.setProperties(JSON.toJSONString(map2, new PropertyFilter() { // from class: com.aliyun.alink.linksdk.tmp.timing.MeshTimerTransaction.10
            @Override // com.alibaba.fastjson.serializer.PropertyFilter
            public boolean apply(Object obj, String str, Object obj2) {
                if (obj instanceof DeviceTimerAttributeModel.ValueItem) {
                    DeviceTimerAttributeModel.ValueItem valueItem3 = (DeviceTimerAttributeModel.ValueItem) obj;
                    if (valueItem3.Y == 0 && valueItem3.E == 1) {
                        if (TextUtils.isEmpty(valueItem3.N)) {
                            return ("R".equals(str) || "S".equals(str) || str.equals("N")) ? false : true;
                        }
                        return true;
                    }
                    if (!valueItem3.checkIsSet()) {
                        return "E".equals(str) || "Y".equals(str);
                    }
                    if (valueItem3.Y == 2 || valueItem3.Y == 1) {
                        return ("R".equals(str) || "S".equals(str) || "N".equals(str)) ? false : true;
                    }
                }
                return true;
            }
        }, new SerializerFeature[0]), new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.timing.MeshTimerTransaction.11
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, @Nullable Object obj) {
                ALog.d(MeshTimerTransaction.TAG, "setProperties complete, isSuccess: " + z + ", data: " + obj.toString());
                MeshTimerTransaction.this.setLocalTimerExecuteContext(i, valueItem2, valueItem, i2, iTimerActionCallback);
            }
        });
    }
}
