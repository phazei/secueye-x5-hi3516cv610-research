package com.taobao.accs.net;

import android.content.Context;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.common.Constants;
import com.taobao.accs.data.Message;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class l implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ j f6387a;

    l(j jVar) {
        this.f6387a = jVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f6387a.t.i("sendAccsHeartbeatMessage");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("dataType", "pingreq");
            jSONObject.put("timeInterval", this.f6387a.o);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ACCSManager.AccsRequest accsRequest = new ACCSManager.AccsRequest(null, null, jSONObject.toString().getBytes(), UUID.randomUUID().toString());
        accsRequest.setTarget("accs-iot");
        accsRequest.setTargetServiceName("sal");
        j jVar = this.f6387a;
        Context context = jVar.f6366d;
        String packageName = this.f6387a.f6366d.getPackageName();
        this.f6387a.i.getAppKey();
        this.f6387a.a(Message.a(jVar, context, packageName, Constants.TARGET_SERVICE, accsRequest, true), true);
    }
}
