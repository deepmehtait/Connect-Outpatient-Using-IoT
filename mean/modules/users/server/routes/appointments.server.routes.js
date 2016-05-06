'use strict';

module.exports = function(app) {
  // Routing logic   
  var appointmentController = require('../controllers/appointments.server.controller');
  app.route('/appointment').post(appointmentController.addAppointment);
  app.route('/appointment/:appointmentId').delete(appointmentController.deleteAppointment);
  app.route('/appointment/:appointmentId').post(appointmentController.updateAppointment);
  app.route('/appointment/:uId').get(appointmentController.getAppointments);
};
