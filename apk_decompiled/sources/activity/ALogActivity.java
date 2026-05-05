package activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.aliyun.iot.aep.sdk.framework.AActivity;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.seculink.app.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes.dex */
public class ALogActivity extends AActivity {
    EditText mALogKeyWordsET;
    ScrollView mALogShowSv;
    private RadioGroup radioGroup;
    String mKeyWords = "";
    final Handler mH = new Handler();
    boolean mLock = false;

    @Override // com.aliyun.iot.aep.sdk.framework.AActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.alog_demo_activity);
        final TextView textView = (TextView) findViewById(R.id.alog_show_tv);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.mALogKeyWordsET = (EditText) findViewById(R.id.alog_keywords_et);
        this.mALogShowSv = (ScrollView) findViewById(R.id.alog_show_sv);
        ((TextView) findViewById(R.id.topbar_title_textview)).setText(R.string.alog_title);
        ((ImageView) findViewById(R.id.topbar_back_imageview)).setOnClickListener(new View.OnClickListener() { // from class: activity.ALogActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ALogActivity.this.finish();
            }
        });
        ((Button) findViewById(R.id.alog_clear_btn)).setOnClickListener(new View.OnClickListener() { // from class: activity.ALogActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                textView.setText("");
            }
        });
        ((Button) findViewById(R.id.alog_copy_btn)).setOnClickListener(new View.OnClickListener() { // from class: activity.ALogActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ALogActivity aLogActivity = ALogActivity.this;
                ALogActivity.this.copyLog(((RadioButton) aLogActivity.findViewById(aLogActivity.radioGroup.getCheckedRadioButtonId())).getText().toString());
            }
        });
        Button button = (Button) findViewById(R.id.alog_v_btn);
        button.setOnClickListener(new View.OnClickListener() { // from class: activity.ALogActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                textView.setText("");
                if (ALogActivity.this.mLock) {
                    return;
                }
                ALogActivity aLogActivity = ALogActivity.this;
                aLogActivity.mLock = true;
                textView.setText(Html.fromHtml(aLogActivity.getCurLogcat("V")));
                ALogActivity.this.refreshShowLog();
            }
        });
        ((Button) findViewById(R.id.alog_d_btn)).setOnClickListener(new View.OnClickListener() { // from class: activity.ALogActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                textView.setText("");
                if (ALogActivity.this.mLock) {
                    return;
                }
                ALogActivity aLogActivity = ALogActivity.this;
                aLogActivity.mLock = true;
                textView.setText(Html.fromHtml(aLogActivity.getCurLogcat("D")));
                ALogActivity.this.refreshShowLog();
            }
        });
        ((Button) findViewById(R.id.alog_i_btn)).setOnClickListener(new View.OnClickListener() { // from class: activity.ALogActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                textView.setText("");
                if (ALogActivity.this.mLock) {
                    return;
                }
                ALogActivity aLogActivity = ALogActivity.this;
                aLogActivity.mLock = true;
                textView.setText(Html.fromHtml(aLogActivity.getCurLogcat("I")));
                ALogActivity.this.refreshShowLog();
            }
        });
        ((Button) findViewById(R.id.alog_e_btn)).setOnClickListener(new View.OnClickListener() { // from class: activity.ALogActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                textView.setText("");
                if (ALogActivity.this.mLock) {
                    return;
                }
                ALogActivity aLogActivity = ALogActivity.this;
                aLogActivity.mLock = true;
                textView.setText(Html.fromHtml(aLogActivity.getCurLogcat("E")));
                ALogActivity.this.refreshShowLog();
            }
        });
        ((Button) findViewById(R.id.alog_w_btn)).setOnClickListener(new View.OnClickListener() { // from class: activity.ALogActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                textView.setText("");
                if (ALogActivity.this.mLock) {
                    return;
                }
                ALogActivity aLogActivity = ALogActivity.this;
                aLogActivity.mLock = true;
                textView.setText(Html.fromHtml(aLogActivity.getCurLogcat("W")));
                ALogActivity.this.refreshShowLog();
            }
        });
        this.radioGroup = (RadioGroup) findViewById(R.id.alog_rg);
        this.radioGroup.check(R.id.alog_v_btn);
        button.callOnClick();
        this.mALogKeyWordsET.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: activity.ALogActivity.9
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView2, int i, KeyEvent keyEvent) {
                if (i != 3) {
                    return false;
                }
                ALogActivity aLogActivity = ALogActivity.this;
                ((RadioButton) aLogActivity.findViewById(aLogActivity.radioGroup.getCheckedRadioButtonId())).callOnClick();
                return false;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshShowLog() {
        this.mH.post(new Runnable() { // from class: activity.ALogActivity.10
            @Override // java.lang.Runnable
            public void run() {
                ALogActivity.this.mALogShowSv.fullScroll(130);
                ALogActivity.this.mLock = false;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getCurLogcat(String str) {
        ALog.d("JC", "getCurLogcat   " + str);
        this.mKeyWords = this.mALogKeyWordsET.getText().toString();
        StringBuilder sb = new StringBuilder();
        sb.append("log start");
        sb.append("<br /><br />");
        try {
            int i = 50;
            if (!this.mKeyWords.equalsIgnoreCase("") && this.mKeyWords.length() >= 2) {
                i = 150;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("logcat -v time -t " + i + " *:" + str).getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                if (this.mKeyWords.equalsIgnoreCase("")) {
                    String strSubstring = line.substring(15, 25);
                    if (strSubstring.contains("W/")) {
                        line = "<font color=\"#EE9A00\">" + line + "</font>";
                    } else if (strSubstring.contains("E/")) {
                        line = "<font color=\"#FF0000\">" + line + "</font>";
                    } else if (strSubstring.contains("I/")) {
                        line = "<font color=\"#00CD00\">" + line + "</font>";
                    }
                    if (strSubstring.contains("D/")) {
                        line = "<font color=\"#4169E1\">" + line + "</font>";
                    }
                    sb.append(line + "<br /><br />");
                } else if (line.contains(this.mKeyWords)) {
                    String strReplace = line.replace(this.mKeyWords, "<font color=\"#FF0000\"><em>" + this.mKeyWords + "</em></font>");
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(strReplace);
                    sb2.append("<br /><br />");
                    sb.append(sb2.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void copyLog(final String str) {
        ThreadPool.DefaultThreadPool.getInstance().submit(new Runnable() { // from class: activity.ALogActivity.11
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r1v0 */
            /* JADX WARN: Type inference failed for: r1v1 */
            /* JADX WARN: Type inference failed for: r1v11 */
            /* JADX WARN: Type inference failed for: r1v13 */
            /* JADX WARN: Type inference failed for: r1v14 */
            /* JADX WARN: Type inference failed for: r1v15 */
            /* JADX WARN: Type inference failed for: r1v16 */
            /* JADX WARN: Type inference failed for: r1v2 */
            /* JADX WARN: Type inference failed for: r1v3, types: [java.io.BufferedReader] */
            /* JADX WARN: Type inference failed for: r1v5 */
            /* JADX WARN: Type inference failed for: r1v7 */
            /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:22:0x006d -> B:32:0x0070). Please report as a decompilation issue!!! */
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                BufferedReader bufferedReader;
                final StringBuilder sb = new StringBuilder();
                ?? r1 = 0;
                BufferedReader bufferedReader2 = null;
                r1 = 0;
                try {
                    try {
                        try {
                            bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("logcat -v time -t 150 *:" + str).getInputStream()));
                        } catch (Throwable th) {
                            th = th;
                        }
                    } catch (IOException e) {
                        e = e;
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    r1 = r1;
                }
                while (true) {
                    try {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        sb.append(line);
                        sb.append(SdkConstant.CLOUDAPI_LF);
                    } catch (IOException e3) {
                        e = e3;
                        bufferedReader2 = bufferedReader;
                        e.printStackTrace();
                        ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: activity.ALogActivity.11.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(ALogActivity.this, "操作失败", 0).show();
                            }
                        });
                        r1 = bufferedReader2;
                        if (bufferedReader2 != null) {
                            bufferedReader2.close();
                            r1 = bufferedReader2;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        r1 = bufferedReader;
                        if (r1 != 0) {
                            try {
                                r1.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        throw th;
                    }
                }
                ThreadPool.MainThreadHandler mainThreadHandler = ThreadPool.MainThreadHandler.getInstance();
                mainThreadHandler.post(new Runnable() { // from class: activity.ALogActivity.11.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ((ClipboardManager) ALogActivity.this.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("LOG", sb));
                        Toast.makeText(ALogActivity.this, "已复制到剪切板", 0).show();
                    }
                });
                bufferedReader.close();
                r1 = mainThreadHandler;
            }
        });
    }
}
