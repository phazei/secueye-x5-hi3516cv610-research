package com.alibaba.sdk.android.openaccount.ui.ui;

import android.os.Bundle;
import android.widget.Button;
import com.alibaba.sdk.android.openaccount.annotation.ExtensionPoint;
import com.alibaba.sdk.android.openaccount.ui.widget.NextStepButtonWatcher;
import org.mozilla.javascript.ES6Iterator;

/* JADX INFO: loaded from: classes.dex */
public abstract class NextStepActivityTemplate extends ActivityTemplate {

    @ExtensionPoint
    protected Button next;
    private NextStepButtonWatcher nextStepTextWatcher = new NextStepButtonWatcher();

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.next = (Button) findViewById(ES6Iterator.NEXT_METHOD);
        Button button = this.next;
        if (button != null) {
            this.nextStepTextWatcher.setNextStepButton(button);
        }
    }

    @ExtensionPoint
    protected NextStepButtonWatcher getNextStepButtonWatcher() {
        return this.nextStepTextWatcher;
    }
}
