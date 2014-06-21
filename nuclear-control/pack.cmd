@echo off

del builds\IC2NuclearControl-dev.zip
"C:\Program Files\7-Zip\7z" a builds\IC2NuclearControl-dev.zip ..\mcp\reobf\minecraft\*  %~dp0client\assets %~dp0common\mcmod.info
