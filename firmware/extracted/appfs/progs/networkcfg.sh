#!/bin/sh

source /home/variable
STORAGE_PATH=/etc/conf.d/syscfg/network
default_interface()
{
	echo "No interface.cfg, build it now"
	echo "#iface is the name of the dev, such as interface0/ppp0/ra0">>/etc/network/interface.cfg
	echo "iface=eth0">>/etc/network/interface.cfg
	echo "#inet=static/dhcp/ppp/wifi">>/etc/network/interface.cfg
	if [ $NET_DHCP == 1 ];then
		echo "inet=dhcp">>/etc/network/interface.cfg
	else
		echo "inet=static">>/etc/network/interface.cfg
	fi
	echo "">>/etc/network/interface.cfg
	echo "ip=192.168.1.123">>/etc/network/interface.cfg
    	echo "netmask=255.255.255.0">>/etc/network/interface.cfg
	echo "gateway=192.168.1.1">>/etc/network/interface.cfg
	echo "dns=8.8.8.8">>/etc/network/interface.cfg
}

[ -d $STORAGE_PATH ] || mkdir -p $STORAGE_PATH
[ -d /etc/network ] && rm -rf /etc/network
[ -d /etc/ppp ] && rm -rf /etc/ppp
ln -s $STORAGE_PATH/ /etc/network
ln -s $STORAGE_PATH/ /etc/ppp
[ -f $STORAGE_PATH/pppoe.conf ] || cp /etc/ppp/* $STORAGE_PATH
[ -d /var/run ] || mkdir /var/run
[ -f /etc/network/interface.cfg ] || default_interface

##################################################
# interface setting now

ifconfig eth0 up
ifconfig lo up

#删除默认路由，避免设置IP时，多出来很多默认路由
route del default
route del 255.255.255.255

. /etc/network/interface.cfg
echo interface info here...
echo iface   = $iface
echo inet    = $inet
echo ip      = $ip
echo netmask = $netmask
echo gateway = $gateway
echo dns     = $dns

#添加广播路由
add_broadcast_route()
{
	route add -net 255.255.255.255 netmask 255.255.255.255 dev $iface
	route add -net 224.0.0.0 netmask 240.0.0.0 dev $iface
}

killall udhcpc
killall wpa_supplicant

#ifconfig eth0:0 169.254.0.1
#set interface ip
case $inet in
	dhcp)
		echo iface $iface udhcpc 
		udhcpc -i $iface -q && add_broadcast_route &
		ifconfig $iface mtu 1460
		;;
	static)
		echo iface $iface static 
		ifconfig $iface $ip netmask $netmask
		route add default gw $gateway
		echo "nameserver $dns">/etc/resolv.conf
		echo "`ifconfig eth0`" > /var/run/jvnetstatus
		echo "interface, static ip..."
		add_broadcast_route
		ifconfig $iface mtu 1460
		;;
	ppp)
		echo iface $iface pppoe
		#ifconfig eth0 $ip netmask $netmask
		#For hisi 3507, 这里会有一个eth0 down, eth0 up的动作（不是PPPOE导致），所以稍等，让过它去。
		sleep 1
		pppoe-start
		add_broadcast_route
		;;
	wifi)
		echo iface $iface wifi
	#	ifconfig eth0 down
		ifconfig $iface up
		sleep 1
		#/wifi/wpa_supplicant -B -i$iface -c /etc/network/wpa_supplicant.conf -Dwext
		/home/wifi/wpa_supplicant -Dnl80211 -i$iface -c /etc/network/wpa_supplicant.conf &
		udhcpc -i $iface -q &
		sleep 2
		add_broadcast_route
		;;
	*)
		echo inet error: $inet
		default_interface
		add_broadcast_route
		;;
esac
