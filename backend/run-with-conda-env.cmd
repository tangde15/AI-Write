@echo off
set JAVA_HOME=E:\Project Practice\Write\conda-env\Library\lib\jvm
set PATH=%JAVA_HOME%\bin;%PATH%
cd /d "%~dp0"
call mvnw.cmd %*
