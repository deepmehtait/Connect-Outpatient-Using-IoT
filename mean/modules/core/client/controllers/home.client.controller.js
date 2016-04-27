'use strict';

angular.module('core').controller('HomeController', ['$scope', 'Authentication',
  function ($scope, Authentication) {
    // This provides Authentication context.
    $scope.authentication = Authentication;

    /*$scope.labels = [0];
    $scope.data = [0];
    for(var i=1 ; i< 25; i++){
      $scope.labels.push(i);
      $scope.data.push(70+(i*2));
    }*/
    $scope.labels = ["18:30", "18:32", "18:33", "18:35", "18:36", "18:37","18:39", "18:40", "18:42","18:44", "18:45", "18:48"];
    $scope.series = ['Heart Rate'];
    $scope.data = [
      [72, 70, 73, 78, 74, 82, 74,76,77,72,73,74]
    ];
    $scope.onClick = function (points, evt) {
      console.log(points, evt);
    };
  }
]);
