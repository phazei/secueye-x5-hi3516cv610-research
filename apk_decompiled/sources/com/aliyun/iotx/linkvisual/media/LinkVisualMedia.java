package com.aliyun.iotx.linkvisual.media;

import android.os.Process;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.video.HardwareDecoderable;
import com.aliyun.iotx.linkvisual.media.video.beans.PreConnectType;
import com.aliyun.iotx.linkvisual.media.video.utils.APIHelper;
import com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener;
import java.util.HashMap;
import lvbyte.lvif;

/* JADX INFO: loaded from: classes2.dex */
public class LinkVisualMedia {
    public static final String TAG = "linksdk_lv_media";

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private static HashMap<Integer, Integer> f4960lvfor = new HashMap<>(16);

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private static LinkVisualMedia f4961lvif;
    public HashMap<String, Long> playerIotIdTidMap = new HashMap<>(16);

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private byte[] f4962lvdo = new byte[1];

    class lvdo implements IAPIHelperListener {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        final /* synthetic */ String f4963lvdo;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        final /* synthetic */ int f4965lvif;

        lvdo(String str, int i) {
            this.f4963lvdo = str;
            this.f4965lvif = i;
        }

        @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
        public void onFailed(lvif lvifVar) {
            ALog.w(LinkVisualMedia.TAG, lvifVar.lvdo() + "\t" + lvifVar.lvfor());
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x008c  */
        @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onResponse(lvbyte.lvif r19) {
            /*
                r18 = this;
                r1 = r18
                int r0 = r19.lvdo()
                r2 = 200(0xc8, float:2.8E-43)
                if (r0 == r2) goto L23
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r2 = r1.f4963lvdo
                r0.append(r2)
                java.lang.String r2 = " pre connect failed."
                r0.append(r2)
                java.lang.String r0 = r0.toString()
                java.lang.String r2 = "linksdk_lv_media"
                com.aliyun.alink.linksdk.tools.ALog.w(r2, r0)
                return
            L23:
                java.lang.Object r0 = r19.lvif()
                java.lang.String r0 = java.lang.String.valueOf(r0)
                com.alibaba.fastjson.JSONObject r0 = com.alibaba.fastjson.JSON.parseObject(r0)
                com.aliyun.iotx.linkvisual.media.video.p2p.P2PConfig r2 = new com.aliyun.iotx.linkvisual.media.video.p2p.P2PConfig
                r2.<init>()
                java.lang.String r3 = "supportRelayPrePlay"
                boolean r7 = r0.getBooleanValue(r3)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                java.lang.String r3 = "supportP2pPrePlay"
                boolean r8 = r0.getBooleanValue(r3)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                java.lang.String r3 = "relayUrl"
                java.lang.String r9 = r0.getString(r3)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                java.lang.String r3 = "signalUrl"
                java.lang.String r3 = r0.getString(r3)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                java.lang.String r4 = "stunUrl"
                java.lang.String r4 = r0.getString(r4)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                r2.setSignalUrl(r3)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                r2.setStunUrl(r4)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                boolean r3 = android.text.TextUtils.isEmpty(r9)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                r4 = 0
                if (r3 != 0) goto L8c
                java.lang.String r3 = "relayDecryptKey"
                boolean r3 = r0.containsKey(r3)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                if (r3 == 0) goto L8c
                java.lang.String r3 = "relayDecryptKey"
                com.alibaba.fastjson.JSONObject r0 = r0.getJSONObject(r3)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                java.lang.String r3 = "iv"
                boolean r3 = r0.containsKey(r3)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                if (r3 == 0) goto L8c
                java.lang.String r3 = "key"
                boolean r3 = r0.containsKey(r3)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                if (r3 == 0) goto L8c
                java.lang.String r3 = "iv"
                byte[] r3 = r0.getBytes(r3)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                java.lang.String r4 = "key"
                byte[] r0 = r0.getBytes(r4)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                r12 = r0
                r11 = r3
                goto L8e
            L8c:
                r11 = r4
                r12 = r11
            L8e:
                java.lang.String r4 = r1.f4963lvdo     // Catch: com.alibaba.fastjson.JSONException -> Lac
                r5 = 1
                int r6 = r1.f4965lvif     // Catch: com.alibaba.fastjson.JSONException -> Lac
                r10 = 1
                java.lang.String r13 = r2.getSession()     // Catch: com.alibaba.fastjson.JSONException -> Lac
                java.lang.String r14 = r2.getStunServer()     // Catch: com.alibaba.fastjson.JSONException -> Lac
                int r15 = r2.getStunPort()     // Catch: com.alibaba.fastjson.JSONException -> Lac
                java.lang.String r16 = r2.getStunKey()     // Catch: com.alibaba.fastjson.JSONException -> Lac
                java.lang.String r17 = r2.getSignalUrl()     // Catch: com.alibaba.fastjson.JSONException -> Lac
                com.aliyun.iotx.linkvisual.media.LinkVisual.pre_create_stream(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17)     // Catch: com.alibaba.fastjson.JSONException -> Lac
                goto Lb0
            Lac:
                r0 = move-exception
                r0.printStackTrace()
            Lb0:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iotx.linkvisual.media.LinkVisualMedia.lvdo.onResponse(lvbyte.lvif):void");
        }
    }

    private LinkVisualMedia() {
        ALog.i(TAG, "Global Media New.");
    }

    public static synchronized LinkVisualMedia getInstance() {
        if (f4961lvif == null) {
            f4961lvif = new LinkVisualMedia();
        }
        return f4961lvif;
    }

    @Deprecated
    public void init() {
    }

    public void internalDeinit(int i) {
        synchronized (this.f4962lvdo) {
            if (f4960lvfor.isEmpty()) {
                return;
            }
            f4960lvfor.remove(Integer.valueOf(i));
            if (f4960lvfor.isEmpty()) {
                ALog.d(TAG, "Global Media Deinit.");
                if (Version.isIlop || Version.isTg) {
                    LinkVisual.stream_p2p_exit();
                }
            }
        }
    }

    public void internalInit(int i) {
        synchronized (this.f4962lvdo) {
            if (f4960lvfor.isEmpty()) {
                ALog.i(TAG, "Global Media Init. Thread priority=" + Process.getThreadPriority(Process.myTid()));
                if (Version.isIlop || Version.isTg) {
                    LinkVisual.stream_p2p_init();
                }
                LinkVisual.set_decoder_strategy(HardwareDecoderable.DecoderStrategy.FORCE_SOFTWARE.ordinal());
                LinkVisual.set_log_level(ALog.getLevel());
            }
            f4960lvfor.put(Integer.valueOf(i), Integer.valueOf(i));
        }
    }

    public boolean isInit() {
        boolean z;
        synchronized (this.f4962lvdo) {
            z = !f4960lvfor.isEmpty();
        }
        return z;
    }

    @Deprecated
    public void preConnectByIotId(String str) {
        preConnectByIotId(str, 0);
    }

    public void preConnectByIotId(String str, int i) {
        Version.checkSupport();
        if (!isInit()) {
            internalInit(hashCode());
        }
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "preConnectByIotId using empty iotid.");
            return;
        }
        PreConnectType preConnectType = new PreConnectType();
        int iQuery_connected_channel = LinkVisual.query_connected_channel(str, i, preConnectType);
        if (iQuery_connected_channel > 0) {
            ALog.i(TAG, "ignore preConnectByIotId due to existed connection=" + iQuery_connected_channel + " , type=" + preConnectType.getType());
            return;
        }
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("streamType", Integer.valueOf(i));
        Boolean bool = Boolean.TRUE;
        map.put("relayEncrypted", bool);
        map.put("relayEncryptType", 0);
        map.put("forceIFrame", bool);
        map.put("enablePrePlay", bool);
        map.put("needDomainName", bool);
        map.put("enableWebSocket", bool);
        APIHelper.sendIoTRequest(lvbyte.lvdo.LIVE_QUERY, map, str, new lvdo(str, i));
    }
}
