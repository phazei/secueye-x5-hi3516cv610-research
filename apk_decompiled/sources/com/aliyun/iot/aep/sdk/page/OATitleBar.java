package com.aliyun.iot.aep.sdk.page;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aliyun.iot.aep.sdk.framework.R;
import com.aliyun.iot.link.ui.component.nav.UIBarItem;
import com.aliyun.iot.link.ui.component.nav.UINavigationBar;

/* JADX INFO: loaded from: classes2.dex */
public class OATitleBar extends LinearLayout {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_SIMPLE = 1;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f4862a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private ImageView f4863b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private TextView f4864c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private UINavigationBar f4865d;

    public void setmBtnBackVisible(int i) {
    }

    public OATitleBar(Context context) {
        this(context, null);
    }

    public OATitleBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public OATitleBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.f4862a = 0;
        setOrientation(1);
        setType(0);
    }

    public void setType(int i) {
        this.f4862a = i;
        a();
    }

    private void a() {
        removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.sdk_framework_ali_sdk_openaccount_title_layout_2, (ViewGroup) this, true);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_title);
        View viewFindViewById = findViewById(R.id.line_title);
        this.f4865d = (UINavigationBar) findViewById(R.id.top_bar);
        this.f4863b = (ImageView) findViewById(R.id.ali_sdk_openaccount_back);
        this.f4864c = (TextView) findViewById(R.id.ali_sdk_openaccount_title);
        if (this.f4862a == 1) {
            this.f4865d.setVisibility(0);
            relativeLayout.setVisibility(8);
            viewFindViewById.setVisibility(8);
        } else {
            this.f4865d.setVisibility(8);
            relativeLayout.setVisibility(0);
            viewFindViewById.setVisibility(0);
        }
    }

    public void setBackClickListener(final View.OnClickListener onClickListener) {
        if (this.f4862a == 1) {
            this.f4865d.setNavigationBackAction(new UIBarItem.Action() { // from class: com.aliyun.iot.aep.sdk.page.OATitleBar.1
                @Override // com.aliyun.iot.link.ui.component.nav.UIBarItem.Action
                public void invoke(View view2) {
                    onClickListener.onClick(view2);
                }
            });
        } else {
            this.f4863b.setOnClickListener(onClickListener);
        }
    }

    public void setTitle(String str) {
        if (this.f4862a == 1) {
            this.f4865d.setTitle(str);
        } else {
            this.f4864c.setText(str);
        }
    }
}
