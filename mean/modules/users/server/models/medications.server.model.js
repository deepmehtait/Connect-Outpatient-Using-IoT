'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
  Schema = mongoose.Schema;

/**
 * Medications Schema
 */
var MedicationsSchema = new Schema({
  // Medications model fields
  patientId: {
    type: String,
    trim: true,
    required: 'Patient ID is required'
  },
  medications :{
    type: Object,
    required: 'Medication information is required'
  }
});

mongoose.model('Medication', MedicationsSchema);
