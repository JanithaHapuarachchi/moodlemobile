package mrt.lk.moodlemobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import mrt.lk.moodlemobile.adapters.AssignEvalutionGroupAdapter;
import mrt.lk.moodlemobile.data.CourseGroupItem;
import mrt.lk.moodlemobile.utils.ProgressBarController;

public class AssignEvaluationGroupActivity extends AppCompatActivity {

    String SELECTED_GROUP_ID,SELECTED_GROUP_NAME,PROJECT_ID,PROJECT_NAME;
    TextView txt_title;
    ExpandableListView list_assign_remove_groups;
    ProgressBarController prgController;
    ArrayList<String> header;
    HashMap<String,ArrayList<CourseGroupItem>>childrendata;
    ArrayList<CourseGroupItem>allocatedgroups,availablegroups;
    AssignEvalutionGroupAdapter adapter;

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
        txt_title = (TextView)findViewById(R.id.txt_title);
        list_assign_remove_groups = (ExpandableListView)findViewById(R.id.list_assign_remove_groups);
        txt_title.setText("Set Evaluation Group for Project "+PROJECT_NAME+" of Group "+SELECTED_GROUP_NAME);
        setSampleData();
        adapter = new AssignEvalutionGroupAdapter(getApplicationContext(),header,childrendata);
        list_assign_remove_groups.setAdapter(adapter);

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
}
