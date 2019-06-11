#!bin/bash
echo "Waiting for connect to launch on 8083..."

while ! nc -z connect 8083; do
  sleep 1 # wait for 1/10 of the second before check again
done

sh curl.sh

echo "curl done"