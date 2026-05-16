# Phase 2 -- NPU + AI Research

Research findings for integrating AI object detection, motion detection,
and alarm logic into our custom daemon. Goal: replicate the stock superb
features (person tracking with bounding boxes, motion alarms, region
intrusion, line crossing) and add motion-triggered recording mode.

---

## Table of Contents

- [Executive Summary](#executive-summary)
- [What Stock superb Does](#what-stock-superb-does)
- [Available AI Paths](#available-ai-paths)
- [Path 1: AIDetect (Recommended for v1)](#path-1-aidetect-recommended-for-v1)
- [Path 2: SVP ACL + YOLOv8](#path-2-svp-acl--yolov8)
- [Path 3: IVE Motion Detection](#path-3-ive-motion-detection)
- [Comparison: AIDetect vs YOLOv8](#comparison-aidetect-vs-yolov8)
- [Recommendation](#recommendation)
- [Implementation Plan](#implementation-plan)
- [Libraries and Models Inventory](#libraries-and-models-inventory)
- [Rule Engine Design (Tripwire, Region, Motion)](#rule-engine-design-tripwire-region-motion)
- [Motion-Triggered Recording](#motion-triggered-recording)
- [Open Questions](#open-questions)

---

## Executive Summary

The Hi3516CV610 has a dedicated SVP NPU that runs at ~17 inferences/sec.
The stock firmware uses HiSilicon's **AIDetect** library with the
`det_hv_hor.bin` model (872 KB) for person/vehicle detection with
built-in object tracking. This is what draws the green boxes in the
stock app.

**Recommendation:** Use **AIDetect as the primary detection engine** for
v1, supplemented by **IVE for motion detection**. Defer YOLOv8 to v2/v3
as an optional upgrade path. This matches what the stock firmware does,
gives us built-in tracking IDs for free, and avoids the complexity of
NMS post-processing and custom tracker implementation.

---

## What Stock superb Does

From CAMERA.md and empirical testing of the stock app:

| Feature | Stock Implementation |
|---------|---------------------|
| Person/vehicle detection | AIDetect via `det_hv_hor.bin`, ~17 fps on NPU |
| Tracking boxes | Built-in track_id from AIDetect library |
| Motion detection | IVP subsystem (likely IVE-based SAD + CCL) |
| Region intrusion | Polygon zone check on detection center coordinates |
| Line crossing | Segment intersection test on detection track path |
| Alarm actions | JPEG snapshot + cloud push + voice prompt + recording |
| Recording modes | Continuous / motion-triggered / alarm-triggered |

The firmware `variable` file confirms: `IVP=1` (AI detection ON),
`NPU_MODEL_TYPE=1`, `AIISP=0`, `NPU=0`. This means the stock firmware
uses the AIDetect/IVP path, **not** the SVP ACL/YOLOv8 path.

---

## Available AI Paths

The Hi3516CV610 SDK provides three tiers of AI processing:

### Tier 1: AIDetect (High-Level Detection Service)

- **API:** `ss_mpi_aidetect_*` (10 functions)
- **Library:** `libss_mpi_aidetect.{a,so}` (608 KB shared, 572 KB static)
- **Dependency:** `libaiservice_comm.{a,so}`, `libss_mpi_cipher.{a,so}`
- **Model:** `.bin` format (proprietary), loaded from memory or path
- **Runs on:** SVP NPU via `ot_svp_npu.ko`
- **Key advantage:** Built-in object tracking (track_id, track_status)

### Tier 2: SVP ACL (Low-Level NPU Runtime)

- **API:** `svp_acl_*` (~80 functions across 5 headers)
- **Library:** `libsvp_acl.{a,so}` (318 KB), `libsvp_aicpu.so` (124 KB)
- **Model:** `.om` format (Open Model, converted from ONNX/Caffe)
- **Runs on:** SVP NPU via `ot_svp_npu.ko`
- **Key advantage:** Any model architecture, retrainable

### Tier 3: IVE (Intelligent Video Engine)

- **API:** `ss_mpi_ive_*` (~30 functions), `ot_ivs_md_*` (7 functions)
- **Library:** `libss_mpi_ive.{a,so}` (59 KB)
- **Runs on:** Dedicated IVE hardware (not NPU, not CPU)
- **Key advantage:** Zero CPU cost, frame-rate motion detection

---

## Path 1: AIDetect (Recommended for v1)

### API Surface

```c
// Lifecycle
ss_mpi_aidetect_create_chn(chn, input_model, chn_attr);
ss_mpi_aidetect_destroy_chn(chn);

// Configuration
ss_mpi_aidetect_set_chn_attr(chn, chn_attr);     // tracking enable per class
ss_mpi_aidetect_get_chn_attr(chn, chn_attr);
ss_mpi_aidetect_set_chn_param(chn, chn_param);   // thresholds, priority
ss_mpi_aidetect_get_chn_param(chn, chn_param);
ss_mpi_aidetect_get_model_info(chn, model_info);  // class list, input size

// Per-frame inference
ss_mpi_aidetect_process(chn, frame, detect_result);

// Monitoring
ss_mpi_aidetect_query_status(chn, status);        // frame rate, recv count
ss_mpi_aidetect_set_log_level(level);
ss_mpi_aidetect_get_log_level(level);
```

### Supported Classes (12 total, model-dependent)

| Enum | Class | Stock model has? |
|------|-------|:---:|
| `OT_AIDETECT_CLASS_FACE` | Face | Yes (hvf models) |
| `OT_AIDETECT_CLASS_HUMAN` | Human body | Yes |
| `OT_AIDETECT_CLASS_VEHICLE` | Motor vehicle | Yes |
| `OT_AIDETECT_CLASS_PET` | Pet | No (hv models) |
| `OT_AIDETECT_CLASS_GARBAGE` | Garbage bag | No |
| `OT_AIDETECT_CLASS_BAG` | Package | No |
| `OT_AIDETECT_CLASS_WALLET` | Wallet | No |
| `OT_AIDETECT_CLASS_PHONE` | Phone | No |
| `OT_AIDETECT_CLASS_HEAD_SHOULDER` | Head + shoulder | No |
| `OT_AIDETECT_CLASS_BICYCLE` | Bicycle | No |
| `OT_AIDETECT_CLASS_MOTORCYCLE` | Motorcycle/scooter | No |
| `OT_AIDETECT_CLASS_PACKAGE` | Package | No |

### Detection Output Per Object

```c
typedef struct {
    ot_rect detect_rect;                    // x, y, width, height
    td_float detect_confidence;             // 0.0 - 1.0
    td_u32 track_id;                        // persistent across frames
    ot_aidetect_track_status track_status;  // NEW, UPDATE, DIE, VALID
} ot_aidetect_object;
```

### Tracking System

Built into the library. Each class can have tracking independently
enabled/disabled via `ot_aidetect_chn_attr.track_class_attr[].track_en`.

Track status lifecycle:
- `NEW` -- first frame an object is detected
- `UPDATE` -- object seen again in subsequent frames
- `DIE` -- object disappeared (after `track_miss_frame_num` frames)
- `VALID` -- tracking disabled for this class, raw detection only

The `track_miss_frame_num` parameter (per-class) controls how many
frames an object can be missing before the track dies. This is key for
line-crossing logic -- we need the track to persist across frames to
detect direction of travel.

### Model Variants Available

| Model File | Size | Classes | Source |
|------------|------|---------|--------|
| `det_hv_hor.bin` (on camera, resfs) | 893 KB | Human + Vehicle | Firmware lite model |
| `det_hvf_hor.bin` (SDK full) | 1.97 MB | Human + Vehicle + Face | SDK/Firmware Building |
| `det_hvf_hor_ll_lite.bin` (KOL) | 894 KB | Human + Vehicle + Face (lite) | Firmware Building KOL |

The camera's current `det_hv_hor.bin` (893 KB) is very close in size to
the KOL `det_hvf_hor_ll_lite.bin` (894 KB). The naming suggests the
on-camera model detects human + vehicle only (no face). The full model
at 1.97 MB adds face detection but may run slower.

**For v1:** Use the existing `det_hv_hor.bin` from resfs (already on
camera). If face detection is needed, swap to `det_hvf_hor.bin` (deploy
to SD card, 1.97 MB).

### Integration Pattern (from shumjj reference)

```c
// 1. Load model into MMZ memory
ss_mpi_sys_mmz_alloc(&phys, &virt, NULL, NULL, file_size);
// read model file into virt

// 2. Create channel
ot_aidetect_input_model input = {
    .model_load_mode = OT_AIDETECT_MODEL_LOAD_FROM_MEMORY,
    .model = virt,
    .size = file_size
};
ot_aidetect_chn_attr attr = { 0 };  // tracking configured after create
ss_mpi_aidetect_create_chn(0, &input, &attr);

// 3. Enable tracking for humans
ot_aidetect_chn_attr track_attr;
ss_mpi_aidetect_get_chn_attr(0, &track_attr);
for (int i = 0; i < track_attr.track_class_num; i++) {
    if (track_attr.track_class_attr[i].class_type == OT_AIDETECT_CLASS_HUMAN)
        track_attr.track_class_attr[i].track_en = TD_TRUE;
}
ss_mpi_aidetect_set_chn_attr(0, &track_attr);

// 4. Set detection thresholds
ot_aidetect_chn_param param;
ss_mpi_aidetect_get_chn_param(0, &param);
for (int i = 0; i < param.detect_threshold_num; i++) {
    param.detect_threshold[i].detect_threshold = 0.5f;
    param.detect_threshold[i].track_miss_frame_num = 30;  // ~1.5s at 20fps
}
ss_mpi_aidetect_set_chn_param(0, &param);

// 5. Per-frame inference loop (in dedicated thread)
while (running) {
    ot_video_frame frame;
    ss_mpi_vpss_get_chn_frame(grp, ai_chn, &frame, timeout);

    ot_aidetect_result_array result;
    // init result arrays...
    ss_mpi_aidetect_process(0, &frame, &result);

    // Process results: rule engine, alarm dispatch, VGS overlay
    for (int c = 0; c < result.class_num; c++) {
        for (int o = 0; o < result.object_class[c].object_num; o++) {
            ot_aidetect_object *obj = &result.object_class[c].objects[o];
            // obj->detect_rect, obj->track_id, obj->detect_confidence
            // Feed to rule engine...
        }
    }

    ss_mpi_vpss_release_chn_frame(grp, ai_chn, &frame);
}

// 6. Cleanup
ss_mpi_aidetect_destroy_chn(0);
ss_mpi_sys_mmz_free(phys, virt);
```

### VPSS Channel for AI

AIDetect expects NV21 frames at the model's native resolution. The
model info query (`ss_mpi_aidetect_get_model_info`) returns the expected
`size.width` and `size.height`. A dedicated VPSS channel (e.g. chn 2)
should be configured to output at this resolution:

- Model input: 640x384 (from CAMERA.md: "Input: 1x3x384x640")
- Pixel format: NV21 (YUV420 semi-planar)
- Frame rate: match sensor (20 fps) -- AIDetect runs at NPU speed internally

The shumjj reference uses a separate VB pool for the AI VPSS channel
to avoid contention with the video encoding path.

### Libraries Needed

To link AIDetect statically into pipeline_test:

```
libss_mpi_aidetect.a   (572 KB)  -- core API
libaiservice_comm.a    (size TBD) -- AI service common
libss_mpi_cipher.a     (size TBD) -- cipher (dependency)
libot_osal.a           (already linked)
```

Or dynamically: deploy `libss_mpi_aidetect.so` (608 KB) +
`libaiservice_comm.so` + add to LD_PRELOAD.

---

## Path 2: SVP ACL + YOLOv8

### Overview

SVP ACL is the low-level NPU runtime. You load an `.om` model, allocate
input/output buffers, execute inference, parse raw tensor output. All
post-processing (NMS, class filtering, coordinate scaling) is your
responsibility.

### API Complexity

~80 functions across 5 headers:
- `svp_acl.h` -- init/finalize
- `svp_acl_rt.h` -- device, context, stream, memory management
- `svp_acl_mdl.h` -- model load/unload/execute, AIPP config
- `svp_acl_base.h` -- data buffer management
- `svp_acl_ext.h` -- NPU utilization, AICPU tasks

### Integration Complexity (from shumjj: 971 lines)

The shumjj `dev_svp_yolov8.cpp` is 971 lines and handles:
1. SVP ACL runtime init (`svp_acl_init`, `svp_acl_rt_set_device`)
2. Model load from memory (`svp_acl_mdl_load_from_mem`)
3. Input/output dataset creation with stride-aware buffer allocation
4. VPSS channel setup (640x640, YVU422 semi-planar)
5. Per-frame: get VPSS frame -> update input buffer -> `svp_acl_mdl_execute` -> parse outputs
6. NMS post-processing (`MulticlassNms` with IoU calculation)
7. VGS rectangle drawing for overlay
8. Optional SVC-aware encoding for ROI bitrate optimization
9. Cleanup of all ACL resources

### Performance (from shumjj measurements)

- `svp_acl_mdl_execute()`: ~90ms per frame for standard YOLOv8
- With `new_rpn` variant: ~53ms per frame
- Effective inference rate: ~2 fps (frame skip: src=3, dst=2)
- Model size: 3.9 MB (`.om` format)

### Post-Processing Required

Unlike AIDetect, YOLOv8 via ACL gives raw tensor output:
- 3 output heads with (Nx6) candidates: x, y, w, h, confidence, class
- ~5040 candidates total before NMS
- Must implement: confidence thresholding, class-specific NMS, coordinate
  rescaling from 640x640 to original resolution
- Must implement own tracker (no built-in tracking)

### YOLOv8 COCO Classes (80)

person, bicycle, car, motorcycle, airplane, bus, train, truck, boat,
traffic light, fire hydrant, stop sign, parking meter, bench, bird,
cat, dog, horse, sheep, cow, elephant, bear, zebra, giraffe, backpack,
umbrella, handbag, tie, suitcase, frisbee, skis, snowboard, sports ball,
kite, baseball bat, baseball glove, skateboard, surfboard, tennis racket,
bottle, wine glass, cup, fork, knife, spoon, bowl, banana, apple,
sandwich, orange, broccoli, carrot, hot dog, pizza, donut, cake, chair,
couch, potted plant, bed, dining table, toilet, tv, laptop, mouse,
remote, keyboard, cell phone, microwave, oven, toaster, sink,
refrigerator, book, clock, vase, scissors, teddy bear, hair drier,
toothbrush

### Model Files Available

| File | Size | Location |
|------|------|----------|
| `yolov8.om` | 3.99 MB | PQ tools, Firmware Building |
| `yolov8_new_rpn.om` | 3.89 MB | shumjj rootfs |

Neither is on the camera currently. Would need to deploy to SD card.

---

## Path 3: IVE Motion Detection

### Overview

The IVE (Intelligent Video Engine) is a **dedicated hardware accelerator**
separate from both the CPU and NPU. It performs classical computer vision
operations at frame rate with zero CPU overhead. The motion detection
module uses SAD (Sum of Absolute Differences) + CCL (Connected Component
Labeling) to detect regions of change between frames.

### API (Simple -- 7 functions)

```c
ot_ivs_md_init();
ot_ivs_md_create_chn(md_chn, &md_attr);
ot_ivs_md_proc(md_chn, &cur_img, &ref_img, &sad_out, &blob_out);
ot_ivs_md_get_bg(md_chn, &bg_img);
ot_ivs_md_set_chn_attr(md_chn, &md_attr);
ot_ivs_md_get_chn_attr(md_chn, &md_attr);
ot_ivs_md_destroy_chn(md_chn);
ot_ivs_md_exit();
```

### Processing Pipeline

```
VPSS chn (downscaled, e.g. 320x240 grayscale)
  -> IVE CSC (YUV -> U8C1 grayscale)
  -> IVE DMA (copy into IVE image buffer)
  -> ot_ivs_md_proc(current, reference)
     internally: SAD block comparison -> threshold -> CCL blob extraction
  -> ot_ive_ccblob (connected component blob output)
  -> blob_to_rect conversion
  -> Rectangle array output
```

### Configuration

```c
ot_md_attr md_attr = {
    .alg_mode = OT_MD_ALG_MODE_BG,     // background model based
    .sad_mode = OT_IVE_SAD_MODE_MB_4X4, // 4x4 macro-block comparison
    .sad_out_ctrl = OT_IVE_SAD_OUT_CTRL_THRESHOLD, // binary threshold output
    .width = 320,
    .height = 240,
    .sad_threshold = 100,               // sensitivity (lower = more sensitive)
    .ccl_ctrl = { .mode = OT_IVE_CCL_MODE_4C, .init_area_threshold = 16,
                  .step = 2 },
    .add_ctrl = { .x = 32768, .y = 32768 }, // background update rate
};
```

### Library Needed

`libss_mpi_ive.{a,so}` (59 KB) -- already available in SDK. Very small.

### Key Advantage

- **Zero CPU cost.** IVE is dedicated silicon.
- **Frame rate.** Runs at full sensor rate (20 fps) with no drop.
- **Simplicity.** ~200 lines of integration code.
- **Complements AIDetect.** Use IVE for "is there any motion?" as a
  cheap pre-filter, then run AIDetect only on frames with motion (saves
  NPU power and reduces false alarms).

### IVE Also Provides

Beyond motion detection, the IVE hardware can do:
- **KCF object tracking** (`ss_mpi_ive_kcf_proc`) -- could supplement
  AIDetect tracking if needed
- **GMM2 background modeling** -- adaptive background for outdoor scenes
- **Optical flow** (Lucas-Kanade) -- velocity estimation
- **Edge detection** (Canny, Sobel) -- scene analysis
- **Perspective transform** -- for virtual tripwire coordinate mapping

---

## Comparison: AIDetect vs YOLOv8

| Factor | AIDetect | YOLOv8 (SVP ACL) |
|--------|----------|-------------------|
| **Inference speed** | ~17 fps (from /proc/umap) | ~2 fps (53-90ms/frame + overhead) |
| **Model size** | 893 KB (lite) / 1.97 MB (full) | 3.9 MB |
| **Classes** | 2-3 (human/vehicle/face) | 80 (COCO) |
| **Built-in tracking** | Yes (track_id, track_status) | No (must implement) |
| **NMS / post-processing** | Internal (hidden) | Must implement (~100 lines) |
| **API complexity** | 10 functions | ~80 functions + post-processing |
| **Integration effort** | ~300 lines C | ~1000 lines C/C++ |
| **Custom training** | No (closed model format) | Yes (ONNX -> .om converter) |
| **Already on camera** | Yes (model in resfs) | No (must deploy .om) |
| **Stock firmware uses** | Yes | No |
| **Library on camera** | Statically linked in superb | Not present |
| **Memory overhead** | ~1-2 MB (model + buffers) | ~5-6 MB (model + buffers) |
| **Retrainable** | No | Yes |
| **Surveillance-tuned** | Yes (designed for IPC) | Generic (COCO dataset) |

---

## Recommendation

### v1: AIDetect + IVE Motion Detection

**Primary detection:** AIDetect with `det_hv_hor.bin` (human + vehicle).
- Already on camera in resfs, proven at ~17 fps.
- Built-in tracking gives us track_id for line-crossing logic for free.
- Matches stock behavior -- users expect the same detection quality.
- Static link `libss_mpi_aidetect.a` (572 KB) + deps into pipeline_test.

**Motion detection:** IVE MD for cheap motion trigger.
- Zero CPU cost, full frame rate.
- Use as pre-filter: "motion detected -> start/continue recording",
  "motion + AIDetect human -> fire alarm".
- ~200 lines to integrate.

**Rule engine:** Our own code (~500 lines), operating on AIDetect output.
- Point-in-polygon for region intrusion.
- Segment intersection for line crossing (using track history).
- Class filter, time-of-day filter, debounce.

### v2/v3: Optional YOLOv8 Upgrade

If users need:
- More object classes (cat, dog, car make/model, etc.)
- Custom-trained models for specific environments
- Higher accuracy at the cost of lower frame rate

Then add SVP ACL + YOLOv8 as an alternative detection backend, selectable
in config. AIDetect and YOLOv8 are **mutually exclusive** on the NPU
(confirmed by shumjj: they check aidetect first, skip yolov8 if enabled).

### What We Do NOT Need

- **AIVSR** (face capture + VLPR) -- overkill for v1, adds complexity.
- **AIISP** (AI noise reduction) -- nice-to-have but not part of
  detection. Our 3DNR V2 tuning is already good. Defer to Phase 8+.
- **SmartAE** -- uses detection results to improve auto-exposure for
  faces/people. Minor quality improvement, low priority.

---

## Implementation Plan

### Phase 7 Integration Steps (when we get there)

1. **Add VPSS channel for AI** -- chn 2, output at model's expected
   resolution (640x384 for AIDetect, or query from model_info),
   NV21 format, separate VB pool.

2. **Add VPSS channel for IVE MD** -- chn 3, output at 320x240,
   grayscale (U8C1 via IVE CSC), separate VB pool.

3. **Vendor AIDetect libraries** -- copy from SDK to
   `driver/prebuilt/aidetect/`:
   - `libss_mpi_aidetect.a` (572 KB)
   - `libaiservice_comm.a`
   - `libss_mpi_cipher.a`
   - Headers already in `driver/prebuilt/sdk_include/`

4. **Vendor IVE library** -- copy `libss_mpi_ive.a` to
   `driver/prebuilt/ive/`.

5. **AIDetect thread** -- dedicated thread in daemon:
   - Init: load model, create channel, enable tracking for human class
   - Loop: get VPSS frame -> `ss_mpi_aidetect_process` -> feed rule engine
   - Exit: destroy channel, free model

6. **IVE MD thread** -- dedicated thread:
   - Init: `ot_ivs_md_init`, create channel, allocate IVE images
   - Loop: get VPSS frame -> CSC to grayscale -> `ot_ivs_md_proc` ->
     update motion state
   - Exit: destroy channel, `ot_ivs_md_exit`

7. **Rule engine** -- evaluates per-frame against detection results:
   - Region intrusion: point-in-polygon test per configured zone
   - Line crossing: track history + segment intersection
   - Class filter, time-of-day, debounce
   - Fire alarm -> JPEG snapshot + webhook POST

8. **Motion-triggered recording** -- see below.

9. **VGS overlay** (optional) -- draw detection boxes on a debug/AI
   stream channel using `ss_mpi_vgs_add_cover_task`.

---

## Libraries and Models Inventory

### Required for v1 (AIDetect + IVE MD)

**Libraries (from SDK, static):**

| Library | Size | Source Path |
|---------|------|-------------|
| `libss_mpi_aidetect.a` | 572 KB | `SDK_V1.0.2.1_MPP_Sample/lib/hisilicon/` |
| `libaiservice_comm.a` | TBD | `SDK_V1.0.2.1_MPP_Sample/lib/hisilicon/` |
| `libss_mpi_cipher.a` | TBD | `SDK_V1.0.2.1_MPP_Sample/lib/hisilicon/` |
| `libss_mpi_ive.a` | ~59 KB | `SDK_V1.0.2.1_MPP_Sample/lib/hisilicon/` or PQ tools |

**Headers (already vendored):**

| Header | Location |
|--------|----------|
| `ss_mpi_aidetect.h` | `driver/prebuilt/sdk_include/` |
| `ot_common_aidetect.h` | `driver/prebuilt/sdk_include/` |
| `ss_mpi_ive.h` | `driver/prebuilt/sdk_include/` |
| `ot_common_ive.h` | `driver/prebuilt/sdk_include/` |
| `ot_ivs_md.h` | `driver/prebuilt/sdk_include/` (needs copy) |
| `ot_common_md.h` | `driver/prebuilt/sdk_include/` (needs copy) |
| `ot_common_svp.h` | `driver/prebuilt/sdk_include/` |

**Models:**

| Model | Size | On Camera? | Deploy To |
|-------|------|:---:|-----------|
| `det_hv_hor.bin` | 893 KB | Yes (resfs) | No deploy needed -- use `/tmp/resfs/ivp/det_hv_hor.bin` |
| `det_hvf_hor.bin` (optional, full) | 1.97 MB | No | SD card if face detection needed |

**Kernel modules (already loaded at boot):**

| Module | Purpose |
|--------|---------|
| `ot_svp_npu.ko` | NPU driver (loaded by `loadhi3516cv610`) |
| `ot_ive.ko` | IVE driver (loaded by `loadhi3516cv610`, `save_power=1`) |
| `ot_vca.ko` | VCA driver (loaded by `loadhi3516cv610`) |

### Optional for v2 (YOLOv8)

| File | Size | Notes |
|------|------|-------|
| `libsvp_acl.a` | 318 KB | SVP ACL static library |
| `libsvp_aicpu.so` | 124 KB | Runtime dependency (deploy to camera) |
| `yolov8_new_rpn.om` | 3.89 MB | Model (deploy to SD card) |

---

## Rule Engine Design (Tripwire, Region, Motion)

### Data Flow

```
AIDetect output (per frame):
  -> List of (class, rect, confidence, track_id, track_status)

IVE MD output (per frame):
  -> Boolean: motion_detected
  -> Optional: motion_region rectangles

Rule Engine (per frame):
  -> For each detection, check against all active rules
  -> Maintain track_history[track_id] = list of recent centers
  -> Evaluate:
     1. Region intrusion: is center inside polygon?
     2. Line crossing: does track path intersect line segment?
     3. Class filter: does class match rule filter?
     4. Time filter: is current time in rule's active window?
     5. Debounce: has this rule fired within cooldown period?
  -> If rule fires: trigger alarm action
```

### Region Intrusion (Polygon Zone)

Standard point-in-polygon (ray casting):

```c
bool point_in_polygon(int px, int py, point_t *poly, int n) {
    bool inside = false;
    for (int i = 0, j = n - 1; i < n; j = i++) {
        if ((poly[i].y > py) != (poly[j].y > py) &&
            px < (poly[j].x - poly[i].x) * (py - poly[i].y) /
                 (poly[j].y - poly[i].y) + poly[i].x)
            inside = !inside;
    }
    return inside;
}
```

Test the center of each detection rect against configured polygon zones.

### Line Crossing (Tripwire)

Requires track history. For each tracked object, maintain a sliding
window of the last N center positions. On each frame:

1. Get current center: `cx = rect.x + rect.width/2`, `cy = rect.y + rect.height/2`
2. Get previous center from `track_history[track_id]`
3. Test if segment (prev_center -> current_center) intersects the
   configured line segment
4. If intersection: determine crossing direction using cross product
5. If direction matches rule (L->R, R->L, or both): fire alarm

```c
// Cross product determines which side of the line a point is on
float cross(line, point) = (line.x2-line.x1)*(point.y-line.y1)
                         - (line.y2-line.y1)*(point.x-line.x1);
// sign change between prev and current = crossing event
// sign value = direction
```

### Track History Management

```c
#define MAX_TRACKS 64
#define HISTORY_LEN 10

typedef struct {
    uint32_t track_id;
    int cx[HISTORY_LEN], cy[HISTORY_LEN];
    int head;           // circular buffer index
    int count;          // frames tracked
    bool active;
    uint64_t last_seen; // frame number
} track_history_t;

track_history_t tracks[MAX_TRACKS];
```

On each frame:
- For each detection with `track_status == UPDATE`: update history
- For each detection with `track_status == NEW`: create new entry
- For each detection with `track_status == DIE`: mark inactive
- Periodically garbage-collect stale entries

### Rule Configuration (JSON)

```json
{
  "rules": [
    {
      "id": "person_in_yard",
      "type": "region",
      "enabled": true,
      "classes": ["human"],
      "polygon": [[100,100], [500,100], [500,400], [100,400]],
      "active_hours": {"start": "22:00", "end": "06:00"},
      "debounce_sec": 30,
      "min_confidence": 0.6
    },
    {
      "id": "driveway_crossing",
      "type": "line",
      "enabled": true,
      "classes": ["human", "vehicle"],
      "line": [[200, 300], [600, 300]],
      "direction": "both",
      "debounce_sec": 10,
      "min_confidence": 0.5
    },
    {
      "id": "any_motion",
      "type": "motion",
      "enabled": true,
      "sensitivity": 50,
      "debounce_sec": 5
    }
  ]
}
```

### Alarm Action

When a rule fires:
1. Capture JPEG snapshot via VENC JPEG channel
2. Optionally annotate snapshot with detection box
3. POST JSON + snapshot to webhook URL (configurable)
4. Log alarm event with timestamp, rule ID, snapshot path
5. Mark recording segment as "alarm" (for alarm-only playback filter)

---

## Motion-Triggered Recording

### Recording Modes

| Mode | Description |
|------|-------------|
| `continuous` | Always recording (current behavior) |
| `motion` | Record only when motion/detection active |
| `disabled` | No recording |

### Motion-Triggered Flow

```
State machine:

IDLE
  -> IVE MD detects motion -> start pre-roll flush -> RECORDING
  -> AIDetect detects object -> start pre-roll flush -> RECORDING

RECORDING
  -> Motion/detection continues -> stay RECORDING
  -> No motion/detection for N seconds -> COOLDOWN

COOLDOWN (timer running)
  -> Motion/detection resumes -> back to RECORDING
  -> Timer expires -> close segment -> IDLE
```

### Pre-Roll Buffer

Always maintain a rolling buffer of the last N seconds of encoded
H.265 frames (e.g. 30 seconds). This is a circular buffer in memory
(RAM cost: ~2-4 MB for 30s at 4 Mbps).

When motion triggers recording:
1. Flush pre-roll buffer to the recording file (so the recording starts
   30s before the motion event)
2. Switch to live recording mode (write frames directly)
3. Continue until cooldown expires

### Implementation Notes

- Pre-roll buffer operates on VENC output (encoded H.265 NALs), not raw
  frames. This keeps memory usage manageable.
- The recording muxer (Phase 4, fMP4) needs to handle both "flush
  pre-roll" and "live append" modes.
- Segment boundaries should still respect the configured duration limit
  (e.g. 5 min segments), even in motion-triggered mode.
- Index file marks segments with `trigger: "motion"` vs `trigger: "alarm"`
  vs `trigger: "continuous"` for the web UI to filter.

---

## Open Questions

1. **AIDetect shared lib dependencies.** We know it needs
   `libaiservice_comm` and `libss_mpi_cipher`. Are there others? Need
   to test-link and resolve undefined symbols.

2. **AIDetect + VB pool interaction.** The SDK sample comment says
   "aidetect do not use vb" (line 364 of sample_aidetect.c). But the
   shumjj reference creates a dedicated VB pool for the VPSS channel
   feeding AIDetect. Clarify: AIDetect doesn't need its own VB pool,
   but the VPSS channel providing frames does.

3. **Model input format.** CAMERA.md says "Input: 1x3x384x640" which
   suggests CHW RGB, but the SDK sample feeds NV21 (YUV420). The model
   likely has internal preprocessing (AIPP-like). Confirm by running
   `ss_mpi_aidetect_get_model_info` on camera.

4. **det_hv_hor.bin vs det_hvf_hor.bin naming.** Does the on-camera
   model support face or not? The `hv` vs `hvf` naming suggests no
   face. Confirm by checking `model_info.class_num` and
   `model_info.classes[]` at runtime.

5. **IVE save_power=1 implication.** The camera loads `ot_ive.ko` with
   `save_power=1`. Does this affect performance? May need to reload
   without save_power for our use case.

6. **NPU concurrent access.** Can AIDetect and IVE MD run simultaneously?
   Almost certainly yes -- they use different hardware (NPU vs IVE
   engine). But confirm no resource conflicts.

7. **Memory budget.** AIDetect model (~1 MB) + IVE buffers (~0.5 MB) +
   pre-roll buffer (~3 MB) + result arrays (~small). Total ~5 MB
   additional. Current free RAM is ~20 MB with superb. We'll have more
   since our daemon is smaller, but need to verify.

---

## References

- SDK headers: `research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/include/hisilicon/`
- SDK AIDetect sample: `research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/src/svp/ai_component/aidetect/`
- SDK AIDetect VIE sample: `research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/src/svp/ai_component/aidetect_vie/`
- SDK IVE MD sample: `research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/src/svp/ive/sample/sample_ive_md.c`
- SDK SVP NPU sample: `research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/src/svp/svp_npu/`
- shumjj AIDetect: `research/shumjj-3516cv610_app/device/aidetect/dev_aidetect.cpp`
- shumjj YOLOv8: `research/shumjj-3516cv610_app/device/dev_svp_yolov8.cpp`
- shumjj main (mutual exclusion): `research/shumjj-3516cv610_app/main.cpp` lines 1231-1262
- HIVIEW IVE MD: `research/HIVIEW/mod/svp/3516c/ive/sample_ive_md.c`
- HIVIEW NPU YOLO: `research/HIVIEW/mod/svp/3519d_003/svp_npu/`
- Firmware model: `firmware/extracted/resfs/ivp/det_hv_hor.bin` (893 KB)
- Firmware variable: `firmware/extracted/appfs/home/variable` (IVP=1, NPU=0)
- CAMERA.md: AI Human/Vehicle Detection section
