package tools;

import android.content.Context;
import android.util.Log;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import config.AppConfig;

/* JADX INFO: loaded from: classes4.dex */
public class WXApiManager {
    private static final String WX_APP_ID = AppConfig.WX_APP_ID;
    private static volatile WXApiManager instance;
    private IWXAPI wxApi;

    private WXApiManager(Context context) {
        this.wxApi = WXAPIFactory.createWXAPI(context.getApplicationContext(), WX_APP_ID, true);
        this.wxApi.registerApp(WX_APP_ID);
        Log.e("WX_Login", "注册" + WX_APP_ID);
    }

    public static WXApiManager getInstance(Context context) {
        if (instance == null) {
            synchronized (WXApiManager.class) {
                if (instance == null) {
                    instance = new WXApiManager(context);
                }
            }
        }
        return instance;
    }

    public IWXAPI getWXApi() {
        return this.wxApi;
    }

    public boolean isWXAppInstalled() {
        IWXAPI iwxapi = this.wxApi;
        return iwxapi != null && iwxapi.isWXAppInstalled();
    }
}
