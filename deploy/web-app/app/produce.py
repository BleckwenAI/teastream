from kafka import KafkaProducer


def send_message(producer, key, message):
    producer.send('requests_topic', key=key, value=message)


def init_producer():
    return KafkaProducer(bootstrap_servers='broker:29092', key_serializer=str.encode, value_serializer=str.encode)
