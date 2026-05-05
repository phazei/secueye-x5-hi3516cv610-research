package com.aliyun.alink.linksdk.tmp.timing;

import android.text.TextUtils;
import android.util.Pair;
import com.aliyun.alink.linksdk.tmp.timing.DeviceTimerAttributeModel;
import com.xiaomi.mipush.sdk.Constants;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class MeshTimerModel implements Serializable {
    private String attributesTargets;
    private String days;
    private TimerEnableType enableType;
    private String endTime;
    private int runTime;
    private int sleepTime;
    private String time;
    private int timeZone;
    private String timerID;
    private TimerType timerType;

    private MeshTimerModel() {
    }

    private MeshTimerModel(TimerType timerType, String str, String str2, String str3, boolean z, int i) {
        this.timerType = timerType;
        this.attributesTargets = str;
        this.time = str2;
        this.days = str3;
        this.enableType = z ? TimerEnableType.TIMER_ENABLE : TimerEnableType.TIMER_ENABLE_NONE;
        this.timeZone = i;
    }

    private MeshTimerModel(TimerType timerType, String str, String str2, String str3, boolean z, int i, String str4, int i2, int i3) {
        this.timerType = timerType;
        this.attributesTargets = str;
        this.time = str2;
        this.days = str3;
        this.enableType = z ? TimerEnableType.TIMER_ENABLE : TimerEnableType.TIMER_ENABLE_NONE;
        this.timeZone = i;
        this.endTime = str4;
        this.runTime = i2;
        this.sleepTime = i3;
    }

    public static MeshTimerModel fromTimerAttributeValueItem(DeviceTimerAttributeModel.ValueItem valueItem) {
        if (valueItem == null) {
            return null;
        }
        MeshTimerModel meshTimerModel = new MeshTimerModel();
        meshTimerModel.timerType = TimerType.fromValue(valueItem.Y);
        meshTimerModel.enableType = TimerEnableType.fromTypeValue(valueItem.E);
        if (meshTimerModel.timerType == TimerType.TIMER_NONE) {
            return meshTimerModel;
        }
        meshTimerModel.attributesTargets = valueItem.A;
        Pair<String, String> timeAndDaysFromCronExpression = getTimeAndDaysFromCronExpression(meshTimerModel.timerType, valueItem.T);
        meshTimerModel.time = (String) timeAndDaysFromCronExpression.first;
        meshTimerModel.days = (String) timeAndDaysFromCronExpression.second;
        meshTimerModel.timeZone = valueItem.Z;
        if (meshTimerModel.timerType == TimerType.TIMER_CIRCULATION) {
            meshTimerModel.endTime = valueItem.N;
            meshTimerModel.runTime = valueItem.R;
            meshTimerModel.sleepTime = valueItem.S;
        }
        return meshTimerModel;
    }

    public String getTimerID() {
        return this.timerID;
    }

    public void setTimerID(String str) {
        this.timerID = str;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String str) {
        this.time = str;
    }

    public String getDays() {
        return this.days;
    }

    public void setDays(String str) {
        this.days = str;
    }

    public TimerType getTimerType() {
        return this.timerType;
    }

    public TimerEnableType getEnableType() {
        return this.enableType;
    }

    public void setEnableType(TimerEnableType timerEnableType) {
        this.enableType = timerEnableType;
    }

    public String getAttributesTargets() {
        return this.attributesTargets;
    }

    public void setAttributesTargets(String str) {
        this.attributesTargets = str;
    }

    public int getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(int i) {
        this.timeZone = i;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String str) {
        this.endTime = str;
    }

    public long getRunTime() {
        return this.runTime;
    }

    public void setRunTime(int i) {
        this.runTime = i;
    }

    public int getSleepTime() {
        return this.sleepTime;
    }

    public void setSleepTime(int i) {
        this.sleepTime = i;
    }

    public DeviceTimerAttributeModel.ValueItem toAttributeModel() {
        DeviceTimerAttributeModel.ValueItem valueItem = new DeviceTimerAttributeModel.ValueItem();
        switch (this.timerType) {
            case TIMER_NONE:
                if (TextUtils.isEmpty(this.time) || this.time.length() == 0) {
                    valueItem.Y = 0;
                    valueItem.E = 0;
                    return valueItem;
                }
                break;
            case TIMER_CIRCULATION:
                valueItem.R = this.runTime;
                valueItem.S = this.sleepTime;
                valueItem.N = this.endTime;
                break;
        }
        valueItem.A = this.attributesTargets;
        valueItem.T = getCronExpression();
        valueItem.Y = this.timerType.getTypeValue();
        valueItem.Z = this.timeZone;
        valueItem.E = this.enableType == TimerEnableType.TIMER_ENABLE ? 1 : 0;
        return valueItem;
    }

    private static Pair<String, String> getTimeAndDaysFromCronExpression(TimerType timerType, String str) {
        String[] strArrSplit = str.split("\\s+");
        if (strArrSplit.length < 6) {
            return new Pair<>("", "");
        }
        Calendar calendar = Calendar.getInstance();
        if (TimerType.TIMER_COUNTDOWN == timerType) {
            calendar.set(Integer.parseInt(strArrSplit[5]), Integer.parseInt(strArrSplit[3]) - 1, Integer.parseInt(strArrSplit[2]), Integer.parseInt(strArrSplit[1]), Integer.parseInt(strArrSplit[0]));
            return new Pair<>(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime()), "");
        }
        calendar.set(11, Integer.parseInt(strArrSplit[1]));
        calendar.set(12, Integer.parseInt(strArrSplit[0]));
        String str2 = new SimpleDateFormat("HH:mm").format(calendar.getTime());
        String str3 = strArrSplit[4];
        if ("?".equals(str3)) {
            str3 = "";
        }
        return new Pair<>(str2, str3);
    }

    private String getCronExpression() {
        String strValueOf;
        String strValueOf2;
        String strValueOf3;
        String str;
        String strValueOf4;
        String strValueOf5 = "";
        switch (this.timerType) {
            case TIMER_NORMAL:
            case TIMER_CIRCULATION:
                strValueOf5 = WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD;
                strValueOf = "?";
                String[] strArrSplit = this.time.split(":");
                strValueOf2 = strArrSplit[0];
                strValueOf3 = strArrSplit[1];
                str = this.days;
                if (TextUtils.isEmpty(str) || str.length() == 0) {
                    str = "?";
                    Date date = new Date();
                    GregorianCalendar gregorianCalendar = new GregorianCalendar();
                    gregorianCalendar.setTime(date);
                    String strValueOf6 = String.valueOf(gregorianCalendar.get(5));
                    String strValueOf7 = String.valueOf(gregorianCalendar.get(2) + 1);
                    String strValueOf8 = String.valueOf(gregorianCalendar.get(1));
                    try {
                        Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(strValueOf8 + Constants.ACCEPT_TIME_SEPARATOR_SERVER + strValueOf7 + Constants.ACCEPT_TIME_SEPARATOR_SERVER + strValueOf6 + " " + strValueOf2 + ":" + strValueOf3);
                        if (date.compareTo(date2) >= 0) {
                            gregorianCalendar.setTime(new Date(date2.getTime() + 86400000));
                            strValueOf6 = String.valueOf(gregorianCalendar.get(5));
                            strValueOf7 = String.valueOf(gregorianCalendar.get(2) + 1);
                            strValueOf8 = String.valueOf(gregorianCalendar.get(1));
                        }
                        strValueOf5 = strValueOf8;
                        strValueOf4 = strValueOf7;
                        strValueOf = strValueOf6;
                    } catch (ParseException e) {
                        e.printStackTrace();
                        strValueOf5 = strValueOf8;
                        strValueOf4 = strValueOf7;
                        strValueOf = strValueOf6;
                    }
                } else {
                    strValueOf4 = WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD;
                }
                break;
            case TIMER_COUNTDOWN:
                str = "?";
                try {
                    Date date3 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(this.time);
                    GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
                    gregorianCalendar2.setTime(date3);
                    strValueOf3 = String.valueOf(gregorianCalendar2.get(12));
                    try {
                        strValueOf2 = String.valueOf(gregorianCalendar2.get(11));
                        try {
                            strValueOf = String.valueOf(gregorianCalendar2.get(5));
                            try {
                                strValueOf4 = String.valueOf(gregorianCalendar2.get(2) + 1);
                                try {
                                    strValueOf5 = String.valueOf(gregorianCalendar2.get(1));
                                } catch (ParseException e2) {
                                    e = e2;
                                    e.printStackTrace();
                                }
                            } catch (ParseException e3) {
                                e = e3;
                                strValueOf4 = "";
                            }
                        } catch (ParseException e4) {
                            e = e4;
                            strValueOf = "";
                            strValueOf4 = strValueOf;
                            e.printStackTrace();
                            return String.format("%s %s %s %s %s %s", strValueOf3, strValueOf2, strValueOf, strValueOf4, str, strValueOf5);
                        }
                    } catch (ParseException e5) {
                        e = e5;
                        strValueOf2 = "";
                        strValueOf = strValueOf2;
                        strValueOf4 = strValueOf;
                        e.printStackTrace();
                        return String.format("%s %s %s %s %s %s", strValueOf3, strValueOf2, strValueOf, strValueOf4, str, strValueOf5);
                    }
                } catch (ParseException e6) {
                    e = e6;
                    strValueOf3 = "";
                    strValueOf2 = strValueOf3;
                }
                break;
            default:
                return "";
        }
        return String.format("%s %s %s %s %s %s", strValueOf3, strValueOf2, strValueOf, strValueOf4, str, strValueOf5);
    }

    private static class BaseTimerBuilder<T> {
        protected boolean mEnable;
        protected String mTargets;
        protected String mTime;
        protected int mTimeZone;

        private BaseTimerBuilder() {
            this.mEnable = true;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public T setTime(String str) {
            this.mTime = str;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public T setTargets(String str) {
            this.mTargets = str;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public T setTimeZone(int i) {
            this.mTimeZone = i;
            return this;
        }
    }

    public static class NormalTimerBuilder extends BaseTimerBuilder<NormalTimerBuilder> {
        private String mDays;

        public NormalTimerBuilder() {
            super();
        }

        public NormalTimerBuilder setDays(String str) {
            this.mDays = str;
            return this;
        }

        public MeshTimerModel build() {
            return new MeshTimerModel(TimerType.TIMER_NORMAL, this.mTargets, this.mTime, this.mDays, this.mEnable, this.mTimeZone);
        }
    }

    public static class CountDownTimerBuilder extends BaseTimerBuilder<CountDownTimerBuilder> {
        public CountDownTimerBuilder() {
            super();
        }

        public MeshTimerModel build() {
            return new MeshTimerModel(TimerType.TIMER_COUNTDOWN, this.mTargets, this.mTime, "", this.mEnable, this.mTimeZone);
        }
    }

    public static class CirculationTimerBuilder extends BaseTimerBuilder<CirculationTimerBuilder> {
        private String mDays;
        private String mEndTime;
        private int mRunTime;
        private int mSleepTime;

        public CirculationTimerBuilder() {
            super();
        }

        public CirculationTimerBuilder setEndTime(String str) {
            this.mEndTime = str;
            return this;
        }

        public CirculationTimerBuilder setRunTime(int i) {
            this.mRunTime = i;
            return this;
        }

        public CirculationTimerBuilder setSleepTime(int i) {
            this.mSleepTime = i;
            return this;
        }

        public CirculationTimerBuilder setDays(String str) {
            this.mDays = str;
            return this;
        }

        public MeshTimerModel build() {
            return new MeshTimerModel(TimerType.TIMER_CIRCULATION, this.mTargets, this.mTime, this.mDays, this.mEnable, this.mTimeZone, this.mEndTime, this.mRunTime, this.mSleepTime);
        }
    }
}
