package a.a.a.a.a.a.c;

import java.util.Random;

/* JADX INFO: compiled from: RandomStringUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class a {
    public static String a(int i) {
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(random.nextInt(62)));
        }
        return stringBuffer.toString();
    }
}
