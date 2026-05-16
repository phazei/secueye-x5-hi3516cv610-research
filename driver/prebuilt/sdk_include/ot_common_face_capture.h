/*
  Copyright (c), 2024-2024, Shenshu Tech. Co., Ltd.
 */

#ifndef OT_COMMON_FACE_CAPTURE_H
#define OT_COMMON_FACE_CAPTURE_H

#include "ot_type.h"
#include "ot_common_aidetect.h"

#define OT_FACE_CAPTURE_MAX_OUTPUT_NUM 10     /* RW; max output face quality result object num */

typedef struct {
    td_bool face_quality_en; /* RW; enable or disable face quality, default: enable */
    td_bool face_pose_en; /* RW; enable or disable face pose, default: enable */
    td_bool face_mask_en; /* RW; enable or disable face mask, default: enable */
} ot_face_capture_attr;

typedef struct {
    td_s16 yaw;
    td_s16 roll;
    td_s16 pitch;
} ot_face_pose;

typedef struct {
    td_u16 min_face_quality; /* RW; face quality threshold, Range:[0, 100], 0 means no filter */
    ot_face_pose max_face_pose; /* RW; max face pose, Range:[0, 180], 0 means no filter */
    td_u16 min_pupil_pixel_dist; /* RW; min pupil distance, 0 means no filter */
} ot_face_capture_param;

typedef struct {
    ot_aidetect_object detect_object;
    td_u16 quality;
    ot_face_pose pose;
    td_bool mask;
} ot_face_quality_result;

typedef struct {
    td_u32 object_num;
    ot_face_quality_result face_quality_result[OT_FACE_CAPTURE_MAX_OUTPUT_NUM];
} ot_face_capture_result_array;

#endif