package mrt.lk.moodlemobile.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mrt.lk.moodlemobile.ProjectReportActivity;
import mrt.lk.moodlemobile.ProjectWorksActivity;
import mrt.lk.moodlemobile.R;
import mrt.lk.moodlemobile.data.GroupSubProjectItem;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.data.WorkCommentItem;
import mrt.lk.moodlemobile.data.WorkSeenItem;
import mrt.lk.moodlemobile.utils.DownloadFileFromUrl;

/**
 * Created by janithah on 9/24/2018.
 */

public class ProjectWorksAdapter extends ArrayAdapter {

    ArrayList<WorkCommentItem> works;
    Context context;
    boolean isProject;
    private final LayoutInflater mInflater;
    //public static final String PDF = "PDF";
    public static final String TEXT = "TEXT";
    String pid;


    public ProjectWorksAdapter( Context context,ArrayList<WorkCommentItem> works,String pid) {
        super(context, R.layout.layout_project_work,works);
        this.works = works;
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
        this.pid =pid;
    }

    public View getView(final int position, View view,ViewGroup viewGroup) {
        View v = view;
        TextView txt_time,txt_comment_by,txt_comment,txt_seen;
        LinearLayout layout_work;
        final Button btn_download;
        if (v == null) {
            v = mInflater.inflate(R.layout.layout_project_work, viewGroup, false);
            v.setTag(R.id.txt_time, v.findViewById(R.id.txt_time));
            v.setTag(R.id.txt_comment_by, v.findViewById(R.id.txt_comment_by));
            v.setTag(R.id.txt_comment, v.findViewById(R.id.txt_comment));
            v.setTag(R.id.txt_seen, v.findViewById(R.id.txt_seen));
            v.setTag(R.id.layout_work, v.findViewById(R.id.layout_work));
            v.setTag(R.id.btn_download, v.findViewById(R.id.btn_download));
        }
        txt_time = (TextView) v.getTag(R.id.txt_time);
        txt_comment_by = (TextView) v.getTag(R.id.txt_comment_by);
        txt_comment = (TextView) v.getTag(R.id.txt_comment);
        txt_seen = (TextView) v.getTag(R.id.txt_seen);
        layout_work = (LinearLayout) v.getTag(R.id.layout_work);
        btn_download = (Button) v.getTag(R.id.btn_download);

        WorkCommentItem item = works.get(position);
        ParticipantItem p = item.participant;
        ArrayList<WorkSeenItem> s = item.seen_list;
        txt_time.setText(item.time);
        txt_comment.setText(item.comment);
        txt_comment_by.setText(p.name);
        String seen ="";
        if(s.size() >0){
            seen = s.get(0).name;
            for(int i=1 ;i<s.size();i++){
                seen += ","+s.get(i).name;
            }
            txt_seen.setVisibility(View.VISIBLE);
            txt_seen.setText(seen);
        }
        else{
            txt_seen.setVisibility(View.GONE);
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layout_work.getLayoutParams();

        if(pid.equals(p.id)){
            layout_work.setBackgroundResource(R.color.app_background_shadow);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        else{
            layout_work.setBackgroundResource(R.color.white);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        layout_work.setLayoutParams(params);

        if(item.comment_type.equals(TEXT)){
            btn_download.setVisibility(View.GONE);
            txt_comment.setVisibility(View.VISIBLE);
        }
        else{
            btn_download.setVisibility(View.VISIBLE);
            txt_comment.setVisibility(View.GONE);
        }

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkCommentItem item = works.get(position);
                new DownloadFileFromUrl(ProjectWorksActivity.prgController,btn_download,context).execute(item.comment_location);
            }
        });


        return  v;
    }
}

