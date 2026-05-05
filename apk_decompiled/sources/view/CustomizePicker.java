package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import androidx.core.view.ViewCompat;

/* JADX INFO: loaded from: classes5.dex */
public class CustomizePicker extends NumberPicker {
    public CustomizePicker(Context context) {
        super(context);
    }

    public CustomizePicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomizePicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.ViewGroup
    public void addView(View view2) {
        super.addView(view2);
        updateView(view2);
    }

    @Override // android.view.ViewGroup
    public void addView(View view2, int i, ViewGroup.LayoutParams layoutParams) {
        super.addView(view2, i, layoutParams);
        updateView(view2);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View view2, ViewGroup.LayoutParams layoutParams) {
        super.addView(view2, layoutParams);
        updateView(view2);
    }

    public void updateView(View view2) {
        if (view2 instanceof EditText) {
            ((EditText) view2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
        }
    }
}
