/*
 * alarm_watcher.c -- On-camera alarm monitoring daemon for SECUEYE X5
 *
 * Monitors /tmp/superb.log in real-time for alarm event patterns.
 * When an alarm is detected:
 *   1. Writes a structured event line to an alarm log on the SD card
 *   2. Optionally executes a webhook command (e.g., ./httppost)
 *
 * Designed to run as a background daemon on the camera itself, launched
 * from debug.sh or an SD card startup script.
 *
 * Usage:
 *   ./alarm_watcher                          # Log alarms to SD card only
 *   ./alarm_watcher -w "./httppost ntfy.sh 80 /my-cam 'ALARM'"
 *                                            # Log + fire webhook
 *   ./alarm_watcher -l /progs/rec/00/alarms.log
 *                                            # Custom log path
 *   ./alarm_watcher -c 15                    # 15 second cooldown
 *
 * Alarm detection patterns (from testing 2026-05-05):
 *   - "start maudio_speaker"  -> Voice alarm prompt fired (PRIMARY)
 *   - "Create snap"           -> Alarm snapshot captured
 *   - "goto preset"           -> PTZ preset triggered by alarm
 *
 * Build:
 *   arm-v01c02-linux-musleabi-gcc -static -Os -o alarm_watcher alarm_watcher.c
 *
 * Deploy:
 *   Copy to SD card at /progs/rec/00/custom/alarm_watcher
 *   Add to debug.sh: /progs/rec/00/custom/alarm_watcher -w "..." &
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include <signal.h>
#include <sys/stat.h>
#include <errno.h>

#define SUPERB_LOG      "/tmp/superb.log"
#define DEFAULT_ALARM_LOG "/progs/rec/00/alarms.log"
#define LINE_BUF_SIZE   2048
#define DEFAULT_COOLDOWN 10  /* seconds between distinct alarm events */
#define MAX_WEBHOOK_CMD  1024

/* Alarm trigger patterns -- order matters (checked first to last) */
static const struct {
    const char *pattern;
    const char *event_type;
} triggers[] = {
    { "start maudio_speaker", "ALARM_VOICE" },
    { "Create snap",          "ALARM_SNAPSHOT" },
    { "goto preset",          "ALARM_PRESET" },
    { NULL, NULL }
};

static volatile int running = 1;
static int alarm_count = 0;
static time_t last_alarm_time = 0;

static void signal_handler(int sig)
{
    (void)sig;
    running = 0;
}

static void get_timestamp(char *buf, int bufsize)
{
    time_t now = time(NULL);
    struct tm *tm = localtime(&now);
    snprintf(buf, bufsize, "%04d-%02d-%02d %02d:%02d:%02d",
             tm->tm_year + 1900, tm->tm_mon + 1, tm->tm_mday,
             tm->tm_hour, tm->tm_min, tm->tm_sec);
}

static void log_alarm(const char *logpath, int num, const char *event_type,
                       const char *line)
{
    char ts[32];
    get_timestamp(ts, sizeof(ts));

    FILE *f = fopen(logpath, "a");
    if (f) {
        fprintf(f, "[%s] ALARM #%d %s: %s\n", ts, num, event_type, line);
        fflush(f);
        fclose(f);
    }

    /* Also print to stdout for debugging */
    fprintf(stdout, "[%s] *** ALARM #%d %s ***\n", ts, num, event_type);
    fflush(stdout);
}

static void fire_webhook(const char *webhook_cmd, int alarm_num,
                          const char *event_type)
{
    if (!webhook_cmd || webhook_cmd[0] == '\0')
        return;

    /* Build command with alarm info substituted */
    char cmd[MAX_WEBHOOK_CMD * 2];
    char ts[32];
    get_timestamp(ts, sizeof(ts));

    /* Simple substitution: replace {NUM} and {TYPE} and {TIME} if present */
    const char *src = webhook_cmd;
    char *dst = cmd;
    char *end = cmd + sizeof(cmd) - 1;

    while (*src && dst < end) {
        if (strncmp(src, "{NUM}", 5) == 0) {
            dst += snprintf(dst, end - dst, "%d", alarm_num);
            src += 5;
        } else if (strncmp(src, "{TYPE}", 6) == 0) {
            dst += snprintf(dst, end - dst, "%s", event_type);
            src += 6;
        } else if (strncmp(src, "{TIME}", 6) == 0) {
            dst += snprintf(dst, end - dst, "%s", ts);
            src += 6;
        } else {
            *dst++ = *src++;
        }
    }
    *dst = '\0';

    /* Execute in background (don't block the log reader) */
    char bg_cmd[sizeof(cmd) + 4];
    snprintf(bg_cmd, sizeof(bg_cmd), "%s &", cmd);
    system(bg_cmd);
}

