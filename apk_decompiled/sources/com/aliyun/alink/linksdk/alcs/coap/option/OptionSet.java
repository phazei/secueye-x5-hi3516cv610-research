package com.aliyun.alink.linksdk.alcs.coap.option;

import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.alcs.coap.Utils;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class OptionSet {
    private static final int MAX_OBSERVE_NO = 16777215;
    private static final String TAG = "OptionSet";
    private Integer accept;
    private BlockOption block1;
    private BlockOption block2;
    private Integer content_format;
    private List<byte[]> etag_list;
    private List<byte[]> if_match_list;
    private boolean if_none_match;
    private List<String> location_path_list;
    private List<String> location_query_list;
    private Long max_age;
    private Integer observe;
    private List<Option> others;
    private String proxy_scheme;
    private String proxy_uri;
    private Integer size1;
    private Integer size2;
    private String uri_host;
    private List<String> uri_path_list;
    private Integer uri_port;
    private List<String> uri_query_list;

    public static boolean isValidObserveOption(int i) {
        return i >= 0 && i <= 16777215;
    }

    public OptionSet() {
        this.if_match_list = null;
        this.uri_host = null;
        this.etag_list = null;
        this.if_none_match = false;
        this.uri_port = null;
        this.location_path_list = null;
        this.uri_path_list = null;
        this.content_format = null;
        this.max_age = null;
        this.uri_query_list = null;
        this.accept = null;
        this.location_query_list = null;
        this.proxy_uri = null;
        this.proxy_scheme = null;
        this.block1 = null;
        this.block2 = null;
        this.size1 = null;
        this.size2 = null;
        this.observe = null;
        this.others = null;
    }

    public void clear() {
        List<byte[]> list = this.if_match_list;
        if (list != null) {
            list.clear();
        }
        this.uri_host = null;
        List<byte[]> list2 = this.etag_list;
        if (list2 != null) {
            list2.clear();
        }
        this.if_none_match = false;
        this.uri_port = null;
        List<String> list3 = this.location_path_list;
        if (list3 != null) {
            list3.clear();
        }
        List<String> list4 = this.uri_path_list;
        if (list4 != null) {
            list4.clear();
        }
        this.content_format = null;
        this.max_age = null;
        List<String> list5 = this.uri_query_list;
        if (list5 != null) {
            list5.clear();
        }
        this.accept = null;
        if (this.location_query_list != null) {
            this.location_path_list.clear();
        }
        this.proxy_uri = null;
        this.proxy_scheme = null;
        this.block1 = null;
        this.block2 = null;
        this.observe = null;
        List<Option> list6 = this.others;
        if (list6 != null) {
            list6.clear();
        }
    }

    public OptionSet(OptionSet optionSet) {
        if (optionSet == null) {
            ALog.e(TAG, "origin emtpy");
            return;
        }
        this.if_match_list = copyList(optionSet.if_match_list);
        this.uri_host = optionSet.uri_host;
        this.etag_list = copyList(optionSet.etag_list);
        this.if_none_match = optionSet.if_none_match;
        this.uri_port = optionSet.uri_port;
        this.location_path_list = copyList(optionSet.location_path_list);
        this.uri_path_list = copyList(optionSet.uri_path_list);
        this.content_format = optionSet.content_format;
        this.max_age = optionSet.max_age;
        this.uri_query_list = copyList(optionSet.uri_query_list);
        this.accept = optionSet.accept;
        this.location_query_list = copyList(optionSet.location_query_list);
        this.proxy_uri = optionSet.proxy_uri;
        this.proxy_scheme = optionSet.proxy_scheme;
        BlockOption blockOption = optionSet.block1;
        if (blockOption != null) {
            this.block1 = new BlockOption(blockOption);
        }
        BlockOption blockOption2 = optionSet.block2;
        if (blockOption2 != null) {
            this.block2 = new BlockOption(blockOption2);
        }
        this.observe = optionSet.observe;
        this.others = copyList(optionSet.others);
    }

    private <T> List<T> copyList(List<T> list) {
        if (list == null) {
            return null;
        }
        return new LinkedList(list);
    }

    public List<byte[]> getIfMatch() {
        synchronized (this) {
            if (this.if_match_list == null) {
                this.if_match_list = new LinkedList();
            }
        }
        return this.if_match_list;
    }

    public int getIfMatchCount() {
        return getIfMatch().size();
    }

    public boolean isIfMatch(byte[] bArr) {
        List<byte[]> list = this.if_match_list;
        if (list == null) {
            return true;
        }
        for (byte[] bArr2 : list) {
            if (bArr2.length == 0 || Arrays.equals(bArr2, bArr)) {
                return true;
            }
        }
        return false;
    }

    public OptionSet addIfMatch(byte[] bArr) {
        if (bArr == null) {
            ALog.e(TAG, "If-Match option must not be null");
            return this;
        }
        if (bArr.length > 8) {
            ALog.e(TAG, "If-Match option must be smaller or equal to 8 bytes: " + Utils.toHexString(bArr));
            return this;
        }
        getIfMatch().add(bArr);
        return this;
    }

    public OptionSet removeIfMatch(byte[] bArr) {
        getIfMatch().remove(bArr);
        return this;
    }

    public OptionSet clearIfMatchs() {
        getIfMatch().clear();
        return this;
    }

    public String getUriHost() {
        return this.uri_host;
    }

    public boolean hasUriHost() {
        return this.uri_host != null;
    }

    public OptionSet setUriHost(String str) {
        if (str == null) {
            ALog.e(TAG, "URI-Host must not be null");
            return this;
        }
        if (str.length() < 1 || 255 < str.length()) {
            ALog.e(TAG, "URI-Host option's length must be between 1 and 255 inclusive");
            return this;
        }
        this.uri_host = str;
        return this;
    }

    public OptionSet removeUriHost() {
        this.uri_host = null;
        return this;
    }

    public List<byte[]> getETags() {
        synchronized (this) {
            if (this.etag_list == null) {
                this.etag_list = new LinkedList();
            }
        }
        return this.etag_list;
    }

    public int getETagCount() {
        return getETags().size();
    }

    public boolean containsETag(byte[] bArr) {
        List<byte[]> list = this.etag_list;
        if (list == null) {
            return false;
        }
        Iterator<byte[]> it = list.iterator();
        while (it.hasNext()) {
            if (Arrays.equals(it.next(), bArr)) {
                return true;
            }
        }
        return false;
    }

    public OptionSet addETag(byte[] bArr) {
        if (bArr == null) {
            ALog.e(TAG, "ETag option must not be null");
            return this;
        }
        getETags().add(bArr);
        return this;
    }

    public OptionSet removeETag(byte[] bArr) {
        getETags().remove(bArr);
        return this;
    }

    public OptionSet clearETags() {
        getETags().clear();
        return this;
    }

    public boolean hasIfNoneMatch() {
        return this.if_none_match;
    }

    public OptionSet setIfNoneMatch(boolean z) {
        this.if_none_match = z;
        return this;
    }

    public Integer getUriPort() {
        return this.uri_port;
    }

    public boolean hasUriPort() {
        return this.uri_port != null;
    }

    public OptionSet setUriPort(int i) {
        if (i < 0 || 65535 < i) {
            ALog.e(TAG, "URI port option must be between 0 and 65535 (2 bytes) inclusive but was " + i);
            return this;
        }
        this.uri_port = Integer.valueOf(i);
        return this;
    }

    public OptionSet removeUriPort() {
        this.uri_port = null;
        return this;
    }

    public List<String> getLocationPath() {
        synchronized (this) {
            if (this.location_path_list == null) {
                this.location_path_list = new LinkedList();
            }
        }
        return this.location_path_list;
    }

    public String getLocationString() {
        StringBuilder sb = new StringBuilder();
        sb.append("/");
        sb.append(getLocationPathString());
        if (getLocationQueryCount() > 0) {
            sb.append("?");
            sb.append(getLocationQueryString());
        }
        return sb.toString();
    }

    public String getLocationPathString() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = getLocationPath().iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            sb.append("/");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    public int getLocationPathCount() {
        return getLocationPath().size();
    }

    public OptionSet addLocationPath(String str) {
        if (str == null) {
            ALog.e(TAG, "Location-Path option must not be null");
            return this;
        }
        if (str.getBytes(AlcsCoAPConstant.UTF8_CHARSET).length > 255) {
            ALog.e(TAG, "Location-Path option must be smaller or euqal to 255 bytes (UTF-8 encoded): " + str);
            return this;
        }
        getLocationPath().add(str);
        return this;
    }

    public OptionSet clearLocationPath() {
        getLocationPath().clear();
        return this;
    }

    public OptionSet setLocationPath(String str) {
        if (str.startsWith("/")) {
            str = str.substring(1);
        }
        clearLocationPath();
        for (String str2 : str.split("/")) {
            addLocationPath(str2);
        }
        return this;
    }

    public List<String> getUriPath() {
        synchronized (this) {
            if (this.uri_path_list == null) {
                this.uri_path_list = new LinkedList();
            }
        }
        return this.uri_path_list;
    }

    public String getUriPathString() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = getUriPath().iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            sb.append("/");
        }
        return sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1);
    }

    public int getURIPathCount() {
        return getUriPath().size();
    }

    public OptionSet setUriPath(String str) {
        if (str.startsWith("/")) {
            str = str.substring(1);
        }
        clearUriPath();
        for (String str2 : str.split("/")) {
            addUriPath(str2);
        }
        return this;
    }

    public OptionSet addUriPath(String str) {
        if (str == null) {
            ALog.e(TAG, "segment empty");
            return this;
        }
        if (str.getBytes(AlcsCoAPConstant.UTF8_CHARSET).length > 255) {
            ALog.e(TAG, "Uri-Path option must be smaller or euqal to 255 bytes (UTF-8 encoded): " + str);
            return this;
        }
        getUriPath().add(str);
        return this;
    }

    public OptionSet clearUriPath() {
        getUriPath().clear();
        return this;
    }

    public int getContentFormat() {
        if (hasContentFormat()) {
            return this.content_format.intValue();
        }
        return -1;
    }

    public boolean hasContentFormat() {
        return this.content_format != null;
    }

    public boolean isContentFormat(int i) {
        Integer num = this.content_format;
        return num != null && num.intValue() == i;
    }

    public OptionSet setContentFormat(int i) {
        if (i > -1) {
            this.content_format = Integer.valueOf(i);
        } else {
            this.content_format = null;
        }
        return this;
    }

    public OptionSet removeContentFormat() {
        this.content_format = null;
        return this;
    }

    public Long getMaxAge() {
        Long l = this.max_age;
        return Long.valueOf(l != null ? l.longValue() : 60L);
    }

    public boolean hasMaxAge() {
        return this.max_age != null;
    }

    public OptionSet setMaxAge(long j) {
        if (j < 0 || 4294967295L < j) {
            ALog.e(TAG, "Max-Age option must be between 0 and 4294967295 (4 bytes) inclusive");
        }
        this.max_age = Long.valueOf(j);
        return this;
    }

    public OptionSet removeMaxAge() {
        this.max_age = null;
        return this;
    }

    public List<String> getUriQuery() {
        synchronized (this) {
            if (this.uri_query_list == null) {
                this.uri_query_list = new LinkedList();
            }
        }
        return this.uri_query_list;
    }

    public int getURIQueryCount() {
        return getUriQuery().size();
    }

    public String getUriQueryString() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = getUriQuery().iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            sb.append("&");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    public OptionSet setUriQuery(String str) {
        while (str.startsWith("?")) {
            str = str.substring(1);
        }
        clearUriQuery();
        for (String str2 : str.split("&")) {
            if (!str2.isEmpty()) {
                addUriQuery(str2);
            }
        }
        return this;
    }

    public OptionSet addUriQuery(String str) {
        if (str == null) {
            ALog.e(TAG, "Uri-Query option must not be null");
            return this;
        }
        if (str.getBytes(AlcsCoAPConstant.UTF8_CHARSET).length > 255) {
            ALog.e(TAG, "Uri-Query option must be smaller or euqal to 255 bytes (UTF-8 encoded): " + str);
            return this;
        }
        getUriQuery().add(str);
        return this;
    }

    public OptionSet removeUriQuery(String str) {
        getUriQuery().remove(str);
        return this;
    }

    public OptionSet clearUriQuery() {
        getUriQuery().clear();
        return this;
    }

    public int getAccept() {
        if (hasAccept()) {
            return this.accept.intValue();
        }
        return -1;
    }

    public boolean hasAccept() {
        return this.accept != null;
    }

    public boolean isAccept(int i) {
        Integer num = this.accept;
        return num != null && num.intValue() == i;
    }

    public OptionSet setAccept(int i) {
        if (i < 0 || i > 65535) {
            ALog.e(TAG, "Accept option must be between 0 and 65535 (2 bytes) inclusive");
            return this;
        }
        this.accept = Integer.valueOf(i);
        return this;
    }

    public OptionSet removeAccept() {
        this.accept = null;
        return this;
    }

    public List<String> getLocationQuery() {
        synchronized (this) {
            if (this.location_query_list == null) {
                this.location_query_list = new LinkedList();
            }
        }
        return this.location_query_list;
    }

    public int getLocationQueryCount() {
        return getLocationQuery().size();
    }

    public String getLocationQueryString() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = getLocationQuery().iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            sb.append("&");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    public OptionSet setLocationQuery(String str) {
        while (str.startsWith("?")) {
            str = str.substring(1);
        }
        clearLocationQuery();
        for (String str2 : str.split("&")) {
            if (!str2.isEmpty()) {
                addLocationQuery(str2);
            }
        }
        return this;
    }

    public OptionSet addLocationQuery(String str) {
        if (str == null) {
            ALog.e(TAG, "Location-Query option must not be null");
            return this;
        }
        if (str.getBytes(AlcsCoAPConstant.UTF8_CHARSET).length > 255) {
            ALog.e(TAG, "Location-Query option must be smaller or euqal to 255 bytes (UTF-8 encoded): " + str);
            return this;
        }
        getLocationQuery().add(str);
        return this;
    }

    public OptionSet removeLocationQuery(String str) {
        getLocationQuery().remove(str);
        return this;
    }

    public OptionSet clearLocationQuery() {
        getLocationQuery().clear();
        return this;
    }

    public String getProxyUri() {
        return this.proxy_uri;
    }

    public boolean hasProxyUri() {
        return this.proxy_uri != null;
    }

    public OptionSet setProxyUri(String str) {
        if (str == null) {
            ALog.e(TAG, "Proxy-Uri option must not be null");
            return this;
        }
        if (str.getBytes(AlcsCoAPConstant.UTF8_CHARSET).length < 1 || 1034 < str.getBytes(AlcsCoAPConstant.UTF8_CHARSET).length) {
            ALog.e(TAG, "Proxy-Uri option must be between 1 and 1034 bytes inclusive (UTF-8 encoded): " + str);
            return this;
        }
        this.proxy_uri = str;
        return this;
    }

    public OptionSet removeProxyUri() {
        this.proxy_uri = null;
        return this;
    }

    public String getProxyScheme() {
        return this.proxy_scheme;
    }

    public boolean hasProxyScheme() {
        return this.proxy_scheme != null;
    }

    public OptionSet setProxyScheme(String str) {
        if (str == null) {
            ALog.e(TAG, "Proxy-Scheme option must not be null");
            return this;
        }
        if (str.getBytes(AlcsCoAPConstant.UTF8_CHARSET).length < 1 || 255 < str.getBytes(AlcsCoAPConstant.UTF8_CHARSET).length) {
            ALog.e(TAG, "Proxy-Scheme option must be between 1 and 255 bytes inclusive (UTF-8 encoded): " + str);
            return this;
        }
        this.proxy_scheme = str;
        return this;
    }

    public OptionSet removeProxyScheme() {
        this.proxy_scheme = null;
        return this;
    }

    public BlockOption getBlock1() {
        return this.block1;
    }

    public boolean hasBlock1() {
        return this.block1 != null;
    }

    public OptionSet setBlock1(int i, boolean z, int i2) {
        this.block1 = new BlockOption(i, z, i2);
        return this;
    }

    public OptionSet setBlock1(byte[] bArr) {
        this.block1 = new BlockOption(bArr);
        return this;
    }

    public OptionSet setBlock1(BlockOption blockOption) {
        this.block1 = blockOption;
        return this;
    }

    public OptionSet removeBlock1() {
        this.block1 = null;
        return this;
    }

    public BlockOption getBlock2() {
        return this.block2;
    }

    public boolean hasBlock2() {
        return this.block2 != null;
    }

    public OptionSet setBlock2(int i, boolean z, int i2) {
        this.block2 = new BlockOption(i, z, i2);
        return this;
    }

    public OptionSet setBlock2(byte[] bArr) {
        this.block2 = new BlockOption(bArr);
        return this;
    }

    public OptionSet setBlock2(BlockOption blockOption) {
        this.block2 = blockOption;
        return this;
    }

    public OptionSet removeBlock2() {
        this.block2 = null;
        return this;
    }

    public Integer getSize1() {
        return this.size1;
    }

    public boolean hasSize1() {
        return this.size1 != null;
    }

    public OptionSet setSize1(int i) {
        this.size1 = Integer.valueOf(i);
        return this;
    }

    public OptionSet removeSize1() {
        this.size1 = null;
        return this;
    }

    public Integer getSize2() {
        return this.size2;
    }

    public boolean hasSize2() {
        return this.size2 != null;
    }

    public OptionSet setSize2(int i) {
        this.size2 = Integer.valueOf(i);
        return this;
    }

    public OptionSet removeSize2() {
        this.size2 = null;
        return this;
    }

    public Integer getObserve() {
        return this.observe;
    }

    public boolean hasObserve() {
        return this.observe != null;
    }

    public OptionSet setObserve(int i) {
        if (!isValidObserveOption(i)) {
            ALog.e(TAG, "Observe option must be between 0 and 16777215 (3 bytes) inclusive");
            return this;
        }
        this.observe = Integer.valueOf(i);
        return this;
    }

    public OptionSet removeObserve() {
        this.observe = null;
        return this;
    }

    public boolean hasOption(int i) {
        return Collections.binarySearch(asSortedList(), new Option(i)) >= 0;
    }

    private List<Option> getOthersInternal() {
        synchronized (this) {
            if (this.others == null) {
                this.others = new LinkedList();
            }
        }
        return this.others;
    }

    public List<Option> getOthers() {
        List<Option> list = this.others;
        if (list == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(list);
    }

    public List<Option> asSortedList() {
        ArrayList arrayList = new ArrayList();
        List<byte[]> list = this.if_match_list;
        if (list != null) {
            Iterator<byte[]> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(new Option(1, it.next()));
            }
        }
        if (hasUriHost()) {
            arrayList.add(new Option(3, getUriHost()));
        }
        List<byte[]> list2 = this.etag_list;
        if (list2 != null) {
            Iterator<byte[]> it2 = list2.iterator();
            while (it2.hasNext()) {
                arrayList.add(new Option(4, it2.next()));
            }
        }
        if (hasIfNoneMatch()) {
            arrayList.add(new Option(5));
        }
        if (hasUriPort()) {
            arrayList.add(new Option(7, getUriPort().intValue()));
        }
        List<String> list3 = this.location_path_list;
        if (list3 != null) {
            Iterator<String> it3 = list3.iterator();
            while (it3.hasNext()) {
                arrayList.add(new Option(8, it3.next()));
            }
        }
        List<String> list4 = this.uri_path_list;
        if (list4 != null) {
            Iterator<String> it4 = list4.iterator();
            while (it4.hasNext()) {
                arrayList.add(new Option(11, it4.next()));
            }
        }
        if (hasContentFormat()) {
            arrayList.add(new Option(12, getContentFormat()));
        }
        if (hasMaxAge()) {
            arrayList.add(new Option(14, getMaxAge().longValue()));
        }
        List<String> list5 = this.uri_query_list;
        if (list5 != null) {
            Iterator<String> it5 = list5.iterator();
            while (it5.hasNext()) {
                arrayList.add(new Option(15, it5.next()));
            }
        }
        if (hasAccept()) {
            arrayList.add(new Option(17, getAccept()));
        }
        List<String> list6 = this.location_query_list;
        if (list6 != null) {
            Iterator<String> it6 = list6.iterator();
            while (it6.hasNext()) {
                arrayList.add(new Option(20, it6.next()));
            }
        }
        if (hasProxyUri()) {
            arrayList.add(new Option(35, getProxyUri()));
        }
        if (hasProxyScheme()) {
            arrayList.add(new Option(39, getProxyScheme()));
        }
        if (hasObserve()) {
            arrayList.add(new Option(6, getObserve().intValue()));
        }
        if (hasBlock1()) {
            arrayList.add(new Option(27, getBlock1().getValue()));
        }
        if (hasBlock2()) {
            arrayList.add(new Option(23, getBlock2().getValue()));
        }
        if (hasSize1()) {
            arrayList.add(new Option(60, getSize1().intValue()));
        }
        if (hasSize2()) {
            arrayList.add(new Option(28, getSize2().intValue()));
        }
        List<Option> list7 = this.others;
        if (list7 != null) {
            arrayList.addAll(list7);
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    public OptionSet addOption(Option option) {
        switch (option.getNumber()) {
            case 1:
                addIfMatch(option.getValue());
                return this;
            case 3:
                setUriHost(option.getStringValue());
                return this;
            case 4:
                addETag(option.getValue());
                return this;
            case 5:
                setIfNoneMatch(true);
                return this;
            case 6:
                setObserve(option.getIntegerValue());
                return this;
            case 7:
                setUriPort(option.getIntegerValue());
                return this;
            case 8:
                addLocationPath(option.getStringValue());
                return this;
            case 11:
                addUriPath(option.getStringValue());
                return this;
            case 12:
                setContentFormat(option.getIntegerValue());
                return this;
            case 14:
                setMaxAge(option.getLongValue());
                return this;
            case 15:
                addUriQuery(option.getStringValue());
                return this;
            case 17:
                setAccept(option.getIntegerValue());
                return this;
            case 20:
                addLocationQuery(option.getStringValue());
                return this;
            case 23:
                setBlock2(option.getValue());
                return this;
            case 27:
                setBlock1(option.getValue());
                return this;
            case 28:
                setSize2(option.getIntegerValue());
                return this;
            case 35:
                setProxyUri(option.getStringValue());
                return this;
            case 39:
                setProxyScheme(option.getStringValue());
                return this;
            case 60:
                setSize1(option.getIntegerValue());
                return this;
            default:
                getOthersInternal().add(option);
                return this;
        }
    }

    public void addOption(int i, byte[] bArr) {
        addOption(new Option(i, bArr));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        sb.append('{');
        StringBuilder sb3 = sb2;
        int number = -1;
        boolean z = false;
        for (Option option : asSortedList()) {
            if (option.getNumber() != number) {
                if (number != -1) {
                    if (z) {
                        sb3.append(']');
                    }
                    sb.append(sb3.toString());
                    sb3 = new StringBuilder();
                    sb.append(", ");
                }
                sb.append('\"');
                sb.append(OptionNumberRegistry.toString(option.getNumber()));
                sb.append('\"');
                sb.append(':');
                z = false;
            } else {
                if (!z) {
                    sb3.insert(0, '[');
                }
                z = true;
                sb3.append(",");
            }
            sb3.append(option.toValueString());
            number = option.getNumber();
        }
        if (z) {
            sb3.append(']');
        }
        sb.append(sb3.toString());
        sb.append('}');
        return sb.toString();
    }
}
