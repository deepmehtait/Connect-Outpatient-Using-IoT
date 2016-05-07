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
      var socketio = req.app.get('socketio');
      var fitbit_data = new Fitbitdata(req.body);
      var message = null;
      Fitbitdata.update({ fitbitUsername: fitbit_data.fitbitUsername },
         { $push: { 'healthdata': fitbit_data.healthdata } }, { upsert: true }, function (err, data) {
           if (err) {
             return res.status(400).send({
               message: errorHandler.getErrorMessage(err)
             });
           } else {
             socketio.emit('FitbitData.received', fitbit_data.healthdata);
             res.json({ 'Result': 'Fitbit data stored successfully' });
           }
         });
    };


    /**
     * Get user's fitbit data
     */
    exports.getFitBitData = function (req, res) {
        var userId = req.params.patientId;
        Fitbitdata.find({ username: userId },'healthdata', function (err, data) {
                if (err) {
                    return res.status(400).send({
                        message: errorHandler.getErrorMessage(err)
                    });
                } else if(data && data[0]) {
                    res.json({ 'healthdata' : data[0].healthdata.slice(-10) });
                }
                else{
                    res.json({ 'Result': 'No data found!'  });
                }
            });
    };




