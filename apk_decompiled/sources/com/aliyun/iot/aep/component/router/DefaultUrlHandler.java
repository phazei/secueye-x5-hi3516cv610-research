package com.aliyun.iot.aep.component.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultUrlHandler {
    public void onUrlHandle(Context context, String str, Bundle bundle, boolean z, int i) {
        onUrlHandle(context, str, bundle, z, i, null);
    }

    public void onUrlHandle(Context context, String str, Bundle bundle, boolean z, int i, IAsyncHandlerCallback iAsyncHandlerCallback) {
        try {
            Intent intent = new Intent();
            intent.setData(Uri.parse(str));
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            if (z && (context instanceof Activity)) {
                ((Activity) context).startActivityForResult(intent, i);
            } else {
                context.startActivity(intent);
            }
            if (iAsyncHandlerCallback != null) {
                iAsyncHandlerCallback.asyncHandle(true);
            }
        } catch (Exception unused) {
            if (iAsyncHandlerCallback != null) {
                iAsyncHandlerCallback.asyncHandle(false);
            }
        }
    }
}
