package mrt.lk.moodlemobile.utils;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by janithah on 8/8/2018.
 */

public class Utility {

    static SharedPreferences preferences;
    private static SharedPreferences getSharedPrefs(Context context){
        if(preferences ==null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences  = context.getSharedPreferences(Constants.FLI_SHARED_PREFS, Context.MODE_PRIVATE);
        }
        return preferences;
    }


    public static void showMessage(String txt, Context context) {
        Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
    }

    public static String getLastLoggedDate(Context context){
        SharedPreferences preferences = getSharedPrefs(context);
        return preferences.getString(Constants.LAST_LOGGED_DATE, "");
    }

    public static void clearCurrentUserLoginDate(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.LAST_LOGGED_DATE,"");
        editor.commit();
    }

//
//    public static void setServerDetails(Context context, ServerDetails serverDetails){
//        Log.e("FLI CURRENT USER",serverDetails.toString());
//        SharedPreferences preferences =  getSharedPrefs(context);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(Constants.SERVER_URL, serverDetails.server_name);
//        editor.putString(Constants.SERVER_PORT, serverDetails.server_port);
//        editor.putString(Constants.SERVER_TENANT,serverDetails.server_tenant);
//        editor.commit();
//    }

//    public static boolean setCurrentUser(Context context, CurrentUser currentuser){
//        Log.e("FLI CURRENT USER",currentuser.toString());
//        SharedPreferences preferences =  getSharedPrefs(context);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(Constants.USER_NAME, currentuser.username);
//        editor.putString(Constants.USER_PASSWORD, currentuser.password);
//        editor.putString(Constants.LAST_LOGGED_DATE,currentuser.loggeddate);
//        editor.putString(Constants.AUTHENTICATION_KEY,currentuser.authkey);
//        editor.putString(Constants.USER_ID,currentuser.userid);
//        editor.putString(Constants.STAFF_ID,currentuser.staffid);
//        editor.commit();
//
//        return true;
//    }
//
//    public static ArrayList<JSONObject> getArrayListformJSONARRAY(JSONArray array) throws JSONException {
//        ArrayList<JSONObject> list = new ArrayList<JSONObject>();
//        for(int i=0;i<array.length();i++){
//            list.add(array.getJSONObject(i));
//        }
//        return  list;
//    }
//
//    public static boolean isCurrentUser(Context context,CurrentUser currentUser){
//        SharedPreferences preferences =  getSharedPrefs(context);
//        String current_user_name =  preferences.getString(Constants.USER_NAME, "");
//        String current_user_password = preferences.getString(Constants.USER_PASSWORD, "");
//        if(current_user_name.equals(currentUser.username) && current_user_password.equals(currentUser.password)){
//            currentUser.authkey = getAuthKey(context);
//            currentUser.userid = getUserID(context);
//            return true;
//        }
//        else{
//            return false;
//        }
//
//    }

    public static String getServerURL(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.SERVER_URL, "");
    }

    public static String getServerPORT(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.SERVER_PORT, "");
    }

    public static String getServerTenant(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.SERVER_TENANT, "");
    }

    public static String getStaffID(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.STAFF_ID, "");
    }

    public static String getUserID(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.USER_ID, "");
    }

    public static boolean clearAuthKey(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.AUTHENTICATION_KEY,"");
        editor.commit();
        return true;
    }

    public static String getAuthKey(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.AUTHENTICATION_KEY, "");
    }

    public static String getCurrentUserName(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.USER_NAME, "");
    }
    public static String getCurrentPassword(Context context){
        SharedPreferences preferences =  getSharedPrefs(context);
        return preferences.getString(Constants.USER_PASSWORD, "");
    }

    public static boolean isJSONKeyAvailable(JSONObject jObject,String key){
        if(jObject.has(key)){
            return true;
        }
        return false;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isMajorServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if ("".equals(service.service
                    .getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void stopService(){
        //  SubmitDataService.stopAsync();
    }

//    public static Dialog change_ServerSettings(final Context context){
//        final Dialog dialog = new Dialog(context);
////        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        View view = inf.inflate(R.layout.layout_add_new_project, null);
//        dialog.setContentView(R.layout.layout_add_new_project);
//        final EditText server_name = (EditText) dialog.findViewById(R.id.server_name);
//        final EditText server_port = (EditText) dialog.findViewById(R.id.server_port);
//        final EditText server_tenant = (EditText) dialog.findViewById(R.id.server_tenant);
//        final ImageView closeicon = (ImageView) dialog.findViewById(R.id.closeimg);
//        server_name.setText(Utility.getServerURL(context));
//        server_port.setText(Utility.getServerPORT(context));
//        server_tenant.setText(Utility.getServerTenant(context));
//        closeicon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        final Button reset = (Button) dialog.findViewById(R.id.btn_close);
//        reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        final Button save = (Button) dialog.findViewById(R.id.btn_save);
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    if (server_name.getText().toString().length() > 1) {
//                        if (server_port.getText().toString().length() > 1) {
//                            if (server_tenant.getText().toString().length() > 1) {
//                                ServerDetails sd =new ServerDetails();
//                                sd.server_name = server_name.getText().toString();
//                                sd.server_port = server_port.getText().toString();
//                                sd.server_tenant = server_tenant.getText().toString();
//                                Utility.setServerDetails(context,sd);
//                                dialog.dismiss();
//                                Utility.showMessage("Successfully Saved Data", context);
//                            } else {
//                                Utility.showMessage("Server Tenant Required", context);
//                            }
//                        } else {
//                            Utility.showMessage("Server Port Required", context);
//                        }
//                    } else {
//                        Utility.showMessage("Server Name Required", context);
//                    }
//                } catch (Exception e) {
//                    Utility.showMessage("Error", context);
//                }
//            }
//        });
//        //dialog.setContentView(view);
//        dialog.setCancelable(false);
//        return dialog;
//    }

}
