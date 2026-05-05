package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.seculink.app.R;
import java.io.Serializable;
import tools.DensityUtil;

/* JADX INFO: loaded from: classes3.dex */
public class BaseDialog extends DialogFragment implements View.OnClickListener {
    Button btn_left;
    Button btn_right;
    private Builder builder;
    TextView line;
    private onDialogViewCreatedListener listener;
    private Activity mActivity;
    TextView tv_content;
    TextView tv_title;

    public interface onDialogViewCreatedListener {
        void onDialogViewCreated(View view2);
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, Bundle bundle) {
        Button button;
        if (bundle == null) {
            this.builder = getBuilder();
        } else {
            this.builder = (Builder) bundle.getSerializable("BaseDialog.Builder.SavedState");
        }
        this.mActivity = getActivity();
        View viewInflate = LayoutInflater.from(this.mActivity).inflate(this.builder.layoutId > 0 ? this.builder.layoutId : R.layout.dialog_base, (ViewGroup) null);
        this.tv_title = (TextView) viewInflate.findViewById(R.id.tv_title);
        this.tv_content = (TextView) viewInflate.findViewById(R.id.tv_content);
        this.btn_left = (Button) viewInflate.findViewById(R.id.btn_left);
        this.btn_right = (Button) viewInflate.findViewById(R.id.btn_right);
        this.line = (TextView) viewInflate.findViewById(R.id.line);
        Button button2 = this.btn_left;
        if (button2 != null) {
            button2.setOnClickListener(this);
        }
        Button button3 = this.btn_right;
        if (button3 != null) {
            button3.setOnClickListener(this);
        }
        Builder builder = this.builder;
        if (builder != null) {
            if (this.tv_title != null) {
                if (TextUtils.isEmpty(builder.title)) {
                    this.tv_title.setVisibility(8);
                } else {
                    this.tv_title.setText(this.builder.title);
                    this.tv_title.setVisibility(0);
                }
            }
            if (this.tv_content != null) {
                if (TextUtils.isEmpty(this.builder.content)) {
                    this.tv_content.setVisibility(8);
                } else {
                    this.tv_content.setText(this.builder.content);
                    this.tv_content.setVisibility(0);
                }
                this.tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());
            }
            if (this.btn_left != null) {
                if (TextUtils.isEmpty(this.builder.leftBtnText)) {
                    this.btn_left.setVisibility(8);
                } else {
                    this.btn_left.setText(this.builder.leftBtnText);
                    this.btn_left.setVisibility(0);
                }
            }
            if (this.btn_right != null) {
                if (TextUtils.isEmpty(this.builder.rightBtnText)) {
                    this.btn_right.setVisibility(8);
                } else {
                    this.btn_right.setText(this.builder.rightBtnText);
                    this.btn_right.setVisibility(0);
                }
            }
            Button button4 = this.btn_left;
            if (button4 != null && button4.getVisibility() == 0 && (button = this.btn_right) != null && button.getVisibility() == 0) {
                this.line.setVisibility(0);
            } else {
                this.line.setVisibility(8);
            }
        }
        onDialogViewCreatedListener ondialogviewcreatedlistener = this.listener;
        if (ondialogviewcreatedlistener != null) {
            ondialogviewcreatedlistener.onDialogViewCreated(viewInflate);
        }
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putSerializable("BaseDialog.Builder.SavedState", this.builder);
        super.onSaveInstanceState(bundle);
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        this.mActivity = getActivity();
        Dialog dialog2 = new Dialog(this.mActivity, R.style.OBDialog);
        dialog2.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: dialog.BaseDialog.1
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == 4) {
                    BaseDialog.this.dismissAllowingStateLoss();
                }
                if (BaseDialog.this.builder.onKeyListener == null) {
                    return false;
                }
                BaseDialog.this.builder.onKeyListener.onKey(dialogInterface, i, keyEvent);
                return true;
            }
        });
        return dialog2;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        initParam();
    }

    public void setOnDialogCreatedListener(onDialogViewCreatedListener ondialogviewcreatedlistener) {
        this.listener = ondialogviewcreatedlistener;
    }

    @Override // androidx.fragment.app.DialogFragment
    public void show(FragmentManager fragmentManager, String str) {
        if (isAdded()) {
            fragmentManager.beginTransaction().remove(this).commitAllowingStateLoss();
        }
        super.show(fragmentManager, str);
    }

    @Override // androidx.fragment.app.DialogFragment
    public int show(FragmentTransaction fragmentTransaction, String str) {
        if (isAdded()) {
            fragmentTransaction.remove(this).commitAllowingStateLoss();
        }
        return super.show(fragmentTransaction, str);
    }

    public boolean isShowing() {
        if (getDialog() == null) {
            return false;
        }
        return getDialog().isShowing();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getStatusBarHeight() {
        int identifier;
        if ((this.mActivity.getWindow().getAttributes().flags & 1024) != 0 || (identifier = getResources().getIdentifier("status_bar_height", "dimen", DispatchConstants.ANDROID)) <= 0) {
            return 0;
        }
        return getResources().getDimensionPixelSize(identifier);
    }

    private void initParam() {
        Activity activity2 = this.mActivity;
        if (activity2 == null || activity2.isFinishing()) {
            return;
        }
        Dialog dialog2 = getDialog();
        final Window window = dialog2.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setWindowAnimations(this.builder.style);
        window.getDecorView().measure(0, 0);
        int screenWidth = (int) ((((double) DensityUtil.getScreenWidth(getContext())) * 5.0d) / 6.0d);
        int screenHeight = (int) ((((double) DensityUtil.getScreenHeight(getContext())) * 1.0d) / 4.0d);
        final WindowManager.LayoutParams attributes = window.getAttributes();
        if (this.builder.windowWidth != 0) {
            screenWidth = this.builder.windowWidth;
        }
        attributes.width = screenWidth;
        if (this.builder.windowHeight != 0) {
            screenHeight = this.builder.windowHeight;
        }
        attributes.height = screenHeight;
        if (this.builder.isFullScreen) {
            window.addFlags(1024);
        } else {
            window.clearFlags(1024);
        }
        dialog2.setCanceledOnTouchOutside(this.builder.canTouchOutside);
        dialog2.setCancelable(this.builder.cancelable);
        if (!this.builder.isFoucusable) {
            dialog2.getWindow().addFlags(8);
        } else {
            dialog2.getWindow().clearFlags(8);
        }
        if (this.builder.isWindowTranslucent) {
            window.clearFlags(4);
        } else {
            window.addFlags(4);
        }
        if (this.builder.isDimEnabled) {
            window.addFlags(2);
        } else {
            window.clearFlags(2);
        }
        attributes.gravity = this.builder.gravity;
        attributes.x = this.builder.x;
        attributes.y = this.builder.y;
        attributes.alpha = this.builder.windowAlpha;
        attributes.dimAmount = this.builder.outWindowAlpha;
        if (this.builder.nearViewId <= 0) {
            window.setAttributes(attributes);
            return;
        }
        final View viewFindViewById = this.mActivity.findViewById(this.builder.nearViewId);
        if (viewFindViewById != null) {
            new Handler().post(new Runnable() { // from class: dialog.BaseDialog.2
                @Override // java.lang.Runnable
                public void run() {
                    Builder.OBGravity oBGravity = BaseDialog.this.builder.nearViewGravity;
                    int[] iArr = new int[2];
                    viewFindViewById.getLocationOnScreen(iArr);
                    attributes.gravity = 51;
                    View view2 = BaseDialog.this.getView();
                    if (oBGravity == Builder.OBGravity.TOP) {
                        attributes.x = iArr[0] + BaseDialog.this.builder.nearViewX;
                        attributes.y = ((iArr[1] - view2.getHeight()) - BaseDialog.this.getStatusBarHeight()) + BaseDialog.this.builder.nearViewY;
                    } else if (oBGravity == Builder.OBGravity.BOTTOM) {
                        attributes.x = iArr[0] + BaseDialog.this.builder.nearViewX;
                        attributes.y = ((((iArr[1] + viewFindViewById.getHeight()) + viewFindViewById.getPaddingTop()) + viewFindViewById.getPaddingBottom()) - BaseDialog.this.getStatusBarHeight()) + BaseDialog.this.builder.nearViewY;
                    } else if (oBGravity == Builder.OBGravity.LEFT) {
                        attributes.x = (iArr[0] - view2.getWidth()) + BaseDialog.this.builder.nearViewX;
                        attributes.y = (iArr[1] - BaseDialog.this.getStatusBarHeight()) + BaseDialog.this.builder.nearViewY;
                    } else if (oBGravity == Builder.OBGravity.RIGHT) {
                        attributes.x = iArr[0] + viewFindViewById.getWidth() + viewFindViewById.getPaddingLeft() + viewFindViewById.getPaddingRight() + BaseDialog.this.builder.nearViewX;
                        attributes.y = (iArr[1] - BaseDialog.this.getStatusBarHeight()) + BaseDialog.this.builder.nearViewY;
                    }
                    window.setAttributes(attributes);
                }
            });
        }
    }

    @NonNull
    private Builder getBuilder() {
        return (Builder) getArguments().getSerializable("BaseDialog.Builder.SavedState");
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        int id = view2.getId();
        if (id == R.id.btn_left) {
            if (this.builder.leftBtnListener != null) {
                this.builder.leftBtnListener.onClick(view2);
            }
        } else if (id == R.id.btn_right && this.builder.rightBtnListener != null) {
            this.builder.rightBtnListener.onClick(view2);
        }
        dismissAllowingStateLoss();
    }

    public static class Builder implements Serializable {
        private String content;
        private int layoutId;
        private transient View.OnClickListener leftBtnListener;
        private String leftBtnText;
        private int nearViewId;
        private int nearViewX;
        private int nearViewY;
        private transient DialogInterface.OnKeyListener onKeyListener;
        private transient View.OnClickListener rightBtnListener;
        private String rightBtnText;
        private int style;
        private String title;
        private int windowHeight;
        private int windowWidth;
        private int x;
        private int y;
        private int gravity = 17;
        private boolean isDimEnabled = true;
        private boolean isWindowTranslucent = true;
        private float windowAlpha = 1.0f;
        private float outWindowAlpha = 0.5f;
        private boolean isFullScreen = false;
        private boolean canTouchOutside = false;
        private boolean cancelable = true;
        private boolean isFoucusable = true;
        private OBGravity nearViewGravity = OBGravity.TOP;

        public enum OBGravity implements Serializable {
            TOP,
            LEFT,
            RIGHT,
            BOTTOM
        }

        public Builder view(@LayoutRes int i) {
            this.layoutId = i;
            return this;
        }

        public Builder title(String str) {
            this.title = str;
            return this;
        }

        public Builder content(String str) {
            this.content = str;
            return this;
        }

        public Builder leftBtnText(String str) {
            this.leftBtnText = str;
            return this;
        }

        public Builder rightBtnText(String str) {
            this.rightBtnText = str;
            return this;
        }

        public Builder clickLeft(View.OnClickListener onClickListener) {
            this.leftBtnListener = onClickListener;
            return this;
        }

        public Builder clickRight(View.OnClickListener onClickListener) {
            this.rightBtnListener = onClickListener;
            return this;
        }

        public Builder onKeyBackPressed(DialogInterface.OnKeyListener onKeyListener) {
            this.onKeyListener = onKeyListener;
            return this;
        }

        public Builder width(int i) {
            this.windowWidth = i;
            return this;
        }

        public Builder height(int i) {
            this.windowHeight = i;
            return this;
        }

        public Builder gravity(int i) {
            this.gravity = i;
            return this;
        }

        public Builder x(int i) {
            this.x = i;
            return this;
        }

        public Builder y(int i) {
            this.y = i;
            return this;
        }

        public Builder style(int i) {
            this.style = i;
            return this;
        }

        public Builder dim(boolean z) {
            this.isDimEnabled = z;
            return this;
        }

        public Builder windowTranslucent(boolean z) {
            this.isWindowTranslucent = z;
            return this;
        }

        public Builder windowAlpha(float f) {
            this.windowAlpha = this.windowAlpha;
            return this;
        }

        public Builder outWindowAlpha(float f) {
            this.outWindowAlpha = f;
            return this;
        }

        public Builder canCancel(boolean z) {
            this.cancelable = z;
            return this;
        }

        public Builder focus(boolean z) {
            this.isFoucusable = z;
            return this;
        }

        public Builder locate(int i, OBGravity oBGravity) {
            this.nearViewId = i;
            this.nearViewGravity = oBGravity;
            return this;
        }

        public Builder locate(int i, OBGravity oBGravity, int i2, int i3) {
            this.nearViewId = i;
            this.nearViewGravity = oBGravity;
            this.nearViewX = i2;
            this.nearViewY = i3;
            return this;
        }

        public Builder fullScreen(boolean z) {
            this.isFullScreen = z;
            return this;
        }

        public Builder canTouchOutside(boolean z) {
            this.canTouchOutside = z;
            return this;
        }

        public BaseDialog create() {
            BaseDialog baseDialog = new BaseDialog();
            Bundle bundle = new Bundle();
            bundle.putSerializable("BaseDialog.Builder.SavedState", this);
            baseDialog.setArguments(bundle);
            return baseDialog;
        }

        public BaseDialog create(BaseDialog baseDialog) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("BaseDialog.Builder.SavedState", this);
            baseDialog.setArguments(bundle);
            return baseDialog;
        }
    }
}
