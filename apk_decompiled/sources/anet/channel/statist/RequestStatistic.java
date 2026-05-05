package anet.channel.statist;

import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.entity.ConnType;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.util.AppLifecycle;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;
import com.huawei.hms.framework.common.ContainerUtils;
import config.Constants;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
@Monitor(module = "networkPrefer", monitorPoint = "network")
public class RequestStatistic extends StatObject {

    @Dimension
    public String abTestBucket;

    @Dimension
    public volatile String bizId;

    @Dimension
    public volatile String bssid;

    @Dimension
    public int deviceLevel;

    @Dimension
    public volatile String f_refer;

    @Dimension
    public int firstErrorCode;

    @Dimension
    public String firstProtocol;

    @Dimension
    public volatile String host;

    @Dimension
    public volatile String ip;

    @Dimension
    public volatile String isBg;

    @Dimension
    public volatile boolean isProxy;
    public boolean isReqMain;
    public boolean isReqSync;

    @Dimension
    public volatile boolean isSSL;
    public String locationUrl;

    @Dimension
    public String mnc;
    public volatile long netReqStart;

    @Dimension
    public volatile String netType;
    public String pTraceId;

    @Dimension
    public volatile int port;

    @Dimension
    public String process;

    @Dimension
    public volatile String protocolType;

    @Dimension
    public volatile String proxyType;

    @Deprecated
    public volatile String quicConnectionID;
    public volatile long reqServiceTransmissionEnd;

    @Dimension
    public volatile int ret;

    @Dimension
    public volatile int retryTimes;

    @Dimension
    public int roaming;
    public volatile long rspCbDispatch;
    public volatile long rspCbEnd;
    public volatile long rspCbStart;

    @Dimension
    public long sinceBgTime;

    @Dimension
    public long sinceInitTime;

    @Dimension
    public long sinceLastLaunchTime;

    @Dimension
    public String speedBucket;
    public String traceId;

    @Dimension
    public String unit;

    @Dimension(name = Constants.URL)
    public volatile String url;
    public String userInfo;

    @Dimension
    public volatile int ipRefer = 0;

    @Dimension
    public volatile int ipType = 1;

    @Dimension
    public volatile boolean isDNS = false;

    @Dimension(name = "errorCode")
    public volatile int statusCode = 0;

    @Dimension(name = WifiProvisionUtConst.KEY_ERROR_MSG)
    public volatile String msg = "";

    @Dimension
    public volatile String contentEncoding = null;

    @Dimension
    public volatile String contentType = null;

    @Dimension
    public volatile int degraded = 0;

    @Dimension
    public volatile StringBuilder errorTrace = null;

    @Dimension
    public double lng = 90000.0d;

    @Dimension
    public double lat = 90000.0d;

    @Dimension
    public float accuracy = -1.0f;

    @Dimension
    public JSONObject extra = null;

    @Dimension
    public int startType = 0;

    @Dimension
    public int isFromExternal = 0;

    @Measure
    public volatile long reqHeadInflateSize = 0;

    @Measure
    public volatile long reqBodyInflateSize = 0;

    @Measure
    public volatile long reqHeadDeflateSize = 0;

    @Measure
    public volatile long reqBodyDeflateSize = 0;

    @Measure
    public volatile long rspHeadDeflateSize = 0;

    @Measure
    public volatile long rspBodyDeflateSize = 0;

    @Measure
    public volatile long rspHeadInflateSize = 0;

    @Measure
    public volatile long rspBodyInflateSize = 0;

    @Measure
    public volatile long retryCostTime = 0;

    @Measure
    public volatile long connWaitTime = 0;

    @Measure
    public volatile long sendBeforeTime = 0;

    @Measure
    public volatile long processTime = 0;

    @Measure
    public volatile long sendDataTime = 0;

    @Measure
    public volatile long firstDataTime = 0;

    @Measure
    public volatile long recDataTime = 0;

    @Measure
    public volatile long serverRT = 0;

    @Measure
    public volatile long cacheTime = 0;

    @Measure
    public volatile long lastProcessTime = 0;

    @Measure
    public volatile long callbackTime = 0;

    @Measure(max = 60000.0d)
    public volatile long oneWayTime = 0;

    @Measure
    public volatile long sendDataSize = 0;

    @Measure
    public volatile long recDataSize = 0;

    @Deprecated
    public volatile long waitingTime = 0;

    @Measure
    public volatile long serializeTransferTime = 0;
    public final AtomicBoolean isDone = new AtomicBoolean(false);
    public volatile boolean spdyRequestSend = false;
    public volatile long start = 0;
    public volatile long reqStart = 0;
    public volatile long sendStart = 0;
    public volatile long sendEnd = 0;
    public volatile long rspStart = 0;
    public volatile long rspEnd = 0;
    public volatile long contentLength = 0;
    public volatile int tnetErrorCode = 0;