static void usage(const char *prog)
{
    fprintf(stderr,
        "Usage: %s [options]\n"
        "\n"
        "Monitor superb.log for alarm events and log/notify.\n"
        "\n"
        "Options:\n"
        "  -l <path>     Alarm log file (default: %s)\n"
        "  -w <command>  Webhook command to run on alarm\n"
        "                Placeholders: {NUM} {TYPE} {TIME}\n"
        "  -c <seconds>  Cooldown between alarms (default: %d)\n"
        "  -d            Daemonize (fork to background)\n"
        "  -h            Show this help\n"
        "\n"
        "Examples:\n"
        "  %s -w './httppost ntfy.sh 80 /my-cam \"Alarm #{NUM} ({TYPE})\"'\n"
        "  %s -d -c 30 -l /progs/rec/00/alarms.log\n",
        prog, DEFAULT_ALARM_LOG, DEFAULT_COOLDOWN, prog, prog);
}

int main(int argc, char *argv[])
{
    const char *alarm_log = DEFAULT_ALARM_LOG;
    const char *webhook_cmd = NULL;
    int cooldown = DEFAULT_COOLDOWN;
    int daemonize = 0;
    int opt;

    while ((opt = getopt(argc, argv, "l:w:c:dh")) != -1) {
        switch (opt) {
        case 'l':
            alarm_log = optarg;
            break;
        case 'w':
            webhook_cmd = optarg;
            break;
        case 'c':
            cooldown = atoi(optarg);
            if (cooldown < 1) cooldown = 1;
            break;
        case 'd':
            daemonize = 1;
            break;
        case 'h':
            usage(argv[0]);
            return 0;
        default:
            usage(argv[0]);
            return 1;
        }
    }

    /* Set up signal handlers */
    signal(SIGTERM, signal_handler);
    signal(SIGINT, signal_handler);

    /* Daemonize if requested */
    if (daemonize) {
        pid_t pid = fork();
        if (pid < 0) {
            perror("fork");
            return 1;
        }
        if (pid > 0) {
            /* Parent exits */
            printf("alarm_watcher started as PID %d\n", pid);
            return 0;
        }
        /* Child continues */
        setsid();
        /* Redirect stdout/stderr to alarm log for daemon mode */
        freopen("/dev/null", "r", stdin);
    }

    /* Log startup */
    {
        char ts[32];
        get_timestamp(ts, sizeof(ts));
        FILE *f = fopen(alarm_log, "a");
        if (f) {
            fprintf(f, "[%s] alarm_watcher started (cooldown=%ds, webhook=%s)\n",
                    ts, cooldown, webhook_cmd ? webhook_cmd : "none");
            fclose(f);
        }
        fprintf(stdout, "[%s] alarm_watcher monitoring %s\n", ts, SUPERB_LOG);
        fprintf(stdout, "[%s] alarm log: %s\n", ts, alarm_log);
        if (webhook_cmd)
            fprintf(stdout, "[%s] webhook: %s\n", ts, webhook_cmd);
        fflush(stdout);
    }

    /* Wait for superb.log to exist */
    while (running) {
        struct stat st;
        if (stat(SUPERB_LOG, &st) == 0)
            break;
        sleep(2);
    }

    /* Open the log file and seek to end (only watch new lines) */
    FILE *logfile = fopen(SUPERB_LOG, "r");
    if (!logfile) {
        fprintf(stderr, "alarm_watcher: cannot open %s: %s\n",
                SUPERB_LOG, strerror(errno));
        return 2;
    }
    fseek(logfile, 0, SEEK_END);

    /* Main monitoring loop */
    char line[LINE_BUF_SIZE];

    while (running) {
        if (fgets(line, sizeof(line), logfile) != NULL) {
            /* Strip trailing newline */
            int len = strlen(line);
            while (len > 0 && (line[len-1] == '\n' || line[len-1] == '\r'))
                line[--len] = '\0';

            if (len == 0)
                continue;

            /* Check alarm patterns */
            for (int i = 0; triggers[i].pattern != NULL; i++) {
                if (strstr(line, triggers[i].pattern) != NULL) {
                    time_t now = time(NULL);

                    /* Cooldown check */
                    if (now - last_alarm_time >= cooldown) {
                        alarm_count++;
                        last_alarm_time = now;

                        log_alarm(alarm_log, alarm_count,
                                  triggers[i].event_type, line);
                        fire_webhook(webhook_cmd, alarm_count,
                                     triggers[i].event_type);
                    }
                    break;  /* Only match first pattern per line */
                }
            }
        } else {
            /* EOF -- log file may have been truncated (logrotate) or
               we're caught up. Clear EOF flag and sleep briefly. */
            clearerr(logfile);

            /* Check if file was truncated (size decreased) */
            struct stat st;
            long pos = ftell(logfile);
            if (stat(SUPERB_LOG, &st) == 0 && st.st_size < pos) {
                /* File was truncated, seek to beginning */
                fseek(logfile, 0, SEEK_SET);
            }

            usleep(200000);  /* 200ms poll interval */
        }
    }

    fclose(logfile);

    /* Log shutdown */
    {
        char ts[32];
        get_timestamp(ts, sizeof(ts));
        FILE *f = fopen(alarm_log, "a");
        if (f) {
            fprintf(f, "[%s] alarm_watcher stopped (%d alarms detected)\n",
                    ts, alarm_count);
            fclose(f);
        }
    }

    return 0;
}
