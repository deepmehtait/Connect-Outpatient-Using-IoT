'use strict';

var _ = require('lodash');
var request = require('request');
var Promise = require('bluebird');
var BRequest = Promise.promisify(request);
var kinesis = require('./kinesis');
var FITBIT_BASE_URL = 'https://api.fitbit.com/1/user/-';
var schedule = require('node-schedule');
var http = require('http');
// var FitbitClient = require('fitbit-client-oauth2');
// var client = new FitbitClient("227L55", "b455bffa7bf89e24a5a87114cf725aca");
var clientID = "227L55";
var clientSecret = "b455bffa7bf89e24a5a87114cf725aca";
var fitbitData;
var fitbitCronData;
var heartRateOptions;
var heartRateCronOptions;
var heartRateCronOptions2;
var heartRateCronToday;
var heartRateCronToday2;
var userCredentials;
var previousTime;
var previousHour;
var previousMinute;
var sensorid;
var BASE_OPTIONS;
var REFRESH_OPTIONS;
var count = 0;

module.exports = function(app, passport) {

    var reauth = false;
    app.get('/auth/fitbit', passport.authenticate('fitbit'));

    app.get('/auth/fitbit/callback', passport.authenticate('fitbit'), function(req, res) {
        if (req.user) {
            res.redirect('/#/user/' + req.user.profile.id);

        } else {
            res.redirect('/');
        }
    });



    // GET FITBIT DATA //

    app.get('/api/v1/data', function(req, res) {

        if (!req.user) {
            res.status(500);
        }

        userCredentials = {
            userId: req.user.profile.id,
            accessToken: req.user.accessToken,
            refreshToken: req.user.refreshToken,
            expires_in: 3600
        };

        console.log(userCredentials.refreshToken);

        function cronJob(fitbitData) {
            if (userCredentials.userId) {
                console.log("here");

                var cron = schedule.scheduleJob('* * * * *', function() {
                    var user = fitbitData.profile.user.fullName;
                    var sensorid = userCredentials.userId;

                    var date = new Date();




                });
            }
        }
        BASE_OPTIONS = {
            method: 'GET',
            headers: {
                Authorization: 'Bearer ' + userCredentials.accessToken

            }
        };

        REFRESH_OPTIONS = {
            method: 'GET',
            headers: {
                Authorization: 'Basic ' + userCredentials.refreshToken
            }
        };

        var currentTime = new Date(); // for now
        var hour = currentTime.getHours();
        var minute = currentTime.getMinutes();
        // heartRateCronOptions         = _.cloneDeep(BASE_OPTIONS);

        // heartRateCronOptions.url         = FITBIT_BASE_URL + '/activities/heart/date/today/1d/1sec/time/'+(cronHour-1) + ':'+(cronMinute-10)+ '/'+cronHour+':'+cronMinute+'.json';


        var profileOptions = _.cloneDeep(BASE_OPTIONS);
        profileOptions.url = FITBIT_BASE_URL + '/profile.json';

        var activitiesTodayOptions = _.cloneDeep(BASE_OPTIONS);
        activitiesTodayOptions.url = FITBIT_BASE_URL + '/activities/date/today.json';

        heartRateOptions = _.cloneDeep(BASE_OPTIONS);
        // heartRateOptions.url         = FITBIT_BASE_URL + '/activities/heart/date/2016-03-23/1d/1sec/time/00.00/00.01.json';
        heartRateOptions.url = FITBIT_BASE_URL + '/activities/heart/date/today/1d/1sec/time/00:00/' + hour + ':' + minute + '.json';

        // heartRateOptions.url         = FITBIT_BASE_URL + '/activities/heart/date/today/1d/1min/time/00:00/00:02.json';


        var heartStreamingData = _.cloneDeep(BASE_OPTIONS);
        // heartStreamingData.url       =  FITBIT_BASE_URL + '/activities/heart/date/today/1d/1sec/time/00:20/00:30.json';

        // heartStreamingData.url       =  FITBIT_BASE_URL + '/activities/heart/date/2016-02-23/today/1min.json';

        heartStreamingData.url = FITBIT_BASE_URL + '/activities/heart/date/today/1m.json';

        //GET https://api.fitbit.com/1/user/-/activities/heart/date/[date]/[end-date]/[detail-level]/time/[start-time]/[end-time].json

        // var stepsOptions             = _.cloneDeep(BASE_OPTIONS);
        // stepsOptions.url             = FITBIT_BASE_URL + '/activities/steps/date/today/7d.json';

        // var veryActiveMinutesOptions = _.cloneDeep(BASE_OPTIONS);
        // veryActiveMinutesOptions.url = FITBIT_BASE_URL + '/activities/minutesVeryActive/date/today/7d.json';

        var activitiesOptions = _.cloneDeep(BASE_OPTIONS);
        activitiesOptions.url = FITBIT_BASE_URL + '/activities.json';


        Promise.props({
                profile: BRequest(profileOptions),

                activitiesToday: BRequest(activitiesTodayOptions),

                heartRateToday: BRequest(heartRateOptions),

                heartStreamingData: BRequest(heartStreamingData),

                activities: BRequest(activitiesOptions)
            })
            .then(function(results) {

                fitbitData = {
                    profile: JSON.parse(results.profile.body),

                    activitiesToday: JSON.parse(results.activitiesToday.body),

                    heartRateToday: JSON.parse(results.heartRateToday.body),

                    heartStreamingData: JSON.parse(results.heartStreamingData.body),

                    activities: JSON.parse(results.activities.body)
                };


                var arr =  JSON.stringify(fitbitData.heartRateToday);
                // console.log(arr);
                var str = arr.replace(/"activities-heart":/g, '"activitiesOfHeart":');
                str = str.replace(/"activities-heart-intraday":/g, '"activitiesHeartIntraday":')

                fitbitData.heartRateToday = str;

                //refresh access token
                var Base64={_keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",encode:function(e){var t="";var n,r,i,s,o,u,a;var f=0;e=Base64._utf8_encode(e);while(f<e.length){n=e.charCodeAt(f++);r=e.charCodeAt(f++);i=e.charCodeAt(f++);s=n>>2;o=(n&3)<<4|r>>4;u=(r&15)<<2|i>>6;a=i&63;if(isNaN(r)){u=a=64}else if(isNaN(i)){a=64}t=t+this._keyStr.charAt(s)+this._keyStr.charAt(o)+this._keyStr.charAt(u)+this._keyStr.charAt(a)}return t},decode:function(e){var t="";var n,r,i;var s,o,u,a;var f=0;e=e.replace(/[^A-Za-z0-9+/=]/g,"");while(f<e.length){s=this._keyStr.indexOf(e.charAt(f++));o=this._keyStr.indexOf(e.charAt(f++));u=this._keyStr.indexOf(e.charAt(f++));a=this._keyStr.indexOf(e.charAt(f++));n=s<<2|o>>4;r=(o&15)<<4|u>>2;i=(u&3)<<6|a;t=t+String.fromCharCode(n);if(u!=64){t=t+String.fromCharCode(r)}if(a!=64){t=t+String.fromCharCode(i)}}t=Base64._utf8_decode(t);return t},_utf8_encode:function(e){e=e.replace(/rn/g,"n");var t="";for(var n=0;n<e.length;n++){var r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r)}else if(r>127&&r<2048){t+=String.fromCharCode(r>>6|192);t+=String.fromCharCode(r&63|128)}else{t+=String.fromCharCode(r>>12|224);t+=String.fromCharCode(r>>6&63|128);t+=String.fromCharCode(r&63|128)}}return t},_utf8_decode:function(e){var t="";var n=0;var r=c1=c2=0;while(n<e.length){r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r);n++}else if(r>191&&r<224){c2=e.charCodeAt(n+1);t+=String.fromCharCode((r&31)<<6|c2&63);n+=2}else{c2=e.charCodeAt(n+1);c3=e.charCodeAt(n+2);t+=String.fromCharCode((r&15)<<12|(c2&63)<<6|c3&63);n+=3}}return t}}

                var concat = clientID + ":" + clientSecret;
                var encoded = Base64.encode(concat);

                var refresh = schedule.scheduleJob('*/45 * * * *', function() {
                    try {
                        console.log("inside try block ");
                        request({
                            url: 'https://api.fitbit.com/oauth2/token',
                            method: 'POST',
                            headers: {
                                Authorization: "Basic " + encoded
                            },
                            form: {
                                grant_type: 'refresh_token',
                                refresh_token: userCredentials.refreshToken
                            }
                        }, function(error, response, body) {
                            if (error) {
                                console.log(error);
                            } else {
                                var b = JSON.parse(body);
                                userCredentials.accessToken = b.access_token;
                                userCredentials.refreshToken = b.refresh_token;
                                console.log(userCredentials.refreshToken);
                                console.log(response.statusCode);

                            }
                        });


                    } catch (err) {
                        console.log('error refreshing user token', err);
                    };

                });


                var pushData = schedule.scheduleJob('* * * * *', function() {
                    sensorid = userCredentials.userId;
                    console.log(userCredentials.refreshToken);
                    console.log(BASE_OPTIONS);
                    var cronCurrentTime = new Date();

                    var cronHour = cronCurrentTime.getHours();

                    var cronMinute = cronCurrentTime.getMinutes();

                    var yesterday = cronCurrentTime.getDate() - 1;

                    var yesterdayMonth = cronCurrentTime.getMonth() + 1;

                    var yesterdayYear = cronCurrentTime.getFullYear();

                    if (yesterdayMonth < 10) {
                        yesterdayMonth = '0' + yesterdayMonth;
                    }
                    if (yesterday < 10) {
                        yesterday = '0' + yesterday;
                    }

                    var yes = [yesterdayYear, yesterdayMonth, yesterday].join('-');




                    heartRateCronOptions2 = _.cloneDeep(BASE_OPTIONS);

                    if (count === 0) {
                        // heartRateCronOptions2         = _.cloneDeep(BASE_OPTIONS);
                        heartRateCronOptions2.url = FITBIT_BASE_URL + '/activities/heart/date/' + yes + '/1d/1min/time/00:00/' + cronHour + ':' + cronMinute + '.json';

                        // heartRateCronOptions2.url         = FITBIT_BASE_URL + '/activities/heart/date/2016-04-24/1d/1min/time/00:00/'+cronHour+':'+cronMinute+'.json';
                        count++;
                    } else {
                        // heartRateCronOptions2         = _.cloneDeep(BASE_OPTIONS);
                        heartRateCronOptions2.url = FITBIT_BASE_URL + '/activities/heart/date/' + yes + '/1d/1min/time/00:00/' + cronHour + ':' + cronMinute + '.json';
                        // heartRateCronOptions2.url         = FITBIT_BASE_URL + '/activities/heart/date/today/1d/1sec/time/'+ previousHour + ':'+previousMinute+ '/'+cronHour+':'+cronMinute+'.json';
                        count++;

                    }

                    Promise.props({

                            heartRateCronToday2: BRequest(heartRateCronOptions2)
                        })
                        .then(function(results) {

                            fitbitCronData = {

                                heartRateCronToday2: JSON.parse(results.heartRateCronToday2.body)
                            };
                        })
                    var user = fitbitData.profile.user.fullName;
                    var sensorid = userCredentials.userId;


                    if (fitbitCronData !== undefined) {
                        var date = new Date();

                        var array =  JSON.stringify(fitbitCronData.heartRateCronToday2);
                        var str = array.replace(/"activities-heart":/g, '"activitiesOfHeart":');

                        str = str.replace(/"activities-heart-intraday":/g, '"activitiesHeartIntraday":');

                        fitbitCronData.heartRateCronToday2 = str;

                        var value = JSON.parse(fitbitCronData.heartRateCronToday2);



                        if (value !== undefined) {
                            if (!value.hasOwnProperty('categories')) {

                                if (value.activitiesHeartIntraday.dataset[0] && value.activitiesHeartIntraday.dataset[0].time) {
                                    // console.log("inside if of hasOwnProperty");
                                    if (value.activitiesOfHeart[0].value != undefined) {
                                        var value2 = value.activitiesHeartIntraday.dataset[value.activitiesHeartIntraday.dataset.length - 1].value;


                                        var value3 = value.activitiesHeartIntraday.dataset[value.activitiesHeartIntraday.dataset.length - 1].time;
                                        console.log("value2 " + value2);
                                        var result = value3.split(':');


                                        kinesis.update(fitbitData, fitbitData.heartRateToday, sensorid, value2);




                                        previousHour = result[0];
                                        previousMinute = result[1];

                                    }
                                }
                            }
                        }
                        // console.log("outside if of hasOwnProperty");
                    }
                });


                res.status(200).json(fitbitCronData);
            })
            .catch(function(error) {

                res.status(500).json(error);
            })
    });

    app.get('/auth/logout', function(req, res) {
        req.logout();
        console.log('You have logged out');
        res.redirect('/#');
    });

};