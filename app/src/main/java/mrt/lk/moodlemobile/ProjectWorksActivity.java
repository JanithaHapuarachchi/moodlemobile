package mrt.lk.moodlemobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import mrt.lk.moodlemobile.adapters.DairyAdapter;
import mrt.lk.moodlemobile.adapters.ProjectWorksAdapter;
import mrt.lk.moodlemobile.data.CourseGroupItem;
import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.data.ResObject;
import mrt.lk.moodlemobile.data.WorkCommentItem;
import mrt.lk.moodlemobile.data.WorkSeenItem;
import mrt.lk.moodlemobile.utils.Constants;
import mrt.lk.moodlemobile.utils.FilePath;
import mrt.lk.moodlemobile.utils.ProgressBarController;
import mrt.lk.moodlemobile.utils.Utility;
import mrt.lk.moodlemobile.utils.WSCalls;

public class ProjectWorksActivity extends AppCompatActivity {

    Button btn_attach,btn_send;
    EditText txt_message;
    public static ListView list_works;
    public static ProgressBarController prgController;
    public static String SELECTED_GROUP_ID,SELECTED_GROUP_NAME,PROJECT_NAME,PROJECT_ID;
    public static boolean IS_PROJECT;
    public static boolean isActivityStarted = false;
    public static ArrayList<WorkCommentItem> works;
    WorkCommentItem sentwork;
    public static ProjectWorksAdapter adapter;
    LinearLayout layout_send;
    WSCalls wsCalls;
    String str_today;
    File file;
    Uri selectedFileURI;

    private static Context cnt;

    int start_comment_id;
    int last_comment_id;

    private static final int REQUEST_DOCUMENTS = 1;

    @Override
    protected void onResume() {
        super.onResume();
        isActivityStarted = true;
        Log.e("Moodle","Activity Started");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityStarted = false;
        Log.e("Moodle","Activity Stoped");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       cnt =  ProjectWorksActivity.this;
        setContentView(R.layout.activity_project_works);
        prgController = new ProgressBarController(this);
        wsCalls =new WSCalls(getApplicationContext());
        btn_attach = (Button)findViewById(R.id.btn_attach);
        btn_send = (Button)findViewById(R.id.btn_send);
        txt_message = (EditText)findViewById(R.id.txt_message);
        list_works = (ListView)findViewById(R.id.list_works);
        layout_send= (LinearLayout)findViewById(R.id.layout_send);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            SELECTED_GROUP_ID= "";
            SELECTED_GROUP_NAME= "";
            PROJECT_ID ="";
            PROJECT_NAME ="";
            IS_PROJECT = false;
        } else {
            SELECTED_GROUP_ID= extras.getString("SELECTED_GROUP_ID");
            SELECTED_GROUP_NAME= extras.getString("GIVEN_GROUP_NAME");
            PROJECT_ID = extras.getString("PROJECT_ID");
            PROJECT_NAME = extras.getString("PROJECT_NAME");
            IS_PROJECT = extras.getBoolean("IS_PROJECT",false);
        }
        Log.e("Moodle PROJECT ID",PROJECT_ID);
        LoggedUser.selected_group_id = SELECTED_GROUP_ID;
        if(IS_PROJECT) {
            getSupportActionBar().setTitle("Project works for " + PROJECT_NAME);
        }
        else{
            getSupportActionBar().setTitle("Sub project works for " + PROJECT_NAME);
        }
        if(!LoggedUser.status.equals(LoggedUser.AS_EVALUATE)){
            layout_send.setVisibility(View.VISIBLE);
        }
        else{
            layout_send.setVisibility(View.GONE);
        }

