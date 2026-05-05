package dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class ASlideDialog extends Dialog {
    private Object customContext;

    private static int getStyle(Gravity gravity) {
        return R.style.ASlideDialog;
    }

    private ASlideDialog(Context context, int i) {
        super(context, i);
        this.customContext = null;
    }

    public static ASlideDialog newInstance(Context context, Gravity gravity, int i) {
        ASlideDialog aSlideDialog = new ASlideDialog(context, getStyle(gravity));
        aSlideDialog.setContentView(i);
        configure(aSlideDialog, gravity);
        return aSlideDialog;
    }

    public void setCustomContext(Object obj) {
        this.customContext = obj;
    }

    public Object getCustomContext() {
        return this.customContext;
    }

    private static void configure(ASlideDialog aSlideDialog, Gravity gravity) {
        if (aSlideDialog == null) {
            return;
        }
        Window window = aSlideDialog.getWindow();
        window.setGravity(gravity.value);
        window.setAttributes(getLayoutParams(window.getAttributes(), gravity));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static WindowManager.LayoutParams getLayoutParams(WindowManager.LayoutParams layoutParams, Gravity gravity) {
        if (layoutParams == null) {
            return layoutParams;
        }
        switch (gravity) {
            case Left:
            case Right:
                layoutParams.width = -2;
                layoutParams.height = -1;
                return layoutParams;
            case Top:
            case Bottom:
                layoutParams.width = -1;
                layoutParams.height = -2;
                return layoutParams;
            case Center:
                layoutParams.width = -2;
                layoutParams.height = -2;
                return layoutParams;
            default:
                return layoutParams;
        }
    }

    public enum Gravity {
        Left(3),
        Right(5),
        Top(48),
        Bottom(80),
        Center(17);

        public final int value;

        Gravity(int i) {
            this.value = i;
        }
    }

    public void setWindowAnimations(int i) {
        getWindow().setWindowAnimations(i);
    }
}
