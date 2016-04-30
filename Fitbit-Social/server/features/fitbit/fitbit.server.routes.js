'use strict';

var _               = require('lodash');
var request         = require('request');
var Promise         = require('bluebird');
var BRequest        = Promise.promisify(request);
var kinesis         = require('./kinesis');
var foo             = require('./changeJSON');
var FITBIT_BASE_URL = 'https://api.fitbit.com/1/user/-';

var schedule = require('node-schedule');

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
    var count= 0;

module.exports = function (app, passport) {

  var reauth = false;
  app.get('/auth/fitbit', passport.authenticate('fitbit'));

  app.get('/auth/fitbit/callback', passport.authenticate('fitbit'), function (req, res) {
    if (req.user) {
      res.redirect('/#/user/' + req.user.profile.id);

    } else {
      res.redirect('/');
    }
  });



  // GET FITBIT DATA //

  app.get('/api/v1/data', function (req, res) {

    if (!req.user) {
      res.status(500);
    }

    userCredentials = {
      userId: req.user.profile.id,
      accessToken: req.user.accessToken,
      refreshToken: req.user.refreshToken
    };

    function cronJob(fitbitData){
      if(userCredentials.userId){
        console.log("here");
        console.log(userCredentials.userId + "    ----------");

        var cron = schedule.scheduleJob('* * * * * *', function(){
            var user = fitbitData.profile.user.fullName;
            var sensorid = userCredentials.userId;
            
      var date = new Date();

            //start of getting data from Ftibit

      //  var cronCurrentTime = new Date();
      
      // var cronHour =      cronCurrentTime.getHours();
      
      // var cronMinute = cronCurrentTime.getMinutes();
      
      // heartRateCronOptions         = _.cloneDeep(BASE_OPTIONS);

      // heartRateCronOptions.url         = FITBIT_BASE_URL + '/activities/heart/date/today/1d/1min/time/00:00/'+ cronHour+':'+cronMinute+'.json';

      //   Promise.props({
       
      //   heartRateCronToday: BRequest(heartRateCronOptions),
       
      // })
      // .then(function (results) {

      //   fitbitCronData = {
         
      //     heartRateCronToday: JSON.parse(results.heartRateCronToday.body),
          
      //   };
          
      //   var arr2 = JSON.stringify(fitbitData.heartRateCronToday);


      //   var str2 = arr2.replace(/"activities-heart":/g, '"activitiesOfHeart":');

      //   str2 = str2.replace(/"activities-heart-intraday":/g, '"activitiesHeartIntraday":')

      //   fitbitCronData.heartRateCronToday = str2;
        
      //   console.log("str2      " + str2);
        
      //         var value = JSON.parse(heartRateCronToday);

      // var value2 = value.activitiesOfHeart[0].value;

      // request.post('http://ec2-54-67-123-247.us-west-1.compute.amazonaws.com/api/fitbitdata',{ json: {  
      //                               "username" : user,
      //                               "fitbitUsername" : sensorid,
      //                               "healthdata" : {
      //                                   "timestamp" : date,
      //                                   "value" : value2
      //                               },
      //                       } 
      //                   },
      //                       function (error, response, body) {
      //                           if (!error && response.statusCode == 200) {
      //                               console.log(body);
      //                               console.log("------=-=-=--=-=-=    ");
      //                           }
      //                       });
        

      //   res.status(200).json(fitbitCronData);
      // })
      // .catch(function (error) {

      //   res.status(500).json(error);
      // }) 

      //end of getting data from fitbit

      console.log("sensor ID " + sensorid);
       

           console.log("hello at " + new Date());
    });
  }
}

    console.log("userCredentials.userId     " +  userCredentials.userId);
    // console.log("userCredentials.accessToken   " + userCredentials.accessToken );
  //   function reAuthFitbit(req, res){
  //   if (reauth) {
  //     ModelName.findOne({ user: user }, function (err, fb) {
  //     var option = {
  //       url: "https://api.fitbit.com/oauth2/token",
  //       headers: {
  //         'Authorization' : 'Basic' + userCredentials.refreshToken,
  //         'Content-Type' : "application/x-www-form-urlencoded"
  //       },
  //       method: 'POST',
  //       form: {
  //         "grant_type" : 'refresh_token',
  //         "refresh_token" : fb.refresh_token
  //       }
  //     }
  //     request(option,function(error,response,body){
  //       var b = JSON.parse(body);

  //       if (!error && response.statusCode == 200) {
  //         Fitbit.findOne({user : user}, function(err, fb) {
  //           fb.access_token = b.access_token;
  //           fb.refresh_token = b.refresh_token;
  //           fb.save();
  //         });

  //         return b.access_token
  //         }
  //     });
  //   })}   
  // }
  // expires_in =31536000;


    BASE_OPTIONS = {
      method: 'GET',
      headers: {
        Authorization: 'Bearer ' + userCredentials.accessToken
 
      }
    };

    REFRESH_OPTIONS = {
      method : 'GET',
      headers: {
        Authorization: 'Basic' + userCredentials.refreshToken
      }
    };



// app.get('/data',  function (req, res) {
   




// //       var list = fitbitData.heartRateToday;
// // for(var index in list)
// // {
// //     var obj = list[index];
// //     var name = obj["name"];
// // }

  
//         request({
//   uri: '/data?userid=/' + req.user.profile.id + '&heartRate=' + fitbitData.profile.user.fullName,
//   method: "GET",
//   timeout: 10000,
//   followRedirect: true,
//   maxRedirects: 10
// }, function(error, response, body) {
//   if (!error && response.statusCode == 200){
//   console.log(body);
//   }
// });
    
//     // res.redirect('/data?userid=/' + req.user.profile.id + '&heartRate=' + fitbitData.profile.user.fullName);  
   
//   });
    





    
    var currentTime = new Date(); // for now
    var hour =      currentTime.getHours();
    var minute = currentTime.getMinutes();

      // var cronCurrentTime = new Date();
      
      // var cronHour =      cronCurrentTime.getHours();
      
      // var cronMinute = cronCurrentTime.getMinutes();
      
      // heartRateCronOptions         = _.cloneDeep(BASE_OPTIONS);

      // heartRateCronOptions.url         = FITBIT_BASE_URL + '/activities/heart/date/today/1d/1sec/time/'+(cronHour-1) + ':'+(cronMinute-10)+ '/'+cronHour+':'+cronMinute+'.json';


    var profileOptions           = _.cloneDeep(BASE_OPTIONS);
    profileOptions.url           = FITBIT_BASE_URL + '/profile.json';
    
    var activitiesTodayOptions   = _.cloneDeep(BASE_OPTIONS);
    activitiesTodayOptions.url   = FITBIT_BASE_URL + '/activities/date/today.json';

     heartRateOptions         = _.cloneDeep(BASE_OPTIONS);
    // heartRateOptions.url         = FITBIT_BASE_URL + '/activities/heart/date/2016-03-23/1d/1sec/time/00.00/00.01.json';
    heartRateOptions.url         = FITBIT_BASE_URL + '/activities/heart/date/today/1d/1sec/time/00:00/'+ hour+':'+minute+'.json';

    // heartRateOptions.url         = FITBIT_BASE_URL + '/activities/heart/date/today/1d/1min/time/00:00/00:02.json';


    var heartStreamingData       = _.cloneDeep(BASE_OPTIONS);
    // heartStreamingData.url       =  FITBIT_BASE_URL + '/activities/heart/date/today/1d/1sec/time/00:20/00:30.json';
  
      // heartStreamingData.url       =  FITBIT_BASE_URL + '/activities/heart/date/2016-02-23/today/1min.json';

      heartStreamingData.url       =  FITBIT_BASE_URL + '/activities/heart/date/today/1m.json';

      //GET https://api.fitbit.com/1/user/-/activities/heart/date/[date]/[end-date]/[detail-level]/time/[start-time]/[end-time].json

    var stepsOptions             = _.cloneDeep(BASE_OPTIONS);
    stepsOptions.url             = FITBIT_BASE_URL + '/activities/steps/date/today/7d.json';
    
    var veryActiveMinutesOptions = _.cloneDeep(BASE_OPTIONS);
    veryActiveMinutesOptions.url = FITBIT_BASE_URL + '/activities/minutesVeryActive/date/today/7d.json';
    
    var activitiesOptions        = _.cloneDeep(BASE_OPTIONS);
    activitiesOptions.url        = FITBIT_BASE_URL + '/activities.json';

   
    Promise.props({
        profile: BRequest(profileOptions),
        activitiesToday: BRequest(activitiesTodayOptions),
        heartRateToday: BRequest(heartRateOptions),
      
        heartStreamingData: BRequest(heartStreamingData),
        steps: BRequest(stepsOptions),
        veryActiveMinutes: BRequest(veryActiveMinutesOptions),
        activities: BRequest(activitiesOptions)
      })
      .then(function (results) {

        fitbitData = {
          profile: JSON.parse(results.profile.body),
          activitiesToday: JSON.parse(results.activitiesToday.body),
          heartRateToday: JSON.parse(results.heartRateToday.body),
        
          heartStreamingData: JSON.parse(results.heartStreamingData.body),
          steps: JSON.parse(results.steps.body),
          veryActiveMinutes: JSON.parse(results.veryActiveMinutes.body),
          activities: JSON.parse(results.activities.body)
        };
          // console.log(typeof kinesis.update);
          // console.log("-0-0-0-0-0-0-0-0-0------     "+fitbitData.profile.user.memberSince);
          // console.log("-0-0-0-0-0-0-0-0-0------     "+JSON.stringify(fitbitData.heartRateToday));
          //change(fitbitData.heartRateToday);
          // foo.foo(fitbitData.heartRateToday);



        
        var arr = JSON.stringify(fitbitData.heartRateToday);
        // console.log(arr + "-- arr");

        var str = arr.replace(/"activities-heart":/g, '"activitiesOfHeart":');
        str = str.replace(/"activities-heart-intraday":/g, '"activitiesHeartIntraday":')

        fitbitData.heartRateToday = str;
        // console.log("str     "+str);
        console.log("before str");
        // console.log(str);

         var jiyo = schedule.scheduleJob('*/30 * * * * *', function(){
          sensorid = userCredentials.userId;
          kinesis.update(fitbitData, fitbitData.heartRateToday, sensorid);
                console.log('The answer to life, the universe, and everything!');

        //         var arr2 = JSON.stringify(fitbitData.heartRateCronToday);

        //          var str2 = arr2.replace(/"activities-heart":/g, '"activitiesOfHeart":');

        //          str2 = str2.replace(/"activities-heart-intraday":/g, '"activitiesHeartIntraday":')

        // fitbitData.heartRateCronToday = str2;
        
        // console.log("str2      " + str2);
        
        // var value = JSON.parse(heartRateCronToday);

      // var value3 = JSON.stringify(fitbitData.heartRateCronToday);

      // console.log("-0--0-0-0--3-3-3--3-3-3-3-3-3-3-3-3-3-3------------                         \n"+ value3);

       var cronCurrentTime = new Date();
      
      var cronHour =      cronCurrentTime.getHours();
      
      var cronMinute = cronCurrentTime.getMinutes();
        
      var yesterday = cronCurrentTime.getDate()-1 ;

      var yesterdayMonth = cronCurrentTime.getMonth() + 1;

      var yesterdayYear = cronCurrentTime.getFullYear();

       if (yesterdayMonth < 10){ yesterdayMonth = '0' + yesterdayMonth;}
    if (yesterday < 10) {day = '0' + yesterday; }

    var yes = [yesterdayYear, yesterdayMonth, yesterday].join('-');

    //   function formatDate() {
    //     console.log("heyya");
    // var d = new Date(),
    //     month = '' + (d.getMonth() + 1),
    //     day = '' + (d.getDate() -1),
    //     year = d.getFullYear();

    // if (month.length < 2) month = '0' + month;
    // if (day.length < 2) day = '0' + day;

    // return [year, month, day].join('-');
    //         }
 
    console.log(yes + "yes   ")

      
      heartRateCronOptions2         = _.cloneDeep(BASE_OPTIONS);

      if(count === 0){
        console.log("it comes inside count ===0");
             // heartRateCronOptions2         = _.cloneDeep(BASE_OPTIONS);
      heartRateCronOptions2.url         = FITBIT_BASE_URL + '/activities/heart/date/'+ yes +'/1d/1min/time/00:00/'+cronHour+':'+cronMinute+'.json';

      // heartRateCronOptions2.url         = FITBIT_BASE_URL + '/activities/heart/date/2016-04-24/1d/1min/time/00:00/'+cronHour+':'+cronMinute+'.json';
      console.log("it comes inside count ===0 after .url    " );
      count++;
       console.log("count inside if ==    "+count);
    }else{
                 // heartRateCronOptions2         = _.cloneDeep(BASE_OPTIONS);
        heartRateCronOptions2.url         = FITBIT_BASE_URL + '/activities/heart/date/'+ yes +'/1d/1min/time/00:00/'+cronHour+':'+cronMinute+'.json';
            // heartRateCronOptions2.url         = FITBIT_BASE_URL + '/activities/heart/date/today/1d/1sec/time/'+ previousHour + ':'+previousMinute+ '/'+cronHour+':'+cronMinute+'.json';
            // console.log("  previousHour   " + previousHour + "  previousMinute   "+  previousMinute);
            console.log("   cronHour" + cronHour + "    cronMinute    " + cronMinute);
            count++;
            console.log("count inside else =     "+count);

    }

    // console.log("heartRateCronOptions2   "  + JSON.stringify(heartRateCronOptions2));
       Promise.props({
       
        heartRateCronToday2 : BRequest(heartRateCronOptions2)
      })
      .then(function (results) {

        fitbitCronData = {
        
          heartRateCronToday2: JSON.parse(results.heartRateCronToday2.body)
        };
      })
     //  var value2 = JSON.stringify(fitbitCronData);
     var user = fitbitData.profile.user.fullName;
     var sensorid = userCredentials.userId;
     // console.log(sesnorid);

     //          var array = JSON.stringify(fitbitCronData);
     //          // console.log("array -----   "+array);
     //            if(array !== undefined){
     //              // console.log("array inside if " + array);
     //             var stringo = array.replace(/"activities-heart":/g, '"activitiesOfHeart":');

     //             stringo = stringo.replace(/"activities-heart-intraday":/g, '"activitiesHeartIntraday":')
     //             // console.log("stringo " + stringo);
     //    fitbitCronData.heartRateCronToday2 = stringo;

     //     // console.log("fitbitCronData    " + fitbitCronData.heartRateCronToday2);

     // var check = fitbitCronData.heartRateCronToday2;
     //  // console.log(check.activitiesHeartIntraday.dataset[0].time);
     //        // var check2 = check.activitiesOfHeart[0].value;

     //        // var check3 = JSON.parse(check);

     //        // var check4 = check3.activitiesOfHeart[0].value;
     //        // console.log("check 2 " + check2);
     //        // console.log("check  " + check);
     //        // console.log("activities length  " + check3.activitiesOfHeart[0].value);
     //      }

     // console.log( "fitbitCronData      "+ JSON.stringify(fitbitCronData));


  
     if(fitbitCronData !== undefined)
     {
       var date = new Date();

        // var array = JSON.stringify(fitbitCronData.heartRateCronToday2);
         var array = JSON.stringify(fitbitCronData.heartRateCronToday2);
         // console.log(array + "-----------------------");
        var str = array.replace(/"activities-heart":/g, '"activitiesOfHeart":');
   
        str = str.replace(/"activities-heart-intraday":/g, '"activitiesHeartIntraday":');

        fitbitCronData.heartRateCronToday2 = str;

        var value = JSON.parse(fitbitCronData.heartRateCronToday2);
            console.log('value ' + JSON.stringify(value)  + "----------0o-0o-0o-0o-0o");

            // if(value.hasOwnProperty('activitiesOfHeart') || value.activitiesOfHeart.hasOwnProperty('value')){
              // if(fitbitCronData.heartRateCronToday2["activities-heart-intraday"].dataset.length > 0 && fitbitCronData.heartRateCronToday2["activities-heart-intraday"].dataset.every(e => !!e["time"])){

  // if (fitbitCronData.heartRateCronToday2["activities-heart-intraday"].dataset[0] !== undefined && fitbitCronData.heartRateCronToday2["activities-heart-intraday"].dataset[0].time !== undefined){
          

            if(value !== undefined){
                   // var checkValidity = function (value) {
              //             return value.indexOf('activitiesHeartIntraday') > 0;
              //         }
                    if(! value.hasOwnProperty('categories')){

                    if (value.activitiesHeartIntraday.dataset[0] && value.activitiesHeartIntraday.dataset[0].time){
              // if(value.activitiesHeartIntraday.dataset.length > 0 && value.activitiesHeartIntraday.dataset.every(e => !!e["time"])){
              console.log("inside if of hasOwnProperty");
            if(value.activitiesOfHeart[0].value != undefined){
            // var value2 = value.activitiesOfHeart[0].value;
             // console.log('value \n \n \n' + JSON.stringify(value));
            var value2 = value.activitiesHeartIntraday.dataset[value.activitiesHeartIntraday.dataset.length - 1].value;

            // console.log("value2 -------------------------------      "+value2);

            var value3 = value.activitiesHeartIntraday.dataset[value.activitiesHeartIntraday.dataset.length - 1].time;
                    console.log("value3 -------------------------------      "+value3);
                    // console.log("value3.length-----------     " + value.activitiesHeartIntraday.dataset.length);
                  
                  var result = value3.split(':');
                  console.log("result  " + result);

                        request.post('http://ec2-52-8-186-40.us-west-1.compute.amazonaws.com/fitbitdata',{ json: {  
                                    "username" : user,
                                    "fitbitUsername" : sensorid,
                                    "healthdata" : {
                                        "timestamp" : date,
                                        "value" : value2
                                    },
                            } 
                        },
                            function (error, response, body) {
                                if (!error && response.statusCode == 200) {
                                    console.log(body);
                                    console.log("------=-=-=--=-=-=    ");
                                }
                            });

                    previousHour = result[0];
                    previousMinute = result[1];
                    console.log("previousHour " + previousHour);
                    console.log("previousMinute " + previousMinute);
                    }
                  }
                }
              }
                  console.log("outside if of hasOwnProperty");
             }

    
     // console.log("value2 -------------------------------      "+value2);

//mongodb request

      // request.post('http://ec2-52-8-186-40.us-west-1.compute.amazonaws.com/fitbitdata',{ json: {  
      //                               "username" : user,
      //                               "fitbitUsername" : sensorid,
      //                               "healthdata" : {
      //                                   "timestamp" : date,
      //                                   "value" : value2
      //                               },
      //                       } 
      //                   },
      //                       function (error, response, body) {
      //                           if (!error && response.statusCode == 200) {
      //                               console.log(body);
      //                               console.log("------=-=-=--=-=-=    ");
      //                           }
      //                       });

      //end of mongoDB request    
            // previousHour = cronHour;
            // previousMinute = cronMinute;

          });
       
        // kinesis.update(fitbitData, fitbitData.heartRateToday);
        
        res.status(200).json(fitbitCronData);
      })
      .catch(function (error) {

        res.status(500).json(error);
      }) 
  });
  

      


// //get fitbit data for cron of heart rate
      // var cronCurrentTime = new Date();
      
      // var cronHour =      cronCurrentTime.getHours();
      
      // var cronMinute = cronCurrentTime.getMinutes();
      
      // heartRateCronOptions         = _.cloneDeep(BASE_CRON_OPTIONS);

      // heartRateCronOptions.url         = FITBIT_CRON_BASE_URL + '/activities/heart/date/today/1d/1min/time/00:00/'+ cronHour+':'+cronMinute+'.json';

      //   Promise.props({
       
      //   heartRateCronToday: BRequest(heartRateCronOptions),
       
      // })
      // .then(function (results) {

      //   fitbitCronData = {
         
      //     heartRateCronToday: JSON.parse(results.heartRateCronToday.body),
          
      //   };
      //     // console.log(typeof kinesis.update);
      //     // console.log("-0-0-0-0-0-0-0-0-0------     "+fitbitData.profile.user.memberSince);
      //     // console.log("-0-0-0-0-0-0-0-0-0------     "+JSON.stringify(fitbitData.heartRateToday));
      //     //change(fitbitData.heartRateToday);
      //     // foo.foo(fitbitData.heartRateToday);



        
      //   var arr2 = JSON.stringify(fitbitData.heartRateToday);


      //   var str = arr2.replace(/"activities-heart":/g, '"activitiesOfHeart":');
      //   str = str.replace(/"activities-heart-intraday":/g, '"activitiesHeartIntraday":')

      //   fitbitCronData.heartRateCronToday = str;
        
      //   console.log(str);
      //   kinesis.update(fitbitCronData, fitbitCronData.heartRateCronToday);
        
      //   res.status(200).json(fitbitData);
      // })
      // .catch(function (error) {

      //   res.status(500).json(error);
      // }) 


// end of cron for fitbit data
/*




*/



  // app.get('/user', function(req, res){
  //     userid = req.param.userId;
  //     heartData = fitbitData.heartRateToday.activities-heart[0];
  // });

  // app.get("/refresh",function(req,res){
  //   reauth = true;
  //   reAuthFitbit(req.user);
  //   reauth = false
  // });
  // LOG OUT //



    // var j = schedule.scheduleJob('* * * * * *', function(){
    //         var user = "user1";
    //         var sensorid = userCredentials.userId;

    //         console.log("sensor ID " + sensorid)
    //        // request.post('http://ec2-54-67-123-247.us-west-1.compute.amazonaws.com/api/fitbitdata',{ json: {  
    //        //                          "username" : user,
    //        //                          "fitbitUsername" : sensorid,
    //        //                          "healthdata" : {
    //        //                              "timestamp" : date,
    //        //                              "value" : value2
    //        //                          },
    //        //                  } 
    //        //              },
    //        //                  function (error, response, body) {
    //        //                      if (!error && response.statusCode == 200) {
    //        //                          console.log(body);
    //        //                          console.log("------=-=-=--=-=-=    ");
    //        //                      }
    //        //                  });
        
    //        console.log("hello at " + new Date());
    // });

  app.get('/auth/logout', function (req, res) {
    req.logout();
    console.log('You have logged out');
    res.redirect('/#');
  });

};



