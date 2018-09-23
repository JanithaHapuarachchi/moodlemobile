package mrt.lk.moodlemobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mrt.lk.moodlemobile.adapters.GroupStudentInfoAdapter;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.utils.ProgressBarController;

public class GroupStudentsEvaluateActivity extends AppCompatActivity {

    static String SELECTED_GROUP_ID,SELECTED_GROUP_NAME,PROJECT_NAME,PROJECT_ID;
    ProgressBarController prgController;
    TextView txt_title;
    ListView list_evaluate_students;
    GroupStudentInfoAdapter adapter;
    ArrayList<ParticipantItem> participants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_students_evaluate);


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

        setSampleData();
        adapter = new GroupStudentInfoAdapter(getApplicationContext(),participants, GroupStudentInfoAdapter.EVALUATION_GROUP);
        list_evaluate_students.setAdapter(adapter);
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
}
