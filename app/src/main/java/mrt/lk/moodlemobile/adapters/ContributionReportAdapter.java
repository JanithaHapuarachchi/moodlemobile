package mrt.lk.moodlemobile.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import mrt.lk.moodlemobile.ProjectReportActivity;
import mrt.lk.moodlemobile.R;
import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.data.ReportItem;
import mrt.lk.moodlemobile.data.ReportLikeItem;
import mrt.lk.moodlemobile.data.ResObject;
import mrt.lk.moodlemobile.data.WorkCommentItem;
import mrt.lk.moodlemobile.utils.Constants;
import mrt.lk.moodlemobile.utils.Utility;
import mrt.lk.moodlemobile.utils.WSCalls;

/**
 * Created by janithah on 9/24/2018.
 */

public class ContributionReportAdapter extends ArrayAdapter {

    public String project_id;
    public boolean isProject;
    private Context context;
    private ArrayList<ReportItem> reports;
    private final LayoutInflater mInflater;


    public ContributionReportAdapter(Context context, ArrayList<ReportItem> reports, String project_id,boolean isProject) {
        super(context, R.layout.layout_report_item,reports);
        this.reports = reports;
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
        this.project_id = project_id;
        this.isProject = isProject;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View v = view;
        TextView txt_report_by,txt_liked,txt_unliked;
        final EditText txt_message;
        LinearLayout layout_other_comments;
        final Button btn_download,btn_unlike,btn_like,btn_send;
        if (v == null) {
            v = mInflater.inflate(R.layout.layout_report_item, viewGroup, false);
            v.setTag(R.id.txt_report_by, v.findViewById(R.id.txt_report_by));
            v.setTag(R.id.txt_liked, v.findViewById(R.id.txt_liked));
            v.setTag(R.id.txt_unliked, v.findViewById(R.id.txt_unliked));
            v.setTag(R.id.txt_message, v.findViewById(R.id.txt_message));
            v.setTag(R.id.layout_other_comments, v.findViewById(R.id.layout_other_comments));
            v.setTag(R.id.btn_download, v.findViewById(R.id.btn_download));
            v.setTag(R.id.btn_unlike, v.findViewById(R.id.btn_unlike));
            v.setTag(R.id.btn_like, v.findViewById(R.id.btn_like));
            v.setTag(R.id.btn_send, v.findViewById(R.id.btn_send));
        }
        txt_report_by = (TextView) v.getTag(R.id.txt_report_by);
        txt_liked = (TextView) v.getTag(R.id.txt_liked);
        txt_unliked = (TextView) v.getTag(R.id.txt_unliked);
        txt_message = (EditText) v.getTag(R.id.txt_message);
        layout_other_comments = (LinearLayout) v.getTag(R.id.layout_other_comments);
        btn_download = (Button) v.getTag(R.id.btn_download);
        btn_unlike = (Button) v.getTag(R.id.btn_unlike);
        btn_like = (Button) v.getTag(R.id.btn_like);
        btn_send = (Button) v.getTag(R.id.btn_send);

        boolean isOwnlikefound = false;
        boolean givenlike = false;
        ParticipantItem p;
        final ReportItem item = reports.get(position);
        ParticipantItem rep_by = item.participant;
        ArrayList<ReportLikeItem> likes = item.likes;
        ReportLikeItem likeItem;
        txt_report_by.setText(rep_by.name);
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

        ArrayList<WorkCommentItem> comments = item.comments;
        if(((LinearLayout)  layout_other_comments).getChildCount() > 0)
            ((LinearLayout) layout_other_comments).removeAllViews();
        TextView tv;
        for(int j=0; j< comments.size();j++){
            View vi = mInflater.inflate(R.layout.layout_text, null); //log.xml is your file.
            tv = (TextView) vi.findViewById(R.id.text1);
            tv.setText(comments.get(j).participant.name+": "+comments.get(j).comment);
            layout_other_comments.addView(vi);
        }

        set_buttoncolors(btn_like,btn_unlike,isOwnlikefound,givenlike);

        final boolean finalIsOwnlikefound = isOwnlikefound;
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!finalIsOwnlikefound){
                   // new SendLikeUnlike(position,btn_like,btn_unlike).execute(item.id,"1");
                    set_buttoncolors(btn_like,btn_unlike,true,true);
                }


            }
        });
        btn_unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!finalIsOwnlikefound){
                   // new SendLikeUnlike(position,btn_like,btn_unlike).execute(item.id,"0");
                    set_buttoncolors(btn_like,btn_unlike,true,false);
                }
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_message.getText().toString().equals("")){
                    Utility.showMessage("Please add a comment",context);
                }
                else{
                    SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
                    String str_today = df.format(Calendar.getInstance().getTime());
                    new SendComment(position).execute(item.id,txt_message.getText().toString(),str_today);
                    txt_message.setText("");
                }
            }
        });
        return v;
    }


    public void set_buttoncolors(Button btn_like,Button btn_unlike,boolean isOwnlikefound, boolean givenlike){
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

    private void populate_comment_response(String msg,String reportid,String comment,String time,int position){
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                ReportItem item = reports.get(position);
                ArrayList<WorkCommentItem> comments = item.comments;
                WorkCommentItem commentItem = new WorkCommentItem();
                commentItem.comment = comment;
                ParticipantItem p = new ParticipantItem();
                p.name = LoggedUser.name;
                p.id = LoggedUser.id;
                commentItem.participant = p;
                comments.add(commentItem);
                item.comments = comments;
                reports.set(position,item);
                notifyDataSetChanged();
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
        int position;

        public SendComment(int position){
            this.position = position;
        }

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
                populate_comment_response(response.msg,reportid,comment,time,position);
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
            if(isProject) {
                return new WSCalls(context).add_comment_report(reportid, LoggedUser.id,"project",comment,time);
            }
            else{
                return new WSCalls(context).add_comment_report(reportid, LoggedUser.id,"subproject",comment,time);
            }
        }
    }

    private void populate_like_unlike_response(String msg,String reportid,String is_like,int position,Button btn_like,Button btn_unlike){
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                ReportItem item = reports.get(position);
                ArrayList<ReportLikeItem> likes = item.likes;
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
                likes.add(likeItem);
                item.likes = likes;
                reports.set(position,item);
                if(is_like.equals("1")){
                    set_buttoncolors(btn_like,btn_unlike,true,true);
                }
                else{
                    set_buttoncolors(btn_like,btn_unlike,true,false);
                }

                notifyDataSetChanged();
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
        int position;
        Button btn_like,btn_unlike;

        public SendLikeUnlike(int position,Button btn_like,Button btn_unlike){
            this.position = position;
            this.btn_like = btn_like;
            this.btn_unlike = btn_unlike;
        }

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
                populate_like_unlike_response(response.msg,reportid,is_like,position,btn_like,btn_unlike);
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
            if(isProject) {
                return new WSCalls(context).add_like_unlike_report(reportid, LoggedUser.id,"project",is_like,str_today);
            }
            else{
                return new WSCalls(context).add_like_unlike_report(reportid, LoggedUser.id,"subproject",is_like,str_today);
            }
        }
    }

}
