package mrt.lk.moodlemobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrt.lk.moodlemobile.CourseProjectsActivity;
import mrt.lk.moodlemobile.GroupDetailsActivity;
import mrt.lk.moodlemobile.R;
import mrt.lk.moodlemobile.StudentEvaluationResultsActivity;
import mrt.lk.moodlemobile.data.CourseGroupItem;
import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.data.ResObject;
import mrt.lk.moodlemobile.utils.Constants;
import mrt.lk.moodlemobile.utils.Utility;
import mrt.lk.moodlemobile.utils.WSCalls;

/**
 * Created by janithah on 9/22/2018.
 */

public class GroupStudentInfoAdapter extends ArrayAdapter {

    public ArrayList<ParticipantItem> items ;
    private final LayoutInflater mInflater;
    private final Context context;
    public static final String EVALUATION_GROUP = "EVALUATION_GROUP";
    public static final String GENERAL_GROUP = "GENERAL_GROUP";
    private final String grouptype;

    public GroupStudentInfoAdapter(Context context, ArrayList<ParticipantItem> items, String grouptype) {
        super(context, R.layout.layout_group_student_item,items);
        this.items = items;
        this.mInflater = LayoutInflater.from(context);
        this.context =context;
        this.grouptype = grouptype;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View v = view;
        TextView txt_student;
        ImageView img_view_student_results,img_remove_student;
        if (v == null) {
            v = mInflater.inflate(R.layout.layout_group_student_item, viewGroup, false);
            v.setTag(R.id.txt_student, v.findViewById(R.id.txt_student));
            v.setTag(R.id.img_view_student_results, v.findViewById(R.id.img_view_student_results));
            v.setTag(R.id.img_remove_student, v.findViewById(R.id.img_remove_student));
        }
        txt_student = (TextView) v.getTag(R.id.txt_student);
        img_view_student_results = (ImageView) v.getTag(R.id.img_view_student_results);
        img_remove_student = (ImageView) v.getTag(R.id.img_remove_student);
        if(LoggedUser.roleshortname.equals(LoggedUser.AS_TEACHER)) {
            img_remove_student.setVisibility(View.VISIBLE);
        }
        else{
            img_remove_student.setVisibility(View.INVISIBLE);
        }
        if(grouptype.equals(EVALUATION_GROUP)){
            img_remove_student.setVisibility(View.GONE);
            img_view_student_results.setVisibility(View.GONE);
        }

        img_view_student_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParticipantItem item = items.get(position);
                Intent i = new Intent(context, StudentEvaluationResultsActivity.class);
                i.putExtra("PARTICIPANT_ID",item.id);
                i.putExtra("PARTICIPANT_NAME",item.firstname);
                context.startActivity(i);

//                ParticipantItem item = items.get(position);
//                Intent i = new Intent(context, CourseProjectsActivity.class);
//                i.putExtra("SELECTED_GROUP_ID",item.group_id);
//                i.putExtra("SELECTED_GROUP_NAME",item.group_name);
//                context.startActivity(i);
            }
        });
        img_remove_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    ParticipantItem item = items.get(position);
                    new RemoveStudentFromGroup(position).execute(GroupDetailsActivity.SELECTED_GROUP_ID, item.id);


            }
        });
        ParticipantItem item = items.get(position);
        txt_student.setText(item.firstname);
        return  v;
    }

    private void populate_remove(String msg,int index){
        try {
            JSONObject jo = new JSONObject(msg);
            if(jo.getString("msg").equals("Success")){
                items.remove(index);
            }
            Utility.showMessage(jo.getString("msg"),context);
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    class RemoveStudentFromGroup extends AsyncTask<String,Void,ResObject>{
        int index;
        public RemoveStudentFromGroup(int index){
            this.index =index;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            GroupDetailsActivity.showPrg("Requesting...");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            GroupDetailsActivity.hidePrg();
            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
                populate_remove(response.msg,index);
            }
            else{
                Utility.showMessage(response.msg,context);
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            return new WSCalls(context).remove_student_from_group(params[0],params[1]);
        }
    }
}
