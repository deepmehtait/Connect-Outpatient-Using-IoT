package iot.connect.com.connectoutpatient.modals;

/**
 * Created by Deep on 04-May-16.
 */
public class AddMedication {
    String patientId;
    Medication medications;

    public Medication getMedications() {
        return medications;
    }

    public void setMedications(Medication medications) {
        this.medications = medications;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientID) {
        this.patientId = patientID;
    }
}
