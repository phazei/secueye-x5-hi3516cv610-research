package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.hjq.permissions.Permission;
import com.seculink.app.R;
import com.seculink.app.databinding.TtsVigilanceActivityLayoutBinding;
import event.EventType;
import event.MyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.greenrobot.eventbus.EventBus;
import tools.G711A;
import tools.G711Code;
import view.SelectorDialogFragment;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class TTSVigilanceActivity extends CommonActivity implements TextToSpeech.OnInitListener {
    private static String[] PERMISSIONS_STORAGE = {Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    TtsVigilanceActivityLayoutBinding binding;
    SelectorDialogFragment soundTypeDialogFragment;
    String[] soundTypeText;
    TextToSpeech textToSpeech;
    File sdCardDir = null;
    byte[] old = null;
    boolean flag = false;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.tts_vigilance_activity_layout;
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.binding = TtsVigilanceActivityLayoutBinding.inflate(getLayoutInflater());
        setEdgeToEdge(this.binding.layoutMain);
        setContentView(this.binding.getRoot());
        this.binding.title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.TTSVigilanceActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                TTSVigilanceActivity.this.finish();
            }

            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
                TTSVigilanceActivity.this.showTips();
            }
        });
        this.soundTypeText = getResources().getStringArray(R.array.soundType);
        this.soundTypeDialogFragment = new SelectorDialogFragment("声音类型", this.soundTypeText);
        this.soundTypeDialogFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.TTSVigilanceActivity.2
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                TTSVigilanceActivity.this.binding.soundType.setRightText(TTSVigilanceActivity.this.soundTypeText[i]);
                if (i == 0) {
                    TTSVigilanceActivity.this.textToSpeech.setPitch(0.1f);
                } else {
                    TTSVigilanceActivity.this.textToSpeech.setPitch(1.0f);
                }
            }
        });
        this.binding.soundType.setRightText(this.soundTypeText[1]);
        this.binding.soundType.setOnClickListener(new View.OnClickListener() { // from class: activity.TTSVigilanceActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TTSVigilanceActivity.this.soundTypeDialogFragment.showAllowingStateLoss(TTSVigilanceActivity.this.getSupportFragmentManager(), "");
            }
        });
        verifyStoragePermissions(this);
        this.textToSpeech = new TextToSpeech(this, this);
        this.binding.soundSpeed.setProgress(10);
        this.binding.soundSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: activity.TTSVigilanceActivity.4
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TTSVigilanceActivity.this.TAG, "onStopTrackingTouch: ------" + (seekBar.getProgress() / 10.0f));
                TTSVigilanceActivity.this.textToSpeech.setSpeechRate(((float) seekBar.getProgress()) / 10.0f);
            }
        });
        this.binding.btNext.setOnClickListener(new View.OnClickListener() { // from class: activity.TTSVigilanceActivity.5
            @Override // android.view.View.OnClickListener
            @RequiresApi(api = 21)
            public void onClick(View view2) {
                TTSVigilanceActivity.this.textToSpeech.speak(TTSVigilanceActivity.this.binding.editText.getText(), 1, null, "");
                TTSVigilanceActivity.this.binding.title.setRightIconText(TTSVigilanceActivity.this.getString(R.string.finish));
            }
        });
        this.textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() { // from class: activity.TTSVigilanceActivity.6
            @Override // android.speech.tts.UtteranceProgressListener
            public void onStart(String str) {
                Log.d(TTSVigilanceActivity.this.TAG, "onStart: " + str);
                TTSVigilanceActivity.this.sdCardDir = new File(G711Code.getFilesPath(TTSVigilanceActivity.this.getApplicationContext()) + "/tts.g711");
            }

            @Override // android.speech.tts.UtteranceProgressListener
            public void onDone(String str) {
                try {
                    byte[] bArr = new byte[TTSVigilanceActivity.this.old.length / 2];
                    G711A.encode(TTSVigilanceActivity.this.old, 0, TTSVigilanceActivity.this.old.length, bArr);
                    TTSVigilanceActivity.this.writeSDFile(TTSVigilanceActivity.this.sdCardDir + "", bArr);
                    TTSVigilanceActivity.this.old = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override // android.speech.tts.UtteranceProgressListener
            public void onError(String str) {
                Log.e(TTSVigilanceActivity.this.TAG, "" + str);
                TTSVigilanceActivity.this.runOnUiThread(new Runnable() { // from class: activity.TTSVigilanceActivity.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(TTSVigilanceActivity.this, "2131755801", 0).show();
                    }
                });
            }

            @Override // android.speech.tts.UtteranceProgressListener
            public void onAudioAvailable(String str, byte[] bArr) {
                byte[] bArr2 = new byte[bArr.length];
                if (TTSVigilanceActivity.this.old != null) {
                    TTSVigilanceActivity tTSVigilanceActivity = TTSVigilanceActivity.this;
                    tTSVigilanceActivity.old = TTSVigilanceActivity.addBytes(tTSVigilanceActivity.old, bArr);
                } else {
                    TTSVigilanceActivity.this.old = bArr;
                }
            }

            @Override // android.speech.tts.UtteranceProgressListener
            public void onBeginSynthesis(String str, int i, int i2, int i3) {
                super.onBeginSynthesis(str, 8000, i2, i3);
                Log.d(TTSVigilanceActivity.this.TAG, "onBeginSynthesis: sampleRateInHz " + i);
            }
        });
    }

    public void writeSDFile(String str, byte[] bArr) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
        fileOutputStream.write(bArr);
        fileOutputStream.close();
    }

    @Override // android.speech.tts.TextToSpeech.OnInitListener
    public void onInit(int i) {
        if (i == 0) {
            int language = this.textToSpeech.setLanguage(Locale.CHINA);
            if (language == -1 || language == -2) {
                Toast.makeText(this, "数据丢失或不支持", 0).show();
            }
        }
    }

    public void stopTTS() {
        TextToSpeech textToSpeech = this.textToSpeech;
        if (textToSpeech != null) {
            textToSpeech.shutdown();
            this.textToSpeech.stop();
            this.textToSpeech = null;
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        stopTTS();
    }

    public static void verifyStoragePermissions(Activity activity2) {
        try {
            if (ActivityCompat.checkSelfPermission(activity2, Permission.WRITE_EXTERNAL_STORAGE) != 0) {
                ActivityCompat.requestPermissions(activity2, PERMISSIONS_STORAGE, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] addBytes(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void uploadLocalFileCustomer(final String str) {
        HashMap map = new HashMap();
        map.put("iotId", getIntent().getStringExtra("iotId"));
        map.put("fileName", str);
        map.put("fileType", 1);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setPath("/vision/customer/app/file/upload").setScheme(Scheme.HTTPS).setApiVersion("1.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.TTSVigilanceActivity.7
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                Log.d(TTSVigilanceActivity.this.TAG, "puppet:onFailure: " + ioTRequest);
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                TTSVigilanceActivity.this.upLoadFile(JSON.parseObject(ioTResponse.getData().toString()).getString("uploadUrl"), str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void upLoadFile(String str, String str2) {
        new OkHttpClient.Builder().connectTimeout(100L, TimeUnit.SECONDS).writeTimeout(150L, TimeUnit.SECONDS).build().newCall(new Request.Builder().url(str).put(RequestBody.create(MediaType.get("application/octet-stream"), new File(G711Code.getFilesPath(this) + "/tts.g711"))).build()).enqueue(new Callback() { // from class: activity.TTSVigilanceActivity.8
            @Override // okhttp3.Callback
            public void onFailure(@NonNull Call call, @NonNull IOException iOException) {
                TTSVigilanceActivity.this.runOnUiThread(new Runnable() { // from class: activity.TTSVigilanceActivity.8.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(TTSVigilanceActivity.this, "2131755801", 0).show();
                    }
                });
            }

            @Override // okhttp3.Callback
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                EventBus.getDefault().post(new MyEvent(EventType.REFRESH_ALERT, null));
                Log.e("事件", "发出");
                TTSVigilanceActivity.this.finish();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTips() {
        TextView textView = new TextView(this);
        textView.setText(getResources().getString(R.string.name_of_warning_sound));
        textView.setTextSize(20.0f);
        textView.setGravity(17);
        textView.setPadding(0, 50, 0, 0);
        textView.setTextColor(getResources().getColor(R.color.color_black));
        View viewInflate = View.inflate(this, R.layout.alertdialog_edittext_view, null);
        final EditText editText = (EditText) viewInflate.findViewById(R.id.editText);
        editText.setHint(getResources().getText(R.string.vigilance_text));
        editText.setBackground(getResources().getDrawable(R.drawable.edittext_style));
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setCustomTitle(textView).setView(viewInflate).setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() { // from class: activity.TTSVigilanceActivity.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                TTSVigilanceActivity.this.uploadLocalFileCustomer(editText.getText().toString());
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
        editText.addTextChangedListener(new TextWatcher() { // from class: activity.TTSVigilanceActivity.10
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
}
