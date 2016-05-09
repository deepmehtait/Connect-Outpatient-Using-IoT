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

  avgValue: {
    type: String
  },

  maxValue : {
    type: String
  },

  minValue : {
    type: String
  },

  'healthdata':{}

});

mongoose.model('Fitbitdata', FitbitdataSchema);
