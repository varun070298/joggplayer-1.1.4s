@echo off

REM ************************************************************************
REM * Run a java application using the Java13 JVM.
REM * This command script is wriiten for OS/2 users.
REM * Tom B. Gutwin - t g u t w i n @ w e b a r t s . b c . c a
REM * 
REM * It sets up the Warp environmnet to use the java13 runtime 
REM * instead of a default 1.1.8.
REM * This is used when you have the 1.3 JVM installed but left the
REM * 1.1.8 JVM as the default some of the base OS/2 Java Apps work.
REM * And now you want to run your applications with the 1.3 runtime.
REM * Test it by typing --->   java2.cmd -version
REM *
REM *
REM * Installation: Set the USER SETTINGS BELOW.
REM *               Put this script in your path somewhere
REM *               Then use java2 to invoke all your java apps.
REM *
REM * Usage is almost identical to the normal way of starting a java app.
REM *  Examples:
REM *    java2.cmd ca.bc.webarts.jOggPlayer
REM *    java2.cmd -jar jOggPlayer.jar
REM *    java2.cmd -version
REM *    java2.cmd -cp g:\pkgs;%CLASSPATH% ca.bc.webarts.jOggPlayer
REM * 
REM * ***Exceptions to the rule because passed parameters on the commandline
REM *    don't like "=" signs.
REM *    It doesn't work to type :
REM *        java2.cmd -Djava.compiler=NONE ca.bc.webarts.jOggPlayer
REM *    If you want to disable the JIT you have to add this one right into 
REM *    this script file. (I have a commented out line below)
REM *
REM * I also added the use of an extra environment variable FRONT_JVM_OPTIONS
REM * to set whatever you want to go directly to the jvm 
REM *  Examples:
REM *     set FRONT_JVM_OPTIONS=-cp g:\pkgs;%CLASSPATH% -Xmx80m
REM *     java2.cmd ca.bc.webarts.jOggPlayer
REM * 
REM * I also added the use of an extra environment variable BEGINCLASSPATH
REM * to add its value to the front of the classpath without always having 
REM * to type in the -cp bla.bla;%classpath% stuff.
REM *  Examples:
REM *     set BEGINCLASSPATH=g:\pkgs
REM *     java2.cmd ca.bc.webarts.jOggPlayer
REM * 
REM ************************************************************************

REM ************************************************************************
REM * Remember initial settings so we can reset them afterwards
REM ************************************************************************
set OLD_LIBPATH=%LIBPATH%
set BEGINLIBPATH=%JAVA_HOME%\BIN;%JAVA_HOME%\dll;%INSTALL_DRIVE%\tcpip\dll;
SET OLD_PATH=%path%
SET OLD_CLASSPATH=%CLASSPATH%
SET OLD_INCLUDE=%INCLUDE%
SET OLD_LIB=%LIB%
SET OLD_JAVA_HOME=%JAVA_HOME%



REM ************************************************************************
REM * USER SETTINGS
REM * PLEASE SET THESE FOR YOUR STYSTEM
REM ************************************************************************
set INSTALL_DRIVE=c:
set JAVA_HOME=g:\java13\JRE
set JAVA_CONSOLE=0

REM ************************************************************************
REM * Thats all you should need to set!
REM ************************************************************************


REM ************************************************************************
REM * On with the show!!!
REM ************************************************************************
SET PATH=.;%JAVA_HOME%\BIN;%JAVA_HOME%\..\BIN;%path%;%INSTALL_DRIVE%\os2
SET INCLUDE=%JAVA_HOME%\..\INCLUDE;
SET LIB=%JAVA_HOME%\..\LIB;
SET CLASSPATH=.;%JAVA_HOME%\lib\rt.jar;%JAVA_HOME%\..\lib\tools.jar;%CLASSPATH%

@echo on
SET CLASSPATH=%BEGINCLASSPATH%;%CLASSPATH%
@echo off
REM ************************************************************************
REM The following line is the one to use if you want to turn the JIT off
REM %JAVA_HOME%\BIN\java %FRONT_JVM_OPTIONS% -Djava.compiler=NONE %1 %2 %3 %4 %5 %6 %7 %8 %9
REM ************************************************************************
@echo on
%JAVA_HOME%\BIN\java %FRONT_JVM_OPTIONS% %1 %2 %3 %4 %5 %6 %7 %8 %9
@echo off


REM ************************************************************************
REM Try to set things back to their previous state.
REM ************************************************************************
set path=%old_path%
set BEGINLIBPATH=%OLD_LIBPATH%;
SET CLASSPATH=%OLD_CLASSPATH%
SET INCLUDE=%OLD_INCLUDE%
SET LIB=%OLD_LIB%
SET JAVA_HOME=%OLD_JAVA_HOME%

REM ************************************************************************
REM CVS tags (don't worry about these)
REM ************************************************************************
REM *  $Source: f:/cvsroot2/open/projects/jOggPlayer/java2.cmd,v $
REM *  $Name:  $
REM *  $Revision: 1.1 $
REM *  $Date: 2002/04/09 17:34:01 $
REM *  $Locker:  $

