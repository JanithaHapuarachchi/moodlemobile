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
import mrt.lk.moodlemobile.adapters.EvaluationResultsAdapter;
import mrt.lk.moodlemobile.data.CourseGroupItem;
import mrt.lk.moodlemobile.data.EvaluationResultItem;
import mrt.lk.moodlemobile.data.ResObject;
import mrt.lk.moodlemobile.utils.Constants;
import mrt.lk.moodlemobile.utils.ProgressBarController;
import mrt.lk.moodlemobile.utils.Utility;
import mrt.lk.moodlemobile.utils.WSCalls;

public class StudentEvaluationResultsActivity extends AppCompatActivity {
    String PARTICIPANT_ID,PARTICIPANT_NAME;
    TextView txt_title;
    ExpandableListView list_evaluation_results;
    ProgressBarController prgController;
    WSCalls wsCalls;
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
        wsCalls = new WSCalls(getApplicationContext());
        txt_title = (TextView)findViewById(R.id.txt_title);
        list_evaluation_results = (ExpandableListView)findViewById(R.id.list_evaluation_results);
        txt_title.setText("Evaluation Results for "+PARTICIPANT_NAME);

       new CallEvaluationResults().execute(PARTICIPANT_ID);
        //setSampleData();
       // adapter = new EvaluationResultsAdapter(getApplicationContext(),header,childrendata);
        //list_evaluation_results.setAdapter(adapter);
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
        adapter = new EvaluationResultsAdapter(getApplicationContext(),header,childrendata);
        list_evaluation_results.setAdapter(adapter);

    }

    private void populate_results(String msg){

        header = new ArrayList<String>();
        header.add("Student Evaluations");
        header.add("Teacher Evaluations");
        studentevaluations = new ArrayList<EvaluationResultItem>();
        teacherevaluations = new ArrayList<EvaluationResultItem>();
        childrendata = new HashMap<String, ArrayList<EvaluationResultItem>>();
        EvaluationResultItem item;
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                JSONArray ja = jo.getJSONArray("data");
                JSONObject prj,eval;
                JSONArray stds,teachs;
                for(int i=0; i<ja.length();i++){

                    prj = ja.getJSONObject(i);
                    stds = prj.getJSONArray("student_marks");
                    teachs = prj.getJSONArray("teachers_marks");
                    for(int j=0; j<stds.length();j++){
                        eval = stds.getJSONObject(j);
                        item = new EvaluationResultItem();
                        item.participant_id= eval.getString("participant_id");
                        item.participant_name = eval.getString("participant_fname"); //participant_name
                        item.project_id = prj.getString("project_id");
                        item.project_name =prj.getString("project_name");
                        item.marks = eval.getString("marks");
                        item.comments = eval.getString("comment");
                        studentevaluations.add(item);
                    }

                    for(int j=0; j<teachs.length();j++){
                        eval = teachs.getJSONObject(j);
                        item = new EvaluationResultItem();
                        item.participant_id= eval.getString("participant_id");
                        item.participant_name = eval.getString("participant_fname");
                        item.project_id = prj.getString("project_id");
                        item.project_name =prj.getString("project_name");
                        item.marks = eval.getString("marks");
                        item.comments = eval.getString("comment");
                        teacherevaluations.add(item);
                    }

                }

            }
            else{
                Utility.showMessage(jo.getString("msg"),getApplicationContext());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        childrendata.put(header.get(0),studentevaluations);
        childrendata.put(header.get(1),teacherevaluations);
        adapter = new EvaluationResultsAdapter(getApplicationContext(),header,childrendata);
        list_evaluation_results.setAdapter(adapter);
    }

    class CallEvaluationResults extends AsyncTask<String,Void,ResObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Requesting...");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            prgController.hideProgressBar();
            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
                populate_results(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            return wsCalls.participant_evaluation_results(params[0]);
        }
    }
}
