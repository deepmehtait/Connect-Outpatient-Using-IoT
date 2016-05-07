var app = angular.module('connect');

app.controller('UserCtrl', function ($scope, userService) {

  $scope.getFitBitData = function () {
    userService.getData().then(function (data) {

      // ALL FITBIT DATA //
      $scope.fitbitData = data;
      console.log('FITBIT HEART DATA: ', $scope.fitbitData.heartRateToday);

     $scope.heart = $scope.fitbitData.heartRateToday;

     $scope.heartStream = $scope.fitbitData.heartStreamingData;

      $scope.heartToday       = numeral($scope.fitbitData.heartRateToday.activities-heart[1].value.heartRateZones[1].max);


      // DISPLAY USERNAME //
      $scope.username           = $scope.fitbitData.profile.user.fullName;

      $scope.gender             = $scope.fitbitData.profile.user.gender;

      $scope.height             = $scope.fitbitData.profile.user.height;

      $scope.heightUnit         = $scope.fitbitData.profile.user.heightUnit;

      $scope.locale             = $scope.fitbitData.profile.user.locale;

      $scope.memberSince        = $scope.fitbitData.profile.user.memberSince;

      $scope.weight             = $scope.fitbitData.profile.user.weight;

      $scope.weightUnit         = $scope.fitbitData.profile.user.weightUnit;
      });
  };

  $scope.getFitBitData();

});
