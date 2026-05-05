package tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes4.dex */
public class SecuritySharedPreference implements SharedPreferences {
    private static final String TAG = "tools.SecuritySharedPreference";
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public static void clear(Context context, String str) {
        SecurityEditor securityEditorEdit = new SecuritySharedPreference(context, str, 0).edit();
        securityEditorEdit.clear();
        securityEditorEdit.commit();
    }

    public SecuritySharedPreference(Context context, String str, int i) {
        this.mContext = context;
        if (TextUtils.isEmpty(str)) {
            this.mSharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            this.mSharedPreferences = context.getSharedPreferences(str, i);
        }
    }

    @Override // android.content.SharedPreferences
    public Map<String, String> getAll() {
        Map<String, ?> all = this.mSharedPreferences.getAll();
        HashMap map = new HashMap();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            if (entry.getValue() != null) {
                map.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return map;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String encryptPreference(String str) {
        return EncryptUtil.getInstance(this.mContext).encrypt(str);
    }

    private String decryptPreference(String str) {
        return EncryptUtil.getInstance(this.mContext).decrypt(str);
    }

    @Override // android.content.SharedPreferences
    @Nullable
    public String getString(String str, String str2) {
        String string = this.mSharedPreferences.getString(encryptPreference(str), null);
        return string == null ? str2 : decryptPreference(string);
    }

    @Override // android.content.SharedPreferences
    @Nullable
    public Set<String> getStringSet(String str, Set<String> set) {
        Set<String> stringSet = this.mSharedPreferences.getStringSet(encryptPreference(str), null);
        if (stringSet == null) {
            return set;
        }
        HashSet hashSet = new HashSet();
        Iterator<String> it = stringSet.iterator();
        while (it.hasNext()) {
            hashSet.add(decryptPreference(it.next()));
        }
        return hashSet;
    }

    @Override // android.content.SharedPreferences
    public int getInt(String str, int i) {
        String string = this.mSharedPreferences.getString(encryptPreference(str), null);
        return string == null ? i : Integer.parseInt(decryptPreference(string));
    }

    @Override // android.content.SharedPreferences
    public long getLong(String str, long j) {
        String string = this.mSharedPreferences.getString(encryptPreference(str), null);
        return string == null ? j : Long.parseLong(decryptPreference(string));
    }

    @Override // android.content.SharedPreferences
    public float getFloat(String str, float f) {
        String string = this.mSharedPreferences.getString(encryptPreference(str), null);
        return string == null ? f : Float.parseFloat(decryptPreference(string));
    }

    @Override // android.content.SharedPreferences
    public boolean getBoolean(String str, boolean z) {
        String string = this.mSharedPreferences.getString(encryptPreference(str), null);
        return string == null ? z : Boolean.parseBoolean(decryptPreference(string));
    }

    @Override // android.content.SharedPreferences
    public boolean contains(String str) {
        return this.mSharedPreferences.contains(encryptPreference(str));
    }

    @Override // android.content.SharedPreferences
    public SecurityEditor edit() {
        return new SecurityEditor();
    }

    @Override // android.content.SharedPreferences
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.mSharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override // android.content.SharedPreferences
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.mSharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public void handleTransition() {
        Map<String, ?> all = this.mSharedPreferences.getAll();
        HashMap map = new HashMap();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            map.put(encryptPreference(entry.getKey()), encryptPreference(entry.getValue().toString()));
        }
        SharedPreferences.Editor editorEdit = this.mSharedPreferences.edit();
        editorEdit.clear().commit();
        for (Map.Entry entry2 : map.entrySet()) {
            editorEdit.putString((String) entry2.getKey(), (String) entry2.getValue());
        }
        editorEdit.commit();
    }

    public final class SecurityEditor implements SharedPreferences.Editor {
        private SharedPreferences.Editor mEditor;

        private SecurityEditor() {
            this.mEditor = SecuritySharedPreference.this.mSharedPreferences.edit();
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putString(String str, String str2) {
            this.mEditor.putString(SecuritySharedPreference.this.encryptPreference(str), SecuritySharedPreference.this.encryptPreference(str2));
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putStringSet(String str, Set<String> set) {
            HashSet hashSet = new HashSet();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                hashSet.add(SecuritySharedPreference.this.encryptPreference(it.next()));
            }
            this.mEditor.putStringSet(SecuritySharedPreference.this.encryptPreference(str), hashSet);
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putInt(String str, int i) {
            this.mEditor.putString(SecuritySharedPreference.this.encryptPreference(str), SecuritySharedPreference.this.encryptPreference(Integer.toString(i)));
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putLong(String str, long j) {
            this.mEditor.putString(SecuritySharedPreference.this.encryptPreference(str), SecuritySharedPreference.this.encryptPreference(Long.toString(j)));
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putFloat(String str, float f) {
            this.mEditor.putString(SecuritySharedPreference.this.encryptPreference(str), SecuritySharedPreference.this.encryptPreference(Float.toString(f)));
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putBoolean(String str, boolean z) {
            this.mEditor.putString(SecuritySharedPreference.this.encryptPreference(str), SecuritySharedPreference.this.encryptPreference(Boolean.toString(z)));
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor remove(String str) {
            this.mEditor.remove(SecuritySharedPreference.this.encryptPreference(str));
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor clear() {
            this.mEditor.clear();
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public boolean commit() {
            return this.mEditor.commit();
        }

        @Override // android.content.SharedPreferences.Editor
        @TargetApi(9)
        public void apply() {
            if (Build.VERSION.SDK_INT >= 9) {
                this.mEditor.apply();
            } else {
                commit();
            }
        }
    }
}
