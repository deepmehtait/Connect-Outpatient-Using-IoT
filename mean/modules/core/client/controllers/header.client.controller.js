'use strict';

angular.module('core').controller('HeaderController', ['$scope', '$state', 'Authentication', 'Menus',
  function ($scope, $state, Authentication, Menus) {
    // Expose view variables
    $scope.$state = $state;
    $scope.authentication = Authentication;

    // Get the topbar menu
    $scope.menu = Menus.getMenu('topbar');

    // Toggle the menu items
    $scope.isCollapsed = false;
    $scope.toggleCollapsibleMenu = function () {
      $scope.isCollapsed = !$scope.isCollapsed;
    };

    $scope.checkAuthenticatedUser = function(user){
      var newState = "landing";
      if($scope.authentication.user.roles.indexOf('admin') > -1){
        newState = 'admin.users';
      }
      else if($scope.authentication.user.roles.indexOf('doctor') > -1){
        newState  = 'doctor-dashboard';
      }
      else{
        newState = 'home';
      }
      return $state.href(newState);
    };
    // Collapsing the menu after navigation
    $scope.$on('$stateChangeSuccess', function () {
      $scope.isCollapsed = false;
    });
  }
]);
