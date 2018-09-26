package mrt.lk.moodlemobile;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import mrt.lk.moodlemobile.adapters.DairyAdapter;
import mrt.lk.moodlemobile.adapters.ProjectWorksAdapter;
import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.data.ResObject;
import mrt.lk.moodlemobile.data.WorkCommentItem;
import mrt.lk.moodlemobile.data.WorkSeenItem;
import mrt.lk.moodlemobile.utils.Constants;
import mrt.lk.moodlemobile.utils.ProgressBarController;
import mrt.lk.moodlemobile.utils.Utility;
import mrt.lk.moodlemobile.utils.WSCalls;

public class DiaryActivity extends AppCompatActivity {

    Button btn_mark,btn_send;
    EditText txt_message;
    ListView list_works;
    ProgressBarController prgController;
    WSCalls wsCalls;
    static String SELECTED_PARTICIPANT_ID,SELECTED_PARTICIPANT_NAME,PROJECT_NAME,PROJECT_ID;
    ArrayList<WorkCommentItem> works;
    WorkCommentItem work;
    DairyAdapter adapter;

    Dialog evaluateStudentDialog;
    String entered_result,entered_comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        prgController = new ProgressBarController(this);
        wsCalls = new WSCalls(getApplicationContext());
        btn_mark = (Button)findViewById(R.id.btn_mark);
        btn_send = (Button)findViewById(R.id.btn_send);
        txt_message = (EditText)findViewById(R.id.txt_message);
        list_works = (ListView)findViewById(R.id.list_works);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            SELECTED_PARTICIPANT_ID= "";
            SELECTED_PARTICIPANT_NAME= "";
            PROJECT_ID ="";
            PROJECT_NAME ="";
        } else {
            SELECTED_PARTICIPANT_ID= extras.getString("SELECTED_PARTICIPANT_ID");
            SELECTED_PARTICIPANT_NAME= extras.getString("SELECTED_PARTICIPANT_NAME");
            PROJECT_ID = extras.getString("PROJECT_ID");
            PROJECT_NAME = extras.getString("PROJECT_NAME");
        }
        getSupportActionBar().setTitle("Diary of "+SELECTED_PARTICIPANT_NAME+" in " + PROJECT_NAME);
        setSampleData();
        adapter = new DairyAdapter(getApplicationContext(),works,SELECTED_PARTICIPANT_ID);
        list_works.setAdapter(adapter);

        btn_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluate();
            }
        });
    }
    public void evaluate(){
        final Context context =this;
        evaluateStudentDialog = new Dialog(context);
        evaluateStudentDialog.setContentView(R.layout.layout_evaluate);
        final EditText txt_marks = (EditText) evaluateStudentDialog.findViewById(R.id.txt_marks);
        final EditText txt_comment = (EditText) evaluateStudentDialog.findViewById(R.id.txt_comment);
        final TextView evaluate_title = (TextView) evaluateStudentDialog.findViewById(R.id.evaluate_title);
        final ImageView closeicon = (ImageView) evaluateStudentDialog.findViewById(R.id.closeimg);
        closeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateStudentDialog.dismiss();
            }
        });


        evaluate_title.setText("Evaluate "+SELECTED_PARTICIPANT_NAME);
        final Button reset = (Button) evaluateStudentDialog.findViewById(R.id.btn_close);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateStudentDialog.dismiss();
            }
        });
        final Button save = (Button) evaluateStudentDialog.findViewById(R.id.btn_add);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (txt_marks.getText().toString().length() > 1) {
                        entered_result = txt_marks.getText().toString();
                        if(txt_comment.getText().toString().length()>0){
                            entered_comment  = txt_comment.getText().toString();
                        }
                        else{
                            entered_comment = "";
                        }
                        evaluateStudentDialog.dismiss();
                        new SendEvalResults().execute(txt_marks.getText().toString(),txt_comment.getText().toString());
                       // Utility.showMessage("Successfully Added Results", context);

                    } else {
                        Utility.showMessage("Enter Result", context);
                    }
                } catch (Exception e) {
                    Utility.showMessage("Error", context);
                }
            }
        });
        //dialog.setContentView(view);
        evaluateStudentDialog.setCancelable(false);
        evaluateStudentDialog.show();
    }

    public void setSampleData(){
        works = new ArrayList<WorkCommentItem>();
        WorkCommentItem work;
        ParticipantItem p;
        WorkSeenItem wsi;
        for(int i=0 ; i<5;i++){
            work = new WorkCommentItem();
            work.comment_id = ""+i;
            work.time= "Time";
            work.project_name = "P Name: "+(i+0);
            p = new ParticipantItem();
            p.id = ""+i;
            p.name = "Name: "+i;
            if(i==0) {
                work.comment_type = ProjectWorksAdapter.TEXT;
                work.comment = "hello";
                work.comment_location ="";
            }
            else{
                work.comment_type = "JPG";
                work.comment = "";
                work.comment_location = "https://exposingtheinvisible.org/media/resources/headers/large/geo2.png?1444601948";
            }
            work.participant =p;
            work.seen_list = new ArrayList<WorkSeenItem>();
            works.add(work);
        }
    }

    private void populte_eval_response(String msg){
        try {
            JSONObject jo = new JSONObject(msg);
            Utility.showMessage(jo.getString("msg"),getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class SendEvalResults extends AsyncTask<String,Void,ResObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Sending...");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            prgController.hideProgressBar();
            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
                populte_eval_response(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
            String str_today = df.format(Calendar.getInstance().getTime());
            return wsCalls.add_group_project_participant_marks(PROJECT_ID,SELECTED_PARTICIPANT_ID, LoggedUser.id,params[0],params[1],str_today);
        }
    }
}
