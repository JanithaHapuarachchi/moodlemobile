package mrt.lk.moodlemobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import mrt.lk.moodlemobile.adapters.AssignEvalutionGroupAdapter;
import mrt.lk.moodlemobile.adapters.EvaluationResultsAdapter;
import mrt.lk.moodlemobile.data.CourseGroupItem;
import mrt.lk.moodlemobile.data.EvaluationResultItem;
import mrt.lk.moodlemobile.utils.ProgressBarController;

public class StudentEvaluationResultsActivity extends AppCompatActivity {
    String PARTICIPANT_ID,PARTICIPANT_NAME;
    TextView txt_title;
    ExpandableListView list_evaluation_results;
    ProgressBarController prgController;
    ArrayList<String> header;
    HashMap<String,ArrayList<EvaluationResultItem>> childrendata;
    ArrayList<EvaluationResultItem>studentevaluations,teacherevaluations;
    EvaluationResultsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_evaluation_results);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            PARTICIPANT_ID= "";
            PARTICIPANT_NAME= "";
        } else {
            PARTICIPANT_ID= extras.getString("PARTICIPANT_ID");
            PARTICIPANT_NAME= extras.getString("PARTICIPANT_NAME");

        }
        getSupportActionBar().hide();
        prgController = new ProgressBarController(this);
        txt_title = (TextView)findViewById(R.id.txt_title);
        list_evaluation_results = (ExpandableListView)findViewById(R.id.list_evaluation_results);
        txt_title.setText("Evaluation Results for "+PARTICIPANT_NAME);
        setSampleData();
        adapter = new EvaluationResultsAdapter(getApplicationContext(),header,childrendata);
        list_evaluation_results.setAdapter(adapter);
    }

    private void setSampleData(){
        header = new ArrayList<String>();
        header.add("Student Evaluations");
        header.add("Teacher Evaluations");
        studentevaluations = new ArrayList<EvaluationResultItem>();
        EvaluationResultItem item;
        for(int i=0;i<10;i++){
            item = new EvaluationResultItem();
            item.participant_id= ""+i;
            item.participant_name = "N : "+(i+1);
            item.project_id = ""+(i+1);
            item.project_name ="P :"+(i*i+1);
            item.marks =""+(i+20);
            item.comments = "comment";
            studentevaluations.add(item);
        }

        teacherevaluations = new ArrayList<EvaluationResultItem>();
        for(int i=0;i<2;i++){
            item = new EvaluationResultItem();
            item.participant_id= ""+i;
            item.participant_name = "N : "+(i+1);
            item.project_id = ""+(i+1);
            item.project_name ="P :"+(i*i+1);
            item.marks =""+(i+20);
            item.comments = "comment";
            teacherevaluations.add(item);
        }
        childrendata = new HashMap<String, ArrayList<EvaluationResultItem>>();
        childrendata.put(header.get(0),studentevaluations);
        childrendata.put(header.get(1),teacherevaluations);

    }
}
