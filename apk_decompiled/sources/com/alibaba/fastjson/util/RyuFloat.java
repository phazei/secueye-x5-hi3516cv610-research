package com.alibaba.fastjson.util;

import com.tencent.mm.opensdk.constants.Build;
import org.apache.commons.codec.language.Soundex;

/* JADX INFO: loaded from: classes.dex */
public final class RyuFloat {
    private static final int[][] POW5_SPLIT = {new int[]{536870912, 0}, new int[]{Build.SUPPORT_SEND_MUSIC_VIDEO_MESSAGE, 0}, new int[]{838860800, 0}, new int[]{1048576000, 0}, new int[]{655360000, 0}, new int[]{819200000, 0}, new int[]{1024000000, 0}, new int[]{640000000, 0}, new int[]{800000000, 0}, new int[]{1000000000, 0}, new int[]{625000000, 0}, new int[]{781250000, 0}, new int[]{976562500, 0}, new int[]{610351562, 1073741824}, new int[]{762939453, 268435456}, new int[]{953674316, 872415232}, new int[]{596046447, 1619001344}, new int[]{745058059, 1486880768}, new int[]{931322574, 1321730048}, new int[]{582076609, 289210368}, new int[]{727595761, 898383872}, new int[]{909494701, 1659850752}, new int[]{568434188, 1305842176}, new int[]{710542735, 1632302720}, new int[]{888178419, 1503507488}, new int[]{555111512, 671256724}, new int[]{693889390, 839070905}, new int[]{867361737, 2122580455}, new int[]{542101086, 521306416}, new int[]{677626357, 1725374844}, new int[]{847032947, 546105819}, new int[]{1058791184, 145761362}, new int[]{661744490, 91100851}, new int[]{827180612, 1187617888}, new int[]{1033975765, 1484522360}, new int[]{646234853, 1196261931}, new int[]{807793566, 2032198326}, new int[]{1009741958, 1466506084}, new int[]{631088724, 379695390}, new int[]{788860905, 474619238}, new int[]{986076131, 1130144959}, new int[]{616297582, 437905143}, new int[]{770371977, 1621123253}, new int[]{962964972, 415791331}, new int[]{601853107, 1333611405}, new int[]{752316384, 1130143345}, new int[]{940395480, 1412679181}};
    private static final int[][] POW5_INV_SPLIT = {new int[]{268435456, 1}, new int[]{214748364, 1717986919}, new int[]{171798691, 1803886265}, new int[]{137438953, 1013612282}, new int[]{219902325, 1192282922}, new int[]{175921860, 953826338}, new int[]{140737488, 763061070}, new int[]{225179981, 791400982}, new int[]{180143985, 203624056}, new int[]{144115188, 162899245}, new int[]{230584300, 1978625710}, new int[]{184467440, 1582900568}, new int[]{147573952, 1266320455}, new int[]{236118324, 308125809}, new int[]{188894659, 675997377}, new int[]{151115727, 970294631}, new int[]{241785163, 1981968139}, new int[]{193428131, 297084323}, new int[]{154742504, 1955654377}, new int[]{247588007, 1840556814}, new int[]{198070406, 613451992}, new int[]{158456325, 61264864}, new int[]{253530120, 98023782}, new int[]{202824096, 78419026}, new int[]{162259276, 1780722139}, new int[]{259614842, 1990161963}, new int[]{207691874, 733136111}, new int[]{166153499, 1016005619}, new int[]{265845599, 337118801}, new int[]{212676479, 699191770}, new int[]{170141183, 988850146}};

    public static String toString(float f) {
        char[] cArr = new char[15];
        return new String(cArr, 0, toString(f, cArr, 0));
    }

