#include <stdio.h>
#include <stddef.h>
#include "hi_common_vi.h"
#include "hi_common_isp.h"
#include "hi_common_vpss.h"

int main(void)
{
    printf("=== ot_vi_dev_attr offsets (B040 headers) ===\n");
    printf("sizeof(ot_vi_dev_attr) = %zu\n", sizeof(ot_vi_dev_attr));
    printf("  intf_mode      @ %zu\n", offsetof(ot_vi_dev_attr, intf_mode));
    printf("  work_mode      @ %zu\n", offsetof(ot_vi_dev_attr, work_mode));
    printf("  component_mask @ %zu\n", offsetof(ot_vi_dev_attr, component_mask));
    printf("  scan_mode      @ %zu\n", offsetof(ot_vi_dev_attr, scan_mode));
    printf("  ad_chn_id      @ %zu\n", offsetof(ot_vi_dev_attr, ad_chn_id));
    printf("  data_seq       @ %zu\n", offsetof(ot_vi_dev_attr, data_seq));
    printf("  sync_cfg       @ %zu  (size=%zu)\n", offsetof(ot_vi_dev_attr, sync_cfg), sizeof(ot_vi_sync_cfg));
    printf("  data_type      @ %zu\n", offsetof(ot_vi_dev_attr, data_type));
    printf("  data_reverse   @ %zu\n", offsetof(ot_vi_dev_attr, data_reverse));
    printf("  in_size        @ %zu\n", offsetof(ot_vi_dev_attr, in_size));
    printf("  data_rate      @ %zu\n", offsetof(ot_vi_dev_attr, data_rate));

    printf("\n=== ot_vpss_chn_attr offsets ===\n");
    printf("sizeof(ot_vpss_chn_attr) = %zu\n", sizeof(ot_vpss_chn_attr));
    printf("  width          @ %zu\n", offsetof(ot_vpss_chn_attr, width));
    printf("  height         @ %zu\n", offsetof(ot_vpss_chn_attr, height));
    printf("  chn_mode       @ %zu\n", offsetof(ot_vpss_chn_attr, chn_mode));
    printf("  pixel_format   @ %zu\n", offsetof(ot_vpss_chn_attr, pixel_format));
    printf("  dynamic_range  @ %zu\n", offsetof(ot_vpss_chn_attr, dynamic_range));
    printf("  video_format   @ %zu\n", offsetof(ot_vpss_chn_attr, video_format));
    printf("  compress_mode  @ %zu\n", offsetof(ot_vpss_chn_attr, compress_mode));
    printf("  depth          @ %zu\n", offsetof(ot_vpss_chn_attr, depth));
    printf("  mirror_en      @ %zu\n", offsetof(ot_vpss_chn_attr, mirror_en));
    printf("  flip_en        @ %zu\n", offsetof(ot_vpss_chn_attr, flip_en));
    printf("  frame_rate     @ %zu\n", offsetof(ot_vpss_chn_attr, frame_rate));

    printf("\n=== ot_vpss_grp_attr offsets ===\n");
    printf("sizeof(ot_vpss_grp_attr) = %zu\n", sizeof(ot_vpss_grp_attr));
    printf("  max_width      @ %zu\n", offsetof(ot_vpss_grp_attr, max_width));
    printf("  max_height     @ %zu\n", offsetof(ot_vpss_grp_attr, max_height));
    printf("  pixel_format   @ %zu\n", offsetof(ot_vpss_grp_attr, pixel_format));
    printf("  dynamic_range  @ %zu\n", offsetof(ot_vpss_grp_attr, dynamic_range));
    printf("  dei_mode       @ %zu\n", offsetof(ot_vpss_grp_attr, dei_mode));
    printf("  frame_rate     @ %zu\n", offsetof(ot_vpss_grp_attr, frame_rate));
    return 0;
}
