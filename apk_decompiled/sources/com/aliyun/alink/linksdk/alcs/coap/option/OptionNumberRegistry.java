package com.aliyun.alink.linksdk.alcs.coap.option;

/* JADX INFO: loaded from: classes2.dex */
public class OptionNumberRegistry {
    public static final int ACCEPT = 17;
    public static final int BLOCK1 = 27;
    public static final int BLOCK2 = 23;
    public static final int CONTENT_FORMAT = 12;
    public static final int ETAG = 4;
    public static final int IF_MATCH = 1;
    public static final int IF_NONE_MATCH = 5;
    public static final int LOCATION_PATH = 8;
    public static final int LOCATION_QUERY = 20;
    public static final int MAX_AGE = 14;
    public static final int OBSERVE = 6;
    public static final int PROXY_SCHEME = 39;
    public static final int PROXY_URI = 35;
    public static final int RESERVED_0 = 0;
    public static final int RESERVED_1 = 128;
    public static final int RESERVED_2 = 132;
    public static final int RESERVED_3 = 136;
    public static final int RESERVED_4 = 140;
    public static final int SIZE1 = 60;
    public static final int SIZE2 = 28;
    public static final int UNKNOWN = -1;
    public static final int URI_HOST = 3;
    public static final int URI_PATH = 11;
    public static final int URI_PORT = 7;
    public static final int URI_QUERY = 15;

    public static class Defaults {
        public static final long MAX_AGE = 60;
    }

    public static class Names {
        public static final String Accept = "Accept";
        public static final String Block1 = "Block1";
        public static final String Block2 = "Block2";
        public static final String Content_Format = "Content-Format";
        public static final String ETag = "ETag";
        public static final String If_Match = "If-Match";
        public static final String If_None_Match = "If-None-Match";
        public static final String Location_Path = "Location-Path";
        public static final String Location_Query = "Location-Query";
        public static final String Max_Age = "Max-Age";
        public static final String Observe = "Observe";
        public static final String Proxy_Scheme = "Proxy-Scheme";
        public static final String Proxy_Uri = "Proxy-Uri";
        public static final String Reserved = "Reserved";
        public static final String Size1 = "Size1";
        public static final String Size2 = "Size2";
        public static final String Uri_Host = "Uri-Host";
        public static final String Uri_Path = "Uri-Path";
        public static final String Uri_Port = "Uri-Port";
        public static final String Uri_Query = "Uri-Query";
    }

    public enum optionFormats {
        INTEGER,
        STRING,
        OPAQUE,
        UNKNOWN
    }

    public static boolean isCritical(int i) {
        return (i & 1) != 0;
    }

    public static boolean isElective(int i) {
        return (i & 1) == 0;
    }

    public static boolean isNoCacheKey(int i) {
        return (i & 30) == 28;
    }

    public static boolean isSingleValue(int i) {
        return (i == 1 || i == 4 || i == 8 || i == 11 || i == 15 || i == 20) ? false : true;
    }

    public static boolean isUnsafe(int i) {
        return (i & 2) > 0;
    }

    public static boolean isUriOption(int i) {
        return i == 3 || i == 11 || i == 7 || i == 15;
    }

    public static optionFormats getFormatByNr(int i) {
        switch (i) {
            case 1:
            case 4:
                return optionFormats.OPAQUE;
            case 3:
            case 8:
            case 11:
            case 15:
            case 20:
            case 35:
            case 39:
                return optionFormats.STRING;
            case 5:
            case 6:
            case 7:
            case 12:
            case 14:
            case 17:
            case 23:
            case 27:
            case 28:
            case 60:
                return optionFormats.INTEGER;
            default:
                return optionFormats.UNKNOWN;
        }
    }

    public static boolean isSafe(int i) {
        return !isUnsafe(i);
    }

    public static boolean isCacheKey(int i) {
        return !isNoCacheKey(i);
    }

    public static String toString(int i) {
        switch (i) {
            case 0:
            case 128:
            case 132:
            case 136:
            case 140:
                return Names.Reserved;
            case 1:
                return "If-Match";
            case 3:
                return Names.Uri_Host;
            case 4:
                return "ETag";
            case 5:
                return "If-None-Match";
            case 6:
                return Names.Observe;
            case 7:
                return Names.Uri_Port;
            case 8:
                return Names.Location_Path;
            case 11:
                return Names.Uri_Path;
            case 12:
                return Names.Content_Format;
            case 14:
                return "Max-Age";
            case 15:
                return Names.Uri_Query;
            case 17:
                return "Accept";
            case 20:
                return Names.Location_Query;
            case 23:
                return Names.Block2;
            case 27:
                return Names.Block1;
            case 28:
                return Names.Size2;
            case 35:
                return Names.Proxy_Uri;
            case 39:
                return Names.Proxy_Scheme;
            case 60:
                return Names.Size1;
            default:
                return String.format("Unknown (%d)", Integer.valueOf(i));
        }
    }

    public static int toNumber(String str) {
        if ("If-Match".equals(str)) {
            return 1;
        }
        if (Names.Uri_Host.equals(str)) {
            return 3;
        }
        if ("ETag".equals(str)) {
            return 4;
        }
        if ("If-None-Match".equals(str)) {
            return 5;
        }
        if (Names.Uri_Port.equals(str)) {
            return 7;
        }
        if (Names.Location_Path.equals(str)) {
            return 8;
        }
        if (Names.Uri_Path.equals(str)) {
            return 11;
        }
        if (Names.Content_Format.equals(str)) {
            return 12;
        }
        if ("Max-Age".equals(str)) {
            return 14;
        }
        if (Names.Uri_Query.equals(str)) {
            return 15;
        }
        if ("Accept".equals(str)) {
            return 17;
        }
        if (Names.Location_Query.equals(str)) {
            return 20;
        }
        if (Names.Proxy_Uri.equals(str)) {
            return 35;
        }
        if (Names.Proxy_Scheme.equals(str)) {
            return 39;
        }
        if (Names.Observe.equals(str)) {
            return 6;
        }
        if (Names.Block2.equals(str)) {
            return 23;
        }
        if (Names.Block1.equals(str)) {
            return 27;
        }
        if (Names.Size2.equals(str)) {
            return 28;
        }
        return Names.Size1.equals(str) ? 60 : -1;
    }

    private OptionNumberRegistry() {
    }
}