    public static int toString(float f, char[] cArr, int i) {
        int i2;
        boolean z;
        boolean z2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        if (Float.isNaN(f)) {
            int i20 = i + 1;
            cArr[i] = 'N';
            int i21 = i20 + 1;
            cArr[i20] = 'a';
            cArr[i21] = 'N';
            return (i21 + 1) - i;
        }
        if (f == Float.POSITIVE_INFINITY) {
            int i22 = i + 1;
            cArr[i] = 'I';
            int i23 = i22 + 1;
            cArr[i22] = 'n';
            int i24 = i23 + 1;
            cArr[i23] = 'f';
            int i25 = i24 + 1;
            cArr[i24] = 'i';
            int i26 = i25 + 1;
            cArr[i25] = 'n';
            int i27 = i26 + 1;
            cArr[i26] = 'i';
            int i28 = i27 + 1;
            cArr[i27] = 't';
            cArr[i28] = 'y';
            return (i28 + 1) - i;
        }
        if (f == Float.NEGATIVE_INFINITY) {
            int i29 = i + 1;
            cArr[i] = Soundex.SILENT_MARKER;
            int i30 = i29 + 1;
            cArr[i29] = 'I';
            int i31 = i30 + 1;
            cArr[i30] = 'n';
            int i32 = i31 + 1;
            cArr[i31] = 'f';
            int i33 = i32 + 1;
            cArr[i32] = 'i';
            int i34 = i33 + 1;
            cArr[i33] = 'n';
            int i35 = i34 + 1;
            cArr[i34] = 'i';
            int i36 = i35 + 1;
            cArr[i35] = 't';
            cArr[i36] = 'y';
            return (i36 + 1) - i;
        }
        int iFloatToIntBits = Float.floatToIntBits(f);
        if (iFloatToIntBits == 0) {
            int i37 = i + 1;
            cArr[i] = '0';
            int i38 = i37 + 1;
            cArr[i37] = '.';
            cArr[i38] = '0';
            return (i38 + 1) - i;
        }
        if (iFloatToIntBits == Integer.MIN_VALUE) {
            int i39 = i + 1;
            cArr[i] = Soundex.SILENT_MARKER;
            int i40 = i39 + 1;
            cArr[i39] = '0';
            int i41 = i40 + 1;
            cArr[i40] = '.';
            cArr[i41] = '0';
            return (i41 + 1) - i;
        }
        int i42 = (iFloatToIntBits >> 23) & 255;
        int i43 = 8388607 & iFloatToIntBits;
        if (i42 == 0) {
            i2 = -149;
        } else {
            i2 = (i42 - 127) - 23;
            i43 |= 8388608;
        }
        boolean z3 = iFloatToIntBits < 0;
        boolean z4 = (i43 & 1) == 0;
        int i44 = i43 * 4;
        int i45 = i44 + 2;
        int i46 = i44 - ((((long) i43) != 8388608 || i42 <= 1) ? 2 : 1);
        int i47 = i2 - 2;
        if (i47 >= 0) {
            int i48 = (int) ((((long) i47) * 3010299) / 10000000);
            if (i48 == 0) {
                i18 = i47;
                i19 = 1;
            } else {
                i18 = i47;
                i19 = (int) ((((((long) i48) * 23219280) + 10000000) - 1) / 10000000);
            }
            int i49 = (-i18) + i48;
            int[][] iArr = POW5_INV_SPLIT;
            long j = iArr[i48][0];
            long j2 = iArr[i48][1];
            long j3 = i44;
            int i50 = (((i19 + 59) - 1) + i49) - 31;
            int i51 = (int) (((j3 * j) + ((j3 * j2) >> 31)) >> i50);
            z = z4;
            long j4 = i45;
            i9 = (int) (((j4 * j) + ((j4 * j2) >> 31)) >> i50);
            long j5 = i46;
            i4 = (int) (((j * j5) + ((j5 * j2) >> 31)) >> i50);
            if (i48 == 0 || (i9 - 1) / 10 > i4 / 10) {
                i6 = 0;
            } else {
                int i52 = i48 - 1;
                int i53 = (i49 - 1) + (((i52 == 0 ? 1 : (int) ((((((long) i52) * 23219280) + 10000000) - 1) / 10000000)) + 59) - 1);
                int[][] iArr2 = POW5_INV_SPLIT;
                i6 = (int) ((((((long) iArr2[i52][0]) * j3) + ((j3 * ((long) iArr2[i52][1])) >> 31)) >> (i53 - 31)) % 10);
            }
            int i54 = 0;
            while (i45 > 0 && i45 % 5 == 0) {
                i45 /= 5;
                i54++;
            }
            int i55 = 0;
            while (i44 > 0 && i44 % 5 == 0) {
                i44 /= 5;
                i55++;
            }
            int i56 = 0;
            while (i46 > 0 && i46 % 5 == 0) {
                i46 /= 5;
                i56++;
            }
            i11 = i54 >= i48 ? 1 : 0;
            i8 = i55 >= i48 ? 1 : 0;
            i10 = i56 >= i48 ? 1 : 0;
            i5 = 0;
            z2 = z3;
            i7 = i48;
            i3 = i51;
        } else {
            z = z4;
            int i57 = -i47;
            int i58 = (int) ((((long) i57) * 6989700) / 10000000);
            int i59 = i57 - i58;
            int i60 = i59 == 0 ? 1 : (int) ((((((long) i59) * 23219280) + 10000000) - 1) / 10000000);
            int[][] iArr3 = POW5_SPLIT;
            long j6 = iArr3[i59][0];
            long j7 = iArr3[i59][1];
            int i61 = (i58 - (i60 - 61)) - 31;
            z2 = z3;
            long j8 = i44;
            i3 = (int) (((j8 * j6) + ((j8 * j7) >> 31)) >> i61);
            long j9 = i45;
            int i62 = (int) (((j9 * j6) + ((j9 * j7) >> 31)) >> i61);
            long j10 = i46;
            i4 = (int) (((j6 * j10) + ((j10 * j7) >> 31)) >> i61);
            if (i58 == 0 || (i62 - 1) / 10 > i4 / 10) {
                i5 = 0;
                i6 = 0;
            } else {
                int i63 = i59 + 1;
                int[][] iArr4 = POW5_SPLIT;
                i5 = 0;
                i6 = (int) ((((((long) iArr4[i63][0]) * j8) + ((j8 * ((long) iArr4[i63][1])) >> 31)) >> (((i58 - 1) - ((i63 == 0 ? 1 : (int) ((((((long) i63) * 23219280) + 10000000) - 1) / 10000000)) - 61)) - 31)) % 10);
            }
            i7 = i58 + i47;
            int i64 = 1 >= i58 ? 1 : i5;
            int i65 = (i58 >= 23 || (((1 << (i58 + (-1))) - 1) & i44) != 0) ? i5 : 1;
            int i66 = (i46 % 2 == 1 ? i5 : 1) >= i58 ? 1 : i5;
            i8 = i65;
            i9 = i62;
            int i67 = i64;
            i10 = i66;
            i11 = i67;
        }
        int i68 = 1000000000;
        int i69 = 10;
        while (i69 > 0 && i9 < i68) {
            i68 /= 10;
            i69--;
        }
        int i70 = (i7 + i69) - 1;
        int i71 = (i70 < -3 || i70 >= 7) ? 1 : i5;
        if (i11 == 0 || z) {
            i12 = i5;
        } else {
            i9--;
            i12 = i5;
        }
        while (true) {
            int i72 = i9 / 10;
            int i73 = i4 / 10;
            if (i72 <= i73 || (i9 < 100 && i71 != 0)) {
                break;
            }
            i10 &= i4 % 10 == 0 ? 1 : i5;
            i6 = i3 % 10;
            i3 /= 10;
            i12++;
            i9 = i72;
            i4 = i73;
        }
        if (i10 != 0 && z) {
            while (i4 % 10 == 0 && (i9 >= 100 || i71 == 0)) {
                i9 /= 10;
                i6 = i3 % 10;
                i3 /= 10;
                i4 /= 10;
                i12++;
            }
        }
        if (i8 != 0 && i6 == 5 && i3 % 2 == 0) {
            i6 = 4;
        }
        int i74 = i3 + (((i3 != i4 || (i10 != 0 && z)) && i6 < 5) ? i5 : 1);
        int i75 = i69 - i12;
        if (z2) {
            i13 = i + 1;
            cArr[i] = Soundex.SILENT_MARKER;
        } else {
            i13 = i;
        }
        if (i71 != 0) {
            while (i5 < i75 - 1) {
                int i76 = i74 % 10;
                i74 /= 10;
                cArr[(i13 + i75) - i5] = (char) (i76 + 48);
                i5++;
            }
            cArr[i13] = (char) ((i74 % 10) + 48);
            cArr[i13 + 1] = '.';
            int i77 = i13 + i75 + 1;
            if (i75 == 1) {
                cArr[i77] = '0';
                i77++;
            }
            int i78 = i77 + 1;
            cArr[i77] = 'E';
            if (i70 < 0) {
                i15 = i78 + 1;
                cArr[i78] = Soundex.SILENT_MARKER;
                i70 = -i70;
                i16 = 10;
            } else {
                i15 = i78;
                i16 = 10;
            }
            if (i70 >= i16) {
                i17 = 48;
                cArr[i15] = (char) ((i70 / 10) + 48);
                i15++;
            } else {
                i17 = 48;
            }
            i14 = i15 + 1;
            cArr[i15] = (char) ((i70 % 10) + i17);
        } else {
            int i79 = 48;
            if (i70 < 0) {
                int i80 = i13 + 1;
                cArr[i13] = '0';
                int i81 = i80 + 1;
                cArr[i80] = '.';
                int i82 = -1;
                while (i82 > i70) {
                    cArr[i81] = '0';
                    i82--;
                    i81++;
                }
                i14 = i81;
                while (i5 < i75) {
                    cArr[((i81 + i75) - i5) - 1] = (char) ((i74 % 10) + i79);
                    i74 /= 10;
                    i14++;
                    i5++;
                    i79 = 48;
                }
            } else {
                int i83 = i70 + 1;
                if (i83 >= i75) {
                    while (i5 < i75) {
                        cArr[((i13 + i75) - i5) - 1] = (char) ((i74 % 10) + 48);
                        i74 /= 10;
                        i5++;
                    }
                    int i84 = i13 + i75;
                    while (i75 < i83) {
                        cArr[i84] = '0';
                        i75++;
                        i84++;
                    }
                    int i85 = i84 + 1;
                    cArr[i84] = '.';
                    i14 = i85 + 1;
                    cArr[i85] = '0';
                } else {
                    int i86 = i13 + 1;
                    while (i5 < i75) {
                        if ((i75 - i5) - 1 == i70) {
                            cArr[((i86 + i75) - i5) - 1] = '.';
                            i86--;
                        }
                        cArr[((i86 + i75) - i5) - 1] = (char) ((i74 % 10) + 48);
                        i74 /= 10;
                        i5++;
                    }
                    i14 = i13 + i75 + 1;
                }
            }
        }
        return i14 - i;
    }
}
