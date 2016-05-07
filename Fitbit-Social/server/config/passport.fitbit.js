'use strict';

var FitbitStrategy = require('passport-fitbit-oauth2').FitbitOAuth2Strategy;
 // var fitbitAuth     = require('./keys/fitbitAuthKeys');

module.exports = function (passport) {

  passport.use(new FitbitStrategy({
    clientID: "227KT9",
    clientSecret: "8dc1ea69daacbe7b4af8ef3ae94cf7d5",
    scope: ['activity', 'profile', 'heartrate', 'weight', 'location', 'settings', 'sleep', 'social'],
    expires_in : "259200",
    prompt : "login",
    callbackURL: 'http://127.0.0.1:3000/auth/fitbit/callback',
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