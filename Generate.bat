cd src\odin\shared\xml\xsd\
xjc -verbose -d ../../../../ -p odin.shared.xml *.xsd
cd..
cd..
cd..
cd odin\xml\xsd
xjc -verbose -d ../../../../ -p odin.odin.xml *.xsd
cd..
cd..
cd..
cd..
cd..
cd build\classes\
wsgen -cp . -d . -keep -verbose odin.odin.ws.SleipnirServiceImpl
cd odin\shared\ws\jaxws\
xcopy *.java ..\..\..\..\..\src\odin\shared\ws\jaxws\
cd..
cd..
cd..
cd..
REM wsgen -cp . -d . -keep -verbose odin.odin.ws.OdinServiceImpl
cd..
cd..
