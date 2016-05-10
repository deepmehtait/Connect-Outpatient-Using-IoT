'use strict';

var FitbitStrategy = require('passport-fitbit-oauth2').FitbitOAuth2Strategy;
 // var fitbitAuth     = require('./keys/fitbitAuthKeys');

module.exports = function (passport) {

  passport.use(new FitbitStrategy({
    clientID: "",
    clientSecret: "",
    scope: ['activity', 'profile', 'heartrate', 'weight', 'location', 'settings', 'sleep', 'social'],
    expires_in : "259200",
    prompt : "login",
    callbackURL: 'http://:3000/auth/fitbit/callback',
    passReqToCallback: true
  }, function (req, accessToken, refreshToken, profile, done) {
    req.session.fitbitAccessToken = accessToken;
    req.session.fitbitRequestToken = refreshToken;

    done(null, {
      accessToken: accessToken,
      refreshToken: refreshToken,
      profile: profile
    });
  }));


  passport.serializeUser(function (user, done) {
    done(null, user);
  });

  passport.deserializeUser(function (obj, done) {
    done(null, obj);
  });
};