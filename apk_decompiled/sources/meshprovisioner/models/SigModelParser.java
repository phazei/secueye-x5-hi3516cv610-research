package meshprovisioner.models;

import a.a.a.a.b.m.a;
import com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayErrorCode;
import com.heytap.mcssdk.constant.MessageConstant;
import java.util.Locale;

/* JADX INFO: loaded from: classes4.dex */
public class SigModelParser {
    public static final short CONFIGURATION_CLIENT = 1;
    public static final short CONFIGURATION_SERVER = 0;
    public static final short GENERIC_ADMIN_PROPERTY_SERVER = 4113;
    public static final short GENERIC_BATTERY_CLIENT = 4109;
    public static final short GENERIC_BATTERY_SERVER = 4108;
    public static final short GENERIC_CLIENT_PROPERTY_SERVER = 4116;
    public static final short GENERIC_DEFAULT_TRANSITION_TIME_CLIENT = 4101;
    public static final short GENERIC_DEFAULT_TRANSITION_TIME_SERVER = 4100;
    public static final short GENERIC_LEVEL_CLIENT = 4099;
    public static final short GENERIC_LEVEL_SERVER = 4098;
    public static final short GENERIC_LOCATION_CLIENT = 4112;
    public static final short GENERIC_LOCATION_SERVER = 4110;
    public static final short GENERIC_LOCATION_SETUP_SERVER = 4111;
    public static final short GENERIC_MANUFACTURER_PROPERTY_SERVER = 4114;
    public static final short GENERIC_ON_OFF_CLIENT = 4097;
    public static final short GENERIC_ON_OFF_SERVER = 4096;
    public static final short GENERIC_POWER_LEVEL_CLIENT = 4107;
    public static final short GENERIC_POWER_LEVEL_SERVER = 4105;
    public static final short GENERIC_POWER_LEVEL_SETUP_SERVER = 4106;
    public static final short GENERIC_POWER_ON_OFF_CLIENT = 4104;
    public static final short GENERIC_POWER_ON_OFF_SERVER = 4102;
    public static final short GENERIC_POWER_ON_OFF_SETUP_SERVER = 4103;
    public static final short GENERIC_PROPERTY_CLIENT = 4117;
    public static final short GENERIC_USER_PROPERTY_SERVER = 4115;
    public static final short HEALTH_CLIENT_MODEL = 3;
    public static final short HEALTH_SERVER_MODEL = 2;
    public static final short LIGHT_CTL_CLIENT = 4869;
    public static final short LIGHT_CTL_SERVER = 4867;
    public static final short LIGHT_CTL_SETUP_SERVER = 4868;
    public static final short LIGHT_CTL_TEMPERATURE_SERVER = 4870;
    public static final short LIGHT_HSL_CLIENT = 4873;
    public static final short LIGHT_HSL_HUE_SERVER = 4874;
    public static final short LIGHT_HSL_SATURATION_SERVER = 4875;
    public static final short LIGHT_HSL_SERVER = 4871;
    public static final short LIGHT_HSL_SETUP_SERVER = 4872;
    public static final short LIGHT_LC_CLIENT = 4881;
    public static final short LIGHT_LC_SERVER = 4879;
    public static final short LIGHT_LC_SETUP_SERVER = 4880;
    public static final short LIGHT_LIGHTNESS_CLIENT = 4866;
    public static final short LIGHT_LIGHTNESS_SERVER = 4864;
    public static final short LIGHT_LIGHTNESS_SETUP_SERVER = 4865;
    public static final short LIGHT_XYL_CLIENT = 4878;
    public static final short LIGHT_XYL_SERVER = 4876;
    public static final short LIGHT_XYL_SETUP_SERVER = 4877;
    public static final short SCENE_CLIENT = 4613;
    public static final short SCENE_SERVER = 4611;
    public static final short SCENE_SETUP_SERVER = 4612;
    public static final short SCHEDULER_CLIENT = 4616;
    public static final short SCHEDULER_SERVER = 4614;
    public static final short SCHEDULER_SETUP_SERVER = 4615;
    public static final short SENSOR_CLIENT = 4354;
    public static final short SENSOR_SERVER = 4352;
    public static final short SENSOR_SETUP_SERVER = 4353;
    public static final String TAG = "SigModelParser";
    public static final short TIME_CLIENT = 4610;
    public static final short TIME_SERVER = 4608;
    public static final short TIME_SETUP_SERVER = 4609;

