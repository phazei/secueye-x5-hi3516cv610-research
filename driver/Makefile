# SC635HAI Sensor Driver -- Build System
#
# Run from WSL:  cd /mnt/e/Projects/ipc_XMeye_camera/driver && make
#
# Outputs:
#   build/libsns_sc635hai.so  -- sensor driver shared library
#   build/sensor_test          -- test binary (phases 1-4)

# ── Toolchain ──────────────────────────────────────────────────────
TC_BASE = ../research/hi3516cv610_toolchain/gcc-20250305-arm-v01c02-linux-musleabi/arm-v01c02-linux-musleabi-gcc
CC      = $(TC_BASE)/bin/arm-linux-musleabi-gcc
STRIP   = $(TC_BASE)/bin/arm-linux-musleabi-strip
SYSROOT = $(TC_BASE)/target

# ── SDK paths ──────────────────────────────────────────────────────
# V1.0.2.1 B020 SDK (compatible with camera's B051 kernel, ISP ioctl type 0x70)
SDK_INC = ../research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/include/hisilicon
SDK_LIB = ../research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/lib/hisilicon
# Legacy B040 SDK (incompatible ISP ioctl type 0x49)
# SDK_INC_B040 = ../research/HIVIEW/mod/mpp/3516c/inc/hisisdk
# SDK_LIB_B040 = ../research/HIVIEW/mod/mpp/3516c/lib/hisisdk

# ── Compiler flags ─────────────────────────────────────────────────
ARCH_FLAGS = -mcpu=cortex-a7 -mfloat-abi=softfp -mfpu=neon-vfpv4 -mthumb
WARN_FLAGS = -Wall -Wextra -Wno-unused-parameter -Wno-sign-compare
OPT_FLAGS  = -Os -ffunction-sections -fdata-sections -fno-lto

# Common flags
CFLAGS = $(ARCH_FLAGS) $(WARN_FLAGS) $(OPT_FLAGS) --sysroot=$(SYSROOT)
CFLAGS += -I$(SDK_INC) -Isrc

# ── Output directory ───────────────────────────────────────────────
BUILD = build
$(shell mkdir -p $(BUILD))

# ── Targets ────────────────────────────────────────────────────────
.PHONY: all clean driver test pipeline recv hook shim check_toolchain awb_dump

all: driver test pipeline recv hook shim pqbin awb_dump

check_toolchain:
	@if [ ! -x "$(CC)" ]; then \
		echo "ERROR: Cross-compiler not found at $(CC)"; \
		echo "Run from WSL: cd /mnt/e/Projects/ipc_XMeye_camera/driver && make"; \
		exit 1; \
	fi

# ── Sensor driver shared library ──────────────────────────────────
# Built as position-independent shared library. Links against the
# ISP/AE/AWB MPI libraries that are already loaded on the camera
# (by the kernel modules + ISP framework).
DRIVER_SRCS = src/sc635hai_cmos.c src/sc635hai_sensor_ctl.c
DRIVER_OBJS = $(BUILD)/sc635hai_cmos.o $(BUILD)/sc635hai_sensor_ctl.o

driver: check_toolchain $(BUILD)/libsns_sc635hai.so
	@echo "Built: $(BUILD)/libsns_sc635hai.so"
	@ls -la $(BUILD)/libsns_sc635hai.so

$(BUILD)/sc635hai_cmos.o: src/sc635hai_cmos.c src/sc635hai_cmos.h
	$(CC) $(CFLAGS) -fPIC -c -o $@ $<

$(BUILD)/sc635hai_sensor_ctl.o: src/sc635hai_sensor_ctl.c src/sc635hai_cmos.h
	$(CC) $(CFLAGS) -fPIC -c -o $@ $<

$(BUILD)/libsns_sc635hai.so: $(DRIVER_OBJS)
	$(CC) $(ARCH_FLAGS) --sysroot=$(SYSROOT) -shared -fno-lto -fno-use-linker-plugin \
		-o $@ $(DRIVER_OBJS) \
		-L$(SDK_LIB) -lss_mpi_isp -lss_mpi_ae -lss_mpi_awb \
		-Wl,--gc-sections
	$(STRIP) $@

# ── Test binary ────────────────────────────────────────────────────
# Statically linked test app that exercises the sensor in 4 phases:
#   Phase 1: I2C chip ID read
#   Phase 2: Register init sequence
#   Phase 3: MIPI + ISP pipeline
#   Phase 4: Full video pipeline (VPSS + VENC)
test: check_toolchain $(BUILD)/sensor_test
	@echo "Built: $(BUILD)/sensor_test"
	@ls -la $(BUILD)/sensor_test

$(BUILD)/sensor_test: test/sensor_test.c src/sc635hai_cmos.h
	$(CC) $(CFLAGS) -static -o $@ $< \
		-Wl,--gc-sections
	$(STRIP) $@

# ── Pipeline test binary ───────────────────────────────────────────
# Dynamically linked ARM binary -- loads libsns_sc635hai.so via dlopen
# and links against all SDK MPI libraries for the full video pipeline.
# At runtime, the SDK .so files must be on the camera (they are, loaded
# by kernel modules at boot).
PIPELINE_LIBS = -lss_mpi -lot_mpi_isp -lss_mpi_isp -lss_mpi_ae -lss_mpi_awb \
                -lss_mpi_sysbind -lss_mpi_sysmem -lsecurec -lot_osal \
                -lpthread -ldl -lm

