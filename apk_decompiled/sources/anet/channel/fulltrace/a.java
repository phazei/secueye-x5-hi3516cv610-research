package anet.channel.fulltrace;

import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static volatile IFullTraceAnalysis f1747a = new C0172a(null);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static boolean f1748b = false;

    public static IFullTraceAnalysis a() {
        return f1747a;
    }

    public static void a(IFullTraceAnalysis iFullTraceAnalysis) {
        f1747a = new C0172a(iFullTraceAnalysis);
    }

    /* JADX INFO: renamed from: anet.channel.fulltrace.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: Taobao */
    private static class C0172a implements IFullTraceAnalysis {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private IFullTraceAnalysis f1749a;

        C0172a(IFullTraceAnalysis iFullTraceAnalysis) {
            this.f1749a = iFullTraceAnalysis;
            boolean unused = a.f1748b = true;
        }

        @Override // anet.channel.fulltrace.IFullTraceAnalysis
        public String createRequest() {
            IFullTraceAnalysis iFullTraceAnalysis;
            if (!a.f1748b || (iFullTraceAnalysis = this.f1749a) == null) {
                return null;
            }
            try {
                return iFullTraceAnalysis.createRequest();
            } catch (Throwable th) {
                boolean unused = a.f1748b = false;
                ALog.e("anet.AnalysisFactory", "createRequest fail.", null, th, new Object[0]);
                return null;
            }
        }

        @Override // anet.channel.fulltrace.IFullTraceAnalysis
        public void commitRequest(String str, RequestStatistic requestStatistic) {
            IFullTraceAnalysis iFullTraceAnalysis;
            if (a.f1748b && (iFullTraceAnalysis = this.f1749a) != null) {
                try {
                    iFullTraceAnalysis.commitRequest(str, requestStatistic);
                } catch (Throwable th) {
                    boolean unused = a.f1748b = false;
                    ALog.e("anet.AnalysisFactory", "fulltrace commit fail.", null, th, new Object[0]);
                }
            }
        }

        @Override // anet.channel.fulltrace.IFullTraceAnalysis
        public b getSceneInfo() {
            IFullTraceAnalysis iFullTraceAnalysis;
            if (!a.f1748b || (iFullTraceAnalysis = this.f1749a) == null) {
                return null;
            }
            try {
                return iFullTraceAnalysis.getSceneInfo();
            } catch (Throwable th) {
                boolean unused = a.f1748b = false;
                ALog.e("anet.AnalysisFactory", "getSceneInfo fail", null, th, new Object[0]);
                return null;
            }
        }
    }
}
