#!/bin/sh

CLASSPATH=~/Sandbox/appengine-java-sdk-1.8.0/lib/user/appengine-api-1.0-sdk-1.8.0.jar
CLASSPATH=$CLASSPATH:~/Applications/gaeshell.jar
CLASSPATH=$CLASSPATH:~/Sandbox/appengine-java-sdk-1.8.0/lib/appengine-remote-api.jar

java -cp $CLASSPATH com.inpun.alt.gaecl.AppEngineShell $@
