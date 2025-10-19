@echo off
setlocal enableextensions enabledelayedexpansion
pushd %~dp0

cd ..
call gradlew clean shadowJar

cd build\libs
set jarloc=
for /f "tokens=*" %%a in ('dir /b *.jar') do (
    set jarloc=%%a
)

if "!jarloc!"=="" (
    echo No JAR found
    exit /b 1
)

java -jar "!jarloc!" < ..\..\text-ui-test\input.txt > ..\..\text-ui-test\ACTUAL.TXT

cd ..\..\text-ui-test
if exist .\data\tasks.txt del .\data\tasks.txt

FC ACTUAL.TXT EXPECTED.TXT >NUL && ECHO Test passed! || ECHO Test failed!