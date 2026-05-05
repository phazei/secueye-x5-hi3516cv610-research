package com.aliyun.alink.linksdk.tmp.data.script;

import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ScriptGetRequestPayload {
    public List<ScriptRequestItem> params;
    public int id = TextHelper.getRandomInt();
    public String version = "1.0";
    public String method = "thing.script.get";

    public ScriptGetRequestPayload(List<ScriptRequestItem> list) {
        this.params = list;
    }
}
