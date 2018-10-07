package mrt.lk.moodlemobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class GroupStudentsEvaluateActivity extends AppCompatActivity {

    static String SELECTED_GROUP_ID,SELECTED_GROUP_NAME,PROJECT_NAME,PROJECT_ID;
    ProgressBarController prgController;
    TextView txt_title;
    ListView list_evaluate_students;
    GroupStudentInfoAdapter adapter;
    ArrayList<ParticipantItem> participants;
    WSCalls wsCalls;
    GroupStudentInfoAdapter infoAdapter;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_students_evaluate);

        wsCalls = new WSCalls(getApplicationContext());
        prgController = new ProgressBarController(this);
        txt_title = (TextView)findViewById(R.id.txt_title);
        list_evaluate_students = (ListView)findViewById(R.id.list_evaluate_students);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            SELECTED_GROUP_ID= "";
            SELECTED_GROUP_NAME= "";
            PROJECT_ID ="";
            PROJECT_NAME ="";
        } else {
            SELECTED_GROUP_ID= extras.getString("SELECTED_GROUP_ID");
            SELECTED_GROUP_NAME= extras.getString("GIVEN_GROUP_NAME");
            PROJECT_ID = extras.getString("PROJECT_ID");
            PROJECT_NAME = extras.getString("PROJECT_NAME");
        }
        txt_title.setText("Evaluate Project "+PROJECT_NAME+" for Group "+SELECTED_GROUP_NAME);

        list_evaluate_students.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParticipantItem p = participants.get(position);
                Intent i = new Intent(GroupStudentsEvaluateActivity.this,DiaryActivity.class);
                i.putExtra("SELECTED_PARTICIPANT_ID", p.id);
                i.putExtra("SELECTED_PARTICIPANT_NAME",p.name);
                i.putExtra("PROJECT_NAME",PROJECT_NAME);
                i.putExtra("PROJECT_ID",PROJECT_ID);
                startActivity(i);
            }
        });
        new CallGroupStudens().execute(SELECTED_GROUP_ID);
       // setSampleData();
     //   adapter = new GroupStudentInfoAdapter(getApplicationContext(),participants, GroupStudentInfoAdapter.EVALUATION_GROUP);
      //  list_evaluate_students.setAdapter(adapter);
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
                adapter = new GroupStudentInfoAdapter(getApplicationContext(),participants, GroupStudentInfoAdapter.EVALUATION_GROUP);
                list_evaluate_students.setAdapter(adapter);
            }
            else{
                Utility.showMessage(jo.getString("msg"),getApplicationContext());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class CallGroupStudens extends AsyncTask<String,Void,ResObject> {

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
