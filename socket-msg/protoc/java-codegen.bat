@echo off

protoc-3.0.exe --java_out="../src/main/java/" *.proto

echo Compiled!, Press any key to quit!

pause>nul