        btn_attach.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(ProjectWorksActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
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
        new CallWorkList().execute("");
      //  setSampleData();
       // adapter = new ProjectWorksAdapter(getApplicationContext(),works, LoggedUser.id);
       // list_works.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                 selectedFileURI = data.getData();
                String selectedFilePath = FilePath.getPath(this,selectedFileURI);
                // file = new File(selectedFileURI.getPath().toString());
                file = new File(selectedFilePath);
                Log.d("MoodleMobile", "File : " + file.getName());
                confirmSendFile(file.getName());
//                uploadedFileName = file.getName().toString();
//                tokens = new StringTokenizer(uploadedFileName, ":");
//                first = tokens.nextToken();
//                file_1 = tokens.nextToken().trim();
//                txt_file_name_1.setText(file_1);
            }
        }
    }

    private void confirmSendFile(String file_name){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProjectWorksActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Confirm to Send File");
        builder.setMessage("File Name: "+file_name);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sentwork = new WorkCommentItem();
                        sentwork.comment = "";
                        String filenameArray[] = file.getName().split("\\.");
                        String extension = filenameArray[filenameArray.length-1];
                        if(Arrays.asList(Constants.ALLOWED_VIDEO_FORMATS).contains(extension)){
                            sentwork.comment_type = Constants.UPLOAD_VIDEO;
                        }
                        else if(Arrays.asList(Constants.ALLOWED_AUDIO_FORMATS).contains(extension)){
                            sentwork.comment_type = Constants.UPLOAD_AUDIO;
                        }
                        else if(Arrays.asList(Constants.ALLOWED_IMAGE_FORMATS).contains(extension)){
                            sentwork.comment_type = Constants.UPLOAD_IMAGE;
                        }
                        else{
                            sentwork.comment_type = Constants.UPLOAD_FILE;
                        }

                        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
                        str_today = df.format(Calendar.getInstance().getTime());
                        sentwork.time =str_today;
                        sentwork.seen_list = new ArrayList<WorkSeenItem>();
                        ParticipantItem p = new ParticipantItem();
                        p.id = LoggedUser.id;
                        p.name = LoggedUser.name;
                        sentwork.participant= p;
                        new UploadWork().execute("");
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


    public void setSampleData(){
        works = new ArrayList<WorkCommentItem>();
        WorkCommentItem work;
        ParticipantItem p;
        WorkSeenItem wsi;
        for(int i=0 ; i<5;i++){
            work = new WorkCommentItem();
            work.comment_id = ""+i;
            work.time= "Time";
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

    private void populate_send_work_response(String msg){
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                sentwork.comment_id = jo.getString("data");
                works.add(sentwork);
                adapter = new ProjectWorksAdapter(getApplicationContext(),works, LoggedUser.id);
                list_works.setAdapter(adapter);
            }
            Utility.showMessage(jo.getString("msg"),getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class UploadWork extends AsyncTask <String,Void,ResObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Uploading...");
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
            if(IS_PROJECT){
                return wsCalls.upload_project_work(LoggedUser.id,PROJECT_ID,sentwork.comment_type,file,sentwork.time,"0");
            }
            else{
                return wsCalls.upload_subproject_work(LoggedUser.id,PROJECT_ID,sentwork.comment_type,file,sentwork.time,"0");
            }
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
            if(IS_PROJECT){
                return wsCalls.add_group_project_work(LoggedUser.id,PROJECT_ID,sentwork.comment_type,sentwork.comment,sentwork.time,"0");
            }
            else{
                return wsCalls.add_groupproject_subproject_work(LoggedUser.id,PROJECT_ID,sentwork.comment_type,sentwork.comment,sentwork.time,"0");
            }
        }
    }

    private void populate_worklist(String msg){
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
                    //work.project_name = jwork.getString("project_name");
                    p = new ParticipantItem();
                    p.id = jwork.getString("participant_id");
                    p.name = jwork.getString("participant_fname");
                    work.participant =p;
                    work.comment_type = jwork.getString("comment_type");
                    work.comment = jwork.getString("comment");
                    if(!work.comment_type.equals(ProjectWorksAdapter.TEXT)) {
                        work.comment_location = jwork.getString("comment_location");
                    }
                    else{
                        work.comment_location = jwork.getString("comment");
                    }
                    work.comment_id = jwork.getString("comment_id");
                    if(jwork.getString("is_diary").equals("1")) {
                        work.isDiary = true;
                    }
                    else{
                        work.isDiary = false;
                    }
                    ws = new ArrayList<WorkSeenItem>();

                    if(i==ja.length()-1){
                        last_comment_id = Integer.parseInt(work.comment_id);
                    }

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
            adapter = new ProjectWorksAdapter(getApplicationContext(),works,LoggedUser.id);
            list_works.setAdapter(adapter);
        } catch (JSONException e) {
            Log.e("Moodle Error",e.getMessage());
            e.printStackTrace();
        }

    }


    class CallWorkList extends AsyncTask<String,Void,ResObject>{
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
                populate_worklist(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            if(IS_PROJECT){
                return wsCalls.group_project_work_list(PROJECT_ID,LoggedUser.id);
            }
            else{
                return wsCalls.groupproject_subproject_work_list(PROJECT_ID,LoggedUser.id);
            }
            //return null;
        }
    }
    public static void populate_data_from_msg(WorkCommentItem item){
       // x(item);
    }
    public void x(final WorkCommentItem item){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                works.add(item);
                adapter = new ProjectWorksAdapter(cnt,ProjectWorksActivity.works, LoggedUser.id);
                list_works.setAdapter(adapter);
                // Stuff that updates the UI

            }
        });



    }

    public static Handler UIHandler;

    static
    {
        UIHandler = new Handler(Looper.getMainLooper());
    }
    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }
}
