'use strict';

module.exports = function(app) {
  // Routing logic
  var fitbitdata = require('../controllers/fitbitdata.server.controller');
  app.route('/api/fitbitdata').post(fitbitdata.saveFitBitData);
};
