package mrt.lk.moodlemobile;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
    LinearLayout layout_msg;
    WSCalls wsCalls;
    static String SELECTED_PARTICIPANT_ID,SELECTED_PARTICIPANT_NAME,PROJECT_NAME,PROJECT_ID;
    ArrayList<WorkCommentItem> works;
    WorkCommentItem sentwork;
    DairyAdapter adapter;

    String str_today;
    File file;
    Uri selectedFileURI;

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
        layout_msg = (LinearLayout)findViewById(R.id.layout_msg);
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

        if(LoggedUser.status.equals(LoggedUser.AS_EVALUATE)){
            btn_mark.setVisibility(View.VISIBLE);
            layout_msg.setVisibility(View.GONE);
        }
        else{
            btn_mark.setVisibility(View.GONE);
            layout_msg.setVisibility(View.VISIBLE);
        }

        //works = new ArrayList<>();
       // new CallDiary().execute();
        setSampleData();
        adapter = new DairyAdapter(getApplicationContext(),works,SELECTED_PARTICIPANT_ID);
        list_works.setAdapter(adapter);

        btn_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluate();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = txt_message.getText().toString();
                if(msg.equals("")){
                    Utility.showMessage("Enter Message",getApplicationContext());
                }
                else {
                    sentwork = new WorkCommentItem();
                    sentwork.comment = msg;
                    sentwork.comment_type = ProjectWorksAdapter.TEXT;
                    SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
                    str_today = df.format(Calendar.getInstance().getTime());
                    sentwork.time =str_today;
                    sentwork.seen_list = new ArrayList<WorkSeenItem>();
                    ParticipantItem p = new ParticipantItem();
                    p.id = LoggedUser.id;
                    p.name = LoggedUser.name;
                    sentwork.participant= p;

                    new SendWork().execute("");
                    txt_message.setText("");
                }
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

    private void populate_diary(String msg){
        JSONObject jo = null;
        try {
            jo = new JSONObject(msg);
            JSONArray ja,jseens;
            JSONObject jwork,jseen;
            WorkCommentItem work;
            ParticipantItem p,ps;
            WorkSeenItem wsi;
            ArrayList<WorkSeenItem>ws;
            works = new ArrayList<WorkCommentItem>();
            if(jo.getString("msg").equals("Success")){
                ja = jo.getJSONArray("data");

                for(int i=0;i< ja.length();i++){
                    jwork = ja.getJSONObject(i);
                    work = new WorkCommentItem();
                    work.time = jwork.getString("time");
                    work.comment_id = jwork.getString("comment_id");
                    work.project_name = jwork.getString("project_name");
                    p = new ParticipantItem();
                    p.id = jwork.getString("participant_id");
                    p.name = jwork.getString("participant_name");
                    work.participant =p;
                    work.comment_type = jwork.getString("comment_type");
                    work.comment = jwork.getString("comment");
                    work.comment_location = jwork.getString("comment");
                    work.comment_id = jwork.getString("comment_id");
                    work.isDiary = jwork.getBoolean("is_diary");
                    ws = new ArrayList<WorkSeenItem>();
                    jseens = jwork.getJSONArray("seen_list");
                    for(int j=0;j<jseens.length();j++){
                        jseen = jseens.getJSONObject(j);
                        wsi = new WorkSeenItem();
                        wsi.time =jseen.getString("time");
                        ps = new ParticipantItem();
                        ps.name = jseen.getString("participant_name");
                        ps.id = jseen.getString("participant_id");
                        wsi.name =  jseen.getString("participant_name");
                        wsi.id = jseen.getString("participant_id");
                        ws.add(wsi);
                    }
                    work.seen_list = ws;
                    works.add(work);
                }
            }
            else{
                Utility.showMessage(jo.getString("msg"),getApplicationContext());
            }
            adapter = new DairyAdapter(getApplicationContext(),works,SELECTED_PARTICIPANT_ID);
            list_works.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class CallDiary extends AsyncTask <String,Void,ResObject>{
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
                populate_diary(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            return wsCalls.group_project_student_diary(PROJECT_ID,SELECTED_PARTICIPANT_ID);
        }
    }

    private void populate_send_work_response(String msg){
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                sentwork.comment_id = jo.getString("data");
                works.add(sentwork);
                adapter = new DairyAdapter(getApplicationContext(),works, LoggedUser.id);
                list_works.setAdapter(adapter);
            }
            Utility.showMessage(jo.getString("msg"),getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class SendWork extends AsyncTask <String,Void,ResObject>{
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
                populate_send_work_response(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {

                return wsCalls.add_group_project_work(LoggedUser.id,PROJECT_ID,sentwork.comment_type,sentwork.comment,sentwork.time,"1");

        }
    }
}
