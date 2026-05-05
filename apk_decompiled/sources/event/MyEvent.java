package event;

/* JADX INFO: loaded from: classes3.dex */
public class MyEvent {
    private Object OBJECT;
    private EventType TYPE;

    public MyEvent(EventType eventType, Object obj) {
        this.TYPE = eventType;
        this.OBJECT = obj;
    }

    public EventType getTYPE() {
        return this.TYPE;
    }

    public void setTYPE(EventType eventType) {
        this.TYPE = eventType;
    }

    public Object getOBJECT() {
        return this.OBJECT;
    }

    public void setOBJECT(Object obj) {
        this.OBJECT = obj;
    }
}
