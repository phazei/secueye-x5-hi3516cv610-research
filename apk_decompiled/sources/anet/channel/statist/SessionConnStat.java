package anet.channel.statist;

import anet.channel.Session;
import anet.channel.status.NetworkStatusHelper;
import com.huawei.hms.framework.common.ContainerUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
@Monitor(module = "networkPrefer", monitorPoint = "conn_stat")
public class SessionConnStat extends StatObject {

    @Dimension
    public String errorCode;

    @Dimension
    public String errorMsg;

    @Dimension
    public StringBuilder errorTrace;

    @Dimension
    public JSONObject extra;

    @Dimension
    public String host;

    @Dimension
    public String ip;

    @Dimension
    public int port;

    @Dimension
    public String protocolType;

    @Dimension
    public int ret;

    @Dimension
    public String unit;

    @Dimension
    public int ipRefer = 0;

    @Dimension
    public int ipType = 1;

    @Dimension
    public double lng = 90000.0d;

    @Dimension
    public double lat = 90000.0d;

    @Dimension
    public float accuracy = -1.0f;

    @Dimension
    public int isProxy = 0;

    @Measure(max = 60000.0d)
    public long totalTime = 0;

    @Measure(max = 60000.0d)
    public long authTime = 0;
    public volatile boolean isCommited = false;
    public volatile long start = 0;
    public volatile long startConnect = 0;

    @Dimension
    public String netType = NetworkStatusHelper.getNetworkSubType();

    @Dimension
    public String bssid = NetworkStatusHelper.getWifiBSSID();

    @Dimension
    public int roaming = NetworkStatusHelper.isRoaming() ? 1 : 0;

    @Dimension
    public String mnc = NetworkStatusHelper.getSimOp();

    @Dimension
    public int retryTimes = -1;

    public void syncValueFromSession(Session session) {
        SessionStatistic sessionStatistic = session.q;
        this.ip = sessionStatistic.ip;
        this.port = sessionStatistic.port;
        this.ipRefer = sessionStatistic.ipRefer;
        this.ipType = sessionStatistic.ipType;
        this.protocolType = sessionStatistic.conntype;
        this.host = sessionStatistic.host;
        this.isProxy = sessionStatistic.isProxy;
        this.authTime = sessionStatistic.authTime;
        this.unit = session.getUnit();
        if (this.unit == null && this.ipRefer == 1) {
            this.unit = "LocalDNS";
        }
    }

    @Override // anet.channel.statist.StatObject
    public boolean beforeCommit() {
        if (this.isCommited) {
            return false;
        }
        this.isCommited = true;
        return true;
    }

    public void appendErrorTrace(int i) {
        if (this.errorTrace == null) {
            this.errorTrace = new StringBuilder();
        }
        if (this.errorTrace.length() > 0) {
            this.errorTrace.append(",");
        }
        StringBuilder sb = this.errorTrace;
        sb.append(i);
        sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
        sb.append(System.currentTimeMillis() - this.startConnect);
    }

    public void putExtra(String str, Object obj) {
        try {
            if (this.extra == null) {
                this.extra = new JSONObject();
            }
            this.extra.put(str, obj);
        } catch (JSONException unused) {
        }
    }
}
