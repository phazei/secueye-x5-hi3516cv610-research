package activity;

import adapter.CloudDownloadAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.internal.view.SupportMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import bean.CloudDownloadBean;
import bean.CloudVideo;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.ut.AUserTrack;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.google.gson.Gson;
import com.haibin.calendarview.CalendarModel;
import com.haibin.calendarview.CalendarView;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityCloudDownloadBinding;
import dialog.BaseDialog;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import jp.wasabeef.glide.transformations.BuildConfig;
import org.json.JSONException;
import org.json.JSONObject;
import tools.DateUtil;
import tools.SpUtil;
import tools.Utils;
import view.DialogView;
import view.DownloadUtil;
import view.EditViewIpc;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class CloudDownloadActivity extends CommonActivity {

    /* JADX INFO: renamed from: adapter, reason: collision with root package name */
    private CloudDownloadAdapter f1566adapter;
    private ActivityCloudDownloadBinding binding;
    private List<CloudVideo.RecordFileListBean> cacheListBean;
    private boolean checkAll;
    private CloudVideo cloudVideoBean;
    int current_Month;
    private TextView deleteText;
    private EditViewIpc editView;
    private Handler handler;
    private int hasSelectedNum = 0;
    private String iotId;
    private boolean isEidt;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_cloud_download;
    }

    static /* synthetic */ int access$408(CloudDownloadActivity cloudDownloadActivity) {
        int i = cloudDownloadActivity.hasSelectedNum;
        cloudDownloadActivity.hasSelectedNum = i + 1;
        return i;
    }

    static /* synthetic */ int access$410(CloudDownloadActivity cloudDownloadActivity) {
        int i = cloudDownloadActivity.hasSelectedNum;
        cloudDownloadActivity.hasSelectedNum = i - 1;
        return i;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.isEidt = false;
        this.binding = ActivityCloudDownloadBinding.inflate(getLayoutInflater());
        this.iotId = getIntent().getStringExtra("iotId");
        int year = DateUtil.getYear();
        int month = DateUtil.getMonth();
        int day = DateUtil.getDay();
        getCloudRecordList((int) (DateUtil.getTimeMillins(year, month, day, 0, 0, 0) / 1000), (int) (DateUtil.getTimeMillins(year, month, day, 23, 59, 59) / 1000));
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(this.binding.getRoot());
        this.handler = new Handler(getMainLooper());
        this.binding.title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.CloudDownloadActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                CloudDownloadActivity.this.finish();
            }
        });
        setEdgeToEdge(this.binding.layoutMain);
        this.editView = (EditViewIpc) findViewById(R.id.editView);
        this.editView.setTvCheckAllDrawable(false);
        this.editView.setTvDeleteDrawable(false);
        this.current_Month = this.binding.calendarView.getCurMonth();
        getMonthList(this.iotId, this.binding.calendarView.getCurYear(), this.current_Month);
        this.binding.calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() { // from class: activity.CloudDownloadActivity.2
            @Override // com.haibin.calendarview.CalendarView.OnCalendarSelectListener
            public void onCalendarOutOfRange(CalendarModel calendarModel) {
            }

            @Override // com.haibin.calendarview.CalendarView.OnCalendarSelectListener
            public void onCalendarSelect(CalendarModel calendarModel, boolean z) {
                CloudDownloadActivity.this.getCloudRecordList((int) (DateUtil.getTimeMillins(calendarModel.getYear(), calendarModel.getMonth(), calendarModel.getDay(), 0, 0, 0) / 1000), (int) (DateUtil.getTimeMillins(calendarModel.getYear(), calendarModel.getMonth(), calendarModel.getDay(), 23, 59, 59) / 1000));
                if (CloudDownloadActivity.this.current_Month != calendarModel.getMonth()) {
                    CloudDownloadActivity.this.current_Month = calendarModel.getMonth();
                    CloudDownloadActivity cloudDownloadActivity = CloudDownloadActivity.this;
                    cloudDownloadActivity.getMonthList(cloudDownloadActivity.iotId, calendarModel.getYear(), calendarModel.getMonth());
                }
            }
        });
        this.f1566adapter = new CloudDownloadAdapter();
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        this.binding.recycler.setAdapter(this.f1566adapter);
        this.f1566adapter.setOnItemClickListener(new CloudDownloadAdapter.OnItemClickListener() { // from class: activity.CloudDownloadActivity.3
            @Override // adapter.CloudDownloadAdapter.OnItemClickListener
            public void onClick(final int i, Button button) {
                if (CloudDownloadActivity.this.isEidt) {
                    button.setVisibility(button.getVisibility() == 0 ? 8 : 0);
                    if (button.getVisibility() == 0) {
                        CloudDownloadActivity.this.cloudVideoBean.getRecordFileList().get(i).setIsSelected(true);
                        CloudDownloadActivity.access$408(CloudDownloadActivity.this);
                    } else {
                        CloudDownloadActivity.this.cloudVideoBean.getRecordFileList().get(i).setIsSelected(false);
                        CloudDownloadActivity.access$410(CloudDownloadActivity.this);
                    }
                    TitleView titleView = CloudDownloadActivity.this.binding.title;
                    CloudDownloadActivity cloudDownloadActivity = CloudDownloadActivity.this;
                    titleView.setTitleText(cloudDownloadActivity.getString(R.string.have_choose_items_ipc, new Object[]{Integer.valueOf(cloudDownloadActivity.hasSelectedNum)}));
                    CloudDownloadActivity.this.checkAll = false;
                    CloudDownloadActivity.this.editView.setTvCheckAllDrawable(false);
                    if (CloudDownloadActivity.this.hasSelectedNum > 0) {
                        CloudDownloadActivity.this.editView.setTvDeleteDrawable(true);
                        return;
                    } else {
                        CloudDownloadActivity.this.editView.setTvDeleteDrawable(false);
                        return;
                    }
                }
                CloudDownloadActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudDownloadActivity.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CloudDownloadActivity.this.showTest(i);
                    }
                });
            }
        });
        this.deleteText = (TextView) findViewById(R.id.delete);
        this.deleteText.setOnClickListener(new View.OnClickListener() { // from class: activity.CloudDownloadActivity.4
            @Override // android.view.View.OnClickListener
            @SuppressLint({"ResourceAsColor"})
            public void onClick(View view2) {
                if (!CloudDownloadActivity.this.isEidt) {
                    CloudDownloadActivity.this.isEidt = true;
                    CloudDownloadActivity.this.deleteText.setText(R.string.cancel);
                    CloudDownloadActivity.this.deleteText.setTextColor(-16776961);
                    CloudDownloadActivity.this.hasSelectedNum = 0;
                    CloudDownloadActivity.this.checkAll = false;
                    CloudDownloadActivity.this.editView.setTvCheckAllDrawable(false);
                    CloudDownloadActivity.this.editView.setTvDeleteDrawable(false);
                    CloudDownloadActivity.this.editView.setVisibility(0);
                    CloudDownloadActivity.this.binding.title.setTitleText(CloudDownloadActivity.this.getString(R.string.have_choose_items_ipc, new Object[]{0}));
                    return;
                }
                CloudDownloadActivity.this.isEidt = false;
                CloudDownloadActivity.this.deleteText.setText(R.string.delete);
                CloudDownloadActivity.this.deleteText.setTextColor(SupportMenu.CATEGORY_MASK);
                CloudDownloadActivity.this.editView.setVisibility(8);
                CloudDownloadActivity.this.binding.title.setTitleText(CloudDownloadActivity.this.getResources().getString(R.string.download));
                Iterator<CloudVideo.RecordFileListBean> it = CloudDownloadActivity.this.cloudVideoBean.getRecordFileList().iterator();
                while (it.hasNext()) {
                    it.next().setIsSelected(false);
                }
                CloudDownloadActivity.this.f1566adapter.setDate(CloudDownloadActivity.this.cloudVideoBean);
                CloudDownloadActivity.this.f1566adapter.notifyDataSetChanged();
            }
        });
        this.editView.addOnClickListener(new AnonymousClass5());
    }

    /* JADX INFO: renamed from: activity.CloudDownloadActivity$5, reason: invalid class name */
    class AnonymousClass5 implements EditViewIpc.OnClickListener {
        @Override // view.EditViewIpc.OnClickListener
        public void OnInverseClick(View view2) {
        }

        AnonymousClass5() {
        }

        @Override // view.EditViewIpc.OnClickListener
        public void OnCheckAllClick(View view2) {
            CloudDownloadActivity.this.hasSelectedNum = 0;
            if (!CloudDownloadActivity.this.checkAll) {
                CloudDownloadActivity.this.checkAll = true;
                Iterator<CloudVideo.RecordFileListBean> it = CloudDownloadActivity.this.cloudVideoBean.getRecordFileList().iterator();
                while (it.hasNext()) {
                    it.next().setIsSelected(true);
                    CloudDownloadActivity.access$408(CloudDownloadActivity.this);
                }
                CloudDownloadActivity.this.editView.setTvCheckAllDrawable(true);
                CloudDownloadActivity.this.editView.setTvDeleteDrawable(true);
            } else {
                CloudDownloadActivity.this.checkAll = false;
                Iterator<CloudVideo.RecordFileListBean> it2 = CloudDownloadActivity.this.cloudVideoBean.getRecordFileList().iterator();
                while (it2.hasNext()) {
                    it2.next().setIsSelected(false);
                }
                CloudDownloadActivity.this.editView.setTvCheckAllDrawable(false);
                CloudDownloadActivity.this.editView.setTvDeleteDrawable(false);
            }
            CloudDownloadActivity.this.f1566adapter.notifyDataSetChanged();
            CloudDownloadActivity.this.binding.title.setTitleText(CloudDownloadActivity.this.getResources().getString(R.string.have_choose_items_ipc, Integer.valueOf(CloudDownloadActivity.this.hasSelectedNum)));
        }

        @Override // view.EditViewIpc.OnClickListener
        public void OnDeleteClick(View view2) {
            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(CloudDownloadActivity.this.getString(R.string.sure_cancel_ipc)).leftBtnText(CloudDownloadActivity.this.getString(R.string.sure_delete)).rightBtnText(CloudDownloadActivity.this.getString(R.string.cancel)).clickLeft(new View.OnClickListener() { // from class: activity.CloudDownloadActivity.5.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    CloudDownloadActivity.this.showProgressDialog(R.string.deleting_ipc);
                    CloudDownloadActivity.this.deleteRecord();
                    CloudDownloadActivity.this.cloudVideoBean.getRecordFileList().removeAll(CloudDownloadActivity.this.cacheListBean);
                    CloudDownloadActivity.this.f1566adapter.setDate(CloudDownloadActivity.this.cloudVideoBean);
                    CloudDownloadActivity.this.handler.post(new Runnable() { // from class: activity.CloudDownloadActivity.5.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CloudDownloadActivity.this.f1566adapter.notifyDataSetChanged();
                            CloudDownloadActivity.this.isEidt = false;
                            CloudDownloadActivity.this.editView.setVisibility(8);
                            CloudDownloadActivity.this.deleteText.setText(R.string.delete);
                            CloudDownloadActivity.this.deleteText.setTextColor(SupportMenu.CATEGORY_MASK);
                            CloudDownloadActivity.this.editView.setVisibility(8);
                            CloudDownloadActivity.this.binding.title.setTitleText(CloudDownloadActivity.this.getResources().getString(R.string.download));
                            CloudDownloadActivity.this.editView.setTvCheckAllDrawable(false);
                            CloudDownloadActivity.this.editView.setTvDeleteDrawable(false);
                            Toast.makeText(CloudDownloadActivity.this.getActivity(), R.string.delete_success_ipc, 1).show();
                            CloudDownloadActivity.this.dismissProgressDialog();
                        }
                    });
                }
            }).canCancel(false).create().show(CloudDownloadActivity.this.getSupportFragmentManager(), "");
        }
    }

    public void getCloudRecordList(int i, int i2) {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        map.put("beginTime", Integer.valueOf(i));
        map.put(AUserTrack.UTKEY_END_TIME, Integer.valueOf(i2));
        map.put(AlinkConstants.KEY_PAGE_SIZE, 500);
        map.put("needSnapshot", true);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/record/query").setApiVersion("2.1.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CloudDownloadActivity.6
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.e(CloudDownloadActivity.this.TAG, exc.getLocalizedMessage());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ALog.e(CloudDownloadActivity.this.TAG, ioTResponse.getData() + "");
                if (ioTResponse.getCode() == 200) {
                    CloudDownloadActivity.this.cloudVideoBean = (CloudVideo) new Gson().fromJson(ioTResponse.getData().toString(), CloudVideo.class);
                    CloudDownloadActivity.this.f1566adapter.setDate(CloudDownloadActivity.this.cloudVideoBean);
                    CloudDownloadActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudDownloadActivity.6.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CloudDownloadActivity.this.f1566adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pollingDownload(final String str, final String str2, final DialogView dialogView) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("fileName", str2);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/vod/cloudfile/get").setApiVersion("2.1.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CloudDownloadActivity.7
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                Log.d(CloudDownloadActivity.this.TAG, "onFailure: ");
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            @SuppressLint({"SetTextI18n"})
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getData() != null) {
                    CloudDownloadBean cloudDownloadBean = (CloudDownloadBean) new Gson().fromJson(ioTResponse.getData().toString(), CloudDownloadBean.class);
                    Log.d(CloudDownloadActivity.this.TAG, "onResponse: " + ioTResponse.getData().toString());
                    String str3 = cloudDownloadBean.getProgress() + "";
                    if (cloudDownloadBean.getProgress() != 100) {
                        try {
                            Thread.sleep(1000L);
                            Log.d(CloudDownloadActivity.this.TAG, "cloudfile/get-onResponse: 已完成转码" + str3);
                            CloudDownloadActivity.this.pollingDownload(str, str2, dialogView);
                            return;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    if (cloudDownloadBean.getProgress() == 100) {
                        CloudDownloadActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudDownloadActivity.7.1
                            @Override // java.lang.Runnable
                            public void run() {
                                dialogView.getTvDes().setText("已完成转码");
                            }
                        });
                        CloudDownloadActivity.this.downLoadVideo(cloudDownloadBean.getUrl(), dialogView);
                        return;
                    }
                    return;
                }
                CloudDownloadActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudDownloadActivity.7.2
                    @Override // java.lang.Runnable
                    public void run() {
                        dialogView.getStatus().setImageResource(R.drawable.error);
                        dialogView.getTvDes().setText("录像正在生成，请等生成完成后再下载");
                        dialogView.getTvCancel().setText(CloudDownloadActivity.this.getResources().getText(R.string.confirm));
                        dialogView.getLoading().setVisibility(8);
                        dialogView.getStatus().setVisibility(0);
                    }
                });
            }
        });
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTest(final int i) {
        new DialogView.Builder(this).setContent("是否开始下载录像？").setPositiveClickListener(getResources().getString(R.string.confirm), new DialogView.OnPositiveClickListener() { // from class: activity.CloudDownloadActivity.9
            @Override // view.DialogView.OnPositiveClickListener
            public void onPositiveClick(DialogView dialogView) {
                TextView tvConfirm = dialogView.getTvConfirm();
                TextView tvDes = dialogView.getTvDes();
                ProgressBar loading = dialogView.getLoading();
                int statusNum = dialogView.getStatusNum();
                if (statusNum != 0) {
                    if (statusNum == 1) {
                        dialogView.dismiss();
                        return;
                    }
                    return;
                }
                loading.setVisibility(0);
                dialogView.setStatusNum(1);
                dialogView.hideConfirmButton();
                tvConfirm.setText(CloudDownloadActivity.this.getString(R.string.finish));
                tvDes.setText("开始转码");
                tvDes.setGravity(17);
                CloudDownloadActivity cloudDownloadActivity = CloudDownloadActivity.this;
                cloudDownloadActivity.pollingDownload(cloudDownloadActivity.iotId, CloudDownloadActivity.this.cloudVideoBean.getRecordFileList().get(i).getFileName(), dialogView);
            }
        }).setNegativeClickListener(getResources().getString(R.string.cancel), new DialogView.OnNegativeClickListener() { // from class: activity.CloudDownloadActivity.8
            @Override // view.DialogView.OnNegativeClickListener
            public void onNegativeClick(DialogView dialogView) {
                dialogView.dismiss();
            }
        }).build().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downLoadVideo(String str, final DialogView dialogView) {
        DownloadUtil.get().download(str, getFilesPath(getApplication()), System.currentTimeMillis() + ".mp4", new DownloadUtil.OnDownloadListener() { // from class: activity.CloudDownloadActivity.10
            @Override // view.DownloadUtil.OnDownloadListener
            public void onDownloadSuccess() {
                CloudDownloadActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudDownloadActivity.10.1
                    @Override // java.lang.Runnable
                    @SuppressLint({"SetTextI18n"})
                    public void run() {
                        dialogView.getStatus().setImageResource(R.drawable.success);
                        dialogView.getTvDes().setText("下载完成，请前往APP相册查看");
                        dialogView.getTvCancel().setText(CloudDownloadActivity.this.getResources().getText(R.string.confirm));
                        dialogView.getLoading().setVisibility(8);
                        dialogView.getStatus().setVisibility(0);
                    }
                });
            }

            @Override // view.DownloadUtil.OnDownloadListener
            public void onDownloading(final int i) {
                CloudDownloadActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudDownloadActivity.10.2
                    @Override // java.lang.Runnable
                    @SuppressLint({"SetTextI18n"})
                    public void run() {
                        dialogView.getStatus().setImageResource(R.drawable.success);
                        dialogView.getTvDes().setText("已下载" + i + "%");
                    }
                });
            }

            @Override // view.DownloadUtil.OnDownloadListener
            public void onDownloadFailed() {
                CloudDownloadActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudDownloadActivity.10.3
                    @Override // java.lang.Runnable
                    public void run() {
                        dialogView.getStatus().setImageResource(R.drawable.audition_tips);
                        dialogView.getTvDes().setText("下载失败");
                        dialogView.getTvCancel().setText(CloudDownloadActivity.this.getResources().getText(R.string.confirm));
                        dialogView.getLoading().setVisibility(8);
                        dialogView.getStatus().setVisibility(0);
                    }
                });
            }
        });
    }

    public String getFilesPath(Context context) {
        String path;
        if ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            path = ((File) Objects.requireNonNull(context.getExternalFilesDir(null))).getPath();
        } else {
            path = context.getFilesDir().getPath();
        }
        return path + "//" + Utils.getUserPhone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getMonthList(String str, final int i, final int i2) {
        String str2 = new DecimalFormat("00").format(i2);
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put(SpUtil.MONTH, i + "" + str2);
        map.put("timeZone", 8);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/monthrecord/query").setApiVersion(BuildConfig.VERSION_NAME).setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CloudDownloadActivity.11
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                String string;
                ArrayList arrayList = new ArrayList();
                try {
                    string = ((JSONObject) ioTResponse.getData()).get("recordFlags").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    string = null;
                }
                for (int i3 = 0; i3 < string.length(); i3++) {
                    if (string.charAt(i3) == '1') {
                        arrayList.add(Integer.valueOf(i3 + 1));
                    }
                }
                CloudDownloadActivity.this.drawCircle(i, i2, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawCircle(int i, int i2, List<Integer> list) {
        HashMap map = new HashMap();
        for (int i3 = 0; i3 < list.size(); i3++) {
            map.put(getSchemeCalendar(i, i2, list.get(i3).intValue(), -12526811, "").toString(), getSchemeCalendar(i, i2, list.get(i3).intValue(), -12526811, ""));
        }
        this.binding.calendarView.setSchemeDate(map);
    }

    private CalendarModel getSchemeCalendar(int i, int i2, int i3, int i4, String str) {
        CalendarModel calendarModel = new CalendarModel();
        calendarModel.setYear(i);
        calendarModel.setMonth(i2);
        calendarModel.setDay(i3);
        return calendarModel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteRecord() {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        ArrayList arrayList = new ArrayList();
        this.cacheListBean = new ArrayList();
        for (CloudVideo.RecordFileListBean recordFileListBean : this.cloudVideoBean.getRecordFileList()) {
            if (recordFileListBean.getIsSelected()) {
                this.cacheListBean.add(recordFileListBean);
                arrayList.add(recordFileListBean.getFileName());
            }
        }
        map.put("fileNameList", arrayList);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTP).setPath("/vision/customer/record/batchdelete").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CloudDownloadActivity.12
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            }
        });
    }
}
