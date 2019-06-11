FROM ubuntu:latest
RUN apt-get update -y
RUN apt-get update -y \
  && apt-get install -y python3-pip python3-dev build-essential netcat \
  && cd /usr/local/bin \
  && ln -s /usr/bin/python3 python \
  && pip3 install --upgrade pip
COPY ./app /app
ADD /flask-entrypoint.sh /app/
WORKDIR /app
RUN chmod +x flask-entrypoint.sh
RUN pip install Flask kafka-python
#CMD ["sh", "export FLASK_APP=routes.py", "flask run"]
#CMD ["export FLASK_APP=routes.py && flask run"]
ENTRYPOINT ["/app/flask-entrypoint.sh"]
