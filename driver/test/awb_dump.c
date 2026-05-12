/*
 * AWB Calibration Dump Tool
 *
 * Queries the running ISP (pipe 0, owned by superb) and dumps all AWB
 * calibration data in a format suitable for copy-paste into sc635hai_cmos.c.
 *
 * This tool does NOT initialize the ISP -- it attaches to the already-running
 * ISP and reads state. All calls are read-only and won't disturb superb.
 *
 * Usage: ./awb_dump
 *
 * Build: make awb_dump
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

/* HiSilicon SDK headers */
#include "ot_type.h"
#include "ot_common.h"
#include "ot_common_isp.h"
#include "ss_mpi_isp.h"
#include "ss_mpi_awb.h"

#define PIPE 0

static void dump_wb_attr(void)
{
    ot_isp_wb_attr wb_attr;
    td_s32 ret;

    memset(&wb_attr, 0, sizeof(wb_attr));
    ret = ss_mpi_isp_get_wb_attr(PIPE, &wb_attr);
    if (ret != TD_SUCCESS) {
        printf("[wb_attr] FAILED: 0x%08X\n", ret);
        return;
    }

    printf("=== WB ATTR (ss_mpi_isp_get_wb_attr) ===\n");
    printf("bypass:           %d\n", wb_attr.bypass);
    printf("awb_run_interval: %d\n", wb_attr.awb_run_interval);
    printf("op_type:          %d (0=AUTO, 1=MANUAL)\n", wb_attr.op_type);
    printf("alg_type:         %d (0=GW, 1=SPEC)\n", wb_attr.alg_type);

    /* Manual gains */
    printf("\n--- Manual WB gains ---\n");
    printf("r_gain:  %u (0x%04X)\n", wb_attr.manual_attr.r_gain, wb_attr.manual_attr.r_gain);
    printf("gr_gain: %u (0x%04X)\n", wb_attr.manual_attr.gr_gain, wb_attr.manual_attr.gr_gain);
    printf("gb_gain: %u (0x%04X)\n", wb_attr.manual_attr.gb_gain, wb_attr.manual_attr.gb_gain);
    printf("b_gain:  %u (0x%04X)\n", wb_attr.manual_attr.b_gain, wb_attr.manual_attr.b_gain);

    /* Auto params */
    printf("\n--- Auto AWB params ---\n");
    printf("enable:           %d\n", wb_attr.auto_attr.enable);
    printf("ref_color_temp:   %u\n", wb_attr.auto_attr.ref_color_temp);
    printf("static_wb[]:      { %u, %u, %u, %u }\n",
        wb_attr.auto_attr.static_wb[0], wb_attr.auto_attr.static_wb[1],
        wb_attr.auto_attr.static_wb[2], wb_attr.auto_attr.static_wb[3]);
    printf("curve_para[]:     { %d, %d, %d, %d, %d, %d }\n",
        wb_attr.auto_attr.curve_para[0], wb_attr.auto_attr.curve_para[1],
        wb_attr.auto_attr.curve_para[2], wb_attr.auto_attr.curve_para[3],
        wb_attr.auto_attr.curve_para[4], wb_attr.auto_attr.curve_para[5]);
    printf("alg_type:         %d (0=LOWCOST, 1=ADVANCE, 2=NATURA)\n",
        wb_attr.auto_attr.alg_type);
    printf("rg_strength:      %u\n", wb_attr.auto_attr.rg_strength);
    printf("bg_strength:      %u\n", wb_attr.auto_attr.bg_strength);
    printf("speed:            %u\n", wb_attr.auto_attr.speed);
    printf("zone_sel:         %u\n", wb_attr.auto_attr.zone_sel);
    printf("high_color_temp:  %u\n", wb_attr.auto_attr.high_color_temp);
    printf("low_color_temp:   %u\n", wb_attr.auto_attr.low_color_temp);
    printf("shift_limit_en:   %d\n", wb_attr.auto_attr.shift_limit_en);
    printf("shift_limit:      %u\n", wb_attr.auto_attr.shift_limit);
    printf("gain_norm_en:     %d\n", wb_attr.auto_attr.gain_norm_en);
    printf("natural_cast_en:  %d\n", wb_attr.auto_attr.natural_cast_en);
    printf("awb_zone_wt_en:   %d\n", wb_attr.auto_attr.awb_zone_wt_en);

    /* CT limit */
    printf("\n--- CT gain limits ---\n");
    printf("ct_limit_en:      %d\n", wb_attr.auto_attr.ct_limit.enable);
    printf("ct_limit_op:      %d (0=AUTO, 1=MANUAL)\n", wb_attr.auto_attr.ct_limit.op_type);
    printf("high_rg_limit:    %u (0x%04X)\n", wb_attr.auto_attr.ct_limit.high_rg_limit, wb_attr.auto_attr.ct_limit.high_rg_limit);
    printf("high_bg_limit:    %u (0x%04X)\n", wb_attr.auto_attr.ct_limit.high_bg_limit, wb_attr.auto_attr.ct_limit.high_bg_limit);
    printf("low_rg_limit:     %u (0x%04X)\n", wb_attr.auto_attr.ct_limit.low_rg_limit, wb_attr.auto_attr.ct_limit.low_rg_limit);
    printf("low_bg_limit:     %u (0x%04X)\n", wb_attr.auto_attr.ct_limit.low_bg_limit, wb_attr.auto_attr.ct_limit.low_bg_limit);

    /* Cb/Cr tracking */
    printf("\n--- CbCr tracking ---\n");
    printf("cb_cr_track_en:   %d\n", wb_attr.auto_attr.cb_cr_track.enable);
    int i;
    printf("cr_max[16]:       {");
    for (i = 0; i < 16; i++) printf(" %u", wb_attr.auto_attr.cb_cr_track.cr_max[i]);
    printf(" }\n");
    printf("cr_min[16]:       {");
    for (i = 0; i < 16; i++) printf(" %u", wb_attr.auto_attr.cb_cr_track.cr_min[i]);
    printf(" }\n");
    printf("cb_max[16]:       {");
    for (i = 0; i < 16; i++) printf(" %u", wb_attr.auto_attr.cb_cr_track.cb_max[i]);
    printf(" }\n");
    printf("cb_min[16]:       {");
    for (i = 0; i < 16; i++) printf(" %u", wb_attr.auto_attr.cb_cr_track.cb_min[i]);
    printf(" }\n");

    /* Luma histogram */
    printf("\n--- Luma histogram ---\n");
    printf("luma_hist_en:     %d\n", wb_attr.auto_attr.luma_hist.enable);
    printf("luma_hist_op:     %d\n", wb_attr.auto_attr.luma_hist.op_type);
    printf("luma_hist_thresh: {");
    for (i = 0; i < 6; i++) printf(" %u", wb_attr.auto_attr.luma_hist.hist_thresh[i]);
    printf(" }\n");
    printf("luma_hist_wt[]:   {");
    for (i = 0; i < 6; i++) printf(" %u", wb_attr.auto_attr.luma_hist.hist_wt[i]);
    printf(" }\n");

    printf("\n");
}

