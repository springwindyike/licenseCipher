#!/usr/bin/env bash
#this script is for EIM install
#although install with or without NTX
#author: MaiLingFeng
#date: 2013-01-15
#modify records:
# -change to cover install gzblicense	2013-01-28	MaiLingFeng
# -redirect release log to $APP_HOME/logs/install.log	2014-10-30	x-Wang

APP_NAME="gzblicense"

[ -f /etc/sysconfig/gzb/functions/functions ] && . /etc/sysconfig/gzb/functions/functions
[ -f /etc/sysconfig/gzb/gzb_common ] && . /etc/sysconfig/gzb/gzb_common
[ -f /etc/sysconfig/gzb/$APP_NAME ] && . /etc/sysconfig/gzb/$APP_NAME

MYSQL_HOST=${MYSQL_HOST:-"127.0.0.1"}
MYSQL_COMMAND=${MYSQL_COMMAND:-"mysql"}
MYSQL_USER=${MYSQL_USER:-"root"}
MYSQL_PWD=${MYSQL_PWD:-"123456abcdef"}
MYSQL_PORT=${MYSQL_PORT:-3306}

APP_HOME=${APP_HOME:-"/home/$APP_NAME"}

APP_HOME=${APP_HOME%/} #remove the ending '/', for example: /home/ -> /home
INSTALL_HOME=${APP_HOME%/*}

APP_CONFIG_DIR=${APP_HOME}/config
APP_LIB_DIR=${APP_HOME}/lib

appConfig=$APP_HOME/config/application.properties
templateAppConfig=$APP_HOME/config/application_template.properties

CWD=$(cd $(dirname $0);pwd)
cd $CWD

#First EIM Install
install() {
    check_mysql

	release_packge

	init_config_files

	init_mysql

	config_system

	echo "Install Finished"
	echo " init address ip:10000"
	echo " example:http://192.168.85.2:10000 "
}

#Upgrade EIM
upgrade() {
	echo "Found the "$APP_HOME" is already existed, will upgrede..."

	remove_previous_files

	release_packge

    config_system

	init_config_files

	echo "Upgrade Finished."
}

#tar the jepush_base.tar.gz
release_packge() {
	\mkdir -p $APP_HOME/logs
    if [ -f $APP_HOME/bin/$APP_NAME ]; then
        tar --exclude $APP_NAME/config/application.properties --exclude $APP_NAME/config/application.yml --exclude $APP_NAME/config/logback.xml -zxvf $APP_NAME.tar.gz -C $INSTALL_HOME > $APP_HOME/logs/install.log 2>&1
    else
        tar -zxvf $APP_NAME.tar.gz -C $INSTALL_HOME > $APP_HOME/logs/install.log 2>&1 
    fi
	chmod -R 755 $APP_HOME
}

delete_install_package() {
	#delete install package
	\rm -f $CWD/$APP_NAME.tar.gz $0
}

#remove some leaving files from previous version
remove_previous_files() {
	echo "remove_previous_files..."

    \rm -f ${APP_LIB_DIR}/*
}


#init Config Files
init_config_files() {
	echo "init_config_files..."

	copyIfNotExist ${appConfig} ${templateAppConfig}
    copyIfNotExist ${APP_CONFIG_DIR}/keystore ${APP_CONFIG_DIR}/keystore_template
    copyIfNotExist ${APP_CONFIG_DIR}/logback.xml ${APP_CONFIG_DIR}/logback_template.xml

}


copyIfNotExist() {

    targetFile=$1
    targetTemplateFile=$2

    if [ -e ${targetFile} ] ; then
		echo "${targetFile} Already Exist, Skip"
	else
		cp -rf ${targetTemplateFile}	${targetFile}
		echo "${targetFile} Not Exist, Will Use Template Config File[${targetTemplateFile}]"
	fi

}

check_mysql() {
    RETVAL=0
    if which $MYSQL_COMMAND &> /dev/null; then
        if $MYSQL_COMMAND -h$MYSQL_HOST -P$MYSQL_PORT -u$MYSQL_USER -p$MYSQL_PWD -e "use mysql" &> /dev/null; then
            RETVAL=0
        else
            RETVAL=1
        fi
    else
        RETVAL=2
    fi

    case $RETVAL in
    0)  echo "finished check mysql."
        ;;
    1)  
        MYSQL_PWD=$(whiptail --title "Can't connect to MySQL server" --inputbox " Command: '$MYSQL_COMMAND -h$MYSQL_HOST -P$MYSQL_PORT -u$MYSQL_USER -p$MYSQL_PWD'\nMaybe it's a wrong password, \nPlease enter the correct password." 10 80 3>&1 1>&2 2>&3)
        [ -z "$MYSQL_PWD" ] && { whiptail --title "Input Error" --msgbox "Error Passowrd." 10 60; exit 1; }
        check_mysql
        ;;
    2)  
        MYSQL_COMMAND=$(whiptail --title "$MYSQL_COMMAND command not found" --inputbox " Enter a valid MySQL command or path." 10 60 3>&1 1>&2 2>&3)
        [ -z "$MYSQL_COMMAND" ] && { whiptail --title "Input Error" --msgbox "Error command." 10 60; exit 1; }
        check_mysql
        ;;
    esac
}

#Initial Mysql
init_mysql() {
    echo "try to initialaze the database..."
    $MYSQL_COMMAND -h$MYSQL_HOST -P$MYSQL_PORT -u$MYSQL_USER -p$MYSQL_PWD < $APP_HOME/install/install.sql
    echo "finished initialaze the database"
}

#Config System
config_system() {
	#config app base server
	echo "begin to config the $APP_NAME......"

    APP_USER=${APP_USER:-root}
    APP_GROUP=${APP_GROUP:-root}

    ULIMIT_FILE=/etc/security/limits.conf
    if grep "GZB" $ULIMIT_FILE ; then
        echo "already set the max number of open files to 500000..."
    else
        echo -e "# GZB \n* - nofile 500000" >> $ULIMIT_FILE
        echo "set the max number of open files to 500000";
    fi

    # Create the jive user and group
    /usr/sbin/groupadd $APP_GROUP
    /usr/sbin/useradd $APP_USER -g $APP_GROUP -s /bin/bash

    # Change the permissions on the installtion directory
    /bin/chown -R $APP_USER:$APP_GROUP $APP_HOME

    # Install the init script
	ln -sf $APP_HOME/bin/$APP_NAME /etc/init.d
	chkconfig --add $APP_NAME

    properties_update $appConfig $templateAppConfig

    service $APP_NAME condrestart

	echo "finished config the $APP_NAME."
}


############
#
# Process
#
###########

#check the $APP_NAME.tar.gz is exist now
if [ -e $APP_NAME.tar.gz ] ; then

	echo "found the $APP_NAME.tar.gz"

	if [ -e $APP_HOME ] ; then
		upgrade
	else
		install
	fi

	delete_install_package
else
	echo "can not found the $APP_NAME.tar.gz , please check and try again..."
	exit
fi
