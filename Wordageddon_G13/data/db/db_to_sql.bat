@echo off
setlocal
set DB_NAME=wordageddonDB.db
set SQL_NAME=wordageddonDB.sql

if exist %DB_NAME% (
    sqlite3 %DB_NAME% .dump > %SQL_NAME%
    ) 
endlocal
