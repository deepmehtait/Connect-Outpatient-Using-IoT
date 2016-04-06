var gcm = require('node-gcm');
var message = new gcm.Message();
message.addNotification({
  title: 'Attention Needed',
  body: 'Patient is in serious condition.!',
  icon: 'ic_stat_emergency'
});
var regTokens = ['doFQIS89FJg:APA91bGc0giN2yc3JWzff08pPZyFe5TBY-T3Wd6d5-74zpOs0J1jbjfM1Gp5zaKLojebu4ahxEPYJVpZXbxSuMcLys0eOUA2sekkuVTr-l1vVnFp98mkivGF7xcdfBBNVFgEJAcocu8j'];
var sender = new gcm.Sender('AIzaSyB9qyp0uN4IqiMIq7SRE8g5B-iOUp8VfS4');
sender.send(message, { registrationTokens: regTokens }, function (err, response) {
	if(err) console.error(err);
	else 	console.log(response);
});