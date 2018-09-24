package mrt.lk.moodlemobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

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

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_report);
        context = getApplicationContext();

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
        btn_unlike = (Button)findViewById(R.id.btn_download);
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

        layout_final_report.setOnClickListener(new View.OnClickListener() {
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
                File file = new File(selectedFileURI.getPath().toString());
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
