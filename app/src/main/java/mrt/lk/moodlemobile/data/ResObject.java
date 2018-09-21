package mrt.lk.moodlemobile.data;

/**
 * Created by janithamh on 8/12/18.
 */

public class ResObject {

    public String validity;
    public String msg;
    public Object result = null;

    @Override
    public String toString() {
        return "validity: "+validity+" msg: "+msg +" result: "+result;
    }
}
