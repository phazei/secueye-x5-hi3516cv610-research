package activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import bean.DeviceInfoBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.huawei.hms.framework.common.ContainerUtils;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityAuthBinding;
import java.util.HashMap;
import java.util.List;
import tools.LogEx;
import tools.OnMultiClickListener;

/* JADX INFO: loaded from: classes.dex */
public class AuthActivity extends CommonActivity {
    private String accountLinkSource;
    private boolean accountLinked;
    private String alexaAppUrl;
    private ActivityAuthBinding binding;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private String lwaFallbackUrl;
    private DeviceInfoBean nvrDevice;
    private String aLiClientId = "26001873";
    String code = "";
    String state = "";

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_auth;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntent(intent);
    }

    private void getIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        this.binding.tvCode.setText(intent.getData() + "");
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityAuthBinding) DataBindingUtil.setContentView(this, R.layout.activity_auth);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.AuthActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                AuthActivity.this.finish();
            }
        });
        getData();
        getState();
        this.binding.tvQuality.setOnClickListener(new OnMultiClickListener() { // from class: activity.AuthActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                AuthActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(AuthActivity.this.alexaAppUrl)));
            }
        });
        this.binding.tvQuality2.setOnClickListener(new OnMultiClickListener() { // from class: activity.AuthActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                AuthActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(AuthActivity.this.lwaFallbackUrl)));
            }
        });
        this.binding.tvQuality3.setOnClickListener(new OnMultiClickListener() { // from class: activity.AuthActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                AuthActivity authActivity = AuthActivity.this;
                authActivity.parsingUrl(authActivity.binding.tvCode.getText().toString());
            }
        });
    }

    private void getState() {
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/living/voice/alexa/apptoapp/status/get").setApiVersion("1.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).build(), new IoTCallback() { // from class: activity.AuthActivity.5
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, AuthActivity.this.TAG, exc.getLocalizedMessage());
                AuthActivity.this.runOnUiThread(new Runnable() { // from class: activity.AuthActivity.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(AuthActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.e(true, AuthActivity.this.TAG, "triggerRecordPlan:" + ioTResponse.getData() + "");
                int code = ioTResponse.getCode();
                String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    AuthActivity.this.binding.tvCode.setText(localizedMsg);
                    return;
                }
                JSONObject object = JSON.parseObject(ioTResponse.getData().toString());
                AuthActivity.this.accountLinked = object.getBoolean("accountLinked").booleanValue();
                AuthActivity.this.accountLinkSource = object.getString("accountLinkSource");
                AuthActivity.this.binding.tvQuality.setText("是否启用" + AuthActivity.this.accountLinked + "  地址" + AuthActivity.this.accountLinkSource);
            }
        });
    }

    private void getData() {
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/living/voice/alexa/apptoapp/config/get").setApiVersion("1.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).build(), new IoTCallback() { // from class: activity.AuthActivity.6
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, AuthActivity.this.TAG, exc.getLocalizedMessage());
                AuthActivity.this.runOnUiThread(new Runnable() { // from class: activity.AuthActivity.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(AuthActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.e(true, AuthActivity.this.TAG, "triggerRecordPlan:" + ioTResponse.getData() + "");
                int code = ioTResponse.getCode();
                String localizedMsg = ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    AuthActivity.this.binding.tvCode.setText(localizedMsg);
                    return;
                }
                JSONObject object = JSON.parseObject(ioTResponse.getData().toString());
                AuthActivity.this.lwaFallbackUrl = object.getString("lwaFallbackUrl").replace("https%3A%2F%2Falexa.secueye.cn%2F", "https://alexa.secueye.cn");
                AuthActivity.this.alexaAppUrl = object.getString("alexaAppUrl");
                Log.e("数据", "" + AuthActivity.this.lwaFallbackUrl);
                Log.e("数据", "" + AuthActivity.this.alexaAppUrl);
                AuthActivity.this.binding.tvCode.setText("获取技能信息成功");
            }
        });
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parsingUrl(String str) {
        String[] strArrSplit = str.trim().split("\\?");
        String str2 = strArrSplit[0];
        if (strArrSplit.length > 1) {
            String[] strArrSplit2 = strArrSplit[1].split("&");
            HashMap map = new HashMap();
            for (String str3 : strArrSplit2) {
                String[] strArrSplit3 = str3.split(ContainerUtils.KEY_VALUE_DELIMITER);
                if (strArrSplit3.length > 1) {
                    map.put(strArrSplit3[0], strArrSplit3[1]);
                }
            }
            this.state = (String) map.get("state");
            this.code = (String) map.get("code");
        }
        accountAuth2CodeGet(this.aLiClientId, this.state, str2, null);
    }

    private void accountAuth2CodeGet(@NonNull String str, @NonNull final String str2, @NonNull String str3, @Nullable List<String> list) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(TmpConstant.KEY_CLIENT_ID, (Object) str);
        jSONObject.put("state", (Object) str2);
        jSONObject.put("redirectUri", (Object) "https://pitangui.amazon.com/api/skill/link/M34QIZAH6O2L9G");
        if (list != null) {
            jSONObject.put("scope", (Object) list);
        }
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setAuthType(AlinkConstants.KEY_IOT_AUTH).setApiVersion("1.0.0").setPath("/living/account/oauth2/code/get").setParams(jSONObject.getInnerMap()).setScheme(Scheme.HTTPS).build(), new IoTCallback() { // from class: activity.AuthActivity.7
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                if ((exc != null ? exc.getMessage() : null) != null || exc == null) {
                    return;
                }
                exc.getLocalizedMessage();
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                Log.e("参数", "" + ioTResponse.getData().toString());
                if (ioTResponse != null && ioTResponse.getData() != null && ioTResponse.getCode() == 200) {
                    AuthActivity authActivity = AuthActivity.this;
                    authActivity.bindUser(authActivity.code, str2);
                } else {
                    AuthActivity.this.runOnUiThread(new Runnable() { // from class: activity.AuthActivity.7.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AuthActivity.this.showToast("" + ioTResponse.getMessage());
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindUser(String str, String str2) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("code", (Object) str);
        jSONObject.put("state", (Object) str2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setAuthType(AlinkConstants.KEY_IOT_AUTH).setApiVersion("1.0.1").setPath("/living/voice/alexa/apptoapp/launch").setParams(jSONObject.getInnerMap()).setScheme(Scheme.HTTPS).build(), new IoTCallback() { // from class: activity.AuthActivity.8
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                if ((exc != null ? exc.getMessage() : null) != null || exc == null) {
                    return;
                }
                exc.getLocalizedMessage();
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                Log.e("参数", "" + ioTResponse.getData().toString());
                AuthActivity.this.binding.tvCode.setText("" + ioTResponse.getCode());
                if (ioTResponse == null || ioTResponse.getData() == null) {
                    return;
                }
                ioTResponse.getCode();
            }
        });
    }
}
