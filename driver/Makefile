# SC635HAI Sensor Driver + IPC Daemon -- Build System
#
# Run from WSL:  cd /mnt/e/Projects/ipc_XMeye_camera/driver && make
#
# Outputs:
#   build/ipc_daemon           -- camera daemon (replaces pipeline_test)
#   build/libsns_sc635hai.so   -- sensor driver shared library
#   build/sensor_test          -- standalone sensor I2C test
#   build/recv                 -- TCP file receiver
#   build/reg_dump             -- I2C register dump tool
#   build/awb_dump             -- ISP AWB calibration reader

# ── Toolchain ──────────────────────────────────────────────────────
TC_BASE = ../research/hi3516cv610_toolchain/gcc-20250305-arm-v01c02-linux-musleabi/arm-v01c02-linux-musleabi-gcc
CC      = $(TC_BASE)/bin/arm-linux-musleabi-gcc
STRIP   = $(TC_BASE)/bin/arm-linux-musleabi-strip
SYSROOT = $(TC_BASE)/target

# ── SDK paths (all vendored locally, no submodule dependencies) ────
SDK_INC = prebuilt/sdk_include
SDK_LIB = prebuilt/sdk_mpi

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
.PHONY: all clean driver test daemon recv reg_dump check_toolchain awb_dump

all: driver test daemon recv reg_dump pqbin awb_dump

check_toolchain:
	@if [ ! -x "$(CC)" ]; then \
		echo "ERROR: Cross-compiler not found at $(CC)"; \
		echo "Run from WSL: cd /mnt/e/Projects/ipc_XMeye_camera/driver && make"; \
		exit 1; \
	fi

# ── Sensor driver shared library ──────────────────────────────────
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
test: check_toolchain $(BUILD)/sensor_test
	@echo "Built: $(BUILD)/sensor_test"
	@ls -la $(BUILD)/sensor_test

$(BUILD)/sensor_test: test/sensor_test.c src/sc635hai_cmos.h
	$(CC) $(CFLAGS) -static -o $@ $< \
		-Wl,--gc-sections
	$(STRIP) $@

# ── IPC Daemon (was pipeline_test) ─────────────────────────────────
# Modular daemon: daemon/main.c + daemon/hal/*.c
# Dynamically linked ARM binary -- loads libsns_sc635hai.so via dlopen
# and links against all SDK MPI libraries for the full video pipeline.
#
# RTSP support: Uses the SDK's xop RTSP library (libxoprtsp.a) for live
# H.265 streaming.  The xop lib is C++, so we need libstdc++.
PIPELINE_LIBS = -lss_mpi -lot_mpi_isp -lss_mpi_isp -lss_mpi_ae -lss_mpi_awb \
                -lss_mpi_sysbind -lss_mpi_sysmem -lsecurec -lot_osal \
                -lpthread -ldl -lm

AUDIO_LIBS    = $(SDK_LIB)/libss_mpi_audio.a \
                $(SDK_LIB)/libupvqe.a \
                $(SDK_LIB)/libdnvqe.a \
                $(SDK_LIB)/libvoice_engine.a

RTSP_LIB_DIR  = rtsp/lib
RTSP_INC_DIR  = rtsp/include
STDCXX_A      = $(TC_BASE)/arm-linux-musleabi/lib/a7_softfp_neon-vfpv4/libstdc++.a
RTSP_LIBS     = -L$(RTSP_LIB_DIR) -lxoprtsp $(STDCXX_A)

# Daemon include paths (daemon/ headers + SDK + RTSP + sensor)
DAEMON_CFLAGS = $(CFLAGS) -Idaemon -Irtsp -I$(RTSP_INC_DIR)

# Daemon source files
DAEMON_SRCS = daemon/main.c \
              daemon/hal/sys.c \
              daemon/hal/vi.c \
              daemon/hal/isp.c \
              daemon/hal/vpss.c \
              daemon/hal/venc.c \
              daemon/hal/audio.c \
              daemon/hal/watchdog.c

DAEMON_OBJS = $(BUILD)/daemon_main.o \
              $(BUILD)/daemon_hal_sys.o \
              $(BUILD)/daemon_hal_vi.o \
              $(BUILD)/daemon_hal_isp.o \
              $(BUILD)/daemon_hal_vpss.o \
              $(BUILD)/daemon_hal_venc.o \
              $(BUILD)/daemon_hal_audio.o \
              $(BUILD)/daemon_hal_watchdog.o

daemon: check_toolchain $(BUILD)/ipc_daemon
	@echo "Built: $(BUILD)/ipc_daemon"
	@ls -la $(BUILD)/ipc_daemon

# Compile each daemon source to its own .o
$(BUILD)/daemon_main.o: daemon/main.c daemon/pipeline.h daemon/hal/sys.h daemon/hal/vi.h daemon/hal/isp.h daemon/hal/vpss.h daemon/hal/venc.h daemon/hal/audio.h daemon/hal/watchdog.h
	$(CC) $(DAEMON_CFLAGS) -c -o $@ $<

