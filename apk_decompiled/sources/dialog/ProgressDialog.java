package dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.StringRes;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ProgressDialog extends Dialog {
    public int height;
    int layout;
    public LayoutInflater layoutInflate;
    TextView message;

    /* JADX INFO: renamed from: view, reason: collision with root package name */
    public View f7874view;
    public int width;

    public ProgressDialog(Context context) {
        super(context, R.style.progress_Dialog);
        this.f7874view = null;
        this.layoutInflate = LayoutInflater.from(context);
        this.f7874view = this.layoutInflate.inflate(R.layout.dialog_progress, (ViewGroup) null);
        this.message = (TextView) this.f7874view.findViewById(R.id.tv_message);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void setMessage(String str) {
        setMessageVisible(str);
        this.message.setText(str + "");
    }

    public void setMessage(@StringRes int i) {
        String string = getContext().getResources().getString(i);
        setMessageVisible(string);
        this.message.setText(string + "");
    }

    public void setMessageVisible(String str) {
        if (TextUtils.isEmpty(str)) {
            this.message.setVisibility(8);
        } else {
            this.message.setVisibility(0);
        }
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        View view2 = this.f7874view;
        if (view2 != null) {
            setContentView(view2);
        }
    }
}
