@echo off
setlocal
set DB_NAME=wordageddonDB.db
set SQL_NAME=wordageddonDB.sql

if exist %SQL_NAME% (
        if exist %DB_NAME% (
        del %DB_NAME%
    )
    sqlite3 %DB_NAME% < %SQL_NAME%
    
)
endlocal
