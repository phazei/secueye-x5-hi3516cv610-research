/*
 * Copyright (c) HiSilicon (Shanghai) Technologies Co., Ltd. 2024-2024. All rights reserved.
 */

#ifndef OT_COMMON_AIDETECT_H
#define OT_COMMON_AIDETECT_H

#include <unistd.h>
#include <stdlib.h>
#include "ot_type.h"
#include "ot_common_video.h"
#include "ot_errno.h"

#define OT_AIDETECT_MAX_CHN_NUM 8          /* RW; max input channel, Range:[0,7] */

typedef td_s32 ot_aidetect_chn;

typedef struct {
    td_bool preemp_en; /* RW; enable or disable preemp ,default enable */
    td_u32 priority;   /* RW; the model execute priority,Range:[0,7],the default is 3.The smaller the number, the higher
                          the priority */
    td_u32 priority_up_step_timeout; /* RW; the model execute priority up step timeout, Range:0 or [15,600000) */
    td_u32 priority_up_top_timeout;  /* RW; the model execute priority up to top timeout,Range:0 or [15,600000) */
} ot_aidetect_priority;

/* Define track status */
typedef enum {
    OT_AIDETECT_TRACK_STATUS_NEW = 0x0, /* RW; track new */
    OT_AIDETECT_TRACK_STATUS_UPDATE,    /* RW; track updated */
    OT_AIDETECT_TRACK_STATUS_DIE,       /* RW; track died */
    OT_AIDETECT_TRACK_STATUS_VALID,     /* RW; not enable track */
    OT_AIDETECT_TRACK_STATUS_BUTT      /* RW; other, not be used */
} ot_aidetect_track_status;

typedef enum {
    OT_AIDETECT_CLASS_FACE = 0x0,
    OT_AIDETECT_CLASS_HUMAN,
    OT_AIDETECT_CLASS_VEHICLE,
    OT_AIDETECT_CLASS_PET,
    OT_AIDETECT_CLASS_GARBAGE,
    OT_AIDETECT_CLASS_BAG,
    OT_AIDETECT_CLASS_WALLET,
    OT_AIDETECT_CLASS_PHONE,
    OT_AIDETECT_CLASS_HEAD_SHOULDER,
    OT_AIDETECT_CLASS_BICYCLE,
    OT_AIDETECT_CLASS_MOTORCYCLE,
    OT_AIDETECT_CLASS_PACKAGE,
    OT_AIDETECT_CLASS_BUTT
} ot_aidetect_class;

typedef struct {
    ot_size size;
    td_u32 class_num;
    ot_aidetect_class classes[OT_AIDETECT_CLASS_BUTT];
} ot_aidetect_model_info;

typedef struct {
    ot_aidetect_class class_type;
    td_bool track_en; /* RW; enbale or disable tracking, default is disable */
} ot_aidetect_track;

typedef struct {
    td_u32 track_class_num;
    ot_aidetect_track track_class_attr[OT_AIDETECT_CLASS_BUTT];
} ot_aidetect_chn_attr;

typedef struct {
    ot_aidetect_class class_type;
    td_float detect_threshold;
    td_u32 track_miss_frame_num; /* RW; track miss num */
} ot_aidetect_threshold;

typedef struct {
    td_u32 detect_threshold_num;
    ot_aidetect_threshold detect_threshold[OT_AIDETECT_CLASS_BUTT];
    ot_aidetect_priority model_priority;
} ot_aidetect_chn_param;

typedef struct {
    ot_rect detect_rect;
    td_float detect_confidence;
    td_u32 track_id;
    ot_aidetect_track_status track_status;
} ot_aidetect_object;

typedef struct {
    ot_aidetect_class class_type;
    td_u32 object_num;
    td_u32 object_capacity;        /* RW; rect max capacity,memory must be setted by user */
    ot_aidetect_object *objects; /* RW; object addr,memory must be setted by user */
} ot_aidetect_object_of_one_class;

typedef struct {
    td_u32 class_num; /* RW; object class num, which is set by user,Range :[0,OT_AIDETECT_CLASS_BUTT) */
    ot_aidetect_object_of_one_class object_class[OT_AIDETECT_CLASS_BUTT]; /* RW; output one class result */
} ot_aidetect_result_array;

typedef enum {
    OT_AIDETECT_MODEL_LOAD_FROM_MEMORY = 0x0,
    OT_AIDETECT_MODEL_LOAD_FROM_PATH,

    OT_AIDETECT_MODEL_LOAD_BUTT /* RW; other, not be used */
} ot_aidetect_model_load_mode;

typedef struct {
    ot_aidetect_model_load_mode model_load_mode; /* RW; model content from path or memory */
    td_u32 size; /*  RW; model size,if input model memory, must be model content size not empty */
    td_void *model;
} ot_aidetect_input_model;

typedef struct {
    td_u32 frame_rate;  /* RO; current frame rate */
    td_u32 avg_frame_rate;  /* RO; average frame rate */
    td_u32 recv_frames; /* RO; How many frames has been received. */
} ot_aidetect_chn_status;

#define OT_ERR_AIDETECT_NULL_PTR   OT_DEFINE_ERR(OT_ID_AIDETECT, OT_ERR_LEVEL_ERROR, OT_ERR_NULL_PTR)
#define OT_ERR_AIDETECT_NOT_READY OT_DEFINE_ERR(OT_ID_AIDETECT, OT_ERR_LEVEL_ERROR, OT_ERR_NOT_READY)
#define OT_ERR_AIDETECT_ILLEGAL_PARAM \
    OT_DEFINE_ERR(OT_ID_AIDETECT, OT_ERR_LEVEL_ERROR, OT_ERR_ILLEGAL_PARAM)
#define OT_ERR_AIDETECT_EXIST \
    OT_DEFINE_ERR(OT_ID_AIDETECT, OT_ERR_LEVEL_ERROR, OT_ERR_EXIST)
#define OT_ERR_AIDETECT_UNEXIST \
    OT_DEFINE_ERR(OT_ID_AIDETECT, OT_ERR_LEVEL_ERROR, OT_ERR_UNEXIST)
#define OT_ERR_AIDETECT_NOT_PERM OT_DEFINE_ERR(OT_ID_AIDETECT, OT_ERR_LEVEL_ERROR, OT_ERR_NOT_PERM)

#endif
