package b.b;

import a.a.a.a.b.G;
import android.content.Context;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pair;
import b.InterfaceC0368b;
import b.InterfaceC0369c;
import b.InterfaceC0370d;
import b.d.c;
import b.e.i;
import b.q;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import meshprovisioner.ProxyProtocolMessageType;
import meshprovisioner.configuration.CommonMessageV2;
import meshprovisioner.configuration.ConfigAppKeyAdd;
import meshprovisioner.configuration.ConfigModelAppBind;
import meshprovisioner.configuration.ConfigModelPublicationSet;
import meshprovisioner.configuration.ConfigModelSubscriptionAdd;
import meshprovisioner.configuration.ConfigModelSubscriptionDelete;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.configuration.bean.CfgAppKeyStatus;
import meshprovisioner.configuration.bean.CfgMsgModelSubscriptionStatus;
import meshprovisioner.configuration.bean.SceneRegisterStatus;
import meshprovisioner.configuration.bean.SceneStatus;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.ConfigModelPublicationSetParams;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: compiled from: MeshMessageHandlerV2.java */
/* JADX INFO: loaded from: classes.dex */
public class a implements InterfaceC0369c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2125a = "" + a.class.getSimpleName();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static LongSparseArray<IActionListener<Object>> f2126b = new LongSparseArray<>();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static Map<Integer, Pair<Integer, byte[]>> f2127c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static Map<Integer, Integer> f2128d;
    public final Context e;
    public final InterfaceC0370d f;
    public final InterfaceC0368b g;
    public q h;
    public long i = 0;

    public a(Context context, InterfaceC0370d interfaceC0370d, InterfaceC0368b interfaceC0368b) {
        this.e = context;
        this.f = interfaceC0370d;
        this.g = interfaceC0368b;
        i.c().a(this.f);
    }

    public void a(q qVar) {
        this.h = qVar;
        i.c().a(this.h);
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, byte[] bArr) {
    }

    @Override // b.InterfaceC0369c
    public void a(ProvisionedMeshNode provisionedMeshNode, byte[] bArr, boolean z) {
    }

    public void b(ProvisionedMeshNode provisionedMeshNode, int i, byte[] bArr, byte[] bArr2, int i2) {
        ConfigModelSubscriptionDelete configModelSubscriptionDelete = new ConfigModelSubscriptionDelete(this.e, provisionedMeshNode, this, i, bArr, bArr2, i2);
        configModelSubscriptionDelete.setTransportCallbacks(this.f);
        configModelSubscriptionDelete.setStatusCallbacks(this.h);
        configModelSubscriptionDelete.executeSend();
    }

    public final int a(byte[] bArr) {
        if (bArr.length == 2) {
            return ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getShort();
        }
        return ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    public void b(byte[] bArr, int i, byte[] bArr2, IActionListener<Object> iActionListener) {
        long jA = a(bArr, i, bArr2);
        a.a.a.a.b.m.a.a(f2125a, "Unregister message listener with key: " + jA + ", address: " + MeshParserUtils.bytesToHex(bArr, false) + ", opcode: " + i);
        f2126b.remove(jA);
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, byte[] bArr, a.a.a.a.b.h.a aVar) {
        IActionListener<Object> iActionListener;
        long j;
        ProvisionedMeshNode provisionedMeshNode2;
        Pair pair;
        int i;
        byte[] bArr2;
        int i2;
        int i3;
        byte[] bArr3;
        long j2;
        byte[] bArr4;
        int i4;
        int i5;
        byte[] bArr5;
        short s;
        Pair<Integer, byte[]> pair2;
        int i6;
        Byte bValueOf;
        byte[] bArr6;
        byte[] bArr7;
        byte[] bArr8;
        byte b2;
        int iN;
        byte[] bArrO;
        try {
            c cVarH = i.c().h(MeshParserUtils.bytesToHex(provisionedMeshNode.getNetworkKey(), false), bArr);
            if (cVarH instanceof b.d.a) {
                b.d.a aVar2 = (b.d.a) cVarH;
                if (aVar != null) {
                    aVar.b(cVarH.s());
                }
                int iN2 = aVar2.n();
                byte[] bArrU = ((b.d.a) cVarH).u();
                ProvisionedMeshNode provisionedMeshNode3 = (ProvisionedMeshNode) G.a().d().a(provisionedMeshNode.getNetworkKey(), aVar2.r());
                if (provisionedMeshNode3 == null) {
                    String str = f2125a;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Received message from Illegal node: ");
                    sb.append(MeshParserUtils.bytesToHex(aVar2.r(), false));
                    a.a.a.a.b.m.a.b(str, sb.toString());
                    return;
                }
                provisionedMeshNode3.setSequenceNumber(MeshParserUtils.getSequenceNumber(cVarH.q()));
                long jA = (iN2 != 13871105 || (bArrO = aVar2.o()) == null || bArrO.length < 3) ? a(provisionedMeshNode3.getUnicastAddress(), iN2) : a(provisionedMeshNode3.getUnicastAddress(), iN2, new byte[]{bArrO[1], bArrO[2]});
                IActionListener<Object> iActionListener2 = f2126b.get(jA);
                Log.d(f2125a, String.format("Received package from %s, opcode: %s(%d), params: %s", MeshParserUtils.bytesToHex(provisionedMeshNode3.getUnicastAddress(), false), Integer.toHexString(iN2), Integer.valueOf(iN2), MeshParserUtils.bytesToHex(aVar2.o(), false)));
                if (iN2 != -32765) {
                    if (iN2 == -32743) {
                        iActionListener = iActionListener2;
                        j = jA;
                        provisionedMeshNode2 = provisionedMeshNode3;
                        a.a.a.a.b.m.a.a(f2125a, "Received model publication status");
                        byte b3 = bArrU[2];
                        boolean z = b3 == 0;
                        byte[] bArr9 = {bArrU[4], bArrU[3]};
                        byte[] bArr10 = {bArrU[6], bArrU[5]};
                        byte[] bArr11 = {(byte) (bArrU[8] & 15), bArrU[7]};
                        byte b4 = bArrU[8];
                        byte b5 = bArrU[9];
                        byte b6 = bArrU[10];
                        byte b7 = bArrU[11];
                        byte b8 = bArrU[11];
                        if (bArrU.length == 14) {
                            bArr2 = new byte[]{bArrU[13], bArrU[12]};
                            i2 = 2;
                        } else {
                            i2 = 2;
                            bArr2 = new byte[]{bArrU[13], bArrU[12], bArrU[15], bArrU[14]};
                        }
                        if (bArr2.length == i2) {
                            i3 = ByteBuffer.wrap(bArr2).order(ByteOrder.BIG_ENDIAN).getShort();
                        } else {
                            i3 = ByteBuffer.wrap(bArr2).order(ByteOrder.BIG_ENDIAN).getInt();
                        }
                        this.h.onPublicationStatusReceived(provisionedMeshNode2, z, b3, bArr9, bArr10, i3);
                    } else if (iN2 == -32737) {
                        iActionListener = iActionListener2;
                        long j3 = jA;
                        provisionedMeshNode2 = provisionedMeshNode3;
                        a.a.a.a.b.m.a.a(f2125a, "Received model subscription status");
                        byte b9 = bArrU[2];
                        boolean z2 = b9 == 0;
                        byte[] bArr12 = {bArrU[4], bArrU[3]};
                        byte[] bArr13 = {bArrU[6], bArrU[5]};
                        if (bArrU.length == 9) {
                            bArr3 = new byte[]{bArrU[8], bArrU[7]};
                        } else {
                            bArr3 = new byte[]{bArrU[8], bArrU[7], bArrU[10], bArrU[9]};
                        }
                        this.h.onSubscriptionStatusReceived(provisionedMeshNode2, z2, b9, bArr12, bArr13, a(bArr3));
                        if (iActionListener == null) {
                            j = j3;
                        } else {
                            pair = new Pair(0, new CfgMsgModelSubscriptionStatus(b9, bArr12, bArr13, bArr3));
                            if (f2128d != null) {
                                int unicastAddressInt = AddressUtils.getUnicastAddressInt(provisionedMeshNode2.getUnicastAddress());
                                Integer num = f2128d.get(Integer.valueOf(unicastAddressInt));
                                if (num != null) {
                                    j2 = j3;
                                    this.h.onCommonMessageStatusReceived(provisionedMeshNode2, cVarH.r(), Integer.toHexString(num.intValue()), aVar2.o(), aVar);
                                    f2128d.remove(Integer.valueOf(unicastAddressInt));
                                    i = iN2;
                                    j = j2;
                                } else {
                                    j2 = j3;
                                }
                            } else {
                                j2 = j3;
                            }
                            this.h.onCommonMessageStatusReceived(provisionedMeshNode2, cVarH.r(), "801F", aVar2.o(), aVar);
                            i = iN2;
                            j = j2;
                        }
                    } else if (iN2 == -32706) {
                        iActionListener = iActionListener2;
                        long j4 = jA;
                        a.a.a.a.b.m.a.a(f2125a, "Received app key bind status");
                        ByteBuffer byteBufferOrder = ByteBuffer.wrap(bArrU).order(ByteOrder.LITTLE_ENDIAN);
                        byteBufferOrder.position(2);
                        byte b10 = byteBufferOrder.get();
                        boolean z3 = b10 == 0;
                        byte[] bArr14 = {bArrU[4], bArrU[3]};
                        byte[] bArr15 = {(byte) (bArrU[6] & 15), bArrU[5]};
                        if (bArrU.length == 9) {
                            bArr4 = new byte[]{bArrU[8], bArrU[7]};
                            i4 = 2;
                        } else {
                            i4 = 2;
                            bArr4 = new byte[]{bArrU[8], bArrU[7], bArrU[10], bArrU[9]};
                        }
                        if (bArr4.length == i4) {
                            i5 = ByteBuffer.wrap(bArr4).order(ByteOrder.BIG_ENDIAN).getShort();
                        } else {
                            i5 = ByteBuffer.wrap(bArr4).order(ByteOrder.BIG_ENDIAN).getInt();
                        }
                        provisionedMeshNode2 = provisionedMeshNode3;
                        this.h.onAppKeyBindStatusReceived(provisionedMeshNode3, z3, b10, AddressUtils.getUnicastAddressInt(bArr14), ByteBuffer.wrap(bArr15).order(ByteOrder.BIG_ENDIAN).getShort(), i5);
                        j = j4;
                    } else if (iN2 == -32187) {
                        iActionListener = iActionListener2;
                        long j5 = jA;
                        String str2 = f2125a;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Received scene register status: ");
                        sb2.append(ConvertUtils.bytes2HexString(bArrU));
                        sb2.append(", userCallback: ");
                        sb2.append(iActionListener);
                        sb2.append(", callbackKey: ");
                        sb2.append(j5);
                        a.a.a.a.b.m.a.a(str2, sb2.toString());
                        byte b11 = bArrU[2];
                        if (bArrU.length >= 5) {
                            s = (short) (((bArrU[4] & 255) << (bArrU[3] + 8)) & 255);
                            if (bArrU.length > 5) {
                                bArr5 = new byte[bArrU.length - 5];
                                System.arraycopy(bArrU, 5, bArr5, 0, bArrU.length - 5);
                            } else {
                                bArr5 = null;
                            }
                        } else {
                            bArr5 = null;
                            s = 0;
                        }
                        if (iActionListener == null) {
                            j = j5;
                            provisionedMeshNode2 = provisionedMeshNode3;
                        } else {
                            pair = new Pair(0, new SceneRegisterStatus(b11, s, bArr5));
                            if (f2127c != null && (pair2 = f2127c.get(Integer.valueOf(AddressUtils.getUnicastAddressInt(provisionedMeshNode3.getUnicastAddress())))) != null) {
                                byte[] bArrO2 = aVar2.o();
                                byte[] bArr16 = new byte[bArrO2.length + 2];
                                System.arraycopy(pair2.second, 0, bArr16, 0, 2);
                                System.arraycopy(bArrO2, 0, bArr16, 2, bArrO2.length);
                                this.h.onCommonMessageStatusReceived(provisionedMeshNode3, cVarH.r(), Integer.toHexString(((Integer) pair2.first).intValue()), bArr16, aVar);
                            }
                            i = iN2;
                            j = j5;
                            provisionedMeshNode2 = provisionedMeshNode3;
                        }
                    } else {
                        if (iN2 == -35) {
                            iActionListener = iActionListener2;
                            long j6 = jA;
                            byte[] bArrO3 = aVar2.o();
                            if (bArrO3 != null && bArrO3.length > 0) {
                                byte b12 = bArrO3[0];
                                if ((b12 & 1) != 1 || bArrO3.length <= 1) {
                                    i6 = 1;
                                    bValueOf = null;
                                } else {
                                    bValueOf = Byte.valueOf(bArrO3[1]);
                                    i6 = (byte) 4;
                                }
                                if ((b12 & 2) != 2 || bArrO3.length <= 3) {
                                    bArr6 = null;
                                } else {
                                    bArr6 = new byte[]{bArrO3[2], bArrO3[3]};
                                    i6 = (byte) (i6 + 4);
                                }
                                if ((b12 & 4) != 4 || bArrO3.length <= 5) {
                                    bArr7 = null;
                                } else {
                                    bArr7 = new byte[]{bArrO3[4], bArrO3[5]};
                                    i6 = (byte) (i6 + 4);
                                }
                                if ((b12 & 8) != 8 || bArrO3.length <= 7) {
                                    bArr8 = null;
                                } else {
                                    bArr8 = new byte[]{bArrO3[6], bArrO3[7]};
                                    i6 = (byte) (i6 + 4);
                                }
                                byte[] bArr17 = new byte[i6];
                                bArr17[0] = -128;
                                if (bValueOf != null) {
                                    System.arraycopy(SIGMeshBizRequestGenerator.Attribute.powerstate.attrType, 0, bArr17, 1, 2);
                                    byte b13 = (byte) 3;
                                    b2 = (byte) (b13 + 1);
                                    bArr17[b13] = bValueOf.byteValue();
                                } else {
                                    b2 = 1;
                                }
                                if (bArr6 != null) {
                                    System.arraycopy(SIGMeshBizRequestGenerator.Attribute.brightness.attrType, 0, bArr17, b2, 2);
                                    byte b14 = (byte) (b2 + 2);
                                    byte b15 = (byte) (b14 + 1);
                                    bArr17[b14] = bArr6[0];
                                    bArr17[b15] = bArr6[1];
                                    b2 = (byte) (b15 + 1);
                                }
                                if (bArr7 != null) {
                                    System.arraycopy(SIGMeshBizRequestGenerator.Attribute.colorTemperature.attrType, 0, bArr17, b2, 2);
                                    byte b16 = (byte) (b2 + 2);
                                    byte b17 = (byte) (b16 + 1);
                                    bArr17[b16] = bArr7[0];
                                    b2 = (byte) (b17 + 1);
                                    bArr17[b17] = bArr7[1];
                                }
                                if (bArr8 != null) {
                                    System.arraycopy(SIGMeshBizRequestGenerator.Attribute.mode.attrType, 0, bArr17, b2, 2);
                                    byte b18 = (byte) (b2 + 2);
                                    bArr17[b18] = bArr8[0];
                                    bArr17[(byte) (b18 + 1)] = bArr8[1];
                                }
                                this.h.onCommonMessageStatusReceived(provisionedMeshNode3, cVarH.r(), "D3A801", bArr17, aVar);
                                j = j6;
                                provisionedMeshNode2 = provisionedMeshNode3;
                            }
                            return;
                        }
                        if (iN2 == 2) {
                            iActionListener = iActionListener2;
                            long j7 = jA;
                            a.a.a.a.b.m.a.a(f2125a, "Received composition data status");
                            this.h.onCompositionDataStatusReceived(provisionedMeshNode3);
                            j = j7;
                            provisionedMeshNode2 = provisionedMeshNode3;
                        } else if (iN2 == 94) {
                            long j8 = jA;
                            a.a.a.a.b.m.a.a(f2125a, "Received scene status");
                            if (bArrU.length < 4) {
                                a.a.a.a.b.m.a.d(f2125a, "Illegal Scene status access message");
                                pair = new Pair(-14, "Illegal Scene Status access message, required parameters length >= 3 bytes, but received reply data less than 3 bytes");
                                iActionListener = iActionListener2;
                                i = iN2;
                                j = j8;
                                provisionedMeshNode2 = provisionedMeshNode3;
                            } else {
                                iActionListener = iActionListener2;
                                this.h.onCommonMessageStatusReceived(provisionedMeshNode3, cVarH.r(), "5E", aVar2.o(), aVar);
                                byte b19 = bArrU[1];
                                short s2 = (short) (((bArrU[3] & 255) << (bArrU[2] + 8)) & 255);
                                Short shValueOf = bArrU.length >= 6 ? Short.valueOf((short) (((bArrU[5] & 255) << (bArrU[4] + 8)) & 255)) : null;
                                if (iActionListener == null) {
                                    j = j8;
                                    provisionedMeshNode2 = provisionedMeshNode3;
                                } else {
                                    pair = new Pair(0, new SceneStatus(b19, s2, shValueOf));
                                    i = iN2;
                                    j = j8;
                                    provisionedMeshNode2 = provisionedMeshNode3;
                                }
                            }
                        } else if (iN2 == 13871105) {
                            long j9 = jA;
                            Pair pair3 = new Pair(0, aVar2.o());
                            this.h.onCommonMessageStatusReceived(provisionedMeshNode3, cVarH.r(), "D3A801", aVar2.o(), aVar);
                            i = iN2;
                            iActionListener = iActionListener2;
                            pair = pair3;
                            j = j9;
                            provisionedMeshNode2 = provisionedMeshNode3;
                        } else if (iN2 != 14592001) {
                            int i7 = (bArrU[0] & 240) >> 6;
                            if (i7 == 0) {
                                i7 = 1;
                            }
                            byte[] bArrO4 = aVar2.o();
                            if (i7 == 1) {
                                iN = iN2 & 255;
                            } else if (i7 == 2) {
                                iN = aVar2.n() & 65535;
                            } else {
                                iN = aVar2.n() & 16777215;
                            }
                            long j10 = jA;
                            this.h.onCommonMessageStatusReceived(provisionedMeshNode3, cVarH.r(), Integer.toHexString(iN), bArrO4, aVar);
                            if (iActionListener2 != null) {
                                pair = new Pair(0, aVar2.o());
                                i = iN;
                                iActionListener = iActionListener2;
                                j = j10;
                                provisionedMeshNode2 = provisionedMeshNode3;
                            } else {
                                i = iN;
                                iActionListener = iActionListener2;
                                pair = null;
                                j = j10;
                                provisionedMeshNode2 = provisionedMeshNode3;
                            }
                        } else {
                            long j11 = jA;
                            byte[] bArrO5 = aVar2.o();
                            if (bArrO5 != null && bArrO5.length >= 3 && Arrays.equals(new byte[]{bArrO5[2], bArrO5[1]}, new byte[]{-16, 31})) {
                                if (this.i == 0 || System.currentTimeMillis() - this.i >= 60000) {
                                    this.i = System.currentTimeMillis();
                                    ByteBuffer byteBufferOrder2 = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                                    byteBufferOrder2.putInt((int) (this.i / 1000));
                                    byte[] bArr18 = {bArrO5[0], 31, -16, 0, 0, 0, 0, (byte) ((TimeZone.getDefault().getRawOffset() / 3600) / 1000)};
                                    System.arraycopy(byteBufferOrder2.array(), 0, bArr18, 3, 4);
                                    a(provisionedMeshNode3, ProxyProtocolMessageType.NetworkPDU, true, provisionedMeshNode3.getAddedAppKeys().get(0), new byte[]{-49, -1}, false, 0, 14657537, bArr18);
                                    return;
                                }
                                return;
                            }
                            iActionListener = iActionListener2;
                            j = j11;
                            provisionedMeshNode2 = provisionedMeshNode3;
                        }
                    }
                    i = iN2;
                    pair = null;
                } else {
                    iActionListener = iActionListener2;
                    j = jA;
                    provisionedMeshNode2 = provisionedMeshNode3;
                    a.a.a.a.b.m.a.a(f2125a, "Received config app key status");
                    byte b20 = bArrU[2];
                    boolean z4 = b20 == 0;
                    byte[] bArr19 = {(byte) (15 & bArrU[4]), bArrU[3]};
                    byte[] bArr20 = {(byte) ((bArrU[5] & 240) >> 4), (byte) ((bArrU[5] << 4) | ((bArrU[4] & 240) >> 4))};
                    this.h.onAppKeyStatusReceived(provisionedMeshNode2, z4, b20, ByteBuffer.wrap(bArr19).order(ByteOrder.BIG_ENDIAN).getShort(), ByteBuffer.wrap(bArr20).order(ByteOrder.BIG_ENDIAN).getShort());
                    if (iActionListener == null) {
                        i = iN2;
                        pair = null;
                    } else {
                        pair = new Pair(0, new CfgAppKeyStatus(b20, bArr19, bArr20));
                        i = iN2;
                    }
                }
                if (iActionListener != null && pair != null) {
                    f2126b.remove(j);
                    if (((Integer) pair.first).intValue() == 0) {
                        iActionListener.onSuccess(pair.second);
                    } else if (pair.second instanceof String) {
                        iActionListener.onFailure(((Integer) pair.first).intValue(), (String) pair.second);
                    }
                }
                b.c.a.b().c().a(provisionedMeshNode2.getNetworkKey(), cVarH.r(), i, cVarH.o(), cVarH.s(), 0);
            }
        } catch (Exception e) {
            a.a.a.a.b.m.a.b(f2125a, e.toString());
        }
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, int i, String str, int i2) {
        provisionedMeshNode.setAddedAppKey(i, str);
        ConfigAppKeyAdd configAppKeyAdd = new ConfigAppKeyAdd(this.e, provisionedMeshNode, this, i2, str, i);
        configAppKeyAdd.setTransportCallbacks(this.f);
        configAppKeyAdd.setStatusCallbacks(this.h);
        configAppKeyAdd.executeSend();
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, int i, byte[] bArr, int i2, int i3) {
        ConfigModelAppBind configModelAppBind = new ConfigModelAppBind(this.e, provisionedMeshNode, this, i, bArr, i2, i3);
        configModelAppBind.setTransportCallbacks(this.f);
        configModelAppBind.setStatusCallbacks(this.h);
        configModelAppBind.executeSend();
    }

    public void a(ConfigModelPublicationSetParams configModelPublicationSetParams) {
        ConfigModelPublicationSet configModelPublicationSet = new ConfigModelPublicationSet(this.e, configModelPublicationSetParams, this);
        configModelPublicationSet.setTransportCallbacks(this.f);
        configModelPublicationSet.setStatusCallbacks(this.h);
        configModelPublicationSet.executeSend();
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, int i, byte[] bArr, byte[] bArr2, int i2) {
        ConfigModelSubscriptionAdd configModelSubscriptionAdd = new ConfigModelSubscriptionAdd(this.e, provisionedMeshNode, this, i, bArr, bArr2, i2);
        configModelSubscriptionAdd.setTransportCallbacks(this.f);
        configModelSubscriptionAdd.setStatusCallbacks(this.h);
        configModelSubscriptionAdd.executeSend();
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, ProxyProtocolMessageType proxyProtocolMessageType, boolean z, String str, byte[] bArr, boolean z2, int i, int i2, byte[] bArr2) {
        if (i2 == 33350 || i2 == 33438) {
            if (f2127c == null) {
                f2127c = new LinkedHashMap();
            }
            f2127c.put(Integer.valueOf(AddressUtils.getUnicastAddressInt(bArr)), new Pair<>(Integer.valueOf(i2), bArr2));
        }
        a.a.a.a.b.m.a.c(f2125a, "sendCommonMessage called, opcode: " + i2 + ", address: " + MeshParserUtils.bytesToHex(bArr, true));
        if (i2 == 32795 || i2 == 32796) {
            if (f2128d == null) {
                f2128d = new LinkedHashMap();
            }
            a.a.a.a.b.m.a.c(f2125a, "sendCommonMessage called, put record: " + i2);
            f2128d.put(Integer.valueOf(AddressUtils.getUnicastAddressInt(bArr)), Integer.valueOf(i2));
        }
        CommonMessageV2 commonMessageV2 = new CommonMessageV2(this.e, provisionedMeshNode, proxyProtocolMessageType, z, this, str, z2, bArr, i, i2, bArr2);
        commonMessageV2.setTransportCallbacks(this.f);
        commonMessageV2.setStatusCallbacks(this.h);
        commonMessageV2.executeSend();
        b.c.a.b().c().a(provisionedMeshNode.getNetworkKey(), bArr, i2, bArr2);
    }

    public void a(byte[] bArr, int i, byte[] bArr2, IActionListener<Object> iActionListener) {
        long jA = a(bArr, i, bArr2);
        a.a.a.a.b.m.a.a(f2125a, "Register message listener with key: " + jA + ", address: " + MeshParserUtils.bytesToHex(bArr, false) + ", opcode: " + i);
        if (f2126b.get(jA) != null) {
            a.a.a.a.b.m.a.a(f2125a, String.format("Update desired message listener for(%s:%d)", MeshParserUtils.bytesToHex(bArr, false), Integer.valueOf(i)));
        }
        f2126b.put(jA, iActionListener);
    }

    public final long a(byte[] bArr, int i) {
        long j = ((((long) (bArr[0] & 255)) | 0) << 8) | ((long) (bArr[1] & 255));
        for (byte b2 : MeshParserUtils.getOpCodes(i)) {
            j = (j << 8) | ((long) (b2 & 255));
        }
        return j;
    }

    public final long a(byte[] bArr, int i, byte[] bArr2) {
        long j = ((((long) (bArr[0] & 255)) | 0) << 8) | ((long) (bArr[1] & 255));
        byte[] opCodes = MeshParserUtils.getOpCodes(i);
        for (byte b2 : opCodes) {
            j = (j << 8) | ((long) (b2 & 255));
        }
        return (bArr2 == null || bArr2.length != 2) ? j : (((j << 8) | ((long) (bArr2[0] & 255))) << 8) | ((long) (bArr2[1] & 255));
    }
}
