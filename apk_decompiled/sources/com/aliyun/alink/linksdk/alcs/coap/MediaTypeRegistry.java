package com.aliyun.alink.linksdk.alcs.coap;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.multipart.HttpPostBodyUtil;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes2.dex */
public class MediaTypeRegistry {
    public static final int APPLICATION_ATOM_XML = 45;
    public static final int APPLICATION_CBOR = 60;
    public static final int APPLICATION_EXI = 47;
    public static final int APPLICATION_FASTINFOSET = 48;
    public static final int APPLICATION_JSON = 50;
    public static final int APPLICATION_LINK_FORMAT = 40;
    public static final int APPLICATION_OCTET_STREAM = 42;
    public static final int APPLICATION_RDF_XML = 43;
    public static final int APPLICATION_SOAP_FASTINFOSET = 49;
    public static final int APPLICATION_SOAP_XML = 44;
    public static final int APPLICATION_VND_OMA_LWM2M_JSON = 11543;
    public static final int APPLICATION_VND_OMA_LWM2M_TLV = 11542;
    public static final int APPLICATION_XML = 41;
    public static final int APPLICATION_XMPP_XML = 46;
    public static final int APPLICATION_X_OBIX_BINARY = 51;
    public static final int AUDIO_RAW = 25;
    public static final int IMAGE_GIF = 21;
    public static final int IMAGE_JPEG = 22;
    public static final int IMAGE_PNG = 23;
    public static final int IMAGE_TIFF = 24;
    public static final int TEXT_CSV = 2;
    public static final int TEXT_HTML = 3;
    public static final int TEXT_PLAIN = 0;
    public static final int TEXT_XML = 1;
    public static final int UNDEFINED = -1;
    public static final int VIDEO_RAW = 26;
    private static final HashMap<Integer, String[]> registry = new HashMap<>();

    public static boolean isPrintable(int i) {
        if (i == 50 || i == 11543) {
            return true;
        }
        switch (i) {
            case -1:
            case 0:
            case 1:
            case 2:
            case 3:
                return true;
            default:
                switch (i) {
                    case 40:
                    case 41:
                        return true;
                    default:
                        switch (i) {
                            case 43:
                            case 44:
                            case 45:
                            case 46:
                                return true;
                            default:
                                return false;
                        }
                }
        }
    }

    static {
        add(-1, "unknown", "???");
        add(0, HttpPostBodyUtil.DEFAULT_TEXT_CONTENT_TYPE, "txt");
        add(2, "text/csv", "csv");
        add(3, "text/html", "html");
        add(21, "image/gif", "gif");
        add(22, "image/jpeg", "jpg");
        add(23, "image/png", "png");
        add(24, "image/tiff", "tif");
        add(40, "application/link-format", "wlnk");
        add(41, "application/xml", "xml");
        add(42, "application/octet-stream", "bin");
        add(43, "application/rdf+xml", "rdf");
        add(44, "application/soap+xml", "soap");
        add(45, "application/atom+xml", "atom");
        add(46, "application/xmpp+xml", "xmpp");
        add(47, "application/exi", "exi");
        add(48, "application/fastinfoset", "finf");
        add(49, "application/soap+fastinfoset", "soap.finf");
        add(50, HttpHeaders.Values.APPLICATION_JSON, "json");
        add(51, "application/x-obix-binary", "obix");
        add(60, "application/cbor", "cbor");
        add(APPLICATION_VND_OMA_LWM2M_TLV, "application/vnd.oma.lwm2m+tlv", "tlv");
        add(APPLICATION_VND_OMA_LWM2M_JSON, "application/vnd.oma.lwm2m+json", "json");
    }

    public static Set<Integer> getAllMediaTypes() {
        return registry.keySet();
    }

    public static int parse(String str) {
        if (str == null) {
            return -1;
        }
        for (Integer num : registry.keySet()) {
            if (registry.get(num)[0].equalsIgnoreCase(str)) {
                return num.intValue();
            }
        }
        return -1;
    }

    public static Integer[] parseWildcard(String str) {
        Pattern patternCompile = Pattern.compile(str.trim().substring(0, str.indexOf(42)).trim().concat(".*"));
        LinkedList linkedList = new LinkedList();
        for (Integer num : registry.keySet()) {
            if (patternCompile.matcher(registry.get(num)[0]).matches()) {
                linkedList.add(num);
            }
        }
        return (Integer[]) linkedList.toArray(new Integer[0]);
    }

    public static String toFileExtension(int i) {
        String[] strArr = registry.get(Integer.valueOf(i));
        if (strArr != null) {
            return strArr[1];
        }
        return "unknown_" + i;
    }

    public static String toString(int i) {
        String[] strArr = registry.get(Integer.valueOf(i));
        if (strArr != null) {
            return strArr[0];
        }
        return "unknown/" + i;
    }

    private static void add(int i, String str, String str2) {
        registry.put(Integer.valueOf(i), new String[]{str, str2});
    }
}
