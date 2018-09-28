package mrt.lk.moodlemobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.data.ResObject;
import mrt.lk.moodlemobile.utils.Constants;
import mrt.lk.moodlemobile.utils.ProgressBarController;
import mrt.lk.moodlemobile.utils.Utility;
import mrt.lk.moodlemobile.utils.WSCalls;

public class LoginActivity extends AppCompatActivity {

    ProgressBarController prgController;
    EditText txt_username,txt_password;
    Button btn_login;
    WSCalls ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_username = (EditText)findViewById(R.id.txt_username);
        txt_password = (EditText)findViewById(R.id.txt_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        prgController = new ProgressBarController(this);
        ws = new WSCalls(getApplicationContext());
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = txt_username.getText().toString();
                String pswrd  =txt_password.getText().toString();
               // gotoMenu();
                // startActivity(new Intent(getApplicationContext(),CourseGroupsActivity.class));
                checkLogin(uname,pswrd);



            }
        });
    }

    public void checkLogin(String uname,String pswrd) {
        if (uname.equals("") || pswrd.equals("")) {
            Utility.showMessage("Username or Password Cannot be Empty", getApplicationContext());
        }
        else{
            new CallAuthenticate().execute(uname,pswrd);
        }
    }

    public void validateLogin(String response){
        try {
            JSONObject obj = new JSONObject(response);
            if(obj.has("error")){
                Utility.showMessage(obj.getString("error"),getApplicationContext());
            }
            else{
                LoggedUser.id = txt_username.getText().toString();
                gotoMenu();
            }
        } catch (JSONException e) {
            Utility.showMessage(e.getMessage(),getApplicationContext());
        }

    }

    class CallAuthenticate extends AsyncTask<String,Void,ResObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Requesting...");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            Log.e("RES", response.toString());
            prgController.hideProgressBar();
            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
               validateLogin(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }

            //gotoMenu();
        }

        @Override
        protected ResObject doInBackground(String... params) {
         //   return wscalls.autherise_user(params[0],params[1]);
            return ws.checkLogin(params[0],params[1]);
        }
    }

    private void gotoMenu(){
        Intent in =  new Intent(getApplicationContext(),CourseGroupsActivity.class);
        in.putExtra(Constants.SHOULD_SYNC_AGAIN,true);
        startActivity(in);
    }

    class CallUserData extends AsyncTask<String,Void,ResObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Requesting...");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
                checkAutehnticate(response);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
            Log.e("RES", response.toString());
            prgController.hideProgressBar();
        }

        @Override
        protected ResObject doInBackground(String... params) {
            //   return wscalls.autherise_user(params[0],params[1]);
            return new ResObject();
        }
    }

    public void checkAutehnticate(ResObject resObject){
        try {
            JSONObject jsonResponse = new JSONObject(resObject.msg);
            if(Utility.isJSONKeyAvailable(jsonResponse,"authenticated")){
                if(jsonResponse.getBoolean("authenticated")){
                  //  Utility.setCurrentUser(getApplicationContext(),currentUser);
                    Intent in =  new Intent(getApplicationContext(),CourseGroupsActivity.class);
                    in.putExtra(Constants.SHOULD_SYNC_AGAIN,true);
                    startActivity(in);
                }
                else{
                    Utility.showMessage("LoginActivity Failed",getApplicationContext());
                }
            }
            else if(Utility.isJSONKeyAvailable(jsonResponse,"defaultUserMessage")){
                Utility.showMessage(jsonResponse.getString("defaultUserMessage"),getApplicationContext());
            }

        } catch (JSONException e) {
            Utility.showMessage(e.getMessage(),getApplicationContext());
            //  e.printStackTrace();
        }

    }

}
