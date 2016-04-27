'use strict';

/**
 * Module dependencies.
 */
var path = require('path'),
  mongoose = require('mongoose'),
  errorHandler = require(path.resolve('./modules/core/server/controllers/errors.server.controller')),
  DeviceToken = mongoose.model('Devicetoken');

/**
 * Add a Devicetoken
 */
exports.addDeviceToken = function (req, res) {
  var newToken = new DeviceToken(req.body);
  newToken.save(function (err) {
    if (err) {
      return res.status(400).send({
        message: errorHandler.getErrorMessage(err)
      });
    } else {
      res.json({ 'Result': 'new device token added successfully' });
    }
  });
};
/**
 * Delete a Devicetoken
 */
exports.deleteDeviceToken = function (req, res) {
  DeviceToken.remove({ $and: [ { username : req.params.tokenUserId } , { uuid: req.params.tokenUUID } ] }, function (err,data) {
    console.log(data);
    if (err) {
      return res.status(400).send({
        message: errorHandler.getErrorMessage(err)
      });
    } else if(data && data.result && data.result.n > 0){
      res.json({ 'Result': 'device token deleted successfully' });
    }
    else{
      res.json({ 'Result': 'Record not found with given parameters' });
    }
  });
};
