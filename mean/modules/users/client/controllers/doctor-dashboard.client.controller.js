

var app =  angular.module('users');

app.controller('DoctorDashboardController', ['$scope', '$http', 'Socket', 'Authentication',
    function ($scope, $http, Socket, Authentication) {
        // This provides Authentication context.
        $scope.authentication = Authentication;
        $scope.viewIt = "hello";


        $http.get('/doctor/patients/' + Authentication.user.username).success(function (response) {
        

            $scope.clientList = [];
            if (response.data instanceof Array) {
                $scope.clientList = response.data;
            }

           console.log($scope.clientList);

        }).error(function (response) {
            $scope.error = response.message;
        });
    }
]);
