package com.alibaba.ailabs.tg.utils;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ListUtils {
    public static final String DEFAULT_JOIN_SEPARATOR = ",";

    private ListUtils() {
    }

    public static <V> int getSize(List<V> list) {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public static <V> boolean isEmpty(List<V> list) {
        return list == null || list.size() == 0;
    }

    public static String join(List<String> list) {
        return join(list, ",");
    }

    public static String join(List<String> list, char c2) {
        return join(list, new String(new char[]{c2}));
    }

    public static String join(List<String> list, String str) {
        return list == null ? "" : TextUtils.join(str, list);
    }

    public static <V> List<V> invertList(List<V> list) {
        if (isEmpty(list)) {
            return list;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (int size = list.size() - 1; size >= 0; size--) {
            arrayList.add(list.get(size));
        }
        return arrayList;
    }

    public static <V> boolean isEquals(ArrayList<V> arrayList, ArrayList<V> arrayList2) {
        if (arrayList == null) {
            return arrayList2 == null;
        }
        if (arrayList2 == null || arrayList.size() != arrayList2.size()) {
            return false;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (!ObjectUtils.isEquals(arrayList.get(i), arrayList2.get(i))) {
                return false;
            }
        }
        return true;
    }
}