static void dump_wb_info(void)
{
    ot_isp_wb_info wb_info;
    td_s32 ret;

    memset(&wb_info, 0, sizeof(wb_info));
    ret = ss_mpi_isp_query_wb_info(PIPE, &wb_info);
    if (ret != TD_SUCCESS) {
        printf("[wb_info] FAILED: 0x%08X\n", ret);
        return;
    }

    printf("=== WB INFO (ss_mpi_isp_query_wb_info) -- runtime state ===\n");
    printf("r_gain:       %u (0x%04X)\n", wb_info.r_gain, wb_info.r_gain);
    printf("gr_gain:      %u (0x%04X)\n", wb_info.gr_gain, wb_info.gr_gain);
    printf("gb_gain:      %u (0x%04X)\n", wb_info.gb_gain, wb_info.gb_gain);
    printf("b_gain:       %u (0x%04X)\n", wb_info.b_gain, wb_info.b_gain);
    printf("saturation:   %u\n", wb_info.saturation);
    printf("color_temp:   %u K\n", wb_info.color_temp);
    printf("ccm[9]:       { 0x%04X, 0x%04X, 0x%04X,\n", wb_info.ccm[0], wb_info.ccm[1], wb_info.ccm[2]);
    printf("                0x%04X, 0x%04X, 0x%04X,\n", wb_info.ccm[3], wb_info.ccm[4], wb_info.ccm[5]);
    printf("                0x%04X, 0x%04X, 0x%04X }\n", wb_info.ccm[6], wb_info.ccm[7], wb_info.ccm[8]);
    printf("ls0_ct:       %u K (primary)\n", wb_info.ls0_ct);
    printf("ls1_ct:       %u K (secondary)\n", wb_info.ls1_ct);
    printf("ls0_area:     %u\n", wb_info.ls0_area);
    printf("ls1_area:     %u\n", wb_info.ls1_area);
    printf("multi_degree: %u\n", wb_info.multi_degree);
    printf("active_shift: %u\n", wb_info.active_shift);
    printf("first_stable: %u\n", wb_info.first_stable_time);
    printf("scene_status: %d (0=INDOOR, 1=OUTDOOR, 2=TRANSITION)\n", wb_info.scene_status);
    printf("bv:           %d\n", wb_info.bv);
    printf("\n");
}

