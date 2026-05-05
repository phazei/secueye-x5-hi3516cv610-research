package activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import bean.SwitchLanguageBean;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OauthService;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConfigs;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService;
import com.alibaba.sdk.android.openaccount.ui.callback.EmailRegisterCallback;
import com.alibaba.sdk.android.openaccount.ui.callback.EmailResetPasswordCallback;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.ui.model.SmsActionType;
import com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener;
import com.alibaba.sdk.android.openaccount.ui.widget.NonMultiClickListener;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.seculink.app.R;
import config.AppConfig;
import dialog.RegisterSelectorDialogFragment;
import dialog.ResetSelectorDialogFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import sdk.DemoApplication;
import tools.ActivityManager;
import tools.LanguageUtil;
import tools.OnMultiClickListener;
import tools.ProgressDialogUtil;
import tools.StatusBarUtil;
import tools.SystemUtil;
import tools.ToastUtils;
import tools.Utils;

/* JADX INFO: loaded from: classes.dex */
public class OALoginActivity extends LoginActivity implements View.OnClickListener {
    private TextView agreeTextView;
    private CheckBox checkBox;
    private ImageView iv_google;
    private RelativeLayout permission_ll;
    private TextView privacyTextView;
    private ProgressDialogUtil progressDialogUtil;
    private RegisterSelectorDialogFragment registerSelectorDialogFragment;
    private ResetSelectorDialogFragment resetSelectorDialogFragment;
    public String saveLoginID;
    private TextView switch_login;
    private ImageView switch_login_image;
    private TextView tvArea_toast;
    private TextView tv_registers;
    private TextView userTextView;
    private boolean isAgree = false;
    private View.OnClickListener registerListenr = new View.OnClickListener() { // from class: activity.OALoginActivity.12
        @Override // android.view.View.OnClickListener
        public void onClick(final View view2) {
            if (AppConfig.isChina) {
                View viewInflate = View.inflate(OALoginActivity.this, R.layout.permission_view, null);
                final AlertDialog alertDialogCreate = new AlertDialog.Builder(OALoginActivity.this).setView(viewInflate).create();
                alertDialogCreate.setCanceledOnTouchOutside(false);
                alertDialogCreate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialogCreate.show();
                int i = OALoginActivity.this.getResources().getDisplayMetrics().widthPixels;
                WindowManager.LayoutParams attributes = alertDialogCreate.getWindow().getAttributes();
                attributes.width = (int) (((double) i) * 0.85d);
                alertDialogCreate.getWindow().setAttributes(attributes);
                ((Button) viewInflate.findViewById(R.id.btn_left)).setOnClickListener(new View.OnClickListener() { // from class: activity.OALoginActivity.12.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view3) {
                        alertDialogCreate.dismiss();
                        OALoginActivity.this.registerSelectorDialogFragment.dismissAllowingStateLoss();
                    }
                });
                ((Button) viewInflate.findViewById(R.id.btn_right)).setOnClickListener(new View.OnClickListener() { // from class: activity.OALoginActivity.12.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view3) {
                        if (view2.getId() == R.id.btn_register_phone) {
                            ((OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class)).showRegister(OALoginActivity.this, OALoginActivity.this.getRegisterLoginCallback());
                            OALoginActivity.this.registerSelectorDialogFragment.dismissAllowingStateLoss();
                            alertDialogCreate.dismiss();
                        } else if (view2.getId() == R.id.btn_register_email) {
                            ((OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class)).showEmailRegister(OALoginActivity.this, OALoginActivity.this.getEmailRegisterCallback());
                            OALoginActivity.this.registerSelectorDialogFragment.dismissAllowingStateLoss();
                            alertDialogCreate.dismiss();
                        }
                    }
                });
                return;
            }
            if (view2.getId() == R.id.btn_register_phone) {
                OpenAccountUIService openAccountUIService = (OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class);
                OALoginActivity oALoginActivity = OALoginActivity.this;
                openAccountUIService.showRegister(oALoginActivity, oALoginActivity.getRegisterLoginCallback());
                OALoginActivity.this.registerSelectorDialogFragment.dismissAllowingStateLoss();
                return;
            }
            if (view2.getId() == R.id.btn_register_email) {
                OpenAccountUIService openAccountUIService2 = (OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class);
                OALoginActivity oALoginActivity2 = OALoginActivity.this;
                openAccountUIService2.showEmailRegister(oALoginActivity2, oALoginActivity2.getEmailRegisterCallback());
                OALoginActivity.this.registerSelectorDialogFragment.dismissAllowingStateLoss();
            }
        }
    };
    private View.OnClickListener resetListenr = new View.OnClickListener() { // from class: activity.OALoginActivity.13
        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            if (view2.getId() == R.id.btn_register_phone) {
                OALoginActivity.this.forgetPhonePassword(view2);
                OALoginActivity.this.resetSelectorDialogFragment.dismissAllowingStateLoss();
            } else if (view2.getId() == R.id.btn_register_email) {
                OALoginActivity.this.forgetMailPassword(view2);
                OALoginActivity.this.resetSelectorDialogFragment.dismissAllowingStateLoss();
            }
        }
    };

    @Override // activity.LoginActivity, com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_login2";
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LanguageUtil.attachBaseContext(context));
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate, androidx.fragment.app.FragmentActivity, android.app.Activity
    @SuppressLint({"SetTextI18n"})
    protected void onResume() {
        super.onResume();
        if (this.saveLoginID != null) {
            this.loginIdEdit.getInputBoxWithClear().getEditText().setText(this.saveLoginID);
            if (this.saveLoginID.length() > 0 && this.passwordEdit.getInputBoxWithClear().getEditText().length() > 0) {
                this.next.setEnabled(true);
            } else {
                this.next.setEnabled(false);
            }
            this.saveLoginID = null;
        }
        if (ConfigManager.getInstance().isSupportOfflineLogin()) {
            this.next.setOnClickListener(new NonMultiClickListener() { // from class: activity.OALoginActivity.1
                @Override // com.alibaba.sdk.android.openaccount.ui.widget.NonMultiClickListener
                public void onMonMultiClick(View view2) {
                    OALoginActivity.this.userLogin(view2);
                }
            });
        } else {
            this.next.setOnClickListener(new NetworkCheckOnClickListener() { // from class: activity.OALoginActivity.2
                @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
                public void afterCheck(View view2) {
                    OALoginActivity.this.userLogin(view2);
                }
            });
        }
        OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers = !SystemUtil.isEmail(this.loginIdEdit.getEditText().getText().toString());
        switchLoginMode();
        if (GlobalConfig.getInstance().getCountry() == null || "".equals(GlobalConfig.getInstance().getCountry().areaName)) {
            this.tvArea_toast.setText(getResources().getString(R.string.china_name));
            return;
        }
        this.tvArea_toast.setText("" + GlobalConfig.getInstance().getCountry().areaName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void userLogin(View view2) {
        if (this.isAgree || !AppConfig.isChina) {
            login(view2);
            return;
        }
        this.permission_ll.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        ToastUtils.showToast(this, getString(R.string.agree_login));
    }

    @Override // activity.LoginActivity, com.alibaba.sdk.android.openaccount.ui.ui.NextStepActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        EventBus.getDefault().register(this);
        OpenAccountUIConfigs.MobileRegisterFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.OneStepMobileRegisterFlow.supportForeignMobileNumbers = true;
        OpenAccountUIConfigs.MobileResetPasswordLoginFlow.supportForeignMobileNumbers = true;
        setRequestedOrientation(1);
        if (bundle != null) {
            this.saveLoginID = bundle.getString("login_id");
        }
        super.onCreate(bundle);
        this.progressDialogUtil = new ProgressDialogUtil();
        this.tv_registers = (TextView) findViewById(R.id.tv_registers);
        ((TextView) findViewById("open_history")).setVisibility(8);
        this.switch_login = (TextView) findViewById(R.id.switch_login);
        this.switch_login_image = (ImageView) findViewById(R.id.switch_login_image);
        this.iv_google = (ImageView) findViewById(R.id.iv_google);
        this.permission_ll = (RelativeLayout) findViewById(R.id.permission_ll);
        this.tvArea_toast = (TextView) findViewById(R.id.tv_area_toast);
        this.checkBox = (CheckBox) findViewById(R.id.check_permission);
        ActivityManager.getInstance().push(this);
        initLoginCallBack();
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setLightStatusBar(this, true);
        if (!AppConfig.isChina) {
            this.permission_ll.setVisibility(8);
        }
        this.registerSelectorDialogFragment = new RegisterSelectorDialogFragment();
        this.registerSelectorDialogFragment.setOnClickListener(this.registerListenr);
        this.resetSelectorDialogFragment = new ResetSelectorDialogFragment();
        this.resetSelectorDialogFragment.setOnClickListener(this.resetListenr);
        this.resetPasswordTV = (TextView) findViewById(ResourceUtils.getRId(this, SmsActionType.RESET_PASSWORD));
        if (this.resetPasswordTV != null) {
            this.resetPasswordTV.setOnClickListener(new View.OnClickListener() { // from class: activity.OALoginActivity.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    if (OALoginActivity.this.resetSelectorDialogFragment.isAdded()) {
                        return;
                    }
                    OALoginActivity.this.resetSelectorDialogFragment.showAllowingStateLoss(OALoginActivity.this.getSupportFragmentManager(), "");
                }
            });
        }
        this.tv_registers.setOnClickListener(new OnMultiClickListener() { // from class: activity.OALoginActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (OALoginActivity.this.registerSelectorDialogFragment.isAdded()) {
                    return;
                }
                if (OALoginActivity.this.isAgree || !AppConfig.isChina) {
                    OALoginActivity.this.registerSelectorDialogFragment.showAllowingStateLoss(OALoginActivity.this.getSupportFragmentManager(), "");
                    return;
                }
                OALoginActivity.this.permission_ll.startAnimation(AnimationUtils.loadAnimation(OALoginActivity.this, R.anim.shake));
                OALoginActivity oALoginActivity = OALoginActivity.this;
                ToastUtils.showToast(oALoginActivity, oALoginActivity.getString(R.string.agree_register));
            }
        });
        this.mToolBar.setVisibility(8);
        findViewById(R.id.main).setBackgroundDrawable(null);
        if (LoginBusiness.isLogin()) {
            ((DemoApplication) DemoApplication.getInstance()).startPushAndConnect();
            Intent intent = new Intent(DemoApplication.getInstance(), (Class<?>) MainActivity.class);
            intent.setFlags(268468224);
            DemoApplication.getInstance().startActivity(intent);
        }
        this.tvArea_toast.setOnClickListener(new View.OnClickListener() { // from class: activity.OALoginActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent2 = new Intent(OALoginActivity.this, (Class<?>) CountryListActivity.class);
                intent2.putExtra("type", "1");
                OALoginActivity.this.startActivity(intent2);
            }
        });
        if (GlobalConfig.getInstance().getCountry() == null || "".equals(GlobalConfig.getInstance().getCountry().areaName)) {
            this.tvArea_toast.setText(getResources().getString(R.string.china_name));
        } else {
            this.tvArea_toast.setText("" + GlobalConfig.getInstance().getCountry().areaName);
        }
        this.switch_login_image.setOnClickListener(new View.OnClickListener() { // from class: activity.OALoginActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                OALoginActivity.this.loginIdEdit.getEditText().setText("");
                OALoginActivity.this.passwordEdit.getEditText().setText("");
                OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers = !OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers;
                OALoginActivity.this.switchLoginMode();
            }
        });
        this.iv_google.setOnClickListener(new NonMultiClickListener() { // from class: activity.OALoginActivity.7
            @Override // com.alibaba.sdk.android.openaccount.ui.widget.NonMultiClickListener
            public void onMonMultiClick(View view2) {
                Log.e("国家地区", GlobalConfig.getInstance().getCountry().areaName);
                LoginBusiness.oauthLogin(OALoginActivity.this, new ILoginCallback() { // from class: activity.OALoginActivity.7.1
                    @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
                    public void onLoginSuccess() {
                        Log.e("谷歌", "  成功");
                    }

                    @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
                    public void onLoginFailed(int i, String str) {
                        Log.e("谷歌", i + "  " + str);
                        ToastUtils.showToast(OALoginActivity.this, "" + i + "  " + str);
                    }
                });
            }
        });
        this.switch_login.setOnClickListener(new View.OnClickListener() { // from class: activity.OALoginActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                OALoginActivity.this.loginIdEdit.getEditText().setText("");
                OALoginActivity.this.passwordEdit.getEditText().setText("");
                OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers = !OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers;
                OALoginActivity.this.switchLoginMode();
            }
        });
        ((TextView) findViewById("edt_chosed_country_num")).setText(Utils.getAreaCode(this));
        this.agreeTextView = (TextView) findViewById(R.id.agree_text);
        String string = getResources().getString(R.string.agree_text);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(Html.fromHtml(string));
        ClickableSpan clickableSpan = new ClickableSpan() { // from class: activity.OALoginActivity.9
            @Override // android.text.style.ClickableSpan
            public void onClick(View view2) {
                OALoginActivity oALoginActivity = OALoginActivity.this;
                oALoginActivity.startActivity(new Intent(oALoginActivity, (Class<?>) AboutUserAgreementActivity.class));
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);
                textPaint.setColor(Color.parseColor("#1AA0FF"));
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() { // from class: activity.OALoginActivity.10
            @Override // android.text.style.ClickableSpan
            public void onClick(@NonNull View view2) {
                OALoginActivity oALoginActivity = OALoginActivity.this;
                oALoginActivity.startActivity(new Intent(oALoginActivity, (Class<?>) PrivacyAgreementActivity.class));
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
                textPaint.setUnderlineText(false);
                textPaint.setColor(Color.parseColor("#1AA0FF"));
            }
        };
        if (AppConfig.isChina) {
            spannableStringBuilder.setSpan(clickableSpan, 8, 12, 33);
            spannableStringBuilder.setSpan(new StyleSpan(1), 8, 12, 33);
            spannableStringBuilder.setSpan(clickableSpan2, 13, Html.fromHtml(string).length(), 33);
            spannableStringBuilder.setSpan(new StyleSpan(1), 13, Html.fromHtml(string).length(), 33);
        }
        this.agreeTextView.setMovementMethod(LinkMovementMethod.getInstance());
        this.agreeTextView.setText(spannableStringBuilder);
        this.agreeTextView.setHighlightColor(Color.parseColor("#00000000"));
        this.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: activity.OALoginActivity.11
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                OALoginActivity.this.isAgree = !r1.isAgree;
                OALoginActivity.this.checkBox.setChecked(OALoginActivity.this.isAgree);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchLanguage(SwitchLanguageBean switchLanguageBean) {
        if (switchLanguageBean.isShowProgressDialog()) {
            showProgressDialog();
            return;
        }
        TextView textView = this.tvArea_toast;
        if (textView != null) {
            textView.setText("" + GlobalConfig.getInstance().getCountry().areaName);
        }
        dismissProgressDialog();
    }

    protected void showProgressDialog(int i) {
        showProgressDialog(getString(i));
    }

    protected void showProgressDialog(String str) {
        this.progressDialogUtil.showProgressDialog(this, str);
    }

    protected void showProgressDialog() {
        this.progressDialogUtil.showProgressDialog(this);
    }

    protected void dismissProgressDialog() {
        this.progressDialogUtil.dismissProgressDialog(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchLoginMode() {
        this.loginIdEdit.getInputBoxWithClear().setSupportForeignMobile(this, OpenAccountUIConfigs.AccountPasswordLoginFlow.mobileCountrySelectorActvityClazz, OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers);
        this.switch_login.setText(OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers ? R.string.email_login : R.string.phone_login);
        this.switch_login_image.setImageResource(OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers ? R.drawable.email : R.drawable.phone);
        this.loginIdEdit.getEditText().setHint(OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers ? R.string.input_phone_account : R.string.input_email_account);
        this.loginIdEdit.getEditText().setInputType(OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers ? 2 : 33);
    }

    public void forgetPhonePassword(View view2) {
        super.forgetPassword(view2);
    }

    public void forgetMailPassword(View view2) {
        ((OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class)).showEmailResetPassword(this, getEmailResetPasswordCallback());
    }

    private EmailResetPasswordCallback getEmailResetPasswordCallback() {
        return new EmailResetPasswordCallback() { // from class: activity.OALoginActivity.14
            @Override // com.alibaba.sdk.android.openaccount.callback.LoginCallback
            public void onSuccess(OpenAccountSession openAccountSession) {
                LoginCallback loginCallback = OALoginActivity.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onSuccess(openAccountSession);
                }
                OALoginActivity.this.finishWithoutCallback();
            }

            @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
            public void onFailure(int i, String str) {
                LoginCallback loginCallback = OALoginActivity.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onFailure(i, str);
                }
            }

            @Override // com.alibaba.sdk.android.openaccount.ui.callback.EmailResetPasswordCallback
            public void onEmailSent(String str) {
                Toast.makeText(OALoginActivity.this.getApplicationContext(), str + OALoginActivity.this.getResources().getString(R.string.has_sent), 1).show();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public EmailRegisterCallback getEmailRegisterCallback() {
        return new EmailRegisterCallback() { // from class: activity.OALoginActivity.15
            @Override // com.alibaba.sdk.android.openaccount.callback.LoginCallback
            public void onSuccess(OpenAccountSession openAccountSession) {
                LoginCallback loginCallback = OALoginActivity.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onSuccess(openAccountSession);
                }
                OALoginActivity.this.finishWithoutCallback();
            }

            @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
            public void onFailure(int i, String str) {
                LoginCallback loginCallback = OALoginActivity.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onFailure(i, str);
                }
            }

            @Override // com.alibaba.sdk.android.openaccount.ui.callback.EmailRegisterCallback
            public void onEmailSent(String str) {
                Toast.makeText(OALoginActivity.this.getApplicationContext(), str + OALoginActivity.this.getResources().getString(R.string.has_sent), 1).show();
            }
        };
    }

    @Override // activity.LoginActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        OauthService oauthService = (OauthService) OpenAccountSDK.getService(OauthService.class);
        if (oauthService != null) {
            oauthService.authorizeCallback(i, i2, intent);
        }
    }

    @Override // activity.LoginActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        ActivityManager.getInstance().pop(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        initLoginCallBack();
    }

    public void initLoginCallBack() {
        if (OpenAccountUIServiceImpl._loginCallback == null) {
            OpenAccountUIServiceImpl._loginCallback = new LoginCallback() { // from class: activity.OALoginActivity.16
                @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
                public void onFailure(int i, String str) {
                    if (i != 10003) {
                        Toast.makeText(DemoApplication.getInstance(), DemoApplication.getInstance().getResources().getString(R.string.login_falied_by) + str, 0).show();
                    }
                }

                @Override // com.alibaba.sdk.android.openaccount.callback.LoginCallback
                public void onSuccess(OpenAccountSession openAccountSession) {
                    OALoginActivity.this.runOnUiThread(new Runnable() { // from class: activity.OALoginActivity.16.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ((DemoApplication) DemoApplication.getInstance()).startPushAndConnect();
                            Intent intent = new Intent(DemoApplication.getInstance(), (Class<?>) MainActivity.class);
                            intent.setFlags(268468224);
                            DemoApplication.getInstance().startActivity(intent);
                        }
                    });
                }
            };
        }
    }
}
