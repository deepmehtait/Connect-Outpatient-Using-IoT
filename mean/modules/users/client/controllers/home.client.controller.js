'use strict';

angular.module('core').controller('HomeController', ['$scope', '$http', 'Socket', 'Authentication',
    function ($scope, $http, Socket, Authentication) {
        // This provides Authentication context.
        $scope.authentication = Authentication;

        /*$scope.labels = [0];
         $scope.data = [0];
         for(var i=1 ; i< 25; i++){
         $scope.labels.push(i);
         $scope.data.push(70+(i*2));
         }*/

        $http.get('/medication/' + Authentication.user.username).success(function (response) {
            $scope.clientMedications = [];
            if (response instanceof Array) {
                $scope.clientMedications = response;
            }
        }).error(function (response) {
            $scope.error = response.message;
        });

        //Handle socket events
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

        //Live Heart Rate data
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
            if(response && response.healthdata) {
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
            if(data.timestamp) {
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

        //Appointments
        $scope.calendarView = 'month';
        $scope.calendarDate = new Date();
        $scope.events = [
            {
                title: 'An event',
                type: 'warning',
                startsAt: moment().startOf('week').subtract(2, 'days').add(8, 'hours').toDate(),
                endsAt: moment().startOf('week').add(1, 'week').add(9, 'hours').toDate(),
                draggable: true,
                resizable: true
            }, {
                title: '<i class="glyphicon glyphicon-asterisk"></i> <span class="text-primary">Another event</span>, with a <i>html</i> title',
                type: 'info',
                startsAt: moment().subtract(1, 'day').toDate(),
                endsAt: moment().add(5, 'days').toDate(),
                draggable: true,
                resizable: true
            }, {
                title: 'This is a really long event title that occurs on every year',
                type: 'important',
                startsAt: moment().startOf('day').add(7, 'hours').toDate(),
                endsAt: moment().startOf('day').add(19, 'hours').toDate(),
                recursOn: 'year',
                draggable: true,
                resizable: true
            }
        ];
    }
]);
