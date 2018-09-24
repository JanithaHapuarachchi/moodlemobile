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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

import mrt.lk.moodlemobile.adapters.ContributionReportAdapter;
import mrt.lk.moodlemobile.adapters.ProjectWorksAdapter;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.data.ReportItem;
import mrt.lk.moodlemobile.data.ReportLikeItem;
import mrt.lk.moodlemobile.data.WorkCommentItem;
import mrt.lk.moodlemobile.data.WorkSeenItem;
import mrt.lk.moodlemobile.utils.ProgressBarController;

public class ProjectReportActivity extends AppCompatActivity {

    ImageView img_upload_report;
    ListView list_reports;
    TextView txt_reports_title;
    ProgressBarController prgController;
    ArrayList <ReportItem> reports;
    ContributionReportAdapter adapter;
    static String PROJECT_NAME,PROJECT_ID;
    boolean IS_PROJECT;

    private static final int REQUEST_DOCUMENTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_report);

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

        setSampleData();
        if(IS_PROJECT) {
            adapter = new ContributionReportAdapter(getApplicationContext(), reports, PROJECT_ID,true);
        }
        else{
            adapter = new ContributionReportAdapter(getApplicationContext(), reports, PROJECT_ID,true);
        }
        list_reports.setAdapter(adapter);
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
                File file = new File(selectedFileURI.getPath().toString());
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
}
