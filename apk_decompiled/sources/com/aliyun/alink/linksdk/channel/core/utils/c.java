package com.aliyun.alink.linksdk.channel.core.utils;

import java.util.Random;
import java.util.UUID;

/* JADX INFO: compiled from: StringUtils.java */
/* JADX INFO: loaded from: classes2.dex */
public class c {
    public static String a() {
        return String.valueOf(b().hashCode());
    }

    public static String b() {
        try {
            return UUID.randomUUID().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(new Random().nextInt());
        }
    }
}
