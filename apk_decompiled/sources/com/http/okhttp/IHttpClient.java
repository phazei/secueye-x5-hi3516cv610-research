package com.http.okhttp;

import com.http.helper.HttpCallback;
import java.io.IOException;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
public interface IHttpClient {
    void getAsync(String str, Map<String, String> map, HttpCallback<IOException, String> httpCallback) throws IOException;

    String getSync(String str, Map<String, String> map) throws IOException;

    void postAsync(String str, Map<String, String> map, String str2, HttpCallback<IOException, String> httpCallback) throws IOException;

    String postSync(String str, Map<String, String> map, String str2) throws IOException;
}
