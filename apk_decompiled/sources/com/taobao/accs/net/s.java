package com.taobao.accs.net;

import android.text.TextUtils;
import anet.channel.IAuth;
import anet.channel.RequestCb;
import anet.channel.bytes.ByteArray;
import anet.channel.statist.RequestStatistic;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import com.taobao.accs.net.j;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.UtilityImpl;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class s implements RequestCb {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ IAuth.AuthCallback f6404a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ j.a f6405b;

    @Override // anet.channel.RequestCb
    public void onDataReceive(ByteArray byteArray, boolean z) {
    }

    s(j.a aVar, IAuth.AuthCallback authCallback) {
        this.f6405b = aVar;
        this.f6404a = authCallback;
    }

    @Override // anet.channel.RequestCb
    public void onResponseCode(int i, Map<String, List<String>> map) {
        if (i == 200) {
            this.f6405b.e.i(BaseMonitor.ALARM_POINT_AUTH, "httpStatusCode", Integer.valueOf(i));
            this.f6404a.onAuthSuccess();
            if (this.f6405b.f6385d instanceof j) {
                ((j) this.f6405b.f6385d).q();
            }
        } else {
            this.f6405b.e.e(BaseMonitor.ALARM_POINT_AUTH, "httpStatusCode", Integer.valueOf(i));
            this.f6404a.onAuthFail(i, "auth fail");
        }
        Map<String, String> mapA = UtilityImpl.a(map);
        this.f6405b.e.d(BaseMonitor.ALARM_POINT_AUTH, "header", mapA);
        String str = mapA.get("x-at");
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.f6405b.f6385d.k = str;
    }

    @Override // anet.channel.RequestCb
    public void onFinish(int i, String str, RequestStatistic requestStatistic) {
        if (i < 0) {
            this.f6405b.e.e("auth onFinish", HiAnalyticsConstant.HaKey.BI_KEY_RESULT, Integer.valueOf(i));
            this.f6404a.onAuthFail(i, "onFinish auth fail");
        }
    }
}
