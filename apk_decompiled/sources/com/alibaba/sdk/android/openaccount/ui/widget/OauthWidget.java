package com.alibaba.sdk.android.openaccount.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.sdk.android.oauth.OauthModule;
import com.alibaba.sdk.android.openaccount.OauthService;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.util.AttributeUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.alibaba.sdk.android.openaccount.util.WidgetUtils;
import com.alibaba.sdk.android.pluto.Pluto;

/* JADX INFO: loaded from: classes.dex */
public class OauthWidget extends LinearLayoutTemplate implements View.OnClickListener {
    private static final int OAUTH_PLATEFORM_COUNT = 5;
    private static final String TAG = "OauthWidget";
    protected OauthOnClickListener oauthOnClickListener;
    protected TextView[] oauths;
    protected TextView[] texts;

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.LinearLayoutTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_oauth";
    }

    public OauthWidget(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.oauths = new TextView[5];
        this.texts = new TextView[5];
        try {
            Class.forName("com.alibaba.sdk.android.openaccount.OauthService");
            int i = 0;
            while (i < 5) {
                StringBuilder sb = new StringBuilder();
                sb.append("oauth_");
                int i2 = i + 1;
                sb.append(i2);
                TextView textView = (TextView) findViewById(sb.toString());
                if (textView != null) {
                    textView.setTypeface(this.iconfont);
                    textView.setOnClickListener(this);
                }
                this.oauths[i] = textView;
                this.texts[i] = (TextView) findViewById("oauth_" + i2 + "_text");
                i = i2;
            }
            useCustomAttrs(context, attributeSet);
        } catch (ClassNotFoundException unused) {
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.LinearLayoutTemplate
    protected void doUseCustomAttrs(Context context, TypedArray typedArray) {
        boolean zIsUmengAvailable;
        super.doUseCustomAttrs(context, typedArray);
        int i = AttributeUtils.getInt(context, typedArray, "ali_sdk_openaccount_attrs_login_oauth_plateform");
        boolean z = false;
        for (int i2 = 0; i2 < 5; i2++) {
            if (((i >>> i2) & 1) != 0) {
                LinearLayout linearLayout = (LinearLayout) findViewById(ResourceUtils.getRId(context, "oauth_" + (i2 + 1) + "_layout"));
                if (i2 == 0) {
                    zIsUmengAvailable = OauthModule.isSecondPartTaobaoSdkAvailable() || OauthModule.isThirdPartMemberSdkAvaiable();
                } else if (i2 == 4) {
                    zIsUmengAvailable = OauthModule.isAlipaySdkAvailable();
                } else {
                    zIsUmengAvailable = OauthModule.isUmengAvailable();
                }
                if (zIsUmengAvailable) {
                    linearLayout.setVisibility(0);
                }
                z = true;
            }
            TextView[] textViewArr = this.oauths;
            if (textViewArr[i2] != null) {
                WidgetUtils.setBackgroundDrawable(textViewArr[i2], AttributeUtils.getDrawable(context, typedArray, "ali_sdk_openaccount_attrs_login_oauth_" + (i2 + 1) + "_bg"));
                this.oauths[i2].setTextColor(AttributeUtils.getColor(context, typedArray, "ali_sdk_openaccount_attrs_login_oauth_text_color"));
            }
            TextView[] textViewArr2 = this.texts;
            if (textViewArr2[i2] != null) {
                textViewArr2[i2].setTextColor(AttributeUtils.getColor(context, typedArray, "ali_sdk_openaccount_attrs_login_oauth_text_text_color"));
            }
        }
        if (z) {
            setVisibility(0);
        }
        TextView textView = (TextView) findViewById(ResourceUtils.getRId(context, "other_plateform_login"));
        if (textView != null) {
            textView.setTextColor(AttributeUtils.getColor(context, typedArray, "ali_sdk_openaccount_attrs_login_other_plateform_login_text_color"));
        }
    }

    public OauthOnClickListener getOauthOnClickListener() {
        return this.oauthOnClickListener;
    }

    public void setOauthOnClickListener(OauthOnClickListener oauthOnClickListener) {
        this.oauthOnClickListener = oauthOnClickListener;
    }

    public void setOauthOnClickListener(final LoginCallback loginCallback) {
        this.oauthOnClickListener = new OauthOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.widget.OauthWidget.1
            @Override // com.alibaba.sdk.android.openaccount.ui.widget.OauthOnClickListener
            public void onClick(View view2, int i) {
                OauthService oauthService = (OauthService) Pluto.DEFAULT_INSTANCE.getBean(OauthService.class);
                if (oauthService != null) {
                    oauthService.oauth((Activity) view2.getContext(), i, loginCallback);
                } else {
                    AliSDKLogger.e(OauthWidget.TAG, "oauth service is null");
                }
            }
        };
    }

    public void authorizeCallback(int i, int i2, Intent intent) {
        OauthService oauthService = (OauthService) Pluto.DEFAULT_INSTANCE.getBean(OauthService.class);
        if (oauthService != null) {
            oauthService.authorizeCallback(i, i2, intent);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (this.oauthOnClickListener == null) {
            return;
        }
        for (int i = 0; i < 5; i++) {
            if (view2 == this.oauths[i]) {
                this.oauthOnClickListener.onClick(view2, i + 1);
            }
        }
    }
}
