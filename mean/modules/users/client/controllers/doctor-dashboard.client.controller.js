

var app =  angular.module('users');

app.controller('DoctorDashboardController', ['$scope', '$http','$uibModal', 'Socket', 'Authentication',
    function ($scope, $http, $uibModal , Socket, Authentication) {
        // This provides Authentication context.
        $scope.authentication = Authentication;
        $scope.viewIt = "hello";
        $http.get('/doctor/patients/' + Authentication.user.username).success(function (response) {
            $scope.clientList = [];
            if (response.data instanceof Array) {
                $scope.clientList = response.data;
            }
        }).error(function (response) {
            $scope.error = response.message;
        });

        //Appointments***********************************
        $scope.calendarView = 'month';
        $scope.calendarDate = new Date();
        $scope.events = [];

        $http.get('/appointment/' + Authentication.user.username).success(function (response) {
            if (response && response.data) {
                for (var i = 0; i < response.data.length; i++) {
                    var newEvent = {type: 'info', draggable: false, resizable: false};
                    newEvent.startsAt = moment(response.data[i].date);
                    newEvent.endsAt = moment(response.data[i].date);
                    newEvent.title = '<img class="header-profile-image" alt="' + response.data[i].doctorName + '" ' +
                        'ng-src="' + response.data[i].doctorProfileImageURL + '" ' +
                        'src="' + response.data[i].doctorProfileImageURL + '"> ' +
                        '<span class="text-primary">' + response.data[i].doctorName + '</span>';
                    if (!showListFilter || (showListFilter && showListFilter == response.data[i].doctorId)) {
                        $scope.events.push(newEvent);
                    }
                }
            }
        }).error(function (response) {
            $scope.error = response.message;
        });



        //Open Appointment Modal***********************************************
        //Time Picker Options
        $scope.appointmentTime = new Date();
        $scope.hstep = 1;
        $scope.mstep = 5;
        $scope.ismeridian = true;
        //Date Picker options
        $scope.appointmentDate = new Date();
        $scope.dateOptions = {
            startingDay: 1,
            initDate: new Date()
        };
        $scope.open1 = function() {
            $scope.popup1.opened = true;
        };
        $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy','dd-MMMM-yyyy', 'shortDate'];
        $scope.format = $scope.formats[0];
        //$scope.altInputFormats = ['M!/d!/yyyy'];
        $scope.popup1 = {
            opened: false
        };
        $scope.addAppointment = function () {
            $scope.appointmentDate = new Date();
            $scope.appointmentDate.setDate($scope.appointmentDate.getDate() - 7);
            $scope.appointmentModalTitle = "Add Appointment";
            $scope.appointmentModalInstance = $uibModal.open({
                templateUrl: 'modules/users/client/views/appointment.client.view.html',
                controller: 'HomeController',
                scope: $scope
            });
        }
    }
]);
