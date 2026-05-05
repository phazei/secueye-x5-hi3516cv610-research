package b;

import android.content.Context;
import android.content.SharedPreferences;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: compiled from: ProvisioningSettings.java */
/* JADX INFO: loaded from: classes.dex */
public class s extends r {
    public final Context h;

    public s(Context context) {
        this.h = context;
        b();
    }

    public final void a() {
        Map<String, ?> all = this.h.getSharedPreferences(Utils.APPLICATION_KEYS, 0).getAll();
        if (all.isEmpty()) {
            this.f2204b.add(SecureUtils.generateRandomApplicationKey().toUpperCase());
            this.f2204b.add(SecureUtils.generateRandomApplicationKey().toUpperCase());
            this.f2204b.add(SecureUtils.generateRandomApplicationKey().toUpperCase());
        } else {
            this.f2204b.clear();
            for (int i = 0; i < all.size(); i++) {
                this.f2204b.add(i, String.valueOf(all.get(String.valueOf(i))));
            }
        }
        j();
    }

    public void b() {
        SharedPreferences sharedPreferences = this.h.getSharedPreferences("PROVISIONING_DATA", 0);
        this.f2203a = sharedPreferences.getString("NETWORK_KEY", SecureUtils.generateRandomNetworkKey());
        this.e = sharedPreferences.getInt("UNICAST_ADDRESS", 1);
        this.f2205c = sharedPreferences.getInt("KEY_INDEX", 0);
        this.f2206d = sharedPreferences.getInt("IV_INDEX", 0);
        this.f = sharedPreferences.getInt("FLAGS", 0);
        this.g = sharedPreferences.getInt("GLOBAL_TTL", 5);
        a();
        p();
    }

    public List<String> c() {
        return Collections.unmodifiableList(this.f2204b);
    }

    public void d(int i) {
        this.f2205c = i;
        n();
    }

    public void e(int i) {
        this.e = i;
        q();
    }

    public int f() {
        return this.f2206d;
    }

    public int g() {
        return this.f2205c;
    }

    public String h() {
        return this.f2203a;
    }

    public int i() {
        return this.e;
    }

    public void j() {
        SharedPreferences.Editor editorEdit = this.h.getSharedPreferences(Utils.APPLICATION_KEYS, 0).edit();
        if (this.f2204b.isEmpty()) {
            editorEdit.clear();
        } else {
            for (int i = 0; i < this.f2204b.size(); i++) {
                editorEdit.putString(String.valueOf(i), this.f2204b.get(i));
            }
        }
        editorEdit.apply();
    }

    public final void k() {
        SharedPreferences.Editor editorEdit = this.h.getSharedPreferences("PROVISIONING_DATA", 0).edit();
        editorEdit.putInt("FLAGS", this.f);
        editorEdit.apply();
    }

    public final void l() {
        SharedPreferences.Editor editorEdit = this.h.getSharedPreferences("PROVISIONING_DATA", 0).edit();
        editorEdit.putInt("GLOBAL_TTL", this.g);
        editorEdit.apply();
    }

    public final void m() {
        SharedPreferences.Editor editorEdit = this.h.getSharedPreferences("PROVISIONING_DATA", 0).edit();
        editorEdit.putInt("IV_INDEX", this.f2206d);
        editorEdit.apply();
    }

    public final void n() {
        SharedPreferences.Editor editorEdit = this.h.getSharedPreferences("PROVISIONING_DATA", 0).edit();
        editorEdit.putInt("KEY_INDEX", this.f2205c);
        editorEdit.apply();
    }

    public final void o() {
        SharedPreferences.Editor editorEdit = this.h.getSharedPreferences("PROVISIONING_DATA", 0).edit();
        editorEdit.putString("NETWORK_KEY", this.f2203a);
        editorEdit.apply();
    }

    public void p() {
        o();
        q();
        n();
        m();
        k();
        l();
        j();
    }

    public final void q() {
        SharedPreferences.Editor editorEdit = this.h.getSharedPreferences("PROVISIONING_DATA", 0).edit();
        editorEdit.putInt("UNICAST_ADDRESS", this.e);
        editorEdit.apply();
    }

    public void c(int i) {
        this.f2206d = i;
        m();
    }

    public int d() {
        return this.f;
    }

    public int e() {
        return this.g;
    }

    public void b(int i, String str) {
        if (!this.f2204b.contains(str)) {
            this.f2204b.set(i, str);
            j();
            return;
        }
        throw new IllegalArgumentException("App key already exists");
    }

    public void a(String str) {
        this.f2203a = str;
        o();
    }

    public void a(int i, String str) {
        if (!this.f2204b.contains(str)) {
            this.f2204b.add(i, str);
            j();
            return;
        }
        throw new IllegalArgumentException("App key already exists");
    }

    public void b(int i) {
        this.g = i;
        l();
    }

    public void a(int i) {
        this.f = i;
        k();
    }
}
