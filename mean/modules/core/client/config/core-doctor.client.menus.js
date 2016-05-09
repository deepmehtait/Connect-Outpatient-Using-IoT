'use strict';

angular.module('core').run(['Menus',
  function (Menus) {
    Menus.addMenuItem('topbar', {
      title: 'Home',
      state: 'doctor-dashboard',
      roles: ['doctor']
    });

    Menus.addMenuItem('topbar', {
      title: 'Home',
      state: 'home',
      roles: ['patient']
    });
  }
]);
