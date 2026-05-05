package tools;

import android.os.Handler;
import android.os.Message;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes4.dex */
public class LocateHandler extends Handler {
    private OnLocationListener locationListener;

    public interface OnLocationListener {
        void onContinuedLocate(String str);

        void onFailLocate();

        void onSuccessLocate(IoTSmart.Country country);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (this.locationListener == null) {
        }
        super.handleMessage(message);
        switch (message.what) {
            case 0:
                this.locationListener.onContinuedLocate(AApplication.getInstance().getResources().getString(R.string.locating));
                break;
            case 1:
                this.locationListener.onContinuedLocate(AApplication.getInstance().getResources().getString(R.string.locating) + ".");
                break;
            case 2:
                this.locationListener.onContinuedLocate(AApplication.getInstance().getResources().getString(R.string.locating) + "..");
                break;
            case 3:
                this.locationListener.onContinuedLocate(AApplication.getInstance().getResources().getString(R.string.locating) + "...");
                break;
            case 4:
                this.locationListener.onSuccessLocate((IoTSmart.Country) message.obj);
                com.aliyun.iot.aep.sdk.page.LocationUtil.cancelLocating();
                break;
            case 5:
                this.locationListener.onFailLocate();
                com.aliyun.iot.aep.sdk.page.LocationUtil.cancelLocating();
                break;
        }
    }

    void setLocationListener(OnLocationListener onLocationListener) {
        this.locationListener = onLocationListener;
    }
}
