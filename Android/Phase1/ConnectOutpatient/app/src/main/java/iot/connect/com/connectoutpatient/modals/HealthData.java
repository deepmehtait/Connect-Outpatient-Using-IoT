package iot.connect.com.connectoutpatient.modals;

import java.util.ArrayList;

/**
 * Created by Deep on 08-May-16.
 */
public class HealthData {
    ArrayList<GraphDataPoints> healthdata;
    String Result;
    String minValue;
    String maxValue;

    public String getAvgValue() {
        return avgValue;
    }

    public void setAvgValue(String avgValue) {
        this.avgValue = avgValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    String avgValue;

    public ArrayList<GraphDataPoints> getHealthdata() {
        return healthdata;
    }

    public void setHealthdata(ArrayList<GraphDataPoints> healthdata) {
        this.healthdata = healthdata;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }
}
