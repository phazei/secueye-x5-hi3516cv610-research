#! /bin/sh

mount_as_tmpfs	/etc/        
mount_as_tmpfs	/progs/rec
NET_STAT=0

export LD_LIBRARY_PATH="${LD_LIBRARY_PATH}:/home/libs"

#设置保留内存大小
echo 1024 > /proc/sys/vm/min_free_kbytes
#echo 4 > /proc/sys/kernel/printk
#设置栈大小
#ulimit -s 8192
ulimit -s unlimited 
ulimit -c unlimited

#get /dev/mtdblockN with mtd name
getmtd()
{
	MTDNAME=$1
	temp=`grep $MTDNAME /proc/mtd`
	temp=${temp%%:*}
	echo ${temp/mtd/\/dev\/mtdblock}
}

#mtd1: 00480000 00010000 "appfs"
CONFIG_MTD=`getmtd \"configfs\"`
CONFIG_MTD2=`getmtd \"config\"`
echo $CONFIG_MTD

[ -d /etc/conf.d ] || mkdir /etc/conf.d
mount -t jffs2 $CONFIG_MTD /etc/conf.d/ || mount -t jffs2 $CONFIG_MTD2 /etc/conf.d/
[ -d /etc/conf.d/syscfg ] || mkdir /etc/conf.d/syscfg
[ -d /etc/conf.d/syscfg/network ] || mkdir /etc/conf.d/syscfg/network
[ -d /etc/conf.d/fixed ] || mkdir /etc/conf.d/fixed

source /home/variable
source /etc/conf.d/fixed/hwconfig.cfg

######BSP##############

if [[ $PART && $PART == 16 ]]; then
	RES_MTD=`getmtd \"resfs\"`
	echo $RES_MTD
	mkdir /tmp/resfs
	mount -t squashfs $RES_MTD /tmp/resfs
	
	mount /tmp/resfs/wifi /home/wifi
	if [ $WIFI == "aic8800_single" ]; then
		mount /tmp/resfs/wifi/aic8800_single /home/wifi/aic8800
	elif [ $WIFI == "aic8800_dual" ]; then
		mount /tmp/resfs/wifi/aic8800_dual /home/wifi/aic8800
	fi

#	if [[ 1 == 1 ]];then
		mount /tmp/resfs/ble /home/ble
#	fi
	if [[ $IVP == 1 ]];then
		mount /tmp/resfs/ivp /home/ivp
	fi

	if [[ $AIISP == 1 ]];then
		mount /tmp/resfs/aiisp /home/aiisp
	fi

	if [[ $NPU == 1 ]];then
		mount /tmp/resfs/npu /home/npu
	fi

	if [[ $USB == 1 ]];then
		mount /tmp/resfs/usb /home/ipc_drv/usb
		
		if [[ $NET4G == 1 ]];then
			mount /tmp/resfs/4g /home/ipc_drv/4g
		fi
		
		if [[ $UVC_TYPE ]];then
			mount /tmp/resfs/uvc/ /home/ipc_drv/uvc
		fi
	fi

#	mount /tmp/resfs/sensor/${SENSOR}/pqbin /home/pq_bin
	mount /tmp/resfs/sensor /home/sensor
	
	mount /tmp/resfs/voice/common /home/voice/common
	if [ ! -z $lang ];then
		mount /tmp/resfs/voice/language/${lang} /home/voice/language
	else
		mount /tmp/resfs/voice/language/${language} /home/voice/language
	fi
fi 

if [[ $USB == 1 ]];then
	echo "insmod usb controller ko"

	if [[ $NET4G == 1 ]];then
		echo "insmod 4g ko"

	fi
	if [[ $UVC_TYPE ]];then
		echo "insmod uvc v4l2 ko"

		if [[ $UVC_TYPE == 1 ]];then
			echo "insmod uvc slave ko"

		elif [[ $UVC_TYPE == 2 ]];then
			echo "insmod uvc host ko"
		fi
	fi
fi

