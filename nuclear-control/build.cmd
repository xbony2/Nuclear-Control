@echo off
xcopy /Q /S /Y common\*  ..\mcp\src\minecraft\
xcopy /Q /S /Y ext\common\modstats\*  ..\mcp\src\minecraft\
xcopy /Q /S /Y client\*  ..\mcp\src\minecraft\

cd ..\mcp
runtime\bin\python\python_mcp runtime\recompile.py && runtime\bin\python\python_mcp runtime\reobfuscate.py  --srgnames
cd %~dp0