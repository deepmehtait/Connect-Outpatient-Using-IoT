var gcm = require('node-gcm');
var message = new gcm.Message();
message.addNotification({
  title: 'Attention Needed',
  body: 'Patient is in serious condition.!',
  icon: 'ic_stat_emergency'
});
var regTokens = ['fdHdJlYZabA:APA91bGv7Y3b6yPHMpsZ5D5cWX6SQjkU9HCovFKfv3J5G1k4JXhHIapC_tPvFTs8Mcrbpnxoavk5AENPs8HNah-k5Lz1H5f0jRC4EnZFaeO4b76ACQ3Ngnn3sop_wkpR1f7IRr7Va-S2'];
var sender = new gcm.Sender('AIzaSyB9qyp0uN4IqiMIq7SRE8g5B-iOUp8VfS4');
var projectID="1051405988728";
sender.send(message, { registrationTokens: regTokens }, function (err, response) {
	if(err) console.error(err);
	else 	console.log(response);
});