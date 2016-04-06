package iot.connect.com.connectoutpatient.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import iot.connect.com.connectoutpatient.activity.MainActivity;

/**
 * Created by Deep on 05-Apr-16.
 */
public class ReadPhoneStatePermissionHandle {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    Context nContext;
    Activity AT;
    public ReadPhoneStatePermissionHandle(Context nContext, Activity AT){
        this.nContext=nContext;
        this.AT=AT;
    }
    public String getDeviceUUID(){
        String UUID=new String();
        if(Build.VERSION.SDK_INT >= 23){
            // Marshmallow +
            int hasPhoneReadStarePermission = nContext.checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            if (hasPhoneReadStarePermission!= PackageManager.PERMISSION_GRANTED){
                AT.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_CODE_ASK_PERMISSIONS);

            }
            TelephonyManager tManager = (TelephonyManager)nContext.getSystemService(Context.TELEPHONY_SERVICE);
            UUID=tManager.getDeviceId();
            Log.d("UUID= ", UUID);
            return UUID;
        }else{
            // Pre-Marshmallow
            TelephonyManager tManager = (TelephonyManager)nContext.getSystemService(Context.TELEPHONY_SERVICE);
            UUID=tManager.getDeviceId();
            Log.d("UUID= ", UUID);
            return UUID;
        }

    }
}
