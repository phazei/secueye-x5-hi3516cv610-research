package com.alibaba.sdk.android.openaccount.ui.widget;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import com.alibaba.sdk.android.openaccount.ui.helper.ITextChangeListner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class NextStepButtonWatcher implements TextWatcher, CompoundButton.OnCheckedChangeListener {
    protected Button button;
    protected List<ComponentStatusWatcher> componentStatusWatchers;
    protected List<CompoundButton> compoundButtons;
    protected List<EditText> editTexts = new ArrayList(5);
    private ITextChangeListner mListner;
    protected EditText mPasswordInputBox;

    public interface ComponentStatusWatcher {
        boolean isDone();
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public NextStepButtonWatcher(Button button) {
        this.button = button;
    }

    public NextStepButtonWatcher() {
    }

    public void addEditText(EditText editText) {
        if (editText == null) {
            return;
        }
        this.editTexts.add(editText);
    }

    public void addEditTexts(EditText... editTextArr) {
        if (editTextArr != null) {
            for (EditText editText : editTextArr) {
                if (editText != null) {
                    this.editTexts.add(editText);
                }
            }
        }
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        ITextChangeListner iTextChangeListner;
        refreshNextStepButtonStatus();
        EditText editText = this.mPasswordInputBox;
        if (editText == null || (iTextChangeListner = this.mListner) == null) {
            return;
        }
        iTextChangeListner.onTextChange(editText.getText().toString());
    }

    public void setNextStepButton(Button button) {
        this.button = button;
    }

    public void refreshNextStepButtonStatus() {
        List<ComponentStatusWatcher> list;
        List<CompoundButton> list2;
        if (this.button == null) {
            return;
        }
        boolean z = true;
        boolean z2 = true;
        for (EditText editText : this.editTexts) {
            if (editText.isShown()) {
                if (TextUtils.isEmpty(editText.getText())) {
                    z = false;
                }
                z2 = false;
            }
        }
        if (z && (list2 = this.compoundButtons) != null) {
            for (CompoundButton compoundButton : list2) {
                if (compoundButton.isShown()) {
                    if (!compoundButton.isChecked()) {
                        z = false;
                    }
                    z2 = false;
                }
            }
        }
        if (z && (list = this.componentStatusWatchers) != null) {
            Iterator<ComponentStatusWatcher> it = list.iterator();
            while (it.hasNext()) {
                if (!it.next().isDone()) {
                    z = false;
                }
            }
        }
        if (z && !z2) {
            this.button.setEnabled(true);
        } else {
            this.button.setEnabled(false);
        }
    }

    public boolean addComponentStatusWatcher(ComponentStatusWatcher componentStatusWatcher) {
        if (this.componentStatusWatchers == null) {
            this.componentStatusWatchers = new ArrayList(2);
        }
        return this.componentStatusWatchers.add(componentStatusWatcher);
    }

    public void setPasswordInputBox(EditText editText) {
        this.mPasswordInputBox = editText;
    }

    public void setPassWordChangeListner(ITextChangeListner iTextChangeListner) {
        this.mListner = iTextChangeListner;
    }
}
