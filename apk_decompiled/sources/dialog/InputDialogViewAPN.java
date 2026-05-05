package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.seculink.app.R;
import tools.LogEx;

/* JADX INFO: loaded from: classes3.dex */
public class InputDialogViewAPN<T> extends DialogFragment {
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
        View viewInflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_imput_apn, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.title);
        final EditText editText = (EditText) viewInflate.findViewById(R.id.et_content);
        textView.setText(this.title);
        editText.setText(this.content);
        String str = this.content;
        if (str != null) {
            editText.setSelection(str.length());
        }
        Button button = (Button) viewInflate.findViewById(R.id.btn_left);
        Button button2 = (Button) viewInflate.findViewById(R.id.btn_right);
        AlertDialog alertDialogCreate = new AlertDialog.Builder(getActivity(), R.style.alert_dialog_soft_input).setView(viewInflate).create();
        alertDialogCreate.setCanceledOnTouchOutside(false);
        alertDialogCreate.setCancelable(true);
        alertDialogCreate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        int i = getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams attributes = alertDialogCreate.getWindow().getAttributes();
        attributes.width = (int) (((double) i) * 0.85d);
        alertDialogCreate.getWindow().setAttributes(attributes);
        alertDialogCreate.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: dialog.InputDialogViewAPN.1
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i2, KeyEvent keyEvent) {
                return i2 == 4;
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: dialog.InputDialogViewAPN.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                InputDialogViewAPN.this.dismiss();
                if (InputDialogViewAPN.this.listener != null) {
                    InputDialogViewAPN.this.listener.onNegativeClick();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() { // from class: dialog.InputDialogViewAPN.3
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                InputDialogViewAPN.this.dismiss();
                if (InputDialogViewAPN.this.listener != null) {
                    InputDialogViewAPN.this.listener.onPositiveClick(editText.getText().toString(), InputDialogViewAPN.this.extra);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() { // from class: dialog.InputDialogViewAPN.4
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
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
        private InputDialogViewAPN f7871dialog = new InputDialogViewAPN();

        public Builder content(String str) {
            this.f7871dialog.content = str;
            return this;
        }

        public Builder title(String str) {
            this.f7871dialog.title = str;
            return this;
        }

        public Builder hint(String str) {
            this.f7871dialog.hint = str;
            return this;
        }

        public Builder isAllowCancel(boolean z) {
            this.f7871dialog.isAllowCancel = z;
            return this;
        }

        public InputDialogViewAPN build() {
            return this.f7871dialog;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (((double) getResources().getDisplayMetrics().widthPixels) * 0.85d);
        attributes.height = -2;
        window.setAttributes(attributes);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        Dialog dialog2 = getDialog();
        if (dialog2 != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            dialog2.getWindow().setLayout((int) (((double) displayMetrics.widthPixels) * 0.85d), -2);
        }
    }
}
