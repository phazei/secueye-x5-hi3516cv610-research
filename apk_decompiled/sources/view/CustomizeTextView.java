package view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import com.seculink.app.R;
import java.util.Timer;

/* JADX INFO: loaded from: classes5.dex */
public class CustomizeTextView extends AppCompatTextView {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    Class<?> cls;
    Context context;
    private Timer onTouchTimer;

    public CustomizeTextView(Context context, AttributeSet attributeSet) throws ClassNotFoundException {
        super(context, attributeSet);
        this.context = context;
        this.cls = Class.forName(context.obtainStyledAttributes(attributeSet, R.styleable.CustomizeTextView).getString(0));
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            ContextCompat.startActivity(this.context, new Intent(this.context, this.cls), null);
            return true;
        }
        motionEvent.getAction();
        return true;
    }
}
