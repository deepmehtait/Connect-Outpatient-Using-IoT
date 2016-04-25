package iot.connect.com.connectoutpatient.modals;

import java.util.ArrayList;

/**
 * Created by Deep on 24-Apr-16.
 */
public class MyPatientList {
    ArrayList<MyPatientListDetails> data=new ArrayList<MyPatientListDetails>();
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<MyPatientListDetails> getData() {
        return data;
    }

    public void setData(ArrayList<MyPatientListDetails> data) {
        this.data = data;
    }
}
