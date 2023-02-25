REM This is a jOggPlayer Start Scrip that can be used with OS/2

REM The settings in this script assume you are running Java 2
REM  If not... you will have to update the classpaths to include JMF and/or
REM  Swing as needed.

set JOGG_PLAYER_HOME=.

set CLASSNAME=ca.bc.webarts.jOggPlayer
set Front_classpath=%JOGG_PLAYER_HOME%\jOggPlayer.jar;%classpath%
java -classpath %Front_classpath% %CLASSNAME% %1 %2 %3 %4 %5 %6 %7 %8 %9
