@echo off
set goal=%*
if "%goal%" == "" set goal=clean install
echo goal: %goal%

call mvn -f ../pom.xml -DskipTests -P repos-oss %goal%

pause
