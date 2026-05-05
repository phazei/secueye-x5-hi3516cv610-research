package tools;

import android.app.Activity;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes4.dex */
public class ActivityManager {
    private static volatile ActivityManager manager;
    public List<WeakReference<Activity>> activityList = new ArrayList();

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        if (manager == null) {
            synchronized (ActivityManager.class) {
                if (manager == null) {
                    manager = new ActivityManager();
                }
            }
        }
        return manager;
    }

    public synchronized void push(Activity activity2) {
        if (activity2 != null) {
            Iterator<WeakReference<Activity>> it = this.activityList.iterator();
            while (it.hasNext()) {
                if (activity2.equals(it.next().get())) {
                    return;
                }
            }
            this.activityList.add(new WeakReference<>(activity2));
        }
    }

    public synchronized void pop(Activity activity2) {
        if (activity2 != null) {
            for (int size = this.activityList.size() - 1; size >= 0; size--) {
                WeakReference<Activity> weakReference = this.activityList.get(size);
                if (weakReference != null && activity2.equals(weakReference.get())) {
                    this.activityList.remove(weakReference);
                    return;
                }
            }
        }
    }

    public synchronized void clear() {
        Activity activity2;
        for (WeakReference<Activity> weakReference : this.activityList) {
            if (weakReference.get() != null && (activity2 = weakReference.get()) != null && !activity2.isFinishing()) {
                activity2.finish();
            }
        }
    }

    public synchronized Activity getActivity() {
        if (this.activityList.isEmpty()) {
            return null;
        }
        return this.activityList.size() > 0 ? this.activityList.get(this.activityList.size() - 1).get() : null;
    }
}
