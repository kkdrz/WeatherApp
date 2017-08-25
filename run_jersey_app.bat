cd jerseyapp
call mvn clean
call mvn package -DskipTests
call mvn jetty:stop
call mvn jetty:run
pause