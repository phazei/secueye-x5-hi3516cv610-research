package com.aliyun.iot.aep.sdk.abus;

import android.app.Fragment;
import android.util.SparseArray;
import com.aliyun.iot.aep.sdk.abus.utils.ALog;
import de.greenrobot.event.EventBus;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class ABus implements IBus {
    private static final String TAG = "ABus";
    private SparseArray<ChannelInfo> channelMap = null;
    private SparseArray<CopyOnWriteArrayList<Object>> delayEvents = null;
    private Map<Object, CopyOnWriteArrayList<Integer>> eventToChannelsMap = null;
    private SparseArray<Boolean> channelBlockMap = null;

    @Override // com.aliyun.iot.aep.sdk.abus.IBus
    public void attachListener(int i, Object obj, String str, Class<? extends Object> cls) {
        try {
            _attachListener(i, obj, str, cls);
        } catch (Exception e) {
            ALog.e(TAG, "attachListener(): " + e.getMessage());
        }
    }

    @Override // com.aliyun.iot.aep.sdk.abus.IBus
    public void detachListener(int i, Object obj) {
        try {
            _detachListener(i, obj, null);
        } catch (Exception e) {
            ALog.e(TAG, "detachListener(): " + e.getMessage());
        }
    }

    @Override // com.aliyun.iot.aep.sdk.abus.IBus
    public void detachListener(int i, Object obj, Class<? extends Object> cls) {
        try {
            _detachListener(i, obj, cls);
        } catch (Exception e) {
            ALog.e(TAG, "detachListener(): " + e.getMessage());
        }
    }

    @Override // com.aliyun.iot.aep.sdk.abus.IBus
    public void postEvent(int i, Object obj) {
        if (i == 0 || obj == null) {
            ALog.w(TAG, "postEvent(channelId, eventClass): 0 == channelId || null == eventClass");
            return;
        }
        SparseArray<ChannelInfo> sparseArray = this.channelMap;
        if (sparseArray == null || sparseArray.size() <= 0) {
            ALog.d(TAG, "postEvent(channelId, eventClass): no channel");
            return;
        }
        ChannelInfo channelInfo = this.channelMap.get(i);
        if (channelInfo == null) {
            ALog.d(TAG, "postEvent(channelId, eventClass): no the channel: " + i);
            return;
        }
        if (channelInfo.blocked) {
            delayEvent(i, obj);
            ALog.d(TAG, "postEvent(channelId, eventClass): the " + i + " channel is blocked");
            return;
        }
        channelInfo.channel.post(obj);
    }

    @Override // com.aliyun.iot.aep.sdk.abus.IBus
    public void blockChannel(int i, boolean z) {
        SparseArray<ChannelInfo> sparseArray = this.channelMap;
        ChannelInfo channelInfo = sparseArray != null ? sparseArray.get(i) : null;
        if (channelInfo != null) {
            channelInfo.blocked = z;
            if (z) {
                setChannelBlock(i, true);
            } else {
                setChannelBlock(i, false);
            }
            postDelayEvents(i);
        }
    }

    @Override // com.aliyun.iot.aep.sdk.abus.IBus
    public void cancelChannel(int i) {
        SparseArray<ChannelInfo> sparseArray = this.channelMap;
        if (sparseArray != null) {
            sparseArray.remove(i);
        }
    }

    public void removeChannelBlock(int i) {
        SparseArray<Boolean> sparseArray = this.channelBlockMap;
        if (sparseArray != null) {
            sparseArray.remove(i);
        }
    }

    public void setChannelBlock(int i, boolean z) {
        if (this.channelBlockMap == null) {
            this.channelBlockMap = new SparseArray<>();
        }
        this.channelBlockMap.put(i, Boolean.valueOf(z));
    }

    public boolean getChannelBlock(int i) {
        SparseArray<Boolean> sparseArray;
        if (i == 0 || (sparseArray = this.channelBlockMap) == null || sparseArray.size() <= 0) {
            return false;
        }
        return this.channelBlockMap.get(i, false).booleanValue();
    }

    public void postDelayEvents(int i) {
        SparseArray<CopyOnWriteArrayList<Object>> sparseArray;
        CopyOnWriteArrayList<Object> copyOnWriteArrayList;
        if (i == 0 || getChannelBlock(i) || (sparseArray = this.delayEvents) == null || sparseArray.size() < 1 || (copyOnWriteArrayList = this.delayEvents.get(i)) == null || copyOnWriteArrayList.isEmpty()) {
            return;
        }
        Iterator<Object> it = copyOnWriteArrayList.iterator();
        while (it.hasNext()) {
            postEvent(i, it.next());
        }
        copyOnWriteArrayList.clear();
        this.delayEvents.remove(i);
    }

    public void delayEvent(int i, Object obj) {
        if (i == 0 || obj == null) {
            ALog.w(TAG, "delayEvent(): ERROR: bad parameters");
            return;
        }
        if (this.delayEvents == null) {
            this.delayEvents = new SparseArray<>();
        }
        CopyOnWriteArrayList<Object> copyOnWriteArrayList = this.delayEvents.get(i);
        if (copyOnWriteArrayList == null) {
            copyOnWriteArrayList = new CopyOnWriteArrayList<>();
            this.delayEvents.put(i, copyOnWriteArrayList);
        } else if (!copyOnWriteArrayList.isEmpty() && copyOnWriteArrayList.contains(obj)) {
            copyOnWriteArrayList.remove(obj);
        }
        copyOnWriteArrayList.add(obj);
    }

    public void removeDelayChannel(int i) {
        SparseArray<CopyOnWriteArrayList<Object>> sparseArray = this.delayEvents;
        if (sparseArray == null || sparseArray.size() < 1) {
            return;
        }
        this.delayEvents.remove(i);
    }

    private synchronized void _detachListener(int i, Object obj, Class<? extends Object> cls) {
        if (i == 0 || obj == null) {
            ALog.w(TAG, "_dettachListener(): ERROR: bad parameters");
            return;
        }
        ChannelInfo channelInfo = this.channelMap.get(i);
        if (channelInfo == null) {
            return;
        }
        if (cls != null) {
            channelInfo.channel.unregister(obj, cls);
            CopyOnWriteArrayList<Class<? extends Object>> copyOnWriteArrayList = channelInfo.listenerToEventMap.get(obj);
            if (copyOnWriteArrayList != null) {
                copyOnWriteArrayList.remove(cls);
                if (copyOnWriteArrayList.size() == 0) {
                    channelInfo.listenerToEventMap.remove(obj);
                }
            } else {
                channelInfo.listenerToEventMap.remove(obj);
            }
        } else {
            channelInfo.channel.unregister(obj);
            channelInfo.listenerToEventMap.remove(obj);
        }
        if (channelInfo.listenerToEventMap.isEmpty()) {
            this.channelMap.remove(i);
        }
        removeDelayChannel(i);
        removeChannelBlock(i);
    }

    private synchronized void _attachListener(int i, Object obj, String str, Class<? extends Object> cls) {
        if (obj != null && str != null) {
            if (str.trim().length() > 0 && i != 0 && cls != null) {
                if (this.channelMap == null) {
                    this.channelMap = new SparseArray<>();
                }
                ChannelInfo channelInfo = this.channelMap.get(i);
                boolean z = false;
                if (channelInfo == null) {
                    if (obj instanceof Fragment) {
                        channelInfo = new ChannelInfo(i);
                    } else {
                        channelInfo = new ChannelInfo(i);
                    }
                    channelInfo.channel.register(obj, str, cls, new Class[0]);
                    this.channelMap.put(i, channelInfo);
                } else {
                    channelInfo.channel.register(obj, str, cls, new Class[0]);
                }
                CopyOnWriteArrayList<Class<? extends Object>> copyOnWriteArrayList = channelInfo.listenerToEventMap.get(obj);
                if (copyOnWriteArrayList == null) {
                    CopyOnWriteArrayList<Class<? extends Object>> copyOnWriteArrayList2 = new CopyOnWriteArrayList<>();
                    copyOnWriteArrayList2.add(cls);
                    channelInfo.listenerToEventMap.put(obj, copyOnWriteArrayList2);
                } else {
                    Iterator<Class<? extends Object>> it = copyOnWriteArrayList.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        } else if (it.next() == cls) {
                            z = true;
                            break;
                        }
                    }
                    if (z) {
                        ALog.w(TAG, obj.getClass() + " already registered to event " + cls);
                    } else {
                        copyOnWriteArrayList.add(cls);
                    }
                }
                return;
            }
        }
        ALog.w(TAG, "_attachListener(): ERROR: bad parameters");
    }

    private static final class ChannelInfo {
        boolean blocked;
        EventBus channel;
        int channelId;
        HashMap<Object, CopyOnWriteArrayList<Class<? extends Object>>> listenerToEventMap;

        public ChannelInfo(int i) {
            this.blocked = false;
            this.channelId = 0;
            this.channel = null;
            this.listenerToEventMap = null;
            this.blocked = false;
            this.channelId = i;
            this.channel = new EventBus();
            this.listenerToEventMap = new HashMap<>();
        }
    }
}
