# Cross-compilation Makefile for SECUEYE X5 (Hi3516CV610 ARM Cortex-A7)
#
# Run from WSL:  cd /mnt/e/Projects/ipc_XMeye_camera/custom && make
#
# Prerequisites: The toolchain must be accessible at the TC path below.

# ── Toolchain ──────────────────────────────────────────────────────
TC_BASE = ../research/hi3516cv610_toolchain/gcc-20250305-arm-v01c02-linux-musleabi/arm-v01c02-linux-musleabi-gcc
CC      = $(TC_BASE)/bin/arm-v01c02-linux-musleabi-gcc
STRIP   = $(TC_BASE)/bin/arm-v01c02-linux-musleabi-strip
SYSROOT = $(TC_BASE)/target

# ── SDK paths (HIVIEW) ────────────────────────────────────────────
SDK_INC = ../research/HIVIEW/mod/mpp/3516c/inc/hisisdk
SDK_LIB = ../research/HIVIEW/mod/mpp/3516c/lib/hisisdk
SVP_INC = ../research/HIVIEW/mod/svp/3516c/common

# ── Compiler flags ─────────────────────────────────────────────────
# Target: ARM Cortex-A7, softfp (matches camera firmware ABI)
ARCH_FLAGS = -mcpu=cortex-a7 -mfloat-abi=softfp -mfpu=neon-vfpv4 -mthumb

# Warning flags
WARN_FLAGS = -Wall -Wextra -Wno-unused-parameter

# Optimization
OPT_FLAGS = -Os -ffunction-sections -fdata-sections

# Common CFLAGS
CFLAGS = $(ARCH_FLAGS) $(WARN_FLAGS) $(OPT_FLAGS) --sysroot=$(SYSROOT)

# Static linking for self-contained binaries (no shared lib dependencies)
LDFLAGS_STATIC = -static -Wl,--gc-sections

# SDK-linked builds (dynamic, needs camera's libs at runtime)
CFLAGS_SDK  = $(CFLAGS) -I$(SDK_INC) -I$(SVP_INC)
LDFLAGS_SDK = -L$(SDK_LIB) --sysroot=$(SYSROOT)

# ── Output directory ───────────────────────────────────────────────
BUILD = build
$(shell mkdir -p $(BUILD))

# ── Targets ────────────────────────────────────────────────────────
.PHONY: all clean httppost alarm_watcher check_toolchain

all: httppost alarm_watcher

check_toolchain:
	@if [ ! -x "$(CC)" ]; then \
		echo "ERROR: Cross-compiler not found at $(CC)"; \
		echo "Make sure you're running this from WSL and the toolchain path is correct."; \
		exit 1; \
	fi

# ── httppost: Minimal HTTP POST client ─────────────────────────────
# Usage on camera: ./httppost <url> <json_body>
# Example: ./httppost https://ntfy.sh/my-alerts '{"alarm":1}'
# Note: HTTPS requires linking with a TLS library. For HTTP-only,
# this raw socket implementation is sufficient and tiny.
httppost: check_toolchain $(BUILD)/httppost
	@echo "Built: $(BUILD)/httppost"
	@ls -la $(BUILD)/httppost

$(BUILD)/httppost: src/httppost.c
	$(CC) $(CFLAGS) $(LDFLAGS_STATIC) -o $@ $<
	$(STRIP) $@

# ── alarm_watcher: On-camera alarm monitoring daemon ───────────────
# Watches /tmp/superb.log for alarm patterns, writes events to SD card,
# optionally fires HTTP webhooks via httppost binary.
alarm_watcher: check_toolchain $(BUILD)/alarm_watcher
	@echo "Built: $(BUILD)/alarm_watcher"
	@ls -la $(BUILD)/alarm_watcher

$(BUILD)/alarm_watcher: src/alarm_watcher.c
	$(CC) $(CFLAGS) $(LDFLAGS_STATIC) -o $@ $<
	$(STRIP) $@

# ── Clean ──────────────────────────────────────────────────────────
clean:
	rm -rf $(BUILD)
