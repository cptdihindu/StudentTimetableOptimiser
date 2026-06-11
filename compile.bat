@echo off
setlocal EnableExtensions EnableDelayedExpansion

rem Always run relative to this script's folder (project root)
pushd "%~dp0" || exit /b 1

set "JAR_NAME=StudentTimetableOptimiser.jar"
set "BUILD_DIR=build"
set "CLASSES_DIR=%BUILD_DIR%\classes"
set "SOURCES_FILE=%BUILD_DIR%\sources.txt"

echo.
echo === Student Timetable Optimiser: Build JAR ===

rem Delete old jar if it exists
if exist "%JAR_NAME%" (
	echo Deleting existing "%JAR_NAME%"...
	del /f /q "%JAR_NAME%" >nul 2>nul
)

rem Clean build output
if exist "%BUILD_DIR%" rmdir /s /q "%BUILD_DIR%" >nul 2>nul
mkdir "%CLASSES_DIR%" || goto :fail

rem Ensure JDK tools are available
where javac >nul 2>nul || (
	echo ERROR: javac not found. Install a JDK and add it to PATH.
	goto :fail
)
where jar >nul 2>nul || (
	echo ERROR: jar tool not found. Install a JDK and add it to PATH.
	goto :fail
)

rem Collect all Java source files
dir /s /b "src\main\java\*.java" > "%SOURCES_FILE%"
for %%A in ("%SOURCES_FILE%") do if %%~zA==0 (
	echo ERROR: No Java sources found under src\main\java
	goto :fail
)

echo Compiling sources...
javac -encoding UTF-8 -d "%CLASSES_DIR%" @"%SOURCES_FILE%"
if errorlevel 1 goto :fail

echo Creating runnable JAR...
jar cfe "%JAR_NAME%" Main -C "%CLASSES_DIR%" .
if errorlevel 1 goto :fail

echo.
echo Built "%JAR_NAME%" successfully.
echo Run: java -jar "%JAR_NAME%"
echo.

popd
exit /b 0

:fail
echo.
echo Build failed.
echo.
popd
exit /b 1
