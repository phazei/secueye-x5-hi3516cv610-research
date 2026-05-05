package fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import tools.ProgressDialogUtil;

/* JADX INFO: loaded from: classes4.dex */
public abstract class CommonFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName();
    private boolean mIsFirst = true;
    protected View mRootView;
    private ProgressDialogUtil progressDialogUtil;

    protected abstract int getContentLayoutId();

    protected void initArgs(Bundle bundle) {
    }

    protected void initData() {
    }

    protected void initWidget(View view2) {
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void onFirstInitData() {
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        this.progressDialogUtil = new ProgressDialogUtil();
        View view2 = this.mRootView;
        if (view2 == null) {
            this.mRootView = layoutInflater.inflate(getContentLayoutId(), viewGroup, false);
            initWidget(this.mRootView);
        } else if (view2.getParent() != null) {
            ((ViewGroup) this.mRootView.getParent()).removeView(this.mRootView);
        }
        return this.mRootView;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view2, @Nullable Bundle bundle) {
        super.onViewCreated(view2, bundle);
        if (this.mIsFirst) {
            this.mIsFirst = false;
            onFirstInitData();
        }
        initData();
    }

    protected void showProgressDialog(int i) {
        showProgressDialog(getString(i));
    }

    protected void showProgressDialog(String str) {
        this.progressDialogUtil.showProgressDialog(getActivity(), str);
    }

    protected void showProgressDialog() {
        this.progressDialogUtil.showProgressDialog(getActivity());
    }

    protected void dismissProgressDialog() {
        this.progressDialogUtil.dismissProgressDialog(getActivity());
    }

    public void setBackgroundAlpha(float f) {
        WindowManager.LayoutParams attributes = getActivity().getWindow().getAttributes();
        attributes.alpha = f;
        getActivity().getWindow().setAttributes(attributes);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }
}
