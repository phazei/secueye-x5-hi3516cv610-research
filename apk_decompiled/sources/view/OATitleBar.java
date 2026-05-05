package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aliyun.iot.link.ui.component.nav.UIBarItem;
import com.aliyun.iot.link.ui.component.nav.UINavigationBar;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class OATitleBar extends LinearLayout {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_SIMPLE = 1;
    private ImageView mBtnBack;
    private TextView mTitle;
    private UINavigationBar mTopBar;
    private int mType;

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
        this.mType = 0;
        setOrientation(1);
        setType(0);
    }

    public void setType(int i) {
        this.mType = i;
        initView();
    }

    private void initView() {
        removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.ali_sdk_openaccount_title_layout_2, (ViewGroup) this, true);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_title);
        View viewFindViewById = findViewById(R.id.line_title);
        this.mTopBar = (UINavigationBar) findViewById(R.id.top_bar);
        this.mBtnBack = (ImageView) findViewById(R.id.ali_sdk_openaccount_back);
        this.mTitle = (TextView) findViewById(R.id.ali_sdk_openaccount_title);
        if (this.mType == 1) {
            this.mTopBar.setVisibility(0);
            relativeLayout.setVisibility(8);
            viewFindViewById.setVisibility(8);
        } else {
            this.mTopBar.setVisibility(8);
            relativeLayout.setVisibility(0);
            viewFindViewById.setVisibility(0);
        }
    }

    public void setBackClickListener(final View.OnClickListener onClickListener) {
        if (this.mType == 1) {
            this.mTopBar.setNavigationBackAction(new UIBarItem.Action() { // from class: view.-$$Lambda$OATitleBar$HSDJQZm9trjxERJAeQ0D-HMVHoI
                @Override // com.aliyun.iot.link.ui.component.nav.UIBarItem.Action
                public final void invoke(View view2) {
                    onClickListener.onClick(view2);
                }
            });
        } else {
            this.mBtnBack.setOnClickListener(onClickListener);
        }
    }

    public void setTitle(String str) {
        if (this.mType == 1) {
            this.mTopBar.setTitle(str);
        } else {
            this.mTitle.setText(str);
        }
    }
}
