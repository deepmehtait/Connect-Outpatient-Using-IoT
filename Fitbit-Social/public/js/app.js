var app = angular.module('connect', ['ngRoute', 'chart.js']);

app.config(function ($routeProvider) {

  $routeProvider
    .when('/', {
      templateUrl: './features/home/homeTemplate.html',
      controller: 'HomeCtrl'
    })

    .when('/user/:id', {
      templateUrl: './features/user/userInfoTemplate.html',
      controller: 'UserCtrl'
    })

    

    .otherwise({
      redirectTo: '/'
    });

});

app.run(function ($rootScope, $location, $anchorScroll, $routeParams) {
  $rootScope.$on('$routeChangeSuccess', function (newRoute, oldRoute) {
    $location.hash($routeParams.scrollTo);
    $anchorScroll();
  });
});




