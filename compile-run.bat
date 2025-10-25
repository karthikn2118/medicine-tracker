@echo off
echo Cleaning old files...
del src\*.class 2>nul

echo Compiling Medicine Tracker...
javac src/*.java

if %errorlevel% equ 0 (
    echo ✅ Compilation successful!
    echo.
    echo Starting Medicine Tracker...
    echo.
    java -cp src MedicineTracker
) else (
    echo.
    echo ❌ Compilation failed! Check errors above.
)

pause