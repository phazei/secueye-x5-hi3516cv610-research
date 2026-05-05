package activity;

import adapter.VigilanceAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import bean.AlertInfoBean;
import bean.VigilanceBean;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.hjq.permissions.Permission;
import com.seculink.app.R;
import com.seculink.app.databinding.VigilanceActivityLayoutBinding;
import event.EventType;
import event.MyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import sdk.IPCManager;
import tools.G711Code;
import tools.SharePreferenceManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class VigilanceActivity extends CommonActivity {

    /* JADX INFO: renamed from: adapter, reason: collision with root package name */
    VigilanceAdapter f1587adapter;
    BottomSheetDialog alertDialog;
    AlertInfoBean alertInfoBean;
    String[] arr;
    String[] arr1;
    private AudioTrack audioTrack;
    VigilanceActivityLayoutBinding binding;
    BottomSheetDialog bottomSheetDialog;
    private FileInputStream fileInputStream;
    String iotId;
    List<VigilanceBean> list;
    TextView textView;
    CountDownTimer timer;
    SwitchCompat vigilanceSwitch;
    MutableLiveData<Boolean> vigilanceIsCheck = new MutableLiveData<>();
    int SAMPLE_RATE_INHZ = G711Code.SAMPLE_RATE_INHZ_16000;
    AnimatorSet set = new AnimatorSet();
    AnimatorSet set1 = new AnimatorSet();
    private AudioRecord audioRecord = null;
    private boolean isRecording = false;
    private boolean isPlaying = false;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.vigilance_activity_layout;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyEvent myEvent) {
        if (myEvent != null && myEvent.getTYPE() == EventType.REFRESH_ALERT) {
            Log.e("事件", "收到");
            runOnUiThread(new Runnable() { // from class: activity.VigilanceActivity.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        Thread.sleep(600L);
                        VigilanceActivity.this.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.binding = (VigilanceActivityLayoutBinding) DataBindingUtil.setContentView(this, R.layout.vigilance_activity_layout);
        setEdgeToEdge(this.binding.layoutMain);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.arr = getResources().getStringArray(R.array.VigilanceArray);
        this.arr1 = getResources().getStringArray(R.array.VigilanceArray1);
        this.list = new ArrayList();
        this.iotId = getIntent().getStringExtra("iotId");
        Log.d(this.TAG, "onCreate:--------------- " + SharePreferenceManager.getInstance().getVoice_Prompt_Type(this.iotId));
        this.f1587adapter = new VigilanceAdapter(this, this.list, this.iotId);
        this.binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.binding.recyclerView.setAdapter(this.f1587adapter);
        this.binding.titleView.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.VigilanceActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                VigilanceActivity.this.finish();
            }
        });
        get();
        this.binding.Vigilance.addRightView(new SwitchCompat(this));
        this.vigilanceSwitch = (SwitchCompat) this.binding.Vigilance.getRightView();
        this.vigilanceSwitch.setTextOff("");
        this.vigilanceSwitch.setTextOn("");
        this.vigilanceSwitch.setText("");
        this.vigilanceSwitch.setThumbDrawable(null);
        this.vigilanceSwitch.setBackgroundResource(R.drawable.sel_switch);
        this.vigilanceSwitch.setChecked(true);
        this.vigilanceIsCheck.postValue(true);
        this.vigilanceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: activity.VigilanceActivity.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                VigilanceActivity.this.vigilanceIsCheck.postValue(Boolean.valueOf(z));
            }
        });
        this.vigilanceIsCheck.observe(this, new Observer<Boolean>() { // from class: activity.VigilanceActivity.4
            @Override // androidx.lifecycle.Observer
            public void onChanged(Boolean bool) {
                if (bool.booleanValue()) {
                    VigilanceActivity.this.binding.recyclerView.setVisibility(0);
                } else {
                    VigilanceActivity.this.binding.recyclerView.setVisibility(8);
                }
            }
        });
        this.bottomSheetDialog = new BottomSheetDialog(this);
        this.bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
        this.bottomSheetDialog.findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() { // from class: activity.VigilanceActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                VigilanceActivity.this.bottomSheetDialog.dismiss();
            }
        });
        this.bottomSheetDialog.findViewById(R.id.image_two).setOnClickListener(new View.OnClickListener() { // from class: activity.VigilanceActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                VigilanceActivity.this.alertDialog.show();
                VigilanceActivity.this.bottomSheetDialog.dismiss();
            }
        });
        this.bottomSheetDialog.findViewById(R.id.image_one).setOnClickListener(new View.OnClickListener() { // from class: activity.VigilanceActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(VigilanceActivity.this.getApplicationContext(), (Class<?>) TTSVigilanceActivity.class);
                intent.putExtra("iotId", VigilanceActivity.this.iotId);
                VigilanceActivity.this.startActivity(intent);
                VigilanceActivity.this.bottomSheetDialog.dismiss();
            }
        });
        this.alertDialog = new BottomSheetDialog(this);
        this.alertDialog.setContentView(R.layout.bottom_sheet_dialog2);
        this.alertDialog.setCanceledOnTouchOutside(false);
        this.textView = (TextView) this.alertDialog.findViewById(R.id.generate_text);
        this.alertDialog.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() { // from class: activity.VigilanceActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                VigilanceActivity.this.alertDialog.dismiss();
            }
        });
        this.alertDialog.findViewById(R.id.image_one).setOnTouchListener(new View.OnTouchListener() { // from class: activity.VigilanceActivity.9
            /* JADX WARN: Type inference failed for: r9v2, types: [activity.VigilanceActivity$9$1] */
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    if (ActivityCompat.checkSelfPermission(VigilanceActivity.this, Permission.RECORD_AUDIO) == 0) {
                        VigilanceActivity.this.createRecord();
                        VigilanceActivity.this.timer = new CountDownTimer(10000L, 1000L) { // from class: activity.VigilanceActivity.9.1
                            @Override // android.os.CountDownTimer
                            @SuppressLint({"SetTextI18n"})
                            public void onTick(long j) {
                                String str = VigilanceActivity.this.TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("onTick: --------------------------");
                                long j2 = j / 1000;
                                sb.append(j2);
                                Log.d(str, sb.toString());
                                VigilanceActivity.this.textView.setText(VigilanceActivity.this.getString(R.string.vigilance_text2) + j2 + VigilanceActivity.this.getString(R.string.second));
                            }

                            @Override // android.os.CountDownTimer
                            public void onFinish() {
                                VigilanceActivity.this.closeRecord();
                                VigilanceActivity.this.textView.setText(R.string.click_to_listen);
                                VigilanceActivity.this.textView.setBackground(VigilanceActivity.this.getResources().getDrawable(R.drawable.edittext_style));
                            }
                        }.start();
                        VigilanceActivity.this.image_effects();
                        VigilanceActivity.this.image_effects1();
                    } else {
                        ActivityCompat.requestPermissions(VigilanceActivity.this, new String[]{Permission.RECORD_AUDIO}, 4372);
                        return false;
                    }
                } else if (motionEvent.getAction() == 1) {
                    VigilanceActivity.this.closeRecord();
                }
                return true;
            }
        });
        this.alertDialog.findViewById(R.id.generate_text).setOnClickListener(new View.OnClickListener() { // from class: activity.VigilanceActivity.10
            @Override // android.view.View.OnClickListener
            @RequiresApi(api = 21)
            public void onClick(View view2) {
                if (VigilanceActivity.this.audioTrack != null) {
                    VigilanceActivity.this.audioTrack.stop();
                }
                VigilanceActivity.this.play();
            }
        });
        this.alertDialog.findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() { // from class: activity.VigilanceActivity.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                VigilanceActivity.this.showTips();
                VigilanceActivity.this.alertDialog.dismiss();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTips() {
        TextView textView = new TextView(this);
        textView.setText(R.string.name_of_warning_sound);
        textView.setTextSize(20.0f);
        textView.setGravity(17);
        textView.setPadding(0, 50, 0, 0);
        textView.setTextColor(getResources().getColor(R.color.color_black));
        View viewInflate = View.inflate(this, R.layout.alertdialog_edittext_view, null);
        final EditText editText = (EditText) viewInflate.findViewById(R.id.editText);
        editText.setHint(R.string.vigilance_text);
        editText.setBackground(getResources().getDrawable(R.drawable.edittext_style));
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setCustomTitle(textView).setView(viewInflate).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: activity.VigilanceActivity.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                VigilanceActivity.this.uploadLocalFileCustomer(editText.getText().toString());
            }
        }).create();
        alertDialogCreate.setCanceledOnTouchOutside(false);
        alertDialogCreate.show();
        alertDialogCreate.getButton(-1).setTextColor(Color.parseColor("#2c99fd"));
        final Button button = alertDialogCreate.getButton(-1);
        button.setEnabled(false);
        button.setTextSize(18.0f);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.weight = 13.0f;
        button.setLayoutParams(layoutParams);
        editText.addTextChangedListener(new TextWatcher() { // from class: activity.VigilanceActivity.13
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (editable != null && !editable.toString().equals("")) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadLocalFileCustomer(final String str) {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        map.put("fileName", str);
        map.put("fileType", 1);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/vision/customer/app/file/upload").setScheme(Scheme.HTTPS).setApiVersion("1.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.VigilanceActivity.14
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                Log.d(VigilanceActivity.this.TAG, "onFailure: " + ioTRequest);
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                String string = JSON.parseObject(ioTResponse.getData().toString()).getString("uploadUrl");
                Log.d(VigilanceActivity.this.TAG, "onResponse: -------------------" + string);
                VigilanceActivity.this.upLoadFile(string, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void upLoadFile(String str, String str2) {
        new OkHttpClient.Builder().connectTimeout(100L, TimeUnit.SECONDS).writeTimeout(150L, TimeUnit.SECONDS).build().newCall(new Request.Builder().url(str).put(RequestBody.create(MediaType.get("application/octet-stream"), new File(G711Code.getFilesPath(this) + "/alert.g711"))).build()).enqueue(new Callback() { // from class: activity.VigilanceActivity.15
            @Override // okhttp3.Callback
            public void onFailure(@NonNull Call call, @NonNull IOException iOException) {
                Log.d(VigilanceActivity.this.TAG, "onFailure: ");
            }

            @Override // okhttp3.Callback
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                VigilanceActivity.this.runOnUiThread(new Runnable() { // from class: activity.VigilanceActivity.15.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            Thread.sleep(500L);
                            VigilanceActivity.this.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void get() {
        IPCManager.getInstance().getDevice(this.iotId).getQueryFileList(1, new IPanelCallback() { // from class: activity.VigilanceActivity.16
            static final /* synthetic */ boolean $assertionsDisabled = false;

            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (z) {
                    Log.d(VigilanceActivity.this.TAG, "onComplete: " + obj);
                    VigilanceActivity.this.list.clear();
                    VigilanceActivity.this.binding.recyclerView.setVisibility(8);
                    String[] stringArray = VigilanceActivity.this.getResources().getStringArray(R.array.VigilanceValue);
                    for (int i = 0; i < VigilanceActivity.this.arr.length; i++) {
                        VigilanceBean vigilanceBean = new VigilanceBean();
                        if (i == 0) {
                            vigilanceBean.setType("title");
                        }
                        vigilanceBean.setCheck(false);
                        vigilanceBean.setNum(i);
                        vigilanceBean.setContent(VigilanceActivity.this.arr[i]);
                        vigilanceBean.setName(stringArray[i]);
                        VigilanceActivity.this.list.add(vigilanceBean);
                    }
                    VigilanceActivity.this.alertInfoBean = (AlertInfoBean) new Gson().fromJson(obj.toString(), AlertInfoBean.class);
                    Log.d(VigilanceActivity.this.TAG, "onComplete: --------------------");
                    if (VigilanceActivity.this.alertInfoBean.getData() != null) {
                        for (int i2 = 0; i2 < VigilanceActivity.this.alertInfoBean.getData().getFileList().size(); i2++) {
                            VigilanceBean vigilanceBean2 = new VigilanceBean();
                            vigilanceBean2.setContent(VigilanceActivity.this.alertInfoBean.getData().getFileList().get(i2).getFileName());
                            vigilanceBean2.setName(VigilanceActivity.this.alertInfoBean.getData().getFileList().get(i2).getFileName());
                            VigilanceActivity.this.list.add(vigilanceBean2);
                        }
                    }
                    VigilanceBean vigilanceBean3 = new VigilanceBean();
                    if (VigilanceActivity.this.list.size() == 4) {
                        vigilanceBean3.setType("title_1");
                    } else if (VigilanceActivity.this.list.size() > 4) {
                        VigilanceActivity.this.list.get(4).setType("title_1");
                    }
                    vigilanceBean3.setContent(VigilanceActivity.this.getString(R.string.add_alert));
                    VigilanceActivity.this.list.add(vigilanceBean3);
                    for (int i3 = 0; i3 < VigilanceActivity.this.list.size(); i3++) {
                        if (VigilanceActivity.this.list.get(i3).getName().equals(SharePreferenceManager.getInstance().getVoice_Prompt_Type(VigilanceActivity.this.iotId))) {
                            VigilanceActivity.this.list.get(i3).setCheck(true);
                        }
                    }
                    VigilanceActivity.this.runOnUiThread(new Runnable() { // from class: activity.VigilanceActivity.16.1
                        @Override // java.lang.Runnable
                        public void run() {
                            VigilanceActivity.this.binding.recyclerView.setVisibility(0);
                            VigilanceActivity.this.f1587adapter.mSelectedPosInit();
                            VigilanceActivity.this.f1587adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void image_effects() {
        ImageView imageView = (ImageView) this.alertDialog.findViewById(R.id.image_effects);
        new ObjectAnimator();
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(imageView, "alpha", 1.0f, 0.3f);
        objectAnimatorOfFloat.setRepeatMode(1);
        objectAnimatorOfFloat.setRepeatCount(-1);
        new ObjectAnimator();
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 1.5f);
        objectAnimatorOfFloat2.setRepeatMode(1);
        objectAnimatorOfFloat2.setRepeatCount(-1);
        new ObjectAnimator();
        ObjectAnimator objectAnimatorOfFloat3 = ObjectAnimator.ofFloat(imageView, "scaleY", 1.0f, 1.5f);
        objectAnimatorOfFloat3.setRepeatMode(1);
        objectAnimatorOfFloat3.setRepeatCount(-1);
        this.set.playTogether(objectAnimatorOfFloat, objectAnimatorOfFloat2, objectAnimatorOfFloat3);
        this.set.setInterpolator(new DecelerateInterpolator());
        this.set.setDuration(AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
        this.set.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void image_effects1() {
        ImageView imageView = (ImageView) this.alertDialog.findViewById(R.id.image_effects1);
        new ObjectAnimator();
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(imageView, "alpha", 1.0f, 0.3f);
        objectAnimatorOfFloat.setRepeatMode(1);
        objectAnimatorOfFloat.setRepeatCount(-1);
        new ObjectAnimator();
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(imageView, "scaleX", 1.0f, 1.5f);
        objectAnimatorOfFloat2.setRepeatMode(1);
        objectAnimatorOfFloat2.setRepeatCount(-1);
        new ObjectAnimator();
        ObjectAnimator objectAnimatorOfFloat3 = ObjectAnimator.ofFloat(imageView, "scaleY", 1.0f, 1.5f);
        objectAnimatorOfFloat3.setRepeatMode(1);
        objectAnimatorOfFloat3.setRepeatCount(-1);
        this.set1.playTogether(objectAnimatorOfFloat, objectAnimatorOfFloat2, objectAnimatorOfFloat3);
        this.set1.setInterpolator(new DecelerateInterpolator());
        this.set1.setDuration(AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
        this.set1.setStartDelay(800L);
        this.set1.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createRecord() {
        final int minBufferSize = AudioRecord.getMinBufferSize(this.SAMPLE_RATE_INHZ, 16, 2);
        this.audioRecord = new AudioRecord(1, this.SAMPLE_RATE_INHZ, 16, 2, minBufferSize);
        final File file = new File(G711Code.getFilesPath(this), "alert.g711");
        if (!file.mkdirs()) {
            Log.e("demo failed---->", "Directory not created");
        }
        if (file.exists()) {
            file.delete();
        }
        this.audioRecord.startRecording();
        this.isRecording = true;
        new Thread(new Runnable() { // from class: activity.VigilanceActivity.17
            @Override // java.lang.Runnable
            public void run() {
                FileOutputStream fileOutputStream;
                int i = minBufferSize;
                short[] sArr = new short[i];
                byte[] bArr = new byte[i];
                try {
                    fileOutputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    fileOutputStream = null;
                }
                if (fileOutputStream != null) {
                    while (VigilanceActivity.this.isRecording) {
                        int i2 = VigilanceActivity.this.audioRecord.read(sArr, 0, sArr.length);
                        if (-3 != i2) {
                            G711Code.G711aEncoder(sArr, bArr, i2);
                            try {
                                fileOutputStream.write(bArr);
                            } catch (IOException e2) {
                                e2.printStackTrace();
                                Log.e("写g711异常", e2.toString() + "");
                            }
                        }
                    }
                    try {
                        Log.e("run------>", "close file output stream !");
                        fileOutputStream.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeRecord() {
        this.isRecording = false;
        AudioRecord audioRecord = this.audioRecord;
        if (audioRecord != null) {
            audioRecord.stop();
            this.audioRecord.release();
            this.audioRecord = null;
        }
        CountDownTimer countDownTimer = this.timer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.timer = null;
        }
        this.textView.setText(getResources().getText(R.string.click_to_listen));
        this.textView.setBackground(getResources().getDrawable(R.drawable.audition_tips));
        this.set.end();
        this.set1.end();
        this.alertDialog.findViewById(R.id.image_effects).setScaleX(1.0f);
        this.alertDialog.findViewById(R.id.image_effects1).setScaleX(1.0f);
        this.alertDialog.findViewById(R.id.image_effects).setScaleY(1.0f);
        this.alertDialog.findViewById(R.id.image_effects1).setScaleY(1.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @RequiresApi(api = 21)
    public void play() {
        final int minBufferSize = AudioTrack.getMinBufferSize(this.SAMPLE_RATE_INHZ, 4, 2);
        this.audioTrack = new AudioTrack(new AudioAttributes.Builder().setUsage(1).setContentType(2).build(), new AudioFormat.Builder().setSampleRate(this.SAMPLE_RATE_INHZ).setEncoding(2).setChannelMask(4).build(), minBufferSize, 1, 0);
        this.audioTrack.play();
        this.isPlaying = true;
        try {
            this.fileInputStream = new FileInputStream(new File(G711Code.getFilesPath(getApplication()) + "/alert.g711"));
            new Thread(new Runnable() { // from class: activity.VigilanceActivity.18
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        byte[] bArr = new byte[minBufferSize];
                        short[] sArr = new short[minBufferSize];
                        while (VigilanceActivity.this.fileInputStream.available() > 0) {
                            int i = VigilanceActivity.this.fileInputStream.read(bArr);
                            if (i != -3 && i != -2 && i != 0 && i != -1) {
                                G711Code.G711aDecoder(sArr, bArr, i);
                                VigilanceActivity.this.audioTrack.write(sArr, 0, i);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override // activity.CommonActivity
    protected void initData() {
        this.iotId = getIntent().getStringExtra("iotId");
        SharePreferenceManager.getInstance().getVoice_Prompt_Type(this.iotId);
    }

    public void addT() {
        this.bottomSheetDialog.show();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        closeRecord();
        AudioTrack audioTrack = this.audioTrack;
        if (audioTrack != null) {
            audioTrack.stop();
        }
    }
}
