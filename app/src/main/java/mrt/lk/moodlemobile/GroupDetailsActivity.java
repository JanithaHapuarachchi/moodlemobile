package mrt.lk.moodlemobile;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mrt.lk.moodlemobile.adapters.GroupStudentInfoAdapter;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.utils.ProgressBarController;

public class GroupDetailsActivity extends AppCompatActivity {

    ProgressBarController prgController;
    TextView group_name;
    Button btn_confirm;
    ListView list_students;
    ArrayList<ParticipantItem> participants,original_participants;
    GroupStudentInfoAdapter infoAdapter;
    FloatingActionButton add_students;
    String SELECTED_GROUP_ID,SELECTED_GROUP_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        prgController = new ProgressBarController(this);
        group_name = (TextView)findViewById(R.id.group_name);
        btn_confirm =  (Button) findViewById(R.id.btn_confirm);
        list_students = (ListView)findViewById(R.id.list_students);
        add_students = (FloatingActionButton)findViewById(R.id.add_students);


      //  if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                SELECTED_GROUP_ID= "";
                SELECTED_GROUP_NAME= "";
            } else {
                SELECTED_GROUP_ID= extras.getString("SELECTED_GROUP_ID");
                SELECTED_GROUP_NAME= extras.getString("GIVEN_GROUP_NAME");
            }
//        } else {
//            SELECTED_GROUP_ID= (String) savedInstanceState.getSerializable("SELECTED_GROUP_ID");
//            SELECTED_GROUP_NAME= (String) savedInstanceState.getSerializable("GIVEN_GROUP_NAME");
//        }
        getSupportActionBar().setTitle(SELECTED_GROUP_NAME);
        setSampleData();
        infoAdapter = new GroupStudentInfoAdapter(getApplicationContext(),participants, GroupStudentInfoAdapter.GENERAL_GROUP);
        list_students.setAdapter(infoAdapter);
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
}
