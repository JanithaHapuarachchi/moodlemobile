package mrt.lk.moodlemobile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mrt.lk.moodlemobile.adapters.GroupProjectsAdapter;
import mrt.lk.moodlemobile.adapters.GroupSubProjectAdapter;
import mrt.lk.moodlemobile.data.CourseGroupItem;
import mrt.lk.moodlemobile.data.GroupProjectItem;
import mrt.lk.moodlemobile.data.GroupSubProjectItem;
import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.utils.Utility;

public class ProjectMenuActivity extends AppCompatActivity {

    Button btn_goto_diary,btn_set_evaluation_group,btn_evaluate,btn_contribution_report,btn_final_report;
    ImageView img_add_sub_group;
    ListView list_subprojects;
    ListView list_allocated_groups;
   // GroupProjectsAdapter adapter;
    ArrayList<CourseGroupItem> allocatedgroups;
    ArrayList<GroupSubProjectItem> projects;
    GroupSubProjectAdapter adapter;
    Dialog addProjectDialog;
    String entered_project_name;
    GroupSubProjectItem newItem;
    static String SELECTED_GROUP_ID,SELECTED_GROUP_NAME,PROJECT_NAME,PROJECT_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);

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

        getSupportActionBar().setTitle(PROJECT_NAME);
        btn_goto_diary = (Button)findViewById(R.id.btn_goto_diary);
        btn_set_evaluation_group = (Button)findViewById(R.id.btn_set_evaluation_group);
        btn_evaluate = (Button)findViewById(R.id.btn_evaluate);
        btn_contribution_report = (Button)findViewById(R.id.btn_contribution_report);
        btn_final_report= (Button)findViewById(R.id.btn_final_report);
        img_add_sub_group = (ImageView)findViewById(R.id.img_add_sub_group);
        list_subprojects = (ListView)findViewById(R.id.list_subprojects);

        setSampleData();
        setSampleEvaluationGroups();
        adapter = new GroupSubProjectAdapter(getApplicationContext(),projects);
        list_subprojects.setAdapter(adapter);
        img_add_sub_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_new_subproject();
            }
        });

        btn_goto_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectMenuActivity.this,DiaryActivity.class);
                i.putExtra("SELECTED_PARTICIPANT_ID", LoggedUser.id);
                i.putExtra("SELECTED_PARTICIPANT_NAME",LoggedUser.name);
                i.putExtra("PROJECT_NAME",PROJECT_NAME);
                i.putExtra("PROJECT_ID",PROJECT_ID);
                startActivity(i);
            }
        });

        btn_set_evaluation_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectMenuActivity.this,AssignEvaluationGroupActivity.class);
                i.putExtra("SELECTED_GROUP_ID",SELECTED_GROUP_ID);
                i.putExtra("SELECTED_GROUP_NAME",SELECTED_GROUP_NAME);
                i.putExtra("PROJECT_NAME",PROJECT_NAME);
                i.putExtra("PROJECT_ID",PROJECT_ID);
                startActivity(i);
            }
        });

        btn_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //populate_evaluation_groups();
                Intent i = new Intent(ProjectMenuActivity.this,GroupStudentsEvaluateActivity.class);
                i.putExtra("SELECTED_GROUP_ID",SELECTED_GROUP_ID);
                i.putExtra("SELECTED_GROUP_NAME",SELECTED_GROUP_NAME);
                i.putExtra("PROJECT_NAME",PROJECT_NAME);
                i.putExtra("PROJECT_ID",PROJECT_ID);
                startActivity(i);
            }
        });

        list_subprojects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ProjectMenuActivity.this,ProjectWorksActivity.class);
                i.putExtra("SELECTED_GROUP_ID",SELECTED_GROUP_ID);
                i.putExtra("SELECTED_GROUP_NAME",SELECTED_GROUP_NAME);
                i.putExtra("PROJECT_NAME",projects.get(position).project_name);
                i.putExtra("PROJECT_ID",projects.get(position).project_id);
                i.putExtra("IS_PROJECT",false);
                startActivity(i);
            }
        });
    }

    private void populate_evaluation_groups(){
        final Context context =this;
        addProjectDialog = new Dialog(context);
        addProjectDialog.setContentView(R.layout.layout_allocated_evaluation_groups);
        final ImageView closeicon = (ImageView) addProjectDialog.findViewById(R.id.closeimg);
        list_allocated_groups = (ListView) addProjectDialog.findViewById(R.id.list_allocated_groups);
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





        //dialog.setContentView(view);
        addProjectDialog.setCancelable(false);
        addProjectDialog.show();
    }

    private void setSampleEvaluationGroups(){
        allocatedgroups = new ArrayList<CourseGroupItem>();
        CourseGroupItem item;
        for(int i=0;i<5;i++){
            item = new CourseGroupItem();
            item.group_id= ""+i;
            item.group_name = "Name : "+(i+1);
            allocatedgroups.add(item);
        }
    }


    private void setSampleData(){
        projects = new ArrayList<GroupSubProjectItem>();
        GroupSubProjectItem item;
        for(int i=0;i<5;i++){
            item = new GroupSubProjectItem();
            item.project_id= ""+i;
            item.project_name = "Name : "+(i+1);
            projects.add(item);
        }
    }

    public void add_new_subproject(){
        final Context context =this;
        addProjectDialog = new Dialog(context);
        addProjectDialog.setContentView(R.layout.layout_add_new_project);
        final EditText project_name = (EditText) addProjectDialog.findViewById(R.id.project_name);
        final ImageView closeicon = (ImageView) addProjectDialog.findViewById(R.id.closeimg);
        final TextView add_new_project_title = (TextView) addProjectDialog.findViewById(R.id.add_new_project_title);

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
        add_new_project_title.setText("Add New Sub Project");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (project_name.getText().toString().length() > 1) {
                        entered_project_name = project_name.getText().toString();
                        addProjectDialog.dismiss();
                        add_new_subproject_to_list("33");
                        Utility.showMessage("Successfully Added the Sub Project", context);

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

    private void add_new_subproject_to_list(String project_id){
        newItem = new GroupSubProjectItem();
        newItem.project_name = entered_project_name;
        newItem.project_id = project_id;
        if(projects == null){
            projects = new ArrayList<GroupSubProjectItem>();
        }
        projects.add(newItem);
        adapter = new GroupSubProjectAdapter(getApplicationContext(),projects);
        list_subprojects.setAdapter(adapter);
    }
}
