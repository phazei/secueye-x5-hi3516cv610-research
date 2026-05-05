package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes3.dex */
public class AlertDialogView extends DialogFragment {
    private int icon;
    private boolean isAllowCancel;
    private boolean isShowNegativeButton;
    private OnClickListener listener;
    private String message;
    private String title;

    public interface OnClickListener {
        void onNegativeClick();

        void onPositiveClick();
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // androidx.fragment.app.DialogFragment
    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder positiveButton = new AlertDialog.Builder(getActivity(), R.style.alert_dialog_soft_input).setTitle(this.title).setIcon(this.icon).setMessage(this.message).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: dialog.AlertDialogView.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialogView.this.dismiss();
                if (AlertDialogView.this.listener != null) {
                    AlertDialogView.this.listener.onPositiveClick();
                }
            }
        });
        if (this.isShowNegativeButton) {
            positiveButton.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: dialog.AlertDialogView.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    AlertDialogView.this.dismiss();
                    if (AlertDialogView.this.listener != null) {
                        AlertDialogView.this.listener.onNegativeClick();
                    }
                }
            });
        }
        AlertDialog alertDialogCreate = positiveButton.create();
        alertDialogCreate.setCanceledOnTouchOutside(this.isAllowCancel);
        alertDialogCreate.setCancelable(this.isAllowCancel);
        alertDialogCreate.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: dialog.AlertDialogView.3
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == 4;
            }
        });
        return alertDialogCreate;
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
        private AlertDialogView f7868dialog = new AlertDialogView();

        public Builder message(String str) {
            this.f7868dialog.message = str;
            return this;
        }

        public Builder icon(int i) {
            this.f7868dialog.icon = i;
            return this;
        }

        public Builder title(String str) {
            this.f7868dialog.title = str;
            return this;
        }

        public Builder isShowNegativeButton(boolean z) {
            this.f7868dialog.isShowNegativeButton = z;
            return this;
        }

        public Builder isAllowCancel(boolean z) {
            this.f7868dialog.isAllowCancel = z;
            return this;
        }

        public AlertDialogView build() {
            return this.f7868dialog;
        }
    }
}
