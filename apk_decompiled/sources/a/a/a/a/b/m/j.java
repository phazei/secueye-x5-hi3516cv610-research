package a.a.a.a.b.m;

/* JADX INFO: compiled from: ReflectUtils.java */
/* JADX INFO: loaded from: classes.dex */
public class j {
    public static boolean a(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (ClassNotFoundException e) {
            a.b("ReflectUtils", "hasClss=" + e);
            return false;
        } catch (Exception e2) {
            a.b("ReflectUtils", "hasClssEx=" + e2);
            return false;
        }
    }
}
