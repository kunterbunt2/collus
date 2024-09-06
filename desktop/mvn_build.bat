call mvn -version
cd ..
call mvn clean install
cd engine
call mvn clean install
cd ../core
call mvn clean install
cd ../desktop
call mvn clean install
pause