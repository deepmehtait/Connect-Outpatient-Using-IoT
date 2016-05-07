(function() {
  'use strict';

  angular
    .module('users')
    .controller('DoctorDashboardController', DoctorDashboardController);

  DoctorDashboardController.$inject = ['$scope'];

  function DoctorDashboardController($scope) {
    var vm = this;

    // Doctor dashboard controller logic
    // ...

    init();

    function init() {
    }
  }
})();
