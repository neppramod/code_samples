#!/bin/bash
# Simple bash script that helps to create spring 3 web mvc project
#
# This script will grow and my default might not suit you, so 
# Every project can have data version specific or local spring 
# configuration file (usually spring.cfg)
# that can override default spring.cfg constants.
#
# @author Pramod Nepal
# Sat Jul  7 17:14:53 NPT 2012

# Don't worry, we need at least some place to start
# For copying default configuration files
# You can create your own version of DATA_DIRECTORY
# By ovverriding DATA_DIRECTORY variable
# in local configuration file

SCRIPT_DIRECTORY="/etc/spring-mvc"
GLOBAL_CONFIG_FILE="global-spring.cfg"

# Default configuration file
source $SCRIPT_DIRECTORY/conf/$GLOBAL_CONFIG_FILE

# Load module configuration file if it exists
if [ -f $SCRIPT_DIRECTORY/$DEFAULT_MODULE_DIRECTORY/$DEFAULT_MODULE/$MODULE_CONFIG_FILE ]; then
	source $SCRIPT_DIRECTORY/$DEFAULT_MODULE_DIRECTORY/$DEFAULT_MODULE/$MODULE_CONFIG_FILE
fi

# Load local configuration file if it exists
if [ -f $LOCAL_CONFIGURATION_DIRECTORY/$LOCAL_CONFIG_FILE ]; then
	source $LOCAL_CONFIGURATION_DIRECTORY/$LOCAL_CONFIG_FILE
fi

# Load local script if it exists else run module script
if [ -f $LOCAL_CONFIGURATION_DIRECTORY/$LOCAL_SCRIPT_FILE ]; then
	source $LOCAL_CONFIGURATION_DIRECTORY/$LOCAL_SCRIPT_FILE
else
	source $SCRIPT_DIRECTORY/$DEFAULT_MODULE_DIRECTORY/$DEFAULT_MODULE/$MODULE_SCRIPT_FILE
fi