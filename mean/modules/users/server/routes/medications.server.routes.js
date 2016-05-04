'use strict';

module.exports = function(app) {
  // Routing logic   
  var medicationController = require('../controllers/medications.server.controller');
  app.route('/medication').post(medicationController.addMedication);
  app.route('/medication/:patientId/:medicationName').delete(medicationController.deleteMedication);
  app.route('/medication/:patientId/:medicationName').post(medicationController.updateMedication);
  app.route('/medication/:patientId').get(medicationController.getMedication);
};
