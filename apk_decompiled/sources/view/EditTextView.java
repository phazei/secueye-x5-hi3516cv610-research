package view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
@RequiresApi(api = 21)
public class EditTextView extends RelativeLayout {
    EditText editText;
    TextView textView;

    public EditTextView(Context context) {
        super(context);
        init(context);
    }

    public EditTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.EditTextView);
        String string = typedArrayObtainStyledAttributes.getString(0);
        int i = typedArrayObtainStyledAttributes.getInt(1, 20);
        typedArrayObtainStyledAttributes.recycle();
        init(context, string, i);
    }

    @SuppressLint({"SetTextI18n"})
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.edit_text_view, (ViewGroup) this, true);
        this.editText = (EditText) findViewById(R.id.editTextView);
        this.textView = (TextView) findViewById(R.id.num);
        this.textView.setText("0/" + getMaxLength());
        this.editText.setPadding(20, 20, 20, 20);
        this.editText.addTextChangedListener(new TextWatcher() { // from class: view.EditTextView.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Log.d("TAG---------", "beforeTextChanged: ");
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Log.d("TAG---------", "onTextChanged: ");
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                Log.d("TAG---------", "afterTextChanged: ");
            }
        });
    }

    @SuppressLint({"SetTextI18n"})
    private void init(Context context, String str, final int i) {
        LayoutInflater.from(context).inflate(R.layout.edit_text_view, (ViewGroup) this, true);
        this.editText = (EditText) findViewById(R.id.editTextView);
        this.textView = (TextView) findViewById(R.id.num);
        this.textView.setText("0/" + i);
        this.editText.addTextChangedListener(new TextWatcher() { // from class: view.EditTextView.2
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                Log.d("TAG---------", "beforeTextChanged: ");
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                Log.d("TAG---------", "onTextChanged: ");
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                Log.d("TAG---------", "afterTextChanged: ");
                EditTextView.this.textView.setText(editable.length() + "/" + i);
            }
        });
        setHint(str);
        setEditTextMaxText(i);
    }

    public void setEditTextMaxText(int i) {
        this.editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(i)});
    }

    public int getMaxLength() {
        for (InputFilter inputFilter : this.editText.getFilters()) {
            if (inputFilter instanceof InputFilter.LengthFilter) {
                return ((InputFilter.LengthFilter) inputFilter).getMax();
            }
        }
        return 0;
    }

    public void setHint(String str) {
        this.editText.setHint(str);
    }

    public Editable getText() {
        return this.editText.getText();
    }
}
