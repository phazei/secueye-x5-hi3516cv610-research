package activity;

import adapter.FeedbackReplyAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import bean.FeedbackBean;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityFeedbackHistoryBinding;
import java.util.Collections;
import java.util.HashMap;
import tools.LogEx;
import tools.OnMultiClickListener;
import tools.SystemUtil;
import tools.Utils;

/* JADX INFO: loaded from: classes.dex */
public class FeedbackHistoryActivity extends CommonActivity {

    /* JADX INFO: renamed from: adapter, reason: collision with root package name */
    FeedbackReplyAdapter f1568adapter;
    private ActivityFeedbackHistoryBinding binding;
    FeedbackBean feedbackBean;
    private Handler handler = new Handler(Looper.myLooper());

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_feedback_history;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityFeedbackHistoryBinding) DataBindingUtil.setContentView(this, R.layout.activity_feedback_history);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.FeedbackHistoryActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                FeedbackHistoryActivity.this.finish();
            }
        });
        this.binding.btNext.setOnClickListener(new OnMultiClickListener() { // from class: activity.FeedbackHistoryActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                FeedbackHistoryActivity.this.setData();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setData() {
        HashMap map = new HashMap();
        map.put("mobileSystem", "Android " + SystemUtil.getSystemVersion());
        map.put("appVersion", Utils.getVersionName(this));
        map.put("mobileModel", SystemUtil.getSystemModel());
        map.put("type", 1);
        if (this.binding.etInput.getText().toString().isEmpty()) {
            showToast(getResources().getString(R.string.problem_description_input));
            return;
        }
        map.put("content", this.binding.etInput.getText().toString());
        map.put("topicid", Long.valueOf(getIntent().getLongExtra("feedbackTopicId", 0L)));
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/feedback/reply/add").setApiVersion("1.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.FeedbackHistoryActivity.3
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.e(true, FeedbackHistoryActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() == 200) {
                    FeedbackHistoryActivity.this.handler.post(new Runnable() { // from class: activity.FeedbackHistoryActivity.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            FeedbackHistoryActivity.this.binding.etInput.setText("");
                            FeedbackHistoryActivity.this.showToast(FeedbackHistoryActivity.this.getResources().getString(R.string.feedback_success));
                            FeedbackHistoryActivity.this.getData();
                        }
                    });
                    return;
                }
                FeedbackHistoryActivity.this.showToast(ioTResponse.getLocalizedMsg() + "");
            }
        });
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        getData();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getData() {
        HashMap map = new HashMap();
        map.put("feedbackTopicId", Long.valueOf(getIntent().getLongExtra("feedbackTopicId", 0L)));
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/feedback/getbytopicId").setApiVersion("1.0.1").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.FeedbackHistoryActivity.4
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.e(true, FeedbackHistoryActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() != 200 || ioTResponse.getData() == null || "".equals(ioTResponse.getData().toString().trim())) {
                    return;
                }
                FeedbackHistoryActivity.this.feedbackBean = (FeedbackBean) JSON.parseObject(ioTResponse.getData().toString(), FeedbackBean.class);
                FeedbackHistoryActivity.this.handler.post(new Runnable() { // from class: activity.FeedbackHistoryActivity.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Collections.reverse(FeedbackHistoryActivity.this.feedbackBean.feedBackReplyList);
                        FeedbackHistoryActivity.this.binding.tvNoData.setVisibility(FeedbackHistoryActivity.this.feedbackBean.feedBackReplyList.size() == 0 ? 0 : 8);
                        FeedbackHistoryActivity.this.f1568adapter = new FeedbackReplyAdapter(FeedbackHistoryActivity.this, FeedbackHistoryActivity.this.feedbackBean.feedBackReplyList);
                        FeedbackHistoryActivity.this.binding.rvList.setLayoutManager(new LinearLayoutManager(FeedbackHistoryActivity.this));
                        FeedbackHistoryActivity.this.binding.rvList.setAdapter(FeedbackHistoryActivity.this.f1568adapter);
                    }
                });
            }
        });
    }
}