static void dump_ccm_attr(void)
{
    ot_isp_color_matrix_attr ccm_attr;
    td_s32 ret;
    int i, j;

    memset(&ccm_attr, 0, sizeof(ccm_attr));
    ret = ss_mpi_isp_get_ccm_attr(PIPE, &ccm_attr);
    if (ret != TD_SUCCESS) {
        printf("[ccm_attr] FAILED: 0x%08X\n", ret);
        return;
    }

    printf("=== CCM ATTR (ss_mpi_isp_get_ccm_attr) ===\n");
    printf("op_type:          %d (0=AUTO, 1=MANUAL)\n", ccm_attr.op_type);

    /* Manual */
    printf("\n--- Manual CCM ---\n");
    printf("sat_en:           %d\n", ccm_attr.manual_attr.sat_en);
    printf("ccm[9]:           { 0x%04X, 0x%04X, 0x%04X,\n",
        ccm_attr.manual_attr.ccm[0], ccm_attr.manual_attr.ccm[1], ccm_attr.manual_attr.ccm[2]);
    printf("                    0x%04X, 0x%04X, 0x%04X,\n",
        ccm_attr.manual_attr.ccm[3], ccm_attr.manual_attr.ccm[4], ccm_attr.manual_attr.ccm[5]);
    printf("                    0x%04X, 0x%04X, 0x%04X }\n",
        ccm_attr.manual_attr.ccm[6], ccm_attr.manual_attr.ccm[7], ccm_attr.manual_attr.ccm[8]);

    /* Auto CCM tables */
    printf("\n--- Auto CCM tables ---\n");
    printf("iso_act_en:       %d\n", ccm_attr.auto_attr.iso_act_en);
    printf("temp_act_en:      %d\n", ccm_attr.auto_attr.temp_act_en);
    printf("ccm_tab_num:      %u\n", ccm_attr.auto_attr.ccm_tab_num);

    for (i = 0; i < (int)ccm_attr.auto_attr.ccm_tab_num && i < 7; i++) {
        printf("\n  [%d] color_temp = %u K\n", i, ccm_attr.auto_attr.ccm_tab[i].color_temp);
        printf("      ccm = {");
        for (j = 0; j < 9; j++) {
            if (j == 3 || j == 6) printf("\n             ");
            printf(" 0x%04X", ccm_attr.auto_attr.ccm_tab[i].ccm[j]);
            if (j < 8) printf(",");
        }
        printf(" }\n");

        /* Decode the matrix to human-readable floats */
        printf("      decoded = {");
        for (j = 0; j < 9; j++) {
            td_u16 v = ccm_attr.auto_attr.ccm_tab[i].ccm[j];
            float fval;
            if (v & 0x8000) {
                fval = -((float)(v & 0x7FFF)) / 256.0f;
            } else {
                fval = ((float)v) / 256.0f;
            }
            if (j == 3 || j == 6) printf("\n                 ");
            printf(" %+6.3f", fval);
            if (j < 8) printf(",");
        }
        printf(" }\n");
    }
    printf("\n");
}

