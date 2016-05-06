'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
  Schema = mongoose.Schema;

/**
 * Appointments Schema
 */
var AppointmentsSchema = new Schema({
  // Appointments model fields
  patientId: {
    type: String,
    trim: true,
    required: 'Patient ID is required'
  },

  doctorId :{
    type: String,
    trim: true,
    required: 'Doctor ID is required'
  },

  doctorName :{
    type: String,
    trim: true,
    required: 'Doctor name is required'
  },

  patientName :{
    type: String,
    trim: true,
    required: 'Patient name is required'
  },

  doctorPhone :{
    type: String,
    trim: true
  },

  patientPhone :{
    type: String,
    trim: true
  },

  locationName :{
    type: String,
    trim: true,
    required: 'Location can not be empty'
  },

  address :{
    type: String,
    trim: true,
    required: 'Address can not be empty'
  },

  city :{
    type: String,
    trim: true,
    required: 'City can not be empty'
  },

  state :{
    type: String,
    trim: true,
    required: 'State can not be empty'
  },

  zipcode : {
    type: String,
    trim: true,
    required: 'Zipcode can not be empty'
  },

  time : {
    type: String,
    trim: true,
    required: 'Appointment time can not be empty'
  },

  date : {
    type: Date,
    trim: true,
    required: 'Appointment date can not be empty'
  },

  doctorProfileImageURL : {
    type: String,
    default: 'modules/users/client/img/profile/default.png'
  },

  patientProfileImageURL : {
    type: String,
    default: 'modules/users/client/img/profile/default.png'
  }

});

mongoose.model('Appointment', AppointmentsSchema);
