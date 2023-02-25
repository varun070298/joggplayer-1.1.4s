#!/bin/sh
#
# the settings in this script assume you are running Java 2
# If not... you will have to update the classpaths to include JMF and/or
# Swing as needed.

JOGG_PLAYER_HOME=.
CLASSNAME=ca.bc.webarts.jOggPlayer

# start the player
echo "\nStarting the Java jOggPlayer..."
java -cp "${JOGG_PLAYER_HOME}/jOggPlayer.jar:${CLASSPATH}" $CLASSNAME $1 $2 $3 $4 $5 $6 $7 $8 $9
