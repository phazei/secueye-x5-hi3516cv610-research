package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class ButtonRipperView extends LinearLayout {
    int imgRes;
    private ImageView roomOutButton;
    private String text;
    private TextView tv_enlarge;

    public ButtonRipperView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ButtonRipperView);
        this.imgRes = typedArrayObtainStyledAttributes.getResourceId(0, 0);
        this.text = typedArrayObtainStyledAttributes.getString(1);
        typedArrayObtainStyledAttributes.recycle();
        init(context, this.text, this.imgRes);
    }

    private void init(Context context, String str, int i) {
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.button_ripper_view, (ViewGroup) this, true);
        this.roomOutButton = (ImageView) viewInflate.findViewById(R.id.btn);
        this.tv_enlarge = (TextView) viewInflate.findViewById(R.id.tv);
        this.roomOutButton.setImageResource(i);
        this.tv_enlarge.setText(str);
    }

    public void setImageResource(int i) {
        this.roomOutButton.setImageResource(i);
    }

    public void setTextColor(int i) {
        this.tv_enlarge.setTextColor(i);
    }
}
