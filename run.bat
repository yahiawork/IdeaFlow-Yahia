\
@echo off
setlocal
set SRC=src
set OUT=out
if exist %OUT% rmdir /S /Q %OUT%
mkdir %OUT%
echo Compiling...
javac -encoding UTF-8 -d %OUT% -sourcepath %SRC% %SRC%\com\yahia\ideaflow\app\Main.java
if errorlevel 1 (
  echo Compile failed.
  pause
  exit /b 1
)
echo Running...
java -cp %OUT% com.yahia.ideaflow.app.Main
pause
