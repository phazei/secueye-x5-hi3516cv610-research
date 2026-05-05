package com.alibaba.sdk.android.openaccount.ui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.ui.R;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.ui.ui.fragment.CheckCodeFragment;
import com.alibaba.sdk.android.openaccount.ui.ui.fragment.ConfirmRegisterFragment;
import com.alibaba.sdk.android.openaccount.ui.ui.fragment.MobileFragment;
import com.alibaba.sdk.android.openaccount.ui.ui.fragment.MobileRegisterFragment;
import com.alibaba.sdk.android.openaccount.ui.ui.fragment.PwdLoginFragment;
import com.alibaba.sdk.android.openaccount.ui.ui.fragment.ResetPwdFragment;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;

/* JADX INFO: loaded from: classes.dex */
public class SpecialLoginActivity extends BaseAppCompatActivity {
    public static final String CHECKCODE_FRAGMENT = "checkcode_fragment";
    public static final String MOBILE_FRAGMENT = "mobile_fragment";
    public static final String MOBILE_REGISTER_FRAGMENT = "mobile_register_fragment";
    public static final String PWD_LOGIN_FRAGMENT = "pwd_login_fragment";
    public static final String REGISTER_CONFIRM_FRAGMENT = "register_confirm_fragment";
    public static final String RESET_PWD_FRAGMENT = "reset_pwd_fragment";
    private Fragment mCurrentFragment;
    protected FragmentManager mFragmentManager;

