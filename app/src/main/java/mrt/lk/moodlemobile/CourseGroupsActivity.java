package mrt.lk.moodlemobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrt.lk.moodlemobile.adapters.CourseGroupsAdapter;
import mrt.lk.moodlemobile.data.CourseGroupItem;
import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.data.ResObject;
import mrt.lk.moodlemobile.utils.Constants;
import mrt.lk.moodlemobile.utils.ProgressBarController;
import mrt.lk.moodlemobile.utils.Utility;
import mrt.lk.moodlemobile.utils.WSCalls;

public class CourseGroupsActivity extends AppCompatActivity {

    ProgressBarController prgController;
    TextView txt_course_groups;
    ImageView img_add_course;
    ListView list_course_groups;
    ArrayList<CourseGroupItem>groups;
    WSCalls wsCalls;
    RelativeLayout layout_owngroup;
    ImageView img_view_students,img_view_projects;
    TextView txt_course_group;
    CourseGroupItem owngroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_groups);
        getSupportActionBar().hide();
        prgController = new ProgressBarController(this);
        txt_course_groups = (TextView)findViewById(R.id.txt_course_groups);
        img_add_course = (ImageView)findViewById(R.id.img_add_course);
        list_course_groups = (ListView)findViewById(R.id.list_course_groups);
        layout_owngroup = (RelativeLayout) findViewById(R.id.layout_owngroup);
        img_view_students = (ImageView)findViewById(R.id.img_view_students);
        img_view_projects = (ImageView) findViewById(R.id.img_view_projects);
        txt_course_group = (TextView) findViewById(R.id.txt_course_group);

        img_add_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CourseGroupsActivity.this, CreateGroupActivity.class);
                i.putExtra("IS_CREATE_GROUP",true);
                i.putExtra("GIVEN_GROUP_NAME","");
                i.putExtra("GIVEN_GROUP_ID","");
                startActivity(i);
            }
        });

        img_view_projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CourseGroupsActivity.this, CourseProjectsActivity.class);
                i.putExtra("SELECTED_GROUP_ID",owngroup.group_id);
                i.putExtra("SELECTED_GROUP_NAME",owngroup.group_name);
                i.putExtra("IS_EVALUATE",false);
                LoggedUser.status = LoggedUser.AS_STUDENT;
                startActivity(i);
            }
        });
        img_view_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CourseGroupsActivity.this, GroupDetailsActivity.class);
                i.putExtra("SELECTED_GROUP_ID",owngroup.group_id);
                i.putExtra("SELECTED_GROUP_NAME",owngroup.group_name);
                i.putExtra("IS_EVALUATE",false);
                LoggedUser.status = LoggedUser.AS_STUDENT;
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //new LoadParticipanDetails().execute(LoggedUser.id);
        setSampleData();
        list_course_groups.setAdapter(new CourseGroupsAdapter(getApplicationContext(),groups, CourseGroupsAdapter.GENERAL_GROUP));
    }

    private void setSampleData(){
        groups = new ArrayList<CourseGroupItem>();
        CourseGroupItem item;
        for(int i=0;i<10;i++){
            item = new CourseGroupItem();
            item.group_id= ""+i;
            item.group_name = "Name : "+(i+1);
            groups.add(item);
        }
    }

    private void populate_details(String msg){
        try {
            JSONObject jo = new JSONObject(msg);
            JSONObject jpersonal,jownGroup;
            JSONObject jdata,jGroup;
            JSONArray jgroups;
            CourseGroupItem item;
            if(jo.getString("msg").equals("Success")){
                jdata = jo.getJSONObject("data");
                jpersonal = jdata.getJSONObject("personal");
                LoggedUser.course_id = jpersonal.getString("courseid");
                LoggedUser.status = jdata.getString("roleshortname");
                LoggedUser.roleshortname = jdata.getString("roleshortname");
                owngroup = new CourseGroupItem();
                jownGroup = jdata.getJSONObject("owngroup");
                if(jownGroup == null || jownGroup.length() == 0){
                    layout_owngroup.setVisibility(View.GONE);
                }
                else{
                    layout_owngroup.setVisibility(View.VISIBLE);
                    owngroup.group_id = jownGroup.getString("gid");
                    owngroup.group_name = jownGroup.getString("groupname");

                }
                jgroups = jdata.getJSONArray("assigned_groups");
                groups = new ArrayList<CourseGroupItem>();
                if(jownGroup == null || jownGroup.length() == 0) {
                    jgroups =new JSONArray();
                }
                for(int  i=0; i<jgroups.length();i++){
                    jGroup = jgroups.getJSONObject(i);
                    item =new CourseGroupItem();
                    item.group_id = jGroup.getString("groupid");
                    item.group_name= jGroup.getString("groupname");
                    groups.add(item);
                }
                list_course_groups.setAdapter(new CourseGroupsAdapter(getApplicationContext(),groups, CourseGroupsAdapter.GENERAL_GROUP));

            }
            else{
                Utility.showMessage(jo.getString("msg"),getApplicationContext());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class LoadParticipanDetails extends AsyncTask<String,Void,ResObject>{
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
                populate_details(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            return wsCalls.participant_details(params[0]);
        }
    }

}
