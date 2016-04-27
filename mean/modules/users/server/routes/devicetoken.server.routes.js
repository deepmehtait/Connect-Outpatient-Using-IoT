'use strict';

module.exports = function(app) {
  // Routing logic   
  var deviceTokenController = require('../controllers/devicetoken.server.controller');
  app.route('/deviceToken').post(deviceTokenController.addDeviceToken);
  app.route('/deviceToken/:tokenUserId/:tokenUUID').delete(deviceTokenController.deleteDeviceToken);
};
