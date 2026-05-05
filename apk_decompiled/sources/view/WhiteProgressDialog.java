package view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.seculink.app.R;
import tools.ScreenUtil;

/* JADX INFO: loaded from: classes5.dex */
public class WhiteProgressDialog extends Dialog implements View.OnClickListener {
    Activity context;
    String text;
    TextView tv_content;

    public interface OnViewClick {
        void onDismiss();
    }

    public WhiteProgressDialog(@NonNull Activity activity2) {
        super(activity2);
        this.context = activity2;
    }

    public WhiteProgressDialog(@NonNull Activity activity2, String str) {
        super(activity2);
        this.context = activity2;
        this.text = str;
    }

    public WhiteProgressDialog(@NonNull Activity activity2, int i) {
        super(activity2, i);
        this.context = activity2;
    }

    protected WhiteProgressDialog(@NonNull Activity activity2, boolean z, @Nullable DialogInterface.OnCancelListener onCancelListener) {
        super(activity2, z, onCancelListener);
        this.context = activity2;
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        setContentView(R.layout.layout_load_progress);
        getWindow().setDimAmount(0.3f);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_button_white);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        this.tv_content = (TextView) findViewById(R.id.tv_content);
        this.tv_content.setText(this.text);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = (ScreenUtil.getDisplayMetrics(this.context)[0] * 6) / 7;
        attributes.height = -2;
        getWindow().setAttributes(attributes);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (isShowing()) {
            dismiss();
        }
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
    }

    public void setText(String str) {
        this.text = str;
        TextView textView = this.tv_content;
        if (textView != null) {
            textView.setText(str);
        }
    }
}
