'use strict';

var express    = require('express');
var bodyParser = require('body-parser');
var cors       = require('cors');
var session    = require('express-session');

module.exports = function () {

  var app = express();

  app.use(cors());
  app.use(bodyParser.urlencoded({extended: false}));

  app.use(session({
    secret: 'thisisasecret',
    resave: false,
    saveUninitialized: true
  }));

  // LINK TO FRONT END //
  app.use(express.static('public'));

  return app;
};