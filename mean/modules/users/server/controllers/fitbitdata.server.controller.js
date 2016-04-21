    'use strict';

    /**
    * Module dependencies.
    */
    var path = require('path'),
      mongoose = require('mongoose'),
      Fitbitdata = mongoose.model('Fitbitdata'),
      errorHandler = require(path.resolve('./modules/core/server/controllers/errors.server.controller'));
    /**
    * Store user's fitbit data
    */
    exports.addFitBitData = function (req, res) {
        // Init Variables
      var fitbit_data = new Fitbitdata(req.body);
      var message = null;
      Fitbitdata.update({ fitbitUsername: fitbit_data.fitbitUsername },
         { $push: { 'healthdata': fitbit_data.healthdata } }, { upsert: true }, function (err, data) {
           if (err) {
             return res.status(400).send({
               message: errorHandler.getErrorMessage(err)
             });
           } else {
             res.json({ 'Result': 'Fitbit data stored successfully' });
           }
         });
    };



