

var app =  angular.module('users');

app.controller('DoctorDashboardController', ['$scope', '$filter', '$http','$uibModal', 'Socket', 'Authentication',
    function ($scope,$filter, $http, $uibModal , Socket, Authentication) {
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
        $scope.doctorEvents = [];

        $http.get('/appointment/' + Authentication.user.username).success(function (response) {
            if (response && response.data) {
                for (var i = 0; i < response.data.length; i++) {
                    var newEvent = {type: 'info', draggable: false, resizable: false, editable: true, deletable:true};
                    newEvent.startsAt = moment(response.data[i].date);
                    newEvent.endsAt = moment(response.data[i].date);
                    newEvent.data = response.data[i];
                    newEvent.title = '<img class="header-profile-image" alt="' + response.data[i].doctorName + '" ' +
                        'ng-src="' + response.data[i].patientProfileImageURL + '" ' +
                        'src="' + response.data[i].patientProfileImageURL + '"> ' +
                        '<span class="text-primary">' + response.data[i].patientName + '</span>';
                    $scope.doctorEvents.push(newEvent);
                }
            }
        }).error(function (response) {
            $scope.error = response.message;
        });


        $scope.deleteAppointment = function (selectedEvent) {
            $http.delete('/appointment/' + selectedEvent.data._id).success(function (response) {
            }).error(function (response) {
                $scope.error = response.message;
            });
        }

        //Open Appointment Modal***********************************************

        $scope.hstep = 1;
        $scope.mstep = 5;
        $scope.ismeridian = true;
        //Date Picker options
        $scope.dateOptions = {
            startingDay: 1
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

        //Edit Appointments**
        $scope.editAppointment = function(selectedEvent){
            $scope.appointmentDate = $filter('date')(selectedEvent.data.date, "dd-MMMM-yyyy");
            $scope.dateOptions.initDate = $filter('date')(selectedEvent.data.date, "dd-MMMM-yyyy");
            $scope.appointmentModalTitle = "Edit Appointment";
            $scope.selectedPatient = {};
            $scope.appointmentTime =  moment(selectedEvent.data.time, "HH:mm").toDate();
            $scope.appointmentModalInstance = $uibModal.open({
                templateUrl: 'modules/users/client/views/appointment.client.view.html',
                controller: 'DoctorDashboardController',
                scope: $scope
            });
            $scope.appointmentModalInstance.result.then(function (data) {
                var payload = {
                    "patientId": data.patientId,
                    "patientName": data.patientName,
                    "doctorId": Authentication.user.username,
                    "patientProfileImageURL" : data.patientProfileImageURL,
                    "date": data.date,
                    "time": data.time,
                }
                payload.date = new Date(payload.date.getFullYear(), payload.date.getMonth(), payload.date.getDate(),
                    payload.time.getHours(), payload.time.getMinutes(), payload.time.getSeconds());
                var tempTime = "";
                tempTime = $filter('date')(payload.time, "HH:mm");
                console.log(tempTime);
                payload.time = tempTime;
                $http.post('/appointment/'+ selectedEvent.data._id,payload).success(function (response) {
                }).error(function (response) {
                    $scope.error = response.message;
                });
            }, function () {
            });
        }

        
        // Add Appointments**

        //Time Picker Options

        $scope.addAppointment = function () {
            $scope.appointmentDate = new Date();
            $scope.appointmentTime = new Date();
            $scope.appointmentModalTitle = "Add Appointment";
            $scope.selectedPatient = {};

            $scope.appointmentModalInstance = $uibModal.open({
                templateUrl: 'modules/users/client/views/appointment.client.view.html',
                controller: 'DoctorDashboardController',
                scope: $scope
            });
            $scope.appointmentModalInstance.result.then(function (data) {
                var payload = {
                        "patientId": data.patientId,
                        "patientName": data.patientName,
                        "doctorId": Authentication.user.username,
                        "patientProfileImageURL" : data.patientProfileImageURL,
                        "date": data.date,
                        "time": data.time,
                    }
                payload.date = new Date(payload.date.getFullYear(), payload.date.getMonth(), payload.date.getDate(),
                    payload.time.getHours(), payload.time.getMinutes(), payload.time.getSeconds());
                var tempTime = "";
                tempTime = $filter('date')(payload.time, "HH:mm");
                console.log(tempTime);
                payload.time = tempTime;
                $http.post('/appointment',payload).success(function (response) {
                }).error(function (response) {
                    $scope.error = response.message;
                });
            }, function () {
            });
        }

        $scope.saveAppointmentForm = function () {
            $scope.appointmentModalInstance.close({
                "date": $scope.appointmentDate,
                "time": $scope.appointmentTime,
                "patientProfileImageURL" : $scope.selectedPatient.profileImageURL,
                "patientId" : $scope.selectedPatient.patientNameD.username,
                "patientName":  $scope.selectedPatient.patientNameD.displayName
            });
        }

        $scope.closeAppointmentForm = function () {
            $scope.appointmentModalInstance.dismiss('cancel');
        }


        //********************Handle Socket Notifications for Appointments*****************************************

        Socket.on('appointment.added', function (data) {
               $scope.addAppointmentToList(data);
        });

        Socket.on('appointment.updated', function (data) {
            console.log('appointment updated', data);
            angular.forEach($scope.doctorEvents, function (appointmentInfo, index) {
                if (appointmentInfo.data._id == data.appointmentId) {
                    $scope.doctorEvents.splice(index, 1);
                    $scope.addAppointmentToList(data.newAppointment);
                }
            });
        });

        Socket.on('appointment.deleted', function (data) {
            angular.forEach($scope.doctorEvents, function (appointmentInfo, index) {
                if (appointmentInfo.data._id == data) {
                    $scope.doctorEvents.splice(index, 1);
                }
            });
        });

        $scope.addAppointmentToList = function(data){
            var newEvent = {type: 'info', draggable: false, resizable: false, editable: true, deletable:true};
            newEvent.startsAt = moment(data.date);
            newEvent.endsAt = moment(data.date);
            newEvent.data = data;
            newEvent.title =
                '<img class="header-profile-image" ' +
                'ng-src="' + data.patientProfileImageURL + '" ' +
                'src="' + data.patientProfileImageURL + '"> ' +
                '<span class="text-primary">' + data.patientName + '</span>';
            $scope.doctorEvents.push(newEvent);
        }
    }
]);
