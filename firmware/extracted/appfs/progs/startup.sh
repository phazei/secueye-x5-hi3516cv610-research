#!/bin/sh

export LD_LIBRARY_PATH=/progs/lib/:$LD_LIBRARY_PATH
#Here the mount cmd, It is because the fs is readonly, So for display of snapshot pic, mount another directory here.
export PATH=/progs/bin/:$PATH
#./bin/thttpd -d /progs/html/ -c /cgi-bin/\* -u root
#./bin/rtspServerForJovision /etc/conf.d/jovision/pipe/ live%d.264 3&

###################################################
#Here These commond, is working for the thttpd bug.
#
# The thtpd bug :
# When thttpd running, If set time year add 1, thttpd
# will do sth. and ipcam work bad.
#
#date -s 071309151991.00
#wget -P /tmp/ http://127.0.0.1/index.html
#rm /tmp/index.html
#hwclock -s -u

#if ! mountpoint /progs/html/cgi-bin/snapshot
#then
#	mount /tmp /progs/html/cgi-bin/snapshot
#fi

#不存在配置分区为18EP
#if [ ! -e /dev/mtdblock5 ];then
#	if [ -f /etc/init.d/rcS_old ] && [ -f /home/bashrc.sh ]; then
#		echo mySystem has been running
#	else
#		cp /progs/bin/mySystem /tmp/
#		/tmp/mySystem &
#	fi
#fi

#gkmm 0x112c0058  0x2 
#gkmm 0x112c005c  0x2

#cp ./bin/superb /tmp/

ulimit -s unlimited

if [ -f /etc/conf.d/debug.sh ]; then
	echo "./corefile/core.%e.%s.%p.%t" >/proc/sys/kernel/core_pattern
	/etc/conf.d/debug.sh &
else
	#insmod /home/ipc_drv/xm_wdt.ko  default_margin=60 nodeamon=1

	echo startup
	/tmp/appfs/progs/bin/superb &
	sleep 15
	cd /progs/rec/00/PQtool && ./PQTools.sh -c
	#cd /progs/PQtool && ./PQTools.sh -c
  	
	#telnetd & 
	#sleep 10 
	#fwdate=`date +%Y-%m-%d-%H%M%S`
	#mkdir /progs/rec/11/
	#mount -t vfat /dev/mmcblk0p1 /progs/rec/11/
	#/tmp/appfs/progs/bin/superb > /progs/rec/11/${fwdate}_$(echo $RANDOM).log 2>&1 &
	#cat /proc/umap/* > /progs/rec/00/${fwdate}_$(echo $RANDOM)_uamp.log 2>&1 &
	#cat /dev/logmpp > /progs/rec/00/${fwdate}_$(echo $RANDOM)_logmmp.log 2>&1 &
	#/tmp/superb 
	#reboot
fi

#superb退出后要清理thttpd和udhcpc
#killall thttpd udhcpc
