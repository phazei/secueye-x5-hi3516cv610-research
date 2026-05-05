package dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.seculink.app.R;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes3.dex */
public class ResetSelectorDialogFragment extends DialogFragment implements View.OnClickListener {
    private View.OnClickListener onClickListener;

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setStyle(1, R.style.window_background);
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.ali_sdk_openaccount_dialog, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view2, @Nullable Bundle bundle) {
        super.onViewCreated(view2, bundle);
        ((TextView) view2.findViewById(R.id.tv_title)).setText(ResourceUtils.getString(getActivity(), "account_choose_find_pwd_style"));
        TextView textView = (TextView) view2.findViewById(R.id.btn_cancel);
        textView.setText(ResourceUtils.getString(getActivity(), "cancel"));
        textView.setOnClickListener(this);
        TextView textView2 = (TextView) view2.findViewById(R.id.btn_register_phone);
        textView2.setText(ResourceUtils.getString(getActivity(), "account_choose_find_pwd_style_phone"));
        textView2.setOnClickListener(this.onClickListener);
        TextView textView3 = (TextView) view2.findViewById(R.id.btn_register_email);
        textView3.setText(ResourceUtils.getString(getActivity(), "account_choose_find_pwd_style_email"));
        textView3.setOnClickListener(this.onClickListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = 80;
        attributes.width = getResources().getDisplayMetrics().widthPixels;
        attributes.height = -2;
        window.setAttributes(attributes);
    }

    public void showAllowingStateLoss(FragmentManager fragmentManager, String str) {
        if (isAdded()) {
            return;
        }
        setFiled("mDismissed", this, false);
        setFiled("mShownByMe", this, true);
        FragmentTransaction fragmentTransactionBeginTransaction = fragmentManager.beginTransaction();
        fragmentTransactionBeginTransaction.add(this, str);
        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
    }

    public void showAllowingStateLoss(FragmentTransaction fragmentTransaction, String str) {
        if (isAdded()) {
            return;
        }
        setFiled("mDismissed", this, false);
        setFiled("mShownByMe", this, true);
        fragmentTransaction.add(this, str);
        setFiled("mViewDestroyed", this, false);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void setFiled(String str, Object obj, Object obj2) {
        try {
            Field declaredField = obj.getClass().getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(obj, obj2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.btn_cancel) {
            dismissAllowingStateLoss();
        }
    }
}