static void dump_awb_attr_ex(void)
{
    ot_isp_awb_attr_ex attr_ex;
    td_s32 ret;
    int i;

    memset(&attr_ex, 0, sizeof(attr_ex));
    ret = ss_mpi_isp_get_awb_attr_ex(PIPE, &attr_ex);
    if (ret != TD_SUCCESS) {
        printf("[awb_attr_ex] FAILED: 0x%08X\n", ret);
        return;
    }

    printf("=== AWB ATTR EX (ss_mpi_isp_get_awb_attr_ex) ===\n");
    printf("tolerance:        %u\n", attr_ex.tolerance);
    printf("zone_radius:      %u\n", attr_ex.zone_radius);
    printf("curve_l_limit:    %u\n", attr_ex.curve_l_limit);
    printf("curve_r_limit:    %u\n", attr_ex.curve_r_limit);
    printf("extra_light_en:   %d\n", attr_ex.extra_light_en);

    for (i = 0; i < 4; i++) {
        printf("  light[%d]: white_r_gain=%u, white_b_gain=%u, exp_quant=%u, light_status=%u, radius=%u\n",
            i,
            attr_ex.light_info[i].white_r_gain,
            attr_ex.light_info[i].white_b_gain,
            attr_ex.light_info[i].exp_quant,
            attr_ex.light_info[i].light_status,
            attr_ex.light_info[i].radius);
    }

    printf("multi_light_src_en: %d\n", attr_ex.multi_light_source_en);
    printf("multi_ls_type:      %d\n", attr_ex.multi_ls_type);
    printf("multi_ls_scaler:    %u\n", attr_ex.multi_ls_scaler);
    printf("multi_ct_bin[8]:    {");
    for (i = 0; i < 8; i++) printf(" %u", attr_ex.multi_ct_bin[i]);
    printf(" }\n");
    printf("multi_ct_wt[8]:     {");
    for (i = 0; i < 8; i++) printf(" %u", attr_ex.multi_ct_wt[i]);
    printf(" }\n");
    printf("fine_tun_en:        %d\n", attr_ex.fine_tun_en);
    printf("fine_tun_strength:  %u\n", attr_ex.fine_tun_strength);
    printf("\n");
}

static void dump_saturation_attr(void)
{
    ot_isp_saturation_attr sat_attr;
    td_s32 ret;
    int i;

    memset(&sat_attr, 0, sizeof(sat_attr));
    ret = ss_mpi_isp_get_saturation_attr(PIPE, &sat_attr);
    if (ret != TD_SUCCESS) {
        printf("[saturation] FAILED: 0x%08X\n", ret);
        return;
    }

    printf("=== SATURATION ATTR (ss_mpi_isp_get_saturation_attr) ===\n");
    printf("op_type:          %d (0=AUTO, 1=MANUAL)\n", sat_attr.op_type);
    printf("manual_sat:       %u\n", sat_attr.manual_attr.saturation);
    printf("auto_sat[16]:     {");
    for (i = 0; i < 16; i++) printf(" %u", sat_attr.auto_attr.sat[i]);
    printf(" }\n\n");
}

