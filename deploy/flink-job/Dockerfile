FROM flink:1.6
RUN mkdir /jobs
ADD /files/flink-jobs-entrypoint.sh /jobs/
RUN chmod +x /jobs/flink-jobs-entrypoint.sh

ADD /flink-assembly-0.1-SNAPSHOT.jar /jobs/
ENTRYPOINT ["/jobs/flink-jobs-entrypoint.sh"]
