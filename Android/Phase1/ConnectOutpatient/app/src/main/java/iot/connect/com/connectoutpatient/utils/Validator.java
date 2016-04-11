package iot.connect.com.connectoutpatient.utils;

/**
 * Created by Deep on 10-Apr-16.
 */
public class Validator {
    // Email Checker
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    // Password Checker
    public final static boolean isValidPassword(CharSequence target) {
        if (target == null)
            return false;

        return (target.length()>=6);
    }
    public final static boolean isValidConfirmPassword(CharSequence target1,CharSequence target2) {
        if (target1 == null || target2 ==null )
            return false;

        return (target1.toString().matches(target2.toString()));
    }
}
