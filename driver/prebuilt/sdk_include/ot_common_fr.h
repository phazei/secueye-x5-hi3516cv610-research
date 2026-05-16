/*
 * Copyright (c) HiSilicon (Shanghai) Technologies Co., Ltd. 2024-2024. All rights reserved.
 * Description: ot_common_fr
 * Author: HiMobileCam middleware develop team
 * Create: 2024-11-18
 */

#ifndef OT_COMMON_FR_H
#define OT_COMMON_FR_H

#include "ot_type.h"
#include "ot_common_aidetect.h"

#define OT_FR_MAX_OUTPUT_NUM 10     /* RW; max output fr result object num */
#define OT_FR_FACE_FEATURE_LEN 512

typedef struct {
    float feature[OT_FR_FACE_FEATURE_LEN];
} ot_fr_face_feature;

typedef struct {
    ot_aidetect_object detect_object;
    ot_fr_face_feature face_feature;
} ot_fr_result;

typedef struct {
    td_u32 object_num;
    td_u32 object_capacity; /* RW; fr result capacity,memory must be setted by user */
    ot_fr_result* fr_info_result; /* RW; fr result addr,memory must be setted by user */
} ot_fr_result_array;

#endif