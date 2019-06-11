from flask import Flask

app = Flask(__name__)

from app import routes

app.config['SECRET_KEY'] = 'you-will-never-guess'
global tracked_words