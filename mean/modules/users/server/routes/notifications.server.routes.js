'use strict';

module.exports = function(app) {
  // Routing logic
  var notifications = require('../controllers/notifications.server.controller');
  app.route('/notification').post(notifications.sendNotification);
};
