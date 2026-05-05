package tools;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes4.dex */
public class PreferenceManager {
    private static volatile PreferenceManager manager;
    private Context ctx;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    private PreferenceManager(Context context) {
        this.ctx = context.getApplicationContext();
    }

    public static PreferenceManager getInstance(Context context) {
        if (manager == null) {
            synchronized (PreferenceManager.class) {
                if (manager == null) {
                    manager = new PreferenceManager(context);
                }
            }
        }
        return manager;
    }

    public PreferenceManager attach(String str) {
        this.sharedPreferences = this.ctx.getSharedPreferences(str, 0);
        this.editor = this.sharedPreferences.edit();
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> void put(String str, T t) {
        if (t instanceof String) {
            this.editor.putString(str, (String) t);
        } else if (t instanceof Integer) {
            this.editor.putInt(str, ((Integer) t).intValue());
        } else if (t instanceof Boolean) {
            this.editor.putBoolean(str, ((Boolean) t).booleanValue());
        } else if (t instanceof Float) {
            this.editor.putFloat(str, ((Float) t).floatValue());
        } else if (t instanceof Long) {
            this.editor.putLong(str, ((Long) t).longValue());
        } else {
            this.editor.putString(str, new Gson().toJson(t));
        }
        this.editor.commit();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> PreferenceManager set(String str, T t) {
        if (t instanceof String) {
            this.editor.putString(str, (String) t);
        } else if (t instanceof Integer) {
            this.editor.putInt(str, ((Integer) t).intValue());
        } else if (t instanceof Boolean) {
            this.editor.putBoolean(str, ((Boolean) t).booleanValue());
        } else if (t instanceof Float) {
            this.editor.putFloat(str, ((Float) t).floatValue());
        } else if (t instanceof Long) {
            this.editor.putLong(str, ((Long) t).longValue());
        } else {
            this.editor.putString(str, new Gson().toJson(t));
        }
        return this;
    }

    public void commit() {
        this.editor.commit();
    }

    public <T> T get(String str, Class<T> cls) {
        return (T) get(str, cls, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T get(String str, Class<T> cls, T t) {
        if (cls == String.class) {
            return (T) this.sharedPreferences.getString(str, (String) t);
        }
        if (cls == Integer.class) {
            return (T) new Integer(this.sharedPreferences.getInt(str, t != 0 ? ((Integer) t).intValue() : 0));
        }
        if (cls == Boolean.class) {
            return (T) new Boolean(this.sharedPreferences.getBoolean(str, t != 0 ? ((Boolean) t).booleanValue() : false));
        }
        if (cls == Float.class) {
            return (T) new Float(this.sharedPreferences.getFloat(str, t == 0 ? 0.0f : ((Float) t).floatValue()));
        }
        if (cls == Long.class) {
            return (T) new Long(this.sharedPreferences.getLong(str, t == 0 ? 0L : ((Long) t).longValue()));
        }
        if (cls.isAssignableFrom(Map.class) || cls.isAssignableFrom(Set.class) || cls.isAssignableFrom(List.class)) {
            return (T) new GsonBuilder().enableComplexMapKeySerialization().create().fromJson(this.sharedPreferences.getString(str, null), cls.getGenericSuperclass());
        }
        return (T) new Gson().fromJson(this.sharedPreferences.getString(str, null), (Class) cls);
    }

    public <T> T get(String str, Type type) {
        return (T) new GsonBuilder().enableComplexMapKeySerialization().create().fromJson(this.sharedPreferences.getString(str, null), type);
    }

    public void remove(String str) {
        this.editor.remove(str);
        this.editor.commit();
    }

    public void clear() {
        this.editor.clear();
        this.editor.commit();
    }

    public Boolean contain(String str) {
        return Boolean.valueOf(this.sharedPreferences.contains(str));
    }

    public Map<String, ?> getAll() {
        return this.sharedPreferences.getAll();
    }
}
