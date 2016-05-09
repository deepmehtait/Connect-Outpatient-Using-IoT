'use strict';

/**
 * Module dependencies.
 */
var path = require('path'),
  mongoose = require('mongoose'),
  errorHandler = require(path.resolve('./modules/core/server/controllers/errors.server.controller')),
  User = mongoose.model('User');

/**
 * Get patients by doctor Id
 */
exports.getPatients = function (req, res) {
  User.findOne({ 'username' : req.params.doctorId },'patients', function (err,data) {
    if (err) {
      return res.status(400).send({
        message: errorHandler.getErrorMessage(err)
      });
    } else {
      User.find({ 'username' : { $in: data.patients } }, function (err2,data2) {
        if (err2) {
          return res.status(400).send({
            message: errorHandler.getErrorMessage(err2)
          });
        } else{
          res.json({ 'message': 'Success', 'data': data2 });
        }
      });
    }
  });
};


/**
 * Add patient by doctor Id
 */
exports.addPatient = function (req, res) {
  var query = { 'username': req.body.doctorId };
  User.findOneAndUpdate(query, { $push: { 'patients': req.body.patientId } }, function(err, data) {
    if (err) {
      return res.status(400).send({
        message: errorHandler.getErrorMessage(err)
      });
    } else {
      res.json({ 'Result': 'patient added successfully' });
    }
  });
};
