package anet.channel.appmonitor;

import android.text.TextUtils;
import anet.channel.statist.AlarmObject;
import anet.channel.statist.CountObject;
import anet.channel.statist.Dimension;
import anet.channel.statist.Measure;
import anet.channel.statist.Monitor;
import anet.channel.statist.StatObject;
import anet.channel.util.ALog;
import anet.channel.util.StringUtils;
import anetwork.channel.config.NetworkConfigCenter;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a implements IAppMonitor {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static boolean f1661a = false;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static Map<Class<?>, List<Field>> f1662b = new ConcurrentHashMap();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Map<Class<?>, List<Field>> f1663c = new ConcurrentHashMap();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static Map<Field, String> f1664d = new ConcurrentHashMap();
    private static Random e = new Random();
    private static Set<Class<?>> f = Collections.newSetFromMap(new ConcurrentHashMap());

    @Override // anet.channel.appmonitor.IAppMonitor
    @Deprecated
    public void register() {
    }

    @Override // anet.channel.appmonitor.IAppMonitor
    @Deprecated
    public void register(Class<?> cls) {
    }

    public a() {
        try {
            Class.forName("com.alibaba.mtl.appmonitor.AppMonitor");
            f1661a = true;
        } catch (Exception unused) {
            f1661a = false;
        }
    }

    @Override // anet.channel.appmonitor.IAppMonitor
    public void commitStat(StatObject statObject) {
        if (!f1661a || statObject == null) {
            return;
        }
        Class<?> cls = statObject.getClass();
        Monitor monitor = (Monitor) cls.getAnnotation(Monitor.class);
        if (monitor == null) {
            return;
        }
        if (!f.contains(cls)) {
            a(cls);
        }
        if (statObject.beforeCommit()) {
            if (monitor.monitorPoint().equals("network")) {
                int requestStatisticSampleRate = NetworkConfigCenter.getRequestStatisticSampleRate();
                if (requestStatisticSampleRate > 10000 || requestStatisticSampleRate < 0) {
                    requestStatisticSampleRate = 10000;
                }
                if (requestStatisticSampleRate != 10000 && e.nextInt(10000) >= requestStatisticSampleRate) {
                    return;
                }
            }
            try {
                DimensionValueSet dimensionValueSetCreate = DimensionValueSet.create();
                MeasureValueSet measureValueSetCreate = MeasureValueSet.create();
                List<Field> list = f1662b.get(cls);
                HashMap map = ALog.isPrintLog(1) ? new HashMap() : null;
                if (list != null) {
                    for (Field field : list) {
                        Object obj = field.get(statObject);
                        dimensionValueSetCreate.setValue(f1664d.get(field), obj == null ? "" : obj.toString());
                    }
                    for (Field field2 : f1663c.get(cls)) {
                        Double dValueOf = Double.valueOf(field2.getDouble(statObject));
                        measureValueSetCreate.setValue(f1664d.get(field2), dValueOf.doubleValue());
                        if (map != null) {
                            map.put(f1664d.get(field2), dValueOf);
                        }
                    }
                }
                AppMonitor.Stat.commit(monitor.module(), monitor.monitorPoint(), dimensionValueSetCreate, measureValueSetCreate);
                if (ALog.isPrintLog(1)) {
                    ALog.d("awcn.DefaultAppMonitor", "commit stat: " + monitor.monitorPoint(), null, "\nDimensions", dimensionValueSetCreate.getMap().toString(), "\nMeasures", map.toString());
                }
            } catch (Throwable th) {
                ALog.e("awcn.DefaultAppMonitor", "commit monitor point failed", null, th, new Object[0]);
            }
        }
    }

    @Override // anet.channel.appmonitor.IAppMonitor
    public void commitAlarm(AlarmObject alarmObject) {
        if (!f1661a || alarmObject == null || TextUtils.isEmpty(alarmObject.module) || TextUtils.isEmpty(alarmObject.modulePoint)) {
            return;
        }
        if (ALog.isPrintLog(1)) {
            ALog.d("awcn.DefaultAppMonitor", "commit alarm: " + alarmObject, null, new Object[0]);
        }
        if (!alarmObject.isSuccess) {
            AppMonitor.Alarm.commitFail(alarmObject.module, alarmObject.modulePoint, StringUtils.stringNull2Empty(alarmObject.arg), StringUtils.stringNull2Empty(alarmObject.errorCode), StringUtils.stringNull2Empty(alarmObject.errorMsg));
        } else {
            AppMonitor.Alarm.commitSuccess(alarmObject.module, alarmObject.modulePoint, StringUtils.stringNull2Empty(alarmObject.arg));
        }
    }

    @Override // anet.channel.appmonitor.IAppMonitor
    public void commitCount(CountObject countObject) {
        if (!f1661a || countObject == null || TextUtils.isEmpty(countObject.module) || TextUtils.isEmpty(countObject.modulePoint)) {
            return;
        }
        if (ALog.isPrintLog(2)) {
            ALog.i("awcn.DefaultAppMonitor", "commit count: " + countObject, null, new Object[0]);
        }
        AppMonitor.Counter.commit(countObject.module, countObject.modulePoint, StringUtils.stringNull2Empty(countObject.arg), countObject.value);
    }

    synchronized void a(Class<?> cls) {
        if (cls != null) {
            if (f1661a) {
                try {
                    if (f.contains(cls)) {
                        return;
                    }
                    Monitor monitor = (Monitor) cls.getAnnotation(Monitor.class);
                    if (monitor == null) {
                        return;
                    }
                    Field[] fields = cls.getFields();
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    DimensionSet dimensionSetCreate = DimensionSet.create();
                    MeasureSet measureSetCreate = MeasureSet.create();
                    for (Field field : fields) {
                        Dimension dimension = (Dimension) field.getAnnotation(Dimension.class);
                        if (dimension != null) {
                            field.setAccessible(true);
                            arrayList.add(field);
                            String name = dimension.name().equals("") ? field.getName() : dimension.name();
                            f1664d.put(field, name);
                            dimensionSetCreate.addDimension(name);
                        } else {
                            Measure measure = (Measure) field.getAnnotation(Measure.class);
                            if (measure != null) {
                                field.setAccessible(true);
                                arrayList2.add(field);
                                String name2 = measure.name().equals("") ? field.getName() : measure.name();
                                f1664d.put(field, name2);
                                if (measure.max() != Double.MAX_VALUE) {
                                    measureSetCreate.addMeasure(new com.alibaba.mtl.appmonitor.model.Measure(name2, Double.valueOf(measure.constantValue()), Double.valueOf(measure.min()), Double.valueOf(measure.max())));
                                } else {
                                    measureSetCreate.addMeasure(name2);
                                }
                            }
                        }
                    }
                    f1662b.put(cls, arrayList);
                    f1663c.put(cls, arrayList2);
                    com.alibaba.mtl.appmonitor.AppMonitor.register(monitor.module(), monitor.monitorPoint(), measureSetCreate, dimensionSetCreate);
                    f.add(cls);
                } catch (Exception e2) {
                    ALog.e("awcn.DefaultAppMonitor", "register fail", null, e2, new Object[0]);
                }
            }
        }
    }
}
