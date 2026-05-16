/*
  Copyright (c), 2024-2024, Shenshu Tech. Co., Ltd.
 */

#ifndef OT_COMMON_AIVSR_H
#define OT_COMMON_AIVSR_H

#include "ot_common_vlpr.h"
#include "ot_common_face_capture.h"
#include "ot_common_fr.h"

#define OT_AIVSR_MAX_CHN_NUM 8          /* RW; max input channel, Range:[0, 7] */

typedef td_s32 ot_aivsr_chn;

typedef enum {
    OT_AIVSR_MODEL_LOAD_FROM_MEMORY = 0x0,
    OT_AIVSR_MODEL_LOAD_FROM_PATH,

    OT_AIVSR_MODEL_LOAD_BUTT /* RW; other, not be used */
} ot_aivsr_model_load_mode;

typedef struct {
    ot_aivsr_model_load_mode model_load_mode; /* RW; model content from path or memory */
    td_u32 size; /*  RW; model size,if input model memory, must be model content size not empty */
    td_void *model;
} ot_aivsr_input_model;

typedef enum {
    OT_AIVSR_TASK_FACE_CAPTURE,
    OT_AIVSR_TASK_VLPR,
    OT_AIVSR_TASK_FR,
    OT_AIVSR_TASK_BUTT
} ot_aivsr_task_type;

typedef struct {
    ot_aivsr_task_type task_type;
    td_u32 depth;                   /* RW; queue length, Range:[0, 8], default:1, 0 means using default value */
    union {
        ot_vlpr_attr vlpr_attr;
        ot_face_capture_attr face_attr;
    };
} ot_aivsr_chn_attr;

typedef struct {
    td_bool preemp_en; /* RW; enable or disable preemp ,default enable */
    td_u32 priority;   /* RW; the model execute priority,Range:[0,7],the default is 3.The smaller the number, the higher
                          the priority */
    td_u32 priority_up_step_timeout; /* RW; the model execute priority up step timeout, Range:0 or [15, 600000) */
    td_u32 priority_up_top_timeout;  /* RW; the model execute priority up to top timeout,Range:0 or [15, 600000) */
} ot_aivsr_model_priority;

typedef struct {
    ot_aivsr_task_type task_type;
    ot_aivsr_model_priority model_priority;
    union {
        ot_vlpr_param vlpr_param;
        ot_face_capture_param face_param;
    };
} ot_aivsr_chn_param;

typedef struct {
    ot_aivsr_task_type task_type;
    td_u64 frame_pts;
    union {
        ot_vlpr_result_array vlpr_result;
        ot_face_capture_result_array face_result;
        ot_fr_result_array fr_result;
    };
} ot_aivsr_result_array;

typedef void (*ot_aivsr_result_callback)(td_s32 retcode, const ot_aivsr_result_array *result);

#define OT_ERR_AIVSR_NULL_PTR   OT_DEFINE_ERR(OT_ID_AIVSR, OT_ERR_LEVEL_ERROR, OT_ERR_NULL_PTR)
#define OT_ERR_AIVSR_NOT_READY OT_DEFINE_ERR(OT_ID_AIVSR, OT_ERR_LEVEL_ERROR, OT_ERR_NOT_READY)
#define OT_ERR_AIVSR_ILLEGAL_PARAM \
    OT_DEFINE_ERR(OT_ID_AIVSR, OT_ERR_LEVEL_ERROR, OT_ERR_ILLEGAL_PARAM)
#define OT_ERR_AIVSR_EXIST \
    OT_DEFINE_ERR(OT_ID_AIVSR, OT_ERR_LEVEL_ERROR, OT_ERR_EXIST)
#define OT_ERR_AIVSR_UNEXIST \
    OT_DEFINE_ERR(OT_ID_AIVSR, OT_ERR_LEVEL_ERROR, OT_ERR_UNEXIST)
#define OT_ERR_AIVSR_NOT_PERM OT_DEFINE_ERR(OT_ID_AIVSR, OT_ERR_LEVEL_ERROR, OT_ERR_NOT_PERM)
#define OT_ERR_AIVSR_BUF_FULL OT_DEFINE_ERR(OT_ID_AIVSR, OT_ERR_LEVEL_ERROR, OT_ERR_BUF_FULL)

#endif