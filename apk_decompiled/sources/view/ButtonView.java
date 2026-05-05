package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class ButtonView extends ConstraintLayout {
    int ButtonImage_Res;
    String ButtonText_Res;
    ImageView buttonImage;
    TextView buttonText;
    LinearLayout button_button;
    int textColor;

    public ButtonView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ButtonView);
        this.ButtonImage_Res = typedArrayObtainStyledAttributes.getResourceId(1, 0);
        this.ButtonText_Res = typedArrayObtainStyledAttributes.getString(2);
        this.textColor = typedArrayObtainStyledAttributes.getColor(0, getResources().getColor(R.color.colors_aeb3c0));
        typedArrayObtainStyledAttributes.recycle();
        init(context, this.ButtonImage_Res, this.ButtonText_Res, this.textColor);
    }

    private void init(Context context, int i, String str, int i2) {
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.button_view, (ViewGroup) this, true);
        this.buttonImage = (ImageView) viewInflate.findViewById(R.id.button_image);
        this.buttonText = (TextView) viewInflate.findViewById(R.id.button_text);
        this.button_button = (LinearLayout) viewInflate.findViewById(R.id.button_button);
        this.buttonImage.setImageResource(i);
        this.buttonText.setText(str);
        this.buttonText.setTextColor(i2);
    }

    public void setClickEffect(final int i, final int i2) {
        setOnTouchListener(new View.OnTouchListener() { // from class: view.ButtonView.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                view2.performClick();
                if (motionEvent.getAction() == 0) {
                    ButtonView.this.buttonImage.setImageResource(i);
                } else if (motionEvent.getAction() == 1) {
                    ButtonView.this.buttonImage.setImageResource(i2);
                }
                return true;
            }
        });
    }

    public void setButtonImage(int i) {
        this.buttonImage.setImageResource(i);
    }

    public String getText() {
        return this.buttonText.getText().toString();
    }

    public void setText(String str) {
        this.buttonText.setText(str);
    }
}
