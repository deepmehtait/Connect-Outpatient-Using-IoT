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
    update : function update(fitbitData, heartRateToday) {
    // alert("hello");
    var date = new Date();
    var sensorid = fitbitData.profile.user.fullName;
    var number = 70;
    var value = JSON.parse(heartRateToday);
    var value2 = value.activitiesOfHeart[0].value;
    console.log(heartRateToday + value.activitiesOfHeart[0].value);
    var user = "user1";
    console.log("Done");
    console.log(value.activitiesOfHeart[0].value);
    console.log("After dones");
    
    var params = {
            Data: JSON.stringify({sensorid: sensorid, value: value, timestamp: new Date().toISOString()}),
            PartitionKey: sensorid,
            StreamName: kinesisStream
        };

        kinesis.putRecord(params, function(err, data) {
            console.log("----------------------here-----------------");
            if (err) {console.log(err + " " + err.stack); } // an error occurred
            else     {

        console.log("-----   "+JSON.stringify(data)); 
        console.log(heartRateToday);
        request.post('http://ec2-54-67-123-247.us-west-1.compute.amazonaws.com/api/fitbitdata',{ json: {  
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
}
}
