FROM ubuntu:latest
RUN apt-get update -y
RUN apt-get install -y netcat curl
ADD /busybox-entry.sh /busybox-entry.sh
ADD /curl.sh /curl.sh
ADD /connector.json /
RUN chmod +x curl.sh
RUN chmod +x busybox-entry.sh
ENTRYPOINT ["/busybox-entry.sh"]