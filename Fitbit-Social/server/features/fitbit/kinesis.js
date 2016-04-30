var AWS = require('aws-sdk'); 
var awsAccessKeyId = '';       
var awsSecretKey = '';  
var awsRegion = 'us-east-1';           
var lambdaFunction = ''; 
var kinesisStream = ''; 
AWS.config.update({accessKeyId: awsAccessKeyId, secretAccessKey: awsSecretKey, region: awsRegion});
var lambda = new AWS.Lambda();
var kinesis = new AWS.Kinesis();
var request = require('request');
var jsonQuery = require('json-query');
var _ = require('lodash');


module.exports = {        
   update : function update(fitbitData, heartRateToday, sensorid) {
        console.log("it comes here");
            var date = new Date(); 
            // var sensorid = fitbitData.profile.user.fullName;
            var sensoridd = sensorid;
            var number = 70;
            var value = JSON.parse(heartRateToday);

            var value2 = value.activitiesOfHeart[0].value;
            console.log("value.activitiesOfHeart[0].value   " + value.activitiesOfHeart[0].value) ;
            var user = "user1";
            console.log("Done");
            console.log(value.activitiesOfHeart[0].value);
            console.log("After dones");
        //  var v =   _.map(heartRateToday, 'activitiesOfHeart[0].value');

            
         //    console.log(v);
           
           //  uncomment after getting value
             // request.post('http://ec2-52-8-186-40.us-west-1.compute.amazonaws.com/fitbitdata',{ json: {  
             //                        "username" : user,
             //                        "fitbitUsername" : sensorid,
             //                        "healthdata" : {
             //                            "timestamp" : date,
             //                            "value" : value2
             //                        },
             //                } 
             //            },
             //                function (error, response, body) {
             //                    if (!error && response.statusCode == 200) {
             //                        console.log(body);
             //                        console.log("------=-=-=--=-=-=    ");
             //                    }
             //                });

        //uncomment before this

            
        console.log(sensoridd + "sen");

             var x ="72";
            var params = {
                Data: value2,
                PartitionKey: sensoridd,
                StreamName: kinesisStream
            };

            kinesis.putRecord(params, function(err, data) {
                console.log(sensorid + "  sensorid");
                console.log("----------------------here-----------------");
                if (err) {console.log(err + " " + err.stack); } // an error occurred
                else     {

                    //console.log("-----   "+JSON.stringify(data)); 
                    // console.log(heartRateToday);
                    request.post('http://52.8.186.40/fitbitdata',{ json: {  
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


                }  // successful response
            });
        }
}
