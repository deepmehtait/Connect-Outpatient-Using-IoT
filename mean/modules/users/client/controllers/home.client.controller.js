'use strict';

angular.module('core').controller('HomeController', ['$scope', '$http', 'Socket', 'Authentication',
    function ($scope, $http, Socket, Authentication) {
        // This provides Authentication context.
        $scope.authentication = Authentication;
        $scope.isDoctor = false;
        var showListFilter = "";
        if (Authentication.user.roles.indexOf('doctor') > -1) {
            $scope.isDoctor = true;
            showListFilter = Authentication.user.username;
        }

        $http.get('/medication/' + Authentication.user.username).success(function (response) {
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

        $http.get('/fitbitdata/' + Authentication.user.username).success(function (response) {
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
            console.log($scope.data);
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
                    if (showListFilter && showListFilter == response.data[i].doctorId) {
                        $scope.events.push(newEvent);
                    }
                    else {
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