    protected boolean needTickTock() {
        return false;
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mFragmentManager = getSupportFragmentManager();
        addClock();
        openFragmentByScene(getIntent());
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.alibaba.sdk.android.openaccount.ui.ui.SpecialLoginActivity$1] */
    private void addClock() {
        if (needTickTock()) {
            new CountDownTimer(60000L, 1000L) { // from class: com.alibaba.sdk.android.openaccount.ui.ui.SpecialLoginActivity.1
                @Override // android.os.CountDownTimer
                public void onTick(long j) {
                    int i = (int) (j / 1000);
                    if (SpecialLoginActivity.this.mClockView != null) {
                        SpecialLoginActivity.this.mClockView.setText(i + " s");
                    }
                }

                @Override // android.os.CountDownTimer
                public void onFinish() {
                    SpecialLoginActivity.this.finishCurrentActivity();
                }
            }.start();
        } else if (this.mClockView != null) {
            this.mClockView.setVisibility(8);
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity
    protected int getLayoutContent() {
        return R.layout.aliuser_activity_frame_content;
    }

    private void openFragmentByScene(Intent intent) {
        Bundle extras;
        int intExtra = 0;
        if (intent != null) {
            intExtra = intent.getIntExtra("scene", 0);
            extras = intent.getExtras();
        } else {
            extras = null;
        }
        if (intExtra == 2) {
            goMobileRegister(extras);
        } else {
            goMobile(extras);
        }
    }

    public void goPwd(Bundle bundle) {
        Fragment pwdLoginFragment = new PwdLoginFragment();
        if (ConfigManager.getInstance().getPwdLoginFragment() != null) {
            try {
                pwdLoginFragment = (Fragment) ConfigManager.getInstance().getPwdLoginFragment().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        toNextFragment(bundle, pwdLoginFragment, PWD_LOGIN_FRAGMENT);
    }

    public void goFindPwd(Bundle bundle) {
        Fragment resetPwdFragment = new ResetPwdFragment();
        if (ConfigManager.getInstance().getResetPwdFragment() != null) {
            try {
                resetPwdFragment = (Fragment) ConfigManager.getInstance().getResetPwdFragment().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        toNextFragment(bundle, resetPwdFragment, RESET_PWD_FRAGMENT);
    }

    public void goMobile(Bundle bundle) {
        if (ConfigManager.getInstance().getMobileFragment() != null) {
            OpenCustomLoginFragment(bundle);
        } else {
            setDefaultLoginFragment(bundle);
        }
    }

    public void goMobileRegister(Bundle bundle) {
        if (ConfigManager.getInstance().getMobileRegisterFragment() != null) {
            try {
                toNextFragment(bundle, (Fragment) ConfigManager.getInstance().getMobileRegisterFragment().newInstance(), MOBILE_REGISTER_FRAGMENT);
                return;
            } catch (Throwable th) {
                th.printStackTrace();
                return;
            }
        }
        toNextFragment(bundle, new MobileRegisterFragment(), MOBILE_REGISTER_FRAGMENT);
    }

    private void setDefaultLoginFragment(Bundle bundle) {
        toNextFragment(bundle, new MobileFragment(), MOBILE_FRAGMENT);
    }

    private void OpenCustomLoginFragment(Bundle bundle) {
        try {
            toNextFragment(bundle, (Fragment) ConfigManager.getInstance().getMobileFragment().newInstance(), MOBILE_FRAGMENT);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void jumpToCheckCode(Bundle bundle) {
        if (ConfigManager.getInstance().getCheckCodeFragment() != null) {
            OpenCustomCheckCode(bundle);
        } else {
            openCheckCodeFragment(bundle);
        }
    }

    public void jumpToRegisterConfirm(Bundle bundle) {
        Class confirmFragment = ConfirmRegisterFragment.class;
        if (ConfigManager.getInstance().getConfirmFragment() != null) {
            confirmFragment = ConfigManager.getInstance().getConfirmFragment();
        }
        try {
            toNextFragment(bundle, confirmFragment.newInstance(), REGISTER_CONFIRM_FRAGMENT);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void OpenCustomCheckCode(Bundle bundle) {
        try {
            toNextFragment(bundle, (Fragment) ConfigManager.getInstance().getCheckCodeFragment().newInstance(), CHECKCODE_FRAGMENT);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void openCheckCodeFragment(Bundle bundle) {
        toNextFragment(bundle, new CheckCodeFragment(), CHECKCODE_FRAGMENT);
    }

    private void toNextFragment(Bundle bundle, Fragment fragment2, String str) {
        Fragment fragmentFindFragmentByTag = this.mFragmentManager.findFragmentByTag(str);
        if (fragmentFindFragmentByTag != null) {
            this.mFragmentManager.beginTransaction().remove(fragmentFindFragmentByTag).commitAllowingStateLoss();
        }
        fragment2.setArguments(bundle);
        FragmentTransaction fragmentTransactionBeginTransaction = this.mFragmentManager.beginTransaction();
        fragmentTransactionBeginTransaction.replace(R.id.aliuser_content_frame, fragment2, str);
        fragmentTransactionBeginTransaction.addToBackStack(null);
        try {
            fragmentTransactionBeginTransaction.commitAllowingStateLoss();
            this.mCurrentFragment = fragment2;
        } catch (Throwable unused) {
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity
    public void finishCurrentActivity() {
        if (getLoginCallback() != null) {
            CommonUtils.onFailure(getLoginCallback(), MessageUtils.createMessage(10003, new Object[0]));
        }
        finish();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.mFragmentManager.getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finishCurrentActivity();
        }
    }

    protected LoginCallback getLoginCallback() {
        if (OpenAccountUIServiceImpl._specialLoginCallback != null) {
            return OpenAccountUIServiceImpl._specialLoginCallback;
        }
        return null;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Fragment fragmentFindFragmentByTag = this.mFragmentManager.findFragmentByTag(CHECKCODE_FRAGMENT);
        Fragment fragmentFindFragmentByTag2 = this.mFragmentManager.findFragmentByTag(PWD_LOGIN_FRAGMENT);
        Fragment fragmentFindFragmentByTag3 = this.mFragmentManager.findFragmentByTag(MOBILE_FRAGMENT);
        Fragment fragmentFindFragmentByTag4 = this.mFragmentManager.findFragmentByTag(MOBILE_REGISTER_FRAGMENT);
        Fragment fragmentFindFragmentByTag5 = this.mFragmentManager.findFragmentByTag(REGISTER_CONFIRM_FRAGMENT);
        if (fragmentFindFragmentByTag != null) {
            fragmentFindFragmentByTag.onActivityResult(i, i2, intent);
            return;
        }
        if (fragmentFindFragmentByTag2 != null) {
            fragmentFindFragmentByTag2.onActivityResult(i, i2, intent);
            return;
        }
        if (fragmentFindFragmentByTag3 != null) {
            fragmentFindFragmentByTag3.onActivityResult(i, i2, intent);
        } else if (fragmentFindFragmentByTag4 != null) {
            fragmentFindFragmentByTag4.onActivityResult(i, i2, intent);
        } else if (fragmentFindFragmentByTag5 != null) {
            fragmentFindFragmentByTag5.onActivityResult(i, i2, intent);
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        OpenAccountUIServiceImpl._specialLoginCallback = null;
    }
}
