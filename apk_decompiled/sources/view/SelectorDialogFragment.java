package view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.seculink.app.R;
import java.lang.reflect.Field;
import java.util.List;
import tools.ScreenUtil;

/* JADX INFO: loaded from: classes5.dex */
public class SelectorDialogFragment extends DialogFragment implements View.OnClickListener {
    private int curPosition;
    private boolean isCenter;
    private boolean isDoubleNet;
    private String[] items;
    private OnItemClickListener onItemClickListener;
    private String title;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setStyle(1, R.style.window_background);
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.dialog_selector, viewGroup, false);
    }

    public SelectorDialogFragment() {
        this.curPosition = -1;
        this.isDoubleNet = false;
        this.isCenter = false;
    }

    @SuppressLint({"ValidFragment"})
    public SelectorDialogFragment(String str, String... strArr) {
        this.curPosition = -1;
        this.isDoubleNet = false;
        this.isCenter = false;
        this.title = str;
        this.items = strArr;
    }

    @SuppressLint({"ValidFragment"})
    public SelectorDialogFragment(String str, List<String> list) {
        this.curPosition = -1;
        this.isDoubleNet = false;
        this.isCenter = false;
        this.title = str;
        this.items = (String[]) list.toArray(new String[0]);
    }

    @SuppressLint({"ValidFragment"})
    public SelectorDialogFragment(String str, boolean z, String... strArr) {
        this.curPosition = -1;
        this.isDoubleNet = false;
        this.isCenter = false;
        this.title = str;
        this.items = strArr;
        this.isDoubleNet = z;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view2, @Nullable Bundle bundle) {
        super.onViewCreated(view2, bundle);
        ((TextView) view2.findViewById(R.id.tv_title)).setText(this.title);
        TextView textView = (TextView) view2.findViewById(R.id.btn_cancel);
        textView.setText(R.string.cancel);
        textView.setOnClickListener(this);
        if (this.isCenter) {
            textView.setVisibility(8);
        }
        LinearLayout linearLayout = (LinearLayout) view2.findViewById(R.id.ll_item_view);
        if (this.items != null) {
            for (int i = 0; i < this.items.length; i++) {
                final TextView textView2 = new TextView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                textView2.setText(this.items[i]);
                textView2.setGravity(17);
                textView2.setTextSize(16.0f);
                int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R.dimen.dimen_15);
                if (this.isDoubleNet && i == 0) {
                    textView2.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, getContext().getResources().getDimensionPixelSize(R.dimen.dimen_3));
                } else {
                    textView2.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
                }
                int i2 = this.curPosition;
                if (i2 >= 0 && i2 == i) {
                    textView2.setTextColor(getContext().getResources().getColor(R.color.color_ff0000));
                } else {
                    textView2.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                }
                textView2.setTag(Integer.valueOf(i));
                textView2.setOnClickListener(new View.OnClickListener() { // from class: view.SelectorDialogFragment.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view3) {
                        SelectorDialogFragment.this.onItemClickListener.onItemClick(((Integer) textView2.getTag()).intValue());
                        SelectorDialogFragment.this.dismissAllowingStateLoss();
                    }
                });
                linearLayout.addView(textView2, layoutParams);
                if (this.isDoubleNet && i == 0) {
                    TextView textView3 = new TextView(getContext());
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, -2);
                    textView3.setText(R.string.wifi_first);
                    textView3.setGravity(17);
                    textView3.setTextSize(14.0f);
                    textView3.setTextColor(getContext().getResources().getColor(R.color.color_gray));
                    textView3.setPadding(dimensionPixelSize, 0, dimensionPixelSize, dimensionPixelSize);
                    linearLayout.addView(textView3, layoutParams2);
                }
                if (i < this.items.length - 1) {
                    View view3 = new View(getContext());
                    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, ScreenUtil.dp2Px(getContext(), 1.0f));
                    view3.setBackgroundColor(getContext().getResources().getColor(R.color.color_F6F6F6));
                    linearLayout.addView(view3, layoutParams3);
                }
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (this.isCenter) {
            attributes.gravity = 17;
        } else {
            attributes.gravity = 80;
        }
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

    public void showAllowingStateLoss(FragmentManager fragmentManager, String str, boolean z) {
        if (isAdded()) {
            return;
        }
        this.isCenter = z;
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

    public void showAllowingStateLoss(FragmentManager fragmentManager, String str, int i) {
        if (isAdded()) {
            return;
        }
        this.curPosition = i;
        setFiled("mDismissed", this, false);
        setFiled("mShownByMe", this, true);
        FragmentTransaction fragmentTransactionBeginTransaction = fragmentManager.beginTransaction();
        fragmentTransactionBeginTransaction.add(this, str);
        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.btn_cancel) {
            dismissAllowingStateLoss();
        }
    }

    public String getItemText(int i) {
        return this.items[i];
    }
}
