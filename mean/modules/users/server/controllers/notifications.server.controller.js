'use strict';

/**
 * Module dependencies.
 */
var path = require('path'),
  mongoose = require('mongoose'),
  errorHandler = require(path.resolve('./modules/core/server/controllers/errors.server.controller')),
  User = mongoose.model('User'),
  Notifications = mongoose.model('Notification');

/**
 *  Store notification information and send a Notification
 */
exports.sendNotification = function (req, res) {
  User.findOne({ 'fitbitUsername' : req.body.fitBitId },'username displayName', function (err,userData) {
    if (err) {
      return res.status(400).send({
        message: errorHandler.getErrorMessage(err)
      });
    } else {
      User.find({ 'patients' : userData.username }, 'username displayName phoneNumber', function (err2,doctorInfo) {
        if (err2) {
          return res.status(400).send({
            message: errorHandler.getErrorMessage(err2)
          });
        } else{
          var doctorIds = [];
          if (!(doctorInfo instanceof Array)) doctorInfo = new Array(doctorInfo);
          if(doctorInfo) {
            var i = 0;
            for (i = 0; i < doctorInfo.length; i++) {
              if (doctorInfo[i] && doctorInfo[i].username) {
                doctorIds.push(doctorInfo[i].username);
              }
            }
          }
          Notifications.update({ username: userData.username },
                      { $set:{ username: userData.username ,
                      fitbitUsername: req.body.fitBitId,
                      'doctorId': doctorIds } }, { upsert: true } ,function(err3, data) {
                        if (err3) {
                          return res.status(400).send({
                            message: errorHandler.getErrorMessage(err3)
                          });
                        } else {
                          res.json({ 'message': 'Success', 'doctorInfo': doctorInfo , 'patientInfo': userData });
                        }
                      });
        }
      });
    }
  });

};

