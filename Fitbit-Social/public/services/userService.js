var app = angular.module('connect');

app.service('userService', function ($http, $q) {

  this.getData = function () {
    var deferred = $q.defer();
    $http({
      method: 'GET',
      url: '/api/v1/data'
    }).then(function (response) {

      var fitbitResponse = response.data;
      
      deferred.resolve(fitbitResponse)
    });
    return deferred.promise;
  };

});