package com.seculink.app;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.seculink.app.databinding.ActivityAccessibilityBindingImpl;
import com.seculink.app.databinding.ActivityAdBindingImpl;
import com.seculink.app.databinding.ActivityAddDeviceBindingImpl;
import com.seculink.app.databinding.ActivityAuthBindingImpl;
import com.seculink.app.databinding.ActivityBleLinkFailedBindingImpl;
import com.seculink.app.databinding.ActivityBleLinkFailedTips4BindingImpl;
import com.seculink.app.databinding.ActivityBleNet4gSwitchBindingImpl;
import com.seculink.app.databinding.ActivityBleRouterBindingImpl;
import com.seculink.app.databinding.ActivityBleRouterLanDhcpBindingImpl;
import com.seculink.app.databinding.ActivityBleRouterSettingBindingImpl;
import com.seculink.app.databinding.ActivityBleRouterWlanDhcpBindingImpl;
import com.seculink.app.databinding.ActivityBleRouterWlanSettingBindingImpl;
import com.seculink.app.databinding.ActivityBleScanBindingImpl;
import com.seculink.app.databinding.ActivityChargeCameraBindingImpl;
import com.seculink.app.databinding.ActivityCloudDownloadBindingImpl;
import com.seculink.app.databinding.ActivityCloudStorageBindingImpl;
import com.seculink.app.databinding.ActivityConnectionFailedBindingImpl;
import com.seculink.app.databinding.ActivityCountrySupportedBindingImpl;
import com.seculink.app.databinding.ActivityCustomerServiceBindingImpl;
import com.seculink.app.databinding.ActivityFeedbackHistoryBindingImpl;
import com.seculink.app.databinding.ActivityFeedbackProblemBindingImpl;
import com.seculink.app.databinding.ActivityFeedbackRecordBindingImpl;
import com.seculink.app.databinding.ActivityGainServiceBindingImpl;
import com.seculink.app.databinding.ActivityGunballSettingBindingImpl;
import com.seculink.app.databinding.ActivityIpcameraDoubleEyeLayoutBindingImpl;
import com.seculink.app.databinding.ActivityIpcameraFourEyesBindingImpl;
import com.seculink.app.databinding.ActivityIpcameraLayoutBindingImpl;
import com.seculink.app.databinding.ActivityIpcameraThreeEyesBindingImpl;
import com.seculink.app.databinding.ActivityIpcameraThreeFalseEyesBindingImpl;
import com.seculink.app.databinding.ActivityLowPowerDiySettingsBindingImpl;
import com.seculink.app.databinding.ActivityLowPowerSettingsBindingImpl;
import com.seculink.app.databinding.ActivityMotionDetectBindingImpl;
import com.seculink.app.databinding.ActivityNanoSimTips2BindingImpl;
import com.seculink.app.databinding.ActivityNanoSimTips3BindingImpl;
import com.seculink.app.databinding.ActivityNanoSimTips4BindingImpl;
import com.seculink.app.databinding.ActivityNet4gSwitchBindingImpl;
import com.seculink.app.databinding.ActivityNetShareBindingImpl;
import com.seculink.app.databinding.ActivityNetWlanSettingBindingImpl;
import com.seculink.app.databinding.ActivityNightLightingSettingsBindingImpl;
import com.seculink.app.databinding.ActivityNvrYunHistoryBindingImpl;
import com.seculink.app.databinding.ActivityNvrYunSelectBindingImpl;
import com.seculink.app.databinding.ActivityOtherSimBindingImpl;
import com.seculink.app.databinding.ActivityPowerModeSettingsBindingImpl;
import com.seculink.app.databinding.ActivityPowerOnBindingImpl;
import com.seculink.app.databinding.ActivityPushHelpBindingImpl;
import com.seculink.app.databinding.ActivityPushHuaweiBindingImpl;
import com.seculink.app.databinding.ActivityPushMiBindingImpl;
import com.seculink.app.databinding.ActivityPushOppoBindingImpl;
import com.seculink.app.databinding.ActivityPushVivoBindingImpl;
import com.seculink.app.databinding.ActivityResetBindingImpl;
import com.seculink.app.databinding.ActivityRouterConnectedDeviceBindingImpl;
import com.seculink.app.databinding.ActivityRouterHotspotBindingImpl;
import com.seculink.app.databinding.ActivityRouterLanBindingImpl;
import com.seculink.app.databinding.ActivityRouterOtherSimBindingImpl;
import com.seculink.app.databinding.ActivitySearchBindingImpl;
import com.seculink.app.databinding.ActivitySelectSimBindingImpl;
import com.seculink.app.databinding.ActivitySimInputBindingImpl;
import com.seculink.app.databinding.ActivityThreeYunSelectBindingImpl;
import com.seculink.app.databinding.ActivityYunHistoryDetailsBindingImpl;
import com.seculink.app.databinding.AutoDoorLayoutBindingImpl;
import com.seculink.app.databinding.AutodoorRecyclerviewItemBindingImpl;
import com.seculink.app.databinding.CloudDownloadRecyclerviewItemBindingImpl;
import com.seculink.app.databinding.DialogConfirmBindingImpl;
import com.seculink.app.databinding.DialogScoreBindingImpl;
import com.seculink.app.databinding.DialogSuccessBindingImpl;
import com.seculink.app.databinding.DialogTitleConfirmBindingImpl;
import com.seculink.app.databinding.DialogWechatBindingImpl;
import com.seculink.app.databinding.ItemAddDeviceBindingImpl;
import com.seculink.app.databinding.ItemBleBindingImpl;
import com.seculink.app.databinding.ItemCloudImgBindingImpl;
import com.seculink.app.databinding.ItemConnectDeviceBindingImpl;
import com.seculink.app.databinding.ItemDialogListBindingImpl;
import com.seculink.app.databinding.ItemFaceBindingImpl;
import com.seculink.app.databinding.ItemFeedbackBindingImpl;
import com.seculink.app.databinding.ItemFeedbackReplyBindingImpl;
import com.seculink.app.databinding.ItemFourLiveBindingImpl;
import com.seculink.app.databinding.ItemLiveBindingImpl;
import com.seculink.app.databinding.ItemPayBindingImpl;
import com.seculink.app.databinding.ItemSearchBindingImpl;
import com.seculink.app.databinding.ItemShareHistoryBindingImpl;
import com.seculink.app.databinding.RecyclerItemActivityPlanBindingImpl;
import com.seculink.app.databinding.StartActivityBindingImpl;
import com.seculink.app.databinding.TtsVigilanceActivityLayoutBindingImpl;
import com.seculink.app.databinding.VigilanceActivityLayoutBindingImpl;
import com.seculink.app.databinding.VigilanceRecyclerviewItemBindingImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class DataBinderMapperImpl extends DataBinderMapper {
    private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(85);
    private static final int LAYOUT_ACTIVITYACCESSIBILITY = 1;
    private static final int LAYOUT_ACTIVITYAD = 2;
    private static final int LAYOUT_ACTIVITYADDDEVICE = 3;
    private static final int LAYOUT_ACTIVITYAUTH = 4;
    private static final int LAYOUT_ACTIVITYBLELINKFAILED = 5;
    private static final int LAYOUT_ACTIVITYBLELINKFAILEDTIPS4 = 6;
    private static final int LAYOUT_ACTIVITYBLENET4GSWITCH = 7;
    private static final int LAYOUT_ACTIVITYBLEROUTER = 8;
    private static final int LAYOUT_ACTIVITYBLEROUTERLANDHCP = 9;
    private static final int LAYOUT_ACTIVITYBLEROUTERSETTING = 10;
    private static final int LAYOUT_ACTIVITYBLEROUTERWLANDHCP = 11;
    private static final int LAYOUT_ACTIVITYBLEROUTERWLANSETTING = 12;
    private static final int LAYOUT_ACTIVITYBLESCAN = 13;
    private static final int LAYOUT_ACTIVITYCHARGECAMERA = 14;
    private static final int LAYOUT_ACTIVITYCLOUDDOWNLOAD = 15;
    private static final int LAYOUT_ACTIVITYCLOUDSTORAGE = 16;
    private static final int LAYOUT_ACTIVITYCONNECTIONFAILED = 17;
    private static final int LAYOUT_ACTIVITYCOUNTRYSUPPORTED = 18;
    private static final int LAYOUT_ACTIVITYCUSTOMERSERVICE = 19;
    private static final int LAYOUT_ACTIVITYFEEDBACKHISTORY = 20;
    private static final int LAYOUT_ACTIVITYFEEDBACKPROBLEM = 21;
    private static final int LAYOUT_ACTIVITYFEEDBACKRECORD = 22;
    private static final int LAYOUT_ACTIVITYGAINSERVICE = 23;
    private static final int LAYOUT_ACTIVITYGUNBALLSETTING = 24;
    private static final int LAYOUT_ACTIVITYIPCAMERADOUBLEEYELAYOUT = 25;
    private static final int LAYOUT_ACTIVITYIPCAMERAFOUREYES = 26;
    private static final int LAYOUT_ACTIVITYIPCAMERALAYOUT = 27;
    private static final int LAYOUT_ACTIVITYIPCAMERATHREEEYES = 28;
    private static final int LAYOUT_ACTIVITYIPCAMERATHREEFALSEEYES = 29;
    private static final int LAYOUT_ACTIVITYLOWPOWERDIYSETTINGS = 30;
    private static final int LAYOUT_ACTIVITYLOWPOWERSETTINGS = 31;
    private static final int LAYOUT_ACTIVITYMOTIONDETECT = 32;
    private static final int LAYOUT_ACTIVITYNANOSIMTIPS2 = 33;
    private static final int LAYOUT_ACTIVITYNANOSIMTIPS3 = 34;
    private static final int LAYOUT_ACTIVITYNANOSIMTIPS4 = 35;
    private static final int LAYOUT_ACTIVITYNET4GSWITCH = 36;
    private static final int LAYOUT_ACTIVITYNETSHARE = 37;
    private static final int LAYOUT_ACTIVITYNETWLANSETTING = 38;
    private static final int LAYOUT_ACTIVITYNIGHTLIGHTINGSETTINGS = 39;
    private static final int LAYOUT_ACTIVITYNVRYUNHISTORY = 40;
    private static final int LAYOUT_ACTIVITYNVRYUNSELECT = 41;
    private static final int LAYOUT_ACTIVITYOTHERSIM = 42;
    private static final int LAYOUT_ACTIVITYPOWERMODESETTINGS = 43;
    private static final int LAYOUT_ACTIVITYPOWERON = 44;
    private static final int LAYOUT_ACTIVITYPUSHHELP = 45;
    private static final int LAYOUT_ACTIVITYPUSHHUAWEI = 46;
    private static final int LAYOUT_ACTIVITYPUSHMI = 47;
    private static final int LAYOUT_ACTIVITYPUSHOPPO = 48;
    private static final int LAYOUT_ACTIVITYPUSHVIVO = 49;
    private static final int LAYOUT_ACTIVITYRESET = 50;
    private static final int LAYOUT_ACTIVITYROUTERCONNECTEDDEVICE = 51;
    private static final int LAYOUT_ACTIVITYROUTERHOTSPOT = 52;
    private static final int LAYOUT_ACTIVITYROUTERLAN = 53;
    private static final int LAYOUT_ACTIVITYROUTEROTHERSIM = 54;
    private static final int LAYOUT_ACTIVITYSEARCH = 55;
    private static final int LAYOUT_ACTIVITYSELECTSIM = 56;
    private static final int LAYOUT_ACTIVITYSIMINPUT = 57;
    private static final int LAYOUT_ACTIVITYTHREEYUNSELECT = 58;
    private static final int LAYOUT_ACTIVITYYUNHISTORYDETAILS = 59;
    private static final int LAYOUT_AUTODOORLAYOUT = 60;
    private static final int LAYOUT_AUTODOORRECYCLERVIEWITEM = 61;
    private static final int LAYOUT_CLOUDDOWNLOADRECYCLERVIEWITEM = 62;
    private static final int LAYOUT_DIALOGCONFIRM = 63;
    private static final int LAYOUT_DIALOGSCORE = 64;
    private static final int LAYOUT_DIALOGSUCCESS = 65;
    private static final int LAYOUT_DIALOGTITLECONFIRM = 66;
    private static final int LAYOUT_DIALOGWECHAT = 67;
    private static final int LAYOUT_ITEMADDDEVICE = 68;
    private static final int LAYOUT_ITEMBLE = 69;
    private static final int LAYOUT_ITEMCLOUDIMG = 70;
    private static final int LAYOUT_ITEMCONNECTDEVICE = 71;
    private static final int LAYOUT_ITEMDIALOGLIST = 72;
    private static final int LAYOUT_ITEMFACE = 73;
    private static final int LAYOUT_ITEMFEEDBACK = 74;
    private static final int LAYOUT_ITEMFEEDBACKREPLY = 75;
    private static final int LAYOUT_ITEMFOURLIVE = 76;
    private static final int LAYOUT_ITEMLIVE = 77;
    private static final int LAYOUT_ITEMPAY = 78;
    private static final int LAYOUT_ITEMSEARCH = 79;
    private static final int LAYOUT_ITEMSHAREHISTORY = 80;
    private static final int LAYOUT_RECYCLERITEMACTIVITYPLAN = 81;
    private static final int LAYOUT_STARTACTIVITY = 82;
    private static final int LAYOUT_TTSVIGILANCEACTIVITYLAYOUT = 83;
    private static final int LAYOUT_VIGILANCEACTIVITYLAYOUT = 84;
    private static final int LAYOUT_VIGILANCERECYCLERVIEWITEM = 85;

    static {
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_accessibility, 1);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ad, 2);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_add_device, 3);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_auth, 4);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ble_link_failed, 5);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ble_link_failed_tips4, 6);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ble_net_4g_switch, 7);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ble_router, 8);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ble_router_lan_dhcp, 9);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ble_router_setting, 10);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ble_router_wlan_dhcp, 11);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ble_router_wlan_setting, 12);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ble_scan, 13);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_charge_camera, 14);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_cloud_download, 15);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_cloud_storage, 16);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_connection_failed, 17);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_country_supported, 18);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_customer_service, 19);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_feedback_history, 20);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_feedback_problem, 21);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_feedback_record, 22);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_gain_service, 23);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_gunball_setting, 24);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ipcamera_double_eye_layout, 25);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ipcamera_four_eyes, 26);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ipcamera_layout, 27);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ipcamera_three_eyes, 28);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_ipcamera_three_false_eyes, 29);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_low_power_diy_settings, 30);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_low_power_settings, 31);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_motion_detect, 32);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_nano_sim_tips2, 33);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_nano_sim_tips3, 34);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_nano_sim_tips4, 35);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_net_4g_switch, 36);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_net_share, 37);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_net_wlan_setting, 38);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_night_lighting_settings, 39);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_nvr_yun_history, 40);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_nvr_yun_select, 41);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_other_sim, 42);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_power_mode_settings, 43);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_power_on, 44);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_push_help, 45);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_push_huawei, 46);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_push_mi, 47);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_push_oppo, 48);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_push_vivo, 49);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_reset, 50);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_router_connected_device, 51);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_router_hotspot, 52);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_router_lan, 53);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_router_other_sim, 54);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_search, 55);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_select_sim, 56);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_sim_input, 57);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_three_yun_select, 58);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.activity_yun_history_details, 59);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.auto_door_layout, 60);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.autodoor_recyclerview_item, 61);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.cloud_download_recyclerview_item, 62);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.dialog_confirm, 63);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.dialog_score, 64);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.dialog_success, 65);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.dialog_title_confirm, 66);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.dialog_wechat, 67);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.item_add_device, 68);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.item_ble, 69);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.item_cloud_img, 70);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.item_connect_device, 71);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.item_dialog_list, 72);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.item_face, 73);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.item_feedback, 74);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.item_feedback_reply, 75);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.item_four_live, 76);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.item_live, 77);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.item_pay, 78);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.item_search, 79);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.item_share_history, 80);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.recycler_item_activity_plan, 81);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.start_activity, 82);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.tts_vigilance_activity_layout, 83);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.vigilance_activity_layout, 84);
        INTERNAL_LAYOUT_ID_LOOKUP.put(R.layout.vigilance_recyclerview_item, 85);
    }

    private final ViewDataBinding internalGetViewDataBinding0(DataBindingComponent dataBindingComponent, View view2, int i, Object obj) {
        switch (i) {
            case 1:
                if ("layout/activity_accessibility_0".equals(obj)) {
                    return new ActivityAccessibilityBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_accessibility is invalid. Received: " + obj);
            case 2:
                if ("layout/activity_ad_0".equals(obj)) {
                    return new ActivityAdBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ad is invalid. Received: " + obj);
            case 3:
                if ("layout/activity_add_device_0".equals(obj)) {
                    return new ActivityAddDeviceBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_add_device is invalid. Received: " + obj);
            case 4:
                if ("layout/activity_auth_0".equals(obj)) {
                    return new ActivityAuthBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_auth is invalid. Received: " + obj);
            case 5:
                if ("layout/activity_ble_link_failed_0".equals(obj)) {
                    return new ActivityBleLinkFailedBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ble_link_failed is invalid. Received: " + obj);
            case 6:
                if ("layout/activity_ble_link_failed_tips4_0".equals(obj)) {
                    return new ActivityBleLinkFailedTips4BindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ble_link_failed_tips4 is invalid. Received: " + obj);
            case 7:
                if ("layout/activity_ble_net_4g_switch_0".equals(obj)) {
                    return new ActivityBleNet4gSwitchBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ble_net_4g_switch is invalid. Received: " + obj);
            case 8:
                if ("layout/activity_ble_router_0".equals(obj)) {
                    return new ActivityBleRouterBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ble_router is invalid. Received: " + obj);
            case 9:
                if ("layout/activity_ble_router_lan_dhcp_0".equals(obj)) {
                    return new ActivityBleRouterLanDhcpBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ble_router_lan_dhcp is invalid. Received: " + obj);
            case 10:
                if ("layout/activity_ble_router_setting_0".equals(obj)) {
                    return new ActivityBleRouterSettingBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ble_router_setting is invalid. Received: " + obj);
            case 11:
                if ("layout/activity_ble_router_wlan_dhcp_0".equals(obj)) {
                    return new ActivityBleRouterWlanDhcpBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ble_router_wlan_dhcp is invalid. Received: " + obj);
            case 12:
                if ("layout/activity_ble_router_wlan_setting_0".equals(obj)) {
                    return new ActivityBleRouterWlanSettingBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ble_router_wlan_setting is invalid. Received: " + obj);
            case 13:
                if ("layout/activity_ble_scan_0".equals(obj)) {
                    return new ActivityBleScanBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ble_scan is invalid. Received: " + obj);
            case 14:
                if ("layout/activity_charge_camera_0".equals(obj)) {
                    return new ActivityChargeCameraBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_charge_camera is invalid. Received: " + obj);
            case 15:
                if ("layout/activity_cloud_download_0".equals(obj)) {
                    return new ActivityCloudDownloadBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_cloud_download is invalid. Received: " + obj);
            case 16:
                if ("layout/activity_cloud_storage_0".equals(obj)) {
                    return new ActivityCloudStorageBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_cloud_storage is invalid. Received: " + obj);
            case 17:
                if ("layout/activity_connection_failed_0".equals(obj)) {
                    return new ActivityConnectionFailedBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_connection_failed is invalid. Received: " + obj);
            case 18:
                if ("layout/activity_country_supported_0".equals(obj)) {
                    return new ActivityCountrySupportedBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_country_supported is invalid. Received: " + obj);
            case 19:
                if ("layout/activity_customer_service_0".equals(obj)) {
                    return new ActivityCustomerServiceBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_customer_service is invalid. Received: " + obj);
            case 20:
                if ("layout/activity_feedback_history_0".equals(obj)) {
                    return new ActivityFeedbackHistoryBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_feedback_history is invalid. Received: " + obj);
            case 21:
                if ("layout/activity_feedback_problem_0".equals(obj)) {
                    return new ActivityFeedbackProblemBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_feedback_problem is invalid. Received: " + obj);
            case 22:
                if ("layout/activity_feedback_record_0".equals(obj)) {
                    return new ActivityFeedbackRecordBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_feedback_record is invalid. Received: " + obj);
            case 23:
                if ("layout/activity_gain_service_0".equals(obj)) {
                    return new ActivityGainServiceBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_gain_service is invalid. Received: " + obj);
            case 24:
                if ("layout/activity_gunball_setting_0".equals(obj)) {
                    return new ActivityGunballSettingBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_gunball_setting is invalid. Received: " + obj);
            case 25:
                if ("layout/activity_ipcamera_double_eye_layout_0".equals(obj)) {
                    return new ActivityIpcameraDoubleEyeLayoutBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ipcamera_double_eye_layout is invalid. Received: " + obj);
            case 26:
                if ("layout/activity_ipcamera_four_eyes_0".equals(obj)) {
                    return new ActivityIpcameraFourEyesBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ipcamera_four_eyes is invalid. Received: " + obj);
            case 27:
                if ("layout/activity_ipcamera_layout_0".equals(obj)) {
                    return new ActivityIpcameraLayoutBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ipcamera_layout is invalid. Received: " + obj);
            case 28:
                if ("layout/activity_ipcamera_three_eyes_0".equals(obj)) {
                    return new ActivityIpcameraThreeEyesBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ipcamera_three_eyes is invalid. Received: " + obj);
            case 29:
                if ("layout/activity_ipcamera_three_false_eyes_0".equals(obj)) {
                    return new ActivityIpcameraThreeFalseEyesBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_ipcamera_three_false_eyes is invalid. Received: " + obj);
            case 30:
                if ("layout/activity_low_power_diy_settings_0".equals(obj)) {
                    return new ActivityLowPowerDiySettingsBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_low_power_diy_settings is invalid. Received: " + obj);
            case 31:
                if ("layout/activity_low_power_settings_0".equals(obj)) {
                    return new ActivityLowPowerSettingsBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_low_power_settings is invalid. Received: " + obj);
            case 32:
                if ("layout/activity_motion_detect_0".equals(obj)) {
                    return new ActivityMotionDetectBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_motion_detect is invalid. Received: " + obj);
            case 33:
                if ("layout/activity_nano_sim_tips2_0".equals(obj)) {
                    return new ActivityNanoSimTips2BindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_nano_sim_tips2 is invalid. Received: " + obj);
            case 34:
                if ("layout/activity_nano_sim_tips3_0".equals(obj)) {
                    return new ActivityNanoSimTips3BindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_nano_sim_tips3 is invalid. Received: " + obj);
            case 35:
                if ("layout/activity_nano_sim_tips4_0".equals(obj)) {
                    return new ActivityNanoSimTips4BindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_nano_sim_tips4 is invalid. Received: " + obj);
            case 36:
                if ("layout/activity_net_4g_switch_0".equals(obj)) {
                    return new ActivityNet4gSwitchBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_net_4g_switch is invalid. Received: " + obj);
            case 37:
                if ("layout/activity_net_share_0".equals(obj)) {
                    return new ActivityNetShareBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_net_share is invalid. Received: " + obj);
            case 38:
                if ("layout/activity_net_wlan_setting_0".equals(obj)) {
                    return new ActivityNetWlanSettingBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_net_wlan_setting is invalid. Received: " + obj);
            case 39:
                if ("layout/activity_night_lighting_settings_0".equals(obj)) {
                    return new ActivityNightLightingSettingsBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_night_lighting_settings is invalid. Received: " + obj);
            case 40:
                if ("layout/activity_nvr_yun_history_0".equals(obj)) {
                    return new ActivityNvrYunHistoryBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_nvr_yun_history is invalid. Received: " + obj);
            case 41:
                if ("layout/activity_nvr_yun_select_0".equals(obj)) {
                    return new ActivityNvrYunSelectBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_nvr_yun_select is invalid. Received: " + obj);
            case 42:
                if ("layout/activity_other_sim_0".equals(obj)) {
                    return new ActivityOtherSimBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_other_sim is invalid. Received: " + obj);
            case 43:
                if ("layout/activity_power_mode_settings_0".equals(obj)) {
                    return new ActivityPowerModeSettingsBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_power_mode_settings is invalid. Received: " + obj);
            case 44:
                if ("layout/activity_power_on_0".equals(obj)) {
                    return new ActivityPowerOnBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_power_on is invalid. Received: " + obj);
            case 45:
                if ("layout/activity_push_help_0".equals(obj)) {
                    return new ActivityPushHelpBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_push_help is invalid. Received: " + obj);
            case 46:
                if ("layout/activity_push_huawei_0".equals(obj)) {
                    return new ActivityPushHuaweiBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_push_huawei is invalid. Received: " + obj);
            case 47:
                if ("layout/activity_push_mi_0".equals(obj)) {
                    return new ActivityPushMiBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_push_mi is invalid. Received: " + obj);
            case 48:
                if ("layout/activity_push_oppo_0".equals(obj)) {
                    return new ActivityPushOppoBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_push_oppo is invalid. Received: " + obj);
            case 49:
                if ("layout/activity_push_vivo_0".equals(obj)) {
                    return new ActivityPushVivoBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_push_vivo is invalid. Received: " + obj);
            case 50:
                if ("layout/activity_reset_0".equals(obj)) {
                    return new ActivityResetBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_reset is invalid. Received: " + obj);
            default:
                return null;
        }
    }

    private final ViewDataBinding internalGetViewDataBinding1(DataBindingComponent dataBindingComponent, View view2, int i, Object obj) {
        switch (i) {
            case 51:
                if ("layout/activity_router_connected_device_0".equals(obj)) {
                    return new ActivityRouterConnectedDeviceBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_router_connected_device is invalid. Received: " + obj);
            case 52:
                if ("layout/activity_router_hotspot_0".equals(obj)) {
                    return new ActivityRouterHotspotBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_router_hotspot is invalid. Received: " + obj);
            case 53:
                if ("layout/activity_router_lan_0".equals(obj)) {
                    return new ActivityRouterLanBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_router_lan is invalid. Received: " + obj);
            case 54:
                if ("layout/activity_router_other_sim_0".equals(obj)) {
                    return new ActivityRouterOtherSimBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_router_other_sim is invalid. Received: " + obj);
            case 55:
                if ("layout/activity_search_0".equals(obj)) {
                    return new ActivitySearchBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_search is invalid. Received: " + obj);
            case 56:
                if ("layout/activity_select_sim_0".equals(obj)) {
                    return new ActivitySelectSimBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_select_sim is invalid. Received: " + obj);
            case 57:
                if ("layout/activity_sim_input_0".equals(obj)) {
                    return new ActivitySimInputBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_sim_input is invalid. Received: " + obj);
            case 58:
                if ("layout/activity_three_yun_select_0".equals(obj)) {
                    return new ActivityThreeYunSelectBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_three_yun_select is invalid. Received: " + obj);
            case 59:
                if ("layout/activity_yun_history_details_0".equals(obj)) {
                    return new ActivityYunHistoryDetailsBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for activity_yun_history_details is invalid. Received: " + obj);
            case 60:
                if ("layout/auto_door_layout_0".equals(obj)) {
                    return new AutoDoorLayoutBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for auto_door_layout is invalid. Received: " + obj);
            case 61:
                if ("layout/autodoor_recyclerview_item_0".equals(obj)) {
                    return new AutodoorRecyclerviewItemBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for autodoor_recyclerview_item is invalid. Received: " + obj);
            case 62:
                if ("layout/cloud_download_recyclerview_item_0".equals(obj)) {
                    return new CloudDownloadRecyclerviewItemBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for cloud_download_recyclerview_item is invalid. Received: " + obj);
            case 63:
                if ("layout/dialog_confirm_0".equals(obj)) {
                    return new DialogConfirmBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for dialog_confirm is invalid. Received: " + obj);
            case 64:
                if ("layout/dialog_score_0".equals(obj)) {
                    return new DialogScoreBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for dialog_score is invalid. Received: " + obj);
            case 65:
                if ("layout/dialog_success_0".equals(obj)) {
                    return new DialogSuccessBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for dialog_success is invalid. Received: " + obj);
            case 66:
                if ("layout/dialog_title_confirm_0".equals(obj)) {
                    return new DialogTitleConfirmBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for dialog_title_confirm is invalid. Received: " + obj);
            case 67:
                if ("layout/dialog_wechat_0".equals(obj)) {
                    return new DialogWechatBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for dialog_wechat is invalid. Received: " + obj);
            case 68:
                if ("layout/item_add_device_0".equals(obj)) {
                    return new ItemAddDeviceBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for item_add_device is invalid. Received: " + obj);
            case 69:
                if ("layout/item_ble_0".equals(obj)) {
                    return new ItemBleBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for item_ble is invalid. Received: " + obj);
            case 70:
                if ("layout/item_cloud_img_0".equals(obj)) {
                    return new ItemCloudImgBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for item_cloud_img is invalid. Received: " + obj);
            case 71:
                if ("layout/item_connect_device_0".equals(obj)) {
                    return new ItemConnectDeviceBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for item_connect_device is invalid. Received: " + obj);
            case 72:
                if ("layout/item_dialog_list_0".equals(obj)) {
                    return new ItemDialogListBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for item_dialog_list is invalid. Received: " + obj);
            case 73:
                if ("layout/item_face_0".equals(obj)) {
                    return new ItemFaceBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for item_face is invalid. Received: " + obj);
            case 74:
                if ("layout/item_feedback_0".equals(obj)) {
                    return new ItemFeedbackBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for item_feedback is invalid. Received: " + obj);
            case 75:
                if ("layout/item_feedback_reply_0".equals(obj)) {
                    return new ItemFeedbackReplyBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for item_feedback_reply is invalid. Received: " + obj);
            case 76:
                if ("layout/item_four_live_0".equals(obj)) {
                    return new ItemFourLiveBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for item_four_live is invalid. Received: " + obj);
            case 77:
                if ("layout/item_live_0".equals(obj)) {
                    return new ItemLiveBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for item_live is invalid. Received: " + obj);
            case 78:
                if ("layout/item_pay_0".equals(obj)) {
                    return new ItemPayBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for item_pay is invalid. Received: " + obj);
            case 79:
                if ("layout/item_search_0".equals(obj)) {
                    return new ItemSearchBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for item_search is invalid. Received: " + obj);
            case 80:
                if ("layout/item_share_history_0".equals(obj)) {
                    return new ItemShareHistoryBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for item_share_history is invalid. Received: " + obj);
            case 81:
                if ("layout/recycler_item_activity_plan_0".equals(obj)) {
                    return new RecyclerItemActivityPlanBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for recycler_item_activity_plan is invalid. Received: " + obj);
            case 82:
                if ("layout/start_activity_0".equals(obj)) {
                    return new StartActivityBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for start_activity is invalid. Received: " + obj);
            case 83:
                if ("layout/tts_vigilance_activity_layout_0".equals(obj)) {
                    return new TtsVigilanceActivityLayoutBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for tts_vigilance_activity_layout is invalid. Received: " + obj);
            case 84:
                if ("layout/vigilance_activity_layout_0".equals(obj)) {
                    return new VigilanceActivityLayoutBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for vigilance_activity_layout is invalid. Received: " + obj);
            case 85:
                if ("layout/vigilance_recyclerview_item_0".equals(obj)) {
                    return new VigilanceRecyclerviewItemBindingImpl(dataBindingComponent, view2);
                }
                throw new IllegalArgumentException("The tag for vigilance_recyclerview_item is invalid. Received: " + obj);
            default:
                return null;
        }
    }

    @Override // androidx.databinding.DataBinderMapper
    public ViewDataBinding getDataBinder(DataBindingComponent dataBindingComponent, View view2, int i) {
        int i2 = INTERNAL_LAYOUT_ID_LOOKUP.get(i);
        if (i2 <= 0) {
            return null;
        }
        Object tag = view2.getTag();
        if (tag == null) {
            throw new RuntimeException("view must have a tag");
        }
        switch ((i2 - 1) / 50) {
            case 0:
                return internalGetViewDataBinding0(dataBindingComponent, view2, i2, tag);
            case 1:
                return internalGetViewDataBinding1(dataBindingComponent, view2, i2, tag);
            default:
                return null;
        }
    }

    @Override // androidx.databinding.DataBinderMapper
    public ViewDataBinding getDataBinder(DataBindingComponent dataBindingComponent, View[] viewArr, int i) {
        if (viewArr == null || viewArr.length == 0 || INTERNAL_LAYOUT_ID_LOOKUP.get(i) <= 0 || viewArr[0].getTag() != null) {
            return null;
        }
        throw new RuntimeException("view must have a tag");
    }

    @Override // androidx.databinding.DataBinderMapper
    public int getLayoutId(String str) {
        Integer num;
        if (str == null || (num = InnerLayoutIdLookup.sKeys.get(str)) == null) {
            return 0;
        }
        return num.intValue();
    }

    @Override // androidx.databinding.DataBinderMapper
    public String convertBrIdToString(int i) {
        return InnerBrLookup.sKeys.get(i);
    }

    @Override // androidx.databinding.DataBinderMapper
    public List<DataBinderMapper> collectDependencies() {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
        return arrayList;
    }

    private static class InnerBrLookup {
        static final SparseArray<String> sKeys = new SparseArray<>(4);

        private InnerBrLookup() {
        }

        static {
            sKeys.put(0, "_all");
            sKeys.put(1, "nvrOwner");
            sKeys.put(2, "model");
        }
    }

    private static class InnerLayoutIdLookup {
        static final HashMap<String, Integer> sKeys = new HashMap<>(85);

        private InnerLayoutIdLookup() {
        }

        static {
            sKeys.put("layout/activity_accessibility_0", Integer.valueOf(R.layout.activity_accessibility));
            sKeys.put("layout/activity_ad_0", Integer.valueOf(R.layout.activity_ad));
            sKeys.put("layout/activity_add_device_0", Integer.valueOf(R.layout.activity_add_device));
            sKeys.put("layout/activity_auth_0", Integer.valueOf(R.layout.activity_auth));
            sKeys.put("layout/activity_ble_link_failed_0", Integer.valueOf(R.layout.activity_ble_link_failed));
            sKeys.put("layout/activity_ble_link_failed_tips4_0", Integer.valueOf(R.layout.activity_ble_link_failed_tips4));
            sKeys.put("layout/activity_ble_net_4g_switch_0", Integer.valueOf(R.layout.activity_ble_net_4g_switch));
            sKeys.put("layout/activity_ble_router_0", Integer.valueOf(R.layout.activity_ble_router));
            sKeys.put("layout/activity_ble_router_lan_dhcp_0", Integer.valueOf(R.layout.activity_ble_router_lan_dhcp));
            sKeys.put("layout/activity_ble_router_setting_0", Integer.valueOf(R.layout.activity_ble_router_setting));
            sKeys.put("layout/activity_ble_router_wlan_dhcp_0", Integer.valueOf(R.layout.activity_ble_router_wlan_dhcp));
            sKeys.put("layout/activity_ble_router_wlan_setting_0", Integer.valueOf(R.layout.activity_ble_router_wlan_setting));
            sKeys.put("layout/activity_ble_scan_0", Integer.valueOf(R.layout.activity_ble_scan));
            sKeys.put("layout/activity_charge_camera_0", Integer.valueOf(R.layout.activity_charge_camera));
            sKeys.put("layout/activity_cloud_download_0", Integer.valueOf(R.layout.activity_cloud_download));
            sKeys.put("layout/activity_cloud_storage_0", Integer.valueOf(R.layout.activity_cloud_storage));
            sKeys.put("layout/activity_connection_failed_0", Integer.valueOf(R.layout.activity_connection_failed));
            sKeys.put("layout/activity_country_supported_0", Integer.valueOf(R.layout.activity_country_supported));
            sKeys.put("layout/activity_customer_service_0", Integer.valueOf(R.layout.activity_customer_service));
            sKeys.put("layout/activity_feedback_history_0", Integer.valueOf(R.layout.activity_feedback_history));
            sKeys.put("layout/activity_feedback_problem_0", Integer.valueOf(R.layout.activity_feedback_problem));
            sKeys.put("layout/activity_feedback_record_0", Integer.valueOf(R.layout.activity_feedback_record));
            sKeys.put("layout/activity_gain_service_0", Integer.valueOf(R.layout.activity_gain_service));
            sKeys.put("layout/activity_gunball_setting_0", Integer.valueOf(R.layout.activity_gunball_setting));
            sKeys.put("layout/activity_ipcamera_double_eye_layout_0", Integer.valueOf(R.layout.activity_ipcamera_double_eye_layout));
            sKeys.put("layout/activity_ipcamera_four_eyes_0", Integer.valueOf(R.layout.activity_ipcamera_four_eyes));
            sKeys.put("layout/activity_ipcamera_layout_0", Integer.valueOf(R.layout.activity_ipcamera_layout));
            sKeys.put("layout/activity_ipcamera_three_eyes_0", Integer.valueOf(R.layout.activity_ipcamera_three_eyes));
            sKeys.put("layout/activity_ipcamera_three_false_eyes_0", Integer.valueOf(R.layout.activity_ipcamera_three_false_eyes));
            sKeys.put("layout/activity_low_power_diy_settings_0", Integer.valueOf(R.layout.activity_low_power_diy_settings));
            sKeys.put("layout/activity_low_power_settings_0", Integer.valueOf(R.layout.activity_low_power_settings));
            sKeys.put("layout/activity_motion_detect_0", Integer.valueOf(R.layout.activity_motion_detect));
            sKeys.put("layout/activity_nano_sim_tips2_0", Integer.valueOf(R.layout.activity_nano_sim_tips2));
            sKeys.put("layout/activity_nano_sim_tips3_0", Integer.valueOf(R.layout.activity_nano_sim_tips3));
            sKeys.put("layout/activity_nano_sim_tips4_0", Integer.valueOf(R.layout.activity_nano_sim_tips4));
            sKeys.put("layout/activity_net_4g_switch_0", Integer.valueOf(R.layout.activity_net_4g_switch));
            sKeys.put("layout/activity_net_share_0", Integer.valueOf(R.layout.activity_net_share));
            sKeys.put("layout/activity_net_wlan_setting_0", Integer.valueOf(R.layout.activity_net_wlan_setting));
            sKeys.put("layout/activity_night_lighting_settings_0", Integer.valueOf(R.layout.activity_night_lighting_settings));
            sKeys.put("layout/activity_nvr_yun_history_0", Integer.valueOf(R.layout.activity_nvr_yun_history));
            sKeys.put("layout/activity_nvr_yun_select_0", Integer.valueOf(R.layout.activity_nvr_yun_select));
            sKeys.put("layout/activity_other_sim_0", Integer.valueOf(R.layout.activity_other_sim));
            sKeys.put("layout/activity_power_mode_settings_0", Integer.valueOf(R.layout.activity_power_mode_settings));
            sKeys.put("layout/activity_power_on_0", Integer.valueOf(R.layout.activity_power_on));
            sKeys.put("layout/activity_push_help_0", Integer.valueOf(R.layout.activity_push_help));
            sKeys.put("layout/activity_push_huawei_0", Integer.valueOf(R.layout.activity_push_huawei));
            sKeys.put("layout/activity_push_mi_0", Integer.valueOf(R.layout.activity_push_mi));
            sKeys.put("layout/activity_push_oppo_0", Integer.valueOf(R.layout.activity_push_oppo));
            sKeys.put("layout/activity_push_vivo_0", Integer.valueOf(R.layout.activity_push_vivo));
            sKeys.put("layout/activity_reset_0", Integer.valueOf(R.layout.activity_reset));
            sKeys.put("layout/activity_router_connected_device_0", Integer.valueOf(R.layout.activity_router_connected_device));
            sKeys.put("layout/activity_router_hotspot_0", Integer.valueOf(R.layout.activity_router_hotspot));
            sKeys.put("layout/activity_router_lan_0", Integer.valueOf(R.layout.activity_router_lan));
            sKeys.put("layout/activity_router_other_sim_0", Integer.valueOf(R.layout.activity_router_other_sim));
            sKeys.put("layout/activity_search_0", Integer.valueOf(R.layout.activity_search));
            sKeys.put("layout/activity_select_sim_0", Integer.valueOf(R.layout.activity_select_sim));
            sKeys.put("layout/activity_sim_input_0", Integer.valueOf(R.layout.activity_sim_input));
            sKeys.put("layout/activity_three_yun_select_0", Integer.valueOf(R.layout.activity_three_yun_select));
            sKeys.put("layout/activity_yun_history_details_0", Integer.valueOf(R.layout.activity_yun_history_details));
            sKeys.put("layout/auto_door_layout_0", Integer.valueOf(R.layout.auto_door_layout));
            sKeys.put("layout/autodoor_recyclerview_item_0", Integer.valueOf(R.layout.autodoor_recyclerview_item));
            sKeys.put("layout/cloud_download_recyclerview_item_0", Integer.valueOf(R.layout.cloud_download_recyclerview_item));
            sKeys.put("layout/dialog_confirm_0", Integer.valueOf(R.layout.dialog_confirm));
            sKeys.put("layout/dialog_score_0", Integer.valueOf(R.layout.dialog_score));
            sKeys.put("layout/dialog_success_0", Integer.valueOf(R.layout.dialog_success));
            sKeys.put("layout/dialog_title_confirm_0", Integer.valueOf(R.layout.dialog_title_confirm));
            sKeys.put("layout/dialog_wechat_0", Integer.valueOf(R.layout.dialog_wechat));
            sKeys.put("layout/item_add_device_0", Integer.valueOf(R.layout.item_add_device));
            sKeys.put("layout/item_ble_0", Integer.valueOf(R.layout.item_ble));
            sKeys.put("layout/item_cloud_img_0", Integer.valueOf(R.layout.item_cloud_img));
            sKeys.put("layout/item_connect_device_0", Integer.valueOf(R.layout.item_connect_device));
            sKeys.put("layout/item_dialog_list_0", Integer.valueOf(R.layout.item_dialog_list));
            sKeys.put("layout/item_face_0", Integer.valueOf(R.layout.item_face));
            sKeys.put("layout/item_feedback_0", Integer.valueOf(R.layout.item_feedback));
            sKeys.put("layout/item_feedback_reply_0", Integer.valueOf(R.layout.item_feedback_reply));
            sKeys.put("layout/item_four_live_0", Integer.valueOf(R.layout.item_four_live));
            sKeys.put("layout/item_live_0", Integer.valueOf(R.layout.item_live));
            sKeys.put("layout/item_pay_0", Integer.valueOf(R.layout.item_pay));
            sKeys.put("layout/item_search_0", Integer.valueOf(R.layout.item_search));
            sKeys.put("layout/item_share_history_0", Integer.valueOf(R.layout.item_share_history));
            sKeys.put("layout/recycler_item_activity_plan_0", Integer.valueOf(R.layout.recycler_item_activity_plan));
            sKeys.put("layout/start_activity_0", Integer.valueOf(R.layout.start_activity));
            sKeys.put("layout/tts_vigilance_activity_layout_0", Integer.valueOf(R.layout.tts_vigilance_activity_layout));
            sKeys.put("layout/vigilance_activity_layout_0", Integer.valueOf(R.layout.vigilance_activity_layout));
            sKeys.put("layout/vigilance_recyclerview_item_0", Integer.valueOf(R.layout.vigilance_recyclerview_item));
        }
    }
}
