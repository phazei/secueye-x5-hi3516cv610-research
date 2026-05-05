#!/bin/sh

[ -d /var/udisk ] && umount /var/udisk
if [ -b /dev/mmcblk0p1 ];then
	[ -d /var/udisk ] || mkdir /var/udisk
	mount -t vfat /dev/mmcblk0p1 /var/udisk
	
	if [ -d /var/udisk/seculinkIdRecycle ]; then
		echo recycling id
		if [ -f /var/udisk/seculinkIdRecycle/recycle_ali.sh ]; then
			echo recycling aliyun lic
			chmod 777 /var/udisk/seculinkIdRecycle/recycle_ali.sh
			source /var/udisk/seculinkIdRecycle/recycle_ali.sh
		fi
	fi
	
	if [ -f /var/udisk/seculinkMac/seculinkMAC.txt ]; then
		echo upate mac config
		cd /var/udisk/seculinkMac/
		if grep -q 'A6:88:E0' seculinkMAC.txt  
		then
			mac=$(awk 'NR==1 {printf $1}' seculinkMAC.txt)
			echo $mac
			sed -i '1d' seculinkMAC.txt
			echo "$mac" >/etc/conf.d/fixed/mac.cfg
			echo USED$mac >>usedmac.txt
			echo "set mac" >>/etc/conf.d/seted_mac
		else
			echo no mac
			[ -f /etc/conf.d/seted_mac ] && rm -f /etc/conf.d/seted_mac
		fi
	else
		echo no mac config
		[ -f /etc/conf.d/seted_mac ] && rm -f /etc/conf.d/seted_mac
	fi
	if [ -d /var/udisk/seculinkDanaleUid/danaleUid/ ]; then
		echo update danale config file
		cd /var/udisk/seculinkDanaleUid/danaleUid/
		if  find *.conf > /dev/null  
		then
			find ./ -type f ! -name '*.conf' -exec rm -f {} \;
			filename=$(find *.conf|head -1)
			echo $filename
			if [ -f  ../alreadyUsedUid/"$filename.used" ];
			then
				echo danaleID file used
				rm -f $filename
			else
				cp $filename /etc/conf.d/danale.conf
				echo "set danale id">>/etc/conf.d/seted_id 
				mv $filename ../alreadyUsedUid/ || ( rm -f /etc/conf.d/danale.conf;rm -f /etc/conf.d/seted_id )
				find ../alreadyUsedUid/*.conf -exec mv {} {}.used \;
			fi
		else
			[ -f /etc/conf.d/seted_id ] && rm -f /etc/conf.d/seted_id
			echo no danale conf file
		fi
	else
		echo no danale config
		[ -f /etc/conf.d/seted_id ] && rm -f /etc/conf.d/seted_id
	fi
	
	if [ -d /var/udisk/seculinkAliyunUid/aliyunUid/ ]; then
		echo update aliyun config file
		cd /var/udisk/seculinkAliyunUid/aliyunUid/
		if  find *.conf > /dev/null  
		then
			find ./ -type f ! -name '*.conf' -exec rm -f {} \;
			filename=$(find *.conf|head -1)
			echo $filename
			if [ -f  ../alreadyUsedUid/"$filename.used" ];
			then
				echo aliyunUid file used
				rm -f $filename
			else
				cp $filename /etc/conf.d/aliyun.conf
				echo "set aliyun id">>/etc/conf.d/seted_id 
				mv $filename ../alreadyUsedUid/ || ( rm -f /etc/conf.d/aliyun.conf;rm -f /etc/conf.d/seted_id )
				find ../alreadyUsedUid/*.conf -exec mv {} {}.used \;
			fi
		else
			if find *.txt > /dev/null
			then
				find ./ -type f ! -name '*.txt' -exec rm -f {} \;
				filename=$(find *.txt|head -1)
				echo $filename
				if [ -f  ../alreadyUsedUid/"$filename.used" ];
				then
					echo aliyunUid file used
					rm -f $filename
				else
					cp $filename /etc/conf.d/lic.bin
					echo "set aliyun id">>/etc/conf.d/seted_id 
					mv $filename ../alreadyUsedUid/ || ( rm -f /etc/conf.d/lic.bin;rm -f /etc/conf.d/seted_id )
					find ../alreadyUsedUid/*.txt -exec mv {} {}.used \;
				fi
			else
				[ -f /etc/conf.d/seted_id ] && rm -f /etc/conf.d/seted_id
				echo no aliyun conf file
			fi
		fi
	else
		echo no aliyun config
		[ -f /etc/conf.d/seted_id ] && rm -f /etc/conf.d/seted_id
	fi
	
	if [ -d /var/udisk/seculinkVoice ]; then
		echo modify voice file
		cd /var/udisk/seculinkVoice
		if find language/*.711 > /dev/null ;
		then
			mkdir tmp
			langpath=/etc/conf.d/fixed/voice/language
			mv $langpath/* ./tmp/
			cp language/*.711 $langpath/ || ( rm $langpath/*; cp ./tmp/*.711 $langpath/ )
			rm -rf ./tmp 
			echo modify voice done
		else
			echo no voice file
		fi
		if find common/*.711 > /dev/null ;
		then
			mkdir tmp2
			langpath2=/etc/conf.d/fixed/voice/common
			mv $langpath2/* ./tmp2/
			cp common/*.711 $langpath2/ || ( rm $langpath2/*; cp ./tmp2/*.711 $langpath2/ )
			rm -rf ./tmp2 
			echo modify voice done
		else
			echo no voice file
		fi
	fi
	
	if [ -d /var/udisk/seculinkHardware ]; then
		echo modify hwconfig file
		cd /var/udisk/seculinkHardware
		if find hwconfig.cfg /dev/null ;
		then
			cp hwconfig.cfg /etc/conf.d/fixed/
			echo modify hwconfig done
		else
			echo no hwconfig file
		fi
	fi
	if [ -d /var/udisk/wifi ]; then
		echo modify wifi password file
		cd /var/udisk/wifi
		if find wpa_supplicant.conf /dev/null ;
		then
			cp wpa_supplicant.conf /etc/conf.d/syscfg/network/
			echo modify wificonfig done
		else
			echo no wificonfig file
		fi
	else
		if [ -f /var/udisk/secu_wifi_test.txt ]; then
			echo Update test wifi
			cd /var/udisk/
			cp secu_wifi_test.txt /etc/conf.d/syscfg/network/
		fi
	fi

	if [ -f /var/udisk/UserMallCfg.txt ]; then
		echo upate user mall 
		FILE="/var/udisk/UserMallCfg.txt"
		KV_IFS="="
		SPCL_CMNT="#"
		while read LINE
		do
			LINE=`echo $LINE | sed 's/[ \r]*$//g'`
			result1=$(echo $LINE | grep "${KV_IFS}")
			result2=$(echo $LINE | grep "${SPCL_CMNT}")
			if [[ "$result1" != "" ]] && [[ "$result2" == "" ]];then
				K=${LINE%%$KV_IFS*}
				V=${LINE##*$KV_IFS}
				if [[ $K == "url" ]];then
					jstr=$(printf "{\"%s\":\"%s\"}" ${K} ${V})
					echo $jstr
					echo $jstr > /etc/conf.d/UserMallCfg.json
					echo "set user mall url">>/etc/conf.d/seted_id
					break;
				fi
			fi
			
		done < $FILE
	fi
	
	for filename in `find /var/udisk/ -maxdepth 1 -type f -name '*.json'`
	do
		echo upate hwinfo
		cp $filename /etc/conf.d/hwinfo.json
		break;
	done
	
else
	echo no sd device
	[ -f /etc/conf.d/seted_id ] && rm -f /etc/conf.d/seted_id
	[ -f /etc/conf.d/seted_mac ] && rm -f /etc/conf.d/seted_mac
fi
cd /
[ -d /var/udisk ] && umount /var/udisk
