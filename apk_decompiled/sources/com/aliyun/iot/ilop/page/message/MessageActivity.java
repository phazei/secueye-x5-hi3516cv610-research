package com.aliyun.iot.ilop.page.message;

import activity.CommonActivity;
import activity.StartActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes2.dex */
public class MessageActivity extends CommonActivity {
    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.demo_activity_layout;
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Log.e("离线推送内容body", "MessageActivity" + getIntent().getData());
        startActivity(new Intent(this, (Class<?>) StartActivity.class));
        finish();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("离线推送内容MessageActivity", "" + intent.getData());
    }
}
