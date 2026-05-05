package com.aliyun.alink.business.devicecenter.base;

import com.alibaba.ailabs.iot.aisbase.env.AppEnv;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.ReflectUtils;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class DCEnvHelper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static AtomicBoolean f3365a = new AtomicBoolean(true);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static AtomicBoolean f3366b = new AtomicBoolean(false);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static AtomicBoolean f3367c = new AtomicBoolean(false);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static AtomicBoolean f3368d = new AtomicBoolean(false);
    public static AtomicBoolean e = new AtomicBoolean(false);
    public static AtomicBoolean f = new AtomicBoolean(false);
    public static AtomicBoolean g = new AtomicBoolean(false);
    public static AtomicBoolean h = new AtomicBoolean(false);
    public static AtomicBoolean i = new AtomicBoolean(false);
    public static AtomicBoolean j = new AtomicBoolean(false);
    public static AtomicBoolean k = new AtomicBoolean(false);

    public static boolean getHasLogExtraSDK() {
        if (!f3366b.get()) {
            initEnv();
        }
        return j.get();
    }

    public static boolean getHasUTAbility() {
        if (!f3366b.get()) {
            initEnv();
        }
        return k.get();
    }

    public static boolean hasApiClient() {
        if (!f3366b.get()) {
            initEnv();
        }
        return f3368d.get();
    }

    public static boolean hasApiClientBiz() {
        if (!f3366b.get()) {
            initEnv();
        }
        return e.get();
    }

    public static boolean hasBreeze() {
        if (!f3366b.get()) {
            initEnv();
        }
        return f3367c.get();
    }

    public static boolean hasCoapAbilitiAB() {
        if (!f3366b.get()) {
            initEnv();
        }
        return i.get();
    }

    public static boolean hasMeshScanAbility() {
        if (!f3366b.get()) {
            initEnv();
        }
        return f.get();
    }

    public static boolean hasTGBleScanAbilityAB() {
        if (!f3366b.get()) {
            initEnv();
        }
        return g.get();
    }

    public static void initEnv() {
        try {
            if (ReflectUtils.hasClass("com.aliyun.iot.breeze.biz.BreezeHelper")) {
                f3367c.set(true);
            }
            if (ReflectUtils.hasClass("com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient")) {
                f3368d.set(true);
            }
            if (ReflectUtils.hasClass("com.aliyun.alink.apiclient.biz.ApiClientBiz")) {
                e.set(true);
            }
            if (ReflectUtils.hasClass("com.alibaba.ailabs.iot.mesh.TgScanManager")) {
                f.set(true);
            }
            if (ReflectUtils.hasClass("com.alibaba.ailabs.iot.bluetoothlesdk.GenieBLEDeviceManager") && (ReflectUtils.hasClass("com.alibaba.ailabs.tg.mtop.MtopCommonHelper") || ReflectUtils.hasClass("com.taobao.api.TaobaoClient"))) {
                ALog.d("DCEnvHelper", "has genie and (top or mtop)");
                g.set(true);
            }
            if (ReflectUtils.hasClass("com.alibaba.ailabs.tg.mtop.MtopCommonHelper") && ReflectUtils.hasClass("mtopsdk.mtop.domain.IMTOPDataObject") && ReflectUtils.hasClass(AppEnv.MTOP_CLASS_NAME)) {
                h.set(true);
            }
            if (ReflectUtils.hasClass("com.aliyun.alink.linksdk.alcs.coap.AlcsCoAP")) {
                i.set(true);
            }
            if (ReflectUtils.hasClass("com.aliyun.alink.linksdk.logextra.upload.Log2Cloud")) {
                j.set(true);
            }
            if (ReflectUtils.hasClass("com.ut.mini.UTAnalytics") && ReflectUtils.hasClass("com.ut.mini.internal.UTOriginalCustomHitBuilder")) {
                k.set(true);
            }
            f3366b.set(true);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static boolean isILopEnv() {
        if (!f3366b.get()) {
            initEnv();
        }
        return hasApiClient() && !isTgEnv();
    }

    public static boolean isTgEnv() {
        if (!f3366b.get()) {
            initEnv();
        }
        return f3365a.get() && hasTGBleScanAbilityAB() && h.get();
    }

    public static void setUseTgEnv(boolean z) {
        f3365a.set(z);
    }
}
