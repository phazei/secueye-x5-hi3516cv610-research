package activity;

import adapter.FeedbackAdapter;
import android.content.Intent;
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
import com.seculink.app.databinding.ActivityFeedbackRecordBinding;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import tools.LogEx;
import tools.OnMultiClickListener;

/* JADX INFO: loaded from: classes.dex */
public class FeedbackRecordActivity extends CommonActivity {
    private ActivityFeedbackRecordBinding binding;
    List<FeedbackBean> deviceInfoBeanList;
    FeedbackAdapter feedbackAdapter;
    private Handler handler = new Handler(Looper.myLooper());

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_feedback_record;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        getData();
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityFeedbackRecordBinding) DataBindingUtil.setContentView(this, R.layout.activity_feedback_record);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.FeedbackRecordActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                FeedbackRecordActivity.this.finish();
            }
        });
        this.binding.btNext.setOnClickListener(new OnMultiClickListener() { // from class: activity.FeedbackRecordActivity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                FeedbackRecordActivity feedbackRecordActivity = FeedbackRecordActivity.this;
                feedbackRecordActivity.startActivity(new Intent(feedbackRecordActivity, (Class<?>) FeedbackProblemActivity.class));
            }
        });
    }

    private void getData() {
        HashMap map = new HashMap();
        map.put(AlinkConstants.KEY_PAGE_NO, 1);
        map.put(AlinkConstants.KEY_PAGE_SIZE, 100);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/feedbacklist/querybyuid").setApiVersion("1.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass3());
    }

    /* JADX INFO: renamed from: activity.FeedbackRecordActivity$3, reason: invalid class name */
    class AnonymousClass3 implements IoTCallback {
        AnonymousClass3() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.e(true, FeedbackRecordActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            if (ioTResponse.getCode() != 200) {
                FeedbackRecordActivity.this.handler.post(new Runnable() { // from class: activity.FeedbackRecordActivity.3.3
                    @Override // java.lang.Runnable
                    public void run() {
                        FeedbackRecordActivity.this.binding.tvNoData.setVisibility(0);
                        FeedbackRecordActivity.this.binding.rvList.setVisibility(8);
                    }
                });
                return;
            }
            if (ioTResponse.getData() == null || "".equals(ioTResponse.getData().toString().trim())) {
                FeedbackRecordActivity.this.handler.post(new Runnable() { // from class: activity.FeedbackRecordActivity.3.2
                    @Override // java.lang.Runnable
                    public void run() {
                        FeedbackRecordActivity.this.binding.tvNoData.setVisibility(0);
                        FeedbackRecordActivity.this.binding.rvList.setVisibility(8);
                    }
                });
                return;
            }
            try {
                String string = ((JSONObject) ioTResponse.getData()).getString("data");
                FeedbackRecordActivity.this.deviceInfoBeanList = JSON.parseArray(string, FeedbackBean.class);
                FeedbackRecordActivity.this.handler.post(new Runnable() { // from class: activity.FeedbackRecordActivity.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (FeedbackRecordActivity.this.deviceInfoBeanList.size() != 0) {
                            FeedbackRecordActivity.this.binding.tvNoData.setVisibility(8);
                            FeedbackRecordActivity.this.binding.rvList.setVisibility(0);
                            FeedbackRecordActivity.this.feedbackAdapter = new FeedbackAdapter(FeedbackRecordActivity.this, FeedbackRecordActivity.this.deviceInfoBeanList);
                            FeedbackRecordActivity.this.binding.rvList.setLayoutManager(new LinearLayoutManager(FeedbackRecordActivity.this));
                            FeedbackRecordActivity.this.binding.rvList.setAdapter(FeedbackRecordActivity.this.feedbackAdapter);
                            FeedbackRecordActivity.this.feedbackAdapter.setOnItemClickListener(new FeedbackAdapter.OnItemClickListener() { // from class: activity.FeedbackRecordActivity.3.1.1
                                @Override // adapter.FeedbackAdapter.OnItemClickListener
                                public void onItemClick(int i, FeedbackBean feedbackBean) {
                                    Intent intent = new Intent(FeedbackRecordActivity.this, (Class<?>) FeedbackHistoryActivity.class);
                                    intent.putExtra("feedbackTopicId", FeedbackRecordActivity.this.deviceInfoBeanList.get(i).id);
                                    FeedbackRecordActivity.this.startActivity(intent);
                                }
                            });
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
