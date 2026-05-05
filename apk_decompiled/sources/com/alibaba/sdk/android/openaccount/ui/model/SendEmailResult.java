package com.alibaba.sdk.android.openaccount.ui.model;

import com.alibaba.sdk.android.openaccount.model.CheckCodeResult;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class SendEmailResult implements Serializable {
    private static final long serialVersionUID = 5372365936794493307L;
    public CheckCodeResult checkCodeResult;
    public String email;
    public String emailToken;
    public String smsCheckTrustToken;
    public int verifyType;
}
