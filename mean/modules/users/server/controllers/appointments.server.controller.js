'use strict';

/**
 * Module dependencies.
 */
var path = require('path'),
    mongoose = require('mongoose'),
    errorHandler = require(path.resolve('./modules/core/server/controllers/errors.server.controller')),
    User = mongoose.model('User'),
    Appointment = mongoose.model('Appointment');


/**
 * Create a Appointment
 */
exports.addAppointment = function (req, res) {
    var newAppointment = new Appointment(req.body);
    User.findOne({username: newAppointment.doctorId}, 'username displayName address city state zipcode profileImageURL hospitalName phoneNumber', function (err, doctorInfo) {
        if (err) {
            return res.status(400).send({
                message: errorHandler.getErrorMessage(err)
            });
        } else {
            newAppointment.doctorPhone = doctorInfo.phoneNumber;
            newAppointment.doctorName = doctorInfo.displayName;
            newAppointment.locationName = doctorInfo.hospitalName;
            newAppointment.address = doctorInfo.address;
            newAppointment.city = doctorInfo.city;
            newAppointment.state = doctorInfo.state;
            newAppointment.zipcode = doctorInfo.zipcode;
            newAppointment.doctorProfileImageURL = doctorInfo.profileImageURL;
            newAppointment.save(function (err, data) {
                if (err) {
                    return res.status(400).send({
                        message: errorHandler.getErrorMessage(err)
                    });
                } else {
                    res.json({'Result': 'Appointment added successfully', 'data': data});
                }
            });
        }
    });
};

/**
 * Show the current Appointments
 */
exports.getAppointments = function (req, res) {
    var userId = req.params.uId;
    console.log(userId);
    Appointment.find({$or: [{patientId: userId}, {doctorId: userId}]}, function (err, data) {
        if (err) {
            return res.status(400).send({
                message: errorHandler.getErrorMessage(err)
            });
        } else {
            res.json({'data':data});
        }
    });
};

/**
 * Update a Appointment
 */
exports.updateAppointment = function (req, res) {
    var newAppointment = new Appointment(req.body);
    var appointmentId = req.params.appointmentId;
    User.findOne({username: newAppointment.doctorId}, 'username displayName address city state zipcode profileImageURL hospitalName phoneNumber', function (err, doctorInfo) {
        if (err) {
            return res.status(400).send({
                message: errorHandler.getErrorMessage(err)
            });
        } else {
            newAppointment.doctorPhone = doctorInfo.phoneNumber;
            newAppointment.doctorName = doctorInfo.displayName;
            newAppointment.locationName = doctorInfo.hospitalName;
            newAppointment.address = doctorInfo.address;
            newAppointment.city = doctorInfo.city;
            newAppointment.state = doctorInfo.state;
            newAppointment.zipcode = doctorInfo.zipcode;
            newAppointment.doctorProfileImageURL = doctorInfo.profileImageURL;
            console.log(newAppointment);
            Appointment.update({_id: appointmentId}, newAppointment , function (err, data) {
                if (err) {
                    return res.status(400).send({
                        message: errorHandler.getErrorMessage(err)
                    });
                } else {
                    res.json({'Result': 'Appointment updated successfully', 'data': data});
                }
            });
        }
    });
};

/**
 * Delete an Appointment
 */
exports.deleteAppointment = function (req, res) {
    var appointmentId = req.params.appointmentId;
    Appointment.remove({_id: appointmentId}, function (err, data) {
        if (err) {
            return res.status(400).send({
                message: errorHandler.getErrorMessage(err)
            });
        } else if(data.result.n > 0) {
            res.json({'Result': 'Appointment deleted successfully'});
        }
        else{
           res.json({'Result': 'Appointment not found'});
        }
    });
};


