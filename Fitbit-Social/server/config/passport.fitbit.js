'use strict';

var FitbitStrategy = require('passport-fitbit-oauth2').FitbitOAuth2Strategy;
 // var fitbitAuth     = require('./keys/fitbitAuthKeys');

module.exports = function (passport) {

  passport.use(new FitbitStrategy({
    clientID: "227Q9W",
    clientSecret: "79b6714378deb54096378b9ba767927e",
    scope: ['activity', 'profile', 'heartrate', 'weight', 'location', 'settings', 'sleep', 'social'],
    callbackURL: 'http://127.0.0.1:3000/auth/fitbit/callback',
    passReqToCallback: true
  }, function (req, accessToken, refreshToken, profile, done) {
    req.session.fitbitAccessToken = accessToken;

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