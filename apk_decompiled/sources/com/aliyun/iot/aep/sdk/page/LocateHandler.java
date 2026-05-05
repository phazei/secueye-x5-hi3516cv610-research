package com.aliyun.iot.aep.sdk.page;

import android.os.Handler;
import android.os.Message;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.R;

/* JADX INFO: loaded from: classes2.dex */
public class LocateHandler extends Handler {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private OnLocationListener f4839a;

    public interface OnLocationListener {
        void onContinuedLocate(String str);

        void onFailLocate();

        void onSuccessLocate(IoTSmart.Country country);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (this.f4839a == null || message == null) {
            return;
        }
        super.handleMessage(message);
        switch (message.what) {
            case 0:
                this.f4839a.onContinuedLocate(AApplication.getInstance().getResources().getString(R.string.locating));
                break;
            case 1:
                this.f4839a.onContinuedLocate(AApplication.getInstance().getResources().getString(R.string.locating) + ".");
                break;
            case 2:
                this.f4839a.onContinuedLocate(AApplication.getInstance().getResources().getString(R.string.locating) + "..");
                break;
            case 3:
                this.f4839a.onContinuedLocate(AApplication.getInstance().getResources().getString(R.string.locating) + "...");
                break;
            case 4:
                this.f4839a.onSuccessLocate((IoTSmart.Country) message.obj);
                LocationUtil.cancelLocating();
                break;
            case 5:
                this.f4839a.onFailLocate();
                LocationUtil.cancelLocating();
                break;
        }
    }

    void a(OnLocationListener onLocationListener) {
        this.f4839a = onLocationListener;
    }
}
