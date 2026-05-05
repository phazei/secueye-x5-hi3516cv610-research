package com.alibaba.sdk.android.oauth.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.alibaba.sdk.android.oauth.AppCredential;
import com.alibaba.sdk.android.oauth.LoginByOauthRequest;
import com.alibaba.sdk.android.oauth.LoginByOauthTask;
import com.alibaba.sdk.android.oauth.OauthServiceProvider;
import com.alibaba.sdk.android.oauth.callback.OABindCallback;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.core.R;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class GoogleOauthServiceProviderImpl implements OauthServiceProvider, EnvironmentChangeListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 9001;
    public static final String TAG = "oa_google";
    public static OABindCallback bindCallback;
    public static LoginCallback mLoginCallback;
    private GoogleSignInOptions gso;
    private Activity mActivity;
    private GoogleApiClient mGoogleApiClient;
    private int oauthPlatform = -1;
    private OauthServiceProvider oauthServiceProvider;

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void bind(Activity activity2, int i, AppCredential appCredential, OABindCallback oABindCallback) {
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void cleanUp() {
    }

    @Override // com.google.android.gms.common.api.internal.OnConnectionFailedListener
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override // com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener
    public void onEnvironmentChange(Environment environment, Environment environment2) {
    }

    public void init(Context context) {
        this.oauthServiceProvider = this;
        AliSDKLogger.d(TAG, "GoogleOauthServiceProviderImpl init done!");
    }

    public static LoginCallback getLoginCallback() {
        return mLoginCallback;
    }

    public static void setLoginCallback(LoginCallback loginCallback) {
        mLoginCallback = loginCallback;
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void oauth(Activity activity2, int i, AppCredential appCredential, LoginCallback loginCallback) {
        if (loginCallback == null) {
            return;
        }
        mLoginCallback = loginCallback;
        if (TextUtils.isEmpty(ConfigManager.getInstance().getGoogleClientId())) {
            loginCallback.onFailure(-5, "Error: set google client ID first!");
            return;
        }
        if (activity2 == null) {
            return;
        }
        if (!(activity2 instanceof FragmentActivity)) {
            loginCallback.onFailure(-1, activity2.getString(R.string.com_alibaba_sdk_android_openaccount_google_activity_invalid));
            return;
        }
        this.mActivity = activity2;
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity2) != 0) {
            if (activity2 != null) {
                loginCallback.onFailure(-2, activity2.getString(R.string.com_alibaba_sdk_android_openaccount_google_service_not_available));
            }
        } else {
            try {
                activity2.startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(obtainGoogleApiClient((FragmentActivity) activity2)), 9001);
            } catch (Exception e) {
                e.printStackTrace();
                loginCallback.onFailure(-3, e.getMessage());
            }
        }
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void authorizeCallback(int i, int i2, Intent intent) {
        GoogleSignInResult signInResultFromIntent;
        if (i != 9001 || (signInResultFromIntent = Auth.GoogleSignInApi.getSignInResultFromIntent(intent)) == null) {
            return;
        }
        handleSignInResult(signInResultFromIntent);
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void logout(Context context, LogoutCallback logoutCallback) {
        if (this.mGoogleApiClient != null) {
            try {
                Auth.GoogleSignInApi.signOut(this.mGoogleApiClient).setResultCallback(new ResultCallback<Status>() { // from class: com.alibaba.sdk.android.oauth.google.GoogleOauthServiceProviderImpl.1
                    @Override // com.google.android.gms.common.api.ResultCallback
                    public void onResult(@NonNull Status status) {
                    }
                });
            } catch (Exception unused) {
            }
        }
    }

    private GoogleApiClient obtainGoogleApiClient(FragmentActivity fragmentActivity) {
        if (this.mGoogleApiClient == null) {
            synchronized (GoogleOauthServiceProviderImpl.class) {
                if (this.mGoogleApiClient == null) {
                    try {
                        String googleClientId = ConfigManager.getInstance().getGoogleClientId();
                        if (!TextUtils.isEmpty(googleClientId)) {
                            this.gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestScopes(new Scope(Scopes.PLUS_LOGIN), new Scope[0]).requestScopes(new Scope(Scopes.PLUS_ME), new Scope[0]).requestIdToken(googleClientId).requestEmail().build();
                        } else {
                            AliSDKLogger.d(TAG, "new google api client but clientID empty");
                        }
                        this.mGoogleApiClient = new GoogleApiClient.Builder(fragmentActivity).enableAutoManage(fragmentActivity, 1, this).addApi(Auth.GOOGLE_SIGN_IN_API, this.gso).build();
                    } catch (Exception e) {
                        e.printStackTrace();
                        AliSDKLogger.e(TAG, "google oauth init failed");
                    }
                }
            }
        }
        return this.mGoogleApiClient;
    }

    private void handleSignInResult(GoogleSignInResult googleSignInResult) {
        if (googleSignInResult == null) {
            return;
        }
        if (googleSignInResult.isSuccess()) {
            GoogleSignInAccount signInAccount = googleSignInResult.getSignInAccount();
            String id = signInAccount.getId();
            String idToken = signInAccount.getIdToken();
            signInAccount.getEmail();
            LoginByOauthRequest loginByOauthRequest = new LoginByOauthRequest();
            loginByOauthRequest.authCode = idToken;
            loginByOauthRequest.openId = id;
            loginByOauthRequest.oauthPlateform = 32;
            loginByOauthRequest.oauthAppKey = ConfigManager.getInstance().getGoogleClientId();
            new LoginByOauthTask(this.mActivity, mLoginCallback, new HashMap(), loginByOauthRequest, this.oauthServiceProvider).execute(new Void[0]);
            return;
        }
        if (mLoginCallback != null) {
            String string = ResourceUtils.getString("com_alibaba_sdk_android_openaccount_google_fail");
            if (googleSignInResult.getStatus() != null) {
                if (!TextUtils.isEmpty(googleSignInResult.getStatus().getStatusMessage())) {
                    string = googleSignInResult.getStatus().getStatusMessage();
                }
                Log.e("", "google statusCode=" + googleSignInResult.getStatus().getStatus());
            }
            mLoginCallback.onFailure(-4, string);
        }
    }
}
