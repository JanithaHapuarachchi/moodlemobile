package mrt.lk.moodlemobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import mrt.lk.moodlemobile.adapters.ContributionReportAdapter;
import mrt.lk.moodlemobile.adapters.ProjectWorksAdapter;
import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.data.ReportItem;
import mrt.lk.moodlemobile.data.ReportLikeItem;
import mrt.lk.moodlemobile.data.ResObject;
import mrt.lk.moodlemobile.data.WorkCommentItem;
import mrt.lk.moodlemobile.data.WorkSeenItem;
import mrt.lk.moodlemobile.utils.Constants;
import mrt.lk.moodlemobile.utils.FilePath;
import mrt.lk.moodlemobile.utils.ProgressBarController;
import mrt.lk.moodlemobile.utils.Utility;
import mrt.lk.moodlemobile.utils.WSCalls;

public class ProjectReportActivity extends AppCompatActivity {

    ImageView img_upload_report;
    ListView list_reports;
    TextView txt_reports_title;
    public static ProgressBarController prgController;
    ArrayList <ReportItem> reports;
    ContributionReportAdapter adapter;
    public static String PROJECT_NAME,PROJECT_ID;
    boolean IS_PROJECT;
    WSCalls wsCalls;
    File file;
    private static final int REQUEST_DOCUMENTS = 1;
    boolean isOwnReportFound = false;
    String uploaded_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_report);
        wsCalls = new WSCalls(getApplicationContext());
        prgController = new ProgressBarController(this);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            PROJECT_ID ="";
            PROJECT_NAME ="";
            IS_PROJECT = false;
        } else {
            PROJECT_ID = extras.getString("PROJECT_ID");
            PROJECT_NAME = extras.getString("PROJECT_NAME");
            IS_PROJECT = extras.getBoolean("IS_PROJECT");
        }
        txt_reports_title = (TextView)findViewById(R.id.txt_reports_title);
        img_upload_report = (ImageView)findViewById(R.id.img_upload_report);
        list_reports = (ListView)findViewById(R.id.list_reports);

        if(IS_PROJECT){
            txt_reports_title.setText("Project Reports: "+PROJECT_NAME);
        }
        else{
            txt_reports_title.setText("Sub project Reports: "+PROJECT_NAME);
        }
        new LoadProjectReports().execute(PROJECT_ID);
       // setSampleData();
