package meshprovisioner.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import androidx.annotation.NonNull;
import b.InterfaceC0367a;
import com.alibaba.ailabs.iot.mesh.R;
import java.nio.ByteBuffer;
import meshprovisioner.states.UnprovisionedMeshNode;

/* JADX INFO: loaded from: classes4.dex */
public class UnprovisionedMeshNodeUtil {
    public static UnprovisionedMeshNode buildUnprovisionedMeshNode(@NonNull Context context, @NonNull String str, String str2, @NonNull String str3, int i, int i2, int i3, int i4, int i5, byte[] bArr, byte[] bArr2, InterfaceC0367a interfaceC0367a) {
        if (!BluetoothAdapter.checkBluetoothAddress(str)) {
            throw new IllegalArgumentException(context.getString(R.string.invalid_bluetooth_address));
        }
        if (!validateProvisioningDataInput(str3, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4))) {
            return null;
        }
        byte[] byteArray = MeshParserUtils.validateNetworkKeyInput(context, str3) ? MeshParserUtils.toByteArray(str3) : null;
        byte[] bArrAddKeyIndexPadding = MeshParserUtils.validateKeyIndexInput(context, Integer.valueOf(i)) ? MeshParserUtils.addKeyIndexPadding(Integer.valueOf(i)) : null;
        byte[] bArrArray = ByteBuffer.allocate(1).put((byte) i2).array();
        byte[] bArrArray2 = MeshParserUtils.validateIvIndexInput(context, Integer.valueOf(i3)) ? ByteBuffer.allocate(4).putInt(i3).array() : null;
        byte[] bArr3 = MeshParserUtils.validateUnicastAddressInput(context, Integer.valueOf(i4)) ? new byte[]{(byte) ((i4 >> 8) & 255), (byte) (i4 & 255)} : null;
        UnprovisionedMeshNode unprovisionedMeshNode = new UnprovisionedMeshNode();
        unprovisionedMeshNode.setBluetoothDeviceAddress(str);
        unprovisionedMeshNode.setBluetoothAddress(str);
        unprovisionedMeshNode.setNodeName(str2);
        unprovisionedMeshNode.setNetworkKey(byteArray);
        unprovisionedMeshNode.setKeyIndex(bArrAddKeyIndexPadding);
        unprovisionedMeshNode.setFlags(bArrArray);
        unprovisionedMeshNode.setIvIndex(bArrArray2);
        unprovisionedMeshNode.setUnicastAddress(bArr3);
        unprovisionedMeshNode.setTtl(i5);
        unprovisionedMeshNode.setConfigurationSrc(bArr);
        unprovisionedMeshNode.setServiceData(bArr2);
        unprovisionedMeshNode.setCloudComfirmationProvisioningCallbacks(interfaceC0367a);
        return unprovisionedMeshNode;
    }

    public static boolean validateProvisioningDataInput(String str, Integer num, Integer num2, Integer num3, Integer num4) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Network key cannot be null or empty!");
        }
        if (num == null) {
            throw new IllegalArgumentException("Key index cannot be null!");
        }
        if (num2 == null) {
            throw new IllegalArgumentException("Flags cannot be null!");
        }
        if (num3 == null) {
            throw new IllegalArgumentException("IV Index cannot be null!");
        }
        if (num4 != null) {
            return true;
        }
        throw new IllegalArgumentException("Unicast Address cannot be null!");
    }
}