    public RequestStatistic(String str, String str2) {
        this.proxyType = "";
        this.netType = "";
        this.bssid = null;
        this.isBg = "";
        this.roaming = 0;
        this.mnc = "0";
        this.sinceBgTime = 0L;
        this.host = str;
        this.proxyType = NetworkStatusHelper.getProxyType();
        this.isProxy = !this.proxyType.isEmpty();
        this.netType = NetworkStatusHelper.getNetworkSubType();
        this.bssid = NetworkStatusHelper.getWifiBSSID();
        this.isBg = GlobalAppRuntimeInfo.isAppBackground() ? "bg" : "fg";
        if ("bg".equals(this.isBg) && AppLifecycle.lastEnterBackgroundTime > 0) {
            this.sinceBgTime = System.currentTimeMillis() - AppLifecycle.lastEnterBackgroundTime;
        }
        this.roaming = NetworkStatusHelper.isRoaming() ? 1 : 0;
        this.mnc = NetworkStatusHelper.getSimOp();
        this.bizId = str2;
    }

    public void setConnType(ConnType connType) {
        this.isSSL = connType.isSSL();
        this.protocolType = connType.toString();
    }

    public void setIPAndPort(String str, int i) {
        this.ip = str;
        this.port = i;
        if (str != null) {
            this.isDNS = true;
        }
        if (this.retryTimes != 0 || str == null) {
            return;
        }
        putExtra("firstIp", str);
    }

    public void setIpInfo(int i, int i2) {
        this.ipRefer = i;
        this.ipType = i2;
    }

    public void appendErrorTrace(String str) {
        if (this.errorTrace == null) {
            this.errorTrace = new StringBuilder();
        }
        if (this.errorTrace.length() != 0) {
            this.errorTrace.append(",");
        }
        StringBuilder sb = this.errorTrace;
        sb.append(str);
        sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
        sb.append(System.currentTimeMillis() - this.reqStart);
    }

    public void recordRedirect(int i, String str) {
        this.url = str;
        appendErrorTrace(String.valueOf(i));
        long jCurrentTimeMillis = System.currentTimeMillis();
        this.retryCostTime += jCurrentTimeMillis - this.start;
        this.start = jCurrentTimeMillis;
    }

    public void putExtra(String str, Object obj) {
        try {
            if (this.extra == null) {
                this.extra = new JSONObject();
            }
            this.extra.put(str, obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(256);
        sb.append("[RequestStatistic]ret=");
        sb.append(this.ret);
        sb.append(",statusCode=");
        sb.append(this.statusCode);
        sb.append(",msg=");
        sb.append(this.msg);
        sb.append(",bizId=");
        sb.append(this.bizId);
        sb.append(",host=");
        sb.append(this.host);
        sb.append(",ip=");
        sb.append(this.ip);
        sb.append(",port=");
        sb.append(this.port);
        sb.append(",protocolType=");
        sb.append(this.protocolType);
        sb.append(",retryTime=");
        sb.append(this.retryTimes);
        sb.append(",retryCostTime=");
        sb.append(this.retryCostTime);
        sb.append(",processTime=");
        sb.append(this.processTime);
        sb.append(",connWaitTime=");
        sb.append(this.connWaitTime);
        sb.append(",cacheTime=");
        sb.append(this.cacheTime);
        sb.append(",sendDataTime=");
        sb.append(this.sendDataTime);
        sb.append(",firstDataTime=");
        sb.append(this.firstDataTime);
        sb.append(",recDataTime=");
        sb.append(this.recDataTime);
        sb.append(",lastProcessTime=");
        sb.append(this.lastProcessTime);
        sb.append(",oneWayTime=");
        sb.append(this.oneWayTime);
        sb.append(",callbackTime=");
        sb.append(this.callbackTime);
        sb.append(",serverRT=");
        sb.append(this.serverRT);
        sb.append(",sendSize=");
        sb.append(this.sendDataSize);
        sb.append(",recDataSize=");
        sb.append(this.recDataSize);
        sb.append(",originalDataSize=");
        sb.append(this.rspBodyDeflateSize);
        if (this.extra != null) {
            sb.append(",extra=");
            sb.append(this.extra.toString());
        }
        sb.append(",isReqSync=");
        sb.append(this.isReqSync);
        sb.append(",isReqMain=");
        sb.append(this.isReqMain);
        sb.append(",process=");
        sb.append(this.process);
        if (!TextUtils.isEmpty(this.speedBucket)) {
            sb.append(", speedBucket=");
            sb.append(this.speedBucket);
        }
        sb.append(",url=");
        sb.append(this.url);
        return sb.toString();
    }

    @Override // anet.channel.statist.StatObject
    public boolean beforeCommit() {
        return this.statusCode != -200;
    }
}