//        if(IS_PROJECT) {
//            adapter = new ContributionReportAdapter(getApplicationContext(), reports, PROJECT_ID,true);
//        }
//        else{
//            adapter = new ContributionReportAdapter(getApplicationContext(), reports, PROJECT_ID,false);
//        }
//        list_reports.setAdapter(adapter);


        img_upload_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            REQUEST_DOCUMENTS);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ProjectReportActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void setSampleData(){
        reports = new ArrayList<ReportItem>();


        ReportItem report;
        ParticipantItem p;
        WorkSeenItem wsi;
        for(int i=0 ; i<5;i++){
            report = new ReportItem();
            report.id = ""+i;
            report.report_location= "https://exposingtheinvisible.org/media/resources/headers/large/geo2.png?1444601948";
            report.time= "Time";
            p = new ParticipantItem();
            p.id = ""+i;
            p.name = "Name: "+i;
            report.participant = p;
            ArrayList<WorkCommentItem> comments = new ArrayList<WorkCommentItem>();
            for(int  j=0;j<2;j++){
                WorkCommentItem wc = new WorkCommentItem();
                ParticipantItem pi = new ParticipantItem();
                pi.name = "AAAA";
                wc.comment="Comment: "+(j+1);
                wc.participant =pi;
                comments.add(wc);
            }
            report.comments = comments;
            report.likes = new ArrayList<ReportLikeItem>();
            reports.add(report);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedFileURI = data.getData();
                String selectedFilePath = FilePath.getPath(this,selectedFileURI);
                 file = new File(selectedFilePath);
                Log.d("MoodleMobile", "File : " + file.getName());
                confirmSendFile(file.getName());

            }
        }
    }

    private void confirmSendFile(String file_name){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProjectReportActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Confirm to Send File");
        builder.setMessage("File Name: "+file_name);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new UploadReport().execute("");
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void load_reports(String msg){
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
            reports = new ArrayList<ReportItem>();
                ReportItem report;
            ReportLikeItem like;
                JSONArray jlikes,jcomments;
                JSONObject jlike,jreport,jcomment;
                 WorkCommentItem comment;
                ParticipantItem p,pc;
            ArrayList<WorkCommentItem>comments;
            ArrayList<ReportLikeItem>likes;
            JSONArray jreports = jo.getJSONArray("data");
             if(jreports == null){
                 jreports = new JSONArray();
             }
             for(int i=0;i<jreports.length();i++){
                 jreport = jreports.getJSONObject(i);
                 report = new ReportItem();
                 report.id = jreport.getString("report_id");
                 report.time = jreport.getString("time");
                 report.report_location = jreport.getString("report");
                 p = new ParticipantItem();
                 p.id = jreport.getString("participant_id");
                 p.name = jreport.getString("participant_name");
                 if(p.id.equals(LoggedUser.id)){
                     isOwnReportFound = true;
                     img_upload_report.setVisibility(View.GONE);
                 }
                 else{
                     isOwnReportFound = false;
                 }
                 report.participant = p;
                 jcomments = jreport.getJSONArray("report_comments");
                 if(jcomments == null){
                     jcomments = new JSONArray();
                 }
                 comments = new ArrayList<WorkCommentItem>();
                 for(int j =0; j<jcomments.length();j++){
                     jcomment = jcomments.getJSONObject(j);
                     comment = new WorkCommentItem();
                     comment.comment  = jcomment.getString("comment");
                     comment.time = jcomment.getString("time");
                     pc = new ParticipantItem();
                     pc.id = jcomment.getString("participant_id");
                     pc.name = jcomment.getString("participant_name");
                     comment.participant =pc;
                     comments.add(comment);
                 }
                 report.comments =comments;
                 jlikes = jreport.getJSONArray("report_likes");
                 if(jlikes == null){
                     jlikes = new JSONArray();
                 }
                 likes = new ArrayList<ReportLikeItem>();
                 for(int j =0; j<jlikes.length();j++){
                     jlike = jlikes.getJSONObject(j);
                     like = new ReportLikeItem();
                     like.time = jlike.getString("time");
                     like.isLike = jlike.getBoolean("islike");
                     pc = new ParticipantItem();
                     pc.id = jlike.getString("participant_id");
                     pc.name = jlike.getString("participant_name");
                     like.participant =pc;
                     likes.add(like);
                 }
                 report.likes =likes;
                 reports.add(report);
             }
            }
            else{
                Utility.showMessage(jo.getString("msg"),getApplicationContext());
            }

            if(IS_PROJECT) {
                adapter = new ContributionReportAdapter(getApplicationContext(), reports, PROJECT_ID,true);
            }
            else{
                adapter = new ContributionReportAdapter(getApplicationContext(), reports, PROJECT_ID,false);
            }
            list_reports.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class LoadProjectReports extends AsyncTask <String,Void,ResObject>{
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
                load_reports(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            if(IS_PROJECT){
                return wsCalls.group_project_contribution_reports(params[0]);
            }
            else{
                return wsCalls.groupproject_subproject_contribution_reports(params[0]);
            }
        }
    }


    private void populate_upload_report(String msg){
        try {
            JSONObject jo = new JSONObject(msg);
            String rid;
            ParticipantItem p;

            if(jo.getString("msg").equals("Success")){
                rid= jo.getString("data");
                img_upload_report.setVisibility(View.GONE);
                ReportItem report = new ReportItem();
                report.id = rid;
                report.time = uploaded_time;
                report.report_location = "";
                p = new ParticipantItem();
                p.id = LoggedUser.id;
                p.name = LoggedUser.name;
                isOwnReportFound = true;
                report.participant = p;
                report.comments = new ArrayList<WorkCommentItem>();
                report.likes = new ArrayList<ReportLikeItem>();
                reports.add(report);
                if(IS_PROJECT) {
                    adapter = new ContributionReportAdapter(getApplicationContext(), reports, PROJECT_ID,true);
                }
                else{
                    adapter = new ContributionReportAdapter(getApplicationContext(), reports, PROJECT_ID,false);
                }
                list_reports.setAdapter(adapter);
            }
            Utility.showMessage(jo.getString("msg"),getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class UploadReport extends  AsyncTask<String,Void,ResObject>{

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
                populate_upload_report(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
             uploaded_time = df.format(Calendar.getInstance().getTime());
            if(IS_PROJECT){
                return  wsCalls.upload_group_project_contribution_report(LoggedUser.id,PROJECT_ID,PROJECT_NAME,file,uploaded_time,"0","0");
            }
            else{
                return wsCalls.upload_groupproject_subproject_contribution_report(LoggedUser.id,PROJECT_ID,PROJECT_NAME,file,uploaded_time,"0","0");
            }

        }
    }

}
