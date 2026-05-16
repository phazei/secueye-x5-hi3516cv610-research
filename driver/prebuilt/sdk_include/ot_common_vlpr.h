/*
  Copyright (c), 2024-2024, Shenshu Tech. Co., Ltd.
 */

#ifndef OT_COMMON_VLPR_H
#define OT_COMMON_VLPR_H

#include "ot_type.h"
#include "ot_common_video.h"
#include "ot_common_aidetect.h"

#define OT_VLPR_MAX_OUTPUT_NUM 10
#define OT_VLPR_LICENSE_PLATE_MAX_CHAR_NUM 20
#define OT_VLPR_LICENSE_PLATE_MAX_NUM 2

typedef struct {
    td_bool vehicle_property_en;        /* RW; enable or disable vehicle property recognition; default: enable */
    td_bool license_plate_detect_en;    /* RW; enable or disable license plate detection; default: enable */
    td_bool license_plate_property_en;  /* RW; enable or disable license plate property recognition; default: enable,
                                           set disable if license_plate_detect_en is disable */
    td_bool license_plate_recognize_en; /* RW; enbale or disable license plate recognize; default: enable,
                                           set disable if license_plate_detect_en is disable */
} ot_vlpr_attr;

typedef enum {
    OT_LICENSE_PLATE_CATEGORY_LARGE_CAR = 0,
    OT_LICENSE_PLATE_CATEGORY_SMALL_CAR,
    OT_LICENSE_PLATE_CATEGORY_EMBASSY_CAR,
    OT_LICENSE_PLATE_CATEGORY_CONSULATE_CAR,
    OT_LICENSE_PLATE_CATEGORY_TRAILER,
    OT_LICENSE_PLATE_CATEGORY_COACH_CAR,
    OT_LICENSE_PLATE_CATEGORY_POLICE_CAR,
    OT_LICENSE_PLATE_CATEGORY_HONGKONG,
    OT_LICENSE_PLATE_CATEGORY_MACAO,
    OT_LICENSE_PLATE_CATEGORY_ARMED_POLICE,
    OT_LICENSE_PLATE_CATEGORY_PLA,
    OT_LICENSE_PLATE_CATEGORY_NEW_ENERGY,
    OT_LICENSE_PLATE_CATEGORY_BUTT,
} ot_license_plate_category;

typedef enum {
    OT_LICENSE_PLATE_COLOR_BLUE = 0,
    OT_LICENSE_PLATE_COLOR_YELLOW,
    OT_LICENSE_PLATE_COLOR_GREEN,
    OT_LICENSE_PLATE_COLOR_BLACK,
    OT_LICENSE_PLATE_COLOR_WHITE,
    OT_LICENSE_PLATE_COLOR_OTHER,
    OT_LICENSE_PLATE_COLOR_BUTT,
} ot_license_plate_color;

typedef struct {
    ot_license_plate_category category;
    ot_license_plate_color color;
} ot_license_plate_property;

typedef struct {
    ot_rect license_plate_rect;
    td_u32 license_plate_len;
    td_u32 license_plate_code[OT_VLPR_LICENSE_PLATE_MAX_CHAR_NUM]; /* RW; license plate characters, max 20 */
    td_float recognize_confidence;
    ot_license_plate_property property;
} ot_license_plate_info;

typedef enum {
    OT_VEHICLE_CATEGORY_BUS = 0,
    OT_VEHICLE_CATEGORY_SEDAN,
    OT_VEHICLE_CATEGORY_VAN,
    OT_VEHICLE_CATEGORY_SUV,
    OT_VEHICLE_CATEGORY_PICKUP,
    OT_VEHICLE_CATEGORY_TRUCK,
    OT_VEHICLE_CATEGORY_BUTT,
} ot_vehicle_category;

typedef enum {
    OT_VEHICLE_COLOR_BLACK = 0,
    OT_VEHICLE_COLOR_BLUE,
    OT_VEHICLE_COLOR_BROWN,
    OT_VEHICLE_COLOR_GREY,
    OT_VEHICLE_COLOR_YELLOW,
    OT_VEHICLE_COLOR_GREEN,
    OT_VEHICLE_COLOR_PURPLE,
    OT_VEHICLE_COLOR_RED,
    OT_VEHICLE_COLOR_WHITE,
    OT_VEHICLE_COLOR_OTHER,
    OT_VEHICLE_COLOR_BUTT,
} ot_vehicle_color;

typedef enum {
    OT_VEHICLE_ORIENT_FRONT = 0,
    OT_VEHICLE_ORIENT_BACK,
    OT_VEHICLE_ORIENT_SIDE,
    OT_VEHICLE_ORIENT_BUTT,
} ot_vehicle_orient;

typedef struct {
    ot_vehicle_category category;
    ot_vehicle_color color;
    ot_vehicle_orient orient;
} ot_vehicle_property;

typedef struct {
    ot_aidetect_object detect_object;
    ot_vehicle_property property;
} ot_vehicle_info;

typedef struct {
    ot_vehicle_info vehicle_info;
    td_u32 license_plate_num; /* RW; Range:[0, 2] */
    ot_license_plate_info license_plate_info[OT_VLPR_LICENSE_PLATE_MAX_NUM];
} ot_vlpr_result_info;

typedef struct {
    td_u32 object_num;
    ot_vlpr_result_info result_info[OT_VLPR_MAX_OUTPUT_NUM];
} ot_vlpr_result_array;

typedef struct {
    td_u32 vehicle_min_width;
    td_u32 license_plate_min_width;
    td_float license_plate_detect_threshold;    /* RW; Range:[0, 1] */
    td_float license_plate_recognize_threshold; /* RW; Range:[0, 1] */
} ot_vlpr_param;
#endif