static void dump_color_tone_attr(void)
{
    ot_isp_color_tone_attr ct_attr;
    td_s32 ret;

    memset(&ct_attr, 0, sizeof(ct_attr));
    ret = ss_mpi_isp_get_color_tone_attr(PIPE, &ct_attr);
    if (ret != TD_SUCCESS) {
        printf("[color_tone] FAILED: 0x%08X\n", ret);
        return;
    }

    printf("=== COLOR TONE ATTR (ss_mpi_isp_get_color_tone_attr) ===\n");
    printf("red_cast_gain:    %u (0x%04X)\n", ct_attr.red_cast_gain, ct_attr.red_cast_gain);
    printf("green_cast_gain:  %u (0x%04X)\n", ct_attr.green_cast_gain, ct_attr.green_cast_gain);
    printf("blue_cast_gain:   %u (0x%04X)\n", ct_attr.blue_cast_gain, ct_attr.blue_cast_gain);
    printf("\n");
}

static void dump_wb_stats_cfg(void)
{
    ot_isp_stats_cfg stats_cfg;
    td_s32 ret;

    memset(&stats_cfg, 0, sizeof(stats_cfg));
    ret = ss_mpi_isp_get_stats_cfg(PIPE, &stats_cfg);
    if (ret != TD_SUCCESS) {
        printf("[stats_cfg] FAILED: 0x%08X\n", ret);
        return;
    }

    ot_isp_wb_stats_cfg *wb = &stats_cfg.wb_cfg;
    printf("=== WB STATS CONFIG (from ss_mpi_isp_get_stats_cfg) ===\n");
    printf("awb_switch:       %d (0=AFTER_DG, 1=AFTER_EXPANDER, 2=AFTER_DRC)\n", wb->awb_switch);
    printf("zone_row:         %u\n", wb->zone_row);
    printf("zone_col:         %u\n", wb->zone_col);
    printf("white_level:      %u\n", wb->white_level);
    printf("black_level:      %u\n", wb->black_level);
    printf("cb_max:           %u\n", wb->cb_max);
    printf("cb_min:           %u\n", wb->cb_min);
    printf("cr_max:           %u\n", wb->cr_max);
    printf("cr_min:           %u\n", wb->cr_min);
    printf("crop enable:      %d\n", wb->crop.enable);
    if (wb->crop.enable) {
        printf("crop x,y,w,h:     %u,%u,%u,%u\n",
            wb->crop.x, wb->crop.y,
            wb->crop.width, wb->crop.height);
    }
    printf("\n");
}

static void dump_pub_attr(void)
{
    ot_isp_pub_attr pub_attr;
    td_s32 ret;

    memset(&pub_attr, 0, sizeof(pub_attr));
    ret = ss_mpi_isp_get_pub_attr(PIPE, &pub_attr);
    if (ret != TD_SUCCESS) {
        printf("[pub_attr] FAILED: 0x%08X\n", ret);
        return;
    }

    printf("=== PUB ATTR (ss_mpi_isp_get_pub_attr) ===\n");
    printf("bayer_format:     %d (0=RGGB, 1=GRBG, 2=GBRG, 3=BGGR)\n", pub_attr.bayer_format);
    printf("wdr_mode:         %d\n", pub_attr.wdr_mode);
    printf("sns_mode:         %d\n", pub_attr.sns_mode);
    printf("frame_rate:       %f\n", pub_attr.frame_rate);
    printf("\n");
}

int main(void)
{
    printf("AWB Calibration Dump -- Querying superb's running ISP (pipe %d)\n", PIPE);
    printf("================================================================\n\n");

    dump_pub_attr();
    dump_wb_info();
    dump_wb_attr();
    dump_ccm_attr();
    dump_awb_attr_ex();
    dump_saturation_attr();
    dump_color_tone_attr();
    dump_wb_stats_cfg();

    printf("================================================================\n");
    printf("Done. Copy relevant values into sc635hai_cmos.c\n");

    return 0;
}
