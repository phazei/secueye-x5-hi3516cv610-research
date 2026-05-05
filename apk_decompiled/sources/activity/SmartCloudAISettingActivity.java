package activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import bean.AIBean;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.google.gson.Gson;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.HashMap;
import view.ItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class SmartCloudAISettingActivity extends CommonActivity {
    String iotId;
    ItemView itemView1;
    ItemView itemView2;
    ItemView itemView3;
    ItemView itemView4;
    ConstraintLayout layout_main;
    private SwitchCompat sw1;
    private SwitchCompat sw2;
    private SwitchCompat sw3;
    private SwitchCompat sw4;
    TitleView titleView;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_smart_cloud_setting_layout;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (ConstraintLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.titleView = (TitleView) findViewById(R.id.titleView);
        this.titleView.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.SmartCloudAISettingActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                SmartCloudAISettingActivity.this.finish();
            }
        });
        this.itemView1 = (ItemView) findViewById(R.id.itemView);
        this.itemView2 = (ItemView) findViewById(R.id.itemView2);
        this.itemView3 = (ItemView) findViewById(R.id.itemView3);
        this.itemView4 = (ItemView) findViewById(R.id.itemView4);
        this.itemView1.addRightView(new SwitchCompat(this));
        this.sw1 = (SwitchCompat) this.itemView1.getRightView();
        this.sw1.setTextOff("");
        this.sw1.setTextOn("");
        this.sw1.setText("");
        this.sw1.setThumbDrawable(null);
        this.sw1.setBackgroundResource(R.drawable.sel_switch);
        this.sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: activity.SmartCloudAISettingActivity.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    SmartCloudAISettingActivity.this.openAI(3);
                } else {
                    SmartCloudAISettingActivity.this.closeAI(3);
                }
            }
        });
        this.itemView2.addRightView(new SwitchCompat(this));
        this.sw2 = (SwitchCompat) this.itemView2.getRightView();
        this.sw2.setTextOff("");
        this.sw2.setTextOn("");
        this.sw2.setText("");
        this.sw2.setThumbDrawable(null);
        this.sw2.setBackgroundResource(R.drawable.sel_switch);
        this.sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: activity.SmartCloudAISettingActivity.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    SmartCloudAISettingActivity.this.openAI(11500);
                } else {
                    SmartCloudAISettingActivity.this.closeAI(11500);
                }
            }
        });
        this.itemView3.addRightView(new SwitchCompat(this));
        this.sw3 = (SwitchCompat) this.itemView3.getRightView();
        this.sw3.setTextOff("");
        this.sw3.setTextOn("");
        this.sw3.setText("");
        this.sw3.setThumbDrawable(null);
        this.sw3.setBackgroundResource(R.drawable.sel_switch);
        this.sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: activity.SmartCloudAISettingActivity.4
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    SmartCloudAISettingActivity.this.openAI(11501);
                } else {
                    SmartCloudAISettingActivity.this.closeAI(11501);
                }
            }
        });
        this.itemView4.addRightView(new SwitchCompat(this));
        this.sw4 = (SwitchCompat) this.itemView4.getRightView();
        this.sw4.setTextOff("");
        this.sw4.setTextOn("");
        this.sw4.setText("");
        this.sw4.setThumbDrawable(null);
        this.sw4.setBackgroundResource(R.drawable.sel_switch);
        this.sw4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: activity.SmartCloudAISettingActivity.5
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    SmartCloudAISettingActivity.this.openAI(11015);
                } else {
                    SmartCloudAISettingActivity.this.closeAI(11015);
                }
            }
        });
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.iotId = getIntent().getStringExtra("iotId");
        getAI();
    }

    private void getAI() {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/vision/customer/aiservice/record/query").setScheme(Scheme.HTTPS).setApiVersion("1.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.SmartCloudAISettingActivity.6
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                Log.d(SmartCloudAISettingActivity.this.TAG, "onFailure: ");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                Log.d(SmartCloudAISettingActivity.this.TAG, "onResponse: ");
                AIBean aIBean = (AIBean) new Gson().fromJson(ioTResponse.getData().toString(), AIBean.class);
                for (int i = 0; i < aIBean.getAiServiceList().size(); i++) {
                    int serviceCode = aIBean.getAiServiceList().get(i).getServiceCode();
                    if (serviceCode == 3) {
                        SmartCloudAISettingActivity.this.runOnUiThread(new Runnable() { // from class: activity.SmartCloudAISettingActivity.6.1
                            @Override // java.lang.Runnable
                            public void run() {
                                SmartCloudAISettingActivity.this.sw1.setChecked(true);
                            }
                        });
                    } else if (serviceCode != 11015) {
                        switch (serviceCode) {
                            case 11500:
                                SmartCloudAISettingActivity.this.runOnUiThread(new Runnable() { // from class: activity.SmartCloudAISettingActivity.6.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        SmartCloudAISettingActivity.this.sw2.setChecked(true);
                                    }
                                });
                                break;
                            case 11501:
                                SmartCloudAISettingActivity.this.runOnUiThread(new Runnable() { // from class: activity.SmartCloudAISettingActivity.6.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        SmartCloudAISettingActivity.this.sw3.setChecked(true);
                                    }
                                });
                                break;
                        }
                    } else {
                        SmartCloudAISettingActivity.this.runOnUiThread(new Runnable() { // from class: activity.SmartCloudAISettingActivity.6.4
                            @Override // java.lang.Runnable
                            public void run() {
                                SmartCloudAISettingActivity.this.sw4.setChecked(true);
                            }
                        });
                    }
                }
            }
        });
    }

    private void getAIList() {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/vision/customer/aiservice/query").setScheme(Scheme.HTTPS).setApiVersion("1.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.SmartCloudAISettingActivity.7
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                Log.d(SmartCloudAISettingActivity.this.TAG, "onFailure: ");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                Log.d(SmartCloudAISettingActivity.this.TAG, "onResponse: ");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openAI(int i) {
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(i));
        map.put("iotId", this.iotId);
        map.put("aiService", arrayList);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/vision/customer/aiservice/open").setScheme(Scheme.HTTPS).setApiVersion("1.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.SmartCloudAISettingActivity.8
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                Log.d(SmartCloudAISettingActivity.this.TAG, "onFailure: ");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                Log.d(SmartCloudAISettingActivity.this.TAG, "onResponse: ");
                if (ioTResponse.getCode() == 200) {
                    SmartCloudAISettingActivity.this.runOnUiThread(new Runnable() { // from class: activity.SmartCloudAISettingActivity.8.1
                        @Override // java.lang.Runnable
                        public void run() {
                        }
                    });
                } else {
                    SmartCloudAISettingActivity.this.runOnUiThread(new Runnable() { // from class: activity.SmartCloudAISettingActivity.8.2
                        @Override // java.lang.Runnable
                        public void run() {
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeAI(int i) {
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(i));
        map.put("iotId", this.iotId);
        map.put("aiService", arrayList);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/vision/customer/aiservice/close").setScheme(Scheme.HTTPS).setApiVersion("1.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.SmartCloudAISettingActivity.9
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                Log.d(SmartCloudAISettingActivity.this.TAG, "onFailure: ");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                Log.d(SmartCloudAISettingActivity.this.TAG, "onResponse: ");
                if (ioTResponse.getCode() == 200) {
                    SmartCloudAISettingActivity.this.runOnUiThread(new Runnable() { // from class: activity.SmartCloudAISettingActivity.9.1
                        @Override // java.lang.Runnable
                        public void run() {
                        }
                    });
                } else {
                    SmartCloudAISettingActivity.this.runOnUiThread(new Runnable() { // from class: activity.SmartCloudAISettingActivity.9.2
                        @Override // java.lang.Runnable
                        public void run() {
                        }
                    });
                }
            }
        });
    }
}
