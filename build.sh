echo "--> Building JAR files..."
sbt flink/assembly
sbt kafkatwitter/assembly
echo "--> Done building JAR files!"
echo "--> Building Docker images..."
cd deploy/busybox/
docker build . -t teastream/busybox
cd ../flink-job/
docker build . -t teastream/flink-job
cd ../producer-job/
docker build . -t teastream/producer-anon-job
cd ../web-app/
docker build . -t teastream/flask
echo "--> Done building Docker images..."