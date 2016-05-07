'use strict';

module.exports = function(app) {
  // Routing logic
  var fitbitdata = require('../controllers/fitbitdata.server.controller');
  app.route('/fitbitdata').post(fitbitdata.addFitBitData);
  app.route('/fitbitdata/:patientId').get(fitbitdata.getFitBitData);
};