    public static SigModel getSigModel(int i) {
        switch (i) {
            case 0:
                return new ConfigurationServerModel(i);
            case 1:
                return new ConfigurationClientModel(i);
            case 2:
                return new HealthServerModel(i);
            case 3:
                return new HealthClientModel(i);
            default:
                switch (i) {
                    case 4096:
                        return new GenericOnOffServerModel(i);
                    case 4097:
                        return new GenericOnOffClientModel(i);
                    case 4098:
                        return new GenericLevelServerModel(i);
                    case 4099:
                        return new GenericLevelClientModel(i);
                    case MessageConstant.MessageType.MESSAGE_ALARM /* 4100 */:
                        return new GenericDefaultTransitionTimeServer(i);
                    case 4101:
                        return new GenericDefaultTransitionTimeClient(i);
                    case 4102:
                        return new GenericPowerOnOffServer(i);
                    case 4103:
                        return new GenericPowerOnOffSetupServer(i);
                    case 4104:
                        return new GenericPowerOnOffClient(i);
                    case MessageConstant.MessageType.MESSAGE_CALL_BACK /* 4105 */:
                        return new GenericPowerLevelServer(i);
                    case 4106:
                        return new GenericPowerLevelSetupServer(i);
                    case 4107:
                        return new GenericPowerLevelClient(i);
                    case MessageConstant.MessageType.MESSAGE_REVOKE /* 4108 */:
                        return new GenericBatteryServer(i);
                    case 4109:
                        return new GenericBatteryClient(i);
                    case GatewayErrorCode.ERROR_INVOKE_PARAMS_INVALID /* 4110 */:
                        return new GenericLocationServer(i);
                    case 4111:
                        return new GenericLocationSetupServer(i);
                    case 4112:
                        return new GenericLocationClient(i);
                    case 4113:
                        return new GenericAdminPropertyServer(i);
                    case 4114:
                        return new GenericManufacturerPropertyServer(i);
                    case 4115:
                        return new GenericUserPropertyServer(i);
                    case 4116:
                        return new GenericClientPropertyServer(i);
                    case 4117:
                        return new GenericPropertyClient(i);
                    default:
                        switch (i) {
                            case 4352:
                                return new SensorServer(i);
                            case 4353:
                                return new SensorSetupServer(i);
                            case 4354:
                                return new SensorClient(i);
                            default:
                                switch (i) {
                                    case 4608:
                                        return new TimeServer(i);
                                    case 4609:
                                        return new TimeSetupServer(i);
                                    case BaseAppCompatActivity.sUiHidNavigationBarFlags /* 4610 */:
                                        return new TimeClient(i);
                                    case 4611:
                                        return new SceneServer(i);
                                    case 4612:
                                        return new SceneSetupServer(i);
                                    case 4613:
                                        return new SceneClient(i);
                                    case 4614:
                                        return new SchedulerServer(i);
                                    case 4615:
                                        return new SchedulerSetupServer(i);
                                    case 4616:
                                        return new SchedulerClient(i);
                                    default:
                                        switch (i) {
                                            case 4864:
                                                return new LightLightnessServer(i);
                                            case 4865:
                                                return new LightLightnessSetupServer(i);
                                            case 4866:
                                                return new LightLightnessClient(i);
                                            case 4867:
                                                return new LightCtlServer(i);
                                            case 4868:
                                                return new LightCtlSetupServer(i);
                                            case 4869:
                                                return new LightCtlClient(i);
                                            case 4870:
                                                return new LightCtlTemperatureServer(i);
                                            case 4871:
                                                return new LightHslServer(i);
                                            case 4872:
                                                return new LightHslSetupServer(i);
                                            case 4873:
                                                return new LightHslClient(i);
                                            case 4874:
                                                return new LightHslHueServer(i);
                                            case 4875:
                                                return new LightHslSaturationServer(i);
                                            case 4876:
                                                return new LightXylServer(i);
                                            case 4877:
                                                return new LightXylSetupServer(i);
                                            case 4878:
                                                return new LightXylClient(i);
                                            case 4879:
                                                return new LightLcServer(i);
                                            case 4880:
                                                return new LightLcSetupServer(i);
                                            case 4881:
                                                return new LightLightnessClient(i);
                                            default:
                                                a.a(TAG, "Model ID: " + String.format(Locale.US, "%04X", Integer.valueOf(i)));
                                                return null;
                                        }
                                }
                        }
                }
        }
    }
}
