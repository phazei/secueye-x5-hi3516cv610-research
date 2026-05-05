package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class SettingTitleView extends LinearLayout {
    private TextView tv_text;

    public SettingTitleView(Context context) {
        this(context, null);
    }

    public SettingTitleView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SettingTitleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        LayoutInflater.from(getContext()).inflate(R.layout.title, (ViewGroup) this, true);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SettingTitleView);
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(0, 0);
        this.tv_text = (TextView) findViewById(R.id.tv_text);
        this.tv_text.setText(resourceId);
        typedArrayObtainStyledAttributes.recycle();
    }
}
