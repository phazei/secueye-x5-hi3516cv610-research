package anetwork.channel.config;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public interface IRemoteConfig {
    String getConfig(String... strArr);

    void onConfigUpdate(String str);

    void register();

    void unRegister();
}
