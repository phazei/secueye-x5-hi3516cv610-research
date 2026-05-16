/*
 * Copyright (c) HiSilicon (Shanghai) Technologies Co., Ltd. 2024-2024. All rights reserved.
 */

#ifndef SS_MPI_AIDETECT_H
#define SS_MPI_AIDETECT_H

#include "ot_common_aidetect.h"

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Prototype    : ss_mpi_aidetect_create_chn
 * Description  : Create detect channel and set channel attr.
 * Parameters   : ot_aidetect_chn   chn            Input  channel,set by user. Range
 * ot_aidetect_input_model   *input_model          Input model info.
 * ot_aidetect_chn_attr       *chn_attr            Chn attr, include tracking enable.
 * Return Value : TD_SUCCESS: Success;Error codes: Failure.
 * Date         : 2024-02-20
 *
 */
td_s32 ss_mpi_aidetect_create_chn(ot_aidetect_chn chn, const ot_aidetect_input_model *input_model,
    const ot_aidetect_chn_attr *chn_attr);

/*
 * Prototype    : ss_mpi_aidetect_destroy_chn
 * Description  : Destroy  channel.
 * Parameters   : ot_aidetect_chn chn  Input  channel, must exist.
 * Return Value : TD_SUCCESS: Success;Error codes: Failure.
 * Date         : 2024-02-20
 *
 */
td_s32 ss_mpi_aidetect_destroy_chn(ot_aidetect_chn chn);

/*
 * Prototype    : ss_mpi_aidetect_set_chn_attr
 * Description  : Set channel attr.
 * Parameters   : ot_aidetect_chn   chn          Input  channel,must  exist.
 * ot_aidetect_chn_attr             *chn_attr    Chn attr.
 * Return Value : TD_SUCCESS: Success;Error codes: Failure.
 * Date         : 2024-02-20
 *
 */
td_s32 ss_mpi_aidetect_set_chn_attr(ot_aidetect_chn chn, const ot_aidetect_chn_attr *chn_attr);

/*
 * Prototype    : ss_mpi_aidetect_get_chn_attr
 * Description  : Get chanel attr.
 * Parameters   : ot_aidetect_chn chn   Input  channel,must  exist.
 * ot_aidetect_chn_attr *chn_attr       Chn attr.
 * Return Value : TD_SUCCESS: Success;Error codes: Failure.
 * Date         : 2024-02-20
 *
 */
td_s32 ss_mpi_aidetect_get_chn_attr(ot_aidetect_chn chn, ot_aidetect_chn_attr *chn_attr);

/*
 * Prototype    : ss_mpi_aidetect_set_chn_param
 * Description  : Set channel param.
 * Parameters   : ot_aidetect_chn   chn          Input  channel,must  exist.
 * ot_aidetect_chn_param            *chn_param    Chn param.
 * Return Value : TD_SUCCESS: Success;Error codes: Failure.
 * Date         : 2024-02-20
 *
 */
td_s32 ss_mpi_aidetect_set_chn_param(ot_aidetect_chn chn, const ot_aidetect_chn_param *chn_param);

/*
 * Prototype    : ss_mpi_aidetect_get_chn_param
 * Description  : Get chanel attr.
 * Parameters   : ot_aidetect_chn chn   Input  channel,must  exist.
 * ot_aidetect_chn_param *chn_attr       Chn param.
 * Return Value : TD_SUCCESS: Success;Error codes: Failure.
 * Date         : 2024-02-20
 *
 */
td_s32 ss_mpi_aidetect_get_chn_param(ot_aidetect_chn chn, ot_aidetect_chn_param *chn_param);

/*
 * Prototype    : ss_mpi_aidetect_get_model_info
 * Description  : Get the resolution of the input frame and model threshs
 * Parameters   : ot_aidetect_chn       chn                 Intput channel ,must   exist.
 * ot_aidetect_model_info               *model_info         The model params.
 * Return Value : TD_SUCCESS: Success;Error codes: Failure.
 * Date         : 2024-02-20
 *
 */
td_s32 ss_mpi_aidetect_get_model_info(ot_aidetect_chn chn, ot_aidetect_model_info *model_info);

/*
 * Prototype    : ss_mpi_aidetect_process
 * Description  : Detect processing, input frame and chn ,then get the detect and track result.
 * Parameters   : ot_aidetect_chn   chn             Input channel must not  exist.
 * ot_video_frame                   *frame          Input source frame data, default nv21.The input data must not be
 * null. ot_aidetect_result_array         *detect_result  Output detect and track result. Return Value : TD_SUCCESS:
 * Success;Error codes: Failure. Date         : 2024-02-20
 *
 */
td_s32 ss_mpi_aidetect_process(ot_aidetect_chn chn, const ot_video_frame *frame,
    ot_aidetect_result_array *detect_result);

/*
 * Prototype    : ss_mpi_aidetect_set_log_level
 * Description  : Set the log level, default level is 0, 0-error 1-warn 2-info.
 * Parameters   : td_u32   log_level  Set log level,range:[0,2].
 * Return Value : TD_SUCCESS: Success;Error codes: Failure.
 * Date         : 2024-02-20
 *
 */
td_s32 ss_mpi_aidetect_set_log_level(td_u32 log_level);

/*
 * Prototype    : ss_mpi_aidetect_get_log_level
 * Description  : Get the log level, default level is 0, 0-error 1-warn 2-info.
 * Parameters   : td_u32   *log_level  Set log level,range:[0,2].
 * Return Value : TD_SUCCESS: Success;Error codes: Failure.
 * Date         : 2024-02-20
 *
 */
td_s32 ss_mpi_aidetect_get_log_level(td_u32 *log_level);

/*
 * Prototype    : ss_mpi_aidetect_query_status
 * Description  : Get the chn status.
 * Parameters   : ot_aidetect_chn_status   *status  total input frames and current framerate.
 * Return Value : TD_SUCCESS: Success;Error codes: Failure.
 * Date         : 2024-02-20
 *
 */
td_s32 ss_mpi_aidetect_query_status(ot_aidetect_chn chn, ot_aidetect_chn_status *status);

#ifdef __cplusplus
}
#endif

#endif
