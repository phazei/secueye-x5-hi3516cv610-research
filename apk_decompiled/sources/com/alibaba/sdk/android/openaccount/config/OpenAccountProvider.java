package com.alibaba.sdk.android.openaccount.config;

import android.content.Context;
import java.util.concurrent.ThreadPoolExecutor;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountProvider {
    protected String TID;
    protected String TTID;
    protected String alipaySsoDesKey;
    protected String appKey;
    protected String appName;
    protected Context context;
    protected String deviceId;
    protected boolean forbidRefreshCookieInAutologin;
    protected String guideAppName;
    protected String guideBackground;
    protected String guideCloseResource;
    protected String guidePwdLoginResource;
    protected String imei;
    protected String imsi;
    protected String productId;
    protected String productVersion;
    protected boolean refreshCookieDegrade;
    protected ThreadPoolExecutor threadPool;
    protected String version;
    protected boolean isAppDebug = false;
    protected boolean isUnitDeploy = false;
    protected boolean forceShowPwdInAlert = false;
    protected boolean isTaobaoApp = false;
    protected boolean needSsoLogin = true;
    protected boolean needSsoLoginUI = false;
    protected boolean needWindVaneInit = false;
    protected boolean needSsoV2Login = false;
    protected boolean needAlipaySsoGuide = false;
    protected boolean needTaobaoSsoGuide = false;
    protected boolean needPwdGuide = true;
    protected boolean needAccsLogin = false;
    protected boolean needSsoV2LoginUI = false;
    protected boolean useSeparateThreadPool = false;
    protected boolean registerMachineCheckDegrade = false;
    protected boolean needEnterPriseRegister = true;
    protected String appId = "";
    protected int envType = 3;
    protected int site = 0;
    protected boolean isForbidLoginFromBackground = false;
    protected boolean needHelpButton = true;
    protected boolean needAlipayLoginBtn = true;

    public boolean isTaobaoApp() {
        return this.isTaobaoApp;
    }

    public boolean showPWDInAlert() {
        return this.forceShowPwdInAlert;
    }

    public void setTaobaoApp(boolean z) {
        this.isTaobaoApp = z;
    }

    public boolean isAppDebug() {
        return this.isAppDebug;
    }

    public void setAppDebug(boolean z) {
        this.isAppDebug = z;
    }

    public boolean isUnitDeploy() {
        return this.isUnitDeploy;
    }

    public void setUnitDeploy(boolean z) {
        this.isUnitDeploy = z;
    }

    public boolean isNeedSsoLogin() {
        return this.needSsoLogin;
    }

    public void setNeedSsoLogin(boolean z) {
        this.needSsoLogin = z;
    }

    public boolean isNeedSsoLoginUI() {
        return this.needSsoLoginUI;
    }

    public void setNeedSsoLoginUI(boolean z) {
        this.needSsoLoginUI = z;
    }

    public boolean isNeedWindVaneInit() {
        return this.needWindVaneInit;
    }

    public void setNeedWindVaneInit(boolean z) {
        this.needWindVaneInit = z;
    }

    public boolean isNeedSsoV2Login() {
        return this.needSsoV2Login;
    }

    public void setNeedSsoV2Login(boolean z) {
        this.needSsoV2Login = z;
    }

    public boolean isNeedAlipaySsoGuide() {
        return this.needAlipaySsoGuide;
    }

    public void setNeedAlipaySsoGuide(boolean z) {
        this.needAlipaySsoGuide = z;
    }

    public boolean isNeedTaobaoSsoGuide() {
        return this.needTaobaoSsoGuide;
    }

    public void setNeedTaobaoSsoGuide(boolean z) {
        this.needTaobaoSsoGuide = z;
    }

    public boolean isNeedPwdGuide() {
        return this.needPwdGuide;
    }

    public void setNeedPwdGuide(boolean z) {
        this.needPwdGuide = z;
    }

    public boolean isNeedAccsLogin() {
        return this.needAccsLogin;
    }

    public void setNeedAccsLogin(boolean z) {
        this.needAccsLogin = z;
    }

    public boolean isNeedSsoV2LoginUI() {
        return this.needSsoV2LoginUI;
    }

    public void setNeedSsoV2LoginUI(boolean z) {
        this.needSsoV2LoginUI = z;
    }

    public boolean isUseSeparateThreadPool() {
        return this.useSeparateThreadPool;
    }

    public void setUseSeparateThreadPool(boolean z) {
        this.useSeparateThreadPool = z;
    }

    public boolean isRegisterMachineCheckDegrade() {
        return this.registerMachineCheckDegrade;
    }

    public void setRegisterMachineCheckDegrade(boolean z) {
        this.registerMachineCheckDegrade = z;
    }

    public boolean isNeedEnterPriseRegister() {
        return this.needEnterPriseRegister;
    }

    public void setNeedEnterPriseRegister(boolean z) {
        this.needEnterPriseRegister = z;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String str) {
        this.deviceId = str;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String str) {
        this.appId = str;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String str) {
        this.productId = str;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String str) {
        this.version = str;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String str) {
        this.appKey = str;
    }

    public String getProductVersion() {
        return this.productVersion;
    }

    public void setProductVersion(String str) {
        this.productVersion = str;
    }

    public String getTTID() {
        return this.TTID;
    }

    public void setTTID(String str) {
        this.TTID = str;
    }

    public String getTID() {
        return this.TID;
    }

    public void setTID(String str) {
        this.TID = str;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String str) {
        this.imei = str;
    }

    public String getImsi() {
        return this.imsi;
    }

    public void setImsi(String str) {
        this.imsi = str;
    }

    public int getEnvType() {
        return this.envType;
    }

    public void setEnvType(int i) {
        this.envType = i;
    }

    public ThreadPoolExecutor getThreadPool() {
        return this.threadPool;
    }

    public void setThreadPool(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPool = threadPoolExecutor;
    }

    public int getSite() {
        return this.site;
    }

    public void setSite(int i) {
        this.site = i;
    }

    public boolean isForbidLoginFromBackground() {
        return this.isForbidLoginFromBackground;
    }

    public void setForbidLoginFromBackground(boolean z) {
        this.isForbidLoginFromBackground = z;
    }

    public boolean isForbidRefreshCookieInAutologin() {
        return this.forbidRefreshCookieInAutologin;
    }

    public void setForbidRefreshCookieInAutologin(boolean z) {
        this.forbidRefreshCookieInAutologin = z;
    }

    public boolean isRefreshCookieDegrade() {
        return this.refreshCookieDegrade;
    }

    public void setRefreshCookieDegrade(boolean z) {
        this.refreshCookieDegrade = z;
    }

    public boolean isNeedHelpButton() {
        return this.needHelpButton;
    }

    public void setNeedHelpButton(boolean z) {
        this.needHelpButton = z;
    }

    public boolean isNeedAlipayLoginBtn() {
        return this.needAlipayLoginBtn;
    }

    public void setNeedAlipayLoginBtn(boolean z) {
        this.needAlipayLoginBtn = z;
    }

    public String getAlipaySsoDesKey() {
        return this.alipaySsoDesKey;
    }

    public void setAlipaySsoDesKey(String str) {
        this.alipaySsoDesKey = str;
    }

    public String getGuideBackground() {
        return this.guideBackground;
    }

    public void setGuideBackground(String str) {
        this.guideBackground = str;
    }

    public String getGuideAppName() {
        return this.guideAppName;
    }

    public void setGuideAppName(String str) {
        this.guideAppName = str;
    }

    public String getGuidePwdLoginResource() {
        return this.guidePwdLoginResource;
    }

    public void setGuidePwdLoginResource(String str) {
        this.guidePwdLoginResource = str;
    }

    public String getGuideCloseResource() {
        return this.guideCloseResource;
    }

    public void setGuideCloseResource(String str) {
        this.guideCloseResource = str;
    }
}
