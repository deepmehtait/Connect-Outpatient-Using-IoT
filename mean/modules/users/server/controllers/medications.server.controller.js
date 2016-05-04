'use strict';

/**
 * Module dependencies.
 */
var path = require('path'),
  mongoose = require('mongoose'),
  errorHandler = require(path.resolve('./modules/core/server/controllers/errors.server.controller')),
  Medication = mongoose.model('Medication');

/**
 * Create a Medication
 */
exports.addMedication = function (req, res) {
  var newMedication = new Medication(req.body);
  var message = null;
  Medication.update({ patientId: newMedication.patientId },
     { $push: { 'medications': newMedication.medications } }, { upsert: true }, function (err, data) {
        if (err) {
            return res.status(400).send({
                message: errorHandler.getErrorMessage(err)
            });
        } else {
            res.json({ 'Result': 'Medication added successfully' });
        }
     });
};

/**
 * Get Medication
 */
exports.getMedication = function (req, res) {
  var patientId = req.params.patientId;
  if(patientId) {
    Medication.findOne({patientId: patientId},'medications',function (err, data) {
      if (err) {
        return res.status(400).send({
          message: errorHandler.getErrorMessage(err)
        });
      } else {
        res.json(data.medications);
      }
    });
  }
  else{
    return res.status(400).send({
       message: 'Patient Id is missing from the request'
    });
  }
};

exports.deleteMedication = function (req, res) {
  var patientId = req.params.patientId;
  var medicineName = req.params.medicationName;
  if(patientId && medicineName ) {
    Medication.update({patientId: patientId},{ $pull: { medications: { name : medicineName } } },function (err, data) {
        console.log(data);
      if (err) {
        return res.status(400).send({
          message: errorHandler.getErrorMessage(err)
        });
      } else if(data && data.nModified > 0) {
          res.json({ 'Result': 'Medication deleted successfully' });
        }
        else{
          res.json({ 'Result': 'No matched record found' });
        }
      });
  }
  else{
    return res.status(400).send({
      message: 'Patient Id or medicine name is missing from the request'
    });
  }
};

exports.updateMedication = function (req, res) {
    var patientId = req.params.patientId;
    var medicineName = req.params.medicationName;
    var updatedInfo = new Medication(req.body);
    if(patientId && medicineName ) {
        Medication.update({ patientId: patientId , medications: { $elemMatch: { name: medicineName } } },{ $set: { "medications.$" : updatedInfo.medications } },function (err, data) {
            console.log(data);
            if (err) {
                return res.status(400).send({
                    message: errorHandler.getErrorMessage(err)
                });
            } else if(data && data.nModified > 0) {
                res.json({ 'Result': 'Medication information updated successfully' });
            }
            else{
                res.json({ 'Result': 'No matched record found' });
            }
        });
    }
    else{
        return res.status(400).send({
            message: 'Patient Id or medicine name is missing from the request'
        });
    }
};
