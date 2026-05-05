package com.aliyun.iot.aep.sdk.log.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.log.R;
import com.aliyun.iot.aep.sdk.log.util.ALogUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class ALogActivity extends Activity {
    private static final int FILE_SELECT_CODE = 0;
    static String TAG = "ALogActivity";
    static String sALogSearchUrl = "http://iot-alog.aliyun.test:8080/index.html";
    private String mALogInfo;
    private EditText mBugDescription;
    private EditText mBugTitle;
    private StringBuilder mDisplayUploadFilesStringBuilder;
    HashMap<String, FileUploadStatusView> mFileUploadStatusViewMap;
    private TextView mOssUrlTv;
    private String mRawLogPresignPublicObjectURL;
    private Button mReportBugBtn;
    private Button mUploadChosenFilesBtn;
    private LinearLayout mUploadFilesLL;
    private TextView mUploadFilesTv;
    private OSSClient oss;
    private Handler mHandler = new Handler();
    private String sBucketName = "iot-alog";
    private ArrayList<String> mAttachmentPresignPublicObjectURLs = new ArrayList<>();
    private ArrayList<Uri> mUploadUris = new ArrayList<>();
    private String sReportBugUrl = "http://iot-alog.aliyun.test:8080/bug";
    private String ossALogTestPath = "alog/test/";
    private int mUploadFilesCounter = 0;

    static /* synthetic */ int access$2010(ALogActivity aLogActivity) {
        int i = aLogActivity.mUploadFilesCounter;
        aLogActivity.mUploadFilesCounter = i - 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkField() {
        if (this.mBugTitle.getText().length() != 0 && this.mBugDescription.getText().length() != 0 && this.mRawLogPresignPublicObjectURL != null) {
            return true;
        }
        Toast.makeText(this, R.string.toast_report_bug_item, 0).show();
        return false;
    }

    private void fillUploadFilesPanel(ArrayList<Uri> arrayList) {
        this.mFileUploadStatusViewMap = new HashMap<>();
        this.mUploadFilesLL.removeAllViews();
        Iterator<Uri> it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                File file = new File(ALogUtils.getPath(this, it.next()));
                FileUploadStatusView fileUploadStatusView = new FileUploadStatusView(getApplicationContext());
                fileUploadStatusView.attachFile(file);
                this.mFileUploadStatusViewMap.put(file.getName(), fileUploadStatusView);
                this.mUploadFilesLL.addView(fileUploadStatusView.getContentView());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getCurLogcat(String str) {
        ALog.d("JC", "getCurLogcat   " + str);
        StringBuilder sb = new StringBuilder();
        sb.append("-------- ALog 信息 --------" + System.lineSeparator());
        sb.append(this.mALogInfo);
        sb.append(System.lineSeparator());
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("logcat -v time -t 1000 *:" + str).getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getUploadFilesDisplayStr(ArrayList<Uri> arrayList) {
        StringBuilder sb = new StringBuilder();
        Iterator<Uri> it = arrayList.iterator();
        while (it.hasNext()) {
            try {
                sb.append(new File(ALogUtils.getPath(this, it.next())).getName() + System.lineSeparator());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private void initSDKs() {
        OSSFederationCredentialProvider oSSFederationCredentialProvider = new OSSFederationCredentialProvider() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.1
            @Override // com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider, com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
            public OSSFederationToken getFederationToken() {
                try {
                    JSONObject jSONObject = new JSONObject(IOUtils.readStreamAsString(((HttpURLConnection) new URL("http://iot-alog.aliyun.test:3000/").openConnection()).getInputStream(), "utf-8"));
                    return new OSSFederationToken(jSONObject.getString("AccessKeyId"), jSONObject.getString("AccessKeySecret"), jSONObject.getString("SecurityToken"), jSONObject.getString("Expiration"));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setConnectionTimeout(15000);
        clientConfiguration.setSocketTimeout(15000);
        clientConfiguration.setMaxConcurrentRequest(5);
        clientConfiguration.setMaxErrorRetry(2);
        this.oss = new OSSClient(getApplicationContext(), OSSConstants.DEFAULT_OSS_ENDPOINT, oSSFederationCredentialProvider, clientConfiguration);
    }

    private void initViews() {
        this.mDisplayUploadFilesStringBuilder = new StringBuilder();
        this.mDisplayUploadFilesStringBuilder.append("uploadFiles:" + System.lineSeparator());
        this.mALogInfo = "UserKey : " + ALog.getUserKey() + System.lineSeparator() + "deviceId : " + ALog.getSource() + System.lineSeparator() + "Level : " + ALog.getLevelStr() + System.lineSeparator();
        this.mUploadFilesTv = (TextView) findViewById(R.id.chosenFileListTv);
        ((TextView) findViewById(R.id.alog_sysinfo)).setText(this.mALogInfo);
        this.mBugTitle = (EditText) findViewById(R.id.bugTitle);
        this.mBugDescription = (EditText) findViewById(R.id.bugDescription);
        this.mUploadFilesLL = (LinearLayout) findViewById(R.id.uploadFilesLL);
        ((Button) findViewById(R.id.dumpRawLogBtn)).setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ALogActivity.this.upLoadLogStr(ALog.getUserKey() + System.currentTimeMillis() + ".txt", ALogActivity.this.getCurLogcat("V"));
            }
        });
        this.mReportBugBtn = (Button) findViewById(R.id.reportBugBtn);
        this.mReportBugBtn.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (ALogActivity.this.checkField()) {
                    if (ALogActivity.this.mAttachmentPresignPublicObjectURLs.size() == 0) {
                        ALogActivity aLogActivity = ALogActivity.this;
                        aLogActivity.reportBug(aLogActivity.mBugTitle.getText().toString(), ALogActivity.this.mBugDescription.getText().toString(), ALogActivity.this.mRawLogPresignPublicObjectURL, "alogurl");
                        return;
                    }
                    JSONArray jSONArray = new JSONArray();
                    for (String str : ALogActivity.this.mAttachmentPresignPublicObjectURLs) {
                        JSONObject jSONObject = new JSONObject();
                        try {
                            jSONObject.put("attachmentFile", str);
                            jSONArray.put(jSONObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    JSONObject jSONObject2 = new JSONObject();
                    try {
                        jSONObject2.put("attachmentFiles", jSONArray);
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                    ALog.d("JC", jSONObject2.toString());
                    ALogActivity aLogActivity2 = ALogActivity.this;
                    aLogActivity2.reportBugWithFiles(aLogActivity2.mBugTitle.getText().toString(), ALogActivity.this.mBugDescription.getText().toString(), ALogActivity.this.mRawLogPresignPublicObjectURL, "alogurl", jSONObject2.toString());
                }
            }
        });
        ((Button) findViewById(R.id.chooseFiles)).setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ALogActivity.this.showFileChooser();
            }
        });
        this.mUploadChosenFilesBtn = (Button) findViewById(R.id.uploadChosenFiles);
        this.mUploadChosenFilesBtn.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (ALogActivity.this.mFileUploadStatusViewMap == null || ALogActivity.this.mFileUploadStatusViewMap.size() == 0) {
                    Toast.makeText(ALogActivity.this.getApplicationContext(), "请先选择要上传的文件", 0).show();
                    return;
                }
                ALogActivity.this.mDisplayUploadFilesStringBuilder = new StringBuilder();
                ALogActivity.this.mDisplayUploadFilesStringBuilder.append("已上传文件：" + System.lineSeparator());
                ALogActivity aLogActivity = ALogActivity.this;
                aLogActivity.uploadChosenFiles(aLogActivity.mUploadUris);
                ALogActivity.this.mUploadChosenFilesBtn.setEnabled(false);
            }
        });
        ((Button) findViewById(R.id.goSearchBtn)).setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(ALogActivity.this, (Class<?>) ALogWebActivity.class);
                intent.putExtra("weburl", ALogActivity.sALogSearchUrl);
                ALogActivity.this.startActivity(intent);
            }
        });
        this.mOssUrlTv = (TextView) findViewById(R.id.ossUrl);
        this.mOssUrlTv.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(ALogActivity.this, (Class<?>) ALogWebActivity.class);
                intent.putExtra("weburl", ALogActivity.this.mRawLogPresignPublicObjectURL);
                ALogActivity.this.startActivity(intent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportBug(String str, String str2, String str3, String str4) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("attachmentFile", "none");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(jSONObject);
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("attachmentFiles", jSONArray);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        reportBugWithFiles(str, str2, str3, str4, jSONObject2.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportBugWithFiles(String str, String str2, String str3, String str4, String str5) {
        try {
            this.mReportBugBtn.setEnabled(false);
            ALogUtils.getHttpClient().newCall(new Request.Builder().url(this.sReportBugUrl).post(ALog.getCommoFormBodyBuilder().add("bugTitle", str).add("bugDes", str2).add("rawLogUrl", str3).add("alogUrl", str4).add("attachmentFiles", str5).build()).build()).enqueue(new Callback() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.8
                @Override // okhttp3.Callback
                public void onFailure(Call call, IOException iOException) {
                    Toast.makeText(ALogActivity.this, "report bug failed, please try again", 0).show();
                    ALogActivity.this.mHandler.post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.8.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ALogActivity.this.mReportBugBtn.setEnabled(true);
                        }
                    });
                }

                @Override // okhttp3.Callback
                public void onResponse(Call call, Response response) {
                    try {
                        final String string = new JSONObject(response.body().string()).getString("bugId");
                        ALogActivity.this.mHandler.post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.8.2
                            @Override // java.lang.Runnable
                            public void run() {
                                ALogActivity.this.mReportBugBtn.setEnabled(true);
                                Intent intent = new Intent(ALogActivity.this, (Class<?>) ReportBugSuccessfulActivity.class);
                                intent.putExtra("bugId", string);
                                ALogActivity.this.startActivity(intent);
                                ALogActivity.this.finish();
                            }
                        });
                    } catch (JSONException unused) {
                        Toast.makeText(ALogActivity.this, "report bug failed, please try again", 0).show();
                        ALogActivity.this.mHandler.post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.8.3
                            @Override // java.lang.Runnable
                            public void run() {
                                ALogActivity.this.mReportBugBtn.setEnabled(true);
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "report bug failed, please try again", 0).show();
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showFileChooser() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/;video/");
        intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        intent.putExtra("android.intent.extra.LOCAL_ONLY", true);
        intent.addCategory("android.intent.category.OPENABLE");
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 0);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this, "Please install a File Manager.", 0).show();
        }
    }

    private void upLoadFile(Uri uri) {
        String path;
        try {
            path = ALogUtils.getPath(this, uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            path = null;
        }
        ALog.d(TAG, "File Path: " + path);
        ALog.d(TAG, "Start upload file " + path + " to oss!");
        upLoadFileToOSS(new File(path));
    }

    private void upLoadFileToOSS(final File file) {
        final String str = this.ossALogTestPath + "screenshot/" + file.getName();
        PutObjectRequest putObjectRequest = new PutObjectRequest("iot-alog", str, file.getAbsolutePath());
        putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.11
            @Override // com.alibaba.sdk.android.oss.callback.OSSProgressCallback
            public void onProgress(PutObjectRequest putObjectRequest2, final long j, final long j2) {
                ALogActivity.this.mHandler.post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.11.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ALogActivity.this.updateStatus(file, (int) ((j / j2) * 100.0d));
                    }
                });
            }
        });
        this.oss.asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.12
            @Override // com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
            public void onFailure(PutObjectRequest putObjectRequest2, ClientException clientException, ServiceException serviceException) {
                if (clientException != null) {
                    clientException.printStackTrace();
                }
                if (serviceException != null) {
                    ALog.e("ErrorCode", serviceException.getErrorCode());
                    ALog.e("RequestId", serviceException.getRequestId());
                    ALog.e("HostId", serviceException.getHostId());
                    ALog.e("RawMessage", serviceException.getRawMessage());
                }
            }

            @Override // com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
            public void onSuccess(PutObjectRequest putObjectRequest2, PutObjectResult putObjectResult) {
                ALog.d("PutObject", "UploadSuccess");
                ALog.d("ETag", putObjectResult.getETag());
                ALog.d("RequestId", putObjectResult.getRequestId());
                String strPresignPublicObjectURL = ALogActivity.this.oss.presignPublicObjectURL(ALogActivity.this.sBucketName, str);
                ALogActivity.this.mAttachmentPresignPublicObjectURLs.add(strPresignPublicObjectURL);
                ALogActivity.this.mDisplayUploadFilesStringBuilder.append(strPresignPublicObjectURL + System.lineSeparator());
                ALogActivity.this.mHandler.post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.12.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ALogActivity.access$2010(ALogActivity.this);
                        if (ALogActivity.this.mUploadFilesCounter != 0 || ALogActivity.this.mRawLogPresignPublicObjectURL == null) {
                            return;
                        }
                        ALogActivity.this.mUploadChosenFilesBtn.setEnabled(true);
                        ALogActivity.this.mReportBugBtn.setEnabled(true);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void upLoadLogStr(String str, String str2) {
        PutObjectRequest putObjectRequest;
        final String str3 = this.ossALogTestPath + str;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("text/plain;charset=utf-8");
        objectMetadata.setContentEncoding("utf-8");
        try {
            putObjectRequest = new PutObjectRequest(this.sBucketName, str3, str2.getBytes("UTF-8"), objectMetadata);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            putObjectRequest = null;
        }
        putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.9
            @Override // com.alibaba.sdk.android.oss.callback.OSSProgressCallback
            public void onProgress(PutObjectRequest putObjectRequest2, long j, long j2) {
            }
        });
        this.mReportBugBtn.setEnabled(false);
        this.oss.asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.10
            @Override // com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
            public void onFailure(PutObjectRequest putObjectRequest2, ClientException clientException, ServiceException serviceException) {
                if (clientException != null) {
                    clientException.printStackTrace();
                }
                if (serviceException != null) {
                    ALog.e("ErrorCode", serviceException.getErrorCode());
                    ALog.e("RequestId", serviceException.getRequestId());
                    ALog.e("HostId", serviceException.getHostId());
                    ALog.e("RawMessage", serviceException.getRawMessage());
                }
                ALogActivity.this.mReportBugBtn.setEnabled(false);
            }

            @Override // com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
            public void onSuccess(PutObjectRequest putObjectRequest2, PutObjectResult putObjectResult) {
                ALog.d("PutObject", "UploadSuccess");
                ALog.d("ETag", putObjectResult.getETag());
                ALog.d("RequestId", putObjectResult.getRequestId());
                ALogActivity.this.mHandler.post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.log.ui.ALogActivity.10.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ALogActivity.this.mRawLogPresignPublicObjectURL = ALogActivity.this.oss.presignPublicObjectURL(ALogActivity.this.sBucketName, str3);
                        ALogActivity.this.mOssUrlTv.setText(Html.fromHtml("<u>" + ALogActivity.this.mRawLogPresignPublicObjectURL.substring(0, 50) + "......</u>"));
                        ALogActivity.this.mReportBugBtn.setEnabled(true);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStatus(File file, int i) {
        Log.d("JC", "count = " + i);
        this.mFileUploadStatusViewMap.get(file.getName()).setProgress(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadChosenFiles(ArrayList<Uri> arrayList) {
        this.mAttachmentPresignPublicObjectURLs = new ArrayList<>();
        this.mUploadFilesCounter = arrayList.size();
        if (this.mUploadFilesCounter != 0) {
            this.mReportBugBtn.setEnabled(false);
        }
        Iterator<Uri> it = arrayList.iterator();
        while (it.hasNext()) {
            upLoadFile(it.next());
        }
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        ClipData clipData;
        super.onActivityResult(i, i2, intent);
        if (i == 0 && i2 == -1) {
            this.mUploadUris = new ArrayList<>();
            if (Build.VERSION.SDK_INT < 16 || (clipData = intent.getClipData()) == null) {
                this.mUploadUris.add(intent.getData());
            } else {
                for (int i3 = 0; i3 < clipData.getItemCount(); i3++) {
                    this.mUploadUris.add(clipData.getItemAt(i3).getUri());
                }
            }
            String str = "uploadFiles:" + System.lineSeparator() + getUploadFilesDisplayStr(this.mUploadUris);
            fillUploadFilesPanel(this.mUploadUris);
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ALog.d("ALogActivity", "ALogActivity onCreate");
        setContentView(R.layout.activity_alog);
        initSDKs();
        initViews();
    }
}
