package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.seculink.app.R;
import tools.LogEx;

/* JADX INFO: loaded from: classes3.dex */
public class InputDialogViewIpc<T> extends DialogFragment {
    private String content;
    private T extra;
    private String hint;
    private boolean isAllowCancel;
    private boolean isShow = true;
    private OnClickListener listener;
    private String title;

    public interface OnClickListener<T> {
        void onNegativeClick();

        void onPositiveClick(String str, T t);
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // androidx.fragment.app.DialogFragment
    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        LogEx.e(true, "onCreateDialog", "onCreateDialog");
        View viewInflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_imput3, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_title);
        final EditText editText = (EditText) viewInflate.findViewById(R.id.et_content);
        final ImageView imageView = (ImageView) viewInflate.findViewById(R.id.eye_detected);
        textView.setText(this.title);
        editText.setText(this.content);
        String str = this.content;
        if (str != null) {
            editText.setSelection(str.length());
        }
        Button button = (Button) viewInflate.findViewById(R.id.left_btn);
        final Button button2 = (Button) viewInflate.findViewById(R.id.right_btn);
        AlertDialog alertDialogCreate = new AlertDialog.Builder(getActivity(), R.style.alert_dialog_soft_input).setView(viewInflate).create();
        alertDialogCreate.setCanceledOnTouchOutside(false);
        alertDialogCreate.setCancelable(true);
        alertDialogCreate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialogCreate.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: dialog.InputDialogViewIpc.1
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == 4;
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: dialog.InputDialogViewIpc.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                InputDialogViewIpc.this.dismiss();
                if (InputDialogViewIpc.this.listener != null) {
                    InputDialogViewIpc.this.listener.onNegativeClick();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() { // from class: dialog.InputDialogViewIpc.3
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                InputDialogViewIpc.this.dismiss();
                if (InputDialogViewIpc.this.listener != null) {
                    InputDialogViewIpc.this.listener.onPositiveClick(editText.getText().toString(), InputDialogViewIpc.this.extra);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() { // from class: dialog.InputDialogViewIpc.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                InputDialogViewIpc.this.isShow = !r4.isShow;
                InputDialogViewIpc inputDialogViewIpc = InputDialogViewIpc.this;
                inputDialogViewIpc.showOrHide(inputDialogViewIpc.isShow, editText, imageView);
            }
        });
        editText.addTextChangedListener(new TextWatcher() { // from class: dialog.InputDialogViewIpc.5
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (editable != null && !editable.toString().equals("") && editable.toString().length() >= 8) {
                    button2.setEnabled(true);
                    button2.setBackgroundResource(R.drawable.bg_button_blue);
                } else {
                    button2.setEnabled(false);
                    button2.setBackgroundResource(R.drawable.bg_button_gray);
                }
            }
        });
        return alertDialogCreate;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public T getExtra() {
        return this.extra;
    }

    public String getHint() {
        return this.hint;
    }

    public void setHint(String str) {
        this.hint = str;
    }

    public void setExtra(T t) {
        this.extra = t;
    }

    public void addOnClickListener(OnClickListener onClickListener) {
        this.listener = onClickListener;
    }

    @Override // androidx.fragment.app.DialogFragment
    public void show(FragmentManager fragmentManager, String str) {
        Fragment fragmentFindFragmentByTag = fragmentManager.findFragmentByTag(str);
        if (fragmentFindFragmentByTag == null || !fragmentFindFragmentByTag.isAdded()) {
            try {
                super.show(fragmentManager, str);
            } catch (IllegalStateException unused) {
            }
        }
    }

    public static class Builder {

        /* JADX INFO: renamed from: dialog, reason: collision with root package name */
        private InputDialogViewIpc f7872dialog = new InputDialogViewIpc();

        public Builder content(String str) {
            this.f7872dialog.content = str;
            return this;
        }

        public Builder title(String str) {
            this.f7872dialog.title = str;
            return this;
        }

        public Builder hint(String str) {
            this.f7872dialog.hint = str;
            return this;
        }

        public Builder isAllowCancel(boolean z) {
            this.f7872dialog.isAllowCancel = z;
            return this;
        }

        public InputDialogViewIpc build() {
            return this.f7872dialog;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showOrHide(boolean z, EditText editText, ImageView imageView) {
        int selectionStart = editText.getSelectionStart();
        if (z) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imageView.setBackgroundResource(R.drawable.wifi_eye_open);
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageView.setBackgroundResource(R.drawable.wifi_eye_close);
        }
        editText.setSelection(selectionStart);
    }
}
