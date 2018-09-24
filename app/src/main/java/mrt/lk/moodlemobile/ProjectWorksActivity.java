package mrt.lk.moodlemobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import mrt.lk.moodlemobile.adapters.ProjectWorksAdapter;
import mrt.lk.moodlemobile.data.CourseGroupItem;
import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.data.WorkCommentItem;
import mrt.lk.moodlemobile.data.WorkSeenItem;
import mrt.lk.moodlemobile.utils.ProgressBarController;

public class ProjectWorksActivity extends AppCompatActivity {

    Button btn_attach,btn_send;
    EditText txt_message;
    ListView list_works;
    ProgressBarController prgController;
    static String SELECTED_GROUP_ID,SELECTED_GROUP_NAME,PROJECT_NAME,PROJECT_ID;
    boolean IS_PROJECT;
    ArrayList<WorkCommentItem> works;
    WorkCommentItem work;
    ProjectWorksAdapter adapter;

    private static final int REQUEST_DOCUMENTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_works);
        prgController = new ProgressBarController(this);
        btn_attach = (Button)findViewById(R.id.btn_attach);
        btn_send = (Button)findViewById(R.id.btn_send);
        txt_message = (EditText)findViewById(R.id.txt_message);
        list_works = (ListView)findViewById(R.id.list_works);
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
        if(IS_PROJECT) {
            getSupportActionBar().setTitle("Project works for " + PROJECT_NAME);
        }
        else{
            getSupportActionBar().setTitle("Sub project works for " + PROJECT_NAME);
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
        setSampleData();
        adapter = new ProjectWorksAdapter(getApplicationContext(),works, LoggedUser.id);
        list_works.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedFileURI = data.getData();
                File file = new File(selectedFileURI.getPath().toString());
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
}
