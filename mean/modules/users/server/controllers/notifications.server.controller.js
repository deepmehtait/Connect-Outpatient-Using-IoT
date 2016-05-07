'use strict';

/**
 * Module dependencies.
 */
var path = require('path'),
  mongoose = require('mongoose'),
  gcm = require('node-gcm'),
  errorHandler = require(path.resolve('./modules/core/server/controllers/errors.server.controller')),
  User = mongoose.model('User'),
  Devicetoken = mongoose.model('Devicetoken'),
  Notifications = mongoose.model('Notification');

/**
 *  Store notification information and send a Notification
 */
exports.sendNotification = function (req, res) {
  User.findOne({ 'fitbitUsername' : req.body.fitBitId },'username displayName emergencyContactName emergencyContactNumber', function (err,userData) {
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
                      doctorId : doctorIds }, $push: { 'events' : { 'time': new Date() , 'value': req.body.value } } }, { upsert: true } ,function(err3, data) {
                        if (err3) {
                          return res.status(400).send({
                            message: errorHandler.getErrorMessage(err3)
                          });
                        } else {
                          res.json({ 'message': 'Success', 'doctorInfo': doctorInfo , 'patientInfo': userData });

                          //Send Notification to mobile app
                          Devicetoken.find({ 'username' : doctorInfo[0].username },'token', function (err,deviceInfo) {
                            if (err) {
                              return res.status(400).send({
                                message: errorHandler.getErrorMessage(err)
                              });
                            } else {
                              console.log(deviceInfo);
                              if(deviceInfo) {
                                var message = new gcm.Message();
                                message.addNotification({
                                  'title' : 'Attention Needed',
                                  'body' : 'Your patient ' + userData.displayName + ' might be in serious condition!',
                                  'icon' : 'ic_stat_emergency'
                                });
                                var regTokens = [];
                                if (!(deviceInfo instanceof Array)) deviceInfo = new Array(deviceInfo);
                                for(var i=0 ; i< deviceInfo.length ; i++){
                                  regTokens.push(deviceInfo[i].token);
                                }
                                var sender = new gcm.Sender('AIzaSyB9qyp0uN4IqiMIq7SRE8g5B-iOUp8VfS4');
                                sender.send(message, { registrationTokens: regTokens }, function (err, response) {
                                  if (err) console.error(err);
                                  else console.log(response);
                                });
                              }
                            }
                          });
                        }
                      });
        }
      });
    }
  });

};

