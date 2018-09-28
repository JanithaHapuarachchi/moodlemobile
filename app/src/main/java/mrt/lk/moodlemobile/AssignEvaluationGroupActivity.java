package mrt.lk.moodlemobile;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import mrt.lk.moodlemobile.adapters.AssignEvalutionGroupAdapter;
import mrt.lk.moodlemobile.data.CourseGroupItem;
import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.data.ResObject;
import mrt.lk.moodlemobile.utils.Constants;
import mrt.lk.moodlemobile.utils.ProgressBarController;
import mrt.lk.moodlemobile.utils.Utility;
import mrt.lk.moodlemobile.utils.WSCalls;

public class AssignEvaluationGroupActivity extends AppCompatActivity {

    public static String SELECTED_GROUP_ID,SELECTED_GROUP_NAME,PROJECT_ID,PROJECT_NAME;
    TextView txt_title;
    ExpandableListView list_assign_remove_groups;
    public static ProgressBarController prgController;
    ArrayList<String> header;
    HashMap<String,ArrayList<CourseGroupItem>>childrendata;
    ArrayList<CourseGroupItem>allocatedgroups,availablegroups;
    AssignEvalutionGroupAdapter adapter;
    WSCalls wsCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_evaluation_group);
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
        getSupportActionBar().hide();
        prgController = new ProgressBarController(this);
        wsCalls = new WSCalls(getApplicationContext());
        txt_title = (TextView)findViewById(R.id.txt_title);
        list_assign_remove_groups = (ExpandableListView)findViewById(R.id.list_assign_remove_groups);
        txt_title.setText("Set Evaluation Group for Project "+PROJECT_NAME+" of Group "+SELECTED_GROUP_NAME);
       // callData();
        setSampleData();
        adapter = new AssignEvalutionGroupAdapter(getApplicationContext(),header,childrendata);
        list_assign_remove_groups.setAdapter(adapter);

    }


    public void callData(){
        header = new ArrayList<String>();
        header.add("Current Evaluators");
        header.add("Available Groups");
        allocatedgroups = new ArrayList<CourseGroupItem>();
        availablegroups = new ArrayList<CourseGroupItem>();
        childrendata = new HashMap<String, ArrayList<CourseGroupItem>>();
        new CallCourseGroups().execute("");
    }

    public static void show_pgr(String msg){
        prgController.showProgressBar(msg);
    }

    public static void hide_pgr(){
        prgController.hideProgressBar();
    }

    private void setSampleData(){
        header = new ArrayList<String>();
        header.add("Current Evaluators");
        header.add("Available Groups");
        allocatedgroups = new ArrayList<CourseGroupItem>();
        CourseGroupItem item;
        for(int i=0;i<10;i++){
            item = new CourseGroupItem();
            item.group_id= ""+i;
            item.group_name = "Name : "+(i+1);
            allocatedgroups.add(item);
        }

        availablegroups = new ArrayList<CourseGroupItem>();
        for(int i=0;i<10;i++){
            item = new CourseGroupItem();
            item.group_id= ""+i;
            item.group_name = "N : "+(i+1);
            availablegroups.add(item);
        }
        childrendata = new HashMap<String, ArrayList<CourseGroupItem>>();
        childrendata.put(header.get(0),allocatedgroups);
        childrendata.put(header.get(1),availablegroups);

    }
    private void populate_all_data(){
        adapter = new AssignEvalutionGroupAdapter(getApplicationContext(),header,childrendata);
        list_assign_remove_groups.setAdapter(adapter);
    }

    private void populate_eval_groups(String msg){
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                JSONArray ja = jo.getJSONArray("data");
                CourseGroupItem item;
                JSONObject j;
                if(ja ==null){
                    ja = new JSONArray();
                }
                for(int i=0; i<ja.length();i++){
                    j = ja.getJSONObject(i);
                    item = new CourseGroupItem();
                    item.group_id = j.getString("groupid");
                    item.group_name = j.getString("groupname");
                    allocatedgroups.add(item);
                }

            }
            else{

            }
            childrendata.put(header.get(0),allocatedgroups);
            populate_all_data();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populate_course_groups(String msg){
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                JSONArray ja = jo.getJSONArray("data");
                CourseGroupItem item;
                JSONObject j;
                if(ja ==null){
                    ja = new JSONArray();
                }

                for(int i=0; i<ja.length();i++){
                    j = ja.getJSONObject(i);
                    item = new CourseGroupItem();
                    item.group_id = j.getString("gid");
                    item.group_name = j.getString("groupname");
                    availablegroups.add(item);
                }
                childrendata.put(header.get(1),availablegroups);
                new CallEvaluationGroups().execute("");
            }
            else{
                Utility.showMessage(jo.getString("msg"),getApplicationContext());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class CallCourseGroups extends AsyncTask<String,Void,ResObject>{
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
                populate_course_groups(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            return wsCalls.course_groups(LoggedUser.course_id);
        }
    }

    class CallEvaluationGroups extends AsyncTask <String,Void,ResObject>{
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
                populate_eval_groups(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            return wsCalls.allocated_evaluation_groups(SELECTED_GROUP_ID, LoggedUser.id,LoggedUser.roleshortname,PROJECT_ID);
        }
    }
}
