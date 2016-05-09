'use strict';

angular.module('core').controller('HomeController', ['$scope', '$stateParams','$uibModal', '$http', 'Socket', 'Authentication',
    function ($scope, $stateParams, $uibModal,$http, Socket, Authentication) {
        // This provides Authentication context
        $scope.authentication = Authentication;
        $scope.isDoctor = false;
        $scope.patientId = Authentication.user.username;
        $scope.patientHomeTitle = Authentication.user.firstName + "'s Status";
        $scope.patientName = Authentication.user.displayName;

        console.log($stateParams.patientDetail);
        //Set vars if coming from doctor dashboard
        var showListFilter = "";
        if (Authentication.user.roles.indexOf('doctor') > -1) {
            $scope.isDoctor = true;
            showListFilter = Authentication.user.username;
            if($stateParams && $stateParams.patientDetail){
                $scope.patientId = $stateParams.patientDetail.username;
                $scope.patientHomeTitle = $stateParams.patientDetail.displayName + "'s Status";
                $scope.patientName = $stateParams.patientDetail.displayName;
            }
            else{
                $scope.patientHomeTitle = "Current Status";
            }
        }

        //Medication ***********************************************************

        $http.get('/medication/' + $scope.patientId).success(function (response) {
            $scope.clientMedications = [];
            if (response instanceof Array) {
                /*if(showListFilter){
                 var tempMedication = [];
                 for(var i=0; i< response.length ; i++){
                 if(response[i].doctorId == showListFilter){
                 tempMedication.push(response[i]);
                 }
                 }
                 $scope.clientMedications = tempMedication;
                 }
                 else{*/
                $scope.clientMedications = response;
                //}
            }
        }).error(function (response) {
            $scope.error = response.message;
        });


        //Open Medication Modal
        $scope.addMedication = function (){
            $scope.medicationModalTitle = "Add Medication";
            $scope.modalInstance = $uibModal.open({
                templateUrl: 'modules/users/client/views/medication.client.view.html',
                controller: 'HomeController',
                scope:$scope
            });

            $scope.modalInstance.result.then(function (data) {
                var payload = {
                    "patientId": $scope.patientId,
                    "medications": {
                        "name": data.medicationName,
                        "dosage": data.medicationDosage,
                        "doctorId": Authentication.user.username,
                        "company": data.medicationCompany,
                        "day": data.medicationDay,
                        "time": data.medicationTime
                    }
                }
                $http.post('/medication',payload).success(function (response) {
                }).error(function (response) {
                    $scope.error = response.message;
                });
            }, function () {

            });
        }


        $scope.editMedication = function (medicationInfo){
            $scope.medicationModalTitle = "Edit Medication";
            //Set Existing Values
            var oldMedicationName = medicationInfo.name;
            $scope.medicationName = medicationInfo.name;
            $scope.medicationTime = medicationInfo.time;
            $scope.medicationDosage =  medicationInfo.dosage;
            $scope.medicationCompany =  medicationInfo.company;
            $scope.medicationDay =  medicationInfo.day;

            $scope.modalInstance = $uibModal.open({
                templateUrl: 'modules/users/client/views/medication.client.view.html',
                controller: 'HomeController',
                scope:$scope
            });

            $scope.modalInstance.result.then(function (data) {
                var payload = {
                    "patientId": $scope.patientId,
                    "medications": {
                        "name": data.medicationName,
                        "dosage": data.medicationDosage,
                        "doctorId": Authentication.user.username,
                        "company": data.medicationCompany,
                        "day": data.medicationDay,
                        "time": data.medicationTime
                    }
                }
                $http.post('/medication/' + $scope.patientId + '/' + oldMedicationName,payload).success(function (response) {
                }).error(function (response) {
                    $scope.error = response.message;
                });
            }, function () {

            });
        }

        $scope.saveMedication = function () {
            $scope.modalInstance.close({
                "medicationName" : $scope.medicationName,
                "medicationDosage" : $scope.medicationDosage,
                "medicationCompany" : $scope.medicationCompany,
                "medicationDay" : $scope.medicationDay,
                "medicationTime" : $scope.medicationTime
            });
        }

        $scope.closeMedication = function () {
            $scope.modalInstance.dismiss('cancel');
        }

        $scope.deleteMedication = function(medicationInfo){
            console.log(medicationInfo);
            $http.delete('/medication/' + $scope.patientId + '/' + medicationInfo.name).success(function (response) {

            }).error(function (response) {
                $scope.error = response.message;
            });
        }



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

        $scope.addAppointment = function (){
            $scope.appointmentDate = new Date();
            $scope.appointmentDate.setDate($scope.appointmentDate.getDate()-7);
            $scope.appointmentModalTitle = "Add Appointment";
            $scope.appointmentModalInstance = $uibModal.open({
                templateUrl: 'modules/users/client/views/appointment.client.view.html',
                controller: 'HomeController',
                scope:$scope
            });

            $scope.appointmentModalInstance.result.then(function (data) {
                var payload = {
                    "patientId": $scope.patientId,
                    "medications": {
                        "name": data.medicationName,
                        "dosage": data.medicationDosage,
                        "doctorId": Authentication.user.username,
                        "company": data.medicationCompany,
                        "day": data.medicationDay,
                        "time": data.medicationTime
                    }
                }
                $http.post('/medication',payload).success(function (response) {
                }).error(function (response) {
                    $scope.error = response.message;
                });
            }, function () {

            });
        }


        $scope.saveAppointmentForm = function () {
            $scope.appointmentModalInstance.close({



            });
        }

        $scope.closeAppointmentForm = function () {
            $scope.appointmentModalInstance.dismiss('cancel');
        }






        //Handle socket events*************************
        Socket.on('medication.created', function (data) {
            $scope.clientMedications.push({
                "name": data.medications.name,
                "dosage": data.medications.dosage,
                "doctorId": data.medications.doctorId,
                "company": data.medications.company,
                "day": data.medications.day,
                "time": data.medications.time
            });
        });

        Socket.on('medication.removed', function (data) {
            angular.forEach($scope.clientMedications, function (medicationInfo, index) {
                if (medicationInfo.name == data) {
                    $scope.clientMedications.splice(index, 1);
                }
            });
        });

        //Live Heart Rate data***********************
        $scope.series = ['Heart Rate'];
        var tempList = [];
        $scope.labels = [];
        $scope.data = [tempList];
        $scope.chartOptions = {
            scaleOverride: true,
            animation: true,
            scaleSteps: 8,
            scaleStepWidth: 10,
            scaleStartValue: 35
        };

        $http.get('/fitbitdata/' + $scope.patientId).success(function (response) {
            if (response && response.healthdata) {
                for (var i = 0; i < response.healthdata.length; i++) {
                    var tempTimestamp = [];
                    var finalTimestamp = "";
                    if (response.healthdata[i].timestamp) {
                        tempTimestamp = response.healthdata[i].timestamp.split("T")[1];
                        if (tempTimestamp) {
                            var res1 = tempTimestamp.split(":");
                            finalTimestamp = res1[0] + ":" + res1[1];
                        }
                    }
                    $scope.labels.push(finalTimestamp);
                    tempList.push(response.healthdata[i].value);
                }
            }
        }).error(function (response) {
            $scope.error = response.message;
        });

        Socket.on('FitbitData.received', function (data) {
            $scope.chartOptions.animation = false;
            var tempTimestamp = [];
            var finalTimestamp = "";
            if (data.timestamp) {
                tempTimestamp = data.timestamp.split("T")[1];
                if (tempTimestamp) {
                    var res1 = tempTimestamp.split(":");
                    finalTimestamp = res1[0] + ":" + res1[1];
                }
            }
            $scope.labels.push(finalTimestamp);
            tempList.push(data.value);
            if ($scope.labels.length > 10) {
                $scope.labels.shift();
            }
            if (tempList.length > 10) {
                tempList.shift();
            }
        });

        $scope.onClick = function (points, evt) {
            console.log(points, evt);
        };

        //Appointments***********************************
        $scope.calendarView = 'month';
        $scope.calendarDate = new Date();
        $scope.events = [];

        $http.get('/appointment/' + $scope.patientId).success(function (response) {
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


        /*$scope.events = [
         {
         title: '<i class="glyphicon glyphicon-asterisk"></i> <span class="text-primary">Another event</span>, with a <i>html</i> title',
         type: 'info',
         startsAt: moment().subtract(1, 'day').toDate(),
         endsAt: moment().add(5, 'days').toDate(),
         draggable: true,
         resizable: false
         }
         ];*/
    }
]);
