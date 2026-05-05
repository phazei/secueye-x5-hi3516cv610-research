package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.seculink.app.R;
import dialog.BaseDialog;
import tools.OnMultiClickListener;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class TmallServiceActivity extends CommonActivity {
    TitleView fl_titlebar;
    private Handler handler = new Handler();
    LinearLayout layout_main;
    LinearLayout ll_tmall;
    LinearLayout ll_tmall_unbind;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_tmall_service;
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) TmallServiceActivity.class));
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.ll_tmall = (LinearLayout) findViewById(R.id.ll_tmall);
        this.ll_tmall_unbind = (LinearLayout) findViewById(R.id.ll_tmall_unbind);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.TmallServiceActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                TmallServiceActivity.this.finish();
            }
        });
        this.ll_tmall.setOnClickListener(new OnMultiClickListener() { // from class: activity.TmallServiceActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                TmallServiceActivity.this.bindTmall();
            }
        });
        this.ll_tmall_unbind.setOnClickListener(new OnMultiClickListener() { // from class: activity.TmallServiceActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                TmallServiceActivity.this.unbindTmall();
            }
        });
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.handler.removeCallbacksAndMessages(null);
    }

    public void bindTmall() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("accountType", (Object) "TAOBAO");
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setAuthType(AlinkConstants.KEY_IOT_AUTH).setApiVersion("1.0.5").setPath("/account/thirdparty/get").setParams(jSONObject.getInnerMap()).setScheme(Scheme.HTTPS).build(), new IoTCallback() { // from class: activity.TmallServiceActivity.4
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                TmallServiceActivity.this.handler.post(new Runnable() { // from class: activity.TmallServiceActivity.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(TmallServiceActivity.this.getActivity(), R.string.get_account_info_fail, 1).show();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                String string;
                String string2;
                if (ioTResponse.getCode() != 200) {
                    TmallServiceActivity.this.handler.post(new Runnable() { // from class: activity.TmallServiceActivity.4.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(TmallServiceActivity.this.getActivity(), R.string.get_account_info_fail, 1).show();
                        }
                    });
                    return;
                }
                if (ioTResponse.getData() == null) {
                    TmallServiceActivity.this.handler.post(new Runnable() { // from class: activity.TmallServiceActivity.4.4
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(TmallServiceActivity.this.getActivity(), R.string.get_account_info_fail, 1).show();
                        }
                    });
                    return;
                }
                String string3 = null;
                try {
                    JSONObject object = JSONObject.parseObject(ioTResponse.getData().toString());
                    string = object.getString("accountId");
                    try {
                        string2 = object.getString("accountType");
                    } catch (Exception e) {
                        e = e;
                        string2 = null;
                    }
                    try {
                        string3 = object.getString("linkIndentityId");
                    } catch (Exception e2) {
                        e = e2;
                        e.printStackTrace();
                    }
                } catch (Exception e3) {
                    e = e3;
                    string = null;
                    string2 = null;
                }
                if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string2) && !TextUtils.isEmpty(string3)) {
                    TmallServiceActivity.this.handler.post(new Runnable() { // from class: activity.TmallServiceActivity.4.3
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(TmallServiceActivity.this.getActivity(), R.string.account_has_bound, 1).show();
                        }
                    });
                } else {
                    TmallServiceActivity.this.startActivity(new Intent(TmallServiceActivity.this.getActivity(), (Class<?>) TmallGenieActivity.class));
                }
            }
        });
    }

    public void unbindTmall() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("accountType", (Object) "TAOBAO");
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setAuthType(AlinkConstants.KEY_IOT_AUTH).setApiVersion("1.0.5").setPath("/account/thirdparty/get").setParams(jSONObject.getInnerMap()).setScheme(Scheme.HTTPS).build(), new AnonymousClass5());
    }

    /* JADX INFO: renamed from: activity.TmallServiceActivity$5, reason: invalid class name */
    class AnonymousClass5 implements IoTCallback {
        AnonymousClass5() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            TmallServiceActivity.this.handler.post(new Runnable() { // from class: activity.TmallServiceActivity.5.1
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(TmallServiceActivity.this.getActivity(), R.string.get_account_info_fail, 1).show();
                }
            });
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            String string;
            String string2;
            if (ioTResponse.getCode() != 200) {
                TmallServiceActivity.this.handler.post(new Runnable() { // from class: activity.TmallServiceActivity.5.2
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(TmallServiceActivity.this.getActivity(), R.string.get_account_info_fail, 1).show();
                    }
                });
                return;
            }
            if (ioTResponse.getData() == null) {
                TmallServiceActivity.this.handler.post(new Runnable() { // from class: activity.TmallServiceActivity.5.5
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(TmallServiceActivity.this.getActivity(), R.string.get_account_info_fail, 1).show();
                    }
                });
                return;
            }
            String string3 = null;
            try {
                JSONObject object = JSONObject.parseObject(ioTResponse.getData().toString());
                string = object.getString("accountId");
                try {
                    string2 = object.getString("accountType");
                } catch (Exception e) {
                    e = e;
                    string2 = null;
                }
                try {
                    string3 = object.getString("linkIndentityId");
                } catch (Exception e2) {
                    e = e2;
                    e.printStackTrace();
                }
            } catch (Exception e3) {
                e = e3;
                string = null;
                string2 = null;
            }
            if (TextUtils.isEmpty(string) || TextUtils.isEmpty(string2) || TextUtils.isEmpty(string3)) {
                TmallServiceActivity.this.handler.post(new Runnable() { // from class: activity.TmallServiceActivity.5.4
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(TmallServiceActivity.this.getActivity(), R.string.account_no_bind_tmall, 1).show();
                    }
                });
                return;
            }
            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(TmallServiceActivity.this.getString(R.string.tmall_unbind) + "?").leftBtnText(TmallServiceActivity.this.getString(R.string.confirm)).rightBtnText(TmallServiceActivity.this.getString(R.string.cancel)).clickLeft(new AnonymousClass3()).canCancel(false).create().show(TmallServiceActivity.this.getSupportFragmentManager(), "");
        }

        /* JADX INFO: renamed from: activity.TmallServiceActivity$5$3, reason: invalid class name */
        class AnonymousClass3 implements View.OnClickListener {
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("accountType", (Object) "TAOBAO");
                new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setAuthType(AlinkConstants.KEY_IOT_AUTH).setApiVersion("1.0.5").setPath("/account/thirdparty/unbind").setParams(jSONObject.getInnerMap()).setScheme(Scheme.HTTPS).build(), new IoTCallback() { // from class: activity.TmallServiceActivity.5.3.1
                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onFailure(IoTRequest ioTRequest, Exception exc) {
                        TmallServiceActivity.this.getActivity().runOnUiThread(new Runnable() { // from class: activity.TmallServiceActivity.5.3.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(TmallServiceActivity.this.getActivity().getApplicationContext(), R.string.unbind_fail, 1).show();
                            }
                        });
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                        if (ioTResponse.getCode() == 200) {
                            TmallServiceActivity.this.getActivity().runOnUiThread(new Runnable() { // from class: activity.TmallServiceActivity.5.3.1.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(TmallServiceActivity.this.getActivity().getApplicationContext(), R.string.unbind_success, 1).show();
                                }
                            });
                        } else {
                            TmallServiceActivity.this.getActivity().runOnUiThread(new Runnable() { // from class: activity.TmallServiceActivity.5.3.1.3
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(TmallServiceActivity.this.getActivity().getApplicationContext(), R.string.unbind_fail, 1).show();
                                }
                            });
                        }
                    }
                });
            }
        }
    }
}
