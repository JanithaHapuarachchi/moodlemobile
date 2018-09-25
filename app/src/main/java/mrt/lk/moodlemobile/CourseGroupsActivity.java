package mrt.lk.moodlemobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mrt.lk.moodlemobile.adapters.CourseGroupsAdapter;
import mrt.lk.moodlemobile.data.CourseGroupItem;
import mrt.lk.moodlemobile.utils.ProgressBarController;

public class CourseGroupsActivity extends AppCompatActivity {

    ProgressBarController prgController;
    TextView txt_course_groups;
    ImageView img_add_course;
    ListView list_course_groups;
    ArrayList<CourseGroupItem>groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_groups);
        getSupportActionBar().hide();
        prgController = new ProgressBarController(this);
        txt_course_groups = (TextView)findViewById(R.id.txt_course_groups);
        img_add_course = (ImageView)findViewById(R.id.img_add_course);
        list_course_groups = (ListView)findViewById(R.id.list_course_groups);

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


    }

    @Override
    protected void onResume() {
        super.onResume();
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
}
