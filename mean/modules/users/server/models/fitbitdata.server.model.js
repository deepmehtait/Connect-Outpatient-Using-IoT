'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
  Schema = mongoose.Schema;

/**
 * Fitbitdata Schema
 */
var FitbitdataSchema = new Schema({
    // Fitbitdata model fields
  username: {
    type: String,
    trim: true
  },

  fitbitUsername: {
    type: String,
    trim: true
  },
  'healthdata':{}

});

mongoose.model('Fitbitdata', FitbitdataSchema);
