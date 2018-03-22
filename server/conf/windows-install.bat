@ECHO OFF
pushd %~dp0
REM This file is needed if you want to run the service on a Windows machine.
REM the custom variables that must be adjusted for each project.
@REM SET project=PROJECT-NAME (directory name of the project)
SET project=
FOR %%* in (..) do SET project=%%~nx*
SET search=-
SET replace=_
CALL SET project=%%project:%search%=%replace%%%

@REM other variables
@REM SET home=C:\opt\scala-adapters\%project%
pushd ..
SET home=%CD%
pushd %~dp0

SET driveletter=%CD:~0,2%

@REM --------------------set project name---------------------------------------
SET projecttmp=
SET /p projecttmp="Default project name is [%project%], ENTER if correct otherwise insert project name here: "
IF NOT [%projecttmp%] == [] (
  SET project=%projecttmp%
  )
@REM ---------------------------------------------------------------------------


@REM --------------------set project home path----------------------------------
SET hometmp=
SET /p hometmp="Default project home folder is [%home%], ENTER if correct otherwise insert path here: "
IF NOT [%hometmp%] == [] (
  IF EXIST "%hometmp%" (
    SET home="%hometmp%"
   ) ELSE (
   ECHO !!!!! Your path "%hometmp%" does not exist. Exit!
   PAUSE
   EXIT /B
   )
) 
@REM ---------------------------------------------------------------------------


@REM -------------------------Settings SUMMARY----------------------------------
ECHO +++ Your project name is "%project%"
ECHO +++ Your path is "%home%"
@REM ---------------------------------------------------------------------------


@REM -------------------------create config file--------------------------------
SET file=%home%\%project%_config.txt
SET appini=%home%\conf\application.ini

@REM remove the '-J' prefix that is not needed by Windows.
setlocal enableextensions disabledelayedexpansion
set "search=-J"
set "replace="

IF EXIST "%appini%" (
    @REM Copy the application.ini (created by the build process)
    ECHO copy /y  %appini% %file%
    copy /y  %appini% %file%

    for /f "delims=" %%i in ('type "%file%" ^& break ^> "%file%" ') do (
       set "line=%%i"
       setlocal enabledelayedexpansion
       set "line=!line:%search%=%replace%!"
       >>"%file%" echo(!line!
       endlocal
    )
    ECHO ===%file% created
) ELSE (
    ECHO !!!!! there is no file at %appini%. EXIT!
    PAUSE
    EXIT /B
)
@REM ---------------------------------------------------------------------------


PAUSE

