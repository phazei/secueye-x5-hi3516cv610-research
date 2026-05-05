package a.a.a.a.b.f;

import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.aliyun.alink.linksdk.connectsdk.ApiCallBack;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: LowEnergyAssist.java */
/* JADX INFO: loaded from: classes.dex */
public class a extends ApiCallBack {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ IActionListener f1324a;

    public a(IActionListener iActionListener) {
        this.f1324a = iActionListener;
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    public void onFail(int i, String str) {
        Utils.notifyFailed(this.f1324a, i, str);
    }

    @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
    public void onSuccess(Object obj) {
        Utils.notifySuccess((IActionListener<boolean>) this.f1324a, true);
    }
}