#insmod /home/ipc_drv/extdrv/hi_gpio.ko
if [ -f /home/ipc_drv/extdrv/motor_advance.ko ]; then
	echo "ins moto"
	insmod /home/ipc_drv/extdrv/motor_advance.ko
fi

#set id  
if [ -f /home/aliyun.conf ]; then
	if [ ! -f /etc/conf.d/aliyun.conf ]; then
		echo "set aliyun id"
		cp /home/aliyun.conf /etc/conf.d/aliyun.conf
	fi
fi
/progs/updateID.sh

########MEDIA############

cd /home/ipc_drv

gkNetStat()
{
	reg=$1
	val=`devmem $reg`
	echo $((($val >> 1) & 0x1))
}
		
getSysMem()
{	
	#parse cmdline
	mem=40
	bootargs=$(cat /proc/cmdline)
	for i in $bootargs
	do
		if [[ "$(echo $i | grep "mem")" != "" ]];then	
			mem=${i##*=}
			mem=${mem%%M*}
			break
		fi
	done
	echo $mem
}

echo SENSOR=${SENSOR} > /etc/conf.d/syscfg/sensor.sh
#echo SENSOR0=${SENSOR0} >> /etc/conf.d/syscfg/sensor.sh
echo SENSOR1=${SENSOR1} >> /etc/conf.d/syscfg/sensor.sh

#/home/loadmpp.sh 
#./load${chip} -i -sensor ${SENSOR} -osmem ${OSMEM} -pcb ${PCBNAME}
#example: $0 -i -sensor0 sc4336p -sensor1 sc4336p -mmz_start 0x42000000 -mmz_size 32M -ir_auto 1\n"

mem_total=64
mem_start=0x40000000 
if [[ $chip == "hi3516cv610_10b" ]]; then
	echo $chip
	mem_total=64
elif [[ $chip == "hi3516cv610_20s" ]]; then
	echo $chip
	mem_total=128
fi

getSysMem()
{	
	#parse cmdline
	mem=37
	bootargs=$(cat /proc/cmdline)
	for i in $bootargs
	do
		if [[ "$(echo $i | grep "mem")" != "" ]];then	
			mem=${i##*=}
			mem=${mem%%M*}
			break
		fi
	done
	echo $mem
}

os_mem_size=`getSysMem`

mmz_start=`echo "$mem_start $os_mem_size"  |
awk 'BEGIN { temp = 0; }
{
		temp = $1/1024/1024 + $2;
}
END { printf("0x%x00000\n", temp); }'`

mmz_size=`echo "$mem_total $os_mem_size"  |
awk 'BEGIN { temp = 0; }
{
		temp = $1 - $2;
}
END { printf("%dM\n", temp); }'`

echo "mem_total: $mem_total, os_mem_size: $os_mem_size, mmz_start: $mmz_start, mmz_size: $mmz_size"

if [[ -n "${VENC_ABILITY+x}" ]]; then
	echo venc ${VENC_ABILITY}
	./loadhi3516cv610 -i -sensor0 $SENSOR -sensor1 $SENSOR1 -mmz_start $mmz_start -mmz_size $mmz_size -chip $chip -venc $VENC_ABILITY
else
	./loadhi3516cv610 -i -sensor0 $SENSOR -sensor1 $SENSOR1 -mmz_start $mmz_start -mmz_size $mmz_size -chip $chip
fi
bspmm 0x11091400 0x00
insmod /home/ipc_drv/ot_adc.ko

#############NET WORK###########

#echo "256000" > /proc/sys/net/core/wmem_default	#500KB默认缓存
#echo "512000" > /proc/sys/net/core/wmem_max	#1000KB最大缓存
#echo "512000" > /proc/sys/net/core/rmem_default	#500KB默认缓存
#echo "1024000" > /proc/sys/net/core/rmem_max	#1000KB最大缓存

if [[ $PART && $PART == 16 ]];then
	source /home/lswifi
	if [ -f "/tmp/wificfg" ]; then
		source /tmp/wificfg
		echo $WIFI
	fi
fi
if [[ $NET_STAT != 1 && $WIFI != "none" ]];then
	echo "insmod wifi ko"
	
	insmod /home/wifi/cfg80211.ko
	insmod /home/wifi/libarc4.ko
	insmod /home/wifi/mac80211.ko
	if [[ $WIFI == "rtl8188" ]]; then
		[ ! -f /home/wifi/rtl8188f.ko ] || insmod /home/wifi/rtl8188f.ko
	elif [[ $WIFI == "rtl8733bu" ]]; then
		[ ! -f /home/wifi/rtl8733bu.ko ] || insmod /home/wifi/rtl8733bu.ko
	elif [[ $WIFI == "rtl8733bs" ]]; then
		[ ! -f /home/wifi/rtl8733bs.ko ] || insmod /home/wifi/rtl8733bs.ko
	elif [[ $WIFI == "ws73v100" ]]; then
		echo "insmod $WIFI"
		[ ! -f /home/wifi/ws73v100/plat_soc.ko ] || insmod /home/wifi/ws73v100/plat_soc.ko
		[ ! -f /home/wifi/ws73v100/wifi_soc.ko ] || insmod /home/wifi/ws73v100/wifi_soc.ko

		[ ! -f /home/ble/crc16.ko ] || insmod /home/ble/crc16.ko
		[ ! -f /home/ble/bluetooth.ko ] || insmod /home/ble/bluetooth.ko
		[ ! -f /home/ble/ble_soc.ko ] || insmod /home/ble/ble_soc.ko
		[ ! -f /home/ble/bin/bluetoothd ] || /home/ble/bin/bluetoothd -n &

	elif [[ $WIFI == "atbm6x3x" ]]; then
		[ ! -f /home/wifi/atbm6x3x_wifi_usb.ko ] || insmod /home/wifi/atbm6x3x_wifi_usb.ko wifi_bt_comb=1
	elif [[ $WIFI == "atbm6x6x" ]]; then
		[ ! -f /home/wifi/ATBM6x6x_wifi_usb.ko ] || insmod /home/wifi/ATBM6x6x_wifi_usb.ko wifi_bt_comb=1
	elif [[ $WIFI == "hi3881" ]]; then
		[ ! -f /home/wifi/hi3881.ko ] || insmod /home/wifi/hi3881.ko; 
	elif [[ $WIFI == "aic8800_dual" || $WIFI == "aic8800_single" ]]; then
		[ ! -f /home/wifi/aic8800_bsp.ko ] || insmod /home/wifi/aic8800_bsp.ko
		[ ! -f /home/wifi/aic8800/aic8800_fdrv.ko ] || insmod /home/wifi/aic8800/aic8800_fdrv.ko	
	fi
fi

#setMAC
cp /etc/conf.d/fixed/mac.cfg /etc/conf.d/syscfg/network/mac.cfg
if [ -f /etc/conf.d/syscfg/network/mac.cfg ] ; then
	. /etc/conf.d/syscfg/network/mac.cfg
fi

#udevstart

#/usr/sbin/setMAC

#配置网络参数
/progs/networkcfg.sh
#telnetd&
#hwclock -s -u

if [[ ${MCU_SUPPORT:-0} -eq 1 ]]; then
	echo "quick mux pin uart2 and disable eth and fephy reset clock"
	bspmm 0x1113004c 0x1105	 #uart tx
	bspmm 0x11130048 0x1105  #uart rx
	bspmm 0x110137cc 0x9	 #disable eth and fephy reset
fi
#######################APP##################
#cd /progs
#cp /progs/bin/mySystem /tmp/
#/tmp/mySystem &
echo "3 4 1 7" > /proc/sys/kernel/printk
echo "/tmp/core_$(echo $RANDOM).%e.%s.%p.%t" >/proc/sys/kernel/core_pattern
/tmp/appfs/progs/bin/mySystem &
#./startup.sh &
/progs/startup.sh &

