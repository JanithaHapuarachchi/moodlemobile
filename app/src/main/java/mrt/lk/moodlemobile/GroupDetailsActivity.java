package mrt.lk.moodlemobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrt.lk.moodlemobile.adapters.GroupStudentInfoAdapter;
import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.data.ResObject;
import mrt.lk.moodlemobile.utils.Constants;
import mrt.lk.moodlemobile.utils.ProgressBarController;
import mrt.lk.moodlemobile.utils.Utility;
import mrt.lk.moodlemobile.utils.WSCalls;

public class GroupDetailsActivity extends AppCompatActivity {

    WSCalls wsCalls;
    static ProgressBarController prgController;
    TextView group_name;
    Button btn_confirm;
    ListView list_students;
    ArrayList<ParticipantItem> participants,original_participants;
    GroupStudentInfoAdapter infoAdapter;
    FloatingActionButton add_students;
    public static String SELECTED_GROUP_ID,SELECTED_GROUP_NAME;
    public static boolean IS_EVALUATE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        prgController = new ProgressBarController(this);
        group_name = (TextView)findViewById(R.id.group_name);
        btn_confirm =  (Button) findViewById(R.id.btn_confirm);
        list_students = (ListView)findViewById(R.id.list_students);
        add_students = (FloatingActionButton)findViewById(R.id.add_students);
        wsCalls = new WSCalls(getApplicationContext());

      //  if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                SELECTED_GROUP_ID= "";
                SELECTED_GROUP_NAME= "";

            } else {
                SELECTED_GROUP_ID= extras.getString("SELECTED_GROUP_ID");
                SELECTED_GROUP_NAME= extras.getString("SELECTED_GROUP_NAME");
            }

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConfirmGroup().execute(SELECTED_GROUP_ID);
            }
        });

//        } else {
//            SELECTED_GROUP_ID= (String) savedInstanceState.getSerializable("SELECTED_GROUP_ID");
//            SELECTED_GROUP_NAME= (String) savedInstanceState.getSerializable("GIVEN_GROUP_NAME");
//        }
        getSupportActionBar().setTitle(SELECTED_GROUP_NAME);
        group_name.setText(SELECTED_GROUP_NAME);
        new CallGroupStudens().execute(SELECTED_GROUP_ID);
      //  setSampleData();
      //  infoAdapter = new GroupStudentInfoAdapter(getApplicationContext(),participants, GroupStudentInfoAdapter.GENERAL_GROUP);
      //  list_students.setAdapter(infoAdapter);
        add_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupDetailsActivity.this, CreateGroupActivity.class);
                i.putExtra("IS_CREATE_GROUP",false);
                i.putExtra("GIVEN_GROUP_NAME",SELECTED_GROUP_NAME);
                i.putExtra("GIVEN_GROUP_ID",SELECTED_GROUP_ID);
                startActivity(i);
            }
        });

        if(LoggedUser.roleshortname.equals(LoggedUser.AS_EVALUATE)){
            btn_confirm.setVisibility(View.VISIBLE);
        }
        else{
            btn_confirm.setVisibility(View.GONE);
        }
    }

    private void setSampleData(){
        participants = new ArrayList<ParticipantItem>();
        ParticipantItem item;
        for(int i=0;i<5;i++){
            item = new ParticipantItem();
            item.id= ""+i;
            item.name = "Name : "+(i+1);
            //item.isSelected = true;
            participants.add(item);
        }
        original_participants = participants;
    }

    private void populate_confirm(String msg){
        try {
            JSONObject jo  = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                btn_confirm.setEnabled(false);
            }
            Utility.showMessage(jo.getString("msg"),getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void showPrg(String msg){
        prgController.showProgressBar(msg);
    }

    public static void hidePrg(){
        prgController.hideProgressBar();
    }

    class ConfirmGroup extends AsyncTask<String,Void,ResObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Confirming...");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            prgController.hideProgressBar();
            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
                populate_confirm(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            return wsCalls.confirm_group_students(params[0]);
        }
    }

    private void populate_Students(String msg){
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                JSONArray ja = jo.getJSONArray("data");
                JSONObject j;
                participants = new ArrayList<ParticipantItem>();
                ParticipantItem pi;
                for(int i=0; i< ja.length();i++){
                       j = ja.getJSONObject(i);
                        pi = new ParticipantItem();
                    pi.firstname = j.getString("firstname");
                    pi.lastname = j.getString("lastname");
                    pi.id = j.getString("participantid");
                    participants.add(pi);
                }
                infoAdapter = new GroupStudentInfoAdapter(getApplicationContext(),participants, GroupStudentInfoAdapter.GENERAL_GROUP);
                list_students.setAdapter(infoAdapter);
            }
            else{
                Utility.showMessage(jo.getString("msg"),getApplicationContext());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class CallGroupStudens extends  AsyncTask <String,Void,ResObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Loading...");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            prgController.hideProgressBar();
            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
                populate_Students(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            return wsCalls.course_group_students(params[0]);
        }
    }
}
