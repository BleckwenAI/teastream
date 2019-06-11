#!/bin/bash
FLINK_CMD="flink run -m jobmanager:8081 /jobs/flink-assembly-0.1-SNAPSHOT.jar"
echo $FLINK_CMD
exec $FLINK_CMD
