package dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class LoadingDialog extends Dialog {
    private TextView tv_text;

    public LoadingDialog(Context context) {
        super(context, R.style.ShareDialogTheme2);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setCancelable(false);
        setContentView(R.layout.dialog_loading);
        this.tv_text = (TextView) findViewById(R.id.tv_text);
        setCanceledOnTouchOutside(false);
    }

    public LoadingDialog setMessage(String str) {
        this.tv_text.setText(str);
        return this;
    }
}