pipeline: check_toolchain $(BUILD)/pipeline_test
	@echo "Built: $(BUILD)/pipeline_test"
	@ls -la $(BUILD)/pipeline_test

$(BUILD)/pipeline_test: test/pipeline_test.c
	$(CC) $(CFLAGS) -o $@ $< \
		-L$(SDK_LIB) $(PIPELINE_LIBS) \
		-Wl,-rpath,/usr/lib -Wl,-rpath,/progs/rec/00 \
		-Wl,--gc-sections -fno-use-linker-plugin \
		-Wl,--allow-shlib-undefined
	$(STRIP) $@

# ── VPSS test binary ──────────────────────────────────────────────
vpss_test: check_toolchain $(BUILD)/vpss_test
	@echo "Built: $(BUILD)/vpss_test"
	@ls -la $(BUILD)/vpss_test

$(BUILD)/vpss_test: test/vpss_test.c
	$(CC) $(CFLAGS) -o $@ $< \
		-L$(SDK_LIB) $(PIPELINE_LIBS) \
		-Wl,-rpath,/usr/lib -Wl,-rpath,/progs/rec/00 \
		-Wl,--gc-sections -fno-use-linker-plugin \
		-Wl,--allow-shlib-undefined
	$(STRIP) $@

# ── recv: Tiny TCP file receiver ───────────────────────────────────
# Static binary, no dependencies. Deploy once via base64, then use
# for fast raw TCP file transfers over WiFi.
recv: check_toolchain $(BUILD)/recv
	@echo "Built: $(BUILD)/recv"
	@ls -la $(BUILD)/recv

$(BUILD)/recv: ../tools/recv.c
	$(CC) $(CFLAGS) -static -Wl,--gc-sections -o $@ $<
	$(STRIP) $@

# ── ioctl hook shared library ──────────────────────────────────
# LD_PRELOAD .so that intercepts ioctl() to capture VI struct payloads.
# Must be a shared library (not static).
hook: check_toolchain $(BUILD)/ioctl_hook.so
	@echo "Built: $(BUILD)/ioctl_hook.so"
	@ls -la $(BUILD)/ioctl_hook.so

$(BUILD)/ioctl_hook.so: ../tools/ioctl_hook.c
	$(CC) $(ARCH_FLAGS) --sysroot=$(SYSROOT) $(WARN_FLAGS) -Os -fPIC -shared \
		-fno-lto -fno-use-linker-plugin \
		-o $@ $< -ldl
	$(STRIP) $@

# ── Offset checker ─────────────────────────────────────────────
offsets: check_toolchain $(BUILD)/check_offsets
	@echo "Built: $(BUILD)/check_offsets"

$(BUILD)/check_offsets: test/check_offsets.c
	$(CC) $(CFLAGS) -static -o $@ $< -Wl,--gc-sections -fno-use-linker-plugin
	$(STRIP) $@

# ── VI B040->B051 ioctl shim ──────────────────────────────────
# LD_PRELOAD .so that translates B040 VI struct layouts to B051.
shim: check_toolchain $(BUILD)/vi_shim.so
	@echo "Built: $(BUILD)/vi_shim.so"
	@ls -la $(BUILD)/vi_shim.so

$(BUILD)/vi_shim.so: ../tools/vi_shim.c
	$(CC) $(ARCH_FLAGS) --sysroot=$(SYSROOT) $(WARN_FLAGS) -Os -fPIC -shared \
		-fno-lto -fno-use-linker-plugin \
		-o $@ $< -ldl
	$(STRIP) $@

# ── AWB dump tool ──────────────────────────────────────────────────
# Queries superb's running ISP and dumps all AWB calibration data.
# Read-only -- won't disturb the running pipeline.
awb_dump: check_toolchain $(BUILD)/awb_dump
	@echo "Built: $(BUILD)/awb_dump"
	@ls -la $(BUILD)/awb_dump

$(BUILD)/awb_dump: test/awb_dump.c
	$(CC) $(CFLAGS) -o $@ $< \
		-L$(SDK_LIB) $(PIPELINE_LIBS) \
		-Wl,-rpath,/usr/lib -Wl,-rpath,/progs/rec/00 \
		-Wl,--gc-sections -fno-use-linker-plugin \
		-Wl,--allow-shlib-undefined
	$(STRIP) $@

# ── PQ bin library (copy from SDK PQ ext API) ─────────────────────
PQ_EXT_DIR = ../research/hi3516cv610_PictureQuality/Hi3516CV610_PQ_ext_api_V1.0.2.1
pqbin: $(BUILD)/libbin.so
	@echo "Copied: $(BUILD)/libbin.so"
	@ls -la $(BUILD)/libbin.so

$(BUILD)/libbin.so: $(PQ_EXT_DIR)/libbin/release/libbin.so
	cp $< $@

# ── Clean ──────────────────────────────────────────────────────────
clean:
	rm -rf $(BUILD)
