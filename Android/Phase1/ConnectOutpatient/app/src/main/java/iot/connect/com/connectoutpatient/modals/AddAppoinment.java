package iot.connect.com.connectoutpatient.modals;

/**
 * Created by Deep on 05-May-16.
 */
public class AddAppoinment {
    String patientId,doctorId,patientName,patientProfileImageURL,time,date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientProfileImageURL() {
        return patientProfileImageURL;
    }

    public void setPatientProfileImageURL(String patientProfileImageURL) {
        this.patientProfileImageURL = patientProfileImageURL;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
