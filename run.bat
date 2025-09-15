@echo off
for /f "tokens=*" %%a in (.env) do set "%%a"
cd example
mvn clean spring-boot:run -Dfile.encoding=UTF-8