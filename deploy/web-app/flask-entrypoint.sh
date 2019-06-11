#!/bin/bash
echo "Waiting for brokers"

while ! nc -z broker 29092; do
  sleep 0.1 # wait for 1/10 of the second before check again
done

export FLASK_APP=routes.py
export LC_ALL=C.UTF-8
export LANG=C.UTF-8
flask run --host=0.0.0.0 --port=5000
