package com.alibaba.fastjson.asm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public class ClassReader {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final byte[] f2823b;
    public final int header;
    private final int[] items;
    private final int maxStringLength;
    private boolean readAnnotations;
    private final String[] strings;

    public ClassReader(InputStream inputStream, boolean z) throws IOException {
        int i;
        this.readAnnotations = z;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int i2 = inputStream.read(bArr);
            i = 0;
            if (i2 == -1) {
                break;
            } else if (i2 > 0) {
                byteArrayOutputStream.write(bArr, 0, i2);
            }
        }
        inputStream.close();
        this.f2823b = byteArrayOutputStream.toByteArray();
        this.items = new int[readUnsignedShort(8)];
        int length = this.items.length;
        this.strings = new String[length];
        int i3 = 10;
        int i4 = 1;
        while (i4 < length) {
            int i5 = i3 + 1;
            this.items[i4] = i5;
            byte b2 = this.f2823b[i3];
            int unsignedShort = 3;
            if (b2 == 1) {
                unsignedShort = 3 + readUnsignedShort(i5);
                if (unsignedShort > i) {
                    i = unsignedShort;
                }
            } else if (b2 == 15) {
                unsignedShort = 4;
            } else if (b2 != 18) {
                switch (b2) {
                    case 3:
                    case 4:
                        break;
                    case 5:
                    case 6:
                        unsignedShort = 9;
                        i4++;
                        continue;
                    default:
                        switch (b2) {
                            case 9:
                            case 10:
                            case 11:
                            case 12:
                                break;
                            default:
                                continue;
                        }
                        break;
                }
                unsignedShort = 5;
            } else {
                unsignedShort = 5;
            }
            i3 += unsignedShort;
            i4++;
        }
        this.maxStringLength = i;
        this.header = i3;
    }

    public void accept(TypeCollector typeCollector) {
        int i;
        char[] cArr = new char[this.maxStringLength];
        if (this.readAnnotations) {
            int attributes = getAttributes();
            for (int unsignedShort = readUnsignedShort(attributes); unsignedShort > 0; unsignedShort--) {
                if ("RuntimeVisibleAnnotations".equals(readUTF8(attributes + 2, cArr))) {
                    i = attributes + 8;
                    break;
                }
                attributes += readInt(attributes + 4) + 6;
            }
            i = 0;
        } else {
            i = 0;
        }
        int i2 = this.header;
        int unsignedShort2 = readUnsignedShort(i2 + 6);
        int i3 = i2 + 8;
        for (int i4 = 0; i4 < unsignedShort2; i4++) {
            i3 += 2;
        }
        int i5 = i3 + 2;
        int i6 = i5;
        for (int unsignedShort3 = readUnsignedShort(i3); unsignedShort3 > 0; unsignedShort3--) {
            i6 += 8;
            for (int unsignedShort4 = readUnsignedShort(i6 + 6); unsignedShort4 > 0; unsignedShort4--) {
                i6 += readInt(i6 + 2) + 6;
            }
        }
        int i7 = i6 + 2;
        for (int unsignedShort5 = readUnsignedShort(i6); unsignedShort5 > 0; unsignedShort5--) {
            i7 += 8;
            for (int unsignedShort6 = readUnsignedShort(i7 + 6); unsignedShort6 > 0; unsignedShort6--) {
                i7 += readInt(i7 + 2) + 6;
            }
        }
        int i8 = i7 + 2;
        for (int unsignedShort7 = readUnsignedShort(i7); unsignedShort7 > 0; unsignedShort7--) {
            i8 += readInt(i8 + 2) + 6;
        }
        if (i != 0) {
            int i9 = i + 2;
            for (int unsignedShort8 = readUnsignedShort(i); unsignedShort8 > 0; unsignedShort8--) {
                typeCollector.visitAnnotation(readUTF8(i9, cArr));
            }
        }
        for (int unsignedShort9 = readUnsignedShort(i3); unsignedShort9 > 0; unsignedShort9--) {
            i5 += 8;
            for (int unsignedShort10 = readUnsignedShort(i5 + 6); unsignedShort10 > 0; unsignedShort10--) {
                i5 += readInt(i5 + 2) + 6;
            }
        }
        int method = i5 + 2;
        for (int unsignedShort11 = readUnsignedShort(i5); unsignedShort11 > 0; unsignedShort11--) {
            method = readMethod(typeCollector, cArr, method);
        }
    }

    private int getAttributes() {
        int i = this.header;
        int unsignedShort = i + 8 + (readUnsignedShort(i + 6) * 2);
        for (int unsignedShort2 = readUnsignedShort(unsignedShort); unsignedShort2 > 0; unsignedShort2--) {
            for (int unsignedShort3 = readUnsignedShort(unsignedShort + 8); unsignedShort3 > 0; unsignedShort3--) {
                unsignedShort += readInt(unsignedShort + 12) + 6;
            }
            unsignedShort += 8;
        }
        int i2 = unsignedShort + 2;
        for (int unsignedShort4 = readUnsignedShort(i2); unsignedShort4 > 0; unsignedShort4--) {
            for (int unsignedShort5 = readUnsignedShort(i2 + 8); unsignedShort5 > 0; unsignedShort5--) {
                i2 += readInt(i2 + 12) + 6;
            }
            i2 += 8;
        }
        return i2 + 2;
    }

    private int readMethod(TypeCollector typeCollector, char[] cArr, int i) {
        int unsignedShort = readUnsignedShort(i);
        String utf8 = readUTF8(i + 2, cArr);
        String utf82 = readUTF8(i + 4, cArr);
        int i2 = i + 8;
        int i3 = 0;
        int i4 = 0;
        for (int unsignedShort2 = readUnsignedShort(i + 6); unsignedShort2 > 0; unsignedShort2--) {
            String utf83 = readUTF8(i2, cArr);
            int i5 = readInt(i2 + 2);
            int i6 = i2 + 6;
            if (utf83.equals("Code")) {
                i4 = i6;
            }
            i2 = i6 + i5;
        }
        MethodCollector methodCollectorVisitMethod = typeCollector.visitMethod(unsignedShort, utf8, utf82);
        if (methodCollectorVisitMethod != null && i4 != 0) {
            int i7 = i4 + 8 + readInt(i4 + 4);
            int i8 = i7 + 2;
            for (int unsignedShort3 = readUnsignedShort(i7); unsignedShort3 > 0; unsignedShort3--) {
                i8 += 8;
            }
            int i9 = i8 + 2;
            int i10 = 0;
            for (int unsignedShort4 = readUnsignedShort(i8); unsignedShort4 > 0; unsignedShort4--) {
                String utf84 = readUTF8(i9, cArr);
                if (utf84.equals("LocalVariableTable")) {
                    i3 = i9 + 6;
                } else if (utf84.equals("LocalVariableTypeTable")) {
                    i10 = i9 + 6;
                }
                i9 += readInt(i9 + 2) + 6;
            }
            if (i3 != 0) {
                if (i10 != 0) {
                    int unsignedShort5 = readUnsignedShort(i10) * 3;
                    int i11 = i10 + 2;
                    int[] iArr = new int[unsignedShort5];
                    while (unsignedShort5 > 0) {
                        int i12 = unsignedShort5 - 1;
                        iArr[i12] = i11 + 6;
                        int i13 = i12 - 1;
                        iArr[i13] = readUnsignedShort(i11 + 8);
                        unsignedShort5 = i13 - 1;
                        iArr[unsignedShort5] = readUnsignedShort(i11);
                        i11 += 10;
                    }
                }
                int i14 = i3 + 2;
                for (int unsignedShort6 = readUnsignedShort(i3); unsignedShort6 > 0; unsignedShort6--) {
                    methodCollectorVisitMethod.visitLocalVariable(readUTF8(i14 + 4, cArr), readUnsignedShort(i14 + 8));
                    i14 += 10;
                }
            }
        }
        return i2;
    }

    private int readUnsignedShort(int i) {
        byte[] bArr = this.f2823b;
        return (bArr[i + 1] & 255) | ((bArr[i] & 255) << 8);
    }

    private int readInt(int i) {
        byte[] bArr = this.f2823b;
        return (bArr[i + 3] & 255) | ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8);
    }

    private String readUTF8(int i, char[] cArr) {
        int unsignedShort = readUnsignedShort(i);
        String[] strArr = this.strings;
        String str = strArr[unsignedShort];
        if (str != null) {
            return str;
        }
        int i2 = this.items[unsignedShort];
        String utf = readUTF(i2 + 2, readUnsignedShort(i2), cArr);
        strArr[unsignedShort] = utf;
        return utf;
    }

    private String readUTF(int i, int i2, char[] cArr) {
        int i3 = i2 + i;
        byte[] bArr = this.f2823b;
        int i4 = 0;
        char c2 = 0;
        char c3 = 0;
        while (i < i3) {
            int i5 = i + 1;
            byte b2 = bArr[i];
            switch (c2) {
                case 0:
                    int i6 = b2 & 255;
                    if (i6 < 128) {
                        cArr[i4] = (char) i6;
                        i4++;
                    } else if (i6 < 224 && i6 > 191) {
                        c3 = (char) (i6 & 31);
                        c2 = 1;
                    } else {
                        c2 = 2;
                        c3 = (char) (i6 & 15);
                    }
                    break;
                case 1:
                    cArr[i4] = (char) ((b2 & 63) | (c3 << 6));
                    i4++;
                    c2 = 0;
                    break;
                case 2:
                    c3 = (char) ((b2 & 63) | (c3 << 6));
                    c2 = 1;
                    break;
            }
            i = i5;
        }
        return new String(cArr, 0, i4);
    }
}