$(BUILD)/daemon_hal_sys.o: daemon/hal/sys.c daemon/hal/sys.h daemon/pipeline.h
	$(CC) $(DAEMON_CFLAGS) -c -o $@ $<

$(BUILD)/daemon_hal_vi.o: daemon/hal/vi.c daemon/hal/vi.h daemon/pipeline.h
	$(CC) $(DAEMON_CFLAGS) -c -o $@ $<

$(BUILD)/daemon_hal_isp.o: daemon/hal/isp.c daemon/hal/isp.h daemon/hal/watchdog.h daemon/pipeline.h
	$(CC) $(DAEMON_CFLAGS) -c -o $@ $<

$(BUILD)/daemon_hal_vpss.o: daemon/hal/vpss.c daemon/hal/vpss.h daemon/pipeline.h
	$(CC) $(DAEMON_CFLAGS) -c -o $@ $<

$(BUILD)/daemon_hal_venc.o: daemon/hal/venc.c daemon/hal/venc.h daemon/hal/watchdog.h daemon/pipeline.h
	$(CC) $(DAEMON_CFLAGS) -c -o $@ $<

$(BUILD)/daemon_hal_audio.o: daemon/hal/audio.c daemon/hal/audio.h daemon/pipeline.h
	$(CC) $(DAEMON_CFLAGS) -c -o $@ $<

$(BUILD)/daemon_hal_watchdog.o: daemon/hal/watchdog.c daemon/hal/watchdog.h daemon/pipeline.h
	$(CC) $(DAEMON_CFLAGS) -c -o $@ $<

# Link daemon
$(BUILD)/ipc_daemon: $(DAEMON_OBJS) $(BUILD)/rtsp_push.o
	$(CC) $(DAEMON_CFLAGS) -o $@ $(DAEMON_OBJS) $(BUILD)/rtsp_push.o \
		-L$(SDK_LIB) $(PIPELINE_LIBS) $(AUDIO_LIBS) $(RTSP_LIBS) \
		-Wl,-rpath,/usr/lib -Wl,-rpath,/progs/rec/00 \
		-Wl,--gc-sections -fno-use-linker-plugin \
		-Wl,--allow-shlib-undefined
	$(STRIP) $@

# RTSP push wrapper (shared between daemon and legacy pipeline_test)
$(BUILD)/rtsp_push.o: rtsp/rtsp_push.c rtsp/rtsp_push.h
	$(CC) $(CFLAGS) -I$(RTSP_INC_DIR) -Irtsp -c -o $@ $<

# ── Legacy pipeline_test (kept for backward compat, builds from monolith) ──
pipeline: check_toolchain $(BUILD)/pipeline_test
	@echo "Built: $(BUILD)/pipeline_test"
	@ls -la $(BUILD)/pipeline_test

$(BUILD)/pipeline_test: test/pipeline_test.c $(BUILD)/rtsp_push.o rtsp/rtsp_push.h
	$(CC) $(CFLAGS) -Irtsp -I$(RTSP_INC_DIR) -o $@ $< $(BUILD)/rtsp_push.o \
		-L$(SDK_LIB) $(PIPELINE_LIBS) $(AUDIO_LIBS) $(RTSP_LIBS) \
		-Wl,-rpath,/usr/lib -Wl,-rpath,/progs/rec/00 \
		-Wl,--gc-sections -fno-use-linker-plugin \
		-Wl,--allow-shlib-undefined
	$(STRIP) $@

# ── recv: Tiny TCP file receiver ───────────────────────────────────
recv: check_toolchain $(BUILD)/recv
	@echo "Built: $(BUILD)/recv"
	@ls -la $(BUILD)/recv

$(BUILD)/recv: ../tools/recv.c
	$(CC) $(CFLAGS) -static -Wl,--gc-sections -o $@ $<
	$(STRIP) $@

# ── reg_dump: I2C register dump utility ────────────────────────────
reg_dump: check_toolchain $(BUILD)/reg_dump
	@echo "Built: $(BUILD)/reg_dump"
	@ls -la $(BUILD)/reg_dump

$(BUILD)/reg_dump: test/reg_dump.c
	$(CC) $(CFLAGS) -static -Wl,--gc-sections -o $@ $<
	$(STRIP) $@

# ── AWB dump tool ──────────────────────────────────────────────────
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

# ── PQ bin library (vendored locally from PQ SDK) ─────────────────
pqbin: $(BUILD)/libbin.so
	@echo "Copied: $(BUILD)/libbin.so"
	@ls -la $(BUILD)/libbin.so

$(BUILD)/libbin.so: prebuilt/pq/libbin.so
	cp $< $@

# ── Clean ──────────────────────────────────────────────────────────
clean:
	rm -rf $(BUILD)
