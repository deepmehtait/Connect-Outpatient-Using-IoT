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
    var socketio = req.app.get('socketio'); // take out socket instance from the app container
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
                    if(socketio.sockets.connected[newAppointment.patientId]) {
                        socketio.sockets.connected[newAppointment.patientId].emit('appointment.added', data);
                    }
                    if(socketio.sockets.connected[newAppointment.doctorId]){
                        socketio.sockets.connected[newAppointment.doctorId].emit('appointment.added', data);
                    }
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
    var newAppointment = {};
    newAppointment.patientId = req.body.patientId;
    newAppointment.doctorId = req.body.doctorId;
    newAppointment.patientName = req.body.patientName;
    newAppointment.time = req.body.time;
    newAppointment.date = req.body.date;
    var appointmentId = req.params.appointmentId;
    var socketio = req.app.get('socketio'); // take out socket instance from the app container
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
            delete newAppointment._id;
            Appointment.update({_id: appointmentId}, newAppointment ,  function (err, data) {
                if (err) {
                    return res.status(400).send({
                        message: errorHandler.getErrorMessage(err)
                    });
                } else {
                    if(socketio.sockets.connected[newAppointment.patientId]) {
                        newAppointment._id = appointmentId;
                        socketio.sockets.connected[newAppointment.patientId].emit('appointment.updated', {"newAppointment":newAppointment,"appointmentId":appointmentId});
                    }
                    if(socketio.sockets.connected[newAppointment.doctorId]){
                        newAppointment._id = appointmentId;
                        socketio.sockets.connected[newAppointment.doctorId].emit('appointment.updated', {"newAppointment":newAppointment,"appointmentId":appointmentId});
                    }
                    res.json({'Result': 'Appointment updated successfully'});
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
    var socketio = req.app.get('socketio'); // take out socket instance from the app container
    Appointment.remove({_id: appointmentId}, function (err, data) {
        if (err) {
            return res.status(400).send({
                message: errorHandler.getErrorMessage(err)
            });
        } else if(data.result.n > 0) {
            socketio.sockets.emit('appointment.deleted', appointmentId);
            res.json({'Result': 'Appointment deleted successfully'});
        }
        else{
           res.json({'Result': 'Appointment not found'});
        }
    });
};


