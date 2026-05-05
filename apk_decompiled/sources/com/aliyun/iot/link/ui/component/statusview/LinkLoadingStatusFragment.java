package com.aliyun.iot.link.ui.component.statusview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.aliyun.iot.link.ui.component.R;

/* JADX INFO: loaded from: classes2.dex */
public class LinkLoadingStatusFragment extends Fragment {
    public static final String ARGS_CANCELABLE = "cancelable";
    public static final String ARGS_ICON_COLOR = "icon_color";
    public static final String ARGS_LOADING_MESSAGE = "message";
    public static final String TAG = "1563175434";
    private View dimmedView;
    private ImageView loadingIcon;
    private TextView loadingTextView;
    private int iconTintColor = Color.parseColor("#0079ff");
    private ObjectAnimator rotateAnimator = null;

    public static void show(@NonNull FragmentActivity fragmentActivity, @StringRes int i, @IdRes int i2, @ColorInt int i3, boolean z, boolean z2) {
        Bundle bundle = new Bundle();
        if (i != 0) {
            bundle.putString("message", fragmentActivity.getString(i));
        }
        bundle.putInt(ARGS_ICON_COLOR, i3);
        bundle.putBoolean(ARGS_CANCELABLE, z);
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        Fragment fragmentFindFragmentByTag = supportFragmentManager.findFragmentByTag(TAG);
        boolean z3 = (fragmentFindFragmentByTag instanceof LinkLoadingStatusFragment) && fragmentFindFragmentByTag.isAdded();
        if (z3) {
            LinkLoadingStatusFragment linkLoadingStatusFragment = (LinkLoadingStatusFragment) fragmentFindFragmentByTag;
            linkLoadingStatusFragment.setCancelable(z);
            if (i != 0) {
                linkLoadingStatusFragment.setLoadingText(fragmentActivity.getString(i));
            } else {
                linkLoadingStatusFragment.setLoadingText(null);
            }
            if (!fragmentFindFragmentByTag.isVisible()) {
                if (fragmentFindFragmentByTag.isHidden()) {
                    supportFragmentManager.beginTransaction().show(fragmentFindFragmentByTag).commitAllowingStateLoss();
                } else if (fragmentFindFragmentByTag.getView() != null && fragmentFindFragmentByTag.getView().getWindowToken() != null) {
                    fragmentFindFragmentByTag.getView().setVisibility(0);
                } else {
                    supportFragmentManager.beginTransaction().remove(fragmentFindFragmentByTag).commitAllowingStateLoss();
                    z3 = false;
                }
            }
        }
        if (z3) {
            return;
        }
        LinkLoadingStatusFragment linkLoadingStatusFragment2 = new LinkLoadingStatusFragment();
        linkLoadingStatusFragment2.setArguments(bundle);
        FragmentTransaction fragmentTransactionReplace = supportFragmentManager.beginTransaction().replace(i2, linkLoadingStatusFragment2, TAG);
        fragmentTransactionReplace.disallowAddToBackStack();
        if (z2) {
            fragmentTransactionReplace.commitAllowingStateLoss();
        } else {
            fragmentTransactionReplace.commit();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.link_loading_status_fragment, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view2, @Nullable Bundle bundle) {
        super.onViewCreated(view2, bundle);
        Log.d(TAG, "onViewCreated: ");
        assignViews(view2);
        setCancelable(getArguments().getBoolean(ARGS_CANCELABLE, false));
        setLoadingText(getArguments().getString("message"));
        if (Build.VERSION.SDK_INT >= 21) {
            this.loadingIcon.setImageTintList(ColorStateList.valueOf(getArguments().getInt(ARGS_ICON_COLOR)));
        }
        startLoadingAnimation();
    }

    private void assignViews(@Nullable View view2) {
        if (view2 == null) {
            return;
        }
        this.dimmedView = view2.findViewById(R.id.link_status_loading_bg);
        this.loadingIcon = (ImageView) view2.findViewById(R.id.link_status_loading_icon);
        this.loadingTextView = (TextView) view2.findViewById(R.id.link_status_loading_text);
    }

    public void setLoadingText(String str) {
        if (this.loadingTextView == null && getView() != null) {
            assignViews(getView());
        }
        if (this.loadingTextView != null) {
            if (TextUtils.isEmpty(str)) {
                this.loadingTextView.setVisibility(8);
            } else {
                this.loadingTextView.setText(str);
            }
        }
    }

    public void setCancelable(final boolean z) {
        if (this.loadingTextView == null && getView() != null) {
            assignViews(getView());
        }
        View view2 = this.dimmedView;
        if (view2 != null) {
            view2.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.link.ui.component.statusview.LinkLoadingStatusFragment.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    if (!z || LinkLoadingStatusFragment.this.getActivity() == null) {
                        return;
                    }
                    LinkLoadingStatusFragment.this.getActivity().getSupportFragmentManager().beginTransaction().remove(LinkLoadingStatusFragment.this).setTransition(4099).commitAllowingStateLoss();
                }
            });
        }
    }

    public void startLoadingAnimation() {
        if (getView() == null || this.loadingIcon == null) {
            return;
        }
        Log.d(TAG, "startLoadingAnimation: ");
        ObjectAnimator objectAnimator = this.rotateAnimator;
        if (objectAnimator == null) {
            this.rotateAnimator = ObjectAnimator.ofFloat(this.loadingIcon, (Property<ImageView, Float>) View.ROTATION, 0.0f, 720.0f);
            this.rotateAnimator.setDuration(1000L);
            this.rotateAnimator.setInterpolator(new LinearInterpolator());
            this.rotateAnimator.setRepeatCount(-1);
        } else {
            objectAnimator.end();
        }
        this.rotateAnimator.start();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        ObjectAnimator objectAnimator;
        super.onPause();
        Log.d(TAG, "onPause: ");
        if (Build.VERSION.SDK_INT < 19 || (objectAnimator = this.rotateAnimator) == null || !objectAnimator.isRunning()) {
            return;
        }
        this.rotateAnimator.pause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        ObjectAnimator objectAnimator;
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (Build.VERSION.SDK_INT < 19 || (objectAnimator = this.rotateAnimator) == null || !objectAnimator.isPaused()) {
            return;
        }
        this.rotateAnimator.resume();
    }

    public void cancelLoadingAnimation() {
        if (getView() == null) {
            return;
        }
        Log.d(TAG, "cancelLoadingAnimation: ");
        ObjectAnimator objectAnimator = this.rotateAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        startLoadingAnimation();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
        cancelLoadingAnimation();
    }
}
