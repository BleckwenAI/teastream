from flask import render_template
from app.produce import send_message, init_producer
from app import app
from flask import request

producer = init_producer()
global tracked_words
tracked_words = []
global tracked_string
tracked_string = ",".join(str(x) for x in tracked_words)


def to_remove(new, old):
    lst3 = [value for value in old if value not in new and (value != '')]
    return lst3


def to_add(new,old):
    return [value for value in new if value not in old and (value != '')]


@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == "POST":
        global tracked_words
        global tracked_string
        text = request.form.get('text')
        new_tracked_words = text.split(',')
        add = to_add(new_tracked_words,tracked_words)
        remove = to_remove(new_tracked_words,tracked_words)
        for i in add:
            send_message(producer, 'track', i)
        for i in remove:
            send_message(producer, 'stop', i)
        tracked_words = new_tracked_words
        tracked_string = ",".join(str(x) for x in tracked_words if (x != ''))

        print(text)

    return render_template('index.html', tracked_string=tracked_string)

if __name__ == "__main__":
    # Only for debugging while developing
    app.run(host='0.0.0.0', debug=True, port=5000)