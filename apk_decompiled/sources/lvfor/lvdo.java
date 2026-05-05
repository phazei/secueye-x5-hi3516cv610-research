package lvfor;

import android.os.Handler;
import android.os.HandlerThread;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.Version;
import com.aliyun.iotx.linkvisual.media.misc.tracking.SlsBundle;
import com.aliyun.iotx.linkvisual.media.misc.tracking.beans.BaseEvent;
import com.aliyun.iotx.linkvisual.media.video.utils.APIHelper;
import com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener;
import java.util.LinkedList;
import lvbyte.lvfor;
import lvcase.lvint;

/* JADX INFO: loaded from: classes4.dex */
public class lvdo {

    /* JADX INFO: renamed from: lvlong, reason: collision with root package name */
    private static volatile lvdo f8001lvlong;

    /* JADX INFO: renamed from: lvbyte, reason: collision with root package name */
    private HandlerThread f8002lvbyte;

    /* JADX INFO: renamed from: lvcase, reason: collision with root package name */
    private Handler f8003lvcase;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private lvcase.lvif f8006lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private String f8008lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    private SlsBundle f8009lvint;

    /* JADX INFO: renamed from: lvtry, reason: collision with root package name */
    private boolean f8011lvtry = false;

    /* JADX INFO: renamed from: lvchar, reason: collision with root package name */
    private Runnable f8004lvchar = new RunnableC0307lvdo();

    /* JADX INFO: renamed from: lvgoto, reason: collision with root package name */
    private Runnable f8007lvgoto = new lvif();

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private LinkedList<lvthis.lvdo> f8005lvdo = new LinkedList<>();

    /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
    private lvfor f8010lvnew = new lvfor(10);

    /* JADX INFO: renamed from: lvfor.lvdo$lvdo, reason: collision with other inner class name */
    class RunnableC0307lvdo implements Runnable {

        /* JADX INFO: renamed from: lvfor.lvdo$lvdo$lvdo, reason: collision with other inner class name */
        class C0308lvdo implements IAPIHelperListener {
            C0308lvdo() {
            }

            @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
            public void onFailed(lvbyte.lvif lvifVar) {
                lvdo.this.f8011lvtry = false;
                ALog.e("linksdk_lv_sls", lvifVar.toString());
            }

            @Override // com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener
            public void onResponse(lvbyte.lvif lvifVar) {
                lvdo.this.f8011lvtry = false;
                try {
                    lvdo.this.f8009lvint = (SlsBundle) JSON.parseObject(String.valueOf(lvifVar.lvif()), SlsBundle.class);
                    if (SlsBundle.isValid(lvdo.this.f8009lvint)) {
                        lvdo.this.lvfor();
                        lvdo.this.f8003lvcase.post(lvdo.this.f8007lvgoto);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("sls token is invalid. ");
                        sb.append(lvdo.this.f8009lvint != null ? lvdo.this.f8009lvint.toString() : "");
                        ALog.w("linksdk_lv_sls", sb.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        RunnableC0307lvdo() {
        }

        @Override // java.lang.Runnable
        public void run() {
            lvdo.this.f8011lvtry = true;
            if (Version.isIlop || Version.isTg) {
                APIHelper.sendIoTRequest(lvbyte.lvdo.SLS_TOKEN_QUERY, null, lvdo.this.f8008lvif, new C0308lvdo());
            } else {
                ALog.i("linksdk_lv_sls", "ignore due to no sls token server.");
            }
        }
    }

    class lvif implements Runnable {
        lvif() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (lvdo.this.f8011lvtry) {
                ALog.d("linksdk_lv_sls", "log client renewing. ignore.");
                return;
            }
            if (lvdo.this.f8006lvfor == null || SlsBundle.isExpired(lvdo.this.f8009lvint)) {
                ALog.d("linksdk_lv_sls", "log client need renew for the valid token.");
                lvdo.this.f8004lvchar.run();
                return;
            }
            synchronized (lvdo.this.f8005lvdo) {
                if (lvdo.this.f8005lvdo.size() > 0) {
                    int iMin = Math.min(lvdo.this.f8005lvdo.size(), 10);
                    lvthis.lvif lvifVar = new lvthis.lvif();
                    int i = 0;
                    for (int i2 = 0; i2 < iMin; i2++) {
                        lvifVar.lvdo((lvthis.lvdo) lvdo.this.f8005lvdo.get(i2));
                    }
                    lvbreak.lvdo lvdoVarLvdo = lvdo.this.f8006lvfor.lvdo(new lvvoid.lvdo(lvdo.this.f8009lvint.getProject(), lvdo.this.f8009lvint.getLogstore(), lvifVar));
                    if (lvdoVarLvdo.lvdo().lvdo() == 200) {
                        ALog.d("linksdk_lv_sls", "report success, remove log size= " + iMin);
                        while (i < iMin) {
                            lvdo.this.f8005lvdo.remove();
                            i++;
                        }
                    } else {
                        ALog.w("linksdk_lv_sls", "report failed, code=" + lvdoVarLvdo.lvdo().lvdo());
                        lvdo.this.f8010lvnew.lvdo();
                        if (lvdo.this.f8010lvnew.lvif()) {
                            ALog.d("linksdk_lv_sls", "The maximum number of retransmissions reached, force remove log size= " + iMin);
                            while (i < iMin) {
                                lvdo.this.f8005lvdo.remove();
                                i++;
                            }
                            lvdo.this.f8010lvnew.lvfor();
                        }
                    }
                }
            }
        }
    }

    private lvdo() {
        HandlerThread handlerThread = new HandlerThread("lv-sls");
        this.f8002lvbyte = handlerThread;
        handlerThread.start();
        this.f8003lvcase = new Handler(this.f8002lvbyte.getLooper());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lvfor() {
        if (ALog.getLevel() < 2) {
            lvint.lvdo();
        }
        this.f8006lvfor = new lvcase.lvif(this.f8009lvint.getEndpoint(), new lvgoto.lvint(this.f8009lvint.getAccessKeyId(), this.f8009lvint.getAccessKeySecret(), this.f8009lvint.getSecurityToken()), new lvcase.lvdo());
        ALog.d("linksdk_lv_sls", "new log client. " + this.f8009lvint.toString());
    }

    public static lvdo lvif() {
        if (f8001lvlong == null) {
            synchronized (lvdo.class) {
                if (f8001lvlong == null) {
                    f8001lvlong = new lvdo();
                }
            }
        }
        return f8001lvlong;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        lvdo();
    }

    public void lvdo() {
        HandlerThread handlerThread = this.f8002lvbyte;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            this.f8002lvbyte = null;
        }
    }

    public void lvdo(BaseEvent baseEvent) {
        if (Version.isIlop || Version.isTg) {
            synchronized (this.f8005lvdo) {
                ALog.d("linksdk_lv_sls", "new event enqueue: " + baseEvent.toString());
                lvthis.lvdo lvdoVar = new lvthis.lvdo();
                lvdoVar.lvdo("content", baseEvent.toString());
                this.f8005lvdo.add(lvdoVar);
            }
            this.f8003lvcase.post(this.f8007lvgoto);
        }
    }

    public void lvdo(String str) {
        this.f8008lvif = str;
    }
}
