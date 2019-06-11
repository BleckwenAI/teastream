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
    global tracked_words
    global tracked_string
    if request.method == "POST":
        opt = request.form.get('start')
        if opt is not None:
            term = request.form['start']
            if term not in tracked_words:
                send_message(producer, 'track', term)
                tracked_words.append(term)
                tracked_string = ",".join(str(x) for x in tracked_words if (x != ''))
        opt = request.form.get('stop')
        print(request.form)
        if opt is not None:
            term = request.form['stop']
            if term in tracked_words:
                send_message(producer, 'stop', term)
                tracked_words.remove(term)
                tracked_string = ",".join(str(x) for x in tracked_words if (x != ''))
    return render_template('index.html', tracked_string=tracked_string)
