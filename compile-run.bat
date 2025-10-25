@echo off
echo Compiling Medicine Tracker...
javac -cp "lib/mysql-connector-java-8.0.33.jar" src/*.java

if %errorlevel% equ 0 (
    echo.
    echo Running Program...
    echo.
    java -cp "src;lib/mysql-connector-java-8.0.33.jar" MedicineTracker
) else (
    echo.
    echo Compilation failed! Check errors above.
    pause
)