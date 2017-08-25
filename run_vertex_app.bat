cd nioapp
call mvn clean
call mvn package -DskipTests
java -jar "target/nioapp-1.0-SNAPSHOT-fat.jar" -conf src/conf/config.json
pause