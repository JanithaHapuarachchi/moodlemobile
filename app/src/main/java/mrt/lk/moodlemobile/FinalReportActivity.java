package mrt.lk.moodlemobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.data.ReportItem;
import mrt.lk.moodlemobile.data.ReportLikeItem;
import mrt.lk.moodlemobile.data.ResObject;
import mrt.lk.moodlemobile.data.WorkCommentItem;
import mrt.lk.moodlemobile.utils.Constants;
import mrt.lk.moodlemobile.utils.DownloadFileFromUrl;
import mrt.lk.moodlemobile.utils.ProgressBarController;
import mrt.lk.moodlemobile.utils.Utility;
import mrt.lk.moodlemobile.utils.WSCalls;

public class FinalReportActivity extends AppCompatActivity {

    Button btn_download,btn_download_preview,btn_like,btn_unlike,btn_send;
    TextView txt_final_upload_by,txt_report_by,txt_liked,txt_unliked;
    ImageView img_upload_preview_report,img_upload_final_report;
    EditText txt_message;
    LinearLayout layout_other_comments,layout_final_report,layout_preview_report;
    static String PROJECT_NAME,PROJECT_ID;
    boolean IS_PROJECT;
    boolean upload_final_report =false;
    private static final int REQUEST_DOCUMENTS = 1;
    public static ProgressBarController prgController;
    WSCalls wsCalls;
    String preview_report_link,final_report_link;
    ReportItem r_final,r_preview;
    ArrayList<ReportLikeItem>lk;
    ArrayList<WorkCommentItem>wc;
    String str_today;
    boolean isOwnlikefound = false;
    boolean givenlike =false;
    File file;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_report);
        context = getApplicationContext();
        r_final= r_preview = new ReportItem();

        prgController = new ProgressBarController(this);
        wsCalls = new WSCalls(getApplicationContext());
        wc= new ArrayList<WorkCommentItem>();
        lk = new ArrayList<ReportLikeItem>();
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
        if(IS_PROJECT){
            getSupportActionBar().setTitle(PROJECT_NAME+" final report");
        }
        else{
            getSupportActionBar().setTitle(PROJECT_NAME+" final report");
        }




        btn_download = (Button)findViewById(R.id.btn_download);
        btn_download_preview = (Button)findViewById(R.id.btn_download_preview);
        btn_like = (Button)findViewById(R.id.btn_like);
        btn_unlike = (Button)findViewById(R.id.btn_unlike);
        btn_send = (Button)findViewById(R.id.btn_send);

        txt_final_upload_by = (TextView) findViewById(R.id.txt_final_upload_by);
        txt_report_by = (TextView) findViewById(R.id.txt_report_by);
        txt_liked = (TextView) findViewById(R.id.txt_liked);
        txt_unliked = (TextView) findViewById(R.id.txt_unliked);

        img_upload_preview_report = (ImageView) findViewById(R.id.img_upload_preview_report);
        img_upload_final_report = (ImageView) findViewById(R.id.img_upload_final_report);

        txt_message = (EditText) findViewById(R.id.txt_message);

        layout_other_comments = (LinearLayout) findViewById(R.id.layout_other_comments);
        layout_final_report = (LinearLayout) findViewById(R.id.layout_final_report);
        layout_preview_report = (LinearLayout) findViewById(R.id.layout_preview_report);


        if(!LoggedUser.status.equals(LoggedUser.AS_EVALUATE)){
            img_upload_preview_report.setVisibility(View.VISIBLE);
            img_upload_final_report.setVisibility(View.VISIBLE);
        }
        else{
            img_upload_preview_report.setVisibility(View.GONE);
            img_upload_final_report.setVisibility(View.GONE);
        }

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = "https://webmail.mobitel.lk/owa/service.svc/s/GetFileAttachment?id=AAMkAGYwNmEwMTY3LWZhNmUtNDg2YS04NWRmLWNhM2RmMTBmYTM5YgBGAAAAAAB4niVppQ8eRqbeLZR%2BfCWuBwAckZDp4cZCQ71tGAc1FEviAAAAAAEMAAAckZDp4cZCQ71tGAc1FEviAADxRXqNAAABEgAQAKNwQcGP%2FxBMsqkxVglrwG0%3D&X-OWA-CANARY=IjDMUsfaTEu4wmXtQoo0F5KkFisYJtYIsEx8xfk1pzXXO1u7R-aFJjxKsfyGi01sGO0E1HOkhjw.";
                new DownloadFileFromUrl(prgController,btn_download,FinalReportActivity.this).execute(u);
            }
        });

        btn_download_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadFileFromUrl(prgController,btn_download_preview,FinalReportActivity.this).execute(preview_report_link);
            }
        });

        img_upload_preview_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_final_report =false;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            REQUEST_DOCUMENTS);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(FinalReportActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOwnlikefound){
                     new SendLikeUnlike(btn_like,btn_unlike).execute(r_preview.id,"1");
                  //  set_buttoncolors(btn_like,btn_unlike,true,true);
                }


            }
        });
        btn_unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOwnlikefound){
                     new SendLikeUnlike(btn_like,btn_unlike).execute(r_preview.id,"0");
                  //  set_buttoncolors(btn_like,btn_unlike,true,false);
                }
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_message.getText().toString().equals("")){
                    Utility.showMessage("Please add a Comment",getApplicationContext());
                }
                else{
                    SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
                     str_today = df.format(Calendar.getInstance().getTime());
                    new SendComment().execute(r_preview.id,txt_message.getText().toString(),str_today);
                }
            }
        });

        img_upload_final_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_final_report =true;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            REQUEST_DOCUMENTS);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(FinalReportActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        preview_report_link = final_report_link = "";
        new CallFinalReports().execute(PROJECT_ID);

    }



    public void set_buttoncolors(Button btn_like, Button btn_unlike, boolean isOwnlikefound, boolean givenlike){
        if(isOwnlikefound){
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                if(givenlike) {
                    btn_like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.like));
                    btn_unlike.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.neutral_dislike));
                }
                else{
                    btn_like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.neutral_like));
                    btn_unlike.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.unlike));
                }
            } else {
                if(givenlike) {
                    btn_like.setBackground(ContextCompat.getDrawable(context, R.drawable.like));
                    btn_unlike.setBackground(ContextCompat.getDrawable(context, R.drawable.neutral_dislike));
                }
                else{
                    btn_like.setBackground(ContextCompat.getDrawable(context, R.drawable.neutral_like));
                    btn_unlike.setBackground(ContextCompat.getDrawable(context, R.drawable.unlike));
                }
            }
        }
        else{
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                btn_like.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.neutral_like));
                btn_unlike.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.neutral_dislike));
            }
            else{
                btn_like.setBackground(ContextCompat.getDrawable(context, R.drawable.neutral_like));
                btn_unlike.setBackground(ContextCompat.getDrawable(context, R.drawable.neutral_dislike));
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedFileURI = data.getData();
                 file = new File(selectedFileURI.getPath().toString());
                Log.d("MoodleMobile", "File : " + file.getName());
                confirmSendFile(file.getName());

            }
        }
    }

    private void confirmSendFile(String file_name){
        AlertDialog.Builder builder = new AlertDialog.Builder(FinalReportActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Confirm to Send File");
        builder.setMessage("File Name: "+file_name);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(upload_final_report){
                            new UploadReport().execute("0");
                        }
                        else{
                            new UploadReport().execute("1");
                        }
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

    private void populate_final_reports(String msg){
        set_buttoncolors(btn_like,btn_unlike,false,false);
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                JSONObject data = jo.getJSONObject("data");

                if(data.length()<1 || data.toString().equals("[]")){
                    set_buttoncolors(btn_like,btn_unlike,false,false);
                }
                JSONObject jprevw = data.getJSONObject("preview_report");
                JSONObject jfinal = data.getJSONObject("final_report");
                JSONArray jcomments,jlikes;
                wc = new ArrayList<WorkCommentItem>();
                lk = new ArrayList<ReportLikeItem>();
                if( jfinal != null && jfinal.length() !=0 ){
                    img_upload_final_report.setVisibility(View.GONE);
                    r_final = new ReportItem();
                    r_final.id = jfinal.getString("report_id");
                    r_final.time = jfinal.getString("time");
                    r_final.report_location = jfinal.getString("report");
                    txt_final_upload_by.setText(jfinal.getString("participant_name"));
                    final_report_link = r_final.report_location;
                }
                else{
                    img_upload_final_report.setVisibility(View.VISIBLE);
                }
                if(jprevw != null && jprevw.length() !=0){
                    img_upload_preview_report.setVisibility(View.GONE);
                    r_preview = new ReportItem();
                    r_preview.id = jprevw.getString("report_id");
                    r_preview.time = jprevw.getString("time");
                    r_preview.report_location = jprevw.getString("report");
                    txt_report_by.setText(jprevw.getString("participant_name"));
                    preview_report_link = r_preview.report_location;
                    jcomments = jprevw.getJSONArray("report_comments");
                    WorkCommentItem wci;

                    ParticipantItem p;
                    JSONObject jwork;
                    for(int i=0;i< jcomments.length();i++){
                        wci = new WorkCommentItem();
                        jwork =jcomments.getJSONObject(i);
                        wci.comment = jwork.getString("comment");
                        wci.time = jwork.getString("time");
                        p = new ParticipantItem();
                        p.name = jwork.getString("participant_name");
                        p.id = jwork.getString("participant_id");
                        wci.participant =p;
                        wc.add(wci);
                    }

                    jlikes = jprevw.getJSONArray("report_likes");
                    ReportLikeItem lki;

                   // ParticipantItem p;
                    JSONObject jlike;
                    for(int i=0;i< jlikes.length();i++){
                        lki = new ReportLikeItem();
                        jlike =jcomments.getJSONObject(i);
                        p = new ParticipantItem();
                        p.name = jlike.getString("participant_name");
                        p.id = jlike.getString("participant_id");
                        lki.isLike = jlike.getBoolean("islike");
                        lki.participant = p;
                        lki.time = jlike.getString("time");
                        lk.add(lki);
                    }

                }
                else{
                    img_upload_preview_report.setVisibility(View.VISIBLE);
                }
            }
            else{
                Utility.showMessage(jo.getString("msg"),getApplicationContext());
                set_buttoncolors(btn_like,btn_unlike,false,false);
            }
            showcomments();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class CallFinalReports extends AsyncTask<String,Void,ResObject>{
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
                populate_final_reports(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            if(IS_PROJECT){
                return wsCalls.group_project_final_report_details(params[0]);
            }
            else{
                return wsCalls.groupproject_subproject_final_report_details(params[0]);
            }
        }
    }

    private void showlikes(){
        ArrayList<ReportLikeItem> likes =lk;
        ReportLikeItem likeItem;
        ParticipantItem p;
        String liked ="Liked: ";
        int like_count = 0 ;
        int unlike_count = 0 ;
        String unliked ="Unliked: ";
        for(int i=1 ;i<likes.size();i++){
            likeItem = likes.get(i);
            p = likeItem.participant;

            if(p.id.equals(LoggedUser.id)){
                isOwnlikefound  =true;
                givenlike = likeItem.isLike;
            }

            if(likeItem.isLike){
                if(like_count >0){
                    liked += p.name;
                }
                else{
                    liked += ","+p.name;
                }
                like_count++;
            }
            else{
                if(unlike_count >0){
                    unliked += p.name;
                }
                else{
                    unliked += ","+p.name;
                }
                unlike_count++;
            }
        }
        if(like_count > 0){
            txt_liked.setText(liked);
        }
        else{
            txt_liked.setVisibility(View.GONE);
        }
        if(unlike_count >0 ){
            txt_unliked.setText(unliked);
        }
        else{
            txt_unliked.setVisibility(View.GONE);
        }
    }



     private void showcomments(){
         if(((LinearLayout)  layout_other_comments).getChildCount() > 0)
             ((LinearLayout) layout_other_comments).removeAllViews();
         TextView tv;
         for(int j=0; j< wc.size();j++){
             View vi =  LayoutInflater.from(context).inflate(R.layout.layout_text, null); //log.xml is your file.
             tv = (TextView) vi.findViewById(R.id.text1);
             tv.setText(wc.get(j).participant.name+": "+wc.get(j).comment);
             layout_other_comments.addView(vi);
         }
     }


    private void populate_comment_response(String msg,String reportid,String comment,String time){
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                WorkCommentItem wci = new WorkCommentItem();
                wci.comment = txt_message.getText().toString();
                wci.time = str_today;
                ParticipantItem p = new ParticipantItem();
                p.name = LoggedUser.name;
                p.id = LoggedUser.id;
                wci.participant =p;
                wc.add(wci);
                txt_message.setText("");
                showcomments();
            }
            else{
                Utility.showMessage(jo.getString("msg"),context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class SendComment extends AsyncTask<String,Void,ResObject>{
        String comment,reportid,time;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProjectReportActivity.prgController.showProgressBar("Sending...");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            ProjectReportActivity.prgController.hideProgressBar();
            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
                populate_comment_response(response.msg,reportid,comment,time);
            }
            else{
                Utility.showMessage(response.msg,context);
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            this.comment = params[1];
            this.reportid = params[0];
            this.time = params[2];
            if(IS_PROJECT) {
                return new WSCalls(context).add_comment_report(reportid, LoggedUser.id,"project",comment,time);
            }
            else{
                return new WSCalls(context).add_comment_report(reportid, LoggedUser.id,"subproject",comment,time);
            }
        }
    }

    private void populate_like_unlike_response(String msg,String reportid,String is_like,Button btn_like,Button btn_unlike){
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                ReportLikeItem likeItem= new ReportLikeItem();
                ParticipantItem p = new ParticipantItem();
                p.id = LoggedUser.id;
                p.name = LoggedUser.name;
                if(is_like.equals("1")) {
                    likeItem.isLike = true;
                }
                else{
                    likeItem.isLike = false;
                }
                likeItem.participant = p;
                lk.add(likeItem);
                if(is_like.equals("1")){
                    set_buttoncolors(btn_like,btn_unlike,true,true);
                }
                else{
                    set_buttoncolors(btn_like,btn_unlike,true,false);
                }
                showlikes();

            }
            else{
                Utility.showMessage(jo.getString("msg"),context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class SendLikeUnlike extends AsyncTask<String,Void,ResObject>{
        String is_like,reportid;

        Button btn_like,btn_unlike;

        public SendLikeUnlike(Button btn_like,Button btn_unlike){

            this.btn_like = btn_like;
            this.btn_unlike = btn_unlike;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // prgController = new ProgressBarController(FinalReportActivity.this);
            prgController.showProgressBar("Sending...");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            prgController.hideProgressBar();
            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
                populate_like_unlike_response(response.msg,reportid,is_like,btn_like,btn_unlike);
            }
            else{
                Utility.showMessage(response.msg,context);
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            this.is_like = params[1];
            this.reportid = params[0];
            SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
            String str_today = df.format(Calendar.getInstance().getTime());
            if(IS_PROJECT) {
                return new WSCalls(context).add_like_unlike_report(reportid, LoggedUser.id,"project",is_like,str_today);
            }
            else{
                return new WSCalls(context).add_like_unlike_report(reportid, LoggedUser.id,"subproject",is_like,str_today);
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
                if(upload_final_report){
                    r_final = new ReportItem();
                    r_final.id = rid;
                    r_final.report_location = "";
                    p = new ParticipantItem();
                    p.id = LoggedUser.id;
                    p.name =LoggedUser.name;
                    txt_final_upload_by.setText(LoggedUser.username);
                    img_upload_final_report.setVisibility(View.GONE);
                    btn_download.setEnabled(false);
                }
                else{
                    r_preview = new ReportItem();
                    r_preview.id = rid;
                    txt_report_by.setText(LoggedUser.username);
                    img_upload_preview_report.setVisibility(View.GONE);
                    btn_download_preview.setEnabled(false);
                }
            }
            Utility.showMessage(jo.getString("msg"),getApplicationContext());
        } catch (JSONException e) {
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
            String str_today = df.format(Calendar.getInstance().getTime());
            if(IS_PROJECT){
                return  wsCalls.upload_group_project_contribution_report(LoggedUser.id,PROJECT_ID,PROJECT_NAME,file,str_today,"1",params[0]);
            }
            else{
                return wsCalls.upload_groupproject_subproject_contribution_report(LoggedUser.id,PROJECT_ID,PROJECT_NAME,file,str_today,"1",params[0]);
            }

        }
    }

}
