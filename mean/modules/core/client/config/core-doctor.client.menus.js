'use strict';

angular.module('core').run(['Menus',
  function (Menus) {
    Menus.addMenuItem('topbar', {
      title: 'Medication',
      state: 'doctorMedication',
      type: 'dropdown',
      roles: ['doctor']
    });

    Menus.addMenuItem('topbar', {
      title: 'Appointment',
      state: 'doctorAppointment',
      type: 'dropdown',
      roles: ['doctor']
    });
  }
]);
