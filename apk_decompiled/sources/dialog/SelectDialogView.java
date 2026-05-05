package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.seculink.app.R;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes3.dex */
public class SelectDialogView<T> extends DialogFragment {
    private T extra;
    private boolean isAllowCancel;
    private OnClickListener listener;
    private ArrayList<RadioButton> radioList = new ArrayList<>();
    private String[] selectItems;
    private String selectStr;
    private String title;

    public interface OnClickListener<T> {
        void onNegativeClick();

        void onPositiveClick(int i, String str, T t);
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
    }

    @Override // androidx.fragment.app.DialogFragment
    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        RadioGroup radioGroup = new RadioGroup(getActivity());
        radioGroup.setId(R.id.radio_group);
        radioGroup.setOrientation(1);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(-2, -2);
        layoutParams.setMargins(dp2Px(getActivity(), 16.0f), dp2Px(getActivity(), 8.0f), 0, 0);
        if (this.selectItems != null) {
            this.radioList.clear();
            for (int i = 0; i < this.selectItems.length; i++) {
                RadioButton radioButton = new RadioButton(getActivity());
                radioButton.setTextColor(getResources().getColor(R.color.color_text));
                radioButton.setText(this.selectItems[i]);
                this.radioList.add(radioButton);
                radioGroup.addView(radioButton, layoutParams);
                if (this.selectItems[i].equals(this.selectStr)) {
                    radioGroup.check(radioButton.getId());
                }
            }
        }
        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.addView(radioGroup, new FrameLayout.LayoutParams(-2, -2));
        AlertDialog.Builder positiveButton = new AlertDialog.Builder(getActivity(), R.style.alert_dialog_soft_input).setTitle(this.title).setView(scrollView).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: dialog.SelectDialogView.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                SelectDialogView.this.dismiss();
                if (SelectDialogView.this.listener != null) {
                    int i3 = 0;
                    for (int i4 = 0; i4 < SelectDialogView.this.radioList.size(); i4++) {
                        RadioButton radioButton2 = (RadioButton) SelectDialogView.this.radioList.get(i4);
                        if (radioButton2.isChecked()) {
                            SelectDialogView.this.selectStr = (String) radioButton2.getText();
                            i3 = i4;
                        }
                    }
                    SelectDialogView.this.listener.onPositiveClick(i3, SelectDialogView.this.selectStr, SelectDialogView.this.extra);
                }
            }
        });
        positiveButton.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: dialog.SelectDialogView.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                SelectDialogView.this.dismiss();
                if (SelectDialogView.this.listener != null) {
                    SelectDialogView.this.listener.onNegativeClick();
                }
            }
        });
        AlertDialog alertDialogCreate = positiveButton.create();
        alertDialogCreate.setCanceledOnTouchOutside(this.isAllowCancel);
        alertDialogCreate.setCancelable(this.isAllowCancel);
        alertDialogCreate.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: dialog.SelectDialogView.3
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i2, KeyEvent keyEvent) {
                return i2 == 4;
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

    public void setSelectStr(String str) {
        this.selectStr = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public T getExtra() {
        return this.extra;
    }

    public void setExtra(T t) {
        this.extra = t;
    }

    private int dp2Px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public ArrayList<RadioButton> getRadioList() {
        return this.radioList;
    }

    public void setRadioList(ArrayList<RadioButton> arrayList) {
        this.radioList = arrayList;
    }

    public static class Builder {

        /* JADX INFO: renamed from: dialog, reason: collision with root package name */
        private SelectDialogView f7875dialog = new SelectDialogView();

        public Builder selectItems(String[] strArr) {
            this.f7875dialog.selectItems = strArr;
            return this;
        }

        public Builder title(String str) {
            this.f7875dialog.title = str;
            return this;
        }

        public Builder isAllowCancel(boolean z) {
            this.f7875dialog.isAllowCancel = z;
            return this;
        }

        public SelectDialogView build() {
            return this.f7875dialog;
        }
    }
}
