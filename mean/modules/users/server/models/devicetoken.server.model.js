'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
  Schema = mongoose.Schema;

/**
 * Devicetoken Schema
 */
var DevicetokenSchema = new Schema({
  // Devicetoken model fields
  username: {
    type: String,
    trim: true,
    required: 'username is required'
  },
  uuid: {
    type: String,
    trim: true,
    required: 'UUID is required'
  },
  token: {
    type: String,
    trim: true,
    required: 'token is required'
  }
});

mongoose.model('Devicetoken', DevicetokenSchema);
