package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.textfield.TextInputLayout;
import com.seculink.app.R;
import tools.LogEx;

/* JADX INFO: loaded from: classes3.dex */
public class InputDialogView<T> extends DialogFragment {
    private String content;
    private T extra;
    private String hint;
    private boolean isAllowCancel;
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
        View viewInflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_input, (ViewGroup) null);
        TextView textView = (TextView) viewInflate.findViewById(R.id.tv_title);
        final EditText editText = (EditText) viewInflate.findViewById(R.id.et_content);
        TextInputLayout textInputLayout = (TextInputLayout) viewInflate.findViewById(R.id.textInputLayout);
        textView.setText(this.title);
        textInputLayout.setHint(this.hint);
        editText.setText(this.content);
        String str = this.content;
        if (str != null) {
            editText.setSelection(str.length());
        }
        AlertDialog.Builder positiveButton = new AlertDialog.Builder(getActivity(), R.style.alert_dialog_soft_input).setView(viewInflate).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: dialog.InputDialogView.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                InputDialogView.this.dismiss();
                if (InputDialogView.this.listener != null) {
                    InputDialogView.this.listener.onPositiveClick(editText.getText().toString(), InputDialogView.this.extra);
                }
            }
        });
        positiveButton.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: dialog.InputDialogView.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                InputDialogView.this.dismiss();
                if (InputDialogView.this.listener != null) {
                    InputDialogView.this.listener.onNegativeClick();
                }
            }
        });
        AlertDialog alertDialogCreate = positiveButton.create();
        alertDialogCreate.setCanceledOnTouchOutside(this.isAllowCancel);
        alertDialogCreate.setCancelable(this.isAllowCancel);
        alertDialogCreate.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: dialog.InputDialogView.3
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == 4;
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
        private InputDialogView f7870dialog = new InputDialogView();

        public Builder content(String str) {
            this.f7870dialog.content = str;
            return this;
        }

        public Builder title(String str) {
            this.f7870dialog.title = str;
            return this;
        }

        public Builder hint(String str) {
            this.f7870dialog.hint = str;
            return this;
        }

        public Builder isAllowCancel(boolean z) {
            this.f7870dialog.isAllowCancel = z;
            return this;
        }

        public InputDialogView build() {
            return this.f7870dialog;
        }
    }
}
