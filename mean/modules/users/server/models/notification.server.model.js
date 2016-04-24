'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
  Schema = mongoose.Schema;

/**
 * Notification Schema
 */
var NotificationSchema = new Schema({
  // Notification model fields
  username: {
    type: String,
    trim: true,
    required: 'user id is required'
  },

  fitbitUsername: {
    type: String,
    trim: true,
    required: 'Fitbit sensor id is required'
  },

  doctorId: {},

  events :{}
});

mongoose.model('Notification', NotificationSchema);
