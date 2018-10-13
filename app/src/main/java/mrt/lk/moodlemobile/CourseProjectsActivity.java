package mrt.lk.moodlemobile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrt.lk.moodlemobile.adapters.CourseGroupsAdapter;
import mrt.lk.moodlemobile.adapters.GroupProjectsAdapter;
import mrt.lk.moodlemobile.data.CourseGroupItem;
import mrt.lk.moodlemobile.data.GroupProjectItem;
import mrt.lk.moodlemobile.data.ResObject;
import mrt.lk.moodlemobile.utils.Constants;
import mrt.lk.moodlemobile.utils.ProgressBarController;
import mrt.lk.moodlemobile.utils.Utility;
import mrt.lk.moodlemobile.utils.WSCalls;

public class CourseProjectsActivity extends AppCompatActivity {

    ProgressBarController prgController;
    WSCalls wsCalls;
    TextView txt_group_name;
    ImageView img_add_project;
    ListView list_group_projects;
    ArrayList<GroupProjectItem> projects;
    GroupProjectItem newItem;
    GroupProjectsAdapter groupProjectsAdapter;
    static String SELECTED_GROUP_ID;
    static String SELECTED_GROUP_NAME;

    Dialog addProjectDialog;
    String entered_project_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_projects);
        getSupportActionBar().hide();
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
//        } else {
//            SELECTED_GROUP_ID= (String) savedInstanceState.getSerializable("SELECTED_GROUP_ID");
//            SELECTED_GROUP_NAME= (String) savedInstanceState.getSerializable("GIVEN_GROUP_NAME");
//        }

        prgController = new ProgressBarController(this);
        txt_group_name = (TextView)findViewById(R.id.txt_group_name);
        img_add_project = (ImageView)findViewById(R.id.img_add_project);
        list_group_projects = (ListView)findViewById(R.id.list_group_projects);

        txt_group_name.setText("Projects of Group: "+SELECTED_GROUP_NAME);

        img_add_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_new_project();
            }
        });



        list_group_projects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(CourseProjectsActivity.this,ProjectWorksActivity.class);
                i.putExtra("SELECTED_GROUP_ID",SELECTED_GROUP_ID);
                i.putExtra("SELECTED_GROUP_NAME",SELECTED_GROUP_NAME);
                i.putExtra("PROJECT_NAME",projects.get(position).project_name);
                i.putExtra("PROJECT_ID",projects.get(position).project_id);
                i.putExtra("IS_PROJECT",true);
                startActivity(i);
            }
        });

        list_group_projects.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(CourseProjectsActivity.this,ProjectMenuActivity.class);
                i.putExtra("SELECTED_GROUP_ID",SELECTED_GROUP_ID);
                i.putExtra("SELECTED_GROUP_NAME",SELECTED_GROUP_NAME);
                i.putExtra("PROJECT_NAME",projects.get(position).project_name);
                i.putExtra("PROJECT_ID",projects.get(position).project_id);
                i.putExtra("IS_PROJECT",true);
                startActivity(i);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
         new CallGroupProjects().execute(SELECTED_GROUP_ID);
//        setSampleData();
//        Log.e("MOODLEMOBILE",projects.toString());
//        groupProjectsAdapter = new GroupProjectsAdapter(getApplicationContext(),projects);
//        list_group_projects.setAdapter(groupProjectsAdapter);
    }

    private void setSampleData(){
        projects = new ArrayList<GroupProjectItem>();
        GroupProjectItem item;
        for(int i=0;i<5;i++){
            item = new GroupProjectItem();
            item.project_id= ""+i;
            item.project_name = "Name : "+(i+1);
            projects.add(item);
        }
    }

    public void add_new_project(){
        final Context context =this;
        addProjectDialog = new Dialog(context);
        addProjectDialog.setContentView(R.layout.layout_add_new_project);
        final EditText project_name = (EditText) addProjectDialog.findViewById(R.id.project_name);
        final ImageView closeicon = (ImageView) addProjectDialog.findViewById(R.id.closeimg);
        closeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProjectDialog.dismiss();
            }
        });

        final Button reset = (Button) addProjectDialog.findViewById(R.id.btn_close);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProjectDialog.dismiss();
            }
        });
        final Button save = (Button) addProjectDialog.findViewById(R.id.btn_add);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (project_name.getText().toString().length() > 1) {
                        entered_project_name = project_name.getText().toString();
                                addProjectDialog.dismiss();
                        new AddGroupProject().execute(SELECTED_GROUP_ID,entered_project_name);
                       // add_new_project_to_list("33");
                       //         Utility.showMessage("Successfully Added the Project", context);

                    } else {
                        Utility.showMessage("Project Name Required", context);
                    }
                } catch (Exception e) {
                    Utility.showMessage("Error", context);
                }
            }
        });
        //dialog.setContentView(view);
        addProjectDialog.setCancelable(false);
        addProjectDialog.show();
    }

    private void add_new_project_to_list(String project_id){
        newItem = new GroupProjectItem();
        newItem.project_name = entered_project_name;
        newItem.project_id = project_id;
        if(projects == null){
            projects = new ArrayList<GroupProjectItem>();
        }
        projects.add(newItem);
        groupProjectsAdapter = new GroupProjectsAdapter(getApplicationContext(),projects);
        list_group_projects.setAdapter(groupProjectsAdapter);
    }

    private void populate_projects(String msg){
        Log.e("MOODLEMOBILE",msg);
        projects = new ArrayList<GroupProjectItem>();
        GroupProjectItem item;
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                JSONArray jarray =  jo.getJSONArray("data");
                JSONObject jobj;

                for(int i=0; i< jarray.length();i++){
                    jobj = jarray.getJSONObject(i);
                    item  =new GroupProjectItem();
                    item.project_id = jobj.getString("projectid");
                    item.project_name = jobj.getString("projectname");
                    projects.add(item);
                }
            }
            else{
                Utility.showMessage(jo.getString("msg"),getApplicationContext());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        groupProjectsAdapter = new GroupProjectsAdapter(getApplicationContext(),projects);
        list_group_projects.setAdapter(groupProjectsAdapter);
    }

    private void populate_add_project(String msg){
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                add_new_project_to_list(jo.getString("data"));
                Utility.showMessage(jo.getString("msg"),getApplicationContext());
            }
            else{
                Utility.showMessage(jo.getString("msg"),getApplicationContext());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class CallGroupProjects extends AsyncTask <String,Void,ResObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Loading Projects ...");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            prgController.hideProgressBar();
            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
                populate_projects(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            return wsCalls.course_group_projects(params[0]);
        }
    }

    class AddGroupProject extends AsyncTask<String,Void,ResObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Sending Data ...");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            prgController.hideProgressBar();
            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
                populate_add_project(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            return wsCalls.add_group_project(params[0],params[1]);
        }
    }
}
