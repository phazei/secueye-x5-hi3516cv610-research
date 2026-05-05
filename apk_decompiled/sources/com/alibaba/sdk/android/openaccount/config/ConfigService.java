package com.alibaba.sdk.android.openaccount.config;

import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.Version;
import com.alibaba.sdk.android.openaccount.callback.OnActivityResultCallback;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface ConfigService {
    int getAppKeyIndex();

    int getAppKeyIndex(Environment environment);

    boolean getBooleanProperty(String str, boolean z);

    OpenAccountProvider getDataProvider();

    Environment getEnvironment();

    int getIntProperty(String str, int i);

    int getLogLevel();

    long getLongProperty(String str, long j);

    OnActivityResultCallback getOnActivityResultCallback();

    Map<String, String> getProperties();

    Version getSDKVersion();

    String getSecurityImagePostfix();

    String[] getStringArrayProperty(String str, String[] strArr);

    String getStringProperty(String str, String str2);

    boolean isDebugEnabled();

    boolean isUseSingleImage();

    boolean openTaobaoUILogin();

    void registerPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener);

    void registerPropertyChangeListener(String[] strArr, PropertyChangeListener propertyChangeListener);

    void setAppKeyIndex(int i, int i2, int i3, int i4);

    void setDataProvider(OpenAccountProvider openAccountProvider);

    void setDebugEnabled(boolean z);

    void setDynamicProperties(String str);

    void setLogLevel(int i);

    void setOnActivityResultCallback(OnActivityResultCallback onActivityResultCallback);

    void setOpenTaobaoUILogin(boolean z);

    void setProperty(String str, String str2);

    void setSecGuardImagePostfix(String str);
}
