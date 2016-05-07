        var AWS = require('aws-sdk');
        var awsAccessKeyId = '';
        var awsSecretKey = '';
        var awsRegion = 'us-east-1';
        var lambdaFunction = '';
        var kinesisStream = '';
        AWS.config.update({
            accessKeyId: awsAccessKeyId,
            secretAccessKey: awsSecretKey,
            region: awsRegion
        });
        var lambda = new AWS.Lambda();
        var kinesis = new AWS.Kinesis();
        var request = require('request');
        var jsonQuery = require('json-query');
        var _ = require('lodash');


        module.exports = {



            update: function update(fitbitData, heartRateToday, sensorid, value2) {
                console.log("it comes here");
                var date = new Date();
                var sensoridd = sensorid;
                var vString = value2.toString();

                var user = "user1";



                //    console.log(v);

                //  uncomment after getting value
                // request.post('http://ec2-52-8-186-40.us-west-1.compute.amazonaws.com/fitbitdata', {
                //         json: {
                //             "username": user,
                //             "fitbitUsername": sensorid,
                //             "healthdata": {
                //                 "timestamp": date,
                //                 "value": value2
                //             },
                //         }
                //     },
                //     function(error, response, body) {
                //         if (!error && response.statusCode == 200) {
                //             console.log(body);
                //         }
                //     });

                //uncomment before this


                // uncomment after this if putting on kinesis
                //  var number ="72";
                console.log(vString);
                var params = {
                    Data: vString,
                    PartitionKey: sensoridd,
                    StreamName: kinesisStream
                };

                kinesis.putRecord(params, function(err, data) {


                    if (err) {console.log(err + " " + err.stack); } // an error occurred
                    else     {

                        //console.log("-----   "+JSON.stringify(data)); 
                        // console.log(heartRateToday);
                        request.post('http://52.8.186.40/fitbitdata',{ json: {  
                                        "username" : user,
                                        "fitbitUsername" : sensorid,
                                        "healthdata" : {
                                            "timestamp" : date,
                                            "value" : vString
                                        },
                                } 
                            },
                                function (error, response, body) {
                                    if (!error && response.statusCode == 200) {
                                        console.log(body);
                         
                                    }
                                });


                    }  
                });
        
                // uncomment before this if putting on kinesis
            }


        }