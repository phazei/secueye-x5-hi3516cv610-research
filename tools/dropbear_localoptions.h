/* dropbear_localoptions.h - Custom build options for SECUEYE X5 camera
 *
 * Target: Hi3516CV610 (Cortex-A7, musl, 36MB usable RAM)
 * Usage: LAN-only, SSH server + SCP file transfer + key generation
 * Build: static multi-binary (dropbearmulti -> dropbear, scp, dropbearkey)
 *
 * Copy this file as 'localoptions.h' in the dropbear build directory.
 * See default_options.h for all available options.
 */

/* --- Disable post-quantum key exchange (LAN-only, not needed) --- */
#define DROPBEAR_SNTRUP761 0
#define DROPBEAR_MLKEM768 0

/* --- Disable unused auth/key types --- */
#define DROPBEAR_SK_KEYS 0          /* No U2F security keys */
#define DROPBEAR_DSS 0              /* Already default, be explicit */
#define DROPBEAR_RSA_SHA1 0         /* Already default */

/* --- Disable unused features --- */
#define DROPBEAR_SVR_AGENTFWD 0     /* No SSH agent forwarding */
#define DROPBEAR_CLI_AGENTFWD 0
#define INETD_MODE 0                /* Standalone daemon only */
#define DROPBEAR_REEXEC 0           /* Save fork overhead; ASLR irrelevant on LAN */
#define DROPBEAR_X11FWD 0           /* Already default */
#define DO_MOTD 0                   /* No message of the day */

/* --- Keep these enabled (camera needs them) --- */
/* DROPBEAR_ED25519 1              -- our primary key type */
/* DROPBEAR_ECDSA 1                -- existing host key */
/* DROPBEAR_RSA 1                  -- compatibility */
/* DROPBEAR_CHACHA20POLY1305 1     -- fast on Cortex-A7 (no AES-NI) */
/* DROPBEAR_AES128 1               -- standard fallback */
/* DROPBEAR_AES256 1               -- standard fallback */
/* DROPBEAR_SVR_PASSWORD_AUTH 1    -- password fallback */
/* DROPBEAR_SVR_PUBKEY_AUTH 1      -- primary auth */
/* NON_INETD_MODE 1                -- standalone daemon */

/* --- Paths adjusted for camera layout --- */
#define SFTPSERVER_PATH "/progs/rec/00/ipc_drv/sftp-server"
#define DROPBEAR_PATH_SSH_PROGRAM "/progs/rec/00/ipc_drv/dbclient"
#define DEFAULT_PATH "/usr/sbin:/usr/bin:/sbin:/bin:/progs/rec/00/ipc_drv"
#define DEFAULT_ROOT_PATH "/usr/sbin:/usr/bin:/sbin:/bin:/progs/rec/00/ipc_drv"
