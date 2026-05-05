package com.alibaba.sdk.android.openaccount.model;

import java.io.Serializable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class LoginSuccessResult implements Serializable {
    private static final long serialVersionUID = 7819864989551080968L;
    public CheckCodeResult checkCodeResult;
    public JSONObject oauthOtherInfo;
    public JSONObject openAccount;
    public Long reTokenCreationTime;
    public Integer reTokenExpireIn;
    public String refreshToken;
    public Integer scenario;
    public String sid;
    public Long sidCreationTime;
    public Integer sidExpireIn;
    public String token;
}
