package com.seculink.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.Scopes;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import tools.WXApiManager;

/* JADX INFO: loaded from: classes3.dex */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private static String TAG = "WX_Login";
    private String expires_in;
    private Handler handler;
    private IWXAPI mWeixinAPI;
    private String refresh_token;

    private void getAccess_token(String str) {
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.e(TAG, "onCreate: ");
        this.handler = new Handler();
        this.mWeixinAPI = WXApiManager.getInstance(getApplicationContext()).getWXApi();
        this.mWeixinAPI.handleIntent(getIntent(), this);
    }

    @Override // android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        this.mWeixinAPI.handleIntent(intent, this);
    }

    @Override // com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
    public void onReq(BaseReq baseReq) {
        Log.e("WX_Login", "onReq: " + baseReq);
        finish();
    }

    @Override // com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
    public void onResp(BaseResp baseResp) {
        Log.e("WX_Login", "errStr: " + baseResp.errStr);
        Log.e("WX_Login", "openId: " + baseResp.openId);
        Log.e("WX_Login", "transaction: " + baseResp.transaction);
        Log.e("WX_Login", "errCode: " + baseResp.errCode);
        Log.e("WX_Login", "getType: " + baseResp.getType());
        Log.e("WX_Login", "checkArgs: " + baseResp.checkArgs());
        int i = baseResp.errCode;
        if (i == -4 || i == -2) {
            if (2 == baseResp.getType()) {
                Log.e("WX_Login", "分享失败");
            } else {
                Log.e("WX_Login", "登录失败");
            }
            Toast.makeText(this, "用户取消", 0).show();
        }
        if (i != 0) {
            return;
        }
        switch (baseResp.getType()) {
            case 1:
                String str = ((SendAuth.Resp) baseResp).code;
                Toast.makeText(this, "登陆成功" + str, 0).show();
                getAccess_token(str);
                Log.e("WX_Login", "code: " + str);
                break;
            case 2:
                Log.e("WX_Login", "微信分享成功");
                finish();
                break;
        }
    }

    private void getUserMesg(String str, String str2) {
        new OkHttpClient().newCall(new Request.Builder().url("https://api.weixin.qq.com/sns/userinfo?access_token=" + str + "&openid=" + str2).get().build()).enqueue(new Callback() { // from class: com.seculink.app.wxapi.WXEntryActivity.1
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                Log.d(WXEntryActivity.TAG, "onFailure: ");
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                String strString = response.body().string();
                Log.e("------", "全部数据: " + strString);
                try {
                    JSONObject jSONObject = new JSONObject(strString);
                    String string = jSONObject.getString("nickname");
                    String[] strArrSplit = string.split(",");
                    for (int i = 0; i < strArrSplit.length; i++) {
                        Log.e("split  " + i, "" + strArrSplit[i]);
                    }
                    String string2 = jSONObject.get("sex").toString();
                    String string3 = jSONObject.getString("headimgurl");
                    String string4 = jSONObject.getString("country");
                    String string5 = jSONObject.getString(Scopes.OPEN_ID);
                    String string6 = jSONObject.getString("province");
                    String string7 = jSONObject.getString("unionid");
                    String string8 = jSONObject.getString("city");
                    Log.e(WXEntryActivity.TAG, "用户基本信息:");
                    Log.e(WXEntryActivity.TAG, "nickname:" + string);
                    Log.e(WXEntryActivity.TAG, "sex:       " + string2);
                    Log.e(WXEntryActivity.TAG, "headimgurl:" + string3);
                    Log.e(WXEntryActivity.TAG, "openid:" + string5);
                    Log.e(WXEntryActivity.TAG, "country:" + string4);
                    Log.e(WXEntryActivity.TAG, "province:" + string6);
                    Log.e(WXEntryActivity.TAG, "unionid:" + string7);
                    Log.e(WXEntryActivity.TAG, "city:" + string8);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("WX_Login", "登陆错误,请重新再试");
                }
            }
        });
    }
}
