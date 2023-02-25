REM
REM A quick batch file to set things up to run the jOggPlayer
REM You can also double click on the jOggPlayer.jar file
REM
REM the settings in this script assume you are running Java 1.3
REM If not, you will have to upadate the classpaths to include JMF and/or
REM Swing as needed.

set JOGG_PLAYER_HOME=.
set CLASSNAME=ca.bc.webarts.jOggPlayer
start /high java -cp "%JOGG_PLAYER_HOME%\jOggPlayer.jar;%CLASSPATH%" %CLASSNAME% %*
