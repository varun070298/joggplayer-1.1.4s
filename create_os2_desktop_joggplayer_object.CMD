/* WP object creator utility */
/*Tom Gutwin ... t.gutwin@ieee.org*/
'@echo off'

call RxFuncAdd "SysLoadFuncs", "RexxUtil", "SysLoadFuncs"
call SysLoadFuncs
call SysCls
say "Workplace Shell Object Creator Utility"
say


/*
call charout ,'What is the name of the Object to create : '
pull objectname
say

call charout ,'Please provide the FULL path to the program : '
pull programname
say
*/

curdir = directory()
objectname = 'jOggPlayer'
programname= curdir'\jOggPlayerJava2.cmd'
iconfile = curdir'\jOggPlayer.ico'

/*
if substr(programname,length(programname)) \= '\' then programname = programname ||'\'
*/
/*check to see if the file exists */
rc = SysFileSearch '.' programname 'file.'
if rc = 3 then
do
  Say
  say programname 'NOT found ... please check path and re-run.'
  say
  exit
end

say 'Attaching Icon "jOggPlayer.ico" to' programname
if SysSetIcon(programname,  iconfile  ) then
  say 'Icon 'iconfile ' attached to file ' programname

rc = SysCreateObject("WPProgram", objectname, "<WP_DESKTOP>", "EXENAME="programname ,"U")

if rc = 0 then
do
 Say 'Object ' objectname ' created on desktop!'
 exit
end

exit
