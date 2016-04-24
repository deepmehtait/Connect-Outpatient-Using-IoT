'use strict';

module.exports = function(app) {
  // Routing logic   
  var doctor = require('../controllers/doctor.server.controller');
  app.route('/doctor/patients/:doctorId').get(doctor.getPatients);
  app.route('/doctor/patients').post(doctor.addPatient);
};
