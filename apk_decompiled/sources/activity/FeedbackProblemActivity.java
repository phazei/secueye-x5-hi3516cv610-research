package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import bean.DeviceInfoBeans;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityFeedbackProblemBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tools.LogEx;
import tools.OnMultiClickListener;
import tools.SharePreferenceManager;
import tools.SystemUtil;
import tools.Utils;
import view.SelectorDialogFragment;

/* JADX INFO: loaded from: classes.dex */
public class FeedbackProblemActivity extends CommonActivity {
    private ActivityFeedbackProblemBinding binding;
    private String[] feedbackTypeArr;
    private SelectorDialogFragment feedbackTypeFragment;
    private Handler handler = new Handler(Looper.myLooper());
    List<DeviceInfoBeans> deviceInfoBeanList = new ArrayList();
    List<String> names = new ArrayList();
    int position = -1;
    int type = -1;
    int[] types = {1, 2, 3};

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_feedback_problem;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityFeedbackProblemBinding) DataBindingUtil.setContentView(this, R.layout.activity_feedback_problem);
        setEdgeToEdge(this.binding.layoutMain);
        this.binding.leftImg.setOnClickListener(new View.OnClickListener() { // from class: activity.FeedbackProblemActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                FeedbackProblemActivity.this.finish();
            }
        });
        this.deviceInfoBeanList = JSON.parseArray(SharePreferenceManager.getInstance().getAllDeviceList(), DeviceInfoBeans.class);
        this.names.clear();
        List<DeviceInfoBeans> list = this.deviceInfoBeanList;
        if (list != null && list.size() != 0) {
            for (int i = 0; i < this.deviceInfoBeanList.size(); i++) {
                if (this.deviceInfoBeanList.get(i).getData().size() != 0) {
                    this.names.add(this.deviceInfoBeanList.get(i).getData().get(0).getName());
                }
            }
        } else {
            showToast(getResources().getString(R.string.never_add_device));
        }
        this.feedbackTypeArr = getResources().getStringArray(R.array.feedback_type);
        this.feedbackTypeFragment = new SelectorDialogFragment(getString(R.string.question_type), this.feedbackTypeArr);
        this.feedbackTypeFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.FeedbackProblemActivity.2
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i2) {
                FeedbackProblemActivity.this.binding.tvType.setText(FeedbackProblemActivity.this.feedbackTypeArr[i2]);
                FeedbackProblemActivity feedbackProblemActivity = FeedbackProblemActivity.this;
                feedbackProblemActivity.type = feedbackProblemActivity.types[i2];
            }
        });
        this.binding.layoutDevice.setOnClickListener(new OnMultiClickListener() { // from class: activity.FeedbackProblemActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent(FeedbackProblemActivity.this.getActivity(), (Class<?>) ChooseCameraActivity.class);
                intent.putStringArrayListExtra("names", (ArrayList) FeedbackProblemActivity.this.names);
                intent.putExtra("pos", FeedbackProblemActivity.this.position);
                FeedbackProblemActivity.this.startActivityForResult(intent, 4096);
            }
        });
        this.binding.btNext.setOnClickListener(new OnMultiClickListener() { // from class: activity.FeedbackProblemActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                FeedbackProblemActivity.this.SubData();
            }
        });
        this.binding.layoutType.setOnClickListener(new OnMultiClickListener() { // from class: activity.FeedbackProblemActivity.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                FeedbackProblemActivity.this.feedbackTypeFragment.showAllowingStateLoss(FeedbackProblemActivity.this.getSupportFragmentManager(), "", -1);
            }
        });
        this.binding.etContent.addTextChangedListener(new TextWatcher() { // from class: activity.FeedbackProblemActivity.6
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                Editable text = FeedbackProblemActivity.this.binding.etContent.getText();
                int length = text.length();
                FeedbackProblemActivity.this.binding.tvCount.setText(length + "/300");
                if (length > 300) {
                    int selectionEnd = Selection.getSelectionEnd(text);
                    FeedbackProblemActivity.this.binding.etContent.setText(text.toString().substring(0, 300));
                    Editable text2 = FeedbackProblemActivity.this.binding.etContent.getText();
                    if (selectionEnd > text2.length()) {
                        selectionEnd = text2.length();
                    }
                    Selection.setSelection(text2, selectionEnd);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void SubData() {
        HashMap map = new HashMap();
        map.put("mobileSystem", "Android " + SystemUtil.getSystemVersion());
        map.put("appVersion", Utils.getVersionName(this));
        int i = this.type;
        if (i == -1) {
            showToast(getResources().getString(R.string.question_type_input));
            return;
        }
        map.put("type", Integer.valueOf(i));
        int i2 = this.position;
        if (i2 == -1) {
            showToast(getResources().getString(R.string.select_device));
            return;
        }
        map.put("productKey", this.deviceInfoBeanList.get(i2).getData().get(0).getProductKey());
        map.put("iotId", this.deviceInfoBeanList.get(this.position).getData().get(0).getIotId());
        map.put("nickName", this.deviceInfoBeanList.get(this.position).getData().get(0).getNickName());
        if (this.binding.etContent.getText().toString().isEmpty()) {
            showToast(getResources().getString(R.string.problem_description_input));
            return;
        }
        map.put("content", this.binding.etContent.getText().toString());
        map.put("mobileModel", SystemUtil.getSystemModel());
        if (this.binding.etContact.getText().toString().isEmpty()) {
            showToast(getResources().getString(R.string.contact_input));
            return;
        }
        map.put("contact", this.binding.etContact.getText().toString());
        map.put("topic", "topic");
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/feedback/add").setApiVersion("1.0.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.FeedbackProblemActivity.7
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.e(true, FeedbackProblemActivity.this.TAG, "getRecordPlan2Dev   onFailure    e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() == 200) {
                    FeedbackProblemActivity.this.handler.post(new Runnable() { // from class: activity.FeedbackProblemActivity.7.1
                        @Override // java.lang.Runnable
                        public void run() {
                            FeedbackProblemActivity.this.showToast(FeedbackProblemActivity.this.getResources().getString(R.string.feedback_success));
                            FeedbackProblemActivity.this.finish();
                        }
                    });
                }
            }
        });
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 4096 && i2 == -1) {
            this.position = intent.getIntExtra("pos", 0);
            int i3 = this.position;
            if (i3 == -1 || i3 >= this.names.size()) {
                return;
            }
            this.binding.tvName.setText(this.names.get(this.position));
        }
    }
}
