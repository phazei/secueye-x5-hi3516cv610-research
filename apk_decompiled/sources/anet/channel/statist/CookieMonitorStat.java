package anet.channel.statist;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
@Monitor(module = "networkPrefer", monitorPoint = "cookieMonitor")
public class CookieMonitorStat extends StatObject {

    @Dimension
    public String cookieName;

    @Dimension
    public String cookieText;

    @Dimension
    public int missType;

    @Dimension
    public String setCookie;

    @Dimension
    public String url;

    public CookieMonitorStat(String str) {
        this.url = str;
    }
}
