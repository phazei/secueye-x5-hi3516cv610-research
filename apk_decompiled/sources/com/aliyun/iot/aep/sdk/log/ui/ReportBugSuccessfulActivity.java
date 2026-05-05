package com.aliyun.iot.aep.sdk.log.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.aliyun.iot.aep.sdk.log.R;

/* JADX INFO: loaded from: classes2.dex */
public class ReportBugSuccessfulActivity extends Activity {
    private String getBugIdUrl(String str) {
        return "http://iot-alog.aliyun.test:8080/abugdetail?bugId=" + str;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_report_bug_successful);
        TextView textView = (TextView) findViewById(R.id.bugId);
        String stringExtra = getIntent().getStringExtra("bugId");
        textView.setText("Bug Id: " + stringExtra);
        ((TextView) findViewById(R.id.bugIdUrl)).setText(getBugIdUrl(stringExtra));
        ((Button) findViewById(R.id.successOkBtn)).setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.log.ui.ReportBugSuccessfulActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ReportBugSuccessfulActivity.this.finish();
            }
        });
        ((Button) findViewById(R.id.reportBugContinueBtn)).setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.log.ui.ReportBugSuccessfulActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ReportBugSuccessfulActivity.this.startActivity(new Intent(ReportBugSuccessfulActivity.this, (Class<?>) ALogActivity.class));
                ReportBugSuccessfulActivity.this.finish();
            }
        });
    }
}
